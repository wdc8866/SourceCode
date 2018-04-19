package com.yqjr.modules.job.dao;

import java.util.List;

import com.yqjr.framework.annotation.FrameworkDao;
import com.yqjr.framework.base.BaseDao;
import com.yqjr.modules.job.condition.JobCondition;
import com.yqjr.modules.job.entity.Job;

@FrameworkDao
public interface JobDao extends BaseDao<Long, Job, JobCondition> {

	public List<Job> queryJobs();

}