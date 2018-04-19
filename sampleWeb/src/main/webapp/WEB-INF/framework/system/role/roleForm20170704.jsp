<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/framework/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/framework/include/head.jsp"%>
<script type="text/javascript">
	jQuery(function($) {

	});
</script>
</head>
	<body class="no-skin">
		<div class="page-content" id="mainContent">
		<div class="widget-box transparent ui-sortable-handle" id="widget-box-search">
			<div class="widget-header">
				<h3 class="widget-title smaller lighter">&nbsp;角色${not empty userModel.id?'修改':'新增'}</h3>

				<div class="widget-toolbar no-border">
				</div>
			</div>
			<div class="widget-body">
				<div class="widget-main padding-6 no-padding-left no-padding-right">
					<div class="space-6"></div>
					<form:form id="inputForm" modelAttribute="roleModel" action="${path}/sys/role/save" method="post" class="form-horizontal" htmlEscape="false">
						<form:hidden path="id"/>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right">角色名称：</label>
							<div class="col-sm-4">
								<input id="oldName" name="oldName" type="hidden" value="${roleModel.name}">
								<form:input path="name" htmlEscape="false" maxlength="50" class="required" cssClass="form-control required" readonly="${not empty roleModel.id}"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right">角色标识（用于工作流）：</label>
							<div class="col-sm-4">
								<input id="oldEnName" name="oldEnName" type="hidden" value="${roleModel.enName}">
								<form:input path="enName" htmlEscape="false" maxlength="50" cssClass="form-control required" class="required" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right">角色类型：</label>
							<div class="col-sm-4">
								<form:select path="roleType" cssClass="form-control required" class="required">
									<form:options items="${fns:getDictList('role_type') }" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>
							<div class="col-sm-4">
								<label class="control-label"></label>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right">数据范围：</label>
							<div class="col-sm-4">
								<form:select path="dataScope" cssClass="form-control required" class="required">
									<form:options items="${fns:getDictList('data_scope') }" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>
							<div class="col-sm-4">
								<label class="control-label"></label>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right">是否系统角色：</label>
							<div class="col-sm-4">
								<form:select path="isSys" cssClass="form-control required" class="required">
									<form:options items="${fns:getDictList('yes_no') }" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>
							<div class="col-sm-4">
								<label class="control-label"></label>
							</div>
						</div>
						<div class="form-group">
								<label class="col-sm-4 control-label no-padding-right">所属权限：</label>
								<div class="col-sm-4">
									<input type="hidden" id="menuIds" name="menuIds">
									<div class="widget-box">
										<div class="widget-body">
											<div class="widget-main padding-8">
												<ul id="tree"></ul>
												<%-- <yqjr:tree url="${path}/sys/role/treeData" treeData="['roleId':'${not empty roleModel.id ? roleModel.id : ''}';'a':'b']" id="tree" folderSelect="false" multiSelect="true"></yqjr:tree> --%>
											</div>
										</div>
									</div>
								</div>
							</div>
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
			$("#name").focus();
			$("#inputForm").validate({
				errorElement: 'label',  //错误提示使用dom
				errorClass: 'help-inline', //错误提示使用的class
				focusInvalid: true,//focusInvalid：类型 Boolean，默认 true。提交表单后，未通过验证的表单（第一个或提交之前获得焦点的未通过验证的表单）会获得焦点。
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
					name: {remote: "角色名已存在"},
					enname: {remote: "英文名已存在"}
				},//自定义错误提示信息
				rules : {
					name: {remote: "${path}/sys/role/checkName?oldName="+$('#oldName').val() + encodeURIComponent("${role.name}")},
					enname: {remote: "${path}/sys/role/checkEnname?oldEnname="+$('#oldEnName').val() + encodeURIComponent("${role.enName}")}
				},//自定义必输项
				submitHandler: function (form) {//校验成功处理
					var items = $('#tree').tree('selectedItems');
					var menuIds = "";
					for(var i in items){
						if (items.hasOwnProperty(i)) {
							var item = items[i];
							menuIds += item.additionalParameters['id'] + "," + item.additionalParameters['pids'];
						}
					} 
					$("#menuIds").val(menuIds);
					form.submit();
				}
			});
			
			var param = {};
			param["roleId"] = "${not empty roleModel.id ? roleModel.id : ''}";
			$('#tree').frameworkTree({
				url : '${path}/sys/role/treeData',
				multiSelect : true,
				data : param
			});
			
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


