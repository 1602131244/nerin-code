var tab_l_projectId;
var tab_l_version;
var tab_l_version_max;
var tab_milestoneIds = [];
var tab_checktype = 1;

function tab_initMilepost() {
    console.log("初始化里程牌");
    //按钮控制,初始只能用查询
    $('#bu_adj_' + activeTabId).attr('disabled', true);
    $('#bu_del_v_' + activeTabId).attr('disabled', true);
    $('#bu_add_' + activeTabId).attr('disabled', true);
    $('#bu_del_' + activeTabId).attr('disabled', true);
    $('#bu_saveas_' + activeTabId).attr('disabled', true);
    $('#bu_avgQuanzhong_' + activeTabId).attr('disabled', true);
    $('#bu_effective_' + activeTabId).attr('disabled', true);
    $('#bu_milestone_status_' + activeTabId).attr('disabled', true);

    //检查是否有已发布的工作计划，如果没有则弹出提示
    // $.ajax({     //异步，调用后台get方法
    //     url: config.baseurl + "/api/milestone/workplan", //调用地址，不包含参数
    //     contentType: "application/json",
    //     dataType: "json",
    //     async: true,
    //     data: {
    //         projectId: tab_l_projectId  //传入参数，依次填写
    //     },
    //     type: "GET",  //调用方法类型
    //     beforeSend: function () {
    //
    //     },
    //     success: function (data) {
    //         if(data==0){
    //             window.parent.error("没有已发布的工作计划！");
    //             tab_checktype=0;
    //             allTabData[activeTabId].tab_checktype = tab_checktype
    //             return false;
    //
    //         }else {
    //             tab_initTables(1);
    //         }
    //     },
    //     complete: function () {
    //
    //     },
    //     error: function () {
    //         window.parent.error(ajaxError_sys);
    //     }
    // });
    tab_initTables(1);
    createMilepostTree();
    createQueryMilepostVersion();
    createMilepostAdd();
    createMilepostAdj();
    deleteMilepostversion();
    createMilepostDel();
    createMilepostSave();
    createMilepostComplete();
    createMilepostUncomplete();
    createEffective();
    createAvgQuanzhongBu();
}

function tab_initTables(auto) { //取值集
    $('#s_milepost_version_' + activeTabId).empty();//清空下拉表

    $.ajax({     //异步，调用后台get方法
        url: config.baseurl + "/api/milestone/version", //调用地址，不包含参数
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: {
            projectId: tab_l_projectId  //传入参数，依次填写
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {

        },
        success: function (data) {
            var list = data.dataSource;
            list.forEach(function (data) {  //取最大版本
                tab_l_version = data.version;
                tab_l_version_max = data.version;
                allTabData[activeTabId].tab_l_version = tab_l_version;
                allTabData[activeTabId].tab_l_version_max = tab_l_version_max;
            });

            // 为发布版，取当前版本号
            var tmp_sub_maxNo;
            for (var i = list.length - 1; i > 0; i--){
                if ("APPROVED" == list[i].approveStatus) {
                    tmp_sub_maxNo = list[i].version;
                    break;
                }
            }

            list.forEach(function (data) {  //下拉列表，如果是最大版本，则默认选中
                if (data.version == tab_l_version_max) {
                    if ("APPROVED" == data.approveStatus)
                        $('#s_milepost_version_' + activeTabId).append("<option value='" + data.version + "'selected>" + data.version + "（生效版）</option>");
                    else if ("EDIT" == data.approveStatus)
                        $('#s_milepost_version_' + activeTabId).append("<option value='" + data.version + "'selected>" + data.version + "（编辑版）</option>");
                    else
                        $('#s_milepost_version_' + activeTabId).append("<option value='" + data.version + "'selected>" + data.version + "</option>");
                } else {
                    if (data.version == tmp_sub_maxNo)
                        $('#s_milepost_version_' + activeTabId).append("<option value='" + data.version + "'>" + data.version + "（生效版）</option>");
                    else
                        $('#s_milepost_version_' + activeTabId).append("<option value='" + data.version + "'>" + data.version + "（历史版）</option>");
                }
            });




            if (1 == auto)
                $('#bu_queryMilepostVersion_' + activeTabId).trigger("click");
        },
        complete: function () {

        },
        error: function () {
            window.parent.error(ajaxError_sys);
        }
    });

}

var tab_showData = [];
//加载 生成树型结构的数据
function reloadMilestone(data) {
    tab_showData = [];
    for (var a = 0; a < data.length; a++) {
        tab_showData.push(data[a]);
    }
    allTabData[activeTabId].tab_showData = tab_showData;
    //rootData = data[0];
    var tree = $("#treetable_" + activeTabId).fancytree("getTree");
    tree.reload(tab_showData).done(function () {
        fancyTreeExpanded();
        fancyTreeeCollapse();
        // 2017-01-19，计算总权重
        var tmpCount = 0;
        var nodeList = getTreeAllNode();
        nodeList.forEach(function (node) {
            if (2 == node.getLevel()) {
                tmpCount += node.data.phaseWeight;
            }
        });
        $('#treetable_' + activeTabId + ' tbody tr:eq(0) td:eq(3)').text(tmpCount);
    });
}

//展开树
function fancyTreeExpanded() {
    $("#treetable_" + activeTabId).fancytree("getRootNode").visit(function (node) {
        node.setExpanded(true);
    });
}
//收回树
function fancyTreeeCollapse() {
    $("#treetable_" + activeTabId).fancytree("getRootNode").visit(function (node) {
        node.setExpanded(false);
    });
}

function setMilePostPercent() {
    var phaseWeight = $("input[name='phaseWeight_" + activeTabId + "']");
    var tmpCount = 0;
    $(phaseWeight).each(function (i) {
        if ("" != phaseWeight[i].value.trim())
            tmpCount += parseFloat(phaseWeight[i].value.trim());
    });
    if (100 < tmpCount)
        $('#treetable_' + activeTabId + ' tbody tr:eq(0) td:eq(3)').html('<font color="red">权重超过100%！</font>');
    else
        $('#treetable_' + activeTabId + ' tbody tr:eq(0) td:eq(3)').text(tmpCount);
}

function createMilepostTree() {
    $("#treetable_" + activeTabId).fancytree({
        selectMode: 2,
        extensions: ["dnd", "edit", "glyph", "table"],
        checkbox: true,
        keyboard: false,
        dnd: {
            focusOnClick: false,
            dragStart: function (node, data) {
                return true;
            },
            dragEnter: function (node, data) {
                return true;
            },
            dragDrop: function (node, data) {
                var moveNode = data.otherNode;
                var inNode = node;

                var moveParentNode = data.otherNode.parent;
                var inParentNode = inNode.parent;
                if (2 != allTabData[activeTabId].tab_checktype) {
                    window.parent.warring("调整计划后才能移动里程碑");
                    return false;
                } else if (2 >= moveNode.getLevel()) {
                    window.parent.warring("只有里程碑才能移动");
                    return false;
                } else if (3 > inNode.getLevel()) {
                    window.parent.warring("只能移动到阶段下");
                    return false;
                } else if (moveParentNode.data.phaseCode != inParentNode.data.phaseCode) {
                    window.parent.warring("不允许跨阶段移动里程碑");
                    return false;
                } else if ("over" == data.hitMode && 3 == inNode.getLevel()) {
                    window.parent.warring("里程碑不允许移动到里程碑节点内");
                    return false;
                } else {
                    data.otherNode.moveTo(node, data.hitMode);
                }
            }
        },
        glyph: glyph_opts,
        source: tab_showData,
        table: {
            checkboxColumnIdx: 0,
            nodeColumnIdx: 1
        },
        select: function (event, data) {
            var selNodes = data.tree.getSelectedNodes();
            var selKeys = $.map(selNodes, function (node) {
                return "[" + node.key + "]: '" + node.title + "'";
            });
        },
        renderColumns: function (event, data) {
            var node = data.node,
                $tdList = $(node.tr).find(">td");
            //console.log(node.data.chapterName);
            // $tdList.eq(1).text(node.getIndexHier());//上下级的关联
            var col2 = null != node.data.name ? node.data.name : "";
            var col3 = null != node.data.phaseWeight ? node.data.phaseWeight : "";//阶段权重
            var col4 = null != node.data.phasePercent ? node.data.phasePercent : "";//估算进度
            var col5 = null != node.data.percent ? node.data.percent : "";//里程碑完成情况
            var col51 = null != node.data.milestoneStatus ? node.data.milestoneStatus : "";//里程碑完成情况
            var col5html = "Y" == col51 ? "<i class='fa fa-check' style='color: #00a65a;'></i>" : "";
            if ("N" == col51 && "APPROVING" == node.data.attribute1)
                col5html = "审批中";
            var col6 = null != node.data.planStartDate ? node.data.planStartDate : "";//计划开始时间
            var col7 = null != node.data.planEndDate ? node.data.planEndDate : "";//计划完成时间
            var col8 = null != node.data.actualCompleteDate ? node.data.actualCompleteDate : "";//计划完成时间
            var col9 = null != node.data.url ? node.data.url : "";//查看流程
            if( node.data.url!=null ){
                $tdList.eq(9).html( "<a name='a_check_oa' href="+"'"+node.data.url+"'"+" target='view_window'>" + "查看" + "</a>");
            }

            // 2017-01-20
            var tmp_col4 = '<div class="progress" style="margin-bottom: 0px;">'
                + '<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="' + col4 + '" aria-valuemin="0" aria-valuemax="100" style="color: black;width: ' + col4 + '%;">'
                + col4 + '%'
                + '</div></div>';
            if (1 == node.getLevel()) {
                $tdList.eq(6).text(col6);
                $tdList.eq(7).text(col7);
            }
            if (tab_checktype == 1) {//查询，不显示输入框
                if (-2 == node.data.parentId) {
                    $tdList.eq(2).text(col2);
                    $tdList.eq(4).html(tmp_col4);
                    $tdList.eq(5).text(col5);
                }
                else if (-1 == node.data.parentId) {//如果是阶段
                    $tdList.eq(2).text(col2);
                    $tdList.eq(3).text(col3);
                    $tdList.eq(4).html(tmp_col4);
                    $tdList.eq(5).text(col5);
                    if (null == col6 || "" == col6) {
                        var parentNode = node.parent;
                        col6 = parentNode.data.planStartDate;
                    }
                    $tdList.eq(6).text(col6);
                    if (null == col7 || "" == col7) {
                        var parentNode = node.parent;
                        col7 = parentNode.data.planEndDate;
                    }
                    $tdList.eq(7).text(col7);
                } else {
                    $tdList.eq(2).text(col2);
                    $tdList.eq(5).html(col5html);
                    $tdList.eq(7).text(col7);
                }
                $tdList.eq(8).text(col8);
                // 2017-01-19，如果是收入确认节点的时候，需要高亮标识
                var $tr = $tdList.eq(8).parent();
                if ("Y" == node.data.ismark)
                    $tr.css("backgroundColor", "#C6E2FF");
                else
                    $tr.css("backgroundColor", "");
            } else { //调整计划，打开输入框
                if (-2 == node.data.parentId) {
                    $tdList.eq(2).text(col2);
                    $tdList.eq(4).html(tmp_col4);
                    $tdList.eq(5).text(col5);
                }
                else if (-1 == node.data.parentId) {//如果是阶段
                    $tdList.eq(2).text(col2);
                    $tdList.eq(3).html("<input type='text' style='width: 100%; display: inline-block;' required='required'  name='phaseWeight_" + activeTabId + "' class='form-control input-sm' value='" + col3 + "' onkeyup='javascript:setMilePostPercent();'/>");
                    $tdList.eq(4).html(tmp_col4);
                    $tdList.eq(5).text(col5);
                    if (null == col6 || "" == col6) {
                        var parentNode = node.parent;
                        col6 = parentNode.data.planStartDate;
                    }
                    var timeCol6 = $("<div class='input-group date'><input onblur='javascript:milepostDateChange(this);' type='text' style='width: 100%;' name='planStartDate_" + activeTabId + "' class='form-control input-sm' value='" + col6 + "' /><span class='input-group-addon' style='padding: 3px 8px; font-size: 13px;'><span class='glyphicon glyphicon-calendar'></span></div>");
                    $tdList.eq(6).html(timeCol6);
                    timeCol6.datetimepicker({
                        format: 'YYYY-MM-DD'
                    });
                    if (null == col7 || "" == col7) {
                        var parentNode = node.parent;
                        col7 = parentNode.data.planEndDate;
                    }
                    var timeCol7 = $("<div class='input-group date'><input onblur='javascript:milepostDateChange(this);' type='text' style='width: 100%;' name='planEndDate_" + activeTabId + "' class='form-control input-sm' value='" + col7 + "' /><span class='input-group-addon' style='padding: 3px 8px; font-size: 13px;'><span class='glyphicon glyphicon-calendar'></span></div>");
                    $tdList.eq(7).html(timeCol7);
                    timeCol7.datetimepicker({
                        format: 'YYYY-MM-DD'
                    });
                } else {
                    $tdList.eq(2).html("<input type='text' style='width: 100%; display: inline-block;' required='required' name='name_" + activeTabId + "' class='form-control input-sm' value='" + col2 + "' />");
                    $tdList.eq(5).html(col5html);
                    var timeCol7 = $("<div class='input-group date'><input onblur='javascript:milepostDateChange(this);' type='text' style='width: 100%;' name='planEndDate_" + activeTabId + "' class='form-control input-sm' value='" + col7 + "' /><span class='input-group-addon' style='padding: 3px 8px; font-size: 13px;'><span class='glyphicon glyphicon-calendar'></span></div>");
                    $tdList.eq(7).html(timeCol7);
                    timeCol7.datetimepicker({
                        format: 'YYYY-MM-DD'
                    });
                }
                $tdList.eq(8).text(col8);
            }
        },
        removeNode: function (event, data) { // 删除node触发
            // var node = data.node;
            // if (undefined != node.data.chapterId)
            //     delTemplateChapterIds.push(node.data.chapterId);
        },
        activate: function (event, data) { //选中触发

        },
        wide: {
            iconWidth: "1em",     // Adjust this if @fancy-icon-width != "16px"
            iconSpacing: "0.5em", // Adjust this if @fancy-icon-spacing != "3px"
            levelOfs: "1.5em"     // Adjust this if ul padding != "16px"
        },
        icon: function (event, data) {

        },
        lazyLoad: function (event, data) {

        }
    });
}

function milepostDateChange(input) {
    var nodeList = getTreeAllNode();//取所有的节点数据
    var name = $("input[name='name_" + activeTabId + "']");
    var phaseWeight = $("input[name='phaseWeight_" + activeTabId + "']");
    var planStartDate = $("input[name='planStartDate_" + activeTabId + "']");
    var planEndDate = $("input[name='planEndDate_" + activeTabId + "']");
    var weightisnull=0;
    var planStartDateisnull=0;
    var planEndDateisnull=0;
    var parentnum = 0;
    var milestonenum=0;
    var Weight = 0;
    // 判断计划结束时间
    var planDateMsg = "";

    var $tr = $(input).parent().parent().parent();
    var $td = $(input).parent().parent();
    var trNo = $('#treetable_' + activeTabId + ' tbody tr').index($tr);
    var tdNo = $('#treetable_' + activeTabId + ' tbody tr:eq(' + trNo + ') td').index($td);
    console.log(trNo);
    console.log(tdNo);

    $(nodeList).each(function (i) {
        if (i == trNo) {
            $('#treetable_' + activeTabId + ' tbody tr:eq(' + (i) + ')').css("backgroundColor", "");
            var node = nodeList[i];
            var tmp_tr = node.tr;
            // 阶段
            if (0 != node.data.phaseCode && 1 < node.getLevel()) {
                var startTd = $(tmp_tr).find('td:eq(6)');
                var endTd = $(tmp_tr).find('td:eq(7)');
                var input = $(startTd[0]).find('input')[0];
                var input2 = $(endTd[0]).find('input')[0];
                if (("" != input2.value && "" != input.value) && input.value > input2.value) {
                    planDateMsg += "第" + (i + 1) + "行计划完成时间小于计划开始时间！<br>";
                    $('#treetable_' + activeTabId + ' tbody tr:eq(' + (i) + ')').css("backgroundColor", "red");
                }
                // // 如果有父节点
                // if (null != node.parent) {
                //     var parentNode = node.parent;
                //     var planStartDate = parentNode.data.planStartDate;
                //     var planEndDate = parentNode.data.planEndDate;
                //     if ("" != input.value && (input.value < planStartDate || input.value > planEndDate)) {
                //         planDateMsg += "第" + (i + 1) + "行计划开始时间不在项目时间范围内！<br>";
                //         $('#treetable_' + activeTabId + ' tbody tr:eq(' + (i) + ')').css("backgroundColor", "red");
                //     }
                //     if ("" != input2.value && (input2.value > planEndDate || input2.value < planStartDate)) {
                //         planDateMsg += "第" + (i + 1) + "行计划完成时间不在项目时间范围内！<br>";
                //         $('#treetable_' + activeTabId + ' tbody tr:eq(' + (i) + ')').css("backgroundColor", "red");
                //     }
                // }

                if ("" != input.value) {
                    $.ajax({
                        url : config.baseurl +"/api/milestone/validataPlanDate",
                        contentType : 'application/x-www-form-urlencoded',
                        dataType:"json",
                        async: false,
                        data: {
                            proId: tab_l_projectId,
                            phaseCode: node.data.phaseCode,
                            date: input.value
                        },
                        type:"POST",
                        beforeSend:function(){},
                        success:function(data){
                            if ("0000" != data.returnCode) {
                                planDateMsg += "第" + (i + 1) + "行，计划开始时间" + data.returnMsg + "<br>";
                                $('#treetable_' + activeTabId + ' tbody tr:eq(' + (i) + ')').css("backgroundColor", "red");
                            }
                        },
                        complete:function(){},
                        error:function(){}
                    });
                }
                if ("" != input2.value) {
                    $.ajax({
                        url : config.baseurl +"/api/milestone/validataPlanDate",
                        contentType : 'application/x-www-form-urlencoded',
                        dataType:"json",
                        async: false,
                        data: {
                            proId: tab_l_projectId,
                            phaseCode: node.data.phaseCode,
                            date: input2.value
                        },
                        type:"POST",
                        beforeSend:function(){},
                        success:function(data){
                            if ("0000" != data.returnCode) {
                                planDateMsg += "第" + (i + 1) + "行，计划结束时间" + data.returnMsg + "<br>";
                                $('#treetable_' + activeTabId + ' tbody tr:eq(' + (i) + ')').css("backgroundColor", "red");
                            }
                        },
                        complete:function(){},
                        error:function(){}
                    });
                }
            } else if (0 == node.data.phaseCode && 2 < node.getLevel()) { // 里程碑
                var endTd = $(tmp_tr).find('td:eq(7)');
                var input2 = $(endTd[0]).find('input')[0];
                // 如果有父节点
                if (null != node.parent) {
                    var parentNode = node.parent;
                    var parentTr = parentNode.tr;
                    var parentStartTd = $(parentTr).find('td:eq(6)');
                    var parentEndTd = $(parentTr).find('td:eq(7)');
                    var parentInput1 = $(parentStartTd[0]).find('input')[0];
                    var parentInput2 = $(parentEndTd[0]).find('input')[0];
                    if ("" != input2.value && (input2.value > parentInput2.value || input2.value < parentInput1.value)) {
                        planDateMsg += "第" + (i + 1) + "行计划完成时间不在阶段时间范围内！<br>";
                        $('#treetable_' + activeTabId + ' tbody tr:eq(' + (i) + ')').css("backgroundColor", "red");
                    }
                }
            } else if (0 == node.data.phaseCode && 2 == node.getLevel()) { // 里程碑
                var endTd = $(tmp_tr).find('td:eq(7)');
                var input2 = $(endTd[0]).find('input')[0];
                // 如果有父节点
                if (null != node.parent) {
                    var planStartDate = node.parent.data.planStartDate;
                    var planEndDate = node.parent.data.planEndDate;
                    if ("" != input2.value && (input2.value > planEndDate || input2.value < planStartDate)) {
                        planDateMsg += "第" + (i + 1) + "行计划完成时间不在项目时间范围内！<br>";
                        $('#treetable_' + activeTabId + ' tbody tr:eq(' + (i) + ')').css("backgroundColor", "red");
                    }
                }
            }
        }
    });


    if("" != planDateMsg){
        window.parent.error(planDateMsg);
        return false;
    }
}

//加载数据，返回里程碑审批状态,控制按钮
function getMilestoneStatus() {
    if (undefined == tab_l_version)
        return;
    $.ajax({
        url: config.baseurl + "/api/milestone/status",
        data: {
            projectId: tab_l_projectId,
            version: tab_l_version
        },
        contentType: 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function () {

        },
        success: function (data) {
            $('#s_milepost_version_' + activeTabId).val(tab_l_version);
            var l_status_tmp = data.status;
            if ("编辑" == l_status_tmp)
                l_status_tmp = "未发布";
            else if ("批准" == l_status_tmp)
                l_status_tmp = "发布";
            $('#status_' + activeTabId).val(l_status_tmp);
            if (tab_checktype == 1) {  //查询
                if (tab_l_version == tab_l_version_max){
                    $('#bu_adj_' + activeTabId).attr('disabled', false);
                    if(l_status_tmp == "未发布")   //只有编辑状态下，才能点击删除版本按钮
                        $('#bu_del_v_' + activeTabId).attr('disabled', false);
                    else
                        $('#bu_del_v_' + activeTabId).attr('disabled', true);
                }
                else{
                    $('#bu_adj_' + activeTabId).attr('disabled', true);
                    $('#bu_del_v_' + activeTabId).attr('disabled', true);
                }

                // if (tab_l_version == tab_l_version_max && $('#status_' + activeTabId)[0].value != '审批中') { //如果查询的是最大版本，且状态不是审批中，则按钮亮
                //     $('#bu_add_' + activeTabId).attr('disabled', true);
                //     $('#bu_del_' + activeTabId).attr('disabled', true);
                //     $('#bu_saveas_' + activeTabId).attr('disabled', true);
                //     $('#bu_effective_' + activeTabId).attr('disabled', true);
                //     // $('#bu_milestone_status').removeClass('disabled');
                //     if ($('#status_' + activeTabId)[0].value == '批准') {//版本为最大，状态为批准，可更新里程碑
                //         $('#bu_milestone_status_' + activeTabId).attr('disabled', false);
                //     } else {
                //         $('#bu_milestone_status_' + activeTabId).attr('disabled', true);
                //     }
                // }
                // 2017-01-09
                $('#bu_add_' + activeTabId).attr('disabled', true);
                $('#bu_del_' + activeTabId).attr('disabled', true);
                $('#bu_saveas_' + activeTabId).attr('disabled', true);
                $('#bu_avgQuanzhong_' + activeTabId).attr('disabled', true);
                $('#bu_effective_' + activeTabId).attr('disabled', true);

                // 2017-01-09，批准下的最大版本号=当前选择的版本号时，里程碑按钮可用
                loadVersionForBu_milestone_status($('#s_milepost_version_' + activeTabId).val());
                // 2017-01-06 只有切换到项目经理，才允许调整
                if (0 > getRoles().indexOf("1")) {
                    $('#bu_adj_' + activeTabId).attr('disabled', true);
                    $('#bu_del_v_' + activeTabId).attr('disabled', true);
                    $('#bu_milestone_status_' + activeTabId).attr('disabled', true);
                }
            } else { //调整
                $('#bu_add_' + activeTabId).attr('disabled', false);
                $('#bu_del_' + activeTabId).attr('disabled', false);
                $('#bu_saveas_' + activeTabId).attr('disabled', false);
                $('#bu_avgQuanzhong_' + activeTabId).attr('disabled', false);
                $('#bu_effective_' + activeTabId).attr('disabled', false);
                // 2017-01-09，批准下的最大版本号=当前选择的版本号时，里程碑按钮可用
                loadVersionForBu_milestone_status($('#s_milepost_version_' + activeTabId).val());
                // 2017-01-09 只有切换到项目经理，才允许调整
                if (0 > getRoles().indexOf("1")) {
                    $('#bu_milestone_status_' + activeTabId).attr('disabled', true);
                }
            }

        },
        complete: function () {
        },
        error: function () {
            window.parent.error("重新加载数据出错1111！");
        }
    });
}

function loadVersionForBu_milestone_status(s_version) {
    $.ajax({
        url: config.baseurl + "/api/milestone/version",
        data: {
            projectId: tab_l_projectId
        },
        contentType: 'application/x-www-form-urlencoded',
        dataType: "json",
        async: false,
        type: "GET",
        beforeSend: function () {},
        success: function (data) {
            var d = data.dataSource;
            if ("APPROVED" == d[d.length - 1].approveStatus) {
                if (d[d.length - 1].version == s_version) {
                    $('#bu_milestone_status_' + activeTabId).attr('disabled', false);
                } else {
                    $('#bu_milestone_status_' + activeTabId).attr('disabled', true);
                }
            } else {
                $('#bu_milestone_status_' + activeTabId).attr('disabled', true);
            }
            // for (var i = d.length - 1; i >= 0; i--){
            //     if ("APPROVED" == d[i].approveStatus) {
            //         if (d[i].version == s_version) {
            //             $('#bu_milestone_status_' + activeTabId).attr('disabled', false);
            //             break;
            //         } else {
            //             $('#bu_milestone_status_' + activeTabId).attr('disabled', true);
            //             break;
            //         }
            //     }
            // }
        },
        complete: function () {},
        error: function () {
            window.parent.error("重新加载数据出错！");
        }
    });
}

//加载数据，返回里程碑结果集，准备生成树形结构
function reloadDataTree() {
    if (undefined == tab_l_version)
        return;
    $.ajax({
        url: config.baseurl + "/api/milestone/milestonelist1",
        data: {
            projectId: tab_l_projectId,
            version: tab_l_version
        },
        contentType: 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function () {

        },
        success: function (data) {
            reloadMilestone(data); //加载数据，生成树结构
        },
        complete: function () {
            fancyTreeExpanded();
        },
        error: function () {
            window.parent.error("重新加载数据出错！");
        }
    });
}

//查询按钮，按版本查询里程碑信息
function createQueryMilepostVersion() {
    $('#bu_queryMilepostVersion_' + activeTabId).on('click', function () {
        if (tab_checktype == 2) {
            if (confirm("请确认已保存调整计划")) {
                tab_checktype = 1;//更改显示类型
                allTabData[activeTabId].tab_checktype = tab_checktype;
                $('#bu_adj_' + activeTabId).attr('disabled', false);
                $('#bu_del_v_' + activeTabId).attr('disabled', false);
                $('#bu_add_' + activeTabId).attr('disabled', true);
                $('#bu_del_' + activeTabId).attr('disabled', true);
                $('#bu_saveas_' + activeTabId).attr('disabled', true);
                $('#bu_avgQuanzhong_' + activeTabId).attr('disabled', true);
                $('#bu_effective_' + activeTabId).attr('disabled', true);
                $('#bu_milestone_status_' + activeTabId).attr('disabled', true);

                tab_l_version = $("#s_milepost_version_" + activeTabId + " option:selected").val();
                allTabData[activeTabId].tab_l_version = tab_l_version;
                getMilestoneStatus();
                reloadDataTree();//加载数据
            }
        } else if(tab_checktype==0){
            // window.parent.error("没有已发布的工作计划！");
            tab_checktype=0;
            allTabData[activeTabId].tab_checktype = tab_checktype;
            return false;
        }
        else {
            tab_checktype = 1;//更改显示类型
            allTabData[activeTabId].tab_checktype = tab_checktype;
            $('#bu_adj_' + activeTabId).attr('disabled', false);
            $('#bu_del_v_' + activeTabId).attr('disabled', false);
            $('#bu_add_' + activeTabId).attr('disabled', true);
            $('#bu_del_' + activeTabId).attr('disabled', true);
            $('#bu_saveas_' + activeTabId).attr('disabled', true);
            $('#bu_avgQuanzhong_' + activeTabId).attr('disabled', true);
            $('#bu_effective_' + activeTabId).attr('disabled', true);
            $('#bu_milestone_status_' + activeTabId).attr('disabled', true);

            tab_l_version = $("#s_milepost_version_" + activeTabId + " option:selected").val();
            allTabData[activeTabId].tab_l_version = tab_l_version;

            getMilestoneStatus();
            reloadDataTree();//加载数据
        }

    });
}

//调整计划按钮
function createMilepostAdj() {
    $('#bu_adj_' + activeTabId).on('click', function () {
        if (0 == $('#s_milepost_version_' + activeTabId + ' option').length) {
            window.parent.warring("请联系系统管理员，维护设置启用阶段");
            return;
        }
        if($('#status_' + activeTabId)[0].value == '发布'){
            window.parent.showConfirm(confirmAdj1, "将现有版本进行升版操作，请确认是否调整");
        }else{
            window.parent.showConfirm(confirmAdj2, "请确认是否调整计划");
        }

    });

}

var  confirmAdj1=function () {
    var auditIng = 0;
    var nodeList = getTreeAllNode();
    nodeList.forEach(function (node) {
        if (0 <= node.data.parentId) {
            if ("N" == node.data.milestoneStatus && "APPROVING" == node.data.attribute1)
                auditIng = 1;
        }
    });
    if (1 == auditIng) {
        window.parent.warring("当前版本有里程碑正在审批中，不能进行升版");
        return;
    }

    tab_checktype = 2;//更改显示类型
    allTabData[activeTabId].tab_checktype = tab_checktype;
    $('#bu_adj_' + activeTabId).attr('disabled', true);
    $('#bu_del_v_' + activeTabId).attr('disabled', true);
    //调整计划后台取值
    $.ajax({
        url: config.baseurl + "/api/milestone/update",
        data: {
            projectId: tab_l_projectId,
            userId: 1
        },
        contentType: 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function () {

        },
        success: function (d) {
            tab_l_version = d;
            tab_l_version_max = d;
            allTabData[activeTabId].tab_l_version = tab_l_version;
            allTabData[activeTabId].tab_l_version_max = tab_l_version_max;
            // console.log(d);
            getMilestoneStatus();
            reloadDataTree();
            tab_initTables();//重新加载下拉列表
        },
        complete: function () {
        },
        error: function () {
            window.parent.error("重新加载数据出错！");
        }
    });
}

var  confirmAdj2=function () {
    tab_checktype = 2;//更改显示类型
    allTabData[activeTabId].tab_checktype = tab_checktype;
    $('#bu_adj_' + activeTabId).attr('disabled', true);
    $('#bu_del_v_' + activeTabId).attr('disabled', true);
    //调整计划后台取值
    $.ajax({
        url: config.baseurl + "/api/milestone/update",
        data: {
            projectId: tab_l_projectId,
            userId: 1
        },
        contentType: 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function () {

        },
        success: function (d) {
            tab_l_version = d;
            tab_l_version_max = d;
            allTabData[activeTabId].tab_l_version = tab_l_version;
            allTabData[activeTabId].tab_l_version_max = tab_l_version_max;
            // console.log(d);
            getMilestoneStatus();
            reloadDataTree();
            tab_initTables(0);//重新加载下拉列表
        },
        complete: function () {
        },
        error: function () {
            window.parent.error("重新加载数据出错！");
        }
    });
}
// var confirmAdj = function () {
//     if($('#status_' + activeTabId)[0].value == '发布'){
//         if (confirm("将现有版本进行升版操作，请确认是否调整")) {
//             // 如果有里程碑正在审批中，不允许升版
//             var auditIng = 0;
//             var nodeList = getTreeAllNode();
//             nodeList.forEach(function (node) {
//                 if (0 <= node.data.parentId) {
//                     if ("N" == node.data.milestoneStatus && "APPROVING" == node.data.attribute1)
//                         auditIng = 1;
//                 }
//             });
//             if (1 == auditIng) {
//                 window.parent.warring("当前版本有里程碑正在审批中，不能进行升版");
//                 return;
//             }
//
//             tab_checktype = 2;//更改显示类型
//             allTabData[activeTabId].tab_checktype = tab_checktype;
//             $('#bu_adj_' + activeTabId).attr('disabled', true);
//             $('#bu_del_v_' + activeTabId).attr('disabled', true);
//             //调整计划后台取值
//             $.ajax({
//                 url: config.baseurl + "/api/milestone/update",
//                 data: {
//                     projectId: tab_l_projectId,
//                     userId: 1
//                 },
//                 contentType: 'application/x-www-form-urlencoded',
//                 dataType: "json",
//                 type: "GET",
//                 beforeSend: function () {
//
//                 },
//                 success: function (d) {
//                     tab_l_version = d;
//                     tab_l_version_max = d;
//                     allTabData[activeTabId].tab_l_version = tab_l_version;
//                     allTabData[activeTabId].tab_l_version_max = tab_l_version_max;
//                     // console.log(d);
//                     getMilestoneStatus();
//                     reloadDataTree();
//                     tab_initTables();//重新加载下拉列表
//                 },
//                 complete: function () {
//                 },
//                 error: function () {
//                     window.parent.error("重新加载数据出错！");
//                 }
//             });
//         }
//     }else{
//         tab_checktype = 2;//更改显示类型
//         allTabData[activeTabId].tab_checktype = tab_checktype;
//         $('#bu_adj_' + activeTabId).attr('disabled', true);
//         $('#bu_del_v_' + activeTabId).attr('disabled', true);
//         //调整计划后台取值
//         $.ajax({
//             url: config.baseurl + "/api/milestone/update",
//             data: {
//                 projectId: tab_l_projectId,
//                 userId: 1
//             },
//             contentType: 'application/x-www-form-urlencoded',
//             dataType: "json",
//             type: "GET",
//             beforeSend: function () {
//
//             },
//             success: function (d) {
//                 tab_l_version = d;
//                 tab_l_version_max = d;
//                 allTabData[activeTabId].tab_l_version = tab_l_version;
//                 allTabData[activeTabId].tab_l_version_max = tab_l_version_max;
//                 // console.log(d);
//                 getMilestoneStatus();
//                 reloadDataTree();
//                 tab_initTables(0);//重新加载下拉列表
//             },
//             complete: function () {
//             },
//             error: function () {
//                 window.parent.error("重新加载数据出错！");
//             }
//         });
//     }
// }
//删除版本按钮
function deleteMilepostversion() {
    $('#bu_del_v_' + activeTabId).on('click', function () {

        window.parent.showConfirm(confirmdelete, "确认[删除该版本里程碑]？");
    });
}
var confirmdelete = function () {
    //如果有里程碑在审批，不能进行删除
    var auditIng = 0;
    var nodeList = getTreeAllNode();
    nodeList.forEach(function (node) {
        if (0 <= node.data.parentId) {
            if ("N" == node.data.milestoneStatus && "APPROVING" == node.data.attribute1)
                auditIng = 1;
        }
    });
    if (1 == auditIng) {
        window.parent.warring("当前版本有里程碑正在审批中，不能删除");
        return;
    }
    $.ajax({
        url: config.baseurl + "/api/milestone/deletev",
        contentType: "application/x-www-form-urlencoded",
        data: {
            projectId: tab_l_projectId,
            version:tab_l_version
        },
        dataType: "json",
        type: "GET",
        beforeSend: function () {
        },
        success: function (data) {
            if("S"==data.db_url){
                if(tab_l_version==1)
                    window.parent.success("版本号为1，已重置！");
                else
                    window.parent.success("删除版本成功！");
                tab_initTables(1);
            }else{
                window.parent.error(data.db_url);
            }
        },
        complete: function (data) {
        },
        error: function () {
            window.parent.error("重新加载数据出错！");
        }
    });
}
//新增按钮
function createMilepostAdd() {
    $('#bu_add_' + activeTabId).on('click', function () {
        var obj = [
            {title: " ", folder: false, phaseCode: 0}
        ];

        var tree = $("#treetable_" + activeTabId).fancytree("getTree"),
            selNodes = tree.getSelectedNodes();
        selNodes.forEach(function (node) {
            if (node.data.parentId == -1) {
                node.addChildren(obj);
            } else if (node.data.parentId > 0) {
                node.appendSibling(obj);
            } else if (node.data.parentId == -2 || node.data.parentId == null) {
                window.parent.error(" 请选中阶段或里程碑行再新增！");
            }
        });
        fancyTreeExpanded(); //展开树
    });
}

//删除按钮
function createMilepostDel() {
    $('#bu_del_' + activeTabId).on('click', function () {
        var tree = $("#treetable_" + activeTabId).fancytree("getTree"),
            selNodes = tree.getSelectedNodes();
        if (0 == selNodes.length) {
            window.parent.warring(valTips_selOne);
            return;
        }
        var no = 0;
        selNodes.forEach(function (node) {
            if (3 != node.getLevel()) {
                no++;
            }
        });
        if (0 < no) {
            window.parent.warring("请勾选里程碑后进行删除");
            return;
        }
        window.parent.showConfirm(confirmDel, "确认[删除]？");
    });
}

var confirmDel = function () {
    var tree = $("#treetable_" + activeTabId).fancytree("getTree"),
        selNodes = tree.getSelectedNodes();
    var l_name = "";
    selNodes.forEach(function (node) {
        if (node.data.ismark == "Y") {
            l_name = l_name + node.data.name + ",";
        }

    });
    if (l_name != "") {
        window.parent.warring(l_name + "该行里程碑已关联收款计划，请在收款计划中取消关联再删除！");
        return false;
    }
    selNodes.forEach(function (node) {
        if (3 == node.getLevel()) {
            while (node.hasChildren()) {
                node.getFirstChild().moveTo(node.parent, "child");
            }
            node.remove();
        }
    });
}

//获取树所有节点值
function getTreeAllNode() {
    var tree = $("#treetable_" + activeTabId).fancytree("getTree");
    var nodeList = [];
    tree.visit(function (node) {
        nodeList.push(node);
    });
    return nodeList;
}

//阶段权重检查
function getphaseWeight() {
    var nodeList = getTreeAllNode();//取所有的节点数据
    var phaseWeight = $("input[name='phaseWeight_" + activeTabId + "']");
    var num = 0;
    var Weight = 0;
    $(nodeList).each(function (i) {
        if (nodeList[i].data.parentId == -1) {
            Weight = Weight + parseFloat(phaseWeight[num].value);
            //console.log(  phaseWeight[num].value)
            num = num + 1;
            // console.log(phaseWeight[num].value) ;
        }
    });

    return Weight;
}

// 提交审批
function createEffective() {
    $('#bu_effective_' + activeTabId).on('click', function () {
        saveMilepost(1);
    });
}

function submitMilepost() {
    window.parent.showConfirm(function () {
        $.ajax({
            url: config.baseurl + "/api/milestone/selfApproval",
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",   //返回格式为json
            data: {
                proId: tab_l_projectId,
                no:  $('#s_milepost_version_' + activeTabId).val()
            },
            type: "POST",   //请求方式
            beforeSend: function () {
                window.parent.showLoading();
            },
            success: function (data) {
                if ("0000" == data.returnCode) {
                    window.parent.success("发布成功");
                    tab_checktype = 1;
                    allTabData[activeTabId].tab_checktype = tab_checktype;
                    // $('#bu_queryMilepostVersion_' + activeTabId).trigger("click");
                    // 刷新拉下列表
                    tab_initTables(1);
                    getApMilestone();
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
    }, "确认[发布]？");
}

//保存按钮
function createMilepostSave() {
    $('#bu_saveas_' + activeTabId).on('click', function () {
        saveMilepost();
    });

}

function saveMilepost(v) {
    var nodeList = getTreeAllNode();//取所有的节点数据
    var name = $("input[name='name_" + activeTabId + "']");
    var phaseWeight = $("input[name='phaseWeight_" + activeTabId + "']");
    var planStartDate = $("input[name='planStartDate_" + activeTabId + "']");
    var planEndDate = $("input[name='planEndDate_" + activeTabId + "']");
    var weightisnull=0;
    var planStartDateisnull=0;
    var planEndDateisnull=0;
    var parentnum = 0;
    var milestonenum=0;
    var Weight = 0;
    // 判断计划结束时间
    var planDateMsg = "";
    $(nodeList).each(function (i) {
        if (nodeList[i].data.parentId == -1) {
            if(phaseWeight[parentnum].value==""){//权重为空判断
                weightisnull=1;
            }else if(planStartDate[parentnum].value==""){//计划开始 时间为空判断
                planStartDateisnull=1
            }
            Weight = Weight + parseFloat(phaseWeight[parentnum].value);
            parentnum = parentnum + 1;
            milestonenum = milestonenum + 1;
        }else if(nodeList[i].data.parentId >0){//计划完成时间为空判断
            if(planEndDate[milestonenum].value==""){
                planEndDateisnull=1;
            }
            milestonenum = milestonenum + 1;
        }

    });
    if(weightisnull==1){
        window.parent.error("有未填写的阶段权重！");
        return false;
    }
    if(Weight!=100){
        window.parent.error("阶段权重之和不等于100！");
        return false;
    }
    if(planStartDateisnull==1){
        window.parent.error("有未填写的计划开始时间！");
        return false;
    }
    if(planEndDateisnull==1){
        window.parent.error("有未填写的计划完成时间！");
        return false;
    }

    $(nodeList).each(function (i) {
        $('#treetable_' + activeTabId + ' tbody tr:eq(' + (i) + ')').css("backgroundColor", "");
        var node = nodeList[i];
        var tmp_tr = node.tr;
        // 阶段
        if (0 != node.data.phaseCode && 1 < node.getLevel()) {
            var startTd = $(tmp_tr).find('td:eq(6)');
            var endTd = $(tmp_tr).find('td:eq(7)');
            var input = $(startTd[0]).find('input')[0];
            var input2 = $(endTd[0]).find('input')[0];
            if (input.value > input2.value) {
                planDateMsg += "第" + (i + 1) + "行计划完成时间小于计划开始时间！<br>";
                $('#treetable_' + activeTabId + ' tbody tr:eq(' + (i) + ')').css("backgroundColor", "red");
            }
            // // 如果有父节点
            // if (null != node.parent) {
            //     var parentNode = node.parent;
            //     var planStartDate = parentNode.data.planStartDate;
            //     var planEndDate = parentNode.data.planEndDate;
            //     if (input.value < planStartDate || input.value > planEndDate) {
            //         planDateMsg += "第" + (i + 1) + "行计划开始时间不在项目时间范围内！<br>";
            //         $('#treetable_' + activeTabId + ' tbody tr:eq(' + (i) + ')').css("backgroundColor", "red");
            //     }
            //     if (input2.value > planEndDate || input2.value < planStartDate) {
            //         planDateMsg += "第" + (i + 1) + "行计划完成时间不在项目时间范围内！<br>";
            //         $('#treetable_' + activeTabId + ' tbody tr:eq(' + (i) + ')').css("backgroundColor", "red");
            //     }
            // }
            if ("" != input.value) {
                $.ajax({
                    url : config.baseurl +"/api/milestone/validataPlanDate",
                    contentType : 'application/x-www-form-urlencoded',
                    dataType:"json",
                    async: false,
                    data: {
                        proId: tab_l_projectId,
                        phaseCode: node.data.phaseCode,
                        date: input.value
                    },
                    type:"POST",
                    beforeSend:function(){},
                    success:function(data){
                        if ("0000" != data.returnCode) {
                            planDateMsg += "第" + (i + 1) + "行，计划开始时间" + data.returnMsg + "<br>";
                            $('#treetable_' + activeTabId + ' tbody tr:eq(' + (i) + ')').css("backgroundColor", "red");
                        }
                    },
                    complete:function(){},
                    error:function(){}
                });
            }
            if ("" != input2.value) {
                $.ajax({
                    url : config.baseurl +"/api/milestone/validataPlanDate",
                    contentType : 'application/x-www-form-urlencoded',
                    dataType:"json",
                    async: false,
                    data: {
                        proId: tab_l_projectId,
                        phaseCode: node.data.phaseCode,
                        date: input2.value
                    },
                    type:"POST",
                    beforeSend:function(){},
                    success:function(data){
                        if ("0000" != data.returnCode) {
                            planDateMsg += "第" + (i + 1) + "行，计划结束时间" + data.returnMsg + "<br>";
                            $('#treetable_' + activeTabId + ' tbody tr:eq(' + (i) + ')').css("backgroundColor", "red");
                        }
                    },
                    complete:function(){},
                    error:function(){}
                });
            }
        } else if (0 == node.data.phaseCode && 2 < node.getLevel()) { // 里程碑
            var endTd = $(tmp_tr).find('td:eq(7)');
            var input2 = $(endTd[0]).find('input')[0];
            // 如果有父节点
            if (null != node.parent) {
                var parentNode = node.parent;
                var parentTr = parentNode.tr;
                var parentStartTd = $(parentTr).find('td:eq(6)');
                var parentEndTd = $(parentTr).find('td:eq(7)');
                var parentInput1 = $(parentStartTd[0]).find('input')[0];
                var parentInput2 = $(parentEndTd[0]).find('input')[0];
                if (input2.value > parentInput2.value || input2.value < parentInput1.value) {
                    planDateMsg += "第" + (i + 1) + "行计划完成时间不在阶段时间范围内！<br>";
                    $('#treetable_' + activeTabId + ' tbody tr:eq(' + (i) + ')').css("backgroundColor", "red");
                }
            }
        } else if (0 == node.data.phaseCode && 2 == node.getLevel()) { // 里程碑
            var endTd = $(tmp_tr).find('td:eq(7)');
            var input2 = $(endTd[0]).find('input')[0];
            // 如果有父节点
            if (null != node.parent) {
                var planStartDate = node.parent.data.planStartDate;
                var planEndDate = node.parent.data.planEndDate;
                if (input2.value > planEndDate || input2.value < planStartDate) {
                    planDateMsg += "第" + (i + 1) + "行计划完成时间不在项目时间范围内！<br>";
                    $('#treetable_' + activeTabId + ' tbody tr:eq(' + (i) + ')').css("backgroundColor", "red");
                }
            }
        }

    });


    if("" != planDateMsg){
        window.parent.error(planDateMsg);
        return false;
    }

    //console.log(name[0].value);
    var o;
    var l_num = 0;//阶段数量
    var l_seq = 0;//排序
    var l_parent;//新增节点的父节点
    var l_project_id;
    var approve_status;
    var arr = new Array();
    console.log(nodeList);
    $(nodeList).each(function (i) {
        o = new Object();
        o.milestoneId = nodeList[i].data.milestoneId;
        o.projectId = nodeList[i].data.projectId;
        if (nodeList[i].data.parentId == -2) {//项目

            o.parentId = nodeList[i].data.parentId;//-2，取原值
            o.name = nodeList[i].data.name;
            o.phaseWeight = nodeList[i].data.phaseWeight;
            o.planStartDate = nodeList[i].data.planStartDate;
            o.planEndDate = nodeList[i].data.planEndDate;
            o.version = nodeList[i].data.version;
            o.approveStatus = nodeList[i].data.approveStatus;
            o.seqNum = nodeList[i].data.seqNum;
            l_num = l_num + 1;
        } else if (nodeList[i].data.parentId == -1) {//阶段
            l_seq = 0;//重置排序，用于新增里程碑用
            l_project_id = nodeList[i].data.projectId;   //取阶段projectid，给新增里程碑用
            approve_status = nodeList[i].data.approveStatus;//取阶段status，给新增里程碑用
            o.name = nodeList[i].data.name;
            if(phaseWeight[l_num - 1].value==""){
                window.parent.warring("权重未填写！");
                return false;
            }
            o.phaseWeight = phaseWeight[l_num - 1].value;//取前台写入的值，需要排除不是输入框的顺序

            o.planStartDate = planStartDate[l_num - 1].value;
            o.planEndDate = planEndDate[i - 1].value;
            o.parentId = nodeList[i].data.parentId;
            o.version = nodeList[i].data.version;
            o.approveStatus = nodeList[i].data.approveStatus;
            o.seqNum = nodeList[i].data.seqNum;
            l_num = l_num + 1;
            l_parent = nodeList[i].data.milestoneId;//取当前阶段的ID,作为新增行的父节点
        }
        else {
            l_seq = l_seq + 1;

            if (nodeList[i].data.milestoneId == null) {//新增里程碑
                o.milestoneId = null;
                o.projectId = l_project_id;
                o.name = name[i - l_num].value;
                o.phaseWeight = null;
                o.planStartDate = null;
                o.planEndDate = planEndDate[i - 1].value;
                o.parentId = l_parent;
                o.version = tab_l_version;
                o.approveStatus = approve_status;
            } else {   //更新里程碑
                o.name = name[i - l_num].value;
                o.phaseWeight = nodeList[i].data.phaseWeight;
                o.planStartDate = nodeList[i].data.planStartDate;
                o.planEndDate = planEndDate[i - 1].value;
                o.parentId = nodeList[i].data.parentId;
                o.version = nodeList[i].data.version;
                o.approveStatus = nodeList[i].data.approveStatus;
            }
            o.seqNum = l_seq;
        }
        arr.push(o);
    });

    console.log(JSON.stringify(arr));

    $.ajax({
        url: config.baseurl + "/api/milestone/save",
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: JSON.stringify(arr),
        type: "POST",
        beforeSend: function () {
            window.parent.showLoading();
        },
        success: function (d) {
            console.log(d);
            tab_l_version = d;
            tab_l_version_max = d;
            allTabData[activeTabId].tab_l_version = tab_l_version;
            allTabData[activeTabId].tab_l_version_max = tab_l_version_max;
            //getMilestoneStatus();
            reloadDataTree();
            window.parent.success("保存成功！");
            if (v == 1)
                submitMilepost();
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

function createAvgQuanzhongBu() {
    $('#bu_avgQuanzhong_' + activeTabId).on('click', function () {
        avgQuanzhong();
    });
}

// 均分权重
function avgQuanzhong() {
    var inputs = $('input[name="phaseWeight_' + activeTabId + '"]');
    var tree = $("#treetable_" + activeTabId).fancytree("getTree"),
        selNodes = tree.getSelectedNodes();

    if (0 == selNodes.length) {
        window.parent.warring("请至少勾选一个阶段");
        return;
    }

    var usedNo = [];
    var setNo = [];
    var usedValue = 0;
    var avgSum = 0;
    inputs.each(function (i) {
        usedNo.push(i);
    });
    selNodes.forEach(function (node) {
        avgSum++;
        var tmp_td = $(node.tr).find("td:eq(3)");
        var tmp_input = tmp_td.find('input')[0];
        var id = inputs.index(tmp_input);
        for(var i = 0; i < usedNo.length; i++) {
            if(usedNo[i] == id) {
                usedNo.splice(i, 1);
                break;
            }
        }
        setNo.push(inputs.index(tmp_input));
    });

    for (var i = 0; i < usedNo.length; i++) {
        var tmpValue = inputs[usedNo[i]].value;
        if ("" != tmpValue.trim())
            usedValue += parseFloat(tmpValue.trim());
    }

    var sumTotal = 100 - usedValue;
    for (var i = 0; i < setNo.length; i++) {
        var input = inputs[setNo[i]];
        if (1 != avgSum) {
            var v = parseInt(sumTotal / avgSum);
            input.value = v;
            sumTotal -= v;
            avgSum--;
        } else{
            input.value = sumTotal;
        }
    }
    setMilePostPercent();
}

//更新里程碑为完成状态
function createMilepostComplete() {
    $('#complete_' + activeTabId).on('click', function () {
        updateMilestoneStatus('Y');
        // milestoneIds = [];//重置返回数组
        // allTabData[activeTabId].milestoneIds = milestoneIds;
        // getMilestoneStatus();
        // reloadDataTree();
    });
}

function createMilepostUncomplete() {
    $('#uncomplete_' + activeTabId).on('click', function () {
        var tree = $("#treetable_" + activeTabId).fancytree("getTree"),
            selNodes = tree.getSelectedNodes();

        if (0 == selNodes.length) {
            window.parent.warring("请勾选一个里程碑");
            return;
        }

        var flag = 0;
        selNodes.forEach(function (node) {
            if (3 == node.getLevel() && "N" == node.data.milestoneStatus && "APPROVING" == node.data.attribute1)
                flag = 1;
        });
        if (1 == flag) {
            window.parent.warring("有里程碑正在审批中，不允许设置为未完成");
            return;
        }

        window.parent.showConfirm(function () {
            updateMilestoneStatus('N');
        }, "更新里程碑状态为未完成后，再次更新状态为完成时需要重新审批，是否确认更改？");
        // milestoneIds = [];//重置返回数组
        // allTabData[activeTabId].milestoneIds = milestoneIds;
        // getMilestoneStatus();
        // reloadDataTree();
    });
}

//更新里程碑完成情况
function updateMilestoneStatus(status) {
    var tree = $("#treetable_" + activeTabId).fancytree("getTree"),
        selNodes = tree.getSelectedNodes();
    var arr = [];
    selNodes.forEach(function (node) {
        if (3 == node.getLevel())
            arr.push(node.data.milestoneId);
    });

    if (0 == arr.length) {
        window.parent.warring("请勾选一个里程碑");
        return;
    } else if (1 < arr.length) {
        window.parent.warring("只能勾选一个里程碑");
        return;
    }

    $.ajax({
        url: config.baseurl + "/api/milestone/submitOaApproved",
        contentType: 'application/x-www-form-urlencoded',
        dataType: "json",   //返回格式为json
        data: {
            milestoneId: arr[0],
            status: status
        },
        type: "POST",   //请求方式
        beforeSend: function () {
            window.parent.showLoading();
        },
        success: function (data) {
            if ("0000" == data.returnCode) {
                window.parent.success("更新成功");
                tab_checktype = 1;
                allTabData[activeTabId].tab_checktype = tab_checktype;
                $('#bu_queryMilepostVersion_' + activeTabId).trigger("click");
                if ("Y" == status && null != data.db_url) {
                    window.open(data.db_url);
                }
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
}



