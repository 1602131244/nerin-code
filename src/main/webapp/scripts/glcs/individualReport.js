/**
 * Created by Administrator on 2017/10/30.
 */

var config = new nerinJsConfig();
var reportHeaderDataSet = [];  
var reportHeaderData;
var reportHeaderId = 0;
var editFlag = 0;
var reportLinesDataSet = [];
var reportLinesData;
var reportLinesColumnData;
var columnList =[];
var columnDefsList =[];
var searchFlag = 0;

$(function () {

    // 初始化查询条件
    initLedgerSel ("1","1","0","");
    initCompanySel ("1","1","0","","");
    initReportSel ("1","0","0","","","")

    //初始化rangeTable数据
    initReportHeaderTables();

    // 模拟点击查询
    setTimeout(function(){
            $('#bu_query').trigger("click");}
        ,100
    );

});


//  初始化dataTables程序
var reportHeaderTable;
function initReportHeaderTables() {
    $('#dataTables-listReportHeader').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-reportHeaderData"></table>');

    reportHeaderTable = $('#dataTables-reportHeaderData').DataTable({
        "processing": true,
        "data": reportHeaderDataSet,
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

        "searching": true,
        "bLengthChange": false,
        "ordering ": true,

        // "bPaginate": true, //翻页功能
        "bLengthChange": true, //改变每页显示数据数量

        // "paging": false,  //禁止表格分页
        "lengthMenu": [ [20, 25, 50, -1], [20, 25, 50, "All"] ],

        // {"title": "<input type='checkbox' name='ch_listAll' id='ch_listAll' onclick='javascript:setCheckAll();' />", "data": null},
        "columns":[
            {"title": "", "data": null},           //0
            {"title": "序号", "data": null ,"className": "dt_center"},      //1
            {"title": "分类账", "data": "ledgerName" , "className": "dt_center"},   //2
            {"title": "公司", "data": "companyName" , "className": "dt_center"}, //3
            {"title": "期间", "data": "periodName" , "className": "dt_center"},  //4
            {"title": "报表名称", "data": "reportName" , "className": "dt_center"},  //5
            {"title": "报表说明", "data": "description" , "className": "dt_center"}, //6
            {"title": "报表状态", "data": "reportStatus" , "className": "dt_center"},//7
            {"title": "上报状态", "data": "submitStatus" , "className": "dt_center"},  //8
            {"title": "创建人", "data": "createByName" , "className": "dt_center"}, //9
            {"title": "创建日期", "data": "createDate" , "className": "dt_center"}, //10
            {"title": "审核人", "data": "approvalByName" , "className": "dt_center"},  //11
            {"title": "审核日期", "data": "approvalDate" , "className": "dt_center"},  //12
            {"title": "上报人", "data": "submitByName" , "className": "dt_center"},  //13
            {"title": "上报日期", "data": "submitDate" , "className": "dt_center"},  //14
            {"title": "包含未过账", "data": "postingFlag" , "className": "dt_center"},  //15
            {"title": "headerId", "data": "headerId" , "className": "dt_center"},  //16
            {"title": "rangeId", "data": "rangeId"}, //17
            {"title": "updateFlag", "data": "updateFlag"}, //18
            {"title": "deleteFlag", "data": "deleteFlag"}, //19
            {"title": "reportFlag", "data": "reportFlag"}, //20
            {"title": "approvalFlag", "data": "approvalFlag"} //21
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
            "orderable": true,
            "width": 200,
            "targets": 2
        },{
            "orderable": true,
            "width": 200,
            "targets": 3
        },{
            "orderable": true,
            "width": 50,
            "targets": 4
        },{
            "orderable": true,
            "width": 120,
            "targets": 5
        },{
            "orderable": true,
            "width": 180,
            "targets": 6
        },{
            "orderable": true,
            "width": 65,
            "targets": 7
        },{
            "orderable": true,
            "width": 65,
            "targets": 8
        },{
            "orderable": true,
            "width": 65,
            "targets": 9
        },{
            "orderable": true,
            "width": 90,
            "targets": 10
        },{
            "orderable": true,
            "width": 65,
            "targets": 11
        },{
            "orderable": true,
            "width": 90,
            "targets": 12
        },{
            "orderable": true,
            "width": 65,
            "targets": 13
        },{
            "orderable": true,
            "width": 90,
            "targets": 14
        },{
            "orderable": true,
            "width": 90,
            "targets": 15,
            "render": function (data, type, full, meta) {
                if ("1" == data)
                    return "是";
                else
                    return "否";
            }
        },{
            "visible": false,
            "width": 0,
            "targets": 16
        },{
            "visible": false,
            "width": 0,
            "targets": 17
        },{
            "visible": false,
            "width": 0,
            "targets": 18
        },{
            "visible": false,
            "width": 0,
            "targets": 19
        },{
            "visible": false,
            "width": 0,
            "targets": 20
        },{
            "visible": false,
            "width": 0,
            "targets": 21
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
            $(row).children('td').eq(13).attr('style', 'text-align: left;');
            $(row).children('td').eq(14).attr('style', 'text-align: left;');

            // $(row).children('td').eq(10).html("<input type='checkbox' id='ch_selectFlag' name='ch_selectFlag'  />");
        },
        "initComplete": function () {
        }
    });

    $('#dataTables-reportHeaderData tbody').on( 'click', 'tr', function () {
        var table = $('#dataTables-reportHeaderData').DataTable();
        if ( $(this).hasClass('dt-select') ) {
            $(this).removeClass('dt-select');
            var check = $(this).find("input[name='ch_list']");
            check.prop("checked", false);

            $('#bu_edit').attr('disabled',"disabled");
            $('#bu_del').attr('disabled',"disabled");
            $('#bu_approved').attr('disabled',"disabled");
            $('#bu_approved_un').attr('disabled',"disabled");
            $('#bu_sumbit').attr('disabled',"disabled");
        }
        else {
            var check = $('#dataTables-reportHeaderData').DataTable().$('tr.dt-select').find("input[name='ch_list']");
            check.prop("checked", false);
            $('#dataTables-reportHeaderData').DataTable().$('tr.dt-select').removeClass('dt-select');
            $(this).addClass('dt-select');
            var check = $(this).find("input[name='ch_list']");
            check.prop("checked", true);

            var table = $('#dataTables-reportHeaderData').DataTable();
            var headerData = table.rows(table.$('tr.dt-select')).data();
            if (headerData[0].submitStatus != '已上报' && headerData[0].submitStatus != '已接收'){
                if (headerData[0].reportStatus != '已审批'){
                    if (headerData[0].deleteFlag == '1') //删除权限
                        $('#bu_del').removeAttr('disabled');
                    else
                        $('#bu_del').attr('disabled',"disabled");

                    if (headerData[0].approvalFlag == '1')  //审批权限
                        $('#bu_approved').removeAttr('disabled');
                    else
                        $('#bu_approved').attr('disabled','disabled');

                    $('#bu_approved_un').attr('disabled','disabled');
                    $('#bu_sumbit').attr('disabled',"disabled");
                }else{
                    $('#bu_approved').attr('disabled','disabled');
                    if (headerData[0].approvalFlag == '1') //审批权限
                        $('#bu_approved_un').removeAttr('disabled');
                    else
                        $('#bu_approved_un').attr('disabled','disabled');

                    if (headerData[0].reportFlag == '1') //上报权限
                        $('#bu_sumbit').removeAttr('disabled');
                    else
                        $('#bu_sumbit').attr('disabled',"disabled");

                    $('#bu_del').attr('disabled',"disabled");
                }
            }else{
                $('#bu_del').attr('disabled',"disabled");
                $('#bu_sumbit').attr('disabled',"disabled");
                $('#bu_approved').attr('disabled','disabled');
                $('#bu_approved_un').attr('disabled','disabled');
            }
            $('#bu_edit').removeAttr('disabled');
        }
    } );

    reportHeaderTable.on( 'order.dt search.dt', function () {
        reportHeaderTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = "<input type='checkbox' name='ch_list' />";
        } ),
            reportHeaderTable.column(1, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } )
        ;
    } ).draw();
}

var reportLinesTable;
function initReportLinesTables() {
    $('#dataTables-listReportLines').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-reportLineData"></table>');

    reportLinesTable = $('#dataTables-reportLineData').DataTable({
        "processing": true,
        "data": reportLinesDataSet,
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

        "columns": columnList,
        // [
        //     {"title": "报表项", "data": "ledgerName" , "className": "dt_center"},   //2
        //     {"title": "序号", "data": "companyName" , "className": "dt_center"}, //3
        // ],
        "columnDefs":columnDefsList,
        // [{
        //     "searchable": false,
        //     "orderable": false,
        //     "targets": 0,
        //     "width": 32
        // },{
        //     "orderable": false,
        //     "width": 300,
        //     "targets": 1
        // },{
        //     "orderable": false,
        //     "width": 60,
        //     "targets": 2
        // }
        // ],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            $(row).children('td').eq(1).attr('style', 'text-align: left;');
            $(row).children('td').eq(2).attr('style', 'text-align: center;');
            // $(row).children('td').eq(3).attr('style', 'text-align: right;');
            // $(row).children('td').eq(4).attr('style', 'text-align: right;');

            $(columnList).each(function (index) {
                if(index>2){
                    $(row).children('td').eq(index).attr('style', 'text-align: right;');
                }
            });

            // $(row).children('td').eq(10).html("<input type='checkbox' id='ch_selectFlag' name='ch_selectFlag'  />");
            // if (editFlag == 1){
            //     $(row).children('td').eq(3).html("<input type='text' id='auditAmount3' id='auditAmount3' class='form-control input-sm' style='width: 100%; text-align: right;' value='" + data.amount01 + "' onKeyPress='if((event.keyCode<48 || event.keyCode>57) && event.keyCode!=46 || /\.\d\d$/.test(value))event.returnValue=false'/>");
            //     $(row).children('td').eq(4).html("<input type='text' id='auditAmount4' id='auditAmount4' class='form-control input-sm' style='width: 100%; text-align: right;' value='" + data.amount02 + "' onKeyPress='if((event.keyCode<48 || event.keyCode>57) && event.keyCode!=46 || /\.\d\d$/.test(value))event.returnValue=false'/>");
            //     $(row).children('td').eq(5).html("<input type='text' id='auditAmount5' id='auditAmount5' class='form-control input-sm' style='width: 100%; text-align: right;' value='" + data.amount02 + "' onKeyPress='if((event.keyCode<48 || event.keyCode>57) && event.keyCode!=46 || /\.\d\d$/.test(value))event.returnValue=false'/>");
            // }else{
            //     null;
            // }
        },
        "initComplete": function () {
        }
    });

    $('#dataTables-reportLineData tbody').on( 'click', 'tr', function () {
        var table = $('#dataTables-reportHeaderData').DataTable();
        if ( $(this).hasClass('dt-select') ) {
            $(this).removeClass('dt-select');
        }
        else {
            $('#dataTables-reportLineData').DataTable().$('tr.dt-select').removeClass('dt-select');
            $(this).addClass('dt-select');
        }
    } );

    $('#dataTables-reportLineData tbody').on( 'click', 'td', function () {
        //获取当前td的行位置
        var row=$(this).parent().prevAll().length;
        //获取当前td的列位置
        var col=$(this).prevAll().length;

        if (reportHeaderData.submitStatus != '已上报' && reportHeaderData.submitStatus != '已接收' && reportHeaderData.reportStatus != '已审批'){
            if (reportHeaderData.updateFlag == '1') {//有权限且状态可以更改才允许更改
                if (col > 2){
                    if (editFlag == 1){
                        //找到当前鼠标单击的td
                        var tdObj = $(this);
                        //保存原来的文本
                        var oldText = $(this).text();
                        //创建一个文本框
                        // var inputObj = $("<input type='text' value='" + commafyback(oldText) + "' onblur=\"check(this)\"  onkeyup=\"this.value=this.value.replace(/[^0-9.]/g,'')\"/>");
                        var inputObj = $("<input type='text' value='" + commafyback(oldText) + "' onKeyPress=\"if((event.keyCode<48 || event.keyCode>57) && event.keyCode!=46 || /\.\d\d$/.test(value))event.returnValue=false\"/>");
                        //去掉文本框的边框
                        inputObj.css("border-width", 0);
                        inputObj.click(function () {
                            return false;
                        });
                        //使文本框的宽度和td的宽度相同
                        inputObj.width(tdObj.width());
                        inputObj.height(tdObj.height());
                        //去掉文本框的外边距
                        inputObj.css("margin", 0);
                        inputObj.css("padding", 0);
                        inputObj.css("text-align", "right");
                        inputObj.css("font-size", "12px");
                        inputObj.css("background-color", tdObj.css("background-color"));
                        //把文本框放到td中
                        tdObj.html(inputObj);
                        //文本框失去焦点的时候变为文本
                        inputObj.blur(function () {
                            var newText = $(this).val();
                            newText = Math.round(newText*100)/100
                            tdObj.html(toThousands(newText));
                        });//保留两位小数
                        //全选
                        inputObj.trigger("focus").trigger("select");
                    }
                }
            }
        }
    } );

    reportLinesTable.on( 'order.dt search.dt', function () {
        // reportLinesTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
        //     cell.innerHTML = "<input type='checkbox' name='ch_list' />";
        // } ),
            reportLinesTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } )
        ;
    } ).draw();
}

// ----------------------------------------------------监控------------------------------------------------------
// 点击查询按钮
$('#bu_query').on('click', function (e) {
    queryReportHeader();
});

// 点击新增按钮
$('#bu_add').on('click', function (e) {
    reportHeaderData = null;
    initReportHeaderModal();
});

// 点击修改按钮
$('#bu_edit').on('click', function (e) {
    var table = $('#dataTables-reportHeaderData').DataTable();
    var data = table.rows(table.$('tr.dt-select')).data();
    if (data.length != 1){
        window.parent.warring("请选择一个数据再编辑");
        return;
    }else{
        reportHeaderId = data[0].headerId;
        // queryReportLines();
        getReportHeader('A',reportHeaderId)
    }
}
);

// 点击删除按钮
$('#bu_del').on('click', function (e) {
    var table = $('#dataTables-reportHeaderData').DataTable();
    var data = table.rows(table.$('tr.dt-select')).data();
    if (data.length != 1){
        window.parent.warring("请选择一个数据再删除");
        return;
    }
    window.parent.showConfirm(delReportHeader, "确认删除 " + data[0].description + "报表吗？");
});

// 点击审批按钮
$('#bu_approved').on('click', function (e) {
        var table = $('#dataTables-reportHeaderData').DataTable();
        var data = table.rows(table.$('tr.dt-select')).data();
        if (data.length != 1){
            window.parent.warring("请选择一个数据再审批");
            return;
        }else{
            reportHeaderId = data[0].headerId;
            // queryReportLines();
            upReportHeaderApproved('HEADER',reportHeaderId)
        }
    }
);

// 点击反审批按钮
$('#bu_approved_un').on('click', function (e) {
        var table = $('#dataTables-reportHeaderData').DataTable();
        var data = table.rows(table.$('tr.dt-select')).data();
        if (data.length != 1){
            window.parent.warring("请选择一个数据再反审批");
            return;
        }else{
            reportHeaderId = data[0].headerId;
            // queryReportLines();
            upReportHeaderReject('HEADER',reportHeaderId);
        }
    }
);

// 点击上报按钮
$('#bu_sumbit').on('click', function (e) {
        var table = $('#dataTables-reportHeaderData').DataTable();
        var data = table.rows(table.$('tr.dt-select')).data();
        if (data.length != 1){
            window.parent.warring("请选择一个数据再上报");
            return;
        }else{
            reportHeaderId = data[0].headerId;
            // queryReportLines();
            upReportHeaderSubmit('HEADER',reportHeaderId);
        }
    }
);

// 保存报表头
$('#saveReportHeader').on('click', function (e) {
    saveReportHeader();
});

// 点击变更参数
$('#bu_edit_header').on('click', function (e) {
    searchFlag = 1;
    getReportHeader('B', reportHeaderId);
});

// 点击重新计算
$('#bu_edit_line').on('click', function (e) {
    searchFlag = 1;
    reportCalculation(reportHeaderId);
});

// 点击审批按钮
$('#bu_approved_line').on('click', function (e) {
    searchFlag = 1;
    upReportHeaderApproved('LINE',reportHeaderId);
});

// 点击反审批按钮
$('#bu_approved_un_line').on('click', function (e) {
    searchFlag = 1;
    upReportHeaderReject('LINE', reportHeaderId);
});

// 点击上报按钮
$('#bu_sumbit_line').on('click', function (e) {
    searchFlag = 1;
    upReportHeaderSubmit('LINE',reportHeaderId);
});

// 点击上报按钮
$('#bu_save_line').on('click', function (e) {
    searchFlag = 1;
    saveReportLines();
});

// 监听编辑报表模态框隐藏时触发
$('#reportLines_modal').on('hide.bs.modal', function (e) {
    if (searchFlag == 1)
        $('#bu_query').trigger("click");
    searchFlag = 0;
});

// ----------------------------------------------------程序------------------------------------------------------
/**
 * 格式化数字(并保留两位小数)
 * @param num
 * @returns {string}
 */
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

/**
 * 格式化数字(取消千分位)
 */
function commafyback(num)
{
    var x = num.split(',');
    return parseFloat(x.join(""));
}

/**
 * 检查数字
 * @param e
 */
function check(e) {
    var re = /^\d+(?=\.{0,1}\d+$|$)/
    if (e.value != "") {
        if (!re.test(e.value)) {
            alert("请输入正确的数字");
            e.value = "";
            e.focus();
        }
    }
}

/**
 * 设置焦点
 * @param val
 * @param time
 * @constructor
 */
function Setfocus(val,time){
    if (time == undefined||''||null)
        vtime = 500;
    else
        vtime = time;

    setTimeout(function(){
            document.getElementById(val).focus()}
        ,vtime
    );
    ;
};


/**
 * 生成分类账下拉选择菜单
 * @param id           1  表示查询条件  2表示新增界面
 * @param checkSign    是否检查查询权限
 * @param checkCreate  是否检查新增权限
 * @param val           分类账值
 */
function initLedgerSel (id,checkSign,checkCreate,val) {
    $.ajax({
        url : config.baseurl +"/api/rangeRest/ledgerList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            checkSign: checkSign,
            checkCreate: checkCreate
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            // 1 表示查询条件
            if ("1" == id){
                // console.log("ledgerId:" + val);
                $("#searchLedgerId").empty();

                var initValue = "";
                var initText = "";
                $('#searchLedgerId').append("<option value='" + initValue + "'>" + initText + "," + initText+ "</option>");

                $(data.dataSource).each(function(index){
                    $("#searchLedgerId").append($('<option value="'+data.dataSource[index].ledgerId + '">'+data.dataSource[index].ledgerId + "," + data.dataSource[index].ledgerName+'</option>'));
                });

                $("#searchLedgerId").val(val);

            }else{
                // console.log("ledgerId:" + val);
                $("#ledgerId").empty();

                var chk = false;
                var ledgerId = "";

                var initValue = "";
                var initText = "";
                $('#ledgerId').append("<option value='" + initValue + "'>" + initText + "," + initText+ "</option>");

                $(data.dataSource).each(function(index){
                    $("#ledgerId").append($('<option value="'+data.dataSource[index].ledgerId + '">'+data.dataSource[index].ledgerId + "," + data.dataSource[index].ledgerName+'</option>'));
                    if (val == data.dataSource[index].ledgerId)
                        chk = true;
                });

                if (1 == data.dataTotal){
                    ledgerId = data.dataSource[0].ledgerId;
                }else{
                    if (chk){
                        ledgerId = val;
                    }
                }

                $("#ledgerId").val(ledgerId);

                initCompanySel("2",checkSign,checkCreate,val,reportHeaderData ? reportHeaderData.companyCode : "");

                if (editFlag == 0){
                    $('#ledgerId').attr('disabled',"disabled");
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

/**
 * 检索界面的分类账变动
 * @param val
 */
function searchLedgerChange(val) {
    var companyCode = $("#searchCompanyCode").val();
    var reportId = $("#searchReportId").val();
    initCompanySel("1","1","0",val,companyCode);
    if (val != ''||null)
        initReportSel ("1","1","0",val,"",reportId)
    else
        initReportSel ("1","0","0",val,"",reportId)
}

/**
 * 新增界面的分类账变动
 * @param val
 */
function ledgerChange(val) {
    var companyCode = $("#companyCode").val();
    var reportId = $("#reportId").val();
    var periodName = $("#periodName").val();
    //刷新公司下拉
    initCompanySel("2","1","1",val,companyCode);
    // 刷新期间下拉
    initPeriodNameSel (val,periodName);
    // 刷新报表下拉
    if (val != ''||null)
        initReportSel ("2","1","1",val,"",reportId)
    else
        $('#reportId').attr('disabled',"disabled");
}


/**
 *生成分类账下拉选择菜单
 * @param id             1 表示查询条件  2表示新增界面
 * @param checkSign     是否检查查询权限
 * @param checkCreate  是否检查新增权限
 * @param ledgerId      分类账
 * @param val           报表
 */
function initCompanySel (id,checkSign,checkCreate,ledgerId,val) {
    $.ajax({
        url : config.baseurl +"/api/rangeRest/ebsCompanyList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            checkSign: checkSign,
            checkCreate: checkCreate,
            ledgerId:        ledgerId
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            // 1 表示查询条件
            if ("1" == id){
                // console.log("companyCode:" + val);
                $("#searchCompanyCode").empty();

                var initValue = "";
                var initText = "";
                $('#searchCompanyCode').append("<option value='" + initValue + "'>" + initText + "," + initText+ "</option>");

                $(data.dataSource).each(function(index){
                    $("#searchCompanyCode").append($('<option value="'+data.dataSource[index].companyCode + '">'+data.dataSource[index].companyCode + "," + data.dataSource[index].companyName +'</option>'));
                });

                $("#searchCompanyCode").val(val);

                // initReportSel (id,checkSign,checkCreate,ledgerId,val,reportId)
            }else{
                // console.log("companyCode:" + val);
                $("#companyCode").empty();

                var chk = false;
                var companyCode = "";

                var initValue = "";
                var initText = "";
                $('#companyCode').append("<option value='" + initValue + "'>" + initText + "," + initText+ "</option>");

                $(data.dataSource).each(function(index){
                    $("#companyCode").append($('<option value="'+data.dataSource[index].companyCode + '">'+data.dataSource[index].companyCode + "," + data.dataSource[index].companyName +'</option>'));
                    if (val == data.dataSource[index].companyCode)
                        chk = true;
                });

                if (1 == data.dataTotal){
                    companyCode = data.dataSource[0].companyCode;
                }else{
                    if (chk){
                        companyCode = val;
                    }
                }
                $("#companyCode").val(companyCode);
                // $("#companyCode").val(val);

                // initReportSel("2", checkSign, checkCreate, reportHeaderData ? reportHeaderData.ledgerId: "", val, reportHeaderData ? reportHeaderData.reportId:"");
                var ledgerId = $("#ledgerId").val();
                var reportId ="";
                if ($("#reportId").val() == ""||null)
                    var reportId = reportHeaderData ? reportHeaderData.reportId:"";
                else
                    var reportId = $("#reportId").val();
                if ($("#companyCode").val() != ''||null){
                    $('#reportId').removeAttr('disabled');
                    initReportSel ("2","1","1", ledgerId, companyCode,reportId)
                } else{
                    $('#reportId').attr('disabled',"disabled");
                }

                if (editFlag == 0){
                    $('#companyCode').attr('disabled',"disabled");
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

/**
 * 检索界面的公司段变动
 * @param val  公司值
 */
function searchCompanyCodeChange(val) {
    var searchLedgerId = $("#searchLedgerId").val();
    var reportId = $("#searchReportId").val();
    if (searchLedgerId != ''||null)
        initReportSel ("1","1","0", searchLedgerId, val,reportId)
    else
        initReportSel ("1","0","0",searchLedgerId, val,reportId)
}

/**
 * 新增界面的公司下拉值变动
 * @param val  公司值
 */
function companyCodeChange(val) {
    var ledgerId = $("#ledgerId").val();
    var reportId = $("#reportId").val();
    if (val != ''||null){
        $('#reportId').removeAttr('disabled');
        initReportSel ("2","1","1", ledgerId, val,reportId)
    } else{
        $('#reportId').attr('disabled',"disabled");
    }
}

/**
 *生成报表下拉选择菜单
 * @param id             1 表示查询条件  2表示新增界面
 * @param checkSign     是否检查查询权限
 * @param checkCreate  是否检查新增权限
 * @param ledgerId      分类账
 * @param companyCode   公司
 * @param val           报表
 */
function initReportSel (id,checkSign,checkCreate,ledgerId,companyCode,val) {
    $.ajax({
        url : config.baseurl +"/api/glcsUserAssignment/ebsReportList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            checkSign:  checkSign,
            checkCreate:    checkCreate,
            ledgerId:   ledgerId,
            companyCode: companyCode
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            // 1 表示查询条件
            if ("1" == id) {
                // console.log("reportId:" + val);
                $("#searchReportId").empty();

                var initValue = "";
                var initText = "";
                $('#searchReportId').append("<option value='" + initValue + "'>" + initText + "</option>");

                $(data.dataSource).each(function (index) {
                    $("#searchReportId").append($('<option value="' + data.dataSource[index].reportId + '">' + data.dataSource[index].reportName + '</option>'));
                });

                $("#searchReportId").val(val);
            }else{
                // console.log("reportId:" + val);
                $("#reportId").empty();
                var chk = false;
                var reportId = "";

                var initValue = "";
                var initText = "";
                $('#reportId').append("<option value='" + initValue + "'>" + initText + "</option>");

                $(data.dataSource).each(function (index) {
                    $("#reportId").append($('<option value="' + data.dataSource[index].reportId + '">' + data.dataSource[index].reportName + '</option>'));
                    if (val == data.dataSource[index].reportId)
                        chk = true;
                });

                if (1 == data.dataTotal){
                    reportId = data.dataSource[0].reportId;
                }else{
                    if (chk){
                        reportId = val;
                    }
                }

                $("#reportId").val(reportId);
                // $("#reportId").val(val);

                if ((reportId != ""||null) && ($('#periodName').val() != ""||null) && ($('#description').val() == ""||null)){
                    $('#description').val($("#reportId").find("option:selected").text());
                }

                if (editFlag == 0){
                        $('#initReportSel').attr('disabled',"disabled");
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

function reportChange(val) {
    if ((val != ""||null)  && ($('#periodName').val() != ""||null)  && ($('#description').val() == ""||null) ){
        $('#description').val($('#periodName').val() + " " + $("#reportId").find("option:selected").text());
    }
}

function periodNameChange(val) {
    if ((val != ""||null) && ($('#reportId').val() != ""||null)  && ($('#description').val() == ""||null)){
        $('#description').val($('#periodName').val() + " " + $("#reportId").find("option:selected").text());
    }
}


/**
 * 生成期间下拉选择菜单
 * @param ledgerId 分类账ID
 * @param val  期间
 */
function initPeriodNameSel (ledgerId,val) {
    $.ajax({
        url : config.baseurl +"/api/report/periodNameList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            ledgerId:   ledgerId
            // periodName: val
        },
        type:"GET",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            // console.log("reportId:" + val);
            $("#periodName").empty();
            var chk = false;
            var periodName = "";

            var initValue = "";
            var initText = "";
            $('#periodName').append("<option value='" + initValue + "'>" + initText + "</option>");

            $(data.dataSource).each(function (index) {
                $("#periodName").append($('<option value="' + data.dataSource[index].periodName + '">' + data.dataSource[index].periodName + '</option>'));
                if (val == data.dataSource[index].periodName)
                    chk = true;
            });

            if (1 == data.dataTotal){
                periodName = data.dataSource[0].periodName;
            }else{
                if (chk){
                    periodName = val;
                }
            }

            $("#periodName").val(periodName);
            // $("#periodName").val(val);.

            if (editFlag == 0){
                    $('#periodName').attr('disabled',"disabled");
            }
        },
        complete:function(){
            window.parent.closeLoading();
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

/**
 * 查询报表主表记录
 */
function queryReportHeader() {
    $.ajax({
        url : config.baseurl +"/api/report/reportHeaderList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            ledgerId:        $('#searchLedgerId').val(),
            companyCode:        $('#searchCompanyCode').val(),
            reportId:        $('#searchReportId').val(),
            periodName:        $('#searchPeriodName').val(),
        },
        type:"GET",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            reportHeaderDataSet = data.dataSource;
            console.log(reportHeaderDataSet);

            //初始化
            initReportHeaderTables();

        },
        complete:function(){
            window.parent.closeLoading();
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}


/**
 * 显示新增窗口
 */
function initReportHeaderModal() {
    //全部清空
    $('#headerId').val("");
    $('#ledgerId').val("");
    $('#companyCode').val("");
    $('#periodName').val("");
    $('#reportId').val("");
    $('#description').val("");

    // 执行初始化
    $('#headerId').val(reportHeaderData ? reportHeaderData.headerId : "");
    $('#description').val(reportHeaderData ? reportHeaderData.description : "");

    if (reportHeaderData) {
        if (reportHeaderData.postingFlag == "1")
            $("#postingFlag").prop("checked", "checked")
        else
            $("#postingFlag").prop("checked", false)
    }

    // 只有编辑时，报表状态为已审批，上报状态为已上报、已接收状态的不允许修改，其他的都允许修改
    if (reportHeaderData){
        if (reportHeaderData.reportStatus == '已审批'||reportHeaderData.submitStatus == '已上报'||reportHeaderData.submitStatus == '已接收'){
            $('#postingFlag').attr('disabled',"disabled");
            editFlag = 0;
        }else{
            $('#postingFlag').removeAttr('disabled');
            editFlag = 1;
        }
    }else {
        $('#postingFlag').removeAttr('disabled');
        editFlag = 1;
    }

    initLedgerSel ("2","1","1",reportHeaderData ? reportHeaderData.ledgerId : "");
    initPeriodNameSel (reportHeaderData ? reportHeaderData.ledgerId : "",reportHeaderData ? reportHeaderData.periodName : "")

    document.getElementById("addReportHeaderLabel").innerHTML=(reportHeaderData ? "编辑个别报表" :  "新增个别报表");

    //隐藏明细界面
    if(reportHeaderData){
        $('#reportLines_modal').on('shown.bs.modal', function (e) {}).modal('hide');
    }

    // 显示新增界面
    $('#reportHeader_modal').modal('show');
}

/**
 * 保存并计算报表
 */
function saveReportHeader() {
    var o = new Object();

    o.headerId = $('#headerId').val();
    o.type = "单体报表";
    o.ledgerId = $('#ledgerId').val();
    o.companyCode = $('#companyCode').val();
    o.periodName = $('#periodName').val();
    o.reportId = $('#reportId').val();
    o.description = $('#description').val();

    if ($("#postingFlag").is(":checked"))
        o.postingFlag = "1";
    else
        o.postingFlag = "0";

    if ("" ==  o.ledgerId.trim()) {
        window.parent.error("请选择分类账！",3000);
        Setfocus('ledgerId',3100);
        return;
    }

    if ("" ==  o.companyCode.trim()) {
        window.parent.error("请选择公司！",3000);
        Setfocus('companyCode',3100);
        return;
    }

    if ("" ==  o.periodName.trim()) {
        window.parent.error("请选择期间！",3000);
        Setfocus('periodName',3100);
        return;
    }

    if ("" ==  o.reportId.trim()) {
        window.parent.error("请选择报表！",3000);
        Setfocus('reportId',3100);
        return;
    }
    if ("" ==  o.description.trim()) {
        window.parent.error("请选择输入报表说明！",3000);
        Setfocus('description',3100);
        return;
    }


    console.log(JSON.stringify(o));

    $.ajax({
        url : config.baseurl +"/api/report/saveReportHeader",
        contentType : 'application/json',
        dataType:"json",
        data: JSON.stringify(o),
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("个别报表保存成功！")
                $('#reportHeader_modal').on('shown.bs.modal', function (e) {}).modal('hide');

                // 模拟查询
                // $('#bu_query').trigger("click");
                reportHeaderId = data.db_sid;
                getReportHeader('C',reportHeaderId)
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

/**
 * 查询报表主表记录
 * @param type A 表示编辑报表   B表示编辑表头（变更参数）  C表示新增或者变更完参数后编辑报表
 * @param val 报表头ID
 */
function getReportHeader(type,val) {
    $.ajax({
        url : config.baseurl +"/api/report/reportHeaderList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            headerId : val
        },
        type:"GET",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            reportHeaderData = data.dataSource[0];
            console.log(reportHeaderDataSet);

            //只有编辑时，报表状态为已审批，上报状态为已上报、已接收状态的不允许修改，其他的都允许修改
            if (reportHeaderData){
                if ((reportHeaderData.reportStatus == '已审批')||(reportHeaderData.submitStatus == '已上报')||(reportHeaderData.submitStatus == '已接收')){
                    $('#postingFlag').attr('disabled',"disabled");
                    editFlag = 0;
                }else{
                    $('#postingFlag').removeAttr('disabled');
                    editFlag = 1;
                }
            }else {
                $('#postingFlag').removeAttr('disabled');
                editFlag = 1;
            }

            if (type == 'B'){
                initReportHeaderModal();
            }
            else{
                queryReportLines(reportHeaderData.headerId,reportHeaderData.reportId);
                document.getElementById("ledgerNameList").innerHTML=(reportHeaderData.ledgerName);
                document.getElementById("reportNameList").innerHTML=(reportHeaderData.reportName);
                document.getElementById("periodNameList").innerHTML=(reportHeaderData.periodName);
                document.getElementById("companyNameList").innerHTML=(reportHeaderData.companyName);
                document.getElementById("reportStatusList").innerHTML=(reportHeaderData.reportStatus + '('+ data.dataSource[0].submitStatus +')');
                document.getElementById("postingFlagList").innerHTML=(reportHeaderData.postingFlag == "1" ? "是":"否");
            };

        },
        complete:function(){
            window.parent.closeLoading();
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

/**
 * 查询报表行的标题等相关信息，动态生成标题成功后执行查询行操作
 * @param headerId  报表头ID
 * @param val   报表ID
 */
function queryReportLines(headerId,val) {
    $.ajax({
        url : config.baseurl +"/api/report/reportColumnList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            // reportId:        headerData[0].reportId
            reportId:        val
        },
        type:"GET",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            reportLinesColumnData = data.dataSource;
            console.log(reportLinesColumnData);

            var o;
            var a;

            columnList = [];
            columnDefsList = [];

            columnList.push({"title": "行号", "data": null ,"className": "dt_center"});
            columnList.push({"title": "报表项名称", "data": "rowName" ,"className": "dt_center"});
            columnList.push({"title": "报表序号", "data": "rowNumber" , "className": "dt_center"});

            columnDefsList.push({
                "searchable": false,
                "orderable": false,
                "targets": 0,
                "width": 32
            });
            columnDefsList.push(
                {
                    "orderable": false,
                    "width": 300,
                    "targets": 1
                }
            );
            columnDefsList.push(
                {
                    "orderable": false,
                    "width": 60,
                    "targets": 2
                }
            );


            $(data.dataSource).each(function (index) {
                o = new Object();

                o.title = data.dataSource[index].columnName;
                o.data = data.dataSource[index].columnData;
                o.className = "dt_center";

                columnList.push(o);

                a = new Object();

                a.searchable = false;
                a.orderable = false;
                a.width = "160";
                a.targets = index + 3;

                columnDefsList.push(a);
            });

            setTimeout(function(){
                    reportLinesDataSet = [];
                    //初始化
                    initReportLinesTables();

                    initReportLines(headerId);
                }
                ,500
            );

        },
        complete:function(){
            // window.parent.closeLoading();
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

/**
 * 查询报表行记录并显示界面
 * @param val  表表头ID
 */
function initReportLines(val) {
    $.ajax({
        url : config.baseurl +"/api/report/reportLineList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            // headerId:        headerData[0].headerId
            headerId:        val
        },
        type:"GET",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            reportLinesDataSet = data.dataSource;
            console.log(reportLinesDataSet);

            //初始化
            setTimeout(function(){
                    initReportLinesTables();}
                ,1000
            );


            if (editFlag == 0){ //报表状态是否可以更改
                $('#bu_edit_header').attr('disabled',"disabled");
                $('#bu_edit_line').attr('disabled',"disabled");
                $('#bu_save_line').attr('disabled',"disabled");
            }else{
                if (reportHeaderData.updateFlag == '1'){  //是否有权限更改
                    $('#bu_edit_header').removeAttr('disabled');
                    $('#bu_edit_line').removeAttr('disabled');
                    $('#bu_save_line').removeAttr('disabled');
                }else{
                    $('#bu_edit_header').attr('disabled',"disabled");
                    $('#bu_edit_line').attr('disabled',"disabled");
                    $('#bu_save_line').attr('disabled',"disabled");
                }
            }

            if (reportHeaderData.submitStatus != '已上报' && reportHeaderData.submitStatus != '已接收'){
                if (reportHeaderData.reportStatus != '已审批'){
                    if (reportHeaderData.approvalFlag == '1')  //审批权限
                        $('#bu_approved_line').removeAttr('disabled');
                    else
                        $('#bu_approved_line').attr('disabled','disabled');
                    $('#bu_approved_un_line').attr('disabled','disabled');
                    $('#bu_sumbit_line').attr('disabled',"disabled");
                }else{
                    $('#bu_approved_line').attr('disabled','disabled');
                    if (reportHeaderData.approvalFlag == '1') //审批权限
                        $('#bu_approved_un_line').removeAttr('disabled');
                    else
                        $('#bu_approved_un_line').attr('disabled','disabled');
                    if (reportHeaderData.reportFlag == '1') //上报权限
                        $('#bu_sumbit_line').removeAttr('disabled');
                    else
                        $('#bu_sumbit_line').attr('disabled',"disabled");
                }
            }else{
                $('#bu_approved_line').attr('disabled');
                $('#bu_approved_un_line').attr('disabled','disabled');
                $('#bu_sumbit_line').attr('disabled',"disabled");
            }


            // 显示新增界面
            $('#reportLines_modal').modal('show');
        },
        complete:function(){
            window.parent.closeLoading();
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

/**
 * 删除报表程序
 */
function delReportHeader() {
    var table = $('#dataTables-reportHeaderData').DataTable();
    var data = table.rows(table.$('tr.dt-select')).data();
    $.ajax({
        url : config.baseurl +"/api/report/delReportHeader",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            headerId : data[0].headerId
        },
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("报表删除成功！")

                // 模拟查询
                $('#bu_query').trigger("click");

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

/**
 * 重新计算报表
 * @param val  报表头ID
 */
function reportCalculation(val) {
    $.ajax({
        url : config.baseurl +"/api/report/createReportLines",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            headerId : val
        },
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("报表计算成功！")
                    getReportHeader('A',val)
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

/**
 * 审核报表
 * @param type  HEADER 报表头按钮  LINE  报表行按钮
 * @param val  报表头ID
 */
function upReportHeaderApproved(type,val) {
    $.ajax({
        url : config.baseurl +"/api/report/reportHeaderApproved",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            headerId : val
        },
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("报表审批成功！")
                if(type == 'HEADER'){
                    // 模拟查询
                    $('#bu_query').trigger("click");
                }else{
                    getReportHeader('A',val)
                }
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


/**
 * 反审核报表
 * @param type  HEADER 报表头按钮  LINE  报表行按钮
 * @param val  报表头ID
 */
function upReportHeaderReject(type,val) {
    $.ajax({
        url : config.baseurl +"/api/report/reportHeaderReject",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            headerId : val
        },
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("报表反审批成功！")
                if(type == 'HEADER'){
                    // 模拟查询
                    $('#bu_query').trigger("click");
                }else{
                    getReportHeader('A',val)
                }
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

/**
 * 报表上报
 * @param type  HEADER 报表头按钮  LINE  报表行按钮
 * @param val  报表头ID
 */
function upReportHeaderSubmit(type,val) {
    $.ajax({
        url : config.baseurl +"/api/report/reportHeaderSubmit",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            headerId : val
        },
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("报表上报成功！")
                if(type == 'HEADER'){
                    // 模拟查询
                    $('#bu_query').trigger("click");
                }else{
                    getReportHeader('A',val)
                }
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

/**
 * 遍历表格内容返回数组
 * @param Int  id 表格id
 * @return Array
 */
function getTableContent(id){
    var mytable = document.getElementById(id);
    var data = [];
    for(var i=0,rows=mytable.rows.length; i<rows; i++){
        for(var j=0,cells=mytable.rows[i].cells.length; j<cells; j++){
            if(!data[i]){
                data[i] = new Array();
            }
            data[i][j] = mytable.rows[i].cells[j].innerHTML;
        }
    }
    return data;
}

/**
 *保存报表行记录
 */
function saveReportLines() {
    var table = $('#dataTables-reportLineData').DataTable();
    var data = table.rows().data();  //获得DATATABLE数据

    var linesData = getTableContent('dataTables-reportLineData');  //抓取HTML表格数据

    var o;
    var arr = new Array();
    var lno = -1 ;

    for( i=0;i<data.length;i++)  {
        for( j=0;j<reportLinesColumnData.length;j++) { //根据标题数组循环
            o = new Object();
            o.headerId = data[i].headerId;
            o.rowNumber = data[i].rowNumber;
            o.columnNumber = reportLinesColumnData[j].columnCode;
            o.amount = commafyback(linesData[i+1][j+3]);  // I+1  剔除标题行  J+3  剔除表格前三列数据

            arr.push(o);
        }
    };
    console.log(JSON.stringify(arr));


    $.ajax({
        url: config.baseurl + "/api/report/saveReportLine",
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: JSON.stringify(arr),
        type: "POST",
        beforeSend: function () {
            window.parent.showLoading();
        },
        success: function (data) {
            if ("0000" == data.returnCode) {
                window.parent.success("报表保存成功！")

                getReportHeader('C',reportHeaderId);

                //隐藏明细界面
                // $('#reportLines_modal').on('shown.bs.modal', function (e) {}).modal('hide');
                // 模拟查询
                // $('#bu_query').trigger("click");

            } else {
                window.parent.error(data.returnMsg);
            }
        },
        complete: function () {
            window.parent.closeLoading();
        },
        error: function () {
            window.parent.closeLoading();
            window.parent.error(ajaxError_sys);
        }
    });
}
