package com.yqjr.modules.job.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yqjr.framework.base.BaseController;
import com.yqjr.framework.component.job.JobScheduler;
import com.yqjr.framework.datatype.BizzException;
import com.yqjr.framework.datatype.Page;
import com.yqjr.framework.utils.StringUtils;
import com.yqjr.modules.job.condition.JobCondition;
import com.yqjr.modules.job.model.JobModel;
import com.yqjr.modules.job.service.JobService;

/**
 * 
 * ClassName: JobController <br>
 * Description: 定时任务控制器 <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年7月26日 下午1:45:26 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
@Controller
@RequestMapping(value = "/sys/job")
public class JobController extends BaseController {

	@Autowired
	private JobService jobService;
	
	@Autowired
	private JobScheduler jobScheduler;

	@RequestMapping(value = { "list" })
	public String list(JobCondition jobCondition, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return "framework/system/job/jobList";
	}

	@ResponseBody
	@RequestMapping(value = { "listData" })
	public Page<JobModel> listData(JobCondition jobCondition, HttpServletRequest request,HttpServletResponse response) {
		Page<JobModel> page = new Page<JobModel>(JobModel.class, request, response);
		jobService.findPage(page, jobCondition);
		return page;
	}

	@RequestMapping(value = "form")
	public String form(JobModel jobModel, Model model) {
		if(jobModel.getId() != null){
			model.addAttribute("jobModel", jobService.get(jobModel));
		}
		return "framework/system/job/jobForm";
	}

	@RequestMapping(value = "save")
	public String save(JobModel jobModel, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) throws Exception {
		// 参数校验
		if (!beanValidator(model, jobModel)) {
			return form(jobModel, model);
		}
		// 执行用户保存
		try {
			jobService.saveModel(jobModel);
		} catch (BizzException e) {
			addMessage(model,super.MESSAGE_TYPE_ERROR, e.getErrorMessage());
			return form(jobModel, model);
		}
		addMessage(redirectAttributes,  super.MESSAGE_TYPE_SUCCESS,"保存定时任务'" + jobModel.getJobName() + "'成功");
		return "redirect:/sys/job/list";
	}

	@RequestMapping(value = "delete")
	public String delete(String ids, RedirectAttributes redirectAttributes) throws Exception {
		if (StringUtils.isNoneBlank(ids)) {
			jobService.batchDelete(ids);
			addMessage(redirectAttributes, super.MESSAGE_TYPE_SUCCESS, "删除定时任务成功");
		}
		return "redirect:/sys/job/list";
	}
	
	@RequestMapping(value = "updateJobStatus")
	public String start(Long id,Integer jobStatus, RedirectAttributes redirectAttributes) throws Exception {
		if (id != null) {
			JobModel jobModel = jobService.id(id);
			jobScheduler.initJob(jobModel);
			if(jobStatus == 1){
				jobScheduler.startJob(id);
				addMessage(redirectAttributes, super.MESSAGE_TYPE_SUCCESS, "启动定时任务成功");
			}else if (jobStatus == 0 ){
				jobScheduler.stopJob(id,true);
				addMessage(redirectAttributes, super.MESSAGE_TYPE_SUCCESS, "停用定时任务成功");

			}
		}
		return "redirect:/sys/job/list";
	}
}
