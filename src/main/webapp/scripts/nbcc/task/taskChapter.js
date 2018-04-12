var delTemplateChapterIds = [];
var showData = [];
var rootData;

var config = new nerinJsConfig();
// var logonUser;

// 任务头id
var chapter_taskId;
// 检出检入控制
var reloadForLock = false;
// 删除章节，记录的chapterId
var delChapterIds = [];
// 是否可以删除或新增章节
var isAddAndDelChapter = false;
// 封面头
var taskCover = null;
// 是否有创建权限
var createRule = false;
// 是否有提交权限
var submitRule = false;
// 可筛选的专业
var chapter_searchSpecialtys;

var ebsUserForWorkBag = [];

function setSpecialtyModalCheckAll() {
    var isChecked = $('#ch_specialtyAll').prop("checked");
    $("input[name='ch_specialty']").prop("checked", isChecked);
    $("input[name='ch_specialty']").each(function () {
        var $tr = $(this).parent().parent();
        if (isChecked){
            $tr.addClass('dt-select');
        } else {
            $tr.removeClass('dt-select');
        }
    });
}

function clearCheckAll() {
    if ($('#chAll_treetableChapter').hasClass("glyphicon-check")) {
        $('#chAll_treetableChapter').removeClass("glyphicon-check");
        $('#chAll_treetableChapter').addClass("glyphicon-unchecked");
    }
}

$('#bu_addback').on('click', function () {
    $('#chapter_modal').modal('hide');
    $('#add_modal').modal('show');
});

function clearTreeReview() {
    var tree = $("#treetable").fancytree("getTree");
    tree.reload([]).done(function(){});
}

function loadCreateRule() {
    $.ajax({
        url : config.baseurl +"/api/taskRest/checkTaskCreate",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        async: false,
        data: {
            projectId: headerData.projectId,
            taskType: headerData.taskType
        },
        type:"GET",
        beforeSend:function(){
            //window.parent.showLoading();
        },
        success:function(d){
            createRule = d;
            if (!d) {
                var li = $('#bu_apply_gsjps').parent();
                $(li).hide();
                $('#taskName').attr("disabled", "disabled");
            } else {
                var li = $('#bu_apply_gsjps').parent();
                $(li).show();
                $('#taskName').removeAttr("disabled");
            }
            isAddAndDelChapter = d;
        },
        complete:function(){
            //window.parent.closeLoading();
        },
        error:function(){
            //window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

function loadSubmitRule() {
    $.ajax({
        url : config.baseurl +"/api/taskRest/checkTaskSubmit",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        async: false,
        data: {
            projectId: headerData.projectId,
            taskType: headerData.taskType
        },
        type:"GET",
        beforeSend:function(){
            //window.parent.showLoading();
        },
        success:function(d){
            submitRule = d;
            if (!d) {
                var li = $('#bu_submit_gsjps').parent();
                $(li).hide();
                var li2 = $('#bu_write_gsjps').parent();
                $(li2).hide();
            } else {
                var li = $('#bu_submit_gsjps').parent();
                $(li).show();
                var li2 = $('#bu_write_gsjps').parent();
                $(li2).show();
            }
        },
        complete:function(){
            //window.parent.closeLoading();
        },
        error:function(){
           //window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

$('#bu_queryChapter').on('click', function () {
    delWithEnableFlag = -1;
    if($('#show_eff').is(':checked')) {
        loadForQueryChapter();
    } else {
        setDisplayTree();
        resetTitleNo();
    }
});

function loadForQueryChapter() {
    $.ajax({
        url: config.baseurl +"/api/taskRest/taskChaptersList",
        data: {
            taskHeaderId: chapter_taskId
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            var d = data.dataSource;
            var tree = $("#treetableChapter").fancytree("getTree");
            showData = [];
            for (var a = 1; a < d.length; a++) {
                showData.push(d[a]);
            }
            rootData = d[0];
            console.log(showData);
            tree.reload(showData).done(function(){
                fancyTreeExpanded();
                setDisplayTree();
                resetTitleNo();
            });
        },
        complete: function() {
            window.parent.closeLoading();
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

// 筛选章节，其他tr隐藏
function setDisplayTree() {
    var nodeList = getTreeAllNode();
    var specialtys = $('select[name="sel_specialty"]');
    var enableFlags = $("input[name='enableFlag']");

    var specialty = $('#sel_query_specialty').val();
    var chapterStatus = $('#sel_query_chapteStatus').val();
    var isChecked = $('#show_eff').is(':checked');

    var trs = $('#treetableChapter tbody tr');

    $(nodeList).each(function (i) {
        $(trs[i]).show();
        // 先判断是不是显示未启用
        if (isChecked) {
            $(trs[i]).show();
            setDisplayTree_2(nodeList, trs, i, specialtys, specialty, chapterStatus);
        } else {
            $(trs[i]).show();
            if (!$(enableFlags[i]).is(':checked')) {
                $(trs[i]).hide();
            } else {
                setDisplayTree_2(nodeList, trs, i, specialtys, specialty, chapterStatus);
            }
        }
    });
}

function setDisplayTree_2(nodeList, trs, i, specialtys, specialty, chapterStatus) {
    if ("" != specialty && "" != chapterStatus) {
        if (specialtys[i].value == specialty && nodeList[i].data.chapterStatus == chapterStatus) {
            $(trs[i]).show();
        } else {
            $(trs[i]).hide();
        }
    } else if ("" != specialty) {
        if (specialtys[i].value == specialty) {
            $(trs[i]).show();
        } else {
            $(trs[i]).hide();
        }
    } else if ("" != chapterStatus) {
        if (nodeList[i].data.chapterStatus == chapterStatus) {
            $(trs[i]).show();
        } else {
            $(trs[i]).hide();
        }
    }
}

function addTaskCover(taskHeaderId) {
    $('#taskCover tbody').empty();
    $.ajax({
        url: config.baseurl +"/api/taskRest/taskChaptersList",
        data: {
            taskHeaderId: taskHeaderId,
            lineType: "COVER"
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        success: function(data) {
            var d = data.dataSource;
            var projectRoleId = "";
            var specialty = "";
            var personIdResponsible = "";
            if (null != d && 1 == d.length) {
                taskCover = d[0];
                if (null != d && 1 == d.length) {
                    projectRoleId = undefined != d[0].projectRoleId ? d[0].projectRoleId : "";
                    specialty = undefined != d[0].specialty ? d[0].specialty : "";
                    personIdResponsible = undefined != d[0].personIdResponsible ? d[0].personIdResponsible : "";
                }
                var table = $('#taskCover');
                var row = $("<tr></tr>");
                var td = $("<td width='140'></td>");
                var td2 = $("<td width='140'></td>");
                var td3 = $("<td width='140'></td>");
                var proRole = createProRoleSel(projectRoleId, "sel_proRole_cover");
                td.append(proRole);
                var specialt = createSpecialt(specialty, "sel_specialty_cover");
                td2.append(specialt);
                var responsible = createResponsible([d[0]], personIdResponsible, "sel_responsible_cover");
                td3.append(responsible);
                row.append(td).append(td2).append(td3);
                table.append(row);
                $(proRole).selectpicker('refresh');
                $(proRole).selectpicker('show');
                $(specialt).selectpicker('refresh');
                $(specialt).selectpicker('show');
            } else {
                taskCover = null;
            }
        }
    });
}

var firstInitNo = 0;
function reloadChapter(taskHeaderId, data, v) {
    clearCheckAll();
    createRule = false;
    submitRule = false;

    window.parent.showLoading();
    // 加载是否可以新增或删除章节权限
    if (0 == firstInitNo) {
        loadCreateRule();
        // 加载是否有提交检审权限
        loadSubmitRule();
        firstInitNo++;
    }

    delChapterIds = [];
    chapter_taskId = taskHeaderId;
    // 初始化按钮显示
    initButtons(v);

    load_chapter_searchSpecialtys();
    load_chapter_status();

    showData = [];
    for (var a = 1; a < data.length; a++) {
        showData.push(data[a]);
    }
    rootData = data[0];
    var tree = $("#treetableChapter").fancytree("getTree");
    console.log(showData);

    if (0 == projectRoles.length || 0 == specialtys.length) {
        $.getJSON(config.baseurl + "/api/templateRest/projectRoleList?projectId=" + headerData.projectId, function (data) {
            projectRoles = data.dataSource;
            $.getJSON(config.baseurl + "/api/templateRest/specialtyList?projectId=" + headerData.projectId, function (data) {
                specialtys = data.dataSource;
                specialtys.forEach(function (data) {
                    data.specialtyName = data.specialty.substring(1) + data.specialtyName;
                });
                tree.reload(showData).done(function(){
                    // 加载封面
                    addTaskCover(taskHeaderId);
                    fancyTreeExpanded();
                    // fancyTreeeCollapse();
                    setChapterTreeTable();
                    setCheckOutWithLoadAfter();
                    // $(".selectpicker1").selectpicker('refresh');
                    // $(".selectpicker1").selectpicker('show');
                    // 设置编号
                    resetTitleNo();
                    window.parent.closeLoading();
                    doCreateSelectpicker = true;

                    // 检查初始数据是否有当前用户已经检出的
                    if (1 == v && ("WORKING" == headerData.taskStatus || "NEW" == headerData.taskStatus)) {
                        var initNo = 0;
                        var nodeList = getTreeAllNode();
                        nodeList.forEach(function (node) {
                            if (node.data.attribute12 == logonUser.personId) {
                                node.setSelected(true);
                                initNo++;
                            }
                        });
                        if (0 < initNo)
                            doCheckOut(2);
                    }
                });
            });
        });
    } else {
        tree.reload(showData).done(function(){
            fancyTreeExpanded();
            // fancyTreeeCollapse();
            setChapterTreeTable();
            setCheckOutWithLoadAfter();
            // $(".selectpicker1").selectpicker('refresh');
            // $(".selectpicker1").selectpicker('show');
            // 设置编号
            resetTitleNo();
            window.parent.closeLoading();
            doCreateSelectpicker = true;

            // 检查初始数据是否有当前用户已经检出的
            if (1 == v && ("WORKING" == headerData.taskStatus || "NEW" == headerData.taskStatus)) {
                var initNo = 0;
                var nodeList = getTreeAllNode();
                nodeList.forEach(function (node) {
                    if (node.data.attribute12 == logonUser.personId) {
                        node.setSelected(true);
                        initNo++;
                    }
                });
                if (0 < initNo)
                    doCheckOut(2);
            }
        });
    }
}

// 只有创建权限和章节的节点责任人中的人员才显示签出按钮，其他人员不显示签出按钮
function setCheckOutWithLoadAfter() {
    var responsibles = $('select[name="sel_responsible"] option:selected');
    var t = false;
    responsibles.each(function () {
        if (logonUser.personId == $(this).val()) {
            t = true;
            return false;
        }
    });
    // 只有新建和正在协作的，才显示签出按钮
    if ("WORKING" == headerData.taskStatus || "NEW" == headerData.taskStatus) {
        if (createRule || t)
            $('#bu_checkOut').show();
        else
            $('#bu_checkOut').hide();
    } else {
        $('#bu_checkOut').hide();
    }
}

function setChapterTreeTable() {
    $('#treetableChapter th:eq(2)').hide();
    $('#treetableChapter tr').find('td:eq(2)').hide();
    if ("B" == headerData.tag || "C" == headerData.tag) {
        $('#treetableChapter th:eq(8)').show();
        $('#treetableChapter th:eq(9)').show();
        $('#treetableChapter th:eq(10)').show();
        $('#treetableChapter th:eq(11)').show();
        $('#treetableChapter th:eq(12)').show();
        $('#treetableChapter th:eq(13)').show();

        $('#treetableChapter tr').find('td:eq(8)').show();
        $('#treetableChapter tr').find('td:eq(9)').show();
        $('#treetableChapter tr').find('td:eq(10)').show();
        $('#treetableChapter tr').find('td:eq(11)').show();
        $('#treetableChapter tr').find('td:eq(12)').show();
        $('#treetableChapter tr').find('td:eq(13)').show();
        $('#treetableChapter').css("width", "1500px");
    }
    if ("A" == headerData.tag) {
        $('#treetableChapter th:eq(8)').hide();
        $('#treetableChapter th:eq(9)').hide();
        $('#treetableChapter th:eq(10)').hide();
        $('#treetableChapter th:eq(11)').hide();
        $('#treetableChapter th:eq(12)').hide();
        $('#treetableChapter th:eq(13)').show();

        $('#treetableChapter tr').find('td:eq(8)').hide();
        $('#treetableChapter tr').find('td:eq(9)').hide();
        $('#treetableChapter tr').find('td:eq(10)').hide();
        $('#treetableChapter tr').find('td:eq(11)').hide();
        $('#treetableChapter tr').find('td:eq(12)').hide();
        $('#treetableChapter tr').find('td:eq(13)').show();
        $('#treetableChapter').css("width", "1080px");
    }
    $('#bu_audit_js').hide();
    $('#bu_un_audit_js').hide();
    $('#bu_gsjps').hide();
    $('#bu_submitAudit').hide();
    if (0 != tmp_initButton) {
        if ("B" == headerData.tag || "C" == headerData.tag) {
            // $('#bu_gsjps').show();

            var personIdDesigns = $('input[name=personIdDesign]');
            var t = false;
            personIdDesigns.each(function () {
                if (logonUser.personId == $(this).val()) {
                    t = true;
                    return false;
                }
            });
            if ("WORKING" == headerData.taskStatus) {
                // 对于文本任务类型为B、C，只有章节的设计人才显示专业专业校审按钮，其他人员不显示
                if (t) {
                    $('#bu_audit_js').removeAttr("disabled");
                    $('#bu_audit_js').show();

                    $('#bu_un_audit_js').removeAttr("disabled");
                    $('#bu_un_audit_js').show();
                } else {
                    $('#bu_audit_js').attr("disabled", true);
                    $('#bu_audit_js').hide();

                    $('#bu_un_audit_js').attr("disabled", true);
                    $('#bu_un_audit_js').hide();
                }
            } else {
                $('#bu_audit_js').attr("disabled", true);
                $('#bu_audit_js').hide();

                $('#bu_un_audit_js').attr("disabled", true);
                $('#bu_un_audit_js').hide();
            }
        }
        if ("A" == headerData.tag || "C" == headerData.tag) {
            $('#bu_submitAudit').show();
        }
        // 对于文本任务类型为B，只有创建权限和提交权限的人员才显示公司级评审按钮，其他人员不显示
        if ("B" == headerData.tag) {
            if (createRule || submitRule)
                $('#bu_gsjps').show();
            else
                $('#bu_gsjps').hide();
        }
    }

}

function load_chapter_searchSpecialtys() {
    $.getJSON(config.baseurl + "/api/taskRest/getMyTaskChapterSpecialtysList?taskHeaderId=" + chapter_taskId, function (data) {
        chapter_searchSpecialtys = data.dataSource;
        chapter_searchSpecialtys.forEach(function (data) {
            data.specialtyName = data.specialty.substring(1) + data.specialtyName;
        });
        var select = $('#sel_query_specialty');
        select.empty();
        select.append("<option value=''>请选择</option>");
        for (var i = 0; i < chapter_searchSpecialtys.length; i++) {
            select.append("<option value='" + chapter_searchSpecialtys[i].specialty + "'>" + chapter_searchSpecialtys[i].specialtyName + "</option>")
        }
    });
}

function load_chapter_status() {
    $.getJSON(config.baseurl + "/api/taskRest/getMyTaskChapterStatusList?taskHeaderId=" + chapter_taskId, function (data) {
        var chapter_status = data.dataSource;
        var select = $('#sel_query_chapteStatus');
        select.empty();
        select.append("<option value=''>请选择</option>");
        for (var i = 0; i < chapter_status.length; i++) {
            select.append("<option value='" + chapter_status[i].chapterStatus + "'>" + chapter_status[i].chapterStatusName + "</option>")
        }
    });
}

function fancyTreeExpanded() {
    $("#treetableChapter").fancytree("getRootNode").visit(function(node){
        node.setExpanded(true);
    });
}

function fancyTreeeCollapse() {
    $("#treetableChapter").fancytree("getRootNode").visit(function (node) {
        node.setExpanded(false);
    });
}

$('#bu_fancyTreeExpanded').on('click', function () {
    fancyTreeExpanded();
});

$('#bu_fancyTreeeCollapse').on('click', function () {
    fancyTreeeCollapse();
});

$('#bu_setOpen').on('click', function () {
    setOpen(true);
});

$('#bu_setUnOpen').on('click', function () {
    setOpen(false);
});

$('#bu_setEnabled').on('click', function () {
    setEnabled(true);
});

$('#bu_setUnEnabled').on('click', function () {
    setEnabled(false);
});

$('#bu_setWorkBag').on('click', function () {
    var tree = $("#treetableChapter").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();

    var checkSpecialty = "";
    var checkNo = 0;
    var enableNo = 0;
    if (0 == selNodes.length) {
        window.parent.warring(valTips_selOne);
        return;
    } else {
        selNodes.forEach(function(node) {
            if (0 == node.data.editFlag)
                enableNo++;
            var tmp_tr = node.tr;
            var trNo = $('#treetableChapter tr').index(tmp_tr);
            if ("" == checkSpecialty) {
                checkSpecialty = $('select[name="sel_specialty"]')[trNo - 1].value;
            } else {
                if (checkSpecialty != $('select[name="sel_specialty"]')[trNo - 1].value)
                    checkNo++;
            }
        });
        if (0 < checkNo) {
            window.parent.warring("请勾选相同专业");
            return;
        }
        if (0 < enableNo) {
            window.parent.warring("不能操作未签出的章节，请确认");
            return;
        }
        var tmp_tr = selNodes[0].tr;
        var trNo = $('#treetableChapter tr').index(tmp_tr);
        checkSpecialty = $('select[name="sel_specialty"]')[trNo - 1];
        openBatchWorkBag(checkSpecialty);
    }
});

function openBatchWorkBag(checkSpecialty) {
    $.ajax({
        url: config.baseurl +"/api/lev3/getDlvr",
        data: {
            projID: headerData.projectId,
            major: checkSpecialty.value,
            phaseCode: headerData.designPhase
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            var panel = ContentMethod_batch(data);
            $('#div_batchWorkBag').html(panel);
            $('#batchWorkBagModal').modal('show');
        },
        complete: function() {
            window.parent.closeLoading();
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

function ContentMethod_batch(d) {
    var table = $('<table class="table table-bordered" style="color: #0a0a0a; cursor: pointer; margin-bottom: 0px;"></table>');
    $(d).each(function (i) {
        var tr = $('<tr></tr>');
        var td = $('<td width="320" class="clickBagWorkTd">' + d[i].name + '/' + d[i].designName + '/' + d[i].checkName + '/' + d[i].reviewName +'</td>');
        td.append('<input type="hidden" value="' + d[i].id + '" />');
        td.append('<input type="hidden" value="' + d[i].name + '" />');
        tr.append(td);
        table.append(tr);
    });
    table.on('click', '.clickBagWorkTd', function () {

        var tr = $(this);
        var tree = $("#treetableChapter").fancytree("getTree"),
            selNodes = tree.getSelectedNodes();
        selNodes.forEach(function(node) {
            if (!node.hasChildren()) {
                var buttons = $('button', node.tr);
                var bu = buttons[0];
                var jqInputs = $('input', tr.get(0));
                $(bu).attr("id", "bu_bag_" + $(jqInputs[0]).val());
                $(bu).html("<i class='fa fa-suitcase' style='margin-right: 5px;'></i>" + $(jqInputs[1]).val());
                loadPersonWithWorkBag_batch($(jqInputs[0]).val(), $(bu).parent());
            }
        });
    });

    // panel_foot.append('<div class="pull-right"><button type="button" class="btn btn-default btn-xs" onclick="javascript:clearTdWorkBag(this);">' +
    //     '<i class="glyphicon glyphicon-remove"></i>清空工作包</button>');
    return table;
}

function loadPersonWithWorkBag_batch(id, b_td) {
    $.ajax({
        url: config.baseurl +"/api/taskRest/getProjectElementPersonList",
        data: {
            elementId: id
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            setNewPerson(data, b_td);
        },
        complete: function() {
            window.parent.closeLoading();
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

$('#bu_setSpecialty').on('click', function () {
    var tree = $("#treetableChapter").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();

    if (0 == selNodes.length) {
        window.parent.warring(valTips_selOne);
        return;
    }
    var enableNo = 0;
    selNodes.forEach(function(node) {
        if (0 == node.data.editFlag)
            enableNo++;
    });
    if (0 < enableNo) {
        window.parent.warring("不能操作未签出的章节，请确认");
        return;
    }

    var select = $('#setBatchSpecialty');
    select.empty();
    select.append("<option value=''>请选择</option>");
    for (var i = 0; i < chapter_searchSpecialtys.length; i++) {
        select.append("<option value='" + chapter_searchSpecialtys[i].specialty + "'>" + chapter_searchSpecialtys[i].specialtyName + "</option>")
    }
    $('#batchSpecialtyModal').modal('show');
});

function setSpecialty(sel) {
    var tree = $("#treetableChapter").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();
    var v = sel.value;
    var t = $(sel).find("option:selected").text();;
    if ("" != v) {
        selNodes.forEach(function(node) {
            var jqInputs = $('select', node.tr);
            var select = $(jqInputs[1]);
            if (2 == select.find('option').length) {
                select.empty();
                select.append("<option value=''>请选择</option>");
                select.append("<option value='" + v + "'>" + t + "</option>");
                select.val(v);
            } else {
                select.val(v);
            }
            var selTr = node.tr;
            var trNo = $('#treetableChapter tr').index(selTr);
            var checkProRole = $('select[name="sel_proRole"]')[trNo - 1];
            var checkSpecialty = $('select[name="sel_specialty"]')[trNo - 1];
            if ("" == checkProRole.value || "" == checkSpecialty.value)
                return;
            $.ajax({
                url: config.baseurl +"/api/taskRest/getTaskChapterResponsibleList",
                data: {
                    projectId: headerData.projectId,
                    specialty: checkSpecialty.value,
                    roleId: checkProRole.value
                },
                contentType : 'application/x-www-form-urlencoded',
                dataType: "json",
                type: "GET",
                beforeSend: function() {
                    window.parent.showLoading();
                },
                success: function(data) {
                    var jqTds = $('>td', selTr);
                    $(jqTds[13]).empty();
                    $(jqTds[13]).html(createResponsible(data.dataSource, ""));
                },
                complete: function() {
                    window.parent.closeLoading();
                },
                error: function() {
                    window.parent.closeLoading();
                    window.parent.error(ajaxError_loadData);
                }
            });
        });
    }
}

function setEnabled(enable) {
    var tree = $("#treetableChapter").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();

    if (0 == selNodes.length) {
        window.parent.warring(valTips_selOne);
        return;
    }
    var enableNo = 0;
    selNodes.forEach(function(node) {
        if (0 == node.data.editFlag)
            enableNo++;
    });
    if (0 < enableNo) {
        window.parent.warring("不能操作未签出的章节，请确认");
        return;
    }
    selNodes.forEach(function(node) {
        var jqInputs = $('input', node.tr);
        if (enable) {
            if (!$(jqInputs[1]).is(':checked')) {
                $(jqInputs[1]).trigger('click');
            }
        } else {
            if ($(jqInputs[1]).is(':checked')) {
                $(jqInputs[1]).trigger('click');
            }
        }
    });
}

function setOpen(isChecked) {
    var tree = $("#treetableChapter").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();

    if (0 == selNodes.length) {
        window.parent.warring(valTips_selOne);
        return;
    }
    var enableNo = 0;
    selNodes.forEach(function(node) {
        if (0 == node.data.editFlag)
            enableNo++;
    });
    if (0 < enableNo) {
        window.parent.warring("不能操作未签出的章节，请确认");
        return;
    }
    selNodes.forEach(function(node) {
        var jqInputs = $('input', node.tr);
        $(jqInputs[2]).prop("checked", isChecked);
    });
}

function setPersonNameResponsible(v) {
    if (1 == v) {
        var checkProRole = $('select[name="sel_proRole_cover"]')[0];
        var checkSpecialty = $('select[name="sel_specialty_cover"]')[0];
        if ("" == checkProRole.value || "" == checkSpecialty.value)
            return;
        $.ajax({
            url: config.baseurl +"/api/taskRest/getTaskChapterResponsibleList",
            data: {
                projectId: headerData.projectId,
                specialty: checkSpecialty.value,
                roleId: checkProRole.value
            },
            contentType : 'application/x-www-form-urlencoded',
            dataType: "json",
            type: "GET",
            beforeSend: function() {
                window.parent.showLoading();
            },
            success: function(data) {
                var td = $('#taskCover>tbody>tr:eq(0)').find("td:eq(2)");
                td.empty();
                td.html(createResponsible(data.dataSource, "", "sel_responsible_cover"));
            },
            complete: function() {
                window.parent.closeLoading();
            },
            error: function() {
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    } else {
        var selTr = $('#treetableChapter tr.fancytree-active')[0];
        var trNo = $('#treetableChapter tr').index(selTr);
        var checkProRole = $('select[name="sel_proRole"]')[trNo - 1];
        var checkSpecialty = $('select[name="sel_specialty"]')[trNo - 1];
        if ("" == checkProRole.value || "" == checkSpecialty.value)
            return;
        $.ajax({
            url: config.baseurl +"/api/taskRest/getTaskChapterResponsibleList",
            data: {
                projectId: headerData.projectId,
                specialty: checkSpecialty.value,
                roleId: checkProRole.value
            },
            contentType : 'application/x-www-form-urlencoded',
            dataType: "json",
            type: "GET",
            beforeSend: function() {
                window.parent.showLoading();
            },
            success: function(data) {
                var jqTds = $('>td', selTr);
                $(jqTds[13]).empty();
                $(jqTds[13]).html(createResponsible(data.dataSource, ""));
            },
            complete: function() {
                window.parent.closeLoading();
            },
            error: function() {
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }
}


var projectRoles = [];
// 初始化项目角色下拉
var createProRoleSel = function (val, name, data) {
    var select;
    if (undefined != data) {
        select = $("<select name='" + (name ? name : 'sel_proRole') + "' class='selectpicker1 form-control input-sm' placeholder='请选择' style='padding-left: 2px;' data-live-search='true' data-size='8' onmouseover='javascript: initCreateProRoleSel(this);'></select>");
        select.append("<option value=''>请选择</option>");
        select.append("<option value='" + data.projectRoleId + "'>" + data.projectRoleName + "</option>");
    } else {
        select = $("<select name='" + (name ? name : 'sel_proRole') + "' class='selectpicker1 form-control input-sm' placeholder='请选择' style='padding-left: 2px;' data-live-search='true' data-size='8' onchange='javascript: setPersonNameResponsible(" + (name ? '1' : '0') + ");'></select>");
        select.append("<option value=''>请选择</option>");
        for (var i = 0; i < projectRoles.length; i++) {
            select.append("<option value='" + projectRoles[i].projectRoleId + "'>" + projectRoles[i].projectRoleName + "</option>");
        }
    }
    select.val(val);
    return select;
};

function initCreateProRoleSel(sel) {
    var $tr = $(sel).parent().parent();
    var trNo = $('#treetableChapter tr').index($(sel).parent().parent());
    sel = createProRoleSel(sel.value);
    $('#treetableChapter tbody tr:eq(' + (trNo-1) + ') td:nth-child(5)').html(sel);
}

function initCreateSpecialt(sel) {
    var $tr = $(sel).parent().parent();
    var trNo = $('#treetableChapter tr').index($(sel).parent().parent());
    sel = createSpecialt(sel.value);
    $('#treetableChapter tbody tr:eq(' + (trNo-1) + ') td:nth-child(6)').html(sel);
}

var specialtys = [];
// 初始化分配专业下拉
var createSpecialt = function (val, name, data) {
    var select;
    if (undefined != data) {
        select = $("<select name='" + (name ? name : 'sel_specialty') + "' class='selectpicker1 form-control input-sm' placeholder='请选择' style='padding-left: 2px;' data-live-search='true' data-size='8' onmouseover='javascript: initCreateSpecialt(this);'></select>");
        select.append("<option value=''>请选择</option>");
        var specialty_1 = data.specialty ? data.specialty.substring(1) : data.specialty;
        select.append("<option value='" + data.specialty + "'>" + specialty_1 + data.specialtyName + "</option>");
    } else {
        select = $("<select name='" + (name ? name : 'sel_specialty') + "' class='selectpicker1 form-control input-sm' placeholder='请选择' style='padding-left: 2px;' data-live-search='true' data-size='8' onchange='javascript: setPersonNameResponsible(" + (name ? '1' : '0') + ");'></select>");
        select.append("<option value=''>请选择</option>");
        for (var i = 0; i < specialtys.length; i++) {
            select.append("<option value='" + specialtys[i].specialty + "'>" + specialtys[i].specialtyName + "</option>")
        }
    }
    select.val(val);
    return select;
}

var createResponsible = function (data, val, name) {
    var select = $("<select name='" + (name ? name : 'sel_responsible') + "' class='form-control input-sm' placeholder='请选择' style='padding-left: 2px;'></select>");
    select.append("<option value=''>请选择</option>");
    for (var i = 0; i < data.length; i++) {
        if (0 != data[i].personIdResponsible && null != data[i].personIdResponsible)
            select.append("<option value='" + data[i].personIdResponsible + "'>" + data[i].personNameResponsible + "</option>")
    }
    select.val(val);
    if (data && 1 == data.length) {
        select.find("option:eq(1)").attr("selected",true);
    }
    return select;
}

var createInput = function (name, type, val) {
    var input = $("<input type='" + type + "' style='width: 100%; display: inline-block;' name='" + name + "' class='form-control input-sm' value='" + val + "' />");
    return input;
}

var createWorkBag = function (bagName, bagId, bagNo) {
    var newBagName = "";
    if (0 <= bagName.indexOf("失效"))
        newBagName = "<font color='red'>" + bagName + "</font>";
    else
        newBagName = bagName;
    var button = $("<button data-toggle='popover' data-trigger='focus' name='workBag' id='bu_bag_" + bagId + "' type='button' class='btn btn-default gyl-btn-sm' style='width: 100%; text-align: left;'><i class='fa fa-suitcase' style='margin-right: 5px;'></i>" + newBagName + "</button>");
    return button;
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

$("#treetableChapter").ZTable({
    // tableLayout: 'automatic'   //css table-layout='automatic'
    // colMinWidth: 50    //最小列宽50px
});

var doCreateSelectpicker = false;
$("#treetableChapter").fancytree({
    selectMode: 2,
    extensions: ["dnd", "edit", "glyph", "table"],
    checkbox: true,
    keyboard: false,
    dnd: {
        focusOnClick: false,
        dragStart: function(node, data) { return true; },
        dragEnter: function(node, data) { return true; },
        dragDrop: function(node, data) {
            var inS = node.data.editFlag;
            var moveS = data.otherNode.data.editFlag;
            if (reloadForLock) {
                if (1 == inS && 0 == moveS) {
                    window.parent.warring("未签出章节不能移动至已签出章节！");
                    return false;
                } else if (0 == inS && 1 == moveS) {
                    window.parent.warring("已签出章节不能移动至未签出！");
                    return false;
                } else if (0 == inS && 0 == moveS) {
                    window.parent.warring("请先签出章节！");
                    return false;
                }
            } else {
                window.parent.warring("请先签出章节！");
                return false;
            }
            data.otherNode.moveTo(node, data.hitMode);
            //fancyTreeExpanded();
            resetTitleNo();
        }
    },
    glyph: glyph_opts,
    source: showData,
    table: {
        checkboxColumnIdx: 0,
        nodeColumnIdx: 1
    },
    select: function(event, data) {
        if (data.node.selected)
            $(data.node.tr).css("backgroundColor", "#C6E2FF");
        else
            $(data.node.tr).css("backgroundColor", "");
        // var selNodes = data.tree.getSelectedNodes();
        // var selKeys = $.map(selNodes, function(node){
        //     // $(node.tr).css("backgroundColor", "#FFFF00");
        //     return "[" + node.key + "]: '" + node.title + "'";
        // });
    },
    renderColumns: function(event, data) {
        var node = data.node,
            $tdList = $(node.tr).find(">td");

        var editFlag = reloadForLock ? ("0" == node.data.editFlag ? true : false) : true;

        $tdList.eq(2).text(node.getIndexHier());
        $tdList.eq(2).hide();
        var col3 = node.data.chapterName ? node.data.chapterName : "";
        $tdList.eq(3).html(createInput("chapterName", "text", col3).attr("disabled", editFlag));
        var col4 = node.data.projectRoleId ? node.data.projectRoleId : "";
        $tdList.eq(4).html(createProRoleSel(col4, null, node.data).attr("disabled", editFlag));
        var col5 = node.data.specialty ? node.data.specialty : "";
        $tdList.eq(5).html(createSpecialt(col5, null, node.data).attr("disabled", editFlag));
        var col6 = null != node.data.enableFlag ? ("1" ==node.data.enableFlag ? "checked" : "") : "checked";
        // 模板控制的是否可删除，true代表可以删除该章节, checkbox启用
        var col6_delFlag = null != node.data.deleteFlag ? ("1" ==node.data.deleteFlag ? true : false) : false;
        if (0 == node.data.templateChapterId)
            col6_delFlag = true;
        var col6Html = $("<input type='checkbox' name='enableFlag' onclick='javascript:autoSaveForChapterNull(this);'" + col6 + "/>");
        $tdList.eq(6).html(col6Html.attr("disabled", col6_delFlag ? editFlag : true));
        $tdList.eq(6).css("textAlign", "center");
        var col7 = null != node.data.attribute10 ? ("1" ==node.data.attribute10 ? "checked" : "") : "checked";
        var col7Html = $("<input type='checkbox' name='attribute10' " + col7 + "/>")
        $tdList.eq(7).html(col7Html.attr("disabled", editFlag));
        $tdList.eq(7).css("textAlign", "center");

        $tdList.eq(8).html(createWorkBag(node.data.projElementName ? node.data.projElementName : " ", node.data.projElementId, node.data.projElementNumber).attr("disabled", editFlag));

        //$tdList.eq(9).text(node.data.personNameDesign);
        $tdList.eq(9).html(createInput("personIdDesign", "hidden", node.data.personIdDesign)).append(node.data.personNameDesign);
        //$tdList.eq(10).text(node.data.personNameProofread);
        $tdList.eq(10).html(createInput("personIdProofread", "hidden", node.data.personIdProofread)).append(node.data.personNameProofread);
       // $tdList.eq(11).text(node.data.personNameAudit);
        $tdList.eq(11).html(createInput("personIdAudit", "hidden", node.data.personIdAudit)).append(node.data.personNameAudit);
        //$tdList.eq(12).text(node.data.personNameApprove);
        $tdList.eq(12).html(createInput("personIdApprove", "hidden", node.data.personIdApprove)).append(node.data.personNameApprove);
        $tdList.eq(13).html(createResponsible([node.data], node.data.personIdResponsible).attr("disabled", editFlag));
        $tdList.eq(14).text(node.data.attribute11Name ? node.data.attribute11Name : "");

        $tdList.eq(16).text(node.data.chapterStatusName);
        $tdList.eq(17).text(node.data.attribute12Name ? node.data.attribute12Name : "");
        
        if (null != node.data.systempLineName)
            $tdList.eq(15).html('<a href="' + node.data.systempLineUrl + '"'+' target="_blank">' + node.data.systempLineName + '</a>');

        if (null != node.data.xmlCode)
            var suffix = ".rtf";
        else
            var suffix = ".docx";

        if ('N' == node.data.wordFlag || undefined == node.data.wordFlag)  //无权处理文档
            {
                var disabledFlag = 'Y';
                var viewType = '&pattern=ViewText';
                // javascript:void(0);
                var PageeOfficeUrl = '';
                var lstyle =' style=" border:1px solid #E8E8E8; background:#EFEFEF; color:#666; text-decoration:none; border-radius:2px; display:inline-block; text-align:center; line-height:30px;"';
            }
        else
            {
                var disabledFlag = 'N';
                if ('S' == node.data.wordFlag)
                    var viewType = '&pattern=ViewText';
                else
                    var viewType = '&pattern=EditText';
                
                // var userID = new logonUser;
                var PageeOfficeUrl = config.baseurl + '/pages/wbsp/code/poweb/poweb.html?source=TASK&chapterId=' + node.data.chapterId + '&taskHeaderId=' + node.data.taskHeaderId + '&isoa=0';
                // var PageeOfficeUrl = config.contextConfig.nbccPageOfficeUrl + 'file_name=' + node.data.taskChapterFileName + '&caption=' + node.data.chapterName + viewType;
                //'file_name=PO__'+ node.data.chapterId + suffix + '&caption=' + node.data.chapterName + viewType;
                var lstyle ='';
            }

        if (undefined == node.data.chapterId)
            disabledFlag = 'Y';
        var ldisable = ('Y' == disabledFlag ? "disabled = 'disabled'" : "");
        // $tdList.eq(18).html('<div align="center"><button type="button" class="btn btn-default" style="padding: 5px 15px;border:none;" id="bu_editChapterword" onclick="hrefClick(&quot;' + PageeOfficeUrl + '&quot;,&quot;' + suffix + '&quot;)"' + ldisable + '> <i class="glyphicon glyphicon-pencil" ></i></button></div>');
        $tdList.eq(18).html('<div align="center"><button type="button" class="btn btn-default" style="padding: 5px 15px;border:none;" id="bu_editChapterword" onclick="_windowOpen(&quot;' + PageeOfficeUrl + '&quot;)"' + ldisable + '> <i class="glyphicon glyphicon-pencil" ></i></button></div>');


        // 加了列拉伸后，去除overflow
        $tdList.eq(4).css("overflow", "");
        $tdList.eq(5).css("overflow", "");

        if (doCreateSelectpicker && !editFlag) {
            // var select = $tdList.eq(4).find("select");
            // var select2 = $tdList.eq(5).find("select");
            // $(select[0]).selectpicker('refresh');
            // $(select[0]).selectpicker('show');
            // $(select2[0]).selectpicker('refresh');
            // $(select2[0]).selectpicker('show');
        }


        if ("B" == headerData.tag || "C" == headerData.tag) {
            $tdList.eq(8).show();
            $tdList.eq(9).show();
            $tdList.eq(10).show();
            $tdList.eq(11).show();
            $tdList.eq(12).show();
            $tdList.eq(13).show();
        }
        if ("A" == headerData.tag) {
            $tdList.eq(8).hide();
            $tdList.eq(9).hide();
            $tdList.eq(10).hide();
            $tdList.eq(11).hide();
            $tdList.eq(12).hide();
            $tdList.eq(13).show();
        }
    },
    removeNode: function (event, data) { // 删除node触发
        var node = data.node;
        if (1 == delWithEnableFlag)
            saveChapterNoForNull(node);
        if (undefined != node.data.chapterId && 0 == delWithEnableFlag)
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

var chapterNoForNullCount = 0;
function autoSaveForChapterNull(dom) {
    if (!$(dom).is(':checked')) {
        $(dom).parent().trigger("click");
        var node = $("#treetableChapter").fancytree("getActiveNode");
        chapterNoForNullNo = 0;
        chapterNoForNullCount = node.countChildren() + 1;
        delWithEnableFlag = 1;
        node.remove();
        // saveChapterNoForNull(node);
    }
}

var chapterNoForNullNo = 0;
function saveChapterNoForNull(node) {
    var arr = [];
    var o = new Object();
    o.chapterId = node.data.chapterId;
    o.parentChapterId = node.data.parentChapterId;
    o.chapterName = node.data.chapterName;
    o.chapterNo = "";
    o.parentChapterNo = "";

    o.projectRoleId = node.data.projectRoleId;
    o.specialty = node.data.specialty;

    o.enableFlag = "0";
    o.attribute10 = node.data.attribute10;
    o.projElementId = node.data.projElementId;
    o.personIdDesign = node.data.personIdDesign;
    o.personIdProofread = node.data.personIdProofread;
    o.personIdAudit = node.data.personIdAudit;
    o.personIdApprove = node.data.personIdApprove;
    o.personIdResponsible = node.data.personIdResponsible;
    // 原有值
    o.taskHeaderId = node.data.taskHeaderId;
    o.templateChapterId = node.data.templateChapterId;
    o.templateParentChapterId = node.data.templateParentChapterId;
    o.comments = node.data.comments;
    o.objectVersionNumber = node.data.objectVersionNumber;
    o.attributeCategory = node.data.attributeCategory;
    o.attribute1 = node.data.attribute1;
    o.attribute2 = node.data.attribute2;
    o.attribute3 = node.data.attribute3;
    o.attribute4 = node.data.attribute4;
    o.attribute5 = node.data.attribute5;
    o.attribute6 = node.data.attribute6;
    o.attribute8 = node.data.attribute8;
    o.attribute9 = node.data.attribute9;
    o.attribute11 = node.data.attribute11;
    o.attribute12 = node.data.attribute12;
    o.attribute13 = node.data.attribute13;
    o.attribute14 = node.data.attribute14;
    o.attribute14 = node.data.attribute14;
    o.attribute15 = node.data.attribute15;
    o.deleteFlag = node.data.deleteFlag;
    o.chapterStatus = node.data.chapterStatus;
    o.chapterNumber = node.data.chapterNumber;
    // 排序
    o.attribute7 = node.data.attribute7;
    arr.push(o);
    console.log(JSON.stringify(arr));
    // return;
    $.ajax({
        url: config.baseurl +"/api/taskRest/saveTaskChapter",
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: JSON.stringify(arr),
        type: "POST",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            if ("0000" == data.returnCode) {
                chapterNoForNullNo++;
                if (chapterNoForNullNo == chapterNoForNullCount) {
                    window.parent.closeLoading();
                    delWithEnableFlag = 0;
                    loadForLock(2); // 2检出查询，1是检入查询
                    window.parent.success(tips_saveSuccess);
                }
            } else {
                window.parent.error(data.returnMsg);
            }
        },
        complete: function() {
            // window.parent.closeLoading();
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_sys);
        }
    });

}

$('#chAll_treetableChapter').on("click", function () {
    if ($(this).hasClass("glyphicon-unchecked")) {
        treeCheck(true);
        $(this).removeClass("glyphicon-unchecked");
        $(this).addClass("glyphicon-check");
    } else {
        treeCheck(false);
        $(this).removeClass("glyphicon-check");
        $(this).addClass("glyphicon-unchecked");
    }
});

function treeCheck(v) {
    $('#treetableChapter').fancytree("getTree").visit(function(node){
        node.setSelected(v);
        // if (v)
        //     $(node.tr).css("backgroundColor", "#FFFF00");
        // else
        //     $(node.tr).css("backgroundColor", "");
    });
    return false;
}



$('#treetableChapter tbody').on('click', 'button[name=workBag]', function () {
    $(this).parent().trigger("click");
    if (undefined != $(this).attr("aria-describedby")) {
        $(this).popover('destroy');
    } else {
        showWorkBagDiv($(this));
    }
});

function showWorkBagDiv($bu) {
    var node = $("#treetableChapter").fancytree("getActiveNode");
    if (null != node.children) {
        window.parent.warring("该章节有子章节，不允许选择工作包！");
        var td = $bu.parent();
        var jqButtons = $('button', td);
        $(jqButtons[0]).attr("id", "bu_bag_0");
        $(jqButtons[0]).html("<i class='fa fa-suitcase' style='margin-right: 5px;'></i>");
        var s = td.next("td");
        s.html(createInput("personIdDesign", "hidden", "0")).append("");
        var j = s.next("td");
        j.html(createInput("personIdProofread", "hidden", "0")).append("");
        var sh = j.next("td");
        sh.html(createInput("personIdAudit", "hidden", "0")).append("");
        var sd = sh.next("td");
        sd.html(createInput("personIdApprove", "hidden", "0")).append("");
        return;
    }
    var trNo = $('#treetableChapter tr').index($bu.parent().parent());
    var checkSpecialty = $('select[name="sel_specialty"]')[trNo - 1];
    if ("" == checkSpecialty.value) {
        window.parent.warring("请先选中专业");
        return;
    }

    $.ajax({
        url: config.baseurl +"/api/lev3/getDlvr",
        data: {
            projID: headerData.projectId,
            major: checkSpecialty.value,
            phaseCode: headerData.designPhase
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            createWorkBagList($bu, data);
        },
        complete: function() {
            window.parent.closeLoading();
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });


    // var $td = $bu.parent();
    // if (0 == $td.find("div[name='div_workBag']").length) {
    //     var div = $("<div name='div_workBag' class='panel panel-default' style='margin-top: -50px; position:absolute; right:0; z-index: 999;'></div>");
    //     var div_header = $("<div class='panel-heading'>工作包头</div>");
    //     var div_body = $("<div class='panel-body'>工作包体</div>");
    //
    //     div.append(div_header);
    //     div.append(div_body);
    //     $td.append(div);
    // } else {
    //
    // }
    // $td.find("div[name='div_workBag']").show();
}

function createWorkBagList($bu, data) {
    var element = $(this);
    var id = element.attr('id');
    var txt = element.html();
    $bu.popover({
        trigger: 'manual',
        placement: 'bottom', //top, bottom, left or right
        title: txt,
        html: 'true',
        content: ContentMethod(data, $('#treetableChapter tr').index($bu.parent().parent())),
    });
    $bu.popover('show');
    var popoverId = $bu.attr("aria-describedby");
    $('#' + popoverId).css("max-width", "500px");
    $($('#' + popoverId).find(".popover-content")[0]).css("cssText", "padding: 3px;");
}

function closeWorkBag(button) {
    var td = $(button).parent().parent().parent().parent().parent().parent();
    var jqButtons = $('button', td);
    $(jqButtons[0]).trigger("click");
}

function clearTdWorkBag(bu) {
    var td = $(bu).parent().parent().parent().parent().parent().parent();
    var jqButtons = $('button', td);
    $(jqButtons[0]).attr("id", "bu_bag_0");
    $(jqButtons[0]).html("<i class='fa fa-suitcase' style='margin-right: 5px;'></i>");
    var s = td.next("td");
    s.html(createInput("personIdDesign", "hidden", "0")).append("");
    var j = s.next("td");
    j.html(createInput("personIdProofread", "hidden", "0")).append("");
    var sh = j.next("td");
    sh.html(createInput("personIdAudit", "hidden", "0")).append("");
    var sd = sh.next("td");
    sd.html(createInput("personIdApprove", "hidden", "0")).append("");
    $(jqButtons[0]).trigger("click");
}

function ContentMethod(d, no) {
    var panel = $('<div class="panel panel-default" style="margin-bottom: 3px;"></div>');
    var panel_heading = $('<div class="panel-heading gyl_panel_header" style="padding: 0 0 0 15px;">工作包' +
        ' <div class="pull-right"><button type="button" class="close" style="margin-right: 10px;" onclick="javascript:closeWorkBag(this);"><span>&times;</span></button></div></div>');
    var panel_body = $('<div class="panel-body" style="padding: 5px; min-width: 200px;"></div>');
    var row = $('<div class="row" style="margin: auto; overflow-y:auto; height: 100%; max-height: 250px;"></div>');
    var panel_foot = $('<div class="panel-footer gyl_panel_header" style="padding-left: 5px;"></div>');
    panel.append(panel_heading);

    var table = $('<table class="table table-bordered" style="color: #0a0a0a; cursor: pointer; margin-bottom: 0px;"></table>');
    $(d).each(function (i) {
        var tr = $('<tr></tr>');
        var td = $('<td width="320" class="clickBagWorkTd">' + d[i].name + '/' + d[i].designName + '/' + d[i].checkName + '/' + d[i].reviewName +'</td>'
            + '<td width="40"><a href="#123" onclick="javascript:updateWorkBag(this,' + no + ');" name="a_workBag" data-bagData="' + d[i].id + '|#|' + d[i].name + '|#|' + d[i].startDate + '|#|' + d[i].dueDate + '|#|' + d[i].designID
            + '|#|' + d[i].checkID + '|#|' + d[i].reviewID + '|#|' + d[i].approveID + '|#|' + d[i].workHour + '|#|' + d[i].workCode + '|#|' + d[i].specID + '">修改</td>');

        td.append('<input type="hidden" value="' + d[i].id + '" />');
        td.append('<input type="hidden" value="' + d[i].name + '" />');
        tr.append(td);
        table.append(tr);
    });
    table.on('click', '.clickBagWorkTd', function () {
        var b_td = $(this).parent().parent().parent().parent().parent().parent().parent().parent().parent();
        var tr = $(this);
        var bu = $(b_td).find("button[name=workBag]")[0];
        var jqInputs = $('input', tr.get(0));
        $(bu).attr("id", "bu_bag_" + $(jqInputs[0]).val());
        $(bu).html("<i class='fa fa-suitcase' style='margin-right: 5px;'></i>" + $(jqInputs[1]).val());
        loadPersonWithWorkBag($(this).parent(), b_td);
    });

    panel_foot.append('<button type="button" class="btn btn-default btn-xs" id="addNewWorkBag_' + no + '"  onclick="javascript:openWorkBagModal(' + no + ');"><i class="fa fa-plus"></i>新增工作包</button>' +
        '<div class="pull-right"><button type="button" class="btn btn-default btn-xs" onclick="javascript:clearTdWorkBag(this);">' +
        '<i class="glyphicon glyphicon-remove"></i>清空工作包</button>');
    row.append(table);
    panel_body.append(row);
    panel.append(panel_body);
    panel.append(panel_foot);

    return panel;
}

var workBagModalData = null;
function openWorkBagModal(no) {
    workBagModalData = null;
    initWorkBagModal(no);
}

function initWorkBagModal(no) {
    $('#workBagNo').val(no);
    var checkSpecialty = $('select[name="sel_specialty"]')[no - 1];
    $('#div_bag_zy').html(createBagZySelect(checkSpecialty.value).attr("disabled", true));
    window.parent.showLoading();
    $.getJSON(config.baseurl + "/api/lev3/getDlvrTypeListForTaskType?taskTypeCode=" + headerData.taskType +"&spec=" + checkSpecialty.value.substr(1) + "&phaseID=" + headerData.designPhase, function (data) {
        bagTypeSel_data = data.rows;
        $.getJSON(config.baseurl + "/api/lev3/getAssignmentList?projID=" + headerData.projectId + "&specialty=" + checkSpecialty.value, function (data) {
            window.parent.closeLoading();
            bagPersonSel = null;
            bagPersonSel2 = null;
            bagPersonSel3 = null;
            bagPersonSel4 = null;
            $("#start_finishDateActive").val(workBagModalData ? workBagModalData.startDate : "");
            $("#end_finishDateActive").val(workBagModalData ? workBagModalData.endDate : "");
            $("#bag_workHour").val(workBagModalData ? workBagModalData.workHour : "");
            var zyData = getZyDate(checkSpecialty.value);
            $("#startDateActive").val(zyData.split(",")[0]);
            $("#endDateActive").val(zyData.split(",")[1]);
            if (null == workBagModalData) {
                $("#start_finishDateActive").val($("#startDateActive").val());
                $("#end_finishDateActive").val($("#endDateActive").val());
            }
            ebsUserForWorkBag = data;
            $('#workBagModal').modal("show").css("top", "15%");
        });
    });
}

function getZyDate(specialty) {
    for (var i = 0; i < specialtys.length; i++) {
        if (specialty == specialtys[i].specialty) {
            return specialtys[i].startDate + "," + specialtys[i].endDate;
        }
    }
    return ",";
}

var createBagZySelect = function (val) {
    var select = $("<select id='bag_zy' name='bag_zy' class='selectpicker1 form-control input-sm' placeholder='请选择' style='padding-left: 2px;' data-live-search='true' data-size='8'></select>");
    select.append("<option value=''>请选择</option>");
    for (var i = 0; i < specialtys.length; i++) {
        select.append("<option value='" + specialtys[i].specialty + "'>" + specialtys[i].specialtyName + "</option>")
    }
    select.val(val);
    return select;
}

// 工作包类型
var bagTypeSel_data;
var bagTypeSel;
var createbagTypeSel = function (val, name) {
    if (undefined == bagTypeSel || null == bagTypeSel) {
        var bagTypeSel = $("<select id='"+ name + "' name='" + name + "' class='selectpicker1 form-control input-sm' placeholder='请选择' style='padding-left: 2px;' data-live-search='true' data-size='8'></select>");
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

// 人员
var bagPersonSel;
var bagPersonSel2;
var bagPersonSel3;
var bagPersonSel4;
var createbagPersonSel = function (val, name) {
    if ("bag_designNum" == name) {
        if (undefined == bagPersonSel || null == bagPersonSel) {
            var bagPersonSel = $("<select id='"+ name + "' name='" + name + "' class='selectpicker1 form-control input-sm' placeholder='请选择' style='padding-left: 2px;' data-live-search='true' data-size='8'></select>");
            bagPersonSel.append("<option value=''>请选择</option>");
            var ebsUser = ebsUserForWorkBag.design;
            for (var i = 0; i < ebsUser.length; i++) {
                bagPersonSel.append("<option value='" + ebsUser[i].perId + "'>" + ebsUser[i].perName + "</option>")
            }
            bagPersonSel.val(val);
            return bagPersonSel;
        } else {
            var select = bagPersonSel.clone(true);
            select.val(val);
            return select;
        }
    } else if ("bag_checkNum" == name) {
        if (undefined == bagPersonSel2 || null == bagPersonSel2) {
            var bagPersonSel2 = $("<select id='"+ name + "' name='" + name + "' class='selectpicker1 form-control input-sm' placeholder='请选择' style='padding-left: 2px;' data-live-search='true' data-size='8'></select>");
            bagPersonSel2.append("<option value=''>请选择</option>");
            var ebsUser = ebsUserForWorkBag.check;
            for (var i = 0; i < ebsUser.length; i++) {
                bagPersonSel2.append("<option value='" + ebsUser[i].perId + "'>" + ebsUser[i].perName + "</option>")
            }
            bagPersonSel2.val(val);
            return bagPersonSel2;
        } else {
            var select = bagPersonSel2.clone(true);
            select.val(val);
            return select;
        }
    } else if ("bag_reviewNum" == name) {
        if (undefined == bagPersonSel3 || null == bagPersonSel3) {
            var bagPersonSel3 = $("<select id='"+ name + "' name='" + name + "' class='selectpicker1 form-control input-sm' placeholder='请选择' style='padding-left: 2px;' data-live-search='true' data-size='8'></select>");
            bagPersonSel3.append("<option value=''>请选择</option>");
            var ebsUser = ebsUserForWorkBag.review;
            for (var i = 0; i < ebsUser.length; i++) {
                bagPersonSel3.append("<option value='" + ebsUser[i].perId + "'>" + ebsUser[i].perName + "</option>")
            }
            bagPersonSel3.val(val);
            return bagPersonSel3;
        } else {
            var select = bagPersonSel3.clone(true);
            select.val(val);
            return select;
        }
    } else if ("bag_approveNum" == name) {
        if (undefined == bagPersonSel4 || null == bagPersonSel4) {
            var bagPersonSel4 = $("<select id='"+ name + "' name='" + name + "' class='selectpicker1 form-control input-sm' placeholder='请选择' style='padding-left: 2px;' data-live-search='true' data-size='8'></select>");
            bagPersonSel4.append("<option value=''>请选择</option>");
            var ebsUser = ebsUserForWorkBag.approve;
            for (var i = 0; i < ebsUser.length; i++) {
                bagPersonSel4.append("<option value='" + ebsUser[i].perId + "'>" + ebsUser[i].perName + "</option>")
            }
            bagPersonSel4.val(val);
            return bagPersonSel4;
        } else {
            var select = bagPersonSel4.clone(true);
            select.val(val);
            return select;
        }
    }

}

$('#workBagModal').on('shown.bs.modal', function (e) {
    $('#bag_jd').val(headerData.designPhaseName).attr("disabled", true);
    $('#bag_jdCode').val(headerData.designPhase);
    var r = createbagPersonSel(workBagModalData ? workBagModalData.design : "", "bag_designNum");
    $('#div_bag_ren').html(r);
    if (2 == r.find('option').length) {
        r.find("option:eq(1)").attr("selected",true);
    }
    r.selectpicker('refresh');
    r.selectpicker('show');
    r = createbagPersonSel(workBagModalData ? workBagModalData.check : "", "bag_checkNum");
    $('#div_bag_ren2').html(r);
    if (2 == r.find('option').length) {
        r.find("option:eq(1)").attr("selected",true);
    }
    r.selectpicker('refresh');
    r.selectpicker('show');
    r = createbagPersonSel(workBagModalData ? workBagModalData.review : "", "bag_reviewNum");
    $('#div_bag_ren3').html(r);
    if (2 == r.find('option').length) {
        r.find("option:eq(1)").attr("selected",true);
    }
    r.selectpicker('refresh');
    r.selectpicker('show');
    r = createbagPersonSel(workBagModalData ? workBagModalData.approve : "", "bag_approveNum");
    $('#div_bag_ren4').html(r);
    r.selectpicker('refresh');
    r.selectpicker('show');
    r = createbagTypeSel(workBagModalData ? workBagModalData.workID : "", "bag_type");
    $('#div_bagType').html(r);
    r.selectpicker('refresh');
    r.selectpicker('show');
    $('#datetimepicker3').datetimepicker({
        format: 'YYYY-MM-DD'
    });
    $('#datetimepicker4').datetimepicker({
        format: 'YYYY-MM-DD'
    });
});

function updateWorkBag(a, no) {
    var data = $(a).attr("data-bagData");
    var s = data.split("|#|");
    var o = new Object();
    o.id = s[0];
    o.startDate = s[2];
    o.endDate = s[3];
    o.workHour = s[8];
    o.design = s[4];
    o.check = s[5];
    o.review = s[6];
    o.approve = s[7];
    o.workID = s[9];
    o.specID = s[10];
    workBagModalData = o;
    console.log(workBagModalData);
    initWorkBagModal(no);
}

$('#save_workBag').on('click', function () {
    var projID = headerData.projectId;
    var phaseCode = headerData.designPhase;
    var major = $('#bag_zy').val();
    var dlvrName = $("#bag_type").find("option:selected").text();
    var startDate = $("#start_finishDateActive").val();
    var endDate = $("#end_finishDateActive").val();
    if ("" == startDate || "" == endDate) {
        window.parent.warring("请选择计划起始日期");
        return;
    }
    if (endDate < startDate) {
        window.parent.warring("计划完成日期小于计划开始日期");
        return;
    }
    if (startDate < $('#startDateActive').val() || endDate > $('#endDateActive').val()) {
        window.parent.warring("计划日期不在专业日期之内");
        return;
    }


    var design = $("#bag_designNum").val();
    if ("" == design) {
        window.parent.warring("请选择设计人");
        return;
    }
    var check = $("#bag_checkNum").val();
    if ("" == check) {
        window.parent.warring("请选择校审人");
        return;
    }
    var review = $("#bag_reviewNum").val();
    if ("" == review) {
        window.parent.warring("请选择审核人");
        return;
    }
    var approve = $("#bag_approveNum").val();
    var checkSpecialty = $('select[name="sel_specialty"]')[$('#workBagNo').val() - 1];
    if ("2PM" != checkSpecialty.value) {
        if (check == design) {
            window.parent.warring("校审人与设计人不能为同一人");
            return;
        }
        if (review == check) {
            window.parent.warring("审核人与校审人不能为同一人");
            return;
        }
        if (approve == review) {
            window.parent.warring("审定人与审核人不能为同一人");
            return;
        }
    }

    var workHour = $("#bag_workHour").val();
    if ((/^(\+|-)?\d+$/.test(workHour)) && 0 < workHour) {

    } else {
        window.parent.warring("计划工时请填正整数");
        return;
    }
    var tmp = $("#bag_type").find("option:selected")[0];
    var workCode = $(tmp).attr("data-workCode");
    var workID = $("#bag_type").val();
    if ("" == workID) {
        window.parent.warring("请选择工作包");
        return;
    }

    if (null == workBagModalData) {
        $.ajax({
            url: config.baseurl +"/api/lev3/createDlvr",
            contentType : 'application/x-www-form-urlencoded',
            dataType: "json",
            data: {
                projID: projID,
                phaseCode: phaseCode,
                major: major,
                dlvrName: dlvrName,
                startDate: startDate,
                endDate: endDate,
                design: design,
                check: check,
                review: review,
                approve: approve,
                workHour: workHour,
                workCode: workCode,
                workID: workID
            },
            type: "GET",
            beforeSend: function() {window.parent.showLoading();},
            success: function(data) {
                if ("done" == data.create) {
                    var workBag = $('button[name="workBag"]')[$('#workBagNo').val() - 1];
                    $(workBag).trigger('click');
                    $('#workBagModal').modal("hide");
                    $(workBag).popover('destroy');
                    setTimeout(function () {
                        $(workBag).trigger('click');
                    }, 300);
                } else {
                    window.parent.error(data.MSG);
                }
            },
            complete: function() {window.parent.closeLoading();},
            error: function(jqXHR, textStatus, errorThrown) {
                window.parent.closeLoading();
                window.parent.error(jqXHR.responseText);
                window.parent.error(jqXHR.statusText);
            }
        });
    } else {
        $.ajax({
            url: config.baseurl +"/api/lev3/updateDlvr",
            contentType : 'application/x-www-form-urlencoded',
            dataType: "json",
            data: {
                projID: projID,
                dlvrID: workBagModalData.id,
                major: major,
                startDate: startDate,
                endDate: endDate,
                design: design,
                check: check,
                review: review,
                approve: approve,
                workHour: workHour,
                specID: workBagModalData.specID
            },
            type: "GET",
            beforeSend: function() {window.parent.showLoading();},
            success: function(data) {
                if ("done" == data.update) {
                    var workBag = $('button[name="workBag"]')[$('#workBagNo').val() - 1];
                    $(workBag).trigger('click');
                    $('#workBagModal').modal("hide");
                    $(workBag).popover('destroy');
                    setTimeout(function () {
                        $(workBag).trigger('click');
                    }, 300);
                } else {
                    window.parent.error(data.MSG);
                }
            },
            complete: function() {window.parent.closeLoading();},
            error: function(jqXHR, textStatus, errorThrown) {
                window.parent.closeLoading();
                window.parent.error(jqXHR.responseText);
                window.parent.error(jqXHR.statusText);
            }
        });
    }

});

function loadPersonWithWorkBag(tr, b_td) {
    var jqInputs = $('input', tr.get(0));
    $.ajax({
        url: config.baseurl +"/api/taskRest/getProjectElementPersonList",
        data: {
            elementId: $(jqInputs[0]).val()
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            setNewPerson(data, b_td);
        },
        complete: function() {
            var $divHeader = tr.parent().parent().parent().parent().prev();
            var $button = $divHeader.children().children();
            $button.trigger("click");
            window.parent.closeLoading();
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

function setNewPerson(data, b_td) {
    var designs = data.designs;
    var checks = data.checks;
    var verifys = data.verifys;
    var approves = data.approves;
    var selTr = $('#treetableChapter tr.fancytree-active')[0];
    var tds = $(selTr).find("td");
    // 设置新的设、校、审、审定
    var s = b_td.next("td");
    s.html(createInput("personIdDesign", "hidden", designs[0].personId)).append(designs[0].personName);
    var j = s.next("td");
    j.html(createInput("personIdProofread", "hidden", checks[0].personId)).append(checks[0].personName);
    var sh = j.next("td");
    sh.html(createInput("personIdAudit", "hidden", verifys[0].personId)).append(verifys[0].personName);
    var sd = sh.next("td");
    sd.html(createInput("personIdApprove", "hidden", approves[0].personId)).append(approves[0].personName);

}

function initButtons(v) {
    if (0 == v) {
        initButtons_0();
        reloadForLock = false;
    } else if (1 == v) {
        initButtons_1();
        reloadForLock = false;
    } else {
        reloadForLock = true;
        initButtons_2();
    }
}

function initButtons_0() {
    $('#bu_addback').show();
    $('#bu_checkOut').hide();
    if ("NEW" == headerData.taskStatus) {
        // $('#bu_content').hide();
        // $('#bu_download').hide();
        // $('#bu_download_zip').hide();
        set_button_Show('hide')
    } else {
        // $('#bu_content').show();
        // $('#bu_download').show();
        // $('#bu_download_zip').show();
        set_button_Show('show')
    }
    $('#bu_checkIn').hide();
    $('#bu_batch').hide();
    $('#bu_chapterGroup').hide();
    $('#bu_delChapter').hide();
    $('#bu_authorityChange').hide();
    $('#bu_save').hide();
    $('#bu_cancel').hide();
    $('#bu_release').hide();

}

// 第一次初始化章节
function initButtons_1() {
    $('#bu_addback').show();

    // 只有新建和正在协作的，才显示签出按钮
    if ("WORKING" == headerData.taskStatus || "NEW" == headerData.taskStatus) {
        $('#bu_checkOut').show();
    } else {
        $('#bu_checkOut').hide();
    }

    if ("NEW" == headerData.taskStatus) {
        // $('#bu_content').hide();
        // $('#bu_download').hide();
        // $('#bu_download_zip').hide();
        set_button_Show('hide')
    } else {
        // $('#bu_content').show();
        // $('#bu_download').show();
        // $('#bu_download_zip').show();
        set_button_Show('show')
    }
    $('#bu_checkIn').hide();
    $('#bu_batch').hide();
    $('#bu_chapterGroup').hide();
    $('#bu_delChapter').hide();
    $('#bu_authorityChange').hide();
    $('#bu_save').hide();
    $('#bu_cancel').hide();
    $('#bu_release').hide();
    // $('#bu_audit_js').hide();
    // $('#bu_gsjps').hide();
    // $('#bu_submitAudit').hide();
}
// 检出成功后
function initButtons_2() {
    // 检出后，签出按钮也显示
    $('#bu_checkOut').show();
    $('#bu_checkIn').show();
    $('#bu_batch').show();
    $('#bu_chapterGroup').show();
    $('#bu_delChapter').show();
    $('#bu_authorityChange').show();
    $('#bu_save').show();
    $('#bu_cancel').show();
    $('#bu_release').show();
    if ("NEW" == headerData.taskStatus) {
        // $('#bu_content').hide();
        // $('#bu_download').hide();
        // $('#bu_download_zip').hide();
        set_button_Show('hide')
    } else {
        // $('#bu_content').show();
        // $('#bu_download').show();
        // $('#bu_download_zip').show();
        set_button_Show('show')
    }
    // $('#bu_audit_js').show();
    // $('#bu_gsjps').show();
    // $('#bu_submitAudit').show();

}

var dataSet_specialtyModal = [];
$('#selSpecialtyModal').on('shown.bs.modal', function (e) {
    loadSelectSpecialtyList();
});

var t_specialtyModal;
function loadSelectSpecialtyList() {
    $('#dataTables-selSpecialtys').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-selSpecialty"></table>');

    t_specialtyModal = $('#dataTables-selSpecialty').DataTable({
        "autoWidth": true,
        "data": dataSet_specialtyModal,
        "paging": false,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "<input type='checkbox' name='ch_specialtyAll' id='ch_specialtyAll' onclick='javascript:setSpecialtyModalCheckAll();' />", "data": null},
            {"title": "序号", "data": null},
            {"title": "专业", "data": "specialtyName"}
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
        }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            $(row).children('td').eq(1).attr('style', 'text-align: center;');
        },
        "initComplete": function () {
            $('#ch_specialtyAll').trigger('click');
        }
    });

    $('#dataTables-selSpecialty tbody').on('click', 'td', function () {
        var $tr = $(this).parent("tr");
        if ( $tr.hasClass('dt-select') ) {
            $tr.removeClass('dt-select');
            var check = $tr.find("input[name='ch_specialty']");
            check.prop("checked", false);
        } else {
            $tr.addClass('dt-select');
            var check = $tr.find("input[name='ch_specialty']");
            check.prop("checked", true);
        }
    });

    t_specialtyModal.on( 'order.dt search.dt', function () {
        t_specialtyModal.column(1, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );
        t_specialtyModal.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = "<input type='checkbox' name='ch_specialty' />";
        } );
    } ).draw();
}

$('#bu_content').on('click', function () {
    try {
        var x = new ActiveXObject("ActiveX.ActiveX");
        x.OpenTaskWord(headerData.taskHeaderId, headerData.templateHeaderId, logonUser.userId, logonUser.fullName);
    } catch (e) {
        console.log(e.name + ": " + e.message);
        $('#plugMsg').html(e.name + ": " + e.message);
        $('#plug_inModal').modal("show");
    }
});

$('#bu_download').on('click', function () {
    var url = "/api/taskRest/downloadAllWord?taskHeaderId=" + headerData.taskHeaderId;
    window.open(url);
});

$('#bu_mergeword').on('click', function () {
    var url = config.contextConfig.nbccMergeWordUrl + 'header_id='+ headerData.taskHeaderId;
    window.open(url);
});

$('#bu_editword').on('click', function () {
    var url = config.contextConfig.nbccPageOfficeUrl + 'file_name=' + headerData.taskHeaderId + '_merge.docx&taskHeaderId=' + headerData.taskHeaderId + '&pattern=ViewText';
    window.open(url);
});

function button_isShow() {
    $.ajax({
        url : config.baseurl +"/api/taskRest/checkTaskCreate",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            projectId: $('#proId').val(),
            taskType: $('#taskType').val()
        },
        type:"GET",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(d){
            if (d) {
                if ("NEW" == headerData.taskStatus)
                    set_button_Show('hide')
                else
                    set_button_Show('show')
            } else {
                set_button_Show('hide')
            }
        },
        complete:function(){

        },
        error:function(){
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

function set_button_Show(v) {
    if ('show' == v){
        // $('#bu_content').show();
        // $('#bu_download').show();
        $('#bu_mergeword').show();
        $('#bu_editword').show();
    }
    else {
        // $('#bu_content').hide();
        // $('#bu_download').hide();
        $('#bu_mergeword').hide();
        $('#bu_editword').hide();
    }

}

$('#bu_checkOut').on('click', function () {
    var nodeList = getTreeAllNode();
    if (0 < nodeList.length) {
        $.ajax({
            url : config.baseurl +"/api/taskRest/getSpecialtyForLockList",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                taskHeaderId: headerData.taskHeaderId
            },
            type:"GET",
            beforeSend:function(){
                window.parent.showLoading();
            },
            success:function(data){
                dataSet_specialtyModal = data.dataSource;
                $('#selSpecialtyModal').modal("show");
            },
            complete:function(){
                window.parent.closeLoading();
            },
            error:function(){
                window.parent.error(ajaxError_loadData);
            }
        });
    } else {
        doCheckOut(2);
    }
});

$('#specialty_checkOut').on('click', function () {
    doCheckOut(1);
});

function doCheckOut(v) {
    var nodeList = getTreeAllNode();
    if (1 == v) {
        var chSpecialty = t_specialtyModal.rows( $("input[name='ch_specialty']:checked").parents('tr') ).data();
        nodeList.forEach(function (node) {
            node.setSelected(false);
        });
        var selCount = 0;
        var specialtys = $('select[name="sel_specialty"]');
        $(chSpecialty).each(function (i) {
            var tmpSpecialty = chSpecialty[i].specialty;
            $(specialtys).each(function (i) {
                if (specialtys[i].value == tmpSpecialty) {
                    nodeList[i].setSelected(true);
                    selCount++;
                }
            });
        });
        // 有创建权限，且章节专业为请选择，则签出时默认勾上
        if (createRule) {
            $(specialtys).each(function (i) {
                if ("" == specialtys[i].value) {
                    nodeList[i].setSelected(true);
                    selCount++;
                }
            });
        }

        if (0 == selCount) {
            window.parent.warring("列表中没有对应的分配专业！");
            return;
        } else {
            $('#selSpecialtyModal').modal("hide");
        }
    }

    var tree = $("#treetableChapter").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();
    var checkOut_taskChaperIds = [];

    if (0 == selNodes.length && 0 < nodeList.length) {
        window.parent.warring(valTips_selOne);
        return;
    }

    if (0 < selNodes.length) {
        selNodes.forEach(function(node) {
            checkOut_taskChaperIds.push(node.data.chapterId)
        });
    }

    $.ajax({
        url: config.baseurl +"/api/taskRest/lockTaskchapters",
        data: {
            taskHeaderId: chapter_taskId,
            taskChaperIds: checkOut_taskChaperIds.toString()
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "POST",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            console.log(data);
            if ("0000" == data.returnCode) {
                clearCheckAll();
                loadForLock(2);
            } else {
                window.parent.error(data.returnMsg);
            }
        },
        complete: function() {
            window.parent.closeLoading();
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

$('#bu_checkIn').on('click', function () {
    $('#confirmModal').modal("show");
});

$('#save_checkIn').on('click', function () {
    f_save(1);
});

var unsaveCheckInNo = 0;
$('#unsave_checkIn').on('click', function () {
    unsaveCheckInNo = 1;
    f_checkIn();
});

function valCheckInData() {
    var nodeList = getTreeAllNode();
    var chapterNames = $("input[name='chapterName']");
    var proRoles = $('select[name="sel_proRole"]');
    var specialtys = $('select[name="sel_specialty"]');
    var responsibles = $('select[name="sel_responsible"]');
    var no = 0;
    $(nodeList).each(function(i) {
        if (1 == unsaveCheckInNo && undefined == nodeList[i].data.attribute7) {

        } else {
            if ("1" == nodeList[i].data.editFlag) {
                if ("" == chapterNames[i].value.trim()) {
                    window.parent.warring("章节[" + nodeList[i].getIndexHier() + "]，章节名称未填写！签入失败！");
                    no = 1;
                    return false;
                }
                if ("" == proRoles[i].value) {
                    window.parent.warring("章节[" + nodeList[i].getIndexHier() + "]，项目角色未选择！签入失败！");
                    no = 1;
                    return false;
                }
                if ("" == specialtys[i].value) {
                    window.parent.warring("章节[" + nodeList[i].getIndexHier() + "]，分配专业未选择！签入失败！");
                    no = 1;
                    return false;
                }
                if ("" == responsibles[i].value) {
                    window.parent.warring("章节[" + nodeList[i].getIndexHier() + "]，节点责任人未选择！签入失败！");
                    no = 1;
                    return false;
                }
            }
        }
    });
    unsaveCheckInNo = 0;
    if (0 == no)
        return true;
    else
        return false;
}

function f_checkIn() {
    if (!valCheckInData()) {
        loadForLock(2);
        return false;
    }

    $.ajax({
        url: config.baseurl +"/api/taskRest/unlockTaskchapters",
        data: {
            taskHeaderId: chapter_taskId,
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "POST",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            console.log(data);
            if ("0000" == data.returnCode) {
                window.parent.success("签入成功！");
                $('#confirmModal').modal("hide");
                clearCheckAll();
                loadForLock(1);
            } else {
                window.parent.error(data.returnMsg);
            }
        },
        complete: function() {
            window.parent.closeLoading();
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

function loadForLock(type) {
    $.ajax({
        url: config.baseurl +"/api/taskRest/taskChaptersList",
        data: {
            taskHeaderId: chapter_taskId
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            reloadChapter(chapter_taskId, data.dataSource, type);
        },
        complete: function() {
            window.parent.closeLoading();
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

$('#bu_addChapter').on('click', function () {
    createChapterRowSibling();
});

$('#bu_addChapterChildren').click(function () {
    createChapterRowChildren();
});

$('#bu_delChapter').click(function () {
    delWithEnableFlag = 0;
    var tree = $("#treetableChapter").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();

    if (0 == selNodes.length) {
        window.parent.warring(valTips_selOne);
        return;
    }

    // 检查是不是勾选的都可以删除章节
    var valMsg = "";
    selNodes.forEach(function (node) {
        if (1 != node.data.editFlag) {
            valMsg += "章节[" + node.getIndexHier() + "]，不能删除，您没有权限<br>";
        } else if (1 == node.data.editFlag && !isAddAndDelChapter && 1 == node.getLevel()) {
            valMsg += "章节[" + node.getIndexHier() + "]，不能删除，您没有权限<br>";
        }
    });
    if ("" != valMsg) {
        window.parent.warring(valMsg);
        return;
    }

    var valMsg = "";
    var valFlag = true;
    selNodes.forEach(function(node) {
        if ("FORASSIGN" == node.data.chapterStatus && (null == node.data.templateChapterId || "" == node.data.templateChapterId)) {

        } else {
            valMsg += "章节[" + node.getIndexHier() + "] 不能被删除<br>";
        }
    });
    if (valMsg) {
        valFlag = false;
        window.parent.warring(valMsg);
    }

    if (valFlag) {
        selNodes.forEach(function(node) {
            // while( node.hasChildren() ) {
            //     node.getFirstChild().moveTo(node.parent, "child");
            // }
            node.remove();
        });
        resetTitleNo();
    }
});

function getTreeAllNode() {
    var tree = $("#treetableChapter").fancytree("getTree");
    var nodeList = [];
    tree.visit(function(node){
        nodeList.push(node);
    });
    return nodeList;
}

var delWithEnableFlag = 0;
function resetTitleNo() {
    var nodeList = getTreeAllNode();
    var _newIndex = [];
    if(!$('#show_eff').is(':checked')) {
        var removeArr = [];
        nodeList.forEach(function(node) {
            var flag = (1 == node.data.enableFlag ? true : false);
            if (!flag) {
                if (-1 != delWithEnableFlag) // -1的时候代表第一次打开模态框，不需要保存未启用章节，是需要remove过滤
                    delWithEnableFlag = 1;
                var parentNode;
                if (1 == node.getLevel())
                    parentNode = node;
                else
                    parentNode = node.parent;

                if (0 == node.data.enableFlag && 1 == node.getLevel()) {
                    removeArr.push(node);
                } else {
                    if (0 != parentNode.data.enableFlag)
                        removeArr.push(node);
                }

            }
        });
        removeArr.forEach(function (node) {
            node.remove();
        });
        nodeList = getTreeAllNode();
    }

    nodeList.forEach(function(node) {
        _newIndex.push(node.getIndexHier());
    });

    $("#treetableChapter tbody tr td:nth-child(3)").each(function (index) {
        $(this).html(_newIndex[index]);
    });
    $("#treetableChapter tbody tr td:nth-child(2)").each(function (index) {
        $($(this).find('.fancytree-title')[0]).html(_newIndex[index]);
        if (1 == nodeList[index].getLevel())
            $($(this).find('.fancytree-icon')[0]).css("color", "#2f9199");
        else if (2 == nodeList[index].getLevel())
            $($(this).find('.fancytree-icon')[0]).css("color", "#f19d50");
        else if (3 == nodeList[index].getLevel())
            $($(this).find('.fancytree-icon')[0]).css("color", "#35cd70");
        else if (4 == nodeList[index].getLevel())
            $($(this).find('.fancytree-icon')[0]).css("color", "#9370DB");
    });

}

function createChapterObject(taskHeaderId, attribute12Name) {
    var obj = new Object();
    obj.title = " ";
    obj.folder = false;
    obj.chapterId = "";
    obj.chapterStatus = "FORASSIGN";
    obj.attribute1 = null;
    obj.attribute2 = null;
    obj.attribute3 = null;
    obj.attribute4 = null;
    obj.attribute5 = null;
    obj.attribute6 = null;
    obj.attribute8 = null;
    obj.attribute9 = null;
    obj.attribute11 = null;
    obj.attribute12 = null;
    obj.attribute13 = null;
    obj.attribute14 = null;
    obj.attribute15 = null;
    obj.deleteFlag = 1;
    obj.objectVersionNumber = 1;
    obj.comments = null;
    obj.taskHeaderId = taskHeaderId;
    obj.templateChapterId = "";
    obj.templateParentChapterId = "";
    obj.projElementId = "0";
    obj.personIdDesign = null;
    obj.personIdProofread = null;
    obj.personIdAudit = null;
    obj.personIdApprove = null;
    obj.personIdResponsible = null;
    obj.chapterStatusName = "等待分配";
    obj.editFlag = "1";
    obj.enableFlag = 1;
    obj.attribute12Name = attribute12Name;
    return obj;
}

function createChapterRowSibling() {
    var obj = [
        { title: " ", folder: false, chapterId: "", chapterStatus: "FORASSIGN", attribute1: null, attribute2: null, attribute3: null, attribute4: null
            , attribute5: null, attribute6: null, attribute8: null, attribute9: null, attribute11: null, attribute12: null, attribute13: null
            , attribute14: null, attribute15: null, deleteFlag: 1, objectVersionNumber: 1, comments: null, taskHeaderId: headerData.taskHeaderId
            , templateChapterId: "", templateParentChapterId: "", projElementId: "0", personIdDesign: null, personIdProofread: null
            , personIdAudit: null, personIdApprove: null, personIdResponsible: null, chapterStatusName: "等待分配", editFlag: "1", attribute12Name: logonUser.fullName}
    ];

    var allData = getTreeAllNode();
    if (0 == allData.length) {
        $.ajax({
            url: config.baseurl +"/api/taskRest/getTaskChapterId",
            dataType: "json",
            type: "POST",
            beforeSend: function() {
                window.parent.showLoading();
            },
            success: function(id) {
                var co = createChapterObject(headerData.taskHeaderId, logonUser.fullName);
                co.chapterId = id;
                co.projectRoleId = "";
                co.projectRoleName = "";
                co.specialty = "";
                co.specialtyName = "";
                $("#treetableChapter").fancytree("getRootNode").addChildren(co);
                resetTitleNo();
            },
            complete: function() {
                window.parent.closeLoading();
            },
            error: function() {
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    } else {
        var tree = $("#treetableChapter").fancytree("getTree"),
            selNodes = tree.getSelectedNodes();

        if (0 == selNodes.length) {
            window.parent.warring(valTips_selOne);
            return;
        }

        // 检查是不是勾选的都可以新增章节
        var valMsg = "";
        selNodes.forEach(function (node) {
            if (1 != node.data.editFlag) {
                valMsg += node.getIndexHier() + "，不能新增，您没有权限<br>";
            } else if (1 == node.data.editFlag && !isAddAndDelChapter && 1 == node.getLevel()) {
                valMsg += node.getIndexHier() + "，不能新增，您没有权限<br>";
            }
        });
        if ("" != valMsg) {
            window.parent.warring(valMsg);
            return;
        }

        //doCreateSelectpicker = true;
        selNodes.forEach(function (node) {
            $.ajax({
                url: config.baseurl +"/api/taskRest/getTaskChapterId",
                dataType: "json",
                type: "POST",
                beforeSend: function() {
                    window.parent.showLoading();
                },
                success: function(id) {
                    var co = createChapterObject(headerData.taskHeaderId, logonUser.fullName);
                    co.chapterId = id;
                    co.projectRoleId = node.data.projectRoleId;
                    co.projectRoleName = node.data.projectRoleName;
                    co.specialty = node.data.specialty;
                    co.specialtyName = node.data.specialtyName;
                    co.personIdResponsible = node.data.personIdResponsible;
                    co.personNameResponsible = node.data.personNameResponsible;
                    node.appendSibling(co);
                    fancyTreeExpanded();
                    resetTitleNo();
                },
                complete: function() {
                    window.parent.closeLoading();
                },
                error: function() {
                    window.parent.closeLoading();
                    window.parent.error(ajaxError_loadData);
                }
            });
        });
        //doCreateSelectpicker = false;
    }
}

function createChapterRowChildren() {
    var obj = [
        { title: " ", folder: false, chapterId: "", chapterStatus: "FORASSIGN", attribute1: null, attribute2: null, attribute3: null, attribute4: null
            , attribute5: null, attribute6: null, attribute8: null, attribute9: null, attribute11: null, attribute12: null, attribute13: null
            , attribute14: null, attribute15: null, deleteFlag: 1, objectVersionNumber: 1, comments: null, taskHeaderId: headerData.taskHeaderId
            , templateChapterId: "", templateParentChapterId: "", projElementId: "0", personIdDesign: null, personIdProofread: null
            , personIdAudit: null, personIdApprove: null, personIdResponsible: null, chapterStatusName: "等待分配", editFlag: "1", attribute12Name: logonUser.fullName}
    ];
    var tree = $("#treetableChapter").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();

    if (0 == selNodes.length) {
        window.parent.warring(valTips_selOne);
        return;
    }

    // 检查是不是勾选的都可以新增章节
    var valMsg = "";
    selNodes.forEach(function (node) {
        if (1 != node.data.editFlag) {
            valMsg += node.getIndexHier() + "，不能新增，您没有权限<br>";
        } else if (1 == node.data.editFlag && 1 > node.getLevel()) {
            valMsg += node.getIndexHier() + "，不能新增一级章节，您没有权限<br>";
        }
    });
    if ("" != valMsg) {
        window.parent.warring(valMsg);
        return;
    }

    //doCreateSelectpicker = true;
    selNodes.forEach(function(node) {
        $.ajax({
            url: config.baseurl +"/api/taskRest/getTaskChapterId",
            dataType: "json",
            type: "POST",
            beforeSend: function() {
                window.parent.showLoading();
            },
            success: function(id) {
                var co = createChapterObject(headerData.taskHeaderId, logonUser.fullName);
                co.chapterId = id;
                co.projectRoleId = node.data.projectRoleId;
                co.projectRoleName = node.data.projectRoleName;
                co.specialty = node.data.specialty;
                co.specialtyName = node.data.specialtyName;
                co.personIdResponsible = node.data.personIdResponsible;
                co.personNameResponsible = node.data.personNameResponsible;
                node.addChildren(co);
                fancyTreeExpanded();
                resetTitleNo();
            },
            complete: function() {
                window.parent.closeLoading();
            },
            error: function() {
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    });
    //doCreateSelectpicker = false;

}

$('#bu_authorityChange').on('click', function () {

    var tree = $("#treetableChapter").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();

    if (0 == selNodes.length) {
        window.parent.warring(valTips_selOne);
        return;
    }

    var selCheck = $('#treetableChapter tr td span.glyphicon-check');
    var j_tr = [];
    selCheck.each(function (i) {
        j_tr.push($(selCheck[i]).parent().parent());
    });
    var arr = [];
    $(j_tr).each(function (j) {
        var trNo = $('#treetableChapter tr').index(j_tr[j].get(0));
        var checkSpecialty = $('select[name="sel_specialty"]')[trNo - 1];
        var checkProRole = $('select[name="sel_proRole"]')[trNo - 1];
        var checkResponsible = $('select[name="sel_responsible"]')[trNo - 1];
        var o = new Object();
        o.projectId = headerData.projectId;
        o.roleId = checkProRole.value;
        o.specialty = checkSpecialty.value;
        o.personIdResponsible = checkResponsible.value;
        arr.push(o);
    });

    console.log(JSON.stringify(arr));

    $.ajax({
        url: config.baseurl +"/api/taskRest/getTaskChapterResponsible2List",
        data: JSON.stringify(arr),
        dataType: "json",
        type: "POST",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            console.log(data);
            responsibleDataSet = data;
            $('#responsibleModal').modal("show");
        },
        complete: function() {
            window.parent.closeLoading();
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });


});

var t_responsible;
var responsibleDataSet = [];
function loadResponsibleList() {
    $('#dataTables-responsibles').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-responsible"></table>');

    t_responsible = $('#dataTables-responsible').DataTable({
        "autoWidth": true,
        "data": responsibleDataSet,
        "paging": false,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "", "data": null},
            {"title": "人员", "data": "personNameResponsible"},
            {"title": "角色", "data": "roleName"},
            {"title": "专业", "data": "specialtyName"}
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "targets": 0,
            "width": 32
        },{
            "searchable": false,
            "orderable": false,
            "width": 150,
            "targets": 1
        },{
            "searchable": false,
            "orderable": false,
            "targets": 2,
            "width": 100
        },{
            "searchable": false,
            "orderable": false,
            "targets": 3,
            "width": 100
        }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "initComplete": function () {
            $('#dataTables-responsible_length').remove();
        }
    });

    $('#dataTables-responsible tbody').on('click', 'tr', function () {
        if ( $(this).hasClass('dt-select') ) {
            $(this).removeClass('dt-select');
            var check = $(this).find("input[name='ch_list_responsible']");
            check.prop("checked", false);
        } else {
            $(this).addClass('dt-select');
            var check = $(this).find("input[name='ch_list_responsible']");
            check.prop("checked", true);
        }
    });

    t_responsible.on( 'order.dt search.dt', function () {
        t_responsible.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = "<input type='radio' name='ch_list_responsible' />";
        } );
    } ).draw();


}

$('#responsibleModal').on('shown.bs.modal', function (e) {
    loadResponsibleList();
});

$('#responsible_ok').on('click', function () {
    var arr = [];
    var tmp = t_responsible.rows( $("input[name='ch_list_responsible']:checked").parents('tr') ).data();
    var to = [];
    var person;
    $(tmp).each(function (index) {
        to.push(tmp[index].id);
        // 人员
        person = new Object();
        person.projectId = headerData.projectId;
        person.roleId = tmp[index].roleId;
        person.specialty = tmp[index].specialty;
        person.personIdResponsible = tmp[index].personIdResponsible;
        arr.push(person);
    });
    if (0 == to.length) {
        window.parent.warring(valTips_selOne);
        return;
    }

    var tree = $("#treetableChapter").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();
    var responsible_taskChaperIds = [];

    if (0 == selNodes.length) {
        window.parent.warring(valTips_selOne);
        return;
    }
    selNodes.forEach(function(node) {
        responsible_taskChaperIds.push(node.data.chapterId)
    });

    var o = new Object();
    o.taskHeaderId = chapter_taskId;
    o.taskChaperIds = responsible_taskChaperIds.toString();
    o.taskResponsibleDtos = arr;

    $.ajax({
        url: config.baseurl +"/api/taskRest/updateTaskResponsible",
        data: JSON.stringify(o),
        dataType: "json",
        type: "POST",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            if ("0000" == data.returnCode) {
                window.parent.success("授权成功！");
                $('#responsibleModal').modal("hide");
                clearCheckAll();
                loadForLock(2);
            } else {
                window.parent.error(data.returnMsg);
            }
        },
        complete: function() {
            window.parent.closeLoading();
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
});

//专业检审
$('#bu_audit_js').on('click', function () {
    $.ajax({
        url: config.baseurl +"/api/taskRest/getProjectElementList",
        data: {
            projectId: headerData.projectId,
            taskHeaderId: chapter_taskId
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            auditWorkBagDataSet = data.dataSource;
            $('#auditModal').modal("show");
        },
        complete: function() {
            window.parent.closeLoading();
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
});

$('#auditModal').on('shown.bs.modal', function (e) {
    loadAuditWorkBagList();
});

var t_audit_workBag;
var auditWorkBagDataSet = [];
function loadAuditWorkBagList() {
    $('#dataTables-audits').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-audit"></table>');
    $('#div_auditMadal_chapters').empty();
    $(auditWorkBagDataSet).each(function (i) {
        auditWorkBagDataSet[i].allString = auditWorkBagDataSet[i].specialtyName + '/' + auditWorkBagDataSet[i].elementName
            + '/' + auditWorkBagDataSet[i].personNameDesign + '/' + auditWorkBagDataSet[i].personNameCheck + '/' + auditWorkBagDataSet[i].personNameVerify;
    });

    t_audit_workBag = $('#dataTables-audit').DataTable({
        "autoWidth": true,
        "data": auditWorkBagDataSet,
        "paging": false,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "", "data": null},
            {"title": "工作包", "data": "allString"}
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "targets": 0,
            "width": 32
        }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "initComplete": function () {
            $('#dataTables-audit_length').remove();
        }
    });

    $('#dataTables-audit tbody').on('click', 'tr', function () {
        // if ( $(this).hasClass('dt-select') ) {
        //     $(this).removeClass('dt-select');
        //     var check = $(this).find("input[name='ch_list_audit']");
        //     check.prop("checked", false);
        // } else {
        //     $(this).addClass('dt-select');
        //
        // }
        var check = $(this).find("input[name='ch_list_audit']");
        check.prop("checked", true);
        $('#dataTables-audit').DataTable().$('tr.dt-select').removeClass('dt-select');
        $(this).addClass('dt-select');
        var userTable = $('#dataTables-audit').DataTable();
        var data = userTable.rows($(this)).data();
        reloadAuditModalChapters(data[0].elementId);
    });

    t_audit_workBag.on( 'order.dt search.dt', function () {
        t_audit_workBag.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = "<input type='radio' name='ch_list_audit' />";
        } );
    } ).draw();
}

function reloadAuditModalChapters(elementId) {
    $.ajax({
        url: config.baseurl +"/api/taskRest/taskChaptersList",
        data: {
            taskHeaderId: headerData.taskHeaderId,
            elementId: elementId,
            type: 1,
            listType: 'REPO'
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            var d = data.dataSource;
            $('#div_auditMadal_chapters').empty();
            var html = "";
            $(d).each(function (i) {
                html += d[i].chapterNo + " " + d[i].chapterName + "<br>";
            });
            $('#div_auditMadal_chapters').html(html);
        },
        complete: function() {
            window.parent.closeLoading();
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

//专业检审状态回退
$('#bu_un_audit_js').on('click', function () {
    $.ajax({
        url: config.baseurl +"/api/taskRest/getRollBackProjectElementList",
        data: {
            projectId: headerData.projectId,
            taskHeaderId: chapter_taskId
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            unAuditWorkBagDataSet = data.dataSource;
            $('#unAuditModal').modal("show");
        },
        complete: function() {
            window.parent.closeLoading();
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
});

$('#unAuditModal').on('shown.bs.modal', function (e) {
    loadUnAuditWorkBagList();
});

var t_unAudit_workBag;
var unAuditWorkBagDataSet = [];
function loadUnAuditWorkBagList() {
    $('#dataTables-unAudits').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-unAudit"></table>');
    $('#div_unAuditMadal_chapters').empty();
    $(unAuditWorkBagDataSet).each(function (i) {
        unAuditWorkBagDataSet[i].allString = unAuditWorkBagDataSet[i].specialtyName + '/' + unAuditWorkBagDataSet[i].elementName
            + '/' + unAuditWorkBagDataSet[i].personNameDesign + '/' + unAuditWorkBagDataSet[i].personNameCheck + '/' + unAuditWorkBagDataSet[i].personNameVerify;
    });

    t_unAudit_workBag = $('#dataTables-unAudit').DataTable({
        "autoWidth": true,
        "data": unAuditWorkBagDataSet,
        "paging": false,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "", "data": null},
            {"title": "工作包", "data": "allString"}
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "targets": 0,
            "width": 32
        }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "initComplete": function () {
            $('#dataTables-unAudit_length').remove();
        }
    });

    $('#dataTables-unAudit tbody').on('click', 'tr', function () {
        // if ( $(this).hasClass('dt-select') ) {
        //     $(this).removeClass('dt-select');
        //     var check = $(this).find("input[name='ch_list_audit']");
        //     check.prop("checked", false);
        // } else {
        //     $(this).addClass('dt-select');
        //
        // }
        var check = $(this).find("input[name='ch_list_unAudit']");
        check.prop("checked", true);
        $('#dataTables-unAudit').DataTable().$('tr.dt-select').removeClass('dt-select');
        $(this).addClass('dt-select');
        var userTable = $('#dataTables-unAudit').DataTable();
        var data = userTable.rows($(this)).data();
        reloadUnAuditModalChapters(data[0].elementId);
    });

    t_unAudit_workBag.on( 'order.dt search.dt', function () {
        t_unAudit_workBag.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = "<input type='radio' name='ch_list_unAudit' />";
        } );
    } ).draw();
}

function reloadUnAuditModalChapters(elementId) {
    $.ajax({
        url: config.baseurl +"/api/taskRest/taskChaptersList",
        data: {
            taskHeaderId: headerData.taskHeaderId,
            elementId: elementId,
            type: 1,
            listType: 'REPO'
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            var d = data.dataSource;
            $('#div_unAuditMadal_chapters').empty();
            var html = "";
            $(d).each(function (i) {
                html += d[i].chapterNo + " " + d[i].chapterName + "<br>";
            });
            $('#div_unAuditMadal_chapters').html(html);
        },
        complete: function() {
            window.parent.closeLoading();
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

function loadChaptersForAudit() {
    $.ajax({
        url: config.baseurl +"/api/taskRest/taskHeaderList",
        data: {
            taskHeaderId: headerData.taskHeaderId,
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "POST",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            headerData = data.dataSource[0];
            if ($('#bu_checkOut').is(':hidden'))
                loadForLock(2);
            else
                loadForLock(1);
        },
        complete: function() {
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

$('#audit_ok').on('click', function () {
    var tmp = t_audit_workBag.rows( $("input[name='ch_list_audit']:checked").parents('tr') ).data();

    if (0 == tmp.length) {
        window.parent.warring(valTips_selOne);
        return;
    }
    if (1 < tmp.length) {
        window.parent.warring(valTips_workOne);
        return;
    }

    $.ajax({
        url: config.baseurl +"/api/taskRest/iapprovingTaskChapters",
        contentType : 'application/x-www-form-urlencoded',
        data: {
            taskHeaderId: chapter_taskId,
            elementId: tmp[0].elementId
        },
        dataType: "json",
        type: "POST",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            if ("0000" == data.returnCode) {
                $('#auditModal').modal("hide");
                if(null == data.db_url){
                    // window.open(config.baseurl + data.db_url);
                    // loadChaptersForAudit();
                    window.parent.success("专业校审审批成功！");
                }else {
                    window.open(config.baseurl + data.db_url);
                    // loadChaptersForAudit();
                    window.parent.success("提交校审成功！");
                }
                $('#chapter_modal').modal("hide");
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
            window.parent.error(ajaxError_loadData);
        }
    });
});

$('#unAudit_ok').on('click', function () {
    var tmp = t_unAudit_workBag.rows( $("input[name='ch_list_unAudit']:checked").parents('tr') ).data();

    if (0 == tmp.length) {
        window.parent.warring(valTips_selOne);
        return;
    }
    if (1 < tmp.length) {
        window.parent.warring(valTips_workOne);
        return;
    }

    $.ajax({
        url: config.baseurl +"/api/taskRest/nIapprovedTaskChapters",
        contentType : 'application/x-www-form-urlencoded',
        data: {
            taskHeaderId: chapter_taskId,
            elementId: tmp[0].elementId
        },
        dataType: "json",
        type: "POST",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            if ("0000" == data.returnCode) {
                $('#unAuditModal').modal("hide");
                window.parent.success("专业校审状态回退成功！");
                $('#chapter_modal').modal("hide");
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
            window.parent.error(ajaxError_loadData);
        }
    });
});

$('#bu_submitAudit').on('click', function () {
    $.ajax({
        url: config.baseurl +"/api/taskRest/approvingTaskChapters",
        contentType : 'application/x-www-form-urlencoded',
        data: {
            taskHeaderId: chapter_taskId,
        },
        dataType: "json",
        type: "POST",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            if ("0000" == data.returnCode) {
                window.open(data.db_url);
                // loadChaptersForAudit();
                window.parent.success("提交审批成功！");
                $('#chapter_modal').modal("hide");
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
            window.parent.error(ajaxError_loadData);
        }
    });
});

$('#bu_apply_gsjps').on('click', function () {
    $.ajax({
        url: config.baseurl +"/api/taskRest/applyTaskChapters",
        contentType : 'application/x-www-form-urlencoded',
        data: {
            taskHeaderId: chapter_taskId,
        },
        dataType: "json",
        type: "POST",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            if ("0000" == data.returnCode) {
                window.open(data.db_url);
                // loadChaptersForAudit();
                window.parent.success("公司级评审申请成功！");
                $('#chapter_modal').modal("hide");
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
            window.parent.error(ajaxError_loadData);
        }
    });
});

$('#bu_submit_gsjps').on('click', function () {
    var tree = $("#treetableChapter").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();

    if (0 == selNodes.length) {
        window.parent.warring(valTips_selOne);
        return;
    }
    var chapterIds = [];
    selNodes.forEach(function(node) {
        chapterIds.push(node.data.chapterId);
    });

    $.ajax({
        url: config.baseurl +"/api/taskRest/eapprovingTaskChapters",
        contentType : 'application/x-www-form-urlencoded',
        data: {
            taskHeaderId: chapter_taskId,
            ChapterIds: chapterIds.toString()
        },
        dataType: "json",
        type: "POST",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            if ("0000" == data.returnCode) {
                window.open(data.db_url);
                // loadChaptersForAudit();
                window.parent.success("提交公司级评审成功！");
                $('#chapter_modal').modal("hide");
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
            window.parent.error(ajaxError_loadData);
        }
    });
});

$('#bu_write_gsjps').on('click', function () {
    $.ajax({
        url: config.baseurl +"/api/taskRest/approvingaTaskChapters",
        contentType : 'application/x-www-form-urlencoded',
        data: {
            taskHeaderId: chapter_taskId,
        },
        dataType: "json",
        type: "POST",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            if ("0000" == data.returnCode) {
                window.open(data.db_url);
                // loadChaptersForAudit();
                window.parent.success("提交填写公司级评审结论表成功！");
                $('#chapter_modal').modal("hide");
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
            window.parent.error(ajaxError_loadData);
        }
    });
});

$('#bu_cancel').on('click', function () {
    clearCheckAll();
    loadForLock(1);
});

$('#bu_release').on('click', function () {
    var allNodes = getTreeAllNode();
    allNodes.forEach(function (node) {
        if (null == node.children && 1 == node.data.editFlag)
            node.setSelected(true);
    });

    var tree = $("#treetableChapter").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();

    if (0 == selNodes.length) {
        window.parent.warring(valTips_selOne);
        return;
    }
    $.ajax({
        url: config.baseurl +"/api/taskRest/checkTaskSpecialty",
        contentType : 'application/x-www-form-urlencoded',
        data: {
            taskHeaderId: chapter_taskId
        },
        dataType: "json",
        type: "GET",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            if (0 == data.dataTotal) {
                window.parent.showConfirm(releaseWithCheckIn, "是否确认发布任务？发布后专业负责人或设计人将收到文本任务提醒");
            } else {
                var html = "<div class='panel panel-warning'><div class='panel-heading' style='padding: 5px 0px 5px 15px;'>专业列表</div>"
                            + "<div class='panel-body'>";
                var tmp = data.dataSource;
                $(tmp).each(function (i) {
                    html += tmp[i].specialtyName + "、";
                });
                html = html.substr(0, html.length - 1);
                html += "</div></div>";
                html = "<h4>存在尚未进行章节分配的专业确认要继续吗？</h4><br>" + html;
                window.parent.showConfirm(releaseWithCheckIn, html);
            }
        },
        complete: function() {
            window.parent.closeLoading();
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
});

var releaseWithCheckIn = function () {
    f_save(2);
}

var continueRelease = function() {
    if (!valCheckInData())
        return;

    $.ajax({
        url: config.baseurl +"/api/taskRest/unlockTaskchapters",
        data: {
            taskHeaderId: chapter_taskId,
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "POST",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            console.log(data);
            if ("0000" == data.returnCode) {
                var tree = $("#treetableChapter").fancytree("getTree"),
                    selNodes = tree.getSelectedNodes();

                if (0 == selNodes.length) {
                    window.parent.warring(valTips_selOne);
                    return;
                }
                var chapterIds = [];
                selNodes.forEach(function(node) {
                    chapterIds.push(node.data.chapterId);
                });

                $.ajax({
                    url: config.baseurl +"/api/taskRest/publishTaskChapters",
                    contentType : 'application/x-www-form-urlencoded',
                    data: {
                        taskHeaderId: chapter_taskId,
                        chapterIds: chapterIds.toString()
                    },
                    dataType: "json",
                    type: "POST",
                    beforeSend: function() {
                        window.parent.showLoading();
                    },
                    success: function(data) {
                        if ("0000" == data.returnCode) {
                            clearCheckAll();
                            $.ajax({
                                url: config.baseurl +"/api/taskRest/taskHeaderList",
                                data: {
                                    taskHeaderId: headerData.taskHeaderId,
                                },
                                contentType : 'application/x-www-form-urlencoded',
                                dataType: "json",
                                type: "POST",
                                beforeSend: function() {
                                    window.parent.showLoading();
                                },
                                success: function(data) {
                                    headerData = data.dataSource[0];
                                    loadForLock(1);
                                },
                                complete: function() {
                                },
                                error: function() {
                                    window.parent.closeLoading();
                                    window.parent.error(ajaxError_loadData);
                                }
                            });
                            window.parent.success("发布成功");
                        } else {
                            window.parent.error(data.returnMsg);
                        }
                    },
                    complete: function() {
                        window.parent.closeLoading();
                    },
                    error: function() {
                        window.parent.closeLoading();
                        window.parent.error(ajaxError_loadData);
                    }
                });
            } else {
                window.parent.error(data.returnMsg);
            }
        },
        complete: function() {
            window.parent.closeLoading();
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });

}

$('#bu_save').on('click', function () {
    f_save(0);
});

function f_save(checkIn) {
    var nodeList = getTreeAllNode();
    var chapterNames = $("input[name='chapterName']");
    var proRoles = $('select[name="sel_proRole"]');
    var specialtys = $('select[name="sel_specialty"]');

    var enableFlags = $("input[name='enableFlag']");
    var attribute10s = $("input[name='attribute10']");
    var bus = $('button[name=workBag]');
    var personIdDesigns = $('input[name=personIdDesign]');
    var personIdProofreads = $('input[name=personIdProofread]');
    var personIdAudits = $('input[name=personIdAudit]');
    var personIdApproves = $('input[name=personIdApprove]');

    var responsibles = $('select[name="sel_responsible"]');


    var o;
    var arr = new Array();
    rootData.delTaskChapterIds = delChapterIds.toString();
    rootData.attribute7 = 0;
    arr.push(rootData);
    console.log(nodeList.length);
    $(nodeList).each(function (i) {
        o = new Object();
        o.chapterId = nodeList[i].data.chapterId;
        o.chapterName = chapterNames[i].value;
        o.chapterNo = nodeList[i].getIndexHier();
        if (0 <= o.chapterNo.lastIndexOf("\."))
            o.parentChapterNo = o.chapterNo.substring(0, o.chapterNo.lastIndexOf("\."));
        else
            o.parentChapterNo = "-1";

        o.projectRoleId = proRoles[i].value;
        o.specialty = specialtys[i].value;

        o.enableFlag = enableFlags[i].checked ? "1" : "0";
        nodeList[i].data.enableFlag = enableFlags[i].checked ? "1" : "0";
        o.attribute10 = attribute10s[i].checked ? "1" : "0";
        var projElementId = bus[i].id;
        o.projElementId = projElementId.substring(projElementId.lastIndexOf("_") + 1, projElementId.length);
        o.personIdDesign = personIdDesigns[i].value;
        o.personIdProofread = personIdProofreads[i].value;
        o.personIdAudit = personIdAudits[i].value;
        o.personIdApprove = personIdApproves[i].value;
        o.personIdResponsible = responsibles[i].value;
        // 原有值
        o.taskHeaderId = nodeList[i].data.taskHeaderId;
        o.templateChapterId = nodeList[i].data.templateChapterId;
        o.templateParentChapterId = nodeList[i].data.templateParentChapterId;
        o.comments = nodeList[i].data.comments;
        o.objectVersionNumber = nodeList[i].data.objectVersionNumber;
        o.attributeCategory = nodeList[i].data.attributeCategory;
        o.attribute1 = nodeList[i].data.attribute1;
        o.attribute2 = nodeList[i].data.attribute2;
        o.attribute3 = nodeList[i].data.attribute3;
        o.attribute4 = nodeList[i].data.attribute4;
        o.attribute5 = nodeList[i].data.attribute5;
        o.attribute6 = nodeList[i].data.attribute6;
        o.attribute8 = nodeList[i].data.attribute8;
        o.attribute9 = nodeList[i].data.attribute9;
        o.attribute11 = nodeList[i].data.attribute11;
        o.attribute12 = nodeList[i].data.attribute12;
        o.attribute13 = nodeList[i].data.attribute13;
        o.attribute14 = nodeList[i].data.attribute14;
        o.attribute14 = nodeList[i].data.attribute14;
        o.attribute15 = nodeList[i].data.attribute15;
        o.deleteFlag = nodeList[i].data.deleteFlag;
        o.chapterStatus = nodeList[i].data.chapterStatus;
        o.chapterNumber = nodeList[i].data.chapterNumber;

        // 排序
        o.attribute7 = (i + 1);
        arr.push(o);
    });

    // 重新设置pid
    for (var j = 1; j < arr.length; j++) {
        if ("-1" == arr[j].parentChapterNo)
            arr[j].parentChapterId = arr[0].chapterId;
        else {
            innerloop: for (var k = 1; k < arr.length; k++) {
                if (arr[j].parentChapterNo == arr[k].chapterNo) {
                    arr[j].parentChapterId = arr[k].chapterId;
                    break innerloop;
                }
            }
        }
    }

    // 封面数据
    if (null != taskCover) {
        o = new Object();
        o.chapterId = taskCover.chapterId;
        o.chapterName = taskCover.chapterName;
        o.chapterNo = taskCover.chapterNo;
        o.parentChapterNo = taskCover.parentChapterNo;
        o.projectRoleId = $('select[name="sel_proRole_cover"]')[0].value;
        o.specialty = $('select[name="sel_specialty_cover"]')[0].value;
        o.enableFlag = taskCover.enableFlag;
        o.attribute10 = taskCover.attribute10;
        o.projElementId = taskCover.projElementId;
        o.personIdDesign = taskCover.personIdDesign;
        o.personIdProofread = taskCover.personIdProofread;
        o.personIdAudit = taskCover.personIdAudit;
        o.personIdApprove = taskCover.personIdApprove;
        o.personIdResponsible = $('select[name="sel_responsible_cover"]')[0].value;
        o.taskHeaderId = taskCover.taskHeaderId;
        o.templateChapterId = taskCover.templateChapterId;
        o.templateParentChapterId = taskCover.templateParentChapterId;
        o.comments = taskCover.comments;
        o.objectVersionNumber = taskCover.objectVersionNumber;
        o.attributeCategory = taskCover.attributeCategory;
        o.attribute2 = taskCover.attribute2;
        o.attribute3 = taskCover.attribute3;
        o.attribute4 = taskCover.attribute4;
        o.attribute5 = taskCover.attribute5;
        o.attribute6 = taskCover.attribute6;
        o.attribute8 = taskCover.attribute8;
        o.attribute9 = taskCover.attribute9;
        o.attribute11 = taskCover.attribute11;
        o.attribute12 = taskCover.attribute12;
        o.attribute13 = taskCover.attribute13;
        o.attribute14 = taskCover.attribute14;
        o.attribute14 = taskCover.attribute14;
        o.attribute15 = taskCover.attribute15;
        o.deleteFlag = taskCover.deleteFlag;
        o.chapterStatus = taskCover.chapterStatus;
        o.chapterNumber = taskCover.chapterNumber;
        o.attribute7 = arr.length;
        o.attribute1 = "COVER";
        o.parentChapterId = taskCover.parentChapterId;
        arr.push(o);
    }
    console.log(JSON.stringify(arr));
    // return;
    $.ajax({
        url: config.baseurl +"/api/taskRest/saveTaskChapter",
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: JSON.stringify(arr),
        type: "POST",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            if ("0000" == data.returnCode) {
                if (0 < delChapterIds.length)
                    delChapterIds = [];
                if (1 == checkIn) {
                    f_checkIn();
                } else if (2 == checkIn) {
                    continueRelease();
                } else {
                    // clearCheckAll();
                    //loadForLock(2); // 2检出查询，1是检入查询
                }
                window.parent.success(tips_saveSuccess);
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

function hrefClick(url,suffix) {
    // url,suffix
    // if (".rtf" == suffix)
        $.ajax({
            url: config.baseurl +"/api/taskRest/autoCreateWordWithXml",
            data: {
                taskHeaderId: chapter_taskId,
            },
            contentType : 'application/x-www-form-urlencoded',
            dataType: "json",
            type: "POST",
            beforeSend: function() {
                window.parent.showLoading();
            },
            success: function(d) {
                console.log(d);
                window.open(url)
            },
            complete: function() {
                window.parent.closeLoading();
            },
            error: function() {
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    // else
    //     window.open(url);
}

function _windowOpen(url) {
        window.open(url);
}

function _windowNoLocation(url,resizable,size){
     if(!size) var size = [screen.width, screen.height];
    var has_showModeless = !!window.showModelessDialog;//定义一个全局变量判定是否有原生showModalDialog方法
    if(!has_showModeless){
        var has_showModal = !!window.showModalDialog;
        if(!has_showModal){
            window.open(url,'_blank','location=no');//console.log(size?size[0]:"nosize");
        }else window.showModalDialog(url,'','resizable:'+resizable+';dialogWidth:'+size[0]+'px;dialogHeight:'+size[1]+'px');
    }else
    window.showModelessDialog(url,'','resizable:'+resizable+';dialogWidth:'+size[0]+'px;dialogHeight:'+size[1]+'px');
}
