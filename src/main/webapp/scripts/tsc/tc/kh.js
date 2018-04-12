
var dataSetGys = [];
function initKhQueryBtn() {
    $('#bu_queryKhData').off("click");
    $('#bu_queryKhData').on("click", function(){
        $.ajax({
            url : config.baseurl +"/api/tsc/tc/tcEbsList",
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
                queryKhList();
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


var t_kh;
function queryKhList() {
    $('#dataTables-khAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-kh"></table>');

    t_kh = $('#dataTables-kh').DataTable({
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
            {"title": "客户编号", "data": "partyNumber"},
            {"title": "客户名称", "data": "partyName"},
            {"title": "客户简称", "data": "organizationName"},
            {"title": "客户曾用名", "data": "knownAs"},
            {"title": "客户属性", "data": "customerCategoryCode"}
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
            "width": 80
        },{
            "width": 300,
            "targets": 2
            // "render": function (data, type, full, meta) {
            //     return "<a name='a_linkTask' href='#1'>" + data + "</a>";
            // }
        },{
            "width": 200,
            "targets": 3
        },{
            "width": 80,
            "targets": 5
        }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "initComplete": function () {
            $('#dataTables-kh_wrapper').css("cssText", "margin-top: -5px;");
            $('#dataTables-kh_length').insertBefore($('#dataTables-kh_info'));
            $('#dataTables-kh_length').addClass("col-sm-4");
            $('#dataTables-kh_length').css("paddingLeft", "0px");
            $('#dataTables-kh_length').css("paddingTop", "5px");
            $('#dataTables-kh_length').css("maxWidth", "130px");
        }
    });

    t_kh.on( 'order.dt search.dt', function () {
        t_kh.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );
    } ).draw();

}

$('#bu_queryKhData').click(function(){
    $.ajax({
        url : config.baseurl +"/api/taskRest/taskHeaderList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: $('#selectFrom').serialize(),
        type:"POST",   //请求方式
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

