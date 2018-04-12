/**
 * Created by Administrator on 2017/6/22.
 */

var config = new nerinJsConfig();
var billEventData;

var contDataSet = [];
var billEventDataSet = [];

var billEventTable;

$(function () {
    console.log("page loaded");
    // 隐藏合同、项目下拉菜单
    $(".selectpicker").selectpicker('hide');

    //接收参数并判断
    var contractId = $.query.get("contractId");
    var projectId = $.query.get("projectId");

    //初始化table数据
    initContTables();
    initBillEventTables();

    //初始化模态框日期格式
    $('#datetimepicker1').datetimepicker({
        format: 'YYYY-MM-DD'
    });
    $('#datetimepicker2').datetimepicker({
        format: 'YYYY-MM-DD'
    });
    $('#datetimepicker3').datetimepicker({
        format: 'YYYY-MM-DD'
    });

    // 初始化dataTables程序
    var contTable;
    function initContTables() {
        $('#dataTables-listCont').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-contData"></table>');

        contTable = $('#dataTables-contData').DataTable({
            "processing": true,
            "data": contDataSet,
            "scrollX": true,
            "scrollY": "500px",
            "scrollCollapse": true,
            "pagingType": "full_numbers",
            "language": {
                "url": "/scripts/common/Chinese.json"
            },

            // searching : false, //去掉搜索框方法一：百度上的方法，但是我用这没管用
            // sDom : '"top"i',   //去掉搜索框方法二：这种方法可以
            // bLengthChange: false,   //去掉每页显示多少条数据方法

            "searching": false,
            "bLengthChange": false,
            "ordering ": false,
            // "paging": false,  //禁止表格分页
            "lengthMenu": [ [8,10, 25, 50, -1], [8,10, 25, 50, "All"] ],

            // {"title": "<input type='checkbox' name='ch_listAll' id='ch_listAll' onclick='javascript:setCheckAll();' />", "data": null},
            "columns": [
                {"title": "", "data": null},           //0
                {"title": "序号", "data": null ,"className": "dt_center"},      //1
                {"title": "合同编号", "data": "contNumber" , "className": "dt_center"},   //2
                {"title": "合同名称", "data": "contName" , "className": "dt_center"}, //3
                {"title": "合同</br>币别", "data": "curr" , "className": "dt_center"},  //4
                {"title": "合同总金额", "data": "amountDis" , "className": "dt_center"}, //5
                {"title": "开票客户", "data": "contCust" , "className": "dt_center"},//6
                {"title": "合同行信息</br>行号/行内容/项目编号/项目名称/合同行金额", "data": "lineProjects" , "className": "dt_center"},  //7
                {"title": "合同</br>状态", "data": "contStatus" , "className": "dt_center"}, //8
                {"title": "合同类型", "data": "contType" , "className": "dt_center"},  //9
                {"title": "公司", "data": "orgName" , "className": "dt_center"},  //10
                {"title": "合同头</br>项目编号", "data": "headerProjectNumber" , "className": "dt_center"},  //11
                {"title": "合同头</br>项目名称", "data": "headerPorjectName" , "className": "dt_center"},  //12
                {"title": "orgId", "data": "orgId"},  //13
                {"title": "contHeaderId", "data": "contHeaderId"}, //14
            ],
            "columnDefs": [{
                "searchable": false,
                "orderable": false,
                "targets": 0,
                "width": 5
            },{
                "searchable": false,
                "orderable": false,
                "targets": 1,
                "width": 20
            },{
                "orderable": false,
                "width": 100,
                "targets": 2
            },{
                "orderable": false,
                "width": 200,
                "targets": 3
            },{
                "orderable": false,
                "width": 26,
                "targets": 4
            },{
                "orderable": false,
                "width": 90,
                "targets": 5
                // "createdCell": function (td, cellData, rowData, row, col) {
                //     $(td).attr('style', 'text-align: right;')
                //     // $(td).css('color', 'red')  也可以写在创建行中
                // }
            },{
                "orderable": false,
                "width": 200,
                "targets": 6
            },{
                "orderable": false,
                "width": 400,
                "targets": 7
            },{
                "orderable": false,
                "width": 50,
                "targets": 8
            },{
                "orderable": false,
                "width": 100,
                "targets": 9
            },{
                "orderable": false,
                "width": 200,
                "targets": 10
            },{
                "orderable": false,
                "width": 100,
                "targets": 11
            },{
                "orderable": false,
                "width": 200,
                "targets": 12
            },{
                "width": 32,
                "targets": 13,
                "visible": false
            },{
                "width": 32,
                "targets": 14,
                "visible": false
            }
            ],
            "createdRow": function( row, data, dataIndex ) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
                $(row).children('td').eq(1).attr('style', 'text-align: center;');
                $(row).children('td').eq(2).attr('style', 'text-align: left;');
                $(row).children('td').eq(3).attr('style', 'text-align: left;');
                $(row).children('td').eq(4).attr('style', 'text-align: left;');
                $(row).children('td').eq(5).attr('style', 'text-align: right;');
                $(row).children('td').eq(6).attr('style', 'text-align: left;');
                $(row).children('td').eq(7).attr('style', 'text-align: left;');
                $(row).children('td').eq(8).attr('style', 'text-align: left;');
                $(row).children('td').eq(9).attr('style', 'text-align: left;');
                $(row).children('td').eq(10).attr('style', 'text-align: left;');
                $(row).children('td').eq(11).attr('style', 'text-align: left;');
                $(row).children('td').eq(12).attr('style', 'text-align: left;');

                // $(row).children('td').eq(6).attr('style', 'text-align: right;');
                // $(row).children('td').eq(6).html("<input type='text' id='amount' name='amount' class='form-control input-sm' style='width: 100%; text-align: right;' value='" + toThousands(data.amount) + "'/>");
            },
            "initComplete": function () {
                // $('#dataTables-data_length').insertBefore($('#dataTables-data_info'));
                // $('#dataTables-data_length').addClass("col-sm-4");
                // $('#dataTables-data_length').css("paddingLeft", "0px");
                // $('#dataTables-data_length').css("paddingTop", "5px");
                // $('#dataTables-data_length').css("maxWidth", "130px");

                // if (0 != contDataSet.length) {
                //     $('#dataTables-contData tr:eq(1)').trigger("click");
                // }

                if (1 == contDataSet.length){
                    $('#dataTables-contData tr:eq(1)').trigger("click");
                }else {
                    billEventDataSet =[];
                    initBillEventTables();
                    // $('#dataTables-InvoiceApplyData').DataTable().draw();
                }

            }
        });

        $('#dataTables-contData tbody').on( 'click', 'tr', function () {
            var table = $('#dataTables-contData').DataTable();
            if ( $(this).hasClass('selected') ) {  //当前记录为选中状态
                //取消选择按钮值
                var check = $(this).find("input[name='ch_list']");
                check.prop("checked", false);
                //取消选中
                $(this).removeClass('selected');
                $(this).attr('bgcolor',"#ffffff");
                //失效新增按钮
                $('#bu_add').attr('disabled',"disabled");
                //重绘开票记录表
                billEventDataSet =[];
                initBillEventTables();
                // $('#dataTables-InvoiceApplyData').DataTable().draw();
                $('#bu_copy').attr('disabled',"disabled");
                $('#bu_edit').attr('disabled',"disabled");
                $('#bu_del').attr('disabled',"disabled");
                $('#bu_submit').attr('disabled',"disabled");

                getContInvoices('N');
            }
            else {
                //取消选择按钮值
                var check = table.$('tr.selected').find("input[name='ch_list']");
                check.prop("checked", false);
                //取消原先选中行
                table.$('tr.selected').attr('bgcolor',"#ffffff");
                table.$('tr.selected').removeClass('selected');
                //选中行生效
                $(this).attr('bgcolor',"#E0FFFF");
                $(this).addClass('selected');
                var check = $(this).find("input[name='ch_list']");
                check.prop("checked", true);
                //启用新增按钮
                $('#bu_add').removeAttr('disabled');
                //重绘开票记录表
                var data =  table.rows($(this)).data();
                initInvoiceApply(data[0].contHeaderId);
                $('#bu_copy').attr('disabled',"disabled");
                $('#bu_edit').attr('disabled',"disabled");
                $('#bu_del').attr('disabled',"disabled");
                $('#bu_submit').attr('disabled',"disabled");

                getContInvoices('Y');
            }
        } );

        contTable.on( 'order.dt search.dt', function () {
            contTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = "<input type='checkbox' name='ch_list' />";
            } ),
            contTable.column(1, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } );
        } ).draw();
    }

    //-------------------------------------------筛选条件--------------------------------------------------------------
    // 点击合同选择按钮
    $('#b_searchCont').on('click', function (e) {
        $.ajax({
            url : config.baseurl +"/api/invoiceApply/contHeaderList",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                checkManager: "N"
            },
            type:"GET",
            beforeSend:function(){
            },
            success:function(data){
                initContSel(data.dataSource);
            },
            complete:function(){
            },
            error:function(){
                window.parent.error(ajaxError_loadData);
            }
        });
    });

    // 初始化合同下拉选择
    function initContSel(data){
        $("#sel_cont").empty();

        var initValue = "";
        var initText = "";
        // var initText = "请选择";
        $('#sel_cont').append("<option value='" + initValue + "'>" + initText + "," + initText+ "</option>");
        $(data).each(function(index){
            $("#sel_cont").append($('<option value="'+data[index].contHeaderId + '">'+data[index].contNumber + "," + data[index].contName+'</option>'));
        });

        $('#sel_cont').selectpicker('val',($('#contId').val()));  //默认值
        $("#sel_cont").selectpicker('refresh');
        $("#sel_cont").selectpicker('show');

        $('button[data-id="sel_cont"]').trigger("click");
    };

    //当合同下拉选择隐藏时
    $("#sel_cont").on('hidden.bs.select', function (e) {
        $('#contId').val($('#sel_cont').val().split(",")[0]);
        var sTest = $('#sel_cont option:selected').text();
        $('#contNumber').val(sTest.split(",")[1]);

        $("#sel_cont").selectpicker('hide');
    });

    // 点击项目选择按钮
    $('#b_searchProj').on('click', function (e) {
        $.ajax({
            url : config.baseurl +"/api/invoiceApply/projectList",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                checkProjectMember:'Y'
            },
            type:"GET",
            beforeSend:function(){
            },
            success:function(data){
                initProjSel(data.dataSource);
            },
            complete:function(){
            },
            error:function(){
                window.parent.error(ajaxError_loadData);
            }
        });
    });

    // 初始化项目下拉选择
    function initProjSel(data){
        $("#sel_proj").empty();

        var initValue = "";
        var initText = "";
        // var initText = "请选择";
        $('#sel_proj').append("<option value='" + initValue + "'>" + initText + "," + initText+ "</option>");
        $(data).each(function(index){
            $("#sel_proj").append($('<option value="'+data[index].projectId + '">'+data[index].projectNumber + "," + data[index].projectName+'</option>'));
        });

        $('#sel_proj').selectpicker('val',($('#projId').val()));  //默认值
        $("#sel_proj").selectpicker('refresh');
        $("#sel_proj").selectpicker('show');    //显示出来

        $('button[data-id="sel_proj"]').trigger("click");
    };

    //当项目下拉选择隐藏时
    $("#sel_proj").on('hidden.bs.select', function (e) {
        $('#projId').val($('#sel_proj').val().split(",")[0]);
        var sTest = $('#sel_proj option:selected').text();
        $('#projNumber').val(sTest.split(",")[1]);

        $("#sel_proj").selectpicker('hide');
    });

    //清空筛选条件
    $('#bu_resetQuery').on('click', function (e) {
        $('#contId').val('');
        $('#projId').val('');
    });

    //--------------------------------------合同查询按钮-------------------------------------------------------------------
    // 点击合同查询按钮
    $('#bu_queryContData').on('click', function (e) {
        $.ajax({
            url : config.baseurl +"/api/invoiceApply/contHeaderList",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                    contractId:       $('#contId').val(),
                    contractSearch:  $('#contSearch').val(),
                    projectId:        $('#projId').val(),
                    projectSearch:   $('#projSearch').val(),
                    projContSearch:   $('#projContSearch').val()
            },
            type:"GET",
            beforeSend:function(){
            },
            success:function(data){
                contDataSet = data.dataSource;
                console.log(contDataSet);
                //初始化合同记录表
                initContTables();
                //失效新增、打开按钮
                $('#bu_add').attr('disabled',"disabled");
                $('#bu_copy').attr('disabled',"disabled");
                $('#bu_edit').attr('disabled',"disabled");

                // if (0 == data.dataTotal){
                //      // $('#dataTables-contData tr:eq(0)').trigger("click");
                // }else {
                //     billEventDataSet =[];
                //     initBillEventTables();
                //     $('#dataTables-InvoiceApplyData').DataTable().draw();
                // }
            },
            complete:function(){
            },
            error:function(){
                window.parent.error(ajaxError_loadData);
            }
        });

        $('#contId').val("");
        $('#contSearch').val("");
        $('#projId').val("");
        $('#projSearch').val("");
    });

    //根据接收参数操作
    if((contractId != "" || null || undefined)|| (projectId != "" || null || undefined )){
        if (contractId != "" || null || undefined){
            $('#contId').val(contractId);

            // $.ajax({
            //     url : config.baseurl +"/api/invoiceApply/contHeaderList",
            //     contentType : 'application/x-www-form-urlencoded',
            //     dataType:"json",
            //     data: {
            //         contractId:contractId,
            //         checkManager: "N"
            //     },
            //     type:"GET",
            //     beforeSend:function(){
            //     },
            //     success:function(data){
            //         if (data.dataTotal == 1){
            //             $('#contId').val(data.dataSource[0].contHeaderId);
            //             $('#contNumber').val(data.dataSource[0].contNumber);
            //         }else{
            //             if (data.dataTotal == 0){
            //                 window.parent.error("合同ID：" + contractId + "未找到或者您无权查看！",1000);
            //             }
            //         }
            //     },
            //     complete:function(){
            //     },
            //     error:function(){
            //         window.parent.error(ajaxError_loadData);
            //     }
            // });
        }

        if (projectId != "" || null || undefined ){
            $('#projId').val(projectId);

            // $.ajax({
            //     url : config.baseurl +"/api/invoiceApply/projectList",
            //     contentType : 'application/x-www-form-urlencoded',
            //     dataType:"json",
            //     data: {
            //         projectId:projectId,
            //         checkProjectMember:'N'
            //     },
            //     type:"GET",
            //     beforeSend:function(){
            //     },
            //     success:function(data){
            //         if (data.dataTotal == 1){
            //             $('#projId').val(data.dataSource[0].projectId);
            //             $('#projNumber').val(data.dataSource[0].projectNumber);
            //         }else{
            //             if (data.dataTotal == 0){
            //                 window.parent.error("项目ID：" + projectId + "未找到或者您无权查看！",1000);
            //             }else {
            //                 window.parent.error("项目ID：" + projectId + "找到多条记录！",1000);
            //             }
            //         }
            //     },
            //     complete:function(){
            //     },
            //     error:function(){
            //         window.parent.error(ajaxError_loadData);
            //     }
            // });
        }

        // 模拟点击查询
        setTimeout(function(){
                $('#bu_queryContData').trigger("click");}
            ,500
        );
    }

    // 合同开票金额统计
    function getContInvoices(val) {
        if (val == 'Y'){
            var table = $('#dataTables-contData').DataTable();
            // var data = table.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
            // var data = table.$('tr.selected').data();
            var data = table.rows(table.$('tr.selected')).data();
            $.ajax({
                url : config.baseurl +"/api/invoiceApply/getContInvoices",
                contentType : 'application/x-www-form-urlencoded',
                dataType:"json",
                data: {
                    contId:       data[0].contHeaderId
                },
                type:"GET",
                beforeSend:function(){
                },
                success:function(data){
                    console.log(data.dataSource[0]);
                    $('#contInvoice').val(toThousandsh(data.dataSource[0].invoiceAmount));
                    $('#contInvoicePro').val(data.dataSource[0].invoicePro);
                    $('#contInvoiceWay').val(toThousandsh(data.dataSource[0].wayAmount));
                    $('#contInvoiceWayPro').val(data.dataSource[0].wayPro);
                },
                complete:function(){
                },
                error:function(){
                    window.parent.error(ajaxError_loadData);
                }
            });
        }else{
            $('#contInvoice').val('');
            $('#contInvoicePro').val('');
            $('#contInvoiceWay').val('');
            $('#contInvoiceWayPro').val('');
        }
    }

//格式化数字
    function toThousandsh(num) {
        var num = (num || 0).toString(), result = '';
        var bool = num.indexOf(".");
        //返回大于等于0的整数值，若不包含"Text"则返回"-1。
        if(bool>0){
            result = num.slice(bool - num.length)
            num = num.slice(0,bool);
            while (num.length > 3) {
                result = ',' + num.slice(-3) + result;
                num = num.slice(0, num.length - 3);
            }
            if (num) { result = num + result ; }
        }else{
            while (num.length > 3) {
                result = ',' + num.slice(-3) + result;
                num = num.slice(0, num.length - 3);
            }
            if (num) { result = num + result + '.00'; }
        }
        return result;
    };
});

function CheckvalLengthb(s)
{
    var n=0;
    for(var i=0;i<s.length;i++)
    {
        //charCodeAt()可以返回指定位置的unicode编码,这个返回值是0-65535之间的整数
        if(s.charCodeAt(i)<128)
        {
            n++;
        }
        else
        {
            n+=3;
        }
    }
    return (n);
}

// 初始化dataTables程序
function initBillEventTables() {
    $('#dataTables-listInvoiceApply').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-InvoiceApplyData"></table>');

    billEventTable = $('#dataTables-InvoiceApplyData').DataTable({
        "processing": true,
        "data": billEventDataSet,
        "scrollX": true,
        "scrollY": "500px",
        "scrollCollapse": true,
        "pagingType": "full_numbers",
        "language": {
            "url": "/scripts/common/Chinese.json"
        },

        // searching : false, //去掉搜索框方法一：百度上的方法，但是我用这没管用
        // sDom : '"top"i',   //去掉搜索框方法二：这种方法可以
        // bLengthChange: false,   //去掉每页显示多少条数据方法

        "searching": false,
        "bLengthChange": false,
        "ordering ": false,
        "paging": false,  //禁止表格分页

        "columns": [
            // {"title": "<input type='checkbox' name='ch_listAll' id='ch_listAll' onclick='javascript:setCheckAll();' />", "data": null},
            {"title": "", "data": null}, //0
            {"title": "序号", "data": null , "className": "dt_center"},
            {"title": "合同</br>行号", "data": "lineNumber" , "className": "dt_center"},  //2
            {"title": "事件日期", "data": "eventDate" , "className": "dt_center"},   //3
            {"title": "事件</br>编号", "data": "billEventNumber" , "className": "dt_center"}, //4
            {"title": "事件类型", "data": "eventType" , "className": "dt_center"},   //5
            {"title": "实物发票类型", "data": "attribute2" , "className": "dt_center"}, //6
            {"title": "开票</br>币别", "data": "curr" , "className": "dt_center"},       //7
            {"title": "开票金额", "data": "amount" , "className": "dt_center"},      //8
            {"title": "状态", "data": "state" , "className": "dt_center"}, //9
            {"title": "开票客户", "data": "custName" , "className": "dt_center"}, //10
            {"title": "开票项目编号", "data": "projectNumber" , "className": "dt_center"}, //10 +1
            {"title": "开票项目名称", "data": "projectName" , "className": "dt_center"},  //11
            {"title": "开票任务", "data": "taskName" , "className": "dt_center"},    //12
            {"title": "汇率", "data": "exchangeRate" , "className": "dt_center"},    //13
            {"title": "开票数量", "data": "quantity" , "className": "dt_center"},     //14
            {"title": "开票单价", "data": "unitPrice" , "className": "dt_center"},    //15
            {"title": "预计到账日期", "data": "attribute1" , "className": "dt_center"},  //16
            {"title": "开票说明", "data": "description" , "className": "dt_center"},   //17

            {"title": "billEventId", "data": "billEventId"},    //18 + 1
            {"title": "contHeaderId", "data": "contHeaderId"},
            {"title": "contLineId", "data": "contLineId"},
            {"title": "deliverableId", "data": "deliverableId"},
            {"title": "paEventId", "data": "paEventId"},  //21
            {"title": "itemId", "data": "itemId"},
            {"title": "lineId", "data": "lineId"},
            {"title": "chgReqId", "data": "chgReqId"},
            {"title": "projectId", "data": "projectId"},
            {"title": "taskId", "data": "taskId"},
            {"title": "orgId", "data": "orgId"},
            {"title": "fundRef1", "data": "fundRef1"},
            {"title": "fundRef2", "data": "fundRef2"},
            {"title": "fundRef3", "data": "fundRef3"},
            {"title": "billOfLading", "data": "billOfLading"},  //31
            {"title": "serialNum", "data": "serialNum"},
            {"title": "rateType", "data": "rateType"},
            {"title": "rateDate", "data": "rateDate"},
            {"title": "attributeCategory", "data": "attributeCategory"},
            {"title": "attribute3", "data": "attribute3"},
            {"title": "attribute4", "data": "attribute4"},
            {"title": "attribute5", "data": "attribute5"},
            {"title": "attribute6", "data": "attribute6"},
            {"title": "attribute7", "data": "attribute7"},
            {"title": "attribute8", "data": "attribute8"}, //41
            {"title": "attribute9", "data": "attribute9"},
            {"title": "attribute10", "data": "attribute10"},
            {"title": "attribute11", "data": "attribute11"},
            {"title": "attribute12", "data": "attribute12"},
            {"title": "attribute13", "data": "attribute13"},
            {"title": "attribute14", "data": "attribute14"},
            {"title": "attribute15", "data": "attribute15"},
            {"title": "initiatedFlag", "data": "initiatedFlag"},  //49
            {"title": "processedFlag", "data": "processedFlag"}  //50
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "targets": 0,
            "width": 5
        },{
            "searchable": false,
            "orderable": false,
            "targets": 1,
            "width": 32
        },{
            "width": 50,
            "targets": 2
        },{
            "width": 70,
            "targets": 3
        },{
            "width": 50,
            "targets": 4
        },{
            "width": 120,
            "targets": 5
        },{
            "width": 90,
            "targets": 6
        },{
            "width": 32,
            "targets": 7
        },{
            "width": 90,
            "targets": 8
            // "createdCell": function (td, cellData, rowData, row, col) {
            //     $(td).attr('style', 'text-align: right;')
            //     // 也可以写在创建行中
            // }
        },{
            "width": 45,
            "targets": 9
        },{
            "width": 160,
            "targets": 10
        },{
            "width": 120,
            "targets": 11
        },{
            "width": 200,
            "targets": 12
        },{
            "width": 65,
            "targets": 13
        },{
            "width": 45,
            "targets": 14
        },{
            "width": 90,
            "targets": 15
        },{
            "width": 65,
            "targets": 16
        },{
            "width": 120,
            "targets": 17
        }, {
            "width": 400,
            "targets": 18
        }
            ,{"visible": false, "width": 0, "targets": 19}
            ,{"visible": false, "width": 0, "targets": 20}
            ,{"visible": false, "width": 0, "targets": 21}
            ,{"visible": false, "width": 0, "targets": 22}
            ,{"visible": false, "width": 0, "targets": 23}
            ,{"visible": false, "width": 0, "targets": 24}
            ,{"visible": false, "width": 0, "targets": 25}
            ,{"visible": false, "width": 0, "targets": 26}
            ,{"visible": false, "width": 0, "targets": 27}
            ,{"visible": false, "width": 0, "targets": 28}
            ,{"visible": false, "width": 0, "targets": 29}
            ,{"visible": false, "width": 0, "targets": 30}
            ,{"visible": false, "width": 0, "targets": 31}
            ,{"visible": false, "width": 0, "targets": 32}
            ,{"visible": false, "width": 0, "targets": 33}
            ,{"visible": false, "width": 0, "targets": 34}
            ,{"visible": false, "width": 0, "targets": 35}
            ,{"visible": false, "width": 0, "targets": 36}
            ,{"visible": false, "width": 0, "targets": 37}
            ,{"visible": false, "width": 0, "targets": 38}
            ,{"visible": false, "width": 0, "targets": 39}
            ,{"visible": false, "width": 0, "targets": 40}
            ,{"visible": false, "width": 0, "targets": 41}
            ,{"visible": false, "width": 0, "targets": 42}
            ,{"visible": false, "width": 0, "targets": 43}
            ,{"visible": false, "width": 0, "targets": 44}
            ,{"visible": false, "width": 0, "targets": 45}
            ,{"visible": false, "width": 0, "targets": 46}
            ,{"visible": false, "width": 0, "targets": 47}
            ,{"visible": false, "width": 0, "targets": 48}
            ,{"visible": false, "width": 0, "targets": 49}
            ,{"visible": false, "width": 0, "targets": 50}
            ,{"visible": false, "width": 0, "targets": 51}
            ,{"visible": false, "width": 0, "targets": 52}
        ],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            $(row).children('td').eq(1).attr('style', 'text-align: center;');
            $(row).children('td').eq(2).attr('style', 'text-align: left;');
            $(row).children('td').eq(3).attr('style', 'text-align: left;');
            $(row).children('td').eq(4).attr('style', 'text-align: left;');
            $(row).children('td').eq(5).attr('style', 'text-align: left;');
            $(row).children('td').eq(6).attr('style', 'text-align: left;');
            $(row).children('td').eq(7).attr('style', 'text-align: left;');
            $(row).children('td').eq(8).attr('style', 'text-align: right;');
            $(row).children('td').eq(9).attr('style', 'text-align: left;');
            $(row).children('td').eq(10).attr('style', 'text-align: left;');
            $(row).children('td').eq(11).attr('style', 'text-align: left;');
            $(row).children('td').eq(12).attr('style', 'text-align: left;');
            $(row).children('td').eq(13).attr('style', 'text-align: right;');
            $(row).children('td').eq(14).attr('style', 'text-align: right;');
            $(row).children('td').eq(15).attr('style', 'text-align: right;');
            $(row).children('td').eq(16).attr('style', 'text-align: left;');
            $(row).children('td').eq(17).attr('style', 'text-align: left;');


        },
        // "initComplete": function () {
        //     $('#dataTables-data_length').insertBefore($('#dataTables-data_info'));
        //     $('#dataTables-data_length').addClass("col-sm-4");
        //     $('#dataTables-data_length').css("paddingLeft", "0px");
        //     $('#dataTables-data_length').css("paddingTop", "5px");
        //     $('#dataTables-data_length').css("maxWidth", "130px");
        // }
    });

    $('#dataTables-InvoiceApplyData tbody').on( 'click', 'tr', function () {
        var table = $('#dataTables-InvoiceApplyData').DataTable();
        if ( $(this).hasClass('selected') ) {
            var check = $(this).find("input[name='ch_list']");
            check.prop("checked", false);

            $(this).removeClass('selected');
            $(this).attr('bgcolor',"#ffffff");
            $('#bu_copy').attr('disabled',"disabled");
            $('#bu_edit').attr('disabled',"disabled");
            $('#bu_del').attr('disabled',"disabled");
            $('#bu_submit').attr('disabled',"disabled");
        }
        else {
            //取消选择按钮值
            var check = table.$('tr.selected').find("input[name='ch_list']");
            check.prop("checked", false);
            table.$('tr.selected').attr('bgcolor',"#ffffff");
            table.$('tr.selected').removeClass('selected');
            $(this).attr('bgcolor',"#E0FFFF");
            $(this).addClass('selected');
            var check = $(this).find("input[name='ch_list']");
            check.prop("checked", true);
            if ('否' == table.row($(this)).data().processedFlag){
                $('#bu_del').removeAttr('disabled');
                $('#bu_submit').removeAttr('disabled');
                document.getElementById("bu_edit").innerHTML=('<i class="fa fa-pencil-square-o" style="color: #B8860B;font-size: 13px;"></i> 编辑');
            }
            else{
                $('#bu_del').attr('disabled',"disabled");
                $('#bu_submit').attr('disabled',"disabled");
                document.getElementById("bu_edit").innerHTML=('<i class="fa fa-pencil-square-o" style="color: #B8860B;font-size: 13px;"></i> 查看');
            }
            if (0 != table.column( 0 ).data().length){
                $('#bu_copy').removeAttr('disabled');
                $('#bu_edit').removeAttr('disabled');
            }
        }
    } );

    billEventTable.on( 'order.dt search.dt', function () {
        billEventTable.column(1, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );
        billEventTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = "<input type='checkbox' name='ch_list' />";
        } );
    } ).draw();
}

//初始化开票记录表格
function initInvoiceApply(val) {
    $.ajax({
        url : config.baseurl +"/api/invoiceApply/billEventList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            contId:       val
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            billEventDataSet = data.dataSource;
            console.log(billEventDataSet);
            initBillEventTables();
        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });

};

// ---------------------------------------------------------------------------------------------------------------------
function commafyback(num)
{
    var x = num.split(',');
    return parseFloat(x.join(""));
}

// function commafy(num)
// {
//     num = num.toFixed(2) +"";
//     var re=/(-?/d+)(/d{3})/
// while(re.test(num)){
//     num=num.replace(re,"$1,$2");
// }
// return num;
// }

//格式化数字
function toThousands(num) {
    var num = (num || 0).toString(), result = '';
    var bool = num.indexOf(".");
    //返回大于等于0的整数值，若不包含"Text"则返回"-1。
    if(bool>0){
        result = num.slice(bool - num.length)
        num = num.slice(0,bool);
        while (num.length > 3) {
            result = ',' + num.slice(-3) + result;
            num = num.slice(0, num.length - 3);
        }
        if (num) { result = num + result ; }
    }else{
        while (num.length > 3) {
            result = ',' + num.slice(-3) + result;
            num = num.slice(0, num.length - 3);
        }
        if (num) { result = num + result + '.00'; }
    }
    return result;
};

//自动补充空格
function format(str,len) {
    var result = str;
    var n = len ? len : 20;

    var lcount = 0;
    for (var i=0; i<str.length; i++) {
        var c = str.charCodeAt(i);
        //单字节加1
        if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) {
            lcount++;
        }
        else {
            lcount+=2;
        }
        // if (str[i].match(/[^\x00-\xff]/ig) != null) //全角
        //     lcount += 2; //如果是全角，占用两个字节
        // else
        //     lcount += 1; //半角占用一个字节
    }

    if(lcount < n){
        for(var i=0;i<= (n - lcount);i++){
                result+="&nbsp";
            }
        // result+= n - lcount;
    }
    // if(result.length>=len){
    //     result = result.substring(0,4);
    // }
    return result;
};


function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    // var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
    return currentdate;
}

function getNowFormatDateTime() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
        + " " + date.getHours() + seperator2 + date.getMinutes()
        + seperator2 + date.getSeconds();
    return currentdate;
}

//设置焦点
function Setfocus(val){
    document.getElementById(val).focus();
};



//合同编号改变
function contChange(data){
    if (data == "" ||undefined || null)
        $('#contId').val('');
};

//项目编号改变
function projChange(data){
    if (data == "" ||undefined || null)
        $('#projId').val('');
};

//-------------------------------------开票操作-------------------------------------------------------------------------
// 点击新增按钮
$('#bu_add').on('click', function (e) {
    billEventData = null;
    initAddOrUpdateModal();
});

$('#bu_edit').on('click', function (e) {
    checkBillEvent('EDIT');
});

$('#bu_submit').on('click', function (e) {
    checkBillEvent('EDIT');
    // 模拟点击提交
    setTimeout(function(){
            $('#bu_fill_process').trigger("click");}
        ,3000
    );
});

$('#bu_copy').on('click', function (e) {
    checkBillEvent('COPY');
});

function checkBillEvent(val) {
    billEventData = null;
    var table = $('#dataTables-InvoiceApplyData').DataTable();
    // var data = table.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
    // var data = table.$('tr.selected').data();
    var data = table.rows(table.$('tr.selected')).data();
    if (data.length != 1){
        window.parent.warring("请选择一行数据编辑！");
        return;
    }
    $.ajax({
        url : config.baseurl +"/api/invoiceApply/billEventList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            contId:       data[0].contHeaderId,
            deliverablesId:       data[0].deliverableId,
            billEventId: data[0].billEventId
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            billEventData = data.dataSource[0];
            if ('COPY' == val){
                billEventData.billEventId = "";
                billEventData.attribute3 = "";
                billEventData.initiatedFlag = "";
                billEventData.processedFlag = "否";
                billEventData.billEventNumber = "";
            }
            console.log(billEventData);
            initAddOrUpdateModal();
        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

function initAddOrUpdateModal (val) {
    var table = $('#dataTables-contData').DataTable();
    var tableData = table.rows(table.$('tr.selected')).data();
    $.ajax({
        url : config.baseurl +"/api/invoiceApply/deliverablesList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            contractId :tableData[0].contHeaderId
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){

            if (0 == data.dataTotal){
                window.parent.error('该合同项无生效的市场活动物品，请联系合同管理员维护市场活动物品再行开票！');
                return;
            }else {
                // 隐藏活动物品下拉菜单
                $(".sel_deliverable").selectpicker('hide');
                //全部清空
                $('#deliverableId').val("");
                $('#contHeaderId').val("");
                $('#contLineId').val("");
                $('#billEventId').val("");
                $('#contCurr').val("");
                $('#deliverableNumber').val("");
                $('#lineStyle').val("");
                $('#lineAmount').val("");
                $('#sel_processedFlag').val("");
                $('#eventDate').val("");
                $('#eventType').val("");
                $('#eventNumber').val("");
                $('#curr').val("");
                $('#rateType').val("");
                $('#rateDate').val("");
                $('#rate').val("");
                $('#quantity').val("");
                $('#unitPrice').val("");
                $('#amount').val("");
                $('#sel_proj_edit').val("");
                $('#sel_task').val("");
                $('#sel_org').val("");
                $('#attribute1').val("");
                $('#attribute2').val("");
                $('#attribute8').val("");
                $('#description').val("");

                // 执行初始化
                $('#deliverableId').val(billEventData ? billEventData.deliverableId : "");
                $('#contHeaderId').val(billEventData ? billEventData.contHeaderId : tableData[0].contHeaderId);
                $('#deliverableNumber').val(billEventData ? billEventData.lineNumber : "");
                $('#lineStyle').val(billEventData ? billEventData.lineStyle : "");
                $('#lineAmount').val(billEventData ? billEventData.deliverablePrice : "");
                $('#contLineId').val(billEventData ? billEventData.contLineId : "");

                $('#billEventId').val(billEventData ? billEventData.billEventId : "");
                $('#eventDate').val(billEventData ? billEventData.eventDate : getNowFormatDate());
                // $('#sel_processedFlag').val(billEventData ? billEventData.processedFlag : "");
                $('#state').val(billEventData ? billEventData.state : "编辑");
                $('#eventNumber').val(billEventData ? billEventData.billEventNumber : "");
                // $('#curr').val(billEventData ? billEventData.curr : "");
                $('#rateDate').val(billEventData ? billEventData.rateDate : "");
                $('#rate').val(billEventData ? billEventData.exchangeRate : "");
                $('#quantity').val(billEventData ? billEventData.quantity : 1);
                // $('#unitPrice').val(billEventData ? billEventData.unitPrice : ($('#lineAmount').val() ? commafyback($('#lineAmount').val()) : ""));
                // $('#amount').val(billEventData ? billEventData.amount : ($('#lineAmount').val() ? commafyback($('#lineAmount').val()) : ""));
                $('#attribute1').val(billEventData ? billEventData.attribute1 : "");
                $('#description').val(billEventData ? billEventData.description : "");

                //初始化市场活动物品(当新增时)
                // initDeliverablesSel(data.dataSource);
                if (!$('#deliverableId').val()){
                    if (1 == data.dataTotal) {
                        $('#deliverableId').val(data.dataSource[0].deliverableId);
                        $('#deliverableNumber').val(data.dataSource[0].deliverableNumber);
                        $('#lineStyle').val(data.dataSource[0].lineStyle);
                        $('#contCurr').val(data.dataSource[0].curr);
                        $('#lineAmount').val(toThousands(data.dataSource[0].unitPrice));
                        $('#contHeaderId').val(data.dataSource[0].contHeaderId);
                        $('#contLineId').val(data.dataSource[0].contLineId);
                        // $('#quantity').val(data.dataSource[0].quantity);
                    }
                }else{
                    $(data.dataSource).each(function(index){
                        if ( $('#deliverableId').val() == data.dataSource[index].deliverableId ){
                            $('#deliverableId').val(data.dataSource[index].deliverableId);
                            $('#deliverableNumber').val(data.dataSource[index].deliverableNumber);
                            $('#lineStyle').val(data.dataSource[index].lineStyle);
                            $('#contCurr').val(data.dataSource[index].curr);
                            $('#lineAmount').val(toThousands(data.dataSource[index].unitPrice));
                            $('#contHeaderId').val(data.dataSource[index].contHeaderId);
                            $('#contLineId').val(data.dataSource[index].contLineId);
                            // $('#quantity').val(data.dataSource[index].quantity);
                        }
                    });
                }

                $("#curr").val(billEventData ? billEventData.curr :($('#contCurr').val() ? $('#contCurr').val() : "" ));
                $('#unitPrice').val(billEventData ? billEventData.unitPrice : ($('#lineAmount').val() ? commafyback($('#lineAmount').val()) : ""));
                $('#amount').val(billEventData ? billEventData.amount : ($('#lineAmount').val() ? $('#lineAmount').val() : ""));

                if ($('#deliverableId').val()){
                    // 生成项目下拉选择菜单
                    initProjectEditSel(billEventData ? billEventData.projectId : "",  billEventData ? billEventData.taskId : "",  billEventData ? billEventData.eventType : "",  billEventData ? billEventData.orgId : "");
                }
                // 生成别种下拉选择菜单
                // initCurrSel (billEventData ? billEventData.curr : $('#contCurr').val());
                // 生成别种下拉选择菜单
                initRateTypeSel (billEventData ? billEventData.rateType : "");
                // 生成实物发票类型下拉选择菜单
                initAttribute2Sel (billEventData ? billEventData.attribute2 : "");
                // 生成客户下拉选择菜单
                initCustEditSel (billEventData ? billEventData.contHeaderId : tableData[0].contHeaderId, billEventData ? billEventData.attribute8 : "");


                // 生成是否已处理事件下拉选择菜单
                // initProcessedFlagSel (billEventData ? billEventData.processedFlag : "");

                if (billEventData){
                    if ('否' == billEventData.processedFlag){
                        $('#b_searchDeliverable').removeAttr('disabled');
                        $('#eventDate').removeAttr('disabled');
                        $('#b_curr').removeAttr('disabled');

                        if ($('#contCurr').val() != $('#curr').val()){
                            $('#rateType').removeAttr('disabled');
                            $('#rateDate').removeAttr('disabled');
                            $('#rate').removeAttr('disabled');
                        }

                        $('#sel_proj_edit').removeAttr('disabled');
                        $('#sel_org').removeAttr('disabled');
                        $('#unitPrice').removeAttr('disabled');
                        $('#quantity').removeAttr('disabled');
                        // $('#amount').removeAttr('disabled');
                        $('#attribute1').removeAttr('disabled');
                        $('#attribute2').removeAttr('disabled');
                        $('#sel_cust').removeAttr('disabled');
                        $('#description').removeAttr('readonly');
                        if($('#sel_proj_edit').val()){
                            $('#eventType').removeAttr('disabled');
                            $('#sel_task').removeAttr('disabled');
                        }

                        $('#bu_save_line').removeAttr('disabled');
                        $('#bu_fill_process').removeAttr('disabled');
                        if (billEventData.billEventId == ""||null){
                            $('#bu_del_line').attr('disabled','disabled');
                        }else{
                            $('#bu_del_line').removeAttr('disabled');
                        }
                    }else {
                        $('#b_searchDeliverable').attr('disabled','disabled');
                        $('#eventDate').attr('disabled','disabled');
                        $('#b_curr').attr('disabled','disabled');
                        $('#rateType').attr('disabled');
                        $('#rateDate').attr('disabled');
                        $('#rate').attr('disabled');
                        $('#sel_proj_edit').attr('disabled','disabled');
                        $('#sel_org').attr('disabled','disabled');
                        $('#unitPrice').attr('disabled','disabled');
                        $('#quantity').attr('disabled','disabled');
                        // $('#amount').attr('disabled');
                        $('#attribute1').attr('disabled','disabled');
                        $('#attribute2').attr('disabled','disabled');
                        $('#sel_cust').attr('disabled','disabled');
                        $('#eventType').attr('disabled','disabled');
                        $('#sel_task').attr('disabled','disabled');
                        $('#description').attr('readonly','readonly');

                        $('#bu_save_line').attr('disabled','disabled');
                        $('#bu_fill_process').attr('disabled','disabled');
                        $('#bu_del_line').attr('disabled','disabled');
                    }
                }else{
                    $('#b_searchDeliverable').removeAttr('disabled');
                    $('#eventDate').removeAttr('disabled');
                    $('#b_curr').removeAttr('disabled');
                    // if ('CNY' != $('#curr').val()){
                    //     $('#rateType').removeAttr('disabled');
                    //     $('#rateDate').removeAttr('disabled');
                    //     $('#rate').removeAttr('disabled');
                    // }
                    $('#sel_proj_edit').removeAttr('disabled');
                    $('#sel_org').removeAttr('disabled');
                    $('#unitPrice').removeAttr('disabled');
                    $('#quantity').removeAttr('disabled');
                    // $('#amount').removeAttr('disabled');
                    $('#attribute1').removeAttr('disabled');
                    $('#attribute2').removeAttr('disabled');
                    $('#sel_cust').removeAttr('disabled');
                    if($('#sel_proj_edit').val()){
                        $('#eventType').removeAttr('disabled');
                        $('#sel_task').removeAttr('disabled');
                    }
                    $('#description').removeAttr('readonly');

                    $('#bu_save_line').removeAttr('disabled');
                    $('#bu_fill_process').removeAttr('disabled');
                    $('#bu_del_line').attr('disabled','disabled');
                }

                document.getElementById("editLineLabel").innerHTML=(billEventData ? ('否' == billEventData.processedFlag ? "编辑合同开票申请" : "查看合同开票申请") : "新增合同开票申请");

                // 显示新增界面
                $('#add_modal').modal('show');
            }
        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });

}

// 点击市场活动物品选择按钮
$('#b_searchDeliverable').on('click', function (e) {
    var table = $('#dataTables-contData').DataTable();
    // var data = table.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
    var data = table.rows(table.$('tr.selected')).data();
    $.ajax({
        url : config.baseurl +"/api/invoiceApply/deliverablesList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            contractId :data[0].contHeaderId
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            initDeliverablesSel(data.dataSource);
        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
});

// 生成开单市场活动物品下拉选择菜单
function initDeliverablesSel (data) {
    $("#sel_deliverable").empty();

    $(data).each(function(index){
        $("#sel_deliverable").append($('<option value="'+data[index].deliverableId + '">'+data[index].deliverableNumber + "," + data[index].lineStyle + ",(" + data[index].projectNumber +')' + data[index].projectName + "," + data[index].curr + "," + data[index].unitPrice + "," + data[index].quantity + "," +  data[index].contLineId+'</option>'));
    });
    if ($('#deliverableId').val()){
        $('#sel_deliverable').selectpicker('val',($('#deliverableId').val()));  //默认值
    }
    $("#sel_deliverable").selectpicker('refresh');
    $("#sel_deliverable").selectpicker('show');    //显示出来

    $('button[data-id="sel_deliverable"]').trigger("click");
}

//当市场活动物品选择按钮下拉隐藏时
$("#sel_deliverable").on('hidden.bs.select', function (e) {
    $('#deliverableId').val($('#sel_deliverable').val().split(",")[0]);
    var sTest = $('#sel_deliverable option:selected').text();
    $('#deliverableNumber').val(sTest.split(",")[0]);
    $('#lineStyle').val(sTest.split(",")[1]);
    $('#contCurr').val(sTest.split(",")[3]);
    $('#lineAmount').val(toThousands(sTest.split(",")[4]));
    $('#quantity').val(sTest.split(",")[5]);
    $('#contLineId').val(sTest.split(",")[6]);

    $("#sel_deliverable").selectpicker('hide');

    // 生成项目下拉选择菜单
    // initProjectEditSel($('#sel_proj_edit').val() ? $('#sel_proj_edit').val() : "",$('#eventType').val() ? $('#eventType').val() : "",$('#eventType').val() ? $('#eventType').val() : "",$('#sel_org').val() ? $('#sel_org').val() : "");
     initProjectEditSel("",$('#eventType').val() ? $('#eventType').val() : "",$('#eventType').val() ? $('#eventType').val() : "","");

    if ($('#contCurr').val() != $('#curr').val()){
        $("#curr").val($('#contCurr').val());
        currChange($('#curr').val());
    }
});

// 点击币种选择按钮
$('#b_curr').on('click', function (e) {
    $.ajax({
        url : config.baseurl +"/api/invoiceApply/currList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            initCurrSel(data.dataSource);
        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
});

// 生成币种下拉选择菜单
function initCurrSel (data) {
    $("#sel_curr").empty();

    $(data).each(function(index){
        $("#sel_curr").append($('<option value="'+data[index].currCode + '">'+data[index].currCode + ","  +  data[index].currName +'</option>'));
    });
    if ($('#curr').val()){
        $('#sel_curr').selectpicker('val',($('#curr').val()));  //默认值
    }
    $("#sel_curr").selectpicker('refresh');
    $("#sel_curr").selectpicker('show');    //显示出来

    $('button[data-id="sel_curr"]').trigger("click");
}

//当币种选择按钮下拉隐藏时
$("#sel_curr").on('hidden.bs.select', function (e) {
    if ($('#curr').val() != $('#sel_curr').val().split(",")[0]){
        currChange($('#sel_curr').val().split(",")[0]);
    }

    $('#curr').val($('#sel_curr').val().split(",")[0]);
    // var sTest = $('#sel_curr option:selected').text();
    // $('#deliverableNumber').val(sTest.split(",")[0]);
    // $('#curr').val(sTest.split(",")[]);

    $("#sel_curr").selectpicker('hide');
});

// 生成项目下拉选择菜单
function initProjectEditSel (projectId,taskId,evenType,orgid) {
    $.ajax({
        url : config.baseurl +"/api/invoiceApply/projectList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            contLineId:  $('#contLineId').val(),
            checkProjectMember:  "N"
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            $("#sel_proj_edit").empty();
            var chk = false;
            var projOrgid = "";
            $(data.dataSource).each(function(index){
                $("#sel_proj_edit").append($('<option value="'+data.dataSource[index].projectId + '">'+format(data.dataSource[index].projectNumber,20) + "," + data.dataSource[index].projectName+'</option>'));
                if (projectId == data.dataSource[index].projectId)
                    chk = true;
                    projOrgid = data.dataSource[index].projOrgId;
            });

            if (projectId){
                if (chk){
                    $("#sel_proj_edit").val(projectId);
                }
                else{
                    if (1 == data.dataTotal)
                        $("#sel_proj_edit").val(data.dataSource[0].projectId);
                        projOrgid = data.dataSource[0].projOrgId;
                }
            }else{
                if (1 == data.dataTotal){
                    $("#sel_proj_edit").val(data.dataSource[0].projectId);
                    projOrgid = data.dataSource[0].projOrgId;
                }else{
                    $("#sel_proj_edit").val("");
                }
            }

            // 生成事件类型下拉选择菜单
            initeventTypeSel (evenType?evenType: "");
            // 生成任务下拉选择菜单
            initTaskSel(taskId?taskId: "");
            // 生成组织下拉选择菜单
            initOrgSel (orgid ? orgid : projOrgid);

            if (billEventData) {
                if ('否' == billEventData.processedFlag) {
                    if($('#sel_proj_edit').val()){
                        $('#eventType').removeAttr('disabled');
                        $('#sel_task').removeAttr('disabled');
                    }
                }
            }else{
                if($('#sel_proj_edit').val()){
                    $('#eventType').removeAttr('disabled');
                    $('#sel_task').removeAttr('disabled');
                }
            }
        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

// 生成事件类型下拉选择菜单
function initeventTypeSel (val) {
    if ($('#sel_proj_edit').val())
        $.ajax({
            url : config.baseurl +"/api/invoiceApply/eventTypeList",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                projectId : $('#sel_proj_edit').val()
            },
            type:"GET",
            beforeSend:function(){
            },
            success:function(data){
                $("#eventType").empty();

                $(data.dataSource).each(function(index){
                    $("#eventType").append($('<option value="'+data.dataSource[index].eventType + '">'+format(data.dataSource[index].eventType,50) + "," + data.dataSource[index].eventTypeName+'</option>'));
                });

                $("#eventType").val(val);
            },
            complete:function(){
            },
            error:function(){
                window.parent.error(ajaxError_loadData);
            }
        });
}

// // 生成币种下拉选择菜单
// function initCurrSel (val) {
//     $.ajax({
//         url : config.baseurl +"/api/invoiceApply/currList",
//         contentType : 'application/x-www-form-urlencoded',
//         dataType:"json",
//         data: {
//         },
//         type:"GET",
//         beforeSend:function(){
//         },
//         success:function(data){
//             $("#curr").empty();
//
//             $(data.dataSource).each(function(index){
//                 $("#curr").append($('<option value="'+data.dataSource[index].currCode + '">' + data.dataSource[index].currCode + "," + data.dataSource[index].currName+'</option>'));
//             });
//
//             $("#curr").val(val);
//
//             if ($('#contCurr').val() != $('#Curr').val()){
//                 $('#rateType').attr('disabled');
//                 $('#rateDate').attr('disabled');
//                 // $('#rate').attr('disabled');
//             }else{
//                 $('#rateType').removeAttr('disabled');
//                 $('#rateDate').removeAttr('disabled');
//                 // $('#rate').removeAttr('disabled');
//             }
//         },
//         complete:function(){
//         },
//         error:function(){
//             window.parent.error(ajaxError_loadData);
//         }
//     });
// }


// 生成汇率类型下拉选择菜单
function initRateTypeSel (val) {
    $.ajax({
        url : config.baseurl +"/api/invoiceApply/rateTypeList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            $("#rateType").empty();

            $(data.dataSource).each(function(index){
                $("#rateType").append($('<option value="'+data.dataSource[index].rateType + '">' + format(data.dataSource[index].rateTypeName,30) + "," + data.dataSource[index].rateTypeDesc+'</option>'));
            });

            $("#rateType").val(val);

            // if ('User' != $('#rateType').val()){
            //     $('#rate').attr('disabled');
            // }else{
            //     $('#rate').removeAttr('disabled');
            // }
        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

// 生成任务下拉选择菜单
function initTaskSel (val) {
    if ($("#sel_proj_edit").val())
        $.ajax({
            url : config.baseurl +"/api/invoiceApply/taskList",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                projectId:  $("#sel_proj_edit").val()
            },
            type:"GET",
            beforeSend:function(){
            },
            success:function(data){
                $("#sel_task").empty();

                var initValue = "";
                var initText = "";
                $('#sel_task').append("<option value='" + initValue + "'>" + initText + "," + initText+ "</option>");

                $(data.dataSource).each(function(index){
                    $("#sel_task").append($('<option value="'+data.dataSource[index].taskId + '">'+data.dataSource[index].taskNumber + "," + data.dataSource[index].taskName+'</option>'));
                });

                $("#sel_task").val(val);
            },
            complete:function(){
            },
            error:function(){
                window.parent.error(ajaxError_loadData);
            }
        });
}

// 生成组织下拉选择菜单
function initOrgSel (val) {
    var table = $('#dataTables-contData').DataTable();
    // var data =  table.rows(table.$('tr.selected')).data();
    var data = table.rows(table.$('tr.selected')).data();
    $.ajax({
        url : config.baseurl +"/api/invoiceApply/orgList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            orgId:  data[0].orgId
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            $("#sel_org").empty();

            $(data.dataSource).each(function(index){
                $("#sel_org").append($('<option value="'+data.dataSource[index].orgId + '">'+data.dataSource[index].orgId + "," + data.dataSource[index].orgName+'</option>'));
            });

            $("#sel_org").val(val);
        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

// 生成实物发票类型下拉选择菜单
function initAttribute2Sel (val) {
    $.ajax({
        url : config.baseurl +"/api/invoiceApply/invoiceTypeList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            $("#attribute2").empty();

            $(data.dataSource).each(function(index){
                $("#attribute2").append($('<option value="'+data.dataSource[index].invoiceType + '">'+data.dataSource[index].invoiceType +'</option>'));
            });

            $("#attribute2").val(val);
        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

//生成合同开票客户
function initCustEditSel (contId,val) {
    $.ajax({
        url : config.baseurl +"/api/invoiceApply/contCustList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            contHeaderId:  contId
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            $("#sel_cust").empty();
            var chk = false;
            var custId = "";

            var initValue = "";
            var initText = "";
            $('#sel_cust').append("<option value='" + initValue + "'>" + initText + initText + initText + "</option>");

            $(data.dataSource).each(function(index){
                $("#sel_cust").append($('<option value="'+data.dataSource[index].custId + '">'+data.dataSource[index].custNumber + "," + data.dataSource[index].custName + "," + data.dataSource[index].custStatus  + '</option>'));
                if (val == data.dataSource[index].custId)
                    chk = true;
            });

            if (1 == data.dataTotal){
                custId = data.dataSource[0].custId;
            }else{
                if (chk){
                    custId = val;
                }
            }

            $("#sel_cust").val(custId);

        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

// 生成是否已处理事件下拉选择菜单
// function initProcessedFlagSel (val) {
//     $("#sel_processedFlag").append($('<option value="是">是</option>'));
//     $("#sel_processedFlag").append($('<option value="否">否</option>'));
//     $("#sel_processedFlag").val(val ? val : "否");
// }


// 获取汇率
function getRate (source,fromCurr,toCurr,rateType,rateDate) {
    $.ajax({
        url : config.baseurl +"/api/invoiceApply/getRate",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            fromCurr:fromCurr,
            toCurr:toCurr,
            rateType:rateType,
            rateDate:rateDate
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            if (0 != data){
                $('#rate').val(data);
                roundPrice();
                // roundAmount();
            }else{
                window.parent.error("未获取到" + rateDate + "日" + fromCurr + "与" + toCurr + "的" + rateType + "兑换汇率！",3000);
                setTimeout(function(){
                        Setfocus(source);}
                    ,3500
                );
                return false;
            }
        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

//计算开单金额
function roundPrice(){
    if (($('#rate').val() != "" || undefined || null) && ($('#contCurr').val() != $('#curr').val()) && ($('#unitPrice').val() != "" || undefined || null)){
        var lprice = $('#rate').val() * $('#unitPrice').val();
        $('#unitPrice').val(lprice.toFixed(2));
    }

    roundAmount();
};

//计算开单金额
function roundAmount(){
    // if (($('#rate').val() != "" || undefined || null) && ($('#contCurr').val() != $('#curr').val()) && ($('#unitPrice').val() != "" || undefined || null)){
    //             var lprice = $('#rate').val() * $('#unitPrice').val();
    //             $('#unitPrice').val(lprice.toFixed(2));
    // }

    if (($('#unitPrice').val() != "" || undefined || null) && ($('#quantity').val() != "" || undefined || null)){
        var lsum = $('#unitPrice').val() * $('#quantity').val() ;
        $('#amount').val(toThousands(lsum.toFixed(2)));
    }

    // if ($('#rate').val() != "" || undefined || null){
    //     if ($('#contCurr').val() != $('#curr').val()) {
    //         // if (($('#rate').val() != "" || undefined || null) && ($('#unitPrice').val() != "" || undefined || null) && ($('#quantity').val() != "" || undefined || null)){
    //         if (($('#rate').val() != "" || undefined || null) && ($('#unitPrice').val() != "" || undefined || null)){
    //             var lprice = $('#rate').val() * $('#unitPrice').val();
    //             $('#unitPrice').val(lprice.toFixed(2));
    //
    //             if ($('#quantity').val() != "" || undefined || null){
    //                 // var lsum = $('#rate').val() * $('#unitPrice').val() * $('#quantity').val() ;
    //                 var lsum = $('#unitPrice').val() * $('#quantity').val() ;
    //                 $('#amount').val(toThousands(lsum.toFixed(2)));
    //             }
    //         }
    //
    //     }else{
    //         if (($('#unitPrice').val() != "" || undefined || null) && ($('#quantity').val() != "" || undefined || null)){
    //             // $('#amount').val(toThousands($('#unitPrice').val() * $('#quantity').val(), 2));
    //             var lsum = $('#unitPrice').val() * $('#quantity').val() ;
    //             $('#amount').val(toThousands(lsum.toFixed(2)));
    //         }
    //     }
    // }else{
    //     if (($('#unitPrice').val() != "" || undefined || null) && ($('#quantity').val() != "" || undefined || null)){
    //         var lsum = $('#unitPrice').val() * $('#quantity').val() ;
    //         $('#amount').val(toThousands(lsum.toFixed(2)));
    //     }
    //
    // }
};


// -------------------------------------------当值改变的程序---------------------------------------------------------------
function editProjectChange(data){
    if (data != "" ||undefined || null){
        // 生成事件类型下拉选择菜单
        initeventTypeSel ($('#eventType').val() ? $('#eventType').val() : "");
        // 生成任务下拉选择菜单
        initTaskSel($('#eventType').val() ? $('#eventType').val() : "");
        // 生成任务下拉选择菜单
        initTaskSel($('#sel_org').val() ? $('#sel_org').val() : "");
    }
};

// 币别重选
function currChange(data){
    $('#rateType').val('');
    $('#rateDate').val('');
    $('#rate').val('');

    if ($('#contCurr').val() == data ){
        $('#rateType').attr('disabled','disabled');
        $('#rateDate').attr('disabled','disabled');
        $('#rate').attr('disabled','disabled');

    }else{
        $('#rateType').removeAttr('disabled');
        $('#rateDate').removeAttr('disabled');
        $('#rate').removeAttr('disabled');
    }
};

// 汇率类型重选
function rateTypeChange(data){
    if ('User' != data){
        $('#rate').attr('disabled','disabled');

        // 重新获取汇率
        if (($('#rateType').val() != "" ||undefined || null)
            &&($('#rateDate').val() != "" ||undefined || null)
            &&($('#curr').val() != "" ||undefined || null)
        ){
            var fromCurr = $('#contCurr').val();
            var toCurr = $('#curr').val();
            var rateType = $('#rateType').val();
            var rateDate = $('#rateDate').val();
            getRate ('rateType',fromCurr,toCurr,rateType,rateDate)
        }
    }else{
        $('#rate').removeAttr('disabled');
    }
};

// 汇率日期重选
function rateDateChange(data) {
    if ('User' != $('#rateType').val()){
        if (data != "" || undefined || null) {
            var fromCurr = $('#contCurr').val();
            var toCurr = $('#curr').val();
            var rateType = $('#rateType').val();
            var rateDate = data;
            getRate('rateDate',fromCurr, toCurr, rateType, rateDate)
        }
    }
}

//汇率更改
function rateChange(data){
    roundPrice();
    // roundAmount();
};

// 开票单价更改
function priceChange(data){
    checkPrice(data);
    roundAmount();
};

function checkPrice(data){
    var num = (data || 0).toString(), result = '';
    var bool = num.indexOf(".");
    //返回大于等于0的整数值，若不包含"Text"则返回"-1。
    if(bool>0) {
        if (num.length - bool > 3){
            window.parent.error("开票单价最多只能输入两位小数！",3000);
            setTimeout(function(){
                    Setfocus('unitPrice');}
                ,3500
            );
            return false;
        }
    }
};

// 开票数量更改
function quantityChange(data){
    roundAmount();
};


// -----------------------------------------------------保存、提交、删除按钮-------------------------------------------
// 保存
$('#bu_save_line').on('click', function (e) {
    checkValue('SAVE');
});

//启动开单
$('#bu_fill_process').on('click', function (e) {
    checkValue('FILL');
});

// 检查填写记录是否完全并执行保存
function checkValue(val){
    var contHeaderId = $('#contHeaderId').val();
    var contLineId = $('#contLineId').val();
    var deliverableId = $('#deliverableId').val();
    var lineStyle =$('#lineStyle').val();
    var eventDate =$('#eventDate').val();
    var eventType =$('#eventType').val();
    var lineId =$('#contLineId').val();
    var projectId = $('#sel_proj_edit').val();
    var taskId = $('#sel_task').val();
    var orgId = $('#sel_org').val();
    var curr = $('#curr').val();
    var contCurr = $('#contCurr').val();
    var rateDate = $('#rateDate').val();
    var rateType = $('#rateType').val();
    var rate = $('#rate').val();
    var exchangeRate = $('#rate').val();
    var quantity = $('#quantity').val();
    var unitPrice = $('#unitPrice').val();
    var attribute1 = $('#attribute1').val();
    var attribute2 = $('#attribute2').val();
    var attribute8 = $('#sel_cust').val();
    var description = $('#description').val();

    if ("" == deliverableId.trim()) {
        window.parent.error("请选择合同行号！");
        Setfocus('deliverableNumber');
        return;
    }
    if ("" == eventDate.trim()) {
        window.parent.error("请选择申请日期！");
        Setfocus('eventDate');
        return;
    }
    if (eventType == null || eventType == "" ) {
        window.parent.error("请填写开票类型！");
        Setfocus('eventType');
        return;
    }
    if ("" == curr.trim()) {
        window.parent.error("请填写开票币种！");
        Setfocus('curr');
        return;
    }
    if (contCurr.trim() != curr.trim()) {
        if ("" == rate.trim()) {
            window.parent.error("请填写汇率！");
            Setfocus('rate');
            return;
        }
    }
    if ("" == orgId.trim()) {
        window.parent.error("请填写开票组织！");
        Setfocus('sel_org');
        return;
    }
    if ("" == quantity.trim()) {
        window.parent.error("请填写开票数量！");
        Setfocus('quantity');
        return;
    }
    if ("" == unitPrice.trim()) {
        window.parent.error("请填写开票单价！");
        Setfocus('unitPrice');
        return;
    }
    if ("" == attribute1.trim()) {
        window.parent.error("请填写预计收款日期！");
        Setfocus('attribute1');
        return;
    }
    if (attribute2 == null || attribute2 == "" ) {
        window.parent.error("请填写实物发票类型！");
        Setfocus('attribute2');
        return;
    }
    if (attribute8 == null || attribute8 == "" ) {
        window.parent.error("请选择开票客户！");
        Setfocus('sel_cust');
        return;
    }
    if ("" == description.trim()) {
        window.parent.error("请填写开票说明！");
        Setfocus('description');
        return;
    }else {
        var i = CheckvalLengthb(description.trim())
        if ( i > 230){
            window.parent.error("开票说明超过230字节,当前内容字节" + i + "个,请删减部分开票说明信息！<br/> (说明：一个汉字、中文标点符号占位3个字节，一个英文、英文符号、数字占位1个字节)");
            Setfocus('description');
            return;
        }
    }

    saveBillEvent(val);
};
// 保存程序
function saveBillEvent(val) {
    var o = new Object();

    o.billEventId = $('#billEventId').val();
    o.contHeaderId = $('#contHeaderId').val();
    o.contLineId = $('#contLineId').val();
    o.deliverableId = $('#deliverableId').val();
    o.eventDate =$('#eventDate').val();
    o.eventType =$('#eventType').val();
    o.lineId =$('#contLineId').val();
    o.projectId = $('#sel_proj_edit').val();
    o.taskId = $('#sel_task').val();
    o.orgId = $('#sel_org').val();
    o.curr = $('#curr').val();
    o.contCurr = $('#contCurr').val();
    o.rateDate = $('#rateDate').val();
    o.rateType = $('#rateType').val();
    o.rate = $('#rate').val();
    o.exchangeRate = $('#rate').val();
    o.quantity = $('#quantity').val();
    o.unitPrice = $('#unitPrice').val();
    o.attribute1 = $('#attribute1').val();
    o.attribute2 = $('#attribute2').val();
    o.attribute8 = $('#sel_cust').val();
    o.description = $('#description').val();

    console.log(JSON.stringify(o));

    if(val == 'SAVE'){
        $.ajax({
            url : config.baseurl +"/api/invoiceApply/saveBillEvent",
            contentType : 'application/json',
            dataType:"json",
            data: JSON.stringify(o),
            type:"POST",
            beforeSend:function(){
                window.parent.showLoading();
            },
            success:function(data){
                if ("0000" == data.returnCode) {
                    window.parent.success("开票申请保存成功！")
                    $('#add_modal').on('shown.bs.modal', function (e) {}).modal('hide');
                    // var table = $('#dataTables-contData').DataTable();
                    // var d = table.rows(table.$('tr.selected')).data();
                    // initInvoiceApply(d[0].contHeaderId);
                    initInvoiceApply(o.contHeaderId);

                } else {
                    window.parent.error(data.returnMsg);
                }
            },
            complete:function(){
                window.parent.closeLoading();
            },
            error:function(){
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }else{
        $.ajax({
            url : config.baseurl +"/api/invoiceApply/fillBillEvent",
            contentType : 'application/json',
            dataType:"json",
            data: JSON.stringify(o),
            type:"POST",
            beforeSend:function(){
                window.parent.showLoading();
            },
            success:function(data){
                if ("0000" == data.returnCode) {
                    window.parent.success("开票申请提交成功！")
                    $('#add_modal').on('shown.bs.modal', function (e) {}).modal('hide');
                    // var table = $('dataTables-contData').DataTable();
                    // var d = table.rows(table.$('tr.selected')).data();
                    // initInvoiceApply(d[0].contHeaderId);

                    initInvoiceApply(o.contHeaderId);
                    getContInvoices2();

                    window.open(data.db_url);
                } else {
                    window.parent.error(data.returnMsg,21000);
                }
            },
            complete:function(){
                window.parent.closeLoading();
            },
            error:function(){
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }
}

// 合同开票金额统计
function getContInvoices2() {
        var table = $('#dataTables-contData').DataTable();
        // var data = table.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        // var data = table.$('tr.selected').data();
        var data = table.rows(table.$('tr.selected')).data();
        $.ajax({
            url : config.baseurl +"/api/invoiceApply/getContInvoices",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                contId:       data[0].contHeaderId
            },
            type:"GET",
            beforeSend:function(){
            },
            success:function(data){
                console.log(data.dataSource[0]);
                $('#contInvoice').val(toThousands(data.dataSource[0].invoiceAmount));
                $('#contInvoicePro').val(data.dataSource[0].invoicePro);
                $('#contInvoiceWay').val(toThousands(data.dataSource[0].wayAmount));
                $('#contInvoiceWayPro').val(data.dataSource[0].wayPro);
            },
            complete:function(){
            },
            error:function(){
                window.parent.error(ajaxError_loadData);
            }
        });
}

// 点击主页面删除按钮
$('#bu_del').on('click', function (e) {
    var table = $('#dataTables-InvoiceApplyData').DataTable();
    var data = table.rows(table.$('tr.selected')).data();
    if (data.length != 1){
        window.parent.warring("请选择一行数据删除！");
        return;
    }
    var contHeaderId = data[0].contHeaderId;
    var billEventId = data[0].billEventId;
    delBillEvent(contHeaderId,billEventId);
});
// 点击编辑面删除按钮
$('#bu_del_line').on('click', function (e) {
    var contHeaderId = $('#contHeaderId').val();
    var billEventId = $('#billEventId').val();
    delBillEvent(contHeaderId,billEventId);
});

// 删除程序
function delBillEvent(contHeaderId,billEventId) {
        $.ajax({
            url : config.baseurl +"/api/invoiceApply/delBillEvent",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                billEventId : billEventId
            },
            type:"POST",
            beforeSend:function(){
                window.parent.showLoading();
            },
            success:function(data){
                if ("0000" == data.returnCode) {
                    window.parent.success("开票申请删除成功！")
                    $('#add_modal').on('shown.bs.modal', function (e) {}).modal('hide');
                    initInvoiceApply(contHeaderId);
                } else {
                    window.parent.error(data.returnMsg);
                }
            },
            complete:function(){
                window.parent.closeLoading();
            },
            error:function(){
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
}