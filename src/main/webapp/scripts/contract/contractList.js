/**
 * Created by zhangwanchuan on 2018-2-14.
 */
var now_pageNo = 0;
var config = new nerinJsConfig();
var dataSet = [];
var logonUser;
var userSource;
var customerLevel;
var customerIndustry;
var customerSource;
var ebsUser = [];
var showNo = 1;
var headerData;
var areaObj = [];
var locationObj = {};
var selectOptions = {};
selectOptions.currency = [['CNY','人民币 Chinese Yuan'],
    ['USD','美元 United States Dollar'],
    ['EUR','欧元 Euro'],
    ['GBP','英镑 Pound Sterling'],
    ['RUB','俄罗斯卢布 Russian Ruble']
];
selectOptions.contractType = [['GCKC','工程勘察 '],
    ['GCZX','工程咨询 '],
    ['GCXJ','工程设计 '],
    ['ZCB','总承包 '],
    ['GCJL','工程监理 '],
    ['XMGL','项目管理 '],
    ['TZZX','投资咨询 '],
    ['SC','审查 '],
    ['DQZB','电气装备 '],
    ['JXZB','机械装备 '],
    ['KYKF','科研开发 '],
    ['CG','采购 '],
    ['SG','施工 '],
    ['PPP','PPP '],
    ['SJCG','设计采购 '],
    ['SJSG','设计施工 '],
    ['CGSG','采购施工 ']
];
selectOptions.contractBody1 = [['1','项目管理部 '],
    ['2','技术质量部 '],
    ['3','矿山事业部 '],
    ['4','冶金事业部 '],
    ['5','化工事业部 '],
    ['6','市政事业部 '],
    ['7','水务事业部 '],
    ['8','环安事业部 '],
    ['9','代建事业部 '],
    ['10','建筑设计研究院 '],
    ['11','勘察设计院 '],
    ['12','深圳分公司 '],
    ['13','东莞分公司 '],
    ['14','珠海分公司 '],
    ['15','佛山分公司 '],
    ['16','赣州分公司 '],
    ['17','海南分公司 '],
    ['18','杭州分公司 '],
    ['19','宁波分公司 '],
    ['20','江苏分公司 '],
    ['21','济南分公司 '],
    ['22','福州分公司 '],
    ['23','江苏分公司 '],
    ['24','江西瑞林装备有限公司 '],
    ['25','江西瑞林电气自动化有限公司 '],
    ['26','江西瑞林工程咨询有限公司 '],
    ['27','江西瑞林投资咨询有限公司 '],
    ['28','江西瑞林建设监理有限公司 '],
    ['29','江西铜瑞项目管理有限公司 ']
];
selectOptions.industry = [['ML','矿业 Mining(ML)'],
    ['ME','冶金 Metallurgy(ME)'],
    ['CP','化工 Chemical Process(CP)'],
    ['NP','加工 Non-Ferrous Metal Processing(NP)'],
    ['EE','环境 Environment Engineering(EE)'],
    ['AR','建筑 Architecture(AR)'],
    ['EU','市政 Municipal(EU)'],
    ['HE','水务 Hydraulic Engineering(HE)'],
    ['EP','环保 Environment Protection(EP)']
];
selectOptions.contry = [];
selectOptions.province = [];
selectOptions.city = [];
selectOptions.type = [[1, '工程'],
    [2, '科研'],
    [3, '内部']];
var api = {
    selectFormQuery: config.baseurl +"/scripts/contract/testData.json",
    customer: config.baseurl +"/scripts/contract/customer.json",
    customer1: config.baseurl +"/api/tsc/tc/tcEbsList",
    location: config.baseurl + "/scripts/tsc/tc/location_chs.js"
};

$(function () {
    queryList();
    $.ajax({
        url: api.location,
        dataType: "script",
        async:false
    });
    queryFormControl();
    checkValidate();
    addFormControl();

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

    // 查询按钮事件
    $('#btnSelectFormQuery').click(function(){
        $.ajax({
            url : api.selectFormQuery,
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: $('#selectFrom').serialize(),
            type:"GET",   //请求方式
            beforeSend:function(){
                window.parent.showLoading();
            },
            success:function(data){
                dataSet = data.dataSource;
                // console.log(dataSet);
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
    // 客户库按钮
    $('#bu_kh').on('click', function () {
        $('#khModal').modal("show");
    });
    // 联系人库按钮
    $('#bu_lxr').on('click', function () {
        $('#lxrModal').modal("show");
    });
    // 申请权限按钮
    $('#bu_sq').on('click', function () {
        window.open(config.contextConfig.oaUrl + logonUser.employeeNo);
    });
    // 客户模态框
    $('#khModal').on('shown.bs.modal', function (e) {
        $("#khModal .modal-body").load("kh.html", function() {
            initKhQueryBtn();
            queryKhList();
        });
    });
    // 联系人模态框
    $('#lxrModal').on('shown.bs.modal', function (e) {
        $("#lxrModal .modal-body").load("lxr.html", function() {
            initlxrQueryBtn();
            querylxrList();
        });
    });
    // 新增按钮-addModal
    $('#bu_add').on('click', function () {
        headerData = null;
        dataSetlxr = [];
        dataSetReceipt = [];
        dataSetzz = [];
        $('#addModal').modal("show");
    });
    // 新增模态框
    $('#addModal').on('shown.bs.modal', function (e) {
        initAddOrUpdateModal();
        initReceiptList();
        // queryZzList();
        // initaddOrUpdateBtn();
    });
    // 新增模态框按钮
    $('#bu_addReceipt').on('click', function () {
        addReceipt();
    });
    $('#bu_delReceipt').on('click', function () {
        delReceiptLine();
    });
});

//登录检查
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
// 绘制查询结果 datatables
function queryList() {
    // DataTable 初始化
    var t;
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
            {"title": "合同编号", "data": "contractId"},
            {"title": "合同名称", "data": "contractName"},
            {"title": "执行部门", "data": "contractDepCode"},
            {"title": "签订日期", "data": "contractDate"},
            {"title": "状态", "data": "contractStatusCode"},
            {"title": "甲方", "data": "contractPartyA"},
            {"title": "总金额", "data": "contractMoney"},
            {"title": "币种", "data": "contractCurrencyCode"},
            {"title": "境内外", "data": "isAbroad"},
            {"title": "高新", "data": "isGX"},
            {"title": "商机号", "data": "BuOpId"},
            {"title": "操作", "data": null}
        ],
        "columnDefs": [
            {
                "targets": 0,
                "width": 32
            },{
                "targets": 1,
                "width": 60
            },{
                "width": 200,
                "targets": 2
            },{
                "width": 65,
                "targets": 3
            },{
                "width": 80,
                "targets": 4
            },{
                "width": 65,
                "targets": 5
            },{
                "width": 200,
                "targets": 6
            },{
                "width": 80,
                "targets": 7
            },{
                "width": 50,
                "targets": 8
            },{
                "width": 40,
                "targets": 9,
                "render": function (data, type, full, meta) {
                    var map = {"native": "境内", "abroad": "境外"};
                    return map[data];
                }
            },{
                "width": 40,
                "targets": 10,
                "render": function (data, type, full, meta) {
                    return data ? "是" : "否";
                }
            },{
                "width": 40,
                "targets": 11
            },{
                "width": 50,
                "targets": 12,
                "render": function (data, type, full, meta) {
                    if ("NEW" == full.contractStatus || "REJECTED" == full.contractStatusCode)
                        return "<i class='fa fa-edit' style='cursor: pointer; color: green;'>更新</i>";
                    else if ("APPROVING" == full.contractStatusCode || "APPROVED" == full.contractStatusCode)
                        return "<i class='fa fa-file-o' style='cursor: pointer; color: blue;'>查看</i>";
                }
            }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "initComplete": function () {
            $('#dataTables-data_wrapper').css("cssText", "margin-top: -5px;");
            $('#dataTables-data_length')
                .insertBefore($('#dataTables-data_info'))
                .addClass("col-sm-4")
                .css("paddingLeft", "0px")
                .css("paddingTop", "5px")
                .css("maxWidth", "130px");
            t.page(now_pageNo).draw('page');
            now_pageNo = 0;
        }
    });

    //申请状态列os跳转事件
    // $('#dataTables-data tbody').on('click', 'a[name=a_linkTemplate2]', function () {
    //     var userTable = $('#dataTables-data').DataTable();
    //     var data = userTable.rows( $(this).parents('tr') ).data();
    //     window.open(data[0].attribute3);
    // });

    // 查看更新列点击事件
    // $('#dataTables-data tbody').on('click', 'i', function () {
    //     // if (0 == ebsUser.length) {
    //     //     window.parent.warring("内部对接人数据还未加载完成，请稍后再试");
    //     //     return;
    //     // }
    //     var userTable = $('#dataTables-data').DataTable();
    //     var data = userTable.rows( $(this).parents('tr') ).data();
    //     showNo = 1;
    //     loadKh(data[0].customerId);
    //     now_pageNo = t.page();
    // });

    t.on('order.dt search.dt', function () {
        t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        });
    }).draw();
}
// 初始化查询条件控件
function queryFormControl() {
    $("#contractDate").datepicker($.datepicker.regional["zh-CN"]);
    $("#contractDate").attr("readonly","readonly");
    $("#industry").html(createSelect(selectOptions.industry,false,true));
    $("#industry2").html(createSelect(selectOptions.industry,false,true));
}
// 初始化新增模态框控件
function addFormControl() {
    var country = $('#country'), province = $('#province'), city = $('#city');

    // 解决模态框内 select2 插件搜索框无效的问题
    $.fn.modal.Constructor.prototype.enforceFocus = function () {};

    $('#belongDep').html(createSelect(selectOptions.contractBody1, false,true));
    $('#ExecutiveDep').html(createSelect(selectOptions.contractBody1, false,true));
    $('#PartyA').css("width","100%").select2({
        ajax: {
            url: function (params) {
                return api.customer1;
            },
            data: function (params) {
                return {
                    partyName: params.term,
                    pageSize: 10,
                    pageNo: 1
                };
            },
            processResults: function (data) {
                return {
                    results: data.dataSource
                };
            },
            dataType: 'json',
            delay: 500
        },
        escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
        templateResult: function (result) {
            return result.partyName || result.text;
        },
        templateSelection: function (result) {
            return result.partyName || result.text;
        },
        multiple : true,
        minimumInputLength: 1,
        language: "zh-CN"
    });
    country.html(createSelect(selectOptions.contry, false,true)).css("width","100%").select2({language: "zh-CN"})
        .on('change', function(e){
            city.val(null).html("");
            province.val(null).html("");
            var contryVal = country.val();
            selectOptions.province = getProvince(contryVal);
            if (selectOptions.province.length) {
                province.html(createSelect(selectOptions.province, false,true)).css("width","100%").attr('title','请直接选择城市')
                    .select2({language: "zh-CN"})
                    .on('change', function(e){
                        city.val(null).html("");
                        var provinceVal = province.val();
                        selectOptions.city = getCity(contryVal, provinceVal);
                        if (selectOptions.city.length) {
                            $('#city').html(createSelect(selectOptions.city, false, true)).css("width", "100%").attr('title', '请直接选择城市')
                                .select2({language: "zh-CN"})
                        }
                    });
            } else {
                province.html("").css("width","100%").attr('title','请直接选择城市');
                selectOptions.city = getCity(contryVal);
                if (selectOptions.city.length) {
                    city.html(createSelect(selectOptions.city, false, true)).css("width", "100%").attr('title', '请直接选择城市')
                        .select2({language: "zh-CN"})
                }
            }

        });
    $('#addFormIndustry1').html(createSelect(selectOptions.industry, false,true));
    $('#addFormIndustry2').html(createSelect(selectOptions.industry, false,true));
    $('#type').html(createSelect(selectOptions.type, false,true));
    $('#addFormCurrency').html(createSelect(selectOptions.currency, false,true));
    $('#addFormDate').datepicker($.datepicker.regional["zh-CN"]);
}
// 初始化世界城市信息
function initLocation(e){
    locationObj = e;
    for (var m in e) {
        if (m != 0) {
            selectOptions.contry.push([m, e[m].n]);
        }
    }
}
// 获取省份
function getProvince(contry){
    var province = [];
    for (var m in locationObj[contry]) {
        if (typeof locationObj[contry][m] === "object" && m != 0) {
            province.push([m, locationObj[contry][m].n])
        }
    }
    return province
}
// 获取城市
function getCity(contry, province){
    var city = [];
    province = province || 0;
    for (var m in locationObj[contry][province]) {
        if (typeof locationObj[contry][province][m] === "object" && m != 0) {
            city.push([m, locationObj[contry][province][m].n])
        }
    }
    return city
}

// 获取客户基本信息 headerData
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
// 获取客户联系人信息 dataSetlxr
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
// 获取客户附件 dataSetzz
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
    $('#s_industry1').html(createSelect(selectOptions.industry, false,true));
    $('#s_type').html(createSelect(selectOptions.contractType, false,true));

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
// 弹出框 客户名称
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

function initReceiptList(){
    $('#dataTables-receipt-wrapper')
        .html('<table cellpadding="0" cellspacing="0" border="0" class="table table-bordered table-hover" id="dataTables-receipt"></table>');

    var t_receipt = $('#dataTables-receipt').DataTable({
        "searching": false,
        "processing": true,
        "data": dataSetReceipt,
        "scrollY": "300px",
        "scrollCollapse": true,
        "paging": false,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "<input type='checkbox' name='ch_receiptAll' id='ch_receiptAll' onclick='javascript:setCheckReceiptAll();' />", "data": null, "className": "style_column"},
            {"title": "<font color='red'>*收费阶段</font>", "data": "receiptPhase"},
            {"title": "<font color='red'>*开始时间</font>", "data": "receiptStartTime"},
            {"title": "<font color='red'>*结束时间</font>", "data": "receiptEndTime"},
            {"title": "金额", "data": "receiptMoney"},
            {"title": "币种", "data": "receiptCurrency"},
            {"title": "备注", "data": "receiptComments"}
        ],
        "columnDefs": [{
            "orderable": false,
            "targets": 0,
            "width": 32
        },{
            "orderable": false,
            "targets": 1,
            "width": 100
        },{
            "width": 150,
            "targets": 2
        },{
            "width": 150,
            "targets": 3
        },{
            "width": 100,
            "targets": 4
        },{
            "width": 200,
            "targets": 6
        }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            $(row).children('td').eq(1).html(createInput("hidden", data.receiptId)).append(createInput("text", data.receiptPhase));
            $(row).children('td').eq(2).html(createInput("text", data.receiptStartTime));
            $(row).children('td').eq(3).html(createInput("text", data.receiptEndTime));
            $(row).children('td').eq(4).html(createInput("text", data.receiptMoney));
            $(row).children('td').eq(5).html(createSelect("select", selectOptions.currency));
            $(row).children('td').eq(6).html(createInput("text", data.receiptComments));
        },
        "initComplete": function () {
        }
    });

    t_receipt.on( 'order.dt search.dt', function () {
        t_receipt.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = "<input type='checkbox' name='ch_receipt' />";
        } );
    } ).draw();
}
// 新增模态框新增一条收款记录行
function addReceipt(){
    var o = {};
    o.receiptId = "";
    o.receiptPhase = "";
    o.receiptStartTime = "";
    o.receiptEndTime = "";
    o.receiptMoney = "";
    o.receiptCurrency = "";
    o.receiptComments = "";
    $('#dataTables-receipt').DataTable().row.add(o).draw();
}
// 新增模态框删除收款记录行
function delReceiptLine() {
    var arr = [];
    var table = $('#dataTables-receipt').DataTable();
    var allCh = $("input[name='ch_receipt']");
    var allData = $("#dataTables-receipt  tr:not(:first)");
    $("input[name='ch_receipt']:checked").each(function (i) {
        var n = allCh.index($(this));
        var $data = $(allData[n]);
        var jqInputs = $('input', $data);
        arr.push( $(jqInputs[1]).val());
    });
    // if (0 < arr.length) {
    //     $(arr).each(function (i) {
    //         if ("" != arr[i]) {
    //             $.ajax({
    //                 url : config.baseurl +"/api/tsc/tc/delContact",
    //                 contentType : 'application/x-www-form-urlencoded',
    //                 dataType:"json",
    //                 data: {
    //                     customerId: $('#s_customerId').val(),
    //                     contactId: arr[i]
    //                 },
    //                 type:"POST",
    //                 beforeSend:function(){
    //
    //                 },
    //                 success:function(data){
    //
    //                 },
    //                 complete:function(){
    //
    //                 },
    //                 error:function(){
    //                     window.parent.error(ajaxError_loadData);
    //                 }
    //             });
    //         }
    //     });
    // }
    table.rows( $("input[name='ch_receipt']:checked").parents('tr')).remove();
    table.draw();
}
// 新增模态框全选收款记录行
function setCheckReceiptAll() {
    var isChecked = $('#ch_receiptAll').prop("checked");
    $("input[name='ch_receipt']").prop("checked", isChecked);
}
// 创建datatables内输入框
function createInput(type, val, flag) {
    var input;
    if (undefined == val)
        val = "";
    input = $("<input type='" + type + "' style='width: 100%; display: inline-block;' class='form-control input-sm' value='" + val + "' />");
    // if (undefined != flag && flag) {
    //     input.autocomplete({
    //         source: ebsUser,
    //         minLength: 0
    //     });
    // }
    return input;
}
// 创建datatables内选择框
function createSelect(vn, isLastOther, onlyOptions){
    var selector, options;
    var i;
    isLastOther = isLastOther || false;
    onlyOptions = onlyOptions || false;
    selector = "<select class='form-control input-sm' style='width: 100%;' title=''>";
    options = "<option value=''>-请选择-</option>";
    for(i=0;i<vn.length;i++){
        options += "<option value='" + vn[i][0] + "'>" + vn[i][1] +"</option>";
    }
    if(isLastOther){options += "<option value='OT'>其他</option>";}
    selector += options;
    selector += "</select>";
    if(onlyOptions){
        return options;
    }else{
        return selector;
    }
}
// 验证
function checkValidate() {
    $('#addForm').bootstrapValidator({
        live: 'enabled',
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            s_contractId: {
                validators: {
                    notEmpty: {
                        message: '合同名称不可为空'
                    }
                }
            },
            belongDep: {
                validators: {
                    notEmpty: {
                        message: '归属部门不可为空'
                    }
                }
            },
            ExecutiveDep: {
                validators: {
                    notEmpty: {
                        message: '执行部门不可为空'
                    }
                }
            }
        }
    });
}
