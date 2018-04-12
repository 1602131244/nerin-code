/**
 * Created by Zach on 16/5/23.
 */
var config = new nerinJsConfig();
var queryBars;
var queryBars_2;
var sourceTemplateId = "";
var sourceTaskId = "";
// 任务头信息
var headerData;
var tmp_initButton = 1;
// 登录人员
var logonUser;
// 是否已选中职责
var userDuty;
// var isoa;

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

$(document).keyup(function(event){
    switch(event.keyCode) {
        case 13:
            $('#bu_queryData').trigger("click");
    }
});

function loadUserDuty() {
    window.parent.showLoading();
    $.getJSON(config.baseurl + "/api/ebsDuty/getUserEbsDutySource?identity=A", function (data) {
        window.parent.closeLoading();
        if (1 == data.length) {
            userDuty = data[0].responsibilityKey;
            $('#respKey').val(userDuty);
        } else {
            dataSet_viewDuty = data;
            $('#viewDutyModal').modal("show");
        }
    });
}

$(function () {
    function doAutoWork() {
        var taskName = $.query.get("taskName");
        var proName = $.query.get("proName");
        var taskHeaderId = $.query.get("taskHeaderId");
        // isoa = $.query.get("isoa");
        if ("" != taskName && "create" == taskName) {
            $('#bu_add').trigger("click");
        } else if ("" != taskHeaderId) {  //"" != taskName && "" != taskHeaderId
            $('input[name="taskName"]').val(taskName);
            // $('#bu_queryData').trigger("click");
            $.ajax({
                url : config.baseurl +"/api/taskRest/taskHeaderList",
                contentType : 'application/x-www-form-urlencoded',
                dataType:"json",
                data: {
                    taskHeaderId: taskHeaderId
                    // ,isoa: isoa
                },
                type:"POST",   //请求方式
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
                    window.parent.error(ajaxError_loadData);
                }
            });
        } else if ("" != proName) {
            $('#projSearch').val(proName.split("|,|")[0]);
            $('#bu_queryData').trigger("click");
        }
    }

    //加载职责列表
    if ("" == $.query.get("taskName"))
        loadUserDuty();

    $('#bu_resetQuery').on('click', function () {
        if ($('#b_taskTypeAll').hasClass('active')) {
            $('#b_taskTypeAll').trigger("click");
        } else {
            $('#b_taskTypeAll').trigger("click");
            $('#b_taskTypeAll').trigger("click");
        }

        if ($('#b_stageAll').hasClass('active')) {
            $('#b_stageAll').trigger("click");
        } else {
            $('#b_stageAll').trigger("click");
            $('#b_stageAll').trigger("click");
        }
    });

    var dataSet = [];
    queryList();
    initQueryBars();

    // $.getJSON(config.baseurl + "/api/templateRest/projectRoleList", function (data) {
    //     projectRoles = data.dataSource;
    // });
    // $.getJSON(config.baseurl + "/api/templateRest/specialtyList", function (data) {
    //     specialtys = data.dataSource;
    // });

    function clearDesignPhase() {
        $('#designPhase').empty();
        var initValue = "";
        var initText = "请选择";
        $('#designPhase').append("<option value='" + initValue + "'>" + initText + "</option>");
    }

    clearDesignPhase();
    $(".selectpickerPhase").selectpicker('refresh');
    $(".selectpickerPhase").selectpicker('show');
    //
    var $div = $('#designPhase').parent();
    var $button = $div.find("button");
    var button = $button[0];
    var $span = $(button).children().eq(0);
    $span.css("marginLeft", "10px");
    
    $('#designPhase').on('change', function () {
        reload_sel_TaskType("");
    });

    function reload_sel_TaskType(v) {
        $.getJSON(config.baseurl + "/api/templateRest/taskTypeList?projectPhase=" + $('#designPhase').val(), function (data) {
            var list = data.dataSource;
            $('#taskType').empty();
            var initValue = "";
            var initText = "请选择";
            $('#taskType').append("<option value='" + initValue + "'>" + initText + "</option>");
            list.forEach(function (data) {
                $('#taskType').append("<option value='" + data.taskType + "'>" + data.taskTypeName + "</option>");
            });
            $('#taskType').val(v);
        });
    }

    $('#taskType').on('change', function () {
        setTaskName();
        var _index = $('label[name="l_tag"]');
        if ($(_index[0]).hasClass("active"))
            loadChapter_templateList();
        else {
            loadHistoryTaskList();
        }
    });

    function initQueryBars(val) {
        window.parent.showLoading();
        $.getJSON(config.baseurl + "/api/taskTypeSetRest/userTaskTypeList", function (data) {
            queryBars = data.dataSource;
            var queryBarsHtml = "<div class='btn-group' data-toggle='buttons'><label class='btn btn-primary2 btn-xs' style='margin-bottom: 2px;' id='b_taskTypeAll'><input type='checkbox' name='b_taskTypeAll' value='all'>全选</label>";
            queryBars.forEach(function (data) {
                queryBarsHtml += "<label name='l_taskType' class='btn btn-primary2 btn-xs' style='margin-bottom: 2px;'><input type='checkbox' name='taskTypes' value='" + data.lookupCode + "'>" + data.meaning + "</label>";
            });
            queryBarsHtml += " </div>";
            $('#div_taskType').append(queryBarsHtml);

            $('#b_taskTypeAll').on('click', function () {
                if (0 == $("input[name='b_taskTypeAll']:checked").length) {
                    $("label[name='l_taskType']").each(function () {
                        if (!$(this).hasClass('active')) {
                            $(this).trigger("click");
                        } else {

                        }
                    });
                } else {
                    $("label[name='l_taskType']").each(function () {
                        if ($(this).hasClass('active')) {
                            $(this).trigger("click");
                        } else {

                        }
                    });
                }
            });
        });

        $.getJSON(config.baseurl + "/api/taskTypeSetRest/projectPhaseCategor", function (data) {
            queryBars_2 = data.dataSource;
            var queryBarsHtml = "<div class='btn-group' data-toggle='buttons'><label class='btn btn-primary2 btn-xs' style='margin-bottom: 2px;' id='b_stageAll'><input type='checkbox' name='b_stageAll' value='all'>全选</label>";
            queryBars_2.forEach(function (data) {
                queryBarsHtml += "<label name='l_stage' class='btn btn-primary2 btn-xs' style='margin-bottom: 2px;'><input type='checkbox' name='designPhase' value='" + data.phaseCategorCode + "'>" + data.phaseCategorName + "</label>";
            });
            queryBarsHtml += " </div>";
            $('#div_stage').append(queryBarsHtml);

            $('#b_stageAll').on('click', function () {
                if (0 == $("input[name='b_stageAll']:checked").length) {
                    $("label[name='l_stage']").each(function () {
                        if (!$(this).hasClass('active')) {
                            $(this).trigger("click");
                        } else {

                        }
                    });
                } else {
                    $("label[name='l_stage']").each(function () {
                        if ($(this).hasClass('active')) {
                            $(this).trigger("click");
                        } else {

                        }
                    });
                }
            });
        });
        window.parent.closeLoading();
    }


    var t;
    function queryList() {
        $('#dataTables-listAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-data"></table>');

        t = $('#dataTables-data').DataTable({
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
                {"title": "文本名称", "data": "taskName"},
                {"title": "项目简称", "data": "projectName"},
                {"title": "项目经理", "data": "projectManagerName"},
                {"title": "阶段", "data": "designPhaseName"},
                {"title": "我的角色", "data": "projectRoleName"},
                {"title": "文本类型", "data": "taskTypeName"},
                {"title": "章节完成情况", "data": "taskProgress"},
                {"title": "专业检审完成情况", "data": "taskProgress2"},
                {"title": "创建人", "data": "createByName"},
                {"title": "状态", "data": "taskStatusName"},
                {"title": "项目编号", "data": "projectNumber"},
                {"title": "是否共享", "data": "attribute10"}
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
                "width": 200,
                "targets": 2
                // "render": function (data, type, full, meta) {
                //     return "<a name='a_linkTask' href='#1'>" + data + "</a>";
                // }
            },{
                "width": 70,
                "targets": 12
            },{
                "width": 200,
                "targets": 3
            },{
                "width": 65,
                "targets": 4
            },{
                "width": 95,
                "targets": 5
            },{
                "width": 65,
                "targets": 6
            },{
                "width": 125,
                "targets": 7
            },{
                "width": 90,
                "targets": 8
            },{
                "width": 115,
                "targets": 9
            },{
                "width": 50,
                "targets": 10
            },{
                "width": 85,
                "targets": 11
            },{
                "width": 65,
                "targets": 13,
                "render": function (data, type, full, meta) {
                    if ("1" == data)
                        return "是";
                    else
                        return "否";
                }
            }
            ],
            "order": [],
            "createdRow": function( row, data, dataIndex ) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
                $(row).children('td').eq(1).attr('style', 'text-align: center;');
            },
            // fixedColumns: {
            //     leftColumns: 4
            // },
            "initComplete": function () {
                $('#dataTables-data_wrapper').css("cssText", "margin-top: -5px;");
                $('#dataTables-data_length').insertBefore($('#dataTables-data_info'));
                $('#dataTables-data_length').addClass("col-sm-4");
                $('#dataTables-data_length').css("paddingLeft", "0px");
                $('#dataTables-data_length').css("paddingTop", "5px");
                $('#dataTables-data_length').css("maxWidth", "130px");
                // if ("" != $.query.get("taskName")) {
                    var trs = $('#dataTables-data tbody tr');
                    if (1 == trs.length && 1 < $(trs[0]).find("td").length) {
                        $($(trs[0]).find("td")[0]).trigger("click");
                        $('#bu_planChapter').trigger("click");
                    }
                // }
                t.page(now_pageNo).draw('page');
                now_pageNo = 0;
            }
        });

        $('#dataTables-data tbody').on('click', 'a[name=a_linkTask]', function () {
            var userTable = $('#dataTables-data').DataTable();
            var data = userTable.rows( $(this).parents('tr') ).data();
            // 只读-按钮控制
            tmp_initButton = 0;
            loadHeader(data[0].taskHeaderId, 0);
        });

        $('#dataTables-data tbody').on('click', 'td', function () {
            if ($('#multiSelectCheck').is(':checked')){
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
            }else{
                var $tr = $(this).parent("tr");
                if ( $tr.hasClass('dt-select') ) {
                    $tr.removeClass('dt-select');
                    var check = $tr.find("input[name='ch_list']");
                    check.prop("checked", false);
                } else {
                    var check = $('#dataTables-data').DataTable().$('tr.dt-select').find("input[name='ch_list']");
                    check.prop("checked", false);
                    $('#dataTables-data').DataTable().$('tr.dt-select').removeClass('dt-select');
                    $tr.addClass('dt-select');
                    var check = $tr.find("input[name='ch_list']");
                    check.prop("checked", true);
                }
            }
        });

        $('#dataTables-data tbody').on('dblclick', 'td', function () {
                var check = $('#dataTables-data').DataTable().$('tr.dt-select').find("input[name='ch_list']");
                check.prop("checked", false);
                $('#dataTables-data').DataTable().$('tr.dt-select').removeClass('dt-select');
                var $tr = $(this).parent("tr");
                $tr.addClass('dt-select');
                var check = $tr.find("input[name='ch_list']");
                check.prop("checked", true);

                $('#bu_planChapter').trigger("click");
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
        // taskHeaderId = null;
        // isoa = 0;
        $.ajax({
            url : config.baseurl +"/api/taskRest/taskHeaderList",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: $('#selectFrom').serialize(),
            type:"POST",   //请求方式
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



    $('#bu_add').on('click', function () {
        headerData = null;
        initAddOrUpdateModal();
    });

    function loadChapter_templateList() {
        $.ajax({
            url : config.baseurl +"/api/templateRest/templateHeaderList",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",   //返回格式为json
            data: {
                templateStatus: "EFFECTIVE",
                taskTypes: $('#taskType').val(),
                templateHeaderId: tmp_templateId
            },
            type:"POST",   //请求方式
            beforeSend:function(){
                window.parent.showLoading();

            },
            success:function(data){
                sourceTemplateId = "";
                sourceTaskId = "";
                clearTreeReview();
                dataSet2 = data.dataSource;
                queryList2(headerData ? true : false);
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


    var tmp_templateId = "";
    var tmp_taskHeaderId = "";
    function initAddOrUpdateModal() {
        tmp_templateId = "";
        tmp_taskHeaderId = "";

        dataSet2 = [];
        dataSet3 = [];
        clearTreeReview();

        initTaskNoteSele();

        $(".selectpicker").selectpicker('hide');

        $('#taskId').val(headerData ? headerData.taskHeaderId : "");
        $('#proId').val(headerData ? headerData.projectId : "");
        $('#pro').val(headerData ? headerData.projectName : "");
        $('#proFullName').val(headerData ? headerData.projectNumber + "," + $('#pro').val() + "," + headerData.projectManagerName : "");
        $('#taskName').val(headerData ? headerData.taskName : "");
        $("#taskNoteSele").val(headerData ? headerData.attribute1 : "");

        if (headerData) {
            $.getJSON(config.baseurl + "/api/taskTypeSetRest/projectPhase?projectId=" + $('#proId').val(), function (data) {
                clearDesignPhase();
                var list = data.dataSource;
                list.forEach(function (d) {
                    $('#designPhase').append("<option value='" + d.phasesCode + "'>" + d.phasesName + "</option>");
                });
                $('#designPhase').val(headerData ? headerData.designPhase : "");
                $(".selectpickerPhase").selectpicker('refresh');
                reload_sel_TaskType(headerData ? headerData.taskType : "");
            });
        } else {
            reload_sel_TaskType(headerData ? headerData.taskType : "");
        }

        var attribute11 = headerData ? headerData.attribute11 : "TEMPLATE";
        var tagNo = 0;
        if ("TEMPLATE" == attribute11) {
            tmp_templateId = headerData ? headerData.templateHeaderId : "";
            tagNo = 0;
        } else {
            tmp_taskHeaderId = headerData ? headerData.taskHeaderId : "";
            tagNo = 1;
        }

        if (headerData) {
            $('#addChapterLabel').html(headerData.taskName + "（" + headerData.projectNumber + "）" + headerData.designPhaseName + "---" + headerData.projectRoleName + "（" + logonUser.fullName + "）");
            $("label[name='l_tag']").each(function (i) {
                $(this).attr("disabled", "disabled");
                if (i == tagNo) {
                    if (!$(this).hasClass('active'))
                        $(this).addClass("active");
                } else {
                    if ($(this).hasClass('active'))
                        $(this).removeClass("active");
                }
            });
            $('#pro').attr("disabled", "disabled");
            $('#b_searchPro').attr("disabled", "disabled");
            $('#designPhase').attr("disabled", "disabled");
            $(".selectpickerPhase").selectpicker('refresh');
            $('#taskType').attr("disabled", "disabled");
            $('#bus_l_tag').show();
        } else {
            clearDesignPhase();
            $('#taskName').removeAttr("disabled");
            $('#tag1').val("");
            $('#add_modal').modal('show');
            $('#pro').removeAttr("disabled");
            $('#b_searchPro').removeAttr("disabled");
            $('#designPhase').removeAttr("disabled");
            $(".selectpickerPhase").selectpicker('refresh');
            $('#taskType').removeAttr("disabled");
            $('#bus_l_tag').hide();
            $("label[name='l_tag']").each(function (i) {
                $(this).removeAttr("disabled");
            });
            // 如果来自导航
            var proNameTmp = $.query.get("proName");
            $.ajax({
                url : config.baseurl +"/api/taskRest/projectList",
                contentType : 'application/x-www-form-urlencoded',
                dataType:"json",
                data: {
                    projectSearch: proNameTmp.split("|,|")[0]
                },
                type:"GET",
                beforeSend:function(){
                },
                success:function(data){
                    if (1 == data.dataTotal) {
                        var tmp =  data.dataSource;
                        $('#proId').val(tmp[0].projectId);
                        $('#pro').val(tmp[0].projectName);
                        $('#proFullName').val(tmp[0].projectNumber + "," + tmp[0].projectName + "," + tmp[0].projectManager);
                        $.getJSON(config.baseurl + "/api/taskTypeSetRest/projectPhase?projectId=" + $('#proId').val(), function (data) {
                            clearDesignPhase();
                            var list = data.dataSource;
                            list.forEach(function (d) {
                                $('#designPhase').append("<option value='" + d.phasesCode + "'>" + d.phasesName + "</option>");
                            });
                            var phaseNo = proNameTmp.split("|,|")[1];
                            if (1 == phaseNo)
                                $('#designPhase option:nth-child(2)').attr("selected" , "selected");
                            else
                                $('#designPhase').val(phaseNo);
                            $(".selectpickerPhase").selectpicker('refresh');
                            reload_sel_TaskType("");
                        });
                    }
                },
                complete:function(){},
                error:function(){}
            });
        }

    }

    $('#add_modal').on('shown.bs.modal', function (e) {
        if (headerData) {
            $("label[name='l_tag']").each(function (i) {
                if ($(this).hasClass('active'))
                    $(this).trigger("click");
            });
            // if ("TEMPLATE" == headerData.attribute11) {
            //     queryList2(1);
            // } else {
            //     initHistoryTaskList(1);
            // }
        } else {
            queryList2();
        }

    });

    var templateTreeData = [];
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

    $("#treetable").fancytree({
        extensions: ["dnd", "edit", "glyph", "table"],
        checkbox: false,
        keyboard: false,
        dnd: {
            focusOnClick: true
        },
        glyph: glyph_opts,
        source: templateTreeData,
        table: {
            nodeColumnIdx: 0
        },
        select: function(event, data) {
            var selNodes = data.tree.getSelectedNodes();
            var selKeys = $.map(selNodes, function(node){
                return "[" + node.key + "]: '" + node.title + "'";
            });
        },
        renderColumns: function(event, data) {
            var node = data.node,
                $tdList = $(node.tr).find(">td");
            //$tdList.eq(1).text(node.getIndexHier());
            $tdList.eq(1).text(node.data.chapterName);
            $tdList.eq(2).text(node.data.projectRoleName);
            $tdList.eq(3).text(node.data.specialtyName);


            var _index = $('label[name="l_tag"]');
            if (_index[0].className.indexOf('active') != -1){ //模板
                if (null != node.data.xdoTemplateId)
                    var suffix = ".rtf";
                else
                    var suffix = ".docx";

                if (null == node.data.templateChapterFileName || "" == node.data.templateChapterFileName)  //文件名称不为空则为末级节点
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
                    // if ('E' == node.data.wordFlag)
                    //     var viewType = '&pattern=ViewText';
                    // else
                    var viewType = '&pattern=ViewText';

                    // var userID = new logonUser;
                    var PageeOfficeUrl = config.baseurl + '/pages/wbsp/code/poweb/poweb.html?source=TEMPLATE&chapterId=' + node.data.chapterId + '&taskHeaderId=' + node.data.templateHeaderId + '&isoa=0';
                    var lstyle ='';
                }
                if (undefined == node.data.chapterId)
                    disabledFlag = 'Y';
                var ldisable = ('Y' == disabledFlag ? "disabled = 'disabled'" : "");
                $tdList.eq(4).html('<div align="center"><button type="button" class="btn btn-default" style="padding: 5px 15px;border:none;" id="bu_editChapterword" onclick="_windowOpen(&quot;' + PageeOfficeUrl + '&quot;)"' + ldisable + '> <i class="glyphicon glyphicon-pencil" ></i></button></div>');

            }else{//任务
                if (null != node.data.xmlCode)
                    var suffix = ".rtf";
                else
                    var suffix = ".docx";

                if (node.data.isLeafNode == 0)  //非叶子节点
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
                    var PageeOfficeUrl = config.baseurl + '/pages/wbsp/code/poweb/poweb.html?source=TASK&chapterId=' + node.data.chapterId + '&taskHeaderId=' + node.data.taskHeaderId + '&isoa=1';
                    var lstyle ='';
                }

                if (undefined == node.data.chapterId)
                    disabledFlag = 'Y';
                var ldisable = ('Y' == disabledFlag ? "disabled = 'disabled'" : "");
                $tdList.eq(4).html('<div align="center"><button type="button" class="btn btn-default" style="padding: 5px 15px;border:none;" id="bu_editChapterword" onclick="_windowOpen(&quot;' + PageeOfficeUrl + '&quot;)"' + ldisable + '> <i class="glyphicon glyphicon-pencil" ></i></button></div>');
                null;
            }



        },
        removeNode: function (event, data) { // 删除node触发

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

    doAutoWork();

    function loadHistoryTaskList() {
        $.ajax({
            url : config.baseurl +"/api/taskRest/taskHeaderHistoryList",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",   //返回格式为json
            data: {
                taskType: $('#taskType').val(),
                taskHeaderId: tmp_taskHeaderId
            },
            type:"GET",   //请求方式
            beforeSend:function(){
                window.parent.showLoading();
            },
            success:function(data){
                sourceTemplateId = "";
                sourceTaskId = "";
                clearTreeReview();
                dataSet3 = data.dataSource;
                $('#dataTables-listAll2').hide();
                $('#dataTables-listAll3').show(300);
                initHistoryTaskList(headerData ? true : false);
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



    $('label[name="l_tag"]').on('click', function () {
        var _index = $('label[name="l_tag"]').index($(this));
        if (0 == _index){
            $('#dataTables-listAll3').hide();
            $('#dataTables-listAll2').show(300, function () {
                loadChapter_templateList();
            });
            
            // $('#taskNoteSele').attr('disabled',"disabled");
            // $("#taskNoteSele").val(0);
        } else {
            $('#dataTables-listAll2').hide();
            $('#dataTables-listAll3').show(300, function () {
                loadHistoryTaskList();
            });
            // if (0 == $('#dataTables-listAll3').find('#dataTables-data3').length) {
            //     loadHistoryTaskList();
            // } else {
            //     loadHistoryTaskList();
            //     $('#dataTables-listAll2').hide();
            //     $('#dataTables-listAll3').show(300);
            // }
            
            // $('#taskNoteSele').removeAttr('disabled');
            // $("#taskNoteSele").val(0);
        }

    });

    $('#b_searchPro').on('click', function (e) {
        $.ajax({
            url : config.baseurl +"/api/taskRest/projectList",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                projectSearch: $('#pro').val()
            },
            type:"GET",
            beforeSend:function(){
            },
            success:function(data){
                initProMenu(data.dataSource);
            },
            complete:function(){
            },
            error:function(){
                window.parent.error(ajaxError_loadData);
            }
        });
    });

    $('.selectpicker').on('hidden.bs.select', function (e) {
       $('#proId').val($('#sel_pro').val().split(",")[0]);
       var sTest = $('#sel_pro option:selected').text();
       $('#pro').val(sTest.split(",")[1]);
       $('#proFullName').val($('#sel_pro option:selected').text() + "," + $('#sel_pro').val().split(",")[1]);
       $(".selectpicker").selectpicker('hide');
       setTaskName();

        $.getJSON(config.baseurl + "/api/taskTypeSetRest/projectPhase?projectId=" + $('#proId').val(), function (data) {
            clearDesignPhase();
            var list = data.dataSource;
            list.forEach(function (d) {
                $('#designPhase').append("<option value='" + d.phasesCode + "'>" + d.phasesName + "</option>");
            });
            $(".selectpickerPhase").selectpicker('refresh');
        });
    });

    function initProMenu(data){
        $("#sel_pro").empty();
        $(data).each(function(index){
            $("#sel_pro").append($('<option value="'+data[index].projectId+ ',' + data[index].projectManager + '">'+data[index].projectNumber + "," + data[index].projectName+'</option>'));
        });
        //$('.selectpicker').selectpicker('destroy');
        $(".selectpicker").selectpicker('refresh');
        $(".selectpicker").selectpicker('show');
        $('button[data-id="sel_pro"]').trigger("click");
    }

    function setTaskName() {
        var proName = $('#sel_pro option:selected').text().split(",")[1];
        var taskType = $('#taskType option:selected').val();
        var taskTypeName = $('#taskType option:selected').text();
        if (proName && "" != taskType) {
            $.ajax({
                url : config.baseurl +"/api/taskRest/getTaskchapterNameNo",
                contentType : 'application/x-www-form-urlencoded',
                dataType:"text",
                data: {
                    taskType: taskType,
                    projectId:  $('#proId').val()
                },
                type:"POST",
                beforeSend:function(){window.parent.showLoading();},
                success:function(data){
                    $('#taskName').val(proName + taskTypeName + data);
                },
                complete:function(){ window.parent.closeLoading();},
                error:function(){
                    window.parent.closeLoading();
                    window.parent.error(ajaxError_loadData);
                }
            });

        }
    }

    $('#saveHeader').on('click', function () {
        var valFlag = true;
        var taskName = $('#taskName').val();
        var proId = $('#proId').val();
        var designPhase = $('#designPhase').val();
        var taskType = $('#taskType').val();
        var taskNoteSele = $('#taskNoteSele').val();
        if ("" == proId.trim()) {
            window.parent.error("请选择项目！");
            valFlag = false;
        }
        if ("" == designPhase.trim()) {
            window.parent.error("请选择当前阶段！");
            valFlag = false;
        }
        if ("" == taskType.trim()) {
            window.parent.error("请选择文本类型！");
            valFlag = false;
        }
        if ("" == taskName.trim()) {
            window.parent.error("请填写文本名称！");
            valFlag = false;
        }
        if ("" == taskNoteSele.trim()) {
            window.parent.error("请选择复制信息！");
            valFlag = false;
        }
        if ("TEMPLATE" == $('input[name="attribute11"]:checked').val()) {
            if ("" == sourceTemplateId) {
                window.parent.error("来源目录请选择一个模板！");
                valFlag = false;
            }
        } else {
            if ("" == sourceTaskId) {
                window.parent.error("来源目录请选择一个历史文本！");
                valFlag = false;
            }
        }
        if (!valFlag)
            return;

        if (headerData) {
            saveHeader();
        } else {
            valCreate();
        }
    });

    function valCreate() {
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
                    valTaskName();
                } else {
                    window.parent.closeLoading();
                    window.parent.error("您不具备创建该文本类型的权限，请与系统管理员联系！");
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

    function valTaskName() {
        $.ajax({
            url : config.baseurl +"/api/taskRest/valTaskName",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                taskName: $('#taskName').val(),
                taskHeaderId: $('#taskId').val()
            },
            type:"GET",
            beforeSend:function(){
            },
            success:function(d){
                if (d) {
                    saveHeader();
                } else {
                    window.parent.closeLoading();
                    window.parent.error("该名称已存在，请修改！");
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

    function saveHeader() {
        var o = new Object();
        o.taskHeaderId = headerData ? headerData.taskHeaderId : $('#taskId').val();
        o.templateHeaderId = headerData ? headerData.templateHeaderId : sourceTemplateId;
        o.taskName = $('#taskName').val();
        o.projectId = headerData ? headerData.projectId: $('#proId').val();
        o.designPhase = headerData ? headerData.designPhase: $('#designPhase').val();
        o.taskType = headerData ? headerData.taskType : $('#taskType').val();
        o.attribute1 = $('#taskNoteSele').val();
        o.attribute11 = headerData ? headerData.attribute11 : $('input[name="attribute11"]:checked').val();
        if ("TEMPLATE" == $('input[name="attribute11"]:checked').val())
            o.attribute12 = headerData ? headerData.attribute12 : sourceTemplateId;
        else
            o.attribute12 = headerData ? headerData.attribute12 : sourceTaskId;

        if (headerData) {
            o.taskStatus = headerData.taskStatus;
            o.taskNumber = headerData.taskNumber;
            o.versionNumber = headerData.versionNumber;
        }

        console.log(JSON.stringify(o));
        // return;
        $.ajax({
            url : config.baseurl +"/api/taskRest/saveTaskHeader",
            contentType : 'application/json',
            dataType:"json",
            data: JSON.stringify(o),
            type:"POST",
            beforeSend:function(){
            },
            success:function(data){
                if ("0000" == data.returnCode) {
                    tmp_initButton = 1;
                    loadHeader(data.db_sid, 1);
                    window.parent.success("基本信息创建成功！")
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

    // function valWork() {
    //     if (!$('#div_continueOrcancel').is(':hidden')) {
    //         window.parent.warring("请完成之前的操作后，再进行其他操作");
    //         return false;
    //     } else {
    //         return true;
    //     }
    // }

    // function show_div_continueOrcancel() {
    //     $('#div_continueOrcancel').show(200);
    // }
    //
    // function hide_div_continueOrcancel() {
    //     $('#div_continueOrcancel').hide(200);
    // }
    // $('#div_continueOrcancel').hide();


    var doNo = 0;
    $('#bu_planChapter').on('click', function () {
        // if (!valWork())
        //     return;
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        } else if (2 <= data.length) {
            window.parent.warring(valTips_workOne);
            return;
        }
        doNo = 1;
        //show_div_continueOrcancel();
        queryTaskChapters();
    });

    $('#bu_editText').on('click', function () {
        // if (!valWork())
        //     return;
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        } else if (2 <= data.length) {
            window.parent.warring(valTips_workOne);
            return;
        }
        doNo = 2;
        //show_div_continueOrcancel();
        queryTaskChapters();
    });

    $('#bu_textReview').on('click', function () {
        // if (!valWork())
        //     return;
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        } else if (2 <= data.length) {
            window.parent.warring(valTips_workOne);
            return;
        }
        doNo = 3;
        //show_div_continueOrcancel();
        queryTaskChapters();
    });

    function queryTaskChapters() {
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        } else if (2 <= data.length) {
            window.parent.warring(valTips_workOne);
            return;
        }
        tmp_initButton = 1;
        loadHeader(data[0].taskHeaderId, 0);
    }

    $('#bu_delTask').on('click', function () {
        // if (!valWork())
        //     return;
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        }
        doNo = 4;
       // show_div_continueOrcancel();
        window.parent.showConfirm(doDelTask, "确认删除任务吗？");
    });

    var doDelTask = function() {
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        }
        var to = [];
        $(data).each(function (index) {
            to.push(data[index].taskHeaderId);
        });
        $.ajax({
            url: config.baseurl +"/api/taskRest/delTask",
            data: {
                taskHeaderIds: to.toString()
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

    var now_pageNo = 0;
    $('#bu_share').click(function () {
        // if (!valWork())
        //     return;
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        }
        doNo = 5;
        //show_div_continueOrcancel();
        window.parent.showConfirm(doShareOrUnShare, "确认共享吗？");
    });

    $('#bu_unShare').click(function () {
        // if (!valWork())
        //     return;
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        }
        doNo = 6;
       // show_div_continueOrcancel();
        window.parent.showConfirm(doShareOrUnShare, "确认取消共享吗？");
    });

    var doShareOrUnShare = function() {
        now_pageNo = t.page();
        var _status;
        var _msg;
        if (5 == doNo) {
            _status = "1";
            _msg = "共享成功！";
        } else if (6 == doNo) {
            _status = "0";
            _msg = "取消共享成功！";
        }
        var tmp = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == tmp.length) {
            window.parent.warring(valTips_selOne);
            return;
        }
        $(tmp).each(function (index) {
            $.ajax({
                url: config.baseurl +"/api/taskRest/upTaskSharde",
                data: {
                    taskHeaderId: tmp[index].taskHeaderId,
                    taskShared: _status
                },
                contentType : 'application/x-www-form-urlencoded',
                dataType: "json",
                type: "POST",
                beforeSend: function() {
                    window.parent.showLoading();
                },
                success: function(data) {
                    if ("0000" == data.returnCode) {
                        window.parent.success(_msg);
                        if (index == tmp.length -1) {
                            $('#bu_queryData').trigger("click");
                        }
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
        });
    }

    // $('#bu_continue').click(function () {
    //     if (1 == doNo || 2 == doNo || 3 == doNo) {
    //         var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
    //         if (0 == data.length) {
    //             window.parent.warring(valTips_selOne);
    //             return;
    //         } else if (2 <= data.length) {
    //             window.parent.warring(valTips_workOne);
    //             return;
    //         }
    //         var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
    //         if (0 == data.length) {
    //             window.parent.warring(valTips_selOne);
    //             return;
    //         } else if (2 <= data.length) {
    //             window.parent.warring(valTips_workOne);
    //             return;
    //         }
    //         tmp_initButton = 1;
    //         loadHeader(data[0].taskHeaderId, 0);
    //         // loadHeader("5555");
    //     } else if (4 == doNo) {
    //         var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
    //         if (0 == data.length) {
    //             window.parent.warring(valTips_selOne);
    //             return;
    //         }
    //
    //         var to = [];
    //         $(data).each(function (index) {
    //             to.push(data[index].taskHeaderId);
    //         });
    //
    //         $.ajax({
    //             url: config.baseurl +"/api/taskRest/delTask",
    //             data: {
    //                 taskHeaderIds: to.toString()
    //             },
    //             contentType : 'application/x-www-form-urlencoded',
    //             dataType: "json",
    //             type: "POST",
    //             beforeSend: function() {
    //                 window.parent.showLoading();
    //             },
    //             success: function(data) {
    //                 if ("0000" == data.returnCode) {
    //                     window.parent.success(tips_delSuccess);
    //                     hide_div_continueOrcancel();
    //                     $('#bu_queryData').trigger("click");
    //                 } else {
    //                     window.parent.error(data.returnMsg);
    //                 }
    //             },
    //             complete: function() {
    //                 window.parent.closeLoading();
    //             },
    //             error: function() {
    //                 window.parent.closeLoading();
    //                 window.parent.error(ajaxError_sys);
    //             }
    //         });
    //     } else if (5 == doNo || 6 == doNo) {
    //
    //     }
    // });

    // $('#bu_continue_cancel').click(function () {
    //     hide_div_continueOrcancel();
    // });

    function loadHeader(taskHeaderId, hide) {
        $.ajax({
            url: config.baseurl +"/api/taskRest/taskHeaderList",
            data: {
                taskHeaderId: taskHeaderId
                // ,isoa:   isoa
            },
            contentType : 'application/x-www-form-urlencoded',
            dataType: "json",
            type: "POST",
            beforeSend: function() {
                window.parent.showLoading();
            },
            success: function(data) {
                headerData = data.dataSource[0];
                console.log(headerData);
                // hide_div_continueOrcancel();
                loadPlanChapter(taskHeaderId, hide);
                loadLogonUser();
            },
            complete: function() {
            },
            error: function() {
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
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
            },
            complete: function() {},
            error: function() {}
        });
    }


    function loadPlanChapter(taskHeaderId, hideHeader) {
        $.ajax({
            url: config.baseurl +"/api/taskRest/taskChaptersList",
            data: {
                taskHeaderId: taskHeaderId
            },
            contentType : 'application/x-www-form-urlencoded',
            dataType: "json",
            type: "GET",
            beforeSend: function() {

            },
            success: function(data) {
                initAddOrUpdateModal();
                if (1 == hideHeader)
                    $('#add_modal').modal('hide');
                $('#chapter_modal').modal('show');
                // 赋全局
                chapter_modal_taskHeaderId = taskHeaderId;
                chapter_modal_data = data.dataSource;
                // reloadChapter(taskHeaderId, data.dataSource, tmp_initButton);
                $('#treetableChapter tbody').empty();
                clearCheckAll();
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

    var chapter_modal_taskHeaderId = "";
    var chapter_modal_data = [];
    $('#chapter_modal').on('shown.bs.modal', function (e) {
        $('#chapter_modal').css("cssText", "display: block; padding-left: 0px; padding-right: 0px;");
        $('#show_eff').removeAttr("checked");
        delWithEnableFlag = -1;
        firstInitNo = 0;
        reloadChapter(chapter_modal_taskHeaderId, chapter_modal_data, tmp_initButton);
    });

    $('#chapter_modal').on('hidden.bs.modal', function (e) {
        projectRoles = [];
        specialtys = [];
        if (!userDuty)
            loadUserDuty();
    });

    $('#add_modal').on('shown.bs.modal', function (e) {
        $('#add_modal').css("cssText", "display: block; padding-left: 0px; padding-right: 0px;");
    });

});

var t2;
var dataSet2 = [];
function queryList2(check) {
    $('#dataTables-listAll2').show();
    $('#dataTables-listAll3').hide();
    $('#dataTables-listAll2').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-data2"></table>');

    t2 = $('#dataTables-data2').DataTable({
        "autoWidth": false,
        // "searching": false,
        "scrollX": true,
        "processing": true,
        "data": dataSet2,
        "pagingType": "full",
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "模板名称", "data": "templateName"},
            {"title": "模板说明", "data": "templateDescription"},
            {"title": "文本类型", "data": "taskTypeName"}
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "targets": 0,
            "width": 32
        },{
            "width": 200,
            "targets": 1
            // "render": function (data, type, full, meta) {
            //     return "<a name='a_linkTemplate' href='#1'>" + data + "</a>";
            // }
        },{
            "targets": 2,
            "width": 200
        },{
            "targets": 3,
            "width": 150
        }
        ],
        "order": [[1, 'asc']],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "rowCallback": function( row, data, index ) {
            if (check)
                $('#dataTables-data2 tr:eq(0)').trigger("click");
        },
        "initComplete": function () {
            $('#dataTables-data2_length').remove();
        }
    });

    $('#dataTables-data2 tbody').on('click', 'tr', function () {
        if ( $(this).hasClass('dt-select') ) {
            $(this).removeClass('dt-select');
        }
        else {
            $('#dataTables-data2').DataTable().$('tr.dt-select').removeClass('dt-select');
            $(this).addClass('dt-select');
        }
        var userTable = $('#dataTables-data2').DataTable();
        var data = userTable.rows($(this)).data();
        $('#tag1').val(data[0].templateName);
        loadChapterPreview(data[0].templateId);
        sourceTemplateId = data[0].templateId;
    });

    t2.on( 'order.dt search.dt', function () {
        t2.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );
    } ).draw();

}

var t3;
var dataSet3 = [];
function initHistoryTaskList(check) {
    $('#dataTables-listAll3').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-data3"></table>');
    t3 = $('#dataTables-data3').DataTable({
        "autoWidth": true,
        // "searching": false,
        "scrollX": true,
        "processing": true,
        "data": dataSet3,
        "pagingType": "full",
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "文本名称", "data": "taskName"},
            {"title": "项目编号", "data": "projectNumber"},
            {"title": "项目经理", "data": "projectManagerName"},
            {"title": "项目阶段", "data": "designPhaseName"},
            {"title": "文本类型", "data": "taskTypeName"}
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "targets": 0,
            "width": 32
        },{
            "width": 200,
            "targets": 1
        },{
            "targets": 2,
            "width": 200
        },{
            "targets": 3,
            "width": 100
        },{
            "targets": 4,
            "width": 150
        },{
            "targets": 5,
            "width": 100
        }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "rowCallback": function( row, data, index ) {
            if (check)
                $('#dataTables-data3 tr:eq(0)').trigger("click");
        },
        "initComplete": function () {
            $('#dataTables-data3_length').remove();
        }
    });

    $('#dataTables-data3 tbody').on('click', 'tr', function () {
        if ( $(this).hasClass('dt-select') ) {
            $(this).removeClass('dt-select');
        } else {
            $('#dataTables-data3').DataTable().$('tr.dt-select').removeClass('dt-select');
            $(this).addClass('dt-select');
        }
        var userTable = $('#dataTables-data3').DataTable();
        var data = userTable.rows($(this)).data();
        $('#tag1').val(data[0].taskName);
        loadChapterPreview2(data[0].taskHeaderId);
        sourceTemplateId = data[0].templateHeaderId;
        sourceTaskId = data[0].taskHeaderId;
    });

    t3.on( 'order.dt search.dt', function () {
        t3.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );
    } ).draw();
}

function loadChapterPreview(templateId) {
    $.ajax({
        url: config.baseurl +"/api/templateRest/templateChapters",
        data: {
            templateId: templateId
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            reloadTemplateChapter(data);
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

function loadChapterPreview2(templateId) {
    $.ajax({
        url: config.baseurl +"/api/taskRest/taskChaptersHistoryList",
        data: {
            taskHeaderId: templateId
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            reloadTemplateChapter(data.dataSource);
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

$('#bu_fancyTreeExpanded_review').on('click', function () {
    $("#treetable").fancytree("getRootNode").visit(function (node) {
        node.setExpanded(true);
    });
    setExpanded = true;
});

$('#bu_fancyTreeeCollapse_review').on('click', function () {
    $("#treetable").fancytree("getRootNode").visit(function (node) {
        node.setExpanded(false);
    });
    setExpanded = false;
});


var setExpanded = false;
function reloadTemplateChapter(data) {
    templateTreeData = [];
    for (var a = 1; a < data.length; a++) {
        templateTreeData.push(data[a]);
    }
    var _newIndex = [];
    var tree = $("#treetable").fancytree("getTree");
    tree.reload(templateTreeData).done(function(){
        $("#treetable").fancytree("getRootNode").visit(function(node){
            node.setExpanded(true);
            _newIndex.push(node.getIndexHier());
        });
    });
    tree = $("#treetable").fancytree("getTree");
    var nodeList = [];
    tree.visit(function(node){
        nodeList.push(node);
    });
    $("#treetable tbody tr td:nth-child(1)").each(function (index) {
        if (1 == nodeList[index].getLevel())
            $($(this).find('.fancytree-icon')[0]).css("color", "#2f9199");
        else if (2 == nodeList[index].getLevel())
            $($(this).find('.fancytree-icon')[0]).css("color", "#f19d50");
        else if (3 == nodeList[index].getLevel())
            $($(this).find('.fancytree-icon')[0]).css("color", "#35cd70");
        else if (4 == nodeList[index].getLevel())
            $($(this).find('.fancytree-icon')[0]).css("color", "#9370DB");
    });

    $("#treetable tr td:nth-child(1)").each(function (index) {
        $($(this).find('.fancytree-title')[0]).html(_newIndex[index]);

    });
    nodeList.forEach(function (node) {
        node.setExpanded(setExpanded);
    });
}

$('#viewDutyModal').on('shown.bs.modal', function (e) {
    queryList_viewDuty();
});

var dataSet_viewDuty = [];
var t_viewDuty;
function queryList_viewDuty() {
    $('#dataTables-viewDutyAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table table-bordered" id="dataTables-data_viewDuty"></table>');
    t_viewDuty = $('#dataTables-data_viewDuty').DataTable({
        "searching": true,
        "processing": true,
        "data": dataSet_viewDuty,
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
            $('#dataTables-data_viewDuty_length').insertBefore($('#dataTables-data_viewDuty_info'));
            $('#dataTables-data_viewDuty_length').addClass("col-sm-4");
            $('#dataTables-data_viewDuty_length').css("paddingLeft", "0px");
            $('#dataTables-data_viewDuty_length').css("paddingTop", "5px");
            $('#dataTables-data_viewDuty_length').css("maxWidth", "130px");
        }
    });

    $('#dataTables-data_viewDuty tbody').on('click', 'tr', function () {
        if ( $(this).hasClass('dt-select') ) {
            $(this).removeClass('dt-select');
        }
        else {
            $('#dataTables-data_viewDuty').DataTable().$('tr.dt-select').removeClass('dt-select');
            $(this).addClass('dt-select');
        }
    });

    t_viewDuty.on( 'order.dt search.dt', function () {
        t_viewDuty.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );
    } ).draw();

}

$('#save_userDuty').on('click', function () {
    var userTable = $('#dataTables-data_viewDuty').DataTable();
    var selectData = userTable.rows('.dt-select').data();
    if (0 == selectData.length) {
        window.parent.warring("请选择一个职责");
        return;
    }
    userDuty = selectData[0].responsibilityKey;
    $('#respKey').val(userDuty);
    $('#viewDutyModal').modal('hide');
});

function initTaskNoteSele(){
    $('#taskNoteSele').empty();
    var initValue = "";
    var initText = "请选择";
    $('#taskNoteSele').append("<option value='" + initValue + "'>" + initText + "</option>");
    var initValue = "0";
    var initText = "不包含内容";
    $('#taskNoteSele').append("<option value='" + initValue + "'>" + initText + "</option>");    
    var initValue = "1";
    var initText = "包含内容";
    $('#taskNoteSele').append("<option value='" + initValue + "'>" + initText + "</option>");
}
