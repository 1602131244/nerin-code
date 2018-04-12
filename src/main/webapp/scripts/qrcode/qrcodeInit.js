/**
 * Created by Administrator on 2017/7/27.
 */
$(function () {
    var text = $.query.get("content");
    jQueryInit(text);

    function jQueryInit(val){
        //接收参数并判断
        jQuery('#output').qrcode({
            render: 'canvas',
            text: utf16to8(val),
            height: 200,
            width: 200,
            typeNumber: -1,         //计算模式
            correctLevel: QRErrorCorrectLevel.M,//纠错等级
            background: "#ffffff",  //背景颜色
            foreground: "#000000",  //前景颜色
            //logo图片地址
//		src: 'D:\logo.png'
        });

//	$("#qrCodeIco").attr<'src','/pages/qrcode/logo.png'>
        var margin = ($("#output").height() - $("#qrCodeIco").height())/2 - 7.5
//			<!- //控制Logo图标的位置-->
        $("#qrCodeIco").css("margin", margin);
    };

//中文编码格式转换
    function utf16to8(str) {
        var out, i, len, c;
        out = "";
        len = str.length;
        for (i = 0; i < len; i++) {
            c = str.charCodeAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                out += str.charAt(i);
            } else if (c > 0x07FF) {
                out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
                out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
                out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
            } else {
                out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
                out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
            }
        }
        return out;
    };
});