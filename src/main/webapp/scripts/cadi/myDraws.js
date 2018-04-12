$(document).keyup(function(event){
    switch(event.keyCode) {
        case 13:
            $('#bu_queryData').trigger("click");
    }
});

function setCheckAll() {
    var isChecked = $('#ch_listAll').prop("checked");
    $("input[name='ch_list']").prop("checked", isChecked);
    $("input[name='ch_list']").each(function () {
        var $tr = $(this).parent().parent();
        if (isChecked){
            $tr.addClass('dt-select');
        } else {
            $tr.removeClass('dt-select');
        }
    });
}

function initProjectMenu() {
    $.getJSON(config.baseurl + '/api/draw/myProjectsList', function (data) {
        $("#projectNum").html("");
        $("#projectNum").append($('<option value="">请选择</option>'));
        $(data).each(function (index) {
            $("#projectNum").append($('<option value="' + data[index].projectId + '">' + data[index].projectNumber + '(' + data[index].projectName + ')</option>'));
        });
        $("#projectNum").val("");
        $("#projectNum").selectpicker('refresh');
    });
}

function initPhaseMenu() {
    $.getJSON(config.baseurl + '/api/draw/projectPhase?projectId=' + $("#projectNum").val(), function (data) {
        var list = data;
        $('#designPhase').empty();
        $('#designPhase').append('<option value="">请选择</option>');
        list.forEach(function (data) {
            $('#designPhase').append('<option value="' + data.phaseCode + '">' + data.phaseName + '(' + data.phaseCode + ')</option>');
        });
        // $("#designPhase").selectpicker('refresh');
    });
}

function initSpecialityMenu() {
    $.getJSON(config.baseurl + '/api/draw/speciality?projectId=' + $('#projectNum').val() + '&phaseId=' + $('#designPhase').val(), function (data) {
        var spcialityList = data;
        $("#specialty").empty();
        $('#specialty').append('<option value="">请选择</option>');
        spcialityList.forEach(function (data) {
            $('#specialty').append('<option value="' + data.specialityCode + '">' + data.specialityName + '(' + data.specialityCode + ')</option>');
        });
        // $('#specialty').selectpicker('refresh');
    });
}

function loadLogonUser() {
    $.ajax({
        url: config.baseurl +"/api/logonUser",
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function() {},
        success: function(data) {
            logonUser = data;
            // $('#pltRequestorName').val(logonUser.fullName);
            // $('#pltDepartment').val(logonUser.deptOrgName);
        },
        complete: function() {window.parent.closeLoading();},
        error: function() {window.parent.closeLoading();}
    });
}

var config = new nerinJsConfig();
var dataSet = [];
var logonUser;
$(function () {
    loadLogonUser();
    initProjectMenu();
    $('#projectNum').on('change', function () {
        initPhaseMenu();
    });
    $('#designPhase').on('change', function () {
        initSpecialityMenu();
    });
    // $.getJSON(config.baseurl + "/api/templateRest/specialtyList", function (data) {
    //     $('#specialty').append("<option value=''>请选择</option>");
    //     var specialtys = data.dataSource;
    //     for (var i = 0; i < specialtys.length; i++) {
    //         $('#specialty').append("<option value='" + specialtys[i].specialty + "'>" + specialtys[i].specialtyName + "</option>")
    //     }
    // });
    // $.getJSON(config.baseurl + "/api/taskTypeSetRest/projectPhaseCategor", function (data) {
    //     var projectPhase = data.dataSource;
    //     $('#designPhase').append("<option value=''>请选择</option>");
    //     for (var i = 0; i < projectPhase.length; i++) {
    //         $('#designPhase').append("<option value='" + projectPhase[i].phaseCategorCode + "'>" + projectPhase[i].phaseCategorName + "</option>")
    //     }
    // });
    $.getJSON(config.baseurl + "/api/draw/getPrintStatusList", function (data) {
        $('#pltStatus').append("<option value=''>请选择</option>");
        for (var i = 0; i < data.length; i++) {
            var s = data[i].split(",");
            $('#pltStatus').append("<option value='" + s[0] + "'>" + s[1] + "</option>")
        }
    });
    // $.getJSON(config.baseurl + "/api/draw/getDrawingPrintTypeList", function (data) {
    //     $('#pltCategory').append("<option value=''>请选择</option>");
    //     for (var i = 0; i < data.length; i++) {
    //         var s = data[i].split(",");
    //         $('#pltCategory').append("<option value='" + s[0] + "'>" + s[1] + "</option>")
    //     }
    // });
    $('#bu_queryData').focus();
    queryList();
    $('#datetimepicker').datetimepicker({
        format: 'YYYY-MM-DD'
    });
    $('#datetimepicker2').datetimepicker({
        format: 'YYYY-MM-DD'
    });
});

$('#bu_resetQuery').on('click', function () {
    $('#projectNum').selectpicker('destroy');
    $("#projectNum").val("");
    $("#projectNum").selectpicker('refresh');
});

$('#bu_queryData').click(function(){
    $.ajax({
        url : config.baseurl +"/api/draw/searchPltOrders",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: $('#selectFrom').serialize(),
        type:"GET",   //请求方式
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            dataSet = data;
            dataSet.forEach(function (d) {
                if ("PLT_NEW_PRINT" == d.pltCategory && (("BLUE_YD" == d.paperType || "BLUE_WD" == d.paperType) && "SIGNET_APPROVED" == d.pltStatus)) {
                    d.addAgain = 1;
                } else if ("PLT_NEW_PRINT" == d.pltCategory && "COMPLETED" == d.pltStatus) {
                    d.addAgain = 1;
                } else if ("DOC_NEW_PRINT" == d.pltCategory && ("SIGNET_APPROVED" == d.pltStatus || "COMPLETED" == d.pltStatus)) {
                    d.addAgain = 1;
                } else {
                    d.addAgain = 0;
                }
            });
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

var now_pageNo = 0;
var t;
function queryList() {
    $('#dataTables-listAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-data"></table>');

    t = $('#dataTables-data').DataTable({
        "pageLength": 10,
        // "lengthMenu": [10, 50, 100],
        "searching": false,
        "processing": true,
        "data": dataSet,
        "scrollX": true,
        "scrollY": "500px",
        "scrollCollapse": true,
        "pagingType": "full_numbers",
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "<input type='checkbox' name='ch_listAll' id='ch_listAll' onclick='javascript:setCheckAll();' />", "data": null},
            {"title": "序号", "data": null},
            {"title": "项目编号", "data": "projectNum"},
            {"title": "项目名称", "data": "projectName"},
            {"title": "设备名称", "data": "equipmentName"},
            {"title": "阶段名称", "data": "phaseName"},
            {"title": "子项号", "data": "taskCode"},
            {"title": "子项名称", "data": "taskName"},
            {"title": "专业", "data": "mainSpeciality"},
            {"title": "文印单号", "data": "pltNum"},
            {"title": "文印内容", "data": "pltContent"},
            {"title": "文印类型", "data": "pltCategoryName"},
            {"title": "文印状态", "data": "pltsStatusName"},
            {"title": "文印人", "data": "docPrintPersonName"},
            {"title": "文印日期", "data": "docPrintDate"},
            {"title": "是否归档", "data": "archiveFlag"},
            {"title": "添晒/加印", "data": null},
            {"title": "操作", "data": null},
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "targets": 1,
            "width": 32
        },{
            "searchable": false,
            "orderable": false,
            "targets": 0,
            "width": 5,
            "render": function (data, type, full, meta) {
                if (0 == full.addAgain)
                    return '';
                else
                    return '<input type="checkbox" name="ch_list" />';

            }
        },{
            "width": 100,
            "targets": 2
        },{
            "targets": 5,
            "width": 100
        },{
            "targets": 4,
            "width": 100
        },{
            "targets": 3,
            "width": 180
        },{
            "targets": 6,
            "width": 60
        },{
            "targets": 7,
            "width": 80
        },{
            "targets": 8,
            "width": 80
        },{
            "targets": 9,
            "width": 100,
            "render": function (data, type, full, meta) {
                return "<a href='#1' name='a_linkTask'>" + data + "</a>";
            }
        },{
            "targets": 10,
            "width": 200
        },{
            "targets": 11,
            "width": 80
        },{
            "targets": 12,
            "width": 70
        },{
            "targets": 13,
            "width": 50
        },{
            "targets": 14,
            "width": 70
        },{
            "targets": 15,
            "width": 60
        },{
            "targets": 16,
            "width": 70,
            "render": function (data, type, full, meta) {
                if (0 == full.addAgain)
                    return "";
                else
                    return "发起流程";
            }
        },{
            "targets": 17,
            "width": 40,
            "render": function (data, type, full, meta) {
                if ("EDIT" == full.pltStatus)
                    return "<i class='fa fa-trash-o' style='cursor: pointer; color: green;'>删除</i>";
                else
                    return "";
            }
        }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            $(row).children('td').eq(1).attr('style', 'text-align: center;');
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

    $('#dataTables-data tbody').on('click', 'td', function () {
        var $tr = $(this).parent("tr");
        if ( $tr.hasClass('dt-select') ) {
            $tr.removeClass('dt-select');
            var check = $tr.find("input[name='ch_list']");
            check.prop("checked", false);
        } else {
            $tr.addClass('dt-select');
            var check = $tr.find("input[name='ch_list']");
            check.prop("checked", true);
        }
    });

    $('#dataTables-data tbody').on('click', 'a[name=a_linkTask]', function () {
        var userTable = $('#dataTables-data').DataTable();
        var data = userTable.rows( $(this).parents('tr') ).data();
        initTz_config(data[0].pltOrderHeaderId);
    });

    $('#dataTables-data tbody').on('click', 'i', function () {
        var userTable = $('#dataTables-data').DataTable();
        var data = userTable.rows( $(this).parents('tr') ).data();
        window.parent.showConfirm(function () {
            $.ajax({
                url: config.baseurl + "/api/draw/deletePltOrders",
                contentType: 'application/x-www-form-urlencoded',
                dataType: "json",   //返回格式为json
                data: {
                    id: data[0].pltOrderHeaderId
                },
                type: "POST",   //请求方式
                beforeSend: function () {
                    window.parent.showLoading();
                },
                success: function (data) {
                    if ("0000" == data.returnCode) {
                        now_pageNo = t.page();
                        window.parent.success("删除成功");
                        $('#bu_queryData').trigger("click");
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
        }, "确认删除吗？");
    });

    t.on( 'order.dt search.dt', function () {
        t.column(1, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );
        // t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
        //     cell.innerHTML = "<input type='checkbox' name='ch_list' />";
        // } );
    } ).draw();

}

$('#bu_add').on('click', function () {
    var data = t.rows($("input[name='ch_list']:checked").parents('tr')).data();
    if (0 == data.length) {
        window.parent.warring(valTips_selOne);
        return;
    }
    var to = [];
    var speciality = "";
    var specialityNo = 0;
    var addMsg = "";
    $(data).each(function (i) {
        if (0 == i) {
            speciality = data[i].mainSpeciality;
        } else {
            if (speciality != data[i].mainSpeciality) {
                specialityNo++;
            }
        }
        if (0 == data[i].addAgain) {
            addMsg += "文印单号[" + data[i].pltNum + "]不能发起添嗮/加印<br>";
        }
        to.push(data[i].pltOrderHeaderId);
    });
    if (0 < specialityNo) {
        window.parent.warring("请勾选同一专业的出图单");
        return;
    }
    if ("" != addMsg) {
        window.parent.warring(addMsg);
        return;
    }
    $.ajax({
        url: config.baseurl + "/api/draw/generatePltOrderAppend",
        contentType: 'application/x-www-form-urlencoded',
        dataType: "json",   //返回格式为json
        data: {
            ids: to.toString()
        },
        type: "POST",   //请求方式
        beforeSend: function () {
            window.parent.showLoading();
        },
        success: function (data) {
            initTz_config(data);
        },
        complete: function () {
            window.parent.closeLoading();
        },
        error: function () {
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
});