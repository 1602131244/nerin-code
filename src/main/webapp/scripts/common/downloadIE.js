/**
 * Created by Administrator on 2018-1-22.
 */

var config = new nerinJsConfig();

function windowsOpen() {
	var url = "http://"+window.location.host +'/index.html?doWork='+getUrlParam("doWork");
    window.open(url,'_self');
}
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}