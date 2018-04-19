/**
 * 
 */
package com.yqjr.framework.component.watcher;

import java.io.File;

import org.springframework.util.Assert;

import com.yqjr.framework.utils.FileUtils;

/**
 * ClassName: FileWatcher <br>
 * Description: 文件监听者 <br>
 * Create By: admin <br>
 * Create Date: 2017年7月5日 上午10:47:14 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public abstract class FileWatcher extends Watcher {

	private String fileName;

	protected long lastModified = 0L;

	/**
	 * @return the lastModified
	 */
	public long getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified
	 *            the lastModified to set
	 */
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @param watcherName
	 * @param delay
	 */
	public FileWatcher(String watcherName, String fileName, long delay) {
		super(watcherName, delay);
		Assert.hasText(fileName, "file name can't be empty");
		this.fileName = fileName;
	}

	/**
	 * @param watcherName
	 */
	public FileWatcher(String watcherName, String fileName) {
		this(watcherName, fileName, DEFAULT_DELAY);
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		Assert.hasText(fileName, "file name can't be empty");
		this.fileName = fileName;
	}

	@Override
	public void doCheck() {
		String filePath = FileUtils.getFilePath(fileName);
		File file = new File(filePath);
		Assert.isTrue(file.exists(), String.format("配置文件%s不存在,文件路径%s", fileName, filePath));
		try {
			if (file.lastModified() > this.lastModified) {
				doOnChange();
				// 更新时间戳
				this.lastModified = file.lastModified();
			}
		} finally {
			file = null;
		}
	}

}
