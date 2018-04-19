<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/framework/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
	<%@include file="/WEB-INF/framework/include/head.jsp" %>
	<script type="text/javascript">
		jQuery(function($) {
			$("#loading").hide();
			
			var messenger = new Messenger('parent', 'IframeHandle');
			var childPage = document.getElementById('childPage');
			var iframe = document.getElementById("mainFrame");
			messenger.listen(function (msg) {
				//msg非高度数据，为cas标志，做登出动作
				if("logout" == msg){
					window.location = '${path}/logout';
				}else{
			        var height = parseInt(msg);
			       iframe.height = Math.max(height,600); 
				}
		    });
		 });
	</script>
	</head>
	<body class="no-skin">
		<%@include file="/WEB-INF/framework/include/top.jsp" %>
		<%@include file="/WEB-INF/framework/include/menu.jsp" %>
		<div class="main-content">
			<div class="main-content-inner">
				<div class="breadcrumbs ace-save-state" id="breadcrumbs">
					<ul class="breadcrumb" id="breadCrumb">
						<li>
							<i class="ace-icon fa fa-home home-icon"></i>
							<a href="${path}">首页</a>
						</li>
					</ul>
					<div class="nav-search pull-right" id="nav-search">
						<form class="form-search" id="searchTradeForm">
							<span class="input-icon">
								<input type="text" placeholder="快捷交易码" class="nav-search-input" id="tradeCode"  name="tradeCode" autocomplete="off"/>
								<input type="hidden" name="tradeCode1">
								<input type="hidden" name="tradeCode2">
								<i class="ace-icon fa fa-keyboard-o nav-search-icon"></i>
							</span>
						</form>
					</div>
				</div>
			   <div id="loading">
			   		<div class="ajax-loading-overlay">
						<div style="margin-top: 15%;" class="center"><i class="ace-icon fa fa-spinner fa-spin orange" style="font-size: 575% "></i></div>
					</div>
				</div>
		        <iframe id="mainFrame" name="mainFrame" onreadystatechange='stateChangeIE(this)'  onload='stateChangeFirefox(this)' scrolling="no" frameborder="no" width="100%" height="1000" ></iframe>
			</div>
		</div>
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>
	</body>
</html>
