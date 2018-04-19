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
				<h3 class="widget-title smaller lighter">&nbsp;数据字典列表列表</h3>
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
					<form:form id="searchForm" modelAttribute="dictCondition" method="post" class="form-horizontal">
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
				<!-- <button type="button" class="btn btn-white btn-sm btn-primary" onclick="insert('normal');">添加同级菜单</button> -->
				<button type="button" class="btn btn-white btn-sm btn-primary" onclick="insert('next');">添加下级数据字典</button>
				<button type="button" class="btn btn-white btn-sm btn-primary" onclick="update();">数据字典修改</button>
				<button type="button" class="btn btn-white btn-sm btn-primary" onclick="batchDelete();">数据字典删除</button>
			</div>
			<div class="row">
				<div class="col-sm-3">
					<ul id="tree"></ul>
					<%-- <yqjr:tree url="${path}/sys/menu/treeData" treeData="{'roleId':'${not empty roleModel.id ? roleModel.id : ''}'}" id="tree" folderSelect="true" multiSelect="false"></yqjr:tree> --%>
				</div>
				<div class="col-sm-9">
					<yqjr:table id="dictTable" colModel="[{label:'id',name:'id',index:'id',width:200, align:'left',hidden:true},
													{label:'数据字典标题',name:'label',sortable:false, width:200, align:'left'},
													{label:'数据字典值',name:'value',sortable:false, width:100, align:'center'},
													{label:'数据字典类型',name:'type',sortable:false, width:100, align:'center'},
													{label:'数据字典排序',name:'sort',index:'sort',width:200, align:'left'},
													{label:'状态',name:'status',sortable:true,width:150,align:'center',formatter: 'select',editoptions: {value: '${fns:getDictListForTable('data_status')}'}},
													{label:'创建时间',name:'createDate',sortable:false, width:200, align:'center'}]"
					    url="${path }/sys/dict/listData" shrinkToFit="true"/>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	jQuery(function($) {
		var param = {};
		param["roleId"] = "${not empty roleModel.id ? roleModel.id : ''}";
		$('#tree').frameworkTree({
			url : '${path}/sys/dict/treeData',
			multiSelect : false,
			data : param,
			folderSelect : true
		});
		$('#tree').on('selected.fu.tree', function(e,data) {
			$("#selectedId").val(data.selected[0].additionalParameters.id);
			$("#selectedParentId").val(data.selected[0].additionalParameters.pid);
			$("#parentId").val(data.selected[0].additionalParameters.id);
			searchData('dictTable');
		}).on('deselected.fu.tree', function(e,data) {
			$("#parentId").val("");
			$("#dictTable").clearGridData();
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
			commonForward('${path}/sys/dict/form?parent.id='+parentId);
		}else{
			alert("请选择左侧上级数据字典！");
		}
	}
	function update(){
		 var ids = $("#dictTable").jqGrid("getGridParam", "selarrrow");// 获得选中行数据
		 if (ids.length == 0) {
             alert("请选择需要操作的数据！");
             return false;
         }else if(ids.length > 1){
        	 alert("请选择一条记录！");
         }else if(ids.length == 1){
        	 var rowdata = $("#dictTable").jqGrid("getRowData", ids[0]);
        	 console.log(rowdata);
        	 commonForward('${path}/sys/dict/form?id='+rowdata.id);
         }
		 return false;
	}
	function batchDelete(){
		 var ids = $("#dictTable").jqGrid("getGridParam", "selarrrow");// 获得选中行数据
		 if (ids.length == 0) {
             alert("请选择需要操作的数据！");
             return false;
         }else{
        	 if(confirm("确定进行批量删除？")){
	        	 commonForward('${path}/sys/dict/delete?ids='+ids.toString());
        	 }
         }
	}
</script>
</body>