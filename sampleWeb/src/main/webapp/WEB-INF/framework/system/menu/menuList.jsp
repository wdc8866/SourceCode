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
		<div class="widget-box transparent ui-sortable-handle collapsed"
			id="widget-box-search">
			<div class="widget-header">
				<h3 class="widget-title smaller lighter">&nbsp;菜单列表</h3>
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
					<form:form id="searchForm" modelAttribute="menuCondition" method="post" class="form-horizontal">
						<div class="row">
							<input type="hidden" id="selectedId" name="selectedId">
							<input type="hidden" id="selectedParentId" name="selectedParentId">
							<form:hidden path="parentId"/>
						</div>
						<div class="hr hr-dotted"></div>
					</form:form>
				</div>
			</div>
			
			<div class="btn-group">
				<button type="button" class="btn btn-white btn-sm btn-primary" onclick="insert('normal');">添加同级菜单</button>
				<button type="button" class="btn btn-white btn-sm btn-primary" onclick="insert('next');">添加下级菜单</button>
				<button type="button" class="btn btn-white btn-sm btn-primary" onclick="update();">菜单修改</button>
				<button type="button" class="btn btn-white btn-sm btn-primary" onclick="batchDelete();">菜单删除</button>
			</div>
			<div class="row">
				<div class="col-sm-2">
					<ul id="tree"></ul>
					<%-- <yqjr:tree url="${path}/sys/menu/treeData" treeData="{'roleId':'${not empty roleModel.id ? roleModel.id : ''}'}" id="tree" folderSelect="true" multiSelect="false"></yqjr:tree> --%>
				</div>
				<div class="col-sm-10">
					<yqjr:table id="menuTable" colModel="[{label:'id',name:'id',index:'id',width:200, align:'left',hidden:true},
													{label:'菜单名称',name:'name',index:'name',width:200, align:'left'},
													{label:'菜单排序',name:'sort',index:'sort',width:200, align:'left'},
													{label:'菜单链接',name:'url',sortable:false, width:200, align:'left'},
													{label:'菜单图标',name:'icon',sortable:false, width:100, align:'center'},
													{label:'权限标识',name:'permission',sortable:false, width:100, align:'center'},
													{label:'是否可见',name:'isShow',sortable:true,width:150,align:'center',formatter: 'select',editoptions: {value: '${fns:getDictListForTable('yes_no')}'}},
													{label:'状态',name:'deleteStatus',sortable:true,width:150,align:'center',formatter: 'select',editoptions: {value: '${fns:getDictListForTable('data_status')}'}},
													{label:'创建时间',name:'createDate',sortable:false, width:200, align:'center'}]"
					    url="${path }/sys/menu/listData" shrinkToFit="true"/>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	jQuery(function($) {
		var param = {};
		param["roleId"] = "${not empty roleModel.id ? roleModel.id : ''}";
		$('#tree').frameworkTree({
			url : '${path}/sys/menu/treeData',
			multiSelect : false,
			data : param,
			folderSelect : true
		});
		$('#tree').on('selected.fu.tree', function(e,data) {
			$("#selectedId").val(data.selected[0].additionalParameters.id);
			$("#selectedParentId").val(data.selected[0].additionalParameters.pid);
			$("#parentId").val(data.selected[0].additionalParameters.id);
			searchData('menuTable');
		}).on('deselected.fu.tree', function(e,data) {
			$("#parentId").val("");
			$("#menuTable").clearGridData();
		});
	});
	function insert(type){
		if($("#parentId").val() != ''){
			var parentId;
			if(type == "next"){
				parentId = $("#selectedId").val();
			}else if(type = "noraml"){
				if($("#parentId").val() == "1"){
					alert("不能添加《功能菜单》的同级菜单！");
					return false;
				}
				parentId = $("#selectedParentId").val();
			}
			commonForward('${path}/sys/menu/form?parentId='+parentId);
		}else{
			alert("请选择左侧上级菜单！");
		}
	}
	function update(){
		 var ids = $("#menuTable").jqGrid("getGridParam", "selarrrow");// 获得选中行数据
		 if (ids.length == 0) {
             alert("请选择需要操作的数据！");
             return false;
         }else if(ids.length > 1){
        	 alert("请选择一条记录！");
         }else if(ids.length == 1){
        	 var rowdata = $("#menuTable").jqGrid("getRowData", ids[0]);
        	 commonForward('${path}/sys/menu/form?id='+rowdata.id);
         }
	}
	function batchDelete(){
		 var ids = $("#menuTable").jqGrid("getGridParam", "selarrrow");// 获得选中行数据
		 if (ids.length == 0) {
             alert("请选择需要操作的数据！");
             return false;
         }else{
        	 if(confirm("确定进行批量删除？")){
	        	 commonForward('${path}/sys/menu/delete?ids='+ids.toString());
        	 }
         }
	}
</script>
</body>