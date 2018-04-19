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
		<div class="error-container">
			<div class="well">
				<h1 class="grey lighter smaller">
					<span class="blue bigger-125">
						<i class="ace-icon fa fa-sitemap"></i>
						403
					</span>
					您的权限不足以访问该页面.......
				</h1>
				<hr />
				<h3 class="lighter smaller">请通过正规途径访问系统！</h3>
				<div>
					<div class="space"></div>
					<h4 class="smaller">同时, 尝试以下解决办法：</h4>
					<ul class="list-unstyled spaced inline bigger-110 margin-15">
						<li>
							<i class="ace-icon fa fa-hand-o-right blue"></i>
							阅读FAQ
						</li>

						<li>
							<i class="ace-icon fa fa-hand-o-right blue"></i>
							告诉我们
						</li>
					</ul>
				</div>
				<hr />
				<div class="space"></div>
				<div class="center">
					<a href="javascript:history.back()" class="btn btn-grey">
						<i class="ace-icon fa fa-arrow-left"></i>
						返回
					</a>
				</div>
			</div>
		</div>
	</div>
</body>