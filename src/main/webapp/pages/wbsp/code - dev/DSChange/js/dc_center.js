var prjdrifwbsdataurl = "http://"+window.location.host+"/api/lev3/getPubDlvrList";
var get_drawurl = "http://"+window.location.host+"/api/draw/DesignChange";
var get_dcurl = "http://"+window.location.host+"/api/naviWbpsRest/sjbgOa";
var OAurl = "192.168.15.198";
var ecmurl = "http://192.168.15.252:16200";
//var pourl = "http://localhost:8090/Samples4/DesignChange";
var pourl = "http://192.168.15.91:8080/cux/DesignChange";
/* 正式环境
var OAurl = "oa.nerin.com";
var ecmurl = "http://wcc.nerin.com:16200";
var pourl = "http://ebs.nerin.com:8008/OA_HTML/cux_cooperate"; */
var drawdata = [
		{'id':1,'name':'图纸目录', 'num':'2A13A03DD0101ST1AA1-1', 'div':'暂存库（一）','spe': '环境工程','work': 'AA1通用图包','design':'陈森','check':'唐斌', 'review':'袁剑平', 'wCode':'836408', 'approve':null, 'certified':null, 'scheme':null},
		{'id':2,'name':'工艺设计说明', 'num':'2A13A03DD0101ST1AA1-2', 'div':'暂存库（一）','spe': '环境工程','work': 'AA1通用图包','design':'陈森','check':'唐斌', 'review':'袁剑平', 'wCode':'836408', 'approve':null, 'certified':null, 'scheme':null},
		{'id':3,'name':'暂存库（一）设备配置图', 'num':'2A13A03DD0101ST1AA1-3', 'div':'暂存库（一）','spe': '环境工程','work': 'AA1通用图包','design':'陈森','check':'唐斌', 'review':'袁剑平', 'wCode':'836408', 'approve':null, 'certified':null, 'scheme':null}
	];
$('#modecheck').prop("checked",!exmode).change(_exmode);//默认标准模式
function _exmode(){
	$.messager.confirm('操作提示', '将放弃所有未保存的编辑，请确认是否继续？', function (r) {
		if (r){
			if(!$('#modecheck').is(":checked")){
				window.location.href = "http://"+window.location.host+window.location.pathname+"?projID="+prjid+"&phaseID="+prjPhaseID+"&exmode=true";
			}else{
				window.location.href = "http://"+window.location.host+window.location.pathname+"?projID="+prjid+"&phaseID="+prjPhaseID;
			}
		}
	});
}
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
						$('#rootlayout').layout('collapse', 'west');
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
			$('#gridDraw').datagrid('options').url = get_drawurl+"?DlvrIds="+ckIds+"&DdocName="+keywords;
			$('#gridDraw').datagrid('clearSelections').datagrid('load');
		}
	}
});
$('#drawshowall').prop("checked",false).change(_hideMoreCols);//显示更多列
function _hideMoreCols(){
	var moreCols = ['xDivision','specialityName','xDlvrName','xDlvrId','xapproved_NAME','xregistered_Engineer_Name','xfangan_name'];
	if($('#drawshowall').is(":checked")){
		for(var i=0;i<moreCols.length;i++) $('#gridDraw').datagrid('showColumn', moreCols[i]);
	}else for(var i=0;i<moreCols.length;i++) $('#gridDraw').datagrid('hideColumn', moreCols[i]);
}
$('#gridDraw').datagrid({//相关图纸列表
	onLoadError:function(){
		$.messager.alert("加载失败","从ECM服务器取数失败，请检查后重试或联系系统管理员。","error");
	},
	data:[],
	method: 'get',
	idField: 'id',
	rownumbers: true, singleSelect: false, pagination: true, pageList:[10,20,30,50],
	fit: true,toolbar:'#drawtb',border:false,pageSize: 20,striped:true,
	onLoadSuccess: function(){
		$('#gridDraw').datagrid('clearSelections');
		$('#gridDraw').datagrid('enableDnd');
	},
	frozenColumns: [[
		{ field: 'ck', checkbox: true },
		{ field: 'drawName', title: '名称', styler: function(value,row,index){
			if($('#gridDrawDS').datagrid('getRowIndex', row.id) > -1)
				return 'background-color:lightgray;color:white';
			},formatter: function(value,row,index){return _recoverCADtext(value);}}]],
	columns: [[
		{ field: 'drawNum', title: '图号',formatter: function(value,row,index){
			if(!exmode){
				return value+" <a href='javascript:_linkCat("+row.id+");'>查看</a>";
			}else return value;
		}},
		{ field: 'xDivision', title: '子项', hidden: true},
		{ field: 'specialityName', title: '专业', hidden: true},
		{ field: 'xDlvrName', title: '工作包', hidden: true},
		{ field: 'xdesigned_NAME', title: '设计人'},
		{ field: 'xchecked_NAME', title: '校核人'},
		{ field: 'xreviewed_NAME', title: '审核人'},
		{ field: 'xDlvrId', title: '工作包号', hidden: true},
		{ field: 'xapproved_NAME', title: '审定人', hidden: true},
		{ field: 'xregistered_Engineer_Name', title: '注册工程师', hidden: true},
		{ field: 'xfangan_name', title: '方案设计人', hidden: true}]]
});
function _linkCat(did){
	window.open(ecmurl+"/cs/idcplg?IdcService=DOC_INFO&dID="+did,'_blank');
}
$('#gridViewDS').datagrid({//变更列表
	data:[],
	method: 'get',
	idField: 'requestId',
	rownumbers: true, singleSelect: false, pagination: true,pageList:[5,10,20,30],
	fit: true,toolbar:'#viewdstb',border:false,pageSize: 5,striped:true,
	onLoadSuccess: function(){
		$('#gridViewDS').datagrid('clearSelections');
	},
	frozenColumns: [[
		{ field: 'createDate', title: '创建日期'},
		{ field: 'requestId', title: '流程ID',formatter: function(value,row,index){
			return value+" <a href='javascript:_linkOA("+row.requestId+");'>查看</a>";
		} }]],
	columns: [[
		{ field: 'bgyy', title: '变更原因'},
		{ field: 'status', title: '当前状态'},
		{ field: 'zxmc', title: '子项', hidden: true},
		{ field: 'zy', title: '专业', hidden: true},
		{ field: 'createBy', title: '创建人'},
		{ field: 'sjbgdh', title: '变更单号'},
		{ field: 'currentPerson', title: '当前操作者'},
		{ field: 'designName', title: '设计人'},
		{ field: 'checkName', title: '校核人'},
		{ field: 'reviewName', title: '审核人'},
		{ field: 'approveName', title: '审定人', hidden: true},
		{ field: 'certified', title: '注册工程师', hidden: true},
		{ field: 'descr', title: '说明'},
		{ field: 'requestName', title: '标题'}]]
});
$('#viewdsshowall').prop("checked",false).change(_hideMoreCols_DSview);//变更显示更多列
function _hideMoreCols_DSview(){
	var moreCols = ['zxmc','zy','approveName','certified'];
	if($('#viewdsshowall').is(":checked")){
		for(var i=0;i<moreCols.length;i++) $('#gridViewDS').datagrid('showColumn', moreCols[i]);
	}else for(var i=0;i<moreCols.length;i++) $('#gridViewDS').datagrid('hideColumn', moreCols[i]);
}
$('#viewdsSearch').searchbox({
	searcher: function (value, name) {
		var ckNodes = $('#divtree').tree('getChecked');
		if(!ckNodes || ckNodes.length==0){
			$.messager.alert("操作提示", "未勾选工作包。", "warning");
			return;
		}else{
			var ckIds = '';
			for(var i=0;i<ckNodes.length;i++)
				if(ckNodes[i].level=="work") ckIds += ","+ckNodes[i].id;
			ckIds = ckIds.substring(1);
			var draws = $('#gridDraw').datagrid('getSelections');
			if(draws.length==0){
				var drawIds = '';
			}else{
				var drawIds = '&draws='+draws[0].id;
				if(draws.length>1)
					for(var i=1;i<draws.length;i++) drawIds += ","+draws[i].id;
			}
			var keywords = value.replace(" ", ",");
			keywords = keywords.replace("，", ",");
			$('#gridViewDS').datagrid('options').url = get_dcurl+"?projectId="+prjid+"&phaseId="+prjPhaseID+"&works="+ckIds+"&keyWords="+keywords+drawIds;
			$('#gridViewDS').datagrid('clearSelections').datagrid('load');
		}
	}
});
function _linkOA(requestId){//跳转OA表单
	window.open("http://"+OAurl+"/workflow/request/ViewRequest.jsp?requestid="+requestId,'_blank','location=no');
}
$('#divtree').tree({});
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
			$.messager.confirm('操作提示', '将新增变更表格中勾选的图纸移除，是否继续？', function (r) {
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
				}
			});
		}else $.messager.alert('操作提示', '新增变更表格中未勾选图纸。', 'error');
    }
});
function sortNumber(a,b){
	return a - b;
}
$('#newDSbtn').linkbutton({//提交变更按钮
	iconCls: 'icon-ok',
	plain:true,
    onClick: function () {
		var gridData = $('#gridDrawDS').datagrid('getData').rows;
		if(gridData.length>0){
			var drawsNameStr = gridData[0].drawName;
			var unfull = true;
			var tmpArr = [];//drawpnum
			var drawsNumStr = "";
			for(var i=0;i<gridData.length;i++){//校验图纸是否属于同一子项和专业
				if(gridData[i].xDivision != gridData[0].xDivision || gridData[i].xDivisionNum != gridData[0].xDivisionNum || gridData[i].specialityName != gridData[0].specialityName){
					$.messager.alert("操作提示","请检查图纸所属子项和专业是否相同后重试。","error");
					return;
				}
				if(i>0 && unfull){
					var newDrawsName = drawsNameStr + "," + gridData[i].drawName;
					if((i==gridData.length-1 && newDrawsName.length < 35) || newDrawsName.length < 31){
						drawsNameStr = newDrawsName;
					}else{
						unfull = false;
						drawsNameStr += "等"+gridData.length+"张";
					}
				}
				var cut = gridData[i].drawNum.indexOf("-");
				if(cut>-1){
					var drawpnum = gridData[i].drawNum.substring(0,cut+1);
				}else var drawpnum = gridData[i].drawNum;
				if($.inArray(drawpnum,tmpArr)==-1){//校验是否首次出现的图号前缀，在其后继续查找相同前缀图号进行排序并汇总
					tmpArr[tmpArr.length] = drawpnum;
					var serial = [];
					for(var j=i;j<gridData.length;j++)
						if(gridData[j].drawNum.indexOf(drawpnum)==0)
							serial[serial.length] = gridData[j].drawNum.substring(drawpnum.length);
					serial.sort(sortNumber);
					drawsNumStr += ","+drawpnum + serial[0];
					if(serial.length>1)
						for(var j=1;j<serial.length;j++){
							if(j==serial.length-1 || serial[j+1]-serial[j-1]!=2){
								if(j>1 && serial[j]-serial[j-2]==2){
									drawsNumStr += "～" + serial[j];
								}else drawsNumStr += "," + serial[j];
							}
						}
				}
			}
			if(drawsNameStr.length>34) drawsNameStr = drawsNameStr.substr(0,33)+"…";
			drawsNumStr = drawsNumStr.substring(1);
			$.ajax({
				url: "http://" + window.location.host +"/api/naviWbpsRest/getProjRoles?projectId="+prjid,
				type: "GET",
				beforeSend:function(){$('#loaddiv').window('open');},
				success: function (pmlist) {
					$('#pmlist').combo('setValue',null).combobox("loadData", pmlist);
					if(pmlist.length==1) $('#pmlist').combobox('select',pmlist[0].num);
					$.ajax({
						url: "http://" + window.location.host +"/api/lev3/getProfessionListAll?projID="+prjid+"&specialty=2"+gridData[0].xSpecialty,
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
							$('#dcrsnlist').combo('setValue',null).combobox("loadData", [
								{"val":1,"text":"业主要求"},
								{"val":2,"text":"施工方建议"},
								{"val":3,"text":"设备或材料代换"},
								{"val":4,"text":"设备资料修改"},
								{"val":5,"text":"现场条件变化"},
								{"val":6,"text":"设计规范变更"},
								{"val":7,"text":"设计改进"},
								{"val":8,"text":"其他"}
							]);
							$('#drawsName').textbox('setText',_recoverCADtext(drawsNameStr));
							$('#drawsNum').textbox('setText',drawsNumStr);
							$('#subName').textbox('setText', gridData[0].xDivision);
							$('#loaddiv').window('close');
							$('#formuser').window('open').panel('setTitle', "填写变更关键信息 - 专业："+gridData[0].specialityName);
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
		}else $.messager.alert('操作提示', '新增变更表格中无图纸。', 'error');
    }
});
function _recoverCADtext(input){
	var sym = [{"cad":"%%c","nor":"Ф"},{"cad":"%%p","nor":"±"},{"cad":"%%d","nor":"°"}];
	for(var i=0;i<sym.length;i++){
		input = input.split(sym[i].cad).join(sym[i].nor);
	}
	return input;
}
$('#btnuserapply').linkbutton({//确定变更提交按钮
	iconCls:'icon-ok',
	onClick: function () {
		var gridData = $('#gridDrawDS').datagrid('getData').rows;
		var postData = {
			"xmjl": $('#pmlist').combo('getValue'),//项目经理
			"zyfzr": $('#chgrlist').combo('getValue'),//专业负责人
			"bgyyJsp":$('#dcrsnlist').combo('getValue'),//变更原因传jsp
			"bgyyOA":$('#dcrsnlist').combo('getText'),//变更原因传OA
			"subName":$('#subName').textbox('getText'),//子项名称传jsp
			"drawsName":$('#drawsName').textbox('getText'),//图纸名称传jsp
			"drawsNum":$('#drawsNum').textbox('getText'),//图纸图号传jsp
			"editDoc":pourl+"/getDoc.html?requestid=",//编辑doc
			"viewDoc":pourl+"/DataRegionView.jsp?requestid=",//查看doc
			"getPdf":pourl+"/getPDF.html?requestid=",//获取pdf
			"xmbh":prjNumber,//项目编号
			"xmmc":prjName,//项目名称
			"sjjd":prjPhase,//设计阶段
			"zxmc":gridData[0].xDivision,//子项名称
			"zxh":gridData[0].xDivisionNum,//子项号
			"zy": gridData[0].specialityName,//专业
			"detail":[]//明细表
		};
		for(var i=0;i<gridData.length;i++){
			postData.detail[i] = {};
			postData.detail[i]["tzmc"] = _recoverCADtext(gridData[i].drawName);//图纸名称
			postData.detail[i]["th"] = gridData[i].drawNum;//图号
			postData.detail[i]["sjr"] = gridData[i].xdesigned;//设计人
			postData.detail[i]["jhr"] = gridData[i].xchecked;//校核人
			postData.detail[i]["shr"] = gridData[i].xreviewed;//审核人
			postData.detail[i]["sdr"] = gridData[i].xapproved;//审定人
			postData.detail[i]["zcgcs"] = gridData[i].xregistered_Engineer;//注册工程师
			postData.detail[i]["gzbh"] = gridData[i].xDlvrId;//工作包号
			postData.detail[i]["tzid"] = gridData[i].id;//图纸id
			postData.detail[i]["url"] = ecmurl+"/cs/idcplg?IdcService=DOC_INFO&dID="+gridData[i].id;//图纸文件地址url
		}
		if(!postData.xmjl || !postData.zyfzr || !postData.bgyyJsp || !postData.subName || !postData.drawsName || !postData.drawsNum){
			$.messager.alert("操作提示","请检查必填项是否已完成填写后重试。","error");
			return;
		}
		$.getJSON("../../common/majorList.json", function (data) {
			postData["zysx"] = data["2"+gridData[0].xSpecialty]["abbr"];//专业缩写
			//$('#iframewindow').window('open');
			//document.getElementById("iframepage").src = "http://localhost:8090/Samples4/DesignChange/FileMakerSingle.jsp?sn=123";
			$('#formuser').window('close');
			//$('#iframewindow').window('close');
			$.ajax({
				url: "http://"+window.location.host + "/api/naviWbpsRest/submit",
				contentType: "application/json",
				data: JSON.stringify(postData),
				type: "POST",
				beforeSend: function () {$('#loaddiv').window('open');},
				success: function (data) {
					$('#loaddiv').window('close');
					if(data.status=="S"){
						$('#formuser').window('close');
						window.open(data.url, "_blank");
					}else $.messager.alert("操作提示","提交ebs生成记录失败。<br>错误信息:"+JSON.stringify(data),"error");
				},
				error: function (data) {
					$('#loaddiv').window('close');
					$.messager.alert("操作提示","提交ebs生成记录失败。<br>错误信息:"+JSON.stringify(data),"error");
				}
			});
		});
	}
});
$('#adddsshowall').prop("checked",false).change(_hideMoreCols_DS);//新增变更相关图纸列表显示更多列
function _hideMoreCols_DS(){
	var moreCols = ['xDivision','specialityName','xDlvrName','xDlvrId','xapproved_NAME','xregistered_Engineer_Name','xfangan_name'];
	if($('#adddsshowall').is(":checked")){
		for(var i=0;i<moreCols.length;i++) $('#gridDrawDS').datagrid('showColumn', moreCols[i]);
	}else for(var i=0;i<moreCols.length;i++) $('#gridDrawDS').datagrid('hideColumn', moreCols[i]);
}
$('#gridDrawDS').datagrid({//新增变更之图纸列表
	method: 'get',
	idField: 'id',
	rownumbers: true, singleSelect: false, pagination: false,
	fit: true,toolbar:'#adddstb',border:false,striped:true,
	frozenColumns: [[
		{ field: 'ck', checkbox: true },
		{ field: 'drawName', title: '名称',formatter: function(value,row,index){return _recoverCADtext(value);}}]],
	columns: [[
		{ field: 'drawNum', title: '图号',formatter: function(value,row,index){
			if(!exmode){
				return value+" <a href='javascript:_linkCat("+row.id+");'>查看</a>";
			}else return value;
		}},
		{ field: 'xDivision', title: '子项', hidden: true},
		{ field: 'specialityName', title: '专业', hidden: true},
		{ field: 'xDlvrName', title: '工作包', hidden: true},
		{ field: 'xdesigned_NAME', title: '设计人'},
		{ field: 'xchecked_NAME', title: '校核人'},
		{ field: 'xreviewed_NAME', title: '审核人'},
		{ field: 'xDlvrId', title: '工作包号', hidden: true},
		{ field: 'xapproved_NAME', title: '审定人', hidden: true},
		{ field: 'xregistered_Engineer_Name', title: '注册工程师', hidden: true},
		{ field: 'xfangan_name', title: '方案设计人', hidden: true}]],
	onLoadSuccess:function(){$('#gridDrawDS').datagrid('enableDnd');}
});
if(exmode){
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
						var isCH = false;
						if(!isPM){//如标准模式则非项目经理、专业负责人仅自己设计工作包可见
							for(var k=0;k<specialty.length;k++){
								if($.inArray(perID,specialty[k].chgrNum)>-1){
									isCH = true;//校验是否当前专业负责人
									break;
								}
							}
						}
						if((!$('#modecheck').is(":checked") || (isPM || isCH || Obj[i][key][j].designNum == perID)) && Obj[i][key][j].worktypeID == "01"){
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
}