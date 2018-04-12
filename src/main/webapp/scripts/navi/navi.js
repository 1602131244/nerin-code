/**
 * Created by Zach on 16/5/23.
 */
var config = new nerinJsConfig();
var workTab = "";
var oaServer = "";
var modalOaDataSet = [];

// 全tab数据
var allTabData = new Array();
// 切换项目单击了哪个按钮
var indexClickNo;
// 当前激活的tabId
var activeTabId;
// 登录人员
var logonUser;
// 工作包进入
var workBag_phaseCode = "";
var workBag_specCode = "";
var workBag_workpkgType = "";

// 返回当前激活的tabId
// function getActiveIndex() {
//     var $li = $($('#homeTab li[class="active"]')[0]);
//     console.log($('#homeTab li[class="active"]'));
//     activeTabId = "_" + $($li.find("a")[0]).attr("aria-controls");
// }

$(document).keyup(function(event){
    switch(event.keyCode) {
        case 13:
            $('#bu_queryData').trigger("click");
    }
});

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

function goPlan(elem) {
    var c = $('#div_plan_' + activeTabId).children();
    var no = c.index(elem);
    var myTab = "myTab_" + activeTabId;
    if (0 == no) {
        $('#' + myTab + ' li:eq(0) a').tab('show');
    } else if (1 == no) {
        $('#' + myTab + ' li:eq(1) a').tab('show');
    } else if (2 == no) {
        $('#' + myTab + ' li:eq(2) a').tab('show');
    } else if (3 == no) {
        $('#' + myTab + ' li:eq(3) a').tab('show');
    } else if (5 == no) {
        var name = "tab_l_stage_" + activeTabId;
        var stageNo = 0;
        $('label[name="' + name + '"]').each(function () {
            if ($(this).hasClass("active"))
                stageNo = $(this).find('input')[0].value;
        });
        var url = config.contextConfig.twoChServerUrl + "projID=" + tab_l_projectId + "&phaseID=" + stageNo;
        window.open(url);
    } else if (6 == no) {
        var name = "tab_l_stage_" + activeTabId;
        var stageNo = 0;
        $('label[name="' + name + '"]').each(function () {
            if ($(this).hasClass("active"))
                stageNo = $(this).find('input')[0].value;
        });
        var url = config.contextConfig.threeChServerUrl + "projID=" + tab_l_projectId + "&phaseID=" + stageNo ;
        window.open(url);
    } else if (7 == no) {
        var name = "tab_l_stage_" + activeTabId;
        var stageNo = 0;
        $('label[name="' + name + '"]').each(function () {
            if ($(this).hasClass("active"))
                stageNo = $(this).find('input')[0].value;
        });
        var xmmc = $('#div_pro_header_' + activeTabId).text().split(")")[1];
        var url = "/pages/management/index.html?doWork=wbrw&proName=" + xmmc + "|,|" + stageNo;
        window.open(url);
    } else if (8 == no) {
        var url = config.contextConfig.zykgbgServerUrl + logonUser.employeeNo;
        window.open(url);
    } else if (9 == no) {
        var url = config.contextConfig.tjdServerUrl + logonUser.employeeNo;
        window.open(url);
    }
    else if (10 == no) {
        var name = "tab_l_stage_" + activeTabId;
        var stageNo = 0;
        $('label[name="' + name + '"]').each(function () {
            if ($(this).hasClass("active"))
                stageNo = $(this).find('input')[0].value;
        });
            var url = config.contextConfig.sjsrptServerUrl+"projID="+tab_l_projectId +"&phaseID=" + stageNo ;
            window.open(url);
    }
    else if (11 == no) {
        var name = "tab_l_stage_" + activeTabId;
        var stageNo = 0;
        $('label[name="' + name + '"]').each(function () {
            if ($(this).hasClass("active"))
                stageNo = $(this).find('input')[0].value;
        });
        //var url = config.contextConfig.oalistServerUrl+"projID="+tab_l_projectId +"&phaseID=" + stageNo + "&userID=" + logonUser.userId+"&exeOrgId="+exeOrgId+"&employeeNo="+logonUser.employeeNo;
        var url = config.contextConfig.oalistServerUrl+"projID="+tab_l_projectId +"&phaseID=" + stageNo;
        window.open(url);
    }
}

function goPlan1(elem) {
    // OA外链
    var workflowid = "interface/addworkflow.jsp?workflowid=";
    var c = $('#div_lightWeight_base_' + activeTabId).children();
    var no = c.index(elem);
    var myTab = "myTab_" + activeTabId;
    if (0 == no) {
        $('#' + myTab + ' li:eq(4) a').tab('show');
    } else if (1 == no) {
        workflowid += "4312&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    } else if (2 == no) {
        workflowid += "4309&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    } else if (3 == no) {
        workflowid += "4310&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    } else if (4 == no) {
        workflowid += "4303&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    } else if (5 == no) {
        workflowid += "4311&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    } else if (6 == no) {
        workflowid += "4304&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    } else if (7 == no) {
        workflowid += "4313&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    } else if (8 == no) {
        workflowid += "4305&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    }
}

function goPlan2(elem) {
    // OA外链
    var workflowid = "interface/addworkflow.jsp?workflowid=";
    var c = $('#div_lightWeight_phase_' + activeTabId).children();
    var no = c.index(elem);
    if (0 == no) {
        workflowid += "4290&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    } else if (1 == no) {
        workflowid += "4292&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    } else if (2 == no) {
        workflowid += "4294&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    } else if (3 == no) {
        workflowid += "4291&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    } else if (4 == no) {
        workflowid += "4300&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    } else if (5 == no) {
        workflowid += "4301&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    } else if (6 == no) {
        workflowid += "4233&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    } else if (7 == no) {
        workflowid += "4295&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    } else if (8 == no) {
        workflowid += "4296&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    } else if (9 == no) {
        workflowid += "4302&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    } else if (10 == no) {
        workflowid += "4297&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    } else if (11 == no) {
        workflowid += "4298&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    } else if (12 == no) {
        workflowid += "4299&workcode=" + logonUser.employeeNo;
        window.open(oaServer + workflowid);
    }
}

function closeChNavi(bu) {
    var $div = $(bu).parent().parent().parent();
    $div.css("cssText", "width: 40px;");
    var $div2 = $div.parent().parent();
    var div2_width = $div2[0].clientWidth;
    var $div10 = $div2.next();
    var div10_width = $div10[0].clientWidth;
    $div2.css("cssText", "width: 40px;");
    $div10.css("cssText", "width:" + (div10_width + (div2_width - 40)) + "px;");
}

function getIsManager() {
    if (0 <= getRoles().indexOf("1")) {
        return "1";
    } else {
        return "0";
    }
    // var $group = $('#div_pro_roles_' + activeTabId).find(".btn-group");
    // var label = $($group[0]).find(".active")[0];
    // if ("项目经理组" == label.innerText) {
    //     return "1";
    // } else {
    //     return "0";
    // }
}

function getRoleGroupNo() {
    var $group = $('#div_pro_roles_' + activeTabId).find(".btn-group");
    var label = $($group[0]).find(".active")[0];
    var input = $(label).find("input")[0];
    return input.value;
}

function getRoles() {
    var s = "";
    $('label[name="tab_l_role_' + activeTabId + '"]').find('input').each(function () {
        s += $(this).val() + ",";
    });
    return s;
}

function setLeftButton() {
    $('#stage_gzb_col3_' + activeTabId).hide(function () {
        $('#stage_gzb_col9_' + activeTabId).removeClass("col-md-9");
        $('#stage_gzb_col9_' + activeTabId).addClass("col-md-12");
        $('#stage_gzb_col9_' + activeTabId).css("paddingLeft", "0px");
        $('#bu_close_stage_gzb_right_' + activeTabId).show();
    });
}

function setRightButton() {
    $('#stage_gzb_col9_' + activeTabId).removeClass("col-md-12");
    $('#stage_gzb_col9_' + activeTabId).addClass("col-md-9");
    $('#stage_gzb_col9_' + activeTabId).css("paddingLeft", "5px");
    $('#bu_close_stage_gzb_right_' + activeTabId).hide();
    $('#stage_gzb_col3_' + activeTabId).show(function () {

    });
}

function setNaviLeftButton() {
    $('#navi_left_' + activeTabId).hide(function () {
        $('#navi_right_' + activeTabId).removeClass("col-md-10");
        $('#navi_right_' + activeTabId).addClass("col-md-12");
        $('#navi_right_' + activeTabId).css("marginLeft", "10px");
        $('#navi_right_' + activeTabId).css("paddingRight", "");
        $('#navi_close_' + activeTabId).show();

    });
}

window.setInterval(autoNaviCloseHeight, 600);

function autoNaviCloseHeight() {
    var s = $('#navi_close_' + activeTabId).is(":hidden");
    if (!s)
        $($('#navi_close_' + activeTabId).find('.gyl_panel_header')[0]).css("height", $('#navi_right_' + activeTabId).height() - 5);
}

function setNaviRightButton() {
    $('#navi_right_' + activeTabId).removeClass("col-md-12");
    $('#navi_right_' + activeTabId).addClass("col-md-10");
    $('#navi_right_' + activeTabId).css("marginLeft", "");
    $('#navi_right_' + activeTabId).css("paddingRight", "5px");
    $('#navi_close_' + activeTabId).hide();
    $('#navi_left_' + activeTabId).show(function () {

    });
}

function openCreateTask(v, f) {
    var url = "";
    if (2 == f) {
        url = "/pages/management/index.html?doWork=wbrw&taskName=create";
        window.open(url);
    } else if (5 == f) {
        url = config.contextConfig.tjdServerUrl + logonUser.employeeNo;
        window.open(url);
    } else if (3 == f) {
        $.getJSON(config.baseurl + "/api/naviIndexRest/getOAurl", function (data) {
            url = data.db_url + "?flowCode=AM02&erpid=" + v + "&workcode=" + logonUser.employeeNo;
            window.open(url);
        });
    }
}

$(function () {
    // 加载OA服务器ip
    $.getJSON(config.baseurl + "/api/naviIndexRest/getOAurl", function (data) {
        oaServer = data.db_indexUrl;
    });
    loadLogonUser();
    function initAddTabs(monitorString) {
        $('#tabs').addtabs({
            monitor: monitorString,
            content: proTempalte,
            callbackOk: function (id) {
                // 该项目我的角色
                loadProRoles(id);
            }
        });
    }

    bindNavStage_tmp();

    var tabCreated = function(tabId) {

        // 设置tab面板项目名称
        $('#div_pro_header_' + tabId).html($('#tab_' + tabId + ' >a>input')[0].value);
        allTabData[tabId] = createTabData(tabId);
        console.log(allTabData);
        // 绑定首页tab切换监听
        createIndexTabChange();
        // 首次创建新tab手动赋值
        setTabWholeParam(tabId);
        activeTabId = tabId;
        // 初始化里程碑
        tab_initMilepost();
        // 初始化收款计划
        tab_initContractAp();
        // 初始化项目概况
        tab_initPinfo();
        // 初始化通用文档
        tab_initPtext();
        //初始化项目预算与成本
        $('#probudget_init_' + activeTabId).on('click', function () {
            getBudgetList();
            getCostList(-1);
        });
        //tab_initBudget();
        //createPhase_gzbzxjd_tree();
       // createWb_zxjd_tree();
        // 最近进入的项目
        visitPro(tabId.split("tab_")[1]);
        // 2017-02-21分公司轻量化
        tab_initBuildingInstitutePro();
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
    
    function loadProRoles(tabId) {
        $.getJSON(config.baseurl + "/api/milestone/rolegroup?projectId=" + tabId.split("tab_")[1], function (data) {
            var list = data.dataSource;
            var no = 1;
            var roleHtml = "<div class='btn-group' data-toggle='buttons' style='margin-left: 5px;'>";
            list.forEach(function (data) {
                roleHtml += "<label class='btn btn-default btn-primary2 btn-xs" + (1 == no? " active" : "") + "' name='" + ("tab_l_role_" + tabId) + "' style='font-size: 13px; font-family: 微软雅黑;'>";
                roleHtml += "<input type='radio' name='tag' id='" + ("tab_in_role_" + no) + "' autocomplete='off' value='" + data.seq + "'" + (1 == no? " checked" : "") +" />";
                roleHtml += data.roleGroup + "</label>";
                no++;
            });
            roleHtml += "</div>";


            // 给id，name加后缀
            $('#' + tabId + " *").each(function () {
                var tmpId = $(this).attr("id");
                var tmpName = $(this).attr("name");
                if (undefined != tmpId)
                    $(this).attr("id", tmpId + "_" + tabId);
                if (undefined != tmpName)
                    $(this).attr("name", tmpName + "_" + tabId);
            });
            $('#' + tabId + " ul>li>a").each(function () {
                var tmpHref = $(this).attr("href");
                $(this).attr("href", tmpHref + "_" + tabId);
            });
            $('#' + tabId + " button").each(function () {
                var tmpHref = $(this).attr("href");
                if (undefined != tmpHref)
                    $(this).attr("href", tmpHref + "_" + tabId);
            });
            console.log(roleHtml);
            $('#div_pro_roles_' + tabId).html(roleHtml);
            //
            $('label[name="' + ('tab_l_role_' + tabId) + '"]').on('click', function () {
                setTimeout(function () {
                    loadDataWithRole();
                    $('#bu_queryMilepostVersion_' + tabId).trigger('click');
                    $('#bu_queryConapPlanVersion_' + tabId).trigger('click');
                    // 项目经理下面才有二级策划的功能，专业负责人/其他人员下没有二级策划的功能
                    closeAndOpenTwoCh();
                    closeAndOpenSk();
                }, 100);
            });
            tabCreated(tabId);
            closeAndOpenSk();
            // 全局-建筑设计院
            oaOverAll();
        });
    }

    function oaOverAll() {
        $('#t_OaOverAll_' + activeTabId + ' tbody').on('click', 'tr', function () {
            if ($(this).hasClass('dt-select') ) {
                $(this).removeClass('dt-select');
            } else {
                $('#t_OaOverAll_' + activeTabId + ' tbody tr').removeClass("dt-select");
                $(this).addClass("dt-select");
            }
        });
    }

    // 收款计划是否可见,项目预算与成本，项目文本及月报
    function closeAndOpenSk() {
        var s = getRoles();
        var li = $('#myTab_' + activeTabId + ' li');
        // if (0 <= s.indexOf("1") || 0 <= s.indexOf("3") || 0 <= s.indexOf("5")) {
        if (0 <= s.indexOf("1") || 0 <= s.indexOf("5")) { //项目经理，特殊权限
            $(li[1]).show();//收款计划
            $(li[3]).show();//项目文本
            $(li[5]).show();//成本
            if ($(li[1]).hasClass("active"))
                $('#recePlan_' + activeTabId).addClass("active in");
            if ($(li[3]).hasClass("active"))
                $('#proTask_' + activeTabId).addClass("active in");
            if ($(li[5]).hasClass("active"))
                $('#probudget_' + activeTabId).addClass("active in");
        } else {
            if ($('#recePlan_' + activeTabId).hasClass("active in"))
                $('#recePlan_' + activeTabId).removeClass("active in");
            if ($('#proTask_' + activeTabId).hasClass("active in"))
                $('#proTask_' + activeTabId).removeClass("active in");
            if ($('#probudget_' + activeTabId).hasClass("active in"))
                $('#probudget_' + activeTabId).removeClass("active in");
            $(li[1]).hide();
            $(li[3]).hide();
            $(li[5]).hide();
        }
        // var v = getRoleGroupNo();
        // var li = $('#myTab_' + activeTabId + ' li');
        // if (1 == v || 3 == v || 5 == v) {
        //     $(li[1]).show();
        //     if ($(li[1]).hasClass("active"))
        //         $('#recePlan_' + activeTabId).addClass("active in");
        // } else {
        //     if ($('#recePlan_' + activeTabId).hasClass("active in"))
        //         $('#recePlan_' + activeTabId).removeClass("active in");
        //     $(li[1]).hide();
        // }
    }
    
    function closeAndOpenTwoCh() {
        var div_plan = "div_plan" + "_" + activeTabId;
        if (1 == getIsManager())
            $('#' + div_plan + ' a').eq(5).show();
        else
            $('#' + div_plan + ' a').eq(5).hide();
    }

    function loadDataWithRole() {
        if("dev"==config.evn) {

            query_t_zykgbg();
            // query_t_gzbzxjd();
            //query_t_gzbzxjd_info();
            query_t_sjx();
            //query_t_phase_gzbzxjd_zy();
        }
            getprojectmhours();
            getprojectshours();

    }
    
    function visitPro(proId) {
        $.ajax({
            url : config.baseurl +"/api/naviIndexRest/visitPro",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                proId: proId
            },
            type:"POST",
            success:function(data){}
        });

    }

    // tab创建完成，初始化该tab的数据
    function createTabData(tabId) {
        var o = new Object();
        o.proId = tabId.split("tab_")[1];
        createTabDataPhase(o, tabId);
        // 里程碑全局赋值
        o.tab_l_projectId = o.proId;
        o.tab_l_version = 0;
        o.tab_l_version_max = 0;
        o.tab_milestoneIds = [];
        o.tab_checktype = 1;
        o.tab_showData = [];
        // 收款计划
        o.tab_rcvType = [];
        o.tab_conap_checktype = undefined;
        o.tab_conap_headerId = "";
        o.tab_conap_kheaderId = "";
        o.tab_conap_version = "";
        o.tab_conap_version_max = "";
        o.tab_linestyle = [];
        o.tab_apMilestone = [];
        o.tab_apMilestone_new=[];
        o.tab_ap_contotal = "";
        o.tab_contractAp_t2 = new Object();
        o.tab_dataSetAp = [];
        o.tab_line_num = "";
        o.tab_t_cash = new Object();
        o.tab_data_Set_c=[];
        o.tab_t_trx = new Object();
        o.tab_data_Set_t = [];
        // 项目概况
        o.tab_pinfo_t3 = new Object();
        o.tab_pinfo_t4 = new Object();
        o.tab_pinfo_ts = new Object();
        o.tab_dataSet1=[];
        o.tab_dataSet_m=[];
        o.tab_dataSet_s=[];
        o.exeOrgId = "";
        // 项目通用文档
        o.tab_dataSetPtext = [];
        o.tab_ptext_text = new Object();
        // 设计月报
        o.tab_dataSetPyb = [];
        o.tab_ptext_yb = new Object();
        //项目预算与成本
        o.tab_BudgetList=new Object();
        o.tab_dataSetProjbudget=[];
        o.tab_dataSetProjCost=[];
        o.tab_CostList=new Object();
        return o;
    }
    // 项目阶段list
    function createTabDataPhase(o, tabId) {
        $.ajax({
            url: config.baseurl + "/api/naviIndexRest/proPhaseList",
            contentType: "application/json",
            dataType: "json",
            async: false,
            data: {
                proId: o.proId
            },
            type: "GET",  //调用方法类型
            beforeSend: function () {},
            success: function (d) {
                o.phaseList = d;
                if("dev"==config.evn) {
                    var queryBarsHtml = "<div class='btn-group' data-toggle='buttons' style='margin-left: 5px;'>";//测试环境
                    d.forEach(function (data) {
                        queryBarsHtml += "<label style='padding: 3px 10px;' class='btn btn-primary2' name='tab_l_stage" + "_" + tabId + "'><input type='radio' name='in_stage' autocomplete='off' value='" + data.phaseCode + "'>" + data.phaseName + "</label>";
                    });
                }else if("prod"==config.evn) {
                    var queryBarsHtml = "<div class='btn-group' data-toggle='buttons' style='margin-left: 20px;'>";//正式环境
                    d.forEach(function (data) {
                 //       if ("1" == data.phaseCode) //正式环境
                            queryBarsHtml += "<label style='padding: 3px 10px;' class='btn btn-primary2' name='tab_l_stage" + "_" + tabId + "'><input type='radio' name='in_stage' autocomplete='off' value='" + data.phaseCode + "'>" + data.phaseName + "</label>";
                    });
                }
                queryBarsHtml += "</div><div class='gyl-panel-line'></div>";
                $('#div_phase_' + tabId).empty();
                $('#div_phase_' + tabId).append(queryBarsHtml);
                // 全局与各阶段button切换
                bindNavStage(tabId);
                // 选择之前点击的阶段
                var name = "tab_l_stage_" + tabId;
                $($('label[name="' + name + '"]')[indexClickNo]).trigger("click");
                // 如果是工作包列表进入
                if ("" != workBag_phaseCode) {
                    $('label[name="' + name + '"]').each(function (i) {
                        var label = $('label[name="' + name + '"]')[i];
                        if (workBag_phaseCode == $(label).find('input')[0].value) {
                            $(label).trigger("click");
                        }
                    });
                }
            },
            complete: function () {},
            error: function () {}
        });

        //
        // $.getJSON(config.baseurl + "/api/naviIndexRest/proPhaseList?proId=" + o.proId, function (d) {
        //
        // });
    }

    // 执行绑定监听，监听全局和阶段按钮
    function createTabChangePhase(id) {
        $('#' + id + ' ul.dropdown-menu>li>a').off('click');
        $('#' + id + ' ul.dropdown-menu>li>a').on('click', function () {
            indexClickNo = $('#' + id + ' ul.dropdown-menu>li>a').index($(this));
        });
    }

    // 执行绑定监听，监听首页tab
    function createIndexTabChange() {
        $('#homeTab li').off('click');
        $('#homeTab li').on('click', function () {
            var a = $(this).find("a")[0];
            var tabId = $(a).attr("aria-controls");
            if (0 <= tabId.indexOf("tab_"))
                setTabWholeParam(tabId);
            activeTabId = tabId;
            console.log("activeTabId变更为：" + activeTabId);
        });
        $('#homeTab').find('a[data-toggle="tab"]').off('shown.bs.tab');
        $('#homeTab').find('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            $.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
        });
    }

    function setTabWholeParam(tabId) {
        window.parent.showLoading();
        console.log("切换tab，active全局变量赋值:" + tabId);
        var data = allTabData[tabId];
        // 里程碑
        tab_l_projectId = data.tab_l_projectId;
        tab_l_version = data.tab_l_version;
        tab_l_version_max = data.tab_l_version_max;
        tab_milestoneIds = data.tab_milestoneIds;
        tab_checktype = data.tab_checktype;
        tab_showData = data.tab_showData;
        // 收款计划
        tab_rcvType = data.tab_rcvType;
        tab_conap_checktype = data.tab_conap_checktype;
        tab_conap_kheaderId = data.tab_conap_kheaderId;
        tab_dataSetAp = data.tab_dataSetAp;
        tab_conap_version = data.tab_conap_version;
        tab_conap_version_max = data.tab_conap_version_max;
        tab_linestyle = data.tab_linestyle;
        tab_apMilestone = data.tab_apMilestone;
        tab_conap_headerId = data.tab_conap_headerId;
        tab_ap_contotal = data.tab_ap_contotal;
        tab_line_num = data.tab_line_num;
        tab_contractAp_t2 = data.tab_contractAp_t2;
        tab_data_Set_c = data.tab_data_Set_c;
        tab_t_cash = data.tab_t_cash;
        tab_data_Set_t = data.tab_data_Set_t;
        tab_t_trx = data.tab_t_trx;
        // 项目概况
        tab_pinfo_t3 = data.tab_pinfo_t3;
        tab_pinfo_t4 = data.tab_pinfo_t4;
        tab_pinfo_ts = data.tab_pinfo_ts;
        tab_dataSet1 = data.tab_dataSet1;
        tab_dataSet_m = data.tab_dataSet_m;
        tab_dataSet_s = data.tab_dataSet_s;
        exeOrgId = data.exeOrgId;
        // 通用文档
        tab_dataSetPtext = data.tab_dataSetPtext;
        tab_ptext_text = data.tab_ptext_text;
        tab_dataSetPyb = data.tab_dataSetPyb;
        tab_ptext_yb = data.tab_ptext_yb;
        tab_rbsVersionId=data.tab_rbsVersionId;
        window.parent.closeLoading();
    }

    function bindNavStage_tmp() {
        var name = "tab_l_stage";
        var div_plan = "div_plan";
        $('label[name="' + name + '"]').on('click', function () {
            var _index = $('label[name="' + name + '"]').index($(this));
            $('#' + div_plan + ' a').each(function () {
                $(this).show();
            });
            if (0 == _index) {
                $('#' + div_plan + ' a').eq(4).slideUp(300);
                $('#' + div_plan + ' a').eq(5).slideUp(300);
                $('#' + div_plan + ' a').eq(6).slideUp(300);
                $('#' + div_plan + ' a').eq(8).slideUp(300);
                $('#div_overall').slideDown(300);
                $('#div_stage').slideUp(300);
            } else {
                $('#' + div_plan + ' a').eq(0).slideUp(300);
                $('#' + div_plan + ' a').eq(1).slideUp(300);
                $('#' + div_plan + ' a').eq(2).slideUp(300);
                $('#' + div_plan + ' a').eq(3).slideUp(300);
                $('#div_overall').slideUp(300);
                $('#div_stage').slideDown(300, function () {
                    phaseLoadNo = 0;
                    if("dev"==config.evn) {


                        window.parent.showLoading();
                        // 项目开工报告
                        query_t_kgbg();
                        // 专业开工报告
                        query_t_zykgbg();
                        // 工作包进度-专业
                        //query_t_phase_gzbzxjd_zy();
                        // 工作包进度-树
                        // query_t_gzbzxjd();
                        // 工作包右边-类型
                        //  query_t_phase_gzbzxjd_gzb();
                        // 工作包右边-info
                        //  init_t_gzbzxjd_gzb();
                        // 收件箱
                        query_t_sjx();
                        // 文本执行进度
                        query_t_wbzxjd();
                    }
                });
            }
        });
    }

    function bindNavStage(tabId) {
        var name = "tab_l_stage" + "_" + tabId;
        var div_plan = "div_plan" + "_" + tabId;
        $('label[name="' + name + '"]').on('click', function () {
            var $label = $(this);
            var _index = $('label[name="' + name + '"]').index($(this));
            $('#' + div_plan + ' a').each(function () {
                $(this).show();
            });
            $('#' + div_plan + ' a').eq(4).css("backgroundColor", "#ddd");
            if (0 == _index) {
                $('#' + div_plan + ' a').eq(4).slideUp(300);
                $('#' + div_plan + ' a').eq(5).slideUp(300);
                $('#' + div_plan + ' a').eq(6).slideUp(300);
                $('#' + div_plan + ' a').eq(8).slideUp(300);
                $('#div_overall' + "_" + tabId).slideDown(300);
                $('#div_stage' + "_" + tabId).slideUp(300);
            } else {
                $('#' + div_plan + ' a').eq(0).slideUp(300);
                $('#' + div_plan + ' a').eq(1).slideUp(300);
                $('#' + div_plan + ' a').eq(2).slideUp(300);
                $('#' + div_plan + ' a').eq(3).slideUp(300);
                $('#' + div_plan + ' a').eq(8).slideUp(300);
                $('#div_overall' + "_" + tabId).slideUp(300);
                $('#div_stage' + "_" + tabId).slideDown(300, function () {
                    projectId = activeTabId.split("tab_")[1];
                    phaseCode = $label.find("input")[0].value;
                    phaseLoadNo = 0;

                    param_t_phase_gzbzxjd_gzb = [];
                    param_t_phase_gzbzxjd_zy = [];
                    if("dev"==config.evn) {
                        window.parent.showLoading();
                        // 项目开工报告
                        query_t_kgbg();
                        // 专业开工报告
                        query_t_zykgbg();
                        // 工作包进度-专业
                        // query_t_phase_gzbzxjd_zy();
                        // // 工作包进度-树
                        // query_t_gzbzxjd();
                        // // 工作包右边-类型
                        // query_t_phase_gzbzxjd_gzb();
                        // 工作包右边-info
                        dataSet_gzbzxjd_gzb = [];
                        //init_t_gzbzxjd_gzb();
                        // 收件箱
                        query_t_sjx();
                        // 文本执行进度
                        query_t_wbzxjd();
                        dataSet_zxjd_detail = [];
                        init_t_zxjd_detail();
                        // 2017-02-21
                        // 分公司轻量级
                        buildingInstitutePro();
                        createTree(projectId, phaseCode);
                        createBuildingInstitutePro1();
                        createBuildingInstitutePro2();
                        createBuildingInstitutePro3();
                        
                    }else if("prod"==config.evn){
                       // window.parent.showLoading();
                        // 分公司轻量级
                        buildingInstitutePro();
                        createTree(projectId, phaseCode);
                        createBuildingInstitutePro1();
                        createBuildingInstitutePro2();
                        createBuildingInstitutePro3();
                    }
                });
            }
            setTimeout(function () {
                allShow();
            }, 100);
        });

        function buildingInstitutePro() {
            $('#t_buildingInstitutePro_' + activeTabId + ' tbody').on('click', 'tr', function () {
                if ($(this).hasClass('dt-select') ) {
                    $(this).removeClass('dt-select');
                } else {
                    $('#t_buildingInstitutePro_' + activeTabId + ' tbody tr').removeClass("dt-select");
                    $(this).addClass("dt-select");
                }
            });
        }

        $('#myTab_' + tabId).find('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            $.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
            if ("项目概况" == e.target.innerHTML) {
                createpersonInfo();
                // createmhours();
                // createshours();
            } else if ("综合管理文档" == e.target.innerHTML) {
                createtext();
                createYb();
            } else if ("收款计划" == e.target.innerHTML) {
                createcash();
                createtrx();
            } else if ("建筑院项目管理流程" == e.target.innerHTML) {
                createBuildingInstitutePro();
            }
        });
    }

    // 最近进入的项目
    // $.getJSON(config.baseurl + "/api/naviIndexRest/proJoinLogList", function (data) {
    //     var d = data.dataSource;
    //     var table = $('#t_loginLogProList');
    //     table.find("tbody").empty();
    //     $(d).each(function (i) {
    //         var row = $("<tr></tr>");
    //         var td = $("<td style='text-align: center;'></td>");
    //         var td2 = $("<td></td>");
    //         td.append("<input type='hidden' value='" + d[i].projectId + "' />");
    //         td2.append(d[i].projectNumber + "(" + d[i].projectManager + ")" + d[i].projectName);
    //         row.append(td).append(td2);
    //         table.append(row);
    //     });
    // });


    // 最近进入列表
    var dataSet_zj = [];
    var t2_zj;
    queryLogonLog();

    $('#query_logonLog').on('click', function () {
        queryLogonLog();
    });
    
    function queryLogonLog() {
        $.getJSON(config.baseurl + "/api/naviIndexRest/proJoinLogList", function (data) {
            var d = data.dataSource;
            $(d).each(function (i) {
                d[i].allString = (d[i].projectNumber + "(" + d[i].projectManager + ")" + d[i].projectName);
            });
            dataSet_zj = d;
            initLogonLogList();
        });
    }

    function initLogonLogList() {
        $('#dataTables-right2').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" style="margin-top: 0px !important;" id="t_proLogonList"></table>');
        t2_zj = $('#t_proLogonList').DataTable({
            "processing": true,
            "data": dataSet_zj,
            "paging": false,
            "searching": false,
            "autoWidth": false,
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "序号","data": null},
                {"title": "项目","data": "allString"}
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
                $('#t_proLogonList_info').remove();
            }
        });

        $('#t_proLogonList tbody').on('click', 'tr', function (e) {
            if ( $(this).hasClass('dt-select') ) {
                $(this).removeClass('dt-select');
            }
            else {
                $('#t_proLogonList').DataTable().$('tr.dt-select').removeClass('dt-select');
                $(this).addClass('dt-select');
            }
            var userTable = $('#t_proLogonList').DataTable();
            var data = userTable.rows( $(this)).data();
            showIndexLoginButton($(this), data[0].projectId, data[0].projectName, "zj", data[0].allString);
        });

        t2_zj.on( 'order.dt search.dt', function () {
            t2_zj.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } );
        } ).draw();
    }

// 工时列表
    $('#query_WorktimeList').on('click', function () {
        window.open(worktimeurl1);
    });
    $('#flash_Worktime').on('click', function () {
        queryWorklist();
    });
var arr=[];
    function _getDay(){//获取所在周各个Day
        var date=new Date;
        var mon = new Date(date.getFullYear(),date.getMonth(),date.getDate());
        mon.setDate(mon.getDate()-(mon.getDay()==0? 6 : mon.getDay()-1));
        var year=mon.getFullYear();
        var month=mon.getMonth()+1;
        var day=mon.getDate();
        day=(day<10?"0"+day:day);
        month =(month<10 ? "0"+month:month);
        var mydate = (year.toString()+"-"+month.toString()+"-"+day.toString());
        console.log(mydate);
        for(var i=0; i<7; i++) {
             year=mon.getFullYear();
             month=mon.getMonth()+1;
             day=mon.getDate();
            day=(day<10?"0"+day:day);
            month =(month<10 ? "0"+month:month);
            var mydate1 = (year.toString()+"-"+month.toString()+"-"+day.toString());
            arr.push(mydate1);
            mon.setDate(mon.getDate() + 1);
        }
        console.log(arr);
        return mydate;
    }
    var dataSet2 ;
    var t2;
    queryWorklist();
    function queryWorklist() {
        $.ajax({     //异步，调用后台get方法
            url: config.baseurl + "/api/wh/getWhInfo", //调用地址，不包含参数
            contentType: "application/json",
            dataType: "json",
            async: true,
            data: {
                currDate:_getDay(),  //传入参数，依次填写
                perId:0
            },
            type: "GET",  //调用方法类型
            beforeSend: function () {
            },
            success: function (data) {
               // dataSet2=data.rows;
                var datarow=data.rows;
                dataSet2=[{"day":"一","fulldate":arr[0],"totalHour": 0},
                    {"day":"二","fulldate":arr[1],"totalHour": 0},
                    {"day":"三","fulldate":arr[2],"totalHour": 0},
                    {"day":"四","fulldate":arr[3],"totalHour": 0},
                    {"day":"五","fulldate":arr[4],"totalHour": 0},
                    {"day":"六","fulldate":arr[5],"totalHour":0},
                    {"day":"日","fulldate":arr[6],"totalHour": 0}
            ]
                console.log(datarow.length);
                for(var i=0;i<datarow.length;i++){
                    for(var j=0;j<7;j++) {
                        if (dataSet2[j].fulldate == datarow[i].fulldate)
                            dataSet2[j].totalHour = datarow[i].totalHour;
                    }
                }
              //  console.log(datarow[0].totalHour) ;
                initWorktimeList() ;
            },
            complete: function () {
            },
            error: function () {
                window.parent.error(ajaxError_sys);
            }
        });
    }
    var worktimeurl=config.baseurl + "/pages/wbsp/code/workCalendar/workCalendar.html?date=";
    var worktimeurl1=config.baseurl + "/pages/wbsp/code/workCalendar/workCalendar.html";
    function initWorktimeList() {
        $('#dataTables-right').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" style="margin-top: 0px !important;" id="t_WorktimeList"></table>');
       // var url=config.baseurl + "/pages/wbsp/code/workCalendar/workCalendar.html?date=";

        t2 = $('#t_WorktimeList').DataTable({
            "processing": true,
            "data": dataSet2,
           // "pagingType": "simple",
            "paging": false,
            "searching": false,
            "autoWidth": false,
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "星期","data":"day" ,"className": "dt_center"},
                {"title": "日期","data": "fulldate", "className": "dt_center"},
                {"title": "已填工时","data": "totalHour","className": "dt_center"},
            ],
            "columnDefs": [{
                "searchable": false,
                "orderable": false,
                "targets": 0,
                "width": 50
            },{
                "targets": 1,
                "width": 100,
                "orderable": false,
            },{
                "targets": 2,
                "orderable": false,
            }

            ],
            "order": [],
            "createdRow": function( row, data, dataIndex ) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
                $(row).children('td').eq(1).attr('style', 'text-align: center;');
                $(row).children('td').eq(2).attr('style', 'text-align: center;');
                $(row).children('td').eq(1).html( "<a name='w_worktime' href="+"'"+worktimeurl+data.fulldate+"'"+" target='view_window'>" +data.fulldate+ "</a>");//合同行号
            },
            "initComplete": function () {
                $('#t_WorktimeList_length').remove();
                $('#t_WorktimeList_info').remove();
            }
        });
        //$("table[id=t_WorktimeList] tr").eq(0).find("td").eq(1).html("一");
        // $('#t_WorktimeList tbody').on('click', 'tr', function (e) {
        //     if ( $(this).hasClass('dt-select') ) {
        //         $(this).removeClass('dt-select');
        //     }
        //     else {
        //         $('#t_WorktimeList').DataTable().$('tr.dt-select').removeClass('dt-select');
        //         $(this).addClass('dt-select');
        //     }
        //     var userTable = $('#t_WorktimeList').DataTable();
        //     var data = userTable.rows( $(this)).data();
        //    // showIndexLoginButton2($(this), data[0].projectId, data[0].projName, data[0].elementNumber, data[0].allString, data[0].phaseCode, data[0].specCode, data[0].workpkgType);
        // });

    }

    var dataSet = [];
    var t;
    function queryList() {
        $('#dataTables-listAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-data"></table>');

        t = $('#dataTables-data').DataTable({
            "searching": false,
            "pageLength": 20,
            "processing": true,
            "data": dataSet,
            "scrollX": true,
            "scrollY": "600px",
            "pagingType": "full_numbers",
            "lengthMenu": [20, 50, 100],
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "序号", "data": null},
                {"title": "项目编号", "data": "projectNumber"},
                {"title": "项目名", "data": "projectName"},
                {"title": "客户", "data": "customer"},
                {"title": "状态", "data": "projectStatus"},
                {"title": "项目类型", "data": "projectType"},
                {"title": "行业类型", "data": "projectClass"},
                {"title": "项目经理", "data": "projectManager"},
                {"title": "所属组织", "data": "projectOrgName"},
                {"title": "项目起始日期", "data": "projStartDate"},
                {"title": "项目完成日期", "data": "completionDate"},
                {"title": "是否财务超期", "data": "overdue"}
            ],
            "columnDefs": [{
                "searchable": false,
                "orderable": false,
                "targets": 0,
                "width": 32
            },{
                "width": 90,
                // "visible": false,
                "targets": 1
            },{
                "width": 200,
                "targets": 2
            },{
                "width": 200,
                "targets": 3
            },{
                "width": 50,
                "targets": 4
            },{
                "width": 60,
                "targets": 5
            },{
                "width": 60,
                "targets": 6
            },{
                "width": 60,
                "targets": 7
            },{
                "width": 65,
                "targets": 8
            },{
                "width": 85,
                "targets": 9
            },{
                "width": 85,
                "targets": 10
            },{
                "width": 85,
                "targets": 11
            }
            ],
            "order": [],
            "createdRow": function( row, data, dataIndex ) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
                // $(row).children('td').eq(1).attr('style', 'text-align: center;');
            },
            "initComplete": function () {
                $('#dataTables-data_wrapper').css("cssText", "margin-top: -5px;");
                $('#dataTables-data_length').insertBefore($('#dataTables-data_info'));
                $('#dataTables-data_length').addClass("col-sm-4");
                $('#dataTables-data_length').css("paddingLeft", "0px");
                $('#dataTables-data_length').css("paddingTop", "5px");
                $('#dataTables-data_length').css("maxWidth", "130px");
            }
        });

        $('#dataTables-data tbody').on('click', 'tr', function () {
            if ( $(this).hasClass('dt-select') ) {
                $(this).removeClass('dt-select');
            }
            else {
                $('#dataTables-data').DataTable().$('tr.dt-select').removeClass('dt-select');
                $(this).addClass('dt-select');
            }
            var userTable = $('#dataTables-data').DataTable();
            var data = userTable.rows( $(this)).data();
            var proId = data[0].projectId;
            var proName = data[0].projectName;
            var allTitle = data[0].projectNumber + "(" + data[0].projectManager + ")" + data[0].projectName;
            $.ajax({
                url : config.baseurl +"/api/naviIndexRest/proPhaseList",
                contentType : 'application/x-www-form-urlencoded',
                dataType:"json",
                data: {
                    proId: proId
                },
                type:"GET",
                beforeSend:function(){
                    window.parent.showLoading();
                },
                success:function(data){
                    $('#allProTab').empty();
                    var button = $("<button type='button' class='btn btn-default btn-sm dropdown-toggle' data-toggle='dropdown' aria-haspopup='true' style='padding: 3px 10px; font-size: 13px; font-family: 微软雅黑;' aria-expanded='false'>"
                        + "进入<span class='caret'></span></button></div>");
                    var ul = $("<ul class='dropdown-menu'></ul>");
                    $(data).each(function (i) {
                        var li;
                        if("dev"==config.evn) {
                            if ("1" == data[i].phaseCode)
                                li = $("<li><a href='#1' id='bu_goAll" + "_" + proId + "' data-addtab='" + proId + "' url='/admin/message' content='指定内容" + proId + "' title='" + proName + "<input type=hidden value=" + allTitle + "/>" + "'>综合管理</a></li>");
                            else
                            //进入下拉列表显示阶段
                            li = $("<li><a href='#1' id='bu_stage" + "_" + data[i].phaseCode + "' data-addtab='" + proId + "' url='/admin/message' content='指定内容" + proId + "' title='" + proName + "<input type=hidden value=" + allTitle + "/>" + "'>" + data[i].phaseName + "</a></li>");
                      }else if("prod"==config.evn){
                           if ("1" == data[i].phaseCode)
                               li = $("<li><a href='#1' id='bu_goAll" + "_" + proId + "' data-addtab='" + proId + "' url='/admin/message' content='指定内容" + proId + "' title='" + proName + "<input type=hidden value=" + allTitle + "/>" + "'>综合管理</a></li>");
                           else
                           //进入下拉列表显示阶段
                               li = $("<li><a href='#1' id='bu_stage" + "_" + data[i].phaseCode + "' data-addtab='" + proId + "' url='/admin/message' content='指定内容" + proId + "' title='" + proName + "<input type=hidden value=" + allTitle + "/>" + "'>" + data[i].phaseName + "</a></li>");
                        }
                        
                        ul.append(li);

                    });
                    $('#allProTab').append(button).append(ul);
                    // 给下拉按钮加单击监听
                    createTabChangePhase("allProTab");
                    initAddTabs("#allProTab");
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

        t.on( 'order.dt search.dt', function () {
            t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } );
        } ).draw();

    }

    function showIndexLoginButton($div, proId, proName, type, allTitle) {
        var $td = $div.children('td');
        if (0 == $td.eq(1).find("div[name='div_login_log_pro']").length) {
            $.ajax({
                url : config.baseurl +"/api/naviIndexRest/proPhaseList",
                contentType : 'application/x-www-form-urlencoded',
                dataType:"json",
                data: {
                    proId: proId
                },
                type:"GET",
                beforeSend:function(){
                    window.parent.showLoading();
                },
                success:function(data){
                    var div = $("<div name='div_login_log_pro' class='panel panel-default' style='margin-top: -20px; position:absolute; right:0; z-index: 999; border: 0px;'></div>");
                    var div_c = $("<div class='panel-body' style='padding: 0px; height: 0px;' id='tabPro_" + type + "_" + proId + "'></div>");
                    var button = $("<button type='button' class='btn btn-default btn-xs dropdown-toggle' style='padding-top: 0px; padding-bottom: 0px;'  data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>"
                        + "进入<span class='caret'></span></button></div>");
                    var ul = $("<ul class='dropdown-menu'></ul>");
                    $(data).each(function (i) {
                        var li;
                        if("dev"==config.evn) {
                        if ("1" == data[i].phaseCode)
                            li = $("<li><a href='#1' id='bu_goAll" + "_" + proId +"' data-addtab='" + proId + "' url='/admin/message' content='指定内容" + proId + "' title='" + proName + "<input type=hidden value=" + allTitle + "/>" + "'>综合管理</a></li>");
                        //进入下拉列表阶段页签显示
                        else
                            li = $("<li><a href='#1' id='bu_stage" + "_" + data[i].phaseCode + "' data-addtab='" + proId + "' url='/admin/message' content='指定内容" + proId + "' title='" + proName + "<input type=hidden value=" + allTitle + "/>" + "'>" + data[i].phaseName + "</a></li>");
                        }else if("prod"==config.evn){
                            if ("1" == data[i].phaseCode)
                                li = $("<li><a href='#1' id='bu_goAll" + "_" + proId +"' data-addtab='" + proId + "' url='/admin/message' content='指定内容" + proId + "' title='" + proName + "<input type=hidden value=" + allTitle + "/>" + "'>综合管理</a></li>");
                            else
                            li = $("<li><a href='#1' id='bu_stage" + "_" + data[i].phaseCode + "' data-addtab='" + proId + "' url='/admin/message' content='指定内容" + proId + "' title='" + proName + "<input type=hidden value=" + allTitle + "/>" + "'>" + data[i].phaseName + "</a></li>");

                        }
                        ul.append(li);
                    });
                    div_c.append(button).append(ul);
                    div.append(div_c);
                    $td.eq(1).append(div);
                    $('div[name="div_login_log_pro"]').each(function () {
                        $(this).hide();
                    })
                    $td.eq(1).find("div[name='div_login_log_pro']").show();
                    var montior = "#tabPro_" + type + "_" + proId;
                    // 给下拉按钮加单击监听
                    createTabChangePhase("tabPro_" + type + "_" + proId);
                    initAddTabs(montior);
                },
                complete:function(){
                    window.parent.closeLoading();
                },
                error:function(){
                    window.parent.closeLoading();
                    window.parent.error(ajaxError_loadData);
                }
            });
        } else {
            $('div[name="div_login_log_pro"]').each(function () {
                $(this).hide();
            })
            $td.eq(1).find("div[name='div_login_log_pro']").show();
        }

    }

    function showIndexLoginButton2($div, proId, proName, type, allTitle, phaseCode, specCode, workpkgType) {
        if (0 == phaseCode) {
            window.parent.warring("工作包没有对应的阶段！");
            workBag_phaseCode = "";
            workBag_specCode = "";
            workBag_workpkgType = "";
            return;
        }
        workBag_phaseCode = phaseCode;
        workBag_specCode = specCode;
        workBag_workpkgType = workpkgType;
        var $td = $div.children('td');
        if (0 == $td.eq(1).find("div[name='div_login_log_pro']").length) {
            var div = $("<div name='div_login_log_pro' class='panel panel-default' style='margin-top: -20px; position:absolute; right:0; z-index: 999; border: 0px;'></div>");
            var div_c = $("<div class='panel-body' style='padding: 0px; height: 0px;' id='tabPro_" + type + "_" + proId + "'></div>");
            var button = $("<button type='button' class='btn btn-default btn-xs' style='padding-top: 0px; padding-bottom: 0px;'"
                + "id='bu_stage"  + "_" +  phaseCode +"' data-addtab='" + proId + "' url='/admin/message' content='指定内容" + proId + "' title='" + proName + "<input type=hidden value=" + allTitle + "/>" + "'"
                + ">"
                + "进入"
                + "</button></div>"); //  + "<a href='#1' id='bu_stage"  + "_" +  phaseCode +"' data-addtab='" + proId + "' url='/admin/message' content='指定内容" + proId + "' title='" + proName + "<input type=hidden value=" + allTitle + "/>" + "'></a>"
            div_c.append(button);
            div.append(div_c);
            $td.eq(1).append(div);
            $('div[name="div_login_log_pro"]').each(function () {
                $(this).hide();
            })
            $td.eq(1).find("div[name='div_login_log_pro']").show();
            var montior = "#tabPro_" + type + "_" + proId;
            // 给下拉按钮加单击监听
            initAddTabs(montior);
        } else {
            $('div[name="div_login_log_pro"]').each(function () {
                $(this).hide();
            })
            $td.eq(1).find("div[name='div_login_log_pro']").show();
        }
        indexClickNo = 0;
    }

    // $('#homeTab a:last').tab('show');

    //bindNavStage();

    $('#bu_queryData').on('click', function () {
        $.ajax({
            url : config.baseurl +"/api/naviIndexRest/proMyAllList",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",   //返回格式为json
            async: true,//请求是否异步，默认为异步，这也是ajax重要特性
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

   $('#bu_queryData').trigger("click");
   // queryList();

    // 阶段
    var projectId ;//= "12107";
    var phaseCode ;//= "1021";
    var phaseLoadNo = 0;
    function query_t_kgbg() {
        $.ajax({
            url : config.baseurl +"/api/phaseRest/phaseForXmkgbg",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                projectId: projectId,
                phaseCode: phaseCode
            },
            type:"GET",
            beforeSend:function(){},
            success:function(data){
                phaseLoadNo++;
                dataSet_kgbg = data;
                init_t_kgbg();
                phaseClose();
            },
            complete:function(){},
            error:function(){
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    function query_t_zykgbg() {
        $.ajax({
            url : config.baseurl +"/api/phaseRest/phaseForZykgbg",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                projectId: projectId,
                phaseCode: phaseCode,
                isManager: getIsManager()
            },
            type:"GET",
            beforeSend:function(){},
            success:function(data){
                phaseLoadNo++;
                dataSet_zykgbg = data;
                init_t_zykgbg();
                phaseClose();
            },
            complete:function(){},
            error:function(){
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    function query_t_gzbzxjd() {
        // 如果是工作包列表进入
        if ("" != workBag_specCode) {
            param_t_phase_gzbzxjd_zy = [];
            param_t_phase_gzbzxjd_zy.push(workBag_specCode);
        }
        $.ajax({
            url : config.baseurl +"/api/phaseRest/phaseForGzbzxjd_gzbTree",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                projectId: projectId,
                phaseCode: phaseCode,
                speciality: param_t_phase_gzbzxjd_zy.toString(),
                isManager: getIsManager()
            },
            type:"GET",
            beforeSend:function(){},
            success:function(data){
                phaseLoadNo++;
                data_gzbzxjd = data;
                reload_jd_data_gzbzxjd();
                // 建筑院项目子项任务
                reload_buildingInstitutePro2_tree(data);
                phaseClose();
            },
            complete:function(){},
            error:function(){
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    function query_t_gzbzxjd_info() {
        if (0 == t_gzbzxjd_tree_sel.length) {
            dataSet_gzbzxjd_gzb = [];
            init_t_gzbzxjd_gzb();
        } else {
            // 如果是工作包列表进入
            if ("" != workBag_workpkgType) {
                param_t_phase_gzbzxjd_gzb = [];
                param_t_phase_gzbzxjd_gzb.push(workBag_workpkgType);
            }
            $.ajax({
                url : config.baseurl +"/api/phaseRest/phaseForGzbzxjd",
                contentType : 'application/x-www-form-urlencoded',
                dataType:"json",
                data: {
                    projectId: projectId,
                    phaseCode: phaseCode,
                    isManager: getIsManager(),
                    specialitys: t_gzbzxjd_tree_sel.toString(),
                    workpkgTypeCode: param_t_phase_gzbzxjd_gzb.toString()
                },
                type:"POST",
                beforeSend:function(){window.parent.showLoading();},
                success:function(data){
                    dataSet_gzbzxjd_gzb = data.dataSource;
                    init_t_gzbzxjd_gzb();
                },
                complete:function(){window.parent.closeLoading();},
                error:function(){
                    window.parent.closeLoading();
                    window.parent.error(ajaxError_loadData);
                }
            });
        }
    }

    function query_t_sjx() {
        $.ajax({
            url : config.baseurl +"/api/phaseRest/phaseForJktjsjx",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                projectId: projectId,
                phaseCode: phaseCode,
                isManager: getIsManager(),
                pageNo: 1,
                pageSize: 3000
            },
            type:"GET",
            beforeSend:function(){},
            success:function(data){
                phaseLoadNo++;
                dataSet_sjx = data.dataSource;
                init_t_sjx();
                phaseClose();
            },
            complete:function(){},
            error:function(){
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    function query_t_wbzxjd() {
        $.ajax({
            url : config.baseurl +"/api/phaseRest/phaseForXzwbzxjdHeader",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                projectId: projectId,
                phaseCode: phaseCode
            },
            type:"GET",
            beforeSend:function(){},
            success:function(data){
                phaseLoadNo++;
                dataSet_zxjd = data;
                init_t_zxjd();
                phaseClose();
            },
            complete:function(){},
            error:function(){
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    function phaseClose() {
       if("dev"==config.evn) {
           if (4 == phaseLoadNo) {
               window.parent.closeLoading();
           }
       }else if("prod"==config.evn){
               window.parent.closeLoading();
       }
    }


    function query_t_phase_gzbzxjd_zy() {
        $('#div_phase_gzbzxjd_zy_' + activeTabId).empty();
        $('#div_stage_buildingInstitutePro_zy_' + activeTabId).empty();
        $('#div_stage_buildingInstitutePro2_zy_' + activeTabId).empty();
        $.getJSON(config.baseurl + "/api/phaseRest/phaseForGzbzxjd_zy?projectId=" + projectId + "&phaseCode=" + phaseCode + "&isManager=" + getIsManager(), function (data) {
            var queryBarsGzbzxjd_zy = "<div class='btn-group' data-toggle='buttons'><label class='btn btn-primary2 btn-xs' style='margin-bottom: 2px;' id='b_phase_gzbzxjd_zyAll_" + activeTabId + "'><input type='checkbox' name='b_phase_gzbzxjd_zyAll_" + activeTabId + "' value='all'>全选</label>&nbsp;";
            var buildingInstitutePro_zy = "<div class='btn-group' data-toggle='buttons'><label class='btn btn-primary2 btn-xs' style='margin-bottom: 2px;' id='b_phase_buildingInstitutePro_zyAll_" + activeTabId + "'><input type='checkbox' name='b_phase_buildingInstitutePro_zyAll_" + activeTabId + "' value='all'>全选</label>&nbsp;";
            var buildingInstitutePro2_zy = "<div class='btn-group' data-toggle='buttons'><label class='btn btn-primary2 btn-xs' style='margin-bottom: 2px;' id='b_phase_buildingInstitutePro2_zyAll_" + activeTabId + "'><input type='checkbox' name='b_phase_buildingInstitutePro2_zyAll_" + activeTabId + "' value='all'>全选</label>&nbsp;";
            data.forEach(function (d) {
                queryBarsGzbzxjd_zy += "<label name='l_phase_gzbzxjd_zy_" + activeTabId + "' class='btn btn-primary2 btn-xs' style='margin-bottom: 2px;'><input type='checkbox' name='phase_gzbzxjd_zy_" + activeTabId + "' value='" + d.specialityCode + "'>" + d.specialityName + "</label>&nbsp;";
                buildingInstitutePro_zy += "<label name='l_phase_buildingInstitutePro_zy_" + activeTabId + "' class='btn btn-primary2 btn-xs' style='margin-bottom: 2px;'><input type='checkbox' name='phase_buildingInstitutePro_zy_" + activeTabId + "' value='" + d.specialityCode + "'>" + d.specialityName + "</label>&nbsp;";
                buildingInstitutePro2_zy += "<label name='l_phase_buildingInstitutePro2_zy_" + activeTabId + "' class='btn btn-primary2 btn-xs' style='margin-bottom: 2px;'><input type='checkbox' name='phase_buildingInstitutePro2_zy_" + activeTabId + "' value='" + d.specialityCode + "'>" + d.specialityName + "</label>&nbsp;";
            });
            queryBarsGzbzxjd_zy += " </div>";
            $('#div_phase_gzbzxjd_zy_' + activeTabId).append(queryBarsGzbzxjd_zy);
            $('#div_stage_buildingInstitutePro_zy_' + activeTabId).append(buildingInstitutePro_zy);
            $('#div_stage_buildingInstitutePro2_zy_' + activeTabId).append(buildingInstitutePro2_zy);

            $('#b_phase_gzbzxjd_zyAll_' + activeTabId).on('click', function () {
                if (0 == $("input[name='b_phase_gzbzxjd_zyAll_" + activeTabId + "']:checked").length) {
                    $("label[name='l_phase_gzbzxjd_zy_" + activeTabId + "']").each(function () {
                        if (!$(this).hasClass('active')) {
                            $(this).addClass("active");
                            var $input = $(this).find("input");
                            $($input[0]).prop("checked", true);
                        } else {

                        }
                    });
                } else {
                    $("label[name='l_phase_gzbzxjd_zy_" + activeTabId + "']").each(function () {
                        if ($(this).hasClass('active')) {
                            $(this).removeClass("active");
                            var $input = $(this).find("input");
                            $($input[0]).prop("checked", false);
                        } else {

                        }
                    });
                }
                set_gzbzxjd_zy();

            });

            // 如果是工作包列表进入
            if ("" != workBag_specCode) {
                $("label[name='l_phase_gzbzxjd_zy_" + activeTabId + "']").each(function () {
                    if (workBag_specCode == $(this).find('input')[0].value) {
                        $(this).trigger("click");
                    }
                });
            }

            $("label[name='l_phase_gzbzxjd_zy_" + activeTabId + "']").on('click', function () {
                setTimeout(function () {
                    set_gzbzxjd_zy();
                }, 100);
                //set_gzbzxjd_zy();
            });

            // 建筑院设计流程、项目子项任务
            $('#b_phase_buildingInstitutePro_zyAll_' + activeTabId).on('click', function () {
                if (0 == $("input[name='b_phase_buildingInstitutePro_zyAll_" + activeTabId + "']:checked").length) {
                    $("label[name='l_phase_buildingInstitutePro_zy_" + activeTabId + "']").each(function () {
                        if (!$(this).hasClass('active')) {
                            $(this).addClass("active");
                            var $input = $(this).find("input");
                            $($input[0]).prop("checked", true);
                        } else {

                        }
                    });
                } else {
                    $("label[name='l_phase_buildingInstitutePro_zy_" + activeTabId + "']").each(function () {
                        if ($(this).hasClass('active')) {
                            $(this).removeClass("active");
                            var $input = $(this).find("input");
                            $($input[0]).prop("checked", false);
                        } else {

                        }
                    });
                }
            });

            $('#b_phase_buildingInstitutePro2_zyAll_' + activeTabId).on('click', function () {
                if (0 == $("input[name='b_phase_buildingInstitutePro2_zyAll_" + activeTabId + "']:checked").length) {
                    $("label[name='l_phase_buildingInstitutePro2_zy_" + activeTabId + "']").each(function () {
                        if (!$(this).hasClass('active')) {
                            $(this).addClass("active");
                            var $input = $(this).find("input");
                            $($input[0]).prop("checked", true);
                        } else {

                        }
                    });
                } else {
                    $("label[name='l_phase_buildingInstitutePro2_zy_" + activeTabId + "']").each(function () {
                        if ($(this).hasClass('active')) {
                            $(this).removeClass("active");
                            var $input = $(this).find("input");
                            $($input[0]).prop("checked", false);
                        } else {

                        }
                    });
                }
                query_buildingInstitutePro2_tree();
            });

            $("label[name='l_phase_buildingInstitutePro2_zy_" + activeTabId + "']").on('click', function () {
                setTimeout(function () {
                    query_buildingInstitutePro2_tree();
                }, 100);
            });

            $('#div_stage_buildingInstitutePro3_zy_' + activeTabId).empty();
            $.getJSON(config.baseurl + "/api/phaseRest/phaseForJzyJktjsjx?projectId=" + projectId + "&phaseCode=" + phaseCode + "&isManager=" + getIsManager(), function (data) {
                var query_zy = "<div class='btn-group' data-toggle='buttons'><label class='btn btn-primary2 btn-xs' style='margin-bottom: 2px;' id='b_phase_buildingInstitutePro3_zyAll_" + activeTabId + "'><input type='checkbox' name='b_phase_buildingInstitutePro3_zyAll_" + activeTabId + "' value='all'>全选</label>&nbsp;";
                data.forEach(function (d) {
                    query_zy += "<label name='l_phase_buildingInstitutePro3_zy_" + activeTabId + "' class='btn btn-primary2 btn-xs' style='margin-bottom: 2px;'><input type='checkbox' name='phase_buildingInstitutePro3_zy_" + activeTabId + "' value='" + d.specialityCode + "'>" + d.specialityName + "</label>&nbsp;";
                });
                query_zy += " </div>";
                $('#div_stage_buildingInstitutePro3_zy_' + activeTabId).append(query_zy);

                $('#b_phase_buildingInstitutePro3_zyAll_' + activeTabId).on('click', function () {
                    if (0 == $("input[name='b_phase_buildingInstitutePro3_zyAll_" + activeTabId + "']:checked").length) {
                        $("label[name='l_phase_buildingInstitutePro3_zy_" + activeTabId + "']").each(function () {
                            if (!$(this).hasClass('active')) {
                                $(this).addClass("active");
                                var $input = $(this).find("input");
                                $($input[0]).prop("checked", true);
                            } else {

                            }
                        });
                    } else {
                        $("label[name='l_phase_buildingInstitutePro3_zy_" + activeTabId + "']").each(function () {
                            if ($(this).hasClass('active')) {
                                $(this).removeClass("active");
                                var $input = $(this).find("input");
                                $($input[0]).prop("checked", false);
                            } else {

                            }
                        });
                    }
                    query_buildingInstitutePro3_tree();
                });

                $("label[name='l_phase_buildingInstitutePro3_zy_" + activeTabId + "']").on('click', function () {
                    setTimeout(function () {
                        query_buildingInstitutePro3_tree();
                    }, 100);
                });
            });
            query_buildingInstitutePro3_tree();

            // 工作包右边-类型
            query_t_phase_gzbzxjd_gzb();
        });
    }

    function query_buildingInstitutePro2_tree() {
        var zy = [];
        $("input[name='phase_buildingInstitutePro2_zy_" + activeTabId + "']:checked").each(function () {
            zy.push($(this).val());
        });
        $.ajax({
            url : config.baseurl +"/api/phaseRest/phaseForGzbzxjd_gzbTree",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                projectId: projectId,
                phaseCode: phaseCode,
                speciality: zy.toString(),
                isManager: getIsManager()
            },
            type:"GET",
            beforeSend:function(){},
            success:function(data){
                // 建筑院项目子项任务
                reload_buildingInstitutePro2_tree(data);
            },
            complete:function(){},
            error:function(){
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    function query_buildingInstitutePro3_tree() {
        var zy = [];
        $("input[name='phase_buildingInstitutePro3_zy_" + activeTabId + "']:checked").each(function () {
            zy.push($(this).val());
        });
        $.ajax({
            url : config.baseurl +"/api/phaseRest/phaseForJzy_Tree",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                projectId: projectId,
                phaseCode: phaseCode,
                speciality: zy.toString(),
                isManager: getIsManager()
            },
            type:"GET",
            beforeSend:function(){},
            success:function(data){
                // 建筑院项目子项任务
                reload_buildingInstitutePro3_tree(data);
            },
            complete:function(){},
            error:function(){
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    // 左边树的条件筛选触发
    var param_t_phase_gzbzxjd_zy = [];
    function set_gzbzxjd_zy() {
        param_t_phase_gzbzxjd_zy = [];
        $("input[name='phase_gzbzxjd_zy_" + activeTabId + "']:checked").each(function () {
            param_t_phase_gzbzxjd_zy.push($(this).val());
        });
       // query_t_gzbzxjd();
    }

    // 右边工作包的条件筛选触发
    var param_t_phase_gzbzxjd_gzb = [];
    function set_gzbzxjd_gzb() {
        param_t_phase_gzbzxjd_gzb = [];
        $("input[name='phase_gzbzxjd_gzb_" + activeTabId + "']:checked").each(function () {
            param_t_phase_gzbzxjd_gzb.push($(this).val());
        });
      //  query_t_gzbzxjd_info();
    }

    function query_t_phase_gzbzxjd_gzb() {
        $('#div_phase_gzbzxjd_gzb_' + activeTabId).empty();
        $.getJSON(config.baseurl + "/api/phaseRest/phaseForGzblx", function (data) {
            var queryBarsGzbzxjd_gzb = "<div class='btn-group' data-toggle='buttons'><label class='btn btn-primary2 btn-xs' style='margin-bottom: 2px;' id='b_phase_gzbzxjd_gzbAll_" + activeTabId + "'><input type='checkbox' name='b_phase_gzbzxjd_gzbAll_" + activeTabId + "' value='all'>全选</label>&nbsp;";
            data.forEach(function (d) {
                queryBarsGzbzxjd_gzb += "<label name='l_phase_gzbzxjd_gzb_" + activeTabId + "' class='btn btn-primary2 btn-xs' style='margin-bottom: 2px;'><input type='checkbox' name='phase_gzbzxjd_gzb_" + activeTabId + "' value='" + d.typeCode + "'>" + d.typeName + "</label>&nbsp;";
            });
            queryBarsGzbzxjd_gzb += " </div>";
            $('#div_phase_gzbzxjd_gzb_' + activeTabId).append(queryBarsGzbzxjd_gzb);

            $('#b_phase_gzbzxjd_gzbAll_' + activeTabId).on('click', function () {
                if (0 == $("input[name='b_phase_gzbzxjd_gzbAll_" + activeTabId + "']:checked").length) {
                    $("label[name='l_phase_gzbzxjd_gzb_" + activeTabId + "']").each(function () {
                        if (!$(this).hasClass('active')) {
                            $(this).addClass("active");
                            var $input = $(this).find("input");
                            $($input[0]).prop("checked", true);
                        } else {

                        }
                    });
                } else {
                    $("label[name='l_phase_gzbzxjd_gzb_" + activeTabId + "']").each(function () {
                        if ($(this).hasClass('active')) {
                            $(this).removeClass("active");
                            var $input = $(this).find("input");
                            $($input[0]).prop("checked", false);
                        } else {

                        }
                    });
                }
                // set_gzbzxjd_gzb();
            });

            $("label[name='l_phase_gzbzxjd_gzb_" + activeTabId + "']").on('click', function () {
                // setTimeout(function () {
                //     set_gzbzxjd_gzb();
                // }, 100);
            });

            // 如果是工作包列表进入
            if ("" != workBag_workpkgType) {
                $("label[name='l_phase_gzbzxjd_gzb_" + activeTabId + "']").each(function () {
                    if (workBag_workpkgType == $(this).find('input')[0].value) {
                        $(this).trigger("click");
                    }
                });
            }

            // 查询按钮绑定
            $('#bu_queryGzbzxjd_' + activeTabId).off('click');
            $('#bu_queryGzbzxjd_' + activeTabId).on('click', function () {
                set_gzbzxjd_gzb();
            });

            // 工作包进度-树
          //  query_t_gzbzxjd();
        });
    }

    // 工作包执行进度-左-树
    var data_gzbzxjd = [];
    var t_gzbzxjd_tree_sel = [];
    function createPhase_gzbzxjd_tree() {
        data_gzbzxjd = [];
        $("#t_gzbzxjd_tree_" + activeTabId).fancytree({
            selectMode: 3,
            extensions: ["dnd", "edit", "glyph", "table"],
            checkbox: true,
            keyboard: false,
            dnd: {
                focusOnClick: true
            },
            glyph: glyph_opts,
            source: data_gzbzxjd,
            table: {
                checkboxColumnIdx: 0,
                nodeColumnIdx: 1
            },
            select: function(event, data) {
                if (data.node.selected)
                    $(data.node.tr).css("backgroundColor", "#C6E2FF");
                else
                    $(data.node.tr).css("backgroundColor", "");
                var selNodes = data.tree.getSelectedNodes();
                t_gzbzxjd_tree_sel = [];
                selNodes.forEach(function(node) {
                    if ("10006" == node.data.typeId)
                        t_gzbzxjd_tree_sel.push(node.data.projElementId);
                });
                // query_t_gzbzxjd_info();
            },
            renderColumns: function(event, data) {
                var node = data.node,
                    $tdList = $(node.tr).find(">td");
                $tdList.eq(2).text(node.data.taskName + "(" + node.data.progress + "%)");
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

    function reload_jd_data_gzbzxjd() {
        var tree = $("#t_gzbzxjd_tree_" + activeTabId).fancytree("getTree");
        tree.reload(data_gzbzxjd).done(function(){
            $("#t_gzbzxjd_tree_" + activeTabId).fancytree("getTree").visit(function(node){
                // 如果是工作包列表进入
                if ("" != workBag_specCode)
                    node.setSelected(true);
                node.setExpanded(true);
            });
            // 如果是工作包列表进入
            if ("" != workBag_specCode) {
                $('#tage_gzb_' + activeTabId).focus();
                $('#bu_queryGzbzxjd_' + activeTabId).trigger('click');
                workBag_specCode = "";
                workBag_workpkgType = "";
                workBag_phaseCode = "";
            }
        });
    }
    //引号转义
    function replacedata(data) {
        var r=data.replace(/'/g,"&apos");
        var q=r.replace(/"/g,"&quot");
    return q;
    }
    // 项目开工报告
    var t_kgbg;
    var dataSet_kgbg = [];
    function init_t_kgbg() {
        if (null == dataSet_kgbg || 0 == dataSet_kgbg.length) {
            $('#stage_kgbg_' + activeTabId).removeClass("in");
        } else {
            $('#stage_kgbg_' + activeTabId).addClass("in");
        }

        $('#div_jd_kgbg_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="t_kgbg_' + activeTabId + '"></table>');
        t_kgbg = $('#t_kgbg_' + activeTabId).DataTable({
            // "scrollY": "300px",
            "scrollX": true,
            // "autoWidth": true,
            "data": dataSet_kgbg,
            "pagingType": "full",
            "searching": false,
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "序号","data": null},
                {"title": "文本名称","data": "taskName"},
                {"title": "创建人","data": "createdName"},
                {"title": "章节完成情况","data": "taskProgress"},
                {"title": "状态","data": "taskStatusName"},
                {"title": "查看","data": null},
            ],
            "columnDefs": [{
                "searchable": false,
                "orderable": false,
                "targets": 0,
                "width": 32
            },{
                "targets": 1,
                "width": 620,
                "render": function (data, type, full, meta) {
                    return "<a name='a_kgbg' href='"+"/pages/management/index.html?doWork=wbrw&taskName=" + replacedata(data) + "&taskHeaderId=" + full.taskHeaderId + "' target='_blank'>" + data + "</a>";
                }
            },{
                "targets": 2,
                "width": 80
            },{
                "targets": 3,
                "width": 100
            },{
                "targets": 4,
                "width": 130,
                "render": function (data, type, full, meta) {
                    return "<a name='a_modal_oa' href='#1'>" + data + "</a>";
                }
            },{
                "targets": 5,
                "width": 50,
                "render": function (data, type, full, meta) {
                    if ("批准" == full.taskStatusName)
                        return "<a name='a_word' href='#1'>查看文本</a>";
                    else
                        return "";
                }
            }
            ],
            "order": [],
            "createdRow": function( row, data, dataIndex ) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
            },
            "initComplete": function () {
                $('#t_kgbg_' + activeTabId + '_length').insertBefore( $('#t_kgbg_' + activeTabId + '_info'));
                $('#t_kgbg_' + activeTabId + '_length').addClass("col-sm-4");
                $('#t_kgbg_' + activeTabId + '_length').css("paddingLeft", "0px");
                $('#t_kgbg_' + activeTabId + '_length').css("paddingTop", "5px");
                $('#t_kgbg_' + activeTabId + '_length').css("maxWidth", "130px");
                $('#t_kgbg_' + activeTabId + '_wrapper').css("cssText", "margin-top: -5px;");
            }
        });

        $('#t_kgbg_' + activeTabId + ' tbody').on('click', 'a[name=a_modal_oa]', function () {
            var userTable = $('#t_kgbg_' + activeTabId).DataTable();
            var data = userTable.rows( $(this).parents('tr') ).data();
             query_modal_oa(data[0].taskHeaderId);
            //queryOAInfor(data[0].taskHeaderId, "", "430", "120", "0");
        });

        $('#t_kgbg_' + activeTabId + ' tbody').on('click', 'a[name=a_word]', function () {
            var userTable = $('#t_kgbg_' + activeTabId).DataTable();
            var data = userTable.rows( $(this).parents('tr') ).data();
            var x = new ActiveXObject("ActiveX.ActiveX");
            x.OpenTaskWord(data[0].taskHeaderId, data[0].templateHeaderId, logonUser.userId, logonUser.fullName);
        });

        $('#t_kgbg_' + activeTabId + ' tbody').on('click', 'tr', function () {
            if ( $(this).hasClass('dt-select') ) {
                $(this).removeClass('dt-select');
            }
            else {
                $('#t_kgbg_' + activeTabId).DataTable().$('tr.dt-select').removeClass('dt-select');
                $(this).addClass('dt-select');
            }
        });

        t_kgbg.on( 'order.dt search.dt', function () {
            t_kgbg.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } );
        } ).draw();
    }

    // 专业开工报告
    var t_zykgbg;
    var dataSet_zykgbg = [];
    function init_t_zykgbg() {
        if (null == dataSet_zykgbg || 0 == dataSet_zykgbg.length) {
            $('#stage_zykgbg_' + activeTabId).removeClass("in");
        } else {
            $('#stage_zykgbg_' + activeTabId).addClass("in");
        }

        $('#div_jd_zykgbg_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="t_zykgbg_' + activeTabId + '"></table>');
        t_zykgbg = $('#t_zykgbg_' + activeTabId).DataTable({
            // "scrollY": "300px",
            // "scrollCollapse": true,
            "scrollX": true,
            "data": dataSet_zykgbg,
            "pagingType": "full",
            "searching": false,
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "序号","data": null},
                {"title": "专业","data": "specialityName"},
                {"title": "流程名称","data": "bt"},
                {"title": "创建人","data": "createdName"},
                {"title": "状态","data": "status"},
                {"title": "创建时间","data": "createDate"},
                {"title": "办结时间","data": "lastOperateDate"}
            ],
            "columnDefs": [{
                "searchable": false,
                "orderable": false,
                "targets": 0,
                "width": 32
            },{
                "targets": 1,
                "width": 150,
                "render": function (data, type, full, meta) {
                    return "<a name='a_modal_ebs' href='#1'>" + data + "</a>";
                }
            },{
                "targets": 2,
                "width": 350,
                "render": function (data, type, full, meta) {
                    if ("无记录" == data)
                       // return "<a name='a_modal_ebs' href='#1'>" + data + "</a>";
                        return data;
                    else
                        return "<a name='a_modal_oa' href='#1'>" + data + "</a>";
                }
            },{
                "targets": 3,
                "width": 80
            },{
                "targets": 4,
                "width": 125
            },{
                "targets": 5,
                "width": 130
            },{
                "targets": 6,
                "width": 130
            }
            ],
            "order": [],
            "createdRow": function( row, data, dataIndex ) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
            },
            "initComplete": function () {
                $('#t_zykgbg_' + activeTabId + '_length').insertBefore( $('#t_zykgbg_' + activeTabId + '_info'));
                $('#t_zykgbg_' + activeTabId + '_length').addClass("col-sm-4");
                $('#t_zykgbg_' + activeTabId + '_length').css("paddingLeft", "0px");
                $('#t_zykgbg_' + activeTabId + '_length').css("paddingTop", "5px");
                $('#t_zykgbg_' + activeTabId + '_length').css("maxWidth", "130px");
                $('#t_zykgbg_' + activeTabId + '_wrapper').css("cssText", "margin-top: -5px;");
            }
        });

        $('#t_zykgbg_' + activeTabId + ' tbody').on('click', 'a[name=a_modal_oa]', function () {
            // var userTable = $('#t_zykgbg_' + activeTabId).DataTable();
            // var data = userTable.rows( $(this).parents('tr') ).data();
            // var id = "4349";
            // var formId = "190";
            // var v = projectId + "," + phaseCode + "," + data[0].specialityCode;
            // queryOAInfor(v, "", id, formId, "5");
                var userTable = $('#t_zykgbg_' + activeTabId).DataTable();
                var data = userTable.rows( $(this).parents('tr') ).data();
                console.log(data[0].requestId);
                openOA_workFlow(data[0].requestId);
            });

        $('#t_zykgbg_' + activeTabId + ' tbody').on('click', 'a[name=a_modal_ebs]', function () {
            var userTable = $('#t_zykgbg_' + activeTabId).DataTable();
            var data1 = userTable.rows($(this).parents('tr')).data();
            window.parent.showConfirm(function () {
                $.getJSON(config.baseurl + "/api/naviIndexRest/getOAurl", function (data) {
                    var v = projectId + "," + phaseCode + "," + data1[0].specialityCode;
                    url = data.db_url + "?flowCode=PM36&erpid=" + v + "&workcode=" + logonUser.employeeNo;
                    window.open(url);

                });
            }, "请确认提交"+data1[0].specialityName+"专业开工报告？");
        });
        $('#t_zykgbg_' + activeTabId + ' tbody').on('click', 'tr',
            function () {
            if ( $(this).hasClass('dt-select') ) {
                $(this).removeClass('dt-select');
            }
            else {
                $('#t_zykgbg_' + activeTabId).DataTable().$('tr.dt-select').removeClass('dt-select');
                $(this).addClass('dt-select');
            }
        });

        t_zykgbg.on( 'order.dt search.dt', function () {
            t_zykgbg.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } );
        } ).draw();
    }

    function format_t_gzbzxjd_gzb(d, row, tr) {
        var msg = '<div class="panel panel-success"><div class="panel-heading" style="padding: 1px 1px 1px 5px;">'
            + '工时填写情况</div><div class="panel-body" style="padding: 5px;"><table class="table table-bordered" style="max-width: 310px; border-top: 1px solid #ddd !important;">'
            + '<tr> <th width="40">序号</th> <th width="100">角色</th> <th width="80">姓名</th> <th width="80">工时数</th> </tr>';

        $.ajax({
            url : config.baseurl +"/api/phaseRest/phaseForGzbzxjdDetail",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                projElementId: d.projElementId
            },
            type:"GET",
            beforeSend:function(){window.parent.showLoading();},
            success:function(data){
                data.forEach(function (d2) {
                    msg += '<tr> <td style="text-align: center">' + d2.num + '</td> <td>' + d2.role + '</td> <td>' + d2.fullName + '</td> <td>' + d2.workTime + '</td> </tr>'
                });
                msg += '</table>';
                msg += '</div></div>';
                row.child(msg).show();
                tr.addClass('shown');
            },
            complete:function(){window.parent.closeLoading();},
            error:function(){
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    // 工作包执行进度info
    var t_gzbzxjd_gzb;
    var dataSet_gzbzxjd_gzb = [];
    function init_t_gzbzxjd_gzb() {
        $('#div_jd_gzbzxjd_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="t_gzbzxjd_gzb_' + activeTabId + '"></table>');
        t_gzbzxjd_gzb = $('#t_gzbzxjd_gzb_' + activeTabId).DataTable({
            "data": dataSet_gzbzxjd_gzb,
            "searching": false,
            "processing": true,
            "scrollX": true,
            "scrollY": "560px",
            "pageLength": 20,
            "lengthMenu": [20, 50, 100],
            // fixedColumns: {
            //     leftColumns: 4
            // },
            "pagingType": "full_numbers",
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "序号","data": null},
                {"title": "子项","data": "subTask"},
                {"title": "专业","data": "spac"},
                {"title": "工作包","data": "elementName"},
                {"title": "计划完成时间","data": "dueDate"},
                {"title": "实际完成时间","data": "completionDate"},
                {"title": "工作包进度","data": "completedPercentage"},
                {"title": "当前状态","data": "projectStatusName"},
                {"title": "计划工时/已填工时","data": "actTime"},
                {"title": "设计人","data": "design"},
                {"title": "校核人","data": "checked"},
                {"title": "审核人","data": "examine"},
                {"title": "审定人","data": "authorize"},
                {"title": "策划人","data": "creater"}
            ],
            "columnDefs": [{
                "searchable": false,
                "orderable": false,
                "targets": 0,
                "width": 32
            },{
                "targets": 1,
                "width": 60
            },{
                "targets": 2,
                "width": 70
            },{
                "targets": 3,
                "width": 200
            },{
                "targets": 4,
                "width": 100
            },{
                "targets": 5,
                "width": 100
            },{
                "targets": 6,
                "width": 100
            },{
                "targets": 7,
                "width": 70,
                "render": function (data, type, full, meta) {
                    return "<a name='a_gzblx_detail' href='#gzblx'>" + data + "</a>";
                }
            },{
                "targets": 8,
                "width": 70,
                "render": function (data, type, full, meta) {
                    return "<a name='a_gzbgs_detail' href='#gzbgs'>" + data + "</a>";
                }
            },{
                "targets": 9,
                "width": 50
            },{
                "targets": 10,
                "width": 50
            },{
                "targets": 11,
                "width": 50
            },{
                "targets": 12,
                "width": 50
            },{
                "targets": 13,
                "width": 50
            }
            ],
            "order": [],
            "createdRow": function( row, data, dataIndex ) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
            },
            "initComplete": function () {
               // var $div = $('#t_gzbzxjd_gzb_' + activeTabId + '_info').parent();
               // var $div2 = $('#t_gzbzxjd_gzb_' + activeTabId + '_paginate').parent();
                // $div.removeClass("col-sm-6");
                // $div.addClass("col-sm-5");
                // $div2.removeClass("col-sm-6");
                // $div2.addClass("col-sm-7");
                $('#t_gzbzxjd_gzb_' + activeTabId + '_length').insertBefore($('#t_gzbzxjd_gzb_' + activeTabId + '_info'));
                $('#t_gzbzxjd_gzb_' + activeTabId + '_length').addClass("col-sm-4");
                $('#t_gzbzxjd_gzb_' + activeTabId + '_length').css("paddingLeft", "0px");
                $('#t_gzbzxjd_gzb_' + activeTabId + '_length').css("paddingTop", "5px");
                $('#t_gzbzxjd_gzb_' + activeTabId + '_length').css("maxWidth", "130px");
                $('#t_gzbzxjd_gzb_' + activeTabId + '_wrapper').css("cssText", "margin-top: -5px;");
            }
        });

        $('#t_gzbzxjd_gzb_' + activeTabId + ' tbody').on('click', 'a[name=a_gzblx_detail]', function () {
            var userTable = $('#t_gzbzxjd_gzb_' + activeTabId).DataTable();
            var data = userTable.rows( $(this).parents('tr') ).data();
            var pkgTypeCode = data[0].workpkgTypeCode;
            clear_wb_tz_tjd_jss_Modal(~~(pkgTypeCode));
            if ("02" == pkgTypeCode) { // 文本
                $('#wb_tz_tjd_jss_ModalLabel').html("文本列表");
                query_modal_wb(data[0].projElementId);
                var bu = '<button type="button" class="btn btn-sm btn-xs" style="margin-left: 10px;" onclick="javascript:openCreateTask(null, 2);"><i class="fa fa-plus" style="color: #3CB371;"></i>发起任务</button>'
                if (logonUser.fullName == data[0].design)
                    $('#wb_tz_tjd_jss_ModalLabel').append(bu);
            } else if ("01" == pkgTypeCode) { // 图纸

            } else if ("05" == pkgTypeCode) { // 条件单
                $('#wb_tz_tjd_jss_ModalLabel').html("条件单");
                query_modal_tjd(data[0].projElementId);
                var bu = '<button type="button" class="btn btn-sm btn-xs" style="margin-left: 10px;" onclick="javascript:openCreateTask(null, 5);"><i class="fa fa-plus" style="color: #3CB371;"></i>发起任务</button>'
                if (logonUser.fullName == data[0].design)
                    $('#wb_tz_tjd_jss_ModalLabel').append(bu);
            } else if ("03" == pkgTypeCode) { // 计算书
                $('#wb_tz_tjd_jss_ModalLabel').html("计算书");
                query_modal_jss(data[0].projElementId);
                var dlvrId = data[0].projElementId;
                var bu = '<button type="button" class="btn btn-sm btn-xs" style="margin-left: 10px;" onclick="javascript:openCreateTask(' + dlvrId +', 3);"><i class="fa fa-plus" style="color: #3CB371;"></i>发起任务</button>'
                if (logonUser.fullName == data[0].design)
                    $('#wb_tz_tjd_jss_ModalLabel').append(bu);
            } else {

            }
        });

        $('#t_gzbzxjd_gzb_' + activeTabId + ' tbody').on('click', 'a[name=a_gzbgs_detail]', function () {
            var tr = $(this).closest('tr');
            var row = $('#t_gzbzxjd_gzb_' + activeTabId).DataTable().row( tr );
            if ( row.child.isShown() ) {
                row.child.hide();
                tr.removeClass('shown');
            } else {
                format_t_gzbzxjd_gzb(row.data(), row, tr);
            }
        } );

        $('#t_gzbzxjd_gzb_' + activeTabId + ' tbody').on('click', 'tr', function () {
            if ( $(this).hasClass('dt-select') ) {
                $(this).removeClass('dt-select');
            }
            else {
                $('#t_gzbzxjd_gzb_' + activeTabId).DataTable().$('tr.dt-select').removeClass('dt-select');
                $(this).addClass('dt-select');
            }
        });

        t_gzbzxjd_gzb.on( 'order.dt search.dt', function () {
            t_gzbzxjd_gzb.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } );
        } ).draw();
    }

    // 接口条件收件箱
    var t_sjx;
    var dataSet_sjx = [];
    function init_t_sjx() {
        if (null == dataSet_sjx || 0 == dataSet_sjx.length) {
            $('#stage_jktjx_' + activeTabId).removeClass("in");
        } else {
            $('#stage_jktjx_' + activeTabId).addClass("in");
        }

        $('#div_jd_sjx_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="t_sjx_' + activeTabId + '"></table>');
        t_sjx = $('#t_sjx_' + activeTabId).DataTable({
            "data": dataSet_sjx,
            "searching": false,
            "processing": true,
            "scrollX": true,
            "autoWidth": true,
            "pagingType": "full",
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "序号","data": null},
                {"title": "子项","data": "subTask"},
                {"title": "专业","data": "receiveSpecialty"},
                {"title": "工作包","data": "elementName"},
                {"title": "计划完成时间","data": "dueDate"},
                {"title": "实际完成时间","data": "completionDate"},
                {"title": "当前状态","data": "projectStatusName"},
                {"title": "设计人","data": "design"},
                {"title": "校核人","data": "checked"},
                {"title": "审核人","data": "examine"},
                {"title": "审定人","data": "authorize"},
                {"title": "策划人","data": "creater"}
            ],
            "columnDefs": [{
                "searchable": false,
                "orderable": false,
                "targets": 0,
                "width": 32
            },{
                "targets": 1,
                "width": 100
            },{
                "targets": 2,
                "width": 130
            },{
                "targets": 3,
                "width": 150
            },{
                "targets": 4,
                "width": 90
            },{
                "targets": 5,
                "width": 90
            },{
                "targets": 6,
                "width": 80,
                "render": function (data, type, full, meta) {
                    return "<a name='a_modal_tjd_oa' href='#1'>" + data + "</a>";
                }
            },{
                "targets": 7,
                "width": 50
            },{
                "targets": 8,
                "width": 50
            },{
                "targets": 9,
                "width": 50
            },{
                "targets": 10,
                "width": 50
            },{
                "targets": 11,
                "width": 50
            }
            ],
            "order": [],
            "createdRow": function( row, data, dataIndex ) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
            },
            "initComplete": function () {
                $('#t_sjx_' + activeTabId + '_length').insertBefore( $('#t_sjx_' + activeTabId + '_info'));
                $('#t_sjx_' + activeTabId + '_length').addClass("col-sm-4");
                $('#t_sjx_' + activeTabId + '_length').css("paddingLeft", "0px");
                $('#t_sjx_' + activeTabId + '_length').css("paddingTop", "5px");
                $('#t_sjx_' + activeTabId + '_length').css("maxWidth", "130px");
                $('#t_sjx_' + activeTabId + '_wrapper').css("cssText", "margin-top: -5px;");
                //$('#t_sjx_length').remove();
                //$('#t_sjx_info').remove();
            }
        });

        $('#t_sjx_' + activeTabId + ' tbody').on('click', 'a[name=a_modal_tjd_oa]', function () {
            var userTable = $('#t_sjx_' + activeTabId).DataTable();
            var data = userTable.rows( $(this).parents('tr') ).data();
            clear_wb_tz_tjd_jss_Modal(5);
            query_modal_tjd(data[0].projElementId);
        });

        $('#t_sjx_' + activeTabId + ' tbody').on('click', 'tr', function () {
            if ( $(this).hasClass('dt-select') ) {
                $(this).removeClass('dt-select');
            }
            else {
                $('#t_sjx_' + activeTabId).DataTable().$('tr.dt-select').removeClass('dt-select');
                $(this).addClass('dt-select');
            }
        });

        t_sjx.on( 'order.dt search.dt', function () {
            t_sjx.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } );
        } ).draw();
    }

    var t_zxjd;
    var dataSet_zxjd = [];
    function init_t_zxjd() {
        if (null == dataSet_zxjd || 0 == dataSet_zxjd.length) {
            $('#stage_wbzxjd_' + activeTabId).removeClass("in");
        } else {
            $('#stage_wbzxjd_' + activeTabId).addClass("in");
        }

        $('#div_jd_zxjd_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="t_zxjd_' + activeTabId + '"></table>');
        t_zxjd = $('#t_zxjd_' + activeTabId).DataTable({
            // "scrollY": "300px",
            // "scrollCollapse": true,
            "scrollX": true,
            "data": dataSet_zxjd,
            "pagingType": "full",
            "searching": false,
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "序号","data": null},
                {"title": "文本名称","data": "taskName"},
                {"title": "创建人","data": "creater"},
                {"title": "章节完成情况","data": "taskProgress"},
                {"title": "专业检审完成情况","data": "taskProgress2"},
                {"title": "状态","data": "status"}
            ],
            "columnDefs": [{
                "searchable": false,
                "orderable": false,
                "targets": 0,
                "width": 32
            },{
                "targets": 1,
                "width": 500,
                "render": function (data, type, full, meta) {
                    return "<a name='a_kgbg' href='/pages/management/index.html?doWork=wbrw&taskName=" + data.trim() + "&taskHeaderId=" + full.taskHeaderId + "' target='_blank'>" + data + "</a>";
                }
            },{
                "targets": 2,
                "width": 80
            },{
                "targets": 3,
                "width": 130
            },{
                "targets": 4,
                "width": 130
            },{
                "targets": 5,
                "width": 130,
                "render": function (data, type, full, meta) {
                    return "<a name='a_modal_oa' href='#1'>" + data + "</a>";
                }
            }
            ],
            "order": [],
            "createdRow": function( row, data, dataIndex ) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
            },
            "initComplete": function () {
                $('#t_zxjd_' + activeTabId + '_length').insertBefore( $('#t_zxjd_' + activeTabId + '_info'));
                $('#t_zxjd_' + activeTabId + '_length').addClass("col-sm-4");
                $('#t_zxjd_' + activeTabId + '_length').css("paddingLeft", "0px");
                $('#t_zxjd_' + activeTabId + '_length').css("paddingTop", "5px");
                $('#t_zxjd_' + activeTabId + '_length').css("maxWidth", "130px");
                $('#t_zxjd_' + activeTabId + '_wrapper').css("cssText", "margin-top: -5px;");
            }
        });

        $('#t_zxjd_' + activeTabId + ' tbody').on('click', 'a[name=a_modal_oa]', function () {
            var userTable = $('#t_zxjd_' + activeTabId).DataTable();
            var data = userTable.rows( $(this).parents('tr') ).data();

            var id = "";
            var formId = "";
            if ("B" == data[0].tag) {
                id = "1128,432";
                formId = "122";
            } else if ("C" == data[0].tag) {
                id = "2150";
                formId = "120";
            }
            queryOAInfor(data[0].taskHeaderId, "", id, formId, "73");
            // query_modal_oa(data[0].taskHeaderId);
        });

        $('#t_zxjd_' + activeTabId + ' tbody').on('click', 'tr', function () {
            if ( $(this).hasClass('dt-select') ) {
                // $(this).removeClass('dt-select');
            } else {
                $('#t_zxjd_' + activeTabId).DataTable().$('tr.dt-select').removeClass('dt-select');
                $(this).addClass('dt-select');
                var userTable = $('#t_zxjd_' + activeTabId).DataTable();
                var data = userTable.rows($(this)).data();
                loadZxjdDetail(data[0].taskHeaderId);
            }
        });

        t_zxjd.on( 'order.dt search.dt', function () {
            t_zxjd.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } );
        } ).draw();
    }

    function loadZxjdDetail(taskHeaderId) {
        $.ajax({
            url : config.baseurl +"/api/phaseRest/phaseForXzwbzxjd",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                taskHeaderId: taskHeaderId
            },
            type:"GET",
            beforeSend:function(){
                window.parent.showLoading();
            },
            success:function(data){
                dataSet_zxjd_detail = data;
                dataSet_zxjd_detail.shift();
                console.log(dataSet_zxjd_detail);
                init_t_zxjd_detail();
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

    var t_zxjd_detail;
    var dataSet_zxjd_detail = [];
    function init_t_zxjd_detail() {
        $('#div_jd_zxjd_detail_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="t_zxjd_detail_' + activeTabId + '"></table>');
        t_zxjd_detail = $('#t_zxjd_detail_' + activeTabId).DataTable({
            "scrollY": "300px",
            "scrollX": true,
            // "autoWidth": false,
            "scrollCollapse": true,
            "data": dataSet_zxjd_detail,
            "paging": false,
            "searching": false,
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "节点","data": "chapterName"},
                {"title": "章节完成情况","data": "progress"},
                {"title": "计划专业校审完成日期","data": "dueDate"},
                {"title": "实际专业校审完成日期","data": "zyjsCompletionDate"},
                {"title": "当前状态","data": "chapterStatus"},
                {"title": "当前处理人","data": "currentPerson"},
                {"title": "节点责任人","data": "owner"},
                {"title": "设计人","data": "designed"},
                {"title": "校核人","data": "checked"},
                {"title": "审核人","data": "reviewed"},
                {"title": "审定人","data": "approved"},
                {"title": "授权人","data": "sqr"}
            ],
            "columnDefs": [{
                "searchable": false,
                "orderable": false,
                "targets": 0,
                "width": 200
            },{
                "searchable": false,
                "orderable": false,
                "targets": 1,
                "width": 100
            },{
                "searchable": false,
                "orderable": false,
                "targets": 2,
                "width": 70
            },{
                "searchable": false,
                "orderable": false,
                "targets": 3,
                "width": 70
            },{
                "searchable": false,
                "orderable": false,
                "targets": 4,
                "width": 70,
                "render": function (data, type, full, meta) {
                    if ("APPROVING" == full.statusCode || "COMPLETED" == full.statusCode || "EAPPROVED" == full.statusCode || "EAPPROVING" == full.statusCode
                        || "IAPPROVED" == full.statusCode || "IAPPROVING" == full.statusCode || "N-EAPPROVED" == full.statusCode || "N-IAPPROVED" == full.statusCode) {
                        return "<a name='a_modal_oa' href='#1'>" + data + "</a>";
                    } else {
                        return data;
                    }
                }
            },{
                "searchable": false,
                "orderable": false,
                "targets": 5,
                "width": 70
            },{
                "searchable": false,
                "orderable": false,
                "targets": 6,
                "width": 70
            },{
                "searchable": false,
                "orderable": false,
                "targets": 7,
                "width": 55
            },{
                "searchable": false,
                "orderable": false,
                "targets": 8,
                "width": 55
            },{
                "searchable": false,
                "orderable": false,
                "targets": 9,
                "width": 55
            },{
                "searchable": false,
                "orderable": false,
                "targets": 10,
                "width": 55
            },{
                "searchable": false,
                "orderable": false,
                "targets": 11,
                "width": 55
            }
            ],
            "order": [],
            "createdRow": function( row, data, dataIndex ) {
               
            },
            "initComplete": function () {
                $('#t_zxjd_detail_' + activeTabId + '_length').remove();
                $('#t_zxjd_detail_' + activeTabId + '_info').remove();
            }
        });

        $('#t_zxjd_detail_' + activeTabId + ' tbody').on('click', 'a[name=a_modal_oa]', function () {
            var userTable = $('#t_zxjd_detail_' + activeTabId).DataTable();
            var data = userTable.rows( $(this).parents('tr') ).data();

            // 头表
            var userTable2 = $('#t_zxjd_' + activeTabId).DataTable();
            var data2 = userTable2.rows('.selected').data();

            var id = "";
            var formId = "";
            if ("B" == data2[0].tag) {
                id = "2174,431";
                formId = "121";
            } else if ("C" == data2[0].tag) {
                id = "431";
                formId = "121";
            }
            queryOAInfor(data2[0].taskHeaderId, data[0].chapterId, id, formId, "75");
        });

    }


    // 文本执行进度还没有接入
    function createWb_zxjd_tree() {
        $("#t_zxjd_detail_" + activeTabId).fancytree({
            // selectMode: 2,
            extensions: ["dnd", "edit", "glyph", "table"],
            checkbox: false,
            keyboard: false,
            dnd: {
                focusOnClick: true,
                // dragStart: function(node, data) { return true; },
                // dragEnter: function(node, data) { return true; },
                // dragDrop: function(node, data) { data.otherNode.copyTo(node, data.hitMode); }
            },
            glyph: glyph_opts,
            source: data_zxjd_detail,
            table: {
                // checkboxColumnIdx: 0,
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


    var data_zxjd_detail = [];
    function reload_jd_zxjd_detail() {
        var tree = $("#t_zxjd_detail_" + activeTabId).fancytree("getTree");
        tree.reload(data_zxjd_detail).done(function(){

        });
    }


    //弹出modal_oa查询
    function query_modal_oa(taskHeaderId) {
        $.ajax({
            url : config.baseurl +"/api/phaseRest/phaseForOalc",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                taskHeaderId: taskHeaderId
            },
            type:"GET",
            beforeSend:function(){window.parent.showLoading();},
            success:function(data){
                modalOaDataSet = data;
                $('#oaModal').modal("show").css("top", "13%");
            },
            complete:function(){window.parent.closeLoading();},
            error:function(){
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    // OA流程弹出框
    $('#oaModal').on('shown.bs.modal', function (e) {
        load_modal_oa();
    });

    // Oa列表定义
    var t_modal_oa;
    function load_modal_oa() {
        $('#dataTables-oas').html('<table cellpadding="0" cellspacing="0" border="0" class="table table-bordered table-hover" id="dataTables-oa"></table>');
        t_modal_oa = $('#dataTables-oa').DataTable({
            "autoWidth": true,
            "data": modalOaDataSet,
            "paging": false,
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "序号", "data": null},
                {"title": "流程名称", "data": "workflowname"},
                {"title": "流程状态", "data": "status"},
                {"title": "发起人", "data": "lastname"},
                {"title": "发起时间", "data": "createtime"},
                {"title": "当前处理人", "data": "approver"}
            ],
            "columnDefs": [{
                "searchable": false,
                "orderable": false,
                "targets": 0,
                "width": 32
            },{
                "searchable": false,
                "orderable": false,
                "width": 260,
                "targets": 1,
                "render": function (data, type, full, meta) {
                    return "<a name='a_modal_oa' href='#1'>" + data + "</a>";
                }
            },{
                "searchable": false,
                "orderable": false,
                "targets": 2,
                "width": 80
            },{
                "searchable": false,
                "orderable": false,
                "targets": 3,
                "width": 50
            },{
                "searchable": false,
                "orderable": false,
                "targets": 4,
                "width": 80
            },{
                "searchable": false,
                "orderable": false,
                "targets": 5,
                "width": 80
            }
            ],
            "order": [],
            "createdRow": function( row, data, dataIndex ) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
            },
            "initComplete": function () {
                $('#dataTables-oa_length').remove();
            }
        });

        $('#dataTables-oa tbody').on('click', 'a[name=a_modal_oa]', function () {
            var userTable = $('#dataTables-oa').DataTable();
            var data = userTable.rows( $(this).parents('tr') ).data();
            openOA_workFlow(data[0].requestid);
        });

        $('#dataTables-oa tbody').on('click', 'tr', function () {
            if ( $(this).hasClass('dt-select') ) {

            } else {
                $('#dataTables-oa').DataTable().$('tr.dt-select').removeClass('dt-select');
                $(this).addClass('dt-select');
            }
        });

        t_modal_oa.on( 'order.dt search.dt', function () {
            t_modal_oa.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } );
        } ).draw();

    }

    // 清空弹出框数据
    function clear_wb_tz_tjd_jss_Modal(type) {
        $('#dataTables-wbs').hide();
        $('#dataTables-tzs').hide();
        $('#dataTables-tjds').hide();
        $('#dataTables-jsss').hide();
        if (1 == type) { // 图纸
            $('#dataTables-tzs').show();
        } else if (2 == type) { // 文本
            $('#dataTables-wbs').show();
        } else if (3 == type) { // 计算书
            $('#dataTables-jsss').show();
        } else if (5 == type) { // 条件单
            $('#dataTables-tjds').show();
        }
    }
    // 文本、图纸、条件单、计算书弹出框
    $('#wb_tz_tjd_jss_Modal').on('shown.bs.modal', function (e) {
        load_modal_wb();
        load_modal_tjd();
        load_modal_jss();
    });
    // 查询弹出框文本列表
    function query_modal_wb(projElementId) {
        $.ajax({
            url : config.baseurl +"/api/phaseRest/phaseForGzbzxjd_wb",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                projElementId: projElementId
            },
            type:"GET",
            beforeSend:function(){window.parent.showLoading();},
            success:function(data){
                modalWbDataSet = data;
                $('#wb_tz_tjd_jss_Modal').modal("show").css("top", "25%");
            },
            complete:function(){window.parent.closeLoading();},
            error:function(){
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    //查询弹出框文本列表-OA
    function query_modal_wb_oa(taskHeaderId) {
        $.ajax({
            url : config.baseurl +"/api/phaseRest/phaseForOalc_wb",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                taskHeaderId: taskHeaderId
            },
            type:"GET",
            beforeSend:function(){window.parent.showLoading();},
            success:function(data){
                modalOaDataSet = data;
                $('#oaModal').modal("show").css("top", "40%");
            },
            complete:function(){window.parent.closeLoading();},
            error:function(){
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    // 文本
    var t_modal_wb;
    var modalWbDataSet = [];
    function load_modal_wb() {
        $('#dataTables-wbs').html('<table cellpadding="0" cellspacing="0" border="0" class="table table-bordered table-hover" id="dataTables-wb"></table>');
        t_modal_wb = $('#dataTables-wb').DataTable({
            "autoWidth": true,
            "data": modalWbDataSet,
            "paging": false,
            "searching": false,
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "序号", "data": null},
                {"title": "文本名称", "data": "taskName"},
                {"title": "创建人", "data": "createdName"},
                {"title": "章节完成情况", "data": "taskProgress"},
                {"title": "专业检审完成情况", "data": "taskProgress2"},
                {"title": "状态", "data": "taskStatusName"}
            ],
            "columnDefs": [{
                "searchable": false,
                "orderable": false,
                "targets": 0,
                "width": 32
            },{
                "searchable": false,
                "orderable": false,
                "width": 260,
                "targets": 1,
                "render": function (data, type, full, meta) {
                    return "<a href='/pages/management/index.html?doWork=wbrw&taskName=" + data.trim() + "&taskHeaderId=" + full.taskHeaderId + "' target='_blank'>" + data + "</a>";
                }
            },{
                "searchable": false,
                "orderable": false,
                "targets": 2,
                "width": 50
            },{
                "searchable": false,
                "orderable": false,
                "targets": 3,
                "width": 150
            },{
                "searchable": false,
                "orderable": false,
                "targets": 4,
                "width": 180
            },{
                "searchable": false,
                "orderable": false,
                "targets": 5,
                "width": 50,
                "render": function (data, type, full, meta) {
                    return "<a name='a_wb_oa' href='#gzbgs'>" + data + "</a>";
                }
            }
            ],
            "order": [],
            "createdRow": function( row, data, dataIndex ) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
            },
            "initComplete": function () {
                $('#dataTables-wb_length').remove();
            }
        });

        $('#dataTables-wb tbody').on('click', 'a[name=a_wb_oa]', function () {
            var userTable = $('#dataTables-wb').DataTable();
            var data = userTable.rows($(this).parents('tr')).data();
            query_modal_wb_oa(data[0].taskHeaderId);
        });

        $('#dataTables-wb tbody').on('click', 'tr', function () {
            if ( $(this).hasClass('dt-select') ) {
                $(this).removeClass('dt-select');
            }
            else {
                $('#dataTables-wb').DataTable().$('tr.dt-select').removeClass('dt-select');
                $(this).addClass('dt-select');
            }
        });

        t_modal_wb.on( 'order.dt search.dt', function () {
            t_modal_wb.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } );
        } ).draw();

    }

    // 查询弹出框条件单列表
    function query_modal_tjd(projElementId) {
        $.ajax({
            url : config.baseurl +"/api/phaseRest/phaseForGzbzxjd_tjd",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                projElementId: projElementId
            },
            type:"GET",
            beforeSend:function(){window.parent.showLoading();},
            success:function(data){
                modalTjdDataSet = data;
                $('#wb_tz_tjd_jss_Modal').modal("show").css("top", "25%");
            },
            complete:function(){window.parent.closeLoading();},
            error:function(){
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    //查询弹出框条件单-OA
    function query_modal_tjd_oa(conditionId) {
        $.ajax({
            url : config.baseurl +"/api/phaseRest/phaseForOalc_tjd",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                conditionId: conditionId
            },
            type:"GET",
            beforeSend:function(){window.parent.showLoading();},
            success:function(data){
                modalOaDataSet = data;
                $('#oaModal').modal("show").css("top", "25%");
            },
            complete:function(){window.parent.closeLoading();},
            error:function(){
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    // 条件单
    var t_modal_tjd;
    var modalTjdDataSet = [];
    function load_modal_tjd() {
        $('#dataTables-tjds').html('<table cellpadding="0" cellspacing="0" border="0" class="table table-bordered table-hover" id="dataTables-tjd"></table>');
        t_modal_tjd = $('#dataTables-tjd').DataTable({
            "autoWidth": true,
            "data": modalTjdDataSet,
            "paging": false,
            "searching": false,
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "序号", "data": null},
                {"title": "条件单编号", "data": "conditionNumber"},
                {"title": "条件单名称", "data": "conditionName"},
                {"title": "状态", "data": "status"},
                {"title": "提交时间", "data": "submitDate"},
                {"title": "接收时间", "data": "receiveDate"},
                {"title": "接收人", "data": "receivePerson"}
            ],
            "columnDefs": [{
                "searchable": false,
                "orderable": false,
                "targets": 0,
                "width": 32
            },{
                "searchable": false,
                "orderable": false,
                "targets": 1
            },{
                "searchable": false,
                "orderable": false,
                "targets": 2
            },{
                "searchable": false,
                "orderable": false,
                "targets": 3,
                "width": 50,
                "render": function (data, type, full, meta) {
                    return "<a name='a_tjd_oa' href='#gzbgs'>" + data + "</a>";
                }
            },{
                "searchable": false,
                "orderable": false,
                "targets": 4,
                "width": 80
            },{
                "searchable": false,
                "orderable": false,
                "targets": 5,
                "width": 80
            },{
                "searchable": false,
                "orderable": false,
                "targets": 6,
                "width": 60
            }
            ],
            "order": [],
            "createdRow": function( row, data, dataIndex ) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
            },
            "initComplete": function () {
                $('#dataTables-tjd_length').remove();
            }
        });

        $('#dataTables-tjd tbody').on('click', 'a[name=a_tjd_oa]', function () {
            var userTable = $('#dataTables-tjd').DataTable();
            var data = userTable.rows($(this).parents('tr')).data();
            query_modal_tjd_oa(data[0].conditionId);
        });

        $('#dataTables-tjd tbody').on('click', 'tr', function () {
            if ( $(this).hasClass('dt-select') ) {
                $(this).removeClass('dt-select');
            }
            else {
                $('#dataTables-tjd').DataTable().$('tr.dt-select').removeClass('dt-select');
                $(this).addClass('dt-select');
            }
        });

        t_modal_tjd.on( 'order.dt search.dt', function () {
            t_modal_tjd.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } );
        } ).draw();

    }

    // 查询弹出框计算书列表
    function query_modal_jss(projElementId) {
        $.ajax({
            url : config.baseurl +"/api/phaseRest/phaseForGzbzxjd_jss",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                projElementId: projElementId
            },
            type:"GET",
            beforeSend:function(){window.parent.showLoading();},
            success:function(data){
                modalJssDataSet = data;
                $('#wb_tz_tjd_jss_Modal').modal("show").css("top", "25%");
            },
            complete:function(){window.parent.closeLoading();},
            error:function(){
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    // 计算书
    var t_modal_jss;
    var modalJssDataSet = [];
    function load_modal_jss() {
        $('#dataTables-jsss').html('<table cellpadding="0" cellspacing="0" border="0" class="table table-bordered table-hover" id="dataTables-jss"></table>');
        t_modal_jss = $('#dataTables-jss').DataTable({
            "autoWidth": true,
            "data": modalJssDataSet,
            "searching": false,
            "paging": false,
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "序号", "data": null},
                {"title": "计算书编号", "data": "jssbh"},
                {"title": "计算书名称", "data": "jssm"},
                {"title": "状态", "data": "status"}
            ],
            "columnDefs": [{
                "searchable": false,
                "orderable": false,
                "targets": 0,
                "width": 32
            },{
                "searchable": false,
                "orderable": false,
                "width": 160,
                "targets": 1
            },{
                "searchable": false,
                "orderable": false,
                "targets": 2
            },{
                "searchable": false,
                "orderable": false,
                "targets": 3,
                "width": 100,
                "render": function (data, type, full, meta) {
                    return "<a name='a_modal_oa' href='#1'>" + data + "</a>";
                }
            }
            ],
            "order": [],
            "createdRow": function( row, data, dataIndex ) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
            },
            "initComplete": function () {
                $('#dataTables-jss_length').remove();
            }
        });

        $('#dataTables-jss tbody').on('click', 'tr', function () {
            if ( $(this).hasClass('dt-select') ) {
                // $(this).removeClass('dt-select');
            }
            else {
                $('#dataTables-jss').DataTable().$('tr.dt-select').removeClass('dt-select');
                $(this).addClass('dt-select');
            }
        });

        $('#dataTables-jss tbody').on('click', 'a[name=a_modal_oa]', function () {
            var userTable = $('#dataTables-jss').DataTable();
            var data = userTable.rows( $(this).parents('tr') ).data();
            openOA_workFlow(data[0].requestId);
        });

        t_modal_jss.on( 'order.dt search.dt', function () {
            t_modal_jss.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } );
        } ).draw();

    }

});

function queryOAInfor(erpId, erpId2, id, formId, oaTop) {
    $.ajax({
        url : config.baseurl +"/api/naviIndexRest/getDepartmentOA",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            id: id,
            formId: formId,
            erpId: erpId,
            erpId2: erpId2
        },
        type:"GET",
        beforeSend:function(){window.parent.showLoading();},
        success:function(data){
            // console.log(data);
            modalOaDataSet = data;
            console.log(modalOaDataSet);
            $('#oaModal').modal("show").css("top", oaTop + "%");
        },
        complete:function(){window.parent.closeLoading();},
        error:function(){
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

function openOA_workFlow(requestId) {
    var url = oaServer + "workflow/request/ViewRequest.jsp?requestid=" + requestId;
    window.open(url);
}

