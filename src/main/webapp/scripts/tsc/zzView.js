var config = new nerinJsConfig();
var dataSet = [];
var vendorId;
var customerId;
var ebsId;
var businessType;
$(function () {
    var id = $.query.get("id");
    ebsId = $.query.get("ebsId");
    businessType = $.query.get("type");
    if ("tc" == businessType) {
        customerId = id;
        loadKh_zz();
    } else if ("ts" == businessType) {
        vendorId = id;
        loadGys_zz();
    }
});

function loadGys_zz() {
    $.ajax({
        url : config.baseurl +"/api/tsc/ts/attachmentContact",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            vendorId: vendorId,
            ebsId: ebsId
        },
        type:"GET",   //请求方式
        beforeSend:function(){
        },
        success:function(data){
            dataSet = data;
            queryZzList();
        },
        complete:function(){
        },
        error:function(){
            alert(ajaxError_loadData);
        }
    });
}

function loadKh_zz() {
    $.ajax({
        url : config.baseurl +"/api/tsc/tc/attachmentContact",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            customerId: customerId,
            ebsId: ebsId
        },
        type:"GET",   //请求方式
        beforeSend:function(){
        },
        success:function(data){
            dataSet = data;
            queryZzList();
        },
        complete:function(){
        },
        error:function(){
            alert(ajaxError_loadData);
        }
    });
}

function setCheckZzAll() {
    var isChecked = $('#ch_zzAll').prop("checked");
    $("input[name='ch_zz']").prop("checked", isChecked);
}

var t_zz;
function queryZzList() {
    $('#dataTables-zzAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-zz"></table>');

    t_zz = $('#dataTables-zz').DataTable({
        "searching": false,
        "processing": true,
        "data": dataSet,
        "scrollY": "300px",
        "scrollCollapse": true,
        "paging": false,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "<input type='checkbox' name='ch_zzAll' id='ch_zzAll' onclick='javascript:setCheckZzAll();' />", "data": null, "className": "style_column"},
            {"title": "名称", "data": "fileName"},
            {"title": "上传时间", "data": "creationDate"},
            {"title": "下载", "data": "fileUrl"}
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "targets": 0,
            "width": 32
        },{
            "width": 160,
            "targets": 3,
            "render": function (data, type, full, meta) {
                if ("tc" == businessType) {
                    return "<a name='a_linkTemplate' target='_blank' href='/api/tsc/tc/downloadZzFile?customerId=" + full.customerId + "&fileName=" + encodeURIComponent(full.fileUrl) + "'>下载</a>";
                } else if ("ts" == businessType) {
                    return "<a name='a_linkTemplate' target='_blank' href='/api/tsc/ts/downloadZzFile?vendorId=" + full.vendorId + "&fileName=" + encodeURIComponent(full.fileUrl) + "'>下载</a>";
                }

            }
        }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "initComplete": function () {
            $('#dataTables-zz_wrapper').css("cssText", "margin-top: -5px;");
        }
    });

    t_zz.on( 'order.dt search.dt', function () {
        t_zz.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = "<input type='checkbox' name='ch_zz' />";
        } );
    } ).draw();

}