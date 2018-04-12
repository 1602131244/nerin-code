var taskHeaderListUrl = "http://"+window.location.host+"/api/taskRest/taskHeaderList";
var prjNumber = null;
var prjid = null
var prjName = null;
var prjPhaseID = null;
var prjPhase = null;
var prjManagerName = null;
var taskName = null;
var workcode = null;
var userID = null;
var userPar = "";
var userName = null;

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
				taskHeaderId = getUrlParam("taskHeaderId");//url必须载入的参数
				userID = ""+logondata.userId;
				userPar = "userID="+userID;
				workcode = logondata.employeeNo;
				$('#loaddiv').panel('collapse').panel('setTitle', "正在执行，请稍候...");//window折叠
				if(taskHeaderId){
					$.ajax({
						url: taskHeaderListUrl,
						data: {"taskHeaderId":taskHeaderId},
						contentType : 'application/x-www-form-urlencoded',
						dataType: "json",
						type: "POST",
						success: function (datalist) {
							if(datalist.dataSource.length==1){
								prjName = datalist.dataSource[0].projectName;
								prjPhase = datalist.dataSource[0].designPhaseName;
								prjManagerName = datalist.dataSource[0].projectManagerName;
								prjNumber = datalist.dataSource[0].projectNumber;
								taskName = datalist.dataSource[0].taskName;
								jQuery.getScript("js/na_center.js");
								jQuery.getScript("js/na_north.js");
							}
						}
					});
				}else{
					jQuery.getScript("js/na_north.js");
					$('#loaddiv').window('close');
				}
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
