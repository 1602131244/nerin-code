/**
 * local dev
 */

$(function () {
  if (900 <= window.screen.height) {
    $("body").css("font-size", "13px");
    $("body").css("font-family", "微软雅黑");
    $(".btn").css("font-size", "13px");
    $(".btn").css("font-family", "微软雅黑");
  } else {
    $("body").css("font-size", "11px");
    $("body").css("font-family", "微软雅黑");
    $(".btn").css("font-size", "11px");
    $(".btn").css("font-family", "微软雅黑");
  }

  //console.log(window.screen.width);
});

function nerinJsConfig() {
  this.evn="dev";

  //通用配制
  this.searchResultSize=1000;
  //开发环境配制
  if("dev"==this.evn){
    // this.baseurl ="http://192.168.15.95:8080";
    this.baseurl = "http://127.0.0.1:8080";
    setDevConfig(this);
  }else if("prod"==this.evn){
    setProdConfig(this);
    this.baseurl = "http://192.168.15.211:8080";
  }
}

function setDevConfig(c) {
  var contextConfig = new Object();
  // OA申请权限链接（供应商）
  contextConfig.supplierOaUrl = "http://192.168.15.198/interface/addworkflow.jsp?workflowid=3468&workcode=";
  // OA申请权限链接（客户）
  contextConfig.oaUrl = "http://192.168.15.198/interface/addworkflow.jsp?workflowid=4269&workcode=";
  // OA-图纸文印选项，头提交
  contextConfig.oaCatUrl = "http://192.168.15.198/login/LoginSSO.jsp?flowCode=AM04&erpid=";
  // 图纸文印-预览目录生成PDF链接、图号外链系统
  contextConfig.catPdfUrl = "http://192.168.15.252:16200/";
  // 条件单平台
  contextConfig.tjdServerUrl = "http://192.168.15.188:8080/ord/order_propage.action?RESPID=-1&FUNCID=-1&USERID=";
  // 二级策划
  contextConfig.twoChServerUrl = "http://192.168.15.95:8080/pages/wbsp/code/WBSLV2/WBSLV2.html?";
  // 三级策划
  contextConfig.threeChServerUrl = "http://192.168.15.95:8080/pages/wbsp/code/WBSLV3/WBSLV3.html?";
  // 专业开工报告
  contextConfig.zykgbgServerUrl = "http://192.168.15.198/interface/addworkflow.jsp?workflowid=602&workcode=";
  // 设计输入平台--测试
  contextConfig.sjsrptServerUrl =  "http://192.168.15.95:8080/pages/wbsp/code/DSINPUT/DESIGNINPUT.html?";
  // 更多流程-测试
  contextConfig.oalistServerUrl = "http://192.168.15.95:8080/pages/wbsp/code/otOAList/otOAList.html?";
  // 开票申请-测试
  contextConfig.kpsqServerUrl = "http://localhost:8080/pages/aria/invoiceApply.html?";
  // 开票申请
 // contextConfig.kpsqServerUrl = "http://192.168.15.95:8080/pages/aria/invoiceApply.html?";
  // 管道等级-标准号
  contextConfig.cqsStandardNumber = "http://192.168.15.152/std/stdsearch.aspx?";
  // NBCC打开PAGEOFFICE的JSP页面
  contextConfig.nbccPageOfficeUrl = "http://ebsapptest.nerin.com:8010/OA_HTML/cux_cooperate/OpenFile.jsp?";
  // NBCC合并文本接口页面
  contextConfig.nbccMergeWordUrl = "http://192.168.15.157:8888/Default.aspx?";
  c.contextConfig = contextConfig;
}

function setProdConfig(c) {
  var contextConfig = new Object();
  // OA申请权限链接（供应商）
  contextConfig.supplierOaUrl = "http://192.168.15.142/interface/addworkflow.jsp?workflowid=3468&workcode=";
  // OA申请权限链接（客户）
  contextConfig.oaUrl = "http://192.168.15.142/interface/addworkflow.jsp?workflowid=4390&workcode=";
  // OA-图纸文印选项，头提交
  contextConfig.oaCatUrl = "http://192.168.15.198/login/LoginSSO.jsp?flowCode=AM04&erpid=";
  // 图纸文印-预览目录生成PDF链接
  contextConfig.catPdfUrl = "http://192.168.15.252:16200/";
  // 条件单平台
  contextConfig.tjdServerUrl = "http://192.168.15.188:8080/ord/order_propage.action?RESPID=-1&FUNCID=-1&USERID=";
  // 二级策划

  contextConfig.twoChServerUrl = "http://192.168.27.55:8080/pages/jquery-easyui-1.4.5/test/WBSLV2/WBSLV2.html?";
  // 三级策划
  contextConfig.threeChServerUrl = "http://192.168.27.55:8080/pages/jquery-easyui-1.4.5/test/WBSLV3/WBSLV3.html?";
  // 专业开工报告
  contextConfig.zykgbgServerUrl = "http://192.168.15.198/interface/addworkflow.jsp?workflowid=602&workcode=";
  // 设计输入平台
  contextConfig.sjsrptServerUrl = "http://ebs.nerin.com:8008";
  // 管道等级-标准号
  contextConfig.cqsStandardNumber = "http://192.168.15.152/std/stdsearch.aspx?";
  // NBCC打开PAGEOFFICE的JSP页面
  contextConfig.nbccPageOfficeUrl = "http://ebs.nerin.com:8008/OA_HTML/cux_cooperate/OpenFile.jsp?";
  // NBCC合并文本接口页面
  contextConfig.nbccMergeWordUrl = "http://192.168.15.157:8888/Default.aspx?";
  // 设计输入平台--测试
  contextConfig.sjsrptServerUrl = "http://192.168.15.211:8080/pages/wbsp/code/DSINPUT/DESIGNINPUT.html?";
  // 更多流程
  contextConfig.oalistServerUrl = "http://192.168.15.211:8080/pages/wbsp/code/otOAList/otOAList.html?";
  c.contextConfig = contextConfig;

}

$.ajaxSetup({
  contentType:"application/json",
  dataType:"json",
});

var ajaxError_loadData = "加载数据出错！";
var ajaxError_sys = "系统异常，请稍后再试！";

var tips_delSuccess = "删除成功！";
var tips_saveSuccess = "保存成功！";
var valTips_selOne = "请勾选一行！";
var valTips_selcheckOne = "请选择一行！";
var valTips_workOne = "每次只能对一行数据进行操作，请确认只勾选一行数据！";

