/**
 * Created by Zach on 16/5/23.
 */
var config = new nerinJsConfig();
var headerData;
$(function () {
    loadIndex();
});

function initAddOrUpdateModal() {
    $('#s_id').val(headerData ? headerData.id : "");
    $('#s_nerinIdentity').val(headerData ? headerData.nerinIdentity : "");
    $('#s_codeRemark').val(headerData ? headerData.codeRemark : "");
    if (headerData) {
        dataSet_d = headerData.dList;
        dataSet_y = headerData.yList;
    } else {
        $.ajax({
            url : config.baseurl +"/api/ebsDuty/queryEbsDutySource",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            async: false,
            type:"GET",
            success:function(d){
                dataSet_d = d;
            }
        });
        dataSet_y = [];
    }
    $('#add_modal').modal("show");
}

$('#save').on('click', function () {
    if ("" == $('#s_nerinIdentity').val()) {
        window.parent.warring("请填写标识");
        return;
    }
    var o = new Object();
    o.id = $('#s_id').val();
    o.nerinIdentity = $('#s_nerinIdentity').val();
    o.codeRemark = $('#s_codeRemark').val();
    var arr = [];
    var l;
    var allData = $("#dataTables-data_y  tr:not(:first)");
    $(allData).each(function () {
        l = new Object();
        var jqInputs = $('input', $(this));
        l.dutyCode = $(jqInputs[1]).val();
        arr.push(l);
    });
    o.ebsCodes = arr;

    $.ajax({
        url : config.baseurl +"/api/ebsDuty/saveEbsDuty",
        contentType : 'application/json',
        dataType:"json",
        data: JSON.stringify(o),
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            window.parent.success("保存成功！");
            $('#add_modal').modal("hide");
            loadIndex();
        },
        complete:function(){
            window.parent.closeLoading();
        },
        error:function(){
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
});

$('#bu_add').on("click", function () {
    headerData = null;
    initAddOrUpdateModal();
});

$('#bu_edit').on("click", function () {
    var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
    if (0 == data.length) {
        window.parent.warring(valTips_selOne);
        return;
    } else if (2 <= data.length) {
        window.parent.warring(valTips_workOne);
        return;
    }
    window.parent.showLoading();
    $.getJSON(config.baseurl + "/api/ebsDuty/queryEbsDuty?id=" + data[0].id, function (data) {
        window.parent.closeLoading();
        headerData = data.data;
        headerData.dList = data.dList;
        headerData.yList = data.yList;
        console.log(headerData);
        initAddOrUpdateModal();
    });
});

$('#bu_del').on("click", function () {
    var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
    if (0 == data.length) {
        window.parent.warring(valTips_selOne);
        return;
    }
    var to = [];
    $(data).each(function (index) {
        to.push(data[index].id);
    });
    window.parent.showConfirm(function () {
        $.ajax({
            url: config.baseurl +"/api/ebsDuty/delEbsDuty",
            data: {
                ids: to.toString(),
            },
            contentType : 'application/x-www-form-urlencoded',
            dataType: "json",
            type: "POST",
            beforeSend: function() {
                window.parent.showLoading();
            },
            success: function(data) {
                window.parent.success("删除成功！");
                loadIndex();
            },
            complete: function() {
                window.parent.closeLoading();
            },
            error: function() {
                window.parent.closeLoading();
                window.parent.error(ajaxError_sys);
            }
        });
    }, "确认删除吗？");
});

function loadIndex() {
    window.parent.showLoading();
    $.getJSON(config.baseurl + "/api/ebsDuty/queryAllEbsDuty", function (data) {
        window.parent.closeLoading();
        dataSet = data;
        queryList();
    });
}

function setCheck() {
    var isChecked = $('#ch_listAll').prop("checked");
    $("input[name='ch_list']").prop("checked", isChecked);
}

$('#viewModal').on('shown.bs.modal', function (e) {
    queryList_view();
});

var dataSet_view = [];
var t_view;
function queryList_view() {
    $('#dataTables-viewAll_y').html("");
    $('#dataTables-viewAll_y').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-data_view"></table>');

    t_view = $('#dataTables-data_view').DataTable({
        "searching": true,
        "processing": true,
        "data": dataSet_view,
        "paging": false,
        "autoWidth": false,
        "scrollY": "400px",
        // "scrollCollapse": true,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "职责", "data": "responsibilityName"}
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "targets": 0,
            "width": 32
        },{
            "targets": 1
        }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "initComplete": function () {
            $('#dataTables-data_view_length').insertBefore($('#dataTables-data_view_info'));
            $('#dataTables-data_view_length').addClass("col-sm-4");
            $('#dataTables-data_view_length').css("paddingLeft", "0px");
            $('#dataTables-data_view_length').css("paddingTop", "5px");
            $('#dataTables-data_view_length').css("maxWidth", "130px");
        }
    });

    t_view.on( 'order.dt search.dt', function () {
        t_view.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );
    } ).draw();

}


var dataSet = [];
var t;
function queryList() {
    $('#dataTables-listAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-data"></table>');

    t = $('#dataTables-data').DataTable({
        "searching": true,
        "processing": true,
        "data": dataSet,
        "paging": false,
        "autoWidth": false,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "<input type='checkbox' name='ch_listAll' id='ch_listAll' onclick='javascript:setCheck();' />", "data": null, "className": "style_column"},
            {"title": "标识", "data": "nerinIdentity"},
            {"title": "三期优化功能说明", "data": "codeRemark"},
            {"title": "EBS职责查看", "data": "codeRemark"}
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "targets": 1,
            "width": 70
        },{
            "searchable": false,
            "orderable": false,
            "targets": 0,
            "width": 5
        },{
            "width": 500,
            "targets": 2
        },{
            "width": 100,
            "targets": 3,
            "render": function (data, type, full, meta) {
                return "<i class='fa fa-file-o' style='cursor: pointer; color: blue;'>查看</i>";
            }
        }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "initComplete": function () {
            $('#dataTables-data_length').insertBefore($('#dataTables-data_info'));
            $('#dataTables-data_length').addClass("col-sm-4");
            $('#dataTables-data_length').css("paddingLeft", "0px");
            $('#dataTables-data_length').css("paddingTop", "5px");
            $('#dataTables-data_length').css("maxWidth", "130px");
        }
    });

    $('#dataTables-data tbody').on('click', 'i', function () {
        var userTable = $('#dataTables-data').DataTable();
        var data = userTable.rows( $(this).parents('tr') ).data();
        window.parent.showLoading();
        $.getJSON(config.baseurl + "/api/ebsDuty/queryEbsDuty?id=" + data[0].id, function (data) {
            window.parent.closeLoading();
            dataSet_view = data.yList;
            $('#viewModal').modal("show");
        });
    });

    $('#dataTables-data tbody').on('click', 'tr', function () {
        var $tr = $(this);
        if ( $tr.hasClass('dt-select') ) {
            $tr.removeClass('dt-select');
        }
        else {
            $('#dataTables-data').DataTable().$('tr.dt-select').removeClass('dt-select');
            $tr.addClass('dt-select');
        }
        var tdNo = $(this).parents("tr").find("td").index($(this));
        if (0 != tdNo) {
            var check = $tr.find("input[name='ch_list']");
            if (!check.prop("checked")) {
                $(this).css("backgroundColor", "#C6E2FF");
                check.prop("checked", true);
            } else {
                check.prop("checked", false);
                $(this).css("backgroundColor", "");
            }
        }
    });

    t.on( 'order.dt search.dt', function () {
        t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = "<input type='checkbox' name='ch_list' />";
        } );
    } ).draw();

}

$('#add_modal').on('shown.bs.modal', function (e) {
    queryList_d();
    queryList_y();
});

function setCheck_d() {
    var isChecked = $('#ch_listAll_d').prop("checked");
    $("input[name='ch_list_d']").prop("checked", isChecked);
}

var dataSet_d = [];
var t_d;
function queryList_d() {
    $('#dataTables-listAll_d').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-data_d"></table>');

    t_d = $('#dataTables-data_d').DataTable({
        "searching": true,
        "processing": true,
        "data": dataSet_d,
        "paging": false,
        "autoWidth": false,
        "scrollY": "400px",
        // "scrollCollapse": true,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "<input type='checkbox' name='ch_listAll_d' id='ch_listAll_d' onclick='javascript:setCheck_d();' />", "data": null},
            {"title": "职责", "data": "responsibilityName"}
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "targets": 0,
            "width": 5
        },{
            "targets": 1,
            "render": function (data, type, full, meta) {
                return '<input type="hidden" value="' + full.responsibilityKey + '" name="responsibilityKey"/>' + data;
            }
        }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "initComplete": function () {
            $('#dataTables-data_d_length').insertBefore($('#dataTables-data_d_info'));
            $('#dataTables-data_d_length').addClass("col-sm-4");
            $('#dataTables-data_d_length').css("paddingLeft", "0px");
            $('#dataTables-data_d_length').css("paddingTop", "5px");
            $('#dataTables-data_d_length').css("maxWidth", "130px");
        }
    });

    $('#dataTables-data_d tbody').on('click', 'td', function () {
        var $tr = $(this).parent("tr");
        if ( $tr.hasClass('dt-select') ) {
            $tr.removeClass('dt-select');
        }
        else {
            $('#dataTables-data_d').DataTable().$('tr.dt-select').removeClass('dt-select');
            $tr.addClass('dt-select');
        }
        var tdNo = $(this).parents("tr").find("td").index($(this));
        if (0 != tdNo) {
            var check = $tr.find("input[name='ch_list_d']");
            if (!check.prop("checked")) {
                $(this).parents("tr").css("backgroundColor", "#C6E2FF");
                check.prop("checked", true);
            } else {
                check.prop("checked", false);
                $(this).parents("tr").css("backgroundColor", "");
            }
        }
    });

    t_d.on( 'order.dt search.dt', function () {
        t_d.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = "<input type='checkbox' name='ch_list_d' />";
        } );
    } ).draw();

}

$('#bu_right').on('click', function () {
    var data = t_d.rows( $("input[name='ch_list_d']:checked").parents('tr') ).data();
    if (0 == data.length) {
        window.parent.warring(valTips_selOne);
        return;
    }
    t_d.rows( $("input[name='ch_list_d']:checked").parents('tr')).remove();
    t_d.draw();
    $(data).each(function (i) {
        var o = new Object();
        o.responsibilityKey = data[i].responsibilityKey;
        o.responsibilityName = data[i].responsibilityName;
        t_y.row.add(o).draw();
    });

});

$('#bu_left').on('click', function () {
    var data = t_y.rows( $("input[name='ch_list_y']:checked").parents('tr') ).data();
    if (0 == data.length) {
        window.parent.warring(valTips_selOne);
        return;
    }
    t_y.rows( $("input[name='ch_list_y']:checked").parents('tr')).remove();
    t_y.draw();
    $(data).each(function (i) {
        var o = new Object();
        o.responsibilityKey = data[i].responsibilityKey;
        o.responsibilityName = data[i].responsibilityName;
        t_d.row.add(o).draw();
    });

});

function setCheck_y() {
    var isChecked = $('#ch_listAll_y').prop("checked");
    $("input[name='ch_list_y']").prop("checked", isChecked);
}

var dataSet_y = [];
var t_y;
function queryList_y() {
    $('#dataTables-listAll_y').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-data_y"></table>');

    t_y = $('#dataTables-data_y').DataTable({
        "searching": true,
        "processing": true,
        "data": dataSet_y,
        "paging": false,
        "autoWidth": false,
        "scrollY": "400px",
        // "scrollCollapse": true,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "<input type='checkbox' name='ch_listAll_y' id='ch_listAll_y' onclick='javascript:setCheck_y();' />", "data": null},
            {"title": "职责", "data": "responsibilityName"}
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "targets": 0,
            "width": 5
        },{
            "targets": 1,
            "render": function (data, type, full, meta) {
                return '<input type="hidden" value="' + full.responsibilityKey + '" name="responsibilityKey"/>' + data;
            }
        }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "initComplete": function () {
            $('#dataTables-data_y_length').insertBefore($('#dataTables-data_y_info'));
            $('#dataTables-data_y_length').addClass("col-sm-4");
            $('#dataTables-data_y_length').css("paddingLeft", "0px");
            $('#dataTables-data_y_length').css("paddingTop", "5px");
            $('#dataTables-data_y_length').css("maxWidth", "130px");
        }
    });

    $('#dataTables-data_y tbody').on('click', 'td', function () {
        var $tr = $(this).parent("tr");
        if ( $tr.hasClass('dt-select') ) {
            $tr.removeClass('dt-select');
        }
        else {
            $('#dataTables-data_y').DataTable().$('tr.dt-select').removeClass('dt-select');
            $tr.addClass('dt-select');
        }
        var tdNo = $(this).parents("tr").find("td").index($(this));
        if (0 != tdNo) {
            var check = $tr.find("input[name='ch_list_y']");
            if (!check.prop("checked")) {
                $(this).parents("tr").css("backgroundColor", "#C6E2FF");
                check.prop("checked", true);
            } else {
                check.prop("checked", false);
                $(this).parents("tr").css("backgroundColor", "");
            }
        }
    });

    t_y.on( 'order.dt search.dt', function () {
        t_y.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = "<input type='checkbox' name='ch_list_y' />";
        } );
    } ).draw();

}


