/**
 * 
 */
package com.yqjr.framework.component.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.util.Assert;

import com.yqjr.framework.base.BaseModel;
import com.yqjr.framework.component.context.SpringContext;
import com.yqjr.framework.datatype.BizzException;

/**
 * ClassName: InteractiveLogger <br>
 * Description: 交互式日志记录组件 <br>
 * Create By: admin <br>
 * Create Date: 2017年6月7日 上午8:09:53 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public abstract class InteractiveLogger {

	protected boolean isInit = false;
	private static int logThread = 5;
	private static long logThreadInterval = 2000l;

	public static final String CLIENT_LOGGER = "client";
	public static final String SERVER_LOGGER = "server";
	protected static final String SAVE_MESSAGE = "framework.interactivelog.savemessage";
	

	private static final String CATEGORY = InteractiveLogger.class.getName();
	private static Logger logger = Logger.getLogger();

	protected Queue<BaseModel<String, ?>> bufferQueue = new ConcurrentLinkedQueue<BaseModel<String, ?>>();

	public static InteractiveLogger getInteractiveLogger(String type) {
		Assert.hasText(type);
		// 获取客户端日志组件
		if (CLIENT_LOGGER.equalsIgnoreCase(type)) {
			return SpringContext.getInstance().getBeanWithName("clientInteractiveLogger");
		}
		// 获取服务端日志组件
		else if (SERVER_LOGGER.equalsIgnoreCase(type)) {
			return SpringContext.getInstance().getBeanWithName("serverInteractiveLogger");
		}
		// 其他情况抛出异常
		else {
			throw new BizzException("暂不支持的类型");
		}
	}

	/**
	 * Description: 直接向数据库中登记日志,在频发操作中不建议使用此种方式登记日志 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月7日 上午10:13:42
	 *
	 * @param logEntity
	 */
	public abstract void saveLog(BaseModel<String, ?> logModel);

	/**
	 * Description: 更改交易状态 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月15日 下午3:52:31
	 *
	 * @param logEntity
	 */
	public abstract void updateLog(BaseModel<String, ?> logModel);

	/**
	 * Description: 异步记录日志方式,先登记至缓冲队列,随后写入数据库 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月7日 上午10:14:13
	 *
	 * @param logEntity
	 */
	public abstract void performanceSaveLog(BaseModel<String, ?> logModel);

	/**
	 * Description: 日志批量处理 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月15日 下午4:47:05
	 *
	 * @param logs
	 */
	protected abstract void batchProcess(List<BaseModel<String, ?>> logs);

	/**
	 * Description: 批量日志组件初始化 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月7日 下午2:01:38
	 */
	public void initInteractiveLogger() {
		if (!isInit) {
			System.out.println(this.getClass() + "日志任务启动");
			ScheduledExecutorService executor = Executors.newScheduledThreadPool(logThread);
			executor.scheduleAtFixedRate(new Runnable() {

				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					// 从缓冲队列中获取待处理任务
					Object[] objects = bufferQueue.toArray();
					bufferQueue.removeAll(Arrays.asList(objects));
					// 如果队列中没有消息则线程退出
					if (objects.length == 0) {
						return;
					}
					// 调用日志登记方法
					else {
						List<BaseModel<String, ?>> list = new ArrayList<BaseModel<String, ?>>();
						for (Object obj : objects) {
							list.add((BaseModel<String, ?>) obj);
						}
						try {
							batchProcess(list);
						} catch (Exception e) {
							logger.error(CATEGORY, "日志登记失败", e);
						}
					}
				}
			}, 0l, logThreadInterval, TimeUnit.MILLISECONDS);
			isInit = true;
		}
	}

}
