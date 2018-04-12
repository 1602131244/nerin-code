var perID = null;
var userNum = null;
var userID = null;
var userName = null;
var userDept = null;
var urldate = getUrlParam("date");
if(urldate){
	var datesplit = urldate.split('-');
	var myDate = new Date(Number(datesplit[0]),(Number(datesplit[1]) - 1),Number(datesplit[2]));
}else var myDate = new Date();
var currYear = DateToString(myDate).strYear;//"2014";//
var currMon = DateToString(myDate).strMon;//"02";//
var currDate = DateToString(myDate).strDate;//"10";//
var currFullDate = "20180218";//DateToString(myDate).full;
var hisYear = null;
var hisMon = null;
var hisDate = null;
$(function () {
	$.ajax({
		url: "http://" + window.location.host +"/api/logonUser",//获取session身份
		contentType : 'application/x-www-form-urlencoded',
		dataType: "json",
		type: "GET",
		success: function(logondata) {
			if(!logondata.userId || logondata.userId == 0){
				alert("请登录 NIMS 2.0。");
				window.open("http://"+window.location.host,"_self");
			}else{
				perID = logondata.personId;
				userNum = logondata.userName;
				userID = ""+logondata.userId;
				userName = logondata.fullName;
				userDept = logondata.deptOrgName;
				
				$('#loaddiv').panel('collapse').panel('setTitle', "正在执行，请稍候...");//window折叠
				//$('#loaddiv').window('close'); 
				jQuery.getScript("js/workCalendar_center.js");
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
function DateToString(date){
	var result = {};
	result.strYear = date.getFullYear();
	result.strMon = date.getMonth()>8 ? ""+(1+date.getMonth()) : "0"+(1+date.getMonth());
	result.strDate = date.getDate()>9 ? ""+date.getDate() : "0"+date.getDate();
	result.full = result.strYear+result.strMon+result.strDate;
	return result;
}
function _getTheDay(fulldate){
	var datesplit = fulldate.split('-');
	var dateCaldate = new Date(Number(datesplit[0]),(Number(datesplit[1]) - 1),Number(datesplit[2]));
	var daysrc = {1:"一",2:"二",3:"三",4:"四",5:"五",6:"六",0:"日"};
	return daysrc[dateCaldate.getDay()];
}