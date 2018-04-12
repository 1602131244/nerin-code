var OAurl = "192.168.15.198";
var minHour = 0;
var alertminHour = 7;//冬令时
var maxHour = 10.5;
var tempAddIds = [];
var batch = false;
var calbuttons = $.extend([], $.fn.datebox.defaults.buttons);
calbuttons.splice(1, 0, {
	text: '今天',
	handler: function(target){
		var today = new Date();
		$('#'+target.id).datebox('calendar').parent().parent().panel('close');
		$('#'+target.id).datebox('setValue',DateToString(today).strYear+"-"+DateToString(today).strMon+"-"+DateToString(today).strDate);
	}
});
$('#dateswitch').datebox({
	currentText:null,
	buttons:calbuttons,    
    required:true
}).datebox('setValue',currYear+"-"+currMon+"-"+currDate).datebox('calendar').calendar({
	firstDay: 1,
	onChange:function(date,oldDate){//confirm、获取新一天工时、根据可见性更新任务、除hisgrid外loaded清零、是否切换周工时、获取新的周工时
		if(DateToString(date).full == currYear+currMon+currDate){
			return;
		}else{
			var ToReloadWeek = _reloadWeek(date,true);
			if (!$('#currdg_save').linkbutton('options').disabled && ToReloadWeek){
				$(this).datebox('setValue',currYear+"-"+currMon+"-"+currDate);
				$.messager.alert("操作提示", "不可切换日期：存在未保存的编辑，请先保存。<br>如果无需保存编辑，请执行重置后再次操作。", "error");
				return;
			}else{
				currYear = DateToString(date).strYear;
				currMon = DateToString(date).strMon;
				currDate = DateToString(date).strDate;
				if(ToReloadWeek){
					_loadWeek(date,'#currweekgrid');
				}else _reloadcurrgrid();
				_switchDate();
			}
		}
	}
});
$(".datebox :text").attr("readonly", "readonly");
function _reloadWeek(date,isCurr){
	var newCaldate = new Date(date.getFullYear(),date.getMonth(),date.getDate());
	newCaldate.setDate(newCaldate.getDate()-(newCaldate.getDay()==0? 6 : newCaldate.getDay()-1));
	var caldate = new Date(Number(isCurr?currYear:hisYear),(Number(isCurr?currMon:hisMon) - 1),Number(isCurr?currDate:hisDate));
	caldate.setDate(caldate.getDate()-(caldate.getDay()==0? 6 : caldate.getDay()-1));
	if(DateToString(newCaldate).full!=DateToString(caldate).full){
		var reloadWeek = true;
	}else var reloadWeek = false;
	return reloadWeek;
}
function _getDay(date,i){//获取所在周各个Day
	var mon = new Date(date.getFullYear(),date.getMonth(),date.getDate());
	mon.setDate(mon.getDate()-(mon.getDay()==0? 6 : mon.getDay()-1)+i);
	return DateToString(mon).strYear+"-"+DateToString(mon).strMon+"-"+DateToString(mon).strDate;
}
function _switchWeekDate(weekindex){
	$('#currweekgrid').datagrid('clearSelections');
	var datesplit = $('#currweekgrid').datagrid('getRows')[weekindex]['fulldate'].split('-');
	currYear = datesplit[0];
	currMon = datesplit[1];
	currDate = datesplit[2];
	$('#dateswitch').datebox('setValue',currYear+"-"+currMon+"-"+currDate);
	$('#currgrid').datagrid('loadData',$('#currweekgrid').datagrid('getRows')[weekindex].tasks);
	_switchDate();//切换了天
}
function _switchDate(){
	ontimeloaded = false;
	outtimeloaded = false;
	if($('#laysouth').is(":visible")){
		var selectedTab = $('#reftabs').tabs('getSelected');
		switch($('#reftabs').tabs('getTabIndex', selectedTab)){
			case 0:
				_ontimegrid();
				break;
			case 1:
				_outtimegrid();
		}
	}
}
function _switchHisWeek(weekindex){
	var datesplit = $('#hisweekgrid').datagrid('getRows')[weekindex]['fulldate'].split('-');
	hisYear = datesplit[0];
	hisMon = datesplit[1];
	hisDate = datesplit[2];
	$('#hisdate').datebox('setValue',hisYear+"-"+hisMon+"-"+hisDate);
	$('#hisgrid').datagrid('loadData',$('#hisweekgrid').datagrid('getRows')[weekindex].tasks);
}
function _reloadhisgrid(){
	var fulldatehis = hisYear + "-" +hisMon + "-" + hisDate;
	var weekrows = $('#hisweekgrid').datagrid('getRows');
	for(var i=0;i<weekrows.length;i++){
		if(weekrows[i].fulldate == fulldatehis){
			$('#hisgrid').datagrid('loadData',weekrows[i].tasks);
			break;
		}
	}
}
$('#weekrefbtn').menubutton({//导入
	iconCls: 'icon-redo',plain:false,
	hasDownArrow: false,menu:'#chooseref'
});
$("#reftask").click(function () {//任务
	if(!$('#laysouth').is(":visible")){
		$.messager.alert('操作提示', '请在我的任务选中将被导入的任务（可多选）。', 'warning');
		$("#rootlayout").layout('expand', 'south');
		return;
	}
	var selectedTab = $('#reftabs').tabs('getSelected');
	switch($('#reftabs').tabs('getTabIndex', selectedTab)){
		case 0:
			var srcRows = $('#ontimegrid').datagrid('getSelections');
			break;
		case 1:
			var srcRows = $('#outtimegrid').datagrid('getSelections');
			break;
		case 2:
			var srcRows = $('#hisgrid').datagrid('getSelections');
	}
	if(srcRows.length==0){
		$.messager.alert('操作提示', '请先选中将被导入的“我的任务”（可多选）。', 'error');
		return;
	}
	$(NoEditing).hide();
	$(Editing).show();
	$('#currgrid').datagrid('loadData',_copyObjArr(srcRows));
	$('#dateswitch').datebox({disabled:true}).datebox('setValue',currYear+"-"+currMon+"-"+currDate);
	batch = true;
	$('#batchmode').combobox('setValue', null);
	$('#batchdiv').show();
});
$("#refdate").click(function () {//工日
	var tgtrows = $('#currweekgrid').datagrid('getSelections');
	if(tgtrows.length==0){
		$.messager.alert('操作提示', '请先选中将导入到的当前目标日期（可多选）。', 'error');
		return;
	}
	if(!$('#hiscal').is(":visible")){
		$.messager.alert('操作提示', '请在历史任务的左侧栏目选中将被导入的历史工日（可多选）。', 'warning');
		if(!$('#laysouth').is(":visible")) $("#rootlayout").layout('expand', 'south');
		$('#reftabs').tabs('select', '历史任务');
		if(!$('#hiscal').is(":visible")) $("#hislayout").layout('expand', 'west');
		return;
	}
	var srcrows = $('#hisweekgrid').datagrid('getSelections');
	if(srcrows.length==0){
		$.messager.alert('操作提示', '请先选中将被导入的历史工日（可多选）。', 'error');
		return;
	}
	$.messager.confirm('操作提示', '注意：将导入' + tgtrows.length + '个工日，原有任务将被覆盖，是否确定继续？', function (r) {
		if (r) {
			for(var i=0;i<tgtrows.length;i++){
				var tgtindex = $('#currweekgrid').datagrid('getRowIndex', tgtrows[i].id);
				var j = i - parseInt(i/srcrows.length)*srcrows.length;
				var rowdata = {"id":tgtrows[i].id,"fulldate":tgtrows[i].fulldate,"totalHour":srcrows[j].totalHour};
				rowdata["tasks"] = _copyObjArr(srcrows[j].tasks);
				$('#currweekgrid').datagrid('updateRow',{
					index: tgtindex,
					row: rowdata
				});
			}//重载当天
			_reloadcurrgrid();
			$('#currdg_save').linkbutton('enable');
		}
	}); 
});
$('#batchmode').combobox({   //导入模式 
	data:[
		{"value":0,"text":"覆盖"},
		{"value":1,"text":"追加"}],
	hasDownArrow:true,
	editable:false,
	required:true,
	panelHeight: 'auto',
	valueField:'value',
	textField:'text',
	onLoadSuccess:function(){
		$(this).combobox('setValue', null);
	}
});
function _copyObjArr(arr){
	var result = [];
	for(var i=0;i<arr.length;i++){
		result[i] = {};
		for(var key in arr[i]) result[i][key] = arr[i][key];
	}
	return result;
}
function _copyObj(obj,withoutkey){
	var result = {};
	for(var key in obj){
		if(!withoutkey || key != withoutkey) result[key] = obj[key];
	}
	return result;
}
$('#currweekgrid').datagrid({//当前周工时
	method: 'get',
	idField: 'id',
	singleSelect: false, pagination: false,
	fit: true,toolbar:'#currweekdgtb',border:false,striped:true,
	onLoadSuccess:function(data){
		$(this).datagrid('clearSelections');
		if(data.applied == "N"){
			var applied = "未锁定 - 可修改";
			$('#currdg_add,#currdg_del,#currdg_edit').linkbutton('enable');
			$('#weekrefbtn').linkbutton('enable');
		}else{
			var applied = "已锁定 - 不可修改";
			$('#currdg_add,#currdg_del,#currdg_edit').linkbutton('disable');
			$('#weekrefbtn').linkbutton('disable');
		}
		$('#currcal').panel({ title: applied});
		_reloadcurrgrid();//不涉及切换天
	},
	columns: [[
		{ field: 'ck', checkbox: true },
		{ field: 'id', title: '星期', formatter:function(value,row,index){
			if(row.fulldate == currYear+"-"+currMon+"-"+currDate){
				return _getTheDay(row.fulldate)+'<img src="../../themes/icons/back.png" />';
			}else return _getTheDay(row.fulldate);
			},styler:function(value,row,index){
				if(_getTheDay(row.fulldate)=="六"){
					return 'background-color:#99FF99';
				}else if(_getTheDay(row.fulldate)=="日"){
					return 'background-color:#FFCC99';
				}
			}},
		{ field: 'fulldate', title: '日期', width:80,align: 'center', halign: 'center', formatter:function(value,row,index){
			if(value != currYear+"-"+currMon+"-"+currDate && !$(Editing).is(":visible")){
				return "<a href='javascript:_switchWeekDate("+index+");'>"+value+"</a>";
			}else return value;
			}},
		{ field: 'totalHour', title: '已填工时',align: 'center', halign: 'center'}]]
});
function _loadWeek(date,weekgrid){
	$.ajax({
		url:'http://'+window.location.host+'/api/wh/getWhInfo?currDate='+_getDay(date,0)+'&perId='+perID,
		contentType : 'application/x-www-form-urlencoded',
		dataType: "json",
		type: "GET",
		success: function(weekdata) {//校验与补全
			var newweekrows = [];
			var newid = (weekdata.rows.length>0) ? (weekdata.rows[weekdata.rows.length-1].id+1) : 1;
			for(var i=0;i<7;i++){
				newweekrows[i] = {};
				newweekrows[i]["fulldate"] = _getDay(date,i);
				newweekrows[i]["tasks"] = [];
				var match = false;
				for(var j=0;j<weekdata.rows.length;j++){
					if(weekdata.rows[j].fulldate==newweekrows[i].fulldate){
						newweekrows[i]["id"] = weekdata.rows[j].id;
						newweekrows[i]["totalHour"] = weekdata.rows[j].totalHour;
						if(weekdata.rows[j].tasks && weekdata.rows[j].tasks.length>0){
							for(var k=0;k<weekdata.rows[j].tasks.length;k++){
								newweekrows[i].tasks[k] = {};
								for(var key in weekdata.rows[j].tasks[k]) newweekrows[i].tasks[k][key] = weekdata.rows[j].tasks[k][key];
							}
						}
						match = true;
						break;
					}
				}
				if(!match){
					newweekrows[i]["id"] = newid+i;
					newweekrows[i]["totalHour"] = 0;
				}
			}
			$(weekgrid).datagrid('loadData',{"applied":weekdata.applied,"rows":newweekrows});
		}
	});
}
_loadWeek(myDate,'#currweekgrid');
function _reloadcurrgrid(){//不涉及切换天
	var fulldatecurr = currYear + "-" +currMon + "-" + currDate;
	var weekrows = $('#currweekgrid').datagrid('getRows');
	for(var i=0;i<weekrows.length;i++){
		if(weekrows[i].fulldate == fulldatecurr){
			$('#currgrid').datagrid('loadData',weekrows[i].tasks);
			break;
		}
	}
}
$('#currdg_add').linkbutton({//加入
	iconCls: 'icon-add',
	onClick: function(){
		if(!$('#laysouth').is(":visible")){
			$(document.body).layout('expand', 'south');
			$.messager.show({title:'提示', msg:"请在页面下方选择需加入的任务。"});
			return false;
		}
		var selectedTab = $('#reftabs').tabs('getSelected');
		switch($('#reftabs').tabs('getTabIndex', selectedTab)){
			case 0:
				var srcRows = $('#ontimegrid').datagrid('getSelections');
				break;
			case 1:
				var srcRows = $('#outtimegrid').datagrid('getSelections');
				break;
			case 2:
				var srcRows = $('#hisgrid').datagrid('getSelections');
		}
		var hasNew = false;
		for(var i=0;i<srcRows.length;i++){//id不存在则追加行
			var currRows = $('#currgrid').datagrid('getRows');
			if(!_inCurrdg(srcRows[i].taskId,srcRows[i].code)){
				//生成临时id
				var addARow = {"id":1};
				for(var j=0;j<currRows.length;j++){
					if(currRows[j].id>=addARow.id) addARow.id = currRows[j].id + 1;
				}
				for(var key in srcRows[i]){
					if(key != "id") addARow[key] = srcRows[i][key];
				}
				$('#currgrid').datagrid('appendRow',addARow);
				tempAddIds[tempAddIds.length] = addARow.id;
				hasNew = true;
			}
		}
		if(!hasNew){
			$.messager.alert('操作提示', '未选中可填报的任务，不可与已选的重复（已选的任务名称为灰底色）。', 'error');
		}else{
			$('#currgrid').datagrid('scrollTo', $('#currgrid').datagrid('getRowIndex',tempAddIds[tempAddIds.length-1])).datagrid('autoSizeColumn');
			_refreshTabs();
			$(NoEditing).hide();
			$(Editing).show();
			_refreshDg('#currweekgrid',true);
			$('#dateswitch').datebox({disabled:true}).datebox('setValue',currYear+"-"+currMon+"-"+currDate);
		}
	}
});
function _inCurrdg(taskId,code){
	var result = false;
	var currRows = $('#currgrid').datagrid('getRows');
	for(var i=0;i<currRows.length;i++){
		if((code && currRows[i].taskId == taskId && currRows[i].code == code) ||
			(!code && currRows[i].taskId == taskId)){
			result = true;
			break;
		}
	}
	return result;
}
function _refreshDg(dg,remain){
	if(!remain) $(dg).datagrid('clearSelections');
	var dgRows = $(dg).datagrid('getRows');
	for(var i=0;i<dgRows.length;i++) $(dg).datagrid('refreshRow', i);
}
function _refreshTabs(){
	if(ontimeloaded) _refreshDg('#ontimegrid');
	if(outtimeloaded) _refreshDg('#outtimegrid');
	if(hisloaded) _refreshDg('#hisgrid');
}
$('#currdg_del').linkbutton({//删除
	iconCls: 'icon-remove',
	onClick: function(){
		var removeRows = $('#currgrid').datagrid('getSelections');
		if (removeRows.length == 0) {
			$.messager.alert('操作提示', '未选中待删除的任务。', 'error');
		} else {
			var removeIds = [];
			for(var i = 0; i < removeRows.length; i++) {
				removeIds[removeIds.length] = removeRows[i].id
			}
			$.messager.confirm('操作提示', '注意：已选中' + removeRows.length + '个待删除的任务，<br>是否确定删除？', function (r) {
				if (r) {
					var fulldatecurr = currYear + "-" +currMon + "-" + currDate;
					var weekrows = $('#currweekgrid').datagrid('getRows');
					for(var i=0;i<weekrows.length;i++){
						if(weekrows[i].fulldate == fulldatecurr){
							var rowdata = {};
							for(var key in weekrows[i]){
								if(key != "tasks") rowdata[key] = weekrows[i][key];
							}
							rowdata["tasks"] = [];
							rowdata["totalHour"] = 0;
							for(var j=0;j<weekrows[i].tasks.length;j++){
								if($.inArray(weekrows[i].tasks[j].id, removeIds)==-1){
									rowdata.tasks[rowdata.tasks.length] = _copyObj(weekrows[i].tasks[j]);//eval(JSON.stringify(weekrows[i].tasks[j]));
									rowdata.totalHour += weekrows[i].tasks[j].updateWorkHour ? 1*weekrows[i].tasks[j].updateWorkHour : 0;
								}
							}
							$('#currweekgrid').datagrid('updateRow',{
								index: i,
								row: rowdata
							});
							$('#currgrid').datagrid('loadData',rowdata.tasks);
							break;
						}
					}
					_refreshTabs();
					$('#currdg_save').linkbutton('enable');
				}
			});
		}
	}
});
$('#currdg_edit').linkbutton({//填报
	iconCls: 'icon-edit',
	onClick: function(){
		var editrows = $('#currgrid').datagrid('getSelections');
		if (editrows.length == 0) {
			$.messager.alert("操作提示", "请先选择需要填报的任务。", "error");
		} else {
			_EditRow(editrows);
		}
	}
});
$('#currdg_cancel').linkbutton({//放弃
	iconCls: 'icon-undo',
	onClick: function(){
		var getrows = $('#currgrid').datagrid('getRows');
		for(var i = 0; i < getrows.length; i++) {
			$('#currgrid').datagrid('cancelEdit',i);
		}
		for (var i = 0; i < tempAddIds.length; i++) {
			$('#currgrid').datagrid('deleteRow', $('#currgrid').datagrid('getRowIndex', tempAddIds[i]));
		}
		tempAddIds = [];
		$(NoEditing).show();
		$(Editing).hide();
		_reloadcurrgrid();
		$('#dateswitch').datebox({disabled:false}).datebox('setValue',currYear+"-"+currMon+"-"+currDate);
		batch = false;
		$('#batchdiv').hide();
	}
});
$('#currdg_accept').linkbutton({//继续
	iconCls: 'icon-ok',
	onClick: function(){
		if(!batch){
			var getrows = $('#currgrid').datagrid('getRows');
			for(var i = 0; i < getrows.length; i++) {
				if(!$('#currgrid').datagrid('validateRow', i)){
					$.messager.alert('操作提示', '不可继续：第'+(i+1)+'行存在不规范数据，请修正。', 'error');
					return;
				}
			}
			var currtotalHour = 0;
			for(var i = 0; i < getrows.length; i++) {
				$('#currgrid').datagrid('endEdit',i);
				currtotalHour += getrows[i].updateWorkHour ? 1*getrows[i].updateWorkHour : 0;
			}
			var fulldatecurr = currYear + "-" +currMon + "-" + currDate;
			var weekrows = $('#currweekgrid').datagrid('getRows');
			for(var i=0;i<weekrows.length;i++){
				if(weekrows[i].fulldate == fulldatecurr){
					var rowdata = {};
					for(var key in weekrows[i]){
						if(key != "tasks") rowdata[key] = weekrows[i][key];
					}
					rowdata["tasks"] = _copyObjArr(getrows);//eval(JSON.stringify(getrows));
					rowdata.totalHour = currtotalHour;
					$('#currweekgrid').datagrid('updateRow',{
						index: i,
						row: rowdata
					});
					break;
				}
			}
			tempAddIds = [];
			_refreshTitle();
		}else{
			var tgtrows = $('#currweekgrid').datagrid('getSelections');
			if(tgtrows.length==0){
				$.messager.alert('操作提示', '请先选中将导入到的当前目标日期（可多选）。', 'error');
				return;
			}
			var srcrows = $('#currgrid').datagrid('getRows');
			for(var i = 0; i < srcrows.length; i++) {
				if(!$('#currgrid').datagrid('validateRow', i)){
					$.messager.alert('操作提示', '不可继续：第'+(i+1)+'行存在不规范数据，请修正。', 'error');
					return;
				}
			}
			if($('#batchmode').combo('getValue')==''){
				$.messager.alert("操作提示", "未选择导入模式。", "error");
				return;
			}else{
				var currtotalHour = 0;
				for(var i = 0; i < srcrows.length; i++) {
					$('#currgrid').datagrid('endEdit',i);
					currtotalHour += srcrows[i].updateWorkHour ? 1*srcrows[i].updateWorkHour : 0;
				}
				for(var i=0;i<tgtrows.length;i++){
					var tgtindex = $('#currweekgrid').datagrid('getRowIndex', tgtrows[i].id);
					var rowdata = {"id":tgtrows[i].id,"fulldate":tgtrows[i].fulldate};
					switch($('#batchmode').combo('getValue')){
						case "0"://覆盖模式
							rowdata["totalHour"] = currtotalHour;
							rowdata["tasks"] = _copyObjArr(srcrows);
							break;
						case "1"://追加模式
							rowdata["totalHour"] = currtotalHour+(tgtrows[i].totalHour ? 1*tgtrows[i].totalHour : 0);
							rowdata["tasks"] = [];
							var matchIndex = [];
							for(var j=0;j<tgtrows[i].tasks.length;j++){
								var matched = false;
								for(var k=0;k<srcrows.length;k++){
									if(tgtrows[i].tasks[j].taskId == srcrows[k].taskId && tgtrows[i].tasks[j].code == srcrows[k].code){
										//重复任务记录下来，合并后再追加
										$('#currgrid').datagrid('endEdit',k);
										rowdata.tasks[j] = _copyObj(srcrows[k],"updateWorkHour");
										rowdata.tasks[j]["updateWorkHour"] = (srcrows[k].updateWorkHour ? 1*srcrows[k].updateWorkHour : 0) + (tgtrows[i].tasks[j].updateWorkHour ? 1*tgtrows[i].tasks[j].updateWorkHour : 0);
										matchIndex[matchIndex.length] = k;
										matched = true;
										break;
									}
								}
								if(!matched){//无重复任务直接追加
									rowdata.tasks[j] = _copyObj(tgtrows[i].tasks[j]);
								}
							}
							for(var j=0;j<srcrows.length;j++){//srcrows剔除重复任务追加
								$('#currgrid').datagrid('endEdit',j);
								if($.inArray(j,matchIndex)==-1){
									rowdata.tasks[rowdata.tasks.length] = _copyObj(srcrows[j]);
								}
							}
					}
					$('#currweekgrid').datagrid('updateRow',{
						index: tgtindex,
						row: rowdata
					});
				}
			}
			_reloadcurrgrid();//重载当天
			batch = false;
			$('#batchdiv').hide();
		}
		$(NoEditing).show();
		$(Editing).hide();
		_refreshDg('#currweekgrid',true);
		$('#dateswitch').datebox({disabled:false}).datebox('setValue',currYear+"-"+currMon+"-"+currDate);
		$('#currdg_save').linkbutton('enable');
	}
});
$('#currdg_save').linkbutton({//保存整周数据
	iconCls: 'icon-save',
	disabled:true,
	onClick: function(){//需要校验数据，包含code、taskId、prjID
		var getrows = $('#currweekgrid').datagrid('getRows');
		var saveData = [];
		for(var i=0;i<getrows.length;i++){
			var currWorkHour = 0;
			saveData[i] = {};
			saveData[i]["date"] = getrows[i].fulldate;
			saveData[i]["tasks"] = [];
			for(var j=0;j<getrows[i].tasks.length;j++){
				currWorkHour += getrows[i].tasks[j].updateWorkHour ? 1*getrows[i].tasks[j].updateWorkHour : 0;
				saveData[i].tasks[j] = {};
				saveData[i].tasks[j]["taskId"] = getrows[i].tasks[j].taskId;
				saveData[i].tasks[j]["commit"] = getrows[i].tasks[j].commit ? ""+getrows[i].tasks[j].commit : "";
				saveData[i].tasks[j]["progress"] = getrows[i].tasks[j].progress ? 1*getrows[i].tasks[j].progress : 0;
				saveData[i].tasks[j]["updateWorkHour"] = getrows[i].tasks[j].updateWorkHour ? 1*getrows[i].tasks[j].updateWorkHour : 0;
				if(getrows[i].tasks[j].code) saveData[i].tasks[j]["dlvrId"] = getrows[i].tasks[j].code;
				saveData[i].tasks[j]["projId"] = getrows[i].tasks[j].projID;
			}
			if(currWorkHour > maxHour || currWorkHour < minHour){
				$.messager.alert("操作提示", "不可保存："+getrows[i].fulldate+"日（周"+_getTheDay(getrows[i].fulldate)+"）可填报的总工时应不少于 "+alertminHour+" 小时，不多于 "+maxHour+" 小时。", "error");
				return;
			}
		}
		$.ajax({
			type: 'POST',
			url: 'http://'+window.location.host+'/api/wh/saveDlvr',
			contentType: "application/json",
			data: JSON.stringify(saveData),
			beforeSend: function (){$('#loaddiv').window('open');},
			success: function (data) {
				$('#loaddiv').window('close');
				if (data.Status == 'Success') {
					reloadcurrweek('dg');
					$.messager.show({ title: '提示', msg: '保存成功。' });
				}else $.messager.alert("保存失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
			}
		});
	}
});
/* $('#currdg_publish').linkbutton({//提交
	iconCls: 'icon-updateversion',
	onClick: function(){
		if (!$('#currdg_save').linkbutton('options').disabled){
			$.messager.alert("操作提示", "不可发布：存在未保存的编辑，请先保存。", "error");
			return;
		}
		$.messager.confirm('操作提示', '提交后将不可修改，请确认是否继续？', function (r) {
			if (r) {
				alert("提交操作");//校验上周、本周、下周范围内
			}
		});
	}
}); */
$('#currdg_reset').linkbutton({//重置
	iconCls: 'icon-reload',
	onClick: function(){
		if (!$('#currdg_save').linkbutton('options').disabled) {
			$.messager.confirm('操作提示', '将放弃所有未保存的编辑，请确认是否继续？', function (r) {
				if (r) reloadcurrweek('dg');
			});
		}else reloadcurrweek('dg');
	}
});
var NoEditing = '#currdg_add,#currdg_del,#currdg_edit,#currdg_save,#currdg_publish,#currdg_reset,#weekrefbtn';
var Editing = '#currdg_cancel,#currdg_accept';
reloadcurrweek();
function reloadcurrweek(dg){
	if(dg) _loadWeek(new Date(Number(currYear),(Number(currMon) - 1),Number(currDate)),'#currweekgrid');
	$(NoEditing).show();
	$(Editing).hide();
	_refreshDg('#currweekgrid',true);
	//$('#dateswitch').datebox({disabled:false}).datebox('setValue',currYear+"-"+currMon+"-"+currDate);
	$('#currdg_save').linkbutton('disable');
}
function _refreshTitle(){
	var currData = $('#currgrid').datagrid('getRows');
	var currWorkHour = 0;
	for(var i=0;i<currData.length;i++) currWorkHour += currData[i].updateWorkHour ? 1*currData[i].updateWorkHour:0;
	if(currWorkHour > maxHour || currWorkHour < alertminHour){
		currWorkHour = "<font color='red'>"+currWorkHour+"</font> 工时<font color='red'>（应填报 "+alertminHour+" ～ "+maxHour+" 小时）</font>";
	}else currWorkHour = currWorkHour+" 工时（应填报 "+alertminHour+" ～ "+maxHour+" 小时）";
	$('#currcenter').panel({ title: "用户：" + userName + " 部门：" + userDept + " - "+currYear+" 年 "+ currMon +" 月 "+ currDate +" 日 星期"+_getTheDay(currYear+"-"+currMon+"-"+currDate)+" 共填报 "+currWorkHour});
}	
$('#currgrid').datagrid({//当前日任务
	method: 'get',
	idField: 'id',
	//url:'js/currTask.json',
	rownumbers: true, singleSelect: false, pagination: false,
	fit: true,toolbar:'#currdgtb',border:false,striped:true,
	onLoadSuccess:function(){
		$(this).datagrid('clearSelections');
		_refreshDg('#currweekgrid',true);
		_refreshTitle();
		_refreshTabs();
		$('#loaddiv').window('close'); 
	},
	frozenColumns: [[
		{ field: 'ck', checkbox: true },
		{ field: 'taskName', title: '名称',formatter: function(value,row,index){
			return "<a href='javascript:_workExcute("+index+",1);'>"+value+"</a>";
		}}]],
	onDblClickRow: function (index, row) {
		if(!$('#currdg_edit').linkbutton('options').disabled) _EditRow([row]);
	},
	columns: [[
		{ field: 'projName', title: '项目'},
		{ field: 'updateWorkHour', title: '*当日工时',styler: function(value,row,index){if(!value||value==0) return 'background-color:#EEE8AA;color:red;';}, editor: { type: 'numberbox', options: { required: true, min: 0,precision:1 } }},
		{ field: 'progress', title: '*进度%', editor: { type: 'numberbox', options: { required: false, min: 0,precision:1 } }},
		{ field: 'commit', title: '填报详情', editor: { type: 'validatebox', options: { required: false } }},
		{ field: 'accuWorkHour', title: '已填工时'},
		{ field: 'workHour', title: '计划工时'},
		{ field: 'code', title: '工作包号'},
		{ field: 'description', title: '说明'},
		{ field: 'phaseName', title: '阶段'},
		{ field: 'division', title: '子项'},
		{ field: 'projNum', title: '项目编号'},
		{ field: 'system', title: '系统', hidden: true},
		{ field: 'specialityName', title: '专业', hidden: true},
		{ field: 'startDate', title: '计划起始日期', hidden: true},
		{ field: 'endDate', title: '计划完成日期'},
		{ field: 'designName', title: '设计人'},
		{ field: 'checkName', title: '校核人'},
		{ field: 'reviewName', title: '审核人'},
		{field: 'status', title: '状态',
			styler: function (value, row, index) {
				if (row.status == "失效") {
					return 'background-color:lightgray;';
				}
			}
		},
		{ field: 'authorName', title: '创建者', hidden: true},
		{ field: 'approveName', title: '审定人', hidden: true},
		{ field: 'certifiedName', title: '注册工程师', hidden: true},
		{ field: 'schemeName', title: '方案设计人', hidden: true},
		{ field: 'otherName', title: '其他参与人', hidden: true},
		{ field: 'grandNum', title: '专业孙项号', hidden: true},
		{ field: 'grandName', title: '专业孙项名称', hidden: true},
		{ field: 'workgrandNum', title: '工作包孙项号', hidden: true },
		{ field: 'matCode', title: '关联物料编码', hidden: true},
		{ field: 'matName', title: '关联物料名称', hidden: true}]]
});
function _EditRow(rows){
	if(rows.length>1){
		//$('#currgrid').datagrid('autoSizeColumn','taskName');
		$('#loaddiv').window('open');
		var i=0;
		var int=window.setInterval(function(){
			if(i<rows.length){
				$('#loaddiv').panel('setTitle', Math.round(parseFloat(100*i/rows.length))+'%已处理，请稍候...');
				$('#currgrid').datagrid('beginEdit', $('#currgrid').datagrid('getRowIndex', rows[i].id));
				i++;
			}else{
				int=window.clearInterval(int);
				$('#loaddiv').window('close');
			}
		},0);
	}else if(rows.length==1) $('#currgrid').datagrid('beginEdit', $('#currgrid').datagrid('getRowIndex', rows[0].id));
	$(NoEditing).hide();
	$(Editing).show();
	_refreshDg('#currweekgrid',true);
	$('#dateswitch').datebox({disabled:true}).datebox('setValue',currYear+"-"+currMon+"-"+currDate);
}
$('#currshowall').prop("checked",false).change(_hideCurrMoreCols);//显示更多列
function _hideCurrMoreCols(){
	var moreCols = ['system','specialityName','startDate','authorName','approveName','certifiedName','schemeName','otherName','grandNum','grandName','workgrandNum','matCode','matName'];
	if($('#currshowall').is(":checked")){
		for(var i=0;i<moreCols.length;i++) $('#currgrid').datagrid('showColumn', moreCols[i]);
	}else for(var i=0;i<moreCols.length;i++) $('#currgrid').datagrid('hideColumn', moreCols[i]);
}

/* $("#curr-calendar").calendar({
	border:false,
	firstDay:1,
	fit:true,
	current:new Date(),
	styler:function(date){
		if(DateToString(date).full == currFullDate){
			return 'border-left:2px solid red;border-top:2px solid red;border-right:2px solid red;border-bottom:2px solid red;font-family:Arial';
		}else if(DateToString(date).full == DateToString(myDate).full){
			return 'border-left:1px solid gray;border-top:1px solid gray;border-right:1px solid gray;border-bottom:1px solid gray;font-family:Arial';
		}else return 'border-right:1px solid #ccc;border-top:1px solid #ccc;font-family:Arial';
	},
	formatter:function(date){
		if(DateToString(date).full == DateToString(myDate).full){
			var datedata = date.getDate() + " - 今天";
		}else var datedata = date.getDate();
		return "<input id="+DateToString(date).full+" type='checkbox'><a href='javascript:_toDate("+DateToString(date).full+");'>10.5 时</a><br><font size='2.5'>"+datedata+"</font>";
	},
	validator:function(date){
		return true;
	}
}); */
$('#laysouth').panel({
	onExpand: function () {
		var selectedTab = $('#reftabs').tabs('getSelected');
		switch($('#reftabs').tabs('getTabIndex',selectedTab)){
			case 0:
				_ontimegrid();
				break;
			case 1:
				_outtimegrid();
				break;
			case 2:
				_hisgrid();
		}
	}
});
$('#ontimemenu').menu('appendItem',{
	text:"全部类型"
}).menu('appendItem',{
	text:"生产项目"
}).menu('appendItem',{
	text:"开发项目"
}).menu('appendItem',{
	text:"科技项目"
}).menu('appendItem',{
	text:"部门项目"
}).menu('appendItem',{
	text:"其他项目"
});
$('#intimeprjSearch').searchbox({
	menu:'#ontimemenu',
	searcher: function (value, name) {
		$('#intimenavgrid').treegrid('load', {
			projType:$('#intimeprjSearch').searchbox('getName'),
			keywords:value
		});
	}
});
$('#expandbtn').linkbutton({
	onClick:function(){ $('#intimenavgrid').treegrid('expandAll'); }
});
$('#collapsebtn').linkbutton({
	onClick:function(){ $('#intimenavgrid').treegrid('collapseAll'); }
});
$('#outtimemenu').menu('appendItem',{
	text:"全部类型"
}).menu('appendItem',{
	text:"生产项目"
}).menu('appendItem',{
	text:"开发项目"
}).menu('appendItem',{
	text:"科技项目"
}).menu('appendItem',{
	text:"部门项目"
}).menu('appendItem',{
	text:"其他项目"
});
$('#outtimeprjSearch').searchbox({
	menu:'#outtimemenu',
	searcher: function (value, name) {
		$('#outtimenavgrid').treegrid('load', {
			projType:$('#outtimeprjSearch').searchbox('getName'),
			keywords:value
		});
	}
});
$('#outexpandbtn').linkbutton({
	onClick:function(){ $('#outtimenavgrid').treegrid('expandAll'); }
});
$('#outcollapsebtn').linkbutton({
	onClick:function(){ $('#outtimenavgrid').treegrid('collapseAll'); }
});
var ontimeloaded = false;
var ontimetgloadId = '';
var outtimeloaded = false;
var outtimetgloadId = '';
var hisloaded = false;
function _ontimegrid(){
	if(!ontimeloaded){
		ontimetgloadId = '';
		$('#intimenavgrid').treegrid({
            url: 'http://'+window.location.host+'/api/wh/getProjInfo?currDate='+currYear+"-"+currMon+"-"+currDate+'&perId='+perID+'&flag=Y',
			queryParams:{
				projType:$('#intimeprjSearch').searchbox('getName')
			},
			fit:true,
			method: 'get',
            idField: 'id',
            treeField: 'taskName',
			fitColumns:true,toolbar:'#intimetgtb',border:false,striped:true,
            pagination: true, pageList: [5, 10, 15, 20], pageSize: 5,
			onSelect:function(row){$(this).treegrid('unselectAll');},
			onBeforeLoad: function(row, param){ontimetgloadId = '';},
            columns: [[
                { field: 'taskName',title:'项目任务树', width:390,halign:'center',formatter: function(value,row){
					if(ontimetgloadId == row.id){
						return value+'<img src="../../themes/icons/back.png" />';
					}else return "<a href='javascript:_loadDetail("+row.id+",1);'>"+value+"</a>";
				}}]]
		});
		$('#ontimegrid').datagrid({//期内任务
			method: 'get',
			idField: 'id',
			data:[],
			//url:'js/ontimeTask.json',
			rownumbers: true, singleSelect: false, pagination: false,
			fit: true,toolbar:'#ontimedgtb',border:false,striped:true,
			onLoadSuccess:function(){
				$(this).datagrid('clearSelections');
				ontimeloaded = true;
			},
			frozenColumns: [[
				{ field: 'ck', checkbox: true },
				{ field: 'taskName', title: '名称',formatter: function(value,row,index){
					return "<a href='javascript:_workExcute("+index+",2);'>"+value+"</a>";
				},styler: function(value,row,index){
					if(_inCurrdg(row.taskId,row.code))
						return 'background-color:lightgray';
				}}]],
			columns: [[
				{ field: 'projName', title: '项目'},
				{ field: 'progress', title: '进度%'},
				{ field: 'accuWorkHour', title: '已填工时'},
				{ field: 'workHour', title: '计划工时'},
				{ field: 'code', title: '工作包号'},
				{ field: 'description', title: '说明'},
				{ field: 'phaseName', title: '阶段'},
				{ field: 'division', title: '子项'},
				{ field: 'projNum', title: '项目编号'},
				{ field: 'system', title: '系统', hidden: true},
				{ field: 'specialityName', title: '专业', hidden: true},
				{ field: 'startDate', title: '计划起始日期', hidden: true},
				{ field: 'endDate', title: '计划完成日期'},
				{ field: 'designName', title: '设计人'},
				{ field: 'checkName', title: '校核人'},
				{ field: 'reviewName', title: '审核人'},
				{field: 'status', title: '状态',
					styler: function (value, row, index) {
						if (row.status == "失效") {
							return 'background-color:lightgray;';
						}
					}
				},
				{ field: 'authorName', title: '创建者', hidden: true},
				{ field: 'approveName', title: '审定人', hidden: true},
				{ field: 'certifiedName', title: '注册工程师', hidden: true},
				{ field: 'schemeName', title: '方案设计人', hidden: true},
				{ field: 'otherName', title: '其他参与人', hidden: true},
				{ field: 'grandNum', title: '专业孙项号', hidden: true},
				{ field: 'grandName', title: '专业孙项名称', hidden: true},
				{ field: 'workgrandNum', title: '工作包孙项号', hidden: true },
				{ field: 'matCode', title: '关联物料编码', hidden: true},
				{ field: 'matName', title: '关联物料名称', hidden: true}]]
		});
	}
}
function _loadDetail(id, tgseq){//加载树表子任务
	switch(tgseq){
		case 1:
			var dg = '#ontimegrid';
			var tg = '#intimenavgrid';
			var oldId = ontimetgloadId;
			ontimetgloadId = id;
			break;
		case 2:
			var dg = '#outtimegrid';
			var tg = '#outtimenavgrid';
			var oldId = outtimetgloadId;
			outtimetgloadId = id;
	}
	var node0 = [$(tg).treegrid('find',id)];
	var nodes = node0.concat($(tg).treegrid('getChildren',id));
	var newdgRows = [];
	for(var i=0;i<nodes.length;i++){
		if(!(nodes[i].children && nodes[i].children.length > 0)){
			newdgRows[newdgRows.length] = {};
			for(var key in nodes[i]) newdgRows[newdgRows.length - 1][key] = nodes[i][key];
		}
	}
	$(dg).datagrid('loadData', newdgRows).datagrid('clearSelections');
	$(tg).treegrid('refresh', id);
	if(oldId != '') $(tg).treegrid('refresh', oldId);
}
function _outtimegrid(){
	if(!outtimeloaded){
		outtimetgloadId = '';
		$('#outtimenavgrid').treegrid({
            url: 'http://'+window.location.host+'/api/wh/getProjInfo?currDate='+currYear+"-"+currMon+"-"+currDate+'&perId='+perID+'&flag=N',
			queryParams:{
				projType:$('#outtimeprjSearch').searchbox('getName')
			},
			fit:true,
			method: 'get',
            idField: 'id',
            treeField: 'taskName',
			fitColumns:true,toolbar:'#outtimetgtb',border:false,striped:true,
            pagination: true, pageList: [5, 10, 15, 20], pageSize: 5,
			onSelect:function(row){$(this).treegrid('unselectAll');},
			onBeforeLoad: function(row, param){outtimetgloadId = '';},
            columns: [[
                { field: 'taskName',title:'项目任务树', width:390,halign:'center',formatter: function(value,row){
					if(outtimetgloadId == row.id){
						return value+'<img src="../../themes/icons/back.png" />';
					}else return "<a href='javascript:_loadDetail("+row.id+",2);'>"+value+"</a>";
				}}]]
		});
		$('#outtimegrid').datagrid({//超期任务
			method: 'get',
			idField: 'id',
			data:[],
			//url:'js/outtimeTask.json',
			rownumbers: true, singleSelect: false, pagination: false,
			fit: true,toolbar:'#outtimedgtb',border:false,striped:true,
			onLoadSuccess:function(){
				$(this).datagrid('clearSelections');
				outtimeloaded = true;
			},
			frozenColumns: [[
				{ field: 'ck', checkbox: true },
				{ field: 'taskName', title: '名称',formatter: function(value,row,index){
					return "<a href='javascript:_workExcute("+index+",3);'>"+value+"</a>";
				},styler: function(value,row,index){
					if(_inCurrdg(row.taskId,row.code))
						return 'background-color:lightgray';
				}}]],
			columns: [[
				{ field: 'projName', title: '项目'},
				{ field: 'progress', title: '进度%'},
				{ field: 'accuWorkHour', title: '已填工时'},
				{ field: 'workHour', title: '计划工时'},
				{ field: 'code', title: '工作包号'},
				{ field: 'description', title: '说明'},
				{ field: 'phaseName', title: '阶段'},
				{ field: 'division', title: '子项'},
				{ field: 'projNum', title: '项目编号'},
				{ field: 'system', title: '系统', hidden: true},
				{ field: 'specialityName', title: '专业', hidden: true},
				{ field: 'startDate', title: '计划起始日期', hidden: true},
				{ field: 'endDate', title: '计划完成日期'},
				{ field: 'designName', title: '设计人'},
				{ field: 'checkName', title: '校核人'},
				{ field: 'reviewName', title: '审核人'},
				{field: 'status', title: '状态',
					styler: function (value, row, index) {
						if (row.status == "失效") {
							return 'background-color:lightgray;';
						}
					}
				},
				{ field: 'authorName', title: '创建者', hidden: true},
				{ field: 'approveName', title: '审定人', hidden: true},
				{ field: 'certifiedName', title: '注册工程师', hidden: true},
				{ field: 'schemeName', title: '方案设计人', hidden: true},
				{ field: 'otherName', title: '其他参与人', hidden: true},
				{ field: 'grandNum', title: '专业孙项号', hidden: true},
				{ field: 'grandName', title: '专业孙项名称', hidden: true},
				{ field: 'workgrandNum', title: '工作包孙项号', hidden: true },
				{ field: 'matCode', title: '关联物料编码', hidden: true},
				{ field: 'matName', title: '关联物料名称', hidden: true}]]
		});
	}
}
function _hisgrid(){
	if(!hisloaded){
		hisYear = currYear;
		hisMon = currMon;
		hisDate = currDate;
/* 		var buttons = $.extend([], $.fn.datebox.defaults.buttons);
		buttons.splice(1, 0, {
			text: '今天',
			handler: function(target){
				$('#'+target.id).datebox('calendar').parent().parent().panel('close');
				$('#'+target.id).datebox('setValue',DateToString(myDate).strYear+"-"+DateToString(myDate).strMon+"-"+DateToString(myDate).strDate);
			}
		}); */
		$('#hisdate').datebox({
			currentText:null,
			buttons:calbuttons,
			required:true
		}).datebox('setValue',hisYear+"-"+hisMon+"-"+hisDate).datebox('calendar').calendar({
			firstDay: 1,
			onChange:function(newDate,oldDate){
				var ToReloadWeek = _reloadWeek(newDate,false);
				hisYear = DateToString(newDate).strYear;
				hisMon = DateToString(newDate).strMon;
				hisDate = DateToString(newDate).strDate;
				if(ToReloadWeek){
					_loadWeek(newDate,'#hisweekgrid');
				}else _reloadhisgrid();
			}
		});
		$(".datebox :text").attr("readonly", "readonly");
		$('#hisweekgrid').datagrid({//历史周工时
			method: 'get',
			idField: 'id',
			singleSelect: false, pagination: false,
			fit: true,border:false,striped:true,
			onLoadSuccess:function(data){
				$(this).datagrid('clearSelections');
				if(data.applied == "N"){
					var applied = "未锁定 - 可修改";
				}else var applied = "已锁定 - 不可修改";
				$('#hiscal').panel({ title: applied});
				_reloadhisgrid();
			},
			columns: [[
				{ field: 'ck', checkbox: true },
				{ field: 'id', title: '星期', formatter:function(value,row,index){
					if(row.fulldate == hisYear+"-"+hisMon+"-"+hisDate){
						return _getTheDay(row.fulldate)+'<img src="../../themes/icons/back.png" />';
					}else return _getTheDay(row.fulldate);
					},styler:function(value,row,index){
						if(_getTheDay(row.fulldate)=="六"){
							return 'background-color:#99FF99';
						}else if(_getTheDay(row.fulldate)=="日"){
							return 'background-color:#FFCC99';
						}
					}},
				{ field: 'fulldate', title: '日期', width:80,align: 'center', halign: 'center', formatter:function(value,row,index){
					if(value != hisYear+"-"+hisMon+"-"+hisDate){
						return "<a href='javascript:_switchHisWeek("+index+");'>"+value+"</a>";
					}else return value;
					}},
				{ field: 'totalHour', title: '已填工时',align: 'center', halign: 'center'}]]
		});
		_loadWeek(new Date(Number(currYear),(Number(currMon) - 1),Number(currDate)),'#hisweekgrid');
		$('#hisgrid').datagrid({//历史任务
			method: 'get',
			idField: 'id',
			//url:'js/hisTask.json',
			rownumbers: true, singleSelect: false, pagination: false,
			fit: true,toolbar:'#hisdgtb',border:false,striped:true,
			onLoadSuccess:function(){
				$(this).datagrid('clearSelections');
				hisloaded = true;
				_refreshDg('#hisweekgrid');
			},
			frozenColumns: [[
				{ field: 'ck', checkbox: true },
				{ field: 'taskName', title: '名称',formatter: function(value,row,index){
					return "<a href='javascript:_workExcute("+index+",4);'>"+value+"</a>";
				},styler: function(value,row,index){
					if(_inCurrdg(row.taskId,row.code))
						return 'background-color:lightgray';
				}}]],
			columns: [[
				{ field: 'projName', title: '项目'},
				{ field: 'updateWorkHour', title: '当日工时',styler: function(value,row,index){if(!value||value==0) return 'background-color:#EEE8AA;color:red;';}},
				{ field: 'progress', title: '进度%'},
				{ field: 'commit', title: '填报详情'},
				{ field: 'accuWorkHour', title: '已填工时'},
				{ field: 'workHour', title: '计划工时'},
				{ field: 'code', title: '工作包号'},
				{ field: 'description', title: '说明'},
				{ field: 'phaseName', title: '阶段'},
				{ field: 'division', title: '子项'},
				{ field: 'projNum', title: '项目编号'},
				{ field: 'system', title: '系统', hidden: true},
				{ field: 'specialityName', title: '专业', hidden: true},
				{ field: 'startDate', title: '计划起始日期', hidden: true},
				{ field: 'endDate', title: '计划完成日期'},
				{ field: 'designName', title: '设计人'},
				{ field: 'checkName', title: '校核人'},
				{ field: 'reviewName', title: '审核人'},
				{field: 'status', title: '状态',
					styler: function (value, row, index) {
						if (row.status == "失效") {
							return 'background-color:lightgray;';
						}
					}
				},
				{ field: 'authorName', title: '创建者', hidden: true},
				{ field: 'approveName', title: '审定人', hidden: true},
				{ field: 'certifiedName', title: '注册工程师', hidden: true},
				{ field: 'schemeName', title: '方案设计人', hidden: true},
				{ field: 'otherName', title: '其他参与人', hidden: true},
				{ field: 'grandNum', title: '专业孙项号', hidden: true},
				{ field: 'grandName', title: '专业孙项名称', hidden: true},
				{ field: 'workgrandNum', title: '工作包孙项号', hidden: true },
				{ field: 'matCode', title: '关联物料编码', hidden: true},
				{ field: 'matName', title: '关联物料名称', hidden: true}]]
		});
	}
}
$('#ontimeshowall').prop("checked",false).change(_hideOntimeMoreCols);//显示更多列
function _hideOntimeMoreCols(){
	var moreCols = ['system','specialityName','startDate','authorName','approveName','certifiedName','schemeName','otherName','grandNum','grandName','workgrandNum','matCode','matName'];
	if($('#ontimeshowall').is(":checked")){
		for(var i=0;i<moreCols.length;i++) $('#ontimegrid').datagrid('showColumn', moreCols[i]);
	}else for(var i=0;i<moreCols.length;i++) $('#ontimegrid').datagrid('hideColumn', moreCols[i]);
}
$('#reftabs').tabs({
	onSelect:function (title,index){
		switch(index){
			case 0:
				_ontimegrid();
				break;
			case 1:
				_outtimegrid();
				break;
			case 2:
				_hisgrid();
		}
	}
});

$('#outtimeshowall').prop("checked",false).change(_hideOuttimeMoreCols);//显示更多列
function _hideOuttimeMoreCols(){
	var moreCols = ['system','specialityName','startDate','authorName','approveName','certifiedName','schemeName','otherName','grandNum','grandName','workgrandNum','matCode','matName'];
	if($('#outtimeshowall').is(":checked")){
		for(var i=0;i<moreCols.length;i++) $('#outtimegrid').datagrid('showColumn', moreCols[i]);
	}else for(var i=0;i<moreCols.length;i++) $('#outtimegrid').datagrid('hideColumn', moreCols[i]);
}

$('#hisshowall').prop("checked",false).change(_hideHisMoreCols);//显示更多列
function _hideHisMoreCols(){
	var moreCols = ['system','specialityName','startDate','authorName','approveName','certifiedName','schemeName','otherName','grandNum','grandName','workgrandNum','matCode','matName'];
	if($('#hisshowall').is(":checked")){
		for(var i=0;i<moreCols.length;i++) $('#hisgrid').datagrid('showColumn', moreCols[i]);
	}else for(var i=0;i<moreCols.length;i++) $('#hisgrid').datagrid('hideColumn', moreCols[i]);
}

function _workExcute(index,dgID) {
	switch(dgID){
		case 1:
			var row = $('#currgrid').datagrid('getRows')[index];
			break;
		case 2:
			var row = $('#ontimegrid').datagrid('getRows')[index];
			break;
		case 3:
			var row = $('#outtimegrid').datagrid('getRows')[index];
			break;
		case 4:
			var row = $('#hisgrid').datagrid('getRows')[index];
	}
	switch (row.worktypeID) {
		case "01"://图纸
			$('#btnDraw,#btnAppr').linkbutton({//创建图纸校审流程
				onClick:function(){
					//正式环境window.open('http://'+window.location.host+'/pages/wbsp/code/CheckReview/CheckReview.html?projID='+prjid+'&phaseID='+prjPhaseID+'&inDlvrId='+row.code,'_blank');
					var isAppr = $(this).text()=="会签" ? "&isAppr=true" : "";
					$('#Drawdiv').window('close');
					window.open('http://'+window.location.host+'/pages/wbsp/code - dev/CheckReview/CheckReview.html?projID='+row.projID+'&phaseID='+row.phaseID+'&inDlvrId='+row.code+isAppr,'_blank');
				}
			}).show().linkbutton('disable');
			$('#btnecm').linkbutton({//查阅ecm
				onClick:function(){
					//正式环境window.open("http://wcc.nerin.com:16200/cs/idcplg?IdcService=CUX_WP_MAIN_SRH&isSearch=1&PG_TYPE=WORKPKG&CUX_USER_NAME="+userNum+"&dlvrId="+row.id+"&<$include%20idc_token_url_parameter$>", '_blank');
					window.open("http://192.168.15.252:16200/cs/idcplg?IdcService=CUX_WP_MAIN_SRH&isSearch=1&PG_TYPE=WORKPKG&CUX_USER_NAME="+userNum+"&dlvrId="+row.code+"&<$include idc_token_url_parameter$>", '_blank');
				}
			})
			if(row.designPerson==perID) $('#btnDraw,#btnAppr').linkbutton('enable');
			$('#Drawdg').datagrid({//Draw表格
				url:"http://"+window.location.host+"/api/wbspToOa/getDlvrOa?dlvrId="+row.code,
				rownumbers: true,
				border: false,singleSelect:false,method: 'post',
				fitColumns:true,
				fit: true,
				idField: 'calcuId',
				toolbar: '#DrawTool',
				singleSelect: true,
				columns:[[
					{ field: 'requestName', title: '流程标题', width: 190,formatter: function(value,row,index){
						return "<a href='javascript:_linkOA("+row.requestId+");'>"+value+"</a>";
					}},
					{ field: 'jsnr', title: '校审内容', width: 130 },
					{ field: 'createDate', title: '创建日期', width: 45 },
					{ field: 'creater', title: '创建人', width: 30 },
					{ field: 'status', title: '当前状态', width: 45 },
					{ field: 'currentPerson', title: '当前操作者', width: 50 }]]
			}).datagrid('clearSelections');
			var designDlvr = row.designName?row.designName:"未分配";
			$('#Drawdiv').panel('setTitle',"图纸校审流程记录 - "+row.taskName+" 设计人："+designDlvr).window('open');
			break;
 		case "02"://文本
			var pElementId = row.code;
			$('#NBCCdg').datagrid({//NBCC表格
				url:"http://"+window.location.host+"/api/naviWbpsRest/wb?pElementId="+pElementId,
				rownumbers: true,
				border: false,singleSelect:false,method: 'get',
				fitColumns:true,
				fit: true,
				idField: 'taskeaderId',
				columns:[[
					{ field: 'taskName', title: '名称', width: 200,formatter: function(value,row,index){
						return "<a href='javascript:_goNBCC("+index+");'>"+value+"</a>";
					} },
					{ field: 'createName', title: '创建人', width: 30 },
					{ field: 'taskProgress', title: '章节完成情况', width: 40 },
					{ field: 'taskProgress2', title: '专业校审完成情况', width: 55,formatter: function(value,row,index){
						return "<a href='javascript:_oaNBCCcheck("+pElementId+","+row.taskeaderId+");'>"+value+"</a>";
					} },
					{ field: 'taskStatusName', title: '状态', width: 60,formatter: function(value,row,index){
						return "<a href='javascript:_oaNBCCreview("+pElementId+","+row.taskeaderId+");'>"+value+"</a>";
					} }]]
			}).datagrid('clearSelections');
			var designDlvr = row.designName?row.designName:"未分配";
			$('#NBCCformdiv').panel('setTitle',"协作文本任务 - "+row.taskName+" 设计人："+designDlvr).window('open');
			break;
		case "03"://计算书
			$('#btnCalculation,#btnCalculationup').linkbutton({//创建流程
				onClick:function(){
					$('#Calculationdiv').window('close');
					window.open("http://"+OAurl+"/login/LoginSSO.jsp?flowCode=AM02&erpid="+row.code+ "&workcode=" +userNum,'_blank','location=no');
				}
			}).show().linkbutton('disable');
			if(row.designPerson==perID) $('#btnCalculation,#btnCalculationup').linkbutton('enable');
			$('#Calculationdg').datagrid({//Calculation表格
				url:"http://"+window.location.host+"/api/naviWbpsRest/jss_oa?projElementId="+row.code,
				rownumbers: true,
				border: false,singleSelect:false,method: 'get',
				fitColumns:true,
				fit: true,
				idField: 'calcuId',
				toolbar: '#CalculationTool',
				singleSelect: true,
				onLoadSuccess:function(){
					if($('#Calculationdg').datagrid('getRows').length==0){
						$('#btnCalculationup').hide();
					}else $('#btnCalculation').hide();
				},
				columns:[[
					{ field: 'requestName', title: '流程标题', width: 220,formatter: function(value,row,index){
						return "<a href='javascript:_linkOA("+row.requestId+");'>"+value+"</a>";
					} },
					{ field: 'calcuNumber', title: '计算书编号', width: 100 },
					{ field: 'createDate', title: '创建日期', width: 50 },
					{ field: 'status', title: '当前状态', width: 50 },
					{ field: 'currentPerson', title: '当前操作者', width: 50 }]]
			}).datagrid('clearSelections');
			var designDlvr = row.designName?row.designName:"未分配";
			$('#Calculationdiv').panel('setTitle',"计算书任务流程记录 - "+row.taskName+" 设计人："+designDlvr).window('open');
			break;
		case "04"://三维
			console.log("04"+row.name);
			break;
		case "05"://接口条件
			$('#btnCondition,#btnConditionup').linkbutton({//创建流程
				onClick:function(){
					$.ajax({
						url: "http://" + window.location.host +"/api/lev3/getProfessionListAll?projID="+row.projID+"&specialty="+row.recvSpec,
						type: "GET",
						async: false,
						beforeSend: function () {$('#loaddiv').window('open');},
						success: function (chgrlist) {
							if(chgrlist.length==0){
								$('#loaddiv').window('close');
								$.messager.alert("操作提示","未查找到专业负责人，请向项目管理部确认策划信息后重试。","error");
								return;
							}
							var chgrArr = [];
							for(var i=0;i<chgrlist.length;i++){
								for(var j=0;j<chgrlist[i].member.length;j++){
									var haschgr = false;
									for(var k=0;k<chgrArr.length;k++){
										if(chgrlist[i].member[j].perNum==chgrArr[k].perNum){
											haschgr = true;
											break;
										}
									}
									if(!haschgr) chgrArr[chgrArr.length] = chgrlist[i].member[j];
								}
							}
							$('#zychgr').combo('setValue',null).combobox("loadData", chgrArr);
							if(chgrArr.length==1) $('#zychgr').combobox('select',chgrArr[0].perNum);
							$('#btnconuserapply').linkbutton({
								iconCls: 'icon-ok',
								onClick:function(){
									if($('#zychgr').combo('getValue')==''){
										$.messager.alert("操作提示", "未选择接收专业负责人。", "error");
										return;
									}else{
										$.ajax({
											url: "http://"+window.location.host+"/api/naviWbpsRest/createcondition?projectId="+row.projID+"&elementId="+row.specId+"&deliverableId="+row.code+"&submitCode="+row.majorCode+"&receiveCode="+row.recvSpec+"&schleDate="+row.endDate+"&userId="+userID+"&jszyfzr="+$('#zychgr').combo('getValue'),
											type: "GET",
											success: function (data) {
												$('#conditionuser').window('close');
												$('#Conditiondg').datagrid('reload');
												var datamsg = eval(data)["message"];
												if(datamsg == "S"){
													$('#Conditiondiv').window('close');
													window.open(eval(data)["url"],'_blank','location=no');
												}else $.messager.alert("操作提示", "发起新流程失败："+datamsg, "error");
											}
										});
									}
								}
							});
							$('#loaddiv').window('close');
							$('#conditionuser').window('open');
						},
						error: function(data){
							$('#loaddiv').window('close');
							$.messager.alert("操作提示","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
						}
					});
				}
			}).show().linkbutton('disable');
			if(row.designPerson==perID) $('#btnCondition,#btnConditionup').linkbutton('enable');
			$('#Conditiondg').datagrid({//条件单审批OA流程表格
				data:[],
				rownumbers: true,
				border: false,singleSelect:false,
				method: 'get',
				fitColumns:true,
				fit: true,
				idField: 'requestId',
				toolbar: '#ConditionTool',
				onLoadSuccess:function(){
					$('#Conditiondg').datagrid('loaded');
					if($('#Conditiondg').datagrid('getRows').length==0 || !$('#Conditiondg').datagrid('getRows')[0].conditionNumber){
						$('#btnConditionup').hide();
						$('#btnCondition').show();
					}else{
						$('#btnCondition').hide();
						$('#btnConditionup').show();
					}
				},
				columns:[[
					{ field: 'conditionNumber', title: '条件单编号', width: 76 },
					{ field: 'requestName', title: '流程标题', width: 240,formatter: function(value,row,index){
						return "<a href='javascript:_linkOA("+row.requestId+");'>"+value+"</a>";
					} },
					{ field: 'createDate', title: '创建日期', width: 47 },
					{ field: 'status', title: '当前状态', width: 60 },
					{ field: 'currentPerson', title: '当前操作者', width: 45 }]]
			}).datagrid('clearSelections');
			$.ajax({
				url: "http://"+window.location.host+"/api/naviWbpsRest/tjd?projElementId="+row.code,
				type: "GET",
				beforeSend: function () {
					$('#Conditiondg').datagrid('loading');
					$('#ConditionDescr').empty();
				},
				success: function (conditData) {
					if(conditData.length>0 && conditData[0].conditionId){
						var conditDescr = "";
						if(conditData[0].conditionName && conditData[0].conditionName.length>0) conditDescr += "说明："+conditData[0].conditionName;
						var conreceiveDate = "";
						if(conditData[0].receiveDate && conditData[0].receiveDate.length>0) conreceiveDate += "&nbsp;&nbsp;当前接收日期："+conditData[0].receiveDate;
						var conreceivePerson = "";
						if(conditData[0].receivePerson && conditData[0].receivePerson.length>0) conreceivePerson += "&nbsp;&nbsp;当前接收人："+conditData[0].receivePerson;
						var constatus = "";
						if(conditData[0].status && conditData[0].status.length>0){
							constatus += "&nbsp;&nbsp;当前状态："+conditData[0].status;
						}else constatus += "&nbsp;&nbsp;当前状态：待提交";
						$('#ConditionDescr').append(conditDescr+conreceiveDate+conreceivePerson+constatus);
						$('#Conditiondg').datagrid('options').url = "http://"+window.location.host+"/api/naviWbpsRest/tjdOa?conditionId="+conditData[0].conditionId;
						$('#Conditiondg').datagrid('reload');
					}else $('#Conditiondg').datagrid('loaded');
				},
				error: function(data){$('#Conditiondg').datagrid('loaded');}
			});
			var designDlvr = row.designName?row.designName:"未分配";
			$('#Conditiondiv').panel('setTitle',"接口条件任务 - "+row.taskName+" 设计人："+designDlvr).window('open');
			break;
		case "06"://其他
			console.log("06"+row.name); 
	}
}


function _goNBCC(index){//跳转NBCC平台协作任务
	var row = $('#NBCCdg').datagrid('getRows')[index];
	window.open("http://"+window.location.host+"/pages/management/index.html?doWork=wbrw&taskName="+row.taskName+"&taskHeaderId="+row.taskeaderId, '_blank');
}
function _oaNBCCcheck(pElementId,taskHeaderId){//NBCC专业校审OA流程列表
	$.ajax({//oracle包-当前工作包在协作文本中相关的“设计文件校审流程"OA流程列表
		url: "http://"+window.location.host+"/api/naviWbpsRest/wbChapter?pElementId="+pElementId+"&taskHeaderId="+taskHeaderId,
		type: "GET",
		success: function (data) {
			var dgData = [];
			for(var i=0;i<data.length;i++){
				if(data[i].requestName.indexOf("公司级评审") < 0){
					dgData[dgData.length] = {};
					for(var key in data[i]) dgData[dgData.length - 1][key] = data[i][key];
				}
			}
			$('#NBCCcheckdg').datagrid({//NBCCcheck表格
				data: dgData,
				rownumbers: true,
				border: false,singleSelect:false,
				method: 'get',
				fitColumns:true,
				fit: true,
				idField: 'requestId',
				columns:[[
					{ field: 'requestName', title: '流程标题', width: 260,formatter: function(value,row,index){
						return "<a href='javascript:_linkOA("+row.requestId+");'>"+value+"</a>";
					} },
					{ field: 'createBy', title: '创建人', width: 28 },
					{ field: 'createDate', title: '创建日期', width: 75 },
					{ field: 'status', title: '当前状态', width: 60 },
					{ field: 'currentPerson', title: '当前操作者', width: 45 }]]
			}).datagrid('clearSelections');
			$('#NBCCcheckdiv').window('open');
		}
	});
}
function _linkOA(requestId){//跳转OA表单
	window.open("http://"+OAurl+"/workflow/request/ViewRequest.jsp?requestid="+requestId,'_blank','location=no');
}
function _oaNBCCreview(pElementId, taskHeaderId){//NBCC公司级评审OA流程列表
	$.ajax({//oracle包-协作任务的公司级评审相关OA流程列表
		url: "http://"+window.location.host+"/api/naviWbpsRest/wbTask?taskHeaderId="+taskHeaderId,
		type: "GET",
		success: function (rw_data) {
			$.ajax({//oracle包-当前工作包在协作文本中相关的“设计文件校审流程"OA流程列表
				url: "http://"+window.location.host+"/api/naviWbpsRest/wbChapter?pElementId="+pElementId+"&taskHeaderId="+taskHeaderId,
				type: "GET",
				success: function (ch_data) {
					var dgData = [];
					for(var i=0;i<rw_data.length;i++){
						if(rw_data[i].requestName.indexOf("公司级评审申请") > -1){
							dgData[dgData.length] = {};
							for(var key in rw_data[i]) dgData[dgData.length - 1][key] = rw_data[i][key];
						}
					}
					for(var i=0;i<ch_data.length;i++){
						if(ch_data[i].requestName.indexOf("公司级评审") > -1){
							dgData[dgData.length] = {};
							for(var key in ch_data[i]) dgData[dgData.length - 1][key] = ch_data[i][key];
						}
					}
					for(var i=0;i<rw_data.length;i++){
						if(rw_data[i].requestName.indexOf("公司级评审结论") > -1){
							dgData[dgData.length] = {};
							for(var key in rw_data[i]) dgData[dgData.length - 1][key] = rw_data[i][key];
						}
					}
					$('#NBCCreviewdg').datagrid({//NBCCreview表格
						data: dgData,
						rownumbers: true,
						border: false,singleSelect:false,
						method: 'get',
						fitColumns:true,
						fit: true,
						idField: 'requestId',
						columns:[[
							{ field: 'requestName', title: '流程标题', width: 260,formatter: function(value,row,index){
								return "<a href='javascript:_linkOA("+row.requestId+");'>"+value+"</a>";
							} },
							{ field: 'createBy', title: '创建人', width: 28 },
							{ field: 'createDate', title: '创建日期', width: 75 },
							{ field: 'status', title: '当前状态', width: 60 },
							{ field: 'currentPerson', title: '当前操作者', width: 45 }]]
					}).datagrid('clearSelections');
					$('#NBCCreviewdiv').window('open');
				}
			});
		}
	});
}