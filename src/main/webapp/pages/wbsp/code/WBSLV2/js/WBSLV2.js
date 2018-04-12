var prjbasicdataurl = "http://"+window.location.host+"/api/lev2/enterProject";//"prjdataurl.json";//oracle包-当前项目阶段信息（面板标题、总部/分公司类型项目、是否启用双语、WBS发布版数、是否分子项管理、阶段计划起始日期、阶段计划完成日期）
var prjNumber = "";
var prjid = "111";
var prjName = "测试项目名称";
var prjPhaseID = "12";
var prjPhase = "DDA施工图设计";
var prjManagerName = "章颂泰";
var prjStartDate = "2015-09-10";
var prjEndDate = "2016-10-10";
var perID = "";
var userNum = "222";
var userID = null;
var userPar = "";
var isPM = false;
var draftwbsID = "123"; //草稿版id
var pubwbsID = "123"; //最新发布版id
var draftwbsCheckout = "";//草稿版WBS签出员工号，若未签出则为空值
var division = false;//是否分子项管理
var headOrbranch = "head";//总部或分公司类型项目
var dual = "";//双语
var oldPhase = false;//老阶段
var prj_major_dataurl = "http://"+window.location.host+"/api/lev2/querySpecList";//"datagrid_Prjmajor.json";//引用专业数据源 oracle包-加载当前项目参与专业列表
var speData = '';
var subitemNoEdting = '#subitem_toolbar_ref,#subitem_toolbar_add,#subitem_toolbar_del,#subitem_toolbar_edit,#subitem_toolbar_save,#subitem_toolbar_update,#subitem_toolbar_reset,#subitem_toolbar_checkin,#prjmajorrates';
var subitemEdting = '#subitem_toolbar_cancel,#subitem_toolbar_accept';
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
				prjid = getUrlParam("projID");  //url必须载入的参数
				prjPhaseID = getUrlParam("phaseID");//url必须载入的参数
				perID = ""+logondata.personId;
				userID = ""+logondata.userId;
				userPar = "userID="+userID;

				$.ajax({
					url: prjbasicdataurl + "?projID=" + prjid + "&phaseID=" + prjPhaseID+"&"+userPar,
					type: "GET",
					success: function (data) {
						try {
							dataprj = eval(data)["data"];
							var wbsStat = eval(data)["returnStatus"];
							var datamsg = eval(data)["msgData"];
						}
						catch (e) {
							dataprj = eval("(" + data + ")")["data"];
							var wbsStat = eval("(" + data + ")")["returnStatus"];
							var datamsg = eval("(" + data + ")")["msgData"];
						}
						if(wbsStat!="S"){
							alert("请完成该项目的综合管理操作："+datamsg);
							window.close();
						}else if(wbsStat=="S"){
							prjName = dataprj.projName;
							prjPhase = dataprj.phaseName;
							prjManagerName = dataprj.mgr;
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

							jQuery.getScript("js/WBSLV2_north.js");
							jQuery.getScript("js/WBSLV2_west.js");
							jQuery.getScript("js/WBSLV2_center.js");
							SubItemControl();
						}
					}
				});
			}
			$.ajax({
				url: prj_major_dataurl+"?projID="+prjid+"&phaseID="+prjPhaseID+"&"+userPar,
				type: "GET",
				success: function (data) {
					speData = JSON.stringify(data);
				}
			});
		},
		error: function(data){$.messager.alert("身份验证失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");}
	});
});
//分子项管理控制
function SubItemControl(){
    if (division) {
        $("#driftchecksub").prop("checked",true);
        $("#subitem_collapse").show();
        $('#reftabs').tabs('enableTab', 0);
        $('#reftabs').tabs('enableTab', 1);
        $('#reftabs').tabs('enableTab', 3);//显示子项tab
        //按钮、菜单项显示 / 有效性规则：
        //阶段分子项管理 & 总部类型项目：全部显示、有效；
        //阶段分子项管理 & 分公司类型项目：“系统”菜单项隐藏，其他显示、有效。
        if (headOrbranch != "head") {
            $("#ckbsysdisplay").hide();
            $('#reftabs').tabs('disableTab', 2);//隐藏系统tab
            $('#addsysbtn').hide();
			$('#adddivbtn').show();
			$('#delsys').hide();
			$('#delsub').show();
        }else {
            $('#reftabs').tabs('enableTab', 2);//显示系统tab
            $("#ckbsysdisplay").show();
            $('#addsysbtn').show();
			$('#adddivbtn').show();
            $('#delsys').show();
            $('#delsub').show();
        }
        subitemNoEdting = '#subitem_toolbar_ref,#subitem_toolbar_add,#subitem_toolbar_del,#subitem_toolbar_edit,#subitem_toolbar_save,#subitem_toolbar_update,#subitem_toolbar_reset,#subitem_toolbar_checkin,#prjmajorrates';
    }else {
        //阶段不分子项管理：“展示”按钮失效；
        $("#subitem_collapse").hide();
        $('#reftabs').tabs('disableTab', 2);//隐藏系统tab
        $('#reftabs').tabs('disableTab', 3);//隐藏子项tab
        $('#addsysbtn').hide();
        $('#adddivbtn').hide();
		$('#delsys').hide();
        $('#delsub').hide();
        $("#driftchecksub").prop("checked",false);
        subitemNoEdting = '#subitem_toolbar_ref,#subitem_toolbar_add,#subitem_toolbar_del,#subitem_toolbar_edit,#subitem_toolbar_save,#subitem_toolbar_update,#subitem_toolbar_reset,#subitem_toolbar_checkin,#prjmajorrates';
    }
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