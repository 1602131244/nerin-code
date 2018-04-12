var wurl = window.location.href;
var cut = wurl.lastIndexOf("/");
var CheckReviewUrl = "http://"+window.location.host+"/pages/wbsp/code - dev/CheckReview/CheckReview.html";
var prjdrifwbsdataurl = "http://"+window.location.host+"/api/lev3/getPubDlvrList";
var prj_major_dataurl = "http://"+window.location.host+"/api/lev2/querySpecList";
var get_drawurl = "http://"+window.location.host+"/api/draw/DesignChange";
var get_dcurl = "http://"+window.location.host+"/api/naviWbpsRest/sjbgOa";
var OAurl = "192.168.15.198";
var ecmurl = "http://192.168.15.252:16200";
var enablePlt = ["未校审","校审中"];
var rolelist = [
{"id":"txtdesign","name":"设计人"},
{"id":"txtcheck","name":"校核人"},
{"id":"txtreview","name":"审核人"},
{"id":"txtapprove","name":"审定人"},
{"id":"txtcertified","name":"注册工程师"},
{"id":"txtscheme","name":"方案设计人"},
{"id":"chgrlist","name":"专业负责人"},
{"id":"txtpm","name":"项目负责人"}];
var roleobj = {
	"txtdesign":"sjr",
	"txtcheck":"jhr",
	"txtreview":"shr",
	"txtapprove":"sdr",
	"txtcertified":"zcs",
	"txtscheme":"fasjr",
	"chgrlist":"zyfzr",
	"txtpm":"xmjl"
};
var pmlist;
$('#draw_layout').layout('remove', 'east');

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
				$('#gridPrjmajors').datagrid('loadData', dgdata).datagrid('clearSelections');
			}
		});
	}else $('#gridPrjmajors').datagrid('loadData', specialty).datagrid('clearSelections');
}
$.getJSON("http://" + window.location.host +"/api/naviWbpsRest/getProjRoles?projectId="+prjid, function (data) {
	$('#txtpm').combo('setValue',null).combobox("loadData", data);
	if(data.length==1) $('#txtpm').combobox('select',data[0].num);
});
$.getJSON("http://" + window.location.host +"/api/lev3/getMemberList?projID="+prjid, function (data) {
	pmNum = data.mgr.perNum;
});
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
			beforeSend: function (){$('#loaddiv').window('open');},
			success:function(xdata){
				/* var tndata = xdata;
				if(headOrbranch!="head" && division){
					if(xdata[0].children.length>0){
						tndata[0].children = xdata[0].children[0].children;
					}else{
						$('#loaddiv').window('close');
						$.messager.alert("操作提示", "无可选子项专业。", "error");
						return;
					}
				} */
				$('#divtree').tree({//系统子项树
					border:false,
					onLoadSuccess:function(node, data){
						$('#wbs_layout').layout('expand', 'west');
						$('#loaddiv').window('close');
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
					data:xdata,//tndata,
					formatter:function(node){
						if(node.code){
							return node.code+" "+node.name;
						}else return node.name;
					},
					onBeforeSelect:function(){return false;}
				});
			}
		});
    }
});
$('#searchdrawbtn').linkbutton({//查找校审流程按钮
	iconCls: 'icon-reload',
	plain: true,
    onClick: function () {
		_loadApprdg();
	}
});
$('#confirmbtn').linkbutton({//提交确认按钮
	iconCls: 'icon-ok',
	plain:true,
    onClick: function () {}
});
$('#drawshowall').prop("checked",false).change(_hideMoreCols);//显示更多列
function _hideMoreCols(){
	var moreCols = ['xDivision','specialityName','xDlvrName','xDlvrId','xapproved_NAME','xregistered_Engineer_Name','xfangan_name','xEquipmentNum','xEquipment'];
	if($('#drawshowall').is(":checked")){
		for(var i=0;i<moreCols.length;i++) $('#gridDraw').datagrid('showColumn', moreCols[i]);
	}else for(var i=0;i<moreCols.length;i++) $('#gridDraw').datagrid('hideColumn', moreCols[i]);
}
$('#gridDraw').datagrid({//相关图纸列表
	data:[],
	method:'POST',
	idField: 'id',
	rownumbers: true, singleSelect: true, pagination: true, pageList:[10,20,30,50],
	fit: true,toolbar:'#drawtb',border:false,pageSize: 20,striped:true,
	onLoadSuccess: function(){
		$('#gridDraw').datagrid('clearSelections');
	},
	frozenColumns: [[
		{ field: 'drawName', title: '图名'}]],
	columns: [[
		{ field: 'drawNum', title: '图号',formatter: function(value,row,index){
			return value+" <a href='javascript:_linkCat("+row.id+");'>查看</a>";
		}},
		{ field: 'xPltStatus', title: '状态',formatter:function(value,row,index){
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
		{ field: 'xapproved_NAME', title: '审定人', hidden: true},
		{ field: 'xregistered_Engineer_Name', title: '注册工程师', hidden: true},
		{ field: 'xfangan_name', title: '方案设计人', hidden: true},
		{ field: 'xEquipmentNum', title: '设备编码', hidden: true},
		{ field: 'xEquipment', title: '设备名称', hidden: true}]]
});
function _loadDraw(workId){
	$('#draw_layout').layout('expand', 'east');
	$('#gridDraw').datagrid('options').url = "http://"+window.location.host+"/api/draw/DrawList?isPM=1";
	$('#gridDraw').datagrid('load',{DlvrIds:workId});
}
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
function _linkOA(requestId){//跳转OA表单
	window.open("http://"+OAurl+"/workflow/request/ViewRequest.jsp?requestid="+requestId,'_blank','location=no');
}
function _linkCat(did){
	window.open(ecmurl+"/cs/idcplg?IdcService=DOC_INFO&dID="+did,'_blank');
}
$('#newDSbtn').linkbutton({//发起校审按钮
	iconCls: 'icon-add',
	plain:true,
    onClick: function () {
		if(!$('#spewest').is(":visible")) $('#rootlayout').layout('expand', 'west');
		if(!$('#wbswest').is(":visible")) $('#wbs_layout').layout('expand', 'west');
		var trnode = $('#divtree').tree('getChecked');
		var spenode = [];
		for(var i=0;i<trnode.length;i++){
			if(trnode[i].level=="spe"){
				if(spenode.length>0 && trnode[i].majorCode!=spenode[0].majorCode){
					$.messager.alert("操作提示","选择多个专业节点应当为相同专业。","error");
					return;
				}else spenode[spenode.length] = trnode[i];
			}
		}
		if(spenode.length>0){
			$.ajax({
				url: "http://"+window.location.host+"/api/lev3/getAssignmentListAll?projID="+prjid+"&specialty="+spenode[0].majorCode,
				type: "GET",
				beforeSend:function(){$('#loaddiv').window('open');},
				success: function (data) {
					var combodata = {
						"txtdesign":[],
						"txtcheck":[],
						"txtreview":[],
						"txtapprove":[],
						//"txtspechgr":[],
						"txtcertified":[],
						"txtscheme":[]};
					for(var i=0;i<data.length;i++){
						for(var j=0;j<rolelist.length;j++){
							if(data[i].paMemberSpecDTO.duty==rolelist[j].name){
								for(var k=0;k<data[i].member.length;k++){
									var ishave = false;
									for(var m=0;m<combodata[rolelist[j].id].length;m++){
										if(combodata[rolelist[j].id][m].perId==data[i].member[k].perId){
											ishave = true;
											break;
										}
									}
									if(!ishave)
										combodata[rolelist[j].id][combodata[rolelist[j].id].length] = {
											"num":data[i].member[k].perNum,
											"name":data[i].member[k].perName,
											"perId":data[i].member[k].perId,
										};
								}
								break;
							}
						}
					}
					for(var key in combodata){
						$("#"+key).combo('setValue',null).combobox("loadData", combodata[key]);
						if(combodata[key].length==1) $('#'+key).combobox('select',combodata[key][0].num);
					}
					$.ajax({
						url: "http://" + window.location.host +"/api/lev3/getProfessionListAll?projID="+prjid+"&specialty="+spenode[0].majorCode,
						type: "GET",
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
							$('#chgrlist').combo('setValue',null).combobox("loadData", chgrArr);
							if(chgrArr.length==1) $('#chgrlist').combobox('select',chgrArr[0].perNum);
							$('#loaddiv').window('close');
							$('#formuser').window('open').panel('setTitle', "选择审批人员 - 专业："+spenode[0].name);
						},
						error: function(data){
							$('#loaddiv').window('close');
							$.messager.alert("操作提示","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
						}
					});
				},
				error: function(data){
					$('#loaddiv').window('close');
					$.messager.alert("操作提示","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
				}
			});
		}else{
			$.messager.alert("操作提示","未选中子项专业树中的专业节点。","error");
		}
    }
});
$('#btnuserapply').linkbutton({//设校审提交按钮
	iconCls:'icon-ok',
	onClick: function () {
		trnode = $('#divtree').tree('getChecked');
		var postData = {
			"xmbh":prjNumber,
			"zyjds":"",
			"sjjd":prjPhase,
			"lcsm":"",
			"tzurl":CheckReviewUrl+"?projID="+prjid+"&phaseID="+prjPhaseID+"&requestid="//wurl.substr(0,cut)+"/drawExplore.html?workId="
		};
		postData.jhkssj = trnode[0].startDate;
		postData.jhjssj = trnode[0].endDate;
		for(var i=0;i<trnode.length;i++){
			if(trnode[i].level == "spe"){
				postData.zyjds += ","+trnode[i].id;
				if(!postData.zymc) postData.zymc = trnode[i].name;
				if(postData.jhkssj.stringToDate() < trnode[i].startDate.stringToDate()) postData.jhkssj = trnode[i].startDate;//日期要取各专业日期区间交集
				if(postData.jhjssj.stringToDate() > trnode[i].endDate.stringToDate()) postData.jhjssj = trnode[i].endDate;
			}
			var pnode = $('#divtree').tree('getParent', trnode[i].target);
			if(pnode && pnode.level=="div"){
				postData.lcsm += ","+pnode.code+pnode.name;
			}
		}
		if(postData.jhkssj.stringToDate() > postData.jhjssj.stringToDate()){
			$.messager.alert("操作提示","任务起止时间未通过校验，请调整合并校审的子项或更新二级策划。","error");
			return;
		}
		postData.zyjds = postData.zyjds.substr(1);
		if(postData.lcsm.length>0){
			postData.lcsm = postData.lcsm.substr(1);
		}else{
			postData.lcsm = prjPhase;
		}
		var design = $('#txtdesign').combobox('getValue');//获取combobox人员并校验不重复
 		var check = $('#txtcheck').combobox('getValue');
		var review = $('#txtreview').combobox('getValue');
		var spechgr = $('#chgrlist').combobox('getValue');
		var pmchgr = $('#txtpm').combobox('getValue');
		if(!pmchgr || !design || !check || !review || !spechgr){
			$.messager.alert("操作提示","项目负责人、设计人、校核人、审核人、专业负责人为必选。","error");
		}else if(design==check || check==review || check==review){
			$.messager.alert("操作提示","设计人、校核人、审核人不可存在重复。","error");
		}else{
			var fcArr = [];
			for(var i=0;i<rolelist.length;i++){
				var value = $('#'+rolelist[i].id).combobox('getValue');
				if(value){
					postData[roleobj[rolelist[i].id]] = value;
					if($.inArray(value,fcArr) == -1){
						postData["fc"+roleobj[rolelist[i].id]] = value;
						fcArr[fcArr.length] = value;
					}
				}
			}
			//postData["xmjl"] = pmchgr;//ajax发起集成OA流程，success后关闭window
			$.ajax({
				url: "http://"+window.location.host + "/api/naviWbpsRest/jzyPrintCheck?workflowId=4233",
				contentType: "application/json",
				data: JSON.stringify(postData),
				type: "POST",
				beforeSend: function () {$('#loaddiv').window('open');},
				success: function (data) {
					$('#loaddiv').window('close');
					if(data.status=="S"){
						$('#formuser').window('close');
						window.open(data.url, "_blank");
					}else $.messager.alert("操作提示","提交OA发起流程失败。<br>错误信息:"+JSON.stringify(data),"error");
				},
				error: function (data) {
					$('#loaddiv').window('close');
					$.messager.alert("操作提示","调用接口失败。<br>错误信息:"+JSON.stringify(data),"error");
				}
			});
		}
	}
});
/* $('#drawbtn').linkbutton({//查看图纸按钮
	iconCls: 'icon-more',
	plain:true,
    onClick: function () {
		var rowline = $('#gridDrawDS').datagrid('getSelections');
		if(rowline.length==0){
			$.messager.alert("操作提示", "请选中一条图纸校审流程。", "error");
		}else{
			var workId = 201919;//rowline[0].workId;//
			if(workId && workId!="" && workId !=0){
				window.open(wurl.substr(0,cut)+"/drawExplore.html?workId="+workId,'_blank');
			}else{
				$.messager.alert("操作提示", "该图纸校审流程尚未生成工作包。<br>提示：工作包应当在通过中间分叉点后生成。", "error");
			}
		}
    }
}); */
$('#viewprjall').prop("checked",false).change(_loadApprdg);//不限项目查看校审流程
$('#gridDrawDS').datagrid({//校审流程列表
	data:[],
	method: 'get',
	idField: 'requestId',
	rownumbers: true, singleSelect: true, 
	pagination: true, pageList:[10,20,30,50],pageSize:20,
	fit: true,toolbar:'#adddstb',border:false,striped:true,
	onLoadSuccess:function(){$('#gridDrawDS').datagrid('clearSelections');},
	frozenColumns: [[
		{ field: 'createDate', title: '创建日期' },
		{ field: 'requestId', title: '流程ID',formatter: function(value,row,index){
			return value+" <a href='javascript:_linkOA("+row.requestId+");'>查看</a>";
		} }]],
	columns:[[
		{ field: 'xmxsmc', title: '项目名称',hidden:!$('#viewprjall').is(":checked")},
		{ field: 'sjjd', title: '设计阶段',hidden:!$('#viewprjall').is(":checked")},
		{ field: 'lcsm', title: '流程说明'},//{ field: 'zxh', title: '子项号'},
		//{ field: 'zxmc1', title: '子项名称'},
		{ field: 'zymc', title: '专业'},
		{ field: 'sjjd1', title: '设计进度'},
		{ field: 'requestName', title: '流程标题'},
		{ field: 'createBy', title: '创建人' },
		{ field: 'status', title: '当前状态'},
		{ field: 'currentPerson', title: '当前操作者'}]]
});
var jdcode = '';
$.getJSON("../../common/phaseList.json", function (data) {
	for(var i=0;i<data.length;i++){
		if(data[i].name==prjPhase){
			jdcode = data[i].phaseCode;
			break;
		}
	}
	_exmode();
	_loadApprdg();
});
function _loadApprdg(){
	if($('#viewprjall').is(":checked")){
		var viewprjall = '';
		$('#gridDrawDS').datagrid('showColumn','xmxsmc').datagrid('showColumn','sjjd');
	}else{
		var viewprjall = "&xmId="+prjid+"&jdcode="+jdcode;
		$('#gridDrawDS').datagrid('hideColumn','xmxsmc').datagrid('hideColumn','sjjd');
	}
	var speIds = '';
	if($('#divtree')[0].className == "tree"){
		var trnodes = $('#divtree').tree('getChecked');
		if(trnodes && trnodes.length>0){
			for(var i=0;i<trnodes.length;i++){
				if(trnodes[i].level=="spe") speIds += ","+trnodes[i].id;
			}
			if(speIds.length>0) speIds = "&zyjds="+speIds.substr(1);
		}
	}
	$('#gridDrawDS').datagrid('options').url = "http://"+window.location.host + "/api/naviWbpsRest/jzyPrintCheckOa?formId=347"+viewprjall+speIds;
	$('#gridDrawDS').datagrid('load');
}
function _enableCombo(ckbid, txtid){//切换下拉框激活状态
	if ($(ckbid).is(':checked')) {
		$(txtid).combo("enable");
	}else $(txtid).combo("disable");
}