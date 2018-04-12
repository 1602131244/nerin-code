/**
 * Created by Administrator on 2017/9/8.
 */
var config = new nerinJsConfig();
var table_sql;
var table_sql2;
var departmentList;
var personList;
$(function () {
    $.ajax({
        url: config.baseurl + "/api/card/testsql",
        data: {
            groupId: "0021"
        },
        contentType: 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function () {

        },
        success: function (data) {
            console.log(data);
            createtable(data);
        },
        complete: function () {
        },
        error: function () {
            window.parent.error("重新加载数据出错！");
            $('#add_modal').on('shown.bs.modal', function (e) {
            }).modal('toggle');
        }
    });
    //部门列表
    $.getJSON(config.baseurl + "/api/card/departmentList", function (data) {
        departmentList = data;
        console.log(departmentList);
        $('#department').append("<option value='-1'>请选择</option>");
        $('#department').append("<option value='-2'>全部</option>");
        departmentList.forEach(function (data) {  //下拉列表，
            $('#department').append("<option value='" + data.groupId + "'>" + data.groupName + "</option>");
        });
    });
    $('#person').append("<option value='-1'>请选择</option>");

    $("div[name=startDate]").datetimepicker({
        format: 'YYYY-MM-DD',
        debug: false
    });
    $("div[name=endDate]").datetimepicker({
        format: 'YYYY-MM-DD',
        debug: false
    });
    querycost();
})

//查询
function querycost() {
    $('#bu_query').on('click', function () {
        console.log($("#department  option:selected").val());
        console.log($("#person  option:selected").val());
        $.ajax({
            url: config.baseurl + "/api/card/testsql",
            data: {
                groupId: $("#department  option:selected").val(),
                personId:$("#person  option:selected").val()
            },
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",
            type: "GET",
            beforeSend: function () {

            },
            success: function (data) {
                console.log(data);
                createtable(data);
            },
            complete: function () {
            },
            error: function () {
                window.parent.error("重新加载数据出错！");
                $('#add_modal').on('shown.bs.modal', function (e) {
                }).modal('toggle');
            }
        });
    })

}
function queryPerson(select) {
    $('#person').empty();
    if(select.value!="-1") {
        $.ajax({
            url: config.baseurl + "/api/card/person",
            data: {
                departmentId: select.value
            },
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",
            type: "GET",
            beforeSend: function () {
            },
            success: function (data) {
                console.log(data)
                personLis=data;
                $('#person').append("<option value='-1'>全部</option>");
                personLis.forEach(function (data) {  //下拉列表，如果是最大版本，则默认选中
                    $('#person').append("<option value='" + data.id + "'>" + data.name + "</option>");
                });
            },
            complete: function () {
            },
            error: function (XMLHttpRequest) {
                window.parent.error(XMLHttpRequest);
            }
        });
    }
}
function createtable(Data) {
    $('#dataTables-listAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables_listAll"></table>');
    table_sql = $('#dataTables_listAll').DataTable({
        "processing": true,
        "data": Data,
        "searching": false,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "exportDataType": "all",
        "showExport": true,
        "dom": 'Bfrtip',
        "buttons": [
            'copy', 'csv', 'excel', 'pdf', 'print'
        ],
        "columns": [
            {"title": "序号", "data": null},
          //  {"title": "id", "data": "base_perid"},
            {"title": "人员姓名", "data": "base_pername"},
            {"title": "人员工号", "data": "base_perno"},
            {"title": "部门", "data": "base_groupname"}

        ],

        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "order": [[1, 'asc']],

    });

    table_sql.on('order.dt search.dt', function () {
        table_sql.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    }).draw();

    $('#dataTables-listAll2').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables_listAll2" hidden="true"></table>');
    table_sql2 = $('#dataTables_listAll2').DataTable({
        "processing": true,
        "data": Data,
        "paging":false,
        "searching": false,
        "zeroRecords":"",
        "info":false,
        "language": {
            "url": "/scripts/common/Chinese.json"

        },
        "columns": [
            {"title": "序号", "data": null},
           // {"title": "id", "data": "base_perid"},
            {"title": "人员姓名", "data": "base_pername"},
            {"title": "人员工号", "data": "base_perno"},
            {"title": "部门", "data": "base_groupname"}

        ],

        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "order": [[1, 'asc']],

    });

    table_sql2.on('order.dt search.dt', function () {
        table_sql2.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    }).draw();
}
$("#bu_qu").click(function () {


    method2("dataTables_listAll2");
});

function method2(tableid)
{

    var curTbl = document.getElementById(tableid);
    var oXL = new ActiveXObject("Excel.Application");
    var oWB = oXL.Workbooks.Add();
    var oSheet = oWB.ActiveSheet;
    var Lenr = curTbl.rows.length;
    oSheet.Cells(1, 1).value="食堂报表";
    oSheet.Range(oSheet.Cells(1, 1),oSheet.Cells(1,5)).Select(); //选择该列
    oXL.Selection.HorizontalAlignment = 3;                          //居中
    oXL.Selection.MergeCells = true; //合并
    oXL.Selection.Font.Size=14;
    oXL.Selection.Font.Name = "黑体";
    for (i = 0; i < Lenr; i++)   //行
    {        var Lenc = curTbl.rows(i).cells.length;
        for (j = 0; j < Lenc; j++)//列
        {
            oSheet.Cells(i + 2, j + 1).NumberFormatLocal = "@";
            oSheet.Cells(i + 2, j + 1).value = curTbl.rows(i).cells(j).innerText;

        }

    }
    oSheet.Columns.AutoFit();
    oXL.Visible = true;//设置为可见
    oXL.UserControl = true;//设置为用户控制
    oSheet.Application.Quit(); //关闭当前进程
}