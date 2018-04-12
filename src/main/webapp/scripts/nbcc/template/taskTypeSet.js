/**
 * Created by Zach on 16/5/23.
 */
function setCheck() {
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

function setCheckAll() {
    var isChecked = $('#r_ch_listAll').prop("checked");
    $("input[name='r_ch_list']").prop("checked", isChecked);
}

function setCheckAll2() {
    var isChecked = $('#r2_ch_listAll').prop("checked");
    $("input[name='r2_ch_list']").prop("checked", isChecked);
}

function setCheckAll3() {
    var isChecked = $('#bag_ch_listAll').prop("checked");
    $("input[name='bag_ch']").prop("checked", isChecked);
}

$(document).keyup(function(event){
    switch(event.keyCode) {
        case 13:
            $('#bu_queryData').trigger("click");
    }
});

var config = new nerinJsConfig();

var work;

$(function () {
    $('#datetimepicker1').datetimepicker({
        format: 'YYYY-MM-DD'
    });
    $('#datetimepicker2').datetimepicker({
        format: 'YYYY-MM-DD'
    });

    $('#form_taskType').bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            lookupCode: {
                validators: {
                    notEmpty: {
                        message: '文本类型代码不能为空'
                    }
                }
            },
            meaning: {
                validators: {
                    notEmpty: {
                        message: '文本类型名称不能为空'
                    }
                }
            }
        }
    });

    var dataSet = [];
    queryList();

    var t;
    function queryList() {
        $('#dataTables-listAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-data"></table>');

        t = $('#dataTables-data').DataTable({
            "searching": false,
            "processing": true,
            "data": dataSet,
            "pagingType": "full_numbers",
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "<input type='checkbox' name='ch_listAll' id='ch_listAll' onclick='javascript:setCheck();' />", "data": null},
                {"title": "序号", "data": null},
                {"title": "文本类型代码", "data": "lookupCode"},
                {"title": "文本类型名称", "data": "meaning"},
                {"title": "文本类型标识", "data": "tag"},
                {"title": "是否有效", "data": "enabledFlag"},
                {"title": "有效时间从", "data": "startDateActive"},
                {"title": "有效时间至", "data": "endDateActive"}
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
                "width": 5
            },{
                "width": 120,
                "targets": 2
            },{
                "width": 360,
                "targets": 3
            },{
                "targets": 5,
                "width": 100,
                "render": function (data, type, full, meta) {
                    if ("1" == data)
                        return "有效";
                    else
                        return "失效";
                }
            },{
                "targets": 4,
                "width": 120
            }
            ],
            "order": [[2, 'asc']],
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

        $('#dataTables-data tbody').on('dblclick', 'tr', function () {
            var userTable = $('#dataTables-data').DataTable();
            var data = userTable.rows( $(this)).data();
            loadTaskType(data[0].lookupCode);
            work = 1;
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
            // if ( $tr.hasClass('dt-select') ) {
            //     $tr.removeClass('dt-select');
            // }
            // else {
            //     $('#dataTables-data').DataTable().$('tr.dt-select').removeClass('dt-select');
            //     $tr.addClass('dt-select');
            // }
            // var tdNo = $(this).parents("tr").find("td").index($(this));
            // if (0 != tdNo) {
            //     var check = $tr.find("input[name='ch_list']");
            //     if (!check.prop("checked")) {
            //         $(this).parents("tr").css("backgroundColor", "#FFFF00");
            //         check.prop("checked", true);
            //     } else {
            //         check.prop("checked", false);
            //         $(this).parents("tr").css("backgroundColor", "");
            //     }
            // }
        });

        t.on( 'order.dt search.dt', function () {
            t.column(1, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } );
            t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = "<input type='checkbox' name='ch_list' />";
            } );
        } ).draw();

    }

    $('#bu_queryData').click(function(){
        $.ajax({
            url : config.baseurl +"/api/taskTypeSetRest/taskTypeList",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                taskTypeDesc: $('#taskTypeDesc').val()
            },
            type:"GET",   //请求方式
            beforeSend:function(){
                window.parent.showLoading();
            },
            success:function(data){
                dataSet = data.dataSource;
                queryList();
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

    $('#bu_add').on('click', function () {
        work = 0;
        loadTaskTypeData = null;
        loadTaskTypeDataAssignments = [];
        loadTaskTypeDataAssignments2 = [];
        delRoles = [];
        initAddOrUpdateModal();
    });

    $('#bu_del').on('click', function () {
        var tmp = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == tmp.length) {
            window.parent.warring(valTips_selOne);
            return;
        }
        window.parent.showConfirm(delTaskType, "确认删除勾选的文本类型吗？");

    });

    $('#bu_edit').on('click', function () {
        work = 1;
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        } else if (2 <= data.length) {
            window.parent.warring(valTips_workOne);
            return;
        }
        loadTaskType(data[0].lookupCode);
    });

    var loadTaskTypeData;
    var loadTaskTypeDataAssignments;
    var loadTaskTypeDataAssignments2;
    var loadTaskTypeDataBag;
    var delRoles = [];
    var loadNo = 0;
    function loadTaskType(val) {
        loadTaskTypeDataAssignments = [];
        loadTaskTypeDataAssignments2 = [];
        loadTaskTypeDataBag = [];
        delRoles = [];
        loadNo = 0;
        $.ajax({
            url : config.baseurl +"/api/taskTypeSetRest/taskTypeList",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                taskTypeCode: val
            },
            type:"GET",   //请求方式
            beforeSend:function(){
                window.parent.showLoading();
            },
            success:function(data){
                loadNo++;
                loadTaskTypeData = data.dataSource[0];
                loadTaskTypeAssignments(val, "create");
                loadTaskTypeAssignments(val, "submit");
                loadTaskTypeBag(val);
                loadComplete();
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

    function loadTaskTypeAssignments(val, work1) {
        $.ajax({
            url : config.baseurl +"/api/taskTypeSetRest/taskTypeAssignments",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                taskTypeCode: val,
                type: work1
            },
            type:"GET",   //请求方式
            beforeSend:function(){
                //请求前的处理
            },
            success:function(data){
                loadNo++;
                if ("create" == work1)
                    loadTaskTypeDataAssignments = data.dataSource;
                else
                    loadTaskTypeDataAssignments2 = data.dataSource;
                loadComplete();
            },
            complete:function(){
                //请求完成的处理
            },
            error:function(){
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    function loadTaskTypeBag(val) {
        $.ajax({
            url : config.baseurl +"/api/taskTypeSetRest/taskTypeElements",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                taskTypeCode: val
            },
            type:"GET",   //请求方式
            beforeSend:function(){
                //请求前的处理
            },
            success:function(data){
                loadNo++;
                loadTaskTypeDataBag = data.dataSource;
                loadComplete();
            },
            complete:function(){
                //请求完成的处理
            },
            error:function(){
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    function loadComplete() {
        if (4 == loadNo)
            initAddOrUpdateModal();
    }

    function initAddOrUpdateModal() {
       // $('#form_taskType')[0].reset();
        $('#lookupCode').val(loadTaskTypeData ? loadTaskTypeData.lookupCode : "");
        $('#meaning').val(loadTaskTypeData ? loadTaskTypeData.meaning : "");
        $('#startDateActive').val(loadTaskTypeData ? loadTaskTypeData.startDateActive : "");
        $('#endDateActive').val(loadTaskTypeData ? loadTaskTypeData.endDateActive : "");
        $('#description').val(loadTaskTypeData ? loadTaskTypeData.description : "");

        var tag = loadTaskTypeData ? loadTaskTypeData.tag : "A";
        var tagNo = 0;
        if ("A" == tag) {
            tagNo = 0;
        } else if ("B" == tag) {
            tagNo = 1;
        } else {
            tagNo = 2;
        }
        $("label[name='l_tag']").each(function (i) {
            if (i == tagNo && !$(this).hasClass('active'))
                $(this).trigger("click");
        });

        var attribute8 = loadTaskTypeData ? loadTaskTypeData.attribute8 : "1";
        var attribute8No = 0;
        if ("1" == attribute8)
            attribute8No = 0;
        else
            attribute8No = 1;
        $("label[name='l_attribute8']").each(function (i) {
            if (i == attribute8No && !$(this).hasClass('active'))
                $(this).trigger("click");
        });

        var attribute9 = loadTaskTypeData ? loadTaskTypeData.attribute9 : 1;
        var attribute9No = 0;
        if (1 == attribute9)
            attribute9No = 0;
        else
            attribute9No = 1;
        $("label[name='l_attribute9']").each(function (i) {
            if (i == attribute9No && !$(this).hasClass('active'))
                $(this).trigger("click");
        });

        $('#attribute1').val(loadTaskTypeData ? loadTaskTypeData.attribute1 : "");
        $('#attribute2').val(loadTaskTypeData ? loadTaskTypeData.attribute2 : "");
        $('#attribute3').val(loadTaskTypeData ? loadTaskTypeData.attribute3 : "");
        $('#attribute4').val(loadTaskTypeData ? loadTaskTypeData.attribute4 : "");
        $('#attribute5').val(loadTaskTypeData ? loadTaskTypeData.attribute5 : "");
        $('#attribute6').val(loadTaskTypeData ? loadTaskTypeData.attribute6 : "");
        $('#attribute7').val(loadTaskTypeData ? loadTaskTypeData.attribute7 : "");
       //  $('#form_taskType').formFieldValues({
       //      lookupCode: (loadTaskTypeData ? loadTaskTypeData.lookupCode : ""),
       //      meaning: loadTaskTypeData ? loadTaskTypeData.meaning : "",
       //      attribute1: loadTaskTypeData ? loadTaskTypeData.attribute1 : "",
       //      attribute2: loadTaskTypeData ? loadTaskTypeData.attribute2 : "",
       //      attribute3: loadTaskTypeData ? loadTaskTypeData.attribute3 : "",
       //      attribute4: loadTaskTypeData ? loadTaskTypeData.attribute4 : "",
       //      attribute5: loadTaskTypeData ? loadTaskTypeData.attribute5 : "",
       //      attribute6: loadTaskTypeData ? loadTaskTypeData.attribute6 : "",
       //      attribute7: loadTaskTypeData ? loadTaskTypeData.attribute7 : ""
       //  });

        $("#t_roleCreate tbody").empty("");
        $(loadTaskTypeDataAssignments).each(function () {
            addTableRow("t_roleCreate", $(this).get(0).projectRoleId, $(this).get(0).specialty, "r_ch_list", "sel_proRole", "sel_specialty", "in_role", $(this).get(0).assignmentId);
        });

        $("#t_roleSubmit tbody").empty("");
        $(loadTaskTypeDataAssignments2).each(function () {
            addTableRow("t_roleSubmit", $(this).get(0).projectRoleId, $(this).get(0).specialty, "r2_ch_list", "sel_proRole2", "sel_specialty2", "in_role", $(this).get(0).assignmentId);
        });

        $("#t_bag tbody").empty("");
        $(loadTaskTypeDataBag).each(function () {
            console.log($(this).get(0));
            addTableRow_bag($(this).get(0).assignmentId, $(this).get(0).elementTypeId);
        });

        $(".selectpicker").selectpicker('refresh');
        $(".selectpicker").selectpicker('show');
        $(".selectpicker1").selectpicker('refresh');
        $(".selectpicker1").selectpicker('show');

        if ($("#add_modal").is(":hidden"))
            $('#add_modal').on('shown.bs.modal', function (e) {}).modal('toggle');
    }

    var delTaskType = function () {
        var tmp = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == tmp.length) {
            window.parent.warring(valTips_selOne);
            return;
        }
        var taskTypeCodes = [];
        $(tmp).each(function (index) {
            taskTypeCodes.push(tmp[index].lookupCode);
        });
        $.ajax({
            url: config.baseurl +"/api/taskTypeSetRest/delTaskType",
            data: {
                taskTypeCodes: taskTypeCodes.toString()
            },
            contentType : 'application/x-www-form-urlencoded',
            dataType: "json",
            type: "POST",
            beforeSend: function() {
                window.parent.showLoading();
            },
            success: function(data) {
                if ("0000" == data.returnCode) {
                    window.parent.success(tips_delSuccess);
                    $('#bu_queryData').trigger("click");
                } else {
                    window.parent.error(data.returnMsg);
                }
            },
            complete: function() {
                window.parent.closeLoading();
            },
            error: function() {
                window.parent.closeLoading();
                window.parent.error(ajaxError_sys);
            }
        });
    }


    var updateStatus;
    var statusTips;
    $('#bu_effective').on('click', function () {
        var tmp = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == tmp.length) {
            window.parent.warring(valTips_selOne);
            return;
        }
        updateStatus = 1;
        statusTips = "启用";
        window.parent.showConfirm(updateStatusFun, "确认启用勾选的文本类型吗？");
    });

    $('#bu_invalid').on('click', function () {
        var tmp = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == tmp.length) {
            window.parent.warring(valTips_selOne);
            return;
        }
        updateStatus = 0;
        statusTips = "失效";
        window.parent.showConfirm(updateStatusFun, "确认失效勾选的文本类型吗？");
    });
    
    var updateStatusFun = function() {
        now_pageNo = t.page();
        var tmpTips = statusTips;
        var tmpStatus = updateStatus;
        var tmp = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == tmp.length) {
            window.parent.warring(valTips_selOne);
            return;
        }
        window.parent.showLoading();
        $(tmp).each(function (index) {
            $.ajax({
                url: config.baseurl +"/api/taskTypeSetRest/upDateTaskTypeStatus",
                data: {
                    taskTypeCode: tmp[index].lookupCode,
                    taskTypeStatus: tmpStatus
                },
                contentType : 'application/x-www-form-urlencoded',
                dataType: "json",
                type: "POST",
                beforeSend: function() {
                },
                success: function(data) {
                    if ("0000" == data.returnCode) {
                        window.parent.success(tmpTips + "成功!");
                        if (index == tmp.length -1)
                            $('#bu_queryData').trigger("click");
                    } else {
                        window.parent.error(data.returnMsg);
                    }
                },
                complete: function() {
                },
                error: function() {
                    window.parent.closeLoading();
                    window.parent.error(ajaxError_sys);
                }
            });
        });
        window.parent.closeLoading();
        
    }

    var projectRoles = [];
    $.getJSON(config.baseurl + "/api/templateRest/projectRoleList", function (data) {
        projectRoles = data.dataSource;
    });
    var specialtys = [];
    $.getJSON(config.baseurl + "/api/templateRest/specialtyList", function (data) {
        specialtys = data.dataSource;
    });

    var bagTypeSel_data = [];
    $.getJSON(config.baseurl + "/api/lev3/getDlvrTypeList", function (data) {
        bagTypeSel_data = data.rows;
    });

    $('#bu_bag').click(function () {
        selectFlag = true;
        addTableRow_bag("", "");
        selectFlag = false;
    });

    $('#bu_delBag').click(function () {
        delTableRow_bag();
    });

    // 工作包类型
    var bagTypeSel;
    var createbagTypeSel = function (val, name) {
        if (undefined == bagTypeSel || null == bagTypeSel) {
            var bagTypeSel = $("<select name='" + name + "' class='selectpicker1 form-control input-sm' placeholder='请选择' style='padding-left: 2px;' data-live-search='true' data-size='8'></select>");
            bagTypeSel.append("<option value=''>请选择</option>");
            for (var i = 0; i < bagTypeSel_data.length; i++) {
                bagTypeSel.append("<option value='" + bagTypeSel_data[i].dlvrID + "' data-workCode='" + bagTypeSel_data[i].dlvrCode + "'>" + bagTypeSel_data[i].dlvrName + "</option>")
            }
            bagTypeSel.val(val);
            return bagTypeSel;
        } else {
            var select = bagTypeSel.clone(true);
            select.val(val);
            return select;
        }
    }

    function addTableRow_bag(eid, val) {
        var table = $('#t_bag');
        var row = $("<tr></tr>");
        var td = $("<td style='text-align: center;'></td>");
        var td2 = $("<td></td>");
        td.append("<input type='checkbox' name='bag_ch' />" + "<input type='hidden' name='in_bag' value='" + eid  + "' />");
        var proRole = createbagTypeSel(val, "sel_bag");
        td2.append(proRole);
        row.append(td).append(td2);
        table.append(row);
        if (selectFlag) {
            $(proRole).selectpicker('refresh');
            $(proRole).selectpicker('show');
        }
    }

    var delElements = [];
    function delTableRow_bag() {
        var checked = $("input[name='bag_ch']:checked")
        $(checked).each(function () {
            var nRow = $(this).parent().parent()[0];
            var jqInputs = $('input', nRow);
            if ($(jqInputs[1]).val())
                delElements.push($(jqInputs[1]).val());
            $(nRow).remove();
        });
    }

    $.getJSON(config.baseurl + "/api/taskTypeSetRest/projectPhaseCategor", function (data) {
        var list = data.dataSource;
        var initValue = "";
        var initText = "请选择";
        $('#attribute1').append("<option value='" + initValue + "'>" + initText + "</option>");
        $('#attribute2').append("<option value='" + initValue + "'>" + initText + "</option>");
        $('#attribute3').append("<option value='" + initValue + "'>" + initText + "</option>");
        $('#attribute4').append("<option value='" + initValue + "'>" + initText + "</option>");
        $('#attribute5').append("<option value='" + initValue + "'>" + initText + "</option>");
        $('#attribute6').append("<option value='" + initValue + "'>" + initText + "</option>");
        $('#attribute7').append("<option value='" + initValue + "'>" + initText + "</option>");
        list.forEach(function (data) {
            $('#attribute1').append("<option value='" + data.phaseCategorCode + "'>" + data.phaseCategorName + "</option>");
            $('#attribute2').append("<option value='" + data.phaseCategorCode + "'>" + data.phaseCategorName + "</option>");
            $('#attribute3').append("<option value='" + data.phaseCategorCode + "'>" + data.phaseCategorName + "</option>");
            $('#attribute4').append("<option value='" + data.phaseCategorCode + "'>" + data.phaseCategorName + "</option>");
            $('#attribute5').append("<option value='" + data.phaseCategorCode + "'>" + data.phaseCategorName + "</option>");
            $('#attribute6').append("<option value='" + data.phaseCategorCode + "'>" + data.phaseCategorName + "</option>");
            $('#attribute7').append("<option value='" + data.phaseCategorCode + "'>" + data.phaseCategorName + "</option>");
        });
    });

    // 初始化项目角色下拉
    var createProRoleSel = function (name, val) {
        var select = $("<select name='" + name + "' class='selectpicker form-control input-sm' placeholder='请选择' data-live-search='true' data-size='13'></select>");
        select.append("<option value=''>请选择</option>");
        for (var i = 0; i < projectRoles.length; i++) {
            select.append("<option value='" + projectRoles[i].projectRoleId + "'>" + projectRoles[i].projectRoleName + "</option>")
        }
        select.val(val);
        return select;
    };

    // 初始化分配专业下拉
    var createSpecialt = function (name, val) {
        var select = $("<select name='" + name + "' class='selectpicker form-control input-sm' placeholder='请选择' data-live-search='true' data-size='13'></select>");
        select.append("<option value=''>请选择</option>");
        for (var i = 0; i < specialtys.length; i++) {
            select.append("<option value='" + specialtys[i].specialty + "'>" + specialtys[i].specialtyName + "</option>")
        }
        select.val(val);
        return select;
    }

    var selectFlag = false;

    $('#bu_addCreate').click(function () {
        selectFlag = true;
        addTableRow("t_roleCreate", "", "", "r_ch_list", "sel_proRole", "sel_specialty", "in_role", "");
        selectFlag = false;
    });

    $('#bu_delCreate').click(function () {
        delTableRow("r_ch_list");
    });

    $('#bu_addSubmit').click(function () {
        selectFlag = true;
        addTableRow("t_roleSubmit", "", "", "r2_ch_list", "sel_proRole2", "sel_specialty2", "in_role2", "");
        selectFlag = false;
    });

    $('#bu_delSubmit').click(function () {
        delTableRow("r2_ch_list");
    });

    function addTableRow(tid, p, s, cname, sname, sname2, rname, rid) {
        var table = $('#' + tid);
        var row = $("<tr></tr>");
        var td = $("<td style='text-align: center;'></td>");
        var td2 = $("<td width='50%'></td>");
        var td3 = $("<td></td>");
        td.append("<input type='checkbox' name='" + cname + "' />" + "<input type='hidden' name='" + rname + "' value='" + rid  + "' />");
        var proRole = createProRoleSel(sname, p);
        td2.append(proRole);
        var specialt = createSpecialt(sname2, s);
        td3.append(specialt);
        row.append(td).append(td2).append(td3);
        table.append(row);
        if (selectFlag) {
            $(proRole).selectpicker('refresh');
            $(proRole).selectpicker('show');
            $(specialt).selectpicker('refresh');
            $(specialt).selectpicker('show');
        }

    }

    function delTableRow(name) {
        var checked = $("input[name='" + name + "']:checked")
        $(checked).each(function () {
            var nRow = $(this).parent().parent()[0];
            var jqInputs = $('input', nRow);
            if ($(jqInputs[1]).val())
                delRoles.push($(jqInputs[1]).val());
            $(nRow).remove();
        });
    }

    var now_pageNo = 0;
    $('#save').on('click', function () {
        $('#form_taskType').data('bootstrapValidator').validate();
        if (!$('#form_taskType').data('bootstrapValidator').isValid())
            return;
        if (!submitValdation())
            return;
        valTask();
    });

    function valTask() {
        $.ajax({
            url: config.baseurl +"/api/taskTypeSetRest/valTask",
            contentType: "application/json",
            dataType: "json",
            data: {
                taskTypeCode: $('#lookupCode').val(),
                taskTypeName: $('#meaning').val(),
                work: work
            },
            type: "GET",
            beforeSend: function() {
                window.parent.showLoading();
            },
            success: function(d) {
                if (d) {
                    saveTaskType();
                } else {
                    window.parent.closeLoading();
                    window.parent.error("代码或名称已存在，请修改！");
                }
            },
            complete: function() {

            },
            error: function() {
                window.parent.closeLoading();
                window.parent.error(ajaxError_sys);
            }
        });
    }

    function submitValdation() {
        if (0 == $('select[name="sel_proRole"]').length || "" == $('select[name="sel_proRole"]')[0].value) {
            window.parent.warring("请至少选择一个创建角色");
            return false;
        }
        if ("B" == $('input[name="tag"]:checked').val() && (0 == $('select[name="sel_proRole2"]').length || "" == $('select[name="sel_proRole2"]')[0].value)) {
            window.parent.warring("高阶段请至少选择一个提交角色");
            return false;
        }
        return true;
    }

    function saveTaskType() {
        var o;
        var createList = [];
        $("#t_roleCreate  tr:not(:first)").each(function () {
            var jqInputs = $('input', $(this));
            var jqSels = $('select', $(this));
            o = new Object();
            o.assignmentId = $(jqInputs[1]).val();
            o.taskTypeCode = $('#lookupCode').val();
            o.assignmentType = "create";
            o.projectRoleId = $(jqSels[0]).val();
            o.specialty = $(jqSels[1]).val();
            createList.push(o);
        });

        $("#t_roleSubmit  tr:not(:first)").each(function () {
            var jqInputs = $('input', $(this));
            var jqSels = $('select', $(this));
            o = new Object();
            o.assignmentId = $(jqInputs[1]).val();
            o.taskTypeCode = $('#lookupCode').val();
            o.assignmentType = "submit";
            o.projectRoleId = $(jqSels[0]).val();
            o.specialty = $(jqSels[1]).val();
            createList.push(o);
        });

        var createBagList = [];
        $("#t_bag  tr:not(:first)").each(function () {
            var jqInputs = $('input', $(this));
            var jqSels = $('select', $(this));
            o = new Object();
            o.assignmentId = $(jqInputs[1]).val();
            o.taskTypeCode = $('#lookupCode').val();
            o.elementTypeId = $(jqSels[0]).val();
            o.elementTypeName = $(jqSels[0]).find("option:selected").text();
            createBagList.push(o);
        });

        var data = $('#form_taskType').serializeJSON();

        data.assignmentsDTOList = createList;
        data.delRoles = delRoles.toString();
        data.elementsDTOList = createBagList;
        data.delElements = delElements.toString();

        $.ajax({
            url: config.baseurl +"/api/taskTypeSetRest/saveTaskType",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(data),
            type: "POST",
            beforeSend: function() {
            },
            success: function(d) {
                if ("0000" == d.returnCode) {
                    now_pageNo = t.page();
                    window.parent.success(tips_saveSuccess);
                    $('#add_modal').on('shown.bs.modal', function (e) {}).modal('hide');
                    $('#bu_queryData').trigger("click");
                } else {
                    window.parent.error(d.returnMsg);
                }
            },
            complete: function() {
                window.parent.closeLoading();
            },
            error: function() {
                window.parent.closeLoading();
                window.parent.error(ajaxError_sys);
            }
        });
    }

});

