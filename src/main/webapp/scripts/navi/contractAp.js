var conap_headerId;
var conap_kheaderId;
var conap_version;
var conap_version_max;
var conap_checktype;//调整，查询类别区分
var dataSet = [];
var linestyle = [];//费用项下拉列表数据源
var linestylefun;
var apMilestone = [];//里程碑下拉
var apMilestonefun;
var t2;//datatable
var rcvType = [];//款项性质数据源
var rcvTypefun;//款项性质生成方法
var line_num;//排序
var ap_contotal; //合同总额

//只输入数字
function checknum(e) {
    var isOk= false;
    var key=e.keyCode;
    console.log(key);
    if ((key > 95 && key < 106) || //小键盘上的0到9
        (key > 47 && key < 60) || //大键盘上的0到9
        (key == 110 && value.indexOf(".") < 0) || //小键盘上的.而且以前没有输入.
        (key == 190 && value.indexOf(".") < 0) || //大键盘上的.而且以前没有输入.
        key == 8 || key == 9 || key == 46 || key == 37 || key == 39 //不影响正常编辑键的使用(8:BackSpace;9:Tab;46:Delete;37:Left;39:Right)
    ) {
        isOk = true;
    }

}
//应收百分比和应收金额互算
function completenum(d) {

    if(d==1){//输入百分比
        var l_amount=0;
        var l_plan=0;
        var l_pa=$("input[name='planapAmount']");
        var l_ap=$("input[name='apPlan']");
        for( i=0;i<t2.data().length-1;i++){
            $(l_pa[i]).val($(l_ap[i]).val()*ap_contotal/100);//百分比算金额
            l_plan=l_plan*1+$(l_ap[i]).val()*1;

        }
        l_amount=l_plan*1*ap_contotal/100;
        $("table[id=dataTables-ap-line] tr").eq(t2.data().length).find("td").eq(6).html(l_plan);
        $("table[id=dataTables-ap-line] tr").eq(t2.data().length).find("td").eq(7).html(l_amount);
        dataSet[dataSet.length-1].apPlan=l_plan;
        dataSet[dataSet.length-1].planapAmount=l_amount;
        //console.log(dataSet[dataSet.length-1].planapAmount);
    }else if(d==2){
        var l_amount=0;
        var l_plan=0;
        var l_pa=$("input[name='planapAmount']");
        var l_ap=$("input[name='apPlan']");
        for( i=0;i<t2.data().length-1;i++){
            $(l_ap[i]).val(($(l_pa[i]).val()*100/ap_contotal).toFixed(3));//金额算百分比
            l_amount=l_amount*1+$(l_pa[i]).val()*1;

        }
        l_plan=(l_amount*100/ap_contotal).toFixed(3);
        $("table[id=dataTables-ap-line] tr").eq(t2.data().length).find("td").eq(6).html(l_plan);
        $("table[id=dataTables-ap-line] tr").eq(t2.data().length).find("td").eq(7).html(l_amount);
        dataSet[dataSet.length-1].apPlan=l_plan;
        dataSet[dataSet.length-1].planapAmount=l_amount;
    }
}
 $(function () {
    // var datetim=$("div[name='datetim']");
    // for(i=0;i<t2.data().length-1;i++){
    //     datetim[i].datetimepicker({
    //         format: 'YYYY-MM-DD'
    //     });
    // }
    // $("div[name='datetim']").datetimepicker({
    //     format: 'YYYY-MM-DD'
    //
    // });


    $('#bu_apadj').attr('disabled', true);
    $('#bu_apadd').attr('disabled', true);
    $('#bu_apdel').attr('disabled', true);
    $('#bu_apsave').attr('disabled', true);
    $('#bu_apsubmit').attr('disabled', true);
    //生成款项性质下拉表数据源
    $.getJSON(config.baseurl + "/api/contract/rcvtype", function (data) {
        rcvType = data.dataSource;
    });
    rcvTypefun = function (val) {
        var select = $("<select name='conap_rcvtype' class='form-control' placeholder='请选择'></select>");
        select.append("<option value='-1'>请选择</option>");
        for (var i = 0; i < rcvType.length; i++) {
            select.append("<option value='" + rcvType[i].typeCode + "'>" + rcvType[i].typeName + "</option>")
        }
        select.val(val);
        return select;
    }
     $.ajax({     //异步，调用后台get方法
         url: config.baseurl + "/api/contract/checkContract", //调用地址，不包含参数
         contentType: "application/json",
         dataType: "json",
         async: true,
         data: {
             projectId: l_projectId  //传入参数，依次填写
         },
         type: "GET",  //调用方法类型
         beforeSend: function () {

         },
         success: function (data) {
             if(data==[]){
                 window.parent.error("该项目没有关联主合同！");
                 conap_checktype=0;
                 return false;
             }else{
                 getcontractNum();
             }
         },
         complete: function () {
         },
         error: function () {
             window.parent.error(ajaxError_sys);
         }
     });

//获取合同编码
     function getcontractNum() {
         $.ajax({     //异步，调用后台get方法
             url: config.baseurl + "/api/contract/number", //调用地址，不包含参数
             contentType: "application/json",
             dataType: "json",
             async: true,
             data: {
                 projectId: l_projectId  //传入参数，依次填写
             },
             type: "GET",  //调用方法类型
             beforeSend: function () {

             },
             success: function (data) {
                 var list = data.dataSource;
                 var l_num = 1;
                 list.forEach(function (data) {  //创建合同按钮组
                     if (l_num == 1) {
                         $('#button1').append("<label class='btn btn-default btn-primary2 active' name='kheadername' > <input type='radio' name='button_con'  value='" + data.kHeaderId + "' >" + data.cognomen + "</label>");
                         conap_kheaderId = data.kHeaderId;
                         l_num = 2;
                     } else {
                         $('#button1').append("<label class='btn btn-default btn-primary2' name='kheadername' > <input type='radio' name='button_con'  value='" + data.kHeaderId + "' >" + data.cognomen + "</label>");
                     }
                 });

                 getConapVersion();//初始化加载版本
                 getConaplinestyle();//费用项下拉框
                 getApMilestone();
                 getapcash();
                 getaptrx();
                 //获取选中的合同ID，当点击合同编号的时候，触发生成版本，及费用项下拉框
                 $('label[name="kheadername"]').on('click', function () {
                     var _index = $('label[name="kheadername"]').index($(this));
                     conap_kheaderId = $("input[name='button_con']")[_index].value;
                     //清除数据
                     dataSet = [];
                     createtable();

                     $('#bu_apadj').attr('disabled', true);
                     $('#bu_apadd').attr('disabled', true);
                     $('#bu_apdel').attr('disabled', true);
                     $('#bu_apsave').attr('disabled', true);
                     $('#bu_apsubmit').attr('disabled', true);

                     getConapVersion();//生成版本下拉框
                     getConaplinestyle();//费用项下拉框
                     getApMilestone();//里程碑下拉框
                     getapcash();
                     getaptrx();
                 });
                 createtable();
             },
             complete: function () {
             },
             error: function () {
                 window.parent.error(ajaxError_sys);
             }
         });

     }

//生成版本下拉列表
    function getConapVersion() {
        $('#s_conapPlan_version').empty();//清空下拉表
        $.ajax({     //异步，调用后台get方法
            url: config.baseurl + "/api/contract/version", //调用地址，不包含参数
            contentType: "application/json",
            dataType: "json",
            async: true,
            data: {
                kheaderId: conap_kheaderId  //传入参数，依次填写
            },
            type: "GET",  //调用方法类型
            beforeSend: function () {
            },
            success: function (data) {
                var list1 = data.dataSource;

                list1.forEach(function (data) {  //取最大版本
                    conap_version = data.version;
                    conap_version_max = data.version;
                });

                list1.forEach(function (data) {  //下拉列表，如果是最大版本，则默认选中
                    if (data.version == conap_version_max) {
                        $('#s_conapPlan_version').append("<option value='" + data.version + "'selected>" + data.version + "</option>");

                    } else {
                        $('#s_conapPlan_version').append("<option value='" + data.version + "'>" + data.version + "</option>");
                    }
                    ;


                });
            },
            complete: function () {
            },
            error: function () {
                window.parent.error(ajaxError_sys);
            }
        });
    }

//生成费用项下拉列表数据源
    function getConaplinestyle() {
        $.ajax({     //异步，调用后台get方法
            url: config.baseurl + "/api/contract/linestyle", //调用地址，不包含参数
            contentType: "application/json",
            dataType: "json",
            async: true,
            data: {
                kheaderId: conap_kheaderId  //传入参数，依次填写
            },
            type: "GET",  //调用方法类型
            beforeSend: function () {
            },
            success: function (data) {
                linestyle = data.dataSource;//数据源赋值
                linestylefun = function (val) {
                    var select = $("<select name='conap_linestyle' class='form-control' placeholder='请选择'></select>");
                    select.append("<option value='-1'>请选择</option>");
                    for (var i = 0; i < linestyle.length; i++) {
                        select.append("<option value='" + linestyle[i].lseId + "'>" + linestyle[i].name + "</option>")
                    }
                    select.val(val);
                    return select;
                }
            },
            complete: function () {
            },
            error: function () {
                window.parent.error(ajaxError_sys);
            }
        });
    }

//生成里程碑下拉列表数据源
    function getApMilestone() {
        $.ajax({     //异步，调用后台get方法
            url: config.baseurl + "/api/contract/milestone", //调用地址，不包含参数
            contentType: "application/json",
            dataType: "json",
            async: true,
            data: {
                projectId: l_projectId  //传入参数，依次填写
            },
            type: "GET",  //调用方法类型
            beforeSend: function () {
            },
            success: function (data) {
                apMilestone = data.dataSource;//数据源赋值
                apMilestonefun = function (val) {
                    var select = $("<select name='conap_milestone' class='form-control' placeholder='请选择'></select>");
                    select.append("<option value=''>请选择</option>");
                    for (var i = 0; i < apMilestone.length; i++) {
                        select.append("<option value='" + apMilestone[i].lineId + "'>" + apMilestone[i].name + "</option>")
                    }
                    select.val(val);
                    return select;
                }
            },
            complete: function () {
            },
            error: function () {
                window.parent.error(ajaxError_sys);
            }
        });
    }
    $("input[name='colll']").bind('input propertychange', function() {
        $("input[name='colll']").value;

            $("input[name='collll']").val( $("input[name='colll']").val());

    });

//加载数据，返回头信息，控制按钮
    function getconapHead() {
        $.ajax({
            url: config.baseurl + "/api/contract/head",
            data: {
                kheaderId: conap_kheaderId,
                version: conap_version
            },
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",
            type: "GET",
            beforeSend: function () {
            },
            success: function (data) {
                var datas = data.dataSource;
                conap_headerId = datas[0].headerId;
                $('#conId').val(datas[0].contractNumber);
                $('#contotal').val(datas[0].total + " " + datas[0].currencyCode);
                ap_contotal = datas[0].total;
                $('#conStatus').val(datas[0].status);
                $('#conflag').val(datas[0].amount_flag);
                if (conap_version == conap_version_max && $('#conStatus')[0].value != '审批中' && conap_checktype == 1) { //如果查询的是最大版本，且状态不是审批中，且是查询时则按钮亮
                    $('#bu_apadj').attr('disabled', false);
                    $('#bu_apadd').attr('disabled', true);
                    $('#bu_apdel').attr('disabled', true);
                    $('#bu_apsave').attr('disabled', true);
                    $('#bu_apsubmit').attr('disabled', true);
                }
                queryconline();
            },

            complete: function () {
            },
            error: function () {
                window.parent.error("重新加载数据出错1111！");
                $('#add_modal').on('shown.bs.modal', function (e) {
                }).modal('toggle');
            }
        });
    }

//加载数据，返回行信息
    function queryconline() {
        $.ajax({
            url: config.baseurl + "/api/contract/line",
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",   //返回格式为json
            async: true,//请求是否异步，默认为异步，这也是ajax重要特性
            data: {
                headerId: conap_headerId,
                projectId: l_projectId
            },
            type: "GET",   //请求方式
            beforeSend: function () {
                window.parent.showLoading();
            },
            success: function (data) {
                dataSet = data.dataSource;
                console.log(dataSet);
                dataSet.forEach(function (data) {
                    if (data.lineNumber != 1000) {
                        line_num = data.lineNumber;
                    }
                });

                createtable();

            },
            complete: function () {
                window.parent.closeLoading();
            },
            error: function () {
                window.parent.showLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    function createtable() {
        var datecol;
        datecol="<span class='input-group-addon' style='padding: 3px 8px; font-size: 13px;'><span class='glyphicon glyphicon-calendar'></span></span>"
        $('#dataTables-conLine').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-ap-line"></table>');
        t2 = $('#dataTables-ap-line').DataTable({
            "processing": true,
            "data": dataSet,
            "bPaginate": false,
            "scrollX": true,
            "scrollY": 200,
            "pagingType": "simple", //简单模式，只有上下页
            "searching": false,//去掉搜索
            "ordering": false,
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {
                    "title": "<input type='checkbox' name='ap_listAll' id='ap_listAll' onclick='javascript:setCheckAll();' />",
                    "data": null
                },
                {"title": "序号", "data": "lineNumber"},
                {"title": "费用项", "data": "lineStyle"},
                {"title": "费用项说明", "data": "styleDesc"},
                {"title": "款项性质", "data": "rcvTypeMeaning"},
                {"title": "预计收款日期", "data": "planapDate"},
                {"title": "应收百分比", "data": "apPlan"},
                {"title": "应收金额", "data": "planapAmount"},
                {"title": "是否是收入确认节点", "data": "milestoneFlag"},
                {"title": "关联里程碑", "data": "name"},
                {"title": "里程碑完成情况", "data": "milestoneStatus"}
            ],
            "columnDefs": [{
                "searchable": false,
                "orderable": false,
                "targets": 1,
                "width": 25,
                "mRender": function (data, type, full) {
                    if (data == 1000) {
                        return "汇总";
                    } else {
                        return data;
                    }
                }
            }, {
                "searchable": false,
                "width": 15,
                "targets": 0//check框
                // "mRender": function (data, type, full) {
                //    if (full.lineNumber!=1000)
                //         return "<input type='checkbox' name='ap_list' id='ap_list'  />";
                //}
            }, {
                "searchable": false,
                "width": 70,
                "targets": 2//费用项 下拉列表 linestyle
            }, {
                "width": 150,
                "targets": 3//费用项说明
            }, {
                "width": 150,
                "targets": 4 //款项性质，下拉列表 rcvType
            }, {
                "width": 100,
                "targets": 5 //接收时间
            }, {
                "width": 70,
                "targets": 6
            }, {
                "width": 100,
                "targets": 7
            }, {
                "width": 100,
                "targets": 8,//是否是收入确认节点
                "mRender": function (data, type, full) {
                    if (data == 'Y') {
                        return "<input type='checkbox' name='ap_checked' id='ap_checked' checked='checked' disabled='disabled'/>";
                    } else {
                        return null;
                    }
                }
            }, {
                "width": 130,
                "targets": 9
            }, {
                "width": 100,
                "targets": 10
            }
            ],
            // "order": [[1, 'asc']],
            "createdRow": function (row, data, dataIndex) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
                $(row).children('td').eq(0).html("<input type='checkbox' name='ap_list' id='ap_list'  />");
                if (data.lineNumber == 1000)
                    $(row).children('td').eq(0).html("");
                if (conap_checktype == 2 && data.lineNumber != 1000) {//调整
                    var l_style = null != data.styleDesc ? data.styleDesc : "";
                    var planapDate = null != data.planapDate ? data.planapDate : "";
                    $(row).children('td').eq(2).html(linestylefun(data.lseId));//费用项
                    $(row).children('td').eq(3).html("<input type='text' id='styleDesc' name='styleDesc' style='margin-left: 5px; display: inline-block;' value='" + l_style + "'/>");
                    $(row).children('td').eq(4).html(rcvTypefun(data.rcvType));
                    $(row).children('td').eq(5).html("<div class='input-group date' name='datetim'><input type='text' class='form-control input-sm'  id='planapDate' name='planapDate' value='" + planapDate + "'/>"+datecol+"</div>");
                    $(row).children('td').eq(6).html("<input type='text' id='apPlan' name='apPlan' onkeyup='completenum(1)'  style='margin-left: 5px; display: inline-block;' value='" + data.apPlan + "'/>");
                    $(row).children('td').eq(7).html("<input type='text' id='planapAmount' name='planapAmount' onkeyup='completenum(2)' style='margin-left: 5px; display: inline-block;' value='" + data.planapAmount + "'/>");
                    //是否是确认节点，---
                    if (data.milestoneFlag == 'Y') {
                        $(row).children('td').eq(8).html("<input type='checkbox' name='ap_checked' id='ap_checked' checked='checked'/>");
                    } else {
                        $(row).children('td').eq(8).html("<input type='checkbox' name='ap_checked' id='ap_checked'/>");
                    }

                    $(row).children('td').eq(9).html(apMilestonefun(data.milestoneId));
                    $(row).children('td').eq(10).text(null != data.milestoneStatus ? data.milestoneStatus : "");
                    //关联里程碑

                }
            },
            "order": [[1, 'asc']],
            "initComplete": function () { //去掉标准表格多余部分
                $('#dataTables-ap-line_length').remove();
                $('#dataTables-ap-line_info').remove();
                $('#dataTables-ap-line_previous').remove();
                $('#dataTables-ap-line_next').remove();
                $("div[name=datetim]").datetimepicker({
                    format: 'YYYY-MM-DD'
                });
            }

        });
        $('#dataTables-ap-line').on('click', 'tr', function () {
            if ($(this).hasClass('dt-select')) {
                $(this).removeClass('dt-select');
            }
            else {
                $('#dataTables-ap-line').DataTable().$('tr.dt-select').removeClass('dt-select');
                $(this).addClass('dt-select');
            }

        });
        // if(conap_checktype==2){
        //     var datetim=$("div[name='datetim']");
        //     for(i=0;i<t2.data().length-1;i++){
        //         datetim[i].datetimepicker({
        //             format: 'YYYY-MM-DD'
        //         });
        //     }
        // }

    }

//查询按钮
    $('#bu_queryConapPlanVersion').on('click', function () {
        if(conap_checktype==2){
        if(confirm("请确认已保存调整计划")) {
            conap_checktype = 1;//更改显示类型
            conap_version = $("#s_conapPlan_version option:selected").val();
            getconapHead();

        }}else if(conap_checktype==0){
            window.parent.error("该项目没有关联主合同！");
            conap_checktype=0;
            return false;
        }else{
            conap_checktype = 1;//更改显示类型
            conap_version = $("#s_conapPlan_version option:selected").val();
            getconapHead();
        }
    });
//调整按钮
    $('#bu_apadj').on('click', function () {
        conap_checktype = 2;//更改显示类型
        $('#bu_apadj').attr('disabled', true);
        $('#bu_apadd').attr('disabled', false);
        $('#bu_apdel').attr('disabled', false);
        $('#bu_apsave').attr('disabled', false);
        $('#bu_apsubmit').attr('disabled', false);
        getconapHead();
    });
    var creater = {
        //生成一个行对象
        "createRowObj": function (num) {
            var lineNumber = num + 1;  //json ? json.templateId : "";
            var lineStyle = "";
            var styleDesc = "";
            var rcvTypeMeaning = "";
            var planapDate = "";
            var apPlan = "";
            var planapAmount = "";
            var milestoneFlag = "";
            var name = "";
            var milestoneStatus = "";
            return {
                "lineNumber": lineNumber,
                "lineStyle": lineStyle,
                "styleDesc": styleDesc,
                "rcvTypeMeaning": rcvTypeMeaning,
                "planapDate": planapDate,
                "apPlan": apPlan,
                "planapAmount": planapAmount,
                "milestoneFlag": milestoneFlag,
                "name": name,
                "milestoneStatus": milestoneStatus
            };
        }
    }
//新增
    $('#bu_apadd').on('click', function () {
        var oldnode = t2.data().length;
        var oldlast = t2.row([oldnode - 1]).data();//保存汇总行
        var num = t2.row([oldnode - 2]).data().lineNumber;
        t2.row([oldnode - 1]).remove();//删除汇总行
        t2.row.add(creater.createRowObj(num));//新增一行
        t2.row.add(oldlast);//增加汇总行
        t2.draw();
        //更新汇总行数据
        //需要重新导入汇总行的数据，

    })
    //删除
    $('#bu_apdel').on('click', function () {
       t2.rows( $("input[name='ap_list']:checked").parents('tr') ).remove();
        t2.draw();
    })

    //保存
    $('#bu_apsave').on('click', function () {
        var o;
        var arr = new Array();
        var lineStyle = $("select[name='conap_linestyle']");
        var styleDesc=$("input[name='styleDesc']");
        var rcvType=$("select[name='conap_rcvtype']");
        var planapDate=$("input[name='planapDate']");
        var apPlan=$("input[name='apPlan']");
        var planapAmount=$("input[name='planapAmount']");
        var milestoneFlag = $("input[name='ap_checked']");//是否是确认项
        var attribute11= $("select[name='conap_milestone']");//里程碑
        var l_applan=0;
        var l_planamount=0;
       //判断
        for( i=0;i<t2.data().length-1;i++){
            if(lineStyle[i].value=="-1"){
                window.parent.error("有未选择的费用项！");
                return false;
            }
            if(rcvType[i].value=="-1"){
                window.parent.error("有未选择的款项性质！");
                return false;
            }
            if(planapDate[i].value==""){
                window.parent.error("有未填写的预计收款日期！");
                return false;
            }
            if(apPlan[i].value==""){
                window.parent.error("有未填写的应收百分比！");
                return false;
            }
            if(planapAmount[i].value==""){
                window.parent.error("有未填写的应收金额！");
                return false;
            }
            if(milestoneFlag[i].checked){
                if(attribute11[i].value==""){
                    window.parent.error("已选择了收入确认节点但未关联里程碑！");
                    return false;
                }
            }
            l_applan=l_applan*1+apPlan[i].value*1;
            l_planamount=l_planamount*1+planapAmount[i].value*1;
        }
        console.log(l_applan);
         if(l_applan!=100){
             window.parent.error("应收百分比之和不为100！");
             return false;
         }


      for( i=0;i<t2.data().length-1;i++){

          o = new Object();
          o.headerId=conap_headerId;
          o.kheaderId=conap_kheaderId;
          if(i<dataSet.length-1){o.lineId=dataSet[i].lineId;} else {o.lineId="";}
          o.lineNumber=i+1;
          o.lseId=lineStyle[i].value;
        //  o.lineStyle=lineStyle
          // [i].html;后台查
          o.styleDesc=styleDesc[i].value;
          o.rcvType=rcvType[i].value;
          o.planapDate=planapDate[i].value;
          o.apPlan=apPlan[i].value;
          o.planapAmount=planapAmount[i].value;
          if(milestoneFlag[i].checked){
              o.attribute13="Y";
              o.attribute11=attribute11[i].value;
              o.attribute12=l_projectId;
          }else{
              o.attribute13="";
              o.attribute11=attribute11[i].value;
              o.attribute12=l_projectId;
          }


          arr.push(o);
         console.log(o);
        }
        console.log(JSON.stringify(arr));

        $.ajax({
            url: config.baseurl + "/api/contract/save",
            contentType: "application/json",
            dataType: "json",
            async: true,
            data: JSON.stringify(arr),
            type: "POST",
            beforeSend: function () {
            },
            success: function (d) {
                console.log(d);
                conap_headerId = d;
               if (d==conap_headerId)
                   window.parent.success("保存成功！");

            },
            complete: function () {
            },
            error: function () {
                window.parent.error(ajaxError_sys);
            }
        });
    })
     var data_Set_c=[];
    function getapcash() {
        $.ajax({     //异步，调用后台get方法
            url: config.baseurl + "/api/contract/cash", //调用地址，不包含参数
            contentType: "application/json",
            dataType: "json",
            async: true,
            data: {
                kheaderId:conap_kheaderId
            },
            type: "GET",  //调用方法类型
            beforeSend: function () {

            },
            success: function (data) {
                data_Set_c=data.dataSource;
                $('#apcashtotal').html(data.total);
                $('#apcashpercent').html(data.percent);
                createcash();
            },
            complete: function () {
            },
            error: function () {
                window.parent.error(ajaxError_sys);
            }
        });
    }
    var t_cash;
    function createcash() {
        $('#dataTables-apcash').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables_apcash"></table>');
        t_cash = $('#dataTables_apcash').DataTable({
            "processing": true,
            "data": data_Set_c,
            "pagingType": "simple", //简单模式，只有上下页
            "searching": false,
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "序号", "data": null},
                {"title": "收款编码", "data": "receiptNumber"},
                {"title": "收款时间", "data": "glDate"},
                {"title": "收款金额", "data": "recAmount"}

            ],
           
            "createdRow": function (row, data, dataIndex) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
            },
            "order": [[1, 'asc']],
            
        });

        t_cash.on('order.dt search.dt', function () {
            t_cash.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                cell.innerHTML = i + 1;
            });
        }).draw();
    }

    var data_Set_t=[];
    function getaptrx() {
        $.ajax({     //异步，调用后台get方法
            url: config.baseurl + "/api/contract/ratrx", //调用地址，不包含参数
            contentType: "application/json",
            dataType: "json",
            async: true,
            data: {
                kheaderId:conap_kheaderId
            },
            type: "GET",  //调用方法类型
            beforeSend: function () {

            },
            success: function (data) {
                data_Set_t=data.dataSource;
                $('#aptrxtotal').html(data.total);
                $('#aptrxpercent').html(data.percent);
                createtrx();
            },
            complete: function () {
            },
            error: function () {
                window.parent.error(ajaxError_sys);
            }
        });
    }
    var t_trx;
    function createtrx() {
        $('#dataTables-aptrx').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables_aptrx"></table>');
        t_trx = $('#dataTables_aptrx').DataTable({
            "processing": true,
            "data": data_Set_t,
            "pagingType": "simple", //简单模式，只有上下页
            "searching": false,
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "序号", "data": null},
                {"title": "发票编码", "data": "trxNumber"},
                {"title": "发票日期", "data": "trxDate"},
                {"title": "发票金额", "data": "amount"}

            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
            },
            "order": [[1, 'asc']],

        });

        t_trx.on('order.dt search.dt', function () {
            t_trx.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                cell.innerHTML = i + 1;
            });
        }).draw();
    }
})

