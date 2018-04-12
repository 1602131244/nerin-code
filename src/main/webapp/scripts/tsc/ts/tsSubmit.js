$(document).keyup(function(event){
    switch(event.keyCode) {
        case 13:
            $('#bu_queryData').trigger("click");
    }
});

var now_pageNo;
var config = new nerinJsConfig();
var dataSet = [];
var logonUser;
var supplierNatures;
var vendorLevels;
var ebsUser = [];
$(function () {
    $('#bu_queryData').focus();
    queryList();
    window.parent.showLoading();
    $.getJSON(config.baseurl + "/api/tsc/ts/querySupplierNature", function (data) {
        supplierNatures = data;
        $.getJSON(config.baseurl + "/api/tsc/ts/getVendorLevel", function (data) {
            vendorLevels = data;
            loadLogonUser();
        });
    });

    // $.getJSON(config.baseurl + "/api/menus/ebsUser", function (data) {
    //    $(data).each(function (index) {
    //        ebsUser.push(data[index].lastName);
    //    });
    // });
});

function loadLogonUser() {
    $.ajax({
        url: config.baseurl +"/api/logonUser",
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function() {},
        success: function(data) {
            logonUser = data;
            $('#tjr').text(logonUser.fullName);
        },
        complete: function() {window.parent.closeLoading();},
        error: function() {window.parent.closeLoading();}
    });
}

var t;
function queryList() {
    $('#dataTables-listAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-data"></table>');

    t = $('#dataTables-data').DataTable({
        "pageLength": 20,
        "lengthMenu": [20, 50, 100],
        "searching": false,
        "processing": true,
        "data": dataSet,
        "scrollY": "500px",
        "scrollCollapse": true,
        "pagingType": "full_numbers",
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "供应商名称", "data": "vendorName"},
            {"title": "申请状态", "data": "statusCode"},
            {"title": "查看更新", "data": "projectManagerName"},
            {"title": "供应商属性", "data": "vendorTypeCode"},
            {"title": "供应商性质", "data": "supplierNature"},
            {"title": "供应商等级", "data": "vendorLevel"},
            {"title": "是否有附件", "data": "attachmentExists"},
            {"title": "提交人", "data": "personName"}
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "targets": 0,
            "width": 32
        },{
            "searchable": false,
            "orderable": false,
            "targets": 1,
            "width": 300
        },{
            "width": 65,
            "targets": 2
            // "render": function (data, type, full, meta) {
            //     return "<a name='a_linkTask' href='#1'>" + data + "</a>";
            // }
        },{
            "width": 65,
            "targets": 3,
            "render": function (data, type, full, meta) {
                if ("NEW" == full.status || "REJECTED" == full.status)
                    return "<i class='fa fa-edit' style='cursor: pointer; color: green;'>更新</i>";
                else if ("APPROVING" == full.status || "APPROVED" == full.status)
                    return "<i class='fa fa-file-o' style='cursor: pointer; color: blue;'>查看</i>";
            }
        },{
            "width": 80,
            "targets": 4
        },{
            "width": 80,
            "targets": 5
        },{
            "width": 80,
            "targets": 6
        },{
            "width": 80,
            "targets": 7
        },{
            "width": 50,
            "targets": 8
        }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "initComplete": function () {
            $('#dataTables-data_wrapper').css("cssText", "margin-top: -5px;");
            $('#dataTables-data_length').insertBefore($('#dataTables-data_info'));
            $('#dataTables-data_length').addClass("col-sm-4");
            $('#dataTables-data_length').css("paddingLeft", "0px");
            $('#dataTables-data_length').css("paddingTop", "5px");
            $('#dataTables-data_length').css("maxWidth", "130px");
            t.page(now_pageNo).draw('page');
            now_pageNo = 0;
        }
    });

    $('#dataTables-data tbody').on('click', 'i', function () {
        // if (0 == ebsUser.length) {
        //     window.parent.warring("内部对接人数据还未加载完成，请稍后再试");
        //     return;
        // }
        var userTable = $('#dataTables-data').DataTable();
        var data = userTable.rows( $(this).parents('tr') ).data();
        showNo = 1;
        loadGys(data[0].vendorId);
        now_pageNo = t.page();
    });

    t.on( 'order.dt search.dt', function () {
        t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );
    } ).draw();

}

function loadGys(vendorId) {
    $.ajax({
        url : config.baseurl +"/api/tsc/ts/tsList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            vendorId: vendorId
        },
        type:"GET",   //请求方式
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            headerData = data.dataSource[0];
            loadGys_lxr(vendorId);
        },
        complete:function(){

        },
        error:function(){
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

function loadGys_lxr(vendorId) {
    $.ajax({
        url : config.baseurl +"/api/tsc/ts/supplierContact",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            vendorId: vendorId
        },
        type:"GET",   //请求方式
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            dataSetlxr = data;
            loadGys_zz(vendorId);
        },
        complete:function(){

        },
        error:function(){
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

var showNo = 1;
function loadGys_zz(vendorId) {
    $.ajax({
        url : config.baseurl +"/api/tsc/ts/attachmentContact",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            vendorId: vendorId
        },
        type:"GET",   //请求方式
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            dataSetzz = data;
            if (1 == showNo) {
                $('#addModal').modal("show");
            } else {
                initAddOrUpdateModal();
                queryLxrList();
                queryZzList();
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

$('#bu_queryData').click(function(){
    $.ajax({
        url : config.baseurl +"/api/tsc/ts/tsList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: $('#selectFrom').serialize(),
        type:"GET",   //请求方式
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            dataSet = data.dataSource;
            console.log(dataSet);
            queryList();
        },
        complete:function(){
            window.parent.closeLoading();
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
});

$('#bu_ts').on('click', function () {
    $('#gysModal').modal("show");
});

$('#gysModal').on('shown.bs.modal', function (e) {
    $("#gysModal .modal-body").load("gys.html", function() {
        initGysQueryBtn();
        queryGysList();
        setSupplierNatureSel("gys_nature","");
    });
});

var headerData;
$('#bu_add').on('click', function () {
    headerData = null;
    dataSetlxr = [];
    dataSetzz = [];
    $('#addModal').modal("show");
});

$('#addModal').on('shown.bs.modal', function (e) {
    $("#addModal .modal-body").load("tsAdd.html", function() {
        initAddOrUpdateModal();
        queryLxrList();
        queryZzList();
        initaddOrUpdateBtn();
    });
});

$('#bu_sq').on('click', function () {
    window.open(config.contextConfig.supplierOaUrl + logonUser.employeeNo);
});

function initAddOrUpdateModal() {
    $('#s_vendorId').val(headerData? headerData.vendorId : "");
    $('#s_vendorName').val(headerData? headerData.vendorName : "");
    $('#s_bankName').val(headerData? headerData.bankName : "");
    $('#s_bankAccountNum').val(headerData? headerData.bankAccountNum : "");
    $('#s_address').val(headerData? headerData.address : "");
    $('#s_vendorTypeCode').val(headerData? headerData.vendorTypeCode : "零星");
    $('#s_vendorType').val(headerData? headerData.vendorType : "L-VENDOR");
    $('#s_statusCode').val(headerData? headerData.statusCode : "新建");
    $('#s_status').val(headerData? headerData.status : "NEW");
    $('#s_vatRegistrationNum').val(headerData? headerData.vatRegistrationNum : "");
    $('#s_num_1099').val(headerData? headerData.num_1099 : "");
    setVendorLevelSel("s_vendorLevel", headerData? headerData.vendorLevel : "");
    setSupplierNatureSel("s_supplierNature", headerData? headerData.supplierNature : "");
    if (null != headerData) {
        if ("NEW" == headerData.status || "REJECTED" == headerData.status) {
            $('#bu_save').attr("disabled", false);
            $('#bu_submit').attr("disabled", false);
            $('#div_lxr').show();
            $('#div_zz').show();
            setDivDisplay(false);
        } else {
            $('#bu_save').attr("disabled", true);
            $('#bu_submit').attr("disabled", true);
            $('#div_lxr').hide();
            $('#div_zz').hide();
            setDivDisplay(true);
        }
    } else {
        $('#bu_save').attr("disabled", false);
        $('#bu_submit').attr("disabled", false);
        $('#div_lxr').show();
        $('#div_zz').show();
        setDivDisplay(false);
    }
    $('#div_add').show();
    $('#div_audit').hide();
}

function setDivDisplay(e) {
    $('#s_vendorName').attr("disabled", e);
    $('#s_bankName').attr("disabled", e);
    $('#s_bankAccountNum').attr("disabled", e);
    $('#s_address').attr("disabled", e);
    $('#s_vendorLevel').attr("disabled", e);
    $('#s_supplierNature').attr("disabled", e);
    $('#s_vatRegistrationNum').attr("disabled", e);
    $('#s_num_1099').attr("disabled", e);
}

function setVendorLevelSel(id, val) {
    var select = $('#' + id);
    select.empty();
    select.append("<option value=''>请选择</option>");
    for (var i = 0; i < vendorLevels.length; i++) {
        select.append("<option value='" + vendorLevels[i] + "'>" + vendorLevels[i] + "</option>")
    }
    select.val(val);
}

function setSupplierNatureSel(id, val) {
    var select = $('#' + id);
    select.empty();
    select.append("<option value=''>请选择</option>");
    for (var i = 0; i < supplierNatures.length; i++) {
        select.append("<option value='" + supplierNatures[i] + "'>" + supplierNatures[i] + "</option>")
    }
    select.val(val);
}

function loadPopoverVendorName(input) {
    if ("" == input.value) {
        $('#popoverVendorName').hide();
        return;
    }
    $.ajax({
        url : config.baseurl +"/api/tsc/ts/tsEbsList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            vendorName: input.value
        },
        type:"GET",   //请求方式
        beforeSend:function(){
        },
        success:function(data){
            var dataSetGys = data.dataSource;
            if (0 < dataSetGys.length) {
                $('#popoverVendorName').empty();
                $(dataSetGys).each(function (i) {
                    $('#popoverVendorName').append('<div class="row">' + dataSetGys[i].vendorName + '</div>');
                });
                $('#popoverVendorName').show();
            }
        },
        complete:function(){
        },
        error:function(){
        }
    });
    
}


