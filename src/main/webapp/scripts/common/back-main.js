/**
 * Created by Zach on 16/5/16.
 */
function doAutoWork(a) {
  var doWork = $.query.get("doWork");
  var url = "";
  // 是否关闭左边菜单栏
  var closeMenu = 0;
  if ("wbrw" == doWork) { // 文本任务
    if (!a) {
      window.parent.warring("没有[文本任务]的菜单权限，请联系管理员");
      return;
    }
    var param = "";
    var taskName = $.query.get("taskName");
    if ("" != taskName)
      param += "taskName=" + taskName;
    var taskHeaderId = $.query.get("taskHeaderId");
    if ("" != taskHeaderId)
      param += "&taskHeaderId=" + taskHeaderId;
    var proName = $.query.get("proName");
    if ("" != proName) {
      param += "&proName=" + proName;
    }
    // var isoa = $.query.get("isoa");
    // if ("" != isoa) {
    //   param += "&isoa=" + isoa;
    // }

    url = "/pages/nbcc/task/taskList.html?";
    if ("" != param)
      url += param;
    closeMenu = 1;
  } else if ("gddj" == doWork){ // 管道等级
    if (!a) {
      window.parent.warring("没有[管道等级]的菜单权限，请联系管理员");
      return;
    }
    closeMenu = 1;
    url = "/pages/cqs/cqs.html";
  } else if ("xmdh" == doWork){ // 项目导航
    if (!a) {
      window.parent.warring("没有[项目导航]的菜单权限，请联系管理员");
      return;
    }
    closeMenu = 1;
    url = "/pages/navi/naviList.html";
  } else if ("lxgys_spr" == doWork){ // 零星供应商-审批人
    if (!a) {
      window.parent.warring("没有[零星供应商-审批人]的菜单权限，请联系管理员");
      return;
    }
    // closeMenu = 1;
    url = "/pages/tsc/ts/tsAuditList.html";
  } else if ("lxgys_tjr" == doWork){ // 零星供应商-提交人
    if (!a) {
      window.parent.warring("没有[零星供应商-提交人]的菜单权限，请联系管理员");
      return;
    }
    // closeMenu = 1;
    url = "/pages/tsc/ts/tsSubmitList.html";
  } else if ("khsq_spr" == doWork){ // 客户申请-审批人
    if (!a) {
      window.parent.warring("没有[客户申请-审批人]的菜单权限，请联系管理员");
      return;
    }
    // closeMenu = 1;
    url = "/pages/tsc/tc/tcAuditList.html";
  } else if ("khsq_tjr" == doWork){ // 客户申请-提交人
    if (!a) {
      window.parent.warring("没有[客户申请-提交人]的菜单权限，请联系管理员");
      return;
    }
    // closeMenu = 1;
    url = "/pages/tsc/tc/tcSubmitList.html";
  } else if ("tb_ht_wd" == doWork){ // 投标及合同模板、文档库
    if (!a) {
      window.parent.warring("没有[投标及合同模板、文档库]的菜单权限，请联系管理员");
      return;
    }
    window.open("/pages/marketing_bid_contract/web/Tdata/Tdata.html");
    var userAgent = navigator.userAgent;
    if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Chrome") !=-1) {
      window.location.href="about:blank";
    } else {
      window.opener = null;
      window.open("", "_self");
      window.close();
    }
  }else if ("tb_ht_wd" == doWork){ // 投标及合同模板、文档库
    if (!a) {
      window.parent.warring("没有[投标及合同模板、文档库]的菜单权限，请联系管理员");
      return;
    }
    window.open("/pages/marketing_bid_contract/web/Tdata/Tdata.html");
    var userAgent = navigator.userAgent;
    if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Chrome") !=-1) {
      window.location.href="about:blank";
    } else {
      window.opener = null;
      window.open("", "_self");
      window.close();
    }
  }else if ("poweb" == doWork){ // NBCC文本编辑界面
    var param = "";
    var chapterId = $.query.get("chapterId");
    param += "chapterId=" + chapterId;
    var taskHeaderId = $.query.get("taskHeaderId");
    param += "&taskHeaderId=" + taskHeaderId;
    var isoa = $.query.get("isoa");
    param += "&isoa=" + isoa;
    url = "/pages/wbsp/code/poweb/poweb.html?";
    if ("" != param)
      url += param;
    // if (!a) {
    //   window.parent.warring("没有[NBCC文本编辑]的菜单权限，请联系管理员");
    //   return;
    // }
    window.open(url);
    var userAgent = navigator.userAgent;
    if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Chrome") !=-1) {
      window.location.href="about:blank";
    } else {
      window.opener = null;
      window.open("", "_self");
      window.close();
    }
  }else if ("billEvent" == doWork){ // 合同开票申请
    var param = "";
    var contractId = $.query.get("contractId");
    param += "contractId=" + contractId;
    var projectId = $.query.get("projectId");
    param += "&projectId=" + projectId;
    url = "/pages/aria/invoiceApply.html?";
    if ("" != param)
      url += param;
    if (!a) {
      window.parent.warring("没有[开票申请]的菜单权限，请联系管理员");
      return;
    }
    closeMenu = 1;
  }
  // 打开链接
  if ("" != url) {
    $('#content').attr("src", url);
  }
  if (1 == closeMenu) {
    $('#indexMenu').hide(function () {
      $('#page-wrapper').css("margin-left", "0px");
      $('#closeMenu2').show();
    });
  }

}

$(function () {
  loadUser();
  // doAutoWork();
  startInit("content", "0");
  $('.closeMenu').on('click', function () {
      if ($('#indexMenu').is(':hidden')) {
        $('#indexMenu').show(function () {
          $('#page-wrapper').css("margin-left", "220px");
          $('#closeMenu2').hide();
        });
      } else {
        $('#indexMenu').hide(function () {
          $('#page-wrapper').css("margin-left", "0px");
          $('#closeMenu2').show();
        });
      }
  });

  $('#logout').click(function(){
    $("#logoutform").submit();
  });

  $("iframe.auto-height").iframeAutoHeight({minHeight: 500});

  var config = new nerinJsConfig();

  $.get(config.baseurl+'/api/isLogon',function (data) {
    if (!data)
        window.location.href = "/index.html";
  });

  var doWorkEnable = false;
  var doWorkEnable_url = "";
  var doWork = $.query.get("doWork");
  if ("wbrw" == doWork) { // 文本任务
    doWorkEnable_url = "/pages/nbcc/task/taskList.html";
  } else if ("gddj" == doWork){ // 管道等级
    doWorkEnable_url = "/pages/cqs/cqs.html";
  } else if ("lxgys_spr" == doWork){ // 零星供应商-审批人
    doWorkEnable_url = "/pages/tsc/ts/tsAuditList.html";
  } else if ("lxgys_tjr" == doWork){ // 零星供应商-提交人
    doWorkEnable_url = "/pages/tsc/ts/tsSubmitList.html";
  } else if ("khsq_spr" == doWork){ // 客户申请-审批人
    doWorkEnable_url = "/pages/tsc/tc/tcAuditList.html";
  } else if ("khsq_tjr" == doWork){ // 客户申请-提交人
    doWorkEnable_url = "/pages/tsc/tc/tcSubmitList.html";
  } else if ("tb_ht_wd" == doWork){ // 投标及合同模板、文档库
    doWorkEnable_url = "/pages/marketing_bid_contract/web/Tdata/Tdata.html";
  } else if ("xmdh" == doWork){ // 项目导航
    doWorkEnable_url = "/pages/navi/naviList.html";
  }else if ("billEvent" == doWork){ // 开票申请
    doWorkEnable_url = "/pages/aria/invoiceApply.html";
  }

  //初始化后端页面菜单
  $.get(config.baseurl+'/api/menus/tree/back',function (data) {
    //循环一级节点数据
    $(data).each(function (index) {
      //获取当前一级菜单节点
      var children_L1 = data[index].children;

      var target_ = ("1" == data[index].outsideUrl ? "_blank" : "content");

      //如果当前一级菜单有子节点
      if(children_L1.length>0){
        isEnableDoWork(doWorkEnable_url, data[index].url);
        //创建带箭头一级子节点
        var rootNav=$('<li><a target="' + target_ + '" href="'+data[index].url+'"><i class="fa '+data[index].icon+' fa-fw"></i>'+data[index].name+'<span class="fa arrow"></span></a></li>');
        //创建一级子节点的孩子节点,即二级节点
        var dropdown_L1 = $('<ul class="nav2 nav-second-level"></ul>');
        //循环二级节点
        $(children_L1).each(function (index1) {
          //获取二级节点子节点,即三级节点
          var children_L2 = children_L1[index1].children;

          //如果当前二级节点包含子节点,即三级节点
          if(children_L2.length>0){
            isEnableDoWork(doWorkEnable_url, children_L1[index1].url);
            var target_2 = ("1" == children_L1[index1].outsideUrl ? "_blank" : "content");

            //创建展开箭头的二级子节点
            var child_L1=$('<li><a target="' + target_2+ '" href="'+children_L1[index1].url+'">'+children_L1[index1].name+'<span class="fa arrow"></span></a></li>');
            //创建二级节点的子节点,即三级节点
            var dropdown_L2 = $('<ul class="nav2 nav-third-level"></ul>');

            //循环三级节点
            $(children_L2).each(function (index2) {
              isEnableDoWork(doWorkEnable_url, children_L2[index2].url);
              var target_3 = ("1" == children_L2[index2].outsideUrl ? "_blank" : "content");

              var child_L2 = $('<li><a target="' + target_3+ '" href="'+children_L2[index2].url+'">'+children_L2[index2].name+'</a></li>');
              child_L1.append(dropdown_L2.append(child_L2));
            });
          }else {
            isEnableDoWork(doWorkEnable_url, children_L1[index1].url);
            var target_2 = ("1" == children_L1[index1].outsideUrl ? "_blank" : "content");
            //创建二级节点
            var child_L1 = $('<li><a target="' + target_2+ '" href="'+children_L1[index1].url+'"><i class="fa '+children_L1[index1].icon+' fa-fw"></i>'+children_L1[index1].name+'</a></li>');

          }
          //展开箭头的二级结点
          dropdown_L1.append(child_L1);
          //添加一级结点
          $(rootNav).append(dropdown_L1);
        });
      }else {
        isEnableDoWork(doWorkEnable_url, data[index].url);
        //正常一级节点
        var rootNav=$('<li><a target="' + target_ + '" href="'+data[index].url+'"><i class="fa '+data[index].icon+' fa-fw"></i> '+data[index].name+'</a></li>');
      }
      $("#side-menu").append(rootNav);

    });
    $(function() {

      $('#side-menu').metisMenu();
      doAutoWork(doWorkEnable);
    });
  });

  function isEnableDoWork(a, b) {
    if (a == b)
      doWorkEnable = true;
  }

  // function setIframeHeight(){
  //   try{
  //     var iframe = document.getElementById("content");
  //     if(iframe.attachEvent){
  //       console.log("a:");
  //       $(iframe).css({ "height": iframe.contentWindow.document.documentElement.scrollHeight});
  //       // iframe.attachEvent("onload", function(){
  //       //   console.log("a" + iframe.contentWindow.document.documentElement.scrollHeight);
  //       //   iframe.height =  iframe.contentWindow.document.documentElement.scrollHeight;
  //       // });
  //       return;
  //     }else{
  //       console.log("b:" + iframe.contentWindow.document.documentElement.scrollHeight);
  //       // iframe.height = iframe.contentWindow.document.documentElement.scrollHeight;
  //       $(iframe).css({ "height": iframe.contentWindow.document.documentElement.scrollHeight});
  //       // iframe.onload = function(){
  //       //   iframe.height = iframe.contentDocument.body.scrollHeight + 10;
  //       // };
  //       return;
  //     }
  //   }catch(e){
  //     throw new Error('setIframeHeight Error');
  //   }
  // }
  //
  // window.setInterval(setIframeHeight, 600);
    
});

function loadUser() {
  $.get('/api/authenticate', function (data) {
    $('#span_userNo').html(data['currentUser']);
  });
}

var browserVersion = window.navigator.userAgent.toUpperCase();
var isOpera = browserVersion.indexOf("OPERA") > -1 ? true : false;
var isFireFox = browserVersion.indexOf("FIREFOX") > -1 ? true : false;
var isChrome = browserVersion.indexOf("CHROME") > -1 ? true : false;
var isSafari = browserVersion.indexOf("SAFARI") > -1 ? true : false;
var isIE = (!!window.ActiveXObject || "ActiveXObject" in window);
var isIE9More = (! -[1, ] == false);
function reinitIframe(iframeId, minHeight) {
  try {
    var iframe = document.getElementById(iframeId);
    var bHeight = 0;
    if (isChrome == false && isSafari == false)
      bHeight = iframe.contentWindow.document.body.scrollHeight;

    var dHeight = 0;
    if (isFireFox == true)
      dHeight = iframe.contentWindow.document.documentElement.offsetHeight + 2;
    else if (isIE == false && isOpera == false)
      dHeight = iframe.contentWindow.document.documentElement.scrollHeight;
    else if (isIE == true && isIE9More) {//ie9+
      var heightDeviation = bHeight - eval("window.IE9MoreRealHeight" + iframeId);
      if (heightDeviation == 0) {
        bHeight += 3;
      } else if (heightDeviation != 3) {
        eval("window.IE9MoreRealHeight" + iframeId + "=" + bHeight);
        bHeight += 3;
      }
    }
    else//ie[6-8]、OPERA
      bHeight += 3;

    var height = Math.max(bHeight, dHeight);
    if (height < minHeight) height = minHeight;
    iframe.style.height = height + "px";
  } catch (ex) { }
}
function startInit(iframeId, minHeight) {
  eval("window.IE9MoreRealHeight" + iframeId + "=0");
  window.setInterval("reinitIframe('" + iframeId + "'," + minHeight + ")", 600);
}
