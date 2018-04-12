var tab_conap_headerId;
var tab_conap_kheaderId;
var tab_conap_version;
var tab_conap_version_max;
var tab_linestyle = [];//费用项下拉列表数据源
var tab_con_line_Num = [];//合同行号下拉列表数据源
var linestylefun;
var linenumfun;
var tab_apMilestone = [];//初始里程碑下拉
var tab_apMilestone_new=[];
var apMilestonefun;
var tab_contractAp_t2;//datatable
var tab_conap_checktype;//调整，查询类别区分
var tab_dataSetAp = [];
var tab_rcvType = [];//款项性质数据源
var rcvTypefun;//款项性质生成方法
var tab_line_num;//排序
var tab_ap_contotal; //合同总额
var isonly;
var tab_t_cash;
var tab_data_Set_c = [];
var tab_t_trx;
var tab_data_Set_t = [];


function tab_initContractAp() {
    console.log("初始化收款计划");
    $('#bu_apadj_' + activeTabId).attr('disabled', true);
    $('#bu_apdel_v_' + activeTabId).attr('disabled', true);
    $('#bu_apadd_' + activeTabId).attr('disabled', true);
    $('#bu_apdel_' + activeTabId).attr('disabled', true);
    $('#bu_apmoveup_' + activeTabId).attr('disabled', true);
    $('#bu_apmovedown_' + activeTabId).attr('disabled', true);
    $('#bu_apsave_' + activeTabId).attr('disabled', true);
    $('#bu_apsubmit_' + activeTabId).attr('disabled', true);
    // if(config.evn=='prod') {
    //     $('#bu_billcreate_' + activeTabId).attr('disabled', true);
    // }
    //生成款项性质下拉表数据源
    $.getJSON(config.baseurl + "/api/contract/rcvtype", function (data) {
        tab_rcvType = data.dataSource;
        allTabData[activeTabId].tab_rcvType = tab_rcvType
    });
    rcvTypefun = function (val) {
        var select = $("<select name='conap_rcvtype_" + activeTabId + "' class='form-control input-sm' placeholder='请选择'></select>");
        select.append("<option value='-1'>请选择</option>");
        for (var i = 0; i < tab_rcvType.length; i++) {
            select.append("<option value='" + tab_rcvType[i].typeCode + "'>" + tab_rcvType[i].typeName + "</option>")
        }
        select.val(val);
        return select;
    }
    $.ajax({     //异步，调用后台get方法
        url: config.baseurl + "/api/contract/checkContract", //调用地址，不包含参数
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
            if (data == []) {
                window.parent.error("该项目没有关联主合同！");
                tab_conap_checktype = 0;
                allTabData[activeTabId].tab_conap_checktype = tab_conap_checktype;
                return false;
            } else {
                getcontractNum();
            }
        },
        complete: function () {
        },
        error: function () {
            window.parent.error(ajaxError_sys);
        }
    });

    createContractApQueryConapPlanVersion();
    createContractApApadj();
    createContractApApadd();
    createContractApApdel();
    deleteConVersion();
    createApmoveup();
    createApmovedown();
    createContractApApsave();
    createContractApApSubmit();
    creatbill();
}

//获取合同编码
function getcontractNum() {
    $.ajax({     //异步，调用后台get方法
        url: config.baseurl + "/api/contract/number", //调用地址，不包含参数
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
            var l_num = 1;
            list.forEach(function (data) {  //创建合同按钮组
                if (l_num == 1) {
                    $('#button1_' + activeTabId).append("<label style='padding: 3px 10px;' class='btn btn-default btn-primary2 active' name='kheadername_" + activeTabId + "' > <input type='radio' name='button_con_" + activeTabId + "'  value='" + data.kHeaderId + "' >" + data.cognomen + "</label>");
                    tab_conap_kheaderId = data.kHeaderId;
                    allTabData[activeTabId].tab_conap_kheaderId = tab_conap_kheaderId;
                    l_num = 2;
                } else {
                    $('#button1_' + activeTabId).append("<label style='padding: 3px 10px;' class='btn btn-default btn-primary2' name='kheadername_" + activeTabId + "' > <input type='radio' name='button_con_" + activeTabId + "'  value='" + data.kHeaderId + "' >" + data.cognomen + "</label>");
                }
            });

            getConapVersion(1);//初始化加载版本
            getConaplinestyle();//费用项下拉框
            getConaplineNum();//合同行号下拉框
            getApMilestone();
            getapcash();
            getaptrx();
            //获取选中的合同ID，当点击合同编号的时候，触发生成版本，及费用项下拉框
            $('label[name="kheadername_' + activeTabId + '"]').on('click', function () {
                var _index = $('label[name="kheadername_' + activeTabId + '"]').index($(this));
                tab_conap_kheaderId = $("input[name='button_con_" + activeTabId + "']")[_index].value;
                allTabData[activeTabId].tab_conap_kheaderId = tab_conap_kheaderId;
                //清除数据
                tab_dataSetAp = [];
                allTabData[activeTabId].tab_dataSetAp = [];
                createtable();

                $('#bu_apadj_' + activeTabId).attr('disabled', true);
                $('#bu_apdel_v_' + activeTabId).attr('disabled', true);
                $('#bu_apadd_' + activeTabId).attr('disabled', true);
                $('#bu_apdel_' + activeTabId).attr('disabled', true);
                $('#bu_apmoveup_' + activeTabId).attr('disabled', true);
                $('#bu_apmovedown_' + activeTabId).attr('disabled', true);
                $('#bu_apsave_' + activeTabId).attr('disabled', true);
                $('#bu_apsubmit_' + activeTabId).attr('disabled', true);

                getConapVersion(1);//生成版本下拉框
                getConaplinestyle();//费用项下拉框
                getConaplineNum();//合同行号下拉框
                getApMilestone();//里程碑下拉框
                getapcash();
                getaptrx();
            });
            createtable();
        },
        complete: function () {
        },
        error: function () {
            window.parent.error(ajaxError_sys);
        }
    });

}

//生成版本下拉列表
function getConapVersion(auto) {
    $('#s_conapPlan_version_' + activeTabId).empty();//清空下拉表
    $.ajax({     //异步，调用后台get方法
        url: config.baseurl + "/api/contract/version", //调用地址，不包含参数
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: {
            kheaderId: tab_conap_kheaderId,  //传入参数，依次填写
            projectId: tab_l_projectId
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {
        },
        success: function (data) {
            var list1 = data.dataSource;
            bu_apadjFlag = false;
            if ("S" == data.message)
                bu_apadjFlag = true;
            else if("F1" == data.message)
                window.parent.warring("项目经理时间过期,请联系项目管理部更改时间！")
            else if("F2" == data.message)
                window.parent.warring("该合同关联多个项目,请联系市场部指定项目经理！")
            list1.forEach(function (data) {  //取最大版本
                tab_conap_version = data.version;
                tab_conap_version_max = data.version;
                allTabData[activeTabId].tab_conap_version = tab_conap_version;
                allTabData[activeTabId].tab_conap_version_max = tab_conap_version_max;
            });

            list1.forEach(function (data) {  //下拉列表，如果是最大版本，则默认选中
                if (data.version == tab_conap_version_max) {
                    $('#s_conapPlan_version_' + activeTabId).append("<option data-statusVersion='" + data.statusVersion + "' value='" + data.version + "'selected>" + data.version + "</option>");

                } else {
                    $('#s_conapPlan_version_' + activeTabId).append("<option data-statusVersion='" + data.statusVersion + "' value='" + data.version + "'>" + data.version + "</option>");
                }

            });
            if (1 == auto)
                $('#bu_queryConapPlanVersion_' + activeTabId).trigger("click");
        },
        complete: function () {
        },
        error: function () {
            window.parent.error(ajaxError_sys);
        }
    });
}

//生成费用项下拉列表数据源
function getConaplinestyle() {
    $.ajax({     //异步，调用后台get方法
        url: config.baseurl + "/api/contract/linestyle", //调用地址，不包含参数
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: {
            kheaderId: tab_conap_kheaderId  //传入参数，依次填写
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {
        },
        success: function (data) {
            tab_linestyle = data.dataSource;//数据源赋值
            allTabData[activeTabId].tab_linestyle = tab_linestyle;
            linestylefun = function (val) {
                var select = $("<select name='conap_linestyle_" + activeTabId + "' class='form-control input-sm' placeholder='请选择'></select>");
                select.append("<option value='-1'>请选择</option>");
                for (var i = 0; i < tab_linestyle.length; i++) {
                    select.append("<option value='" + tab_linestyle[i].lseId + "'>" + tab_linestyle[i].name + "</option>")
                }
                select.val(val);
                if (tab_linestyle && 1 == tab_linestyle.length) {
                    select.find("option:eq(1)").attr("selected", true);
                }
                return select;
            }
        },
        complete: function () {
        },
        error: function () {
            window.parent.error(ajaxError_sys);
        }
    });
}
var isonly;
//生成合同行号下拉列表
function getConaplineNum() {
    $.ajax({     //异步，调用后台get方法
        url: config.baseurl + "/api/contract/lineNum", //调用地址，不包含参数
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: {
            kheaderId: tab_conap_kheaderId  //传入参数，依次填写
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {
        },
        success: function (data) {
            tab_con_line_Num = data.dataSource;//数据源赋值
            allTabData[activeTabId].tab_con_line_Num = tab_con_line_Num;
            linenumfun = function (val) {
                var select = $("<select name='conap_linenum_" + activeTabId + "'  class='form-control input-sm' placeholder='请选择' onchange='javascript:setLineInfo(this)'></select>");
                select.append("<option value=''>请选择</option>");
                for (var i = 0; i < tab_con_line_Num.length; i++) {
                    select.append("<option value='" + tab_con_line_Num[i].lineNumber + "'>" + tab_con_line_Num[i].key + "</option>")
                    allTabData[activeTabId].linenumber1= tab_con_line_Num[i].key.split(',')[0];
                    allTabData[activeTabId].linestyle1= tab_con_line_Num[i].key.split(',')[1];
                    allTabData[activeTabId].linevalue1= tab_con_line_Num[i].key.split(',')[2];
                }
                console.log("i:="+i);
                select.val(val);
                if (i == 1) {
                    allTabData[activeTabId].isonly = "Y";
                }
                // if (tab_con_line_Num && 1 == tab_con_line_Num.length) {
                //     select.find("option:eq(1)").attr("selected",true);
                // }
                console.log(select);
                return select;

            }
        },
        complete: function () {
        },
        error: function () {
            window.parent.error(ajaxError_sys);
        }
    });
}

//生成里程碑下拉列表数据源，调整计划初始使用，不用行参数
function getApMilestone() {
    $.ajax({     //异步，调用后台get方法
        url: config.baseurl + "/api/contract/milestone", //调用地址，不包含参数
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: {
            kHeaderId: tab_conap_kheaderId  //传入参数，依次填写
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {
        },
        success: function (data) {
            tab_apMilestone = data.dataSource;//数据源赋值
            allTabData[activeTabId].tab_apMilestone = tab_apMilestone;
            // apMilestonefun = function (val) {
            //     var select = $("<select name='conap_milestone_" + activeTabId + "' class='form-control input-sm' placeholder='请选择' onchange='javascript:setstatus(this)'></select>");
            //     select.append("<option value=''>请选择</option>");
            //     for (var i = 0; i < tab_apMilestone.length; i++) {
            //         select.append("<option value='" + tab_apMilestone[i].lineId + "'>" + tab_apMilestone[i].name + "</option>")
            //     }
            //     select.val(val);
            //     return select;
            // }
        },
        complete: function () {
        },
        error: function () {
            window.parent.error(ajaxError_sys);
        }
    });
}
//生成调整计划后里程初始下拉列表
function apMilestonefun(val,lineNum) {
    var select = $("<select name='conap_milestone_" + activeTabId + "' style='width:100%;' class='form-control input-sm' placeholder='请选择' onchange='javascript:setstatus(this)'></select>");
        select.append("<option value=''>请选择</option>");
        for (var i = 0; i < allTabData[activeTabId].tab_apMilestone.length; i++) {
            if(allTabData[activeTabId].tab_apMilestone[i].conlineNum==lineNum)
            select.append("<option value='" + allTabData[activeTabId].tab_apMilestone[i].lineId + "'>" + allTabData[activeTabId].tab_apMilestone[i].name + "</option>")
        }
        select.val(val);
        return select;
}
//加载数据，返回头信息，控制按钮
function getconapHead() {
    var tmp = $('#s_conapPlan_version_' + activeTabId).find("option:selected")[0];
    var statusVersion = $(tmp).attr("data-statusVersion");
    $.ajax({
        url: config.baseurl + "/api/contract/head",
        data: {
            kheaderId: tab_conap_kheaderId,
            version: tab_conap_version,
            statusVersion: statusVersion
        },
        contentType: 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function () {
        },
        success: function (data) {
            var datas = data.dataSource;
            tab_conap_headerId = datas[0].headerId;
            allTabData[activeTabId].tab_conap_headerId = tab_conap_headerId;
            console.log(datas);

            $('#conId_' + activeTabId).val(datas[0].contractNumber);
            $('#contotal_' + activeTabId).val(fmoney(datas[0].total, 2));
            $('#contotalCode_' + activeTabId).val(datas[0].currencyCode);
            tab_ap_contotal = datas[0].total;
            allTabData[activeTabId].tab_ap_contotal = tab_ap_contotal;
            allTabData[activeTabId].tab_ap_cashTotal = datas[0].total;

            $('#conStatus_' + activeTabId).val(datas[0].status);
            $('#conflag_' + activeTabId).val(datas[0].amount_flag);
            $('#congetamount_' + activeTabId).val(fmoney(datas[0].getAmount, 2));
            $('#congetpercent_' + activeTabId).val(datas[0].getPercent + "%");
            if (tab_conap_checktype == 1) {
                if (tab_conap_version == tab_conap_version_max && $('#conStatus_' + activeTabId)[0].value != '审批中') { //如果查询的是最大版本，且状态不是审批中，且是查询时则按钮亮
                    if ($('#conStatus_' + activeTabId)[0].value == '批准') {//批准状态不能删除
                        $('#bu_apdel_v_' + activeTabId).attr('disabled', true);
                    } else {
                        $('#bu_apdel_v_' + activeTabId).attr('disabled', false);
                    }
                    $('#bu_apadj_' + activeTabId).attr('disabled', false);
                    $('#bu_apdel_v_' + activeTabId).attr('disabled', false);
                    $('#bu_apadd_' + activeTabId).attr('disabled', true);
                    $('#bu_apdel_' + activeTabId).attr('disabled', true);
                    $('#bu_apmoveup_' + activeTabId).attr('disabled', true);
                    $('#bu_apmovedown_' + activeTabId).attr('disabled', true);
                    $('#bu_apsave_' + activeTabId).attr('disabled', true);
                    $('#bu_apsubmit_' + activeTabId).attr('disabled', true);
                    // 2017-03-14 只有切换到项目经理，才允许调整
                    if (0 > getRoles().indexOf("1")) {
                        $('#bu_apadj_' + activeTabId).attr('disabled', true);
                        $('#bu_apdel_v_' + activeTabId).attr('disabled', true);

                    } else {
                        if (bu_apadjFlag) {
                            $('#bu_apadj_' + activeTabId).attr('disabled', false);
                            $('#bu_apdel_v_' + activeTabId).attr('disabled', false);
                        }
                        else {
                            $('#bu_apadj_' + activeTabId).attr('disabled', true);
                            $('#bu_apdel_v_' + activeTabId).attr('disabled', true);
                        }
                    }
                } else {
                    $('#bu_apadj_' + activeTabId).attr('disabled', true);
                    $('#bu_apdel_v_' + activeTabId).attr('disabled', true);
                    $('#bu_apadd_' + activeTabId).attr('disabled', true);
                    $('#bu_apdel_' + activeTabId).attr('disabled', true);
                    $('#bu_apmoveup_' + activeTabId).attr('disabled', true);
                    $('#bu_apmovedown_' + activeTabId).attr('disabled', true);
                    $('#bu_apsave_' + activeTabId).attr('disabled', true);
                    $('#bu_apsubmit_' + activeTabId).attr('disabled', true);
                }
            } else if (tab_conap_checktype == 2) {
                $('#bu_apadj_' + activeTabId).attr('disabled', true);
                $('#bu_apdel_v_' + activeTabId).attr('disabled', true);
                $('#bu_apadd_' + activeTabId).attr('disabled', false);
                $('#bu_apdel_' + activeTabId).attr('disabled', false);
                $('#bu_apmoveup_' + activeTabId).attr('disabled', false);
                $('#bu_apmovedown_' + activeTabId).attr('disabled', false);
                $('#bu_apsave_' + activeTabId).attr('disabled', false);
                $('#bu_apsubmit_' + activeTabId).attr('disabled', false);
            }
            queryconline();
        },
        complete: function () {
        },
        error: function () {
            window.parent.error("重新加载数据出错1111！");
        }
    });
}

//加载数据，返回行信息
function queryconline() {
    var tmp = $('#s_conapPlan_version_' + activeTabId).find("option:selected")[0];
    var statusVersion = $(tmp).attr("data-statusVersion");
    $.ajax({
        url: config.baseurl + "/api/contract/line",
        contentType: 'application/x-www-form-urlencoded',
        dataType: "json",   //返回格式为json
        async: true,//请求是否异步，默认为异步，这也是ajax重要特性
        data: {
            headerId: tab_conap_headerId,
            projectId: tab_l_projectId,
            statusVersion: statusVersion
        },
        type: "GET",   //请求方式
        beforeSend: function () {
            window.parent.showLoading();
        },
        success: function (data) {

            tab_dataSetAp = data.dataSource;
            allTabData[activeTabId].tab_dataSetAp = tab_dataSetAp;
            //console.log(parseFloat(30934378.000000004));
            console.log(tab_dataSetAp);
            tab_dataSetAp.forEach(function (data) {
                data.planapAmount = fmoney(data.planapAmount, 2);
                if (data.lineNumber != 1000) {
                    tab_line_num = data.lineNumber;
                    allTabData[activeTabId].tab_line_num = tab_line_num;
                }
            });

            createtable();

        },
        complete: function () {
            window.parent.closeLoading();
        },
        error: function () {
            window.parent.showLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

function fmoney(s, n) //s:传入的float数字 ，n:希望返回小数点几位
{
    n = n > 0 && n <= 20 ? n : 2;
    s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
    var l = s.split(".")[0].split("").reverse(),
        r = s.split(".")[1];
    t = "";
    for (i = 0; i < l.length; i++) {
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
    }
    return t.split("").reverse().join("") + "." + r;
}

function rmoney(s) {
    if ("" == s)
        return "";
    return parseFloat(s.replace(/[^\d\.-]/g, ""));
}

function setCheckAll() {
    var isChecked = $('#ap_listAll_' + activeTabId).prop("checked");
    $("input[name='ap_list_" + activeTabId + "']").prop("checked", isChecked);
}

function createtable() {
    var datecol;
    datecol = "<span class='input-group-addon' style='padding: 3px 8px; font-size: 13px;'><span class='glyphicon glyphicon-calendar'></span></span>"
    $('#dataTables-conLine_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table table-bordered table-hover" id="dataTables-ap-line_' + activeTabId + '"></table>');
    tab_contractAp_t2 = $('#dataTables-ap-line_' + activeTabId).DataTable({
        "processing": true,
        "autoWidth": true,
        "data": tab_dataSetAp,
        // "scrollY": "500px",
        "scrollX": true,
        "scrollCollapse": true,
        "paging": false,
        "searching": false,//去掉搜索
        "ordering": false,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {
                "title": "<input type='checkbox' name='ap_listAll_" + activeTabId + "' id='ap_listAll_" + activeTabId + "' onclick='javascript:setCheckAll();' />",
                "data": null,
                "className": "dt_center"
            },
            {"title": "序号", "data": "lineNumber", "className": "dt_center"},
            {"title": "交付<br>节点编号", "data": "attribute2", "className": "dt_center"},
            {"title": "合同行", "data": "lineInfo", "className": "dt_center"},
            {"title": "费用项", "data": "lineStyle", "className": "dt_center"},
            {"title": "合同行金额", "data": "lineAmount", "className": "dt_center"},
            {"title": "交付内容", "data": "styleDesc", "className": "dt_center"},
            {"title": "款项性质", "data": "rcvTypeMeaning", "className": "dt_center"},
            {"title": "预计收款日期", "data": "planapDate", "className": "dt_center"},
            {"title": "应收百<br>分比(%)", "data": "apPlan", "className": "dt_center"},
            {"title": "应收金额", "data": "planapAmount", "className": "dt_center"},
            {"title": "是否是收入<br>确认节点", "data": "milestoneFlag", "className": "dt_center"},
            {"title": "关联里程碑", "data": "name", "className": "dt_center"},
            {"title": "里程碑<br>完成情况", "data": "milestoneStatus", "className": "dt_center"},
            {"title": "attribute3", "data": "attribute3"}
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "targets": 1,
            "width": 28,
            "mRender": function (data, type, full) {
                if (data == 1000) {
                    return "汇总";
                } else {
                    return data;
                }
            }
        }, {
            "searchable": false,
            "width": 15,
            "targets": 0//check框
            // "mRender": function (data, type, full) {
            //    if (full.lineNumber!=1000)
            //         return "<input type='checkbox' name='ap_list' id='ap_list'  />";
            //}
        }, {
            "searchable": false,
            "width": 70,
            "targets": 2
        }, {
            "searchable": false,
            "width": 130,
            "targets": 3//合同行号
        }, {
            "searchable": false,
            "width": 130,
            "targets": 4//费用项
        }, {
            "searchable": false,
            "width": 130,
            "targets": 5//行金额
        }, {
            "width": 130,
            "targets": 6//交付内容
        }, {
            "width": 100,
            "targets": 7 //款项性质，下拉列表 rcvType
        }, {
            "width": 125,
            "targets": 8 //接收时间
        }, {
            "width": 80,
            "targets": 9
        }, {
            "width": 90,
            "targets": 10
        }, {
            "width": 80,
            "targets": 11,//是否是收入确认节点
            "mRender": function (data, type, full) {
                if (data == 'Y') {
                    return "<input type='checkbox' name='ap_checked_" + activeTabId + "' id='ap_checked_" + activeTabId + "' checked='checked' disabled='disabled'/>";
                } else {
                    return null;
                }
            }
        }, {
            "width": 120,
            "targets": 12
        }, {
            "width": 60,
            "targets": 13
        },
            {"visible": false, "width": 0, "targets": 14}
            // {
            //     "width": 60,
            //     "targets": 12
            // }
        ],
        "order": [[1, 'asc']],
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            $(row).children('td').eq(1).attr('style', 'text-align: center;');
            $(row).children('td').eq(7).attr('style', 'text-align: right;');
            $(row).children('td').eq(12).attr('style', 'text-align: right;');
            $(row).children('td').eq(13).attr('style', 'text-align: center;');
            $(row).children('td').eq(0).html("<input type='checkbox' name='ap_list_" + activeTabId + "' id='ap_list_" + activeTabId + "'  />");
            if (data.lineNumber == 1000)
                $(row).children('td').eq(0).html("");
            if (tab_conap_checktype == 2 && data.lineNumber != 1000) {//调整
                // 2017-01-20，增加节点编号
                $(row).children('td').eq(2).html("<input type='text' name='contractAp_attribute2_" + activeTabId + "' class='form-control input-sm' style='width: 100%;' value='" + (data.attribute2 ? data.attribute2 : "") + "'/>");
                var l_style = null != data.styleDesc ? data.styleDesc : "";
                var planapDate = null != data.planapDate ? data.planapDate : "";
                // $(row).children('td').eq(3).html(linestylefun(data.lseId));//费用项
                $(row).children('td').eq(3).html(linenumfun(data.attribute3));//合同行号
                $(row).children('td').eq(6).html("<input type='text' id='styleDesc_" + activeTabId + "' name='styleDesc_" + activeTabId + "' class='form-control input-sm' style='width: 100%;' value='" + l_style + "'/>");
                $(row).children('td').eq(7).html(rcvTypefun(data.rcvType));
                var col5 = "<div class='input-group date' name='datetim_" + activeTabId + "'><input type='text' class='form-control input-sm' name='planapDate_" + activeTabId + "' value='" + planapDate + "'/>" + datecol + "</div>";
                $(row).children('td').eq(8).html(col5);
                //$(row).children('td').eq(9).html("<input type='text' id='apPlan_" + activeTabId + "' name='apPlan_" + activeTabId + "' onkeyup='javascript:setMoney(this);'  class='form-control input-sm' style='width: 100%;' value='" + data.apPlan + "'/>");
               // $(row).children('td').eq(9).html("<input type='text' id='apPlan_" + activeTabId + "' name='apPlan_" + activeTabId + "' onkeyup='javascript:setMoney(this);'  class='form-control input-sm' style='width: 100%;' value='" + fmoney(rmoney(data.planapAmount)*100/allTabData[activeTabId].tab_ap_contotal,4) + "'/>");
                $(row).children('td').eq(9).html("<input type='text' id='apPlan_" + activeTabId + "' name='apPlan_" + activeTabId + "' onkeyup='javascript:setMoney(this);' onblur='javascript:setMoney(this);' class='form-control input-sm' style='width: 100%;' value='" + Math.round(rmoney(data.planapAmount)*10000/allTabData[activeTabId].tab_ap_contotal)/100 + "'/>");
                $(row).children('td').eq(10).html("<input type='text' id='planapAmount_" + activeTabId + "' name='planapAmount_" + activeTabId + "' onkeyup='javascript:setPercent(this);' onblur='javascript:setPercent(this);' class='form-control input-sm' value='" + rmoney(data.planapAmount) + "'/>");
                //是否是确认节点，---
                if (data.milestoneFlag == 'Y') {
                    $(row).children('td').eq(11).html("<input type='checkbox' name='ap_checked_" + activeTabId + "' id='ap_checked_" + activeTabId + "' checked='checked'/>");
                } else {
                    $(row).children('td').eq(11).html("<input type='checkbox' name='ap_checked_" + activeTabId + "' id='ap_checked_" + activeTabId + "'/>");
                }

                $(row).children('td').eq(12).html(apMilestonefun(data.milestoneId,data.attribute3));
                $(row).children('td').eq(13).text(null != data.milestoneStatus ? data.milestoneStatus : "");
                //关联里程碑

            }else if(tab_conap_checktype == 2 && data.lineNumber== 1000) {//调整汇总行
                $(row).children('td').eq(9).text( Math.round(rmoney(data.planapAmount)*10000/allTabData[activeTabId].tab_ap_contotal)/100 )
            }
        },
        //  "order": [[1, 'asc']],
        "initComplete": function () { //去掉标准表格多余部分
            $("div[name=datetim_" + activeTabId + "]").datetimepicker({
                format: 'YYYY-MM-DD',
                debug: false
            });
            $('#dataTables-ap-line_' + activeTabId + '_wrapper').css("cssText", "margin-top: -8px;");
            $('#dataTables-ap-line_' + activeTabId + '_info').remove();
            $('#dataTables-conLine_' + activeTabId).css("height", "100%");
            autoTableHeight("dataTables-ap-line_" + activeTabId);
        }

    });
    $('#dataTables-ap-line_' + activeTabId).on('click', 'tr', function () {
        // if ($(this).hasClass('dt-select')) {
        //     $(this).removeClass('dt-select');
        // }
        // else {
        //     $('#dataTables-ap-line_' + activeTabId).DataTable().$('tr.dt-select').removeClass('dt-select');
        //     $(this).addClass('dt-select');
        // }

    });
    allTabData[activeTabId].tab_contractAp_t2 = tab_contractAp_t2;


}

function autoTableHeight(id) {
    window.setInterval(function () {
        if (0 == $('#' + id).length)
            return;
        var datetime = $('#' + id + ' tbody').find('.bootstrap-datetimepicker-widget');
        var h2 = $('#' + id).get(0).scrollHeight;
        if (0 != datetime.length) {
            // $(datetime[0]).focus();
            $('#' + id).parent().css("height", (h2 + 25) + "px");
            // $('#' + id).parent().css("maxHeight", "");
            // console.log("打开：" + h2);
        } else {
            $('#' + id).parent().css("height", (h2 + 20) + "px");
            // $('#' + id).parent().css("maxHeight", "500px");
            // console.log("关闭：" + h2);
        }
        if (500 < h2)
            $('#' + id).parent().css("maxHeight", "");
    }, 600);
}


function setMoney(input) {
    input.value = input.value.replace(/[^\-?\d.]/g, '');
    if ("" == input.value.trim())
        input.value = "0";
    var cash = parseFloat(allTabData[activeTabId].tab_ap_cashTotal);
    var percent = parseFloat(input.value);
    var $td = $(input).parent();
    var $td2 = $td.next();
    var tmpInput = $td2.find('input')[0];
    tmpInput.value = rmoney(fmoney(cash * percent / 100, 2));
    setLastRow();
}

function setPercent(input) {
    input.value = input.value.replace(/[^\-?\d.]/g, '');
    if ("" == input.value.trim())
        input.value = "0";
    var cash = parseFloat(allTabData[activeTabId].tab_ap_cashTotal);
    var cash2 = parseFloat(input.value);
    var $td = $(input).parent();
    var $td2 = $td.prev();
    var tmpInput = $td2.find('input')[0];
    tmpInput.value = rmoney(fmoney(cash2 / cash * 100, 4));
    setLastRow();
}

function setLastRow() {
    var $tds = $('#dataTables-ap-line_' + activeTabId + ' tr:last').find('td');
    var apPlan = $("input[name='apPlan_" + activeTabId + "']");
    var planapAmount = $("input[name='planapAmount_" + activeTabId + "']");
    var apPlanSum = 0;
    apPlan.each(function (i) {
        var a = apPlan[i].value;
        if (undefined == a || null == a || "" == a)
            a = "0";
        apPlanSum += rmoney(fmoney(a, 4));
    });
    var planapAmountSum = 0;
    planapAmount.each(function (i) {
        var b = planapAmount[i].value;
        if (undefined == b || null == b || "" == b)
            b = "0";
        planapAmountSum += rmoney(fmoney(b, 2));
    });
    $($tds[9]).html(fmoney(apPlanSum, 2));
    $($tds[10]).html(fmoney(planapAmountSum, 2));
}
//里程碑状态变更 setstatus
function setstatus(select) {
    console.log(select.value);
    var $td = $(select).parent();
    var $td2 = $td.next();
    var val = $('select', $(select).parent().parent().children('td').eq(3))[0].value;//合同行号
    console.log(val);
    if (select.value != "") {
        $.ajax({
            url: config.baseurl + "/api/contract/milestoneStatus",
            data: {
                kHeaderId:tab_conap_kheaderId,//合同号
                lineNum: val,//行号
                lineId: select.value//里程碑行号
            },
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",
            type: "GET",
            beforeSend: function () {
            },
            success: function (data) {
                console.log(data)
                $td2.text(data.status);
            },
            complete: function () {
            },
            error: function (XMLHttpRequest) {
                window.parent.error(XMLHttpRequest);
            }
        });
    } else {
        $td2.text("");
    }
}
//setLineInfo,合同行信息，同时更新里程下拉列表
function setLineInfo(select) {
    console.log(select.value);
    var $td = $(select).parent();
    var $td2 = $td.next();
    var $td3 = $td2.next();
    if (select.value != "") {
        $.ajax({
            url: config.baseurl + "/api/contract/contractInfo",
            data: {
                kheaderId: tab_conap_kheaderId,
                lineNum: select.value
            },
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",
            type: "GET",
            beforeSend: function () {
            },
            success: function (data) {
                console.log(data)
                $td2.text(data.style);
                $td3.text(data.amount);

                updateMilestone(select);
            },
            complete: function () {
            },
            error: function (XMLHttpRequest) {
                window.parent.error(XMLHttpRequest);
            }
        });
    } else {
        $td2.text("");
        $td3.text("");
        updateMilestone(select);
    }
    // if (select.value != "") {//更新里程碑下拉列表
    //     $.ajax({     //异步，调用后台get方法
    //         url: config.baseurl + "/api/contract/milestone", //调用地址，不包含参数
    //         contentType: "application/json",
    //         dataType: "json",
    //         async: true,
    //         data: {
    //             kHeaderId: tab_conap_kheaderId , //传入参数，依次填写
    //             lineNumber:select.value
    //         },
    //         type: "GET",  //调用方法类型
    //         beforeSend: function () {
    //         },
    //         success: function (data) {
    //             console.log(tab_l_projectId)
    //             tab_apMilestone_new = data.dataSource;//数据源赋值
    //             allTabData[activeTabId].tab_apMilestone_new = tab_apMilestone_new;
    //             // 获取到对象
    //             var check="N";
    //             var sel = $('select', $(select).parent().parent().children('td').eq(12));
    //             var selval=sel.value;
    //             //清空已有数据
    //             sel.empty();
    //
    //             for (var i = 0; i < allTabData[activeTabId].tab_apMilestone_new.length; i++) {
    //                  if (tab_l_projectId == allTabData[activeTabId].tab_apMilestone_new[i].projectId) {
    //                      check = "Y";//如果是当前项目，可以重置默认选项
    //                  }
    //                 sel.append("<option value='" + allTabData[activeTabId].tab_apMilestone_new[i].lineId + "'>" + allTabData[activeTabId].tab_apMilestone_new[i].name + "</option>")
    //             }
    //             if(check=="Y"){
    //                 sel.val(selval);
    //             }
    //         },
    //         complete: function () {
    //         },
    //         error: function () {
    //             window.parent.error(ajaxError_sys);
    //         }
    //     });
    // }

}
function updateMilestone(select) {
    if (select.value != "") {//更新里程碑下拉列表
        $.ajax({     //异步，调用后台get方法
            url: config.baseurl + "/api/contract/milestone", //调用地址，不包含参数
            contentType: "application/json",
            dataType: "json",
            async: true,
            data: {
                kHeaderId: tab_conap_kheaderId , //传入参数，依次填写
                lineNumber:select.value
            },
            type: "GET",  //调用方法类型
            beforeSend: function () {
            },
            success: function (data) {
                console.log(tab_l_projectId)
                tab_apMilestone_new = data.dataSource;//数据源赋值
                allTabData[activeTabId].tab_apMilestone_new = tab_apMilestone_new;
                // 获取到对象
                var check="N";
                var sel = $('select', $(select).parent().parent().children('td').eq(12));
                var selval=sel[0].value;

                //清空已有数据
                sel.empty();
                sel.append("<option value=''>请选择</option>");
                for (var i = 0; i < allTabData[activeTabId].tab_apMilestone_new.length; i++) {
                    if (tab_l_projectId == allTabData[activeTabId].tab_apMilestone_new[i].projectId) {
                        check = "Y";//如果是当前项目，可以重置默认选项
                    }
                    sel.append("<option value='" + allTabData[activeTabId].tab_apMilestone_new[i].lineId + "'>" + allTabData[activeTabId].tab_apMilestone_new[i].name + "</option>")
                }
                if(check=="Y"){
                    sel.val(selval);
                }else{
                    sel.val(-1);
                }
            },
            complete: function () {
            },
            error: function () {
                window.parent.error(ajaxError_sys);
            }
        });
    }

}
//查询按钮
function createContractApQueryConapPlanVersion() {
    $('#bu_queryConapPlanVersion_' + activeTabId).on('click', function () {
        if (tab_conap_checktype == 2) {
            window.parent.showConfirm(confirmquery, "请确认已保存调整计划");
        } else if (tab_conap_checktype == 0) {
            window.parent.error("该项目没有关联主合同！");
            tab_conap_checktype = 0;
            allTabData[activeTabId].tab_conap_checktype = tab_conap_checktype;
            return false;
        } else {
            tab_conap_checktype = 1;//更改显示类型
            allTabData[activeTabId].tab_conap_checktype = tab_conap_checktype;
            tab_conap_version = $("#s_conapPlan_version_" + activeTabId + " option:selected").val();
            allTabData[activeTabId].tab_conap_version = tab_conap_version;

            getconapHead();
        }
    });
}
var confirmquery = function () {
    tab_conap_checktype = 1;//更改显示类型
    allTabData[activeTabId].tab_conap_checktype = tab_conap_checktype;
    tab_conap_version = $("#s_conapPlan_version_" + activeTabId + " option:selected").val();
    allTabData[activeTabId].tab_conap_version = tab_conap_version;

    getconapHead();
}
//调整按钮
function createContractApApadj() {
    $('#bu_apadj_' + activeTabId).on('click', function () {
        if ($('#conStatus_' + activeTabId)[0].value == '批准') {
            window.parent.showConfirm(confirmApadj1, "调整计划将会将现有版本升版为新建状态，请确认是否调整");
        } else if ($('#conStatus_' + activeTabId)[0].value != '批准' && $('#conStatus_' + activeTabId)[0].value != '审批中') {
            window.parent.showConfirm(confirmApadj2, "请确认是否调整计划");
        }
    });
}

var confirmApadj1 = function () {
    tab_conap_checktype = 2;//更改显示类型
    allTabData[activeTabId].tab_conap_checktype = tab_conap_checktype;
    $('#bu_apadj_' + activeTabId).attr('disabled', true);
    $('#bu_apdel_v_' + activeTabId).attr('disabled', true);
    //调整计划后台取值
    $.ajax({
        url: config.baseurl + "/api/contract/update",
        data: {
            kheaderId: tab_conap_kheaderId,
            version: $('#s_conapPlan_version_' + activeTabId).val()
        },
        contentType: 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "POST",
        beforeSend: function () {
        },
        success: function (d) {
            loadWithUp();

        },
        complete: function () {
        },
        error: function () {
            window.parent.error("重新加载数据出错！");
        }
    });
}

var confirmApadj2 = function () {
    tab_conap_checktype = 2;//更改显示类型
    allTabData[activeTabId].tab_conap_checktype = tab_conap_checktype;
    $('#bu_apadj_' + activeTabId).attr('disabled', true);
    $('#bu_apdel_v_' + activeTabId).attr('disabled', true);
    $('#bu_apadd_' + activeTabId).attr('disabled', false);
    $('#bu_apdel_' + activeTabId).attr('disabled', false);
    $('#bu_apmoveup_' + activeTabId).attr('disabled', false);
    $('#bu_apmovedown_' + activeTabId).attr('disabled', false);
    $('#bu_apsave_' + activeTabId).attr('disabled', false);
    $('#bu_apsubmit_' + activeTabId).attr('disabled', false);
    getconapHead();

}
// var confirmApadj = function () {
//     if($('#conStatus_' + activeTabId)[0].value == '批准'){
//         if (confirm("调整计划将会将现有版本升版为新建状态，请确认是否调整")) {
//             tab_conap_checktype = 2;//更改显示类型
//             allTabData[activeTabId].tab_conap_checktype = tab_conap_checktype;
//             $('#bu_apadj_' + activeTabId).attr('disabled', true);
//             //调整计划后台取值
//             $.ajax({
//                 url: config.baseurl + "/api/contract/update",
//                 data: {
//                     kheaderId: tab_conap_kheaderId,
//                     version: $('#s_conapPlan_version_' + activeTabId).val()
//                 },
//                 contentType: 'application/x-www-form-urlencoded',
//                 dataType: "json",
//                 type: "POST",
//                 beforeSend: function () { },
//                 success: function (d) {
//                     loadWithUp();
//                 },
//                 complete: function () { },
//                 error: function () {
//                     window.parent.error("重新加载数据出错！");
//                 }
//             });
//         }
//     } else {
//         tab_conap_checktype = 2;//更改显示类型
//         allTabData[activeTabId].tab_conap_checktype = tab_conap_checktype;
//         $('#bu_apadj_' + activeTabId).attr('disabled', true);
//         $('#bu_apadd_' + activeTabId).attr('disabled', false);
//         $('#bu_apdel_' + activeTabId).attr('disabled', false);
//         $('#bu_apsave_' + activeTabId).attr('disabled', false);
//         $('#bu_apsubmit_' + activeTabId).attr('disabled', false);
//         getconapHead();
//     }
// }

// 调整按钮最后是否可用
var bu_apadjFlag = false;
function loadWithUp() {
    $('#s_conapPlan_version_' + activeTabId).empty();//清空下拉表
    $.ajax({     //异步，调用后台get方法
        url: config.baseurl + "/api/contract/version", //调用地址，不包含参数
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: {
            kheaderId: tab_conap_kheaderId,  //传入参数，依次填写
            projectId: tab_l_projectId
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {
        },
        success: function (data) {
            bu_apadjFlag = false;
            var list1 = data.dataSource;
            if ("S" == data.message)
                bu_apadjFlag = true;
            list1.forEach(function (data) {  //取最大版本
                tab_conap_version = data.version;
                tab_conap_version_max = data.version;
                allTabData[activeTabId].tab_conap_version = tab_conap_version;
                allTabData[activeTabId].tab_conap_version_max = tab_conap_version_max;
            });
            list1.forEach(function (data) {  //下拉列表，如果是最大版本，则默认选中
                if (data.version == tab_conap_version_max) {
                    $('#s_conapPlan_version_' + activeTabId).append("<option data-statusVersion='" + data.statusVersion + "' value='" + data.version + "'selected>" + data.version + "</option>");
                } else {
                    $('#s_conapPlan_version_' + activeTabId).append("<option data-statusVersion='" + data.statusVersion + "' value='" + data.version + "'>" + data.version + "</option>");
                }
            });
            getconapHead();
        },
        complete: function () {
        },
        error: function () {
        }
    });
}
//删除版本按钮
function deleteConVersion() {
    $('#bu_apdel_v_' + activeTabId).on('click', function () {

        window.parent.showConfirm(confirmdelete_v, "确认[删除该版本收款计划]？");
    });
}
var confirmdelete_v = function () {
    $.ajax({
        url: config.baseurl + "/api/contract/deleteapv",
        contentType: "application/x-www-form-urlencoded",
        data: {
            headerId: tab_conap_headerId,
            kHeaderId: tab_conap_kheaderId
        },
        dataType: "json",
        type: "GET",
        beforeSend: function () {
        },
        success: function (data) {
            if ("S" == data.db_url) {
                if (tab_conap_version == 1)
                    window.parent.success("版本号为1，已重置！");
                else
                    window.parent.success("删除版本成功！")
                tab_conap_version = tab_conap_version - 1;
                getConapVersion(1);
            } else {
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
var creater = {
    //生成一个行对象
    "createRowObj": function (num) {
        var lineNumber = num + 1;  //json ? json.templateId : "";
        var styleDesc = "";
        var lineInfo = "";

        var lineStyle = "";
        var lineAmount = "";
        var rcvTypeMeaning = "";
        var planapDate = "";
        var apPlan = "";
        var planapAmount = "";
        var milestoneFlag   = "";
        var name = "";
        var milestoneStatus = "";
        var attribute2 = "";
        var attribute3 = "";
        if (allTabData[activeTabId].isonly == "Y") {  //如果合同行唯一，则在调整时直接更新合同行
            attribute3 = allTabData[activeTabId].linenumber1;
            lineStyle=allTabData[activeTabId].linestyle1;
            lineAmount=allTabData[activeTabId].linevalue1;
        }
        return {
            "lineNumber": lineNumber,
            "lineInfo": lineInfo,
            "lineStyle": lineStyle,
            "lineAmount": lineAmount,
            "styleDesc": styleDesc,
            "rcvTypeMeaning": rcvTypeMeaning,
            "planapDate": planapDate,
            "apPlan": apPlan,
            "planapAmount": planapAmount,
            "milestoneFlag": milestoneFlag,
            "name": name,
            "milestoneStatus": milestoneStatus,
            "attribute2": attribute2,
            "attribute3": attribute3
        };
    }
}

//新增
function createContractApApadd() {
    $('#bu_apadd_' + activeTabId).on('click', function () {
        var oldnode = tab_contractAp_t2.data().length;
        var oldlast = tab_contractAp_t2.row([oldnode - 1]).data();//保存汇总行
        var num = tab_contractAp_t2.row([oldnode - 2]).data().lineNumber;
        tab_contractAp_t2.row([oldnode - 1]).remove();//删除汇总行
        tab_contractAp_t2.row.add(creater.createRowObj(num));//新增一行
        tab_contractAp_t2.row.add(oldlast);//增加汇总行
        tab_contractAp_t2.draw();
        $("div[name=datetim_" + activeTabId + "]").datetimepicker({
            format: 'YYYY-MM-DD'
        });

        //更新汇总行数据
        //需要重新导入汇总行的数据，


    });
}
//删除
function createContractApApdel() {
    $('#bu_apdel_' + activeTabId).on('click', function () {
        var tmp = tab_contractAp_t2.rows($("input[name='ap_list_" + activeTabId + "']:checked").parents('tr'));
        if (0 == tmp[0].length) {
            window.parent.warring(valTips_selOne);
            return;
        }
        window.parent.showConfirm(confirmApApdel, "确认[删除]？");
    });
}

var confirmApApdel = function () {
    tab_contractAp_t2.rows($("input[name='ap_list_" + activeTabId + "']:checked").parents('tr')).remove();
    tab_contractAp_t2.draw();
}

//上移
function createApmoveup() {
    $('#bu_apmoveup_' + activeTabId).on('click', function () {
        var data = tab_contractAp_t2.rows($("input[name='ap_list_" + activeTabId + "']:checked").parents('tr')).data();
        console.log("data:" + data);
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        } else if (2 <= data.length) {
            window.parent.warring(valTips_workOne);
            return;
        }
        var oldnum = data[0].lineNumber;
        console.log("oldnum:" + oldnum);
        if (oldnum == 1) {
            window.parent.warring("第一行不能上移");
            return;
        } else {

            var data2 = tab_contractAp_t2.data();
            tab_contractAp_t2.clear();
            data2.splice((oldnum - 2), 0, data2.splice(oldnum - 1, 1)[0]);
            data2[oldnum - 2].lineNumber = oldnum - 1;//调换顺序
            data2[oldnum - 1].lineNumber = oldnum;
            tab_contractAp_t2.rows.add(data2).draw();
        }
    });
}
//下移
function createApmovedown() {
    $('#bu_apmovedown_' + activeTabId).on('click', function () {
        var data = tab_contractAp_t2.rows($("input[name='ap_list_" + activeTabId + "']:checked").parents('tr')).data();
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        } else if (2 <= data.length) {
            window.parent.warring(valTips_workOne);
            return;
        }
        var oldnum = data[0].lineNumber;
        console.log("oldnum:" + oldnum);
        console.log(tab_contractAp_t2.rows().data().length);
        if (oldnum == tab_contractAp_t2.rows().data().length - 1) {
            window.parent.warring("最后一行不能下移");
            return;
        } else {

            var Tdata = tab_contractAp_t2.data();
            tab_contractAp_t2.clear();
            Tdata.splice((oldnum - 1), 0, Tdata.splice(oldnum, 1)[0]);
            Tdata[oldnum - 1].lineNumber = oldnum;//调换顺序
            Tdata[oldnum].lineNumber = oldnum + 1;
            tab_contractAp_t2.rows.add(Tdata).draw();
        }
    });
}

// 提交审批
function createContractApApSubmit() {
    $('#bu_apsubmit_' + activeTabId).on('click', function () {
        saveContractAp(1);
    });
}

// 开票申请
function creatbill() {
    $('#bu_billcreate_' + activeTabId).on('click', function () {
        var url = "/pages/aria/invoiceApply.html?" + "dowork=billEvent&contractId=" + tab_conap_kheaderId + "&projectId=" + tab_l_projectId;
        console.log(url);
        window.location.href = url;

    });
}

function submitContractAp() {
    window.parent.showConfirm(function () {
        $.ajax({
            url: config.baseurl + "/api/contract/submitOaApproved",
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",   //返回格式为json
            data: {
                headerId: tab_conap_headerId
            },
            type: "POST",   //请求方式
            beforeSend: function () {
                window.parent.showLoading();
            },
            success: function (data) {
                if ("0000" == data.returnCode) {
                    window.parent.success("提交成功");
                    tab_conap_checktype = 1;
                    $('#bu_queryConapPlanVersion_' + activeTabId).trigger("click");
                    window.open(data.db_url);
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
    }, "确认[提交审批]？");
}

//保存
function createContractApApsave() {
    $('#bu_apsave_' + activeTabId).on('click', function () {
        saveContractAp();
    });
}

function saveContractAp(v) {
    var o;
    var arr = new Array();
    var lineInfo = $("select[name='conap_linenum_" + activeTabId + "']");
    var styleDesc = $("input[name='styleDesc_" + activeTabId + "']");
    var rcvType = $("select[name='conap_rcvtype_" + activeTabId + "']");
    var planapDate = $("input[name='planapDate_" + activeTabId + "']");
    var apPlan = $("input[name='apPlan_" + activeTabId + "']");
    var planapAmount = $("input[name='planapAmount_" + activeTabId + "']");
    var milestoneFlag = $("input[name='ap_checked_" + activeTabId + "']");//是否是确认项
    var attribute11 = $("select[name='conap_milestone_" + activeTabId + "']");//里程碑
    var attribute2 = $("input[name='contractAp_attribute2_" + activeTabId + "']");//节点编号
    var l_applan = 0;
    var l_planamount = 0;
    //判断
    var tipsMsg = "";
    for (i = 0; i < tab_contractAp_t2.data().length - 1; i++) {
        if (lineInfo[i].value == "-1" || lineInfo[i].value == "") {
            tipsMsg += "第" + (i + 1) + "行，未选择合同行！<br>" ;
            window.parent.warring(tipsMsg);
            return false;
        }
    }

    if (1 == v) {
        for (i = 0; i < tab_contractAp_t2.data().length - 1; i++) {
            if (attribute2[i].value == "") {
                tipsMsg += "第" + (i + 1) + "行，未填写交付节点编号！<br>";
                // window.parent.error("未填写交付节点编号！");
                // return false;
            }
            if (lineInfo[i].value == "-1" || lineInfo[i].value == "") {
                tipsMsg += "第" + (i + 1) + "行，未选择合同行！<br>";
                // window.parent.error("");
                // return false;
            }
            if (rcvType[i].value == "-1") {
                tipsMsg += "第" + (i + 1) + "行，未选择款项性质！<br>";
                // window.parent.error("有未选择的款项性质！");
                // return false;
            }
            if (planapDate[i].value == "") {
                tipsMsg += "第" + (i + 1) + "行，未填写预计收款日期！<br>";
                // window.parent.error("有未填写的预计收款日期！");
                // return false;
            }
            if (apPlan[i].value == "") {
                tipsMsg += "第" + (i + 1) + "行，未填写应收百分比！<br>";
                // window.parent.error("有未填写的应收百分比！");
                // return false;
            }
            if (planapAmount[i].value == "") {
                tipsMsg += "第" + (i + 1) + "行，未填写应收金额！<br>";
                // window.parent.error("有未填写的应收金额！");
                // return false;
            }
            if (milestoneFlag[i].checked) {
                if (attribute11[i].value == "") {
                    tipsMsg += "第" + (i + 1) + "行，已选择了收入确认节点但未关联里程碑！<br>";
                    // window.parent.error("已选择了收入确认节点但未关联里程碑！");
                    // return false;
                }
            }
            l_applan = l_applan * 1 + apPlan[i].value * 1;
            l_planamount = l_planamount * 1 + planapAmount[i].value * 1;
        }

        // 里程碑不能选择重复
        var lcbList = [];
        for (var j = 0; j < attribute11.length; j++) {
            var lcb_tmp = attribute11[j].value;
            if ("" != lcb_tmp) {
                if (0 > lcbList.indexOf(lcb_tmp)) {
                    lcbList.push(lcb_tmp);
                } else {
                    tipsMsg += "第" + (j + 1) + "行关联里程碑与第" + (lcbList.indexOf(lcb_tmp) + 1) + "行相同，请修改<br>";
                }
            }
        }

        // if(fmoney(l_applan,2)!=100){    //去掉百分比验证
        //     tipsMsg += "应收百分比之和不为100！<br>";
        //     // window.parent.warring("应收百分比之和不为100！");
        //     // return false;
        // }

        // if (parseFloat (l_planamount) != parseFloat(allTabData[activeTabId].tab_ap_contotal)) {
        //     tipsMsg += "应收金额不等于合同金额";
        //     // window.parent.warring("应收金额大于合同金额");
        //     // return false;
        // }
        if (l_planamount.toFixed(2) != allTabData[activeTabId].tab_ap_contotal) {
            tipsMsg += "应收金额不等于合同金额";
        }
        //console.log(Math.round(30934378.000000004*100)/100);


        console.log(allTabData[activeTabId].tab_ap_contotal);
        if ("" != tipsMsg) {
            window.parent.warring(tipsMsg);
            return;
        }
    }

    for (var i = 0; i < tab_contractAp_t2.data().length - 1; i++) {

        o = new Object();
        o.headerId = tab_conap_headerId;
        o.kheaderId = tab_conap_kheaderId;
        if (i < tab_dataSetAp.length - 1) {
            o.lineId = tab_dataSetAp[i].lineId;
        } else {
            o.lineId = "";
        }
        o.lineNumber = i + 1;

        o.attribute3 = lineInfo[i].value;
        o.styleDesc = styleDesc[i].value;
        o.rcvType = rcvType[i].value;
        o.planapDate = planapDate[i].value;
        o.apPlan = apPlan[i].value;
        o.planapAmount = planapAmount[i].value;
        if (milestoneFlag[i].checked) {
            o.attribute13 = "Y";
            o.attribute11 = attribute11[i].value;
           // o.attribute12 = tab_l_projectId;
        } else {
            o.attribute13 = "";
            o.attribute11 = attribute11[i].value;
            //o.attribute12 = tab_l_projectId;
        }
        o.attribute2 = attribute2[i].value;
        arr.push(o);
        console.log(o);
    }
    console.log(JSON.stringify(arr));

    $.ajax({
        url: config.baseurl + "/api/contract/save",
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: JSON.stringify(arr),
        type: "POST",
        beforeSend: function () {
            window.parent.showLoading();
        },
        success: function (d) {
            if (d.total == -1) {
                window.parent.warring(d.message);
            } else {
                console.log(d);
                tab_conap_headerId = d.headerId;
                allTabData[activeTabId].tab_conap_headerId = tab_conap_headerId;
                if (d.message == "保存成功"){
                    window.parent.success("保存成功！");
                }
                else{
                    window.parent.warring(d.message);

                    return;
                }

                if (1 == v)
                    submitContractAp();
            }
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

function getapcash() {
    $.ajax({     //异步，调用后台get方法
        url: config.baseurl + "/api/contract/cash", //调用地址，不包含参数
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: {
            kheaderId: tab_conap_kheaderId
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {

        },
        success: function (data) {
            tab_data_Set_c = data.dataSource;

            $('#apcashtotal_' + activeTabId).html(fmoney(data.total, 2));
            console.log(data.percent);
            $('#apcashpercent_' + activeTabId).html(data.percent + "%");
            $('#apcashtotalCNY_' + activeTabId).html(fmoney(data.totalCNY, 2));
            tab_data_Set_c.forEach(function (data) {
                data.recAmount = fmoney(data.recAmount, 2);
                data.recAmountCNY = fmoney(data.recAmountCNY, 2);
            });
            allTabData[activeTabId].tab_data_Set_c = tab_data_Set_c;
            createcash();
        },
        complete: function () {
        },
        error: function () {
            window.parent.error(ajaxError_sys);
        }
    });
}
function createcash() {
    $('#dataTables-apcash_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables_apcash_' + activeTabId + '"></table>');
    tab_t_cash = $('#dataTables_apcash_' + activeTabId).DataTable({
        "processing": true,
        "data": tab_data_Set_c,
        // "scrollX": true,
        "searching": false,
        "orderable": false,
        "autoWidth": false,
        "pagingType": "full", //
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "收款编码", "data": "receiptNumber"},
            {"title": "收款时间", "data": "glDate"},
            {"title": "币种", "data": "recCurr"},
            {"title": "收款金额（原币）", "data": "recAmount"},
            {"title": "收款金额（CNY）", "data": "recAmountCNY"},
            {"title": "项目编号", "data": "projNumber"},
            {"title": "客户名称", "data": "custName"},
            {"title": "摘要", "data": "description"}

        ],
        "columnDefs": [ {
            "searchable": false,
            "orderable": false,
            "width": 40,
            "targets": 0 //排序
        }, {
            "width": 120,
            "targets": 1
        },{
            "width": 100,
            "targets": 2
        }, {
            "orderable": false,
            "width": 40,
            "targets": 3
        },
            {
                "width": 120,
                "targets": 4 //原币
            },
            {"orderable": false,
                "width": 120,
                "targets": 5
            },{
                "width": 110,
                "targets": 6
            },
            {
                "width": 240,
                "targets": 7
            },{
                "width": 80,
                "targets": 8
            }
        ],
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            $(row).children('td').eq(5).attr('style', 'text-align: right;');
            $(row).children('td').eq(4).attr('style', 'text-align: right;');
        },
        "order": [],
        "initComplete": function () {
            $('#dataTables_apcash_' + activeTabId + '_length').insertBefore($('#dataTables_apcash_' + activeTabId + '_info'));
            $('#dataTables_apcash_' + activeTabId + '_length').addClass("col-sm-4");
            $('#dataTables_apcash_' + activeTabId + '_length').css("paddingLeft", "0px");
            $('#dataTables_apcash_' + activeTabId + '_length').css("paddingTop", "5px");
            $('#dataTables_apcash_' + activeTabId + '_length').css("maxWidth", "130px");
            $('#dataTables_apcash_' + activeTabId + '_wrapper').css("cssText", "margin-top: -5px;");
        }
    });

    tab_t_cash.on('order.dt search.dt', function () {
        tab_t_cash.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    }).draw();

    allTabData[activeTabId].tab_t_cash = tab_t_cash;
}

function getaptrx() {
    $.ajax({     //异步，调用后台get方法
        url: config.baseurl + "/api/contract/ratrx", //调用地址，不包含参数
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: {
            kheaderId: tab_conap_kheaderId
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {

        },
        success: function (data) {
            tab_data_Set_t = data.dataSource;

            $('#aptrxtotal_' + activeTabId).html(fmoney(data.total, 2));
            $('#aptrxpercent_' + activeTabId).html(data.percent + "%");
            $('#aptrxtotalCNY_' + activeTabId).html(fmoney(data.totalCNY, 2));
            tab_data_Set_t.forEach(function (data) {
                data.amount = fmoney(data.amount, 2);
                data.amountCNY = fmoney(data.amountCNY, 2);
            });
            allTabData[activeTabId].tab_data_Set_t = tab_data_Set_t;
            createtrx();
        },
        complete: function () {
        },
        error: function () {
            window.parent.error(ajaxError_sys);
        }
    });
}

function createtrx() {
    $('#dataTables-aptrx_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables_aptrx_' + activeTabId + '"></table>');
    tab_t_trx = $('#dataTables_aptrx_' + activeTabId).DataTable({
        "processing": true,
        "data": tab_data_Set_t,
        // "scrollX": true,
        "searching": false,
        "autoWidth": false,
        "pagingType": "full", //
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "发票编码", "data": "trxNumber"},
            {"title": "发票日期", "data": "trxDate"},
            {"title": "币种", "data": "invCURR"},
            {"title": "发票金额（原币）", "data": "amount"},
            {"title": "发票金额（CNY）", "data": "amountCNY"},
            {"title": "项目编号", "data": "projNumber"},
            {"title": "客户名称", "data": "custName"},
            {"title": "摘要", "data": "description"}
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "width": 40,
            "targets": 0 //排序
        }, {
            "width": 120,
            "targets": 1
        },{
            "width": 100,
            "targets": 2
        }, {
            "orderable": false,
            "width": 40,
            "targets": 3
        },
            {
                "width": 120,
                "targets": 4 //原币
            },
            {"orderable": false,
                "width": 120,
                "targets": 5
            },{
                "width": 110,
                "targets": 6
            },
            {
                "width": 240,
                "targets": 7
            },{
                "width": 80,
                "targets": 8
            }
        ],
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            $(row).children('td').eq(4).attr('style', 'text-align: right;');
            $(row).children('td').eq(5).attr('style', 'text-align: right;');
        },
        "order": [],
        "initComplete": function () {
            $('#dataTables_aptrx_' + activeTabId + '_length').insertBefore($('#dataTables_aptrx_' + activeTabId + '_info'));
            $('#dataTables_aptrx_' + activeTabId + '_length').addClass("col-sm-4");
            $('#dataTables_aptrx_' + activeTabId + '_length').css("paddingLeft", "0px");
            $('#dataTables_aptrx_' + activeTabId + '_length').css("paddingTop", "5px");
            $('#dataTables_aptrx_' + activeTabId + '_length').css("maxWidth", "130px");
            $('#dataTables_aptrx_' + activeTabId + '_wrapper').css("cssText", "margin-top: -5px;");
        }
    });

    tab_t_trx.on('order.dt search.dt', function () {
        tab_t_trx.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    }).draw();

    allTabData[activeTabId].tab_t_trx = tab_t_trx;
}

