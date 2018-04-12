
var prjviewproductsdataurl = "datagrid_Prjviewproductlist.json";// oracle包-链接图纸状态记录页面
var prjid = "";
var prjPhaseID = "";
var userNum = "";

$(function () {
    prjid = getUrlParam("projID");  //url必须载入的参数
    prjPhaseID = getUrlParam("phaseID");//url必须载入的参数
    userNum = getUrlParam("userNum");//url必须载入的参数

    var opts = $("#grid_changelists").datagrid("options");
    opts.url = prjviewproductsdataurl;
    $('#grid_changelists').datagrid("reload", {
        projID: prjid,
        phaseID: prjPhaseID,
        userNum: userNum
    });
});


function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}