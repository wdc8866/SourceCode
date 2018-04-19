<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/framework/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="表格ID"%>
<%@ attribute name="url" type="java.lang.String" required="true" description="数据地址"%>
<%@ attribute name="treeData" type="java.lang.String" required="true" description="自定义参数"%>
<%@ attribute name="multiSelect" type="java.lang.Boolean" required="true" description="是否支持多选"%>
<%@ attribute name="folderSelect" type="java.lang.Boolean" required="true" description="是否可选择根节点"%>
<script type="text/javascript">
	jQuery(function($) {
		var data = {};
		data = "${treeData}";
		$('#tree').frameworkTree({
			url : '${url}',
			multiSelect : ${multiSelect},
			folderSelect : ${folderSelect},
			data : data
		});
	});
</script>
<ul id="${id}"></ul>
