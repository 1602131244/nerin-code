/**
 * Created by Administrator on 2017/9/13.
 */


var config = new nerinJsConfig();
var rangeDataSet = [];
// var unitDataSet = [];
var companyDataSet = [];
var unitTreeData = [];


var rangeData;
var unitData;
var companyData;

var rangeId;
var rangeVerson;
var rangeStartDate;
var rangeEndDate;
// var unitId;

$(function () {

    //初始化rangeTable数据
    initRangeTables();

    //初始化模态框日期格式
    $('#datetimepicker1').datetimepicker({
        format: 'YYYY-MM-DD'
    });

    // 模拟点击查询
    setTimeout(function(){
            $('#bu_queryRange').trigger("click");}
        ,100
    );

});

// 初始化dataTables程序
var rangeTable;
function initRangeTables() {
    $('#dataTables-listRange').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-rangeData"></table>');

    rangeTable = $('#dataTables-rangeData').DataTable({
        "processing": true,
        "data": rangeDataSet,
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

        // {"title": "<input type='checkbox' name='ch_listAll' id='ch_listAll' onclick='javascript:setCheckAll();' />", "data": null},
        "columns": [
            {"title": "", "data": null},           //0
            {"title": "序号", "data": null ,"className": "dt_center"},      //1
            {"title": "编码", "data": "rangeNumber" , "className": "dt_center"},   //2
            {"title": "名称", "data": "rangeName" , "className": "dt_center"}, //3
            {"title": "描述", "data": "description" , "className": "dt_center"},  //4
            {"title": "版本", "data": "verson" , "className": "dt_center"}, //5
            {"title": "启用日期", "data": "startDate" , "className": "dt_center"},//6
            {"title": "截止日期", "data": "endDate" , "className": "dt_center"},  //7
            {"title": "变更人", "data": "lastByName" , "className": "dt_center"}, //8
            {"title": "变更日期", "data": "lastUpdateDate" , "className": "dt_center"},  //9

            {"title": "rangeId", "data": "rangeId"} //10
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
            "orderable": false,
            "width": 120,
            "targets": 2
        },{
            "orderable": false,
            "width": 200,
            "targets": 3
        },{
            "orderable": false,
            "width": 300,
            "targets": 4
        },{
            "orderable": false,
            "width": 32,
            "targets": 5
        },{
            "orderable": false,
            "width": 70,
            "targets": 6
        },{
            "orderable": false,
            "width": 70,
            "targets": 7
        },{
            "orderable": false,
            "width": 65,
            "targets": 8
        },{
            "orderable": false,
            "width": 70,
            "targets": 9
        },{
            "width": 32,
            "targets": 10,
            "visible": false
        }],
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

        },
        "initComplete": function () {
        }
    });

    $('#dataTables-rangeData tbody').on( 'click', 'tr', function () {
        var table = $('#dataTables-rangeData').DataTable();
        if ( $(this).hasClass('dt-select') ) {
            $(this).removeClass('dt-select');
            var check = $(this).find("input[name='ch_list']");
            check.prop("checked", false);

            $('#bu_edit').attr('disabled',"disabled");
            $('#bu_del').attr('disabled',"disabled");
            $('#bu_adu').attr('disabled',"disabled");
            $('#bu_rej').attr('disabled',"disabled");
            $('#bu_copy').attr('disabled',"disabled");
            $('#bu_unit').attr('disabled',"disabled");
        }
        else {
            var check = $('#dataTables-rangeData').DataTable().$('tr.dt-select').find("input[name='ch_list']");
            check.prop("checked", false);
            $('#dataTables-rangeData').DataTable().$('tr.dt-select').removeClass('dt-select');
            $(this).addClass('dt-select');
            var check = $(this).find("input[name='ch_list']");
            check.prop("checked", true);

            if (table.row($(this)).data().endDate ==null||''){
                if (table.row($(this)).data().startDate ==null||''){
                    $('#bu_edit').removeAttr('disabled');
                    $('#bu_del').removeAttr('disabled');
                    $('#bu_copy').attr('disabled',"disabled");
                    $('#bu_adu').removeAttr('disabled');
                    $('#bu_rej').attr('disabled',"disabled");
                    $('#bu_unit').removeAttr('disabled');
                }else{
                    $('#bu_edit').removeAttr('disabled');
                    $('#bu_del').attr('disabled',"disabled");
                    $('#bu_copy').removeAttr('disabled');
                    $('#bu_adu').attr('disabled',"disabled");
                    $('#bu_rej').removeAttr('disabled');
                    $('#bu_unit').removeAttr('disabled');
                }
                document.getElementById("bu_edit").innerHTML=('<i class="fa fa-edit" style="color: #FF4500;font-size: 13px;"></i> 编辑');
            }else{
                $('#bu_edit').removeAttr('disabled');
                $('#bu_del').attr('disabled',"disabled");
                $('#bu_copy').attr('disabled',"disabled");
                $('#bu_adu').attr('disabled',"disabled");
                $('#bu_rej').attr('disabled',"disabled");
                $('#bu_unit').removeAttr('disabled');
                document.getElementById("bu_edit").innerHTML=('<i class="fa fa-edit" style="color: #FF4500;font-size: 13px;"></i> 查看');
            }
        }
    } );

    rangeTable.on( 'order.dt search.dt', function () {
        rangeTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = "<input type='checkbox' name='ch_list' />";
        } ),
            rangeTable.column(1, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } );
    } ).draw();
}

// 初始化dataTables程序
var companyTable;
function initCompanyTables() {
    $('#dataTables-listCompany').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-companyData"></table>');

    companyTable = $('#dataTables-companyData').DataTable({
        "processing": true,
        "data": companyDataSet,
        "scrollX": true,
        "scrollY": "500px",
        "scrollCollapse": true,
        "pagingType": "full_numbers",
        "language": {
            "url": "/scripts/common/Chinese.json"
        },

        // searching : false, //去掉搜索框方法一：百度上的方法，但是我用这没管用
        // sDom : '"top"i',   //去掉搜索框方法二：这种方法可以

        bLengthChange: false,   //去掉每页显示多少条数据方法
        "searching": false,
        "bLengthChange": false,
        "ordering ": false,
        "paging": false,  //禁止表格分页

        // {"title": "<input type='checkbox' name='ch_listAll' id='ch_listAll' onclick='javascript:setCheckAll();' />", "data": null},
        "columns": [
            // {"title": "", "data": null},           //0
            {"title": "序号", "data": null ,"className": "dt_center"},      //1
            {"title": "分类账", "data": "ledgerNameList" , "className": "dt_center"}, //2
            {"title": "公司", "data": "companyNameList" , "className": "dt_center"},  //3
            {"title": "显示公司名称", "data": "companyName" , "className": "dt_center"},   //4
            {"title": "备注", "data": "description" , "className": "dt_center"},//5

            {"title": "ledgerId", "data": "ledgerId" }, //6
            {"title": "companyCode", "data": "ledgerId" }, //7
            {"title": "rangeId", "data": "rangeId"}, //8
            {"title": "RangeVerson", "data": "rangeVerson"}, //9
            {"title": "unitId", "data": "unitId"} //10

        ],
        "columnDefs": [
            //     {
            //     "searchable": false,
            //     "orderable": false,
            //     "targets": 0,
            //     "width": 5
            // },
            {
                "searchable": false,
                "orderable": false,
                "targets": 0,
                "width": 32
            },{
                "orderable": false,
                "width": 200,
                "targets": 1
            },{
                "orderable": false,
                "width": 200,
                "targets": 2
            },{
                "orderable": false,
                "width": 200,
                "targets": 3
            },{
                "orderable": false,
                "width": 200,
                "targets": 4
            },{
                "width": 32,
                "targets": 5,
                "visible": false
            },{
                "width": 32,
                "targets": 6,
                "visible": false
            },{
                "width": 32,
                "targets": 7,
                "visible": false
            },{
                "width": 32,
                "targets": 8,
                "visible": false
            },{
                "width": 32,
                "targets": 9,
                "visible": false
            }],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            $(row).children('td').eq(1).attr('style', 'text-align: left;');
            $(row).children('td').eq(2).attr('style', 'text-align: left;');
            $(row).children('td').eq(3).attr('style', 'text-align: left;');
            $(row).children('td').eq(4).attr('style', 'text-align: left;');
            $(row).children('td').eq(5).attr('style', 'text-align: left;');

        },
        "initComplete": function () {
        }
    });

    $('#dataTables-companyData tbody').on( 'click', 'tr', function () {
        var table = $('#dataTables-companyData').DataTable();
        if ( $(this).hasClass('dt-select') ) {
            $(this).removeClass('dt-select');
            // var check = $(this).find("input[name='ch_list']");
            // check.prop("checked", false);

            $('#bu_del_company').attr('disabled',"disabled");
        }
        else {
            // var check = $('#dataTables-companyData').DataTable().$('tr.dt-select').find("input[name='ch_list']");
            // check.prop("checked", false);
            $('#dataTables-companyData').DataTable().$('tr.dt-select').removeClass('dt-select');
            $(this).addClass('dt-select');
            // var check = $(this).find("input[name='ch_list']");
            // check.prop("checked", true);

            if (rangeEndDate ==null||''){
                $('#bu_del_company').removeAttr('disabled');
            }else{
                $('#bu_del_company').attr('disabled',"disabled");
            }
        }
    } );

    companyTable.on( 'order.dt search.dt', function () {
        // companyTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
        //     cell.innerHTML = "<input type='checkbox' name='ch_list' />";
        // } ),
        companyTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );
    } ).draw();
}

// ----------------------------------------------------按钮监控------------------------------------------------------
// 点击查询按钮
$('#bu_queryRange').on('click', function (e) {
    queryRange();
});

// 点击新增按钮
$('#bu_add').on('click', function (e) {
    rangeData = null;
    initRangeModal();
});

// 点击修改按钮
$('#bu_edit').on('click', function (e) {
    rangeData = null;
    getRangeData();
});

// 点击删除按钮
$('#bu_del').on('click', function (e) {
    // delRange() ;
    var table = $('#dataTables-rangeData').DataTable();
    var data = table.rows(table.$('tr.dt-select')).data();
    if (data.length != 1){
        window.parent.warring("请选择一个数据再删除");
        return;
    }
    window.parent.showConfirm(delRange, "确认删除 " + data[0].rangeNumber + " " + data[0].rangeName + " " + data[0].verson+" 吗？");
});

// 点击增加新版本按钮
$('#bu_copy').on('click', function (e) {
    window.parent.showConfirm(copyRange, "确认复制合并范围新增新版本吗？");
});

// 点击启用按钮
$('#bu_adu').on('click', function (e) {
    rangeData = null;
    getRangeEnableData();
});

// 点击反启用按钮
$('#bu_rej').on('click', function (e) {
    var table = $('#dataTables-rangeData').DataTable();
    var data = table.rows(table.$('tr.dt-select')).data();
    if (data.length != 1){
        window.parent.warring("请选择一个数据再反启用");
        return;
    }
    window.parent.showConfirm(RangeDisable, "确认反启用合并范围 " + data[0].rangeNumber +" "+ data[0].rangeName + " " + data[0].verson + " 吗？");
});

// 点击合并单元按钮
$('#bu_unit').on('click', function (e) {
    initUnitCompanyModal();
});

// 点击合并单元保存按钮
$('#saveRange').on('click', function (e) {
    saveRange();
});

// 点击启用窗口的保存按钮
$('#saveRangeEnable').on('click', function (e) {
    rangeEnable();
});

// 点击新增单元按钮
$('#bu_add_unit').on('click', function (e) {
    initUnitAddModal('ADD');
});

// 点击新增子单元按钮
$('#bu_add_unitChild').on('click', function (e) {
    initUnitAddModal('ACH');
});

// 点击编辑单元按钮
$('#bu_edit_unit').on('click', function (e) {
    initUnitAddModal('EDIT');
});

// 点击删除单元按钮
$('#bu_del_unit').on('click', function (e) {
    o = new Object();
    var tree = $("#tree").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();
    if (selNodes.length != 1){
        window.parent.warring("请选择一个数据再删除");
        return;
    }
    selNodes.forEach(function(node) {
        o.unitId = node.data.unitId;
        o.unitParentId = node.data.unitParentId;
        o.unitNumber = node.data.unitNumber;
        o.unitShortName = node.data.unitShortName;
        o.unitName = node.data.unitName;
    });
    window.parent.showConfirm(delUnit, "确认删除合并单元" + o.unitNumber + " "+o.unitName +" 吗？");
});

// 点击引入公司按钮
$('#bu_add_company').on('click', function (e) {
    initCompanyAddModal();
});

// 点击引出公司按钮
$('#bu_del_company').on('click', function (e) {
    var table = $('#dataTables-companyData').DataTable();
    var data = table.rows(table.$('tr.dt-select')).data();
    if (data.length != 1){
        window.parent.warring("请选择一个数据再删除");
        return;
    }
    window.parent.showConfirm(delCompany, "确认引出" + data[0].companyCode + " "+ data[0].companyName +" 吗？");
});

// 点击公司保存按钮
$('#saveUnit').on('click', function (e) {
    saveUnit();
});

// 点击公司保存按钮
$('#saveCompany').on('click', function (e) {
    saveCompany();
});

// ---------------------------------------------主界面程序---------------------------------------------------------
/**
 * 设置焦点
 * @param val
 * @constructor
 */
function Setfocus(val){
    document.getElementById(val).focus();
};

/**
 * 查询合并范围记录
 */
function queryRange() {
    $.ajax({
        url : config.baseurl +"/api/rangeRest/rangeList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            rangeNumber:        $('#rangeNumberSear').val(),
            rangeName:          $('#rangeNameSear').val(),
            rangeVerson:        $('#rangeVersonSear').val()
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            rangeDataSet = data.dataSource;
            console.log(rangeDataSet);

            //初始化
            initRangeTables();

            //失效新增、打开按钮
            $('#bu_add').removeAttr('disabled',"disabled");

        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}


/**
 * 显示合并范围新增窗口
 */
function initRangeModal() {
    //全部清空
    $('#rangeId').val("");
    $('#rangeVerson').val("");
    $('#rangeNumber').val("");
    $('#rangeName').val("");
    $('#startDate').val("");
    $('#endDate').val("");
    $('#description').val("");

    // 执行初始化
    $('#rangeId').val(rangeData ? rangeData.rangeId : "");
    $('#rangeVerson').val(rangeData ? rangeData.verson : "");
    $('#rangeNumber').val(rangeData ? rangeData.rangeNumber : "");
    $('#rangeName').val(rangeData ? rangeData.rangeName : "");
    $('#startDate').val(rangeData ? rangeData.startDate : "");
    $('#endDate').val(rangeData ? rangeData.endDate : "");
    $('#description').val(rangeData ? rangeData.description : "");

    if (rangeData != null) {
        if(rangeData.endDate==null||''){
            if(rangeData.startDate==null||'')
                $('#rangeNumber').removeAttr('disabled',"disabled");
            else
                $('#rangeNumber').attr('disabled',"disabled");
            $('#rangeName').removeAttr('disabled',"disabled");
            $('#description').removeAttr('readonly',"readonly");
            $('#saveRange').removeAttr('disabled',"disabled");
        }else{
            $('#rangeNumber').attr('disabled',"disabled");
            $('#rangeName').attr('disabled',"disabled");
            $('#description').attr('readonly',"readonly");
        }
    }else{
        $('#rangeNumber').removeAttr('disabled',"disabled");
        $('#rangeName').removeAttr('disabled',"disabled");
        $('#description').removeAttr('readonly',"readonly");
        $('#saveRange').removeAttr('disabled',"disabled");
    }

    document.getElementById("addRangeLabel").innerHTML=(rangeData ? ('' == rangeData.endDate ? "编辑合并范围" : "查看合并范围") : "新增合并范围");
    // 显示新增界面
    $('#addRange_modal').modal('show');

    Setfocus('rangeNumber');
    // document.getElementById(rangeNumber).focus();
}

/**
 * 查询合并范围记录
 */
function getRangeData() {
    var table = $('#dataTables-rangeData').DataTable();
    var data = table.rows(table.$('tr.dt-select')).data();
    if (data.length != 1){
        window.parent.warring("请选择一行数据");
        return;
    }
    $.ajax({
        url : config.baseurl +"/api/rangeRest/rangeList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            rangeId:             data[0].rangeId,
            rangeVerson:        data[0].verson
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            rangeData = data.dataSource[0];
            console.log(rangeData);
            initRangeModal();
        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}


/**
 * 保存合并范围程序
 */
function saveRange() {
    var o = new Object();

    o.rangeId = $('#rangeId').val();
    o.verson = $('#rangeVerson').val();
    o.rangeNumber = $('#rangeNumber').val();
    o.rangeName = $('#rangeName').val();
    o.startDate =$('#startDate').val();
    o.endDate =$('#endDate').val();
    o.description =$('#description').val();

    if ("" == o.rangeNumber.trim()) {
        window.parent.error("请输入合并范围编码！");
        Setfocus('rangeNumber');
        return;
    }
    if ("" == o.rangeName.trim()) {
        window.parent.error("请输入合并范围名称！");
        Setfocus('rangeName');
        return;
    }

    console.log(JSON.stringify(o));

    $.ajax({
        url : config.baseurl +"/api/rangeRest/saveRange",
        contentType : 'application/json',
        dataType:"json",
        data: JSON.stringify(o),
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("合并范围保存成功！")
                $('#addRange_modal').on('shown.bs.modal', function (e) {}).modal('hide');

                $('#bu_queryRange').trigger("click");
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
 * 删除合并范围程序
 */
function delRange() {
    var table = $('#dataTables-rangeData').DataTable();
    var data = table.rows(table.$('tr.dt-select')).data();
    $.ajax({
        url : config.baseurl +"/api/rangeRest/delRange",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            rangeId:       data[0].rangeId,
            verson:        data[0].verson
        },
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("合并范围删除成功！")
                $('#bu_queryRange').trigger("click");
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
 * 复制新版本
 */
function copyRange() {
    var table = $('#dataTables-rangeData').DataTable();
    var data = table.rows(table.$('tr.dt-select')).data();
    if (data.length != 1){
        window.parent.warring("请选择一个数据再反新增新版本");
        return;
    }
    $.ajax({
        url : config.baseurl +"/api/rangeRest/copyRange",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            rangeId:       data[0].rangeId,
            verson:        data[0].verson
        },
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("合并范围新增新版本成功！")
                $('#bu_queryRange').trigger("click");
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
 * 查询合并范围记录
 */
function getRangeEnableData() {
    var table = $('#dataTables-rangeData').DataTable();
    var data = table.rows(table.$('tr.dt-select')).data();
    if (data.length != 1){
        window.parent.warring("请选择一个数据");
        return;
    }
    $.ajax({
        url : config.baseurl +"/api/rangeRest/rangeList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            rangeId:             data[0].rangeId,
            rangeVerson:        data[0].verson
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            rangeData = data.dataSource[0];
            console.log(rangeData);
            rangeEnableModal();
        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}


/**
 * 显示启用合并范围窗口
 */
function rangeEnableModal() {
    //全部清空
    $('#enableRangeId').val("");
    $('#enableRangeVerson').val("");
    $('#enableStartDate').val("");

    // 执行初始化
    $('#enableRangeId').val(rangeData ? rangeData.rangeId : "");
    $('#enableRangeVerson').val(rangeData ? rangeData.verson : "");

    if (rangeData != null) {
        if ((rangeData.endDate==null||'') && (rangeData.startDate==null||'')){
            $('#enableStartDate').removeAttr('disabled');
            $('#saveRangeEnable').removeAttr('disabled');
        }
        else{
            $('#enableStartDate').attr('disabled',"disabled");
            $('#saveRangeEnable').attr('disabled',"disabled");
        }
    }else{
        $('#saveRangeEnable').attr('disabled',"disabled");
    }

    // 显示新增界面
    $('#rangeEnable_modal').modal('show');

}


/**
 * 启用合并范围
 */
function rangeEnable() {
    var rangeId = $('#enableRangeId').val();
    var verson = $('#enableRangeVerson').val();
    var startDate =$('#enableStartDate').val();

    if ("" == startDate.trim()) {
        window.parent.error("请输入合并范围启用时间！");
        Setfocus('startDate');
        return;
    }

    $.ajax({
        url : config.baseurl +"/api/rangeRest/rangeEnable",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            rangeId:       rangeId,
            verson:        verson,
            startDate:      startDate
        },
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("合并范围启用成功！")

                $('#rangeEnable_modal').on('shown.bs.modal', function (e) {}).modal('hide');

                $('#bu_queryRange').trigger("click");
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
 * 反启用
 * @constructor
 */
function RangeDisable() {
    var table = $('#dataTables-rangeData').DataTable();
    var data = table.rows(table.$('tr.dt-select')).data();
    $.ajax({
        url : config.baseurl +"/api/rangeRest/rangeDisable",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            rangeId:       data[0].rangeId,
            verson:        data[0].verson
        },
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("合并范围反启用成功！")
                $('#bu_queryRange').trigger("click");
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
 * 显示合并范围新增窗口
 */
function initUnitCompanyModal() {
    var table = $('#dataTables-rangeData').DataTable();
    var data = table.rows(table.$('tr.dt-select')).data();
    if (data.length != 1){
        window.parent.warring("请选择一个数据");
        return;
    }
    $.ajax({
        url : config.baseurl +"/api/rangeRest/rangeList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            rangeId:             data[0].rangeId,
            rangeVerson:        data[0].verson
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            rangeData = data.dataSource[0];
            console.log(rangeData);

            rangeId= rangeData.rangeId;
            rangeVerson = rangeData.verson;
            rangeStartDate= rangeData.startDate;
            rangeEndDate = rangeData.endDate;

            queryUnitTree(rangeId,rangeVerson,'');

            // 显示合并单元界面
            $('#Unit_modal').modal('show');
        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

glyph_opts = {
    map: {
        doc: "glyphicon glyphicon-file",
        docOpen: "glyphicon glyphicon-file",
        checkbox: "glyphicon glyphicon-unchecked",
        checkboxSelected: "glyphicon glyphicon-check",
        checkboxUnknown: "glyphicon glyphicon-share",
        dragHelper: "glyphicon glyphicon-play",
        dropMarker: "glyphicon glyphicon-arrow-right",
        error: "glyphicon glyphicon-warning-sign",
        expanderClosed: "glyphicon glyphicon-menu-right",
        expanderLazy: "glyphicon glyphicon-menu-right",  // glyphicon-plus-sign
        expanderOpen: "glyphicon glyphicon-menu-down",  // glyphicon-collapse-down
        folder: "glyphicon glyphicon-folder-close",
        folderOpen: "glyphicon glyphicon-folder-open",
        loading: "glyphicon glyphicon-refresh glyphicon-spin"
    }
};

/**
 *刷新树结构 
 */
$("#tree").fancytree({
    extensions: ["dnd", "edit", "glyph", "wide"],
    checkbox: true,
    keyboard: false,
    dnd: {
        focusOnClick: false,
        dragStart: function(node, data) { return false; },
        dragEnter: function(node, data) { return false; },
        dragDrop: function(node, data) { data.otherNode.copyTo(node, data.hitMode); }
    },
    glyph: glyph_opts,
    selectMode: 1,
    source: unitTreeData,
    wide: {
        iconWidth: "1em",     // Adjust this if @fancy-icon-width != "16px"
        iconSpacing: "0.5em", // Adjust this if @fancy-icon-spacing != "3px"
        levelOfs: "1.5em"     // Adjust this if ul padding != "16px"
    },
    icon: function(event, data){
        // if( data.node.isFolder() ) {
        //   return "glyphicon glyphicon-book";
        // }
    },
    // activate: function (event, data) { //选中触发
    //     var node = data.node;
    //     // if (undefined != node.data.unitId)
    //     // delTemplateChapterIds.push(node.data.chapterId);
    // },
    click: function(event, data) {
        // unitId = data.node.data.unitId;

        // var tree = $("#tree").fancytree("getTree"),
        //     selNodes = tree.getSelectedNodes();
        $("#tree").fancytree("getTree").getSelectedNodes().forEach(function(node) {
            node.selected = false;
        });

        data.node.selected = true;

        if (rangeEndDate == null||''||undefined){
            $('#bu_add_unitChild').removeAttr('disabled');
            $('#bu_edit_unit').removeAttr('disabled');
            $('#bu_del_unit').removeAttr('disabled');
            $('#bu_add_company').removeAttr('disabled');

            queryCompanyTable(rangeId,rangeVerson,data.node.data.unitId);
        }

        // $(this).parentspan.hasClass('fancytree-selected')


        // logEvent(event, data, ", targetType=" + data.targetType);
        // return false to prevent default behavior (i.e. activation, ...)
        //return false;
    },
    lazyLoad: function(event, data) {
        data.result = {url: "ajax-sub2.json", debugDelay: 1000};
    }
});

/**
 * 查询合并单元
 * @param rangeId
 * @param rangeVerson
 * @param unitId
 */
function queryUnitTree(rangeId,rangeVerson,unitId) {
    $.ajax({
        url : config.baseurl +"/api/rangeRest/unitListTree",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            rangeId:        rangeId,
            rangeVerson:   rangeVerson,
            unitId:         unitId
        },
        type:"GET",   //请求方式
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if (0 == data.dataTotal)
                $('#bu_add_unit').removeAttr('disabled');
            else
                $('#bu_add_unit').attr('disabled','disabled');

            unitTreeData = data.dataSource;
            // console.log(unitTreeData);
            var tree = $("#tree").fancytree("getTree");
            tree.reload(unitTreeData).done(function(){
                $("#tree").fancytree("getRootNode").visit(function(node){
                    node.setExpanded(true);
                });
            });

            $('#bu_add_unitChild').attr('disabled','disabled');
            $('#bu_edit_unit').attr('disabled','disabled');
            $('#bu_del_unit').attr('disabled','disabled');
            $('#bu_add_company').attr('disabled','disabled');

            //初始化
            setTimeout(function(){
                    companyDataSet = [];
                    initCompanyTables();
                    // queryCompanyTable(-1,-1,-1);
                }
                ,500
            );
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
 * 查询公司
 * @param rangeId
 * @param rangeVerson
 * @param unitId
 */
function queryCompanyTable(rangeId,rangeVerson,unitId) {
    if ($('#checkChild').is(':checked'))
        var unitFlag = 1;
    else
        var unitFlag = 0;

    $.ajax({
        url : config.baseurl +"/api/rangeRest/companyList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            rangeId:        rangeId,
            rangeVerson:   rangeVerson,
            unitId:         unitId,
            unitFlag:       unitFlag
        },
        type:"GET",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            companyDataSet = data.dataSource;
            console.log(rangeDataSet);

            //初始化
            initCompanyTables();
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
 * 显示合并范围新增窗口
 * @param val
 */
function initUnitAddModal(val) {
    //全部清空
    $('#unitId').val("");
    $('#unitParentId').val("-1");
    $('#unitNumber').val("");
    $('#unitShortName').val("");
    $('#unitName').val("");

    o = new Object();
    var tree = $("#tree").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();
    selNodes.forEach(function(node) {
        o.unitId = node.data.unitId;
        o.unitParentId = node.data.unitParentId;
        o.unitNumber = node.data.unitNumber;
        o.unitShortName = node.data.unitShortName;
        o.unitName = node.data.unitName;
    });

    // 执行初始化
    // $('#rangeId').val(rangeId);
    // $('#rangeVerson').val(rangeVerson);

    // 编辑
    if (val == 'EDIT'){
        $('#unitId').val(o.unitId);
        $('#unitParentId').val(o.unitParentId);

        $('#unitNumber').val(o.unitNumber);
        $('#unitShortName').val(o.unitShortName);
        $('#unitName').val(o.unitName);
    }else{
        //新郑
        if (val == 'ADD'){
            // $('#unitId').val(o.unitId);
            $('#unitParentId').val("-1");
        }
        //新增子项
        else{
            $('#unitParentId').val(o.unitId);
        }
    }

    var lable = (val != 'EDIT' ? (val == 'ADD' ? "新增合并单元" : "新增合并子单元") : "编辑合并单元");
    document.getElementById("addUnitLabel").innerHTML = lable;

    // 显示新增界面
    $('#addUnit_modal').modal('show');

    Setfocus('unitNumber')
}



/**
 * 保存合并单元程序
 */
function saveUnit() {
    var o = new Object();

    o.rangeId = rangeId;
    o.rangeVerson = rangeVerson;
    o.unitId = $('#unitId').val();
    o.unitParentId = $('#unitParentId').val();
    o.unitNumber =$('#unitNumber').val();
    o.unitShortName =$('#unitShortName').val();
    o.unitName =$('#unitName').val();

    if ("" == o.unitNumber.trim()) {
        window.parent.error("请输入合并单元编码！");
        Setfocus('unitNumber');
        return;
    }
    if ("" == o.unitName.trim()) {
        window.parent.error("请输入合并单元名称！");
        Setfocus('unitName');
        return;
    }
    if ("" == o.unitShortName.trim()) {
        window.parent.error("请输入合并单元短名称！");
        Setfocus('unitShortName');
        return;
    }

    console.log(JSON.stringify(o));

    $.ajax({
        url : config.baseurl +"/api/rangeRest/saveUnit",
        contentType : 'application/json',
        dataType:"json",
        data: JSON.stringify(o),
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("合并单元保存成功！")
                $('#addUnit_modal').on('shown.bs.modal', function (e) {}).modal('hide');

                queryUnitTree(rangeId,rangeVerson,'');
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
 * 删除合并单元
 */
function delUnit() {
    o = new Object();
    var tree = $("#tree").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();
    if (selNodes.length != 1){
        window.parent.warring("请选择一个数据再删除");
        return;
    }
    selNodes.forEach(function(node) {
        o.unitId = node.data.unitId;
        o.unitParentId = node.data.unitParentId;
        o.unitNumber = node.data.unitNumber;
        o.unitShortName = node.data.unitShortName;
        o.unitName = node.data.unitName;
    });
    $.ajax({
        url : config.baseurl +"/api/rangeRest/delUnit",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            rangeId:       rangeId,
            rangeVerson:        rangeVerson,
            unitId:         o.unitId
        },
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("合并单元删除成功！")

                queryUnitTree(rangeId,rangeVerson,'');

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
 * 显示引入公司窗口
 */
function initCompanyAddModal() {
    //全部清空
    $('#cUnitId').val("");
    $('#ledgerId').val("");
    $('#ledgerName').val("");
    $('#companyCode').val("");
    $('#companyNameList').val("");
    $('#companyName').val("");
    $('#companyDescription').val("");

    o = new Object();
    var tree = $("#tree").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();
    selNodes.forEach(function(node) {
        o.unitId = node.data.unitId;
        // o.unitParentId = node.data.unitParentId;
        // o.unitNumber = node.data.unitNumber;
        // o.unitShortName = node.data.unitShortName;
        // o.unitName = node.data.unitName;
    });
    $('#cUnitId').val(o.unitId);

    initLedgerSel ("");
    // initCompanySel("");

    // 显示新增界面
    $('#addCompany_modal').modal('show');

    Setfocus('ledgerId')
}

/**
 * 保存公司程序
 */
function saveCompany() {
    var o = new Object();

    o.rangeId = rangeId;
    o.rangeVerson = rangeVerson;
    o.unitId = $('#cUnitId').val();
    o.ledgerId =$('#ledgerId').val();
    o.companyCode =$('#companyCode').val();
    o.companyName =$('#companyName').val();
    o.description =$('#companyDescription').val();

    if ("" == o.ledgerId.trim()) {
        window.parent.error("请选择分类账！");
        Setfocus('ledgerId');
        return;
    }
    if ("" == o.companyCode.trim()) {
        window.parent.error("请选择公司！");
        Setfocus('companyCode');
        return;
    }
    if ("" == o.companyName.trim()) {
        window.parent.error("请输入公司名称！");
        Setfocus('companyName');
        return;
    }

    console.log(JSON.stringify(o));

    $.ajax({
        url : config.baseurl +"/api/rangeRest/saveCompany",
        contentType : 'application/json',
        dataType:"json",
        data: JSON.stringify(o),
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("公司引入成功！")
                $('#addCompany_modal').on('shown.bs.modal', function (e) {}).modal('hide');

                queryCompanyTable(rangeId,rangeVerson,o.unitId);
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
 * 删除合并单元
 */
function delCompany() {
    o = new Object();
    var tree = $("#tree").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();
    selNodes.forEach(function(node) {
        o.unitId = node.data.unitId;
    });

    var table = $('#dataTables-companyData').DataTable();
    var data = table.rows(table.$('tr.selected')).data();
    $.ajax({
        url : config.baseurl +"/api/rangeRest/delCompany",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            rangeId:       rangeId,
            rangeVerson:        rangeVerson,
            unitId:         data[0].unitId,
            ledgerId:        data[0].ledgerId,
            companyCode:        data[0].companyCode
        },
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("公司引出成功！")

                queryCompanyTable(rangeId,rangeVerson,o.unitId);

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

// -----------------------------------------------------------------------------------------------------------------------
/**
 * 生成分类账下拉选择菜单
 * @param val
 */
function initLedgerSel (val) {
    // if ($("#ledgerId").val())
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
            $("#ledgerId").empty();

            // var initValue = "";
            // var initText = "";
            // $('#ledgerId').append("<option value='" + initValue + "'>" + initText + "," + initText+ "</option>");

            $(data.dataSource).each(function(index){
                $("#ledgerId").append($('<option value="'+data.dataSource[index].ledgerId + '">'+data.dataSource[index].ledgerId + "," + data.dataSource[index].ledgerName+'</option>'));
            });

            $("#ledgerId").val(val);
        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

/**
 * 分类账值改变后公司也重新刷新下拉
 * @param data
 */
function ledgerChange(data) {
    initCompanySel($("#companyCode").val());
    }


/**
 * 生成分类账下拉选择菜单
 * @param val  公司
 */
function initCompanySel (val) {
    // if ($("#companyCode").val())
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
            $("#companyCode").empty();

            var initValue = "";
            var initText = "";
            $('#companyCode').append("<option value='" + initValue + "'>" + initText + "," + initText+ "</option>");

            $(data.dataSource).each(function(index){
                $("#companyCode").append($('<option value="'+data.dataSource[index].companyCode + '">'+data.dataSource[index].companyCode + "," + data.dataSource[index].companyName +'</option>'));
            });

            $("#companyCode").val(val);

            $('#companyCode').removeAttr('disabled');
        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

/**
 * 当公司下拉改变
 * @param val
 */
function companyChange(val) {
    // $("#companyName").val($('#companyCode').val().split(",")[2]);
    $("#companyName").val($("#companyCode").find("option:selected").text().split(",")[1]);
}

/**
 * 包含合并单元所有下级节点公司，重新刷新公司列表
 * @param val
 */
function checkChildChange(val) {
    o = new Object();
    var tree = $("#tree").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();
    if (selNodes.length != 1){
        return;
    }
    selNodes.forEach(function(node) {
        o.unitId = node.data.unitId;
    });

    queryCompanyTable(rangeId,rangeVerson,o.unitId);
}

