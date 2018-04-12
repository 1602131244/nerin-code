/**
 * Created by 104528 on 2017/9/26.
 */
$(function(){
    var config = new nerinJsConfig();

    $.get(config.baseurl+'/api/isLogon',function (data) {
        if (!data)
            window.location.href = "/index.html?doWork=jumptobbs&url=" + encodeURIComponent($.query.get("url"));
    });

    $.get('/api/bbsLogin/getLoginScript', function (data) {
        if (data['script'] != '') {
            var ucSynLogin = $(data['script']).attr('src');
            $.ajax({
                type: 'GET',
                url: ucSynLogin,
                dataType: 'jsonp',
                xhrFields: {
                    withCredentials: true
                },
                complete: function(){
                    var defUrl = 'http://192.168.15.71/bbs';
                    var getUrl = $.query.get("url");
                    var bbs = document.createElement('iframe');
                    if(typeof getUrl == 'string' && getUrl != ''){
                        // window.location.href = getUrl;
                        bbs.src=getUrl;
                    }else{
                        // window.location.href = defUrl;
                        bbs.src=defUrl;
                    }
                    bbs.width=window.screen.availWidth;
                    bbs.height=window.screen.availHeight;
                    bbs.frameBorder=0;
                    bbs.marginheight=0;
                    bbs.marginwidth=0;
                    document.body.appendChild(bbs);
                    $("body div").remove();
                    $('body').css("margin","0px");
                }
            });
            // $('body').append(data['script']);
            // win = window.open(ucSynLogin);
            // win.location.href = 'http://192.168.15.71/bbs/';
        } else {
            message = "自动登录失败，请尝试手动登录BBS论坛。";
            $('body div:last').after('<div>' + message + '</div>');
        }
    });
}());
