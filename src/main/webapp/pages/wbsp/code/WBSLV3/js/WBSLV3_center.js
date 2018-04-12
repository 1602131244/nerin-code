var OAurl = "192.168.15.198";
var prjdrifwbsdataurl = "http://"+window.location.host+"/api/lev3/getPubDlvrList";// "prjdrifwbsdataurl1.json";//oracle包-加载当前阶段草稿版WBS详细数据
var prjpubproductsdataurl = "datagrid_Prjpubproductlist.json";// oracle包-加载图纸状态变更预览列表
var prjviewproductsdataurl = "datagrid_Prjviewproductlist.json";// oracle包-链接图纸状态记录页面
var NIMS2_0url = "192.168.15.95:8080";//正式环境15.211:8080
var saveUrl = "http://"+window.location.host+"/api/lev3/saveDlvr"; // oracle包-保存当前阶段草稿版WBS
var checkinUrl = "http://"+window.location.host+"/api/lev3/checkInSpec"; // oracle包-签入当前阶段草稿版WBS
var checkouturl = "http://"+window.location.host+"/api/lev3/checkOutSpec";// oracle包-签出当前阶段草稿版WBS
var puburl = '';// oracle包-发布当前阶段草稿版WBS

var addurl = ""; //物料新增地址
var searchurl = "";//物料查询地址

var driftversionexpanded = false;
var subitemCheck = '#subitem_toolbar_ref,#subitem_toolbar_me,#subitem_toolbar_del,#subitem_toolbar_edit,#subitem_toolbar_save,#subitem_toolbar_checkin';
var subitemNoEdting = '#subitem_toolbar_ref,#subitem_toolbar_me,#subitem_toolbar_del,#subitem_toolbar_edit,#subitem_toolbar_save,#subitem_toolbar_reset,#subitem_toolbar_checkin,#setHidden,[name=btnothers]';
var subitemEdting = '#subitem_toolbar_cancel,#subitem_toolbar_accept';
var editId = [];//"处于编辑状态数据行"动态数组
var endEditId = [];//"已完成编辑状态"动态数组
var unproductId = 20;//非标设备id
var dutys = ["design","check","review","approve","certified","scheme"];
var panelValues = new Object();//建立人员下拉菜单data对象
var subitem_newId = 0;
var tmpmajor = null;
var hiddenIdList = [];


var prolist = [];//项目专业表
var subitem_west_max = false;//子项选库面板是否最大化
var cancelId = [];//"放弃则删除新增行"动态数组

var subitem_Vals = "";//系统子项号参照对象集
var pbs_ref_tab;//引用pbs版本tab
var subitemContextRow;//系统子项表格右键点击行

$("#subitem_collapse").addClass("l-btn");//改变默认下拉按钮样式
$("#subitem_collapse").removeClass("l-btn-plain");//改变默认下拉按钮样式

/* $('#subitem_toolbar_excute').linkbutton({//工作包执行
	iconCls: 'icon-tip',
	onClick: function(){
		var rows = $('#subitem_grid').datagrid('getSelections');
		if(rows.length!=1){
			$.messager.alert("操作提示", "不可执行：请选取一条工作包，不可多选或不选；或者采取双击行的方式直接执行本操作。", "error");
		}else _workExcute(rows[0]);
	}
}); */
function _workExcute(dgindex) {
	var row = $('#subitem_grid').datagrid('getRows')[dgindex];
	switch (row.worktypeID) {
		case "01"://图纸
			$('#btnDraw,#btnAppr').linkbutton({//创建图纸校审流程
				onClick:function(){
					//正式环境window.open('http://'+window.location.host+'/pages/wbsp/code/CheckReview/CheckReview.html?projID='+prjid+'&phaseID='+prjPhaseID+'&inDlvrId='+row.code,'_blank');
					var isAppr = $(this).text()=="会签" ? "&isAppr=true" : "";
					$('#Drawdiv').window('close');
					window.open('http://'+window.location.host+'/pages/wbsp/code - dev/CheckReview/CheckReview.html?projID='+prjid+'&phaseID='+prjPhaseID+'&inDlvrId='+row.code+isAppr,'_blank');
				}
			}).show().linkbutton('disable');
			$('#btnecm').linkbutton({//查阅ecm
				onClick:function(){
					//正式环境window.open("http://wcc.nerin.com:16200/cs/idcplg?IdcService=CUX_WP_MAIN_SRH&isSearch=1&PG_TYPE=WORKPKG&CUX_USER_NAME="+userNum+"&dlvrId="+row.id+"&<$include%20idc_token_url_parameter$>", '_blank');
					window.open("http://192.168.15.252:16200/cs/idcplg?IdcService=CUX_WP_MAIN_SRH&isSearch=1&PG_TYPE=WORKPKG&CUX_USER_NAME="+userNum+"&dlvrId="+row.id+"&<$include idc_token_url_parameter$>", '_blank');
				}
			})
			if(isPM || (row.designNum==perID && !guest)) $('#btnDraw,#btnAppr').linkbutton('enable');
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
			$('#Drawdiv').panel('setTitle',"图纸校审流程记录 - "+row.name+" 设计人："+designDlvr).window('open');
			break;
		case "02"://文本
			var pElementId = row.code;
			$('#NBCCdg').datagrid({//NBCC表格
				/* data:[{"taskHeaderId":1,"taskName":"名称","createdName":"袁景平","taskProgress":"100/100","taskProgress2":"30/30","taskStatusName":"公司级评审已通过"},
					{"taskHeaderId":2,"taskName":"名称","createdName":"袁景平","taskProgress":"100/100","taskProgress2":"30/30","taskStatusName":"公司级评审已通过"},
					{"taskHeaderId":3,"taskName":"名称","createdName":"袁景平","taskProgress":"100/100","taskProgress2":"30/30","taskStatusName":"公司级评审已通过"}],//url: '',
				 *///oracle包-文本类工作包相关NBCC协作任务列表
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
			$('#NBCCformdiv').panel('setTitle',"协作文本任务 - "+row.name+" 设计人："+designDlvr).window('open');
			break;
		case "03"://计算书
			$('#btnCalculation,#btnCalculationup').linkbutton({//创建流程
				onClick:function(){
					$('#Calculationdiv').window('close');
					window.open("http://"+OAurl+"/login/LoginSSO.jsp?flowCode=AM02&erpid="+row.code+ "&workcode=" +userNum,'_blank','location=no');
				}
			}).show().linkbutton('disable');
			if(row.designNum==perID && !guest) $('#btnCalculation,#btnCalculationup').linkbutton('enable');
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
			$('#Calculationdiv').panel('setTitle',"计算书任务流程记录 - "+row.name+" 设计人："+designDlvr).window('open');
			break;
		case "04"://三维
			console.log("04"+row.name);
			break;
		case "05"://接口条件
			$('#btnCondition,#btnConditionup').linkbutton({//创建流程
				onClick:function(){
					$.ajax({
						url: "http://" + window.location.host +"/api/lev3/getProfessionListAll?projID="+prjid+"&specialty="+row.recvSpec,
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
											url: "http://"+window.location.host+"/api/naviWbpsRest/createcondition?projectId="+prjid+"&elementId="+row.specId+"&deliverableId="+row.id+"&submitCode="+row.majorCode+"&receiveCode="+row.recvSpec+"&schleDate="+row.endDate+"&userId="+userID+"&jszyfzr="+$('#zychgr').combo('getValue'),
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
			if(row.designNum==perID && !guest) $('#btnCondition,#btnConditionup').linkbutton('enable');
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
			$('#Conditiondiv').panel('setTitle',"接口条件任务 - "+row.name+" 设计人："+designDlvr).window('open');
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
$('#subitem_toolbar_checkout').linkbutton({//项目结构签出
	iconCls: 'icon-back',
	onClick: function () {
		reloaddriftwbstree(undefined,"noupgrade");
		if(!$('#subitem_toolbar_checkout').is(":hidden") && $('#subitem_toolbar_checkout').linkbutton('options').disabled){
			$('#speformdiv').window('close');
			$.messager.alert("操作提示", "不可策划：请检查当前专业状态。", "error");
			reloaddriftwbstree(currDiv);
		}else{
			$.ajax({
				url: checkouturl+"?"+userPar+"&personID="+perID,
				type: "POST",
				data:JSON.stringify({"lockSpecs":[{"specId": tmpmajor.substring(5)}],"phaseID": prjPhaseID,"projID": prjid,"personID":perID}),
				contentType: "application/json",
				success: function (data) {
					$('#speformdiv').window('close');
					if(data.checkOut && data.checkOut=="done"){
						reloaddriftwbstree(currDiv);
						$.messager.show({title:'提示', msg:'进入策划模式。'});
					}else $.messager.alert("不可策划", "请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data), "error");
				}
			});
		}
	}
});
$('#subitem_toolbar_checkin').linkbutton({//项目结构签入
	iconCls: 'icon-enter',
	onClick: function () {
		if (!$('#subitem_toolbar_save').linkbutton('options').disabled){
			$.messager.alert("操作提示", "不可释放：存在未保存的编辑，请先保存。如放弃当前编辑，请重置。", "error");
			return;
		}
		reloaddriftwbstree(undefined,"noupgrade");
		if($('#subitem_toolbar_checkin').is(':hidden')){
			$('#speformdiv').window('close');
			reloaddriftwbstree(currDiv);
			$.messager.show({title:'提示', msg:'已释放。'});
		}else{
			$.ajax({
				url: checkinUrl+"?projectId="+prjid+"&"+userPar+"&personID="+perID,
				type: "POST",
				data:JSON.stringify({"lockSpecs": [{"specId": tmpmajor.substring(5)}],"phaseID": prjPhaseID,"projID": prjid,"personID":perID}),
				contentType: "application/json",
				success: function (data) {
					$('#speformdiv').window('close');
					if(data.db_state=="S"){
						reloaddriftwbstree(currDiv);
						$.messager.show({title:'提示', msg:'释放成功。'});
					}else $.messager.alert("释放失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
				}
			});
		}
	}
});
$('#subitem_toolbar_reset').linkbutton({//项目结构编辑重置
	iconCls: 'icon-reload',
	onClick: function () {
		if (!$('#subitem_toolbar_save').linkbutton('options').disabled) {
			$.messager.confirm('操作提示', '将放弃所有未保存的编辑，请确认是否继续？', function (r) {
				if (r) {
					reloaddriftwbstree();
				}
			});
		}else {
			reloaddriftwbstree();
		}
	}
});
$('#subitem_toolbar_ref').linkbutton({//项目结构编辑引用按钮
	iconCls: 'icon-redo',
	onClick: function () {
		if(!$('#west').is(":visible")){
			$(document.body).layout('expand', 'west');
			$.messager.show({title:'提示', msg:"请在左侧页面选择被引用对象。"});
			return false;
		}
		if (!currDiv) {
			$.messager.alert('操作提示', '当前无可用专业。', 'error');
			return false;
		}else{
			var parent = $('#divtree').tree('find', currDiv);
			if(parent.status != "生效") {
				$.messager.alert('操作提示', '当前专业状态必须为“生效”。', 'error');
				return false;
			}
		}
		var selectedTab = $('#reftabs').tabs('getSelected');
		var SourceRows = [];
		switch ($('#reftabs').tabs('getTabIndex', selectedTab)) {
			case 0:
				try {
					var selRows = $('#gridPrjSys').datagrid('getSelections');
				} catch (e) { return; }
				for (var i = 0; i < selRows.length; i++) {
					SourceRows[i] = {};
					SourceRows[i].code = selRows[i].dlvrCode;
					SourceRows[i].name = selRows[i].dlvrName;
					SourceRows[i].src = "系统";
					SourceRows[i].status = "未启动";
					SourceRows[i].level = "work";
					SourceRows[i].worktypeID = "0"+selRows[i].classID;
					//if (SourceRows[i].worktypeID != unproductId)
					//    SourceRows[i].name = SourceRows[i].code.substring(SourceRows[i].code.length - 2, SourceRows[i].code.length) + "0" + SourceRows[i].name;
					SourceRows[i].authorName = userNum;
					SourceRows[i].authorNum = userName;
					SourceRows[i].grandNum = 1;
					SourceRows[i].otherslist = {"othersCount":"无","paOthers":[]};
					SourceRows[i].workCode = selRows[i].dlvrID;
				}
				var icon_cls = "icon-work";
				break;
			case 1:
				if ($('#gridPrjmajors').datagrid('getSelections')) {
					var selRows = $('#gridPrjmajors').datagrid('getSelections');
					for (var i = 0; i < selRows.length; i++) {
						//接口条件工作包名称为接口条件工作包代字“TJ”+工作包孙项号+当前工作包专业名称+“-”+接收专业名称+“接口条件”
						SourceRows[i] = {};
						SourceRows[i].code = "TJ";
						SourceRows[i].name = selRows[i].name + "接口条件";
						SourceRows[i].src = "系统";
						SourceRows[i].status = "未启动";
						SourceRows[i].level = "work";
						SourceRows[i].worktypeID = "05";
						//if (SourceRows[i].worktypeID != unproductId)
						//    SourceRows[i].name = SourceRows[i].code.substring(SourceRows[i].code.length - 2, SourceRows[i].code.length) + "0" + SourceRows[i].name;
						SourceRows[i].authorName = userNum;
						SourceRows[i].authorNum = userName;
						SourceRows[i].grandNum = 1;
						SourceRows[i].otherslist = {"othersCount":"无","paOthers":[]};
						SourceRows[i].recvSpec = 2+selRows[i].code;
						SourceRows[i].workCode = 14029;
					}
				}
				var icon_cls = "icon-work";
				break;
			case 2:
				var selrowArr = $('#hisgrid').datagrid('getSelections');
				var Regx = /(^[A-Za-z0-9]$)/;
				for(var i=0;i<selrowArr.length;i++){
					var selrow = selrowArr[i];
					var SourceNode = {};
					var isCode = true;
					for(var j=1;j<3;j++){
						var val = selrow.dlvrName.substring(j-1, j);
						if(!Regx.test(val)) isCode = false;
						break;
					}
					if(!isCode){
						SourceNode.code = "OT";
						SourceNode.name = selrow.dlvrName;
					}else{
						SourceNode.code = selrow.dlvrName.substring(0, 2);
						if(isNaN(selrow.dlvrName.substring(2, 3))){
							SourceNode.name = selrow.dlvrName.substring(2, selrow.dlvrName.length);
						}else SourceNode.name = selrow.dlvrName.substring(3, selrow.dlvrName.length);
					}
					var inSpeList = true;
					if(selrow.dlvrName.indexOf("接口条件") > 0){
						SourceNode.name = SourceNode.name.substr(SourceNode.name.indexOf("-")+1);
						var recvSpeName = SourceNode.name.replace("接口条件", "");
						for(var j=0;j<specialty.length;j++){
							if(specialty[j].name==recvSpeName){
								SourceNode.recvSpec = specialty[j].id;
								break;
							}
						}
						if(!SourceNode.recvSpec) inSpeList = false;
					}
					if(inSpeList){//接收专业在当前项目参与专业范围内
						SourceNode.workHour = selrow.workHour;
						SourceNode.level = "work";
						SourceNode.grandNum = 1;
						SourceNode.iconCls = "icon-work";
						SourceNode.worktypeID = selrow.worktypeID;
						SourceNode.src = "历史项目";//row.name.substring(0, row.name.indexOf("<a")) + "<a id=src_a" + row.wbsID + "></a>";
						/* SourceNode.srcID = row.wbsID;
						SourceNode.srcStru = row.level;
						SourceNode.srcRow = row.id; */
						SourceNode.status = "未启动";
						SourceNode.authorName = userNum;
						SourceNode.authorNum = userName;
						SourceNode.otherslist = {"othersCount":"无","paOthers":[]};
						SourceNode.workCode = selrow.workCode;
						SourceRows[SourceRows.length] = SourceNode;
					}
				}
		}
		_appendToSubitem(SourceRows, icon_cls);
	}
});

$('#subitem_toolbar_del').linkbutton({//项目结构编辑删除菜单按钮
	iconCls: 'icon-remove',
	onClick: function () {
		_removeSubitem();
	}
});
$("#meadd").click(function () {
	window.open(addurl, "_blank");
});
$("#mesearch").click(function () {
	window.open(searchurl, "_blank");
});
$("#edittext").click(function () {//修改详情
	var rows = $('#subitem_grid').datagrid('getSelections');
	editId =[];
	var parent = $('#divtree').tree('find', currDiv);
	if(parent.status != "失效")
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].level == "work" && rows[i].status != "失效") editId[editId.length] = rows[i].id;
		}
	if (editId.length == 0) {
		$.messager.alert("操作提示", "请先选择需要修改的工作包。<br>注意：状态必须为“未启动”或“执行中”，且当前专业状态不可为“失效”。", "error");
	} else {
		_EditRow(editId, "beginEdit");
	}
});

//修改日期
$("#editdate").click(function () {
	var rows = $('#subitem_grid').datagrid('getSelections');
	var oldstartdate = "";
	var oldenddate = "";
	var editrows = false;
	var parent = $('#divtree').tree('find', currDiv);
	if(parent.status != "失效")
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].level == "work" && rows[i].status != "失效" && !(!isPM && rows[i].authorClass=="PM")) {
				editrows = true;
				break;
			}
		}
	if (!editrows) {
		$.messager.alert("操作提示", "请先选择需要修改日期的工作包。<br>注意：状态必须为“未启动”或“执行中”，且当前专业状态不可为“失效”，非项目经理角色不可修改由项目经理创建的工作包起止日期。", "error");
	} else {
		var treeIds = [currDiv];
		for(var i=0;i<treeIds.length;i++){
			var node = $('#divtree').tree('find', treeIds[i]);
			if(node.startDate && node.startDate!="" && ((oldstartdate!="" && oldstartdate.stringToDate() < node.startDate.stringToDate())||oldstartdate=="")){
				oldstartdate=node.startDate;
			}
			if(node.endDate && node.endDate!="" && ((oldenddate!="" && oldenddate.stringToDate() > node.endDate.stringToDate())||oldenddate=="")){
				oldenddate=node.endDate;
			}
		}
		if(oldstartdate.stringToDate()>oldenddate.stringToDate()){
			$.messager.alert("操作提示", "所选各专业起止日期无可选区间。", "error");
		}else{
			$('#ckbdatestart').prop("checked",false);
			$('#ckbdateend').prop("checked",false);
			$("#dateplanstart").datebox('calendar').calendar({
				validator: function (date) {
					return oldstartdate.stringToDate() <= date && date <= oldenddate.stringToDate();
				}
			});
			$("#dateplanend").datebox('calendar').calendar({
				validator: function (date) {
					return oldstartdate.stringToDate() <= date && date <= oldenddate.stringToDate();
				}
			});
			$("#dateplanstart").datebox('setValue', oldstartdate).combo("disable");
			$("#dateplanend").datebox('setValue', oldenddate).combo("disable");
			$("#oldstartdate").text(oldstartdate);
			$("#oldenddate").text(oldenddate);
			$('#dateformdiv').window('open');
		}
	}
});
$('#btndateapply').linkbutton({
	onClick: function () {
		if ($("#ckbdatestart").is(':checked') && $("#ckbdateend").is(':checked') && $("#dateplanstart").datebox('getValue').stringToDate() > $("#dateplanend").datebox('getValue').stringToDate()) {
			$.messager.alert({ title: '提示', msg: '计划完成日期不可早于计划起始日期，请更正。' });
			return false;
		}else if($("#ckbdatestart").is(':checked') && !$("#ckbdateend").is(':checked') && $("#dateplanstart").datebox('getValue').stringToDate() > $("#oldenddate").text().stringToDate()) {
			$.messager.alert({ title: '提示', msg: '计划起始日期不可晚于可选区间完成日期，请更正。' });
			return false;
		}else if(!$("#ckbdatestart").is(':checked') && $("#ckbdateend").is(':checked') && $("#dateplanend").datebox('getValue').stringToDate() < $("#oldstartdate").text().stringToDate()) {
			$.messager.alert({ title: '提示', msg: '计划完成日期不可早于可选区间起始日期，请更正。' });
			return false;
		}
		if(!$("#ckbdatestart").is(':checked') && !$("#ckbdateend").is(':checked')){
			$('#dateformdiv').window('close');
			return false;
		}
		var rows = $('#subitem_grid').datagrid('getSelections');
		var i=0;
		var int=window.setInterval(function(){
			if(i==1) $('#loaddiv').window('open');
			if(i>0) $('#loaddiv').panel('setTitle', Math.round(parseFloat(100*i/rows.length))+'%已处理，请稍候...');
			if(i<rows.length){
				if (rows[i].level == "work" && rows[i].status != "无效" && !(!isPM && rows[i].authorClass=="PM")) {
					if($("#ckbdatestart").is(':checked')){
						var newstartDate = $('#dateplanstart').combo('getValue');
					}else var newstartDate = rows[i].startDate;
					if($("#ckbdateend").is(':checked')){
						var newendDate = $('#dateplanend').combo('getValue');
					}else var newendDate = rows[i].endDate;
					var newDate = {"startDate":newstartDate,"endDate":newendDate};
					editId[editId.length] = rows[i].id;
					$('#subitem_grid').datagrid('beginEdit', $('#subitem_grid').datagrid('getRowIndex',rows[i].id));
					var pNode = $('#divtree').tree('find', currDiv);
					_seteditor(pNode, rows[i].id, newDate);
					_setComboboxValues(rows[i].majorCode, rows[i].id, rows[i]);
				}
				i++;
			}else{
				int=window.clearInterval(int);
				$('#loaddiv').window('close');
			}
		},0);
		$(".datebox :text").attr("readonly", "readonly");
		$(subitemNoEdting).hide();
		$(subitemEdting).show();
		$('#dateformdiv').window('close');
	}
});

//设置生效
$("#editstatuson").click(function () {
	var rows = $('#subitem_grid').datagrid('getSelections');
	editId = [];
	var parent = $('#divtree').tree('find', currDiv);
	if(parent.status != "失效")
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].status == "失效" && !(!isPM && rows[i].authorClass=="PM")) editId[editId.length] = rows[i].id;
		}
	if (editId.length == 0) {
		$.messager.alert("操作提示", "请先选择需要生效的工作包。<br>注意：状态必须为“失效”，且当前专业状态不可为“失效”，非项目经理角色不可失效由项目经理创建的工作包。", "error");
	} else {
		_EditRow(editId, "beginEdit", "statuson");
	}
	
});

//设置失效
$("#editstatusoff").click(function () {
	editrows = [];
	var rows = $('#subitem_grid').datagrid('getSelections');
	for (var i = 0; i < rows.length; i++) {
		if ($.inArray(rows[i].status,["执行中","已完成"])>-1 && !(!isPM && rows[i].authorClass=="PM")) {
			editrows[editrows.length] = rows[i];
		}
	}
	if (editrows.length == 0) {
		$.messager.alert("操作提示", "请先选择需要失效的工作包。<br>注意：状态不可为“失效”、“未启动”，且非项目经理角色不可失效由项目经理创建的工作包。", "error");
	} else {
		for (var i = 0; i < editrows.length; i++) {
			updatechildNodeStatus(editrows[i]);
		}
		$('#subitem_toolbar_save').linkbutton('enable');
		var strRows = JSON.stringify($('#subitem_grid').datagrid('getRows'));//更新树
		$('#divtree').tree('update', {target: $('#divtree').tree('find',currDiv).target,speChildren:JSON.parse(strRows)});
		}
});
function updatechildNodeStatus(row) {
	$('#subitem_grid').datagrid("updateRow", {
		index: $('#subitem_grid').datagrid('getRowIndex',row.id),
		row: {
			"status": "失效"
		}
	});
	var childnodes = row.children;
	for (var i = 0; i < childnodes.length; i++) {
		updatechildNodeStatus(childnodes[i]);
	}
}

$('#subitem_toolbar_cancel').linkbutton({//项目结构编辑放弃
	iconCls: 'icon-undo',
	onClick: function () {
		$('#totalwork').text(parseInt($('#totalwork').text())-cancelId.length);
		$('#displaywork').text(parseInt($('#displaywork').text())-cancelId.length);
		$(subitemEdting).hide();
		$(subitemNoEdting).show();
		_enterNode(currDiv);
		editId = [];
		cancelId = [];
	}
});
$('#subitem_toolbar_accept').linkbutton({//项目结构编辑继续
	iconCls: 'icon-ok',
	onClick: function () {
		_AcceptTreegridEdting();
	}
});
$(subitemEdting).hide();

$('#subitem_toolbar_save').linkbutton({// 编辑保存
	disabled: true,
	iconCls: 'icon-save',
	onClick: function () {
		_saveWork();
	}
});
function _saveWork(){//保存当前工作包数据
	var result = {"addRows":[],"updateRows":[],"deleteRows":[],"phaseCode":prjPhaseID,"projectId":prjid};
	var before = JSON.parse(subitem_Vals);
	var curr = _initialSubitemVals();
	for(var i=0;i<before.length;i++){
		var deled = true;
		for(var j=0;j<curr.length;j++){
			if(before[i].id==curr[j].id){
				for(var key in curr[j]){
					if(key!="otherslist"){
						if(before[i][key]!=curr[j][key]){
							result.updateRows[result.updateRows.length]=curr[j];
							break;
						}
					}else if((curr[j][key].othersCount!="无" && before[i][key].othersCount=="无")||
						(curr[j][key].othersCount=="无" && before[i][key].othersCount!="无")){
						result.updateRows[result.updateRows.length]=curr[j];
						break;
					}else if(curr[j][key].othersCount!="无" && before[i][key].othersCount!="无"){
						var ok = true;
						for(var k=0;k<curr[j][key].paOthers.length;k++){
							var ishave = false;
							for(var kk=0;kk<before[i][key].paOthers.length;kk++){
								if(curr[j][key].paOthers[k].otherNum==before[i][key].paOthers[kk].otherNum &&
									curr[j][key].paOthers[k].otherRatio==before[i][key].paOthers[kk].otherRatio){
									before[i][key].paOthers.splice(kk,1);
									ishave=true;
									break;
								}
							}
							if(!ishave){
								ok = false;
								break;
							}
						}
						if(!ok || before[i][key].paOthers.length>0){
							result.updateRows[result.updateRows.length]=curr[j];
							break;
						}
					}
				}
				curr.splice(j,1);
				deled = false;
				break;
			}
		}
		if(deled) result.deleteRows[result.deleteRows.length] = {"id":before[i].id};
	}
	for(var i=0;i<curr.length;i++){
		result.addRows[result.addRows.length]=curr[i];
	}
	if(result.addRows.length==0 && result.deleteRows.length==0 && result.updateRows.length==0){
		$.messager.show({ title: '提示', msg: '无更改需要保存。' });
		return;
	}
	reloaddriftwbstree(undefined,"noupgrade");
	if($('#subitem_toolbar_save').is(':hidden')){
		$('#speformdiv').window('close');
		$.messager.alert("操作提示", "不可保存：请检查当前专业状态。", "error");
	}else{
		$.ajax({
			type: 'POST',
			url: saveUrl+"?"+userPar+"&personID="+perID,
			contentType: "application/json",
			data: JSON.stringify(result),
			success: function (data) {
				$('#speformdiv').window('close');
				if (data.saveWork == 'done') {
					reloaddriftwbstree(currDiv);
					$.messager.show({ title: '提示', msg: '保存成功。' });
				}else $.messager.alert("保存失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
			}
		});
	}
}
$('#pubwindowexecute').linkbutton({
	onClick: function () {
		//如通过校验则再校验图纸状态变更列表数据行数是否与选中行数相等
		var total = $('#grid_publists').datagrid('getData');
		var checked = $('#grid_publists').datagrid('getSelections');
		if (checked.length == 0 || checked.length < total.rows.length) {
			$.messager.alert("操作提示", "不可执行：图纸状态变更未全部确认。", "error");
			return false;
		}
		$("#pubwindowexecute").linkbutton('disable');
		$('#grid_publists').datagrid('loading');
		//_uploadSubitemData(saveUrl, "保存");
	}
});
//更新图纸状态预览窗口
function updateviewwindow() {
	$("#pubwindowexecute").linkbutton('disable');
	$('#grid_publists').datagrid('loading');
	$('#grid_publists').datagrid('reload', {
		projID: prjid,
		phaseID: prjPhaseID,
		updateRow: []
	});
}
$('#pubwindowupdate').linkbutton({
	onClick: function () {
		updateviewwindow();
	}
});
$('#divtree').tree({//系统子项树
	//checkbox:true,//cascadeCheck:false,//onlyLeafCheck:true,
	formatter:function(node){
		var txt = node.name;
		if(node.level!="root") txt = node.code+" "+txt;
		if(dual) txt = txt+" | "+node.dualName;
		var childrenNum = 0;
		if(node.speChildren) childrenNum = node.speChildren.length;
		if(node.status == "失效"){
			var style = "background-color:lightgray;color:#fff";
		}else if(node.level=="spe" && childrenNum == 0){
			var style = "color:red";
		}else var style = "";
		if(node.level=="spe"){
			txt += " - "+childrenNum;
		}else{
			var workCount = 0;
			var Ids = [];
			_getObjId(node.children, Ids);
			for(var i=0;i<Ids.length;i++){
				workCount += $('#divtree').tree('find', Ids[i]).speChildren.length;
			}
			txt += " - "+workCount;
		}
		txt = "<span style='"+style+"'>"+txt+"</span>";
		if(node.level=="spe") txt += "&nbsp;<a id='gospe"+node.id+"'></a>";
		return txt;
	},
	onSelect:function(node){
		//_uncheckGrid('#spegrid');
		$('a[id^="gospe"]').hide();
		$('#gospe'+currDiv).show();
		if ($(subitemEdting).is(':hidden')) {//非编辑模式下、当前专业列表非当前子项时可切换专业列表
			$('#gospe'+node.id).linkbutton({
				onClick:function(){
					hiddenIdList = [];
					_enterNode(node.id);
				}
			}).show();
			if(node.id!=currDiv)
				$('#gospe'+node.id).linkbutton('enable').html('&nbsp;进入&nbsp;');
			else $('#gospe'+node.id).linkbutton('disable').html('&nbsp;已进入&nbsp;');
		}
	},
	onDblClick:function(node){
		if(node.id!=currDiv && $(subitemEdting).is(':hidden') && node.level=="spe"){
			hiddenIdList = [];
			_enterNode(node.id);
			$('#divtree').tree('select', node.target);
		}
	},
	onLoadSuccess: function (node, data) {
		$('#speformdiv').window('close');
	},
	onCheck:function(node){
		//_uncheckGrid('#spegrid');
		if(node.checked){
			$(node.target).find("span.tree-title").css({"background-color":"#0e2d5f","color":"#fff"});
		}else $(node.target).find("span.tree-title").removeAttr("style");
	},
	/* onContextMenu: function(e, node){
		if (node != null) {
			$('#divtree').tree('select', node.target);
			subitemContextRow = node;
			e.preventDefault(); //阻止浏览器捕获右键事件
			$('#subitem_grid_ContextMenu').menu('show', {//显示右键菜单
				left: e.pageX,//在鼠标点击处显示菜单
				top: e.pageY
			});
			e.preventDefault();  //阻止浏览器自带的右键菜单弹出
		}
	} */
});
	
$('#subitem_grid').datagrid({//子项表格
	rownumbers: true,
	border: false,method:'get',
	singleSelect: false,
	fit: true,
	idField: 'id',
	//treeField: 'code',
	frozenColumns: [[
			{ field: 'code', title: '节点代号', align: 'left', halign: 'center', formatter:function(value,row,index){
				return '<img src="../../themes/icons/tree_file.png" /> '+value;
				},width:80 },
			{ field: 'name', title: '节点名称', align: 'left', halign: 'center',formatter: function(value,row,index){
					return "<a href='javascript:_workExcute("+index+");'>"+value+"</a>";
				}, editor: { type: 'validatebox', options: {disabled: true, required: false } } },
			{ field: 'description', title: '说明', align: 'left', halign: 'center', editor: { type: 'validatebox', options: { required: false } } },
			
	]],
	columns: [[
			{ field: 'startDate', title: '计划起始日期', width: 95, editor: { type: 'datebox'} },
			{ field: 'endDate', title: '计划完成日期', width: 95, editor: { type: 'datebox'} },
			{ field: 'workHour', title: '*计划工时', styler: function(value,row,index){if(row.level=="work"&&(!value||value==0)) return 'background-color:#EEE8AA;color:red;';}, editor: { type: 'numberbox', options: { required: false, min: 0 } } },
			{ field: 'designName', title: '*设计人', styler: function(value,row,index){if(row.level=="work"&&(!value||value==0)) return 'background-color:#EEE8AA;color:red;';}, editor: { type: 'combobox', options: { panelHeight: 'auto', hasDownArrow: false, required: false, valueField: 'num', textField: 'name', editable: false } } },
			{ field: 'checkName', title: '*校核人', styler: function(value,row,index){if(row.level=="work"&&(!value||value==0)) return 'background-color:#EEE8AA;color:red;';}, editor: { type: 'combobox', options: { panelHeight: 'auto',  hasDownArrow: false, required: false, valueField: 'num', textField: 'name', editable: false } } },
			{ field: 'reviewName', title: '*审核人', styler: function(value,row,index){if(row.level=="work"&&(!value||value==0)) return 'background-color:#EEE8AA;color:red;';}, editor: { type: 'combobox', options: { panelHeight: 'auto',  hasDownArrow: false, required: false, valueField: 'num', textField: 'name', editable: false } } },
			{
				field: 'status', title: '状态', width: 45, editor: { type: 'combobox', options: { hasDownArrow: false, disabled:true, valueField: 'name', textField: 'name', data: [{ name: '未启动' }, { name: '执行中' }, { name: '已完成' }, { name: '失效' }] } },
				styler: function (value, row, index) {
					if (row.status == "失效") {
						return 'background-color:lightgray;';
					}
				}
			},
			{ field: 'authorName', title: '创建者', width: 45 },
			{ field: 'approveName', title: '审定人', width: 45, hidden: true, editor: { type: 'combobox', options: { panelHeight: 'auto',  hasDownArrow: false, required: false, valueField: 'num', textField: 'name', editable: false } } },
			{ field: 'certifiedName', title: '注册工程师', width: 65, hidden: true, editor: { type: 'combobox', options: { panelHeight: 'auto',  hasDownArrow: false, required: false, valueField: 'num', textField: 'name', editable: false } } },
			{ field: 'schemeName', title: '方案设计人', width: 65, hidden: true, editor: { type: 'combobox', options: { panelHeight: 'auto',  hasDownArrow: false, required: false, valueField: 'num', textField: 'name', editable: false } } },
			// { field: 'othersCount', title: '其它数量', width: 50, hidden: true, editor: { type: 'textbox', options: { required: false, hidden: true } } },
			{
				field: 'otherslist', 
				/* formatter: function (val, row) {
					if (typeof (val) == "object") {
						row.otherslist = JSON.stringify(row.otherslist);
						return row.otherslist;
					}
					else {
						return val;
					}
				},  */
				title: '其他人员', width: 50, hidden: true, editor: { type: 'textbox', options: { required: false, hidden: true } }
			},
			{
				field: 'otherName', title: '其他参与人', width: 70, align: 'right',hidden: true, 
				formatter: function managerRoleFormatfunction(val, row) {
					if(row.level == "work"){
						var text = "无";
						if (row.otherslist.othersCount) {
							text = row.otherslist.othersCount;
						}
						//if (row.authorNum != null && row.authorNum == userNum) 
							text += "&nbsp;<a class='l-btn l-btn-small' name='btnothers' onclick=allocateusers('" + row.id + "')>分配</a>";
						
						return text;
					}
				}
			},
			{ field: 'grandNum', title: '*专业孙项号', styler: function(value,row,index){if(row.level=="work"&&value==0) return 'background-color:#EEE8AA;color:red;';}, width: 70, editor: { type: 'numberbox', options: { required: false, min: 0, precision: 0 } } },
			{ field: 'grandName', title: '专业孙项名称', width: 80, hidden: true, editor: { type: 'validatebox', options: { required: false } } },
			{ field: 'workgrandNum', title: '*工作包孙项号', hidden: true, styler: function(value,row,index){if(row.level=="work"&&value==0) return 'background-color:#EEE8AA;color:red;';}, width: 80, editor: { type: 'numberbox', options: { required: false, min: 0, precision: 0 } } },
			{ field: 'matCode', title: '关联物料编码', width: 80, hidden: true, editor: { type: 'validatebox', options: {required: false } } },
			{ field: 'matName', title: '关联物料名称', width: 80, hidden: true, editor: { type: 'validatebox', options: {required: false } } },
			{ field: 'src', title: '节点来源', width: 320, hidden: true, },
			{ field: 'dwgNum', title: '工作包图纸数量', width: 95, hidden: true, editor: { type: 'validatebox', options: { required: false } } },
			{ field: 'amountTrial', title: '送审额', width: 45, hidden: true, editor: { type: 'validatebox', options: { required: false } } },
			{ field: 'amountAppr', title: '核定额', width: 45, hidden: true, editor: { type: 'validatebox', options: { required: false } } },
			{ field: 'budget', title: '预算值', width: 45, hidden: true, editor: { type: 'validatebox', options: { required: false } } }
	]],
	onLoadSuccess: function(data){
		for(var i=0;i<hiddenIdList.length;i++){
			$('#wbs_layout').find("tr[datagrid-row-index='" + $('#subitem_grid').datagrid('getRowIndex',hiddenIdList[i]) + "']").hide();
		}
		$('#totalwork').text(data.rows.length);
		$('#hiddenwork').text(hiddenIdList.length);
		$('#displaywork').text(data.rows.length - hiddenIdList.length);
	},
	onDblClickRow: function (index, row) {
/* 		if($('#subitem_toolbar_excute').is(":visible")){
			_workExcute(index);
			$('#subitem_grid').datagrid('clearSelections').datagrid('selectRow', index);//取消选择其他行，选中当前行
		}else{ */
			var parent = $('#divtree').tree('find', currDiv);
			if (parent.status == "失效" || row.status == "失效" || row.level != "work" || !($(subitemEdting).is(":visible")||$(subitemCheck).is(":visible"))) return false;
			_EditRow([row],"beginEdit");
		//}
	},
	onRowContextMenu: function (e, index, row) { //右键时触发事件
		if (row != null) {
			subitemContextRow = row;
			e.preventDefault(); //阻止浏览器捕获右键事件
			$('#subitem_grid_ContextMenu').menu('show', {//显示右键菜单
				left: e.pageX,//在鼠标点击处显示菜单
				top: e.pageY
			});
			e.preventDefault();  //阻止浏览器自带的右键菜单弹出
		}
	},
	onSelect: function (index, row) {
		//$('a[id^="src_a"]').hide();
		var srcID = row.srcID;
		if ($('#src_a' + srcID)) {
			$('#src_a' + srcID).linkbutton({}).html('&nbsp;<font color=red>进入</font>&nbsp;').show();
			$('#src_a' + srcID).unbind("click");
			$('#src_a' + srcID).bind('click', function () {
				//alert(row.src + "---" + this.id.replace("src_a", "") + row.srcStru);
				$(document.body).layout('expand', 'west');
				$('#reftabs').tabs('select', 1);
				LoadWbsHisTree(this.id.replace("src_a", ""), row.srcStru);
				//event.cancelBubble = true;
				return false;
			});
		}
	}
}).datagrid('resize');
$('#subitem_grid').datagrid('options').url = prjdrifwbsdataurl;
//子项表格右键菜单
$('#subitem_grid_ContextMenu').menu({ noline: true }).menu('appendItem', {
	text: '全选可见节点',
	onclick: function () {
		var treeRoot = $('#subitem_grid').datagrid('getRows');
		var ContextIds = [];
		if (treeRoot) {
			_getObjId(treeRoot, ContextIds);
		}
		for (var i = 0; i < ContextIds.length; i++) {
			if($('#wbs_layout').find("tr[datagrid-row-index='" + $('#subitem_grid').datagrid('getRowIndex',ContextIds[i]) + "']").is(":visible"))
				$('#subitem_grid').datagrid('selectRow', $('#subitem_grid').datagrid('getRowIndex',ContextIds[i]));
		}
	}
}).menu('appendItem', {
	text: '清空选择所有节点',
	onclick: function () {
		$('#subitem_grid').datagrid('clearSelections');
	}
});
$('#setHidden').linkbutton({//过滤工作包
	plain:true,
	onClick:function(){
		//$('#form2div1').prepend("<input id='ckbworktype"+industryArr[i].id+"' type='checkbox'>"+industryArr[i].type+"&nbsp;&nbsp;");
        var treeRoot = $('#subitem_grid').datagrid('getRows');
		var ContextIds = [];
		$('#form2div1,#form2div2,#form2div3,#form2div4').empty();//先清空原div元素
		if (treeRoot) {
			var type_list = [];
			var stat_list = [];
			var rec_list = [];
			var design_list = [];
			_getObjId(treeRoot, ContextIds);
			for (var i = 0; i < ContextIds.length; i++) {
				if($('#wbs_layout').find("tr[datagrid-row-index='" + $('#subitem_grid').datagrid('getRowIndex',ContextIds[i]) + "']").is(":visible")){
					type_list[type_list.length] = treeRoot[i].worktypeID;
					stat_list[stat_list.length] = treeRoot[i].status;
					if(treeRoot[i].recvSpec) rec_list[rec_list.length] = treeRoot[i].recvSpec;
					if(treeRoot[i].designName) design_list[design_list.length] = treeRoot[i].designName;
				}
			}
			typeArr = _uniqueArr(type_list);
			for(var i = 0; i < typeArr.length; i++) {
				$('#form2div1').append("<input id='ckbworktype"+typeArr[i]+"' type='checkbox'>"+workTypeList[typeArr[i]]);
				if(i<typeArr.length - 1) $('#form2div1').append("&nbsp;&nbsp;");
			}
			statArr = _uniqueArr(stat_list);
			$('#form2div2').append("<input id='ckbworksel' type='checkbox'>被选中&nbsp;&nbsp;");
			for(var i = 0; i < statArr.length; i++) {
				$('#form2div2').append("<input id='ckbworkstat"+i+"' name='"+statArr[i]+"' type='checkbox'>"+statArr[i]);
				if(i<statArr.length - 1) $('#form2div2').append("&nbsp;&nbsp;");
			}
			recArr = _uniqueArr(rec_list);
			for(var i = 0; i < recArr.length; i++) {
				$('#form2div3').append("<input id='ckbrec"+recArr[i]+"' type='checkbox'>"+speList[recArr[i]]);
				if(i<recArr.length - 1) $('#form2div3').append("&nbsp;&nbsp;");
			}
			designArr = _uniqueArr(design_list);
			for(var i = 0; i < designArr.length; i++) {
				$('#form2div4').append("<input id='ckbdesign"+i+"' name='"+designArr[i]+"' type='checkbox'>"+designArr[i]);
				if(i<statArr.length - 1) $('#form2div4').append("&nbsp;&nbsp;");
			}
		}
		$('#sethiddenwindow').window("open");
	}
}).html("&nbsp过滤工作包&nbsp");
function _uniqueArr(Arr){//自定义数组去重
	var result = [];
	for(var i = 0; i < Arr.length - 1; i++) {
		var isUnique = true;
		for(var j = i+1; j < Arr.length; j++) {
			if(Arr[i]==Arr[j]){
				isUnique = false;
				break;
			}
		}
		if(isUnique) result[result.length] = Arr[i];
	}
	result[result.length] = Arr[Arr.length - 1];
	return result;
}
$('#btnfiltertypeall').linkbutton({
	plain:true,
	onClick: function () {
		$('#form2div1').find(":checkbox").prop("checked",true);
	}
});
$('#btnfiltertypenone').linkbutton({
	plain:true,
	onClick: function () {
		$('#form2div1').find(":checkbox").prop("checked",false);
	}
});
$('#btnfilterstatall').linkbutton({
	plain:true,
	onClick: function () {
		$('#form2div2').find(":checkbox").prop("checked",true);
	}
});
$('#btnfilterstatnone').linkbutton({
	plain:true,
	onClick: function () {
		$('#form2div2').find(":checkbox").prop("checked",false);
	}
});
$('#btnfilterrecall').linkbutton({
	plain:true,
	onClick: function () {
		$('#form2div3').find(":checkbox").prop("checked",true);
	}
});
$('#btnfilterrecnone').linkbutton({
	plain:true,
	onClick: function () {
		$('#form2div3').find(":checkbox").prop("checked",false);
	}
});
$('#btnfilterdesignall').linkbutton({
	plain:true,
	onClick: function () {
		$('#form2div4').find(":checkbox").prop("checked",true);
	}
});
$('#btnfilterdesignnone').linkbutton({
	plain:true,
	onClick: function () {
		$('#form2div4').find(":checkbox").prop("checked",false);
	}
});
$('#btnhiddenapply').linkbutton({//应用过滤
	iconCls: 'icon-ok',
	onClick: function () {
		var hiddenArr = [];
		if($('#ckbworksel').is(":checked")){
			var selArr = $('#subitem_grid').datagrid('getSelections');
		}else var selArr =[];
		var ckbtypeArr = $('#form2div1').find(":checked");
		var typeArr = [];
		for(var i=0;i<ckbtypeArr.length;i++){
			typeArr[typeArr.length] = ckbtypeArr[i].id.substr(11);
		}
		var ckbstatArr = $('#form2div2').find(":checked");
		var statArr = [];
		for(var i=0;i<ckbstatArr.length;i++){
			statArr[statArr.length] = ckbstatArr[i].name;
		}
		var ckbrecArr = $('#form2div3').find(":checked");
		var recArr = [];
		for(var i=0;i<ckbrecArr.length;i++){
			recArr[recArr.length] = ckbrecArr[i].id.substr(6);
		}
		var ckbdesignArr = $('#form2div4').find(":checked");
		var designArr = [];
		for(var i=0;i<ckbdesignArr.length;i++){
			designArr[designArr.length] = ckbdesignArr[i].name;
		}
		var treeData = $('#subitem_grid').datagrid('getRows');
		for(var i=0;i<treeData.length;i++){
			if($('#wbs_layout').find("tr[datagrid-row-index='" + $('#subitem_grid').datagrid('getRowIndex',treeData[i].id) + "']").is(":visible") && treeData[i].level == "work"){
				if($.inArray(treeData[i],selArr)>-1){
					hiddenArr[hiddenArr.length] = treeData[i].id;
					hiddenIdList[hiddenIdList.length] = treeData[i].id;
				}else if($.inArray(treeData[i].worktypeID,typeArr)>-1 || $.inArray(treeData[i].status,statArr)>-1
				|| $.inArray(treeData[i].recvSpec,recArr)>-1 || $.inArray(treeData[i].designName,designArr)>-1){
					hiddenArr[hiddenArr.length] = treeData[i].id;
					hiddenIdList[hiddenIdList.length] = treeData[i].id;
				}
			}
		}
		for(var i=0;i<hiddenArr.length;i++){
			$('#subitem_grid').datagrid('unselectRow',$('#subitem_grid').datagrid('getRowIndex', hiddenArr[i]));
			$('#wbs_layout').find("tr[datagrid-row-index='" + $('#subitem_grid').datagrid('getRowIndex',hiddenArr[i]) + "']").hide();
		}
		var hiddentotal = parseInt($('#hiddenwork').text())+hiddenArr.length;
		$('#hiddenwork').text(hiddentotal);
		$('#displaywork').text(parseInt($('#totalwork').text())-hiddentotal);
		$('#sethiddenwindow').window("close");
		$.messager.show({
			title:'过滤工作包',
			msg:'本次过滤了'+hiddenArr.length+'个工作包。'
		});
	}
});
$('#rejHidden').linkbutton({//解除过滤
	plain:true,
	onClick:function(){
		var rejCount = 0;
		var treeData = $('#subitem_grid').datagrid('getRows');
		for(var i=0;i<treeData.length;i++){
			if($('#wbs_layout').find("tr[datagrid-row-index='" + $('#subitem_grid').datagrid('getRowIndex',treeData[i].id) + "']").is(":hidden")){
				rejCount++;
				$('#wbs_layout').find("tr[datagrid-row-index='" + $('#subitem_grid').datagrid('getRowIndex',treeData[i].id) + "']").show();
			}
		}
		$('#hiddenwork').text(0);
		$('#displaywork').text($('#totalwork').text());
		hiddenIdList = [];
		$.messager.show({
			title:'解除过滤',
			msg:rejCount+'个工作包解除过滤。'
		});
	}
}).html("&nbsp解除过滤&nbsp");
$('#driftshowall').change(_hideMoreCols);//显示更多列
function _hideMoreCols(){
	var moreCols = ['approveName','certifiedName','schemeName','otherName','grandName','workgrandNum','matCode','matName','src','dwgNum','amountTrial','amountAppr','budget'];
	if($('#driftshowall').is(":checked")){
		for(var i=0;i<moreCols.length;i++){$('#subitem_grid').datagrid('showColumn', moreCols[i]);}
	}else for(var i=0;i<moreCols.length;i++){$('#subitem_grid').datagrid('hideColumn', moreCols[i]);}
}
$('#mywork').change(_showMywork);//我的设计工作包
function _showMywork(){
	if($('#mywork').is(":checked")){
		$.messager.confirm('操作提示', '只显示自己参与设计的工作包，请确认是否继续？', function (r) {
			if (r) {
				$('#mycheck').prop("checked",false);
				reloaddriftwbstree();
			}else $('#mywork').prop("checked",false);
		});
	}else{
		$.messager.confirm('操作提示', '显示当前专业全部工作包，请确认是否继续？', function (r) {
			if (r) {
				reloaddriftwbstree();
			}else $('#mywork').prop("checked",true);
		});
	}
}
$('#mycheck').change(_showMycheck);//我的校审工作包
function _showMycheck(){
	if($('#mycheck').is(":checked")){
		$.messager.confirm('操作提示', '只显示自己参与校审的工作包，请确认是否继续？', function (r) {
			if (r) {
				$('#mywork').prop("checked",false);
				reloaddriftwbstree();
			}else $('#mycheck').prop("checked",false);
		});
	}else{
		$.messager.confirm('操作提示', '显示当前专业全部工作包，请确认是否继续？', function (r) {
			if (r) {
				reloaddriftwbstree();
			}else $('#mycheck').prop("checked",true);
		});
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
function _appendToSubitem(SourceRows, icon_cls){
	var Editing = false;
	if(SourceRows.length > 0) {
		var workrows = $('#subitem_grid').datagrid('getRows');
		var grands = new Object();//建立目标行子节点孙项号、工作包流水号模型对象
		for (var i = 0;i<workrows.length;i++){
			var workname = workrows[i].name.substr(0,2) + workrows[i].name.substr(String(workrows[i].workgrandNum).length + 2);
			if(workrows[i].level == "work" && grands[workrows[i].grandNum] == undefined){
				grands[workrows[i].grandNum] = {};
				grands[workrows[i].grandNum][workname] = [parseInt(workrows[i].workgrandNum)];
			}else if (workrows[i].level == "work"){
				if(grands[workrows[i].grandNum][workname] == undefined){
					grands[workrows[i].grandNum][workname] = [parseInt(workrows[i].workgrandNum)];
				}else grands[workrows[i].grandNum][workname][grands[workrows[i].grandNum][workname].length] = parseInt(workrows[i].workgrandNum);
			}
		}
		var rows = [];//获取当前树节点id与当前表行id数组合并后取最大值
		_appendChildren($('#divtree').tree('find', currDiv), SourceRows);
	}
	if(Editing){
		var lastIndex = 0;
		for(var i=0;i<rows.length;i++){
			$('#subitem_grid').datagrid('appendRow', rows[i]);
			if(i==rows.length-1) lastIndex = $('#subitem_grid').datagrid('getRowIndex', rows[i].id);
		}
		_EditRow(cancelId);
		$('#subitem_grid').datagrid('scrollTo',lastIndex);
		$('#totalwork').text(parseInt($('#totalwork').text())+cancelId.length);
		$('#displaywork').text(parseInt($('#displaywork').text())+cancelId.length);
	}else $.messager.alert('操作提示', '请先选择被引用的工作包。<br>项目经理专业不设接口条件工作包。<br>不可向本专业提交接口条件。', 'error');
	function _appendChildren(targetrow, sourcerow) {//生成新节点
		for (var i = 0; i < sourcerow.length; i++) {
			var unique = true;
			if (sourcerow[i].name == targetrow.name+"接口条件") unique = false;//过滤相同专业不存在接口条件
			if (sourcerow[i].name.indexOf("接口条件") > 0 && targetrow.name=="项目经理") unique = false;
			if (unique) {
				Editing = true;
				subitem_newId++;
				//editId[editId.length] = subitem_newId;
				cancelId[cancelId.length] = subitem_newId;
				if (sourcerow[i].iconCls) {
					var rowicon = sourcerow[i].iconCls;
				}else var rowicon = icon_cls;
				var newrow = sourcerow[i];
				newrow.id = subitem_newId;
				newrow.parentId = targetrow.id;

				//专业孙项号的设置以及最大孙项号叠加
				if (newrow.worktypeID != unproductId) {
					if (newrow.name.indexOf("接口条件") > 0) {
						var newname = newrow.code + targetrow.name + "-" + newrow.name;
					}else var newname = newrow.code.substring(newrow.code.length - 2, newrow.code.length) + newrow.name;
					if(grands[newrow.grandNum] != undefined){
						if(grands[newrow.grandNum][newname] != undefined){
							newrow.workgrandNum = Math.max.apply(null, grands[newrow.grandNum][newname]) + 1;
							grands[newrow.grandNum][newname][grands[newrow.grandNum][newname].length] = newrow.workgrandNum;
						}else{
							newrow.workgrandNum = 1;
							grands[newrow.grandNum][newname] = [1];
						}
					}else{
						grands[newrow.grandNum] = {};
						newrow.workgrandNum = 1;
						grands[newrow.grandNum][newname] = [1];
					}
					if (newrow.name.indexOf("接口条件") > 0) {
						//当前工作包专业名称+“-”+接收专业名称+“接口条件”newrow.name = newrow.code + String(newrow.workgrandNum).substr(0,1) + targetrow.name + "-" + newrow.name;
						newrow.name = newrow.code + newrow.workgrandNum + targetrow.name + "-" + newrow.name;
					}//newrow.name = newrow.code.substring(newrow.code.length - 2, newrow.code.length) + String(newrow.workgrandNum).substr(0,1) + newrow.name;
					else newrow.name = newrow.code.substring(newrow.code.length - 2, newrow.code.length) + newrow.workgrandNum + newrow.name;
				}else newrow.workgrandNum = "";
				if (newrow.startDate == null || newrow.startDate == "") {
					newrow.startDate = targetrow.startDate;
				}
				if (newrow.endDate == null || newrow.endDate == "") {
					newrow.endDate = targetrow.endDate;
				}
				newrow.code = "";
				newrow["majorCode"] = targetrow.majorCode;
				newrow.authorName = userName;
				newrow.authorNum = perID;
				newrow.authorClass = isCH ? "CH" : "PM";
				rows[rows.length] = newrow;
			}
		}
	}
}
function _removeSubitem() {//从子项表格删除节点
	var selectedRows = $('#subitem_grid').datagrid('getSelections');
	var removeRows = [];
	for (var i = 0; i < selectedRows.length; i++) {
		if (selectedRows[i].level == "work" && selectedRows[i].status == "未启动" && selectedRows[i].authorNum == perID) {
				removeRows[removeRows.length] = {"id":selectedRows[i].id};
		}
	}
	if (removeRows.length == 0) {
		$.messager.alert('操作提示', '未选中权限内可执行操作的“未启动”状态工作包。<br>注意：只能删除本人创建的工作包。', 'error');
	} else {
		$.messager.confirm('操作提示', '注意：已选中' + removeRows.length + '个待删除的工作包，<br>是否确定删除？', function (r) {
			if (r) {
				for (var i = 0; i < removeRows.length; i++) {
					$('#subitem_grid').datagrid('unselectRow', $('#subitem_grid').datagrid('getRowIndex', removeRows[i].id)).datagrid('deleteRow', $('#subitem_grid').datagrid('getRowIndex', removeRows[i].id));
				}
				$('#subitem_toolbar_save').linkbutton('enable');
				var strRows = JSON.stringify($('#subitem_grid').datagrid('getRows'));//更新树
				$('#divtree').tree('update', {target: $('#divtree').tree('find',currDiv).target,speChildren:JSON.parse(strRows)});
				$('#totalwork').text(parseInt($('#totalwork').text())-removeRows.length);
				$('#displaywork').text(parseInt($('#displaywork').text())-removeRows.length);
			}
		});
	}
}
function _AcceptTreegridEdting() {//接受对子项表格的编辑操作
	var nullId = [];
	var levelnodes = eval(JSON.stringify($('#subitem_grid').datagrid("getRows")));
	for (var i = 0; i < editId.length; i++) {
		if($.inArray(editId[i], endEditId) < 0){
			var editIndex = $('#subitem_grid').datagrid('getRowIndex',editId[i]);
			var edit_editor = $('#subitem_grid').datagrid('getEditor', { index: editIndex, field: 'name' });
			var sdate_editor = $('#subitem_grid').datagrid('getEditor', { index: editIndex, field: 'startDate' });
			var edate_editor = $('#subitem_grid').datagrid('getEditor', { index: editIndex, field: 'endDate' });
			if ((edit_editor != null && edit_editor.target[0].value != "" && sdate_editor != null && $(sdate_editor.target).datebox('getValue') != "" && edate_editor != null && $(edate_editor.target).datebox('getValue') != "")
				) {
				if (sdate_editor && $(sdate_editor.target).datebox('getValue').stringToDate() > $(edate_editor.target).datebox('getValue').stringToDate()) {
					$.messager.alert({ title: '提示', msg: '计划完成日期不可早于计划起始日期，请更正。' });
					return false;
				}
				//非标设备工作包孙项号必填
				//判断 取节点名称第四字符起+专业孙项号+工作包孙项号组合，不可存在相同字符串
				var wh_editor = $('#subitem_grid').datagrid('getEditor', { index: editIndex, field: 'workHour' });
				var gd_editor = $('#subitem_grid').datagrid('getEditor', { index: editIndex, field: 'grandNum' });
				var wgd_editor = $('#subitem_grid').datagrid('getEditor', { index: editIndex, field: 'workgrandNum' });
				var name = edit_editor.target[0].value;
				var Regx = /(^[A-Za-z0-9]$)/;
				var isCode = true;
				for(var j=1;j<3;j++){
					var val = name.substring(j-1, j);
					if(!Regx.test(val)) isCode = false;
					break;
				}
				if(isCode) name = name.substring(0, 2) + $(wgd_editor.target).numberbox('getValue') + name.substring(3, name.length);
				if ($(gd_editor.target).numberbox('getValue') == "" || $(gd_editor.target).numberbox('getValue') == 0 || $(gd_editor.target).numberbox('getValue') > 9){
					$.messager.alert({ title: '提示', msg: '专业孙项号应为不超过9的正整数值，请更正。' });
					return false;
				}else if ($(wgd_editor.target).numberbox('getValue') == "" || $(wgd_editor.target).numberbox('getValue') == 0 || $(wgd_editor.target).numberbox('getValue') > 9){
					$.messager.alert({ title: '提示', msg: '工作包孙项号应为不超过9的正整数值，请更正。' });
					return false;
				}else{
					var code = name.substring(2, name.length) + $(gd_editor.target).numberbox('getValue');
					for (var lv = 0; lv < levelnodes.length; lv++) {
						if (levelnodes[lv].status != "失效" && levelnodes[lv].worktypeID != unproductId) {
							if (levelnodes[lv].id != editId[i] && levelnodes[lv].workgrandNum + levelnodes[lv].name.substring(3, levelnodes[lv].name.length) + levelnodes[lv].grandNum == code) {
								$.messager.alert({ title: '提示', msg: '存在相同专业孙项重复工作包孙项号，请更正。' });
								return false;
							}
						}
					}
				}
				//更新人员id和name
				var d_editor = $($('#subitem_grid').datagrid('getEditor', { index: editIndex, field: 'designName' }).target).combo("getValue");
				var d_editort = $($('#subitem_grid').datagrid('getEditor', { index: editIndex, field: 'designName' }).target).combo("getText");
				var c_editor = $($('#subitem_grid').datagrid('getEditor', { index: editIndex, field: 'checkName' }).target).combo("getValue");
				var c_editort = $($('#subitem_grid').datagrid('getEditor', { index: editIndex, field: 'checkName' }).target).combo("getText");
				var r_editor = $($('#subitem_grid').datagrid('getEditor', { index: editIndex, field: 'reviewName' }).target).combo("getValue");
				var r_editort = $($('#subitem_grid').datagrid('getEditor', { index: editIndex, field: 'reviewName' }).target).combo("getText");
				var a_editor = $($('#subitem_grid').datagrid('getEditor', { index: editIndex, field: 'approveName' }).target).combo("getValue");
				var a_editort = $($('#subitem_grid').datagrid('getEditor', { index: editIndex, field: 'approveName' }).target).combo("getText");
				var e_editor = $($('#subitem_grid').datagrid('getEditor', { index: editIndex, field: 'certifiedName' }).target).combo("getValue");
				var e_editort = $($('#subitem_grid').datagrid('getEditor', { index: editIndex, field: 'certifiedName' }).target).combo("getText");
				var sc_editor = $($('#subitem_grid').datagrid('getEditor', { index: editIndex, field: 'schemeName' }).target).combo("getValue");
				var sc_editort = $($('#subitem_grid').datagrid('getEditor', { index: editIndex, field: 'schemeName' }).target).combo("getText");
				var rowMajor = levelnodes[editIndex].majorCode;
				if ((d_editort != "" && d_editort == c_editort) || (rowMajor!="2PM" && c_editort != "" && c_editort == r_editort) || (r_editort != "" && d_editort == r_editort)) {
					$.messager.alert({ title: '提示', msg: '设计人、校核人、审核人存在重复，请更正。' });
					return false;
				}
				if (($(wh_editor.target).numberbox('getValue') == "" || $(wh_editor.target).numberbox('getValue') == 0)||
					d_editort== ""||c_editort== ""||r_editort==""){
					if($.inArray(levelnodes[editIndex].status,["执行中","已完成"])>-1){
						$.messager.alert({ title: '提示', msg: '“执行中、已完成”工作包的计划工时、设校审人不可为空或0，请更正。' });
						return false;
					}else $.messager.show({ title: '提示', msg: '计划工时、设校审人不可为空或0，否则会造成工作包不能启动。' });
				}
				var dstart = $(sdate_editor.target).datebox('getValue');
				var dend = $(edate_editor.target).datebox('getValue');
				$('#subitem_grid').datagrid('endEdit', editIndex);//通过校验则结束编辑状态
				endEditId.push(editId[i]);
				$('#subitem_grid').datagrid("updateRow", {
					index: editIndex,
					row: {
						"designNum": d_editor,
						"designName": d_editort,
						"checkNum": c_editor,
						"checkName": c_editort,
						"reviewNum": r_editor,
						"reviewName": r_editort,
						"approveNum": a_editor,
						"approveName": a_editort,
						"certifiedNum": e_editor,
						"certifiedName": e_editort,
						"schemeNum": sc_editor,
						"schemeName": sc_editort,
						"name": name,
						"startDate": dstart,
						"endDate": dend
					}
				});
				if(levelnodes[editIndex].otherslist.paOthers==null){
					$('#subitem_grid').datagrid("updateRow", {index: editIndex,row:{"otherslist":{"othersCount":"无","paOthers":[]}}});
				}else $('#subitem_grid').datagrid("updateRow", {index: editIndex,row:{"otherslist":levelnodes[editIndex].otherslist}});
				if ($.inArray(editId[i], cancelId) != -1) {//并删除其在"放弃则删除未启动行"数组中的值
					cancelId.splice($.inArray(editId[i], cancelId), 1);
				}
			} else {//否则维持编辑状态并记录下来
				nullId.push(editId[i]);
			}
		}
	}
	if (nullId.length == 0) {//无空输入框时
		var strRows = JSON.stringify($('#subitem_grid').datagrid('getRows'));//更新树
		$('#divtree').tree('update', {target: $('#divtree').tree('find',currDiv).target,speChildren:JSON.parse(strRows)});
		$(subitemEdting).hide();
		$(subitemNoEdting).show();
		$('#subitem_toolbar_save').linkbutton('enable');
		editId = [];
		endEditId = [];
		cancelId = [];
	} else {
		$.messager.alert({ title: '提示', msg: '必输项存在空值，请完成填写。' });
	}
}

//切换专业
$("#majorsdiv").html("");
for (var i = 0; i < specialty.length; i++) {
	$("#majorsdiv").append("<a id=major" + specialty[i].id + ">" + specialty[i].name + "</a>&nbsp;");
	$("#major" + specialty[i].id).linkbutton({plain:true});
	$("#major" + specialty[i].id).click(function () {
		if (tmpmajor.substring(5) == this.id.substring(5)) return;
		if (!CheckMajorSelect(this)) return;
	});
}
$("#majorALL").click(function () {
	if (tmpmajor == this.id) return;
	if (!CheckMajorSelect(this)) return;
});
if (specialty.length == 1) {
	$("#major" + specialty[0].id).linkbutton("select");
	tmpmajor = "major" + specialty[0].id;
	reloaddriftwbstree();
}else {
	if (specialty.length> 1) {
		for (var i = 0; i < specialty.length; i++) {
			$("#majorsdiv_w").append("<a id=wajor" + specialty[i].id + ">" + specialty[i].name + "</a>&nbsp;");
			$("#wajor" + specialty[i].id).linkbutton({plain:true});
			$("#wajor" + specialty[i].id).click(function () {
				if (!CheckMajorSelect(this)) return;
			});
		}
		$("#majorsdiv_w").append("<a id=wajorALL>不限</a>");
		$("#wajorALL").linkbutton({plain:true}).click(function () {
			if (tmpmajor == this.id) return;
			if (!CheckMajorSelect(this)) return;
		});
	}else {
		$("#majorsdiv_w").html("需要项目管理员为该项目配置专业。");
	}
	$('#speformdiv').window("open");
}

function CheckMajorSelect(obj) {
	if (!$('#subitem_toolbar_save').linkbutton('options').disabled || $(subitemEdting).is(':visible')) {
		var message = "将放弃所有未保存的编辑，切换到 "+obj.innerText+" 专业，请确认是否继续？";
	}else var message = "将切换到 "+obj.innerText+" 专业，请确认是否继续？";
	$.messager.confirm('操作提示', message, function (r) {
		if (r) {
			ChangeMajor(obj);
			return true;
		}else return false;
	});
}

function _getObjValArr(Obj, result, allkey, gethidden) {//递归获取树形数据对象内各字段值数组
    if (Obj) {
        for (var i = 0; i < Obj.length; i++) {
            if (allkey == undefined) {
                result[result.length] = { "id": Obj[i].id, "code": Obj[i].code, "name": Obj[i].name, "parentId": Obj[i].parentId, "majorCode": Obj[i].majorCode };
            } else {
				result[result.length] = {};
				for(var key in Obj[i]){
					if(key!="children") result[result.length - 1][key] = Obj[i][key];
				}
            }
			if(gethidden){
				if($('#wbs_layout').find("tr[datagrid-row-index='" + $('#subitem_grid').datagrid('getRowIndex',Obj[i].id) + "']").is(":hidden"))
					gethidden[gethidden.length] = Obj[i].id;
			}
            if (Obj[i].children) {
                _getObjValArr(Obj[i].children, result, allkey, gethidden);
            }
        }
    }
}
//切换专业
function ChangeMajor(obj) {
    if (tmpmajor)
        $("#major" + tmpmajor.substring(5)).linkbutton("unselect");
    $(obj).linkbutton("select");
    tmpmajor = obj.id;
    reloaddriftwbstree();
	if (sysloaded) $('#gridPrjSys').datagrid('loadData',[]);
}
function reloaddriftwbstree(nodeId,noupgrade) {
	$('#speformdiv').panel('collapse').panel('setTitle', "正在执行，请稍候...");//window折叠
	$('#speformdiv').window('open');
	hiddenIdList = [];
	cancelId = [];
	editId = [];
	endEditId = [];
	$('#subitem_toolbar_checkout').linkbutton('disable').show();
	$('#subitem_toolbar_excute').show();
	$(subitemCheck).hide();//默认禁用签出
	$('#subitem_toolbar_checkin').hide();
	if(tmpmajor.substring(5)!="ALL"){
		$.ajax({
			url: prjbasicdataurl + "?projID=" + prjid + "&phaseID=" + prjPhaseID + "&" + userPar,
			type: "GET",
			async : false,
			success: function (data) {
				try {
					dataprj = eval(data)["data"];
					var wbsStat = eval(data)["returnStatus"];
				}
				catch (e) {
					dataprj = eval("(" + data + ")");
					var wbsStat = eval("(" + data + ")")["returnStatus"];
				}
				if(wbsStat=="F"){
					alert("该项目尚未发布WBS，请先完成二级策划并发布WBS。");
					window.close();
				}else if(wbsStat=="S"){
					prjName = dataprj.projName;
					prjPhase = dataprj.phaseName;
					prjManagerName = dataprj.mgr;
					userName=dataprj.userName;
					if(dataprj.vmgrNum){
						var vmgr = dataprj.vmgrNum.split(",");
					}else var vmgr = [];
					if(perID==""+dataprj.mgrNum || perID==""+dataprj.gmgrNum || $.inArray(perID,vmgr)>-1) isPM = true;
					pubwbsID = dataprj.pubwbsID;
					division = dataprj.division;
					headOrbranch = dataprj.headOrbranch;
					dual=dataprj.dual;
					prjNumber = dataprj.projNumber;
					specialty = [];
					if(guest){
						$.ajax({
							url: prj_major_dataurl+"?projID="+prjid+"&phaseID="+prjPhaseID+"&"+userPar,
							type: "GET",
							async : false,
							success: function (prjspelist) {
								for(var i=0;i<prjspelist.length;i++){
									specialty[i] = {};
									specialty[i].id = prjspelist[i].industryCode;
									specialty[i].name = prjspelist[i].name;
								}
							}
						});
					}else specialty = dataprj.specialty;
					if(specialty.length==0){
						alert("非项目成员仅限只读模式。");
						window.location.href = window.location+"&mode=guest";
					}
					wbsVerName = dataprj.pubwbsName;
					SubItemControl(tmpmajor.substring(5));
				}
			}
		});
		isCH = false;
		var checkoutable = false;
		for(var i=0;i<specialty.length;i++){
			if(specialty[i].id==tmpmajor.substring(5)){
				var majorName = specialty[i].name;
				$('#myworkdiv,#mycheckdiv').show();
				if(!guest){
					if($.inArray(perID,specialty[i].chgrNum)>-1) isCH = true;//校验是否当前专业负责人
					if(specialty[i].lockBy==perID){
						var checkoutTxt = " 状态：策划中";
						$('#mywork,#mycheck').prop('checked',false);
						$('#myworkdiv,#mycheckdiv').hide();
						$('#subitem_toolbar_checkout,#subitem_toolbar_excute').hide();
						$(subitemNoEdting).show();//当前签出人与当前用户相同则自动签出
					}else if(specialty[i].lockBy==0){
						checkoutable = true;
						var checkoutTxt = " 状态：释放";
					}else var checkoutTxt = " 状态：其他用户（"+specialty[i].lockName+"）策划";
				}else var checkoutTxt = " 只读模式";
				break;
			}
		}
		if((isPM || isCH)&& checkoutable) $('#subitem_toolbar_checkout').linkbutton('enable');//未签出且当前用户具备签出权限
	}else{
		var checkoutTxt = "";
		var majorName = "不限";
	}
	if(majorName){
		$('#wbs_layout').layout('panel', 'north').panel("setTitle", wbsVerName+" - 专业："+majorName+checkoutTxt);
		$('#wbs_layout div.panel-title').text(wbsVerName+" - 专业："+majorName+checkoutTxt);
		$('#wbs_layout_center div.panel-title').text("");
		$('#wbs_layout_center').layout('panel', 'west').panel("setTitle", "导航 - 系统子项树");
		$('#wbs_layout').layout('collapse', 'north');
		$("#major" + tmpmajor.substring(5)).linkbutton("select");
	}
	if(!noupgrade){
		$.ajax({
			url:prjdrifwbsdataurl+"?projectId="+prjid+"&phaseCode="+prjPhaseID+"&structureVerId="+pubwbsID+"&spec="+tmpmajor.substring(5)+"&"+userPar,
			type:"GET",
			success:function(data){
				//var data = headOrbranch=="head"?xdata:[xdata[0].children[0]];
				$('#divtree').tree('loadData',data);
				$('#subitem_toolbar_save').linkbutton('disable');
				subitem_Vals = JSON.stringify(_initialSubitemVals());
				if(nodeId){
					_enterNode(nodeId);
					$('#divtree').tree('select',$('#divtree').tree('find',nodeId).target);
				}else{
					if(($('#myworkdiv').is(":visible") && $('#mywork').is(":checked")) || ($('#mycheckdiv').is(":visible") && $('#mycheck').is(":checked"))){
						var trIds = [];
						_getObjId(data, trIds);
						for(var i=0;i<trIds.length;i++){
							if($('#divtree').tree('find',trIds[i]).level=="spe"){
								var updateworks = [];
								for(var j=0;j<$('#divtree').tree('find',trIds[i]).speChildren.length;j++){
									if(($('#myworkdiv').is(":visible") && $('#mywork').is(":checked") && $('#divtree').tree('find',trIds[i]).speChildren[j].designNum==perID) ||
										($('#mycheckdiv').is(":visible") && $('#mycheck').is(":checked") && ($('#divtree').tree('find',trIds[i]).speChildren[j].checkNum==perID ||
										$('#divtree').tree('find',trIds[i]).speChildren[j].reviewNum==perID))){
										updateworks[updateworks.length] = $('#divtree').tree('find',trIds[i]).speChildren[j];
									}
								}
								if(updateworks.length>0){
									$('#divtree').tree('update', {target: $('#divtree').tree('find',trIds[i]).target,speChildren:eval(JSON.stringify(updateworks))});
								}else $('#divtree').tree('remove', $('#divtree').tree('find',trIds[i]).target);
							}
						}
						for(var i=0;i<trIds.length-1;i++){
							if($('#divtree').tree('find',trIds[trIds.length-1-i]) && $('#divtree').tree('find',trIds[trIds.length-1-i]).children.length==0 && $('#divtree').tree('find',trIds[trIds.length-1-i]).speChildren.length==0)
								$('#divtree').tree('remove', $('#divtree').tree('find',trIds[trIds.length-1-i]).target);
						}
						$('#divtree').tree('loadData', $('#divtree').tree('getRoots'));
					}
					var Ids = [];
					var getSpe = false;
					_getObjId($('#divtree').tree('getRoots'), Ids);
					for(var i=0;i<Ids.length;i++){
						if($('#divtree').tree('find',Ids[i]).level=="spe"){
							_enterNode(Ids[i]);
							$('#divtree').tree('select',$('#divtree').tree('find',Ids[i]).target);
							getSpe = true;
							break;
						}
					}
					if(!getSpe){
						$('#spepage').panel('setTitle',"无可用专业节点");
						currDiv = null;
						$('#subitem_grid').datagrid('loadData',[]);
					}
				}
			}
		});
	}
}
function _initialSubitemVals() {//记录系统子项表初始参照值
	var keyArr = ["parentId","id","name","endDate","startDate","designNum","checkNum","reviewNum",
			"approveNum","certifiedNum","workHour","majorCode","grandNum","workgrandNum","workCode","description",
			"status","grandName","matCode","matName","srcID","srcRow","srcClass","authorClass","authorNum","dlvrExtId",
			"amountTrial","amountAppr","schemeNum","dwgNum","budget","level","otherslist","recvSpec"];
	var data = $('#divtree').tree('getRoots');
	var treeIds = [];
	_getObjId(data, treeIds);
	subitem_newId = Math.max.apply(null, treeIds);
	var obj = [];
	_getArr(data[0]);
	return obj;
	function _getArr(input){
		if(input.speChildren && input.speChildren.length>0){
			for(var i=0;i<input.speChildren.length;i++){
				obj[obj.length]={};
				for(var j=0;j<keyArr.length;j++){
					for(var key in input.speChildren[i]){
						if(key==keyArr[j]){
							obj[obj.length-1][keyArr[j]]=input.speChildren[i][keyArr[j]];
							break;
						}
					}
				}
			}
		}
		if(input.children && input.children.length>0){
			for(var i=0;i<input.children.length;i++){
				_getArr(input.children[i]);
			}
		}
	}
}
function _enterNode(id,tree,grid){//进入节点详情
	if(tree && tree=='#histree'){
		hisId = id;
		var pageId = '#hispage';
	}else{
		currDiv = id;
		var pageId = '#spepage';
	}
	var treeId = tree ? tree : '#divtree';
	var node = $(treeId).tree('find', id);
	var key = "speChildren";
	if(node[key]){
		var sNode = JSON.stringify(node[key]);
		var gridId = grid ? grid : '#subitem_grid';
		$(gridId).datagrid('clearSelections').datagrid('loadData',eval(sNode));
	}
	var titleTxt = "";
	_getParentText(id,treeId,titleTxt)
	titleTxt = titleTxt.substring(3);
	if(node.status) titleTxt += " - 状态："+node.status;
	if(!tree) titleTxt += " 计划起止日期："+node.startDate+" 至 "+node.endDate;
	$(pageId).panel('setTitle',titleTxt);
	function _getParentText(id,tree){
		var node = $(tree).tree('find', id);
		if(node.level!="root"){
			var nodename = node.name ? "name" : "dlvrName";
			titleTxt = " > "+node.code+" "+node[nodename]+titleTxt;
			_getParentText($(tree).tree('getParent', node.target).id,tree);
		}
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

$('#changelist').linkbutton({//图纸状态变更记录
    plain:true,
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
}).html("&nbsp图纸状态变更记录&nbsp");
//分配其他参与人
var currentOtherRow = null;
function allocateusers(id) {
	currentOtherRow = $('#subitem_grid').datagrid('getRows')[$('#subitem_grid').datagrid('getRowIndex', id)];
	var parent = $('#divtree').tree('find', currDiv);
	if (parent.status == "失效" || currentOtherRow.status == "失效" || currentOtherRow.level != "work" || !($(subitemEdting).is(":visible")||$(subitemCheck).is(":visible"))) return false;
    var othersobj = currentOtherRow.otherslist;
    try { othersobj = eval(othersobj); } catch (e) { othersobj = eval("(" + othersobj + ")"); }
    //加载专业待选择人员,同步加载在本工作包里参与的角色
	sel = othersobj.paOthers;
    var majoruserlist = [];
    //去除重复角色
	if(panelValues[currentOtherRow.majorCode])
		for (var u in panelValues[currentOtherRow.majorCode]) {
			for(var k=0;k<panelValues[currentOtherRow.majorCode][u].length;k++){
				if(k>0){
					var ishave = false;
					for (var t = 0; t < majoruserlist.length; t++) {
						if (majoruserlist[t].num == panelValues[currentOtherRow.majorCode][u][k].num) {
							ishave = true;
							break;
						}
					}
					if (!ishave)
						majoruserlist[majoruserlist.length] = { num: panelValues[currentOtherRow.majorCode][u][k].num, name: panelValues[currentOtherRow.majorCode][u][k].name, duty:""};
				}
			}
		}
    var newlist = [];
    for (var i = 0; i < majoruserlist.length; i++) {
        var ishave = false;
        if (sel){
            for (var t = 0; t < sel.length; t++) {
				if(!sel[t].duty){
					sel[t].duty = "";
					_alreadyrole(sel[t],"otherNum",currentOtherRow);
				}
                if (majoruserlist[i].num == sel[t].otherNum) {
                    ishave = true;
                    break;
                }
			}
		}else sel = [];
        if (!ishave){
			_alreadyrole(majoruserlist[i], "num", currentOtherRow);
			newlist[newlist.length] = majoruserlist[i];
		}
    }
	function _alreadyrole(majoruserlist, num, currentOtherRow){
		if(majoruserlist[num]==currentOtherRow.designNum) majoruserlist.duty = majoruserlist.duty+"，设计人";
		if(majoruserlist[num]==currentOtherRow.checkNum) majoruserlist.duty = majoruserlist.duty+"，校核人";
		if(majoruserlist[num]==currentOtherRow.reviewNum) majoruserlist.duty = majoruserlist.duty+"，审核人";
		if(majoruserlist[num]==currentOtherRow.approveNum) majoruserlist.duty = majoruserlist.duty+"，审定人";
		if(majoruserlist[num]==currentOtherRow.certifiedNum) majoruserlist.duty = majoruserlist.duty+"，注册工程师";
		if(majoruserlist[num]==currentOtherRow.schemeNum) majoruserlist.duty = majoruserlist.duty+"，方案设计人";
		if(majoruserlist.duty.charAt(0) == "，") majoruserlist.duty = majoruserlist.duty.substring(1);
	}
    $('#grid_userall').datagrid({data: newlist});
    $('#grid_usersel').datagrid({
		data:sel,
		onClickRow: function(index,row){
			$('#grid_usersel').datagrid('beginEdit', index);
		},
		showFooter:true
    });
    var datarowsall = $('#grid_usersel').datagrid("getRows")
	total = onAfterMajorEdit();
	$('#otherallpercent').numberbox('setValue', total);
	$('#grid_usersel').datagrid('loadData',{"total":datarowsall.length,"rows":datarowsall,"footer":[{"duty":"计算比例总和","otherRatio":total}]});
    $('#divothers').window('open');
	//event.cancelBubble = true;
    try { e.preventDefault(); } catch (e) { }
}

$("#usersel").click(function () {
    var sel = $('#grid_userall').datagrid("getSelections");
    var havesel = $('#grid_usersel').datagrid("getRows");
    for (var i = 0; i < sel.length; i++) {
        var rowIndex = $('#grid_userall').datagrid('getRowIndex', sel[i]);
        $('#grid_usersel').datagrid('insertRow', {
            index: havesel.length + i, row: {
                otherName: sel[i].name,
                otherNum: sel[i].num,
				duty:sel[i].duty,
                otherRatio: 0
            }
        });
        $('#grid_userall').datagrid('deleteRow', rowIndex);
    }
});
$("#userunsel").click(function () {
    var sel = $('#grid_usersel').datagrid("getSelections");
    var havesel = $('#grid_userall').datagrid("getRows");
    for (var i = 0; i < sel.length; i++) {
        var rowIndex = $('#grid_usersel').datagrid('getRowIndex', sel[i]);
        $('#grid_userall').datagrid('insertRow', {
            index: havesel.length + i, row: {
                name: sel[i].otherName,
                num: sel[i].otherNum,
				duty:sel[i].duty
            }
        });
        $('#grid_usersel').datagrid('deleteRow', rowIndex);
    }
});

function onClickMajorRow(index) {
    $('#grid_usersel').datagrid('selectRow', index)
            .datagrid('beginEdit', index);
}

$('#majorratesave').linkbutton({
    onClick: function () {
        $('#grid_usersel').datagrid('acceptChanges');
        var datarows = $('#grid_usersel').datagrid("getRows")//"getChecked");
        var total = 0;
        for (var i = 0; i < datarows.length; i++) {
            if (datarows[i].otherRatio == "" || parseFloat(datarows[i].otherRatio) <= 0) {
                $.messager.alert('操作提示', '不可应用：比例存在非正数值，请修改。', 'error');
                return false;
            }else total += parseFloat(datarows[i].otherRatio);
        }
		total = Math.round(total*100)/100
		$('#grid_usersel').datagrid('loadData',{"total":datarows.length,"rows":datarows,"footer":[{"duty":"计算比例总和","otherRatio":total}]});
		var givtotal = $('#otherallpercent').numberbox('getValue');
        if (total > givtotal) {
            $.messager.alert('操作提示', '不可应用：比例总和超过预设总比例，请修改。', 'error');
            return false;
        }
        else if (total < givtotal) {
			$.messager.alert('操作提示', '不可应用：比例总和小于预设总比例，请修改。', 'error');
            return false;
        }
        var specialty = [];
        for (var i = 0; i < datarows.length; i++) {
            specialty[specialty.length] = { "otherNum": datarows[i].otherNum, "otherName": datarows[i].otherName, "otherRatio": Math.round(parseFloat(datarows[i].otherRatio)*100)/100 };
        }
		$('#subitem_grid').datagrid('updateRow',{
			index:$('#subitem_grid').datagrid('getRowIndex',currentOtherRow.id),
			row:{
				otherslist:{
					othersCount:datarows.length == 0 ? "无" : datarows.length + "人",
					paOthers:specialty//JSON.stringify(specialty)
				}
			}
		});
		_AcceptTreegridEdting();
        $('#divothers').window('close');
    }
});

//平分剩余比例
$('#majorratesplit').linkbutton({
    onClick: function () {
        $('#grid_usersel').datagrid('acceptChanges');
        var givtotal = $('#otherallpercent').numberbox('getValue');
        if (givtotal == 0) {
            $.messager.alert('操作提示', '不可等分：比例总和不能为0，请修改。', 'error');
			return false;
        }
        var datarowsall = $('#grid_usersel').datagrid("getRows")//getChecked");
        var datarows = $('#grid_usersel').datagrid("getChecked");
        if (datarows.length == 0) {
            $.messager.alert('操作提示', '不可等分：至少选中一行。', 'error');
			return false;
        }
        var unchecktotal = 0;
        var checkedtotal = 0;
        for (var i = 0; i < datarows.length; i++) {
            if (datarows[i].otherRatio != "") {
                checkedtotal += parseFloat(datarows[i].otherRatio);
            }
        }
        for (var k = 0; k < datarowsall.length; k++) {
            var unchecked = true;
            for (var i = 0; i < datarows.length; i++) {
                if (datarows[i].otherNum == datarowsall[k].otherNum) {
                    unchecked = false;
                    break;
                }
            }
            if (unchecked && datarowsall[k].otherRatio != "") {
                unchecktotal += parseFloat(datarowsall[k].otherRatio);
            }
        }
        if (unchecktotal < givtotal) {
            var left = givtotal - unchecktotal;
            var avg = Math.round(left / datarows.length * 100) / 100;
            var totaladded = 0;
            for (var i = 0; i < datarows.length; i++) {
				totaladded += avg
				if(i == datarows.length - 1 && totaladded != left)
					avg = Math.round((avg + left - totaladded) * 100) / 100;
                $('#grid_usersel').datagrid('updateRow', {
                    index: $('#grid_usersel').datagrid("getRowIndex", datarows[i]),
                    row: { otherRatio: avg }
                });
            }
        }
        var total = onAfterMajorEdit();
		$('#grid_usersel').datagrid('loadData',{"total":datarowsall.length,"rows":datarowsall,"footer":[{"duty":"计算比例总和","otherRatio":total}]});
	}
});

function onAfterMajorEdit() {
    var total = 0;
    var datarows = $('#grid_usersel').datagrid("getRows")//"getChecked");
    for (var i = 0; i < datarows.length; i++) {
        if (datarows[i].otherRatio != "") {
            total += parseFloat(datarows[i].otherRatio);
        }
    }
	return Math.round(total*100)/100;
}
//求和计算
$('#majorratesum').linkbutton({
    onClick: function () {
        $('#grid_usersel').datagrid('acceptChanges');
        var datarowsall = $('#grid_usersel').datagrid("getRows")
		total = onAfterMajorEdit();
		$('#grid_usersel').datagrid('loadData',{"total":datarowsall.length,"rows":datarowsall,"footer":[{"duty":"计算比例总和","otherRatio":total}]});
	}
});

//修改人员
$("#edituser").click(function () {
    var rows = $('#subitem_grid').datagrid('getSelections');
    var editrows = [];
	var parent = $('#divtree').tree('find', currDiv);
	if(parent.status != "失效")
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].level == "work" && rows[i].status != "失效") editrows[editrows.length] = rows[i];
		}
    if (editrows.length == 0) {
        $.messager.alert("操作提示", "请先选择需要批量设置人员的工作包。<br>注意：状态必须为“未启动”或“执行中”，且当前专业状态不可为“失效”。", "error");
    } else {
		var speid = null;
		for (var i = 0; i < editrows.length; i++) {
			var row = $('#subitem_grid').datagrid('getRows')[$('#subitem_grid').datagrid('getRowIndex', editrows[i].id)];
			if(i==0){
				speid = row.majorCode;
				spename = $('#divtree').tree('find', currDiv).name;
			}else{
				if(row.majorCode != speid){
                    $.messager.alert("操作提示", "请先选择需要批量设置人员的工作包。<br>注意：状态必须为“未启动”或“执行中”且专业相同。", "error");
                    return false;
				}
			}
		}
        $('#formuser').window('open').panel('setTitle', "批量修改设校审人员 - 专业："+spename);
		for(var i=0; i<dutys.length;i++){
			if(panelValues[speid] && panelValues[speid][dutys[i]]) $("#txt"+dutys[i]).combobox("loadData", panelValues[speid][dutys[i]]);
			$("#ckb"+dutys[i]).prop("checked", false);
			$("#txt"+dutys[i]).combo("disable");
		}
    }
});
function _enableCombo(ckbid, txtid){//切换下拉框激活状态
	if ($(ckbid).is(':checked')) {
		$(txtid).combo("enable");
	}else $(txtid).combo("disable");
}
$('#btnuserapply').linkbutton({
    onClick: function () {
        var selectObj = new Object();
		var selectckb = false;
		for(var i=0; i<dutys.length;i++){
			if($("#ckb"+dutys[i]).is(':checked')) {
				selectObj[dutys[i]+"Num"] = parseInt($("#txt"+dutys[i]).combobox("getValue"));
				selectckb = true;
			}
		}
		if(!selectckb){
			$('#formuser').window('close');
		}else{
			var pTitle =$('#formuser').panel('options').title.replace("批量修改设校审人员 - 专业：","");
			if((selectObj.designNum && selectObj.checkNum && !isNaN(selectObj.designNum) && selectObj.designNum == selectObj.checkNum)
				||(selectObj.designNum && selectObj.reviewNum && !isNaN(selectObj.designNum) && selectObj.designNum == selectObj.reviewNum)
				||(selectObj.reviewNum && selectObj.checkNum && !isNaN(selectObj.checkNum) && selectObj.reviewNum == selectObj.checkNum && pTitle!="项目经理")){
				$.messager.alert({ title: '提示', msg: '不可应用：设计人、校核人、审核人存在重复。' });
				return false;
			}else{
				var rows = $('#subitem_grid').datagrid('getSelections');
				var i=0;
				var int=window.setInterval(function(){
					if(i==1) $('#loaddiv').window('open');
					if(i>0) $('#loaddiv').panel('setTitle', Math.round(parseFloat(100*i/rows.length))+'%已处理，请稍候...');
					if(i<rows.length){
						if (rows[i].level == "work" && rows[i].status != "失效") {
							var updateVal = new Object();
							for(var j = 0; j<dutys.length;j++){
								if(selectObj[dutys[j]+"Num"]!= undefined){
									if(isNaN(selectObj[dutys[j]+"Num"])){
										updateVal[dutys[j]+"Num"] = null;
									}else updateVal[dutys[j]+"Num"] = selectObj[dutys[j]+"Num"];
								}else updateVal[dutys[j]+"Num"] = rows[i][dutys[j]+"Num"];
							}
							editId[editId.length] = rows[i].id;
							$('#subitem_grid').datagrid('beginEdit', $('#subitem_grid').datagrid('getRowIndex',rows[i].id));
							var pNode = $('#divtree').tree('find', currDiv);
							_seteditor(pNode, rows[i].id, rows[i]);
							_setComboboxValues(rows[i].majorCode, rows[i].id, updateVal);
						}
						i++;
					}else{
						int=window.clearInterval(int);
						$('#loaddiv').window('close');
					}
				},0);
				$(".datebox :text").attr("readonly", "readonly");
				$(subitemNoEdting).hide();
				$(subitemEdting).show();
				$('#formuser').window('close');
			}
		}
    }
});
function _EditRow(rows, beginEdit, statuson){
	if(rows.length>1){
		$('#subitem_grid').datagrid('autoSizeColumn','name');
		$('#loaddiv').window('open');
		var i=0;
		var int=window.setInterval(function(){
			if(i<rows.length){
				$('#loaddiv').panel('setTitle', Math.round(parseFloat(100*i/rows.length))+'%已处理，请稍候...');
				_editSingleRow(rows[i]);
				i++;
			}else{
				int=window.clearInterval(int);
				$('#loaddiv').window('close');
			}
		},0);
	}else if(rows.length==1) _editSingleRow(rows[0]);
	$(".datebox :text").attr("readonly", "readonly");
	$(subitemNoEdting).hide();
    $(subitemEdting).show();
	function _editSingleRow(input){
		if(input.id){
			var id = input.id;
			var row = input;
			editId[editId.length] = id;
		}else {
			var id = input;
			var row = $('#subitem_grid').datagrid('getRows')[$('#subitem_grid').datagrid('getRowIndex', id)];
		}
		if(statuson)
			$('#subitem_grid').datagrid('updateRow',{
				index:$('#subitem_grid').datagrid('getRowIndex', id),
				row:{status:"执行中"}
			});
		if(beginEdit){
			$('#subitem_grid').datagrid('beginEdit', $('#subitem_grid').datagrid('getRowIndex', id));
			var pNode = $('#divtree').tree('find', currDiv);
			_seteditor(pNode, id, row);
			_setComboboxValues(row.majorCode, id, row);
		}
	}
}
function _seteditor(pNode, id, row){//设置起止日期初值和校验器
	var s_editor = $('#subitem_grid').datagrid('getEditor', { index: $('#subitem_grid').datagrid('getRowIndex', id), field: 'startDate' });
	var e_editor = $('#subitem_grid').datagrid('getEditor', { index: $('#subitem_grid').datagrid('getRowIndex', id), field: 'endDate' });
	if(!isPM && row.authorClass=="PM"){
		$(s_editor.target).combo('disable');
		$(e_editor.target).combo('disable');
		return;
	}
	if(row.startDate) $(s_editor.target).datebox('setValue', row.startDate);
	$(s_editor.target).datebox('calendar').calendar({
		validator: function (date) {
			return pNode.startDate.stringToDate() <= date && date <= pNode.endDate.stringToDate();
		}
	});
	if($(s_editor.target).combo('getValue') == "" || pNode.startDate.stringToDate() > row.startDate.stringToDate() || 
	row.startDate.stringToDate() > pNode.endDate.stringToDate()) $(s_editor.target).datebox('setValue', pNode.startDate);
	if(row.endDate) $(e_editor.target).datebox('setValue', row.endDate);
	$(e_editor.target).datebox('calendar').calendar({
		validator: function (date) {
			return pNode.startDate.stringToDate() <= date && date <= pNode.endDate.stringToDate();
		}
	});
	if($(e_editor.target).combo('getValue') == "" || pNode.startDate.stringToDate() > row.endDate.stringToDate() || 
	row.endDate.stringToDate() > pNode.endDate.stringToDate()) $(e_editor.target).datebox('setValue', pNode.endDate);
}
function _setComboboxValues(specialty,rowid,row){//设置人员列表
	for (var i=0;i<dutys.length;i++){
		var combobox_editor = $('#subitem_grid').datagrid('getEditor', { index: $('#subitem_grid').datagrid('getRowIndex',rowid), field: dutys[i]+"Name" });
		if(panelValues[specialty] && panelValues[specialty][dutys[i]]){
			$(combobox_editor.target).combobox("loadData", panelValues[specialty][dutys[i]]);
			if(row){
				var ishave = false;
				for(var j = 0;j<panelValues[specialty][dutys[i]].length;j++){
					if(panelValues[specialty][dutys[i]][j].num == row[dutys[i]+"Num"]){
						$(combobox_editor.target).combobox("select", row[dutys[i]+"Num"]);
						ishave = true;
						break;
					}
				}
				if(!ishave) $(combobox_editor.target).combobox("select", null);
			}
		}else $(combobox_editor.target).combobox("select", null);
	}
}