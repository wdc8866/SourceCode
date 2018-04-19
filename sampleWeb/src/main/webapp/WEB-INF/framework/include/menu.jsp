<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="main-container ace-save-state" id="main-container">
	<script type="text/javascript">
		try {
			ace.settings.loadState('main-container')
		} catch (e) {
		}
	</script>
	<div id="sidebar" class="sidebar responsive ace-save-state">
		<script type="text/javascript">
			try {
				ace.settings.loadState('sidebar')
			} catch (e) {
			}
		</script>
		<div class="sidebar-shortcuts" id="sidebar-shortcuts">
			<div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
				<!-- <button class="btn btn-warning" onclick="showMenuById('menuTree')">
					<i class="ace-icon fa fa-bars"></i> 所有菜单
				</button> -->
				<button class="btn btn-info">
					<i class="ace-icon fa fa-cog"></i> 预留功能
				</button>
				<button class="btn btn-success">
					<i class="ace-icon fa fa-star"></i> 预留功能
				</button>
				<!-- <div class="nav-search" id="nav-search">
					<span class="input-icon">
						<input type="text" placeholder="快捷交易码" class="nav-search-input" id="tradeCode"  name="tradeCode" autocomplete="off" />
						<i class="ace-icon fa fa-search nav-search-icon"></i>
					</span>
				</div> -->
			</div>
			<div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
				<span class="btn btn-info"></span> <span class="btn btn-success"></span>
			</div>
		</div>
		<ul class="nav nav-list" id="framework_menu_tree">
			
		</ul>
		<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
			<i id="sidebar-toggle-icon"
				class="ace-icon fa fa-angle-double-left ace-save-state"
				data-icon1="ace-icon fa fa-angle-double-left"
				data-icon2="ace-icon fa fa-angle-double-right"></i>
		</div>
		<script type="text/javascript">
			jQuery(function($) {
				$("#framework_menu_tree").frameworkMenu({url : "${path}/sys/menu/menuData"});
				$(".nav-list a").click(function() {
					var url = $(this).attr("href");
					var menuName = $(this).attr("menu-name");
					var isMini =  $(this).attr("isMini");
					var menuId = $(this).attr("menuId");
					//异步加载
					if (url != '#') {
						//iframe
						//window.open(url, "mainFrame");
						//var $overlay = $('<div class="ajax-loading-overlay"><i class="ajax-loading-icon fa fa-spin fa-spinner fa-2x orange"></i></div>');
						if(url.indexOf("?") >= 0 ){
							url = url + "&portalTime=" + new Date().getTime();
						}else{
							url = url + "?portalTime=" + new Date().getTime();
						}
						$("#loading").show();
						$("#mainFrame").attr("src","${path}"+url);
						$("#helpButton").attr("menuId",menuId);
						//$("#mainFrame").hide();
						//配置显示迷你菜单时，自动切换
						if(isMini == "1"){
							if(!$("#sidebar").hasClass("menu-min")){
								$("#sidebar-toggle-icon").click();
							}
						}else{
							if($("#sidebar").hasClass("menu-min")){
								$("#sidebar-toggle-icon").click();
							}
						}
						
						//设置面包屑
						$("#breadCrumb").html("");
						$(this).parents().each(function(obj) {
							if ($(this).get(0).tagName == "LI") {
								$("#breadCrumb").prepend("<li>"+ $(this).children().attr("menu-name")+"</li>");
							}
						});
						$("#breadCrumb").prepend("<li><i class=\"ace-icon fa fa-home home-icon\"></i> <a href=\"${path}\">首页</a></li>");
						var $overlay = $('<div class="ajax-loading-overlay"><i class="ajax-loading-icon fa fa-spin fa-spinner fa-2x orange"></i></div>');
						//清楚menuTree选中样式
						$(".nav-list li").each(function(index) {
							if ($(this).hasClass("open")) {
								$(this).removeClass("open");
							}
							if ($(this).hasClass("active")) {
								$(this).removeClass("active");
							}
						});
						//收起所有打开的ul下的li标签
						$(".nav-list ul").each(function(index) {
							$(this).css("display", "none");
						});
						//选中的li添加样式
						var $li = $(this).parent();
						$li.addClass("active");
						$(this).parents().each(function(obj) {
							if ($(this).eq(0).tagName = "UL") {
								//打开当前ul下的li标签
								$(this).css("display","block");
							}
							if ($(this).eq(0).tagName == "LI") {
								$(this).addClass("active").addClass("open");
							}
						});
						return false;
					};
				});

			});
		</script>
	</div>