var prjdrifwbsdataurl = "http://"+window.location.host+"/api/lev3/getPubDlvrList";
var prj_major_dataurl = "http://"+window.location.host+"/api/lev2/querySpecList";
var get_drawurl = "http://"+window.location.host+"/api/draw/DrawList";//draw/DesignChange";//
var get_dcurl = "http://"+window.location.host+"/api/naviWbpsRest/sjbgOa";
var ecmurl = "http://192.168.15.252:16200";
var OAurl = "192.168.15.198";
/* var catMode = 0;//默认不跨子项专业出图
var tmpSpeID = null;
var tmpSpeSeq = null;//xspecialtySeq专业孙项 */
var p_xml_id, p_rtf_id, catId;
var enablePlt = ['对图通过', '已出图文印', '校审通过', '出图文印中'];

$('#modecheck').prop("checked",true).change(_exmode);//默认标准模式
function _exmode(){
	if(!$('#modecheck').is(":checked")){
		$.ajax({
			url: prj_major_dataurl+"?projID="+prjid+"&phaseID="+prjPhaseID+"&"+userPar,
			type: "GET",
			success: function (prjspelist) {
				var dgdata = [];
				for(var i=0;i<prjspelist.length;i++){
					dgdata[i] = {};
					dgdata[i]["id"] = prjspelist[i].industryCode;
					dgdata[i]["name"] = prjspelist[i].name;
				}
				$('#gridPrjmajors').datagrid('loadData', dgdata).datagrid('clearSelections').datagrid('autoSizeColumn');
			}
		});
	}else $('#gridPrjmajors').datagrid('loadData', specialty).datagrid('clearSelections').datagrid('autoSizeColumn');
}
$('#divtree').tree({});
$('#filtermajorbtn').linkbutton({//选择专业按钮
	iconCls: 'icon-ahead',
	plain: true,
    onClick: function () {
		var majorSelections = $('#gridPrjmajors').datagrid('getSelections');
		if(majorSelections.length == 0){
			$.messager.alert("操作提示", "未选择专业。", "error");
			return;
		}else if(majorSelections.length == 1){
			var url = prjdrifwbsdataurl+"?projectId="+prjid+"&phaseCode="+prjPhaseID+"&structureVerId="+pubwbsID+"&spec="+majorSelections[0].id+"&"+userPar;
		}else var url = prjdrifwbsdataurl+"?projectId="+prjid+"&phaseCode="+prjPhaseID+"&structureVerId="+pubwbsID+"&spec=ALL&"+userPar;
		$.ajax({
			url:url,
			type:"GET",
			beforeSend: function (){
				$('#loaddiv').window('open');
			},
			success:function(data){
				var majorArr = [];
				for(var i=0;i<majorSelections.length;i++) majorArr[majorArr.length] = majorSelections[i].id;
				var reData = [];
				_reChildren(data, majorArr, reData);
				var reWork = [];
				_reWorktype(reData, reWork);
				$('#divtree').tree({//系统子项树
					border:false,
					onLoadSuccess:function(node, data){
						$('#pubdraw_layout').layout('collapse', 'west');
						$('#wbs_layout').layout('expand', 'west');
						$('#draw_layout').layout('expand', 'west');
						$('#loaddiv').window('close');
						if(data.length==0) $.messager.alert("操作提示", "未查找到图纸工作包。", "warning");
					},
					checkbox:true,
					cascadeCheck:true,
					onDblClick:function(node){
						$('#divtree').tree('toggle', node.target);
					},
					onCheck:function(node){
						var checkednodes = $('#divtree').tree('getChecked');
						for(var i=0;i<checkednodes.length;i++){
							$(checkednodes[i].target).find("span.tree-title").css({"background-color":"#0e2d5f","color":"#fff"});
						}
						var uncheckednodes = $('#divtree').tree('getChecked', 'unchecked');
						for(var i=0;i<uncheckednodes.length;i++){
							$(uncheckednodes[i].target).find("span.tree-title").removeAttr("style");
						}
						var incheckednodes = $('#divtree').tree('getChecked', 'indeterminate');
						for(var i=0;i<incheckednodes.length;i++){
							$(incheckednodes[i].target).find("span.tree-title").removeAttr("style");
						}
					},
					data:reWork,
					formatter:function(node){
						if(node.code){
							return node.code+" "+node.name;
						}else return node.name;
					}
				});
			}
		});
    }
});
$('#drawNumSearch').searchbox({
	searcher: function (value, name) {
		var ckNodes = $('#divtree').tree('getChecked');
		if(ckNodes.length==0){
			$.messager.alert("操作提示", "未勾选工作包。", "warning");
			return;
		}else{
			var ckIds = '';
			for(var i=0;i<ckNodes.length;i++)
				if(ckNodes[i].level=="work") ckIds += ","+ckNodes[i].id;
			ckIds = ckIds.substring(1);
			var keywords = value.replace(" ", ",");
			keywords = keywords.replace("，", ",");
			var pmcheck = isPM ? 1 : 0;
			$('#gridDraw').datagrid('options').url = get_drawurl+"?DdocName="+keywords+"&isPM="+pmcheck;
			$('#gridDraw').datagrid('clearSelections').datagrid('load',{"DlvrIds":ckIds});
		}
	}
});
$('#drawshowall').prop("checked",false).change(_hideMoreCols);//显示更多列
function _hideMoreCols(){
	var moreCols = ['xDivision','specialityName','xDlvrName','xDlvrId','xEquipmentNum','xEquipment','xapproved_NAME','xregistered_Engineer_Name','xfangan_name'];
	if($('#drawshowall').is(":checked")){
		for(var i=0;i<moreCols.length;i++) $('#gridDraw').datagrid('showColumn', moreCols[i]);
	}else for(var i=0;i<moreCols.length;i++) $('#gridDraw').datagrid('hideColumn', moreCols[i]);
}
$('#autocheck').linkbutton({//智能勾选按钮
    onClick: function () {
		var dgrows = $('#gridDraw').datagrid('getRows');
		for(var i=0;i<dgrows.length;i++){
			var drawStat = _getdrawStat(dgrows[i]);
			if($.inArray(drawStat, enablePlt)>-1){
				$('#gridDraw').datagrid('selectRow',i);
			}
		}
    }
});
$('#gridDraw').datagrid({//相关图纸列表
	onLoadError:function(){
		$.messager.alert("加载失败","从ECM服务器取数失败，请检查后重试或联系系统管理员。","error");
	},
	data:[],
	method: 'post',
	idField: 'id',
	rownumbers: true, singleSelect: false, pagination: true, pageList:[10,20,30,50],
	fit: true,toolbar:'#drawtb',border:false,pageSize: 20,striped:true,
	onLoadSuccess: function(){
		$('#gridDraw').datagrid('clearSelections');
	},
	frozenColumns: [[
		{ field: 'ck', checkbox: true },
		{ field: 'drawName', title: '名称', styler: function(value,row,index){
			if($('#gridDrawDS').datagrid('getRowIndex', row.id) > -1)
				return 'background-color:lightgray;color:white';
			},formatter: function(value,row,index){return _recoverCADtext(value);}
		}]],
	columns: [[
		{ field: 'drawNum', title: '图号',formatter: function(value,row,index){
			return value+" <a href='javascript:_linkCat("+row.id+");'>查看</a>";
		}},
		{ field: 'xPltStatus', title: '图纸状态',formatter:function(value,row,index){
			return _getdrawStat(row);
		},styler: function(value,row,index){
			var drawStat = _getdrawStat(row);
			if($.inArray(drawStat,enablePlt)==-1)
				return 'background-color:lightgray;color:white';
		} },
		{ field: 'reviseNum', title: '版本' },
		{ field: 'xSize', title: '规格' },
		{ field: 'xDivision', title: '子项', hidden: true},
		{ field: 'specialityName', title: '专业', hidden: true},
		{ field: 'xDlvrName', title: '工作包', hidden: true},
		{ field: 'xdesigned_NAME', title: '设计人'},
		{ field: 'xchecked_NAME', title: '校核人'},
		{ field: 'xreviewed_NAME', title: '审核人'},
		{ field: 'xDlvrId', title: '工作包号', hidden: true},
		{ field: 'xEquipmentNum', title: '设备编码', hidden: true},
		{ field: 'xEquipment', title: '设备名称', hidden: true},
		{ field: 'xapproved_NAME', title: '审定人', hidden: true},
		{ field: 'xregistered_Engineer_Name', title: '注册工程师', hidden: true},
		{ field: 'xfangan_name', title: '方案设计人', hidden: true}]]
});

$('#gridViewDS').datagrid({//待文印图纸包列表
	url:"http://"+window.location.host+"/api/draw/searchTmpPlts?projectId="+prjid+"&phaseCode="+phaseCode+"&tw=1",
	method: 'get',
	idField: 'did',
	rownumbers: true, singleSelect: false, //pagination: true,pageList:[5,10,20,30],pageSize: 5,
	fit: true,toolbar:'#viewdstb',border:false,striped:true,
	remoteSort:false,
	onLoadSuccess: function(){
		$('#gridViewDS').datagrid('clearSelections');
	},
	frozenColumns: [[
		{field:'ck',checkbox:true},
		{ field: 'createdTime', title: '创建时间',sortable:true} ]],
	columns: [[
		{ field: 'xSpecialty', title: '专业'},
		{ field: 'did', title: '子项/设备名称',formatter: function(value,row,index){
			return _DivEquip(row);
		}},
		{ field: 'drawNum', title: '图纸目录编号',formatter: function(value,row,index){
			return value+" <a href='javascript:_linkCat("+row.did+");'>查看</a>";
		}
		},
		{ field: 'reviseNum', title: '版本'},
		{ field: 'createdByName', title: '创建人'},
		{ field: 'xDivisionNum', title: '子项号',hidden:true},
		{ field: 'xDivision', title: '子项名称',hidden:true},
		{ field: 'xEquipmentNum', title: '设备编码',hidden:true},
		{ field: 'xEquipment', title: '设备名称',hidden:true},
		{ field: 'xDesignPhaseName', title: '阶段',hidden:true},
		{ field: 'projectSName', title: '项目名称',hidden:true}]]
}).datagrid('sort', {sortName: 'createdTime',sortOrder: 'desc'});
function _linkCat(did){
	window.open(ecmurl+"/cs/idcplg?IdcService=DOC_INFO&dID="+did,'_blank');
}
$('#viewdsshowall').prop("checked",false).change(_hideMoreCols_DSview);//图纸包显示更多列
function _hideMoreCols_DSview(){
	var moreCols = ['xDesignPhaseName','projectSName'];
	if($('#viewdsshowall').is(":checked")){
		for(var i=0;i<moreCols.length;i++) $('#gridViewDS').datagrid('showColumn', moreCols[i]);
	}else for(var i=0;i<moreCols.length;i++) $('#gridViewDS').datagrid('hideColumn', moreCols[i]);
}
$('#delcat').linkbutton({//删除图纸包按钮
	iconCls: 'icon-cancel',
	plain: true,
	onClick: function () {
		var dgrows = $('#gridViewDS').datagrid('getSelections');
		if(dgrows.length==0){
			$.messager.alert("操作提示", "未选中图纸包。", "warning");
		}else{
			$.messager.confirm('操作提示', '将删除 '+dgrows.length+' 个待文印图纸包，是否继续？', function (r) {
				if (r) {
					var delcat = dgrows[0].did;
					if(dgrows.length>1)
						for(var i=1;i<dgrows.length;i++){
							delcat += ","+dgrows[i].did;
						}
					$.ajax({
						url: "http://"+window.location.host+"/api/draw/deleteTmpPlts",
						contentType: 'application/x-www-form-urlencoded',
						dataType: "json",   //返回格式为json
						data: {dids: delcat},
						type: "POST",   //请求方式
						beforeSend: function () {$('#loaddiv').window('open');},
						success: function (data) {
							$('#gridViewDS').datagrid('reload').datagrid('sort', {sortName: 'createdTime',sortOrder: 'desc'});
							$('#loaddiv').window('close');
						},
						error: function (data) {
							$('#loaddiv').window('close');
							$.messager.alert("删除失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
						}
					});
				}
			});
		}
	}
});
$('#gocat').menubutton({//图纸包继续菜单按钮
	iconCls: 'icon-ahead',
	plain: true,
	menu: '#menuCat',
	hasDownArrow: false
});
$('#menuCat').menu('appendItem', {
	text: '图纸新晒',
	onclick: function () {
		var hasCol = _gopublist('#gridViewDS',0);
		if(hasCol) alert("请确认选中图纸包后提交图纸新晒申请。");
	}
}).menu('appendItem', {
	text: '文本文印附图',
	onclick: function () {
		var hasCol = _gopublist('#gridViewDS',3);
		if(hasCol) alert("请确认选中图纸包后提交文本文印申请。");
	}
});



$('#gridreplt').datagrid({//增晒图纸明细
	data:[],
	method: 'get',
	idField: 'id',
	rownumbers: true, singleSelect: false,
	fit: true,toolbar:'#replttb',border:false,striped:true,
	onLoadSuccess: function(){
		$('#gridreplt').datagrid('clearSelections');
	},
	columns: [[
		{ field: 'specialityName', title: '专业'},
		{ field: 'xDivision', title: '子项/设备名称',formatter:function(value,row,index){
			return _DivEquip(row);
		}},
		{ field: 'drawnums', title: '图号'},
		{ field: 'drawcount', title: '自然张数'},
		{ field: 'xSize', title: '折A1张数'},
		{ field: 'descr', title: '备注'}]]
});

$('#delreplt').linkbutton({//删除增晒图按钮
	iconCls: 'icon-cancel',
	plain: true,
	onClick: function () {
		var dgrows = $('#gridreplt').datagrid('getSelections');
		if(dgrows.length==0){
			$.messager.alert("操作提示", "未选中图纸。", "warning");
		}else{
			$.messager.confirm('操作提示', '将当前表格中勾选的行移除，是否继续？', function (r) {
				if (r) {
					var delIds = [];
					for(var i=0;i<dgrows.length;i++)
						delIds[delIds.length] = dgrows[i].id;
					var newdg = [];
					var oldrows = $('#gridreplt').datagrid('getRows');
					for(var i=0;i<oldrows.length;i++){
						if($.inArray(oldrows[i].id,delIds)==-1)
							newdg[newdg.length]=oldrows[i];
					}
					$('#gridreplt').datagrid('loadData', newdg);
				}
			});
		}
	}
});
$('#goreplt').menubutton({//增晒继续菜单按钮
	iconCls: 'icon-ahead',
	plain: true,
	menu: '#menureplt',
	hasDownArrow: false
});
$('#menureplt').menu('appendItem', {
	text: '对内收费图纸增晒',
	onclick: function () {
		var hasCol = _gopublist('#gridreplt',1,true);
		if(hasCol) alert("请确认增晒/加印图纸明细后提交对内收费图纸增晒申请。");
	}
}).menu('appendItem', {
	text: '对外收费图纸增晒',
	onclick: function () {
		var hasCol = _gopublist('#gridreplt',2,true);
		if(hasCol) alert("请确认增晒/加印图纸明细后提交对外收费图纸增晒申请。");
	}
});

function _gopublist(dg,index,nocheck){//跳转文印分类列表//校验当前表格是否有选中行
	if(!nocheck){
		var selectedRow = $(dg).datagrid('getSelections');
	}else var selectedRow = $(dg).datagrid('getRows');
	if(selectedRow.length==0){
		var mess = nocheck ? '当前表格中无图纸。' : '当前表格中未勾选图纸。';
		$.messager.alert('操作提示', mess, 'error');
		return false;
	}else{
		$('#pubdraw_layout').layout('collapse', 'west');
		$('#wbs_layout').layout('collapse', 'west');
		$('#draw_layout').layout('collapse', 'west');
		$('#oalistgrid').datagrid('selectRow',index);
		if(index==3) $('#withdrawpub').combobox('setValue', 0);
		return true;
	}
}
function _resizeiframe(hide){
	if(hide){
		$('#iframepage').height(0);
		$('#iframepage').width(0);
	}else{
		var fparent = $('#iframepage').parent();
		$('#iframepage').height(fparent.height());
		$('#iframepage').width(fparent.width());
	}
}
function _addworkflow(workflowid){//发起纯OA流程
	window.open('http://'+OAurl+'/interface/addworkflow.jsp?workflowid='+workflowid+'&workcode='+userNum,"_blank","location=no");
}
function _linkOA(requestId){//跳转OA表单
	window.open("http://"+OAurl+"/workflow/request/ViewRequest.jsp?requestid="+requestId,'_blank','location=no');
}
function _publishrec(requestId){//查找相关文印流程
	alert("弹出明细行中包含当前文本的文印流程列表");
	//window.open("http://"+OAurl+"/workflow/request/ViewRequest.jsp?requestid="+requestId,'_blank','location=no');
}
$('#adddrawbtn').linkbutton({//添加图纸按钮
	iconCls: 'icon-add',
	plain:true,
    onClick: function () {
		var addrow = $('#gridDraw').datagrid('getSelections');
		//如非项目经理文印高阶段文本
		//校验所选图纸工作包id在工作包树是否属相同父节点
		//如图纸目录表为空则改写tmpSpeId，否则校验该专业id是否与当前tmpSpeId相同
		if(addrow.length>0){
			/* if(!isPM){
				var selSpe = null;
				var selSpeSqe = null;
				for(var i=0;i<addrow.length;i++){
					var SpeId = _getSpenode(addrow[i].xDlvrId);
					if(!selSpe){
						selSpe = SpeId;
					}else if(selSpe != SpeId){
						$.messager.alert("操作提示", "不可添加跨子项或专业的图纸，请检查后重试。", "error");
						return;
					}
					if(!selSpeSqe){
						selSpeSqe = addrow[i].xspecialtySeq;
					}else if(selSpeSqe != addrow[i].xspecialtySeq){
						$.messager.alert("操作提示", "不可添加跨专业孙项的图纸，请检查后重试。", "error");
						return;
					}
				}
				if($('#gridDrawDS').datagrid('getRows').length == 0 || !tmpSpeID || !tmpSpeSeq){
					tmpSpeID = selSpe;
					tmpSpeSeq = selSpeSqe;
				}else if(tmpSpeID != selSpe){
					$.messager.alert("操作提示", "不可添加跨子项或专业的图纸，请检查后重试。", "error");
					return;
				}else if(tmpSpeSeq != selSpeSqe){
					$.messager.alert("操作提示", "不可添加跨专业孙项的图纸，请检查后重试。", "error");
					return;
				}
			} */
			_addDraw(addrow);
		}else $.messager.alert('操作提示', '相关图纸列表中未勾选图纸。', 'error');
    }
});
$('#deldrawbtn').linkbutton({//去除图纸按钮
	iconCls: 'icon-remove',
	plain:true,
    onClick: function () {
		var delArr = $('#gridDrawDS').datagrid('getSelections');
		if(delArr.length>0){
			$.messager.confirm('操作提示', '将当前表格中勾选的图纸移除，是否继续？', function (r) {
				if (r) {
					var delIds = [];
					for(var i=0;i<delArr.length;i++)
						delIds[delIds.length] = delArr[i].id;
					var newdg = [];
					var oldrows = $('#gridDrawDS').datagrid('getRows');
					for(var i=0;i<oldrows.length;i++){
						if($.inArray(oldrows[i].id,delIds)==-1)
							newdg[newdg.length]=oldrows[i];
					}
					$('#gridDrawDS').datagrid('loadData', newdg);
					$('#gridDrawDS').datagrid('clearSelections').datagrid('autoSizeColumn');
					for(var i=0;i<delIds.length;i++){
						var line = $('#gridDraw').datagrid('getRowIndex', delIds[i]);
						if(line>-1) $('#gridDraw').datagrid('refreshRow', line);
					}
				}
			});
		}else $.messager.alert('操作提示', '当前表格中未勾选图纸。', 'error');
    }
});
$('#templatecombo').combobox({   //选择图纸目录模板 
	url:"http://"+window.location.host+"/api/draw/drawsCatalogTem?projectId="+prjid,
	method:'get',
	editable:false,
	panelHeight: 'auto',
	valueField:'templateCode',    
	textField:'templateName',
	onLoadSuccess:function(){
		$('#templatecombo').combobox('setValue',$('#templatecombo').combobox('getData')[0].templateCode);
	}
}); 
$('#confirmCatck').prop("checked",false).change(_enableCatbtn);
function _enableCatbtn(){
	if($('#confirmCatck').is(":checked")){
		$('#confirmCatbtn').linkbutton('enable');
	}else $('#confirmCatbtn').linkbutton('disable');
}
$('#confirmCatbtn').linkbutton({//提交目录按钮
	disabled:true,
	plain:true,
	onClick: function () {
		_resizeiframe(true);
		$.messager.confirm('操作提示', '将生成新的待文印图纸包，是否继续？', function (r) {
			if (r) {
				var templateCode = $('#templatecombo').combobox('getValue');
				$.ajax({
					url: "http://"+window.location.host + "/api/draw/submitCatHeaders",
					contentType: 'application/x-www-form-urlencoded',
					data: {
						id: catId,
						templateCode: templateCode,
						tw: 1,
						xmlid: p_xml_id,
						tempid: p_rtf_id,
						docType: 'PJT-DRAW'
					},
					type: "POST",   //请求方式
					beforeSend: function () {
						_resizeiframe(true);
						$('#loaddiv').window('open');
					},
					success: function (data) {
						$('#loaddiv').window('close');
						_resizeiframe(false);
						if ("0000" == data.returnCode) {
							$('#pdfdiv').window('close');
							$.messager.show({title:'提示', msg:'已生成新的待文印图纸包。'});
							$('#gridViewDS').datagrid('reload').datagrid('sort', {sortName: 'createdTime',sortOrder: 'desc'});
							$('#dstabs').tabs('select',1);
						} else $.messager.alert("生成失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
					},
					error: function (data) {
						$('#loaddiv').window('close');
						_resizeiframe(false);
						$.messager.alert("生成失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
					}
				});
			}
		}).panel('options').onBeforeClose = function(){_resizeiframe(false);};
	}
});
function _getSpenode(xDlvrId){
	var worknode = $('#divtree').tree('find',xDlvrId);
	var Spenode = $('#divtree').tree('getParent', worknode.target);
	return Spenode.id;
}
$('#newDSbtn').menubutton({//选中图纸列表菜单按钮
	iconCls: 'icon-ahead',
	plain: true,
	menu: '#menuDS',
	hasDownArrow: false
});
$('#menuDS').menu('appendItem', {
	text: '图纸新晒或文本文印附图',
	onclick: function () {
		_generateCatContents();
	}
}).menu('appendItem', {
	text: '图纸增晒',
	onclick: function () {
		var gridData = $('#gridDrawDS').datagrid('getRows');
		if(gridData.length>0){
			var newData = [];
			var tmpArr = [];
			var DLArr = [];
			var indexArr = [];
			for(var i=0;i<gridData.length;i++){
				if(gridData[i].xPltStatus!="已出图文印"){
					$.messager.alert("操作提示", "未出图文印的图纸不可增晒，请检查后重试。", "error");
					return;
				}
				var cut = gridData[i].drawNum.indexOf("-");
				if(cut>-1){
					var drawpnum = gridData[i].drawNum.substring(0,cut+1);
					var DLnum = gridData[i].drawNum.substring(0,cut-3);
				}else{
					var drawpnum = gridData[i].drawNum;
					var DLnum = drawpnum;
				}
				var pnumEquip = drawpnum + gridData[i].xEquipment + gridData[i].xEquipmentNum;
				if($.inArray(pnumEquip,tmpArr)==-1){
					tmpArr[tmpArr.length] = pnumEquip;
					if($.inArray(DLnum,DLArr)==-1){//所属图号在DLArr未出现过
						DLArr[DLArr.length] = DLnum;
						indexArr[indexArr.length] = {
							"index":newData.length,
							"pdrawnum":DLnum
						};
						newData[newData.length] = {
							"id":newData.length,
							"specialityName":gridData[i].specialityName,
							"xDivision":gridData[i].xDivision,
							"xEquipmentNum":gridData[i].xEquipmentNum,
							"xEquipment":gridData[i].xEquipment
						};//图纸目录行
					}
					newData[newData.length] = {
						"id":newData.length,
						"specialityName":gridData[i].specialityName,
						"xDivision":gridData[i].xDivision,
						"xEquipmentNum":gridData[i].xEquipmentNum,
						"xEquipment":gridData[i].xEquipment,
						"drawnums":drawpnum,
						"drawcount":0,
						"xSize":0
					};//图纸包行
					var serial = [];
					for(var j=i;j<gridData.length;j++){
						if(gridData[j].drawNum.indexOf(drawpnum)==0){
							newData[newData.length-1].drawcount ++;
							newData[newData.length-1].xSize +=  _countA1(gridData[j].xSize);
							serial[serial.length] = gridData[j].drawNum.substring(drawpnum.length);
						}
					}
					serial.sort(sortNumber);
					newData[newData.length-1].drawnums += serial[0];
					if(serial.length>1)
						for(var j=1;j<serial.length;j++){
							if(j==serial.length-1 || serial[j+1]-serial[j-1]!=2){
								if(j>1 && serial[j]-serial[j-2]==2){
									newData[newData.length-1].drawnums += "～" + serial[j];
								}else newData[newData.length-1].drawnums += "," + serial[j];
							}
						}
				}
			}
			if($('#adddssinglepub').is(":checked")){
				var selerows = $('#gridDrawDS').datagrid('getSelections');
				for(var i=0;i<selerows.length;i++){
					newData[newData.length] = {
						"id":newData.length,
						"specialityName":selerows[i].specialityName,
						"xDivision":selerows[i].xDivision,
						"xEquipmentNum":selerows[i].xEquipmentNum,
						"xEquipment":selerows[i].xEquipment,
						"drawnums":selerows[i].drawNum,
						"drawcount":1,
						"xSize":_countA1(selerows[i].xSize),
						"descr":_recoverCADtext(selerows[i].drawName)+" <a href='javascript:_linkCat("+selerows[i].id+");'>查看</a>"
					}
				}
			}
			$.ajax({
				url: "http://"+window.location.host + "/api/draw/DrawSize",
				contentType: "application/json",
				data: JSON.stringify(indexArr),
				type: "POST",   //请求方式
				beforeSend: function () {$('#loaddiv').window('open');},
				success: function (data) {
					$('#loaddiv').window('close');
					for(var i=0;i<data.length;i++){
						newData[data[i].index]["drawnums"] = data[i].drawnum;
						newData[data[i].index]["drawcount"] = data[i].pages;
						newData[data[i].index]["xSize"] = _countA1(data[i].xsize);
						newData[data[i].index]["descr"] = "第"+data[i].version_num+"版图纸目录 <a href='javascript:_linkCat("+data[i].did+");'>查看</a>";
					}
					$('#gridreplt').datagrid('loadData', newData);
					$('#dstabs').tabs('select',2);
				},
				error: function (data) {
					$('#loaddiv').window('close');
					$.messager.alert("操作提示","获取图纸目录数据请求失败，请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
				}
			});
/* 			ajax传indexArr获取后端图纸目录数据回写newData，过程中$('#loaddiv').window('open');
			获取图纸目录数据接口
			入参：[{"index":0,"pdrawnum":"2A13A03DD0101CT1"},...]
			返回：[{index,图号,自然张数,xSize},...] */
		}else $.messager.alert('操作提示', '当前表格中无图纸。', 'error');
	}
});
function _DivEquip(row){//生成子项/设备名称字段
	var result = row.xDivision;
	if(row.xEquipmentNum) result += " "+row.xEquipmentNum;
	if(row.xEquipment) result += " "+row.xEquipment;
	return result;
}
function _countA1(input){
	var base = [{"xSize":"A0","scale":2},{"xSize":"A1","scale":1},{"xSize":"A2","scale":0.5},{"xSize":"A3","scale":0.25},{"xSize":"A4","scale":0.125}];
	var Acut = input.indexOf("A");
	var k = input.substring(0,Acut);
	if(k=='') k = 1;
	var xSize = input.substring(Acut);
	for(var i=0;i<base.length;i++){
		if(xSize==base[i].xSize) return k * base[i].scale;
	}
}
function sortNumber(a,b){
	return a - b;
}

function _generateCatContents() {//新晒菜单项
	var gridData = $('#gridDrawDS').datagrid('getRows');
	if(gridData.length>0){
		$.messager.confirm('操作提示', '将生成新一版的图纸目录，请确认是否继续？', function (r) {
			if (r) {
				var dids = gridData[0].id;//遍历校验图纸是否属于同一父节点专业且专业孙项、设备名称、编码相同
				var SpeId = _getSpenode(gridData[0].xDlvrId);
				var Speseq = gridData[0].xspecialtySeq+gridData[0].xEquipment+gridData[0].xEquipmentNum;
				var catMode = 0;
				if(gridData.length>1)
					for(var i=1;i<gridData.length;i++){
						if(_getSpenode(gridData[i].xDlvrId) != SpeId || gridData[i].xspecialtySeq+gridData[i].xEquipment+gridData[i].xEquipmentNum != Speseq)
							catMode = 1;
						dids += ","+gridData[i].id;
					}
				if((!isPM || !isHighphase) && catMode == 1){
					$.messager.alert("操作提示", "非项目经理、非高阶段不可跨子项、专业、专业孙项及设备出图，请检查后重试。", "error");
					return;
				}
				$.ajax({
					url:"http://"+window.location.host+"/api/draw/generateCatContentsMain1",
					type:"POST",
					data:{"dids":dids,"isPM":catMode},
					beforeSend: function (){
						$('#loaddiv').window('open');
					},
					success:function(catIddata){
						catId = catIddata;
						var templateCode = $('#templatecombo').combobox('getValue');
						var val = "";
						if ("01" == templateCode)
							val = "CUXDrawContentReport";
						else if ("02" == templateCode)
							val = "CUXDrawContentENReport";
						else if ("03" == templateCode)
							val = "CUXDrawContentDNReport";
						$.getJSON("http://"+window.location.host + "/api/draw/getSourceFileId?templateCode=" + val + "&proId=" + prjid, function (data) {
							var i=0, sec = 30;
							$('#confirmCatlb').html("请耐心等待 <a id='sec'><b>"+sec+"</b></a> s，在图纸目录生成并阅读目录内容后，再确认提交，否则后果自负");
							$('#confirmCatck').attr("disabled","disabled").prop("checked",false);
							$('#confirmCatbtn').linkbutton('disable');
							var int=window.setInterval(function(){
								if(i==1){
									p_xml_id = data.p_xml_id;
									p_rtf_id = data.p_rtf_id;
									$("#iframepage").attr('src',ecmurl + "/rep/?" + "xmlid=" + data.p_xml_id + "&tempid=" + data.p_rtf_id + "&argsname=P_HEADER_ID&argsvalue=" + catId);
									$('#loaddiv').window('close');
									$('#pdfdiv').window('open').panel('maximize');
								}else if(i==0) $("#iframepage").attr('src','sec.html');
								if(i<sec){
									$('#sec').html("<b>"+(sec - i)+"</b>");
									i++;
								}else{
									int=window.clearInterval(int);
									$('#confirmCatlb').html("图纸目录生成已成功，已阅读了目录内容，现确认提交，责任自负");
									$('#confirmCatck').removeAttr("disabled");
								}
							},1000);
						});
					},
					error:function(data){
						$('#loaddiv').window('close');
						$.messager.alert("生成失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
					}
				});
			}
		});
	}else $.messager.alert('操作提示', '当前表格中无图纸。', 'error');
}

$('#oalistgrid').datalist({ 
    url:'headPrj.json',
	method:'get',
    fit:true,striped:true,
	rownumbers: true,
	lines: true,
	border: false,
	textFormatter: function(value,row,index){
		var datatext = row.workflow[0].name;
		if(row.workflow.length>1)
			for(var i=1;i<row.workflow.length;i++)
				datatext += '/<br>'+row.workflow[i].name;
		return datatext;//'<img src="../../themes/icons/enter.png" /> ' + datatext;
	},
	onSelect:function(index, row){
		_loaddg();
		if(index==3 || index==4 || index==5){
			$('#publayout').layout('expand','south');
			if(index==4 || index==5){
				$('#withdrawdiv').hide();
			}else $('#withdrawdiv').show();
		}else{
			$('#publayout').layout('collapse','south');
			$('#withdrawdiv').hide();
		}
	}
});
$('#publayout').layout('panel', 'south').panel({
	onExpand: function () {
		$('#repock').show();
		if(!repoloaded) _loadrepo();
	},
	onCollapse: function () {
		$('#repock').hide();
	}
});
$('#repoprjall').prop("checked",false).change(_loadrepo);//不限项目查看文本
var repoloaded = false;
function _loadrepo(){
	if($('#repoprjall').is(":checked")){
		var dgurl = "http://"+window.location.host + "/api/naviWbpsRest/printAllOa?formId=432";
	}else var dgurl = "http://"+window.location.host + "/api/naviWbpsRest/printAllOa?formId=432"+"&projectId="+prjid;
	$("#oadatagrid").datagrid({//文本流程列表
		url:dgurl,
		pagination: true, pageList:[5,10,15,20],pageSize:5,
		onLoadSuccess:function(){$("#oadatagrid").datagrid('clearSelections');},
		rownumbers: true,
		border: false,
		singleSelect:false,
		method: 'get',
		//fitColumns:true,
		fit: true,striped:true,
		idField: 'requestId',
		onLoadSuccess:function(){
			repoloaded = true;
			$("#oadatagrid").datagrid('clearSelections');
		},
		frozenColumns: [[
			{field:'ck',checkbox:true},
			{ field: 'pltStatus', title: '文印状态'},
			{ field: 'createDate', title: '创建日期' },
			{ field: 'requestId', title: '流程ID',formatter: function(value,row,index){
				return value+" <a href='javascript:_linkOA("+row.requestId+");'>查看</a>";
			} }]],
		columns:[[
			{ field: 'xmmc', title: '项目名称',hidden:!$('#repoprjall').is(":checked")},
			{ field: 'sjjd', title: '设计阶段' },
			{ field: 'rwlx', title: '任务类型' },
			{ field: 'requestName', title: '流程标题'},
			{ field: 'createBy', title: '创建人' },
			{ field: 'status', title: '当前状态' },
			{ field: 'currentPerson', title: '当前操作者'}]]
	});
}
$('#viewprjall').prop("checked",false).change(_loaddg);//不限项目查看文印流程
function _loaddg(){
	var listrows = $('#oalistgrid').datagrid('getSelections');
	if(listrows.length>0){
		var row = listrows[0];
		if($('#viewprjall').is(":checked")){
			var dgurl = "http://"+window.location.host + row.url;
		}else var dgurl = "http://"+window.location.host + row.url+"&projectId="+prjid;
		$("#pubdg").datagrid({
			url:dgurl,
			pagination: true, pageList:[10,20,30,50],pageSize:20,
			//data:row.data,
			rownumbers: true,
			border: false,toolbar:'#pubdgtb',
			singleSelect:true,
			method: 'get',
			//fitColumns:true,
			fit: true,striped:true,
			idField: 'requestId',
			frozenColumns: [[
				{ field: 'createDate', title: '创建日期' },
				{ field: 'requestId', title: '流程ID',formatter: function(value,row,index){
					return value+" <a href='javascript:_linkOA("+row.requestId+");'>查看</a>";
				} }]],
			columns:[[
				{ field: 'xmmc', title: '项目名称',hidden:!$('#viewprjall').is(":checked")},
				{ field: 'wynr', title: '文印内容'},
				{ field: 'major', title: '专业',hidden:row.noMajor},
				{ field: 'requestName', title: '流程标题'},
				{ field: 'createBy', title: '创建人' },
				{ field: 'status', title: '当前状态'},
				{ field: 'currentPerson', title: '当前操作者'}]]
		}).datagrid('clearSelections');
	}
}
$('#adddsshowall').prop("checked",false).change(_hideMoreCols_DS);//新增图纸包之图纸列表显示更多列
function _hideMoreCols_DS(){
	var moreCols = ['xDivision','specialityName','xDlvrName','xDlvrId','xEquipmentNum','xEquipment','xapproved_NAME','xregistered_Engineer_Name','xfangan_name'];
	if($('#adddsshowall').is(":checked")){
		for(var i=0;i<moreCols.length;i++) $('#gridDrawDS').datagrid('showColumn', moreCols[i]);
	}else for(var i=0;i<moreCols.length;i++) $('#gridDrawDS').datagrid('hideColumn', moreCols[i]);
}
$('#gridDrawDS').datagrid({//新增图纸包之图纸列表
	method: 'get',
	idField: 'id',
	rownumbers: true, singleSelect: false, pagination: false,
	fit: true,toolbar:'#adddstb',border:false,striped:true,
	remoteSort:false,
	frozenColumns: [[
		{ field: 'ck', checkbox: true },
		{ field: 'drawName', title: '名称',formatter: function(value,row,index){return _recoverCADtext(value);}}]],
	columns: [[
		{ field: 'drawNum', title: '图号' ,sortable:true,sorter:function(a,b){  
			var acut = a.indexOf("-");
			var bcut = b.indexOf("-");
			var afr = acut>-1 ? a.substring(0,acut):a;
			var aend = acut>-1 ? a.substring(acut+1):a;
			var bfr = bcut>-1 ? b.substring(0,bcut):b;
			var bend = bcut>-1 ? b.substring(bcut+1):b;
			if(afr==bfr){
				return (1*aend>1*bend ? -1:1);
			}else return (afr>bfr ? -1:1);
		},formatter: function(value,row,index){
			return value+" <a href='javascript:_linkCat("+row.id+");'>查看</a>";
		}},
		{ field: 'xPltStatus', title: '图纸状态',formatter:function(value,row,index){
			return _getdrawStat(row);
		} },
		{ field: 'reviseNum', title: '版本' },
		{ field: 'xSize', title: '规格' },
		{ field: 'xDivision', title: '子项', hidden: true},
		{ field: 'specialityName', title: '专业', hidden: true},
		{ field: 'xDlvrName', title: '工作包', hidden: true},
		{ field: 'xdesigned_NAME', title: '设计人'},
		{ field: 'xchecked_NAME', title: '校核人'},
		{ field: 'xreviewed_NAME', title: '审核人'},
		{ field: 'xDlvrId', title: '工作包号', hidden: true},
		{ field: 'xEquipmentNum', title: '设备编码', hidden: true},
		{ field: 'xEquipment', title: '设备名称', hidden: true},
		{ field: 'xapproved_NAME', title: '审定人', hidden: true},
		{ field: 'xregistered_Engineer_Name', title: '注册工程师', hidden: true},
		{ field: 'xfangan_name', title: '方案设计人', hidden: true}]],
	onLoadSuccess:function(){
		$('#gridDrawDS').datagrid('enableDnd');
	}
});
function _getdrawStat(row){
	if(!row.xWpApprStatus){
		var result = "未校审";
	}else if(row.xWpApprStatus!="校审通过"){
		var result = row.xWpApprStatus;
	}else{
		if(!row.xCountersignStatus){
			if(!row.xPltStatus){
				var result = row.xWpApprStatus;
			}else var result = row.xPltStatus;
		}else if(row.xCountersignStatus!="对图通过"){
			var result = row.xCountersignStatus;
		}else{
			if(!row.xPltStatus){
				var result = row.xCountersignStatus;
			}else var result = row.xPltStatus;
		}
	}
	return result;
}
function _reChildren(Obj, speArr, result){//递归重构对象
	for(var i=0;i<Obj.length;i++){
		if(Obj[i]["level"] != "spe" || $.inArray(Obj[i]["majorCode"],speArr) > -1){
			result[result.length] = {};
			for(var key in Obj[i]){
				if(Obj[i]["level"] != "spe" && key != "speChildren"){
					result[result.length-1][key] = key == "children" ? [] : Obj[i][key];
				}else if(Obj[i]["level"] == "spe" && key != "speChildren" && key != "children"){
					result[result.length-1][key] = Obj[i][key];
				}else if(Obj[i]["level"] == "spe" && key == "speChildren"){
					result[result.length-1]["children"] = [];
					for(var j=0;j<Obj[i][key].length;j++){
						/* var isCH = false;
						if(!isPM){//如标准模式则非项目经理、专业负责人仅自己设计工作包可见
							for(var k=0;k<specialty.length;k++){
								if($.inArray(perID,specialty[k].chgrNum)>-1){
									isCH = true;//校验是否当前专业负责人
									break;
								}
							}
						}
						if((!$('#modecheck').is(":checked") || (isPM || isCH || Obj[i][key][j].designNum == perID)) && Obj[i][key][j].worktypeID == "01"){ */
						if(Obj[i][key][j].worktypeID == "01"){
							result[result.length-1]["children"][result[result.length-1]["children"].length] = Obj[i][key][j];
						}
					}
				}
			}
			if(Obj[i].children && result[result.length-1].children){
				_reChildren(Obj[i].children, speArr, result[result.length-1].children);
			}
		}
	}
}
function _getDrawPack(listindex,drawpks,general,selerows){//获取打印包下载路径
	var drawpkids = '';
	for(var i=0;i<drawpks.length;i++) drawpkids += drawpks[i].did+",";
	drawpkids = drawpkids.substr(0,drawpkids.length-1);
	$.ajax({
		url: "http://"+window.location.host +"/api/draw/generatePltOrderMain",
		type: "POST",
		data:{dids:drawpkids,pltCategory: "PLT_NEW_PRINT"},
		contentType: "application/x-www-form-urlencoded",
		beforeSend:function(){$('#loaddiv').window('open');},
		success: function (PltOrder) {
			$.ajax({
				url: "http://" + window.location.host +"/api/draw/searchDrawPrintInfo?id="+PltOrder,
				type: "get",
				contentType: "application/json",
				success: function (PltInfo) {
					$.ajax({
						url: "http://" + window.location.host +"/api/draw/getZipFileByEcm",
						type: "POST",
						data:{plt_order_header_id:PltOrder,plt_num:PltInfo.plt_Num,plt_category:"PLT_NEW_PRINT"},
						contentType: "application/x-www-form-urlencoded",
						success: function (result) {
							$('#loaddiv').window('close');
							//分drawDoc组装post data、区分url,执行ajax
							general.zipurl = result.filePath.replace("http://wcc.nerin.com:16200",ecmurl);;
							$.ajax({
								url: "http://" + window.location.host +"/api/draw/searchDrawprintLines?id="+PltOrder,
								type: "get",
								contentType: "application/json",
								success: function (printLines) {
									var DrawList = [];
									for(var i=0;i<drawpks.length;i++){
										var drawcount = 0;
										var xSize = 0;
										for(var j=0;j<printLines.length;j++){
											if(drawpks[i].drawNum==printLines[j].ddocname){
												for(var k=j;k<printLines.length;k++){
													if(i==drawpks.length-1 || drawpks[i+1].drawNum!=printLines[k].ddocname){
														drawcount += printLines[k].commQty*1;
														xSize += printLines[k].commQty*printLines[k].stdQty;
													}else break;
												}
											}
										}
										DrawList[i] = {
											//"specialityName": drawpks[i].xSpecialty,
											//"PltOrder":PltOrder,
											"divEquip" : _DivEquip(drawpks[i]),
											"drawnums": drawpks[i].drawNum,
											"drawcount": drawcount,
											"xSize": xSize,
											"plancnDrawingNum":plancnDrawingNum,
											"plandrawingNum":plandrawingNum
										};
									}
									general.pltOrder = PltOrder;
									general.detail = DrawList;
									if(listindex==3){//文本文印需再转生成文本明细	
										_generateDocPlt(listindex,selerows,general,"hasDraw");
									}else{//图纸新晒
										general.zy = drawpks[0].xSpecialty;
										general.docList = [];
										$.ajax({
											url: "http://"+window.location.host + "/api/naviWbpsRest/printSubmit?workflowId=4597",
											contentType: "application/json",
											data: JSON.stringify(general),
											type: "POST",   //请求方式
											beforeSend: function () {$('#loaddiv').window('open');},
											success: function (data) {
												$('#loaddiv').window('close');
												if(data.status=="S"){
													$('#formuser').window('close');
													$('#gridViewDS').datagrid('reload').datagrid('sort', {sortName: 'createdTime',sortOrder: 'desc'});
													_loaddg();
													window.open(data.url, "_blank");
												}else $.messager.alert("操作提示","发起文印流程失败，请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
											},
											error: function (data) {
												$('#loaddiv').window('close');
												$.messager.alert("操作提示","发起文印流程失败，请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
											}
										});
									}
								},
								error:function(data){
									$('#loaddiv').window('close');
									$.messager.alert("操作提示","未能获取文印单图纸明细，请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
									return false;
								}
							});
						},
						error:function(data){
							$('#loaddiv').window('close');
							$.messager.alert("操作提示","未能获取打印包地址，请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
							return false;
						}
					});
				},
				error:function(data){
					$('#loaddiv').window('close');
					$.messager.alert("操作提示","未能获取文印单信息，请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
					return false;
				}
			});
		},
		error:function(data){
			$('#loaddiv').window('close');
			$.messager.alert("操作提示","未能生成文印单，请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
			return false;
		}
	});
}

function _generateDrawPlt(dg,listindex,general){//生成增晒图纸明细含计划份数
	var drawreplt = $(dg).datagrid('getRows');//允许无图纸明细发起增晒
	var DrawList = [];
	var nr = [];
	for(var i=0;i<drawreplt.length;i++){
		var match = false;
		if(nr.length>0){
			for(var j=0;j<nr.length;j++){
				if(nr[j].spe == drawreplt[i].specialityName){
					match = true;
					var div = _DivEquip(drawreplt[i]);
					if($.inArray(div,nr[j].div)==-1) nr[j].div[nr[j].div.length] = div;
					break;
				}
			}
		}
		if(!match){
			nr[nr.length] = {
				"spe":drawreplt[i].specialityName,
				"div":[_DivEquip(drawreplt[i])]
			};
		}
		DrawList[i] = {
			"specialityName": drawreplt[i].specialityName,
			"divEquip" : _DivEquip(drawreplt[i]),
			"drawnums": drawreplt[i].drawnums,
			"drawcount": drawreplt[i].drawcount,
			"xSize": drawreplt[i].xSize,
			"plancnDrawingNum":plancnDrawingNum,
			"plandrawingNum":plandrawingNum
		};
	}//校验两数组是否等效
	var nrArr = [{"spe":[nr[0].spe],"div":nr[0].div}];
	if(nr.length>1){
		for(var i=1;i<nr.length;i++){
			var match1 = false;
			for(var j=0;j<nrArr.length;j++){
				var match2 = true;
				for(var k=0;k<nr[i].div.length;k++){
					if($.inArray(nr[i].div[k],nrArr[j].div)==-1){
						match2 = false;
						break;
					}
				}
				if(match2){
					for(var k=0;k<nrArr[j].div.length;k++){
						if($.inArray(nrArr[j].div[k],nr[i].div)==-1){
							match2 = false;
							break;
						}
					}
				}
				if(match2){//匹配成功
					match1 = true;
					nrArr[j].spe[nrArr[j].spe.length] = nr[i].spe;
					break;
				}
			}
			if(!match1){//新建nrArr元素
				nrArr[nrArr.length] = {"spe":[nr[i].spe],"div":nr[i].div};
			}
		}
	}
	for(var i=0;i<nrArr.length;i++){
		var nrspe = "";
		for(var j=0;j<nrArr[i].spe.length;j++){
			nrspe += ","+nrArr[i].spe[j];
		}
		var nrdiv = "";
		for(var j=0;j<nrArr[i].div.length;j++){
			nrdiv += ","+nrArr[i].div[j];
		}
		general.wynr += ";"+nrspe.substr(1)+"-"+nrdiv.substr(1);
	}
	general.wynr = general.wynr.substr(1);
	general.detail = DrawList;
	general.docList = [];
	var wfid = listindex==1?4600:4601;
	$.ajax({
		url: "http://"+window.location.host + "/api/naviWbpsRest/printSubmit?workflowId="+wfid,
		contentType: "application/json",
		data: JSON.stringify(general),
		type: "POST",   //请求方式
		beforeSend: function () {$('#loaddiv').window('open');},
		success: function (data) {
			$('#loaddiv').window('close');
			if(data.status=="S"){
				$('#formuser').window('close');
				_loaddg();
				window.open(data.url, "_blank");
			}else $.messager.alert("操作提示","发起文印流程失败，请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
		},
		error: function (data) {
			$('#loaddiv').window('close');
			$.messager.alert("操作提示","发起文印流程失败，请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
		}
	});
}
function _generateDocPlt(listindex,selerows,general,hasDraw){//生成文本明细含计划份数
	$.getJSON("../../common/phaseList.json",function(jsondata){
		var DocList = [];
		for(var i=0;i<selerows.length;i++){//先匹配name得id，再phaseDraws得份数
			var cnDrawingNum = '';
			var drawingNum = '';
			for(var j=0;j<jsondata.length;j++){
				if(jsondata[j].name==selerows[i].sjjd || selerows[i].sjjd == "设计-"+jsondata[j].name){
					for(var k=0;k<phaseDraws.length;k++){
						if(phaseDraws[k].phaseId==jsondata[j].phaseID){
							cnDrawingNum = phaseDraws[k].cnDrawingNum;
							drawingNum = phaseDraws[k].drawingNum;
							break;
						}
					}
					break;
				}
			}
			if(cnDrawingNum == '') cnDrawingNum = phaseDraws[0].cnDrawingNum;
			if(drawingNum == '') drawingNum = phaseDraws[0].drawingNum;
			DocList[i] = {
				"requestId":selerows[i].requestId,
				"plancnDrawingNum":cnDrawingNum,
				"plandrawingNum":drawingNum
			};
		}
		general.docList = DocList;
		if(!general.detail) general.detail = [{}];
		switch(listindex){
			case 3:
				var wfid = 4602;
				break;
			case 4:
				var wfid = 4595;
				break;
			case 5:
				for(var i=0;i<selerows.length;i++){
					for(var j=0;j<i;j++){
						if(selerows[i].zgfzg!=selerows[j].zgfzg && selerows[i].zgfzg.length>0 && selerows[j].zgfzg.length>0){
							$.messager.alert("操作提示","所选文本评审（结论）流程主管（副）总工程师应唯一。","error");
							return;
						}
					}
				}
				general.zgfzg = selerows[0].zgfzg;
				var wfid = 4594;
				break;
		}
		$.ajax({
			url: "http://"+window.location.host + "/api/naviWbpsRest/printSubmit?workflowId="+wfid,
			contentType: "application/json",
			data: JSON.stringify(general),
			type: "POST",   //请求方式
			beforeSend: function () {$('#loaddiv').window('open');},
			success: function (data) {
				$('#loaddiv').window('close');
				if(data.status=="S"){
					$('#formuser').window('close');
					_loaddg();
					if(hasDraw) $('#gridViewDS').datagrid('reload').datagrid('sort', {sortName: 'createdTime',sortOrder: 'desc'});
					window.open(data.url, "_blank");
				}else $.messager.alert("操作提示","发起文印流程失败，请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
			},
			error: function (data) {
				$('#loaddiv').window('close');
				$.messager.alert("操作提示","发起文印流程失败，请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
			}
		});
	});
}
$('#addpub').linkbutton({//提交发起按钮
	iconCls: 'icon-ok',
	plain: true,
	onClick: function () {
		var publist = $('#oalistgrid').datagrid('getSelections');
		var listindex = $('#oalistgrid').datagrid('getRowIndex',publist[0]);
		switch(listindex){
			case 0://图纸新晒
				var drawpks = $('#gridViewDS').datagrid('getSelections');
				if(drawpks.length==0){
					$.messager.alert("操作提示", "未选中新晒图纸包。", "error");
					return;
				}else{
					for(var i=0;i<drawpks.length;i++){
						if(drawpks[i].xSpecialty!=drawpks[0].xSpecialty){
							$.messager.alert("操作提示", "新晒图纸包专业必须唯一。", "error");
							return;
						}
					}
				}
					//（项目经理专业除外）标题加专业、专业负责人、项目秘书、项目负责人，分管领导失效
				if(drawpks[0].xSpecialty=="项目经理"){
					$('#zychgr').combo('setValue',null).combobox("loadData", pmlist);
					$('#zychgr').combobox('select',$('#wfchgr').combo('getValue'));
				}else{
					$.ajax({
						url: "http://" + window.location.host +"/api/lev3/getProfessionListAll?projID="+prjid+"&specialty=2"+drawpks[0].xSpecialtyName,
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
							$('#loaddiv').window('close');
						},
						error: function(data){
							$('#loaddiv').window('close');
							$.messager.alert("操作提示","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
						}
					});
				}
				$('#zychgrdiv').show();
				$('#scradiv').show();
				$('#leaderdiv').hide();
				$('#formuser').window('open').panel('setTitle', "选择审批执行人员 - 专业："+drawpks[0].xSpecialty);
				break;
			case 1://对内收费增晒
			case 2://对外收费增晒
				//项目秘书、项目负责人，标题去专业、专业负责人、分管领导失效
				$('#zychgrdiv').hide();
				$('#scradiv').show();
				$('#leaderdiv').hide();
				$('#formuser').window('open').panel('setTitle', "选择审批执行人员");
				break;
			case 3://文本文印
			case 4://文本加印
			case 5://文本重印
				if($('#repoprjall').is(":checked")){
					$.messager.alert("操作提示", "文本选择不可选中不限于当前项目查看选项，请检查后重试。", "error");
					return;
				}
				var selerows = $('#oadatagrid').datagrid('getSelections');
				for(var i=0;i<selerows.length;i++){
					if(selerows[i].status!="办结" && selerows[i].status!="通过办结"){
						$.messager.alert("操作提示", "不可选中未办结的文本评审（结论）流程。", "error");
						return;
					}
				}
				if(selerows.length ==0){
					$.messager.alert("操作提示", "未选中已办结的文本评审（结论）流程。", "error");
					return;
				}else if(listindex==3){
					if($('#withdrawpub').combo('getValue')==''){
						$.messager.alert("操作提示", "未选择是否文印附图。", "error");
						return;
					}else if($('#withdrawpub').combo('getValue')==0){
						var drawpks = $('#gridViewDS').datagrid('getSelections');
						if(drawpks.length==0){
							$.messager.alert("操作提示", "未选中新晒图纸包。", "error");
							return;
						}
					}
				}
				//项目负责人、分管领导，去专业、专业负责人、项目秘书失效
				$('#zychgrdiv').hide();
				$('#scradiv').hide();
				$('#leaderdiv').show();
				$('#formuser').window('open').panel('setTitle', "选择审批执行人员");
				break;
			default://跳过选择审批执行人员直接发起流程
				_addworkflow(publist[0].workflow[0].id);
				break;
		}
	}
});
$('#btnuserapply').linkbutton({//应用按钮
	iconCls: 'icon-ok',
	onClick: function () {
		var general = {
			"wynr":"",
			"xmbh":prjNumber,
			"xmmc":prjName,
			"xmjl":pmNum
		};
		if($('#wfchgr').combo('getValue')==''){
			$.messager.alert("操作提示", "未选择执行流程审批的项目负责人。", "error");
			return;
		}else general.xmfzr = $('#wfchgr').combo('getValue');//项目负责人
		var publist = $('#oalistgrid').datagrid('getSelections');
		if(publist.length==0) return;
		var listindex = $('#oalistgrid').datagrid('getRowIndex',publist[0]);
		switch(listindex){
			case 0://图纸新晒，校验专业负责人，项目秘书
				if($('#zychgr').combo('getValue')==''){
					$.messager.alert("操作提示", "未选择执行流程审批的专业负责人。", "error");
					return;
				}else general.zyfzr = $('#zychgr').combo('getValue');//专业负责人
				if($('#scra').combo('getValue')==''){
					$.messager.alert("操作提示", "未选择执行流程审批的项目秘书。", "error");
					return;
				}else general.xmmsx = $('#scra').combo('getValue');//项目秘书
				var drawpks = $('#gridViewDS').datagrid('getSelections');
				general.wynr += drawpks[0].xSpecialty+"-"+_DivEquip(drawpks[0]);
				if(drawpks.length>1){
					for(var i=1;i<drawpks.length;i++){
						if(_DivEquip(drawpks[i])!=_DivEquip(drawpks[0]))
							general.wynr += ","+_DivEquip(drawpks[i]);
					}
				}
				_getDrawPack(listindex, drawpks,general);
				break;
			case 1://对内收费增晒
			case 2://对外收费增晒，校验项目秘书
				if($('#scra').combo('getValue')==''){
					$.messager.alert("操作提示", "未选择执行流程审批的项目秘书。", "error");
					return;
				}else general.xmmsx = $('#scra').combo('getValue');//项目秘书
				_generateDrawPlt('#gridreplt',listindex,general);
				break;
			case 3://文本文印
			case 4://文本加印
			case 5://文本重印，校验分管领导
				if($('#leader').combo('getValue')==''){
					$.messager.alert("操作提示", "未选择执行流程审批的分管领导。", "error");
					return;
				}else general.fgld = $('#leader').combo('getValue');//分管领导
				var selerows = $('#oadatagrid').datagrid('getSelections');
				for(var i=0;i<selerows.length;i++){
					if(selerows[i].requestName.indexOf("公司级评审结论流程")>-1) general.wynr += ","+selerows[i].rwlx+"文本";
					if(selerows[i].requestName.indexOf("项目专篇报告审批流程")>-1) general.wynr += ",专篇报告文本";
				}
				general.wynr = general.wynr.substr(1);
				switch(listindex){
					case 3:
						if($('#withdrawpub').combo('getValue')==0){
							general.wynr += ",附图";
							var drawpks = $('#gridViewDS').datagrid('getSelections');
							_getDrawPack(listindex,drawpks,general,selerows);
						}else _generateDocPlt(listindex,selerows,general);//直接发起OA流程
						break;
					case 4:
					case 5:
						_generateDocPlt(listindex,selerows,general);//直接发起OA流程
						break;
				}
				break;
		}
	}
});
function _getObjchildren(nodeObj, result) {//递归获取树形数据对象子节点数组（含自身）
	result[result.length] = {};
	for(var key in nodeObj) result[result.length-1][key]=nodeObj[key];
	if (nodeObj.children) {
		for(var i=0;i<nodeObj.children.length;i++)
			_getObjchildren(nodeObj.children[i], result);
	}
}
function _reWorktype(Obj, result){
	for(var i=0;i<Obj.length;i++){
		var childArr = [];
		_getObjchildren(Obj[i], childArr);
		var hasWork = false;
		for(var j=0;j<childArr.length;j++)//子节点中含工作包层级则保留
			if(childArr[j].level=="work"){
				hasWork =true;
				break;
			}
		if(hasWork){
			result[result.length] = {};
			for(var key in Obj[i]){
				if(key=="children"){
					result[result.length-1][key] = [];
				}else result[result.length-1][key] = Obj[i][key];
			}
			if(Obj[i].children && result[result.length-1].children){
				_reWorktype(Obj[i].children, result[result.length-1].children);
			}
		}
	}
}
function _addDraw(drawArr){
	var getRows = $('#gridDrawDS').datagrid('getRows');
	var hasNew = false;
	for(var i=0;i<drawArr.length;i++){//图纸状态校验
		var drawStat = _getdrawStat(drawArr[i]);
		if($.inArray(drawStat, enablePlt)==-1){
			$.messager.alert("操作提示", "不可添加图纸状态灰色的图纸，请检查后重试。", "error");
			return;
		}
	}
	for(var i=0;i<drawArr.length;i++){//id不存在则追加行
		if($('#gridDrawDS').datagrid('getRowIndex', drawArr[i].id) == -1){
			getRows.push(drawArr[i]);
			hasNew = true;
		}
		var line = $('#gridDraw').datagrid('getRowIndex', drawArr[i].id);
		$('#gridDraw').datagrid('refreshRow', line);
	}
	if(hasNew){
		$('#gridDrawDS').datagrid('loadData', getRows);
		var index = $('#gridDrawDS').datagrid('getRowIndex', drawArr[0].id);
		$('#gridDrawDS').datagrid('scrollTo', index).datagrid('autoSizeColumn');
	}
}
$.getJSON("http://" + window.location.host +"/api/naviWbpsRest/getProjRoles?projectId="+prjid, function (data) {
	pmlist = data;
	$('#wfchgr').combo('setValue',null).combobox("loadData", pmlist);
	if(pmlist.length==1) $('#wfchgr').combobox('select',pmlist[0].num);
});
$.getJSON("http://" + window.location.host +"/api/lev3/getMemberList?projID="+prjid, function (data) {
	pmNum = data.mgr.perNum;
	for(var i=0;i<data.oth.length;i++){
		if(data.oth[i].duty == "公司分管领导"){
			$('#leader').combo('setValue',null).combobox("loadData", data.oth[i].member);
			if(data.oth[i].member.length==1) $('#leader').combobox('select',data.oth[i].member[0].perNum);
		}else if(data.oth[i].duty == "项目秘书"){
			$('#scra').combo('setValue',null).combobox("loadData", data.oth[i].member);
			if(data.oth[i].member.length==1) $('#scra').combobox('select',data.oth[i].member[0].perNum);
		}
	}
});
function _recoverCADtext(input){
	var sym = [{"cad":"%%c","nor":"Ф"},{"cad":"%%p","nor":"±"},{"cad":"%%d","nor":"°"}];
	for(var i=0;i<sym.length;i++){
		input = input.split(sym[i].cad).join(sym[i].nor);
	}
	return input;
}
$('#withdrawpub').combobox({   //是否合并图纸文印 
	data:[
		{"value":0,"text":"含新晒图纸包合并文印"},
		{"value":1,"text":"只文印文本"}],
	hasDownArrow:false,
	editable:false,
	required:true,
	panelHeight: 'auto',
	valueField:'value',
	textField:'text',
	onLoadSuccess:function(){
		$('#withdrawpub').combobox('setValue', null);
	}
});
function _checkinqueue(dg, rowid){//加入文印队列勾选
	var queuecheck = $('#checkinqueue_'+dg+'_'+rowid).is(":checked");
	var line = $('#'+dg).datagrid('getRowIndex', rowid);
	$('#'+dg).datagrid('refreshRow', line);
	if(queuecheck) $('#checkinqueue_'+dg+'_'+rowid).prop("checked",true);
}