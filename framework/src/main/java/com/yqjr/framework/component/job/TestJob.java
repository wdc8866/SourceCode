package com.yqjr.framework.component.job;

import org.springframework.stereotype.Service;

import com.yqjr.framework.utils.DateUtils;

/**
 * ClassName: TestJob <br>
 * Description: 定时任务DEMO <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年7月31日 上午9:55:14 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
@Service
public class TestJob extends JobTask{

	@Override
	public void innerRun() throws Exception {
		
		System.out.println("正在执行一个定时任务："+DateUtils.getDateTime());
	}

}
