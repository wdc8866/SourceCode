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
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right">登录名：</label>
							<div class="col-sm-4">
								<form:hidden path="oldLoginName"/>
								<form:input path="loginName" htmlEscape="false" maxlength="50" cssClass="form-control required" readonly="${not empty userModel.id}"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right">姓名：</label>
							<div class="col-sm-4">
								<form:input path="userName" htmlEscape="false" maxlength="50" cssClass="form-control required"/>
							</div>
						</div>
						<c:if test="${empty userModel.id}">
							<div class="form-group">
								<label class="col-sm-4 control-label no-padding-right">密码：</label>
								<div class="col-sm-4">
									<form:password path="newPassword" maxlength="50" minlength="6" cssClass="${empty userModel.id?'required':''} form-control"/>
								</div>
								<div class="col-sm-4">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 control-label no-padding-right">确认密码：</label>
								<div class="col-sm-4">
									<form:password path="confirmNewPassword" maxlength="50" minlength="6" equalTo="#newPassword" cssClass="form-control"/>
								</div>
								<div class="col-sm-4">
								</div>
							</div>
						</c:if>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right">邮箱：</label>
							<div class="col-sm-4">
								<form:input path="email" htmlEscape="false" maxlength="100" cssClass="email form-control required"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right">电话：</label>
							<div class="col-sm-4">
								<form:input path="phone" htmlEscape="false" maxlength="12" cssClass="form-control"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right">是否允许登录：</label>
							<div class="col-sm-4">
								<form:select path="canLogin" cssClass="form-control required">
									<form:options items="${fns:getDictList('yes_no') }" itemLabel="label" itemValue="value" htmlEscape="false" cssClass="required"/>
								</form:select>
							</div>
							<div class="col-sm-4">
								<label class="control-label">是-代表允许登录，否-则表示不允许登录</label>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right">角色：</label>
							<div class="col-sm-4 ">
								<form:checkboxes path="roleIdList" items="${roleList}" itemLabel="name" itemValue="id" htmlEscape="false" cssClass="required"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right">备注：</label>
							<div class="col-sm-4">
								<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200"  class="form-control "/>
							</div>
						</div>
						<c:if test="${not empty user.id}">
							<div class="form-group">
								<label class="col-sm-4 control-label no-padding-right">创建时间：</label>
								<div class="col-sm-4">
									<label class="control-label no-padding-left"><fmt:formatDate value="${userModel.createDate}" type="both" dateStyle="full"/></label>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 control-label no-padding-right">最后登陆：</label>
								<div class="col-sm-4">
									<label class="control-label no-padding-left">IP: ${userModel.lastLoginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${userModel.lastLoginDate}" type="both" dateStyle="full"/></label>
								</div>
							</div>
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
			$("input[type='checkbox']").each(function(){
				$(this).addClass("ace").addClass("ace-checkbox-2").next().replaceWith("<span class='lbl'>&nbsp;"+$(this).next().html()+"</span>");
				$(this).parent().replaceWith("<label class='control-label'>"+$(this).parent().html()+"</label>");
			});
			
			var validator = $("#inputForm").validate({
				errorElement: 'label',  //错误提示使用dom
				errorClass: 'help-inline', //错误提示使用的class
				focusInvalid: true,//focusInvalid：类型 Boolean，默认 true。提交表单后，未通过验证的表单（第一个或提交之前获得焦点的未通过验证的表单）会获得焦点。
				onfocusout: function(element){
				   $(element).valid();
				},
				onclick: function(element){
				   $(element).valid();
				},
				onkeyup: function(element){
					if(event.keyCode == "13"){
						$("#inputForm").submit();
    				}
				},
				highlight: function (e) {//校验失败class处理
					$(e).closest('.form-group').removeClass('has-info').addClass('has-error');
				},
				success: function (e) {//校验成功class处理
					$(e).closest('.form-group').removeClass('has-error');//.addClass('has-info');
					$(e).remove();
				},
				errorPlacement: function (error, element) {//错误提示显示位置处理
					//error.insertAfter(element.parent());//input下方
					error.appendTo(element.parent());//input右侧
					if (element.is(":checkbox")||element.is(":radio")){
						error.appendTo(element.parent().parent());
					}
				},
				messages : {
					loginName: {remote: "用户登录名已存在"},
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				},//自定义错误提示信息
				rules : {
					loginName: {remote: "${path}/sys/user/checkLoginName?oldLoginName=" + encodeURIComponent($("#oldLoginName").val())}
				},//自定义必输项
				submitHandler: function (form) {//校验成功处理
					//yqjrLoading();
					//form.submit();
					yqjrSubmit(form);
				}
					
			});
			
			/* if($("#id").val()!=''){
				$("#loginName").rules("remove");
			} */
			
			setHash();
		});
		
		function yqjrSubmit(form,message){
			if (message == undefined || message == ""){
				message = "正在提交，请稍等...";
			}
			$.gritter.add({
				title: '操作提示',
				sticky : true,
				text: message,
				class_name: 'gritter-info gritter-center',
				time: 3000000
			}); 
			$("button").attr("disabled",true);
			form.submit();
		}
	</script>


