<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/framework/include/taglib.jsp"%>
<div id="navbar" class="navbar navbar-default          ace-save-state">
	<div class="navbar-container ace-save-state" id="navbar-container">
		<button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler" data-target="#sidebar">
			<span class="sr-only">Toggle sidebar</span>

			<span class="icon-bar"></span>

			<span class="icon-bar"></span>

			<span class="icon-bar"></span>
		</button>

		<div class="navbar-header pull-left">
			<a href="#" class="navbar-brand">
				  <img src="${path }/static/images/logo_yqjr.png">
				<small>
				</small>
			</a>
		</div>
		<nav role="navigation" class="navbar-menu pull-left collapse navbar-collapse">
			<ul class="nav navbar-nav">
			</ul>
		</nav>
		<div class="navbar-buttons navbar-header pull-right" role="navigation">
			<ul class="nav ace-nav">
				<li class="light-blue">
					<a data-toggle="dropdown" href="#" class="dropdown-toggle">
						<i class="ace-icon fa fa-user"></i>
						您好，${fns:getUser().userName}
						<i class="ace-icon fa fa-caret-down"></i>
					</a>
					<ul id="topUL" class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
						<li>
							<a href="#">
								<i class="ace-icon fa fa-clock-o"></i>
								${fns:getDate('yyyy-MM-dd')}
							</a>
						</li>
						<li class="divider"></li>
						<li>
							<a href="${path}/logout">
								<i class="ace-icon fa fa-power-off"></i>
								退出登录
							</a>
						</li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
</div>