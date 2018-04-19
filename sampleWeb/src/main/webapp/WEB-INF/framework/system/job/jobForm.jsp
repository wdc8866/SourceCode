<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/framework/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/framework/include/head.jsp"%>
</head>
	<body class="no-skin">
		<div class="page-content" id="mainContent">
		<div class="widget-box transparent ui-sortable-handle" id="widget-box-search">
			<div class="widget-header">
				<h3 class="widget-title smaller lighter">&nbsp;定时任务${not empty userModel.id?'修改':'新增'}</h3>
			</div>
			<yqjr:message content="${frameMessageContent}" type="${frameMessageType}"></yqjr:message>
			<div class="widget-body">
				<div class="widget-main padding-6 no-padding-left no-padding-right">
					<div class="space-6"></div>
					<form:form id="inputForm" modelAttribute="jobModel" action="${path}/sys/job/save" method="post" class="form-horizontal" htmlEscape="false">
						<form:hidden path="id"/>
						<div class="row">
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">任务名称：</label>
								<div class="col-sm-3">
									<form:input path="jobName" htmlEscape="false" maxlength="50" cssClass="form-control required" readonly="${not empty jobModel.id}"/>
								</div>
							</div>
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">任务状态：</label>
								<div class="col-sm-3">
									<form:select path="jobStatus" cssClass="form-control required">
										<form:option value="">请选择...</form:option>
										<form:options items="${fns:getDictList('job_status') }" itemLabel="label" itemValue="value" htmlEscape="false" cssClass="required"/>
									</form:select>
								</div>
							</div>
						</div>
						<div class="space-4"></div>
						<div class="row">
							<%-- <div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">执行规则：</label>
								<div class="col-sm-3">
									<form:select path="jobCron" cssClass="form-control required">
										<form:option value="">请选择...</form:option>
										<form:options items="${fns:getDictList('job_crontab') }" itemLabel="label" itemValue="value" htmlEscape="false" cssClass="required"/>
									</form:select>
									<form:input path="jobCron" htmlEscape="false" cssClass="form-control required"/>
								</div>
							</div> --%>
							<div class="form-group-2">
								<div class="form-group-2">
									<label class="col-sm-2 control-label no-padding-right">执行类：</label>
									<div class="col-sm-3">
										<form:input path="jobClass" htmlEscape="false" cssClass="form-control required"/>
									</div>
								</div>
							</div>
						</div>
						<div class="space-4"></div>
						<%-- <div class="row">
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">任务执行状态：</label>
								<div class="col-sm-3">
									<form:input path="jobExecStatus" htmlEscape="false" maxlength="100" cssClass="email form-control required"/>
								</div>
							</div>
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">任务执行结果：</label>
								<div class="col-sm-3">
									<form:input path="jobExecResult" htmlEscape="false" maxlength="12" cssClass="form-control"/>
								</div>
							</div>
						</div> --%>
						<div class="space-4"></div>
						<div class="row">
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">任务执行节点：</label>
								<div class="col-sm-3">
									<form:input path="jobExecNode" htmlEscape="false" cssClass="form-control required"/>
								</div>
							</div>
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">任务执行周期：</label>
								<div class="col-sm-3">
									<form:select path="frequency" cssClass="form-control required">
										<form:option value="">请选择...</form:option>
										<%-- <form:option value="">一次</form:option>
										<form:option value="">每天</form:option>
										<form:option value="">每周</form:option>
										<form:option value="">每月</form:option> --%>
										<form:options items="${fns:getDictList('job_frequency') }" itemLabel="label" itemValue="value" htmlEscape="false" cssClass="required"/>
									</form:select>
								</div>
							</div>
						</div>
						<div class="space-4"></div>
						<div class="row">
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">开始日期：</label>
								<div class="col-sm-3">
									<form:input path="exectime" htmlEscape="false" rows="3" maxlength="200"  class="form-control date-picker required" data-date-format="yyyy-mm-dd" readonly="true"/>
								</div>
							</div>
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">开始时间：</label>
								<div class="col-sm-3">
									<form:input path="exectime" htmlEscape="false" rows="3" maxlength="200"  class="form-control time-picker required" readonly="true"/>
								</div>
							</div>
						</div>
						<div class="space-4"></div>
						<div class="row" id="execWeeks_row">
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">执行星期：</label>
								<div class="col-sm-8">
									<form:radiobuttons path="execWeeks" items="${fns:getDictList('job_execWeeks') }" itemLabel="label" itemValue="value" htmlEscape="false" cssClass="required"/>
								</div>
							</div>
						</div>
						<div class="space-4"></div>
						<div class="row" id="execDays_row">
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">执行日期：</label>
								<div class="col-sm-8">
									<form:checkboxes path="execDays" items="${fns:getDictList('job_execDays') }" itemLabel="label" itemValue="value" htmlEscape="false" cssClass="required"/>
								</div>
							</div>
						</div>
						<div class="space-4"></div>
						<div class="row">
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">备注：</label>
								<div class="col-sm-8">
									<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200"  class="form-control "/>.
								</div>
							</div>
						</div>
						<div class="space-4"></div>
						<div class="clearfix form-actions">
							<div class="col-md-offset-4 col-md-8">
								<button id="btnSubmit" class="btn btn-info" type="button" onclick="javascript:$('#inputForm').submit()">
									<i class="ace-icon fa fa-check bigger-110"></i>
									保存
								</button>
									&nbsp; &nbsp; &nbsp;
								<button class="btn" type="button" onclick="history.go(-1)">
									<i class="ace-icon fa fa-undo bigger-110"></i>
									返回
								</button>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
		</div>	
	</body>
	<script type="text/javascript">
	jQuery(function($) {
		$("#inputForm").frameworkValid({
			submitHandler: function (form) {//校验成功处理
				form.submit();
			}
		});
		$('.date-picker').datepicker({
			autoclose: true,
			todayHighlight: true
		})
		.next().on(ace.click_event, function(){
			$(this).prev().focus();
		});
		
		$('.time-picker').timepicker({
			minuteStep: 1,
			showSeconds: true,
			showMeridian: false,
			disableFocus: true
		}).on('focus', function() {
			$(this).timepicker('showWidget');
		}).next().on(ace.click_event, function(){
			$(this).prev().focus();
		});
		$("#execWeeks_row").hide();
		$("#execDays_row").hide();
		$("#frequency").change(function(){
			var value = $(this).val();
			//一次
			if(value == "1"){
				$("#execWeeks_row").hide();
				$("#execDays_row").hide();
			}
			//每天
			else if(value == "2"){
				$("#execWeeks_row").hide();
				$("#execDays_row").hide();
			}
			//每周
			else if(value == "3"){
				$("#execWeeks_row").show();
				$("#execDays_row").hide();
			}
			//每月
			else if(value == "4"){
				$("#execDays_row").show();
				$("#execWeeks_row").hide();
			}
		});
		setHash();
	});
	</script>


