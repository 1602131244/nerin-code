var prjdrifwbsdataurl = "http://"+window.location.host+"/api/lev3/getPubDlvrList";
var get_drawurl = "http://"+window.location.host+"/api/draw/DrawList";
var get_dcurl = "http://"+window.location.host+"/api/naviWbpsRest/sjbgOa";
var POwebUrl = "http://"+window.location.host+"/pages/wbsp/code - dev/poweb/poweb.html";
var CheckReviewUrl = "http://"+window.location.host+"/pages/wbsp/code - dev/CheckReview/CheckReview.html";
var OAurl = "192.168.15.198";
var ecmurl = "http://192.168.15.252:16200";
//var pourl = "http://localhost:8090/Samples4/DesignChange";
var pourl = "http://192.168.15.91:8080/cux/DesignChange";
/* 正式环境
var OAurl = "oa.nerin.com";
var ecmurl = "http://wcc.nerin.com:16200";
var pourl = "http://ebs.nerin.com:8008/OA_HTML/cux_cooperate"; */
var majorArr,designId,checkId,reviewId,approveId,certifiedId,schemeId;
var enableChr = isAppr ? ['校审通过','对图未通过','出图文印未通过'] : ['未校审','校审未通过','校审取消','对图未通过','出图文印未通过'];
var jzyform = 347;//建筑院校审流程
var gsjform = 121;//公司级评审流程
var taskType = "文本";//由查询dlvrId返回workTypeID获取或查询requestid直接返回
var oa_erpid = null;
//var chapterId = "104118,104123,104338";
//var taskHeaderId = 9268;
//dlvrId+"文本"
//dlvrId+"图纸"
//requestid+编辑+"文本"
//requestid+编辑+"图纸"
//requestid+查看
var mode = 5;
var btntext = "提交";
$('#rootlayout').layout('panel', 'west').panel("setTitle", '图纸工作包树&nbsp;&nbsp;<a href="javascript:_searchDraw();"><img title="查找工作包关联的图纸" src="../../themes/icons/search.png"/></a>');
if(isAppr) $('#draw_layout').layout('panel', 'center').panel("setTitle", '本次会签的图纸');
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
			if($.inArray(drawStat,enableChr)==-1)
				return 'background-color:lightgray;color:white';
		} },
		{ field: 'xDivision', title: '子项', hidden: true},
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
$('#gridDrawDS').datagrid({//本次校审的图纸列表
	method: 'post',
	idField: 'id',
	rownumbers: true, singleSelect: false, pagination: false,
	fit: true,toolbar:'#adddstb',border:false,striped:true,
	remoteSort:false,
	frozenColumns: [[
		{ field: 'ck', checkbox: true },
		{ field: 'drawName', title: '名称',formatter: function(value,row,index){return _recoverCADtext(value);}}]],
	columns: [[
		{ field: 'drawNum', title: '图号',sortable:true,sorter:function(a,b){  
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
		}},
		{ field: 'xDivision', title: '子项', hidden: true},
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
	onLoadSuccess:function(){$('#gridDrawDS').datagrid('enableDnd');}
});
if(inDlvrId){//ajax查询专业id、人员
	//if(chapterId && taskHeaderId){
	if(taskHeaderId){
		mode = 1;
		$.ajax({
			url: "http://"+window.location.host+"/api/taskRest/taskHeaderList",
			data: {"taskHeaderId":taskHeaderId,"isoa":0},
			contentType : 'application/x-www-form-urlencoded',
			dataType: "json",
			type: "POST",
			success: function(d) {
				if(d.dataSource.length>0){
					nbccTaskName = d.dataSource[0].taskName;
				}
			}
		});
	}else{//仅图纸类工作包入工作包id串
		mode = 2;
		if(inDlvrId) dlvrs = inDlvrId;
	}
	$.ajax({
		url:"http://"+window.location.host+"/api/wbspToOa/getDlvr?dlvrId="+inDlvrId,
		type:"GET",
		success:function(dlvrdata){
			_loadDlvr(dlvrdata);
		}
	});
}else if(requestid){//ajax查询OA状态、当前用户是否匹配、校审类型获取mode、人员
	$('#draw_layout').layout('collapse','west');
	$.ajax({
		url:"http://"+window.location.host+"/api/wbspToOa/getApproveForm?requestId="+requestid,
		type:"GET",
		success:function(requestdata){
			_loadDlvr(requestdata);
		}
	});
	if(erpid){
		$.ajax({
			url:"http://"+window.location.host+"/api/draw/OADrawList?requestids="+erpid+"&page=1&rows=10000",
			type:"post",
			contentType: "application/json",
			success:function(oadrawlist){
				$('#gridDrawDS').datagrid('loadData',oadrawlist.rows);
			}
		});
	}
}
function _loadDlvr(indata){
	if(indata.majorCode){
		majorArr = [indata.majorCode];//["2CT"];//
	}else majorArr = ["ALL"];
	designId = indata.design;//"101288";//"100380";//
	checkId = indata.check;//"100449";//"100394";//
	reviewId = indata.review;//"100451";//"100381";//
	approveId = indata.approve;//null;
	certifiedId = indata.certified;//null;
	schemeId = indata.scheme;//null;
	//dlvrs = "201919,201923";//888406";//
	if(indata.dlvrs) dlvrs = indata.dlvrs;
	if(indata.formid) formid = indata.formid;
	if(indata.erpid) oa_erpid = indata.erpid;
	if(indata.status && (indata.status=="申请人处理" || indata.status=="设计人审核" || indata.status=="退回") && indata.currentPersonNum==""+userNum){
	//if(indata.status && designId==""+userNum){
		if(indata.nbccTaskName && indata.nbccTaskName.length>0){
			nbccTaskName = indata.nbccTaskName;
			mode = 3;//文本类
		}else mode = 4;//图纸类
		btntext = "保存";
	}
	$.ajax({
		url:prjdrifwbsdataurl+"?projectId="+prjid+"&phaseCode="+prjPhaseID+"&structureVerId="+pubwbsID+"&spec="+majorArr[0]+"&"+userPar,
		type:"GET",
		beforeSend: function (){
			$('#loaddiv').window('open');
		},
		success:function(data){
			var reData = [];
			_reChildren(data, majorArr, reData);
			//console.log(reData);
			var reWork = [];
			_reWorktype(reData, reWork);
			$('#divtree').tree({//系统子项树
				border:false,
				onLoadSuccess:function(node, data){
					$('#loaddiv').window('close');
					_updateTreeDlvrs(dlvrs.split(','),"checkDS");
					if(data.length==0) $.messager.alert("操作提示", "未查找到图纸工作包。", "warning");
				},
				checkbox:true,
				cascadeCheck:true,
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
				formatter:function(node){return node.name;},
				onBeforeSelect:function(){return false;}
			});
		}
	});
	//console.log(mode);
	if(mode==1){
		$('#withdrawpub').combobox({   //是否合并图纸校审 
			data:[
				{"value":0,"text":"含本次校审的图纸"},
				{"value":1,"text":"只校审文本"}],
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
		$('#withdrawdiv').show();
	}
	if(mode==2 || (!inDlvrId && !erpid)){
		var dg = mode==2 ? $('#gridDraw') : $('#gridDrawDS');
		var dgurl = mode==2 ? "" : "&page=1&rows=10000";
		dg.datagrid('options').url = get_drawurl+"?isPM=1"+dgurl;
		dg.datagrid('clearSelections').datagrid('load',{"DlvrIds":dlvrs});
	}
	if(mode != 5){
		$('#drawbtndiv').show();
		$('#adddrawbtn').linkbutton({//添加图纸按钮
			iconCls: 'icon-add',
			plain:true,
			onClick: function () {
				_addDraw($('#gridDraw').datagrid('getSelections'));
			}
		});
		$('#deldrawbtn').linkbutton({//去除图纸按钮
			iconCls: 'icon-remove',
			plain:true,
			onClick: function () {
				var delArr = $('#gridDrawDS').datagrid('getSelections');
				if(delArr.length>0){
					$.messager.confirm('操作提示', '将本次校审的图纸列表中勾选的图纸移除，是否继续？', function (r) {
						if (r) {
							var delIds = [];
							for(var i=0;i<delArr.length;i++){
								delIds[delIds.length] = delArr[i].id;
							}
							for(var i=0;i<delIds.length;i++){ 
								var delIndex = $('#gridDrawDS').datagrid('getRowIndex', delIds[i]);
								$('#gridDrawDS').datagrid('deleteRow', delIndex);
								var line = $('#gridDraw').datagrid('getRowIndex', delIds[i]);
								if(line > -1) $('#gridDraw').datagrid('refreshRow', line);
							}
							var newdg = $('#gridDrawDS').datagrid('getRows');
							$('#gridDrawDS').datagrid('loadData', newdg);
							$('#gridDrawDS').datagrid('clearSelections').datagrid('autoSizeColumn');
							dlvrs = _updateDlvrs();
						}
					});
				}else $.messager.alert('操作提示', '本次校审的图纸列表中未勾选图纸。', 'error');
			}
		});
	}
	if(mode == 5 && formid == jzyform && indata.status && indata.currentPersonNum==""+userNum && !indata.dlvrs){
		$('#workbtndiv').show();
		$('#workbtn').linkbutton({//工作包按钮
			iconCls: 'icon-publish',
			plain:true,
			onClick: function () {
				$('#loaddiv').window('open');
				var workdata = {};
				workdata["approveId"] = indata.approve;
				workdata["checkId"] = indata.check;
				workdata["designId"] = indata.design;
				workdata["endDate"] = indata.endDate;
				workdata["startDate"] = indata.startDate;
				workdata["major"] = indata.majorCode;
				workdata["projectId"] = prjid;
				workdata["regEngineerId"] = indata.certified;
				workdata["requestId"] = requestid;
				workdata["reviewId"] = indata.review;
				workdata["schemeDesignerId"] = indata.scheme;
				workdata["specId"] = indata.zyjds;
				workdata["userId"] = ""+userNum;
				$.ajax({
					type: 'POST',
					url: "http://"+window.location.host+"/api/wbspToOa/createDlvr",
					contentType: "application/json",
					data: JSON.stringify(workdata),
					success: function (createDlvr) {
						$('#loaddiv').window('close');
						if(createDlvr.MSG && createDlvr.MSG == "S"){
							$.messager.alert("操作提示","已成功生成关联工作包。","info");
							window.location.href = window.location.href;
						}else $.messager.alert("操作提示","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(createDlvr),"error");
					},
					error: function(data){
						$('#loaddiv').window('close');
						$.messager.alert("操作提示","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
					}
				});
			}
		});
	}
	if(mode != 5 || (formid == jzyform && indata.status && (indata.status=="设计人发布最终图纸" || indata.status=="退回" || indata.status=="初审通过") && indata.currentPersonNum==""+userNum)){
		$('#newbtndiv').show();
		$('#newDSbtn').linkbutton({//提交按钮
			iconCls: 'icon-ok',
			text:btntext,
			plain:true,
			disabled:mode == 5 && !indata.dlvrs,
			onClick: function () {
				if(mode==1 && $('#withdrawpub').combo('getValue')==''){
					$.messager.alert("操作提示", "未选择是否校审附图。", "error");
					return;
				}
				var general = {};
				general["dlvrDetail"] = [];
				if(mode != 5){
					general["content"] = "";
				}else general["content"] = indata.lcsm;
				if((mode==1 && $('#withdrawpub').combo('getValue')==0) || mode==2 || mode==3 || mode==4 || mode==5){
					//获取图纸num串、工作包id串、工作包明细行/if mode==3不校验length>0
					var drawlines = $('#gridDrawDS').datagrid('getRows');
					if(drawlines.length==0 && mode != 3){
						$.messager.alert("操作提示", "本次校审的图纸列表中无图纸。", "error");
						return;
					}
					if(inDlvrId || erpid || oa_erpid){
						dlvrs = _updateDlvrs();
						general["dlvrs"] = dlvrs;
					}
					//general["dlvrDetail"] = [];
					var dlvrArr = dlvrs.split(",");
					var dlvrObj = {};
					var troot = $('#divtree').tree('getRoots')[0];
					var tnodes = $('#divtree').tree('getChildren', troot.target);
					for(var i=0;i<dlvrArr.length;i++){
						dlvrObj[dlvrArr[i]] = [];
						general.dlvrDetail[i] = {};
						for(var j=0;j<tnodes.length;j++){
							if(tnodes[j].id==dlvrArr[i] && tnodes[j].level=="work"){
								general.dlvrDetail[i]["descr"] = tnodes[j].description;
								general.dlvrDetail[i]["dlvrName"] = tnodes[j].name;
								var spenode = $('#divtree').tree('getParent',tnodes[j].target);
								var divnode = $('#divtree').tree('getParent',spenode.target);
								general.dlvrDetail[i]["divEquip"] = divnode.name;
								general.dlvrDetail[i]["zxhmx"] = divnode.code;
								if(divnode.matCode) general.dlvrDetail[i].divEquip += " "+divnode.matCode;
								if(divnode.matName) general.dlvrDetail[i].divEquip += " "+divnode.matName;
							}
						}
						general.dlvrDetail[i]["dlvrId"] = dlvrArr[i];
					}
					//var tempDrawNums = [];//图号中转表传参
					for(var i=0;i<drawlines.length;i++){
						//tempDrawNums[i] = drawlines[i].drawNum;
						dlvrObj[drawlines[i].xDlvrId][dlvrObj[drawlines[i].xDlvrId].length] = drawlines[i].drawNum;
					}
					for(var i=0;i<general.dlvrDetail.length;i++){
						if(dlvrObj[general.dlvrDetail[i].dlvrId].length==0){
							$.messager.alert("操作提示", general.dlvrDetail[i].dlvrId+"工作包尚未上传图纸，请刷新页面后再次确认并重试。", "error");
							return;
						}
						var cut = dlvrObj[general.dlvrDetail[i].dlvrId][0].indexOf("-");
						var head = dlvrObj[general.dlvrDetail[i].dlvrId][0].substring(0,cut+1);
						var serial = [];
						for(var j=0;j<dlvrObj[general.dlvrDetail[i].dlvrId].length;j++){
							serial[j] = dlvrObj[general.dlvrDetail[i].dlvrId][j].substring(head.length);
						}
						serial.sort(sortNumber);
						var merges = serial[0];
						if(serial.length>1)
							for(var j=1;j<serial.length;j++){
								if(j==serial.length-1 || serial[j+1]-serial[j-1]!=2){
									if(j>1 && serial[j]-serial[j-2]==2){
										merges += "～" + serial[j];
									}else merges += "," + serial[j];
								}
							}
						general.dlvrDetail[i]["drawNumAbbr"] = head + merges;
						general.dlvrDetail[i]["drawCount"] = serial.length;
					}
					if(mode==2 || mode==4){
						for(var i=0;i<general.dlvrDetail.length;i++){
							general.content += ","+ general.dlvrDetail[i].divEquip;
						}
						general.content = general.content.substr(1)+"图纸"+drawlines.length+"张";
					}else if(mode != 5){
						general.content = nbccTaskName;
						if(drawlines.length>0) general.content += ",附图"+drawlines.length+"张";
					}
				}
				if(mode==1){//general添加文本相关参数
					//general["wb"] = POwebUrl+"?chapterId="+chapterId+"&taskHeaderId="+taskHeaderId+"&requestid=";
					general["wb"] = POwebUrl+"?taskHeaderId="+taskHeaderId+"&nbccDlvr="+inDlvrId+"&requestid=";
					general["nbccTaskName"] = nbccTaskName;
					general["nbccDlvr"] = inDlvrId;
					general["taskHeaderId"] = taskHeaderId;
				}
				if(mode==1 || mode==2){//发起OA流程
					//if($('#withdrawpub').combo('getValue')==0 || mode==2)
					var saveurl = "/api/wbspToOa/saveHead";
					if(isAppr){
						general["tz"] = CheckReviewUrl+"?projID="+prjid+"&phaseID="+prjPhaseID+"&isAppr=true&requestid=";
						saveurl = "/api/wbspToOa/saveSign";
					}else general["tz"] = CheckReviewUrl+"?projID="+prjid+"&phaseID="+prjPhaseID+"&requestid=";
					general["xmbh"] = prjNumber;
					general["xmmc"] = prjName;
					general["design"] = designId;
					general["check"] = checkId;
					general["review"] = reviewId;
					if(approveId) general["approve"] = approveId;
					if(certifiedId) general["certified"] = certifiedId;
					if(schemeId) general["scheme"] = schemeId;
					general["majorCode"] = majorArr[0];
					$.getJSON("../../common/majorList.json", function (majorJson) {
						general["zy"] = majorJson[majorArr[0]].full;
						$.ajax({
							type: 'POST',
							url: "http://"+window.location.host+saveurl,
							contentType: "application/json",
							data: JSON.stringify(general),
							success: function (saveHead) {
								var reg = new RegExp("(^|&)erpid=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
								var newerpid = saveHead.URL.substr(saveHead.URL.indexOf("?")+1).match(reg); //匹配目标参数
								_saveDrawNum(unescape(newerpid[2]),saveHead.URL);
							}
						});
					});
				}else{//ajax更新OA流程表单
					general["formid"] = formid;
					general["requestid"] = requestid;
					//console.log(general);
					$.ajax({
						type: 'POST',
						url: "http://"+window.location.host+"/api/wbspToOa/updateApprove",
						contentType: "application/json",
						data: JSON.stringify(general),
						success: function (updateApprove) {
							_saveDrawNum(erpid);
						}
					});
				}
			}
		});
	}
}
function _saveDrawNum(erpid,url){
	var drawNumsArr = [];
	var drawdsrows = $('#gridDrawDS').datagrid('getRows');
	for(var i=0;i<drawdsrows.length;i++){
		drawNumsArr[drawNumsArr.length] = drawdsrows[i].drawNum;
	}
	var req_erpid = erpid ? erpid : oa_erpid;
	$.ajax({
		type: 'POST',
		url: "http://"+window.location.host+"/api/wbspToOa/saveDrawNum",
		contentType: "application/json",
		data: JSON.stringify({"requestid":req_erpid,"drawNums":drawNumsArr}),
		success: function (drawnumdata) {
			//跳转OA表单
			if(url){
				window.open(url,'_blank');
			}else alert('保存成功。');
			//else window.open("http://"+OAurl+"/workflow/request/ViewRequest.jsp?requestid="+requestid,'_blank');
			window.opener = null;  
			window.close();  
			//if(url){
			//	window.location.href = url;
			//}else window.location.href = "http://"+OAurl+"/workflow/request/ViewRequest.jsp?requestid="+requestid;
		}
	});
}
$('#drawNumSearch').searchbox({
	searcher: function (value, name) {
		_searchDraw(value);
	}
});
function _searchDraw(value){
	if(!$('#drawwest').is(":visible")){$('#draw_layout').layout('expand', 'west');}
	var ckNodes = $('#divtree').tree('getChecked');
	if(ckNodes.length==0){
		$.messager.alert("操作提示", "未勾选工作包。", "warning");
		return;
	}else{
		var ckIds = '';
		for(var i=0;i<ckNodes.length;i++)
			if(ckNodes[i].level=="work") ckIds += ","+ckNodes[i].id;
		ckIds = ckIds.substring(1);
		var keywords = value ? value.replace(" ", ",") : '';
		keywords = keywords.replace("，", ",");
		$('#gridDraw').datagrid('options').url = get_drawurl+"?DdocName="+keywords+"&isPM=1";
		$('#gridDraw').datagrid('clearSelections').datagrid('load',{"DlvrIds":ckIds});
	}
}
$('#drawshowall').prop("checked",false).change(_hideMoreCols);//显示更多列
function _hideMoreCols(){
	var moreCols = ['xDivision','xEquipmentNum','xEquipment','xDlvrName','xDlvrId','xapproved_NAME','xregistered_Engineer_Name','xfangan_name'];
	if($('#drawshowall').is(":checked")){
		for(var i=0;i<moreCols.length;i++) $('#gridDraw').datagrid('showColumn', moreCols[i]);
	}else for(var i=0;i<moreCols.length;i++) $('#gridDraw').datagrid('hideColumn', moreCols[i]);
}
function _getdrawStat(row){
/* 	if(row.dlvr_status_code=="PROCESS"){
		var result = "校审中";
	}else  */
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
function _linkCat(did){
	window.open(ecmurl+"/cs/idcplg?IdcService=DOC_INFO&dID="+did,'_blank');
}
function _linkOA(requestId){//跳转OA表单
	window.open("http://"+OAurl+"/workflow/request/ViewRequest.jsp?requestid="+requestId,'_blank','location=no');
}
$('#autocheck').linkbutton({//智能勾选按钮
    onClick: function () {
		$('#gridDraw').datagrid('clearSelections');
		var selcount = 0;
		var dgrows = $('#gridDraw').datagrid('getRows');
		for(var i=0;i<dgrows.length;i++){
			var drawStat = _getdrawStat(dgrows[i]);
			if($.inArray(drawStat, enableChr)>-1 && $('#gridDrawDS').datagrid('getRowIndex', dgrows[i].id) == -1){
				$('#gridDraw').datagrid('selectRow',i);
				selcount++;
			}
		}
		$.messager.show({title:'提示', msg:'智能勾选了'+selcount+'行。'});
    }
});
function sortNumber(a,b){
	return a - b;
}
$('#adddsshowall').prop("checked",false).change(_hideMoreCols_DS);//新增变更相关图纸列表显示更多列
function _hideMoreCols_DS(){
	var moreCols = ['xDivision','xEquipmentNum','xEquipment','xDlvrName','xDlvrId','xapproved_NAME','xregistered_Engineer_Name','xfangan_name'];
	if($('#adddsshowall').is(":checked")){
		for(var i=0;i<moreCols.length;i++) $('#gridDrawDS').datagrid('showColumn', moreCols[i]);
	}else for(var i=0;i<moreCols.length;i++) $('#gridDrawDS').datagrid('hideColumn', moreCols[i]);
}
function _recoverCADtext(input){
	var sym = [{"cad":"%%c","nor":"Ф"},{"cad":"%%p","nor":"±"},{"cad":"%%d","nor":"°"}];
	for(var i=0;i<sym.length;i++){
		input = input.split(sym[i].cad).join(sym[i].nor);
	}
	return input;
}
function _reChildren(Obj, speArr, result){//递归重构对象
	for(var i=0;i<Obj.length;i++){
		if(Obj[i]["level"] != "spe" || $.inArray(Obj[i]["majorCode"],speArr) > -1 || speArr[0] == "ALL"){
			result[result.length] = {};
			for(var key in Obj[i]){
				if(Obj[i]["level"] != "spe" && key != "speChildren"){
					result[result.length-1][key] = key == "children" ? [] : Obj[i][key];
				}else if(Obj[i]["level"] == "spe" && key != "speChildren" && key != "children"){
					result[result.length-1][key] = Obj[i][key];
				}else if(Obj[i]["level"] == "spe" && key == "speChildren"){
					result[result.length-1]["children"] = [];
					for(var j=0;j<Obj[i][key].length;j++){//匹配给定设校审定注册方案人
						if(((designId == Obj[i][key][j].designId && checkId == Obj[i][key][j].checkId && Obj[i][key][j].reviewId == reviewId
						&& Obj[i][key][j].approveId == approveId && Obj[i][key][j].certifiedId == certifiedId && Obj[i][key][j].schemeId == schemeId)
						|| ((formid == gsjform || speArr[0] == "ALL") && ($.inArray(Obj[i][key][j].code, dlvrs.split(','))>-1 || $.inArray(""+Obj[i][key][j].code, dlvrs.split(','))>-1))
						|| isAppr) && Obj[i][key][j].worktypeID == "01"){
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
	var hasNew = false;
	for(var i=0;i<drawArr.length;i++){//图纸状态校验
		var drawStat = _getdrawStat(drawArr[i]);
		if($.inArray(drawStat, enableChr)==-1){
			$.messager.alert("操作提示", "不可添加图纸状态灰色的图纸，请检查后重试。", "error");
			return;
		}
	}
	for(var i=0;i<drawArr.length;i++){//id不存在则追加行
		if($('#gridDrawDS').datagrid('getRowIndex', drawArr[i].id) == -1){
			$('#gridDrawDS').datagrid('appendRow',drawArr[i]);
			hasNew = true;
		}
		var line = $('#gridDraw').datagrid('getRowIndex', drawArr[i].id);
		$('#gridDraw').datagrid('refreshRow', line);
	}
	if(hasNew){
		var newdg = $('#gridDrawDS').datagrid('getRows');
		$('#gridDrawDS').datagrid('loadData', newdg);
		var index = $('#gridDrawDS').datagrid('getRowIndex', drawArr[0].id);
		$('#gridDrawDS').datagrid('scrollTo', index).datagrid('autoSizeColumn');
	}
	dlvrs = _updateDlvrs();
}
function _updateDlvrs(){//更新工作包id串
	var Arr = [];
	var dgrows = $('#gridDrawDS').datagrid('getRows');
	for(var i=0;i<dgrows.length;i++)
		if($.inArray(dgrows[i].xDlvrId,Arr)==-1) Arr[Arr.length] = dgrows[i].xDlvrId;
	if(!inDlvrId && !erpid){
		_updateTreeDlvrs(Arr,"checkDS");
	}else _updateTreeDlvrs(Arr);
	return Arr.join(',');
}
function _updateTreeDlvrs(Arr,checkDS){//更新树形工作包粗体下划线
	var troot = $('#divtree').tree('getRoots')[0];
	if(troot){
		var tnodes = $('#divtree').tree('getChildren', troot.target);
		if(checkDS){
			var DSdlvr = [];
			var DSrows = $('#gridDrawDS').datagrid('getRows');
			for(var i=0;i<DSrows.length;i++)
				if($.inArray(DSrows[i].xDlvrId,DSdlvr)==-1) DSdlvr[DSdlvr.length] = DSrows[i].xDlvrId;
			//console.log(DSdlvr);
		}
		for(var i=0;i<tnodes.length;i++){
			if(tnodes[i].code){
				var txt = tnodes[i].code+" "+tnodes[i].name;
			}else var txt = tnodes[i].name;
			if(($.inArray(tnodes[i].id,Arr)>-1 || $.inArray(""+tnodes[i].id,Arr)>-1) && tnodes[i].level=="work"){
				if(checkDS && $.inArray(tnodes[i].id,DSdlvr)==-1){
					$(tnodes[i].target).find("span.tree-title")[0].innerHTML = "<font color='red'><b><u>"+txt+"</u></b></font>";
				}else $(tnodes[i].target).find("span.tree-title")[0].innerHTML = "<b><u>"+txt+"</u></b>";
			}else $(tnodes[i].target).find("span.tree-title")[0].innerHTML = "<font color='lightsteelblue'>"+txt+"</font>";
		}
	}
}