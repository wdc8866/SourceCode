<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/framework/include/taglib.jsp"%>
<%@ attribute name="content" type="java.lang.String" required="true" description="消息内容"%>
<%@ attribute name="type" type="java.lang.String" description="消息类型：info、success、warning、error"%>
<c:if test="${not empty content}">
	<script type="text/javascript">
		var content = "";
		content = '${content}';
		if(content != ""){
			$.gritter.add({
				title: "操作提示",
				text: "${content}",
				class_name: 'gritter-${type} gritter-center',
				time: 1000
			});
		}
	</script>
</c:if>