package com.yqjr.framework.component.boot;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import com.yqjr.framework.component.bean.ExtensionComponentLoader;
import com.yqjr.framework.component.context.SpringContext;
import com.yqjr.framework.component.dict.DictLoader;
import com.yqjr.framework.component.job.JobScheduler;
import com.yqjr.framework.component.log.InteractiveLogger;
import com.yqjr.framework.component.webservice.Publisher;
import com.yqjr.modules.job.model.JobModel;
import com.yqjr.modules.job.service.JobService;

@Component
@Lazy(false)
public class SystemBoot extends ApplicationObjectSupport implements InitializingBean {

	public void afterPropertiesSet() throws Exception {
		startup();
	}

	private void startup() throws Exception {

		System.out.println("系统服务开始启动... ...");

		// 初始化springContext
		SpringContext.getInstance().setApplicationContext(this.getApplicationContext());

		// 加载框架扩展组件
		ExtensionComponentLoader.getInstance().loadComponent();

		// 初始化数据字典
		System.out.println("数据字典初始化开始...");
		DictLoader.getInstance().init();
		System.out.println("数据字典初始化完毕");

		// webservice服务发布
		//Publisher.getInstance().publish();

		// 初始化系统定时任务
		System.out.println("系统定时任务开始启动...");
		JobService jobService = SpringContext.getInstance().getBeanWithName("jobService");
		List<JobModel> jobs = jobService.queryJobs();
		// 获取自动任务调度器
		JobScheduler jobScheduler = SpringContext.getInstance().getBeanWithName("jobScheduler");
		// 启动定时任务
		for (JobModel job : jobs) {
			jobScheduler.startJob(job);
		}
		System.out.println("系统定时任务启动完毕");
//
//		// 初始化批量日志组件
//		InteractiveLogger.getInteractiveLogger(InteractiveLogger.CLIENT_LOGGER).initInteractiveLogger();
//		InteractiveLogger.getInteractiveLogger(InteractiveLogger.SERVER_LOGGER).initInteractiveLogger();
//
//		// 加载应用系统自定义启动组件
//		IApplicationBoot applicationBoot = null;
//		try {
//			applicationBoot = SpringContext.getInstance().getBeanWithClass(IApplicationBoot.class);
//			System.out.println("找到应用自定义启动类配置信息," + applicationBoot.getClass().getName());
//		} catch (Exception e) {
//			System.out.println("未能找到应用自定义启动类配置");
//		}
//		if (applicationBoot != null) {
//			System.out.println("开始执行应用启动类...");
//			applicationBoot.startup();
//			System.out.println("应用启动类执行完毕");
//		}

		System.out.println("系统服务启动完毕!");
	}

}
