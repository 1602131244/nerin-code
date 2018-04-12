var data_set_buildingInstitutePro = [];
var data_set_buildingInstitutePro2 = [];
var data_set_buildingInstitutePro3 = [];
var data_tree_buildingInstitutePro = [{"title" : "<div style='background: #77923c; padding: 2px; color: white; border-radius: 4px; float: left;'>阶段</div><div style='float: left; padding-top: 2px;'>施工图设计</div>"
    , children: [ {
        "title": "<div style='background: #FDBF01; padding: 2px; color: white; border-radius: 4px; float: left;'>子项</div><div style='float: left; padding-top: 2px;'>0000默认子项</div>",
        children: [ {
            "title": "<div style='background: #00AF50; padding: 2px; color: white; border-radius: 4px; float: left;'>专业</div><div style='float: left; padding-top: 2px;'>AR 建筑</div>"
        }, {
            "title": "<div style='background: #00AF50; padding: 2px; color: white; border-radius: 4px; float: left;'>专业</div><div style='float: left; padding-top: 2px;'>ST 结构</div>"
        }, {
            "title": "<div style='background: #00AF50; padding: 2px; color: white; border-radius: 4px; float: left;'>专业</div><div style='float: left; padding-top: 2px;'>EL 电力</div>"
        }]
    }]
}];

glyph_opts_2 = {
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

function createTree(projectId, phaseCode) {
    $("#table_stage_buildingInstitutePro2_" + activeTabId).fancytree({
        selectMode: 3,
        extensions: ["dnd", "edit", "glyph", "table"],
        checkbox: true,
        keyboard: false,
        dnd: {
            focusOnClick: true
        },
        glyph: glyph_opts,
        source: [],
        table: {
            checkboxColumnIdx: 0,
            nodeColumnIdx: 1
        },
        select: function(event, data) {

        },
        renderColumns: function(event, data) {
            var node = data.node,
                $tdList = $(node.tr).find(">td");
            $tdList.eq(2).text(node.data.taskName);
        },
        removeNode: function (event, data) { // 删除node触发
            var node = data.node;
            if (undefined != node.data.chapterId)
                delChapterIds.push(node.data.chapterId);
        },
        activate: function(event, data) { //选中触发

        },
        wide: {
            iconWidth: "1em",     // Adjust this if @fancy-icon-width != "16px"
            iconSpacing: "0.5em", // Adjust this if @fancy-icon-spacing != "3px"
            levelOfs: "1.5em"     // Adjust this if ul padding != "16px"
        },
        icon: function(event, data){

        },
        lazyLoad: function(event, data) {

        }
    });
    $("#table_stage_buildingInstitutePro3_" + activeTabId).fancytree({
        selectMode: 3,
        extensions: ["dnd", "edit", "glyph", "table"],
        checkbox: true,
        keyboard: false,
        dnd: {
            focusOnClick: true
        },
        glyph: glyph_opts,
        source: [],
        table: {
            checkboxColumnIdx: 0,
            nodeColumnIdx: 1
        },
        select: function(event, data) {

        },
        renderColumns: function(event, data) {
            var node = data.node,
                $tdList = $(node.tr).find(">td");
            $tdList.eq(2).text(node.data.taskName);
        },
        removeNode: function (event, data) { // 删除node触发
            var node = data.node;
            if (undefined != node.data.chapterId)
                delChapterIds.push(node.data.chapterId);
        },
        activate: function(event, data) { //选中触发

        },
        wide: {
            iconWidth: "1em",     // Adjust this if @fancy-icon-width != "16px"
            iconSpacing: "0.5em", // Adjust this if @fancy-icon-spacing != "3px"
            levelOfs: "1.5em"     // Adjust this if ul padding != "16px"
        },
        icon: function(event, data){

        },
        lazyLoad: function(event, data) {

        }
    });
}

function reload_buildingInstitutePro2_tree(data) {
    var tree = $("#table_stage_buildingInstitutePro2_" + activeTabId).fancytree("getTree");
    tree.reload(data).done(function() {
        $("#table_stage_buildingInstitutePro2_" + activeTabId).fancytree("getTree").visit(function (node) {
            node.setExpanded(true);
        });
    });
}

function reload_buildingInstitutePro3_tree(data) {
    var tree = $("#table_stage_buildingInstitutePro3_" + activeTabId).fancytree("getTree");
    tree.reload(data).done(function() {
        $("#table_stage_buildingInstitutePro3_" + activeTabId).fancytree("getTree").visit(function (node) {
            node.setExpanded(true);
        });
    });
}

function tab_initBuildingInstitutePro() {
    createBuildingInstitutePro();
}

function getBuildingInstitutePro() {
    var $tr = $('#t_OaOverAll_' + activeTabId + ' tbody tr.dt-select');
    var id = "";
    var formId = "";
    if (1 == $tr.length) {
        var $td = $($($tr[0]).find('td')[0]);
        var inputs = $td.find('input');
        id = inputs[0].value;
        formId = inputs[1].value;
    }
    var status = "";
    $('input[name="ch_buildingInstitutePro_' + activeTabId + '"]:checked').each(function () {
        status += "'" + $(this).val() + "'" + ",";
    });
    if (0 < status.length)
        status = status.substr(0, status.length - 1);
    var xmbh = $('#div_pro_header_' + activeTabId).text().split("(")[0];

    $.ajax({
        url: config.baseurl + "/api/naviIndexRest/getOaOverAll",
        contentType: "application/json",
        dataType: "json",
        data: {
            xmbh: xmbh,
            id: id,
            formid: formId,
            status: status,
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {
            window.parent.showLoading();
        },
        success: function (data) {
            createBuildingInstitutePro(data);
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

function createBuildingInstitutePro(data) {
    $('#dataTables-buildingInstitutePro_all_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-buildingInstitutePro_' + activeTabId + '"></table>');

    var tmpDt = $('#dataTables-buildingInstitutePro_' + activeTabId).DataTable({
        "processing": true,
        "autoWidth": false,
        "data": data ? data : [],
        "pagingType": "full", //简单模式，只有上下页
        "searching": false,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "流程名称", "data": "workflowname"},
            {"title": "创建人", "data": "lastname"},
            {"title": "状态", "data": "status"},
            {"title": "创建时间", "data": "createtime"},
            {"title": "办结时间", "data": "endtime"}
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "width": 32,
            "targets": 0 //排序
        },{
            "targets": 1,
            "render": function (data, type, full, meta) {
                return "<a name='a_modal_oa' href='#1'>" + data + "</a>";
            }
        }
        ],
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "order": [],
        "initComplete": function () {
            $('#dataTables-buildingInstitutePro_' + activeTabId + '_length').insertBefore($('#dataTables-buildingInstitutePro_' + activeTabId + '_info'));
            $('#dataTables-buildingInstitutePro_' + activeTabId + '_length').addClass("col-sm-4");
            $('#dataTables-buildingInstitutePro_' + activeTabId + '_length').css("paddingLeft", "0px");
            $('#dataTables-buildingInstitutePro_' + activeTabId + '_length').css("paddingTop", "5px");
            $('#dataTables-buildingInstitutePro_' + activeTabId + '_length').css("maxWidth", "130px");
            $('#dataTables-buildingInstitutePro_' + activeTabId).css("cssText", "margin-top: 0px !important;");
        }
        // "initComplete": function () { //去掉标准表格多余部分
        //     $('#dataTables-mhours-line_length').remove();
        //     $('#dataTables-mhours-line_info').remove();
        //     $('#dataTables-mhours-line_previous').remove();
        //     $('#dataTables-mhours-line_next').remove();
        //
        // }
    });

    $('#dataTables-buildingInstitutePro_' + activeTabId + ' tbody').on('click', 'a[name=a_modal_oa]', function () {
        var userTable = $('#dataTables-buildingInstitutePro_' + activeTabId).DataTable();
        var data = userTable.rows( $(this).parents('tr') ).data();
        openOA_workFlow(data[0].requestid);
    });

    tmpDt.on('order.dt search.dt', function () {
        tmpDt.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    }).draw();
}

function getBuildingInstitutePro1() {
    var $tr = $('#t_buildingInstitutePro_' + activeTabId + ' tbody tr.dt-select');
    var id = "";
    var formId = "";
    if (1 == $tr.length) {
        var $td = $($($tr[0]).find('td')[0]);
        var inputs = $td.find('input');
        id = inputs[0].value;
        formId = inputs[1].value;
    }
    var status = "";
    $('input[name="ch_buildingInstitutePro1_' + activeTabId + '"]:checked').each(function () {
        status += "'" + $(this).val() + "'" + ",";
    });
    if (0 < status.length)
        status = status.substr(0, status.length - 1);
    var zy = "";
    $('input[name="phase_buildingInstitutePro_zy_' + activeTabId + '"]:checked').each(function () {
        zy += "'" + $(this).val() + "'" + ",";
    });
    if (0 < zy.length)
        zy = zy.substr(0, status.length - 1);
    var xmbh = $('#div_pro_header_' + activeTabId).text().split("(")[0];

    $.ajax({
        url: config.baseurl + "/api/naviIndexRest/getBuildingInstitutePro",
        contentType: "application/json",
        dataType: "json",
        data: {
            xmbh: xmbh,
            id: id,
            formid: formId,
            status: status,
            zy: zy
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {
            window.parent.showLoading();
        },
        success: function (data) {
            createBuildingInstitutePro1(data);
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

function createBuildingInstitutePro1(data) {
    $('#dataTables-stage_buildingInstitutePro_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTable-stage_buildingInstitutePro_' + activeTabId + '"></table>');

    var tmpDt = $('#dataTable-stage_buildingInstitutePro_' + activeTabId).DataTable({
        "processing": true,
        "autoWidth": false,
        "data": data,
        "pagingType": "full", //简单模式，只有上下页
        "searching": false,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "流程名称", "data": "workflowname"},
            {"title": "创建人", "data": "lastname"},
            {"title": "状态", "data": "status"},
            {"title": "创建时间", "data": "createtime"},
            {"title": "办结时间", "data": "endtime"}
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "width": 32,
            "targets": 0 //排序
        },{
            "targets": 1,
            "render": function (data, type, full, meta) {
                return "<a name='a_modal_oa' href='#1'>" + data + "</a>";
            }
        }
        ],
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "order": [],
        "initComplete": function () {
            $('#dataTable-stage_buildingInstitutePro_' + activeTabId + '_length').insertBefore($('#dataTable-stage_buildingInstitutePro_' + activeTabId + '_info'));
            $('#dataTable-stage_buildingInstitutePro_' + activeTabId + '_length').addClass("col-sm-4");
            $('#dataTable-stage_buildingInstitutePro_' + activeTabId + '_length').css("paddingLeft", "0px");
            $('#dataTable-stage_buildingInstitutePro_' + activeTabId + '_length').css("paddingTop", "5px");
            $('#dataTable-stage_buildingInstitutePro_' + activeTabId + '_length').css("maxWidth", "130px");
            $('#dataTable-stage_buildingInstitutePro_' + activeTabId).css("cssText", "margin-top: 0px !important;");
        }
        // "initComplete": function () { //去掉标准表格多余部分
        //     $('#dataTables-mhours-line_length').remove();
        //     $('#dataTables-mhours-line_info').remove();
        //     $('#dataTables-mhours-line_previous').remove();
        //     $('#dataTables-mhours-line_next').remove();
        //
        // }
    });

    $('#dataTable-stage_buildingInstitutePro_' + activeTabId + ' tbody').on('click', 'a[name=a_modal_oa]', function () {
        var userTable = $('#dataTable-stage_buildingInstitutePro_' + activeTabId).DataTable();
        var data = userTable.rows( $(this).parents('tr') ).data();
        openOA_workFlow(data[0].requestid);
    });

    tmpDt.on('order.dt search.dt', function () {
        tmpDt.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    }).draw();
}

function getBuildingInstitutePro2() {
    var status = "";
    $('input[name="ch_buildingInstitutePro2_' + activeTabId + '"]:checked').each(function () {
        status += "'" + $(this).val() + "'" + ",";
    });
    if (0 < status.length)
        status = status.substr(0, status.length - 1);
    var lx = "";
    $('input[name="ch_buildingInstitutePro2_lx_' + activeTabId + '"]:checked').each(function () {
        lx += "'" + $(this).val() + "'" + ",";
    });
    if (0 < lx.length)
        lx = lx.substr(0, lx.length - 1);
    var xmbh = $('#div_pro_header_' + activeTabId).text().split("(")[0];

    var zy = [];
    var zy_zx = [];
    var tree = $("#table_stage_buildingInstitutePro2_" + activeTabId).fancytree("getTree"),
        selNodes = tree.getSelectedNodes();
    if (0 == selNodes.length) {
        window.parent.warring(valTips_selOne);
        return;
    }
    selNodes.forEach(function (node) {
        var lx_tmp = node.data.typeName;
        if ("专业" == lx_tmp) {
            var twn = "'" + node.data.taskWbsNumber + "'";
            if (0 > zy.indexOf(twn)) {
                zy.push(twn);
                var zxNode = node.parent;
                if (null != zxNode && "子项" == zxNode.data.typeName) {
                    var twn2 = "'" + zxNode.data.taskWbsNumber + "'";
                    if (0 > zy_zx.indexOf(twn2)) {
                        zy_zx.push(twn2);
                    }
                }
            }
        }
    });

    $.ajax({
        url: config.baseurl + "/api/naviIndexRest/getBuildingInstitutePro23",
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        data: {
            xmbh: xmbh,
            status: status,
            zy: zy.toString(),
            types: lx,
            zx: zy_zx.toString()
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {
            window.parent.showLoading();
        },
        success: function (data) {
            createBuildingInstitutePro2(data);
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

function createBuildingInstitutePro2(data) {
    $('#dataTables-stage_buildingInstitutePro2_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTable-buildingInstitutePro2_' + activeTabId + '"></table>');

    var tmpDt = $('#dataTable-buildingInstitutePro2_' + activeTabId).DataTable({
        "processing": true,
        "autoWidth": true,
        "data": data,
        "pagingType": "full", //简单模式，只有上下页
        "searching": false,
        "scrollX": true,
        "scrollCollapse": true,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "专业", "data": "zy"},
            {"title": "流程名称", "data": "workflowname"},
            {"title": "子项号", "data": "zxh"},
            {"title": "子项名称", "data": "zxm"},
            {"title": "状态", "data": "status"},
            {"title": "设计人", "data": "sjr"},
            {"title": "校核人", "data": "jhr"},
            {"title": "审核人", "data": "shr"},
            {"title": "审定人", "data": "sdr"},
            {"title": "注册工程师", "data": "zcs"},
            {"title": "方案设计人", "data": "fasjr"},
            {"title": "条件接收人", "data": "tjjsr"},
            {"title": "创建时间", "data": "createtime"},
            {"title": "办结时间", "data": "endtime"}
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "width": 32,
            "targets": 0 //排序
        },{"width": 80,"targets": 1,"targets": 1},{"width": 100,"targets": 2,"render": function (data, type, full, meta) {
            return "<a name='a_modal_oa' href='#1'>" + data + "</a>";
        }},{"width": 60,"targets": 3},{"width": 100,"targets": 4},{"width": 60,"targets": 5}
            ,{"width": 60,"targets": 6},{"width": 60,"targets": 7},{"width": 60,"targets": 8},{"width": 60,"targets": 9},{"width": 80,"targets": 10}
            ,{"width": 80,"targets": 11},{"width": 80,"targets": 12},{"width": 80,"targets": 13},{"width": 80,"targets": 14}
        ],
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "order": [],
        "initComplete": function () {
            $('#dataTable-buildingInstitutePro2_' + activeTabId + '_length').insertBefore($('#dataTable-buildingInstitutePro2_' + activeTabId + '_info'));
            $('#dataTable-buildingInstitutePro2_' + activeTabId + '_length').addClass("col-sm-4");
            $('#dataTable-buildingInstitutePro2_' + activeTabId + '_length').css("paddingLeft", "0px");
            $('#dataTable-buildingInstitutePro2_' + activeTabId + '_length').css("paddingTop", "5px");
            $('#dataTable-buildingInstitutePro2_' + activeTabId + '_length').css("maxWidth", "130px");
            $('#dataTable-buildingInstitutePro2_' + activeTabId).css("cssText", "margin-top: 0px !important;");
        }
        // "initComplete": function () { //去掉标准表格多余部分
        //     $('#dataTables-mhours-line_length').remove();
        //     $('#dataTables-mhours-line_info').remove();
        //     $('#dataTables-mhours-line_previous').remove();
        //     $('#dataTables-mhours-line_next').remove();
        //
        // }
    });

    $('#dataTable-buildingInstitutePro2_' + activeTabId + ' tbody').on('click', 'a[name=a_modal_oa]', function () {
        var userTable = $('#dataTable-buildingInstitutePro2_' + activeTabId).DataTable();
        var data = userTable.rows( $(this).parents('tr') ).data();
        openOA_workFlow(data[0].requestid);
    });

    tmpDt.on('order.dt search.dt', function () {
        tmpDt.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    }).draw();
}

function getBuildingInstitutePro3() {
    var status = "";
    $('input[name="ch_buildingInstitutePro3_' + activeTabId + '"]:checked').each(function () {
        status += "'" + $(this).val() + "'" + ",";
    });
    if (0 < status.length)
        status = status.substr(0, status.length - 1);
    var xmbh = $('#div_pro_header_' + activeTabId).text().split("(")[0];
    var zy = [];
    var zy_zx = [];
    var tree = $("#table_stage_buildingInstitutePro3_" + activeTabId).fancytree("getTree"),
        selNodes = tree.getSelectedNodes();
    if (0 == selNodes.length) {
        window.parent.warring(valTips_selOne);
        return;
    }
    selNodes.forEach(function (node) {
        var lx_tmp = node.data.typeName;
        if ("专业" == lx_tmp) {
            var twn = "'" + node.data.specialityCode + "'";
            if (0 > zy.indexOf(twn)) {
                zy.push(twn);
                var zxNode = node.parent;
                if (null != zxNode && "子项" == zxNode.data.typeName) {
                    var twn2 = "'" + zxNode.data.specialityCode + "'";
                    if (0 > zy_zx.indexOf(twn2)) {
                        zy_zx.push(twn2);
                    }
                }
            }
        }
    });

    $.ajax({
        url: config.baseurl + "/api/naviIndexRest/getBuildingInstitutePro23",
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        data: {
            xmbh: xmbh,
            status: status,
            types: "05",
            zy: zy.toString(),
            zx: zy_zx.toString()
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {
            window.parent.showLoading();
        },
        success: function (data) {
            createBuildingInstitutePro3(data);
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

function createBuildingInstitutePro3(data) {
    $('#dataTables-stage_buildingInstitutePro3_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTable-buildingInstitutePro3_' + activeTabId + '"></table>');

    var tmpDt = $('#dataTable-buildingInstitutePro3_' + activeTabId).DataTable({
        "processing": true,
        "autoWidth": true,
        "data": data,
        "pagingType": "full", //简单模式，只有上下页
        "searching": false,
        "scrollX": true,
        "scrollCollapse": true,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "专业", "data": "zy"},
            {"title": "流程名称", "data": "workflowname"},
            {"title": "子项号", "data": "zxh"},
            {"title": "子项名称", "data": "zxm"},
            {"title": "状态", "data": "status"},
            {"title": "设计人", "data": "sjr"},
            {"title": "校核人", "data": "jhr"},
            {"title": "审核人", "data": "shr"},
            {"title": "审定人", "data": "sdr"},
            {"title": "条件接收人", "data": "tjjsr"},
            {"title": "创建时间", "data": "createtime"},
            {"title": "办结时间", "data": "endtime"}
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "width": 32,
            "targets": 0 //排序
        },{"width": 80,"targets": 1},{"width": 100,"targets": 2,"render": function (data, type, full, meta) {
            return "<a name='a_modal_oa' href='#1'>" + data + "</a>";
        }},{"width": 60,"targets": 3},{"width": 100,"targets": 4},{"width": 60,"targets": 5}
            ,{"width": 60,"targets": 6},{"width": 60,"targets": 7},{"width": 60,"targets": 8},{"width": 60,"targets": 9},
            {"width": 80,"targets": 10},{"width": 80,"targets": 11},{"width": 80,"targets": 12}
        ],
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "order": [],
        "initComplete": function () {
            $('#dataTable-buildingInstitutePro3_' + activeTabId + '_length').insertBefore($('#dataTable-buildingInstitutePro3_' + activeTabId + '_info'));
            $('#dataTable-buildingInstitutePro3_' + activeTabId + '_length').addClass("col-sm-4");
            $('#dataTable-buildingInstitutePro3_' + activeTabId + '_length').css("paddingLeft", "0px");
            $('#dataTable-buildingInstitutePro3_' + activeTabId + '_length').css("paddingTop", "5px");
            $('#dataTable-buildingInstitutePro3_' + activeTabId + '_length').css("maxWidth", "130px");
            $('#dataTable-buildingInstitutePro3_' + activeTabId).css("cssText", "margin-top: 0px !important;");
        }
        // "initComplete": function () { //去掉标准表格多余部分
        //     $('#dataTables-mhours-line_length').remove();
        //     $('#dataTables-mhours-line_info').remove();
        //     $('#dataTables-mhours-line_previous').remove();
        //     $('#dataTables-mhours-line_next').remove();
        //
        // }
    });

    $('#dataTable-buildingInstitutePro3_' + activeTabId + ' tbody').on('click', 'a[name=a_modal_oa]', function () {
        var userTable = $('#dataTable-buildingInstitutePro3_' + activeTabId).DataTable();
        var data = userTable.rows( $(this).parents('tr') ).data();
        openOA_workFlow(data[0].requestid);
    });

    tmpDt.on('order.dt search.dt', function () {
        tmpDt.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    }).draw();
}