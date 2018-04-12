var personId;
$.ajax({
    url:"http://"+window.location.host+"/api/logonUser",
    type:"GET",
    async:false,
    success:function (data) {
        personId=70;
        console.log(data);
    }
});
function Close() {
    //关闭播放列表
    $(".video_opera").css("display","none");
    $("#a1").css("width","calc("+100+"% - "+10+"px)");
    $("#a2").css("display","none");
    $("#a3").css("display","block");
}
function Open() {
    //打开播放列表
    $(".video_opera").css("display","block");
    $("#a1").css("width","");
    $("#a2").css("display","block");
    $("#a3").css("display","none");
}

var nowId=1;
var posTime=0;
function loadedHandler() {
    if (CKobject.getObjectById('ckplayer_a1').getType()) {
        addPlayListener();
    }
    else {
        addPlayListener();
    }
}
function timeHandler(t) {
    if (t > -1) {
        posTime=t;
    }
}
function addPlayListener() {//增加播放监听
    if (CKobject.getObjectById('ckplayer_a1').getType()) {//说明使用html5播放器
        CKobject.getObjectById('ckplayer_a1').addListener('play', playHandler);
    }
    else {
        CKobject.getObjectById('ckplayer_a1').addListener('play', 'playHandler');
    }
}
function playHandler() {
    removePlayListener();
    CKobject.getObjectById('ckplayer_a1').videoSeek(getLastPosition(nowId));
    addTimeListener();
}
function removePlayListener() {//删除播放监听事件
    if (CKobject.getObjectById('ckplayer_a1').getType()) {//说明使用html5播放器
        CKobject.getObjectById('ckplayer_a1').removeListener('play', playHandler);
    }
    else {
        CKobject.getObjectById('ckplayer_a1').removeListener('play', 'playHandler');
    }
}
function addTimeListener() {//增加时间监听
    if (CKobject.getObjectById('ckplayer_a1').getType()) {//说明使用html5播放器
        CKobject.getObjectById('ckplayer_a1').addListener('time', timeHandler);
    }
    else {
        CKobject.getObjectById('ckplayer_a1').addListener('time', 'timeHandler');
    }
}
//保存lastposition
function saveRecord(id, t)
{
    $.ajax({
        url: "http://"+window.location.host+"/api/video/saveRecord",
        type: "POST",
        async:false,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify([{
            "createdBy": 70,
            "lastPosition": t,
            "vId": id
        }]),
        success:function () {
            console.log(id,t);
        }
    });
}
//获取lastposition
function getLastPosition(id)
{
    $.ajax({
        url: "http://"+window.location.host+"/api/video/getLastPosition?ID=" + id + "&personID="+personId,
        type: "GET",
        async:false,
        success: function(data) {
            if(data.length>0){
                posTime=data[0].lastPosition;
            }else{
                posTime=0;
            }
        }
    });
    console.log(posTime);
    return posTime;
}

function getParam(paramName) {
    paramValue = "", isFound = !1;
    if(this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=") > 1) {
        arrSource = unescape(this.location.search).substring(1, this.location.search.length).split("&"), i = 0;
        while(i < arrSource.length && !isFound) arrSource[i].indexOf("=") > 0 && arrSource[i].split("=")[0].toLowerCase() == paramName.toLowerCase() && (paramValue = arrSource[i].split("=")[1], isFound = !0), i++
    }
    return paramValue == "" && (paramValue = null), paramValue
}

//渲染方法
function ergodic(data){
    for(var j=0;j<data.length;j++){
        data[j].fav=false;
    }
    $.ajax({
        url:"http://"+window.location.host+"/api/video/myFavorite?personID="+personId,
        type:"GET",
        success:function (res) {
            for(var i=0;i<res.length;i++){
                for(var j=0;j<data.length;j++){
                    if(res[i].id==data[j].id){
                        data[j].fav=true;
                    }
                }
            }
            console.log(data);
            new Vue({
                el:"#video_list",
                data:{
                    items:data,
                    nowAct:0,
                    iscur:0,
                    favList:[]
                },
                ready:function () {//页面渲染完毕后
                    //打分插件
                    for(var i=0;i<this.items.length;i++){
                        $(".raty_con"+i).raty({
                            target: ".target",
                            targetType: 'hint',
                            hints:[1,2,3,4,5]
                        });
                    }
                    if(this.items.length>0){
                        var prevId=this.items[this.iscur].id;
                        $(window).on('beforeunload', function(){
                            saveRecord(prevId, posTime);
                        });
                        nowId=this.items[0].id;
                        var flashvars = {
                            f: "http://192.168.27.55"+this.items[0].url,
                            c: 0,
                            p: 1,
                            b: 0,
                            loaded: 'loadedHandler'
                        };
                        CKobject.embed('./ckplayer/ckplayer.swf', 'a1', 'ckplayer_a1', '100%', '100%', false, flashvars);
                    }
                },
                methods:{
                    checkVideo:function (index) {//切换视频
                        var prevId=this.items[this.iscur].id;
                        $(window).on('beforeunload', function() {
                            saveRecord(prevId, posTime);
                        });
                        if(posTime>0){
                            saveRecord(prevId, posTime);
                        }
                        posTime=0;
                        this.iscur=index;
                        nowId=this.items[this.iscur].id;
                        if(index!=this.nowAct){
                            this.nowAct=index;
                            var flashvars = {
                                f: "http://192.168.27.55"+this.items[index].url,
                                c: 0,
                                p: 1,
                                b: 0,
                                loaded: 'loadedHandler'
                            };
                            CKobject.embed('./ckplayer/ckplayer.swf', 'a1', 'ckplayer_a1', '100%', '100%', false, flashvars);
                        }
                    },
                    favClick:function (index) {//点击收藏
                        if(this.items[index].fav){
                            $.ajax({
                                type:"POST",
                                url:"http://"+window.location.host+"/api/video/delete",
                                contentType: "application/json; charset=utf-8",
                                async: false,
                                data:JSON.stringify({"rows": [{"id": this.items[index].fId}],"type": "Favorite"})
                            });
                        }else{
                            $.ajax({
                                type:"POST",
                                url:"http://"+window.location.host+"/api/video/saveFavorite",
                                contentType: "application/json; charset=utf-8",
                                async: false,
                                data:JSON.stringify([{"createdBy": 70, "vId":this.items[index].id}])
                            });
                        }
                        this.items[index].fav=!this.items[index].fav;
                    }
                }
            });
        }
    });
}

//判断入口
var viewType=getParam("type");
if(viewType=="vItem"){
    var videoId=getParam("ID");
    $.ajax({
        url:"http://"+window.location.host+"/api/video/allVideo?page=1&rows=1000&perId=70&ID="+videoId,
        type:"GET",
        success:function (data) {
            ergodic(data.rows);
        }
    });
}else if(viewType=="search"){
    var queryTerm=getParam("queryTerm");
    $.ajax({
        url:"http://"+window.location.host+"/api/video/searchVideo?page=1&rows=1000&queryTerm="+queryTerm+"&perId="+personId,
        type:"GET",
        success:function (data) {
            ergodic(data.rows);
        }
    });
}else if(viewType=="myFavorite"){
    $(".favorite").addClass("active");
    $.ajax({
        url:"http://"+window.location.host+"/api/video/myFavorite?personID="+personId,
        type:"GET",
        success:function (data) {
            ergodic(data);
        }
    });
}else{
    $(".myRecord").addClass("active");
    $.ajax({
        url:"http://"+window.location.host+"/api/video/myRecord?personID="+personId,
        type:"GET",
        success:function (data) {
            ergodic(data);
        }
    });
}