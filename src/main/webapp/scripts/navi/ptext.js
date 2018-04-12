/**
 * Created by Administrator on 2016/8/8.
 */
var dataSetPtext=[];
var text;
$(function () {
    getptext();
    function getptext() {
        $.ajax({     //异步，调用后台get方法
            url: config.baseurl + "/api/Ptext/text", //调用地址，不包含参数
            contentType: "application/json",
            dataType: "json",
            async: true,
            data: {
                projectId: l_projectId  //传入参数，依次填写
            },
            type: "GET",  //调用方法类型
            beforeSend: function () {

            },
            success: function (data) {
                dataSetPtext=data.dataSource;
                console.log(dataSetPtext);
                createtext();
            },
            complete: function () {
            },
            error: function () {
                window.parent.error(ajaxError_sys);
            }
        });
    }


    //创建文本列表
    function createtext() {
        $('#dataTables-projtext').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-projtext-line"></table>');
        text = $('#dataTables-projtext-line').DataTable({
            "processing": true,
            "data": dataSetPtext,
            "pagingType": "simple", //简单模式，只有上下页
            "searching": false,
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "序号", "data": null},
                {"title": "文本名称", "data": "taskName"},
                {"title": "创建人", "data": "creater"},
                {"title": "章节完成情况", "data": "taskProgress"},
                {"title": "状态", "data": "status"}

            ],
            "columnDefs": [{
                "searchable": false,
                "orderable": false,
                "width": 15,
                "targets": 0 //排序

            }, {
                "targets": 4,//状态
                "render": function (data, type, full, meta) {
                    return "<a name='ptext_oa' href='#1'>" + data + "</a>";
                }
            }
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
            },
            "order": [[1, 'asc']],
            "initComplete": function () { //去掉标准表格多余部分
                $('#dataTables-projtext-line_length').remove();
                $('#dataTables-projtext-line_info').remove();
                $('#dataTables-projtext-line_previous').remove();
                $('#dataTables-projtext-line_next').remove();

            }
        });
        $('#dataTables-projtext-line').on('click', 'a[name=ptext_oa]', function () {
            var userTable = $('#dataTables-projtext-line').DataTable();
            var data = userTable.rows( $(this).parents('tr') ).data();
            query_text_oa(data[0].taskHeaderId,data[0].taskType);
        });
        text.on('order.dt search.dt', function () {
            text.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                cell.innerHTML = i + 1;
            });
        }).draw();
    }

    //弹出oa查询
    function query_text_oa(taskHeaderId,taskType) {
        $.ajax({
            url : config.baseurl +"/api/Ptext/textoa",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                taskHeaderId: 2957,
                taskType:"GLJH"
            },
            type:"GET",
            beforeSend:function(){window.parent.showLoading();},
            success:function(data){
               // console.log(data);
                modalOaDataSet = data;
                console.log(modalOaDataSet);
                $('#oaModal').modal("show").css("top", "13%");
            },
            complete:function(){window.parent.closeLoading();},
            error:function(){
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    
})