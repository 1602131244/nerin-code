/**
 * Created by Administrator on 2017/7/21.
 */
var tab_dataSetProjbudget = [];
var tab_BudgetList;
var tab_CostList;
var tab_rbsVersionId;
var tab_expenditureType;
var tab_taskId;
var tab_dataSetProjCost = [];

function tab_initBudget() {
    getBudgetList();
    getCostList(-1);
    if(config.evn=="prod"){
        $("#allcostlist_"+ activeTabId).hide();
    }
}

function getBudgetList() {
    $.ajax({     //异步，调用后台get方法
        url: config.baseurl + "/api/budgetRest/projectBudgetList", //调用地址，不包含参数
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: {
            projectId: tab_l_projectId  //传入参数，依次填写
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {
            window.parent.showLoading();
        },
        success: function (data) {
            tab_dataSetProjbudget = data.dataSource;
            allTabData[activeTabId].tab_dataSetProjbudget = tab_dataSetProjbudget;
            console.log(tab_dataSetProjbudget);
            if(tab_dataSetProjbudget.length!=0) {
                tab_rbsVersionId = tab_dataSetProjbudget[0].rbsVersionId;//获取第一行rbsVersionId，用于全显示按钮
                allTabData[activeTabId].tab_rbsVersionId = tab_rbsVersionId;
            }

            //console.log(tab_dataSetProjbudget[0].rbsVersionId);
            createBudgetList();
            showAllCostList(tab_rbsVersionId);
        },
        complete: function () {
            window.parent.closeLoading();
        },
        error: function () {
            window.parent.error(ajaxError_sys);
        }
    });
}
function createBudgetList() {
    $('#dataTables-budgetlist_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-budgetlist-line_' + activeTabId + '"></table>');
    tab_BudgetList = $('#dataTables-budgetlist-line_' + activeTabId).DataTable({
        "processing": true,

        "data": tab_dataSetProjbudget,
        "paging": false,
        "searching": false,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "大类", "data": "budgetItemName"},
            {"title": "预算项", "data": "budgetName"},
            {"title": "顶层任务", "data": "parentTaskName"},
            {"title": "任务", "data": "taskName"},
            {"title": "预算金额", "data": "budgetAmount"},
            {"title": "占用预算金额", "data": "cost"},
            {"title": "发生金额", "data": "occurrenceAmount"},
            {"title": "在途成本", "data": "transitAmount"},
            {"title": "可用预算金额", "data": "surplusAmount"},
            {"title": "rbsVersionId", "data": "rbsVersionId"},
            //{"title": "budgetName", "data": "budgetName"},
            {"title": "taskId", "data": "taskId"}

        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "width": 32,
            "targets": 0 //排序
        }, {
            "width": 100,
            "targets": 1 //排序
        }, {
            "width": 100,
            "targets": 2 //排序
        }, {
            "width": 60,
            "targets": 3 //排序
        }, {
            "width": 60,
            "targets": 4 //排序
        }, {"visible": false, "width": 0, "targets": 10},
            {"visible": false, "width": 0, "targets": 11}
          //  {"visible": false, "width": 0, "targets": 12}
        ],
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            $(row).children('td').eq(5).attr('style', 'text-align: right;');
            $(row).children('td').eq(6).attr('style', 'text-align: right;');
            $(row).children('td').eq(7).attr('style', 'text-align: right;');
            $(row).children('td').eq(8).attr('style', 'text-align: right;');
            $(row).children('td').eq(9).attr('style', 'text-align: right;');
        },
        "order": [],
        "initComplete": function () {
            $('#dataTables-budgetlist-line_' + activeTabId).css("cssText", "margin-top: 0px !important; margin-bottom: 0px !important;");

        }

    });

    tab_BudgetList.on('order.dt search.dt', function () {
        tab_BudgetList.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    }).draw();
    $('#dataTables-budgetlist-line_' + activeTabId).on('click', 'tr', function () {
        var table = $('#dataTables-budgetlist-line_' + activeTabId).DataTable();
        if ($(this).hasClass('dt-select')) {
            $(this).removeClass('dt-select');
        }
        else {
            $('#dataTables-budgetlist-line_' + activeTabId).DataTable().$('tr.dt-select').removeClass('dt-select');
            $(this).addClass('dt-select');
            var data = table.rows($(this)).data();
            getCostList(data[0].rbsVersionId,data[0].budgetName,data[0].taskId);
        }

    });
    allTabData[activeTabId].tab_BudgetList = tab_BudgetList;
}

function getCostList(rbsVersionId, budgetName, taskId) {
    if(rbsVersionId==-1) {
        tab_dataSetProjCost = [];
        allTabData[activeTabId].tab_dataSetProjCost = tab_dataSetProjCost;
        createCostList();
    }else {
        $.ajax({     //异步，调用后台get方法
            url: config.baseurl + "/api/budgetRest/projectCostList", //调用地址，不包含参数
            contentType: "application/json",
            dataType: "json",
            async: true,
            data: {
                projectId: tab_l_projectId,//传入参数，依次填写
                rbsVersionId: rbsVersionId,
                expenditureType: budgetName,
                taskId: taskId
            },
            type: "GET",  //调用方法类型
            beforeSend: function () {
                window.parent.showLoading();
            },
            success: function (data) {
                tab_dataSetProjCost = data.dataSource;
                allTabData[activeTabId].tab_dataSetProjCost = tab_dataSetProjCost;
                console.log(tab_dataSetProjCost);
                createCostList();
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
}

function createCostList() {
    $('#dataTables-costlist_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-all_cost-line_' + activeTabId + '"></table>');
    tab_CostList = $('#dataTables-all_cost-line_' + activeTabId).DataTable({
        "processing": true,
        "scrollX": true,
        "data": tab_dataSetProjCost,
        "searching": true,
        "pagingType": "full_numbers",
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "成本类型", "data": "costType"},
            {"title": "支出类型", "data": "expenditureType"},
            {"title": "顶层任务", "data": "parentTaskName"},
            {"title": "任务名称", "data": "taskName"},
            {"title": "支出日期", "data": "expenditureDate"},
            {"title": "发票来源", "data": "invnoType"},
            {"title": "发票编号", "data": "invnoNumber"},
            {"title": "数量", "data": "quantity"},//8
            {"title": "金额", "data": "cost"},
            {"title": "供应商名称", "data": "vendorName"},
            {"title": "摘要", "data": "note"}

        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "width": 32,
            "targets": 0 //排序
        }, {
            "width": 100,
            "targets": 1 //排序
        }, {
            "width": 100,
            "targets": 2 //排序
        }, {
            "width": 60,
            "targets": 3 //排序
        }, {
            "width": 60,
            "targets": 4 //排序
        },
            {
                "width": 80,
                "targets": 5 //排序
            },{
                "width": 60,
                "targets": 6 //排序
            },{
                "width": 150,
                "targets": 7 //排序
            },{
                "width": 60,
                "targets": 8 //排序  数量
            },{
                "width": 80,
                "targets": 9 //排序
            },{
                "width": 150,
                "targets": 10 //排序
            },{
                "width": 150,
                "targets": 11//排序
            }
        ],
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            $(row).children('td').eq(8).attr('style', 'text-align: right;');
           // $(row).children('td').eq(5).attr('style', 'text-align: right;');
            $(row).children('td').eq(9).attr('style', 'text-align: right;');
        },
        "order": [],
        "initComplete": function () {
            $('#dataTables-all_cost-line_' + activeTabId).css("cssText", "margin-top: 0px !important; margin-bottom: 0px !important;");

        }

    });

    tab_CostList.on('order.dt search.dt', function () {
        tab_CostList.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    }).draw();

    allTabData[activeTabId].tab_BudgetList = tab_BudgetList;
}
//所有成本列表按钮
function showAllCostList(tab_rbsVersionId) {
    $('#allcostlist_' + activeTabId).on('click', function () {
        getCostList(tab_rbsVersionId,null,null);
    });
}