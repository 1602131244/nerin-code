var catHeaderData;
var catLineData;
var catId;

function searchCatHeaders(id) {
    catId = id;

    $.getJSON(config.baseurl + "/api/draw/searchCatHeaders?id=" + id, function (data) {
        catHeaderData = data;
        initCatHeader();
        $.getJSON(config.baseurl + "/api/draw/searchCatLines?id=" + id, function (data) {
            catLineData = data;
            $('#DrawCatalog').modal('show');
            
        });
    });
}

function initCatHeader() {
    $('.SubmitCatalogC').attr("disabled", true);
    $('#ProjectSNameC').val(catHeaderData.projectSname);
    $('#DrawNumC').val(catHeaderData.drawingNum);
    $('#DesignPhaseC').val(catHeaderData.phaseName);
    $('#SpecialtyC').val(catHeaderData.specialityClass);
    $('#RefDrawNumC').val(catHeaderData.drawingLnum);
    $('#RevisionC').val(catHeaderData.versionNum);
    $('#DesignDateC').val(catHeaderData.catDate);
    $('#UnitTaskNameC').val(catHeaderData.unitTaskName);
    $('#EquipmentC').val(catHeaderData.equipmentC);
    $.getJSON(config.baseurl + '/api/draw/drawsCatalogTem?projectId=' + selectedDraws[0].projectId, function (data) {
        var select = $("#TemplateC");
        select.empty();
        select.append("<option value=''>请选择</option>");
        $(data).each(function (index) {
            select.append($('<option value="' + data[index].templateCode + '">' + data[index].templateName + '</option>'));
        });
        // 默认中文
        select.val("01");
    });
}

var t_catLine;
function initCatList() {
    $('#DrawCatalog_container').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="DrawCatalog_table"></table>');
    t_catLine = $('#DrawCatalog_table').DataTable({
        //设置数据源
        "data": catLineData,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        //定义列名
        "columns": [
            {"title": "序号", "data": null},
            {"title": "图纸名称", "data": "ddoctitle"},
            {"title": "图号", "data": "ddocname"},
            {"title": "图纸规格", "data": "xsizeNum"},
            {"title": "备注", "data": "comments"},
        ],
        //控制隐藏列
        "columnDefs": [
            {
                "orderable": false,
                "targets": 0,
                "width": 32
            },
            {
                "orderable": false,
                "targets": 1,
                "width": 400
            },
            {
                "orderable": false,
                "targets": 2,
                "width": 260
            },
            {
                "searchable": false,
                "orderable": false,
                "targets": 3,
                "width": 180
            },
            {
                "targets": 4
            }
        ],
        //不分页，加入垂直导航条
        "scrollY": "400",
        "scrollCollapse": true,
        "paging": false,
        "searching": false,
        "order": [],
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "initComplete": function () {
            $('#DrawCatalog_table_wrapper').css("cssText", "margin-top: -5px;");
        }
    });

    t_catLine.on( 'order.dt search.dt', function () {
        t_catLine.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );
    } ).draw();
}

//预览图纸目录--没有完全实现
$('.PreviewCatalogC').on('click', function () {
    generateDrawsCagalog();
});

var xmlid;
var tempid;
function generateDrawsCagalog() {
    var templateCode = $('#TemplateC').val();
    if ("" == templateCode) {
        window.parent.warring("请选择模板");
        return;
    }
    var val = "";
    if ("01" == templateCode)
        val = "CUXDrawContentReport";
    else if ("02" == templateCode)
        val = "CUXDrawContentENReport";
    else if ("03" == templateCode)
        val = "CUXDrawContentDNReport";
    window.parent.showLoading();
    $.getJSON(config.baseurl + "/api/draw/getSourceFileId?templateCode=" + val + "&proId=" + catHeaderData.projectId, function (data) {
        window.parent.closeLoading();
        xmlid = data.p_xml_id;
        tempid = data.p_rtf_id;
        window.open(config.contextConfig.catPdfUrl + "rep/?" + "xmlid=" + data.p_xml_id + "&tempid=" + data.p_rtf_id + "&argsname=P_HEADER_ID&argsvalue=" + catId);
        $('.SubmitCatalogC').removeAttr("disabled");
    });

}

//提交图纸目录
$('.SubmitCatalogC').on('click', function () {
    submitDrawCatalog();
});

//提交图纸目录
function submitDrawCatalog() {
    var templateCode = $('#TemplateC').val();
    if ("" == templateCode) {
        window.parent.warring("请选择模板");
        return;
    }
    window.parent.showConfirm(function () {
        $.ajax({
            url: config.baseurl + "/api/draw/submitCatHeaders",
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",   //返回格式为json
            data: {
                id: catId,
                templateCode: templateCode,
                tw: tw,
                xmlid: xmlid,
                tempid: tempid,
                docType: 'PJT-DRAW'
            },
            type: "POST",   //请求方式
            beforeSend: function () {
                window.parent.showLoading();
            },
            success: function (data) {
                if ("0000" == data.returnCode) {
                    window.parent.success("提交成功！");
                    $('#DrawCatalog').modal('hide');
                    selectedDraws = [];
                    loadSelectedTable();
                    initTmpPlt();
                } else {
                    window.parent.error(data.returnMsg);
                }
            },
            complete: function () {
                window.parent.closeLoading();
            },
            error: function () {
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }, "确定要提交图号为：" + catHeaderData.drawingNum + "，版本号：" + catHeaderData.versionNum + "的图纸目录吗？");
}