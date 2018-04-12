var prjbasicdataurl = "http://"+window.location.host+"/api/lev3/enterProject";//"prjdataurl.json";//oracle包-当前项目阶段信息（面板标题、总部/分公司类型项目、是否启用双语、WBS发布版数、是否分子项管理、阶段计划起始日期、阶段计划完成日期）
var prjmajoruserdataurl = "http://"+window.location.host+"/api/lev3/getAssignmentListAll";//
var prj_major_dataurl = "http://"+window.location.host+"/api/lev2/querySpecList";
var prjNumber = "";
var prjid = "111";
var prjName = "测试项目名称";
var prjPhaseID = "12";
var prjPhase = "DDA施工图设计";
var prjManagerName = "章颂泰";
/* var mgrNum = ""; //项目经理工号
var gmgrNum = "";//常务副经理工号
var vmgrNum = "";//项目副经理工号 */
var guest = false;
var isPM = false;
var isCH = false;
var userID = null;
var userPar = "";
var prjStartDate = "";
var prjEndDate = "";
var userNum = "100232";
var userName = "";
var perID = 369;//1080
var draftwbsID = "123"; //草稿版id
var pubwbsID = "123"; //最新发布版id
var division = false;//是否分子项管理
var headOrbranch = "head";//总部或分公司类型项目
var dual = true;
var specialty = [];
var wbsVerName = "";
var currDiv = null;
var hisId = null;
var workTypeList = {};
var speList = {};
$(function () {
	$('#loaddiv').window('close');
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
				$("#major1").linkbutton("select");
				$("#major2").linkbutton("unselect");
				prjid = getUrlParam("projID");  //url必须载入的参数
				prjPhaseID = getUrlParam("phaseID");//url必须载入的参数
				guest = getUrlParam("mode")=="guest";//url必须载入的参数
				userID = ""+logondata.userId;
				userPar = "userID="+userID;
				$.ajax({
					url: prjbasicdataurl + "?projID=" + prjid + "&phaseID=" + prjPhaseID + "&" + userPar,
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
							alert("该项目尚未发布WBS，请先完成二级策划并发布WBS。");
							window.close();
						}else if(wbsStat=="S"){
							prjName = dataprj.projName;
							prjPhase = dataprj.phaseName;
							prjManagerName = dataprj.mgr;
							userName=dataprj.userName;
							userNum=dataprj.userNum;
							perID=""+dataprj.perID;
							if(dataprj.vmgrNum){
								var vmgr = dataprj.vmgrNum.split(",");
							}else var vmgr = [];
							if(perID==""+dataprj.mgrNum || perID==""+dataprj.gmgrNum || $.inArray(perID,vmgr)>-1) isPM = true;
							pubwbsID = dataprj.pubwbsID;
							division = dataprj.division;
							headOrbranch = dataprj.headOrbranch;
							dual=dataprj.dual;
							prjNumber = dataprj.projNumber;
							wbsVerName = dataprj.pubwbsName;
							$.ajax({
								url: prj_major_dataurl+"?projID="+prjid+"&phaseID="+prjPhaseID+"&"+userPar,
								type: "GET",
								success: function (prjspelist) {
									for(var i=0;i<prjspelist.length;i++){
										speList[prjspelist[i].industryCode] = prjspelist[i].name;
									}
									if(guest){
										for(var i=0;i<prjspelist.length;i++){
											specialty[i] = {};
											specialty[i].id = prjspelist[i].industryCode;
											specialty[i].name = prjspelist[i].name;
										}
									}else specialty = dataprj.specialty;
									if(specialty.length==0){
										alert("非项目成员仅限只读模式。");
										window.location.href = window.location+"&mode=guest";
									}
									jQuery.getScript("js/WBSLV3_north.js");
									jQuery.getScript("js/WBSLV3_west.js");
									jQuery.getScript("js/WBSLV3_center.js");
								}
							});
						}
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
//分子项管理控制
function SubItemControl(major) {
    //获取当前项目专业参与人员specialty:string:不限专业（all）或专业代字（2ME、2DC、2AR）
    $.ajax({
        url: prjmajoruserdataurl + "?projID=" + prjid + "&specialty=" + major,
        type: "GET",
        success: function (dataprj) {
			var majorusers = [];
            try {
                majorusers = eval(dataprj);
            }
            catch (e) { majorusers = eval("(" + dataprj + ")"); }
			var roles = {"设计人":"design","校核人":"check","审核人":"review","审定人":"approve","注册工程师":"certified","方案设计人":"scheme"};
			panelValues = new Object();
			for(var i = 0;i<majorusers.length;i++){
				var memberlist = [];
				for(var j=0;j<majorusers[i]["member"].length;j++){
					memberlist[memberlist.length] = {"num":majorusers[i]["member"][j]["perId"],"name":majorusers[i]["member"][j]["perName"]};
				}
				if(panelValues[majorusers[i]["paMemberSpecDTO"].specialty] == undefined){
					panelValues[majorusers[i]["paMemberSpecDTO"].specialty] = {};
					panelValues[majorusers[i]["paMemberSpecDTO"].specialty][roles[majorusers[i]["paMemberSpecDTO"].duty]] = [{"num":null,"name":"待定"}].concat(memberlist);
				}else {
					if(panelValues[majorusers[i]["paMemberSpecDTO"].specialty][roles[majorusers[i]["paMemberSpecDTO"].duty]] == undefined){
						panelValues[majorusers[i]["paMemberSpecDTO"].specialty][roles[majorusers[i]["paMemberSpecDTO"].duty]] = [{"num":null,"name":"待定"}].concat(memberlist);
					}else panelValues[majorusers[i]["paMemberSpecDTO"].specialty][roles[majorusers[i]["paMemberSpecDTO"].duty]] = panelValues[majorusers[i]["paMemberSpecDTO"].specialty][roles[majorusers[i]["paMemberSpecDTO"].duty]].concat(memberlist);
				}
			}
		}
    });
}
