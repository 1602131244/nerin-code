/**
 * Created by Administrator on 2017/10/19.
 */

var config = new nerinJsConfig();
var userAssignmentDataSet = [];

var userAssignmentData;

$(function () {
    // $(".selectpicker").selectpicker('hide');

    //初始化rangeTable数据
    initUserAssignmentTables();

    //初始化模态框日期格式
    // $('#datetimepicker1').datetimepicker({
    //     format: 'YYYY-MM-DD'
    // });

    // 模拟点击查询
    setTimeout(function(){
            $('#bu_query').trigger("click");}
        ,100
    );

});

// 初始化dataTables程序
var userAssignmentTable;
function initUserAssignmentTables() {
    $('#dataTables-listUserAssienment').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-userAssignmentData"></table>');

    userAssignmentTable = $('#dataTables-userAssignmentData').DataTable({
        "processing": true,
        "data": userAssignmentDataSet,
        "scrollX": true,
        // "scrollX": "100%",
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
        // "paging": false,  //禁止表格分页

        // {"title": "<input type='checkbox' name='ch_listAll' id='ch_listAll' onclick='javascript:setCheckAll();' />", "data": null},
        "columns": [
            {"title": "", "data": null},           //0
            {"title": "序号", "data": null ,"className": "dt_center"},      //1
            {"title": "用户</br>编号", "data": "userNumber" , "className": "dt_center"},   //2
            {"title": "用户</br>名称", "data": "userName" , "className": "dt_center"}, //3
            {"title": "合并范围", "data": "rangeName" , "className": "dt_center"},  //4
            {"title": "版本", "data": "rangeVerson" , "className": "dt_center"},  //5
            {"title": "合并单元", "data": "unitName" , "className": "dt_center"}, //6
            {"title": "分类账", "data": "ledgerName" , "className": "dt_center"},//7
            {"title": "公司", "data": "companyName" , "className": "dt_center"},  //8
            {"title": "报表名称", "data": "reportName" , "className": "dt_center"}, //9
            {"title": "查看</br>权限", "data": "selectFlag" , "className": "dt_center"},  //10
            {"title": "制作</br>权限", "data": "insertFlag" , "className": "dt_center"},  //11
            {"title": "编辑</br>权限", "data": "updateFlag" , "className": "dt_center"},  //12
            {"title": "删除</br>权限", "data": "deleteFlag" , "className": "dt_center"},  //13
            {"title": "审核</br>权限", "data": "approvalFlag" , "className": "dt_center"},  //14
            {"title": "上报</br>权限", "data": "reportFlag" , "className": "dt_center"},  //15
            {"title": "接收</br>权限", "data": "receiveFlag" , "className": "dt_center"},  //16

            {"title": "assignmentId", "data": "assignmentId" , "className": "dt_center"},  //17
            {"title": "userId", "data": "userId" , "className": "dt_center"},  //18
            {"title": "rangeId", "data": "rangeId" , "className": "dt_center"},  //19
            {"title": "unitId", "data": "unitId" , "className": "dt_center"},  //20
            {"title": "ledgerId", "data": "ledgerId" , "className": "dt_center"},  //21
            {"title": "companyCode", "data": "companyCode" , "className": "dt_center"},  //22
            {"title": "reportId", "data": "reportId" , "className": "dt_center"},  //23

            // {"title": "rangeId", "data": "rangeId"} //10
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
            "width": 50,
            "targets": 2
        },{
            "orderable": true,
            "width": 50,
            "targets": 3
        },{
            "orderable": true,
            "width": 200,
            "targets": 4
        },{
            "orderable": true,
            "width": 32,
            "targets": 5
        },{
            "orderable": true,
            "width": 200,
            "targets": 6
        },{
            "orderable": true,
            "width": 120,
            "targets": 7
        },{
            "orderable": true,
            "width": 200,
            "targets": 8
        },{
            "orderable": true,
            "width": 120,
            "targets": 9
        },{
            "orderable": true,
            "width": 50,
            "targets": 10,
            "render": function (data, type, full, meta) {
                if ("1" == data)
                    return "√";
                else
                    return "";
            }
        },{
            "orderable": true,
            "width": 50,
            "targets": 11,
            "render": function (data, type, full, meta) {
                if ("1" == data)
                    return "√";
                else
                    return "";
            }
        },{
            "orderable": true,
            "width": 50,
            "targets": 12,
            "render": function (data, type, full, meta) {
                if ("1" == data)
                    return "√";
                else
                    return "";
            }
        },{
            "orderable": true,
            "width": 50,
            "targets": 13,
            "render": function (data, type, full, meta) {
                if ("1" == data)
                    return "√";
                else
                    return "";
            }
        },{
            "orderable": true,
            "width": 50,
            "targets": 14,
            "render": function (data, type, full, meta) {
                if ("1" == data)
                    return "√";
                else
                    return "";
            }
        },{
            "orderable": true,
            "width": 50,
            "targets": 15,
            "render": function (data, type, full, meta) {
                if ("1" == data)
                    return "√";
                else
                    return "";
            }
        },{
            "orderable": true,
            "width": 50,
            "targets": 16,
            "render": function (data, type, full, meta) {
                if ("1" == data)
                    return "√";
                else
                    return "";
            }
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
        },{
            "visible": false,
            "width": 0,
            "targets": 22
        },{
            "visible": false,
            "width": 0,
            "targets": 23

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
            $(row).children('td').eq(10).attr('style', 'text-align: center;');
            $(row).children('td').eq(11).attr('style', 'text-align: center;');
            $(row).children('td').eq(12).attr('style', 'text-align: center;');
            $(row).children('td').eq(13).attr('style', 'text-align: center;');
            $(row).children('td').eq(14).attr('style', 'text-align: center;');
            $(row).children('td').eq(15).attr('style', 'text-align: center;');
            $(row).children('td').eq(16).attr('style', 'text-align: center;');

            // $(row).children('td').eq(10).html("<input type='checkbox' id='ch_selectFlag' name='ch_selectFlag'  />");
        },
        "initComplete": function () {
        }
        // ,fixedColumns : {//关键是这里了，需要前4列不滚动就设置4
        //     leftColumns : 4
        // }
    });

    $('#dataTables-userAssignmentData tbody').on( 'click', 'tr', function () {
        var table = $('#dataTables-userAssignmentData').DataTable();
        if ( $(this).hasClass('dt-select') ) {
            $(this).removeClass('dt-select');
            var check = $(this).find("input[name='ch_list']");
            check.prop("checked", false);

            $('#bu_edit').attr('disabled',"disabled");
            $('#bu_del').attr('disabled',"disabled");
        }
        else {
            var check = $('#dataTables-userAssignmentData').DataTable().$('tr.dt-select').find("input[name='ch_list']");
            check.prop("checked", false);
            $('#dataTables-userAssignmentData').DataTable().$('tr.dt-select').removeClass('dt-select');
            $(this).addClass('dt-select');
            var check = $(this).find("input[name='ch_list']");
            check.prop("checked", true);

            $('#bu_edit').removeAttr('disabled');
            $('#bu_del').removeAttr('disabled');
        }
    } );

    userAssignmentTable.on( 'order.dt search.dt', function () {
        userAssignmentTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = "<input type='checkbox' name='ch_list' />";
        } ),
        userAssignmentTable.column(1, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
        } )
        ;
    } ).draw();
}

// ----------------------------------------------------按钮监控------------------------------------------------------
// 点击查询按钮
$('#bu_query').on('click', function (e) {
    queryUserAsiignment();
});

// 点击新增按钮
$('#bu_add').on('click', function (e) {
    userAssignmentData = null;
    initUserAsiignmentModal();
});

// 点击修改按钮
$('#bu_edit').on('click', function (e) {
    userAssignmentData = null;
    getUserAsiignmentData();
});

// 点击删除按钮
$('#bu_del').on('click', function (e) {
    // delRange() ;
    var table = $('#dataTables-userAssignmentData').DataTable();
    var data = table.rows(table.$('tr.dt-select')).data();
    if (data.length != 1){
        window.parent.warring("请选择一个数据再删除");
        return;
    }
    window.parent.showConfirm(delUserAsiignment, "确认删除 " + data[0].userNumber + " " + data[0].userName + "的权限分配吗？");
});

// 保存
$('#saveUserAssignment').on('click', function (e) {
    saveUserAssignment();
});

// ---------------------------------------------主界面程序---------------------------------------------------------
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
 * 查询分配权限记录
 */
function queryUserAsiignment() {
    $.ajax({
        url : config.baseurl +"/api/glcsUserAssignment/userAssignmentList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            search:        $('#userAssignmentSear').val()
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            userAssignmentDataSet = data.dataSource;
            console.log(userAssignmentDataSet);

            // userAssignmentDataSet.forEach(function(e){
            // });

            for(j = 0,len=userAssignmentDataSet.length; j < len; j++) {
                if (userAssignmentDataSet[j].rangeVerson == "0")
                    userAssignmentDataSet[j].rangeVerson = ""
            }

            //初始化
            initUserAssignmentTables();

        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

/**
 * 编辑时查询一条记录
 */
function getUserAsiignmentData() {
    var table = $('#dataTables-userAssignmentData').DataTable();
    var data = table.rows(table.$('tr.dt-select')).data();    
    $.ajax({
        url : config.baseurl +"/api/glcsUserAssignment/userAssignmentList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            assignmentId : data[0].assignmentId
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            userAssignmentData = data.dataSource[0];
            console.log(userAssignmentData);

            if (userAssignmentData.rangeId == 0)
                userAssignmentData.rangeId =""

            if (userAssignmentData.rangeVerson == 0)
                userAssignmentData.rangeVerson =""

            if (userAssignmentData.unitId == 0)
                userAssignmentData.unitId =""

            if (userAssignmentData.ledgerId == 0)
                userAssignmentData.ledgerId =""

            if (userAssignmentData.reportId == 0)
                userAssignmentData.reportId =""

            //初始化
            initUserAsiignmentModal();

        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

/**
 * 显示新增窗口
 */
function initUserAsiignmentModal() {
    //隐藏下拉菜单
    // $("#sel_user").selectpicker('hide');
    $(".selectpicker").selectpicker('hide');

    //全部清空
    $('#assignmentId').val("");
    $('#user').val("");
    $('#userId').val("");
    $('#reportId').val("");
    $('#rangeId').val("");
    $('#unitId').val("");
    $('#ledgerId').val("");
    $('#companyCode').val("");

    // $("#selectFlag").attr("checked", true)
    // $("#insertFlag").attr("checked", true)
    // $("#updateFlag").attr("checked", true)
    // $("#deleteFlag").attr("checked", true)
    // $("#reportFlag").attr("checked", true)
    // $("#approvalFlag").attr("checked", true)
    // $("#receiveFlag").attr("checked", true)

    if (userAssignmentData){
        if (userAssignmentData.selectFlag == "1")
            $("#selectFlag").prop("checked", "checked")
        else
            $("#selectFlag").prop("checked", false)

        if (userAssignmentData.insertFlag == "1")
            $("#insertFlag").prop("checked", "checked")
        else
            $("#insertFlag").prop("checked", false)

        if (userAssignmentData.updateFlag == "1")
            $("#updateFlag").prop("checked", "checked")
        else
            $("#updateFlag").prop("checked", false)

        if (userAssignmentData.deleteFlag == "1")
            $("#deleteFlag").prop("checked", "checked")
        else
            $("#deleteFlag").prop("checked", false)

        if (userAssignmentData.reportFlag == "1")
            $("#reportFlag").prop("checked", "checked")
        else
            $("#reportFlag").prop("checked", false)

        if (userAssignmentData.approvalFlag == "1")
            $("#approvalFlag").prop("checked", "checked")
        else
            $("#approvalFlag").prop("checked", false)

        if (userAssignmentData.receiveFlag == "1")
            // $("#receiveFlag").attr("checked", true)
            $("#receiveFlag").prop("checked", true)
        else
            // $("#receiveFlag").attr("checked", false)
            $("#receiveFlag").prop("checked", false)

    }
    else{
        $("#selectFlag").prop("checked", false)
        $("#insertFlag").prop("checked", false)
        $("#updateFlag").prop("checked", false)
        $("#deleteFlag").prop("checked", false)
        $("#reportFlag").prop("checked", false)
        $("#approvalFlag").prop("checked", false)
        $("#receiveFlag").prop("checked", false)
    }

    // 执行初始化
    $('#assignmentId').val(userAssignmentData ? userAssignmentData.assignmentId : "");
    $('#userId').val(userAssignmentData ? userAssignmentData.userId : "");
    $('#user').val(userAssignmentData ? userAssignmentData.userNumber +' '+ userAssignmentData.userName  : "");
    // $('#reportId').val(userAssignmentData ? userAssignmentData.reportId : "");
    // $('#rangeId').val(userAssignmentData ? userAssignmentData.rangeId + '_' + userAssignmentData.rangeVerson : "");
    // $('#unitId').val(userAssignmentData ? userAssignmentData.unitId : "");
    // $('#ledgerId').val(userAssignmentData ? userAssignmentData.ledgerId : "");
    // $('#companyCode').val(userAssignmentData ? userAssignmentData.companyCode : "");
    
    // initUserSel ($('#userId').val());
    initReportSel (userAssignmentData ? userAssignmentData.reportId : "");
    initRangeSel(userAssignmentData ? userAssignmentData.rangeId + '_' + userAssignmentData.rangeVerson : "");
    // initUnitSel(val);
    initLedgerSel (userAssignmentData ? userAssignmentData.ledgerId : "");
    // initCompanySel (val);

    document.getElementById("addRangeLabel").innerHTML=(userAssignmentData ? "编辑用户权限分配信息" :  "新增用户权限分配信息");

    // 显示新增界面
    $('#userAsiignment_modal').modal('show');
}


/**
 * 点击用户选择按钮
 */
$('#b_searchUser').on('click', function (e) {
    $.ajax({
        url : config.baseurl +"/api/glcsUserAssignment/ebsUserList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            initUserSel(data.dataSource);
        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
});


/**
 * 生成用户下拉选择菜单
 * @param data
 */
function initUserSel (data) {
    $("#sel_user").empty();

    $(data).each(function(index){
        $("#sel_user").append($('<option value="'+data[index].userId + '">'+data[index].userNumber + "," + data[index].userName+'</option>'));
    });

    if ($('#userId').val()){
        $('#sel_user').selectpicker('val',($('#userId').val()));  //默认值
    }
    $("#sel_user").selectpicker('refresh');
    $("#sel_user").selectpicker('show');    //显示出来

    $('button[data-id="sel_user"]').trigger("click");
}


/**
 * 当用户选择按钮下拉隐藏时
 */
$("#sel_user").on('hidden.bs.select', function (e) {
    $('#userId').val($('#sel_user').val().split(",")[0]);
    var sTest = $('#sel_user option:selected').text();
    $('#user').val(sTest.split(",")[0] + ' ' + sTest.split(",")[1]);

    $("#sel_user").selectpicker('hide');
});


/**
 * 生成报表下拉选择菜单
 * @param val
 */
function initReportSel (val) {
    $.ajax({
        url : config.baseurl +"/api/glcsUserAssignment/ebsReportList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            // console.log("reportId:" + val);
            $("#reportId").empty();

            var initValue = "";
            var initText = "";
            $('#reportId').append("<option value='" + initValue + "'>" + initText  +  "</option>");

            $(data.dataSource).each(function(index){
                $("#reportId").append($('<option value="'+data.dataSource[index].reportId + '">'+data.dataSource[index].reportName + '</option>'));
            });

            $("#reportId").val(val);
        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

/**
 * 查询合并范围
 * @param val
 */
function initRangeSel(val) {
    $.ajax({
        url : config.baseurl +"/api/rangeRest/rangeList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            // console.log("rangeId:" + val);
            // rangeDataSet = data.dataSource;
            $("#rangeId").empty();

            var initValue = "";
            var initText = "";
            // $('#rangeId').append("<option value='" + initValue + "'>" + initText + "," + initText + "," + initText +  "</option>");
            $('#rangeId').append("<option value='" + initValue + "'>" + initText + "," + initText +  "</option>");

            $(data.dataSource).each(function(index){
                // $("#rangeId").append($('<option value="'+data.dataSource[index].rangeId + '">'+data.dataSource[index].rangeNumber + "," +data.dataSource[index].rangeName + "," + data.dataSource[index].verson+'</option>'));
                $("#rangeId").append($('<option value="'+data.dataSource[index].rangeId +'_'+data.dataSource[index].verson+'">' +data.dataSource[index].rangeName + "," + data.dataSource[index].verson + "," + data.dataSource[index].startDate + "," + data.dataSource[index].endDate +  '</option>'));
            });

            if (val == ''||'_'||null){
                $("#rangeId").val('');
                $('#unitId').attr('disabled',"disabled");
                $("#unitId").empty();
                $("#unitId").val("");
            }
            else{
                $("#rangeId").val(val);
                initUnitSel(userAssignmentData ? userAssignmentData.unitId : "");
                $('#unitId').removeAttr('disabled');
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
 *当合并范围改变 
 * @param val  合并单元
 */
function rangeChange(val) {
    if (val != ''||null){
        initUnitSel($("#unitId").val());
        $('#unitId').removeAttr('disabled');
    }
    else{
        $('#unitId').attr('disabled',"disabled");
        $("#unitId").empty();
        $("#unitId").val("");
    }

}

/**
 * 查询合并单元
 * @param val
 */
function initUnitSel(val) {
    $.ajax({
        url : config.baseurl +"/api/rangeRest/unitList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            rangeId:        $("#rangeId").val().split("_")[0],
            rangeVerson:   $("#rangeId").find("option:selected").text().split(",")[1]
        },
        type:"GET",   //请求方式
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            // console.log("unitId:" + val);
            $("#unitId").empty();

            var initValue = "";
            var initText = "";
            $('#unitId').append("<option value='" + initValue + "'>" + initText + "," + initText+ "</option>");

            $(data.dataSource).each(function(index){
                $("#unitId").append($('<option value="'+data.dataSource[index].unitId + '">'+data.dataSource[index].unitNumber + "," +data.dataSource[index].unitName + '</option>'));
            });

            $("#unitId").val(val);
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
 * 生成分类账下拉选择菜单
 * @param val
 */
function initLedgerSel (val) {
    $.ajax({
        url : config.baseurl +"/api/rangeRest/ledgerList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            // console.log("ledgerId:" + val);
            $("#ledgerId").empty();

            var initValue = "";
            var initText = "";
            $('#ledgerId').append("<option value='" + initValue + "'>" + initText + "," + initText+ "</option>");

            $(data.dataSource).each(function(index){
                $("#ledgerId").append($('<option value="'+data.dataSource[index].ledgerId + '">'+data.dataSource[index].ledgerId + "," + data.dataSource[index].ledgerName+'</option>'));
            });

            $("#ledgerId").val(val);

            if (val != ''||null){
                initCompanySel(userAssignmentData ? userAssignmentData.companyCode : "");
                $('#companyCode').removeAttr('disabled');
            }else{
                $('#companyCode').attr('disabled',"disabled");
                $("#companyCode").empty();
                $("#companyCode").val("");
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
 * 当分类账改变
 * @param val
 */
function ledgerChange(val) {
    if (val != ''||null){
        initCompanySel($("#companyCode").val());
        $('#companyCode').removeAttr('disabled');
    }else{
        $('#companyCode').attr('disabled',"disabled");
        $("#companyCode").empty();
        $("#companyCode").val("");
    }
}

/**
 * 生成分类账下拉选择菜单
 * @param val
 */
function initCompanySel (val) {
    $.ajax({
        url : config.baseurl +"/api/rangeRest/ebsCompanyList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            ledgerId:   $('#ledgerId').val()
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            // console.log("companyCode:" + val);
            $("#companyCode").empty();

            var initValue = "";
            var initText = "";
            $('#companyCode').append("<option value='" + initValue + "'>" + initText + "," + initText+ "</option>");

            $(data.dataSource).each(function(index){
                $("#companyCode").append($('<option value="'+data.dataSource[index].companyCode + '">'+data.dataSource[index].companyCode + "," + data.dataSource[index].companyName +'</option>'));
            });

            $("#companyCode").val(val);
        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

/**
 * 保存用户权限分配信息
 */
function saveUserAssignment() {
    var o = new Object();

    o.assignmentId = $('#assignmentId').val();
    o.userId = $('#userId').val();
    o.reportId = $('#reportId').val();
    if ("" != $("#rangeId").val()){
        o.rangeId = $("#rangeId").val().split("_")[0];
        o.rangeVerson = $("#rangeId").val().split("_")[1];
    }else{
        o.rangeId = "";
        o.rangeVerson = "";
    }
    if(null != $('#unitId').val())
        o.unitId = $('#unitId').val();
    else
        o.unitId = "";
    o.ledgerId =$('#ledgerId').val();
    if(null != $('#companyCode').val())
        o.companyCode =$('#companyCode').val();
    else
        o.companyCode ="";

    if ($("#selectFlag").is(":checked"))
        o.selectFlag = "1";
    else
        o.selectFlag = "0";

    if ($("#insertFlag").is(":checked"))
        o.insertFlag = "1";
    else
        o.insertFlag = "0";

    if ($("#updateFlag").is(":checked"))
        o.updateFlag = "1";
    else
        o.updateFlag = "0";

    if ($("#deleteFlag").is(":checked"))
        o.deleteFlag = "1";
    else
        o.deleteFlag = "0";

    if ($("#reportFlag").is(":checked"))
        o.reportFlag = "1";
    else
        o.reportFlag = "0";

    if ($("#approvalFlag").is(":checked"))
        o.approvalFlag = "1";
    else
        o.approvalFlag = "0";

    if ($("#receiveFlag").is(":checked"))
        o.receiveFlag = "1";
    else
        o.receiveFlag = "0";

    if ("" ==  o.userId.trim()) {
        window.parent.error("请选择用户！");
        // Setfocus('deliverableNumber');
        return;
    }

    if ("" ==  o.rangeId.trim() && "" ==  o.unitId.trim() &&  "" ==  o.ledgerId.trim() && "" ==  o.companyCode.trim()){
        window.parent.error("合并范围、合并单元、分类账、公司名称选项不能全为空！");
        return;
    }

    if (("" !=  o.rangeId.trim() || "" !=  o.unitId.trim()) && ("" !=  o.ledgerId.trim() || ""  !=  o.companyCode.trim())){
        window.parent.error("合并范围、合并单元与分类账、公司名称选项不能同时选择！");
        return;
    }

    console.log(JSON.stringify(o));

    $.ajax({
        url : config.baseurl +"/api/glcsUserAssignment/saveUserAssignment",
        contentType : 'application/json',
        dataType:"json",
        data: JSON.stringify(o),
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("用户权限分配保存成功！")
                $('#userAsiignment_modal').on('shown.bs.modal', function (e) {}).modal('hide');

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
 * 删除程序
 * @param contHeaderId
 * @param billEventId
 */
function delUserAsiignment(contHeaderId,billEventId) {
    var table = $('#dataTables-userAssignmentData').DataTable();
    var data = table.rows(table.$('tr.dt-select')).data();
    $.ajax({
        url : config.baseurl +"/api/glcsUserAssignment/delUserAssignment",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            assignmentId : data[0].assignmentId
        },
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("用户权限分配删除成功！")
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
 * 当选择框改变时
 * @param val  哪个选择框
 */
function checkboxChange(val) {
    if (val == 'insertFlag'){
         if ($("#insertFlag").is(":checked"))
             $("#selectFlag").prop("checked", "checked")
    }

    if (val == 'updateFlag'){
        if ($("#updateFlag").is(":checked"))
            $("#selectFlag").prop("checked", "checked")
    }

    if (val == 'deleteFlag'){
        if ($("#deleteFlag").is(":checked"))
            $("#selectFlag").prop("checked", "checked")
    }

    if (val == 'reportFlag'){
        if ($("#reportFlag").is(":checked"))
            $("#selectFlag").prop("checked", "checked")
    }

    if (val == 'approvalFlag'){
        if ($("#approvalFlag").is(":checked"))
            $("#selectFlag").prop("checked", "checked")
    }

    if (val == 'receiveFlag'){
        if ($("#receiveFlag").is(":checked"))
            $("#selectFlag").prop("checked", "checked")
    }
}