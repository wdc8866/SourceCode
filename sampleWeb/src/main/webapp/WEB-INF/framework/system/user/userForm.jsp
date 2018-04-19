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
				<h3 class="widget-title smaller lighter">&nbsp;用户${not empty userModel.id?'修改':'新增'}</h3>
			</div>
			<yqjr:message content="${frameMessageContent}" type="${frameMessageType}"></yqjr:message>
			<div class="widget-body">
				<div class="widget-main padding-6 no-padding-left no-padding-right">
					<div class="space-6"></div>
					<form:form id="inputForm" modelAttribute="userModel" action="${path}/sys/user/save" method="post" class="form-horizontal" htmlEscape="false">
						<form:hidden path="id"/>
						<div class="row">
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">登录名：</label>
								<div class="col-sm-3">
									<form:hidden path="oldLoginName"/>
									<form:input path="loginName" htmlEscape="false" maxlength="50" cssClass="form-control required" readonly="${not empty userModel.id}"/>
								</div>
							</div>
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">姓名：</label>
								<div class="col-sm-3">
									<form:input path="userName" htmlEscape="false" maxlength="50" cssClass="form-control required"/>
								</div>
							</div>
						</div>
						<div class="space-4"></div>
						<div class="row">
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">密码：</label>
								<div class="col-sm-3">
									<form:password path="newPassword" maxlength="50" minlength="6" cssClass="${empty userModel.id?'required':''} form-control"/>
								</div>
							</div>
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">确认密码：</label>
								<div class="col-sm-3">
									<form:password path="confirmNewPassword" maxlength="50" minlength="6" equalTo="#newPassword" cssClass="${empty userModel.id?'required':''} form-control"/>
								</div>
							</div>
						</div>
						<div class="space-4"></div>
						<div class="row">
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">邮箱：</label>
								<div class="col-sm-3">
									<form:input path="email" htmlEscape="false" maxlength="100" cssClass="email form-control required"/>
								</div>
							</div>
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">电话：</label>
								<div class="col-sm-3">
									<form:input path="phone" htmlEscape="false" maxlength="12" cssClass="form-control"/>
								</div>
							</div>
						</div>
						<div class="space-4"></div>
						<div class="row">
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">是否允许登录：</label>
								<div class="col-sm-3">
									<form:select path="canLogin" cssClass="form-control required">
										<form:option value="">请选择...</form:option>
										<form:options items="${fns:getDictList('yes_no') }" itemLabel="label" itemValue="value" htmlEscape="false" cssClass="required"/>
									</form:select>
								</div>
							</div>
						</div>
						<div class="space-4"></div>
						<div class="row">
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">角色：</label>
								<div class="col-sm-6 ">
									<form:checkboxes path="roleIdList" items="${roleList}" itemLabel="name" itemValue="id" htmlEscape="false" cssClass="required"/>
								</div>
							</div>
						</div>
						<div class="space-4"></div>
						<div class="row">
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">备注：</label>
								<div class="col-sm-8">
									<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200"  class="form-control "/>
								</div>
							</div>
						</div>
						<%-- <div class="space-4"></div>
						<div class="row">
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">测试金额：</label>
								<div class="col-sm-8">
									<form:input path="userNo" htmlEscape="false" cssClass="form-control" />
								</div>
							</div>
						</div> --%>
						<div class="space-4"></div>
						<c:if test="${not empty userModel.id}">
							<div class="row">
								<label class="col-sm-2 control-label no-padding-right">创建时间：</label>
								<div class="col-sm-3">
									<label class="control-label no-padding-left">${userModel.createDate}</label>
								</div>
								<label class="col-sm-2 control-label no-padding-right">最后登陆：</label>
								<div class="col-sm-3">
									<label class="control-label no-padding-left">IP: ${userModel.lastLoginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：${userModel.lastLoginDate}</label>
								</div>
							</div>
							<div class="space-4"></div>
						</c:if>
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
			messages : {
				loginName: {remote: "用户登录名已存在"},
				confirmNewPassword: {equalTo: "输入与上面相同的密码"}
			},//自定义错误提示信息
			rules : {
				loginName: {remote: "${path}/sys/user/checkLoginName?oldLoginName=" + encodeURIComponent($("#oldLoginName").val())}
			},//自定义必输项
			submitHandler: function (form) {//校验成功处理
				form.submit();
			}
		});
		YQJR.AmountWords.show($("#userNo"));
		setHash();
	});
	</script>


