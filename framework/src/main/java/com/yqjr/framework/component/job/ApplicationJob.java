/**
 * 
 */
package com.yqjr.framework.component.job;

import org.springframework.stereotype.Service;

import com.yqjr.framework.component.job.JobTask;

/**
 * ClassName: ApplicationJob <br>
 * Description: TODO <br>
 * Create By: admin <br>
 * Create Date: 2017年5月16日 下午4:47:26 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@Service
public class ApplicationJob extends JobTask {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yqjr.framework.component.job.JobTask#innerRun()
	 */
	@Override
	public void innerRun() throws Exception {
		System.out.println(123);
//		Thread.sleep(5000l);
//		throw new RuntimeException("妹妹的嘛");
	}

}
