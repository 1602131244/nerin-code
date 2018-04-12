
var dataSetGys1 = [];
function initlxrQueryBtn() {
    $('#bu_querylxrData').off("click");
    $('#bu_querylxrData').on("click", function(){
        $.ajax({
            url : config.baseurl +"/api/tsc/tc/tcLxr",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: $('#selectFrom_2').serialize(),
            type:"GET",   //请求方式
            beforeSend:function(){
                window.parent.showLoading();
            },
            success:function(data){
                dataSetGys1 = data.dataSource;
                console.log(dataSetGys1);
                querylxrList();
            },
            complete:function(){
                window.parent.closeLoading();
            },
            error:function(){
                window.parent.error(ajaxError_loadData);
            }
        });
    });
}


var t_lxr;
function querylxrList() {
    $('#dataTables-lxrAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-lxr"></table>');

    t_lxr = $('#dataTables-lxr').DataTable({
        "searching": false,
        "processing": true,
        "data": dataSetGys1,
        "scrollY": "500px",
        "scrollCollapse": true,
        "pagingType": "full_numbers",
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "联系人", "data": "personName"},
            {"title": "客户名称", "data": "customerName"},
            {"title": "电子邮箱", "data": "email"},
            {"title": "联系方式", "data": "phone"},
            {"title": "客户地址", "data": "address"},
            {"title": "行业分类", "data": "custI"}
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
            "width": 60
        },{
            "width": 200,
            "targets": 2
            // "render": function (data, type, full, meta) {
            //     return "<a name='a_linkTask' href='#1'>" + data + "</a>";
            // }
        },{
            "width": 120,
            "targets": 3
        },{
            "width": 120,
            "targets": 4
        },
            {
                "width": 200,
                "targets": 5
            },
    {
        "width": 100,
        "targets": 6
    }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "initComplete": function () {
            $('#dataTables-lxr_wrapper').css("cssText", "margin-top: -5px;");
            $('#dataTables-lxr_length').insertBefore($('#dataTables-lxr_info'));
            $('#dataTables-lxr_length').addClass("col-sm-4");
            $('#dataTables-lxr_length').css("paddingLeft", "0px");
            $('#dataTables-lxr_length').css("paddingTop", "5px");
            $('#dataTables-lxr_length').css("maxWidth", "130px");
        }
    });

    t_lxr.on( 'order.dt search.dt', function () {
        t_lxr.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );
    } ).draw();

}


