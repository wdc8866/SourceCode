﻿<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/framework/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>一汽金融开发平台</title>
<style type="text/css">
html, body {
	position: relative;
	width: 100%;
	height: 100%;
	margin: 0;
	padding: 0
}

ul li {
	list-style: none;
}

.content {
	width: 1180px;
	margin: 0 auto;
	height: 800px;
}

.title {
	float: left;
	position: relative;
	margin-top: 200px;
	margin-left: 114px;
	width: 402px;
	height: 191px;
	z-index: 2;
	background: url(${path}/static/images/login/title.png) no-repeat;
}

.loginbox {
	float: right;
	margin-right: 60px;
	position: relative;
	top: 40px;
	width: 568px;
	height: 543px;
	z-index: 3;
	background: url(${path}/static/images/login/loginbox.png) no-repeat;
}

.footer {
	position: relative;
	font-family: 微软雅黑;
	font-size: 12px;
	color: #fff;
	text-align: center;
	width: 100%;
	top: 200px;
	clear: both;
}

.input {
	width: 348px;
	margin: 0 auto;
	margin-top: 262px;
}

ul input {
	border: 0 none;
	background: none;
	height: 40px;
	width: 250px;
	line-height: 40px;
	padding-left: 40px;
	font-family: 微软雅黑;
	color: #2f2f2f;
	outline: none;
}

ul li {
	margin-top: 26px;
	position: relative;
	font-family: 微软雅黑;
	font-size: 14px;
	color: #fff;
	width: 314px;
}

ul li img {
	border: none;
}

.li-text {
	margin-left: 4px;
	line-height: 24px;
	margin-top: 10px;
}

.li-text a {
	font-family: 微软雅黑;
	font-size: 14px;
	color: #efb028;
}

.check {
	width: 130px;
}

.t3 {
	margin-top: 3px;
}

.t20 {
	margin-top: 40px;
}

.ut {
	margin-top: -2px;
}

.vImgBox {
	height: 38px;
	width: 100px;
	background-color: #fff;
	position: absolute;
	right: 1px;
	top: 5px;
}

::-webkit-input-placeholder { /* WebKit browsers */
	color: #868686;
}

:-moz-placeholder { /* Mozilla Firefox 4 to 18 */
	color: #868686;
}

::-moz-placeholder { /* Mozilla Firefox 19+ */
	color: #868686;
}

:-ms-input-placeholder { /* Internet Explorer 10+ */
	color: #868686;
}
</style>
<script type="text/javascript" src="${path}/static/js/jquery.js"></script>
<script type="text/javascript" src="${path}/static/js/messenger.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#username").focus();
	$("#username").bind('keypress',function(event){
		if(event.keyCode == "13"){
				$("#password").focus();
		}	
	});
	$("#password").bind('keypress',function(event){
		if(event.keyCode == "13"){
			$("#loginForm").submit();
		}	
	});
	
	var messenger = new Messenger('childPage', 'IframeHandle');
	messenger.addTarget(window.top, 'parent');
	//messenger.targets['parent'].send(document.body.scrollHeight);
	messenger.targets['parent'].send("logout");
	
});
function doLogin(){
	$("#loginForm").submit();
}
</script>
</head>
<body style="background: url(${path}/static/images/login/login.jpg) center center no-repeat;">
	<div class="content">
		<div class="title"></div>
		<form:form id="loginForm" class="form-signin" action="${path}/login" method="post">
			<div class="loginbox">
				<ul class="input">
					<li><input type="text" placeholder="请输入用户名" id="username"
						name="username"></li>
					<li><input type="password" id="password" name="password"
						placeholder="请输入用户密码"></li>
					<li class="t20"><a href="#" onclick="doLogin();"><img
							src="${path}/static/images/login/login_btn.png" width="317" height="40"></a></li>
					<c:if test="${message != '' }">
						<li class="li-text">${message }</li>
					</c:if>
					<%-- <li class="li-text"><form:errors path="*" id="msg" cssStyle="" element="div" /></li> --%>
				</ul>
			</div>
		</form:form>
		<div class="footer">版权所有©一汽资本控股有限公司</div>
	</div>
</body>
</html>