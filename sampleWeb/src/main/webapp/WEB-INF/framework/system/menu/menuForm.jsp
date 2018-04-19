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
				<h3 class="widget-title smaller lighter">&nbsp;菜单${not empty userModel.id?'修改':'新增'}</h3>

				<div class="widget-toolbar no-border">
				</div>
			</div>
			<div class="widget-body">
				<yqjr:message content="${frameMessageContent}" type="${frameMessageType}"></yqjr:message>
				<div class="widget-main padding-6 no-padding-left no-padding-right">
					<div class="space-6"></div>
					<form:form id="inputForm" modelAttribute="menu" action="${path}/sys/menu/save" method="post" class="form-horizontal" htmlEscape="false">
						<form:hidden path="id"/>
						<div class="row">
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">上级菜单:</label>
								<div class="col-sm-3">
									<form:hidden path="parent.id"/>
									<form:input path="parent.name" htmlEscape="false" maxlength="50" cssClass="form-control" readonly="true"/>
								</div>
							</div>
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">菜单名称:</label>
								<div class="col-sm-3">
									<form:input path="name" htmlEscape="false" maxlength="50" cssClass="form-control required"/>
								</div>
							</div>
						</div>
						<div class="space-4"></div>
						<div class="row">
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">链接(父菜单请填#):</label>
								<div class="col-sm-3">
									<form:input path="url" htmlEscape="false" maxlength="200" cssClass="form-control required"/>
								</div>
							</div>
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">排序:</label>
								<div class="col-sm-3">
									<form:input path="sort" htmlEscape="false" maxlength="50" cssClass="form-control required"/>
								</div>
							</div>
						</div>
						<div class="space-4"></div>
						<div class="row">
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">是否可见:</label>
								<div class="col-sm-3">
									<form:select path="isShow" cssClass="form-control required">
										<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" />
									</form:select>
								</div>
							</div>
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">菜单图标:</label>
								<div class="col-sm-3">
									<form:input path="icon" htmlEscape="false" maxlength="50" cssClass="form-control"/>
								</div>
							</div>
						</div>
						<div class="space-4"></div>
						<div class="row">
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">权限标识:</label>
								<div class="col-sm-8">
									<form:textarea path="permission" htmlEscape="false" maxlength="200" cssClass="form-control"/>
								</div>
							</div>
						</div>
						<div class="space-4"></div>
						<div class="row">
							<div class="form-group-2">
								<label class="col-sm-2 control-label no-padding-right">备注:</label>
								<div class="col-sm-8">
									<form:textarea path="remarks" htmlEscape="false" maxlength="200" cssClass="form-control"/>
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
		});
	</script>


