$(document).keyup(function(event){
    switch(event.keyCode) {
        case 13:
            $('#bu_queryData').trigger("click");
    }
});

var config = new nerinJsConfig();
var dataSet = [];
$(function () {
    $('#bu_queryData').focus();
    queryList();
});


var t;
function queryList() {
    $('#dataTables-listAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-data"></table>');

    t = $('#dataTables-data').DataTable({
        "pageLength": 20,
        "lengthMenu": [20, 50, 100],
        "searching": false,
        "processing": true,
        "data": dataSet,
        "scrollY": "500px",
        "scrollCollapse": true,
        "pagingType": "full_numbers",
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "客户名称", "data": "partyName"},
            {"title": "客户简称", "data": "organizationName"},
            {"title": "提交人", "data": "personName"},
            {"title": "申请状态", "data": "statusCode"},
            {"title": "客户来源", "data": "customerSourceCode"},
            {"title": "客户属性", "data": "customerCategoryCode"},
            {"title": "是否有附件", "data": "attachmentExists"}

        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "targets": 0,
            "width": 32
        },{
            "searchable": false,
            "orderable": false,
            "targets": 1,
            "width": 200,
            "render": function (data, type, full, meta) {
                if ("APPROVED" == full.status)
                    return "<a name='a_linkTemplate' href='#1'>" + data + "</a>";
                else
                    return data;
            }
        },{
            "width": 200,
            "targets": 2
        },{
            "width": 65,
            "targets": 3
        },{
            "width": 80,
            "targets":4,
            "render": function (data, type, full, meta) {
                if ("APPROVING" == full.status || "APPROVED" == full.status || "REJECTED" == full.status)
                    return "<a name='a_linkTemplate2' href='#1'>" + data + "</a>";
                else
                    return data;
            }
        },{
            "width": 80,
            "targets": 5
        },{
            "width": 80,
            "targets": 6
        },{
            "width": 50,
            "targets": 7
        }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
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

    $('#dataTables-data tbody').on('click', 'a[name=a_linkTemplate]', function () {
        var userTable = $('#dataTables-data').DataTable();
        var data = userTable.rows( $(this).parents('tr') ).data();
        window.open(data[0].attribute2);
    });

    $('#dataTables-data tbody').on('click', 'a[name=a_linkTemplate2]', function () {
        var userTable = $('#dataTables-data').DataTable();
        var data = userTable.rows( $(this).parents('tr') ).data();
        window.open(data[0].attribute3);
    });

    t.on( 'order.dt search.dt', function () {
        t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );
    } ).draw();

}

$('#bu_queryData').click(function(){
    $.ajax({
        url : config.baseurl +"/api/tsc/tc/tcList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: $('#selectFrom').serialize(),
        type:"GET",   //请求方式
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            dataSet = data.dataSource;
            console.log(dataSet);
            queryList();
        },
        complete:function(){
            window.parent.closeLoading();
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
});

