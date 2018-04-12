
function initGysQueryBtn() {
    $('#bu_queryGysData').off("click");
    $('#bu_queryGysData').on("click", function(){
        $.ajax({
            url : config.baseurl +"/api/tsc/ts/tsEbsList",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: $('#selectFrom_1').serialize(),
            type:"GET",   //请求方式
            beforeSend:function(){
                window.parent.showLoading();
            },
            success:function(data){
                dataSetGys = data.dataSource;
                console.log(dataSetGys);
                queryGysList();
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


var dataSetGys = [];

var t_gys;
function queryGysList() {
    $('#dataTables-gysAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-gys"></table>');

    t_gys = $('#dataTables-gys').DataTable({
        "searching": false,
        "processing": true,
        "data": dataSetGys,
        "scrollY": "500px",
        "scrollCollapse": true,
        "pagingType": "full_numbers",
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "供应商编号", "data": "vendorNumber"},
            {"title": "供应商名称", "data": "vendorName"},
            {"title": "供应商属性", "data": "vendorTypeCode"},
            {"title": "供应商性质", "data": "supplierNature"},
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
            "width": 100
        },{
            "width": 200,
            "targets": 2
            // "render": function (data, type, full, meta) {
            //     return "<a name='a_linkTask' href='#1'>" + data + "</a>";
            // }
        },{
            "width": 80,
            "targets": 3
        },{
            "width": 80,
            "targets": 4
        }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "initComplete": function () {
            $('#dataTables-gys_wrapper').css("cssText", "margin-top: -5px;");
            $('#dataTables-gys_length').insertBefore($('#dataTables-gys_info'));
            $('#dataTables-gys_length').addClass("col-sm-4");
            $('#dataTables-gys_length').css("paddingLeft", "0px");
            $('#dataTables-gys_length').css("paddingTop", "5px");
            $('#dataTables-gys_length').css("maxWidth", "130px");
        }
    });

    t_gys.on( 'order.dt search.dt', function () {
        t_gys.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );
    } ).draw();

}


