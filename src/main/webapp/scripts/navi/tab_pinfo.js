var tab_dataSet1=[];
var tab_dataSet_m=[];
var tab_dataSet_s=[];

var tab_pinfo_t3;
var tab_pinfo_t4;
var tab_pinfo_ts;

var l_userId=1613;
var exeOrgId = "";

function tab_initPinfo() {
    getprojectInfo();
    getprojectperson();
    getprojectmAllhours();
    // getprojectmhours();
    // getprojectshours();
}

function getprojectInfo() {
    $.ajax({     //异步，调用后台get方法
        url: config.baseurl + "/api/Pinfo/info", //调用地址，不包含参数
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
            console.log(config.evn);
            var datas = data.dataSource;
            console.log(datas);
            exeOrgId = datas[0].exeId;
            allTabData[activeTabId].exeOrgId = exeOrgId;
            allShow();
            $('#pinfoProjectNumber_' + activeTabId).html(datas[0].projectNumber);
            $('#pinfoProjectName_' + activeTabId).html(datas[0].projectName);
            $('#pinfoProjectManager_' + activeTabId).html(datas[0].manager);
            $('#pinfoProjectLongName_' + activeTabId).html(datas[0].projectLongName);
            $('#pinfoProjectClass_' + activeTabId).html(datas[0].projectClass);
            $('#pinfoOrgNmae_' + activeTabId).html(datas[0].orgName);
            $('#pinfoExeName_' + activeTabId).html(datas[0].exeName);
            $('#pinfoProjStartDate_' + activeTabId).html(datas[0].projStartDate);
            $('#pinfoProjEndDate_' + activeTabId).html(datas[0].projEndDate);
            $('#pinfoStratDate_' + activeTabId).html(datas[0].startDate);
            $('#pinfoCompletionDate_' + activeTabId).html(datas[0].completionDate);
            $('#pinfoCustomer_' + activeTabId).html(datas[0].customer);
            $('#pinfoprojectStatus_' + activeTabId).html(datas[0].projectStatus);
            $('#pinfoOverdue_' + activeTabId).html(datas[0].overdue);
            setStatusList(datas[0].startDate, datas[0].completionDate, datas[0].sysDate, datas[0].projectStatusCode)
        },
        complete: function () {
        },
        error: function () {
            window.parent.error(ajaxError_sys);
        }
    });
}

function allShow() {
    var name = "tab_l_stage_" + activeTabId;
    var div_plan = "div_plan" + "_" + activeTabId;
    var li = $('#myTab_' + activeTabId + ' li');
    var stageNo = 0;
    $('label[name="' + name + '"]').each(function () {
        if ($(this).hasClass("active"))
            stageNo = $(this).find('input')[0].value;
    });
    console.log(stageNo + "|" + exeOrgId);
    // exeOrgId = 170;
    if (1 == stageNo) {
        if ("147" == exeOrgId || "167" == exeOrgId || "572" == exeOrgId || "652" == exeOrgId || "653" == exeOrgId || "654" == exeOrgId || "122" == exeOrgId) {

            $(li[4]).hide();
            $('#buildingInstitutePro_' + activeTabId).hide();
            $('#buildingInstitutePro_' + activeTabId).css("display", "");
            $('#div_lightWeight_base_' + activeTabId).hide();
            $('#div_lightWeight_phase_' + activeTabId).hide();
            if("dev"==config.evn) {
                $('#' + div_plan + ' a').eq(3).show();  //综合管理报告          //测试环境
                $('#' + div_plan + ' a').eq(7).show();//协作文本策划
                $('#' + div_plan + ' a').eq(9).show();
                 $('#' + div_plan + ' a').eq(10).show();
                $('#' + div_plan + ' a').eq(11).hide();
                $(li[3]).show();
                $('#div_stage_' + activeTabId).hide();
                $('#div_stage_' + activeTabId).css("display", "none");
                $('#proTask_' + activeTabId).show();
                $('#proTask_' + activeTabId).css("display", "");
            }else if("prod"==config.evn) {
                $(li[3]).hide();
                $('#' + div_plan + ' a').eq(3).hide();     ////正式环境
                $('#' + div_plan + ' a').eq(7).hide();
                $('#' + div_plan + ' a').eq(9).hide();
                $('#' + div_plan + ' a').eq(10).hide();
                $('#' + div_plan + ' a').eq(11).hide();
            }

        } else if ("170" == exeOrgId) {
            $('#div_lightWeight_base_' + activeTabId).show();
            $('#div_lightWeight_phase_' + activeTabId).hide();
             $(li[3]).hide();
             $(li[4]).show();
            $('#buildingInstitutePro_' + activeTabId).show();
             $('#buildingInstitutePro_' + activeTabId).css("display", "");
            $('#' + div_plan + ' a').eq(3).hide();
            $('#' + div_plan + ' a').eq(7).hide();
            $('#' + div_plan + ' a').eq(9).hide();
            $('#' + div_plan + ' a').eq(10).hide();
            $('#' + div_plan + ' a').eq(11).hide();
            $('#proTask_' + activeTabId).hide();
            $('#proTask_' + activeTabId).css("display", "");
        } else {
            
            $(li[3]).hide();
            $(li[4]).hide();
            $('#buildingInstitutePro_' + activeTabId).hide();
            $('#buildingInstitutePro_' + activeTabId).css("display", "");
            $('#' + div_plan + ' a').eq(3).hide();
            $('#' + div_plan + ' a').eq(7).hide();
            $('#' + div_plan + ' a').eq(9).hide();
            $('#' + div_plan + ' a').eq(10).hide();
            $('#' + div_plan + ' a').eq(11).hide();
            $('#proTask_' + activeTabId).hide();
            $('#proTask_' + activeTabId).css("display", "");
            $('#div_lightWeight_base_' + activeTabId).hide();
            $('#div_lightWeight_phase_' + activeTabId).hide();
        }
    } else {
        if ("147" == exeOrgId || "167" == exeOrgId || "572" == exeOrgId || "652" == exeOrgId || "653" == exeOrgId || "654" == exeOrgId || "122" == exeOrgId) {
            $('#div_stage_buildingInstitutePro_1_' + activeTabId).hide();
            $('#div_stage_buildingInstitutePro_2_' + activeTabId).hide();
            $('#div_stage_buildingInstitutePro_3_' + activeTabId).hide();
            $('#div_lightWeight_base_' + activeTabId).hide();
            $('#div_lightWeight_phase_' + activeTabId).hide();
            if("dev"==config.evn) {
                $('#div_stage_kgbg_' + activeTabId).show();
                $('#div_stage_zykgbg_' + activeTabId).show();
              //  $('#div_stage_gzb_' + activeTabId).show();
               // $('#div_stage_sjbg' + activeTabId).show();//设计变更
                $('#div_stage_jktjx_' + activeTabId).show();
                $('#div_stage_wbzxjd_' + activeTabId).show();

                $('#' + div_plan + ' a').eq(6).show();
                $('#' + div_plan + ' a').eq(7).show();
                $('#' + div_plan + ' a').eq(9).show();
                $('#' + div_plan + ' a').eq(10).show();
                $('#' + div_plan + ' a').eq(11).show();
            }else if("prod"==config.evn) {
                $('#div_stage_kgbg_' + activeTabId).hide();
                $('#div_stage_zykgbg_' + activeTabId).hide();
                //  $('#div_stage_gzb_' + activeTabId).show();
                // $('#div_stage_sjbg' + activeTabId).show();//设计变更
                $('#div_stage_jktjx_' + activeTabId).hide();
                $('#div_stage_wbzxjd_' + activeTabId).hide();
                $('#' + div_plan + ' a').eq(4).hide();
                $('#' + div_plan + ' a').eq(5).hide();
                $('#' + div_plan + ' a').eq(6).hide();
                $('#' + div_plan + ' a').eq(7).hide();
                $('#' + div_plan + ' a').eq(9).hide();
                $('#' + div_plan + ' a').eq(10).show();
                $('#' + div_plan + ' a').eq(11).show();
            }
        } else if ("170" == exeOrgId) {
            $('#' + div_plan + ' a').eq(6).hide();
            $('#' + div_plan + ' a').eq(7).hide();
            $('#' + div_plan + ' a').eq(9).hide();
            $('#' + div_plan + ' a').eq(10).hide();
            $('#' + div_plan + ' a').eq(11).show();
            $('#div_lightWeight_base_' + activeTabId).hide();
            $('#div_lightWeight_phase_' + activeTabId).show();
            $('#div_stage_buildingInstitutePro_1_' + activeTabId).show();
            $('#div_stage_buildingInstitutePro_2_' + activeTabId).show();
            $('#div_stage_buildingInstitutePro_3_' + activeTabId).show();
            $('#div_stage_kgbg_' + activeTabId).hide();
            $('#div_stage_zykgbg_' + activeTabId).hide();
            $('#div_stage_gzb_' + activeTabId).hide();
            $('#div_stage_sjbg' + activeTabId).hide();
            $('#div_stage_jktjx_' + activeTabId).hide();
            $('#div_stage_wbzxjd_' + activeTabId).hide();
        } else {
            $('#div_stage_buildingInstitutePro_1_' + activeTabId).hide();
            $('#div_stage_buildingInstitutePro_2_' + activeTabId).hide();
            $('#div_stage_buildingInstitutePro_3_' + activeTabId).hide();
            $('#div_lightWeight_base_' + activeTabId).hide();
            $('#div_lightWeight_phase_' + activeTabId).hide();
            $('#div_stage_kgbg_' + activeTabId).hide();
            $('#div_stage_zykgbg_' + activeTabId).hide();
            $('#div_stage_gzb_' + activeTabId).hide();
            $('#div_stage_sjbg' + activeTabId).hide();
            $('#div_stage_jktjx_' + activeTabId).hide();
            $('#div_stage_wbzxjd_' + activeTabId).hide();
            $('#' + div_plan + ' a').eq(6).hide();
            $('#' + div_plan + ' a').eq(7).hide();
            $('#' + div_plan + ' a').eq(9).hide();
            $('#' + div_plan + ' a').eq(10).hide();
            $('#' + div_plan + ' a').eq(11).show();
        }
    }
    var s = getRoles();//侧边栏去掉收款计划
    // if (0 <= s.indexOf("1") || 0 <= s.indexOf("3") || 0 <= s.indexOf("5")) {
    if (0 <= s.indexOf("1") || 0 <= s.indexOf("5")) { //项目经理，特殊权限
        $('#' + div_plan + ' a').eq(1).show();
    }else{
            $('#' + div_plan + ' a').eq(1).hide();
        }

}

function setStatusList(proStartDate, proEndDate, sysDate, status) {
    var v_1_1 = false;
    var v_2_1 = false;
    var v_3_1 = false;
    var v_4_1 = false;
    var v_5_1 = false;
    var v_6_1 = false;
    var v_7_1 = false;
    var v_8_1 = false;
    var v_9_1 = false;
    if (1000 == status) {
        v_3_1 = true;
        v_4_1 = true;
        v_5_1 = true;
        v_7_1 = true;
    } else if (1001 == status || 1003 == status || 1004 == status || 1005 == status) {
        v_1_1 = true;
        v_2_1 = true;
        v_3_1 = true;
        v_4_1 = true;
        v_5_1 = true;
        v_6_1 = true;
        v_7_1 = true;
        v_8_1 = true;
        v_9_1 = true;
    } else if (1002 == status) {
        v_1_1 = true;
        v_2_1 = true;
        v_3_1 = true;
        v_4_1 = true;
        v_5_1 = true;
        v_6_1 = true;
        v_8_1 = true;
        v_9_1 = true;
    } else if (1006 == status) {
        v_1_1 = true;
        v_3_1 = true;
        v_4_1 = true;
        v_6_1 = true;
        v_8_1 = true;
        v_9_1 = true;
    } else if (1007 == status) {

    } else if (1008 == status) {
        v_8_1 = true;
    }

    var v_1_2 = false;
    var v_2_2 = false;
    var v_3_2 = false;
    var v_4_2 = false;
    var v_5_2 = false;
    var v_6_2 = false;
    var v_7_2 = false;
    var v_8_2 = false;
    var v_9_2 = false;
    if (proStartDate > sysDate || sysDate > proEndDate) {
        v_3_2 = true;
        v_4_2 = true;
        v_5_2 = true;
        v_6_2 = true;
        v_7_2 = true;
        v_8_2 = true;
    } else if (proStartDate < sysDate && sysDate < proEndDate) {
        v_1_2 = true;
        v_2_2 = true;
        v_3_2 = true;
        v_4_2 = true;
        v_5_2 = true;
        v_6_2 = true;
        v_7_2 = true;
        v_8_2 = true;
        v_9_2 = true;
    }
    var vS = "是";
    var vF = "否";
    if (v_1_1 && v_1_2)
        $('#pinfoprojectStatus_1_' + activeTabId).html(vS);
    else
        $('#pinfoprojectStatus_1_' + activeTabId).html(vF);
    if (v_2_1 && v_2_2)
        $('#pinfoprojectStatus_2_' + activeTabId).html(vS);
    else
        $('#pinfoprojectStatus_2_' + activeTabId).html(vF);
    if (v_3_1 && v_3_2)
        $('#pinfoprojectStatus_3_' + activeTabId).html(vS);
    else
        $('#pinfoprojectStatus_3_' + activeTabId).html(vF);
    if (v_4_1 && v_4_2)
        $('#pinfoprojectStatus_4_' + activeTabId).html(vS);
    else
        $('#pinfoprojectStatus_4_' + activeTabId).html(vF);
    if (v_5_1 && v_5_2)
        $('#pinfoprojectStatus_5_' + activeTabId).html(vS);
    else
        $('#pinfoprojectStatus_5_' + activeTabId).html(vF);
    if (v_6_1 && v_6_2)
        $('#pinfoprojectStatus_6_' + activeTabId).html(vS);
    else
        $('#pinfoprojectStatus_6_' + activeTabId).html(vF);
    if (v_7_1 && v_7_2)
        $('#pinfoprojectStatus_7_' + activeTabId).html(vS);
    else
        $('#pinfoprojectStatus_7_' + activeTabId).html(vF);
    if (v_8_1 && v_8_2)
        $('#pinfoprojectStatus_8_' + activeTabId).html(vS);
    else
        $('#pinfoprojectStatus_8_' + activeTabId).html(vF);
    if (v_9_1 && v_9_2)
        $('#pinfoprojectStatus_9_' + activeTabId).html(vS);
    else
        $('#pinfoprojectStatus_9_' + activeTabId).html(vF);
}

function getprojectperson() {
    $.ajax({     //异步，调用后台get方法
        url: config.baseurl + "/api/Pinfo/personInfo", //调用地址，不包含参数
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
            tab_dataSet1=data.dataSource;
            allTabData[activeTabId].tab_dataSet1 = tab_dataSet1;
            console.log(tab_dataSet1);
            createpersonInfo();
            // 2017-01-19，需要显示出项目成员人数；增加显示总的项目人员个数（如果一个人任多个角色，则计数为1）
            var tmp_dataSet = [];
            var tmpCount = 0;
            for (var i = 0; i < tab_dataSet1.length; i++) {
                if (-1 == tmp_dataSet.indexOf(tab_dataSet1[i].fullName)) {
                    tmp_dataSet.push(tab_dataSet1[i].fullName);
                    tmpCount++;
                }
            }
            $('#dataTables-projperson_count_' + activeTabId).append('（项目总人数：' + tmpCount + "）");
        },
        complete: function () {
        },
        error: function () {
            window.parent.error(ajaxError_sys);
        }
    });
}

//创建人员列表
function createpersonInfo() {
    $('#dataTables-projperson_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-proj-line_' + activeTabId + '"></table>');
    $('#div_projpersonSearch_' + activeTabId).empty();
    var s = '<div class="row form-inline form-group" style="margin-bottom: 0px;">'
       + '<input data-column="1" class="form-control input-sm column_filter_' + activeTabId + '" style="margin-left: 55px; width: 58px;" type="text" placeholder="">'
       + '<input data-column="2" class="form-control input-sm column_filter_' + activeTabId + '" style="margin-left: 2px; width: 94px;" type="text" placeholder="">'
       + '<input data-column="3" class="form-control input-sm column_filter_' + activeTabId + '" style="margin-left: 2px; width: 109px;" type="text" placeholder="">'
       + '<input data-column="4" class="form-control input-sm column_filter_' + activeTabId + '" style="margin-left: 2px; width: 116px;" type="text" placeholder="">'
       + '<input data-column="5" class="form-control input-sm column_filter_' + activeTabId + '" style="margin-left: 2px; width: 98px;" type="text" placeholder="">'
       + '<input data-column="6" class="form-control input-sm column_filter_' + activeTabId + '" style="margin-left: 2px; width: 205px;" type="text" placeholder="">'
       + '</div>';
    $('#div_projpersonSearch_' + activeTabId).append(s);

    tab_pinfo_t3 = $('#dataTables-proj-line_' + activeTabId).DataTable({
        "processing": true,
        "data": tab_dataSet1,
        //  "scrollX": true,
        "searching": true,
        "autoWidth": false,
        "pagingType": "full", //简单模式，只有上下页
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "人员", "data": "fullName"},
            {"title": "角色", "data": "assignment_name"},
            {"title": "专业", "data": "specaility_name"},
            {"title": "办公电话", "data": "work_phone"},
            {"title": "移动电话", "data": "m_phone"},
            {"title": "电子邮箱", "data": "email_address"}
        ],
        "columnDefs": [{
            "targets": 1, //人员
            "width": 50

        }, {
            "searchable": false,
            "orderable": false,
            "width": 32,
            "targets": 0 //排序

        }, {
            "width": 100,
            "targets": 2//专业
        }, {
            "width": 115,
            "targets": 3//角色
        }, {
            "width": 115,
            "targets": 4 //办公电话
        }, {
            "width": 90,
            "targets": 5 //移动电话
        }, {
            "width": 200,
            "targets": 6//电子邮箱
        }
        ],
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "order": [],
        "initComplete": function () {
            $('#dataTables-proj-line_' + activeTabId + '_length').insertBefore($('#dataTables-proj-line_' + activeTabId + '_info'));
            $('#dataTables-proj-line_' + activeTabId + '_length').addClass("col-sm-4");
            $('#dataTables-proj-line_' + activeTabId + '_length').css("paddingLeft", "0px");
            $('#dataTables-proj-line_' + activeTabId + '_length').css("paddingTop", "5px");
            $('#dataTables-proj-line_' + activeTabId + '_length').css("maxWidth", "130px");
            $('#dataTables-proj-line_' + activeTabId + '_filter').hide();
            // $('#dataTables-proj-line_' + activeTabId + '_wrapper').css("cssText", "margin-top: -5px;");
        }
    });

    $('input.column_filter_' + activeTabId).on('keyup', function () {
        filterColumn($(this).attr('data-column'), $(this).val());
    });

    tab_pinfo_t3.on('order.dt search.dt', function () {
        tab_pinfo_t3.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    }).draw();
    allTabData[activeTabId].tab_pinfo_t3 = tab_pinfo_t3;
}

function filterColumn (i, value) {
    $('#dataTables-proj-line_' + activeTabId).DataTable().column( i ).search(
        value,
        true,
        true
    ).draw();
}

function getprojectmhours() {
    $.ajax({     //异步，调用后台get方法
        url: config.baseurl + "/api/Pinfo/Mhours", //调用地址，不包含参数
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: {
            projectId: tab_l_projectId,  //传入参数，依次填写
            userId:l_userId,
            ismanager:getIsManager()
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {

        },
        success: function (data) {
            tab_dataSet_m=data.dataSource;
            allTabData[activeTabId].tab_dataSet_m = tab_dataSet_m;
            $('#pinfototal_' + activeTabId).html(data.total);

            createmhours();
        },
        complete: function () {
        },
        error: function () {
            window.parent.error(ajaxError_sys);
        }
    });
}

function getprojectmAllhours() {
    $.ajax({     //异步，调用后台get方法
        url: config.baseurl + "/api/Pinfo/Whours", //调用地址，不包含参数
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: {
            projectId: tab_l_projectId,  //传入参数，依次填写
            userId:l_userId,
            ismanager:getIsManager()
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {

        },
        success: function (data) {
            tab_dataSet_m=data.dataSource;
            allTabData[activeTabId].tab_dataSet_m = tab_dataSet_m;
            // $('#pinfototal_' + activeTabId).html(data.total);
            createmhoursAll();
        },
        complete: function () {
        },
        error: function () {
            window.parent.error(ajaxError_sys);
        }
    });
}

//创建工时汇总
function createmhoursAll() {
    $('#dataTables-projmhoursAll_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-hoursAll-line_' + activeTabId + '"></table>');
    tab_pinfo_t4 = $('#dataTables-hoursAll-line_' + activeTabId).DataTable({
        "processing": true,
        "autoWidth": false,
        "data": tab_dataSet_m,
        "pagingType": "full", //简单模式，只有上下页
        "searching": false,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "人员", "data": "fullName"},
            {"title": "项目管理工时", "data": "manageHours"},
            {"title": "设计工时", "data": "designHours"},
            {"title": "施工服务工时", "data": "serverHours"}

        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "width": 32,
            "targets": 0 //排序
        }
        ],
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "order": [],
        "initComplete": function () {
            $('#dataTables-hoursAll-line_' + activeTabId + '_length').insertBefore($('#dataTables-hoursAll-line_' + activeTabId + '_info'));
            $('#dataTables-hoursAll-line_' + activeTabId + '_length').addClass("col-sm-4");
            $('#dataTables-hoursAll-line_' + activeTabId + '_length').css("paddingLeft", "0px");
            $('#dataTables-hoursAll-line_' + activeTabId + '_length').css("paddingTop", "5px");
            $('#dataTables-hoursAll-line_' + activeTabId + '_length').css("maxWidth", "130px");
            $('#dataTables-hoursAll-line_' + activeTabId).css("cssText", "margin-top: 0px !important;");
        }
        // "initComplete": function () { //去掉标准表格多余部分
        //     $('#dataTables-mhours-line_length').remove();
        //     $('#dataTables-mhours-line_info').remove();
        //     $('#dataTables-mhours-line_previous').remove();
        //     $('#dataTables-mhours-line_next').remove();
        //
        // }
    });

    tab_pinfo_t4.on('order.dt search.dt', function () {
        tab_pinfo_t4.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    }).draw();

    allTabData[activeTabId].tab_pinfo_t4 = tab_pinfo_t4;
}

//创建工时列表
function createmhours() {
    $('#dataTables-projmhours_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-mhours-line_' + activeTabId + '"></table>');
    tab_pinfo_t4 = $('#dataTables-mhours-line_' + activeTabId).DataTable({
        "processing": true,
        "autoWidth": false,
        "data": tab_dataSet_m,
        "pagingType": "full", //简单模式，只有上下页
        "searching": false,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "人员", "data": "fullName"},
            {"title": "工时数", "data": "measure"}

        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "width": 32,
            "targets": 0 //排序
        }
        ],
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "order": [],
        "initComplete": function () {
            $('#dataTables-mhours-line_' + activeTabId + '_length').insertBefore($('#dataTables-mhours-line_' + activeTabId + '_info'));
            $('#dataTables-mhours-line_' + activeTabId + '_length').addClass("col-sm-4");
            $('#dataTables-mhours-line_' + activeTabId + '_length').css("paddingLeft", "0px");
            $('#dataTables-mhours-line_' + activeTabId + '_length').css("paddingTop", "5px");
            $('#dataTables-mhours-line_' + activeTabId + '_length').css("maxWidth", "130px");
            $('#dataTables-mhours-line_' + activeTabId).css("cssText", "margin-top: 0px !important;");
        }
        // "initComplete": function () { //去掉标准表格多余部分
        //     $('#dataTables-mhours-line_length').remove();
        //     $('#dataTables-mhours-line_info').remove();
        //     $('#dataTables-mhours-line_previous').remove();
        //     $('#dataTables-mhours-line_next').remove();
        //
        // }
    });

    tab_pinfo_t4.on('order.dt search.dt', function () {
        tab_pinfo_t4.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    }).draw();

    allTabData[activeTabId].tab_pinfo_t4 = tab_pinfo_t4;
}

//项目施工服务工时
function getprojectshours() {
    $.ajax({     //异步，调用后台get方法
        url: config.baseurl + "/api/Pinfo/Shours", //调用地址，不包含参数
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: {
            projectId: tab_l_projectId,  //传入参数，依次填写
            userId:l_userId,
            ismanager:getIsManager()
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {

        },
        success: function (data) {
            tab_dataSet_s=data.dataSource;
            allTabData[activeTabId].tab_dataSet_s = tab_dataSet_s;
            createshours();
        },
        complete: function () {
        },
        error: function () {
            window.parent.error(ajaxError_sys);
        }
    });
}

//创建工时列表
function createshours() {
    $('#dataTables-projshours_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-shours-line_' + activeTabId + '"></table>');
    tab_pinfo_ts = $('#dataTables-shours-line_' + activeTabId).DataTable({
        "processing": true,
        "data": tab_dataSet_s,
        "pagingType": "full", //简单模式，只有上下页
        "searching": false,
        "autoWidth": false,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "人员", "data": "fullName"},
            {"title": "工时数", "data": "measure"}

        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "width": 32,
            "targets": 0 //排序
        }
        ],
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "order": [],
        "initComplete": function () {
            $('#dataTables-shours-line_' + activeTabId + '_length').insertBefore($('#dataTables-shours-line_' + activeTabId + '_info'));
            $('#dataTables-shours-line_' + activeTabId + '_length').addClass("col-sm-4");
            $('#dataTables-shours-line_' + activeTabId + '_length').css("paddingLeft", "0px");
            $('#dataTables-shours-line_' + activeTabId + '_length').css("paddingTop", "5px");
            $('#dataTables-shours-line_' + activeTabId + '_length').css("maxWidth", "130px");
            $('#dataTables-shours-line_' + activeTabId + '_wrapper').css("cssText", "margin-top: -5px;");
        }
    });

    tab_pinfo_ts.on('order.dt search.dt', function () {
        tab_pinfo_ts.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    }).draw();

    allTabData[activeTabId].tab_pinfo_ts = tab_pinfo_ts;
}