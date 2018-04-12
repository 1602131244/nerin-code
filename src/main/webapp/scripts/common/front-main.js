/**
 * Created by Zach on 16/5/15.
 */


// $(function() {
//   if (isIE) {
//     var versionName = getBrowserVersion();
//     var versionNo = 0;
//     if (undefined != versionName && "" != versionName)
//       versionNo = parseInt(versionName.replace("IE", ""));
//     if (versionNo < 10) {
//       alert("您的IE浏览器版本过低，请升级到IE10及以上版本！");
//       window.location.href = "downloadIE.html";
//     }
//   }
//   var config = new nerinJsConfig();
//
//   $('iframe.auto-height').iframeAutoHeight({minHeight: 500});
//
//   //初始化前端页面菜单
//   $.get(config.baseurl+'/api/menus/tree/front',function (data) {
//     console.log(data);
//     $(data).each(function (index) {
//       var children_L1 = data[index].children;
//       if(children_L1.length>0){
//         var rootNav=$('<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" href="'+data[index].url+'">'+data[index].name+'<span class="caret"></span></a></li>');
//         var dropdown_L1 = $('<ul class="dropdown-menu"></ul>');
//         $(children_L1).each(function (index1) {
//           console.log(children_L1[index1].name);
//           var child_L1 = $('<li><a href="'+children_L1[index1].url+'">'+children_L1[index1].name+'</a></li>')
//           dropdown_L1.append(child_L1);
//         });
//         $(rootNav).append(dropdown_L1);
//       }else {
//         var rootNav=$('<li><a href="'+data[index].url+'">'+data[index].name+'</a></li>');
//       }
//       $("#frontNav").append(rootNav);
//
//     });
//   });
//
// });

function checkIEVersion() {
  if (isIE) {
    var versionName = getBrowserVersion();
    var versionNo = 0;
    if (undefined != versionName && "" != versionName)
      versionNo = parseInt(versionName.replace("IE", ""));
    if (versionNo < 10) {
      alert("您的IE浏览器版本过低，请升级到IE10及以上版本！");
      // window.location.href = "downloadIE.html";
      window.location.href = "/downloadIE.html?doWork="+$.query.get("doWork");
    }
  }
  var config = new nerinJsConfig();

  $('iframe.auto-height').iframeAutoHeight({minHeight: 500});

  //初始化前端页面菜单
  $.get(config.baseurl+'/api/menus/tree/front',function (data) {
    console.log(data);
    $(data).each(function (index) {
      var children_L1 = data[index].children;
      if(children_L1.length>0){
        var rootNav=$('<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" href="'+data[index].url+'">'+data[index].name+'<span class="caret"></span></a></li>');
        var dropdown_L1 = $('<ul class="dropdown-menu"></ul>');
        $(children_L1).each(function (index1) {
          console.log(children_L1[index1].name);
          var child_L1 = $('<li><a href="'+children_L1[index1].url+'">'+children_L1[index1].name+'</a></li>')
          dropdown_L1.append(child_L1);
        });
        $(rootNav).append(dropdown_L1);
      }else {
        var rootNav=$('<li><a href="'+data[index].url+'">'+data[index].name+'</a></li>');
      }
      $("#frontNav").append(rootNav);

    });
  });

};


function isIE() { //ie?
  if (!!window.ActiveXObject || "ActiveXObject" in window) {
    return true;
  } else {
    return false;
  }
}

function getBrowserVersion() {
  var userAgent = navigator.userAgent.toLowerCase();
  if(userAgent.match(/msie ([\d.]+)/)!=null){//ie6--ie9
    var uaMatch = userAgent.match(/msie ([\d.]+)/);
    return 'IE'+uaMatch[1];
  } else if (userAgent.match(/(trident)\/([\w.]+)/)) {
    var uaMatch = userAgent.match(/trident\/([\w.]+)/);
    switch (uaMatch[1]){
      case "4.0": return "IE8" ;break;
      case "5.0": return "IE9" ;break;
      case "6.0": return "IE10";break;
      case "7.0": return "IE11";break;
      default:return "undefined" ;
    }
  }
  return "undefined";
}
