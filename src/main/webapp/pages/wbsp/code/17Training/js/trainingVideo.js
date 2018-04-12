$(function () {
	var userlist = [];
	for(var key in dgData){
		userlist[userlist.length] = {};
		userlist[userlist.length-1]["value"] = key;
		userlist[userlist.length-1]["text"] = key+"-"+dgData[key]["userName"]+"-"+dgData[key]["prjName"];
	}
	var projID = null;
	$('#numLogin').combobox({
		data:userlist,
		editable:false,
		panelHeight: 'auto',
		valueField:'value',    
		textField:'text',
		onChange:function(newValue){
			_refreshNavi(newValue);
		}
	});
	/* $('#numLogin').searchbox({
		searcher: function (value) {
			if(!dgData[value]){
				$.messager.show({ title: '提示', msg: '非本次培训员工号或输入有误，请修正后重试。',showType:'fade',style:{left:0,top:0}});
			}else{
				_refreshNavi(value);
			}
		}
	}); */
	function _refreshNavi(value){
		$('#dgtoolbar').html("&nbsp;"+dgData[value].prjNum+"<br>&nbsp;"+dgData[value].prjName+" <input id='phasecombo' style='width:150px'>");
		$('#dg').datagrid({
			data:dgData[value].prjData,
			idField: 'id',
			fitColumns: true,
			toolbar:'#dgtoolbar',
			rownumbers: true, singleSelect: true,
			fit: true,
			columns: [[{ field: 'id', title: '员工号', width: 75 },
				{ field: 'name', title: '姓名', width: 70 },
				{ field: 'unit', title: '部门/专业', width: 90},
				{ field: 'role', title: '角色', width:160}]]
		}).datagrid('clearSelections');
		$('#dgLayout').panel('setTitle', "操作导航区"+" - 当前用户："+dgData[value].userName);
		projID = dgData[value].projID;
		$('#phasecombo').combobox({    
			url:"http://"+window.location.host+"/api/lev2/queryPhaseList?projID="+projID,//oracle包-当前项目阶段列表,
			method:'get',
			editable:false,
			panelHeight: 'auto',
			valueField:'phaseID',    
			textField:'phaseName',
			onLoadSuccess:function(){
				$('#phasecombo').combobox('setValue',$('#phasecombo').combobox('getData')[$('#phasecombo').combobox('getData').length-1].phaseID);
			}
		}); 
		$("#navLayout").layout("split", "south");
		$('#macros').datagrid({
			data:[
				{"id":38,"name":"工作日历 <a id=macro_37 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/workCalendar/workCalendar.html"},
				{"id":10,"name":"设计输入管理平台2.0 <a id=macro_9 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/DSINPUT/DESIGNINPUT.html"},
				{"id":11,"name":"更多流程 <a id=macro_10 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/otOAList/otOAList.html"},
				{"id":12,"name":"设计变更管理 <a id=macro_11 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/DSChange/DSChange.html"},
				{"id":29,"name":"设计文印管理 <a id=macro_28 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/Publish/Publish.html"},
				{"id":1,"name":"二级策划 <a id=macro_0 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/WBSLV2/WBSLV2.html"},
				{"id":2,"name":"三级策划 <a id=macro_1 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/WBSLV3/WBSLV3.html"},
				{"id":30,"name":"建筑院图纸校审 <a id=macro_29 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/DrawReview/DrawReview.html"},
				{"id":9,"name":"NIMS2.0培训环境:8080 <a id=macro_8 style='display:none'>进入</a>","password":"同所选角色员工号","href":"http://192.168.15.95:8080/index.html"},
				{"id":6,"name":"OA（新）15.198 <a id=macro_5 style='display:none'>进入</a>","password":1,"href":"http://192.168.15.198/login/LoginAdmin.jsp"},
				{"id":4,"name":"EBS:8010 <a id=macro_3 style='display:none'>进入</a>","password":111111,"href":"http://ebsapptest.nerin.com:8010"},
				{"id":8,"name":"ECM:weblogic@15.252 <a id=macro_7 style='display:none'>进入</a>","password":"weblogic123","href":"http://192.168.15.252:16200/cs/login/login.htm"},
				{"id":39,"name":"Swagger-ui <a id=macro_38 style='display:none'>进入</a>","href":"/swagger-ui.html"},
				{"id":32,"name":"mode1设计成果校审 <a id=macro_31 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/CheckReview/CheckReview.html?chapterId=104500,104501,104540&taskHeaderId=9508&projID=284666&phaseID=1171&inDlvrId=836411"},
				{"id":34,"name":"mode2设计成果校审 <a id=macro_33 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/CheckReview/CheckReview.html?projID=88474&phaseID=1022&inDlvrId=201919"},
				{"id":35,"name":"mode3设计成果校审 <a id=macro_34 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/CheckReview/CheckReview.html?chapterId=104118,104123,104338&taskHeaderId=9268&projID=88474&phaseID=1022&inDlvrId=201919"},
				{"id":36,"name":"mode4设计成果校审 <a id=macro_35 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/CheckReview/CheckReview.html?projID=88474&phaseID=1022&requestid=201325&erpid=321"},
				{"id":37,"name":"mode5设计成果校审 <a id=macro_36 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/CheckReview/CheckReview.html?projID=88474&phaseID=1022&requestid=201325&erpid=321"},
				{"id":31,"name":"图纸浏览 <a id=macro_30 style='display:none'>进入</a>","password":"","href":"http://"+window.location.host+"/pages/wbsp/code/DrawReview/drawExplore.html?workId=201919"},
				{"id":15,"name":"专业输入模板查询 <a id=macro_14 style='display:none'>进入</a>","password":1,"href":"http://192.168.15.198/formmode/search/CustomSearchBySimple.jsp?customid=554"},
				{"id":16,"name":"项目类型查询 <a id=macro_15 style='display:none'>进入</a>","password":1,"href":"http://192.168.15.198/formmode/search/CustomSearchBySimple.jsp?customid=565"},
				{"id":17,"name":"阶段类型查询 <a id=macro_16 style='display:none'>进入</a>","password":1,"href":"http://192.168.15.198/formmode/search/CustomSearchBySimple.jsp?customid=563"},
				{"id":18,"name":"专业输入查询 <a id=macro_17 style='display:none'>进入</a>","password":1,"href":"http://192.168.15.198/formmode/search/CustomSearchBySimple.jsp?customid=555"},
				{"id":19,"name":"项目输入查询 <a id=macro_18 style='display:none'>进入</a>","password":1,"href":"http://192.168.15.198/formmode/search/CustomSearchBySimple.jsp?customid=569"},
				{"id":20,"name":"设备资料查询 <a id=macro_19 style='display:none'>进入</a>","password":1,"href":"http://192.168.15.198/formmode/search/CustomSearchBySimple.jsp?customid=572"},
				{"id":21,"name":"客户财物查询 <a id=macro_20 style='display:none'>进入</a>","password":1,"href":"http://192.168.15.198/formmode/search/CustomSearchBySimple.jsp?customid=571"},
				{"id":22,"name":"正式阶段类型查询 <a id=macro_21 style='display:none'>进入</a>","password":1,"href":"http://oa.nerin.com/formmode/search/CustomSearchBySimple.jsp?customid=531"},
				{"id":23,"name":"正式项目类型查询 <a id=macro_22 style='display:none'>进入</a>","password":1,"href":"http://oa.nerin.com/formmode/search/CustomSearchBySimple.jsp?customid=532"},
				{"id":24,"name":"正式专业模板查询 <a id=macro_23 style='display:none'>进入</a>","password":1,"href":"http://oa.nerin.com/formmode/search/CustomSearchBySimple.jsp?customid=533"},
				{"id":25,"name":"正式专业输入查询 <a id=macro_24 style='display:none'>进入</a>","password":1,"href":"http://oa.nerin.com/formmode/search/CustomSearchBySimple.jsp?customid=534"},
				{"id":26,"name":"正式项目输入查询 <a id=macro_25 style='display:none'>进入</a>","password":1,"href":"http://oa.nerin.com/formmode/search/CustomSearchBySimple.jsp?customid=535"},
				{"id":27,"name":"正式设备反馈查询 <a id=macro_26 style='display:none'>进入</a>","password":1,"href":"http://oa.nerin.com/formmode/search/CustomSearchBySimple.jsp?customid=536"},
				{"id":28,"name":"正式客户财产查询 <a id=macro_27 style='display:none'>进入</a>","password":1,"href":"http://oa.nerin.com/formmode/search/CustomSearchBySimple.jsp?customid=537"},
				{"id":33,"name":"公司级评审导航 <a id=macro_32 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/nbccAppr/nbccAppr.html?taskHeaderId=9268&projID=12107&phaseID=1021"},
				{"id":13,"name":"PageOffice:NBCC <a id=macro_12 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/poweb/poweb.html?chapterId=104118,104123,104338&taskHeaderId=9268"},
				{"id":14,"name":"PageOffice:OA <a id=macro_13 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/poweb/poweb.html?chapterId=104118,104123,104338&taskHeaderId=9268"},
				{"id":3,"name":"OA（旧）25.156 <a id=macro_2 style='display:none'>进入</a>","password":1,"href":"http://192.168.25.156/login/LoginAdmin.jsp"},
				{"id":5,"name":"合同及投标文件模板库 <a id=macro_4 style='display:none'>进入</a>","password":"","href":"http://"+window.location.host+"/pages/wbsp/code/Tdata/Tdata.html"},
				{"id":7,"name":"NIMS2.0培训环境:8091 <a id=macro_6 style='display:none'>进入</a>","password":"同所选角色员工号","href":"http://192.168.15.95:8091/index.html?jobNo="}
				],
			idField: 'id',
			fitColumns: true,
			rownumbers: true, 
			singleSelect: true,
			fit: true,
			onSelect:function(index, row){
				$('a[id^="macro_"]').hide();
				$('#macro_'+(row.id-1)).linkbutton({
					onClick:function(){
						_enterMacro(row);
					}
				}).show().html('&nbsp;进入&nbsp;');
			},
			onDblClickRow:function(index, row){
				_enterMacro(row);
			},
			columns: [[{ field: 'name', title: '模块名称', width: 135 },
				{ field: 'password', title: '默认密码', width: 85 }]],
			onLoadSuccess:function(){
				$(this).datagrid('enableDnd');
			}
		}).datagrid('clearSelections');
	}
	$('#nav').tree({
		data : nav,
		onLoadSuccess : function (node, data) {
			if (data) {
				$(data).each(function (index, value) {
					if (this.state == 'closed') {
						$('#nav').tree('expandAll');
					}
				});
			}
		},
		onClick : function (node) {
			if (node.nid != 0) {
				if ($('#tabs').tabs('exists', node.text)) {
					$('#tabs').tabs('select', node.text);
				}else{
					$('#tabs').tabs('close', 1);
					$('#tabs').tabs('add', {
						title : node.text,
						closable : true,
						content : function(){
							$(this).append("<div id='a1' style='margin: 0 auto;text-align:center;'></div>");
							var flashvars={
								f:"http://192.168.27.55/NewFrontEnd/test/15111601NIMStraining/"+encodeURI(encodeURI(node.file)),//中文文件名会导致firefox浏览器加载文件失败。
								c:0,
								b:1
								};
							var params={bgcolor:'#FFF',allowFullScreen:true,allowScriptAccess:'always'};
							CKobject.embedSWF('ckplayer/ckplayer.swf','a1','ckplayer_a1',node.width,node.height,flashvars,params);
						}
					});
				}
			}else if(node.text == "浏览 任务清单"){
				$('#tabs').tabs('select', 0);
 			}else if(node.text == "下载 NIMS培训助手 & 新版CAD插件"){
				//window.open("http://192.168.27.55/NewFrontEnd/test/15111601NIMStraining/NIMS培训助手v2.4(可生成任务清单含新版CAD插件).zip"); 
			}
		}
	});
	$('#tabs').tabs({
		fit : true,
		border : false,
		plain : true,
	});
	$('#tabs').tabs('add', {
		title : '任务清单',
		closable : false,
		content : function(){
			$(this).append("<iframe src='任务清单-公司NIMS培训20151225.swf' id='iframepage' frameborder=0 scrolling='no' marginheight=0 marginwidth=0></iframe>");
		},
		onResize : function(width, height){
			$('#iframepage').height(height-4);
			$('#iframepage').width(width-2);
		}
	});
	function _enterMacro(row){
		if(row.id!=5){
			var staff = $('#dg').datagrid('getSelected');
		}else var staff = true;
/* 		if(!staff && (row.id<3 || row.id>9)){
			$.messager.show({ title: '提示', msg: '请先在上表中选择一个角色。',showType:'fade',style:{left:0,top:document.body.clientHeight-100}});
		}else{ */
			var url = row.href;
			if(row.id<3 || row.id==10){//wbsp、设计输入
				url = "http://"+window.location.host+url+"?projID="+projID+"&phaseID="+$('#phasecombo').combobox('getValue');//+"&userID="+staff.uid;
				window.open(url, '_blank');
				//_windowNoLocation(url,'yes');
			}else if(row.id==11 || row.id==12 || row.id==29 || row.id==30){//更多流程、设计变更、设计文印、图纸校审
				url = "http://"+window.location.host+url+"?projID="+projID+"&phaseID="+$('#phasecombo').combobox('getValue');
				window.open(url, '_blank');//_windowNoLocation(url,'no',[960,380]);
			}else if(row.id==13 || row.id == 38){//pageoffice?userID
				url = "http://"+window.location.host+url;
				//_windowNoLocation(url,'yes');
				window.open(url, '_blank');
			}else if(row.id==14){//pageoffice?workcode
				url = "http://"+window.location.host+url+"&requestid=201297";
				//_windowNoLocation(url,'yes');
				window.open(url, '_blank');
			}else if(row.id==7){//8091
				url = url+staff.id;
				window.open(url, '_blank');
			}else window.open(url, '_blank');
		//}
	}
	function _windowNoLocation(url,resizable,size){
		if(!size) var size = [screen.width, screen.height];
		var has_showModeless = !!window.showModelessDialog;//定义一个全局变量判定是否有原生showModalDialog方法  
		if(!has_showModeless){
			var has_showModal = !!window.showModalDialog;
			if(!has_showModal){
				window.open(url,'_blank','location=no');//console.log(size?size[0]:"nosize");
			}else window.showModalDialog(url,'','resizable:'+resizable+';dialogWidth:'+size[0]+'px;dialogHeight:'+size[1]+'px');
		}else window.showModelessDialog(url,'','resizable:'+resizable+';dialogWidth:'+size[0]+'px;dialogHeight:'+size[1]+'px');
	}
});
var dgData = {
	1501:{
		"userName":"测试甲",
		"projID":12107,
		"prjNum":"2A13A010401",
		"prjName":"广西南国150kt/a铜冶炼",
		"prjData":[
			{"id":"000000","name":"虚拟用户","unit":"系统管理员","role":"测试角色","uid":3085},
			{"id":100009,"name":"姚素平","unit":"总工办","role":"主管（副）总工程师","uid":2307},
			{"id":100232,"name":"袁剑平","unit":"项目经理","role":"项目经理","uid":1336,"pid":369},
			{"id":100984,"name":"唐斌","unit":"项目经理","role":"项目副经理","uid":1681},
			{"id":100243,"name":"毛志琨","unit":"冶金","role":"A专业负责人","uid":1343},
			{"id":101108,"name":"陈森","unit":"冶金","role":"A专业设计人","uid":1693},
			{"id":100240,"name":"尹湘华","unit":"冶金","role":"A专业校核人","uid":1342},
			{"id":100239,"name":"李明","unit":"冶金","role":"A专业审核人","uid":1341},
			{"id":100235,"name":"赵欣","unit":"冶金","role":"A专业主任工程师","uid":1337},
			{"id":100615,"name":"刘进","unit":"建筑","role":"B专业负责人","uid":2343},
			{"id":100971,"name":"韦雯","unit":"总工办","role":"评审管理员","uid":1676}
		]
	},
	1502:{
		"userName":"测试乙",
		"projID":258666,
		"prjNum":"2A16E980401",
		"prjName":"建筑院测试项目1",
		"prjData":[
			{"id":"000000","name":"虚拟用户","unit":"系统管理员","role":"测试角色","uid":3085},
			{"id":100009,"name":"姚素平","unit":"总工办","role":"主管（副）总工程师","uid":2307},
			{"id":101215,"name":"查淑萍","unit":"项目经理","role":"项目经理","uid":1839},
			{"id":100243,"name":"毛志琨","unit":"冶金","role":"A专业负责人","uid":1343},
			{"id":101108,"name":"陈森","unit":"冶金","role":"A专业设计人","uid":1693},
			{"id":100240,"name":"尹湘华","unit":"冶金","role":"A专业校核人","uid":1342},
			{"id":100239,"name":"李明","unit":"冶金","role":"A专业审核人","uid":1341},
			{"id":100235,"name":"赵欣","unit":"冶金","role":"A专业主任工程师","uid":1337},
			{"id":100615,"name":"刘进","unit":"建筑","role":"B专业负责人","uid":2343},
			{"id":100971,"name":"韦雯","unit":"总工办","role":"评审管理员","uid":1676}
		]
	},
	1601:{
		"userName":"测试项目一",
		"projID":264665,
		"prjNum":"2A17A020401",
		"prjName":"测试项目一",
		"prjData":[
			{"id":000000,"name":"虚拟用户","unit":"系统管理员","role":"测试角色","uid":3085},
			{"id":100009,"name":"姚素平","unit":"总工办","role":"主管（副）总工程师","uid":2307},
			{"id":100320,"name":"李大浪","unit":"项目经理","role":"项目经理","uid":1403},
			{"id":100320,"name":"李大浪","unit":"结构","role":"A专业负责人","uid":1403},
			{"id":101224,"name":"徐华冰","unit":"结构","role":"A专业设计人","uid":1724},
			{"id":100353,"name":"熊天屹","unit":"结构","role":"A专业校核人","uid":1412},
			{"id":100361,"name":"周丽霞","unit":"结构","role":"A专业审核人","uid":1418},
			{"id":100360,"name":"左菊林","unit":"结构","role":"A专业主任工程师","uid":2339},
			{"id":100390,"name":"彭修莲","unit":"建筑","role":"B专业负责人","uid":1441},
			{"id":100971,"name":"韦雯","unit":"总工办","role":"评审管理员","uid":1676}
		]
	},
	1602:{
		"userName":"测试项目二",
		"projID":264666,
		"prjNum":"2A17A020402",
		"prjName":"测试项目二",
		"prjData":[
			{"id":000000,"name":"虚拟用户","unit":"系统管理员","role":"测试角色","uid":3085},
			{"id":100009,"name":"姚素平","unit":"总工办","role":"主管（副）总工程师","uid":2307},
			{"id":100320,"name":"李大浪","unit":"项目经理","role":"项目经理","uid":1403},
			{"id":100320,"name":"李大浪","unit":"结构","role":"A专业负责人","uid":1403},
			{"id":101224,"name":"徐华冰","unit":"结构","role":"A专业设计人","uid":1724},
			{"id":100353,"name":"熊天屹","unit":"结构","role":"A专业校核人","uid":1412},
			{"id":100361,"name":"周丽霞","unit":"结构","role":"A专业审核人","uid":1418},
			{"id":100360,"name":"左菊林","unit":"结构","role":"A专业主任工程师","uid":2339},
			{"id":100390,"name":"彭修莲","unit":"建筑","role":"B专业负责人","uid":1441},
			{"id":100971,"name":"韦雯","unit":"总工办","role":"评审管理员","uid":1676}
		]
	},
	1603:{
		"userName":"测试项目三",
		"projID":264667,
		"prjNum":"2A17A020403",
		"prjName":"测试项目三",
		"prjData":[
			{"id":000000,"name":"虚拟用户","unit":"系统管理员","role":"测试角色","uid":3085},
			{"id":100854,"name":"顾建文","unit":"总工办","role":"主管（副）总工程师","uid":1286},
			{"id":100320,"name":"李大浪","unit":"项目经理","role":"项目经理","uid":1403},
			{"id":100320,"name":"李大浪","unit":"结构","role":"A专业负责人","uid":1403},
			{"id":101224,"name":"徐华冰","unit":"结构","role":"A专业设计人","uid":1724},
			{"id":100353,"name":"熊天屹","unit":"结构","role":"A专业校核人","uid":1412},
			{"id":100361,"name":"周丽霞","unit":"结构","role":"A专业审核人","uid":1418},
			{"id":100360,"name":"左菊林","unit":"结构","role":"A专业主任工程师","uid":2339},
			{"id":100390,"name":"彭修莲","unit":"建筑","role":"B专业负责人","uid":1441},
			{"id":100971,"name":"韦雯","unit":"总工办","role":"评审管理员","uid":1676}
		]
	},
	1604:{
		"userName":"测试项目四",
		"projID":264668,
		"prjNum":"2A17A020404",
		"prjName":"测试项目四",
		"prjData":[
			{"id":000000,"name":"虚拟用户","unit":"系统管理员","role":"测试角色","uid":3085},
			{"id":100009,"name":"姚素平","unit":"总工办","role":"主管（副）总工程师","uid":2307},
			{"id":100320,"name":"李大浪","unit":"项目经理","role":"项目经理","uid":1403},
			{"id":100320,"name":"李大浪","unit":"结构","role":"A专业负责人","uid":1403},
			{"id":101224,"name":"徐华冰","unit":"结构","role":"A专业设计人","uid":1724},
			{"id":100353,"name":"熊天屹","unit":"结构","role":"A专业校核人","uid":1412},
			{"id":100361,"name":"周丽霞","unit":"结构","role":"A专业审核人","uid":1418},
			{"id":100360,"name":"左菊林","unit":"结构","role":"A专业主任工程师","uid":2339},
			{"id":100390,"name":"彭修莲","unit":"建筑","role":"B专业负责人","uid":1441},
			{"id":100971,"name":"韦雯","unit":"总工办","role":"评审管理员","uid":1676}
		]
	},
	1605:{
		"userName":"测试项目五",
		"projID":264669,
		"prjNum":"2A17A020405",
		"prjName":"测试项目五",
		"prjData":[
			{"id":000000,"name":"虚拟用户","unit":"系统管理员","role":"测试角色","uid":3085},
			{"id":100009,"name":"姚素平","unit":"总工办","role":"主管（副）总工程师","uid":2307},
			{"id":100320,"name":"李大浪","unit":"项目经理","role":"项目经理","uid":1403},
			{"id":100320,"name":"李大浪","unit":"结构","role":"A专业负责人","uid":1403},
			{"id":101224,"name":"徐华冰","unit":"结构","role":"A专业设计人","uid":1724},
			{"id":100353,"name":"熊天屹","unit":"结构","role":"A专业校核人","uid":1412},
			{"id":100361,"name":"周丽霞","unit":"结构","role":"A专业审核人","uid":1418},
			{"id":100360,"name":"左菊林","unit":"结构","role":"A专业主任工程师","uid":2339},
			{"id":100390,"name":"彭修莲","unit":"建筑","role":"B专业负责人","uid":1441},
			{"id":100971,"name":"韦雯","unit":"总工办","role":"评审管理员","uid":1676}
		]
	},
	1606:{
		"userName":"东南铜业项目数据",
		"projID":151716,
		"prjNum":"2A15G090402",
		"prjName":"东南铜业铜冶炼基地",
		"prjData":[
			{"id":000000,"name":"虚拟用户","unit":"系统管理员","role":"测试角色","uid":3085},
			{"id":100009,"name":"姚素平","unit":"项目经理","role":"项目经理","uid":2307},
			{"id":100152,"name":"普宗祥","unit":"项目经理","role":"项目常务副经理","uid":2076},
			{"id":100944,"name":"何峰","unit":"项目经理","role":"项目副经理","uid":1680},
			{"id":100322,"name":"罗晓斌","unit":"项目经理","role":"项目副经理","uid":1402},
			{"id":101032,"name":"杨慧兰","unit":"冶金","role":"A专业负责人","uid":1688},
			{"id":101032,"name":"杨慧兰","unit":"冶金","role":"A专业设计人","uid":1688},
			{"id":100239,"name":"李明","unit":"冶金","role":"A专业校核人","uid":1341},
			{"id":100232,"name":"袁剑平","unit":"冶金","role":"A专业审核人","uid":1336,"pid":369},
			{"id":100235,"name":"赵欣","unit":"冶金","role":"A专业主任工程师","uid":1337},
			{"id":100382,"name":"许志民","unit":"建筑","role":"B专业负责人","uid":1437},
			{"id":100390,"name":"彭修莲","unit":"建筑","role":"B专业设计人","uid":1441},
			{"id":100971,"name":"韦雯","unit":"总工办","role":"评审管理员","uid":1676}
		]
	},
	1607:{
		"userName":"东南铜业测试项目01",
		"projID":284666,
		"prjNum":"2A15G090403",
		"prjName":"东南铜业测试项目01",
		"prjData":[
			{"id":000000,"name":"虚拟用户","unit":"系统管理员","role":"测试角色","uid":3085},
			{"id":100009,"name":"姚素平","unit":"总工办","role":"主管（副）总工程师","uid":2307},
			{"id":100232,"name":"袁剑平","unit":"项目经理","role":"项目经理","uid":1336,"pid":369},
			{"id":100243,"name":"毛志琨","unit":"冶金","role":"A专业负责人","uid":1343},
			{"id":101108,"name":"陈森","unit":"冶金","role":"A专业设计人","uid":1693},
			{"id":100240,"name":"尹湘华","unit":"冶金","role":"A专业校核人","uid":1342},
			{"id":100239,"name":"李明","unit":"冶金","role":"A专业审核人","uid":1341},
			{"id":100235,"name":"赵欣","unit":"冶金","role":"A专业主任工程师","uid":1337},
			{"id":100615,"name":"刘进","unit":"建筑","role":"B专业负责人","uid":2343},
			{"id":100390,"name":"彭修莲","unit":"建筑","role":"B专业设计人","uid":1441},
			{"id":100971,"name":"韦雯","unit":"总工办","role":"评审管理员","uid":1676}
		]
	},
	1701:{
		"userName":"联测项目01",
		"projID":297665,
		"prjNum":"2A17A070401",
		"prjName":"联测项目01",
		"prjData":[
			{"id":000000,"name":"虚拟用户","unit":"系统管理员","role":"测试角色","uid":3085},
			{"id":100009,"name":"姚素平","unit":"总工办","role":"主管（副）总工程师","uid":2307},
			{"id":100232,"name":"袁剑平","unit":"项目经理","role":"项目经理","uid":1336,"pid":369},
			{"id":100243,"name":"毛志琨","unit":"冶金","role":"A专业负责人","uid":1343},
			{"id":101108,"name":"陈森","unit":"冶金","role":"A专业设计人","uid":1693},
			{"id":100240,"name":"尹湘华","unit":"冶金","role":"A专业校核人","uid":1342},
			{"id":100239,"name":"李明","unit":"冶金","role":"A专业审核人","uid":1341},
			{"id":100235,"name":"赵欣","unit":"冶金","role":"A专业主任工程师","uid":1337},
			{"id":100615,"name":"刘进","unit":"建筑","role":"B专业负责人","uid":2343},
			{"id":100390,"name":"彭修莲","unit":"建筑","role":"B专业设计人","uid":1441},
			{"id":100971,"name":"韦雯","unit":"总工办","role":"评审管理员","uid":1676}
		]
	}
};
var nav = [{
	"id" : 1,
	"text" : "15年11月~16年1月NIMS项目上线培训",
	"nid" : 0,
	"children" : [{
		/* "iconCls" : 'icon-nims',
		"text" : "下载 NIMS培训助手 & 新版CAD插件",
		"nid" : 0
	},{ */
		"text" : "浏览 任务清单",
		"nid" : 0
	},{
		"text" : "播放 教学视频",
		"nid" : 0,
		"children" : [{
			"text" : "01 编制合同收款计划",
			"nid" : 1,
			"file" : "video/01编制合同收款计划.flv",
			"width" : 800,
			"height" : 600
		},{
			"text" : "02 搭建PBS",
			"nid" : 1,
			"file" : "video/02搭建PBS.flv",
			"width" : 800,
			"height" : 600
		},{
			"text" : "03 发布WBS",
			"nid" : 1,
			"file" : "video/03发布WBS.flv",
			"width" : 800,
			"height" : 600
		},{
			"text" : "04 编制互提条件计划",
			"nid" : 1,
			"file" : "video/04编制互提条件计划.flv",
			"width" : 800,
			"height" : 600
		},{
			"text" : "05 设计输入评审",
			"nid" : 1,
			"file" : "video/05设计输入评审.flv",
			"width" : 800,
			"height" : 600
		},{
			"text" : "06 公司级方案评审",
			"nid" : 1,
			"file" : "video/06公司级方案评审.flv",
			"width" : 800,
			"height" : 600
		},{
			"text" : "07 项目开工报告",
			"nid" : 1,
			"file" : "video/07项目开工报告.flv",
			"width" : 800,
			"height" : 600
		},{
			"text" : "08 协作文本任务分配",
			"nid" : 1,
			"file" : "video/08协作文本任务分配.flv",
			"width" : 800,
			"height" : 600
		},{
			"text" : "09 专业开工报告",
			"nid" : 1,
			"file" : "video/09专业开工报告.flv",
			"width" : 800,
			"height" : 600
		},{
			"text" : "10 专业方案评审",
			"nid" : 1,
			"file" : "video/10专业方案评审.flv",
			"width" : 800,
			"height" : 600
		},{
			"text" : "11 策划PBS工作包",
			"nid" : 1,
			"file" : "video/11策划PBS工作包.flv",
			"width" : 800,
			"height" : 600
		},{
			"text" : "12 项目资料管理",
			"nid" : 1,
			"file" : "video/12项目资料管理.flv",
			"width" : 800,
			"height" : 600
		},{
			"text" : "13 计算书管理",
			"nid" : 1,
			"file" : "video/13计算书管理.flv",
			"width" : 800,
			"height" : 600
		},{
			"text" : "14 专业间互提条件",
			"nid" : 4,
			"file" : "video/14专业间互提条件.flv",
			"width" : 800,
			"height" : 600
		},{
			"text" : "15 协作文本编制与校审",
			"nid" : 1,
			"file" : "video/15协作文本编制与校审.flv",
			"width" : 800,
			"height" : 600
		},{
			"text" : "16 E-Mobile移动端校审",
			"nid" : 1,
			"file" : "video/16E-Mobile移动端校审.flv",
			"width" : 800,
			"height" : 600
		},{
			"text" : "17 协作文本公司级评审",
			"nid" : 0,
			"children" : [{
				"text" : "17a 公司级评审申请",
				"nid" : 2,
				"file" : "video/17a公司级评审申请.flv",
				"width" : 1024,
				"height" : 768
			},{
				"text" : "17b 公司级评审",
				"nid" : 2,
				"file" : "video/17b公司级评审.flv",
				"width" : 800,
				"height" : 600
			},{
				"text" : "17c 公司级评审结论",
				"nid" : 2,
				"file" : "video/17c公司级评审结论.flv",
				"width" : 800,
				"height" : 600
			}]
		},{
			"text" : "18 文本文印",
			"nid" : 1,
			"file" : "video/18文本文印.flv",
			"width" : 800,
			"height" : 600
		},{
			"text" : "19 项目月报",
			"nid" : 1,
			"file" : "video/19项目月报.flv",
			"width" : 1024,
			"height" : 768
		},{
			"text" : "20 图纸设计与提交",
			"nid" : 1,
			"file" : "video/20图纸设计与提交.flv",
			"width" : 1024,
			"height" : 768
		},{
			"text" : "21 图纸校审",
			"nid" : 1,
			"file" : "video/21图纸校审.flv",
			"width" : 1024,
			"height" : 768
		},{
			"text" : "22 瑞林云移动端校审",
			"nid" : 1,
			"file" : "video/22瑞林云移动端校审.flv",
			"width" : 800,
			"height" : 600
		},{
			"text" : "23 图纸会签",
			"nid" : 1,
			"file" : "video/23图纸会签.flv",
			"width" : 1024,
			"height" : 768
		},{
			"text" : "24 图纸新晒",
			"nid" : 1,
			"file" : "video/24图纸新晒.flv",
			"width" : 1024,
			"height" : 768
		},{
			"text" : "testmp401",
			"nid" : 1,
			"file" : "video/01.flv",
			"width" : 960,
			"height" : 540
		},{
			"text" : "testmp402",
			"nid" : 1,
			"file" : "video/02.flv",
			"width" : 960,
			"height" : 540
		},{
			"text" : "testmp403",
			"nid" : 1,
			"file" : "video/03.flv",
			"width" : 960,
			"height" : 540
		}]
	}]
}];