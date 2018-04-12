var updateDivision = "http://"+window.location.host+"/api/lev2/updateDivision";
var prjdriftdataurl = "http://"+window.location.host+"/api/lev2/queryWorkingStructure";//"datagrid_prjdrift.json";// oracle包-当前阶段草稿版WBS版本信息表
var prjdrifwbsdataurl = "http://"+window.location.host+"/api/lev2/queryWorkingStructureDetails";//"prjdrifwbsdataurl.json";// oracle包-加载当前阶段草稿版WBS详细数据
var prjpubproductsdataurl = "datagrid_Prjpubproductlist.json";// oracle包-加载图纸状态变更预览列表
var prjviewproductsdataurl = "datagrid_Prjviewproductlist.json";// oracle包-链接图纸状态记录页面

var saveUrl = "http://"+window.location.host+"/api/lev2/addTask"; // oracle包-保存当前阶段草稿版WBS
var checkinUrl = "http://"+window.location.host+"/api/lev2/unlockWorkingStructure"; // oracle包-签入当前阶段草稿版WBS
var checkouturl = "http://"+window.location.host+"/api/lev2/lockWorkingStructure";// oracle包-签出当前阶段草稿版WBS
var puburl = "http://"+window.location.host+"/api/lev2/publishStructure";// oracle包-发布当前阶段草稿版WBS
var levelText = {"root":"阶段","sys":"系统","div":"子项","spe":"专业"};
var getnewid = "http://"+window.location.host+"/api/lev2/getTaskIds";//【对接修正161011
var driftversionexpanded = false;

var nameTextbox = ["tbxdivname", "tbxdivdual"];
var nameTextCancel = ["", ""];//名称文本框编辑撤销缓存
var currDiv = null;//当前专业列表所属子项node.id

var prolist = [];//项目专业表
var subitem_west_max = false;//子项选库面板是否最大化
var editId = [];//"处于编辑状态数据行"动态数组
var endEditId = [];//"已完成编辑状态"动态数组
var cancelId = [];//"放弃则删除新增行"动态数组
var editNode =[];//"处于编辑状态树节点"动态数组
var subitem_startcodes = [];//节点其实代号对象{pid:1,start:1}
var treeCancelData = 'treeCancelData';//树操作撤销缓存字符串化数据
var gridCancelData = 'gridCancelData';//表格操作撤销缓存字符串化数据
var subitem_dndCancel = [];//拖拽操作撤销数据初始化
var subitem_Vals = [];//系统子项号参照对象集
var pbs_ref_tab;//引用pbs版本tab
var subitemContextRow;//系统子项表格右键点击行
var newIds = [];//后台取编号
var rootId = "";//存根节点
var recode = true;//继续操作时是否重新编号

$('#prjdrftgrid').datagrid({
	singleSelect: true, pagination: false,
	fit: true,
    fitColumns: true,
	columns: [[{ field: 'draftwbsName', title: '版本名称', width: 250 },
		{ field: 'time', title: '保存时间', width: 126,fixed:true },
		{ field: 'operator', title: '操作人', width: 55, fixed:true },
		{ field: 'checkout', title: '当前签出', width: 55, fixed:true }]]
});

$("#subitem_collapse").addClass("l-btn");//改变默认下拉按钮样式
$("#subitem_collapse").removeClass("l-btn-plain");//改变默认下拉按钮样式

$('#subitem_toolbar_checkout').linkbutton({//项目结构签出
	iconCls: 'icon-back',
	onClick: function () {
		_reloadtree("noupdate");
		if(!$('#subitem_toolbar_checkout').is(":hidden") && $('#subitem_toolbar_checkout').linkbutton('options').disabled){
			$('#speformdiv').window('close');
			$.messager.alert("操作提示", "不可签出：请检查当前WBS签出状态。", "error");
			$('#wbs_layout').layout('expand', 'north');
			_reloadtree();
		}else{
			$.ajax({
				url: checkouturl+"?projID="+prjid+"&phaseID="+prjPhaseID+"&"+userPar+"&personID="+perID,
				type: "POST",
				success: function (data) {
					$('#speformdiv').window('close');
					if(data.checkOut && data.checkOut=="done"){
						_reloadtree();
						$.messager.show({title:'提示', msg:'已签出。'});
						$(subitemNoEdting).show();
					}else $.messager.alert("签出失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
				}
			});
		}
	}
});
$('#subitem_toolbar_checkin').linkbutton({//项目结构签入
	iconCls: 'icon-enter',
	onClick: function () {
		_checkInWBS();
	}
});
function _checkInWBS(publish){
	if (!$('#subitem_toolbar_save').linkbutton('options').disabled){
		$.messager.alert("操作提示", "不可签入：存在未保存的编辑，请先保存。", "error");
		return;
	}
	_reloadtree("noupdate");
	if($('#subitem_toolbar_checkin').is(":hidden")){
		$('#speformdiv').window('close');
		_reloadtree();
		$.messager.show({title:'提示', msg:'已签入。'});
	}else{
		$.ajax({
			url: checkinUrl+"?projectId="+prjid+"&"+userPar+"&personID="+perID,
			type: "POST",
			success: function (data) {
				$('#speformdiv').window('close');
				if(data.returnMsg=="成功"){
					_reloadtree();
					_toggleTextbox();
					if(publish){
						$.messager.show({title:'提示', msg:'发布成功。'});
					}else $.messager.show({title:'提示', msg:'签入成功。'});
				}else $.messager.alert("签入失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
			}
		});
	}
}
$('#subitem_toolbar_reset').linkbutton({//项目结构编辑重置
	iconCls: 'icon-reload',
	onClick: function () {
		if (!$('#subitem_toolbar_save').linkbutton('options').disabled) {
			$.messager.confirm('操作提示', '将放弃所有未保存的编辑，请确认是否继续？', function (r) {
				if (r){
					_reloadtree();
				}
			});
		}else{
			_reloadtree();
		}
	}
});
function _getSrc(grid, withChildren,noDefault){//获取被引用行
	var result = [];
	var gridSrc = eval(JSON.stringify($(grid).datagrid('getSelections')));
	if(noDefault){
		for(var i=0;i<gridSrc.length;i++){
			if(_isDefaultNode(gridSrc[i])){
				gridSrc.splice(i,1);
				break;
			}
		}
	}
	for(var i=0;i<gridSrc.length;i++){
		result[result.length] = {};
		for(var key in gridSrc[i]){
			if(!withChildren&&(key=="children"||key=="spechildren")){
				result[result.length-1][key]=[];
			}else if(key!="src") result[result.length-1][key]=gridSrc[i][key];
		}
		switch(grid){
			case "#pubgrid":
				result[result.length-1]["src"]="当前项目发布版WBS";
				break;
			case "#hisgrid":
				result[result.length-1]["src"]="历史项目PBS/WBS";
				break;
			case "#gridPrjSys":
				result[result.length-1]["children"]=[];
				result[result.length-1]["iconCls"]="icon-sysbr";
				result[result.length-1]["level"]="sys";
				result[result.length-1]["src"]="模板";
				break;
			case "#gridPrjsub":
				result[result.length-1]["spechildren"]=[];
				result[result.length-1]["iconCls"]="icon-divbr";
				result[result.length-1]["level"]="div";
				result[result.length-1]["src"]="模板";
				break;
			case "#gridPrjmajors":
				result[result.length-1]["children"]=[];
				result[result.length-1]["level"]="spe";
				result[result.length-1]["src"]="参与项目的专业列表";
		}
	}
	return result;
}
$('#subitem_toolbar_ref').linkbutton({//项目结构编辑引用按钮
	iconCls: 'icon-redo',
	onClick: function () {
		if(!$('#west').is(":visible")){
			$(document.body).layout('expand', 'west');
			$.messager.show({title:'提示', msg:"请在左侧页面选择被引用对象。"});
			return false;
		}
		var selectedTab = $('#reftabs').tabs('getSelected');
		switch ($('#reftabs').tabs('getTabIndex', selectedTab)) {
			case 0:
				_addbr(_getSrc('#pubgrid', $("#prjpubcheck").is(':checked'),"noDefault"));
				break;
			case 1:
				_addbr(_getSrc('#hisgrid', $("#prjhischeck").is(':checked'),"noDefault"));
				break;
			case 2:
				_addbr(_getSrc('#gridPrjSys'));
				break;
			case 3:
				_addbr(_getSrc('#gridPrjsub'));
				break;
			case 4:
				_addbr(_getSrc('#gridPrjmajors'),"专业");
		}
	}
});

$('#subitem_toolbar_del').menubutton({//项目结构编辑删除菜单按钮
	iconCls: 'icon-remove',
	plain: false,
	menu: '#subitem_toolbar_delmenu',
	hasDownArrow: false
});
//项目结构编辑删除按钮菜单
$('#subitem_toolbar_delmenu').menu({ minWidth: 90 }).menu('appendItem', {
	text: '系统',
	iconCls: 'icon-sysbr',
	id: 'delsys',
	onclick: function () {
		_delbr("sys","系统");
	}
}).menu('appendItem', {
	text: '子项',
	id: 'delsub',
	iconCls: 'icon-divbr',
	onclick: function () {
		_delbr("div","子项");
	}
}).menu('appendItem', {
	text: '专业',
	iconCls: 'icon-spebr',
	onclick: function () {
		_delbr("spe","专业");
	}
});
function _delbr(level,levelname){//删除方法
	var tObj = _getTargetIds(level,["新增"],null,"noDefault");
	if(!tObj) return
	$.messager.confirm('操作提示', tObj.count+"个"+levelname+"将被删除，是否继续？", function (r) {
		if (r) {
			switch(tObj.treeOrGrid){
				case "tree":
					_setEditing([], "tree");
					if(level!="spe"){
						for(var i=0;i<tObj.treeIds.length;i++){
							var node = $('#divtree').tree('find', tObj.treeIds[i]);
							$('#divtree').tree('remove', node.target);
						}
					}else{
						for(var i=0;i<tObj.treeIds.length;i++){
							var pNode = $('#divtree').tree('find', tObj.treeIds[i].pId);
							var newSpe = [];
							for(var j=0;j<pNode.spechildren.length;j++){
								var ishave = false;
								for(var k=0;k<tObj.treeIds[i].spe.length;k++){//匹配删除数组
									if(pNode.spechildren[j].id==tObj.treeIds[i].spe[k]){
										ishave = true;
										break;
									}
								}
								if(!ishave) newSpe[newSpe.length] = pNode.spechildren[j];//未匹配到则留下
							}
							var strNewSpe = JSON.stringify(newSpe);
							$('#divtree').tree('update',{target:pNode.target,spechildren:JSON.parse(strNewSpe)});
						}
					}
					break;
				case "grid":
					_setEditing([], "grid");
					for(var i=0;i<tObj.Ids.length;i++){
						var delIndex = $('#spegrid').datagrid('getRowIndex',tObj.Ids[i]);
						$('#spegrid').datagrid('deleteRow', delIndex);
					}
					editId = [];
			}
		}else return false;
	});
}
$('#subitem_toolbar_add').menubutton({//项目结构编辑新增菜单按钮
	iconCls: 'icon-add',
	plain: false,
	menu: '#subitem_toolbar_addmenu',
	hasDownArrow: false
});
$('#subitem_toolbar_addmenu').menu({ minWidth: 90 }).menu('appendItem', {
	text: '系统',
	iconCls: 'icon-sysbr',
	id: 'addsysbtn',
	onclick: function () {
		if(_addbr([{"code":"", "name": '新系统', "level": "sys", "iconCls":"icon-sysbr", "src": "手动新增", "children":[]}])){
			editId=[$('#spegrid').datagrid('getRows').length-1];
			_setEditing(editId, "grid");
		}
	}
}).menu('appendItem', {
	text: '子项',
	iconCls: 'icon-divbr',
	id: 'adddivbtn',
	onclick: function () {
		if(_addbr([{"code":"", "name": '新子项', "level": "div", "iconCls":"icon-divbr", "src": "手动新增", "spechildren":[]}])){
			editId=[$('#spegrid').datagrid('getRows').length-1];
			_setEditing(editId, "grid");
		}
	}
}).menu('appendItem', {
	text: '专业',
	iconCls: 'icon-spebr',
	id: 'addspebtn',
	onclick: function () {//默认展开west并跳转到专业tab
		if(!$('#west').is(":visible")) $(document.body).layout('expand', 'west');
		if($('#reftabs').tabs('getTabIndex', $('#reftabs').tabs('getSelected'))!=4) $('#reftabs').tabs('select', 4);
		_addbr(_getSrc('#gridPrjmajors'),"专业");
	}
});
function _addbr(srcObj, levelName){//新增及引用方法
	if(!srcObj || srcObj.length==0){
		var msg = "请先勾选被引用的节点，不含“默认系统”、“默认子项”。"
		if(levelName && levelName =="专业"){
			var msg = "请先勾选需要新增的专业。"
			$.messager.show({title:'提示', msg:msg});
			return false;
		} else{
			$.messager.alert('操作提示', msg, 'error');
			return false;
		}
	}
	var treeOrGrid = "grid";
	if(division){//验证Src与Tgt是否匹配
		switch(srcObj[0].level){
			case "sys":
				if(headOrbranch != "head"){
					$.messager.alert('操作提示', '不可应用：当前阶段不支持系统层级。', 'error');
					return false;
				}else _continue("root");
				break;
			case "div":
				if(headOrbranch == "head"){
					var tgtLevel = "sys";
				}else _continue("root");
				break;
			case "spe"://专业判断树是否勾了子项>>是否进入的是子项层级
				var tgtLevel = "div";
		}
	}else{
		if(srcObj[0].level !="spe"){
			$.messager.alert('操作提示', '不可应用：当前阶段不支持非专业层级。', 'error');
			return false;
		}else _continue("root");
	}
	if(tgtLevel){
		switch(tgtLevel){
			case "sys":
				if($('#divtree').tree('find',currDiv).level!=tgtLevel || $('#divtree').tree('find',currDiv).status=="失效"){
					$.messager.alert('操作提示', '不可应用：请先进入目标系统节点详情页面，状态不可为“失效”。', 'error');
					return false;
				}else if(_isDefaultNode($('#divtree').tree('find',currDiv))){
					$.messager.alert('操作提示', '不可应用：默认系统下不可增加子项。', 'error');
					return false;
				}else _continue();
				break;
			case "div":
				var tObj = _getTargetIds(tgtLevel, ["生效","新增"], "noMsg");
				if(tObj && tObj.treeOrGrid=="tree" && tObj.treeIds.length>0){
					$.messager.confirm('操作提示 - ◀◀&nbsp;&nbsp;左侧（树）增加专业', '将向左侧系统子项树勾选的子项下增加专业，请确认是否继续？<br>（如果想向右侧子项详情页面增加，请取消本次操作并去掉左侧系统子项树的勾选项。）', function (r) {
						if (r) {
							treeOrGrid = "tree";
							_continue();
						}
					});
				}else if($('#divtree').tree('find',currDiv).level!=tgtLevel || $('#divtree').tree('find',currDiv).status=="失效"){
					$.messager.alert('操作提示', '不可应用：请先勾选左侧系统子项树的子项节点或者进入目标子项详情页面，状态不可为“失效”。', 'error');
					return false;
				}else{
					$.messager.confirm('操作提示 - ▶▶ 右侧（表）增加专业', '将向右侧子项详情页面内增加专业，请确认是否继续？<br>（如果想向左侧系统子项树增加，请取消本次操作并勾选左侧系统子项树的子项节点。）', function (r) {
						if (r) {_continue();}
					});
				}
		}
	}
	function _continue(rootLevel){
		var newidcount_i = 0;
		var reSrc = new Object();
		var start = 0;
		if(treeOrGrid == "grid"){
			if(!rootLevel){
				var tgtArr = [currDiv];
			}else var tgtArr = [$('#divtree').tree('getRoots')[0].id];
		}else var tgtArr = tObj.treeIds;
		for(var i=0;i<tgtArr.length;i++){
			reSrc[tgtArr[i]] = _rebuildSrc(srcObj, tgtArr[i]);
		}
		if(newidcount_i==0){
			$.messager.alert('操作提示', '被引用或新增节点未能通过校验（例如：对象节点下专业不可重复）。', 'error');
			return;
		}
		$.ajax({
			url: getnewid,
			data: { num: newidcount_i},
			contentType: 'application/x-www-form-urlencoded',
			async: false,
			type: "GET",
			success: function (data) {
				for(var key in reSrc){
					_reNewId(reSrc[key], data);
					for(var i=0;i<reSrc[key].length;i++){
						reSrc[key][i].parentId = parseInt(key);//首层子节点parentId值更新为key值
					}
				}
				if(rootLevel){
					var node = $('#divtree').tree('getRoots')[0];
					_enterNode(node.id);
					$('#divtree').tree('select',node.target);
				}
				if(treeOrGrid=="grid"){
					_setCancelEditing("grid");
					for(var i=0;i<reSrc[currDiv].length;i++){
						$('#spegrid').datagrid('appendRow', reSrc[currDiv][i]);
						//editId[editId.length]=$('#spegrid').datagrid('getRows').length-1;//生成Ids数组
					}
					//_setGridEditing(editId);
					$('#spegrid').datagrid('scrollTo',$('#spegrid').datagrid('getRows').length-1).datagrid('autoSizeColumn','name');//滚动到末尾行
				}else if(treeOrGrid == "tree"){
					_setTreeEditing();
					$('#divtree').tree('disableDnd');
					$('#gospe'+currDiv).linkbutton({disabled:true}).html('&nbsp;已进入&nbsp;');
					for(var key in reSrc){
						var node = $('#divtree').tree('find', key);
						var ckey = node.spechildren ? "spechildren" : "children";
						if(node[ckey]){
							var cnode = JSON.parse(JSON.stringify(node[ckey]));
						}else{
							node[ckey]=[];
							cnode = [];
						}
						for(var i=0;i<reSrc[key].length;i++){
							cnode[cnode.length]=reSrc[key][i];
						}
						if(ckey=="spechildren"){
							$('#divtree').tree('update', {target: node.target, spechildren:cnode});
							$('#divtree').tree('scrollTo',node.target);//滚动到node.target
						}else{
							$('#divtree').tree('update', {target: node.target, children:cnode});
							$('#divtree').tree('scrollTo',$('#divtree').tree('find',cnode.id).target);
						}//滚动到cnode
					}
				}
			}
		});
		/* for(var key in reSrc){
			_loopConsole(reSrc[key]);
		} */
		function _reNewId(obj, Ids){
			for(var i=0;i<obj.length;i++){
				var key = obj[i].spechildren ? "spechildren" : "children";
				if(obj[i][key]){;//搜索下一级节点parentId为此id的一起更新
					for(var j=0;j<obj[i][key].length;j++){
						if(obj[i][key][j].parentId && obj[i][key][j].parentId==obj[i].id)
							obj[i][key][j].parentId=Ids[start].id;
					}
				}
				obj[i]["projElementId"]=Ids[start].taskId;
				obj[i].id=Ids[start].id;
				start ++;
				if(obj[i][key]) _reNewId(obj[i][key], Ids);
			}
		}
		function _loopConsole(obj){
			for(var i=0;i<obj.length;i++){
				var key = obj[i].spechildren ? "spechildren" : "children";
				if(obj[i][key]) _loopConsole(obj[i][key]);
			}
		}
		function _rebuildSrc(src, tgt){
			var rebuilt = [];
			_loopSrc(src, rebuilt, tgt);
			return rebuilt;
			function _loopSrc(obj, result, tgtId){//递归校验重构子节点
				for(var i=0;i<obj.length;i++){
					var ishave = false;
					if(obj[i].level=="spe"){
						//obj[i]["workCoef"] = 1;
						var speArr = JSON.parse(speData);
						for(var j=0;j<speArr.length;j++){//先匹配code，如无则匹配name，成功则赋值其余字段
							if((obj[i].code && speArr[j].code==obj[i].code)||(obj[i].name && speArr[j].name==obj[i].name)){
								ishave = true;
								obj[i]["code"] = speArr[j].code;
								obj[i]["name"] = speArr[j].name;
								obj[i]["dualName"] = speArr[j].dual;
								break;
							}
						}//接下来去重
						if(ishave && srcObj[0].level=="spe"){
							var tgtChildren = $('#divtree').tree('find',tgtId).spechildren;
							for(var j=0;j<tgtChildren.length;j++){
								if((obj[i].code && tgtChildren[j].code==obj[i].code)||(obj[i].name && tgtChildren[j].name==obj[i].name)){
									ishave=false;
									break;
								}
							}
						}
					}else ishave = true;
					if(ishave){
						result[result.length] = {};
						for(var key in obj[i]){
							if($.inArray(key,["code","spechildren","children","startDate","endDate","status"])<0)
								result[result.length-1][key]=obj[i][key];
						}
						if(obj[i].level=="spe"){
							result[result.length-1]["code"]=obj[i]["code"];
						}else result[result.length-1]["code"]="";
						result[result.length-1]["startDate"]=prjStartDate;
						result[result.length-1]["endDate"]=prjEndDate;
						result[result.length-1]["status"]="新增";
						result[result.length-1]["workCoef"]=1;
						newidcount_i++;
						var ckey = obj[i].spechildren ? "spechildren" : "children";
						if(obj[i][ckey]){
							result[result.length-1][ckey] = [];
							_loopSrc(obj[i][ckey], result[result.length-1][ckey], tgtId);
						}
					}
				}
			}
		}
	}
}
$('#subitem_toolbar_edit').menubutton({    //项目结构编辑修改菜单按钮
	iconCls: 'icon-edit',
	plain: false,
	menu: '#chooseedits',
	hasDownArrow: false
});
$('#chooseedits').menu({ minWidth: 90 }).menu('appendItem', {
	text: '详情',
	iconCls: 'icon-input',
	id: 'edittext',
	onclick: function () {//修改详情
		var rows = $('#spegrid').datagrid('getSelections');
		for(var i=0;i<rows.length;i++){
			if(rows[i].status!="失效" && !_isDefaultNode(rows[i])){
				editId[editId.length]=$('#spegrid').datagrid('getRowIndex', rows[i].id);
			}
		}
		if(editId.length==0){
			$.messager.alert("操作提示", "请先选择详情列表中需要修改的节点行。<br>注意：状态不可为失效，专业只可修改日期，系统、子项只可修改名称，默认系统、默认子项不可修改。", "error");
		}else _setEditing(editId, "grid");
	}
}).menu('appendItem', {
	text: '日期',
	iconCls: 'icon-date',
	id: 'editdate',
	onclick: function () {//修改日期
		var tObj = _getTargetIds("spe",["生效","新增"]);
		if(!tObj) return;
		if(tObj.treeOrGrid=="tree"){
			var rows = [];
			for(var i=0;i<tObj.treeIds.length;i++){
				var pNode = $('#divtree').tree('find', tObj.treeIds[i].pId);
				for(var j=0;j<pNode.spechildren.length;j++){
					for(var k=0;k<tObj.treeIds[i].spe.length;k++){
						if(pNode.spechildren[j].id==tObj.treeIds[i].spe[k]){
							rows[rows.length] = pNode.spechildren[j];
							break;//匹配筛选数组成功则加入目标
						}
					}
				}
			}
		}else if(tObj.treeOrGrid=="grid"){
			var rows = $('#spegrid').datagrid('getSelections');
		}
		var oldstartdate = "";
		var oldenddate = "";
		var ishave = false;
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].level == "spe" && rows[i].status != "失效") {
				ishave = true;
				if(rows[i].startDate && rows[i].startDate!="" && ((oldstartdate!="" && oldstartdate.stringToDate() < rows[i].startDate.stringToDate())||oldstartdate=="")){
					oldstartdate=rows[i].startDate;
				}
				if(rows[i].endDate && rows[i].endDate!="" && ((oldenddate!="" && oldenddate.stringToDate() > rows[i].endDate.stringToDate())||oldenddate=="")){
					oldenddate=rows[i].endDate;
				}
			}
		}
		if (!ishave) {
			$.messager.alert("操作提示", "请先选择详情列表中需要修改日期的专业。", "error");
		} else {
			$('#ckbdatestart').prop("checked",false);
			$('#ckbdateend').prop("checked",false);
			$("#dateplanstart").datebox('calendar').calendar({
				validator: function (date) {
					return prjStartDate.stringToDate() <= date && date <= prjEndDate.stringToDate();
				}
			});
			$("#dateplanend").datebox('calendar').calendar({
				validator: function (date) {
					return prjStartDate.stringToDate() <= date && date <= prjEndDate.stringToDate();
				}
			});
			$("#dateplanstart").datebox('setValue', prjStartDate).combo("disable");
			$("#dateplanend").datebox('setValue', prjEndDate).combo("disable");
			$("#oldstartdate").text(oldstartdate);
			$("#oldenddate").text(oldenddate);
			$('#dateformdiv').window('open');
		}
	}
}).menu('appendItem', {
	text: '设置生效',
	iconCls: 'icon-stateon',
	id: 'editstatuson',
	onclick: function (){//设置生效
		var tObj = _getTargetIds(null,["失效"]);
		if(!tObj) return;
		var errortxt = "";
		if(tObj.treeOrGrid=="tree"){
			_setTreeEditing();
			$('#divtree').tree('disableDnd');
			$('#gospe'+currDiv).linkbutton({disabled:true}).html('&nbsp;已进入&nbsp;');
			for(var i=0;i<tObj.treeIds.length;i++){
				var node = $('#divtree').tree('find', tObj.treeIds[i]);
				var pNode = $('#divtree').tree('getParent', node.target);
				if(pNode.status!="失效"){//校验父节点是否生效
					$("#divtree").tree('update',{target:node.target, status:"生效"});
				}else{
					errortxt = "请先将目标节点的父节点 "+pNode.code+" "+pNode.name+" 状态设置为“生效”。";
					_cancelEditing();
					break;
				}
			}
		}else if(tObj.treeOrGrid=="grid"){
			var pNode = $('#divtree').tree('find', currDiv);
			if(pNode.status!="失效"){
				_setCancelEditing("grid");
				var rows = $('#spegrid').datagrid('getSelections');
				for(var i=0;i<rows.length;i++){
					if(rows[i].status=="失效"){
						editId[editId.length]=$('#spegrid').datagrid('getRowIndex', rows[i].id);
						$('#spegrid').datagrid('updateRow',{index: editId[editId.length-1], row: {status:"生效"}});
					}
				}
				_setEditing(editId);
			}else{
				errortxt = "请先将目标节点的父节点 "+pNode.code+" "+pNode.name+" 状态设置为非“失效”。";
			}
		}
		if(errortxt!="") $.messager.alert("操作提示", errortxt, "error");
	}
}).menu('appendItem', {
	text: '设置失效',
	iconCls: 'icon-stateoff',
	id: 'editstatusoff',
	onclick: function (){//设置失效
		var tObj = _getTargetIds(null,["生效"],null,"noDefault");
		if(!tObj) return;
		var rows = [];
		if(tObj.treeOrGrid=="tree"){//首先筛选出父节点未check的rows
			for(var i=0;i<tObj.treeIds.length;i++){
				if(_checkparentchecked(tObj.treeIds[i], true)){
					rows[rows.length] = $('#divtree').tree('find', tObj.treeIds[i]);
				}
			}
		}else if(tObj.treeOrGrid=="grid") rows = $('#spegrid').datagrid('getSelections');
		var nodes = [];
		for(var i=0;i<rows.length;i++){
			if(rows[i].status=="生效" && !_isDefaultNode(rows[i])){
				var key = rows[i].spechildren ? "spechildren" : "children";
				if(_checkchildstatus(rows[i][key],true)){
					nodes[nodes.length] = rows[i];
				}
			}
		}
		if(nodes.length==0){
			$.messager.alert("操作提示", "不可失效：请勾选状态为“生效”的节点，并预先删除其状态为“新增”的子节点。", "error");
		}else{
			if(tObj.treeOrGrid=="tree"){
				_setTreeEditing();
				$('#divtree').tree('disableDnd');
				$('#gospe'+currDiv).linkbutton({disabled:true}).html('&nbsp;已进入&nbsp;');
			}else if(tObj.treeOrGrid=="grid") _setCancelEditing("grid");
			for(var i=0;i<nodes.length;i++){
				var newChildren = [];
				var ckey = nodes[i].spechildren ? "spechildren" : "children";
				if(nodes[i][ckey]) _getUpdatedStatusData(nodes[i][ckey], newChildren);
				if(tObj.treeOrGrid=="tree"){
					if(ckey=="spechildren"){
						$('#divtree').tree('update', {target:nodes[i].target, status:"失效", spechildren:newChildren});
					}else if(ckey=="children") $('#divtree').tree('update', {target:nodes[i].target, status:"失效", children:newChildren});
				}else if(tObj.treeOrGrid=="grid"){
					if(ckey=="spechildren"){
						$('#spegrid').datagrid('updateRow',{index: $('#spegrid').datagrid('getRowIndex', nodes[i].id), row: {
							status:"失效",
							spechildren:newChildren}
						});
					}else if(ckey=="children"){
						$('#spegrid').datagrid('updateRow',{index: $('#spegrid').datagrid('getRowIndex', nodes[i].id), row: {
							status:"失效",
							children:newChildren}
						});
					}
					editId = [];
					_setGridEditing(editId);
				}
				
			}
		}
		function _checkparentchecked(id, flag){
			if($('#divtree').tree('find', id).level!="root"){
				var pNode = $('#divtree').tree('getParent', $('#divtree').tree('find', id).target);
				if(pNode.level!="root"){
					if($.inArray(pNode.id, tObj.treeIds)>-1){
						flag = false;
					}else flag = _checkparentchecked(pNode.id, flag);
				}
			}
			return flag;
		}
		function _checkchildstatus(children, flag) {
			for (var j = 0; j < children.length; j++) {
				if (children[j].status == "新增" || _isDefaultNode(children[j])) {
					flag = false;
					break;
				}
				var key = children[j].spechildren ? "spechildren" : "children";
				if (children[j][key])
					flag = _checkchildstatus(children[j][key], flag);
			}
			return flag;
		}
		function _getUpdatedStatusData(data, result) {
			for(var i=0;i<data.length;i++){
				result[result.length]={status:"失效"};
				for(var key in data[i]){
					if(key!="status"&&key!="spechildren"&&key!="children"){
						result[result.length-1][key]=data[i][key];
					}else if(key=="spechildren"||key=="children"){
						result[result.length-1][key]=[];
						_getUpdatedStatusData(data[i][key], result[result.length-1][key]);
					}
				}
			}
		}
	}
});
function _enableCombo(ckbid, txtid){//切换下拉框激活状态
	if ($(ckbid).is(':checked')) {
		$(txtid).combo("enable");
	}else $(txtid).combo("disable");
}
$('#btndateapply').linkbutton({//应用批量日期
	onClick: function () {
		if ($("#ckbdatestart").is(':checked') && $("#ckbdateend").is(':checked') && $("#dateplanstart").datebox('getValue').stringToDate() > $("#dateplanend").datebox('getValue').stringToDate()) {
			$.messager.alert({ title: '提示', msg: '计划完成日期不得早于计划起始日期，请更正。' });
			return false;
		}else if($("#ckbdatestart").is(':checked') && !$("#ckbdateend").is(':checked') && $("#dateplanstart").datebox('getValue').stringToDate() > $("#oldenddate").text().stringToDate()) {
			$.messager.alert({ title: '提示', msg: '计划起始日期不得晚于原区间完成日期，请更正。' });
			return false;
		}else if(!$("#ckbdatestart").is(':checked') && $("#ckbdateend").is(':checked') && $("#dateplanend").datebox('getValue').stringToDate() < $("#oldstartdate").text().stringToDate()) {
			$.messager.alert({ title: '提示', msg: '计划完成日期不得早于原区间起始日期，请更正。' });
			return false;
		}
		if(!$("#ckbdatestart").is(':checked') && !$("#ckbdateend").is(':checked')){
			$('#dateformdiv').window('close');
			return false;
		}
		var tObj = _getTargetIds("spe",["生效","新增"]);
		if(!tObj) return;
		if(tObj.treeOrGrid=="tree"){
			_setTreeEditing();
			$('#divtree').tree('disableDnd');
			$('#gospe'+currDiv).linkbutton({disabled:true}).html('&nbsp;已进入&nbsp;');
			var rows = [];
			for(var i=0;i<tObj.treeIds.length;i++){
				var pNode = $('#divtree').tree('find', tObj.treeIds[i].pId);
				var newSpe = [];
				for(var j=0;j<pNode.spechildren.length;j++){
					var ishave = false;
					for(var k=0;k<tObj.treeIds[i].spe.length;k++){
						if(pNode.spechildren[j].id==tObj.treeIds[i].spe[k]){
							ishave = true;
							break;//匹配筛选数组成功则加入目标
						}
					}
					if(ishave){
						newSpe[newSpe.length] = {};
						for(var key in pNode.spechildren[j]){
							if(key=="startDate" && $("#ckbdatestart").is(':checked')){
								newSpe[newSpe.length-1][key]=$('#dateplanstart').combo('getValue');
							}else if(key=="endDate" && $("#ckbdateend").is(':checked')){
								newSpe[newSpe.length-1][key]=$('#dateplanend').combo('getValue');
							}else newSpe[newSpe.length-1][key]=pNode.spechildren[j][key];
						}
					}else newSpe[newSpe.length] = pNode.spechildren[j];
				}
				var strNewSpe = JSON.stringify(newSpe);
				$('#divtree').tree('update',{target:pNode.target,spechildren:JSON.parse(strNewSpe)});
			}
		}else if(tObj.treeOrGrid=="grid"){
			_setCancelEditing("grid");
			var rows = $('#spegrid').datagrid('getSelections');
			for (var i = 0; i < rows.length; i++) {
				if (rows[i].level == "spe" && rows[i].status != "无效") {
					editId[editId.length] = $('#spegrid').datagrid('getRowIndex', rows[i].id);
					if($("#ckbdatestart").is(':checked')){
						$('#spegrid').datagrid('updateRow',{index: editId[editId.length-1], row: {
							startDate: $('#dateplanstart').combo('getValue')}
						});
					}
					if($("#ckbdateend").is(':checked')){
						$('#spegrid').datagrid('updateRow',{index: editId[editId.length-1], row: {
							endDate: $('#dateplanend').combo('getValue')}
						});
					}
				}
			}
			_setGridEditing(editId);
		}
		$('#dateformdiv').window('close');
	}
});
function _cancelEditing(){//编辑放弃
	if(treeCancelData!='treeCancelData'){
		$('#divtree').tree('loadData', JSON.parse(treeCancelData));
		if(currDiv) $('#gospe'+currDiv).linkbutton({disabled:true}).html('&nbsp;已进入&nbsp;');
		treeCancelData = 'treeCancelData';
	}else if(!document.getElementById("tbxdivname").readOnly){
		for(var i=0;i<nameTextbox.length;i++){
			$('#'+nameTextbox[i]).val(nameTextCancel[i]);
		}
	}else if(gridCancelData!='gridCancelData'){
		$('#spegrid').datagrid('rejectChanges');
		$('#spegrid').datagrid('clearChecked');
		$('#spegrid').datagrid('loadData', JSON.parse(gridCancelData));
		editId = [];
		gridCancelData = 'gridCancelData';
	}
	if(currDiv) $('#gospe'+currDiv).show();
	_recheck();
	$(subitemEdting).hide();
	$(subitemNoEdting).show();
	$('#divtree').tree('enableDnd');
	_toggleTextbox();
}

$('#subitem_toolbar_cancel').linkbutton({//项目结构编辑放弃
	iconCls: 'icon-undo',
	onClick: function () {
		_cancelEditing();
	}
});
$('#subitem_toolbar_accept').linkbutton({//项目结构编辑继续
	iconCls: 'icon-ok',
	onClick: function () {
		if(treeCancelData!='treeCancelData'){
			var validcode = _AutoSubitemCode();
			if(validcode){//同样需要刷新表，如果当前父节点被删除了，还要重置为初始状态；
				if(!$('#divtree').tree('find',currDiv)) currDiv = $('#divtree').tree('getRoots')[0].id;
				_enterNode(currDiv);
				treeCancelData = 'treeCancelData';
				var success = true;
			}
		}else if(!document.getElementById("tbxdivname").readOnly){
			var tbxcontent = $.trim($('#tbxdivname').val());
			if(tbxcontent.replace(/ /g, "")==""){
				$.messager.alert("操作提示", "节点名称不可为空。", "error");
				return false;
			}else{
				$('#divtree').tree('update', {target:$('#divtree').tree('find',currDiv).target, name: tbxcontent, dualName:$.trim($('#tbxdivdual').val())});
				var success = true;
			}
		}else if(gridCancelData!='gridCancelData'){//名称不可为空、日期先后
			for(var i=0;i<editId.length;i++){
				var nameEditor = $('#spegrid').datagrid('getEditor', {index:editId[i],field:'name'});
				var nameContent = $.trim($(nameEditor.target).textbox('getValue'));
				if(nameContent.replace(/ /g, "")==""){
					$.messager.alert("操作提示", "第"+(editId[i]+1)+"行节点名称不可为空。", "error");
					return false;
				}else{
					var sdateEditor = $('#spegrid').datagrid('getEditor', {index:editId[i],field:'startDate'});
					if(!$(sdateEditor.target).datebox('options').disabled){
						var sdateContent = $(sdateEditor.target).datebox('getValue').stringToDate();
						if(sdateContent<prjStartDate.stringToDate()||sdateContent>prjEndDate.stringToDate()){
							$.messager.alert("操作提示", "第"+(editId[i]+1)+"行计划起始日期超期。", "error");
							return false;
						}else{
							var edateEditor = $('#spegrid').datagrid('getEditor', {index:editId[i],field:'endDate'});
							if(!$(edateEditor.target).datebox('options').disabled){
								var edateContent = $(edateEditor.target).datebox('getValue').stringToDate();
								if(edateContent<prjStartDate.stringToDate()||edateContent>prjEndDate.stringToDate()){
									$.messager.alert("操作提示", "第"+(editId[i]+1)+"行计划完成日期超期。", "error");
									return false;
								}else if(edateContent<sdateContent){
									$.messager.alert("操作提示", "第"+(editId[i]+1)+"行计划完成日期不得早于计划起始日期。", "error");
									return false;
								}
							}
						}
					}
				}
			}
			$('#spegrid').datagrid('acceptChanges');//验证通过后接受列表更改并刷新树
			var strRows = JSON.stringify($('#spegrid').datagrid('getData').rows);
			var treeNode = $('#divtree').tree('find',currDiv);
			if(treeNode.children){
				var Ids = [];
				for(var i=0;i<treeNode.children.length;i++){
					Ids[Ids.length]=treeNode.children[i].id;
				}
				for(var i=0;i<Ids.length;i++){
					var node = $('#divtree').tree('find', Ids[i]);
					$('#divtree').tree('remove', node.target);
				}
				var newChildren = JSON.parse(strRows);
				for(var i=0;i<newChildren.length;i++){
					$('#divtree').tree('append',  {parent: treeNode.target,	data:newChildren[i]} );
				}
			}else $('#divtree').tree('update', {target: treeNode.target,spechildren:JSON.parse(strRows)});
			var validcode = _AutoSubitemCode();
			if(validcode){
				_enterNode(currDiv);
				editId = [];
				gridCancelData = 'gridCancelData';
				var success = true;
			}
		}
		if(success){//刷新专业表格中的信息
			if(currDiv) $('#gospe'+currDiv).show();
			_updateTextbox();
			_updateSpeTitle();
			_recheck();
			$(subitemEdting).hide();
			$(subitemNoEdting).show();
			$('#divtree').tree('enableDnd');
			$('#subitem_toolbar_save').linkbutton('enable');
			_toggleTextbox();
		}
	}
});
$('#subitem_toolbar_save').linkbutton({//项目结构编辑保存
	disabled: true,
	iconCls: 'icon-save',
	onClick: function () {//不可直接用数组给数组赋值，会造成相互引用
		_saveWBS();
	}
});
$('#subitem_toolbar_update').linkbutton({//项目结构编辑发布
	iconCls: 'icon-updateversion',
	onClick: function () {
		if (!$('#subitem_toolbar_save').linkbutton('options').disabled){
			$.messager.alert("操作提示", "不可发布：存在未保存的编辑，请先保存。", "error");
			return;
		}
		$.messager.confirm('操作提示', '将发布为新的一版WBS，请确认是否继续？', function (r) {
			if (r) {
				$('#speformdiv').window('open');
				$.ajax({
					url: prj_major_dataurl+"?projID="+prjid+"&phaseID="+prjPhaseID+"&"+userPar,
					type: "GET",
					success: function (data) {
						speData = JSON.stringify(data);
						if(majorloaded) $('#gridPrjmajors').datagrid('loadData', data);
						var total = 0;
						for(var i=0;i<data.length;i++){
							total += parseFloat(data[i].ratio);
						}
						total = Math.round(total*100)/100;
						if(total!=100){
							$('#speformdiv').window('close');
							$.messager.alert("操作提示", "不可发布：专业总工作量比例之和不为100%，请先前往“专业”页面编辑比例。", "error");
							if(!$('#west').is(":visible")) $(document.body).layout('expand', 'west');
							if($('#reftabs').tabs('getTabIndex', $('#reftabs').tabs('getSelected'))!=4) $('#reftabs').tabs('select', 4);
							return;
						}else{
							_reloadtree("noupdate");
							if($('#subitem_toolbar_update').is(':hidden')){
								$('#speformdiv').window('close');
								$.messager.alert("操作提示", "不可发布：请检查当前WBS签出状态。", "error");
								$('#wbs_layout').layout('expand', 'north');
							}else{
								$.ajax({
									url:puburl+"?projID="+prjid+"&phaseID="+prjPhaseID+"&headOrbranch="+headOrbranch+"&division="+division+"&"+userPar+"&personID="+perID,
									type: "POST",
									contentType: "application/json",
									success: function (data) {
										$('#speformdiv').window('close');
										if(data.returnMsg=="成功"){
											_checkInWBS("publish");//未发布版自动签入、列表更新
										}else $.messager.alert("发布失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
									}
								});
							}
						}
					}
				});
			}
		});
	}
});
$('#pubwindowexecute').linkbutton({
	onClick: function () {
		//首先非总部项目校验如果“工作量系数”列可见则非失效专业节点行中的数值必须为正数
		/* if (headOrbranch != "head") {
			var treeData = $('#subitem_grid').treegrid('getData')[0].children;
			var flag = true;
			checkWorkCoef(treeData, flag);
			if (!flag) {
				$.messager.alert("操作提示", "不可执行：请检查非失效专业工作量系数必须为正数。", "error");
				return false;
			}
		}
		//如通过校验则再校验图纸状态变更列表数据行数是否与选中行数相等
		var total = $('#grid_publists').datagrid('getData');
		var checked = $('#grid_publists').datagrid('getSelections');
		if (checked.length == 0 || checked.length < total.rows.length) {
			$.messager.alert("操作提示", "不可执行：图纸状态变更未全部确认。", "error");
			return false;
		}
		$("#pubwindowexecute").linkbutton('disable');
		$('#grid_publists').datagrid('loading');
		_uploadSubitemData(saveUrl, "发布"); */
	}
});
function checkWorkCoef(Obj, flag) {
	if (Obj) {
		for (var i = 0; i < Obj.length; i++) {
			if (Obj[i].level == "spe" && parseFloat(Obj[i].workCoef) < 0) {
				flag = false;
				return;
			}
			else {
				if (Obj[i].children) {
					checkWorkCoef(Obj[i].children, flag);
				}
			}
		}
	}
}
//更新图纸状态预览窗口
function updateviewwindow() {
	$("#pubwindowexecute").linkbutton('disable');
	$('#grid_publists').datagrid('loading');
	$('#grid_publists').datagrid('reload', {
		draftwbsID: draftwbsID,
		pubwbsID: pubwbsID
	});
}
$('#pubwindowupdate').linkbutton({
	onClick: function () {
		updateviewwindow();
	}
});
$('#divtree').tree({//系统子项树
	checkbox:true,
	dnd:true,
	cascadeCheck:false,
	formatter:function(node){
		var txt = node.name;
		if(node.level!="root") txt = node.code+" "+txt;
		if(dual) txt = txt+" | "+node.dualName;
		var childrenNum = 0;
		if(node.children) childrenNum = node.children.length;
		if(node.spechildren) childrenNum = node.spechildren.length;
		if(node.status == "失效"){
			var style = "background-color:lightgray;color:#fff";
		}else if(childrenNum == 0){
			var style = "color:red";
		}else var style = "";
		txt = "<span style='"+style+"'>"+txt+" - "+childrenNum+"</span>";
		//if(node.spechildren)
			txt += "&nbsp;<a id='gospe"+node.id+"'></a>";
		return txt;
	},
	onSelect:function(node){
		_uncheckGrid('#spegrid');
/* 			if(node.checked){
			$('#divtree').tree('uncheck',node.target);
		}else $('#divtree').tree('check',node.target); */
		$('a[id^="gospe"]').hide();
		$('#gospe'+currDiv).show();
		if ($(subitemEdting).is(':hidden')) {//非编辑模式下、当前专业列表非当前子项时可切换专业列表
			$('#gospe'+node.id).linkbutton({
				onClick:function(){
					_enterNode(node.id);
				}
			}).show();
			if(node.id!=currDiv)
				$('#gospe'+node.id).linkbutton('enable').html('&nbsp;进入&nbsp;');
			else $('#gospe'+node.id).linkbutton('disable').html('&nbsp;已进入&nbsp;');
		}
	},
	onDblClick:function(node){
		if(node.id!=currDiv && $(subitemEdting).is(':hidden')){
			_enterNode(node.id);
			$('#divtree').tree('select', node.target);
		}
	},
	onLoadSuccess: function (node, data) {
		$('#speformdiv').window('close');
		if ($('#subitem_toolbar_save').linkbutton('options').disabled) {
			subitem_startcodes = [];
			_getObjstartcode(data[0].children);
		}
	},
	onCheck:function(node){
		_uncheckGrid('#spegrid');
		if(node.checked){
			$(node.target).find("span.tree-title").css({"background-color":"#0e2d5f","color":"#fff"});
		}else $(node.target).find("span.tree-title").removeAttr("style");
	},
	onBeforeDrag: function (node) {//修改、签入状态禁用拖拽
		if (node.level == "root" || !$('#subitem_toolbar_checkout').is(":hidden") || _isDefaultNode(node)) return false;
	},
	onBeforeDrop:function(target, source, point) {
		targetNode = $('#divtree').tree('getNode',target);
		if(!((point == "append" && (source.level == "div"&&targetNode.level=="sys"&&source.parentId != targetNode.id))
			||(((point == "top")||(point == "bottom"))&&source.level == targetNode.level)) || _isDefaultNode(targetNode)){
				return false;
		}else _setTreeEditing();
	},
	onDrop:function(target, source, point){
		if(point == "append"){
			var pId = $('#divtree').tree('getNode',target).id;
		}else var pId = $('#divtree').tree('getParent',target).id;
		$('#divtree').tree('update', {target: $('#divtree').tree('find',source.id).target, parentId:pId});
		$('#gospe'+currDiv).linkbutton({disabled:true}).html('&nbsp;已进入&nbsp;');
		_recheck();
	},
	onContextMenu: function(e, node){
		if (node != null  && $('#subitem_toolbar_checkout').is(":hidden")) {
			$('#divtree').tree('select', node.target);
			subitemContextRow = node;
			var item = $('#subitem_grid_ContextMenu').menu('findItem', '设置起始代号');
			if ($(subitemEdting).is(':visible') || _isDefaultNode(node)){
				$('#subitem_grid_ContextMenu').menu('disableItem', item.target);
			}else{
				$('#subitem_grid_ContextMenu').menu('enableItem', item.target);
			};
			e.preventDefault(); //阻止浏览器捕获右键事件
			$('#subitem_grid_ContextMenu').menu('show', {//显示右键菜单
				left: e.pageX,//在鼠标点击处显示菜单
				top: e.pageY
			});
			e.preventDefault();  //阻止浏览器自带的右键菜单弹出
		}
	}
});
_reloadtree();
function _recheck(){//重置check效果
	var nodes = $('#divtree').tree('getChecked');
	for(var i=0;i<nodes.length;i++){
		if(nodes[i].status != "失效")
			$(nodes[i].target).find("span.tree-title").css({"background-color":"#0e2d5f","color":"#fff"});
	}
}
function _getTargetIds(level,statArr,noMsg,noDefault){//获取勾选节点中匹配目标节点层级的id数组，editId为全局变量
	var levelName = "节点";
	if(level){
		switch(level){
			case "sys":
				levelName = "系统";
				break;
			case "div":
				levelName = "子项";
				break;
			case "spe":
				levelName = "专业";
		}
	}
	var count = 0;
	var Ids = [];
	var nodes = $('#divtree').tree('getChecked');
	if(nodes.length>0){
		for(var i=0;i<nodes.length;i++){
			if(level){
				if(level=="spe" && nodes[i].spechildren){//专业层级则匹配专业父节点
					for(var j=0;j<nodes[i].spechildren.length;j++){
						if($.inArray(nodes[i].spechildren[j].status,statArr)>-1){
							count++;
							var ishave = false;
							for(var k=0;k<Ids.length;k++){
								if(Ids[k].pId==nodes[i].id){
									Ids[k].spe[Ids[k].spe.length] = nodes[i].spechildren[j].id;
									Ids[k].name[Ids[k].name.length] = nodes[i].spechildren[j].name;
									ishave = true;
									break;
								}
							}
							if(!ishave){
								Ids[Ids.length] = {};
								Ids[Ids.length-1]["pId"] = nodes[i].id;
								Ids[Ids.length-1]["spe"] =[nodes[i].spechildren[j].id];
								Ids[Ids.length-1]["name"] =[nodes[i].spechildren[j].name];
							}
						}
					}
				}else if(nodes[i].level==level && $.inArray(nodes[i].status,statArr)>-1){
					if((noDefault && !_isDefaultNode(nodes[i]))||!noDefault){
						Ids[Ids.length] = nodes[i].id;
						count++;
					}
				}
			}else if($.inArray(nodes[i].status, statArr)>-1 && nodes[i].level!="root"){
				if((noDefault && !_isDefaultNode(nodes[i]))||!noDefault){
					Ids[Ids.length] = nodes[i].id;
					count++;
				}
			}
		}
		if(Ids.length>0) return {"treeOrGrid":"tree","treeIds":Ids,"count":count};
	}else{
		var rows = $('#spegrid').datagrid('getChecked');
		if(rows.length>0){
			for(var i=0;i<rows.length;i++){
				if(level){
					if(rows[i].level==level && $.inArray(rows[i].status,statArr)>-1){
						if((noDefault && !_isDefaultNode(rows[i]))||!noDefault){
							count++;
							Ids[Ids.length] = rows[i].id;
						}
					}
				}else if($.inArray(rows[i].status,statArr)>-1){
					if((noDefault && !_isDefaultNode(rows[i]))||!noDefault){
						count++;
						Ids[Ids.length] = rows[i].id;
					}
				}
			}
			if(Ids.length>0) return {"treeOrGrid":"grid","count":count,"Ids":Ids};
		}
	}
	if(Ids.length==0){
		var statDescr = "";
		for(var i=0;i<statArr.length;i++){
			if(i==0){
				statDescr += statArr[i]+"”";
			}else statDescr += "、“"+statArr[i]+"”";
		}
		if(noDefault) statDescr += "，“默认系统”、“默认子项”不可选";
		if(!noMsg) $.messager.alert('操作提示', '请勾选目标'+levelName+'，状态为“'+statDescr+'。', 'error');
		return false;
	}
}
for(var i=0;i<nameTextbox.length;i++){
	$('#'+nameTextbox[i]).click(function () {
		if(!document.getElementById(this.id).readOnly) _tbxEditing();
	});
}
function _tbxEditing(){//名称文本框编辑模式
	if ($(subitemNoEdting).is(':visible')) {
		for(var i=0;i<nameTextCancel.length;i++){
			nameTextCancel[i] = $('#'+nameTextbox[i]).val();
		}
		$('#divtree').tree('disableDnd');
		$(subitemNoEdting+',a[id^="gospe"]').hide();
		$(subitemEdting+',#gospe'+currDiv).show();
	}
}
function _setTreeEditing(){//树编辑模式并缓存树
	if ($(subitemNoEdting).is(':visible')) {//记录当前撤销缓存
		treeCancelData = JSON.stringify($('#divtree').tree('getRoots'));
		$(subitemEdting).show();
		recode = true;
		$(subitemNoEdting).hide();
		_toggleTextbox("lock");
	}
}
function _setEditing(Ids, treeOrGrid){//列表行编辑模式
	_setCancelEditing(treeOrGrid);
	_setGridEditing(Ids);
}
function _setCancelEditing(treeOrGrid){//列表行缓存
	if ($(subitemNoEdting).is(':visible')) {
		_toggleTextbox("lock");
		$('#divtree').tree("disableDnd");
		$(subitemNoEdting+',a[id^="gospe"]').hide();
		$(subitemEdting+',#gospe'+currDiv).show();
		if(treeOrGrid == "tree"){
			treeCancelData = JSON.stringify($('#divtree').tree('getRoots'));
		}else{
			gridCancelData = JSON.stringify($('#spegrid').datagrid('getData'));
		}
	}
}
function _setGridEditing(Ids){//列表行编辑模式
	if(Ids.length>1){
		$('#loaddiv').window('open');//$('#spepage').panel('collapse');
		var i=0;
		var int=window.setInterval(function(){
			if(i<Ids.length){
				$('#loaddiv').panel('setTitle', Math.round(parseFloat(100*i/Ids.length))+'%已处理，请稍候...');
				_setSingleRow(Ids[i]);
				i++;
			}else{
				int=window.clearInterval(int);//$('#spepage').panel('expand');
				$('#loaddiv').window('close');
			}
		},0);
	}else if(Ids.length==1) _setSingleRow(Ids[0]);
}
function _setSingleRow(Id){
	$('#spegrid').datagrid('beginEdit', Id);
	var sdateEditor = $('#spegrid').datagrid('getEditor', {index:Id,field:'startDate'});
	var edateEditor = $('#spegrid').datagrid('getEditor', {index:Id,field:'endDate'});
	var workCoefEditor = $('#spegrid').datagrid('getEditor', {index:Id,field:'workCoef'});
	var level = $('#spegrid').datagrid('getData').rows[Id].level;
	var code = $('#spegrid').datagrid('getData').rows[Id].code;
	if(level=="spe"){
		$(sdateEditor.target).datebox('calendar').calendar({
			validator: function (date) {
				return prjStartDate.stringToDate() <= date && date <= prjEndDate.stringToDate();
			}
		});
		$(edateEditor.target).datebox('calendar').calendar({
			validator: function (date) {
				return prjStartDate.stringToDate() <= date && date <= prjEndDate.stringToDate();
			}
		});
		var nameEditor = $('#spegrid').datagrid('getEditor', {index:Id,field:'name'});
		var speArr = JSON.parse(speData);
		for(var j=0;j<speArr.length;j++){
			if("2"+code==speArr[j].industryCode){
				$(nameEditor.target).textbox("setValue", speArr[j].name);
				break;
			}
		}
		$(nameEditor.target).textbox('disable');
		if($(workCoefEditor.target).numberbox('getValue')=="") $(workCoefEditor.target).numberbox('setValue', 1);
	}else{
		$(sdateEditor.target).combo("disable");
		$(edateEditor.target).combo("disable");
		$(workCoefEditor.target).numberbox("disable");
	}
}
$('#spegrid').datagrid({//分子项专业列表
	idField: 'id',
	autoRowHeight:false,
	rownumbers: true,
	fit: true,toolbar:'#spegrid_toolbar',
	border:false,
	checkbox: true,
	columns: [[{field:'ck',checkbox:true },
		{ field: 'code', title: '节点代号', align: 'left', halign: 'center', formatter:function(value,row,index){
			var iconsrc = {"sys":"sysbr.png","div":"subbr.png","spe":"probr.png"};
			return '<img src="../../themes/icons/'+iconsrc[row.level]+'" /> '+value;
			} },
		{ field: 'name', title: '节点名称', align: 'left', halign: 'center', editor: { type: 'textbox', options: { required: true } } },
		{ field: 'startDate', title: '计划起始日期', width: 95, fixed: true, editor: { type: 'datebox', options: { required: true } } },
		{ field: 'endDate', title: '计划完成日期', width: 95, fixed: true, editor: { type: 'datebox', options: { required: true } } },
		{ field: 'workCoef', title: '工作量系数', hidden:true, editor: { type: 'numberbox', options: { required: true,max: 1, min: 0, precision: 2, value: 1 } } },
		{
			field: 'status', title: '状态',
			styler: function (value, row, index) {
				if (row.status == "失效") {
					return 'background-color:lightgray;';
				}
			}
		},
		{ field: 'src', title: '节点来源', width: 180, hidden:true },
		{ field: 'dualName', title: '双语名称', width: 70, hidden:true, editor: { type: 'validatebox', options: { required: false, } } }
	]],
	onLoadSuccess:function(){
		if (dual)	$('#spegrid').datagrid('showColumn','dualName');
		if (headOrbranch != "head")	$('#spegrid').datagrid('showColumn','workCoef');
		if ($('#driftshowall').is(":checked")) $('#spegrid').datagrid('showColumn','src');
	},
	onDblClickRow:function(index, row){
		if(!((!document.getElementById("tbxdivname").readOnly || treeCancelData!='treeCancelData') && $(subitemEdting).is(':visible')) &&
		$.inArray(index, editId)<0 && row.status != "失效" && $('#subitem_toolbar_checkout').is(":hidden") && !_isDefaultNode(row)){
			editId[editId.length]=index;
			_setEditing([index], "grid");
		}
	},
	onSelect:function(){
		_uncheckTree('#divtree');
	},
	onCheck:function(){
		_uncheckTree('#divtree');
	}
});
//是否显示节点来源
$("#driftshowall").prop("checked", false).change(_driftshowsrc);
function _driftshowsrc() {
	if ($("#driftshowall").is(':checked')) {
		$('#spegrid').datagrid("showColumn", "src");
	}else $('#spegrid').datagrid("hideColumn", "src");
}
//是否分子项管理
$("#driftchecksub").change(function () {
	if ($("#driftchecksub").is(':checked')) {
		var mode= "分子项";
	}else var mode="不分子项";
	$.messager.confirm('操作提示', "即将放弃所有未保存的编辑并切换到"+mode+"管理模式，请确认是否继续？", function (r) {
		if (r) {
			$.ajax({
				type: 'GET',
				url: updateDivision+"?draftwbsID="+draftwbsID+"&phaseID="+prjPhaseID+"&division="+$("#driftchecksub").is(':checked')+"&userID="+userID,
				success: function (data) {
					if (data.saveWork == 'done') {
						_reloadtree();
						$.messager.show({ title: '提示', msg: '切换成功。' });
					}else $.messager.alert("切换失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
				}
			});
		}else $("#driftchecksub").prop("checked",!$("#driftchecksub").is(':checked'));
	});
});

//子项表格右键菜单
$('#subitem_grid_ContextMenu').menu({ noline: true }).menu('appendItem', {
	text: '全选子节点',
	onclick: function () {
		var treeNodes = $('#divtree').tree('getChildren',subitemContextRow.target);
		for(var j=0;j<treeNodes.length;j++){
			$('#divtree').tree('check',treeNodes[j].target);
		}
	}
}).menu('appendItem', {
	text: '清空选择子节点',
	onclick: function () {
		var treeNodes = $('#divtree').tree('getChildren',subitemContextRow.target);
		for(var j=0;j<treeNodes.length;j++){
			$('#divtree').tree('uncheck',treeNodes[j].target);
		}
	}
}).menu('appendItem', {
	text: '设置起始代号',
	onclick: function () {
		//console.log($('#divtree').tree('getParent',subitemContextRow.target));
		if (subitemContextRow.level != "sys" && subitemContextRow.level != "div") {
			$.messager.alert("操作提示", "请先选择需要设置的系统和子项。", "error");
			return;
		} else {
			for(var i = 0;i<subitem_startcodes.length;i++){//匹配暂存数组中是否存在记录
				if($('#divtree').tree('getParent',subitemContextRow.target).id == subitem_startcodes[i].pid){
					$('#startnumber').spinner('setValue', subitem_startcodes[i].start);
					var isupdate = true;
					break;
				}
			}
			if(!isupdate) $('#startnumber').spinner('setValue', 1);//未匹配到则默认取1
			var childrenNodes = $('#divtree').tree('getChildren', $('#divtree').tree('getParent',subitemContextRow.target).target);
			for(var i=0;i<childrenNodes.length;i++){
				if(childrenNodes[i].level==subitemContextRow.level && !_isDefaultNode(childrenNodes[i])){
					var firstNode = childrenNodes[i];
					break;
				}
			}
			if(subitemContextRow.level == "sys"){
				$('#startcodetxt').html('当前父节点下首个非默认系统为：<br>'+firstNode.code+" "+firstNode.name+"<br>");
			}else $('#startcodetxt').html('当前父节点下首个非默认子项为：<br>'+firstNode.code+" "+firstNode.name+"<br>");
			$('#startformdiv').window('open');
		}
	}
}).menu('appendItem', {
	text: '全选所有节点',
	onclick: function () {
		var treeRoot = $('#divtree').tree('getRoots');
		for(var i=0;i<treeRoot.length;i++){
			var treeNodes = $('#divtree').tree('getChildren',treeRoot[i].target);
			for(var j=0;j<treeNodes.length;j++){
				$('#divtree').tree('check',treeNodes[j].target);
			}
		}
	}
}).menu('appendItem', {
	text: '清空选择所有节点',
	onclick: function () {
		var treeRoot = $('#divtree').tree('getRoots');
		for(var i=0;i<treeRoot.length;i++){
			var treeNodes = $('#divtree').tree('getChildren',treeRoot[i].target);
			for(var j=0;j<treeNodes.length;j++){
				$('#divtree').tree('uncheck',treeNodes[j].target);
			}
		}
	}
});
//设置起始编号
$('#btnstartapply').linkbutton({
	onClick: function () {
		var start = $('#startnumber').numberspinner('getValue');
		var isupdate = false;
		for (var i = 0; i < subitem_startcodes.length; i++) {
			if (subitem_startcodes[i].pid == $('#divtree').tree('getParent',subitemContextRow.target).id) {
				var recovercode = subitem_startcodes[i].start;
				subitem_startcodes[i].start = start;
				isupdate = true;
				var splice_i = i;
				break;
			}
		}
		if(!isupdate){//未匹配到则增加此节点起始号
			subitem_startcodes[subitem_startcodes.length] = { pid: $('#divtree').tree('getParent',subitemContextRow.target).id, start: start };
			var splice_i = subitem_startcodes.length - 1;
		} 
		var validcode = _AutoSubitemCode("nostartend");
		if(validcode){
			if(currDiv) _updateSpeTitle();
			_enterNode(currDiv);
			$('#startformdiv').window('close');
			$('#subitem_toolbar_save').linkbutton('enable');
		}else{//未通过校验则维持窗口，撤销编辑
			if(isupdate){//有匹配过则用暂存值还原
				subitem_startcodes[splice_i].start = recovercode;
			}else subitem_startcodes.splice(splice_i,1);//未匹配过但增加过则去掉此新增号
		} 
	}
});

function _initialSubitemVals() {//记录系统子项表初始参照值
	var keyArr = ["projElementId","id","level","code","name","startDate","endDate","dualName","workCoef","status","srcStru","srcID","src","srcRow","parentId"];//记录id、表格显示的字段
	var data = $('#divtree').tree('getRoots');
	var obj = [];
	_getArr(data);
	return obj;
	function _getArr(input){
		for(var i=0;i<input.length;i++){
			obj[obj.length] = {};
			for(var j=0;j<keyArr.length;j++){
				for(var key in input[i]){
					if(key==keyArr[j]){
						obj[obj.length-1][keyArr[j]]=input[i][keyArr[j]];
						break;
					}
				}
			}
			var key = input[i].spechildren ? "spechildren" : "children";
			if(input[i][key]) _getArr(input[i][key], obj);
		}
	}
}
function _startenddate(treeData){
	if(division){//更新起止日期
		var nodeArr = [];
		_findNodes(treeData);
		for(var i=0;i<nodeArr.length;i++){
			var temp_sDate = "";
			var temp_eDate = "";
			for(var key in nodeArr[i]){
				if(key=="children" || key=="spechildren") _findDate(nodeArr[i][key]);
			}
			$('#divtree').tree('update',{target:$('#divtree').tree('find',nodeArr[i].id).target,startDate:temp_sDate,endDate:temp_eDate});
		}
	}
	function _findNodes(Nodes){//递归获取需要更新的节点
		for(var i=0;i<Nodes.length;i++){
			for(var key in Nodes[i]){
				if((key=="children"||key=="spechildren") && Nodes[i][key].length>0){
					nodeArr[nodeArr.length]=Nodes[i];
					_findNodes(Nodes[i][key]);
				}
			}
			
		}
	}
	function _findDate(Nodes){//递归获取起止日期
		for(var i=0;i<Nodes.length;i++){
			if(Nodes[i].children && Nodes[i].children.length>0){//先检查有无子节点，如有则直接下沉到子节点
				_findDate(Nodes[i].children);
			}else if(Nodes[i].spechildren && Nodes[i].spechildren.length>0){
				_findDate(Nodes[i].spechildren);
			}else{
				if(Nodes[i].startDate && (temp_sDate=="" || temp_sDate>Nodes[i].startDate)){
					sNode = Nodes[i].code+Nodes[i].name; temp_sDate = Nodes[i].startDate;
				}
				if(Nodes[i].endDate && (temp_eDate=="" || temp_eDate<Nodes[i].endDate)){
					eNode = Nodes[i].code+Nodes[i].name; temp_eDate = Nodes[i].endDate;
				}
			}
		}
	}
}

function _AutoSubitemCode(nostartend) {//自动依排序生成新的系统子项号、含校验是否超限
	var treeRoot = $('#divtree').tree('getRoots')[0];
	if (!treeRoot.children) return true;
	var treeData = eval(JSON.stringify(treeRoot.children));
	if(_isDefaultNode(treeData[0])) treeData.splice(0,1);
	var codeArr = new Object();
	var errortxt = '';
	var addcheck = _autoSubitemCodeRow(treeData, true);
	if (!addcheck) {
		$.messager.alert("操作提示", errortxt+"所更新编号过大，操作不可继续。", "error");
		return false;
	}else{
		for(var key in codeArr){
			$('#divtree').tree('update', {target: $('#divtree').tree('find', key).target, code:codeArr[key]});
		}
		if(!nostartend) _startenddate($('#divtree').tree('getRoots')[0].children);
		$('#gospe'+currDiv).linkbutton({disabled:true}).html('&nbsp;已进入&nbsp;');
		return true;
	}
	function _autoSubitemCodeRow(Obj, addtag) {//递归生成Code行数组
		for(var i=0;i<Obj.length;i++){
			if(!addtag) break;
			var start = 1;
			for(var j=0;j<subitem_startcodes.length;j++){//Obj[i].parentId
				if(subitem_startcodes[j].pid == $('#divtree').tree('getParent',$('#divtree').tree('find',Obj[i].id).target).id) {
					start = parseInt(subitem_startcodes[j].start);
					break;
				}
			}
			if(Obj[i].level=="sys"){
				if(i+start<10){
					codeArr[Obj[i].id] = "0"+(i+start);
				}else if(i+start<100){
					codeArr[Obj[i].id] = ""+(i+start);
				}else{
					errortxt = levelText[Obj[i].level]+" "+Obj[i].code+" "+Obj[i].name+" ";
					addtag = false;
				}
			}else if(Obj[i].level=="div"){
				if(headOrbranch=="head"){
					pCode = codeArr[Obj[i].parentId];
				}else{
					pCode = "01";
				}
				if(i+start<10){
					codeArr[Obj[i].id] = pCode+"0"+(i+start);
				}else if(i+start<100){
					codeArr[Obj[i].id] = pCode+(i+start);
				}else{
					errortxt = levelText[Obj[i].level]+" "+Obj[i].code+" "+Obj[i].name+" ";
					addtag = false;
				}
			}
			if(Obj[i].children) _autoSubitemCodeRow(Obj[i].children, addtag);
		}
		return addtag;
	}
}
function _searchSubitemInDB(inputname, combobox, list, prjid) {//查询系统子项库
	var arrname = _getInputKeyword(inputname);
	$('#' + list).datalist('load', {
		namekeyword: arrname,
		indu: combobox,
		prj: prjid,
		query: true
	});
}
function _getInputKeyword(boxtext) {//获取输入框内容数组
	var result = [];
	if (boxtext != "") {
		var tempkeyword = boxtext.split(" ");
		for (var i = 0; i < tempkeyword.length; i++) {
			if (tempkeyword[i] != "") {
				result[result.length] = tempkeyword[i];
			}
		}
	}
	return result;
}
function _getObjValArr(Obj, result, allkey) {//递归获取树形数据对象内各字段值数组
	if (Obj) {
		for (var i = 0; i < Obj.length; i++) {
			if (allkey == undefined) {
				result[result.length] = { "id": Obj[i].id, "code": Obj[i].code, "name": Obj[i].name, "_parentId": Obj[i]._parentId };
			} else {
				result[result.length] = {
					"id": Obj[i].id, "code": Obj[i].code, "name": Obj[i].name, "iconCls": Obj[i].iconCls, "_parentId": Obj[i]._parentId, "state": Obj[i].state, "status": Obj[i].status, "startDate": Obj[i].startDate, "endDate": Obj[i].endDate, "dualName": Obj[i].dualName
				, "src": Obj[i].src, "srcID": Obj[i].srcID, "srcRow": Obj[i].srcRow, "srcStru": Obj[i].srcStru, "level": Obj[i].level, "workCoef": Obj[i].workCoef
				};
			}
			if (Obj[i].children) {
				_getObjValArr(Obj[i].children, result, allkey);
			}
		}
	}
}
function _getObjstartcode(Obj) {//递归初始化起始编号数组
	if (Obj) {
		for (var i = 0; i < Obj.length; i++) {
			var startcode = "1";
			if (Obj[i].level == "sys" && !_isDefaultNode(Obj[i])){
				startcode = parseInt(Obj[i].code);
				if (startcode.length == 0 || isNaN(startcode)) startcode = 1;//【对接修正161010
			}else if (Obj[i].level == "div" && !_isDefaultNode(Obj[i])) {
				startcode = parseInt(Obj[i].code.toString().substring(2, Obj[i].code.toString().length));
				if (startcode.length == 0 || isNaN(startcode)) startcode = 1;//【对接修正161010
			}
			var isupdate = false;
			for (var k = 0; k < subitem_startcodes.length; k++) {
				if (subitem_startcodes[k].pid == Obj[i].parentId) {
					if (subitem_startcodes[k].startcode > startcode) subitem_startcodes[k].start = startcode;
					isupdate = true;
					break;
				}
			}
			if (!isupdate && Obj[i].level != "spe"){
				subitem_startcodes[subitem_startcodes.length] = { pid: Obj[i].parentId, start: startcode };}
			if (Obj[i].children && Obj[i].level != "spe") {
				_getObjstartcode(Obj[i].children);
			}
		}
	}
}

function _saveWBS(){//保存WBS数据
	var result = {"addRows":[],"updateRows":[],"deleteRows":[],
		"divisin":division,"draftwbsID":draftwbsID,"headOrbranch":headOrbranch,"phaseID":prjPhaseID,"projID":prjid};
	var before = JSON.parse(subitem_Vals);
	var curr = _initialSubitemVals();
	for(var i=0;i<before.length;i++){
		var deled = true;
		for(var j=0;j<curr.length;j++){
			if(before[i].id==curr[j].id){
				for(var key in curr[j]){
					if(before[i][key]!=curr[j][key]){
						result.updateRows[result.updateRows.length]=curr[j];
						break;
					}
				}
				curr.splice(j,1);
				deled = false;
				break;
			}
		}
		if(deled) result.deleteRows[result.deleteRows.length] = {"id":before[i].id, "projElementId":before[i].projElementId};
	}
	for(var i=0;i<curr.length;i++){
		result.addRows[result.addRows.length]=curr[i];
	}
	if(result.addRows.length==0 && result.deleteRows.length==0 && result.updateRows.length==0){
		$.messager.show({ title: '提示', msg: '无更改需要保存。' });
		return;
	}
	_reloadtree("noupdate");
	if($('#subitem_toolbar_save').is(':hidden')){
		$('#speformdiv').window('close');
		$.messager.alert("操作提示", "不可保存：请检查当前WBS签出状态。", "error");
		$('#wbs_layout').layout('expand', 'north');
	}else{
		$.ajax({
			type: 'POST',
			url: saveUrl+"?"+userPar+"&personID="+perID,
			contentType: "application/json",
			data: JSON.stringify(result),
			success: function (data) {
				$('#speformdiv').window('close');
				if (data.saveDraft == 'done') {
					_reloadtree();//重载未发布版详情、未发布版列表
					$.messager.show({ title: '提示', msg: '保存成功。' });
				}else $.messager.alert("保存失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
			}
		});
	}
}

function _getObjId(Obj, result) {//递归获取树形数据对象id字段数组
    for (var i = 0; i < Obj.length; i++) {
        result[result.length] = Obj[i].id;
        if (Obj[i].children) {
            _getObjId(Obj[i].children, result);
        }
    }
}

function _collapseSet(tr, level) {//实现展开节点到指定层级
	tr.tree('expandAll');
	if(level=="sys"){
		var treeRoot = $('#divtree').tree('getRoots');
		for(var i=0;i<treeRoot.length;i++){
			var treeNodes = $('#divtree').tree('getChildren',treeRoot[i].target);
			for(var j=0;j<treeNodes.length;j++){
				if(treeNodes[j].level == level) $('#divtree').tree('collapse',treeNodes[j].target);
			}
		}
	}
}
function _uncheckTree(tree){
	var nodes = $(tree).tree('getChecked');
	for(var i=0;i<nodes.length;i++){
		$(tree).tree('uncheck', nodes[i].target);
	}
	$(tree).find("div.tree-node-selected").removeClass("tree-node-selected");
}
function _uncheckGrid(grid){
	$(grid).datagrid('clearChecked');
	$(grid).datagrid('clearSelections');
}
function _enterNode(id,tree,grid){//进入节点详情
	if(tree && tree=='#pubtree'){
		pubId = id;
	}else if(tree && tree=='#histree'){
		hisId = id;
	}else currDiv = id;
	var treeId = tree ? tree : '#divtree';
	var node = $(treeId).tree('find', id);
	if(!node){
		node = $(treeId).tree('getRoots')[0];
		currDiv = node.id;
	}
	var key = node.spechildren ? "spechildren" : "children";
	if(node[key]){
		for(var i=0;i<node[key].length;i++){
			if(node[key][i].checked) node[key][i].checked = null;
		}
		var sNode = JSON.stringify(node[key]);
		var gridId = grid ? grid : '#spegrid';
		$(gridId).datagrid('clearChecked').datagrid('loadData',JSON.parse(sNode));
	}
	if(!tree && !grid){
		_updateSpeTitle();
		_updateTextbox();
		_toggleTextbox();
	}
}
function _updateSpeTitle(tree, page){
	if(tree=='#pubtree'){
		var id = pubId;
	}else if(tree=='#histree'){
		var id = hisId;
	}else var id = currDiv;
	var treeId = tree ? tree : '#divtree';
	var newCode = $(treeId).tree('find',id).code;
	var newName = $(treeId).tree('find',id).name;
	var nodeStatus = $(treeId).tree('find',id).status;
	var pageId = page ? page : '#spepage';
	switch($(treeId).tree('find',id).level){
		case "root":
			var title = "阶段详情 - "+newName;
			break;
		case "sys":
			var title = '<img src="../../themes/icons/sysbr.png" /> '+"系统详情 - "+newCode+" "+newName;
			if(nodeStatus) title += " - 状态："+nodeStatus;
			break;
		case "div":
			var title = '<img src="../../themes/icons/subbr.png" /> '+"子项详情 - "+newCode+" "+newName;
			if(nodeStatus) title += " - 状态："+nodeStatus;
	}
	$(pageId).panel('setTitle',title);
}
function _updateTextbox(){//更新文本框
	if(currDiv){
		var Node = $('#divtree').tree('find', currDiv);
		$('#tbxdivname').val(Node.name);
		$('#tbxdivsdate').val(Node.startDate);
		$('#tbxdivedate').val(Node.endDate);
		if(dual) $('#tbxdivdual').val(Node.dualName);//子项双语
	}else{
		$('#tbxdivname').val("");
		$('#tbxdivsdate').val("");
		$('#tbxdivedate').val("");
		if(dual) $('#tbxdivdual').val("");//子项双语
	}
}
function _toggleTextbox(type){//名称文本框锁定解锁lock/unlock
	for(var i=0;i<nameTextbox.length;i++){
		if((currDiv!=null && ($('#divtree').tree('find', currDiv).level == "root" 
			|| $('#divtree').tree('find', currDiv).status == "失效" || _isDefaultNode($('#divtree').tree('find', currDiv))))
			|| type || currDiv == null || !$('#subitem_toolbar_checkout').is(":hidden")){
			$('#'+nameTextbox[i]).css('background-color','#F0F0F0');
			document.getElementById(nameTextbox[i]).readOnly = true;
		}else{
			$('#'+nameTextbox[i]).css('background-color','#FFFFFF');
			document.getElementById(nameTextbox[i]).readOnly = false;
		}
	}
}
function _reObj(Obj, wbsDiv, result, otherPrj){//递归重构对象
	for(var i=0;i<Obj.length;i++){
		result[result.length] = {};
		for(var key in Obj[i]){
			if(key=="children"){
				/* if((!otherPrj && ((wbsDiv && Obj[i]["level"] == "div")||(!wbsDiv && Obj[i]["level"] != "spe")))||
					(otherPrj && Obj[i].children && Obj[i].children.length>0 && Obj[i].children[0].level=="spe")){ */
				if(Obj[i]["level"] == "div" || (Obj[i].children && Obj[i].children.length>0 && Obj[i].children[0].level=="spe") ||
					((!Obj[i].children || (Obj[i].children && Obj[i].children.length == 0)) && !wbsDiv && Obj[i]["level"] == "root")){
					result[result.length-1]["spechildren"] = Obj[i][key];
				}else result[result.length-1][key] = [];
			}else result[result.length-1][key] = Obj[i][key];
		}
		if(Obj[i].children && result[result.length-1].children){
			_reObj(Obj[i].children, wbsDiv, result[result.length-1].children, otherPrj);
		}
	}
}
$('#changelist').linkbutton({//查看项目策划变更记录
    plain: true,
	onClick: function () {
        window.open("productchangeview.html?projID=" + prjid + "&phaseID=" + prjPhaseID + "&userNum=" + userNum, "_blank");
        /*var opts = $("#grid_changelists").datagrid("options");
        opts.url = prjviewproductsdataurl;
        $('#grid_changelists').datagrid("reload", {
            projID: prjid,
            phaseID: prjPhaseID,
            userNum: userNum
        });
        $('#divchangviewwindow').window('open');*/
    }
}).html('&nbsp;图纸状态变更记录&nbsp;');
function _reloadtree(noupdate){//加载系统子项树
	$('#speformdiv').window('open');
	$.ajax({
		url: prjbasicdataurl + "?projID=" + prjid + "&phaseID=" + prjPhaseID+"&"+userPar,
		type: "GET",
		async:false,
		success: function (data) {
			try {
				dataprj = eval(data)["data"];
				var wbsStat = eval(data)["returnStatus"];
				var datamsg = eval(data)["msgData"];
			}
			catch (e) {
				dataprj = eval("(" + data + ")")["data"];
				var wbsStat = eval("(" + data + ")")["returnStatus"];
				var datamsg = eval("(" + data + ")")["msgData"];
			}
			if(wbsStat!="S"){
				alert("请完成该项目的综合管理操作："+datamsg);
				window.close();
			}else if(wbsStat=="S"){
				if(pubLoaded) $('#pub_grid').datagrid('reload');//发布版列表更新
				prjName = dataprj.projName;
				prjPhase = dataprj.phaseName;
				prjManagerName = dataprj.mgr;
				if(dataprj.checkoutable) isPM = true;
				prjStartDate = dataprj.phaseStart;
				prjEndDate = dataprj.phaseEnd;
				draftwbsID = dataprj.draftwbsID; //草稿版id
				pubwbsID = dataprj.pubwbsID;
				division = dataprj.division;
				headOrbranch = dataprj.headOrbranch;
				draftwbsCheckout = dataprj.draftwbsCheckout;
				dual = dataprj.dual; 
				prjNumber = dataprj.projNumber;
				SubItemControl();
				$.ajax({
					url: prjdriftdataurl+"?draftwbsID="+draftwbsID+"&prjPhaseID="+prjPhaseID+"&projID="+prjid+"&userID="+userID,
					type: "GET",
					async:false,
					success: function (data) {
						$('#prjdrftgrid').datagrid('loadData',data);
						$(subitemEdting).hide();
						$("#subitem_toolbar_checkout").linkbutton('enable').show();
						$("#driftchecksub").attr('disabled',true);
						if (data && data[0] && data[0].checkoutId && perID == ""+draftwbsCheckout){
							//根据初始状态,设置签出签入等按钮显示
							$(subitemNoEdting).show();
							$("#subitem_toolbar_checkout").hide();
							if(!oldPhase) $("#driftchecksub").removeAttr("disabled");
						}else if (!isPM ||(data && data[0] && data[0].checkoutId && data[0].checkoutId !="" && perID != ""+draftwbsCheckout)){
							$(subitemNoEdting).hide();
							$("#subitem_toolbar_checkout").linkbutton('disable');
						}else $(subitemNoEdting).hide();
					}
				});
				if(!noupdate){
					$.ajax({
						url:prjdrifwbsdataurl+"?prjPhaseID="+prjPhaseID+"&draftwbsID="+draftwbsID,
						type:"GET",
						success:function(data){
							var wbsDiv = true;
							if(data.divison!=undefined) wbsDiv = data.divison;
							var reData = [];
							_reObj(data.paDetailsDTO, wbsDiv, reData);
							$('#divtree').tree('loadData',reData);
							$('#subitem_toolbar_save').linkbutton('disable');
							subitem_Vals = JSON.stringify(_initialSubitemVals());
							if(!dual) $('#dualname').hide();
							var ids = ['tbxdivsdate','tbxdivedate'];
							var vals = ['', ''];
							for(var i=0;i<ids.length;i++){
								$('#'+ids[i]).css('background-color','#F0F0F0').val(vals[i]);//系统子项阶段日期置灰只读
								document.getElementById(ids[i]).readOnly = true;
							}
							if(!currDiv) currDiv = reData[0].id;
							_enterNode(currDiv);
							$('#divtree').tree('select',$('#divtree').tree('find',currDiv).target);
						}
					});
				}
			}
		}
	});
}
function _isDefaultNode(node){
	if((node.code=="00"&&node.name=="默认系统")||(node.code=="0000"&&node.name=="默认子项")){
		return true;
	}else return false;
}