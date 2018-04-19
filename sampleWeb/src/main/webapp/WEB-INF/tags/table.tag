<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/framework/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="表格ID"%>
<%@ attribute name="url" type="java.lang.String" required="true" description="数据地址"%>
<%@ attribute name="colModel" type="java.lang.String" required="true" description="表格列信息配置"%>
<%@ attribute name="loadComplete" type="java.lang.String" required="false" description="表格加载成功回调方法"%>
<%@ attribute name="shrinkToFit" type="java.lang.Boolean" required="true" description="不按百分比自适应列宽"%>
<script type="text/javascript">
	jQuery(function($) {
		var grid_selector = "#${id}";
		var pager_selector = "#yqjrPage_${id}";
		jQuery(grid_selector).jqGrid({
			//获取数据地址
			url:"${url}",
			//服务器端返回的数据类型
			datatype: "json",
			//ajax提交方式
			mtype : "GET",
			//描述json 数据格式的数组
			jsonReader: {
				root: "list",
				pageNo: "pageNo",
				total: "totalPage",
				records: "count"
			}, 
			//表格高度，可以是数字，像素值或者百分比
			height:"auto",
			//列显示名称，是一个数组对象
			//colNames:['登录名','姓名','电话', '手机', '用户类型',{name:'是否允许登录',align:'center'}, '操作'],
			//常用到的属性：name 列显示的名称；index 传到服务器端用来排序用的列名称；width 列宽度；align 对齐方式；sortable 是否可以排序
			colModel:${colModel}, 
			//定义是否要显示总记录数
			viewrecords : true,
			//设置表格可以显示的记录数
			rowNum:10,
			//一个下拉选择框，用来改变显示记录数，当选择时会覆盖rowNum参数传递到后台
			rowList:[10,20,30],
			//定义翻页用的导航栏，必须是有效的html元素。翻页工具栏可以放置在html页面任意位置
			pager : pager_selector,
			//设置表格 zebra-striped 值
			altRows: true,
			//此属性用来说明当初始化列宽度时候的计算类型，如果为ture，则按比例初始化列宽度。如果为false，则列宽度使用colModel指定的宽度
			shrinkToFit: ${shrinkToFit},// 不按百分比自适应列宽
			//multiselect
			multiselect: true,
			//只有在multiselect设置为ture时起作用，定义使用那个key来做多选。shiftKey，altKey，ctrlKey
			//multikey: "ctrlKey",
			//只有当multiselect = true.起作用，当multiboxonly 为ture时只有选择checkbox才会起作用
	        multiboxonly: false,
	        sortable : true,
	        sorttype : true,
	        //当从服务器返回响应时执行
			loadComplete : function(xhr) {
				var table = this;
				updatePagerIcons(table);
				${loadComplete}
				setHash();
			},
			//如果请求服务器失败则调用此方法。xhr：XMLHttpRequest 对象；satus：错误类型，字符串类型；error：exception对象
			loadError : function(xhr,status,error){
				
			},
			//当点击单元格时触发。rowid：当前行id；iCol：当前单元格索引；cellContent：当前单元格内容；e：event对象
			onCellSelect : function(rowid,iCol,cellcontent,e){
				
			},
			//双击行时触发。rowid：当前行id；iRow：当前行索引位置；iCol：当前单元格位置索引；e:event对象
			ondblClickRow : function(rowid,iRow,iCol,e){
				
			},
			autowidth: true
		});
		$(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size
		if(!${shrinkToFit}){
			$(grid_selector).closest(".ui-jqgrid-bdiv").css({ 'overflow-x' : 'scroll' });
		}
		//enable search/filter toolbar
		//jQuery(grid_selector).jqGrid('filterToolbar',{defaultSearch:true,stringResult:true})
		//jQuery(grid_selector).filterToolbar({});
		//enable datepicker
		//设置按钮
		jQuery(grid_selector).jqGrid('navGrid',pager_selector,{
			edit:false,
			add:false,
			del:false,
			search:false,
			refresh:false
		})
		//replace icons with FontAwesome icons like above
		function updatePagerIcons(table) {
			var replacement = 
			{
				'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
				'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
				'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
				'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
			};
			$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
				var icon = $(this);
				var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
				
				if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
			})
		}
	
	
		//var selr = jQuery(grid_selector).jqGrid('getGridParam','selrow');
	
		
		
		//$(grid_selector).jqGrid( 'setGridWidth',$("#widget-box-search").width() );
		//jQuery("#id").closest(".ui-jqgrid-bdiv").css({ 'overflow-x' : 'scroll' });
		//setHash();
	});
</script>
<table id="${id}"></table>
<div id="yqjrPage_${id}"></div>
