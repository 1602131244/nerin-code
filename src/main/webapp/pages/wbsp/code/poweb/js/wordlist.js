var hosturl = "http://"+window.location.host;
var ebsurl = "http://ebsapptest.nerin.com:8080/cux/NBCC/OpenFile.jsp";
//"http://ebsapptest.nerin.com:8010/OA_HTML/cux_cooperate/OpenFile.jsp";
var currpattern = "ViewText";
var currchapter = null;
var editableArr = ["正在编辑", "专业检审修改", "重新编辑", "公司级评审修改"];//,"审批中","公司级评审中"];
var editstatus =["申请人处理","设计人审核","设计人处理"];
var nbccDlvr = null;
var opennode = false;
var OAurl = "192.168.15.198";
var taskHeaderId = getUrlParam("taskHeaderId");  //url必须载入的参数9268
var taskName = "";
var sourceType = getUrlParam("source") ? getUrlParam("source") : "TASK";//当前打开的是文本任务or文本模板
window.onunload = function(){
	$("#iframepage").attr('src','about:blank');
	//alert('The onunload event was triggered');
};
$(function () {
/* 	$(document).bind("contextmenu",function(e){//禁用右键
         return false;
    }); */
	$.ajax({
		url: "http://" + window.location.host +"/api/logonUser",//获取session身份
		contentType : 'application/x-www-form-urlencoded',
		dataType: "json",
		type: "GET",
		beforeSend: function() {},
		success: function(logondata) {
			if(!logondata.userId || logondata.userId == 0){
				alert("请登录 NIMS 2.0。");
				window.open("http://"+window.location.host,"_self");
			}else{
				var userNum = logondata.employeeNo;
				//var userNum = logondata.fullName;
				var Chapters = getUrlParam("chapterId");//104118,104123
				var requestid = getUrlParam("requestid");
				nbccDlvr = getUrlParam("nbccDlvr");
				var isoa = requestid?"1":"0";
				//if(getUrlParam("isoa")) isoa = getUrlParam("isoa");
				if(requestid){
					$.ajax({
						url:"http://"+window.location.host+"/api/wbspToOa/getApproveForm?requestId="+requestid,
						type:"GET",
						async:false,
						success:function(requestdata){
							var editablePeople = requestdata.currentPersonNum ? requestdata.currentPersonNum.split(",") : [];
							if($.inArray(""+userNum,editablePeople)>-1 && $.inArray(requestdata.status,editstatus)>-1){
								_mainload(requestdata.nbccTaskName);
							}else _mainload();
						}
					});
				}else _mainload();
				function _mainload(headtaskName){
					if(Chapters){
						var chapArr = Chapters.split(",");
						//currchapter = parseInt(chapArr[0]);
					}else var chapArr = [];
					if(getUrlParam("isoa")){
						var taskHeaderListreq = {"taskHeaderId":taskHeaderId,"isoa":getUrlParam("isoa")};
					}else var taskHeaderListreq = {"taskHeaderId":taskHeaderId,"isoa":isoa};
					$.ajax({
						url: hosturl+"/api/taskRest/taskHeaderList",
						data: taskHeaderListreq,
						contentType : 'application/x-www-form-urlencoded',
						dataType: "json",
						type: "POST",
						success: function(d) {
							if(d.dataSource.length>0){
								taskName = d.dataSource[0].taskName;
								$('#rootlayout').layout('panel', 'west').panel("setTitle", taskName);
								$('#layoutwest').layout('panel', 'south').panel("setTitle", '审批流程记录 <a href="javascript:_goNBCC();"><img src="../../themes/icons/edit_proper.png"/>策划章节</a>');
								if(getUrlParam("isoa")){
									var taskChaptersListreq = {"headerId":taskHeaderId,"isoa":getUrlParam("isoa")};
								}else var taskChaptersListreq = {"headerId":taskHeaderId,"isoa":isoa};
								$.ajax({
									url: hosturl+"/api/taskRest/chaptersList?lineType=ALL&source="+sourceType,
									data: taskChaptersListreq,
									contentType : 'application/x-www-form-urlencoded',
									dataType: "json",
									type: "GET",
									success: function(data) {
										$('#divtree').tree({//章节树
											onLoadSuccess:function(){
												$('#modal').window('close');
												if(!currchapter && chapArr.length>0){
													var node = $('#divtree').tree('find', parseInt(chapArr[0]));
													_goNode(node);
													if(!currchapter) $.messager.alert("操作提示", "此章节尚无内容可查看，请双击其他章节以查看文本内容。", "warning");
												}else _selectcurrnode();
											},
											checkbox:false,
											onBeforeSelect:function(node){
												if(!currchapter) return false;
											},
											onClick:function(node){
												_selectcurrnode();
											},
											onDblClick:function(node){
												_goNode(node);
											},
											data: data.dataSource,
											formatter:function(node){
												if(node.wordFlag == "E" && ($.inArray(node.chapterStatusName, editableArr) > -1 || (headtaskName && headtaskName==taskName)) && !node.xmlCode){
													var txt = "<u>"+node.chapterName+"</u>";
													if(node.chapterFileFlag == 0) txt = "<span style='color:red'>* </span>" + txt;
												}else if(node.wordFlag != "N" && (node.chapterFileFlag == 1 || node.xmlCode)){
													var txt = node.chapterName;
												}else txt = "<span style='color:lightsteelblue'>"+node.chapterName+"</span>";
												if(isoa && isoa == "0" && $.inArray(""+node.chapterId,chapArr) > -1) txt = "<strong>"+txt+"</strong>";
												txt = node.chapterNo ? node.chapterNo+" "+txt : txt;
												return txt;
											},
											filter:function(q, node){  // q: doFilter的第二个参数
												var b_filter = node.enableFlag == 1 ? true : false;
												var b_filter = false;
												if(node.enableFlag == 1 && (isoa == "0" || node.projElementId == nbccDlvr || $.inArray(""+node.chapterId,chapArr) > -1)){
													b_filter = true;
													if(!opennode && node.wordFlag != "N" && chapArr.length == 0){
														opennode = true;
														_goNode(node);
													}
												}
												//((isoa=="1" && $.inArray(""+node.chapterId,chapArr)>-1)||(isoa == "0"))) b_filter = true;
												return b_filter;
											}
										}).tree('doFilter', '');
										$('#divtree').tooltip({position:"bottom",content:"需双击切换章节"});
										$('#viewworkall').prop("checked",false).change(_loadspedg);//不限工作包查看
										$('#viewspeall').prop("checked",false).change(_loadentdg);//不限专业查看
									},
									error:function(data){
										$('#modal').window('close');
										$.messager.alert("加载失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
									}
								});
								function _enterChapter(nid){
									var node = $('#divtree').tree('find',nid);
									if(node.xmlCode){
										$.ajax({
											url: hosturl+"/api/taskRest/autoCreateWordWithXml",
											data: {taskHeaderId: taskHeaderId},
											contentType : 'application/x-www-form-urlencoded',
											dataType: "json",
											type: "POST",
											async:false,
											beforeSend: function() {
												_resizeiframe(true);
												$('#modal').window('open');
											},
											success: function(xmldata) {
												_resizeiframe(false);
												$('#modal').window('close');
												if(xmldata==true) _goiframe("ViewText");
											}
										});
									}else{
										if(node.wordFlag == "E" && ($.inArray(node.chapterStatusName, editableArr) > -1 || (headtaskName && headtaskName==taskName))){
											_goiframe("EditText");
										}else _goiframe("ViewText");
									}
									function _goiframe(pattern){
										var potitle = node.chapterNo ? node.chapterNo+" "+node.chapterName : node.chapterName;
										potitle = pattern == "EditText" ? potitle+"（可编辑）" : potitle+"（仅查看）";
										$("#iframepage").attr('src',ebsurl+"?file_name="+node.taskChapterFileName+"&pattern="+pattern+"&taskHeaderId="+taskHeaderId+"&taskChapterId="+node.chapterId+"&source_file_name="+node.sourceChapterFileName);//"&caption="+potitle+
										//alert(potitle);
										currpattern = pattern;
										currchapter = node.chapterId;
										_selectcurrnode("loaddg");
										setTimeout(_onbeforeunload, 500);
									}
								}
								function _goNode(node){
									_selectcurrnode();
									window.onbeforeunload = null;
									if(//node.chapterId == currchapter ||
									!((node.wordFlag != "N" && (node.chapterFileFlag == 1 || node.xmlCode))
									|| (node.wordFlag == "E" && ($.inArray(node.chapterStatusName, editableArr) > -1 || (headtaskName && headtaskName==taskName)) && !node.xmlCode))){
										setTimeout(_onbeforeunload, 500);
										return;
									}else if(currpattern == "EditText"){
										_resizeiframe(true);
										var currnode = $('#divtree').tree('find',currchapter);
										var currtitle = currnode.chapterNo ? currnode.chapterNo+" "+currnode.chapterName : currnode.chapterName;
										$.messager.confirm('操作提示', "请确认 <b>"+currtitle+"</b> 文本内容已无需保存，将更新显示章节。", function (r) {
											_resizeiframe(false);
											if (r){
												_enterChapter(node.id);
												if(getUrlParam("isoa")){
													var reChaptersListreq = {"headerId":taskHeaderId,"isoa":getUrlParam("isoa")};
												}else var reChaptersListreq = {"headerId":taskHeaderId,"isoa":isoa};
												$.ajax({
													url: hosturl+"/api/taskRest/chaptersList?lineType=ALL&source="+sourceType,
													data: reChaptersListreq,
													contentType : 'application/x-www-form-urlencoded',
													dataType: "json",
													type: "GET",
													success: function(data){$('#divtree').tree('loadData',data.dataSource);}
												});
											}else{
												setTimeout(_onbeforeunload, 500);
											}
										}).panel('options').onBeforeClose = function(){
											_resizeiframe(false);
											setTimeout(_onbeforeunload, 500);
										};
									}else _enterChapter(node.id);
								}
							}else{
								$('#modal').window('close');
								alert("无权限查看当前文本，即将关闭当前页面。");
								//window.close();
							}
						}
					});
				}
			}
		},
		error: function(data){$.messager.alert("身份验证失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");}
	});
	function _onbeforeunload(){
		if(currpattern == "EditText"){
			window.onbeforeunload = function (e) {
				return (e || window.event).returnValue = '请确认当前文本内容已无需保存，将离开页面。';
			}
		}else window.onbeforeunload = null;
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
	function _selectcurrnode(loaddg){
		if(currchapter){
			var currnode = $('#divtree').tree('find',currchapter);
			$('#divtree').tree('select', currnode.target);
		}
		if(loaddg){
			if(!$('#viewworkall').is(":checked")) _loadspedg();
			if(!$('#viewspeall').is(":checked")) _loadentdg();
		}
	}
	function _loadspedg(){
		if($('#viewworkall').is(":checked")){
			var viewworkall = '';
		}else{
			var currnd = $('#divtree').tree('getSelected');
			if(currnd) var viewworkall = "&pElementId="+currnd.projElementId;
		}
		if(viewworkall!=undefined){
			$.ajax({//oracle包-当前工作包在协作文本中相关的“设计文件校审流程"OA流程列表
				url: "http://"+window.location.host+"/api/naviWbpsRest/wbChapter?taskHeaderId="+taskHeaderId+viewworkall,
				type: "GET",
				success: function (spedgdata) {
					var dgData = [];
					for(var i=0;i<spedgdata.length;i++){
						if(spedgdata[i].requestName.indexOf("公司级评审") < 0){
							dgData[dgData.length] = {};
							for(var key in spedgdata[i]) dgData[dgData.length - 1][key] = spedgdata[i][key];
						}
					}
					$("#spedg").datagrid({
						data:dgData,
						rownumbers: true,
						border: false,toolbar:'#spedgtb',
						singleSelect:true,
						method: 'get',
						//fitColumns:true,
						fit: true,striped:true,
						idField: 'requestId',
						frozenColumns: [[
							{ field: 'zy', title: '专业'},
							{ field: 'requestId', title: '流程ID',formatter: function(value,row,index){
								return value+" <a href='javascript:_linkOA("+row.requestId+");'>查看</a>";
							} }]],
						columns:[[
							{ field: 'status', title: '当前状态'},
							{ field: 'currentPerson', title: '当前操作者'},
							{ field: 'createBy', title: '创建人' },
							{ field: 'createDate', title: '创建日期' },
							{ field: 'requestName', title: '流程标题'}]]
					}).datagrid('clearSelections');
				}
			});
		}
	}
	function _loadentdg(){
		if($('#viewspeall').is(":checked")){
			var viewspeall = '';
		}else{
			var currnd = $('#divtree').tree('getSelected');
			if(currnd) var viewspeall = "&zhuanye="+currnd.specialtyName;
		}
		if(viewspeall!=undefined){
			$.ajax({//oracle包-协作任务的公司级评审相关OA流程列表
				url: "http://"+window.location.host+"/api/naviWbpsRest/wbTask?taskHeaderId="+taskHeaderId,
				type: "GET",
				success: function (rw_data) {
					$.ajax({//oracle包-当前工作包在协作文本中相关的“设计文件校审流程"OA流程列表
						url: "http://"+window.location.host+"/api/naviWbpsRest/wbChapter?taskHeaderId="+taskHeaderId+viewspeall,
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
							$("#entdg").datagrid({
								data:dgData,
								rownumbers: true,
								border: false,toolbar:'#entdgtb',
								singleSelect:true,
								method: 'get',
								//fitColumns:true,
								fit: true,striped:true,
								idField: 'requestId',
								frozenColumns: [[
									{ field: 'ot', title: '流程类型',formatter: function(value,row,index){
										var ot = row.requestName.split("-");
										if(ot.length>0){
											return ot[ot.length - 1];
										}else return "";
									}},
									{ field: 'zy', title: '专业'},
									{ field: 'requestId', title: '流程ID',formatter: function(value,row,index){
										return value+" <a href='javascript:_linkOA("+row.requestId+");'>查看</a>";
									} }]],
								columns:[[
									{ field: 'status', title: '当前状态'},
									{ field: 'currentPerson', title: '当前操作者'},
									{ field: 'createBy', title: '创建人' },
									{ field: 'createDate', title: '创建日期' },
									{ field: 'requestName', title: '流程标题'}]]
							}).datagrid('clearSelections');
						}
					});
				}
			});
		}
	}
});

function _goNBCC(){//跳转NBCC平台协作任务
	window.open("http://"+window.location.host+"/pages/management/index.html?doWork=wbrw&taskName="+taskName+"&taskHeaderId="+taskHeaderId, '_blank');
}
function _linkOA(requestId){//跳转OA表单
	window.open("http://"+OAurl+"/workflow/request/ViewRequest.jsp?requestid="+requestId,'_blank','location=no');
}
//公共方法,字符串转换日期
String.prototype.stringToDate = function () {
    return new Date(Date.parse(this.replace(/-/g, "/")));
}

function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}
