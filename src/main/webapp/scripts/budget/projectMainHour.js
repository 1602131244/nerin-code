/**
 * Created by Administrator on 2018/2/13.
 */


var config = new nerinJsConfig();
var projData;
var projDataSet = [];
var projTable;

$(function () {
    console.log("page loaded");

    //初始化table数据
    initProjTables();
});


// 初始化dataTables程序
function initProjTables() {
    $('#dataTables-listProj').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-projData"></table>');

    projTable = $('#dataTables-projData').DataTable({
        "processing": true,
        "data": projDataSet,
        "scrollX": true,
        "scrollY": "500px",
        "scrollCollapse": true,
        "pagingType": "full_numbers",
        "language": {
            "url": "/scripts/common/Chinese.json"
        },

        // searching : false, //去掉搜索框方法一：百度上的方法，但是我用这没管用
        // sDom : '"top"i',   //去掉搜索框方法二：这种方法可以
        // bLengthChange: false,   //去掉每页显示多少条数据方法

        "searching": false,
        // "bLengthChange": false,
        "ordering ": false,
        // "paging": false,  //禁止表格分页
        "lengthMenu": [[10, 25, 50,100,1000, -1], [10, 25, 50,100,1000, "所有"]],

        // {"title": "<input type='checkbox' name='ch_listAll' id='ch_listAll' onclick='javascript:setCheckAll();' />", "data": null},
        "columns": [
            {"title": "", "data": null},           //0
            {"title": "序号", "data": null, "className": "dt_center"},      //1
            {"title": "项目编号", "data": "projectNumber", "className": "dt_center"},   //2
            {"title": "项目名称", "data": "projectName", "className": "dt_center"}, //3
            {"title": "总工时</br>（小时）", "data": "cExtAttr4", "className": "dt_center"},  //4
            {"title": "项目状态", "data": "projectStatus", "className": "dt_center"}, //5
            {"title": "项目开始日期", "data": "projectStartDate", "className": "dt_center"},//6
            {"title": "项目结束日期", "data": "projectEndDate", "className": "dt_center"},  //7
            {"title": "项目类型", "data": "projectType", "className": "dt_center"}, //8
            {"title": "项目经理", "data": "projectManager", "className": "dt_center"},  //9
            {"title": "项目行业", "data": "projectClass", "className": "dt_center"},  //10
            {"title": "项目开票客户", "data": "projectCustName", "className": "dt_center"},  //11
            {"title": "科目属性", "data": "attribute1", "className": "dt_center"},  //12
            {"title": "全员可见项目无安全性", "data": "attribute2", "className": "dt_center"},  //13
            {"title": "费用报销超预算不强控", "data": "attribute3", "className": "dt_center"}, //14
            {"title": "科研项目是否有国拨资金", "data": "attribute5", "className": "dt_center"}, //15
            {"title": "科目部门段值是否需要", "data": "attribute6", "className": "dt_center"}, //16
            {"title": "项目执行部门", "data": "attribute4", "className": "dt_center"}, //17
            {"title": "收入归口部门", "data": "attribute8", "className": "dt_center"}, //18
            {"title": "项目组织", "data": "projectOrgName", "className": "dt_center"}, //19
            {"title": "公司", "data": "orgName", "className": "dt_center"}, //20
            {"title": "projectId", "data": "projectId", "className": "dt_center"}, //21
            {"title": "extensioId", "data": "extensioId", "className": "dt_center"}, //22
            {"title": "projectId", "data": "editFlag", "className": "dt_center"} //23
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "targets": 0,
            "width": 5
        }, {
            "searchable": false,
            "orderable": false,
            "targets": 1,
            "width": 20
        }, {
            "orderable": false,
            "width": 100,
            "targets": 2
        }, {
            "orderable": false,
            "width": 200,
            "targets": 3
        }, {
            "orderable": false,
            "width": 65,
            "targets": 4
        }, {
            "orderable": false,
            "width": 65,
            "targets": 5
        }, {
            "orderable": false,
            "width": 90,
            "targets": 6
        }, {
            "orderable": false,
            "width": 90,
            "targets": 7
        }, {
            "orderable": false,
            "width": 65,
            "targets": 8
        }, {
            "orderable": false,
            "width": 65,
            "targets": 9
        }, {
            "orderable": false,
            "width": 65,
            "targets": 10
        }, {
            "orderable": false,
            "width": 200,
            "targets": 11
        }, {
            "orderable": false,
            "width": 100,
            "targets": 12
        }, {
            "orderable": false,
            "width": 90,
            "targets": 13
        }, {
            "orderable": false,
            "width": 90,
            "targets": 14
        }, {
            "orderable": false,
            "width": 90,
            "targets": 15
        }, {
            "orderable": false,
            "width": 90,
            "targets": 16
        }, {
            "orderable": false,
            "width": 200,
            "targets": 17
        }, {
            "orderable": false,
            "width": 200,
            "targets": 18
        }, {
            "orderable": false,
            "width": 200,
            "targets": 19
        }, {
            "orderable": false,
            "width": 200,
            "targets": 20
        },{
            "width": 0,
            "targets": 21,
            "visible": false
        },{
            "width": 0,
            "targets": 22,
            "visible": false
        },{
            "width": 0,
            "targets": 23,
            "visible": false
        }
        ],
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            $(row).children('td').eq(1).attr('style', 'text-align: center;');
            $(row).children('td').eq(2).attr('style', 'text-align: left;');
            $(row).children('td').eq(3).attr('style', 'text-align: left;');
            $(row).children('td').eq(4).attr('style', 'text-align: right;');
            $(row).children('td').eq(5).attr('style', 'text-align: left;');
            $(row).children('td').eq(6).attr('style', 'text-align: left;');
            $(row).children('td').eq(7).attr('style', 'text-align: left;');
            $(row).children('td').eq(8).attr('style', 'text-align: left;');
            $(row).children('td').eq(9).attr('style', 'text-align: left;');
            $(row).children('td').eq(10).attr('style', 'text-align: left;');
            $(row).children('td').eq(11).attr('style', 'text-align: left;');
            $(row).children('td').eq(12).attr('style', 'text-align: left;');
            $(row).children('td').eq(13).attr('style', 'text-align: left;');
            $(row).children('td').eq(14).attr('style', 'text-align: left;');
            $(row).children('td').eq(15).attr('style', 'text-align: left;');
            $(row).children('td').eq(16).attr('style', 'text-align: left;');
            $(row).children('td').eq(17).attr('style', 'text-align: left;');
            $(row).children('td').eq(18).attr('style', 'text-align: left;');
            $(row).children('td').eq(19).attr('style', 'text-align: left;');
            $(row).children('td').eq(20).attr('style', 'text-align: left;');

            // $(row).children('td').eq(6).attr('style', 'text-align: right;');
            // $(row).children('td').eq(6).html("<input type='text' id='amount' name='amount' class='form-control input-sm' style='width: 100%; text-align: right;' value='" + toThousands(data.amount) + "'/>");
        },
        "initComplete": function () {
            // if (1 == contDataSet.length) {
            //     $('#dataTables-projData tr:eq(1)').trigger("click");
            // } else {
            //     billEventDataSet = [];
            //     initBillEventTables();
            //     // $('#dataTables-InvoiceApplyData').DataTable().draw();
            // }
        }
    });

    $('#dataTables-projData tbody').on('click', 'tr', function () {
        var table = $('#dataTables-projData').DataTable();
        if ($(this).hasClass('selected')) {  //当前记录为选中状态
            //取消选择按钮值
            var check = $(this).find("input[name='ch_list']");
            check.prop("checked", false);
            //取消选中
            $(this).removeClass('selected');
            $(this).attr('bgcolor', "#ffffff");

            $('#bu_edit').attr('disabled', "disabled");

        }
        else {
            //取消选择按钮值
            var check = table.$('tr.selected').find("input[name='ch_list']");
            check.prop("checked", false);
            //取消原先选中行
            table.$('tr.selected').attr('bgcolor', "#ffffff");
            table.$('tr.selected').removeClass('selected');
            //选中行生效
            $(this).attr('bgcolor', "#E0FFFF");
            $(this).addClass('selected');
            var check = $(this).find("input[name='ch_list']");
            check.prop("checked", true);

            var table = $('#dataTables-projData').DataTable();
            var data = table.rows(table.$('tr.selected')).data();
            if (data[0].editFlag == 1)
                $('#bu_edit').removeAttr('disabled');
            else
                $('#bu_edit').attr('disabled', "disabled");
        }
    });

    projTable.on('order.dt search.dt', function () {
        projTable.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = "<input type='checkbox' name='ch_list' />";
        }),
            projTable.column(1, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                cell.innerHTML = i + 1;
            });
    }).draw();
}


//--------------------------------------合同查询按钮-------------------------------------------------------------------
// 点击查询按钮
$('#bu_queryData').on('click', function (e) {
    $.ajax({
        url: config.baseurl + "/api/budgetRest/projectInfoList",
        contentType: 'application/x-www-form-urlencoded',
        dataType: "json",
        data: {
            projectSearch: $('#projSearch').val()
        },
        type: "GET",
        beforeSend: function () {
            window.parent.showLoading();
        },
        success: function (data) {
            projDataSet = data.dataSource;
            console.log(projDataSet);

            //初始化记录表
            initProjTables();

            //失效新增、打开按钮
            $('#bu_edit').attr('disabled', "disabled");

        },
        complete: function () {
            window.parent.closeLoading();
        },
        error: function () {
            window.parent.error(ajaxError_loadData);
        }
    });
});

$('#bu_edit').on('click', function (e) {
    checkProj();
});

function checkProj() {
    projData = null;
    var table = $('#dataTables-projData').DataTable();
    var data = table.rows(table.$('tr.selected')).data();
    if (data.length != 1){
        window.parent.warring("请选择一行数据编辑！");
        return;
    }
    $.ajax({
        url : config.baseurl +"/api/budgetRest/projectInfoList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            projectId:       data[0].projectId
        },
        type:"GET",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            projData = data.dataSource[0];

            console.log(projData);
            initAddOrUpdateModal();
        },
        complete:function(){
            window.parent.closeLoading();
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

function initAddOrUpdateModal () {
        //全部清空
        $('#projectName').val("");
        $('#projectId').val("");
        $('#extensioId').val("");
        $('#cExtAttr4').val("");


        // 执行初始化
        $('#projectName').val(projData ? projData.projectNumber + " " +projData.projectName : "");
        $('#projectId').val(projData ? projData.projectId : "");
        $('#extensioId').val(projData ? projData.extensioId : "");
        $('#cExtAttr4').val(projData ? projData.cExtAttr4 : "");

        if (projData){
            if (projData.editFlag == 1)
                $('#bu_save_line').removeAttr('disabled');
            else
                $('#bu_save_line').attr('disabled', "disabled");
        }

        // 显示新增界面
        $('#add_modal').modal('show');
}

// -----------------------------------------------------保存、提交、删除按钮-------------------------------------------
// 保存
$('#bu_save_line').on('click', function (e) {
    checkValue('SAVE');
});

// 检查填写记录是否完全并执行保存
function checkValue(val){
    var projectId = $('#projectId').val();
    var extensioId = $('#extensioId').val();
    var cExtAttr4 = $('#cExtAttr4').val();

    if ("" == cExtAttr4.trim()) {
        window.parent.error("请输入项目总工时！");
        Setfocus('cExtAttr4');
        return;
    }

    saveProjInfo(projectId,cExtAttr4);

};

// 删除程序
function saveProjInfo(projectId,cExtAttr4) {
    $.ajax({
        url : config.baseurl +"/api/budgetRest/upProjInfo",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            projectId : projectId,
            cExtAttr4 : cExtAttr4
        },
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("更新项目总工时成功！")
                $('#add_modal').on('shown.bs.modal', function (e) {}).modal('hide');
                $('#bu_queryData').trigger("click");
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

//设置焦点
function Setfocus(val){
    document.getElementById(val).focus();
};

