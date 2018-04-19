package com.yqjr.framework.component.job;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.yqjr.framework.component.cache.AbstractCache;
import com.yqjr.framework.component.cache.ICache;
import com.yqjr.framework.component.config.Configuration;
import com.yqjr.framework.component.context.SpringContext;
import com.yqjr.framework.component.log.Logger;
import com.yqjr.framework.datatype.BizzException;
import com.yqjr.framework.datatype.Date;
import com.yqjr.modules.job.model.JobModel;
import com.yqjr.modules.job.service.JobService;

@Service
public class JobScheduler {

	private Logger logger = Logger.getLogger();

	private static final String CACHE_NAME = "framework.job.cache";

	@Autowired
	private JobService jobService;

	/**
	 * 任务调度器
	 */
	private TaskScheduler taskScheduler;

	/**
	 * 任务登记薄
	 */
	private Map<Long, ScheduledFuture<?>> taskRegister = new HashMap<Long, ScheduledFuture<?>>();

	/**
	 * 当前执行节点IP地址
	 */
	private static String LOCAL_ADDR = null;

	static {
		try {
			LOCAL_ADDR = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 任务对象缓存
	 */
	private ICache jobCache = AbstractCache.getCache();

	public JobScheduler() {
		ScheduledExecutorService jobExecutor = Executors
				.newScheduledThreadPool(Configuration.getConfig().getIntValue("framework.job.thread"));
		taskScheduler = new ConcurrentTaskScheduler(jobExecutor);
	}

	/**
	 * Description: 初始化任务 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月7日 上午9:50:21
	 *
	 * @param jobConfig
	 * @throws Exception
	 */
	public void initJob(JobModel job) throws Exception {
		// 参数检查
		Assert.notNull(job);
		Assert.hasText(job.getJobClass(), "业务逻辑类为空");

		// 检查缓存中的任务实例
		Long jobId = job.getId();
		if (jobCache.get(CACHE_NAME, jobId.toString()) == null) {
			synchronized (jobCache) {
				if (jobCache.get(CACHE_NAME, jobId.toString()) == null) {
					logger.info("jobCache中不存在job实例,初始化开始." + job);
					// 如果不存在,则进行初始化
					Object taskInstance = SpringContext.getInstance().getBeanWithClassName(job.getJobClass());
					// 判断是否为jobTask实例
					if (!(taskInstance instanceof JobTask)) {
						throw new BizzException(String.format("%s不是JobTask的实例", job.getJobClass()));
					}
					JobTask jobTask = (JobTask) taskInstance;
					// jobTask设置参数
					jobTask.setJobConfig(job);
					jobTask.setTrigger(new CronTrigger(job.getJobCron()));
					// 将jobTask添加至缓存
					jobCache.put(CACHE_NAME, jobId.toString(), jobTask);
					logger.info("job实例初始化完毕");
				}
			}
		} else {
			JobTask jobTask = jobCache.get(CACHE_NAME, jobId.toString());
			logger.info("jobCache中存在," + jobTask.getJobConfig());
		}
	}

	/**
	 * Description: 启动任务 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月7日 上午9:53:51
	 *
	 * @param jobId
	 *            任务Id
	 */
	public synchronized void startJob(long jobId) {
		JobTask jobTask = jobCache.get(CACHE_NAME, String.valueOf(jobId));
		// 判断job是否初始化
		Assert.notNull(jobTask, "任务尚未初始化,请在调用startJob前调用initJob方法完成任务初始化");
		// 判断job是否正在执行
		Assert.isNull(taskRegister.get(jobId), "任务正在运行");
		// 判断当前任务执行位置
		if (!LOCAL_ADDR.equals(jobTask.getJobConfig().getJobExecNode())) {
			logger.info("任务执行节点与当前节点不符,忽略任务启动请求");
			return;
		}
		// 任务启动
		logger.info(String.format("任务启动开始,任务ID:%d", jobId));

		// 更新任务状态
		JobModel job = jobTask.getJobConfig();
		job.setJobStatus(1);
		job.setJobNextExectime(new Date(jobTask.getNextExectime().getTime()).toString());
		jobService.update(job);

		taskRegister.put(jobId, taskScheduler.schedule(jobTask, jobTask.getTrigger()));

		logger.info("任务启动完毕,任务信息:" + job);
	}

	/**
	 * Description: job初始化+启动 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月8日 上午10:35:11
	 *
	 * @param jobConfig
	 * @throws Exception
	 */
	public void startJob(JobModel job) throws Exception {
		initJob(job);
		startJob(job.getId());
	}

	/**
	 * Description: 停止任务 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月7日 上午9:54:05
	 *
	 * @param jobId
	 *            任务Id
	 */
	public synchronized void stopJob(long jobId, boolean mayInterrupt) {
		ScheduledFuture<?> future = taskRegister.get(jobId);
		// 判断job是否正在执行
		Assert.notNull(future, "任务已停止");

		// 任务停止
		logger.info(String.format("开始停止任务,任务ID:%d", jobId));
		// 如果退出成功需要删除登记表中数据
		if (future.cancel(mayInterrupt)) {
			// 更新任务为已停止状态
			JobTask jobTask = jobCache.get(CACHE_NAME, String.valueOf(jobId));
			JobModel job = jobTask.getJobConfig();
			job.setJobStatus(2);
			job.setJobNextExectime(null);
			jobService.update(job);
			// 删除登记表
			taskRegister.remove(jobId);
			logger.info("任务停止完毕");
		}
		// 否则抛出异常
		else {
			throw new BizzException("任务停止失败");
		}
	}
}
