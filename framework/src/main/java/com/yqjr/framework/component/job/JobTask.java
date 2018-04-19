package com.yqjr.framework.component.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.Assert;

import com.yqjr.framework.component.log.Logger;
import com.yqjr.framework.datatype.Date;
import com.yqjr.modules.job.model.JobModel;
import com.yqjr.modules.job.service.JobService;

public abstract class JobTask implements Runnable {

	private Logger logger = Logger.getLogger();

	@Autowired
	private JobService jobService;

	/**
	 * 任务配置信息
	 */
	private JobModel job;

	/**
	 * 任务触发器
	 */
	private CronTrigger trigger;

	/**
	 * 时间计算器(使用spring原生类)
	 */
	private CronSequenceGenerator cronSequenceGenerator;

	public JobModel getJobConfig() {
		return job;
	}

	public void setJobConfig(JobModel job) {
		this.job = job;
		// 初始化计算器
		Assert.hasText(job.getJobCron(), "jobConfig非法,cron表达式不能为空");
		cronSequenceGenerator = new CronSequenceGenerator(job.getJobCron());
	}

	public CronTrigger getTrigger() {
		return trigger;
	}

	public void setTrigger(CronTrigger trigger) {
		this.trigger = trigger;
	}

	/**
	 * Description: 获取任务信息 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月7日 下午4:05:04
	 *
	 * @return
	 */
	public String getJobInfo() {
		return job.toString();
	}

	/**
	 * 任务执行
	 */
	public void run() {

		// 更改任务状态,清空上次执行结果,更改执行状态
		try {
			job.setJobExecCost(-1l);
			job.setJobExecResult(null);
			job.setJobExecStatus(0);
			jobService.update(job);
		} catch (Exception e) {
			logger.error("更改任务状态异常", e);
			return;
		}

		// 记录任务开始时间
		long startTimestamp = System.currentTimeMillis();
		// 执行任务业务逻辑代码
		try {
			logger.info("任务开始执行,任务信息:" + job);
			innerRun();
			logger.info("任务执行成功");
			job.setJobExecStatus(1);
			job.setJobExecResult("执行成功");
		} catch (Exception e) {
			logger.error("任务执行失败", e);
			job.setJobExecStatus(2);
			job.setJobExecResult(e.getMessage());
		} finally {
			// 更改任务执行状态
			long endTimestamp = System.currentTimeMillis();
			job.setJobExecCost(endTimestamp - startTimestamp);
			job.setJobNextExectime(new Date(getNextExectime().getTime()).toString());
			try {
				jobService.update(job);
			} catch (Exception e) {
				logger.error(String.format("更改任务状态异常,任务执行状态:%s,结果信息:%s,任务耗时:%d", job.getJobExecStatus(),
						job.getJobExecResult(), job.getJobExecCost()), e);
			}
		}

	}

	/**
	 * Description: 获取任务下次执行时间 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月8日 上午8:43:03
	 *
	 * @return
	 */
	public java.util.Date getNextExectime() {
		java.util.Date curdate = new java.util.Date();
		return cronSequenceGenerator.next(curdate);
	}

	/**
	 * Description: 业务逻辑方法 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月8日 上午10:02:40
	 *
	 * @throws Exception
	 */
	public abstract void innerRun() throws Exception;

}
