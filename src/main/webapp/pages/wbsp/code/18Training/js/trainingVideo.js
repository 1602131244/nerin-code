$(function () {
//	$.getJSON("users.json", function (dgData) {
		var config = new nerinJsConfig();
		var selectedUser = "";
	/* 	var userlist = [];
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
		}); */
		$('#numLogin').searchbox({
			searcher: function (value) {
				if(!dgData[value]){
					$.messager.show({ title: '提示', msg: '非本次培训员工号或输入有误，请修正后重试。',showType:'fade',style:{left:0,top:0}});
				}else{
					_refreshNavi(value);
				}
			}
		});
		function _refreshNavi(value){
			$('#dgtoolbar').html("&nbsp;"+dgData[value].prjNum+"<br>&nbsp;"+dgData[value].prjName+" <input id='phasecombo' style='width:150px'>");
			$('#dg').datagrid({
				data:dgData[value].prjData,
				idField: 'id',
				fitColumns: true,
				toolbar:'#dgtoolbar',
				rownumbers: true,
				singleSelect: true,
				fit: true,
				columns: [[{ field: 'id', title: '员工号', width: 75 },
					{ field: 'name', title: '姓名', width: 70 },
					{ field: 'unit', title: '部门/专业', width: 90},
					{ field: 'role', title: '角色', width:160}]],
				onDblClickRow: function(index, row){
					var user = "" + value + "-" + index;
					if (config.baseurl == "http://"+window.location.host && selectedUser != user) {
						selectedUser = user;
						nimsTestLogin(row);
						oaTestLogin(row.id);
						ecmTestLogin({j_username:row.id, j_password:11111111});
					}
				}
			}).datagrid('clearSelections');
			var ecmTestLogin = function(PARAMS) {
				var ecmLoginForm = $("#ecmLoginForm");
				$("#j_username").val(PARAMS.j_username);
				$("#j_password").val(PARAMS.j_password);
				ecmLoginForm .submit();
			};
			var nimsTestLogin = function(row){
				$.get('/api/authenticate')
					.done(function(data){
						if (data.currentuser != row.id) {
							$.ajax({
								type: 'POST',
								url: '/api/authentication',
								contentType: 'application/x-www-form-urlencoded',
								data: {j_username:row.id,j_password:row.id},
								// data: {'_csrf':$.cookie('CSRF-TOKEN'),j_username:row.id,j_password:row.id},
								complete: function(){
									$.messager.show({
										title:'NIMS2.0系统培训环境',
										msg:"NIMS成功切换至" + row.name,
										timeout:5000,
										showType:'slide'
									});
								}
							})
						}
					});
			};
			var oaTestLogin = function(userId){
				var oaUrl = "http://192.168.15.198/login/VerifyLogin.jsp?loginfile=%2Fwui%2Ftheme%2Fecology7%2Fpage%2Flogin.jsp%3FtemplateId%3D3%26logintype%3D1%26gopage%3D";
				oaUrl += "&logintype=1&fontName=(unable+to+decode+value)&message=&gopage=&formmethod=get&rnd=&serial=&username=&isie=false&loginid=";
				oaUrl += userId;
				oaUrl += "&userpassword=1&tokenAuthKey=&submit=";
				window.open(oaUrl, "asdf");
			};
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
					{"id":9,"name":"项目门户（培训环境） <a id=macro_8 style='display:none'>进入</a>","password":"同所选角色员工号","href":"http://"+window.location.host+"/index.html?doWork=xmdh"},
					{"id":10,"name":"设计输入管理平台2.0 <a id=macro_9 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/DSINPUT/DESIGNINPUT.html"},
					{"id":11,"name":"更多流程 <a id=macro_10 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/otOAList/otOAList.html"},
					{"id":12,"name":"设计变更管理 <a id=macro_11 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/DSChange/DSChange.html"},
					{"id":29,"name":"设计文印管理 <a id=macro_28 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/Publish/Publish.html"},
					{"id":1,"name":"二级策划 <a id=macro_0 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/WBSLV2/WBSLV2.html"},
					{"id":2,"name":"三级策划 <a id=macro_1 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/WBSLV3/WBSLV3.html"},
					{"id":30,"name":"建筑院图纸校审 <a id=macro_29 style='display:none'>进入</a>","password":"","href":"/pages/wbsp/code/DrawReview/DrawReview.html"},
					{"id":6,"name":"OA（新）15.198 <a id=macro_5 style='display:none'>进入</a>","password":1,"href":"http://192.168.15.198/login/LoginAdmin.jsp"},
					{"id":4,"name":"EBS:8010 <a id=macro_3 style='display:none'>进入</a>","password":111111,"href":"http://ebsapptest.nerin.com:8010"},
					{"id":8,"name":"ECM:weblogic@15.252 <a id=macro_7 style='display:none'>进入</a>","password":"weblogic123","href":"http://192.168.15.252:16200/cs/login/login.htm"},
					{"id":32,"name":"下载2010版64位CAD插件 <a id=macro_31 style='display:none'>下载</a>","password":"","href":"http://"+window.location.host+"/pages/wbsp/code/18Training/eZNerin170906/eZNerin 2010 x64.exe"},
					{"id":33,"name":"下载2010版32位CAD插件 <a id=macro_32 style='display:none'>下载</a>","password":"","href":"http://"+window.location.host+"/pages/wbsp/code/18Training/eZNerin170906/eZNerin 2010 x86.exe"},
					{"id":34,"name":"下载2014版64位CAD插件 <a id=macro_33 style='display:none'>下载</a>","password":"","href":"http://"+window.location.host+"/pages/wbsp/code/18Training/eZNerin170906/eZNerin 2014 x64.exe"},
					{"id":35,"name":"下载2014版32位CAD插件 <a id=macro_34 style='display:none'>下载</a>","password":"","href":"http://"+window.location.host+"/pages/wbsp/code/18Training/eZNerin170906/eZNerin 2014 x86.exe"},
					{"id":36,"name":"下载2012版64位CAD插件 <a id=macro_35 style='display:none'>下载</a>","password":"","href":"http://"+window.location.host+"/pages/wbsp/code/18Training/eZNerin170906/eZNerin 2012 x64.exe"},
					{"id":37,"name":"下载2012版32位CAD插件 <a id=macro_36 style='display:none'>下载</a>","password":"","href":"http://"+window.location.host+"/pages/wbsp/code/18Training/eZNerin170906/eZNerin 2012 x86.exe"},
					{"id":38,"name":"下载2016版64位CAD插件 <a id=macro_37 style='display:none'>下载</a>","password":"","href":"http://"+window.location.host+"/pages/wbsp/code/18Training/eZNerin170906/eZNerin 2016 x64.exe"},
					{"id":39,"name":"下载2016版32位CAD插件 <a id=macro_38 style='display:none'>下载</a>","password":"","href":"http://"+window.location.host+"/pages/wbsp/code/18Training/eZNerin170906/eZNerin 2016 x86.exe"},
					{"id":40,"name":"下载2008版32位CAD插件 <a id=macro_39 style='display:none'>下载</a>","password":"","href":"http://"+window.location.host+"/pages/wbsp/code/18Training/eZNerin170906/eZNerin 2008 x86.exe"},
					{"id":41,"name":"下载2009版32位CAD插件 <a id=macro_40 style='display:none'>下载</a>","password":"","href":"http://"+window.location.host+"/pages/wbsp/code/18Training/eZNerin170906/eZNerin 2009 x86.exe"},
					{"id":42,"name":"下载2009版64位CAD插件 <a id=macro_41 style='display:none'>下载</a>","password":"","href":"http://"+window.location.host+"/pages/wbsp/code/18Training/eZNerin170906/eZNerin 2009 x64.exe"},
					{"id":43,"name":"下载2011版64位CAD插件 <a id=macro_42 style='display:none'>下载</a>","password":"","href":"http://"+window.location.host+"/pages/wbsp/code/18Training/eZNerin170906/eZNerin 2011 x64.exe"},
					{"id":44,"name":"下载2011版32位CAD插件 <a id=macro_43 style='display:none'>下载</a>","password":"","href":"http://"+window.location.host+"/pages/wbsp/code/18Training/eZNerin170906/eZNerin 2011 x86.exe"},
					{"id":45,"name":"下载2013版64位CAD插件 <a id=macro_44 style='display:none'>下载</a>","password":"","href":"http://"+window.location.host+"/pages/wbsp/code/18Training/eZNerin170906/eZNerin 2013 x64.exe"},
					{"id":46,"name":"下载2013版32位CAD插件 <a id=macro_45 style='display:none'>下载</a>","password":"","href":"http://"+window.location.host+"/pages/wbsp/code/18Training/eZNerin170906/eZNerin 2013 x86.exe"},
					{"id":47,"name":"下载2015版64位CAD插件 <a id=macro_46 style='display:none'>下载</a>","password":"","href":"http://"+window.location.host+"/pages/wbsp/code/18Training/eZNerin170906/eZNerin 2015 x64.exe"},
					{"id":48,"name":"下载2015版32位CAD插件 <a id=macro_47 style='display:none'>下载</a>","password":"","href":"http://"+window.location.host+"/pages/wbsp/code/18Training/eZNerin170906/eZNerin 2015 x86.exe"}
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
					}).show().html('&nbsp;继续&nbsp;');
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
						console.log("http://"+window.location.host+"/pages/wbsp/code/18Training/video/"+encodeURI(encodeURI(node.file)));
						$('#tabs').tabs('close', 1);
						$('#tabs').tabs('add', {
							title : node.text,
							closable : true,
							content : function(){
								$(this).append("<div id='a1' style='margin: 0 auto;text-align:center;'></div>");
								var flashvars={
									f:"http://"+window.location.host+"/pages/wbsp/code/18Training/"+encodeURI(encodeURI(node.file)),//中文文件名会导致firefox浏览器加载文件失败。
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
			title : '操作指南',
			closable : false,
			content : function(){
				$(this).append("<iframe src='18tr.swf' id='iframepage' frameborder=0 scrolling='no' marginheight=0 marginwidth=0></iframe>");
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
	var dgData = {100563:{"userName":"100563徐国强","projID":318677,"prjNum":"2A18A100412","prjName":"NIMS优化徐国强","prjData":[{"id":100382,"name":"许志民","unit":"建筑","role":"专业负责人"},
{"id":100390,"name":"彭修莲","unit":"建筑","role":"设计人"},
{"id":100381,"name":"刘先长","unit":"建筑","role":"校核人"},
{"id":100382,"name":"许志民","unit":"建筑","role":"审核人"},
{"id":100258,"name":"周亮亮","unit":"化验","role":"专业负责人"},
{"id":100009,"name":"姚素平","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100390,"name":"彭修莲","unit":"项目经理","role":"项目经理"},
{"id":100123,"name":"姜文娟","unit":"","role":"合同管理工程师"},
{"id":100146,"name":"熊焰","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]},
100141:{"userName":"100141龙越","projID":318670,"prjNum":"2A18A100405","prjName":"NIMS优化龙越","prjData":[{"id":100258,"name":"周亮亮","unit":"化验","role":"专业负责人"},
{"id":100258,"name":"周亮亮","unit":"化验","role":"设计人"},
{"id":100253,"name":"熊少华","unit":"化验","role":"校核人"},
{"id":100254,"name":"万小龙","unit":"化验","role":"审核人"},
{"id":100282,"name":"熊丽芳","unit":"化工设备","role":"专业负责人"},
{"id":100224,"name":"叶薇","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100253,"name":"熊少华","unit":"项目经理","role":"项目经理"},
{"id":104156,"name":"万斯琴","unit":"","role":"合同管理工程师"},
{"id":100990,"name":"龚丽芬","unit":"","role":"项目秘书"},
{"id":100971,"name":"韦雯","unit":"","role":"评审管理员"}]},
101310:{"userName":"101310王冠","projID":318684,"prjNum":"2A18A110406","prjName":"NIMS优化王冠","prjData":[{"id":100282,"name":"熊丽芳","unit":"化工设备","role":"专业负责人"},
{"id":100282,"name":"熊丽芳","unit":"化工设备","role":"设计人"},
{"id":100280,"name":"葛帅华","unit":"化工设备","role":"校核人"},
{"id":100260,"name":"黄志远","unit":"化工设备","role":"审核人"},
{"id":100273,"name":"王召启","unit":"化工","role":"专业负责人"},
{"id":100259,"name":"黄卫华","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100280,"name":"葛帅华","unit":"项目经理","role":"项目经理"},
{"id":100951,"name":"黄强","unit":"","role":"合同管理工程师"},
{"id":100138,"name":"傅云","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]},
100096:{"userName":"100096万琮","projID":318667,"prjNum":"2A18A100402","prjName":"NIMS优化万琮","prjData":[{"id":100273,"name":"王召启","unit":"化工","role":"专业负责人"},
{"id":100273,"name":"王召启","unit":"化工","role":"设计人"},
{"id":100264,"name":"涂瑞","unit":"化工","role":"校核人"},
{"id":100541,"name":"郭智生","unit":"化工","role":"审核人"},
{"id":100451,"name":"邹丽芳","unit":"通信","role":"专业负责人"},
{"id":100259,"name":"黄卫华","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100264,"name":"涂瑞","unit":"项目经理","role":"项目经理"},
{"id":100108,"name":"王琪","unit":"","role":"合同管理工程师"},
{"id":100153,"name":"袁蓓蓓","unit":"","role":"项目秘书"},
{"id":100971,"name":"韦雯","unit":"","role":"评审管理员"}]},
100098:{"userName":"100098金兴楠","projID":318668,"prjNum":"2A18A100403","prjName":"NIMS优化金兴","prjData":[{"id":100451,"name":"邹丽芳","unit":"通信","role":"专业负责人"},
{"id":101288,"name":"刘少文","unit":"通信","role":"设计人"},
{"id":100451,"name":"邹丽芳","unit":"通信","role":"校核人"},
{"id":100449,"name":"张卫国","unit":"通信","role":"审核人"},
{"id":100256,"name":"涂建华","unit":"收尘","role":"专业负责人"},
{"id":100068,"name":"宋筱平","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":101288,"name":"刘少文","unit":"项目经理","role":"项目经理"},
{"id":100109,"name":"陈鸿","unit":"","role":"合同管理工程师"},
{"id":100928,"name":"胡杨","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]},
100357:{"userName":"100357杨毅","projID":318675,"prjNum":"2A18A100410","prjName":"NIMS优化杨毅","prjData":[{"id":100256,"name":"涂建华","unit":"收尘","role":"专业负责人"},
{"id":101284,"name":"郭鹏辉","unit":"收尘","role":"设计人"},
{"id":100256,"name":"涂建华","unit":"收尘","role":"校核人"},
{"id":100254,"name":"万小龙","unit":"收尘","role":"审核人"},
{"id":100966,"name":"陈志","unit":"造价","role":"专业负责人"},
{"id":100224,"name":"叶薇","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":101284,"name":"郭鹏辉","unit":"项目经理","role":"项目经理"},
{"id":100116,"name":"舒畅","unit":"","role":"合同管理工程师"},
{"id":100143,"name":"樊俊","unit":"","role":"项目秘书"},
{"id":100971,"name":"韦雯","unit":"","role":"评审管理员"}]},
101022:{"userName":"101022刘闻翎","projID":318681,"prjNum":"2A18A110403","prjName":"NIMS优化刘闻翎","prjData":[{"id":100966,"name":"陈志","unit":"造价","role":"专业负责人"},
{"id":100966,"name":"陈志","unit":"造价","role":"设计人"},
{"id":100939,"name":"王爱萍","unit":"造价","role":"校核人"},
{"id":100589,"name":"蒋文彬","unit":"造价","role":"审核人"},
{"id":100454,"name":"殷铭宏","unit":"电力","role":"专业负责人"},
{"id":100009,"name":"姚素平","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100939,"name":"王爱萍","unit":"项目经理","role":"项目经理"},
{"id":100955,"name":"汪洁","unit":"","role":"合同管理工程师"},
{"id":100692,"name":"淦杰","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]},
101045:{"userName":"101045牛荦","projID":318682,"prjNum":"2A18A110404","prjName":"NIMS优化牛荦","prjData":[{"id":100454,"name":"殷铭宏","unit":"电力","role":"专业负责人"},
{"id":100454,"name":"殷铭宏","unit":"电力","role":"设计人"},
{"id":100469,"name":"胡雅伶","unit":"电力","role":"校核人"},
{"id":100423,"name":"黄薇","unit":"电力","role":"审核人"},
{"id":100574,"name":"葛鑫","unit":"环保","role":"专业负责人"},
{"id":100130,"name":"黄永青","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100469,"name":"胡雅伶","unit":"项目经理","role":"项目经理"},
{"id":103485,"name":"廖一文","unit":"","role":"合同管理工程师"},
{"id":101176,"name":"朱祥英","unit":"","role":"项目秘书"},
{"id":100971,"name":"韦雯","unit":"","role":"评审管理员"}]},
101319:{"userName":"101319李根明","projID":318685,"prjNum":"2A18A110407","prjName":"NIMS优化李根明","prjData":[{"id":100574,"name":"葛鑫","unit":"环保","role":"专业负责人"},
{"id":100574,"name":"葛鑫","unit":"环保","role":"设计人"},
{"id":100573,"name":"赵晋","unit":"环保","role":"校核人"},
{"id":100561,"name":"胡奔流","unit":"环保","role":"审核人"},
{"id":100252,"name":"简正柱","unit":"工业炉","role":"专业负责人"},
{"id":100128,"name":"龙燕","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100573,"name":"赵晋","unit":"项目经理","role":"项目经理"},
{"id":100107,"name":"姜茜","unit":"","role":"合同管理工程师"},
{"id":100146,"name":"熊焰","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]},
101320:{"userName":"101320贾岩","projID":318686,"prjNum":"2A18A110408","prjName":"NIMS优化贾岩","prjData":[{"id":100252,"name":"简正柱","unit":"工业炉","role":"专业负责人"},
{"id":100252,"name":"简正柱","unit":"工业炉","role":"设计人"},
{"id":100249,"name":"黄文华","unit":"工业炉","role":"校核人"},
{"id":100251,"name":"袁精华","unit":"工业炉","role":"审核人"},
{"id":100408,"name":"陈美孙","unit":"总图","role":"专业负责人"},
{"id":100033,"name":"刘庆华","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100249,"name":"黄文华","unit":"项目经理","role":"项目经理"},
{"id":101293,"name":"罗志凌","unit":"","role":"合同管理工程师"},
{"id":100990,"name":"龚丽芬","unit":"","role":"项目秘书"},
{"id":100971,"name":"韦雯","unit":"","role":"评审管理员"}]},
104051:{"userName":"104051李佳英","projID":318687,"prjNum":"2A18A110409","prjName":"NIMS优化李佳英","prjData":[{"id":100408,"name":"陈美孙","unit":"总图","role":"专业负责人"},
{"id":100408,"name":"陈美孙","unit":"总图","role":"设计人"},
{"id":100413,"name":"何友军","unit":"总图","role":"校核人"},
{"id":100404,"name":"梁晓文","unit":"总图","role":"审核人"},
{"id":101041,"name":"唐笑","unit":"水工","role":"专业负责人"},
{"id":100698,"name":"钟为民","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100413,"name":"何友军","unit":"项目经理","role":"项目经理"},
{"id":100123,"name":"姜文娟","unit":"","role":"合同管理工程师"},
{"id":100138,"name":"傅云","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]},
104073:{"userName":"104073刘磊","projID":318688,"prjNum":"2A18A110410","prjName":"NIMS优化刘磊","prjData":[{"id":101041,"name":"唐笑","unit":"水工","role":"专业负责人"},
{"id":101041,"name":"唐笑","unit":"水工","role":"设计人"},
{"id":100551,"name":"罗敏杰","unit":"水工","role":"校核人"},
{"id":100543,"name":"吴国高","unit":"水工","role":"审核人"},
{"id":100941,"name":"杜龙","unit":"安全与职业健康","role":"专业负责人"},
{"id":100540,"name":"袁永强","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100551,"name":"罗敏杰","unit":"项目经理","role":"项目经理"},
{"id":104156,"name":"万斯琴","unit":"","role":"合同管理工程师"},
{"id":100153,"name":"袁蓓蓓","unit":"","role":"项目秘书"},
{"id":100971,"name":"韦雯","unit":"","role":"评审管理员"}]},
104515:{"userName":"104515甘陽杰","projID":318689,"prjNum":"2A18A110411","prjName":"NIMS优化甘陽杰","prjData":[{"id":100941,"name":"杜龙","unit":"安全与职业健康","role":"专业负责人"},
{"id":100941,"name":"杜龙","unit":"安全与职业健康","role":"设计人"},
{"id":100560,"name":"魏娜","unit":"安全与职业健康","role":"校核人"},
{"id":100542,"name":"曹学新","unit":"安全与职业健康","role":"审核人"},
{"id":101081,"name":"刘峰","unit":"暖通","role":"专业负责人"},
{"id":100128,"name":"龙燕","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100560,"name":"魏娜","unit":"项目经理","role":"项目经理"},
{"id":100951,"name":"黄强","unit":"","role":"合同管理工程师"},
{"id":100928,"name":"胡杨","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]},
104528:{"userName":"104528张万川","projID":318690,"prjNum":"2A18A110412","prjName":"NIMS优化张万川","prjData":[{"id":101081,"name":"刘峰","unit":"暖通","role":"专业负责人"},
{"id":101081,"name":"刘峰","unit":"暖通","role":"设计人"},
{"id":100518,"name":"舒春林","unit":"暖通","role":"校核人"},
{"id":100519,"name":"袁正明","unit":"暖通","role":"审核人"},
{"id":100435,"name":"胡国军","unit":"仪控","role":"专业负责人"},
{"id":100009,"name":"姚素平","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100518,"name":"舒春林","unit":"项目经理","role":"项目经理"},
{"id":100108,"name":"王琪","unit":"","role":"合同管理工程师"},
{"id":100143,"name":"樊俊","unit":"","role":"项目秘书"},
{"id":100971,"name":"韦雯","unit":"","role":"评审管理员"}]},
100083:{"userName":"100083朱新涛","projID":318666,"prjNum":"2A18A100401","prjName":"NIMS优化朱新涛","prjData":[{"id":100435,"name":"胡国军","unit":"仪控","role":"专业负责人"},
{"id":100435,"name":"胡国军","unit":"仪控","role":"设计人"},
{"id":100427,"name":"白天","unit":"仪控","role":"校核人"},
{"id":100424,"name":"文辉煌","unit":"仪控","role":"审核人"},
{"id":101032,"name":"杨慧兰","unit":"冶金","role":"专业负责人"},
{"id":100131,"name":"王烜","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100427,"name":"白天","unit":"项目经理","role":"项目经理"},
{"id":100109,"name":"陈鸿","unit":"","role":"合同管理工程师"},
{"id":100692,"name":"淦杰","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]},
100522:{"userName":"100522付文伟","projID":318676,"prjNum":"2A18A100411","prjName":"NIMS优化付文伟","prjData":[{"id":101032,"name":"杨慧兰","unit":"冶金","role":"专业负责人"},
{"id":101032,"name":"杨慧兰","unit":"冶金","role":"设计人"},
{"id":100239,"name":"李明","unit":"冶金","role":"校核人"},
{"id":100232,"name":"袁剑平","unit":"冶金","role":"审核人"},
{"id":101012,"name":"李文越","unit":"冶金设备","role":"专业负责人"},
{"id":100033,"name":"刘庆华","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100239,"name":"李明","unit":"项目经理","role":"项目经理"},
{"id":100116,"name":"舒畅","unit":"","role":"合同管理工程师"},
{"id":101176,"name":"朱祥英","unit":"","role":"项目秘书"},
{"id":100971,"name":"韦雯","unit":"","role":"评审管理员"}]},
100146:{"userName":"100146熊焰","projID":318673,"prjNum":"2A18A100408","prjName":"NIMS优化熊焰","prjData":[{"id":101012,"name":"李文越","unit":"冶金设备","role":"专业负责人"},
{"id":101254,"name":"曾芳成","unit":"冶金设备","role":"设计人"},
{"id":101012,"name":"李文越","unit":"冶金设备","role":"校核人"},
{"id":100287,"name":"魏振","unit":"冶金设备","role":"审核人"},
{"id":101139,"name":"周峰","unit":"矿加","role":"专业负责人"},
{"id":100009,"name":"姚素平","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":101254,"name":"曾芳成","unit":"项目经理","role":"项目经理"},
{"id":100955,"name":"汪洁","unit":"","role":"合同管理工程师"},
{"id":100146,"name":"熊焰","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]},
100990:{"userName":"100990龚丽芬","projID":318680,"prjNum":"2A18A110402","prjName":"NIMS优化龚丽芬","prjData":[{"id":101139,"name":"周峰","unit":"矿加","role":"专业负责人"},
{"id":101139,"name":"周峰","unit":"矿加","role":"设计人"},
{"id":100212,"name":"余浔","unit":"矿加","role":"校核人"},
{"id":100204,"name":"雷存有","unit":"矿加","role":"审核人"},
{"id":101044,"name":"姜涛","unit":"机械工艺","role":"专业负责人"},
{"id":102022,"name":"章晋叔","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100212,"name":"余浔","unit":"项目经理","role":"项目经理"},
{"id":103485,"name":"廖一文","unit":"","role":"合同管理工程师"},
{"id":100990,"name":"龚丽芬","unit":"","role":"项目秘书"},
{"id":100971,"name":"韦雯","unit":"","role":"评审管理员"}]},
100138:{"userName":"100138傅云","projID":318669,"prjNum":"2A18A100404","prjName":"NIMS优化傅云","prjData":[{"id":101044,"name":"姜涛","unit":"机械工艺","role":"专业负责人"},
{"id":101044,"name":"姜涛","unit":"机械工艺","role":"设计人"},
{"id":100925,"name":"宋艳红","unit":"机械工艺","role":"校核人"},
{"id":100313,"name":"王彤彤","unit":"机械工艺","role":"审核人"},
{"id":103580,"name":"方建中","unit":"管道","role":"专业负责人"},
{"id":102027,"name":"赵新生","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100925,"name":"宋艳红","unit":"项目经理","role":"项目经理"},
{"id":100107,"name":"姜茜","unit":"","role":"合同管理工程师"},
{"id":100138,"name":"傅云","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]},
100153:{"userName":"100153袁蓓蓓","projID":318674,"prjNum":"2A18A100409","prjName":"NIMS优化袁蓓蓓","prjData":[{"id":103580,"name":"方建中","unit":"管道","role":"专业负责人"},
{"id":100532,"name":"付红春","unit":"管道","role":"设计人"},
{"id":103580,"name":"方建中","unit":"管道","role":"校核人"},
{"id":100261,"name":"施群","unit":"管道","role":"审核人"},
{"id":100378,"name":"吕辉勇","unit":"结构","role":"专业负责人"},
{"id":100009,"name":"姚素平","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100532,"name":"付红春","unit":"项目经理","role":"项目经理"},
{"id":101293,"name":"罗志凌","unit":"","role":"合同管理工程师"},
{"id":100153,"name":"袁蓓蓓","unit":"","role":"项目秘书"},
{"id":100971,"name":"韦雯","unit":"","role":"评审管理员"}]},
100928:{"userName":"100928胡杨","projID":318679,"prjNum":"2A18A110401","prjName":"NIMS优化胡杨","prjData":[{"id":100378,"name":"吕辉勇","unit":"结构","role":"专业负责人"},
{"id":100378,"name":"吕辉勇","unit":"结构","role":"设计人"},
{"id":100362,"name":"李小毛","unit":"结构","role":"校核人"},
{"id":100360,"name":"左菊林","unit":"结构","role":"审核人"},
{"id":100538,"name":"汤金华","unit":"热工","role":"专业负责人"},
{"id":100320,"name":"李大浪","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100362,"name":"李小毛","unit":"项目经理","role":"项目经理"},
{"id":100123,"name":"姜文娟","unit":"","role":"合同管理工程师"},
{"id":100928,"name":"胡杨","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]},
100143:{"userName":"100143樊俊","projID":318671,"prjNum":"2A18A100406","prjName":"NIMS优化樊俊","prjData":[{"id":100538,"name":"汤金华","unit":"热工","role":"专业负责人"},
{"id":100538,"name":"汤金华","unit":"热工","role":"设计人"},
{"id":100529,"name":"廖祚洗","unit":"热工","role":"校核人"},
{"id":100517,"name":"宋冬根","unit":"热工","role":"审核人"},
{"id":100586,"name":"胡青","unit":"技经","role":"专业负责人"},
{"id":100009,"name":"姚素平","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100529,"name":"廖祚洗","unit":"项目经理","role":"项目经理"},
{"id":104156,"name":"万斯琴","unit":"","role":"合同管理工程师"},
{"id":100143,"name":"樊俊","unit":"","role":"项目秘书"},
{"id":100971,"name":"韦雯","unit":"","role":"评审管理员"}]},
100692:{"userName":"100692淦杰","projID":318678,"prjNum":"2A18A100413","prjName":"NIMS优化淦杰","prjData":[{"id":100586,"name":"胡青","unit":"技经","role":"专业负责人"},
{"id":100586,"name":"胡青","unit":"技经","role":"设计人"},
{"id":100958,"name":"王苏丹","unit":"技经","role":"校核人"},
{"id":100585,"name":"黎文","unit":"技经","role":"审核人"},
{"id":100487,"name":"夏安林","unit":"给排水","role":"专业负责人"},
{"id":100009,"name":"姚素平","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100585,"name":"黎文","unit":"项目经理","role":"项目经理"},
{"id":100951,"name":"黄强","unit":"","role":"合同管理工程师"},
{"id":100692,"name":"淦杰","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]},
101176:{"userName":"101176朱祥英","projID":318683,"prjNum":"2A18A110405","prjName":"NIMS优化朱祥英","prjData":[{"id":100487,"name":"夏安林","unit":"给排水","role":"专业负责人"},
{"id":100487,"name":"夏安林","unit":"给排水","role":"设计人"},
{"id":100492,"name":"吴国平","unit":"给排水","role":"校核人"},
{"id":100479,"name":"熊鸣","unit":"给排水","role":"审核人"},
{"id":101103,"name":"祝强文","unit":"总图","role":"专业负责人"},
{"id":100129,"name":"何小英","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100479,"name":"熊鸣","unit":"项目经理","role":"项目经理"},
{"id":100108,"name":"王琪","unit":"","role":"合同管理工程师"},
{"id":101176,"name":"朱祥英","unit":"","role":"项目秘书"},
{"id":100971,"name":"韦雯","unit":"","role":"评审管理员"}]},
100144:{"userName":"100144胡捷慧","projID":318672,"prjNum":"2A18A100407","prjName":"NIMS优化胡捷慧","prjData":[{"id":101103,"name":"祝强文","unit":"总图","role":"专业负责人"},
{"id":101103,"name":"祝强文","unit":"总图","role":"设计人"},
{"id":100408,"name":"陈美孙","unit":"总图","role":"校核人"},
{"id":100401,"name":"曾小平","unit":"总图","role":"审核人"},
{"id":101342,"name":"周天驰","unit":"仪控","role":"专业负责人"},
{"id":100698,"name":"钟为民","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100401,"name":"曾小平","unit":"项目经理","role":"项目经理"},
{"id":100109,"name":"陈鸿","unit":"","role":"合同管理工程师"},
{"id":100146,"name":"熊焰","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]},
201800:{"userName":"2018001测试","projID":318691,"prjNum":"2A18A120401","prjName":"NIMS优化2018001","prjData":[{"id":101342,"name":"周天驰","unit":"仪控","role":"专业负责人"},
{"id":101342,"name":"周天驰","unit":"仪控","role":"设计人"},
{"id":100435,"name":"胡国军","unit":"仪控","role":"校核人"},
{"id":100427,"name":"白天","unit":"仪控","role":"审核人"},
{"id":100248,"name":"陈迟","unit":"冶金","role":"专业负责人"},
{"id":100131,"name":"王烜","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100435,"name":"胡国军","unit":"项目经理","role":"项目经理"},
{"id":100116,"name":"舒畅","unit":"","role":"合同管理工程师"},
{"id":100990,"name":"龚丽芬","unit":"","role":"项目秘书"},
{"id":100971,"name":"韦雯","unit":"","role":"评审管理员"}]},
201800:{"userName":"2018002测试","projID":318692,"prjNum":"2A18A120402","prjName":"NIMS优化2018002","prjData":[{"id":100248,"name":"陈迟","unit":"冶金","role":"专业负责人"},
{"id":100248,"name":"陈迟","unit":"冶金","role":"设计人"},
{"id":100230,"name":"刘建军","unit":"冶金","role":"校核人"},
{"id":100235,"name":"赵欣","unit":"冶金","role":"审核人"},
{"id":101254,"name":"曾芳成","unit":"冶金设备","role":"专业负责人"},
{"id":100224,"name":"叶薇","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100230,"name":"刘建军","unit":"项目经理","role":"项目经理"},
{"id":100955,"name":"汪洁","unit":"","role":"合同管理工程师"},
{"id":100138,"name":"傅云","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]},
201800:{"userName":"2018003测试","projID":318693,"prjNum":"2A18A120403","prjName":"NIMS优化2018003","prjData":[{"id":100247,"name":"杨文浩","unit":"冶金","role":"专业负责人"},
{"id":100247,"name":"杨文浩","unit":"冶金","role":"设计人"},
{"id":100228,"name":"廖文江","unit":"冶金","role":"校核人"},
{"id":100230,"name":"刘建军","unit":"冶金","role":"审核人"},
{"id":100532,"name":"付红春","unit":"管道","role":"专业负责人"},
{"id":100224,"name":"叶薇","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100228,"name":"廖文江","unit":"项目经理","role":"项目经理"},
{"id":103485,"name":"廖一文","unit":"","role":"合同管理工程师"},
{"id":100153,"name":"袁蓓蓓","unit":"","role":"项目秘书"},
{"id":100971,"name":"韦雯","unit":"","role":"评审管理员"}]},
201800:{"userName":"2018004测试","projID":318694,"prjNum":"2A18A120404","prjName":"NIMS优化2018004","prjData":[{"id":100944,"name":"何峰","unit":"冶金","role":"专业负责人"},
{"id":100944,"name":"何峰","unit":"冶金","role":"设计人"},
{"id":101193,"name":"王永强","unit":"冶金","role":"校核人"},
{"id":100224,"name":"叶薇","unit":"冶金","role":"审核人"},
{"id":100551,"name":"罗敏杰","unit":"水工","role":"专业负责人"},
{"id":100033,"name":"刘庆华","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":101193,"name":"王永强","unit":"项目经理","role":"项目经理"},
{"id":100107,"name":"姜茜","unit":"","role":"合同管理工程师"},
{"id":100928,"name":"胡杨","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]},
201800:{"userName":"2018005测试","projID":318695,"prjNum":"2A18A120405","prjName":"NIMS优化2018005","prjData":[{"id":103752,"name":"吕阳","unit":"冶金","role":"专业负责人"},
{"id":103752,"name":"吕阳","unit":"冶金","role":"设计人"},
{"id":100944,"name":"何峰","unit":"冶金","role":"校核人"},
{"id":100240,"name":"尹湘华","unit":"冶金","role":"审核人"},
{"id":100939,"name":"王爱萍","unit":"造价","role":"专业负责人"},
{"id":100007,"name":"徐赤农","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100240,"name":"尹湘华","unit":"项目经理","role":"项目经理"},
{"id":101293,"name":"罗志凌","unit":"","role":"合同管理工程师"},
{"id":100143,"name":"樊俊","unit":"","role":"项目秘书"},
{"id":100971,"name":"韦雯","unit":"","role":"评审管理员"}]},
201800:{"userName":"2018006测试","projID":318696,"prjNum":"2A18A120406","prjName":"NIMS优化2018006","prjData":[{"id":100327,"name":"吴乐亮","unit":"结构","role":"专业负责人"},
{"id":100327,"name":"吴乐亮","unit":"结构","role":"设计人"},
{"id":100322,"name":"罗晓斌","unit":"结构","role":"校核人"},
{"id":100362,"name":"李小毛","unit":"结构","role":"审核人"},
{"id":100573,"name":"赵晋","unit":"环保","role":"专业负责人"},
{"id":100320,"name":"李大浪","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100322,"name":"罗晓斌","unit":"项目经理","role":"项目经理"},
{"id":100123,"name":"姜文娟","unit":"","role":"合同管理工程师"},
{"id":100692,"name":"淦杰","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]},
201800:{"userName":"2018007测试","projID":318697,"prjNum":"2A18A120407","prjName":"NIMS优化2018007","prjData":[{"id":100377,"name":"刘皓","unit":"结构","role":"专业负责人"},
{"id":100377,"name":"刘皓","unit":"结构","role":"设计人"},
{"id":100323,"name":"李霞","unit":"结构","role":"校核人"},
{"id":100346,"name":"涂相明","unit":"结构","role":"审核人"},
{"id":100382,"name":"许志民","unit":"建筑","role":"专业负责人"},
{"id":100320,"name":"李大浪","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100323,"name":"李霞","unit":"项目经理","role":"项目经理"},
{"id":104156,"name":"万斯琴","unit":"","role":"合同管理工程师"},
{"id":101176,"name":"朱祥英","unit":"","role":"项目秘书"},
{"id":100971,"name":"韦雯","unit":"","role":"评审管理员"}]},
201800:{"userName":"2018008测试","projID":318698,"prjNum":"2A18A120408","prjName":"NIMS优化2018008","prjData":[{"id":100355,"name":"徐远志","unit":"结构","role":"专业负责人"},
{"id":100355,"name":"徐远志","unit":"结构","role":"设计人"},
{"id":100366,"name":"袁蔚文","unit":"结构","role":"校核人"},
{"id":100341,"name":"杨忠","unit":"结构","role":"审核人"},
{"id":100253,"name":"熊少华","unit":"化验","role":"专业负责人"},
{"id":100320,"name":"李大浪","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100366,"name":"袁蔚文","unit":"项目经理","role":"项目经理"},
{"id":100951,"name":"黄强","unit":"","role":"合同管理工程师"},
{"id":100146,"name":"熊焰","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]},
201800:{"userName":"2018009测试","projID":318699,"prjNum":"2A18A120409","prjName":"NIMS优化2018009","prjData":[{"id":100338,"name":"徐建","unit":"结构","role":"专业负责人"},
{"id":100338,"name":"徐建","unit":"结构","role":"设计人"},
{"id":100346,"name":"涂相明","unit":"结构","role":"校核人"},
{"id":100366,"name":"袁蔚文","unit":"结构","role":"审核人"},
{"id":100264,"name":"涂瑞","unit":"化工","role":"专业负责人"},
{"id":100320,"name":"李大浪","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100346,"name":"涂相明","unit":"项目经理","role":"项目经理"},
{"id":100108,"name":"王琪","unit":"","role":"合同管理工程师"},
{"id":100990,"name":"龚丽芬","unit":"","role":"项目秘书"},
{"id":100971,"name":"韦雯","unit":"","role":"评审管理员"}]},
201801:{"userName":"2018010测试","projID":318700,"prjNum":"2A18A120410","prjName":"NIMS优化2018010","prjData":[{"id":100336,"name":"陈绍隆","unit":"结构","role":"专业负责人"},
{"id":100336,"name":"陈绍隆","unit":"结构","role":"设计人"},
{"id":100341,"name":"杨忠","unit":"结构","role":"校核人"},
{"id":100322,"name":"罗晓斌","unit":"结构","role":"审核人"},
{"id":100925,"name":"宋艳红","unit":"机械工艺","role":"专业负责人"},
{"id":100320,"name":"李大浪","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100341,"name":"杨忠","unit":"项目经理","role":"项目经理"},
{"id":100109,"name":"陈鸿","unit":"","role":"合同管理工程师"},
{"id":100138,"name":"傅云","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]},
100051:{"userName":"100051陈志永","projID":320665,"prjNum":"2A18A130401","prjName":"NIMS优化陈志永","prjData":[{"id":101342,"name":"周天驰","unit":"仪控","role":"专业负责人"},
{"id":101342,"name":"周天驰","unit":"仪控","role":"设计人"},
{"id":100435,"name":"胡国军","unit":"仪控","role":"校核人"},
{"id":100427,"name":"白天","unit":"仪控","role":"审核人"},
{"id":100248,"name":"陈迟","unit":"冶金","role":"专业负责人"},
{"id":100131,"name":"王烜","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100435,"name":"胡国军","unit":"项目经理","role":"项目经理"},
{"id":100116,"name":"舒畅","unit":"","role":"合同管理工程师"},
{"id":100990,"name":"龚丽芬","unit":"","role":"项目秘书"},
{"id":100971,"name":"韦雯","unit":"","role":"评审管理员"}]},
100155:{"userName":"100155过晓斌","projID":320666,"prjNum":"2A18A130402","prjName":"NIMS优化过晓斌","prjData":[{"id":100248,"name":"陈迟","unit":"冶金","role":"专业负责人"},
{"id":100248,"name":"陈迟","unit":"冶金","role":"设计人"},
{"id":100230,"name":"刘建军","unit":"冶金","role":"校核人"},
{"id":100235,"name":"赵欣","unit":"冶金","role":"审核人"},
{"id":101254,"name":"曾芳成","unit":"冶金设备","role":"专业负责人"},
{"id":100224,"name":"叶薇","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100230,"name":"刘建军","unit":"项目经理","role":"项目经理"},
{"id":100955,"name":"汪洁","unit":"","role":"合同管理工程师"},
{"id":100138,"name":"傅云","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]},
101233:{"userName":"101233蔡铁","projID":320667,"prjNum":"2A18A130403","prjName":"NIMS优化蔡铁","prjData":[{"id":100247,"name":"杨文浩","unit":"冶金","role":"专业负责人"},
{"id":100247,"name":"杨文浩","unit":"冶金","role":"设计人"},
{"id":100228,"name":"廖文江","unit":"冶金","role":"校核人"},
{"id":100230,"name":"刘建军","unit":"冶金","role":"审核人"},
{"id":100532,"name":"付红春","unit":"管道","role":"专业负责人"},
{"id":100224,"name":"叶薇","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":100228,"name":"廖文江","unit":"项目经理","role":"项目经理"},
{"id":103485,"name":"廖一文","unit":"","role":"合同管理工程师"},
{"id":100153,"name":"袁蓓蓓","unit":"","role":"项目秘书"},
{"id":100971,"name":"韦雯","unit":"","role":"评审管理员"}]},
100533:{"userName":"100533李津","projID":320668,"prjNum":"2A18A130404","prjName":"NIMS优化李津","prjData":[{"id":100944,"name":"何峰","unit":"冶金","role":"专业负责人"},
{"id":100944,"name":"何峰","unit":"冶金","role":"设计人"},
{"id":101193,"name":"王永强","unit":"冶金","role":"校核人"},
{"id":100224,"name":"叶薇","unit":"冶金","role":"审核人"},
{"id":100551,"name":"罗敏杰","unit":"水工","role":"专业负责人"},
{"id":100033,"name":"刘庆华","unit":"","role":"主管副总工程师"},
{"id":100007,"name":"徐赤农","unit":"","role":"公司分管领导"},
{"id":101193,"name":"王永强","unit":"项目经理","role":"项目经理"},
{"id":100107,"name":"姜茜","unit":"","role":"合同管理工程师"},
{"id":100928,"name":"胡杨","unit":"","role":"项目秘书"},
{"id":100919,"name":"周晓茵","unit":"","role":"评审管理员"}]}
};
	var nav = [{
		"text" : "00 前言_运行要求",
		"nid" : 1,
		"file" : "video/00前言_运行要求.flv",
		"width" : 1024,
		"height" : 768
	},{
		"text" : "01 进入项目门户",
		"nid" : 1,
		"file" : "video/01进入项目门户.flv",
		"width" : 1024,
		"height" : 768
	},{
		"text" : "02 阶段与里程碑计划",
		"nid" : 1,
		"file" : "video/02阶段与里程碑计划.flv",
		"width" : 1024,
		"height" : 768
	},{
		"text" : "03 收款计划",
		"nid" : 1,
		"file" : "video/03收款计划.flv",
		"width" : 1024,
		"height" : 768
	},{
		"text" : "04 二级策划",
		"nid" : 1,
		"file" : "video/04二级策划.flv",
		"width" : 1024,
		"height" : 768
	},{
		"text" : "05 三级策划",
		"nid" : 1,
		"file" : "video/05三级策划.flv",
		"width" : 1024,
		"height" : 768
	},{
		"text" : "06 设计输入管理",
		"nid" : 1,
		"file" : "video/06设计输入管理.flv",
		"width" : 1024,
		"height" : 768
	},{
		"text" : "07 项目开工报告",
		"nid" : 1,
		"file" : "video/07项目开工报告.flv",
		"width" : 1024,
		"height" : 768
	},{
		"text" : "08 公司级方案评审",
		"nid" : 1,
		"file" : "video/08公司级方案评审.flv",
		"width" : 1024,
		"height" : 768
	},{
		"text" : "09 专业开工报告",
		"nid" : 1,
		"file" : "video/09专业开工报告.flv",
		"width" : 1024,
		"height" : 768
	},{
		"text" : "10 专业方案评审",
		"nid" : 1,
		"file" : "video/10专业方案评审.flv",
		"width" : 1024,
		"height" : 768
	},{
		"text" : "11 计算书校审",
		"nid" : 1,
		"file" : "video/11计算书校审.flv",
		"width" : 1024,
		"height" : 768
	},{
		"text" : "12 专业接口条件校审与提交",
		"nid" : 1,
		"file" : "video/12专业接口条件校审与提交.flv",
		"width" : 1024,
		"height" : 768
	},{
		"text" : "13 设计文本任务创建与分配",
		"nid" : 1,
		"file" : "video/13设计文本任务创建与分配.flv",
		"width" : 1024,
		"height" : 768
	},{
		"text" : "14 设计文本编制与专业校审",
		"nid" : 1,
		"file" : "video/14设计文本编制与专业校审.flv",
		"width" : 1024,
		"height" : 768
	},{
		"text" : "15 设计文本公司级评审",
		"nid" : 1,
		"file" : "video/15设计文本公司级评审.flv",
		"width" : 1024,
		"height" : 768
	},{
		"text" : "16 文本文印",
		"nid" : 1,
		"file" : "video/16文本文印.flv",
		"width" : 1024,
		"height" : 768
	},{
		"text" : "17 图纸校审",
		"nid" : 1,
		"file" : "video/17图纸校审.flv",
		"width" : 1024,
		"height" : 768
	}];
//});