/**
 * Created by Zach on 16/5/16.
 */
var jobNo = "";
$(function () {
  var doWork = $.query.get("doWork");
  var chapterId = $.query.get("chapterId");
  var taskHeaderId = $.query.get("taskHeaderId");
  var isoa = $.query.get("isoa");
  jobNo = $.query.get("jobNo");

  var config = new nerinJsConfig();
  if ("sysLogin" == doWork)
    $('#loginModal').modal('show');

  //login popup
  //if("dev"!=config.evn) {
    $.get('/api/authenticate', function (data) {
      if (!data['currentUser'] || '' == data['currentUser']) {
        // $('#loginModal').modal({});
        getADUser();
        autoLogon(false);
        $('#loginPopup').css('display', 'block');
      } else {
        if ("true" != jobNo && "" != jobNo) {
          autoLogon(true);
          return;
        }
        $('#span_userNo').html(data['currentUser']);
        // $.getJSON('/api/account', function (data) {
        //   console.log(data);
        // });

        checkIEVersion();
        setTimeout(function(){
              jump();}
            ,1000
        );
        // jump();

        $('#loginPopup').css('display', 'none');
        $('#userProfile').css('display', 'block')
      }
    });
  //}else {
  //  $('#loginPopup').css('display', 'block');
  //}

  $('#loginPopup').click(function () {
    $('#j_username').val("");
    $('#j_password').val("");
    $('#loginModal').modal({
    });
  });

  $('#loginModal').on('shown.bs.modal', function (e) {
    $('#j_username').focus();
  });

  //login
  $('#loginbutton').click(function(){
    var username=$('#j_username').val();
    var password=$('#j_password').val();
    $.ajaxSetup({
      contentType:"application/x-www-form-urlencoded; charset=UTF-8",
      dataType:"",
    });
    $.post('/api/authentication',{'_csrf':$.cookie('CSRF-TOKEN'),j_username:username,j_password:password})
      .done(function (data) {
        checkIEVersion();
        setTimeout(function(){
              jump();}
            ,1000
        );
          // console.log(data);
        })
      .fail(function(response) {
        if(401==response.status){
          window.parent.error('请检查用户名密码是否正确!');
          //console.log(response);
        }
      });
  });

  //logout
  $('#logout').click(function(){
    //$.cookie('name');
    //$.post(config.baseurl+'/api/logout',{'_csrf':$.cookie('CSRF-TOKEN')},function () {
    //  location.reload();
    //});
     $("#logoutform").submit();
  });

  var jump = function(){
    var url = "pages/management/index.html";
    if (doWork == "jumptobbs") {
      url = "pages/bbsLogin/index.html?url=" + encodeURIComponent($.query.get("url"));
    } else if ("" != doWork && "true" != doWork) {
      if (("" != taskHeaderId && "true" != taskHeaderId)||("" != chapterId && "true" != chapterId)){
        url += "?doWork=" + doWork + "&chapterId=" + chapterId + "&taskHeaderId=" + taskHeaderId + "&isoa=" + isoa;
      }else{
        url += "?doWork=" + doWork ;
      }
    }
    window.location.href = url;
  }
});

function autoLogon(flag) {
  if ("true" != jobNo && "" != jobNo) {
    if (flag) {
      $.post('/api/logout',{'_csrf':$.cookie('CSRF-TOKEN')},function () {
      });
      $('#j_username').val(jobNo);
      $('#j_password').val(jobNo);
      $('#loginbutton').trigger("click");
    } else {
      $('#j_username').val(jobNo);
      $('#j_password').val(jobNo);
      $('#loginbutton').trigger("click");
    }
  }
}

function getADUser() {
  if ("true" != jobNo && "" != jobNo) {
    return;
  }
  //console.log("取ad");
  // 判断浏览器是不是ie
  var isIE = false;
  var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
  var isOpera = userAgent.indexOf("Opera") > -1;
  if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) {
    isIE = true;
  }
  // 因为ie10-ie11的版本问题，不再支持document.all判断，所以ie判断函数要重新写了
  if(!isIE){
    isIE = !!window.ActiveXObject || "ActiveXObject" in window;
  }
  // 如果是ie，则获取以下信息并登陆
  if(isIE){
    try
    {
      var WshNetwork = new ActiveXObject("WScript.Network");
      if (undefined != WshNetwork.UserName && "" != WshNetwork.UserName && null != WshNetwork.UserName) {
        $('#j_username').val(WshNetwork.UserName);
        $('#j_password').val(WshNetwork.UserName);
        $('#loginbutton').trigger("click");
      }
    }
    catch(e)
    {
      var promptStr = "自动登录需要允许ActiveXObject脚本运行，请您先进行设置！"
          + "\n设置步骤：在“IE-Internet选项-安全-自定义级别-ActiveX控件和插件-对未标记为可安全执行脚本的ActivesX控件”,设置为“提示”或“启用”";
      alert(promptStr);
    }
  }
  else{
    var promptStr = "请使用IE10及以上版本或360浏览器（兼容模式）运行程序！";
    alert(promptStr);
  }
}
