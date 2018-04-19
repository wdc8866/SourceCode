/**
 * 菜单组件
 */
jQuery.fn.frameworkMenu = function(ops) {
	$this = $(this);
	// 校验
	if (!ops.url) {
		alert("url不能为空");
		return;
	}
	jQuery.ajax({
		async : false,
		type : 'GET',
		contentType : 'application/json',
		url : ops.url,
		dataType : 'json',
		success : function(obj) {
			// 判断异常
			if(obj.exceptionCode){
				alert("主界面初始化失败,"+obj.exceptionMessage);
				return;
			}
			// 菜单渲染	
			var constant = new Constant();
			var treeHtml = "";
			$(obj).each(function(index) {
				var data = this;
				if(!data.icon){
					data.icon = "fa-caret-right";
				}
				var tempHead = constant.header.format(data.url,data.name,data.icon, data.name);
				var tempBody = constant.body.format(data.url,data.name,data.url, data.icon, data.name);
				var tempFoot = "</ul></li>";
				// 当前节点菜单等级
				var parentIds = data.parentIds;
				var level = parentIds.split(",").length-1;
				if (index < $(obj).size() - 1) {
					var pLevel = obj[index + 1].parentIds.split(",").length-1;
					var pUrl = obj[index + 1].url;
				}
				// url为"#",有下级菜单，拼出菜单头部
				if (data.url == '#' && level < pLevel) {
					treeHtml += tempHead;
				}
				// url不为"#",并且当前节点与下级节点同级，说明下一节点有同级菜单，拼菜单体
				else if (data.url != '#' && level == pLevel) {
					treeHtml += tempBody;
				}
				// url不为"#",并且当前节点大于下级节点,下级节点不为"#"，有level-pLevel-1级子菜单
				else if (pUrl != '#' && level > pLevel
						&& data.url != '#') {
					var tempNewFoot = tempFoot;
					// alert(obj[index].name+"
					// 等级差："+(level-pLevel-1));
					for (var i = 0; i < (level - pLevel - 1); i++) {
						tempNewFoot = tempNewFoot + tempFoot;
					}
					treeHtml = treeHtml + tempBody + tempNewFoot;
				}
				// url不为"#"，并且当前节点与下级节点不同级，说明下一节点无同级菜单，拼接菜单体、菜单尾*等级
				else if (data.url != '#' && level != pLevel) {
					for (var i = 1; i < (level - pLevel); i++) {
						tempFoot = tempFoot + tempFoot;
					}
					treeHtml = treeHtml + tempBody + tempFoot;
				}
				// url不为"#",并且当前节点与下级节点同级，说明下一节点有同级菜单，拼菜单体
				else if (data.url != '#' && level == pLevel
						&& pUrl == '#') {
					treeHtml += tempBody;
				}
			});
			$this.append(treeHtml);
		},
		error : function(e) {
			alert("主界面初始化失败:" + e);
		}
	});
};
//通知顶层页面高度
function setHash(){
	//alert("document.body.scrollHeight"+document.body.scrollHeight);
	//alert("document.body.clientHeight"+document.body.clientHeight);
	//alert("document.documentElement.scrollHeight"+document.documentElement.scrollHeight);
	//alert("document.documentElement.clientHeight"+document.documentElement.clientHeight);
	var messenger = new Messenger('childPage', 'IframeHandle');
	messenger.addTarget(window.top, 'parent');
	//messenger.targets['parent'].send(document.body.scrollHeight);
	messenger.targets['parent'].send(document.body.clientHeight);
}
/**
 * 常量
 */
function Constant() {
	//菜单头
	this.header = "<li><a href=\"{0}\" menu-name= \"{1}\" class=\"dropdown-toggle\"><i class=\"menu-icon fa {2}\"></i><span class=\"menu-text\">{3}</span><b class=\"arrow fa fa-angle-down\"></b></a><b class=\"arrow\"></b><ul class=\"submenu\">";
	//菜单体
	this.body = "<li><a data-url=\"{0}\" menu-name= \"{1}\" href=\"{2}\"><i class=\"menu-icon fa {3}\"></i>&nbsp;{4}</a><b class=\"arrow\"></b></li>";
}
/**
 * 字符串格式化
 * 
 * @returns {String}
 */
String.prototype.format = String.prototype.f = function() {

	var s = this, i = arguments.length;

	while (i--) {
		s = s.replace(new RegExp('\\{' + i + '\\}', 'm'), arguments[i]);
	}
	return s;
};

/**
 * 树组件
 */
jQuery.fn.frameworkTree = function(ops) {
	$this = $(this);
	var param = {};
	var folderSelect = false;
	var multiSelect = false;
	// 校验
	if (!ops.url) {
		alert("url不能为空");
		return;
	}
	if(ops.data){
		param = ops.data;
	}
	if(ops.folderSelect){
		folderSelect = ops.folderSelect;
	}
	if(ops.multiSelect){
		multiSelect = ops.multiSelect;
	}
	//带回调函数数据源
	var remoteDateSource = function(options, callback) {
		//初始化parent_id
		var parent_id = null
		if( !('text' in options || 'type' in options) ){
			parent_id = 0;
		}
		else if('type' in options && options['type'] == 'folder') {
			if('additionalParameters' in options && 'children' in options.additionalParameters){
				parent_id = options.additionalParameters['id'];
			}
		}
		param["parent_id"] = parent_id;
		if(parent_id !== null) {
			$.ajax({
				url: ops.url,
				data : param,
				type: 'GET',
				dataType: 'json',
				success : function(response) {
					//console.log(response.data);
					//异步获取数据,绘制子节点
					callback({ data: response });
					setHash();
				},
				error: function(response) {
					console.log(response);
				}
			})
		}
	};
	$this.ace_tree(
	{
		dataSource : remoteDateSource,
		//'multiSelect' : multiSelect,
		multiSelect : multiSelect,
		cacheItems : true,
		'open-icon' : 'ace-icon fa fa-folder-open',
		'close-icon' : 'ace-icon fa fa-folder',
		'itemSelect' : true,
		'folderSelect' : folderSelect,
		'selected-icon' : 'ace-icon fa fa-check',
		'unselected-icon' : 'ace-icon fa fa-times',
		loadingHTML : '<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>',
		'folder-open-icon' : 'ace-icon tree-plus',
		'folder-close-icon' : 'ace-icon tree-minus'
	});
	return $this;
};
/**
 * 表单校验组件
 */
jQuery.fn.frameworkValid = function(ops) {
	$this = $(this);
	$("input[type='checkbox']").each(function(){
		$(this).addClass("ace").addClass("ace-checkbox-2").next().replaceWith("<span class='lbl'>&nbsp;"+$(this).next().html()+"</span>");
		$(this).parent().replaceWith("<label class='control-label'>"+$(this).parent().html()+"</label>");
	});
	var $required_obj = $('.required');
	$required_obj.each(function(index){
		$(this).attr("required_index",index);
		if($(this).is("select")){
			$(this).on("change",function(){
				var required_index = parseInt($(this).attr("required_index"));
				$required_obj.each(function(index){
    				if(parseInt($(this).attr("required_index")) == (parseInt(required_index)+1)){
    					$(this).focus();
    				}
    			});	
			});
			$(this).on("keydown",function(){
				var required_index = parseInt($(this).attr("required_index"));
				if(event.keyCode == "13"){
    				$required_obj.each(function(index){
        				if(parseInt($(this).attr("required_index")) == (parseInt(required_index)+1)){
        					$(this).focus();
        				}
        			});	
				}
			}); 
		}
	}).eq(0).focus();
	$this.validate({
		errorElement: 'label',  //错误提示使用dom
		errorClass: 'help-inline', //错误提示使用的class
		focusInvalid: true,//focusInvalid：类型 Boolean，默认 true。提交表单后，未通过验证的表单（第一个或提交之前获得焦点的未通过验证的表单）会获得焦点。
		onfocusout: function(element){
		   $(element).valid();
		},
		onclick: function(element){
		   $(element).valid();
		},
		onkeyup: function(element){
			if(event.keyCode == "13"){
				if($(element).valid()){
					var required_index = parseInt($(element).attr("required_index"));
					$required_obj.each(function(index){
        				if(parseInt($(this).attr("required_index")) == (parseInt(required_index)+1)){
        					$(this).focus();
        				}else if((parseInt(required_index)+1) >= $required_obj.size()){
        					$("#inputForm").submit();
        				}
        			});
				}
				//console.log(element);
				//$("#inputForm").submit();
			}
		},
		highlight: function (e) {//校验失败class处理
			$(e).closest('.form-group-2').removeClass('has-info').addClass('has-error');
		},
		success: function (e) {//校验成功class处理
			$(e).closest('.form-group-2').removeClass('has-error').addClass('has-info');
			$(e).remove();
		},
		errorPlacement: function (error, element) {//错误提示显示位置处理
			//error.insertAfter(element.parent());//input下方
			error.appendTo(element.parent());//input右侧
			if (element.is(":checkbox")||element.is(":radio")){
				error.appendTo(element.parent().parent());
			}
		},
		messages : ops.messages,//自定义错误提示信息
		rules : ops.rules,//自定义必输项
		submitHandler: function(form){
			$("button").attr("disabled",true);
			$.gritter.add({
				title: '操作提示',
				sticky : true,
				text: "正在提交，请稍等...",
				class_name: 'gritter-info gritter-center'
			}); 
			ops.submitHandler(form);
		}
	});
	return $this;
};
function stateChangeFirefox(_frame){
   $("#loading").hide();
   $("#mainFrame").show();
   var ifm = document.getElementById("mainFrame");
	var subWeb = document.frames ? document.frames["mainFrame"].document : ifm.contentDocument;
	if (ifm != null && subWeb != null) {
		ifm.height = subWeb.body.scrollHeight;
	}
}

function stateChangeIE(_frame){
	if (_frame.readyState=="interactive"){
	  $("#loading").hide();
	  $("#mainFrame").show();
	  var ifm = document.getElementById("mainFrame");
		var subWeb = document.frames ? document.frames["mainFrame"].document : ifm.contentDocument;
		if (ifm != null && subWeb != null) {
			ifm.height = subWeb.body.scrollHeight;
		}
	}
}
function searchData(tableName){
	var postData = {};
	var array = $("#searchForm").serializeArray();
	for(var i=0;i < array.length;i++){
		if (array[i].value != '') {
			postData[array[i].name] = array[i].value;
		}
	}
	$("#"+tableName).jqGrid("setGridParam",{
		postData : postData,
		page : 1
	},true).trigger("reloadGrid");
}
function commonForward(url){
	window.location = url;
}
function registerNS(ns) {
	var nsParts = ns.split(".");
	var root = window;
	for ( var i = 0; i < nsParts.length; i++) {
		if (typeof root[nsParts[i]] == "undefined")
			root[nsParts[i]] = new Object();
		root = root[nsParts[i]];
	}
}