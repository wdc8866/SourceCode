/**
 * 
 */
package com.yqjr.framework.component.watcher;

import org.springframework.util.Assert;

import com.yqjr.framework.component.log.Logger;

/**
 * ClassName: Watcher <br>
 * Description: 监听者线程 <br>
 * Create By: admin <br>
 * Create Date: 2017年7月5日 上午10:28:18 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public abstract class Watcher extends Thread {

	protected static long DEFAULT_DELAY = 60000;

	protected long delay = DEFAULT_DELAY;

	private boolean running = false;

	private static Logger logger = Logger.getLogger();

	private String watcherName;

	public Watcher(String watcherName, long delay) {
		Assert.hasText(watcherName, "watcher name must be point out");
		Assert.isTrue(delay > 1000, "watcher delay grant than 1000ms");
		this.watcherName = watcherName;
		this.delay = delay;
	}

	public Watcher(String watcherName) {
		this(watcherName, DEFAULT_DELAY);
	}

	@Override
	public synchronized void start() {
		running = true;
		super.start();
		logger.info(watcherName + "已启动");
	}

	public synchronized void shutdown() {
		running = false;
		logger.info(watcherName + "已停止");
	}

	/**
	 * Description: 检查<br>
	 * Create By: admin <br>
	 * Create Date: 2017年7月5日 上午10:46:27
	 */
	protected abstract void doCheck();

	/**
	 * Description: 变化时处理 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年7月5日 上午11:16:21
	 */
	protected abstract void doOnChange();

	@Override
	public void run() {
		while (running) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
			}
			doCheck();
		}
	}

}
