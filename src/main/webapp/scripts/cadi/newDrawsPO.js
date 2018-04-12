/**
 * Created by wdl on 16/7/7.
 */
/**
 *  定义初始化图纸树型变量
 */
var drawsTree = [], CLIPBOARD = null;
var test =
    [{
        "did": null,
        "drawNum": null,
        "reviseNum": null,
        "drawName": null,
        "xSize": null,
        "xProjectNum": null,
        "xDesignPhase": null,
        "xDivisionNum": null,
        "xDivision": null,
        "xSpecialty": null,
        "xDlvrId": null,
        "xDlvrName": null,
        "xEmFlag": null,
        "xDesignDate": null,
        "xWpApprStatus": null,
        "xCountersignStatus": null,
        "xPltStatus": null,
        "dReleaseDate": null,
        "children": [
            {
                "did": null,
                "drawNum": null,
                "reviseNum": null,
                "drawName": null,
                "xSize": null,
                "xProjectNum": "2A14A010431",
                "xDesignPhase": null,
                "xDivisionNum": null,
                "xDivision": null,
                "xSpecialty": null,
                "xDlvrId": null,
                "xDlvrName": null,
                "xEmFlag": null,
                "xDesignDate": null,
                "xWpApprStatus": null,
                "xCountersignStatus": null,
                "xPltStatus": null,
                "dReleaseDate": null,
                "children": null,
                "title": "1.1",
                "folder": false,
            }],
        "title": "1",
        "folder": true
    }];
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

$("#chooseArea").fancytree({
    checkbox: true,
    selectMode: 3,
    titlesTabbable: true,     // Add all node titles to TAB chain
    quicksearch: true,        // Jump to nodes when pressing first character
    source: drawsTree,
    extensions: ["table", "gridnav", "glyph"],
    glyph: glyph_opts,
    table: {
        indentation: 10,
        nodeColumnIdx: 1,
        checkboxColumnIdx: 0
    },
    gridnav: {
        autofocusInput: false,
        handleCursorKeys: true
    },
    select: function(event, data) {
        // console.log(data);
        // var node = data.node;
        // var status = node.data.nodeStatus;
        // if ("校审中" == status || "已删除" == status || "校审未通过" == status)
        //     node.setSelected(false);
    },
    renderColumns: function (event, data) {
        var node = data.node,
            $tdList = $(node.tr).find(">td");
        // (Index #0 is rendered by fancytree by adding the checkbox)
        //$tdList.eq(1).text(node.getIndexHier()).addClass("alignRight");  //不需要显示索引号及索引层次
        var status = node.data.nodeStatus;
        if ("校审中" == status || "已删除" == status || "校审未通过" == status || "校审取消" == status
            || "对图中" == status || "对图未通过" == status )
            $tdList.eq(0).html("");

        if ("" == status || null == status) {
            var nodeType = node.data.nodeType;
            if ("gzb" == nodeType || "zy" == nodeType || "zx" == nodeType) {

            } else {
                if (node.data.dDocType == "PJT-DESIGN-DOC" && node.data.nodeType == "tz" && ("" == status || null == status)) {

                } else {
                    $tdList.eq(0).html("");
                }
            }
        }

        var col2 = undefined != node.data.objectName ? node.data.objectName : "";
        $tdList.eq(2).text(col2);
        var col3 = undefined != node.data.reviseNum ? node.data.reviseNum : "";
        $tdList.eq(3).text(col3);
        var col4 = undefined != node.data.nodeStatus ? node.data.nodeStatus : "";
        $tdList.eq(4).text(col4);
    }
}).on("nodeCommand", function (event, data) {
    // Custom event handler that is triggered by keydown-handler and
    // context menu:
    var refNode, moveMode,
        tree = $(this).fancytree("getTree"),
        node = tree.getActiveNode();

    switch (data.cmd) {
        case "moveUp":
            refNode = node.getPrevSibling();
            if (refNode) {
                node.moveTo(refNode, "before");
                node.setActive();
            }
            break;
        case "moveDown":
            refNode = node.getNextSibling();
            if (refNode) {
                node.moveTo(refNode, "after");
                node.setActive();
            }
            break;
        case "indent":
            refNode = node.getPrevSibling();
            if (refNode) {
                node.moveTo(refNode, "child");
                refNode.setExpanded();
                node.setActive();
            }
            break;
        case "outdent":
            if (!node.isTopLevel()) {
                node.moveTo(node.getParent(), "after");
                node.setActive();
            }
            break;
        default:
            alert("Unhandled command: " + data.cmd);
            return;
    }
}).on("keydown", function (e) {
    var cmd = null;
    switch ($.ui.fancytree.eventToString(e)) {
        case "ctrl+up":
            cmd = "moveUp";
            break;
        case "ctrl+down":
            cmd = "moveDown";
            break;
        case "ctrl+right":
        case "ctrl+shift+right": // mac
            cmd = "indent";
            break;
        case "ctrl+left":
        case "ctrl+shift+left": // mac
            cmd = "outdent";
    }
    if (cmd) {
        $(this).trigger("nodeCommand", {cmd: cmd});
        // e.preventDefault();
        // e.stopPropagation();
        return false;
    }
});

/**
 * 全选图纸
 */
function setCheckAll() {
    var isChecked = $('#ch_listAll').prop("checked");
    $("input[name='ch_list']").prop("checked", isChecked);
}

/**
 * 全选文印条目
 */

function setCheckAll2() {
    var isChecked = $('#ch_listAll2').prop("checked");
    $("input[name='ch_list2']").prop("checked", isChecked);
    $("input[name='ch_list2']").each(function () {
        var $tr = $(this).parent().parent();
        if (isChecked){
            $tr.addClass('dt-select');
        } else {
            $tr.removeClass('dt-select');
        }
    });
}

// 删除数组中第一个匹配的元素，成功则返回位置索引，失败则返回 -1。
Array.prototype.deleteElementByValue = function (varElement) {
    var numDeleteIndex = -1;
    for (var i = 0; i < this.length; i++) {
        // 严格比较，即类型与数值必须同时相等。
        if (this[i] === varElement) {
            this.splice(i, 1);
            numDeleteIndex = i;
            break;
        }
    }
    return numDeleteIndex;
}
//定义从树上选择的图纸
var selectedDraws = [];
//定义从树上选择的文本
var selectedTexts = [];
//定义待文印图纸列表；
var tmpPlts = [];
var config = new nerinJsConfig();
// var logonUser;
// 区分文本还是图纸，1文本，2图纸
var tw;
// 生成出图单类型
var pltCategory;
//-------------------------Document.ready----------------------------------
$(function () {
    var type = $.query.get("type");
    if ("PJT-DRAW" != type) {
        $('#docType').val("");
        tw = 2;
        pltCategory = "DOC_NEW_PRINT";
    } else {
        tw = 1;
        pltCategory = "PLT_NEW_PRINT";
    }
    // initTz_config(1785);
    // $.getJSON(config.baseurl + "//api/logonUser", function (data) {
    //     logonUser = data;
    // });
    //定义索引结果集返回
    //var dataSet = [];
    /*
     * --------------------初始化各菜单下拉内容----------------------------
     */
    initProjectMenu(); //初始化项目值列表；
    // initPhaseMenu(); //阶段
    // initUnitTaskMenu(); //子项
    // initSpecialityMenu() //专业
    // initDlvrMenu(); //工作包
    initDrawStatusMenu();//图纸状态
    loadSelectedTable(); //
    loadSelectedTmpPlt();
    // initTmpPlt(); //初始化待出图文印条目;

    //--------------------普通页面控制区内容--------------------------------
    //查询函数
    $('#bu_queryData').click(function () {
        if ("" == $('#ProjectNumber').val()) {
            window.parent.warring("请选择项目编号");
            return;
        }
        if ("" == $('#PhaseCode').val()) {
            window.parent.warring("请选择阶段名称");
            return;
        }

        $.ajax({
            url: config.baseurl + "/api/draw/draw",
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",   //返回格式为json
            async: true,//请求是否异步，默认为异步，这也是ajax重要特性
            data: $('#selectFrom').serialize(),
            type: "GET",   //请求方式
            beforeSend: function () {
                window.parent.showLoading();
            },
            success: function (data) {
                drawsTree = data;
                console.log(drawsTree);
                loadDrawsTree();
                initTmpPlt();
                // 如果右边选定区域有值，则删除
                if (0 < selectedDraws.length)
                    $('#deleteSelection').trigger('click');
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
    //reset select
    $('#bu_resetQuery').click(function () {
        $('.selectpicker').selectpicker('val', '');
    });
    //------------------图纸目录处理区-------------------------------------
    //初始化图纸目录模态框
    $('#DrawCatalog').on('shown.bs.modal', function (e) {
        // init_tzml();
        initCatList();
    });

    $('#addCatalog').on('click', function () {
        // 初始化目录行表
        var data_tmp = $('#choosingArea').DataTable().rows($("input[name='ch_list']").parents('tr')).data();
        var to = [];
        for (var i = data_tmp.length - 1; i >= 0; i--) {
            to.push(data_tmp[i].did);
        }
        console.log(to);
        $.ajax({
            url : config.baseurl +"/api/draw/generateCatContentsMain",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                dids: to.toString()
            },
            type:"POST",
            beforeSend:function(){
                window.parent.showLoading();
            },
            success:function(data){
                console.log("id:" + data);
                $('#SubmitCatalogC').attr("disabled", true);
                searchCatHeaders(data);
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

    function init_tzml() {
        //初始化目录头表
        //从选择区的第一行得到数据
        if (selectedDraws.length > 0) {
            $('#ProjectSNameC').val(selectedDraws[0].projectSName);
            //设备名称的取值逻辑 23 = 装备
            if(selectedDraws[0].xOrganization =='23' && selectedDraws[0].xEquipmentNum !=null)
            {
                $('#EquipmentC').val(selectedDraws[0].xDivision);
            }
            //END
            //阶段的取值，阶段要完善
            $('#DesignPhaseC').val(selectedDraws[0].xDesignPhase);

            //图纸目录子项取值
            if(selectedDraws[0].xEquipmentNum != null )
            {
                if(selectedDraws[0].xOrganization =='23') //装备公司
                {
                    $('#UnitTaskNameC').val(selectedDraws[0].xDlvrName);
                }
                else
                {
                    $('#UnitTaskNameC').val(selectedDraws[0].xEquipment);
                }
            }
            else
            {
                $('#UnitTaskNameC').val(selectedDraws[0].xDivision);
            }
            //END
            //专业取值逻辑： 对装备非标的特殊化处理;
            //if(selectedDraws[0].xOrganization =='23' && selectedDraws[0].xEquipmentNum != null &&  selectedDraws[0].specialityName = '冶金设备')
            $('#SpecialtyC').val("机械");
            //  else
            $('#SpecialtyC').val(selectedDraws[0].specialityName);

            //工业版图纸目录
            var drawNum = selectedDraws[0].drawNum.substr(0, selectedDraws[0].drawNum.indexOf('-') - 3) + 'DL1-1';
            //非标版图纸目录待完成。
            //end
            $('#DrawNumC').val(drawNum);
            //所属图号
            if (selectedDraws[0].xEquipment == null) {
                //初始化引用图号;
                var refDrawNum = '';
            }
            else
            {

            }//end 所属图号

            //初始化图纸目录版本
            $.getJSON(config.baseurl + '/api/draw/drawsCatalogTem?projectId=' + selectedDraws[0].projectId, function (data) {
                $("#TemplateC").html("");
                $(data).each(function (index) {
                    $("#TemplateC").append($('<option value="' + data[index].templateCode + '">' + data[index].templateName + '</option>'));
                });
                $("#TemplateC").selectpicker('refresh');


            });
            //初始化年月
            var yearMonth = '';
            if (new Date().getMonth() + 1 > 9) {
                yearMonth = new Date().getFullYear().toString() + (new Date().getMonth() + 1).toString()
            }
            else {
                yearMonth = new Date().getFullYear().toString() + '0' + (new Date().getMonth() + 1).toString();

            }
            $('#DesignDateC').val(yearMonth);
            //初始化图纸目录模板
            $.getJSON(config.baseurl + '/api/draw/drawRevision?drawNum=' + drawNum, function (data) {
                $('#RevisionC').val(data);
            });
            // 初始化目录行表
            var data_tmp = $('#choosingArea').DataTable().rows($("input[name='ch_list']").parents('tr')).data();


            var new_selectedDraws = [];
            for (var i = data_tmp.length - 1; i >= 0; i--) {
                new_selectedDraws.push(data_tmp[i]);
            }
            $('#DrawCatalog_container').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="DrawCatalog_table"></table>');
            t = $('#DrawCatalog_table').DataTable({
                //设置数据源
                "data": new_selectedDraws,
                "language": {
                    "url": "/scripts/common/Chinese.json"
                },
                //定义列名
                "columns": [

                    {"title": "No", "data": null},
                    {"title": "图纸名称", "data": "drawName"},
                    {"title": "图号", "data": "drawNum"},
                    {"title": "图纸规格", "data": "xSize"},
                    {"title": "备注", "data": "xPltStatus"},
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
                    },
                    {
                        "orderable": false,
                        "targets": 2,
                    },
                    {
                        "searchable": false,
                        "orderable": false,
                        "targets": 3,
                    },
                    {
                        "targets": 4,
                        "visible": false
                    }
                ],
                //不分页，加入垂直导航条
                "scrollY": "400",
                "scrollCollapse": true,
                "paging": false,
                "searching": false,
                //"order": [],
                "createdRow": function (row, data, dataIndex) {
                    $(row).children('td').eq(0).attr('style', 'text-align: center;');
                },
                "initComplete": function () {
                    $('#DrawCatalog_table_wrapper').css("cssText", "margin-top: -5px;");
                }
            });
            /* t.on('order.dt search.dt', function () {
             t.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
             cell.innerHTML = i + 1;
             });
             }).draw();*/

        }
    }

    //-------------------选择图纸条目--------------------------------------
    /*
     * Tooltips
     */
    $("#chooseArea").tooltip({
        content: function () {
            return $(this).attr("title");
        }
    });
    $('#bu_fancyTreeExpanded').on('click', function () {
        fancyTreeExpanded();
    });

    $('#bu_fancyTreeeCollapse').on('click', function () {
        fancyTreeeCollapse();
    });
    $('#addSelection').on('click', function () {
        var tree = $("#chooseArea").fancytree("getTree"),
            selNodes = tree.getSelectedNodes();

        if (0 == selNodes.length) {
            window.parent.warring(valTips_selOne);
            return;
        }

        var new_nodes = [];
        selNodes.forEach(function (node) {
            var status = node.data.nodeStatus;
            if ("校审中" == status || "已删除" == status || "校审未通过" == status || "校审取消" == status
                || "对图中" == status || "对图未通过" == status || "" == status || null == status) {
                if (node.data.dDocType == "PJT-DESIGN-DOC" && node.data.nodeType == "tz" && ("" == status || null == status)) {
                    new_nodes.push(node);
                } else {

                }
            } else {
                new_nodes.push(node);
            }
        });
        selNodes = new_nodes;
        if (0 == selNodes.length) {
            window.parent.warring(valTips_selOne);
            return;
        }

        selectedTexts = [];
        selectedDraws = [];
        // 图纸node
        var selectedDrawNodes = [];
        // 文本node
        var selectedTextNodes = [];
        var selectedDrawFlag = false;
        var selectedTextFlag = false;
        selNodes.forEach(function (node) {
            if (node.data.drawNum != null && selectedDraws.indexOf(node.data) == -1 && node.data.dDocType == ('PJT-DRAW')) {
                if (!selectedDraws.some(function (item) {
                        return item.drawNum == node.data.drawNum;
                    })
                ) {
                    selectedDraws.push(node.data);
                    selectedDrawNodes.push(node);
                    selectedDrawFlag = true;
                }

            }
            if (node.data.drawNum != null && selectedTexts.indexOf(node.data) == -1 && node.data.dDocType == ('PJT-DESIGN-DOC')) {
                if (!selectedTexts.some(function (item) {
                        return item.drawNum == node.data.drawNum;
                    })
                ) {
                    selectedTexts.push(node.data);
                    selectedTextNodes.push(node);
                    selectedTextFlag = true;
                }
            }

        });

        var tempZx = "a1";
        var temp_zx = "";
        // 是否可以添加到右边
        var addFlag = 0;
        // 2、是否提示添嗮
        var addAgainFlag = 0;
        // 4、专业孙项
        var temp_xspecialtySeq = "";
        // 1.2设备号如果不同
        var xEquipmentNumFlag = 0;
        var xEquipmentNum_tmp = "";
        selectedDrawNodes.some(function (node) {
            // 3、只有已出图文印、校审通过、出图文印中、对图通过才能添加到右边
            var status = ["已出图文印", "出图文印中", "校审通过", "对图通过"];
            if (0 > status.indexOf(node.data.nodeStatus) || null == node.data.nodeStatus) {
                window.parent.warring("只有已出图文印、校审通过、出图文印中、对图通过才能添加，[" + node.data.objectCode + "]不满足条件");
                addFlag = 1;
                return true;
            }
            // 1、跨子项
            var nodeType = node.data.nodeType;
            if ("tz" == nodeType) {
                var gzbNode = node.parent;
                if ("gzb" == gzbNode.data.nodeType) {
                    var zyNode = gzbNode.parent;
                    if ("zy" == zyNode.data.nodeType) {
                        var zxNode = zyNode.parent;
                        if ("zx" == zxNode.data.nodeType) {
                            if ("" == temp_zx) {
                                temp_zx = zxNode.data.xDivisionNum;
                            } else {
                                if (temp_zx != zxNode.data.xDivisionNum){
                                    addFlag = 1;
                                    window.parent.warring("不允许跨子项进行出图，请确保勾选的图纸均在同一子项下！");
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            // 4、跨专业孙项
            if ("tz" == nodeType) {
                if ("" == temp_xspecialtySeq) {
                    temp_xspecialtySeq = node.data.xspecialtySeq;
                } else {
                    if (temp_xspecialtySeq != node.data.xspecialtySeq) {
                        addFlag = 1;
                        window.parent.warring("当前选中图纸的专业孙项不同，请确保选中的图纸是同一专业孙项");
                        return true;
                    }
                }
            }
            // 2、图纸是否添嗮
            // if ("tz" == nodeType) {
            //     var gzbNode = node.parent;
            //     if ("gzb" == gzbNode.data.nodeType) {
            //         var arrStatus = ["已出图文印", "出图文印中"];
            //         var childrens = gzbNode.children;
            //         childrens.forEach(function (node2) {
            //             if (0 <= arrStatus.indexOf(node2.data.nodeStatus)) {
            //                 addAgainFlag = 1;
            //             }
            //         });
            //     }
            // }
            // 只能增晒2017-03-24
            if ("tz" == nodeType) {
                if ("已出图文印" == node.data.nodeStatus) {
                    addAgainFlag += 1;
                }
                if ("" == xEquipmentNum_tmp && "" != node.data.xEquipmentNum){
                    xEquipmentNum_tmp = node.data.xEquipmentNum;
                } else {
                    if ("" != node.data.xEquipmentNum && xEquipmentNum_tmp != node.data.xEquipmentNum) {
                        xEquipmentNumFlag = 1;
                    }
                }
            }

        });

        selectedTextNodes.some(function (node) {
            // 1、跨子项
            var nodeType = node.data.nodeType;
            if ("tz" == nodeType) {
                var gzbNode = node.parent;
                if ("gzb" == gzbNode.data.nodeType) {
                    var zyNode = gzbNode.parent;
                    if ("zy" == zyNode.data.nodeType) {
                        var zxNode = zyNode.parent;
                        if ("zx" == zxNode.data.nodeType) {
                            if ("" == temp_zx) {
                                temp_zx = zxNode.data.xDivisionNum;
                            } else {
                                if (temp_zx != zxNode.data.xDivisionNum){
                                    addFlag = 1;
                                    window.parent.warring("不允许跨子项进行出图，请确保勾选的图纸均在同一子项下！");
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            // 2、文本判断
            if ("tz" == nodeType) {
                if ("出图文印中" == node.data.nodeStatus) {
                    window.parent.warring("[" + node.data.objectCode + "]已在出图文印中，无法文印");
                    addFlag = 1;
                    return true;
                }
                if ("" == xEquipmentNum_tmp && "" != node.data.xEquipmentNum){
                    xEquipmentNum_tmp = node.data.xEquipmentNum;
                } else {
                    if ("" != node.data.xEquipmentNum && xEquipmentNum_tmp != node.data.xEquipmentNum) {
                        xEquipmentNumFlag = 1;
                    }
                }
            }
        });

        if (0 < addFlag)
            return;

        if (0 < xEquipmentNumFlag) {
            window.parent.warring("所选图纸不属于同一个设备");
            return;
        }

        if (selectedDrawNodes.length == addAgainFlag && 0 < addAgainFlag) {
            window.parent.warring("该批图纸已文印，请发起图纸增晒流程");
            // $.confirm({
            //     title: '<font style="font-size: 14px;">信息提示</font>',
            //     autoClose: 'confirm|11000',
            //     icon: 'fa fa-warning confirm2_info',
            //     content: "该批图纸已文印，可发起图纸增晒流程，是否需要继续新晒？",
            //     closeIcon: true,
            //     confirmButton: '是',
            //     cancelButton: '否',
            //     confirm: function () {
            //         //去加载已经选择的图纸列表
            //         if (selectedDrawFlag)
            //             loadSelectedTable();
            //         //去加载已经选择的文本列表；
            //         if (selectedTextFlag)
            //             addText2Plt();
            //     },
            //     cancel:function(){
            //         window.parent.info("图纸添晒流程，可以在“我的出图单”中发起");
            //     },
            //     keyboardEnabled: true,
            //     columnClass: 'col-md-6 col-md-offset-3'
            // });
            return;
        }
        //去加载已经选择的图纸列表
        if (selectedDrawFlag)
            loadSelectedTable();
        //去加载已经选择的文本列表；
        if (selectedTextFlag)
            addText2Plt();

        /* 以下是调用api重新绘制表格无效；所以暂时采用重新填写div的方式
         var table = $('#choosingArea').DataTable();
         table.order( [[ 0, 'asc' ]] ).draw( false );
         */
    });
    /**
     * 定义加载图纸树函数；
     */
    function loadDrawsTree() {
        var tree = $("#chooseArea").fancytree("getTree");
        tree.reload(drawsTree).done(function(){

        });
    }

    /**
     * 设置树表全部展开及收缩的
     */
    function fancyTreeExpanded() {
        $("#chooseArea").fancytree("getRootNode").visit(function (node) {
            node.setExpanded(true);
        });
    }

    function fancyTreeeCollapse() {
        $("#chooseArea").fancytree("getRootNode").visit(function (node) {
            node.setExpanded(false);
        });
    }

    /**
     * 删除选定条目-图纸
     */
    $('#deleteSelection').on('click', function () {
        var data = $('#choosingArea').DataTable().rows($("input[name='ch_list']:checked").parents('tr')).data();
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        }
        for (var i = 0; i < data.length; i++) {
            selectedDraws.deleteElementByValue(data[i]);
        }

        $("input[name='ch_list']:checked").each(function () {
            var $checkBox = $(this);
            var $tr = $checkBox.parent().parent();
            $tr.remove();
        });
        // selectedDraws = [];
        // loadSelectedTable();
    });
    //--------------处理待文印内容----------------------------------------

    function addText2Plt() {
        var arr = new Array();
        $(selectedTexts).each(function (i) {
            arr.push(selectedTexts[i].did);
        });
        $.ajax({
            url: config.baseurl + "/api/draw/genPjtDesignDocPltTmp",
            contentType : 'application/x-www-form-urlencoded',
            dataType: "json",   //返回格式为json
            data: {
                dids: arr.toString()
            },
            type: "POST",   //请求方式
            beforeSend: function () {
                window.parent.showLoading();
            },
            success: function (data) {
                if ("0000" == data.returnCode) {
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
    }



    //--------------处理页面值列表联动关系---------------------------------


    /**
     * 选择项目联动项目阶段/子项/专业/工作包
     */
    $('#ProjectNumber').on('change', function () {
        initPhaseMenu();
        // initUnitTaskMenu();
        // initSpecialityMenu();
        // initDlvrMenu();
    });

    /**
     * 选择项目阶段联动项目子项/专业PhaseCode
     */
    $('#PhaseCode').on('change', function () {
        initUnitTaskMenu();
        // initSpecialityMenu();
    });
    /**
     * 选择专业联动工作包
     */
    $("#SpecialityCode").on('change', function () {
        initDlvrMenu();
    });

//-----------------定义各个初始化函数--------------------------
    var t;

    /**
     * 初始项目编号列表
     *
     get /api/draw/myProjectsList
     */
    function initProjectMenu() {
        $.getJSON(config.baseurl + '/api/draw/myProjectsList', function (data) {
            $("#ProjectNumber").html("");
            $(data).each(function (index) {
                $("#ProjectNumber").append($('<option value="' + data[index].projectId + '">' + data[index].projectNumber + '(' + data[index].projectName + ')</option>'));
            });
            $("#ProjectNumber").selectpicker('refresh');


        });
    }

//初始化阶段值列表
    function initPhaseMenu() {
        $.ajax({
            url : config.baseurl +"/api/draw/projectPhase",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",   //返回格式为json
            // async: false,
            data: {
                projectId: $("#ProjectNumber").val(),
                docType: $('#docType').val()
            },
            type:"GET",   //请求方式
            beforeSend:function(){
                window.parent.showLoading();
            },
            success:function(data){
                var list = data;
                $('#PhaseCode').empty();
                //var initValue = ''; //ck 需要初始化
                //var initText = '请选择';
                //$('#PhaseCode').append('<option value="' + initValue + '">' + initText + '</option>');
                list.forEach(function (data) {
                    $('#PhaseCode').append('<option value="' + data.phaseCode + '">' + data.phaseName + '(' + data.phaseCode + ')</option>');
                });
                $("#PhaseCode").selectpicker('refresh');
            },
            complete:function(){
                window.parent.closeLoading();
            },
            error:function(){
                window.parent.closeLoading();
                // window.parent.error(ajaxError_loadData);
            }
        });

        // $.getJSON(config.baseurl + '/api/draw/projectPhase?projectId=' + $("#ProjectNumber").val() + "&docType=" + $('#docType').val(), function (data) {
        //     var list = data;
        //     $('#PhaseCode').empty();
        //     //var initValue = ''; //ck 需要初始化
        //     //var initText = '请选择';
        //     //$('#PhaseCode').append('<option value="' + initValue + '">' + initText + '</option>');
        //     list.forEach(function (data) {
        //         $('#PhaseCode').append('<option value="' + data.phaseCode + '">' + data.phaseName + '(' + data.phaseCode + ')</option>');
        //     });
        //     $("#PhaseCode").selectpicker('refresh');
        // });
    }

//初始化子项列表
    function initUnitTaskMenu() {
        $.ajax({
            url : config.baseurl +"/api/draw/unitTask",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",   //返回格式为json
            // async: false,
            data: {
                projectId: $("#ProjectNumber").val(),
                phaseId: $('#PhaseCode').val()
            },
            type:"GET",   //请求方式
            beforeSend:function(){
                window.parent.showLoading();
            },
            success:function(data){
                var list = data;// data=子项列表
                $('#UnitTaskNumber').empty();
                $('#UnitTaskNumber').append("<option value=''>请选择</option>");
                //测试需要初始化;
                list.forEach(function (data) //data= 每一个子项
                {
                    $('#UnitTaskNumber').append('<option value="' + data.unitTaskCode + '">' + data.unitTaskName + '(' + data.unitTaskCode + ')</option>');
                });
                $('#UnitTaskNumber').selectpicker('refresh');
                initSpecialityMenu();
            },
            complete:function(){
                window.parent.closeLoading();
            },
            error:function(){
                window.parent.closeLoading();
                // window.parent.error(ajaxError_loadData);
            }
        });

        // $.getJSON(config.baseurl + '/api/draw/unitTask?projectId=' + $('#ProjectNumber').val() + '&phaseId=' + $('#PhaseCode').val(), function (data) {
        //     var list = data;// data=子项列表
        //     $('#UnitTaskNumber').empty();
        //     $('#UnitTaskNumber').append("<option value=''>请选择</option>");
        //     //测试需要初始化;
        //     list.forEach(function (data) //data= 每一个子项
        //     {
        //         $('#UnitTaskNumber').append('<option value="' + data.unitTaskCode + '">' + data.unitTaskName + '(' + data.unitTaskCode + ')</option>');
        //     });
        //     $('#UnitTaskNumber').selectpicker('refresh');
        // });
    }

//初始化专业值列表
    function initSpecialityMenu() {
        $.ajax({
            url : config.baseurl +"/api/draw/speciality",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",   //返回格式为json
            // async: false,
            data: {
                projectId: $("#ProjectNumber").val(),
                phaseId: $('#PhaseCode').val()
            },
            type:"GET",   //请求方式
            beforeSend:function(){
                window.parent.showLoading();
            },
            success:function(data){
                var spcialityList = data;
                $("#SpecialityCode").empty();
                $("#SpecialityCode").append("<option value=''>请选择</option>");
                spcialityList.forEach(function (data) {
                    $('#SpecialityCode').append('<option value="' + data.specialityCode + '">' + data.specialityName + '(' + data.specialityCode + ')</option>');
                });
                $('#SpecialityCode').selectpicker('refresh');
            },
            complete:function(){
                window.parent.closeLoading();
            },
            error:function(){
                window.parent.closeLoading();
                // window.parent.error(ajaxError_loadData);
            }
        });

        // $.getJSON(config.baseurl + '/api/draw/speciality?projectId=' + $('#ProjectNumber').val() + '&phaseId=' + $('#PhaseCode').val(), function (data) {
        //     var spcialityList = data;
        //     $("#SpecialityCode").empty();
        //     $("#SpecialityCode").append("<option value=''>请选择</option>");
        //     spcialityList.forEach(function (data) {
        //         $('#SpecialityCode').append('<option value="' + data.specialityCode + '">' + data.specialityName + '(' + data.specialityCode + ')</option>');
        //     });
        //     $('#SpecialityCode').selectpicker('refresh');
        // });
    }

//初始化交付列表
    function initDlvrMenu() {
        $.ajax({
            url : config.baseurl +"/api/draw/dlvr",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",   //返回格式为json
            // async: false,
            data: {
                projectId: $("#ProjectNumber").val(),
                specialityCode: $('#SpecialityCode').val()
            },
            type:"GET",   //请求方式
            beforeSend:function(){
                window.parent.showLoading();
            },
            success:function(data){
                var dlvrList = data;
                $('#DlvrId').empty();
                $('#DlvrId').append("<option value=''>请选择</option>");
                dlvrList.forEach(function (data) {
                    $("#DlvrId").append('<option value="' + data.dlvrId + '">' + data.dlvrName + '(' + data.dlvrId + ')</option>');
                });
                $('#DlvrId').selectpicker('refresh');
            },
            complete:function(){
                window.parent.closeLoading();
            },
            error:function(){
                window.parent.closeLoading();
                // window.parent.error(ajaxError_loadData);
            }
        });
        //
        // $.getJSON(config.baseurl + '/api/draw/dlvr?projectId=' + $('#ProjectNumber').val() + '&specialityCode=' + $('#SpecialityCode').val(), function (data) {
        //     var dlvrList = data;
        //     $('#DlvrId').empty();
        //     $('#DlvrId').append("<option value=''>请选择</option>");
        //     dlvrList.forEach(function (data) {
        //         $("#DlvrId").append('<option value="' + data.dlvrId + '">' + data.dlvrName + '(' + data.dlvrId + ')</option>');
        //     });
        //     $('#DlvrId').selectpicker('refresh');
        // });
    }

//初始化图纸状态
    function initDrawStatusMenu() {
        $.getJSON(config.baseurl + '/api/draw/drawStatus', function (data) {
            var drawStatusList = data;
            $('#DrawStatus').empty();
            $('#DrawStatus').append("<option value=''>请选择</option>");
            drawStatusList.forEach(function (data) {
                $("#DrawStatus").append('<option value="' + data.drawStatusCode + '">' + data.drawStatusName + '</option>');
            });
            $("#DrawStatus").selectpicker('refresh');

        });
    }

});

//--------------处理选择的图纸内容-----------------------------------
function loadSelectedTable() {
    $('#choosingArea_container').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="choosingArea"></table>');
    t = $('#choosingArea').DataTable({
        //设置数据源
        "data": selectedDraws,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        //定义列名
        "columns": [
            {
                "title": "<input type='checkbox' name='ch_listAll' id='ch_listAll' onclick='javascript:setCheckAll();' />",
                "data": null
            },
            {"title": "序号", "data": null},
            {"title": "图号", "data": "drawNum"},
            {"title": "图名", "data": "drawName"},
            {"title": "子项号", "data": "xDivisionNum"},
            {"title": "子项名称", "data": "xDivision"},
            {"title": "专业", "data": "specialityName"},
            {"title": "工作包号", "data": "xDlvrId"},
            {"title": "工作包", "data": "xDlvrName"},
            {"title": "版本", "data": "reviseNum"},
            {"title": "图纸状态", "data": "nodeStatus"}
        ],
        //控制隐藏列
        "columnDefs": [
            {
                "orderable": false,
                "targets": 0
            },
            {
                "searchable": false,
                "orderable": false,
                "targets": 1,
                "width": 32

            },{
                "orderable": false,
                "targets": 2,
                "width": 200,
                "render": function (data, type, full, meta) {
                    return "<a href=#1' name='linkOa'>" + data + "</a>";
                }
            },{
                "orderable": false,
                "targets": 3,
                "width": 200
            },{
                "orderable": false,
                "targets": 4,
                "width": 50
            },{
                "orderable": false,
                "targets": 5,
                "width": 70
            },{
                "orderable": false,
                "targets": 6,
                "width": 70
            },{
                "orderable": false,
                "targets": 7,
                "width": 70
            },{
                "orderable": false,
                "targets": 8,
                "width": 100
            },{
                "orderable": false,
                "targets": 9,
                "width": 32
            },{
                "orderable": false,
                "targets": 10,
                "width": 70
            }
        ],
        //控制排序:按照排序号
        "order": [],
        //不分页，加入垂直导航条
        "scrollY": "270px",
        "scrollX": true,
        "scrollCollapse": true,
        "paging": false,
        "searching": false,
        "rowReorder": {
            selector: 'td:not(.cList)', //td:not(.cList)    tr
            update: false
        },
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            $(row).children('td').eq(1).attr('style', 'text-align: center;');
            $(row).children('td').eq(0).addClass('cList');
            $(row).children('td').eq(2).addClass('link');
            $(row).children('td').eq(2).addClass('cList');
            // $(row).children('td').eq(1).attr('style', 'text-align: center;');
        },
        "initComplete": function () {
            $('#choosingArea_wrapper').css("cssText", "margin-top: -5px;");
            var a = $('#choosingArea_wrapper').find("div.dataTables_scrollHead");
            var headerTable = a.find("table")[0];
            var $hT_tr = $(headerTable).find("tr").eq(0);
            var $th = $hT_tr.find("th").eq(2);

            if (isSortFlag) {
                if ("ASC" == isOrderBy) {
                    $th.removeClass("sorting");
                    $th.removeClass("sorting_disabled");
                    $th.removeClass("sorting_desc");
                    $th.addClass("sorting_asc");
                } else {
                    $th.removeClass("sorting");
                    $th.removeClass("sorting_disabled");
                    $th.removeClass("sorting_asc");
                    $th.addClass("sorting_desc");
                }
                isSortFlag = false;
                isOrderBy = "";
            } else {
                $th.removeClass("sorting_disabled");
                $th.addClass("sorting");
            }

            $th.on('click', function () {
                var data_tmp = $('#choosingArea').DataTable().rows($("input[name='ch_list']").parents('tr')).data();
                if (0 == data_tmp.length)
                    return;
                getDrawsListOrderSort($th);
            });
        }
    });

    $('#choosingArea tbody').on('click', 'a[name=linkOa]', function () {
        var userTable = $('#choosingArea').DataTable();
        var data = userTable.rows( $(this).parents('tr') ).data();
        window.open(config.contextConfig.catPdfUrl + "cs/idcplg?IdcService=DOC_INFO&dID=" + data[0].did);
    });

    t.on('order.dt search.dt', function () {
        t.column(1, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
        t.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = "<input type='checkbox' name='ch_list' />";
        });
    }).draw();
}

var isSortFlag = false;
var isOrderBy = "";
function getDrawsListOrderSort($th) {
    var classString = $th.attr("class");
    var orderBy = "ASC";
    if ("sorting" == classString)
        orderBy = "ASC";
    else if ("sorting_asc" == classString)
        orderBy = "DESC";
    else if ("sorting_desc" == classString)
        orderBy = "ASC";
    isOrderBy = orderBy;
    // 初始化目录行表
    var data_tmp = $('#choosingArea').DataTable().rows($("input[name='ch_list']").parents('tr')).data();
    var to = [];
    for (var i = data_tmp.length - 1; i >= 0; i--) {
        to.push(data_tmp[i].did);
    }
    console.log(to);
    $.ajax({
        url : config.baseurl +"/api/draw/getDrawsListOrderSort",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            dids: to.toString(),
            orderBy: orderBy
        },
        type:"GET",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            var sortData = data.data;
            var arr = sortData.split(",");
            var newSelectDraws = [];
            arr.forEach(function (d) {
                for (var j = 0; j < data_tmp.length; j++) {
                    if (data_tmp[j].did == d)
                        newSelectDraws.push(data_tmp[j]);
                }
            });
            selectedDraws = newSelectDraws;
            isSortFlag = true;
            loadSelectedTable();

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

function initTmpPlt() {
    var projectId = $('#ProjectNumber').val();
    var phaseCode = $('#PhaseCode').val();
    var unitTaskCode = $('#UnitTaskNumber').val();
    var specialityCode = $('#SpecialityCode').val();
    var dlvrId = $('#DlvrId').val();
    var drawStatusCode = $('#DrawStatus').val();
    $.getJSON(config.baseurl + '/api/draw/searchTmpPlts?projectId=' + projectId + "&phaseCode=" + phaseCode + "&unitTaskCode=" + unitTaskCode
        + "&specialityCode=" + specialityCode + "&dlvrId=" + dlvrId + "&drawStatusCode=" + drawStatusCode + "&tw=" + tw, function (data) {
        tmpPlts = data;
        console.log(tmpPlts);
        loadSelectedTmpPlt();
    });
}

var t_tmpPlt;
function loadSelectedTmpPlt() {

    $('#tmpPltTable_container').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="tmpPltTable"></table>');
    t_tmpPlt = $('#tmpPltTable').DataTable({
        //设置数据源
        "data": tmpPlts,
        "scrollX": true,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        //定义列名
        "columns": [
            {
                "title": "<input type='checkbox' name='ch_listAll2' id='ch_listAll2' onclick='javascript:setCheckAll2();' />",
                "data": null
            },
            {"title": "No", "data": null},
            {"title": "项目ID", "data": "projectId"},
            {"title": "项目编号", "data": "xProjectNum"},
            {"title": "项目名称", "data": "projectSName"},
            {"title": "设备名称", "data": "xEquipment"},
            {"title": "阶段名称", "data": "xDesignPhaseName"},
            {"title": "子项编号", "data": "xDivisionNum"},
            {"title": "子项名称", "data": "xDivision"},
            {"title": "专业", "data": "xSpecialty"},
            {"title": "图纸目录号", "data": "drawNum"},
            {"title": "版本", "data": "reviseNum"},
            {"title": "创建者", "data": "createdByName"},
            {"title": "时间", "data": "createdTime"}
        ],
        //控制隐藏列
        "columnDefs": [
            {
                "searchable": false,
                "orderable": false,
                "targets": 0,
                "width": 5

            },
            {
                "searchable": false,
                "orderable": false,
                "targets": 1,
                "width": 5,
                "visible": false
            },
            {
                "searchable": false,
                "orderable": false,
                "targets": 2,
                "visible": false
            },
            {
                "searchable": false,
                "orderable": false,
                "targets": 3,
                "visible": false
            },
            {
                "targets": 4,
                "width": 160
            },{
                "targets": 5,
                "width": 100
            },{
                "targets": 6,
                "width": 70
            },{
                "targets": 7,
                "width": 70
            },{
                "targets": 8,
                "width": 70
            },{
                "targets": 9,
                "width": 70
            },{
                "targets": 10,
                "width": 180,
                "render": function (data, type, full, meta) {
                    return "<a href=#1' name='linkOa'>" + data + "</a>" ;
                }
            },{
                "targets": 11,
                "width": 40
            },{
                "targets": 12,
                "width": 50
            },{
                "targets": 13,
                "width": 130
            }
        ],
        //控制排序:按照排序号
        "order": [[13, 'desc']],
        //不分页，加入垂直导航条
        "scrollY": "180px",
        "scrollCollapse": true,
        "paging": false,
        "searching": false,
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            // $(row).children('td').eq(1).attr('style', 'text-align: center;');
        },
        "initComplete": function () {
            $('#tmpPltTable_wrapper').css("cssText", "margin-top: -5px;");
        }
    });

    $('#tmpPltTable tbody').on('click', 'a[name=linkOa]', function () {
        var userTable = $('#tmpPltTable').DataTable();
        var data = userTable.rows( $(this).parents('tr') ).data();
        window.open(config.contextConfig.catPdfUrl + "cs/idcplg?IdcService=DOC_INFO&dID=" + data[0].did);
    });

    $('#tmpPltTable tbody').on('click', 'td', function () {
        var $tr = $(this).parent("tr");
        if ( $tr.hasClass('dt-select') ) {
            $tr.removeClass('dt-select');
            var check = $tr.find("input[name='ch_list2']");
            check.prop("checked", false);
        } else {
            $tr.addClass('dt-select');
            var check = $tr.find("input[name='ch_list2']");
            check.prop("checked", true);
        }
    });

    t_tmpPlt.on('order.dt search.dt', function () {
        t_tmpPlt.column(1, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
        t_tmpPlt.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = "<input type='checkbox' name='ch_list2' />";
        });
    }).draw();
}

/**
 * 删除选定条目-待文印条目
 */
$('#delObj').on('click', function () {
    var data = t_tmpPlt.rows($("input[name='ch_list2']:checked").parents('tr')).data();
    if (0 == data.length) {
        window.parent.warring(valTips_selOne);
        return;
    }
    window.parent.showConfirm(function () {
        var to = [];
        $(data).each(function (i) {
            to.push(data[i].did);
        });
        $.ajax({
            url: config.baseurl + "/api/draw/deleteTmpPlts",
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",   //返回格式为json
            data: {
                dids: to.toString()
            },
            type: "POST",   //请求方式
            beforeSend: function () {
                window.parent.showLoading();
            },
            success: function (data) {
                initTmpPlt();
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

$('#addPo').on('click', function () {
    var data = t_tmpPlt.rows($("input[name='ch_list2']:checked").parents('tr')).data();
    if (0 == data.length) {
        window.parent.warring(valTips_selOne);
        return;
    }
    var to = [];
    var speciality = "";
    var specialityNo = 0;
    $(data).each(function (i) {
        console.log(data[i]);
        if (0 == i) {
            speciality = data[i].xSpecialtyName;
        } else {
            if (speciality != data[i].xSpecialtyName) {
                specialityNo++;
            }
        }
        to.push(data[i].did);
    });
    if (0 < specialityNo) {
        window.parent.warring("请确保选择的出图单的专业信息相同");
        return;
    }
    // return ;
    $.ajax({
        url: config.baseurl + "/api/draw/generatePltOrderMain",
        contentType: 'application/x-www-form-urlencoded',
        dataType: "json",   //返回格式为json
        data: {
            dids: to.toString(),
            pltCategory: pltCategory
        },
        type: "POST",   //请求方式
        beforeSend: function () {
            window.parent.showLoading();
        },
        success: function (data) {
            initTz_config(data);
            initTmpPlt();
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
