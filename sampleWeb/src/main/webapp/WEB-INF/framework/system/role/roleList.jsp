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
	function update(){
		 var ids = $("#roleTable").jqGrid("getGridParam", "selarrrow");// 获得选中行数据
		 if (ids.length == 0) {
             alert("请选择需要操作的数据！");
             return false;
         }else if(ids.length > 1){
        	 alert("请选择一条记录！");
         }else if(ids.length == 1){
        	 var rowdata = $("#roleTable").jqGrid("getRowData", ids[0]);
        	 commonForward('${path}/sys/role/form?id='+rowdata.id);
         }
	}
	function batchDelete(){
		 var ids = $("#roleTable").jqGrid("getGridParam", "selarrrow");// 获得选中行数据
		 if (ids.length == 0) {
             alert("请选择需要操作的数据！");
             return false;
         }else{
        	 if(confirm("确定进行批量删除？")){
	        	 commonForward('${path}/sys/role/delete?ids='+ids.toString());
        	 }
         }
	}
</script>
</head>
<body class="no-skin">
	<div class="page-content" id="mainContent">
		<div class="widget-box transparent ui-sortable-handle collapsed"
			id="widget-box-search">
			<div class="widget-header">
				<h3 class="widget-title smaller lighter">&nbsp;角色列表</h3>
				<div class="widget-toolbar no-border">
					<a href="#" data-action="collapse"> <i class="ace-icon fa fa-chevron-down">
						<span style="font-family: '微软雅黑'; font-size: 14px;">&nbsp;打开</span></i>
					</a>
				</div>
			</div>
			<yqjr:message content="${frameMessageContent}" type="${frameMessageType}"></yqjr:message>
			<div class="widget-body">
				<div class="widget-main padding-6 no-padding-left no-padding-right">
					<div class="space-6"></div>
					<form:form id="searchForm" modelAttribute="roleCondition" method="post" class="form-horizontal">
						<div class="row">
							<div class="col-sm-4">
								<label class="col-sm-4 control-label no-padding-right" for="name"> 角色名 ：</label>
								<div class="col-sm-8">
									<form:input path="name" htmlEscape="false" maxlength="50" cssClass="form-control input-sm" />
								</div>
							</div>
							<div class="col-sm-4">
								<label class="col-sm-4 control-label no-padding-right" for="enName"> 角色标识 ：</label>
								<div class="col-sm-8">
									<form:input path="enName" htmlEscape="false" maxlength="50" cssClass="form-control input-sm" />
								</div>
							</div>
							<div class="col-sm-4">
								<div class="dataTables_paginate paging_simple_numbers">
									<ul class="pagination">
										<li>
											<button id="searchBtn" onclick="searchData('roleTable')" type="button" class="btn btn-primary">
												<span class="ace-icon fa fa-search icon-on-right bigger-110"></span>
												查询
											</button>
										</li>
										<li>
											<button id="resetBtn" type="reset" class="btn btn-primary">
												<span class="ace-icon fa fa-refresh icon-on-right bigger-110"></span>
												重 置
											</button>
										</li>
									</ul>
								</div>
							</div>
						</div>
						<div class="hr hr-dotted"></div>
					</form:form>
				</div>
			</div>
			<div class="btn-group">
				<button type="button" class="btn btn-white btn-sm btn-primary" onclick="commonForward('${path}/sys/role/form')">角色新增</button>
				<button type="button" class="btn btn-white btn-sm btn-primary" onclick="update();">角色修改</button>
				<button type="button" class="btn btn-white btn-sm btn-primary" onclick="batchDelete();">角色删除</button>
			</div>
			<yqjr:table id="roleTable" colModel="[{label:'id',name:'id',index:'id',width:200, align:'left',hidden:true},
													{label:'角色名称',name:'name',index:'name',width:200, align:'left'},
													{label:'角色标识(用于工作流)',name:'enName',index:'enName',width:200, align:'left'},
													{label:'角色类型',name:'roleType',sortable:false, width:200, align:'center',formatter: 'select',editoptions: {value: '${fns:getDictListForTable('role_type')}'}},
													{label:'数据范围',name:'dataScope',sortable:false, width:200, align:'center',formatter: 'select',editoptions: {value: '${fns:getDictListForTable('data_scope')}'}},
													{label:'状态',name:'deleteStatus',sortable:true,width:150,align:'center',formatter: 'select',editoptions: {value: '${fns:getDictListForTable('data_status')}'}},
													{label:'创建时间',name:'createDate',sortable:false, width:200, align:'center'}]"
					    url="${path }/sys/role/listData" shrinkToFit="true"/>
		</div>
	</div>
</body>