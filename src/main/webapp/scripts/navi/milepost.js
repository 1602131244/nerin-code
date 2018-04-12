var config = new nerinJsConfig();
var l_projectId = config.projectId ;
var l_version;
var l_version_max;
var milestoneIds = [];
var checktype = 1;
//var l_status;

$(function () {
    
    console.log("page loaded");
    //按钮控制,初始只能用查询
    $('#bu_adj').attr('disabled', true);
    $('#bu_add').attr('disabled', true);
    $('#bu_del').attr('disabled', true);
    $('#bu_saveas').attr('disabled', true);
    $('#bu_effective').attr('disabled', true);
    $('#bu_milestone_status').attr('disabled', true);
    //检查是否有已发布的工作计划，如果没有则弹出提示
    $.ajax({     //异步，调用后台get方法
        url: config.baseurl + "/api/milestone/workplan", //调用地址，不包含参数
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: {
            projectId: l_projectId  //传入参数，依次填写
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {

        },
        success: function (data) {
        if(data==0){
            window.parent.error("没有已发布的工作计划！");
            checktype=0;
            return false;

        }else {
            initTables();
        }


        },
        complete: function () {

        },
        error: function () {
            window.parent.error(ajaxError_sys);
        }
    });

    // $('#bu_milestone_status').addClass('disabled');

    //版本下拉列表数据

})

function initTables() { //取值集
    $('#s_milepost_version').empty();//清空下拉表

    $.ajax({     //异步，调用后台get方法
        url: config.baseurl + "/api/milestone/version", //调用地址，不包含参数
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: {
            projectId: l_projectId  //传入参数，依次填写
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {

        },
        success: function (data) {
            var list = data.dataSource;

            list.forEach(function (data) {  //取最大版本
                l_version = data.version;
                l_version_max = data.version;
            });

            list.forEach(function (data) {  //下拉列表，如果是最大版本，则默认选中
                if (data.version == l_version_max) {
                    $('#s_milepost_version').append("<option value='" + data.version + "'selected>" + data.version + "</option>");

                } else {
                    $('#s_milepost_version').append("<option value='" + data.version + "'>" + data.version + "</option>");
                }
                ;


            });

        },
        complete: function () {

        },
        error: function () {
            window.parent.error(ajaxError_sys);
        }
    });

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

var showData = [];
//加载 生成树型结构的数据
function reloadMilestone(data) {
    //saveTemplateId = "";
    //delTemplateChapterIds = [];
    showData = [];
    // saveTemplateId = data[0].templateHeaderId;

    for (var a = 0; a < data.length; a++) {
        showData.push(data[a]);
    }
    //rootData = data[0];
    var tree = $("#treetable").fancytree("getTree");
    tree.reload(showData).done(function () {
        if(checktype==2){
            $("div[name=pStartDate]").datetimepicker({
                format: 'YYYY-MM-DD'
            });
            $("div[name=planEndDate]").datetimepicker({
                format: 'YYYY-MM-DD'
            });
        }

        fancyTreeExpanded();
        fancyTreeeCollapse();
    });
}
//展开树
function fancyTreeExpanded() {
    $("#treetable").fancytree("getRootNode").visit(function (node) {
        node.setExpanded(true);
    });
}
//收回树
function fancyTreeeCollapse() {
    $("#treetable").fancytree("getRootNode").visit(function (node) {
        node.setExpanded(false);
    });
}

$("#treetable").fancytree({
    selectMode: 3,
    extensions: ["dnd", "edit", "glyph", "table"],
    checkbox: true,
    keyboard: false,
    dnd: {
        focusOnClick: true,
        dragStart: function (node, data) {
            return true;
        },
        dragEnter: function (node, data) {
            return true;
        },
        dragDrop: function (node, data) {
            data.otherNode.copyTo(node, data.hitMode);
        }
    },
    glyph: glyph_opts,
    source: showData,
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
        var datecol;
        datecol="<span class='input-group-addon' style='padding: 3px 8px; font-size: 13px;'><span class='glyphicon glyphicon-calendar'></span></span>";
        var node = data.node,
            $tdList = $(node.tr).find(">td");
        //console.log(node.data.chapterName);
        // $tdList.eq(1).text(node.getIndexHier());//上下级的关联
        var col2 = null != node.data.name ? node.data.name : "";
        var col3 = null != node.data.phaseWeight ? node.data.phaseWeight : "";//阶段权重
        var col4 = null != node.data.phasePercent ? node.data.phasePercent : "";//估算进度
        var col5 = null != node.data.percent ? node.data.percent : "";//里程碑完成情况
        var col51 = null != node.data.milestoneStatus ? node.data.milestoneStatus : "";//里程碑完成情况
        var col6 = null != node.data.planStartDate ? node.data.planStartDate : "";//计划开始时间
        var col7 = null != node.data.planEndDate ? node.data.planEndDate : "";//计划完成时间
        var col8 = null != node.data.actualCompleteDate ? node.data.actualCompleteDate : "";//计划完成时间
        if (checktype == 1) {//查询，不显示输入框
            if (-2 == node.data.parentId) {
                $tdList.eq(2).text(col2);
                $tdList.eq(4).text(col4);
                $tdList.eq(5).text(col5);
            }
            else if (-1 == node.data.parentId) {//如果是阶段
                $tdList.eq(2).text(col2);
                $tdList.eq(3).text(col3);
                $tdList.eq(4).text(col4);
                $tdList.eq(5).text(col5);
                $tdList.eq(6).text(col6);
                $tdList.eq(7).text(col7);
            } else {
                $tdList.eq(2).text(col2);
                if(col51=="Y"){
                    $tdList.eq(5).html("<i class='fa fa-check'></i>")
                }else{
                    $tdList.eq(5).text("");
                }
                //
                $tdList.eq(7).text(col7);
            }
            $tdList.eq(8).text(col8);
        } else { //调整计划，打开输入框
            if (-2 == node.data.parentId) {
                $tdList.eq(2).text(col2);
                $tdList.eq(4).text(col4);
                $tdList.eq(5).text(col5);
            }
            else if (-1 == node.data.parentId) {//如果是阶段
                $tdList.eq(2).text(col2);
                $tdList.eq(3).html("<input type='text' style='margin-left: 5px; width: 85%; display: inline-block;' required='required'  name='phaseWeight' class='form-control' value='" + col3 + "' />");
                $tdList.eq(4).text(col4);
                $tdList.eq(5).text(col5);
                $tdList.eq(6).html("<div class='input-group date' name='pStartDate'><input type='text' class='form-control input-sm' name='planStartDate' value='" + col6 + "'/>"+datecol+"</div>");
                $tdList.eq(7).html("<div class='input-group date' name='planEndDate'><input type='text' class='form-control input-sm' name='planEndDate' value='" + col7 + "'/>"+datecol+"</div>");
            } else {
                $tdList.eq(2).html("<input type='text' style='margin-left: 5px; width: 85%; display: inline-block;' required='required' name='name' class='form-control' value='" + col2 + "' />");

                $tdList.eq(5).text(col51);
                $tdList.eq(7).html("<div class='input-group date' name='planEndDate'><input type='text' class='form-control input-sm' name='planEndDate' value='" + col7 + "'/>"+datecol+"</div>");
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

//加载数据，返回里程碑审批状态,控制按钮
function getMilestoneStatus() {
    $.ajax({
        url: config.baseurl + "/api/milestone/status",
        data: {
            projectId: l_projectId,
            version: l_version
        },
        contentType: 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function () {

        },
        success: function (data) {
            $('#s_milepost_version').val(l_version);
            // l_status=data.status
            $('#status').val(data.status);
            console.log($('#status')[0].value);
            //console.log(l_version);
            //console.log(l_version_max);
            if (checktype == 1) {  //查询
                if (l_version == l_version_max && $('#status')[0].value != '审批中') { //如果查询的是最大版本，且状态不是审批中，则按钮亮
                    $('#bu_adj').attr('disabled', false);
                    ;
                    console.log("22");

                    $('#bu_add').attr('disabled', true);
                    $('#bu_del').attr('disabled', true);
                    $('#bu_saveas').attr('disabled', true);
                    $('#bu_effective').attr('disabled', true);
                    // $('#bu_milestone_status').removeClass('disabled');
                    if ($('#status')[0].value == '批准') {//版本为最大，状态为批准，可更新里程碑
                        $('#bu_milestone_status').attr('disabled', false);
                    }
                }
            } else { //调整
                $('#bu_add').attr('disabled', false);
                $('#bu_del').attr('disabled', false);
                $('#bu_saveas').attr('disabled', false);
                $('#bu_effective').attr('disabled', false);
            }

        },
        complete: function () {
        },
        error: function () {
            window.parent.error("重新加载数据出错1111！");
            $('#add_modal').on('shown.bs.modal', function (e) {
            }).modal('toggle');
        }
    });
}

//加载数据，返回里程碑结果集，准备生成树形结构
function reloadDataTree() {
    $.ajax({
        url: config.baseurl + "/api/milestone/milestonelist1",
        data: {
            projectId: l_projectId,
            version: l_version
        },
        contentType: 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function () {

        },
        success: function (data) {

            reloadMilestone(data); //加载数据，生成树结构
            //  console.log(data);
        },
        complete: function () {
            fancyTreeExpanded();
        },
        error: function () {
            window.parent.error("重新加载数据出错！");
            $('#add_modal').on('shown.bs.modal', function (e) {
            }).modal('toggle');
        }
    });
}
//查询按钮，按版本查询里程碑信息
$('#bu_queryMilepostVersion').on('click', function () {
    if (checktype == 2) {
        if (confirm("请确认已保存调整计划")) {
            checktype = 1;//更改显示类型
            $('#bu_adj').attr('disabled', false);
            $('#bu_add').attr('disabled', true);
            $('#bu_del').attr('disabled', true);
            $('#bu_saveas').attr('disabled', true);
            $('#bu_effective').attr('disabled', true);
            $('#bu_milestone_status').attr('disabled', true);

            l_version = $("#s_milepost_version option:selected").val();

            getMilestoneStatus();
            reloadDataTree();//加载数据
        }
    } else if(checktype==0){
        window.parent.error("没有已发布的工作计划！");
        checktype=0;
        return false;
    }
    else {
        checktype = 1;//更改显示类型
        $('#bu_adj').attr('disabled', false);
        $('#bu_add').attr('disabled', true);
        $('#bu_del').attr('disabled', true);
        $('#bu_saveas').attr('disabled', true);
        $('#bu_effective').attr('disabled', true);
        $('#bu_milestone_status').attr('disabled', true);

        l_version = $("#s_milepost_version option:selected").val();

        getMilestoneStatus();
        reloadDataTree();//加载数据
    }

});


//调整计划按钮
$('#bu_adj').on('click', function () {
    if($('#status')[0].value == '批准'){
        if (confirm("调整计划将会将现有版本升版为编辑状态，请确认是否调整")) {
            checktype = 2;//更改显示类型
            $('#bu_adj').attr('disabled', true);
            //调整计划后台取值
            $.ajax({
                url: config.baseurl + "/api/milestone/update",
                data: {
                    projectId: l_projectId,
                    userId: 1
                },
                contentType: 'application/x-www-form-urlencoded',
                dataType: "json",
                type: "GET",
                beforeSend: function () {

                },
                success: function (d) {
                    l_version = d;
                    l_version_max = d;
                    // console.log(d);
                    getMilestoneStatus();
                    reloadDataTree();
                    initTables();//重新加载下拉列表
                },
                complete: function () {
                },
                error: function () {
                    window.parent.error("重新加载数据出错！");
                    $('#add_modal').on('shown.bs.modal', function (e) {
                    }).modal('toggle');
                }
            });
        }
    }else{
        checktype = 2;//更改显示类型
        $('#bu_adj').attr('disabled', true);
        //调整计划后台取值
        $.ajax({
            url: config.baseurl + "/api/milestone/update",
            data: {
                projectId: l_projectId,
                userId: 1
            },
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",
            type: "GET",
            beforeSend: function () {

            },
            success: function (d) {
                l_version = d;
                l_version_max = d;
                // console.log(d);
                getMilestoneStatus();
                reloadDataTree();
                initTables();//重新加载下拉列表
            },
            complete: function () {
            },
            error: function () {
                window.parent.error("重新加载数据出错！");
                $('#add_modal').on('shown.bs.modal', function (e) {
                }).modal('toggle');
            }
        });
    }
})
//新增按钮
$('#bu_add').on('click', function () {
    var obj = [
        {title: " ", folder: false}
    ];

    var tree = $("#treetable").fancytree("getTree"),
        selNode = tree.getActiveNode();

    if (selNode.data.parentId == -1) {
        selNode.addChildren(obj);
    } else if (selNode.data.parentId > 0) {
        selNode.appendSibling(obj);
    } else if (selNode.data.parentId == -2 || selNode.data.parentId == null) {
        window.parent.error(" 请选中阶段或里程碑行再新增！");
    }

    fancyTreeExpanded();

});
//删除按钮
$('#bu_del').on('click', function () {
    var num=0;
    var phasenum=0;
    var tree = $("#treetable").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();
    var l_name = "";
    selNodes.forEach(function (node) {
        if (node.data.ismark == "Y") {
            l_name = l_name + node.data.name + ",";
        }
        if(node.data.parentId==-1||node.data.parentId==-2){
            phasenum=phasenum+1;
        }
        num=num+1;
    });
    if (l_name != "") {
        window.parent.warring(l_name + "该行里程碑已关联收款计划，请在收款计划中取消关联再删除！");
        return false;
    }
    if (num ==phasenum) {
        window.parent.warring(l_name + "请选中里程碑节点再删除！");
        return false;
    }
    selNodes.forEach(function (node) {
        if(node.data.parentId>1){
            while (node.hasChildren()) {
                node.getFirstChild().moveTo(node.parent, "child");
            }
            node.remove();

        }

    });

});


//获取树所有节点值
function getTreeAllNode() {
    var tree = $("#treetable").fancytree("getTree");
    var nodeList = [];
    tree.visit(function (node) {
        nodeList.push(node);
    });
    return nodeList;
}
//阶段权重检查
function getphaseWeight() {
    var nodeList = getTreeAllNode();//取所有的节点数据
    var phaseWeight = $("input[name='phaseWeight']");
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

//保存按钮
$('#bu_saveas').on('click', function () {

    var nodeList = getTreeAllNode();//取所有的节点数据
    var name = $("input[name='name']");
    var phaseWeight = $("input[name='phaseWeight']");
    var planStartDate = $("input[name='planStartDate']");
    var planEndDate = $("input[name='planEndDate']");
    var weightisnull=0;
    var milename=0;
    var planStartDateisnull=0;
    var planEndDateisnull=0;
    var parentnum = 0;
    var milestonenum=0;
    var Weight = 0;
    $(nodeList).each(function (i) {
        if (nodeList[i].data.parentId == -1) {
            if(phaseWeight[parentnum].value==""){//权重为空判断
                weightisnull=1;
            }else if(planStartDate[parentnum].value==""){//计划开始 时间为空判断
                planStartDateisnull=1
            }else if(planEndDate[i-1].value==""){
                planEndDateisnull=1;
            }
            Weight = Weight + parseFloat(phaseWeight[parentnum].value);
            parentnum = parentnum + 1;
        }else if(nodeList[i].data.parentId >0){//计划完成时间为空判断
            console.log(milestonenum);
            if(planEndDate[i-1].value==""){
                planEndDateisnull=1;
            }else if(name[milestonenum].value==""){
                milename=1;
            }
            milestonenum = milestonenum + 1;
        }
    });
   // console.log(milestonenum);
    if(milename==1){
        window.parent.error("有未填写的里程碑名称！");
        return false;
    }
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

    //console.log(name[0].value);
    var o;
    var l_num = 0;//阶段数量
    var l_seq = 0;//排序
    var l_parent;//新增节点的父节点
    var l_project_id;
    var approve_status;
    var arr = new Array();
   // console.log(nodeList);
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
                o.version = l_version;
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
        },
        success: function (d) {
            console.log(d);
            l_version = d;
            l_version_max = d;
            //getMilestoneStatus();
            reloadDataTree();
            window.parent.success("保存成功！");
        },
        complete: function () {
        },
        error: function () {
            window.parent.error(ajaxError_sys);
        }
    });

});

//更新里程碑为完成状态
$('#complete').on('click', function () {
    updateMilestoneStatus('Y');
    milestoneIds = [];//重置返回数组
    getMilestoneStatus();
    reloadDataTree();
});
$('#uncomplete').on('click', function () {
    updateMilestoneStatus('N');
    milestoneIds = [];//重置返回数组

    getMilestoneStatus();

    reloadDataTree();
});

//更新里程碑完成情况
function updateMilestoneStatus(status) {

    var tree = $("#treetable").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();
    selNodes.forEach(function (node) {
        if (node.data.parentId > 0)
            milestoneIds.push(node.data.milestoneId);//拼选中的行的字符串列
    });
    // if(milestoneIds==[]){
    //     window.parent.error("请先选中需要更改的行！");
    //     return false;
    // }
    var strings = "";
    strings = milestoneIds.toString();
    // console.log(strings);
    // console.log(status);
    $.ajax({
        url: config.baseurl + "/api/milestone/updateMilestoneStatus",
        data: {
            milestoneIds: strings,
            status: status
        },
        contentType: 'application/x-www-form-urlencoded',
        dataType: "text",
        type: "GET",
        beforeSend: function () {

        },
        success: function (d) {
            console.log(d);
            window.parent.error(d);
        },
        complete: function () {
        },
        error: function () {
            window.parent.error("重新加载数据出错2222！");

        }
    });
}
