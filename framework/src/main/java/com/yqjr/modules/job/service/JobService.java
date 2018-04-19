package com.yqjr.modules.job.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yqjr.framework.base.BaseService;
import com.yqjr.modules.job.condition.JobCondition;
import com.yqjr.modules.job.dao.JobDao;
import com.yqjr.modules.job.entity.Job;
import com.yqjr.modules.job.model.JobModel;

@Service
public class JobService extends BaseService<Long, JobDao, Job, JobCondition, JobModel> {

	public List<JobModel> queryJobs() {
		return toModels(dao.queryJobs(), JobModel.class);
	}

	public void saveModel(JobModel model) {
		if(model.getId() == null){
			String frequency = model.getFrequency();
			String exectime = model.getExectime();//yyyy-MM-dd,hh:mm:ss
			String day = exectime.split(",")[0];
			String time = exectime.split(",")[1];
			String execWeeks = model.getExecWeeks();
			String execDays = model.getExecDays();
			String jobCron = "%s %s %s %s %s %s";
			//一次
			if("1".equals(frequency)){
				jobCron = String.format(jobCron, time.split(":")[2],time.split(":")[1],time.split(":")[0],day.split("-")[2],day.split("-")[1],"?");
			}
			//每天
			else if("2".equals(frequency)){
				jobCron = String.format(jobCron, time.split(":")[2],time.split(":")[1],time.split(":")[0],"*","*","?");
			}
			//每周
			else if("3".equals(frequency)){
				jobCron = String.format(jobCron, time.split(":")[2],time.split(":")[1],time.split(":")[0],"*","*",execWeeks);
			}
			//每月
			else if("4".equals(frequency)){
				jobCron = String.format(jobCron, time.split(":")[2],time.split(":")[1],time.split(":")[0],execDays,"*","?");
			}
			model.setJobCron(jobCron);
			super.save(model);
		}else{
			super.update(model);
		}
	}

	/**
	 * Description: 批量启动/停止任务 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年7月31日 上午9:37:06
	 *
	 * @param ids
	 * @param flag： start   stop
	 */
//	public void batchUpdate(String ids, Integer jobStatus) {
//		List<Job> list = new ArrayList<Job>();
//		for(String id : ids.split(",")){
//			Job job = dao.id(Long.parseLong(id));
//			job.setJobStatus(jobStatus);
//			list.add(job);
//		}
//		this.batchUpdate(list);
//	}
}