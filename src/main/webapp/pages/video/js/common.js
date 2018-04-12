/**
 * Created by 104523 on 2017/8/22.
 */
$(document).ready(function () {
    //头部动效插件
    new WOW().init();
    //登陆页input点击效果
    $(".main_login .txt_input").each(function(){
        $(this).click(function(e){
            e.stopPropagation();
            $(".txt_input").removeClass("active");
            $(this).addClass("active");
        });
    });
    $(document).click(function(e){
        e.stopPropagation();
        $(".main_login .txt_input").removeClass("active");
    });
    /*点击搜索按钮*/
    $(".search_ipt>input").keydown(function (e) {
        if(e.keyCode==13){
            $("#search").trigger("click");
        }
    });
    $("#search").click(function () {
        var queryTerm=$(this).prev("input[type=text]").val();
        var href=$(this).attr("href");
        if(queryTerm!=""){
            $.ajax({
                url:"http://localhost:8080/api/video/searchVideo?page=1&rows=1000&queryTerm="+queryTerm+"&perId=70",
                type:"GET",
                success:function (data) {
                    if(data.rows.length>0){
                        window.location.href= href+"&queryTerm="+encodeURI(encodeURI(queryTerm));
                    }else {
                        alert("查无数据");
                    }
                }
            });
        }else {
            alert("请输入搜索内容");
        }
        return false;
    });

    //首页渲染
    if($("#app").length>0){
        $.ajax({
            url:"http://localhost:8080/api/video/getNode?ID=0",
            type:"GET",
            success:function (data) {
                var itemsList=[];
                for(var i=0;i<data.length;i++){
                    $.ajax({
                        url:"http://localhost:8080/api/video/getNode?ID="+data[i].id,
                        type:"GET",
                        async: false,
                        success:function (data) {
                            itemsList.push(data);
                        }
                    })
                };
                new Vue({
                    el:'#app',
                    data:{
                        items:data,
                        itemsText:["中国瑞林工程技术有限公司（简称中国瑞林、英文简称Nerin）是由南昌有色冶金设计研究院通过改制，按照股权多元化现代企业制度由南昌有色冶金设计研究院管理技术骨干、中国有色金属建设股份有限公司、江西铜业集团、中国中钢股份有限公司共同出资组建的国际化工程公司。","中国瑞林一体化智能管理信息系统建设项目","全球工程师、建筑师、施工人员和业主运营商都在使用 Bentley 软件解决方案加快基础设施项目交付并提升资产性能，这些基础设施为我们的经济和环境可持续发展提供强大助力。我们携手并进，共促基础设施进步。","建筑信息模型（Building Information Modeling）具有信息完备性、信息关联性、信息一致性、可视化、协调性、模拟性、优化性和可出图性八大特点。将建设单位、设计单位、施工单位、监理单位等项目参与方在同一平台上，共享同一建筑信息模型。利于项目可视化、精细化建造。","回首过去，我们瑞林人思绪纷飞，感慨万千；立足今日，我们瑞林人胸有成竹，信心百倍；展望未来，我们瑞林人引吭高歌，一路欢笑。","非学无以成才，非志无以成学。"],
                        itemsImg:["images/nerin.jpg","images/nims1.png","images/Bentley.jpg","images/bim.jpg","images/springFestival1.png","images/training1.jpg"],
                        itemsList:itemsList
                    },
                    ready: function () {
                        for(var i = 0; i < this.items.length; i++) {
                            new Swiper('.swiper-container'+this.items[i].id, {
                                slidesPerView: 'auto',
                                prevButton: '.swiper-button-prev' + this.items[i].id,
                                nextButton: '.swiper-button-next' + this.items[i].id
                            });
                        };
                    }
                });
            }
        });
    }
});