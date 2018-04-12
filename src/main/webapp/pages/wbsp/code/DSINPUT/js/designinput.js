var prjbasicdataurl = "http://" + window.location.host + "/api/lev2/enterProject";//"prjdataurl.json";//oracle包-当前项目阶段信息（面板标题、总部/分公司类型项目、是否启用双语、WBS发布版数、是否分子项管理、阶段计划起始日期、阶段计划完成日期）
var prj_get_info = "http://" + window.location.host + "/api/logonUser";
var prjNumber = "";
var prjid = "";
var prjName = "测试项目名称";
var prjPhaseID = "";
var prjPhase = "DDA施工图设计";
var prjManagerName = "章颂泰";
var prjStartDate = "2015-09-10";
var prjEndDate = "2016-10-10";
var userNum = "";
var userID = null;
var personID = null;
var userPar = "";
var personPar = "";
var userName = "章颂泰";
var isPM = false;
var draftwbsID = "123"; //草稿版id
var pubwbsID = "123"; //最新发布版id
var draftwbsCheckout = "";//草稿版WBS签出员工号，若未签出则为空值
var division = false;//是否分子项管理
var headOrbranch = "head";//总部或分公司类型项目
var dual = "";//双语
var oldPhase = false;//老阶段
//var prj_major_dataurl = "http://"+window.location.host+"/api/lev2/querySpecList";//"datagrid_Prjmajor.json";//引用专业数据源 oracle包-加载当前项目参与专业列表
var speData = '';
var guest = false;
var majorData;
$(function () {
	$.ajax({
        url: prj_get_info,
        contentType: 'application/x-www-form-urlencoded',
        dataType: 'json',
        type: 'GET',
        success: function (logondata) {
            if (!logondata.userId || logondata.userId == 0) {
                alert("请登录 NIMS2.0。");
                window.open("http://" + window.location.host, "_self");
            } else {
                prjid = getUrlParam("projID");  //url必须载入的参数
                prjPhaseID = getUrlParam("phaseID");//url必须载入的参数
				guest = getUrlParam("mode")=="guest";
                userID = "" + logondata.userId;
                personID = "" + logondata.personId;
                userNum = "" + logondata.employeeNo;
                if (userID != null) userPar = "userID=" + userID;
                if (userID != null) personPar = "personID=" + personID;
				userName = getUrlParam("userName");//url必须载入的参数
				$('#loaddiv').panel('collapse').panel('setTitle', "正在执行，请稍候...");//window折叠
				$.ajax({
					url: prjbasicdataurl + "?projID=" + prjid + "&phaseID=" + prjPhaseID + "&" + userPar,
					type: "GET",
					success: function (dataprj) {
						try {
							dataprj = eval(dataprj.data);
						}
						catch (e) {
							dataprj = eval("(" + dataprj + ")");
							;
						}
						prjName = dataprj.projName;
						prjPhase = dataprj.phaseName;
						prjManagerName = dataprj.mgr;
						userID = "" + dataprj.userID;
						if (dataprj.checkoutable) isPM = true;
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
						$('#loaddiv').window('close');
						$.getJSON("../../common/majorList.json", function (data) {
							majorData = data;
						});

						jQuery.getScript("js/dt_north.js");
						jQuery.getScript("js/dt_tabs.js");
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
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}
