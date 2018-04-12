var prjbasicdataurl = "http://"+window.location.host+"/api/lev3/enterProject";//"prjdataurl.json";//oracle包-当前项目阶段信息（面板标题、总部/分公司类型项目、是否启用双语、WBS发布版数、是否分子项管理、阶段计划起始日期、阶段计划完成日期）
var prj_major_dataurl = "http://"+window.location.host+"/api/lev2/querySpecList";
var prjdataurl = "http://"+window.location.host+"/api/lev2/queryProjectList";//"datagrid_project.json";//oracle包-模糊查找项目列表及信息（包括各项目阶段列表）
var prjNumber = "";
var prjid = "111";
var prjName = "测试项目名称";
var prjPhaseID = "12";
var prjPhase = "DDA施工图设计";
var prjManagerName = "章颂泰";
var prjStartDate = "2015-09-10";
var prjEndDate = "2016-10-10";
var userNum = "100232";
var userID = null;
var userPar = "";
var userName = null;
var perID = 369;//1080
var isPM = false;
var draftwbsID = "123"; //草稿版id
var pubwbsID = "123"; //最新发布版id
var draftwbsCheckout = "";//草稿版WBS签出员工号，若未签出则为空值
var division = false;//是否分子项管理
var headOrbranch = "head";//总部或分公司类型项目
var dual = "";//双语
var oldPhase = false;//老阶段
var speData = '';
var specialty = [];
var exmode = false;
$(function () {
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
				prjid = getUrlParam("projID");  //url必须载入的参数
				prjPhaseID = getUrlParam("phaseID");//url必须载入的参数
				userID = logondata.userId;
				userPar = "userID="+userID;
				userNum = logondata.employeeNo;
				exmode = getUrlParam("exmode");
				$('#loaddiv').panel('collapse').panel('setTitle', "正在执行，请稍候...");//window折叠
				$.ajax({
					url: prjbasicdataurl + "?projID=" + prjid + "&phaseID=" + prjPhaseID+"&"+userPar,
					type: "GET",
					success: function (data) {
						try {
							dataprj = eval(data)["data"];
							var wbsStat = eval(data)["returnStatus"];
						}
						catch (e) {
							dataprj = eval("(" + data + ")")["data"];
							var wbsStat = eval("(" + data + ")")["returnStatus"];
						}
						if(wbsStat=="F"){
							$('#loaddiv').window('close');
							alert("该项目尚未发布WBS，请先完成二级策划并发布WBS。");
							window.close();
						}else if(wbsStat=="S"){
							prjName = dataprj.projName;
							prjPhase = dataprj.phaseName;
							prjManagerName = dataprj.mgr;
							//userID = ""+dataprj.userID;
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
							oldPhase = dataprj.oldPhase;
							specialty = dataprj.specialty;
							perID=""+dataprj.perID;

							if(dataprj.vmgrNum){
								var vmgr = dataprj.vmgrNum.split(",");
							}else var vmgr = [];
							if(perID==""+dataprj.mgrNum || perID==""+dataprj.gmgrNum || $.inArray(perID,vmgr)>-1) isPM = true;
							$('#gridPrjmajors').datagrid({
								data: [],
								idField: 'id',
								rownumbers: true, singleSelect: false, pagination: false,
								fit: true,toolbar:'#majortb',border:false,
								fitColumns: true,striped:true,
								columns: [[
									{ field: 'ck', checkbox: true },
									{ field: 'id', title: '代字', width: 70, formatter:function(value){return value.substr(1);} },
									{ field: 'name', title: '专业名称', width: 150 }]],
								onLoadSuccess: function (data) {
									$('#loaddiv').window('close');
								}
							});
							jQuery.getScript("js/dc_north.js");
							jQuery.getScript("js/dc_center.js");
						}
					},
					error:function(data){
						$('#loaddiv').window('close');
						$.messager.alert("加载失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
					}
				});
			}
		},
		error: function(data){$.messager.alert("身份验证失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");}
	});
});
//公共方法,字符串转换日期
String.prototype.stringToDate = function () {
    return new Date(Date.parse(this.replace(/-/g, "/")));
}

function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}