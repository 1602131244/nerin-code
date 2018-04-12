// 表格全选
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

function proRoleChanged(sel) {
    var _index = $('select[name="sel_proRole"]').index(sel);
    addSpecialtySelOptions($(sel).children('option:selected').val(), _index);
}

// 填充分配专业下拉框
function addSpecialtySelOptions(proRole, _index) {
    var sel_specialty = $('select[name="sel_specialty"]')[_index];
    specialtys.forEach(function (data) {
        $(sel_specialty).append("<option value='" + data.specialty + "'>" + data.specialtyName + "</option>");
    });
}

function addTemplateCover(templateId) {
    $('#templateCover tbody').empty();
    $.ajax({
        url: config.baseurl +"/api/templateRest/templateChapters",
        data: {
            templateId: templateId,
            lineType: "COVER"
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        success: function(data) {
            var d = data;
            var projectRoleId = "";
            var specialty = "";
            var xdoTemplateId = "";
            var templateCoverId = "";
            if (null != d && 1 == d.length) {
                projectRoleId = undefined != d[0].projectRoleId ? d[0].projectRoleId : "";
                specialty = undefined != d[0].specialty ? d[0].specialty : "";
                xdoTemplateId = undefined != d[0].xdoTemplateId ? d[0].xdoTemplateId : "";
                templateCoverId = undefined != d[0].chapterId ? d[0].chapterId : "";
            }
            var table = $('#templateCover');
            var row = $("<tr></tr>");
            var td = $("<td width='140'></td>");
            var td2 = $("<td width='140'></td>");
            var td3 = $("<td width='280'></td>");
            var proRole = createProRoleSel(projectRoleId, "sel_proRole_cover");
            td.append(proRole).append($('<input id="templateCoverId" type="hidden" value="' + templateCoverId +'"/>'));
            var specialt = createSpecialt(specialty, "sel_specialty_cover");
            td2.append(specialt);
            var xml = createXmlTemplateSel(xdoTemplateId, "sel_xmlTemplate_cover");
            td3.append(xml);
            row.append(td).append(td2).append(td3);
            table.append(row);
            // $(proRole).selectpicker('refresh');
            // $(proRole).selectpicker('show');
            // $(specialt).selectpicker('refresh');
            // $(specialt).selectpicker('show');
        }
    });
}

//$(function () {

function initSelectpicker(sel) {
    if (!$(sel).hasClass("selectpicker")) {
        $(sel).selectpicker('refresh');
        $(sel).selectpicker('show');
        $(sel).addClass("selectpicker");
    }
}

    // 初始化项目角色下拉
    var createProRoleSel = function (val, name) {
        var select = $("<select name='" + (name ? name : 'sel_proRole') + "' class='form-control input-sm' placeholder='请选择' data-live-search='true' data-size='8' data-dropup-auto='false' onmouseover='javascript:initSelectpicker(this);'></select>");
        select.append("<option value=''>请选择</option>");
        for (var i = 0; i < projectRoles.length; i++) {
            select.append("<option value='" + projectRoles[i].projectRoleId + "'>" + projectRoles[i].projectRoleName + "</option>")
        }
        select.val(val);
        return select;
    };

    // 初始化分配专业下拉
    var createSpecialtyNull = function () {
        var select = $("<select name='sel_specialty' class='form-control input-sm' placeholder='请选择'></select>");
        select.append("<option value=''>请选择</option>");
        select.val("");
        return select;
    }

    var createSpecialt = function (val, name) {
        var select = $("<select name='" + (name ? name : 'sel_specialty') + "' class='form-control input-sm' placeholder='请选择' data-live-search='true' data-size='8' data-dropup-auto='false' onmouseover='javascript:initSelectpicker(this);'></select>");
        select.append("<option value=''>请选择</option>");
        for (var i = 0; i < specialtys.length; i++) {
            select.append("<option value='" + specialtys[i].specialty + "'>" + specialtys[i].specialtyName + "</option>")
        }
        select.val(val);
        return select;
    }

    // 初始化XML下拉
    var createXmlTemplateSel = function (val, name) {
        var select = $("<select name='" + (name ? name : 'sel_xmlTemplate') + "' class='form-control input-sm' placeholder='请选择'></select>");
        select.append("<option value=''>请选择</option>");
        for (var i = 0; i < xmlTemplates.length; i++) {
            select.append("<option value='" + xmlTemplates[i].xdoTemplateId + "'>" + xmlTemplates[i].xdoTemplateName + "</option>")
        }
        select.val(val);
        return select;
    };

    // 初始化系统链接下拉
    var createSystemUiSel = function (val) {
        var select = $("<select name='sel_systemUi' class='form-control input-sm' placeholder='请选择'></select>");
        select.append("<option value=''>请选择</option>");
        for (var i = 0; i < systemUis.length; i++) {
            select.append("<option value='" + systemUis[i].systemUiCode + "'>" + systemUis[i].systemUiName + "</option>")
        }
        select.val(val);
        return select;
    };

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

$("#treetable").ZTable();

    var saveTemplateId = "";
    var delTemplateChapterIds = [];
    var showData = [];
    var rootData;
    var editAbled;

function reloadTemplateChapter(data) {
    window.parent.showLoading();
    // 加载封面
    addTemplateCover(data[0].templateHeaderId);
    saveTemplateId = "";
    delTemplateChapterIds = [];
    showData = [];
    saveTemplateId = data[0].templateHeaderId;
    for (var a = 1; a < data.length; a++) {
        showData.push(data[a]);
    }
    rootData = data[0];
    console.log(data);
    var tree = $("#treetable").fancytree("getTree");
    tree.reload(showData).done(function(){
        fancyTreeExpanded();
        //fancyTreeeCollapse();
        editFunction();
        if (!editAbled) {
            // $(".selectpicker").selectpicker('refresh');
            // $(".selectpicker").selectpicker('show');
        }
        // 设置编号
        resetTitleNo();
        window.parent.closeLoading();
    });
}

function editFunction() {
    if (!editAbled) {
        $('#bu_group').hide();
        $('#bu_delChapter').hide();
        $('#bu_content').hide();
        $('#bu_save').hide();
        $('#bu_release').hide();
    } else {
        $('#bu_group').show();
        $('#bu_delChapter').show();
        $('#bu_content').show();
        $('#bu_save').show();
        $('#bu_release').show();
    }
}

function clearCheckAll() {
    if ($('#chAll_treetableChapter').hasClass("glyphicon-check")) {
        $('#chAll_treetableChapter').removeClass("glyphicon-check");
        $('#chAll_treetableChapter').addClass("glyphicon-unchecked");
    }
}

$('#chAll_treetableChapter').on("click", function () {
    console.log(11111);
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
    $('#treetable').fancytree("getTree").visit(function(node){
        node.setSelected(v);
    });
    return false;
}
//


var doCreateSelectpicker = false;
$("#treetable").fancytree({
    selectMode: 2,
    extensions: ["dnd", "edit", "glyph", "table"],
    checkbox: true,
    keyboard: false,
    dnd: {
        focusOnClick: false,
        dragStart: function(node, data) { return true; },
        dragEnter: function(node, data) { return true; },
        dragDrop: function(node, data) {
            data.otherNode.moveTo(node, data.hitMode);
            fancyTreeExpanded();
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
        //     return "[" + node.key + "]: '" + node.title + "'";
        // });
    },
    renderColumns: function(event, data) {
        var node = data.node,
            $tdList = $(node.tr).find(">td");
        //console.log(node.data.chapterName);
        //$tdList.eq(2).text(node.getIndexHier());
        var col3 = undefined != node.data.chapterName ? node.data.chapterName : "";
        $tdList.eq(2).html("<input type='text' style='width: 100%; display: inline-block;' name='chapterName' class='form-control input-sm' value='" + col3 + "' />");
        var col4 = undefined != node.data.projectRoleId ? node.data.projectRoleId : "";
        $tdList.eq(3).html(createProRoleSel(col4));
        var col5 = undefined != node.data.specialty ? node.data.specialty : "";
        $tdList.eq(4).html(createSpecialt(col5));
        var col6 = undefined != node.data.xdoTemplateId ? node.data.xdoTemplateId : "";
        $tdList.eq(5).html(createXmlTemplateSel(col6));
        var col7 = undefined != node.data.systemLink ? node.data.systemLink : "";
        $tdList.eq(6).html(createSystemUiSel(col7));
        var col8 = null != node.data.publicFlag ? ("1" ==node.data.publicFlag ? "checked" : "") : "checked";
        $tdList.eq(7).html("<input type='checkbox' name='publicFlag' " + col8 + "/>");
        var col9 = null != node.data.deleteFlag ? ("1" ==node.data.deleteFlag ? "checked" : "") : "checked";
        $tdList.eq(8).html("<input type='checkbox' name='deleteFlag' " + col9 + "/>");
        // 加了列拉伸后，去除overflow
        $tdList.eq(3).css("overflow", "");
        $tdList.eq(4).css("overflow", "");

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
            // if ('S' == node.data.wordFlag)
            //     var viewType = '&pattern=ViewText';
            // else
                var viewType = '&pattern=EditText';

            // var userID = new logonUser;
            var PageeOfficeUrl = config.baseurl + '/pages/wbsp/code/poweb/poweb.html?source=TEMPLATE&chapterId=' + node.data.chapterId + '&taskHeaderId=' + node.data.templateHeaderId + '&isoa=0';
            // var PageeOfficeUrl = config.contextConfig.nbccPageOfficeUrl + 'file_name=' + node.data.taskChapterFileName + '&caption=' + node.data.chapterName + viewType;
            //'file_name=PO__'+ node.data.chapterId + suffix + '&caption=' + node.data.chapterName + viewType;
            var lstyle ='';
        }
        if (undefined == node.data.chapterId)
            disabledFlag = 'Y';
        var ldisable = ('Y' == disabledFlag ? "disabled = 'disabled'" : "");
        $tdList.eq(9).html('<div align="center"><button type="button" class="btn btn-default" style="padding: 5px 15px;border:none;" id="bu_editChapterword" onclick="_windowOpen(&quot;' + PageeOfficeUrl + '&quot;)"' + ldisable + '> <i class="glyphicon glyphicon-pencil" ></i></button></div>');

        if (editAbled) {
            var select = $tdList.eq(3).find("select");
            var select2 = $tdList.eq(4).find("select");
            // $(select[0]).selectpicker('refresh');
            // $(select[0]).selectpicker('show');
            // $(select2[0]).selectpicker('refresh');
            // $(select2[0]).selectpicker('show');
        }
    },
    removeNode: function (event, data) { // 删除node触发
        var node = data.node;
        if (undefined != node.data.chapterId)
            delTemplateChapterIds.push(node.data.chapterId);
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
}).on("nodeCommand", function(event, data){
    // Custom event handler that is triggered by keydown-handler and
    // context menu:
    var refNode, moveMode,
        tree = $(this).fancytree("getTree"),
        node = tree.getActiveNode();

    switch( data.cmd ) {
        case "moveUp":
            refNode = node.getPrevSibling();
            if( refNode ) {
                node.moveTo(refNode, "before");
                node.setActive();
            }
            break;
        case "moveDown":
            refNode = node.getNextSibling();
            if( refNode ) {
                node.moveTo(refNode, "after");
                node.setActive();
            }
            break;
        case "indent":
            refNode = node.getPrevSibling();
            if( refNode ) {
                node.moveTo(refNode, "child");
                refNode.setExpanded();
                node.setActive();
            }
            break;
        case "outdent":
            if( !node.isTopLevel() ) {
                node.moveTo(node.getParent(), "after");
                node.setActive();
            }
            break;
        case "rename":
            node.editStart();
            break;
        case "remove":
            refNode = node.getNextSibling() || node.getPrevSibling() || node.getParent();
            node.remove();
            if( refNode ) {
                refNode.setActive();
            }
            break;
        case "addChild":
            node.editCreateNode("child", "");
            break;
        case "addSibling":
            node.editCreateNode("after", "");
            break;
        case "cut":
            CLIPBOARD = {mode: data.cmd, data: node};
            break;
        case "copy":
            CLIPBOARD = {
                mode: data.cmd,
                data: node.toDict(function(n){
                    delete n.key;
                })
            };
            break;
        case "clear":
            CLIPBOARD = null;
            break;
        case "paste":
            if( CLIPBOARD.mode === "cut" ) {
                // refNode = node.getPrevSibling();
                CLIPBOARD.data.moveTo(node, "child");
                CLIPBOARD.data.setActive();
            } else if( CLIPBOARD.mode === "copy" ) {
                node.addChildren(CLIPBOARD.data).setActive();
            }
            break;
        case "selSibling":
            console.log(1);
            break;
        default:
            alert("Unhandled command: " + data.cmd);
            return;
    }

    // }).on("click dblclick", function(e){
    //   console.log( e, $.ui.fancytree.eventToString(e) );

});

$("#treetable").contextmenu({
    delegate: "span.fancytree-node",
    menu: [
        {title: "Copy", cmd: "copy", uiIcon: "ui-icon-copy"},
        {title: "----"},
        {title: "More", children: [
            {title: "Sub 1", cmd: "sub1"},
            {title: "Sub 2", cmd: "sub1"}
        ]}

    ],
    beforeOpen: function(event, ui) {
        var node = $.ui.fancytree.getNode(ui.target);
        //$("#tree").contextmenu("enableEntry", "paste", !!CLIPBOARD);
        node.setActive();
    },
    select: function(event, ui) {
        var that = this;
        // delay the event, so the menu can close and the click event does
        // not interfere with the edit control
        setTimeout(function(){
            $(that).trigger("nodeCommand", {cmd: ui.cmd});
        }, 100);
    }
});

$('#bu_release').on('click', function () {
    var nodeList = getTreeAllNode();
    var chapterNames = $("input[name='chapterName']");
    var proRoles = $('select[name="sel_proRole"]');
    var specialtys = $('select[name="sel_specialty"]');
    var no = 0;
    $(nodeList).each(function(i) {
        if ("" == chapterNames[i].value.trim()) {
            window.parent.warring("章节[" + nodeList[i].getIndexHier() + "]，章节名称未填写！");
            no = 1;
            return false;
        }
        if ("" == proRoles[i].value) {
            window.parent.warring("章节[" + nodeList[i].getIndexHier() + "]，项目角色未选择！");
            no = 1;
            return false;
        }
        if ("" == specialtys[i].value) {
            window.parent.warring("章节[" + nodeList[i].getIndexHier() + "]，分配专业未选择！");
            no = 1;
            return false;
        }
    });
    if (1 == no)
        return;
    saveChapters(1);

});

function doRelease() {
    $.ajax({
        url: config.baseurl +"/api/templateRest/upTemplateStatus",
        data: {
            templateId: saveTemplateId,
            templateStatus: "EFFECTIVE"
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "POST",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            if ("0000" == data.returnCode) {
                window.parent.success("发布成功！");
                $('#add_modal').on('shown.bs.modal', function (e) {}).modal('toggle');
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

$('#bu_content').on('click', function () {
    var x = new ActiveXObject("ActiveX.ActiveX");
    x.OpenTemplateWord(saveTemplateId);
});

    $('#bu_save').on('click', function () {
        saveChapters(0);
    });

    function saveChapters(v) {
        var nodeList = getTreeAllNode();
        var chapterNames = $("input[name='chapterName']");
        var proRoles = $('select[name="sel_proRole"]');
        var specialtys = $('select[name="sel_specialty"]');
        var xmlTemplates = $('select[name="sel_xmlTemplate"]');
        var systemUis = $('select[name="sel_systemUi"]');
        var publicFlags = $("input[name='publicFlag']");
        var deleteFlags = $("input[name='deleteFlag']");
        console.log(chapterNames.length);
        var o;
        var arr = new Array();
        rootData.delTemplateChapterIds = delTemplateChapterIds.toString();
        rootData.attribute7 = 0;
        arr.push(rootData);
        $(nodeList).each(function (i) {
            o = new Object();
            o.chapterId = nodeList[i].data.chapterId;
            o.chapterName = chapterNames[i].value;
            o.templateHeaderId = saveTemplateId;
            o.chapterNo = nodeList[i].getIndexHier();
            if (0 < o.chapterNo.lastIndexOf("\."))
                o.parentChapterNo = o.chapterNo.substring(0, o.chapterNo.lastIndexOf("\."));
            else
                o.parentChapterNo = "-1";
            o.projectRoleId = proRoles[i].value;
            o.specialty = specialtys[i].value;
            o.xdoTemplateId = xmlTemplates[i].value;
            o.systemLink = systemUis[i].value;
            o.publicFlag = publicFlags[i].checked ? "1" : "0";
            o.deleteFlag = deleteFlags[i].checked ? "1" : "0";
            // 排序
            o.attribute7 = (i + 1);
            arr.push(o);
        });
        // 封面数据
        o = new Object();
        o.chapterId = $('#templateCoverId').val();
        o.chapterName = "封面";
        o.templateHeaderId = saveTemplateId;
        o.projectRoleId = $('select[name="sel_proRole_cover"]')[0].value;
        o.specialty = $('select[name="sel_specialty_cover"]')[0].value;
        o.xdoTemplateId = $('select[name="sel_xmlTemplate_cover"]')[0].value;
        o.attribute7 = arr.length;
        o.attribute1 = "COVER";
        o.parentChapterNo = "-1";
        arr.push(o);
        console.log(JSON.stringify(arr));

        $.ajax({
            url: config.baseurl +"/api/templateRest/saveTemplateChapter",
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
                    if (0 < delTemplateChapterIds.length)
                        delTemplateChapterIds = [];
                    if (0 == v) {
                        clearCheckAll();
                        reloadData();
                        window.parent.success(tips_saveSuccess);
                    } else {
                        doRelease();
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
    }

    function reloadData() {
        $.ajax({
            url: config.baseurl +"/api/templateRest/templateChapters",
            data: {
                templateId: saveTemplateId
            },
            contentType : 'application/x-www-form-urlencoded',
            dataType: "json",
            type: "GET",
            beforeSend: function() {

            },
            success: function(data) {
                reloadTemplateChapter(data);
            },
            complete: function() {
            },
            error: function() {
                window.parent.error("重新加载数据出错！");
            }
        });
    }

    $('#bu_addChapter').click(function () {
        var obj = [
            { title: " ", folder: false}
        ];

        var allData = getTreeAllNode();
        if (0 == allData.length) {
            $("#treetable").fancytree("getRootNode").addChildren(obj);
        } else {
            var tree = $("#treetable").fancytree("getTree"),
                selNodes = tree.getSelectedNodes();

            if (0 == selNodes.length) {
                window.parent.warring(valTips_selOne);
                return;
            }
            //doCreateSelectpicker = true;
            selNodes.forEach(function (node) {
                obj[0].projectRoleId = node.data.projectRoleId;
                obj[0].specialty = node.data.specialty;
                node.appendSibling(obj);
            });
            //doCreateSelectpicker = false;
            //     selNode = tree.getActiveNode();
            // selNode.appendSibling(obj);
            fancyTreeExpanded();
            resetTitleNo();
        }
    });

    $('#bu_addChapterChildren').click(function () {
        var obj = [
            { title: " ", folder: false}
        ];
        var tree = $("#treetable").fancytree("getTree"),
            selNodes = tree.getSelectedNodes();

        if (0 == selNodes.length) {
            window.parent.warring(valTips_selOne);
            return;
        }
        //doCreateSelectpicker = true;
        selNodes.forEach(function(node) {
            obj[0].projectRoleId = node.data.projectRoleId;
            obj[0].specialty = node.data.specialty;
            node.addChildren(obj);
        });
        //doCreateSelectpicker = false;
        //     selNode = tree.getActiveNode();
        // selNode.addChildren(obj);
        fancyTreeExpanded();

        resetTitleNo();
    });

    $('#bu_delChapter').click(function () {
        var tree = $("#treetable").fancytree("getTree"),
            selNodes = tree.getSelectedNodes();

        if (0 == selNodes.length) {
            window.parent.warring(valTips_selOne);
            return;
        }

        selNodes.forEach(function(node) {
            while( node.hasChildren() ) {
                node.getFirstChild().moveTo(node.parent, "child");
            }
            node.remove();
        });
        resetTitleNo();
        // var tree = $("#treetable").fancytree("getTree");
        // tree.reload(tree.options.source).done(function(){
        //     alert("reloaded");
        // });
    });

$('#bu_fancyTreeExpanded').on('click', function () {
    fancyTreeExpanded();
});

function fancyTreeExpanded() {
        $("#treetable").fancytree("getRootNode").visit(function(node){
            node.setExpanded(true);
        });
    }

function fancyTreeToggleExpanded() {
    $("#treetable").fancytree("getRootNode").visit(function(node){
        node.toggleExpanded();
    });
}

$('#bu_fancyTreeeCollapse').on('click', function () {
    fancyTreeeCollapse();
});

function fancyTreeeCollapse() {
    $("#treetable").fancytree("getRootNode").visit(function (node) {
        node.setExpanded(false);
    });
}

    // $('#bu_resetTitleNo').on('click', function () {
    //     resetTitleNo();
    // });

    function resetTitleNo() {
        var nodeList = getTreeAllNode();
        var _newIndex = [];
        nodeList.forEach(function(node) {
            _newIndex.push(node.getIndexHier());
        });
        // $("#treetable tr td:nth-child(3)").each(function (index) {
        //     $(this).html(_newIndex[index]);
        // });
        $("#treetable tr td:nth-child(2)").each(function (index) {
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

function getTreeAllNode() {
    var tree = $("#treetable").fancytree("getTree");
    var nodeList = [];
    tree.visit(function(node){
        nodeList.push(node);
    });
    return nodeList;
}
//});