$(document).keyup(function(event){
    switch(event.keyCode) {
        case 13:
            $('#bu_queryData').trigger("click");
    }
});

var now_pageNo = 0;
var config = new nerinJsConfig();
var dataSet = [];
var logonUser;
var userSource;
var customerLevel;
var customerIndustry;
var customerSource;
var ebsUser = [];
$(function () {
    $('#bu_queryData').focus();
    queryList();
    window.parent.showLoading();
    $.getJSON(config.baseurl + "/api/tsc/tc/queryUserOrg", function (data) {
        userSource = data;
        $.getJSON(config.baseurl + "/api/tsc/tc/queryCustomerLevel", function (data) {
            customerLevel = data;
            $.getJSON(config.baseurl + "/api/tsc/tc/queryCustomerIndustry", function (data) {
                customerIndustry = data;
                $.getJSON(config.baseurl + "/api/tsc/tc/queryCustomerSource", function (data) {
                    customerSource = data;
                    loadLogonUser();
                });
            });
        });
    });
if("prod"==config.evn)
    $('#bu_lxr').hide();

    // $.getJSON(config.baseurl + "/api/menus/ebsUser", function (data) {
    //     $(data).each(function (index) {
    //         ebsUser.push(data[index].lastName);
    //     });
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
            {"title": "客户名称", "data": "partyName"},
            {"title": "客户简称", "data": "organizationName"},
            {"title": "申请状态", "data": "statusCode"},
            {"title": "查看更新", "data": "projectManagerName"},
            {"title": "客户属性", "data": "customerCategoryCode"},
            {"title": "是否有附件", "data": "attachmentExists"},
            {"title": "客户来源", "data": "customerSourceCode"},
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
            "width": 200
        },{
            "width": 200,
            "targets": 2
        },{
            "width": 65,
            "targets": 3,
            "render": function (data, type, full, meta) {
                if ("APPROVING" == full.status || "APPROVED" == full.status || "REJECTED" == full.status)
                    return "<a name='a_linkTemplate2' href='#1'>" + data + "</a>";
                else
                    return data;
            }
        },{
            "width": 80,
            "targets": 4,
            "render": function (data, type, full, meta) {
                if ("NEW" == full.status || "REJECTED" == full.status)
                    return "<i class='fa fa-edit' style='cursor: pointer; color: green;'>更新</i>";
                else if ("APPROVING" == full.status || "APPROVED" == full.status)
                    return "<i class='fa fa-file-o' style='cursor: pointer; color: blue;'>查看</i>";
            }
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

    $('#dataTables-data tbody').on('click', 'a[name=a_linkTemplate2]', function () {
        var userTable = $('#dataTables-data').DataTable();
        var data = userTable.rows( $(this).parents('tr') ).data();
        window.open(data[0].attribute3);
    });

    $('#dataTables-data tbody').on('click', 'i', function () {
        // if (0 == ebsUser.length) {
        //     window.parent.warring("内部对接人数据还未加载完成，请稍后再试");
        //     return;
        // }
        var userTable = $('#dataTables-data').DataTable();
        var data = userTable.rows( $(this).parents('tr') ).data();
        showNo = 1;
        loadKh(data[0].customerId);
        now_pageNo = t.page();
    });

    t.on( 'order.dt search.dt', function () {
        t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );
    } ).draw();

}

function loadKh(customerId) {
    $.ajax({
        url : config.baseurl +"/api/tsc/tc/tcList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            customerId: customerId
        },
        type:"GET",   //请求方式
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            headerData = data.dataSource[0];
            loadGys_lxr(customerId);
        },
        complete:function(){

        },
        error:function(){
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

function loadGys_lxr(customerId) {
    $.ajax({
        url : config.baseurl +"/api/tsc/tc/contact",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            customerId: customerId
        },
        type:"GET",   //请求方式
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            dataSetlxr = data;
            loadGys_zz(customerId);
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
function loadGys_zz(customerId) {
    $.ajax({
        url : config.baseurl +"/api/tsc/tc/attachmentContact",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            customerId: customerId
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
        url : config.baseurl +"/api/tsc/tc/tcList",
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

$('#bu_kh').on('click', function () {
    $('#khModal').modal("show");
});

$('#bu_lxr').on('click', function () {
    $('#lxrModal').modal("show");
});

$('#bu_sq').on('click', function () {
    window.open(config.contextConfig.oaUrl + logonUser.employeeNo);
});

$('#khModal').on('shown.bs.modal', function (e) {
    $("#khModal .modal-body").load("kh.html", function() {
        initKhQueryBtn();
        queryKhList();
    });
});

$('#lxrModal').on('shown.bs.modal', function (e) {
    $("#lxrModal .modal-body").load("lxr.html", function() {
        initlxrQueryBtn();
        querylxrList();
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
    $("#addModal .modal-body").load("tcAdd.html", function() {
        initAddOrUpdateModal();
        queryLxrList();
        queryZzList();
        initaddOrUpdateBtn();
    });
});

function initAddOrUpdateModal() {
    $('#s_customerId').val(headerData? headerData.customerId : "");
    $('#s_partyName').val(headerData? headerData.partyName : "");
    $('#s_organizationName').val(headerData? headerData.organizationName : "");
    setCustomerSource("s_customerSource", headerData? headerData.customerSource : "");
    // $('#s_customerSource').val(headerData? headerData.customerSource : userSource.organizationId);
    // $('#s_customerSourceCode').val(headerData? headerData.customerSourceCode : userSource.organizationName);
    $('#s_customerCategory').val(headerData? headerData.customerCategory : "");
    setCustomerLevel("s_customerLevel", headerData? headerData.customerLevel : "");
    setCustomerIndustry("s_customerIndustry", headerData? headerData.customerIndustry : "");
    $('#s_address').val(headerData? headerData.address : "");
    $('#s_vatRegistrationNum').val(headerData? headerData.vatRegistrationNum : "");
    $('#s_statusCode').val(headerData? headerData.statusCode : "新建");
    $('#s_status').val(headerData? headerData.status : "NEW");
    $('#s_knownAs').val(headerData? headerData.knownAs : "");
    $('#s_customerUrl').val(headerData? headerData.customerUrl : "");
    $('#s_commets').val(headerData? headerData.commets : "");

    setLabelName($('#s_customerCategory').get(0));

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

function setCustomerSource(id, val) {
    var select = $('#' + id);
    select.empty();
    select.append("<option value=''>请选择</option>");
    for (var i = 0; i < customerSource.length; i++) {
        select.append("<option value='" + customerSource[i].sourceKey + "'>" + customerSource[i].sourceValue + "</option>")
    }
    select.val(val);
}

function setCustomerLevel(id, val) {
    var select = $('#' + id);
    select.empty();
    select.append("<option value=''>请选择</option>");
    for (var i = 0; i < customerLevel.length; i++) {
        select.append("<option value='" + customerLevel[i].sourceKey + "'>" + customerLevel[i].sourceKey + "</option>")
    }
    select.val(val);
}

function setCustomerIndustry(id, val) {
    var select = $('#' + id);
    select.empty();
    select.append("<option value=''>请选择</option>");
    for (var i = 0; i < customerIndustry.length; i++) {
        select.append("<option value='" + customerIndustry[i].sourceKey + "'>" + customerIndustry[i].sourceValue + "</option>")
    }
    select.val(val);
}

function setDivDisplay(e) {
    $('#s_partyName').attr("disabled", e);
    $('#s_organizationName').attr("disabled", e);
    $('#s_customerCategory').attr("disabled", e);
    $('#s_customerLevel').attr("disabled", e);
    $('#s_customerIndustry').attr("disabled", e);
    $('#s_address').attr("disabled", e);
    $('#s_vatRegistrationNum').attr("disabled", e);
    $('#s_knownAs').attr("disabled", e);
    $('#s_customerUrl').attr("disabled", e);
    $('#s_commets').attr("disabled", e);
    $('#s_customerSource').attr("disabled", e);
    $('#s_province').attr("disabled", e);
    $('#s_city').attr("disabled", e);
}

function loadPopoverVendorName(input) {
    if ("" == input.value) {
        $('#popoverVendorName').hide();
        return;
    }
    $.ajax({
        url : config.baseurl +"/api/tsc/tc/tcEbsList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            partyName: input.value
        },
        type:"GET",   //请求方式
        beforeSend:function(){
        },
        success:function(data){
            var dataSetGys = data.dataSource;
            if (0 < dataSetGys.length) {
                $('#popoverVendorName').empty();
                $(dataSetGys).each(function (i) {
                    $('#popoverVendorName').append('<div class="row">' + dataSetGys[i].partyName + '</div>');
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
