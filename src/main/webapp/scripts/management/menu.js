/**
 * Created by Zach on 16/5/23.
 */
$(function () {
    var config = new nerinJsConfig();
    //初始化表格
    initDataTable();

    /**
     * 当显示弹出窗口时
     */
    $('#addMenu').on('show.bs.modal',function(e){
        $('#addMenuForm')[0].reset();


        if("addMenuButton"== e.relatedTarget.id){
            $('#id').val("");
            initParentMenu("");
        }else{
            var tablesRows = $('#menu-table').DataTable();
            if(tablesRows.rows('.edit').data().length==1){
                var editMenu = tablesRows.rows('.edit').data()[0];
                $('#addMenuForm').formFieldValues({
                    id:editMenu[0],
                    name:editMenu[1],
                    description:editMenu[2],
                    type:editMenu[3],
                    url:editMenu[4],
                    icon:editMenu[5],
                    parentId:editMenu[7],
                    outsideUrl: editMenu[8],
                    orderNo: editMenu[9]
                });
                initParentMenu(editMenu[7]);
            }
        }
    });

    /**
     * 当关闭弹出窗口时
     */
    $('#addMenu').on('hide.bs.modal',function(e){
        $('#menu-table-list .edit').toggleClass('edit');
        $('#menu-table-list .selected').toggleClass('selected');
    });

    /**
     * 新增或者修改菜单调用方法
     */
    $('#saveMenu').click(function () {

        var jsonString = JSON.stringify($('#addMenuForm').serializeJSON());
        $.ajax({
            url : config.baseurl +"/api/menu/add",
            type : 'POST',
            data : jsonString,//注意：需要使用字符串传输
            success: function (data) {
                window.parent.success("创建菜单成功!");
                $('#addMenu').modal('toggle');
                initDataTable();
            },
            error: function (data) {
                window.parent.error("创建菜单失败!");
            }
        });
    });

    /**
     * 删除菜单
     */
    $('#deleteSelection').click(function () {
        window.parent.showConfirm(function deleteMenu() {

            var tablesRows = $('#menu-table').DataTable();
            // alert(tablesRows.rows('.selected').data().length + ' row(s) selected');
            if(tablesRows.rows('.selected').data().length>0){
                var menuIds = new Array();
                $(tablesRows.rows('.selected').data()).each(function (index) {
                    menuIds.push($(this)[0]);
                });
                $.ajax({
                    url : config.baseurl +"/api/menus/delete/"+menuIds.toString(),
                    type : 'DELETE',
                    success: function (data) {
                        if(data.code==0){
                            window.parent.success(data.message);
                        }else {
                            window.parent.error(data.message);
                        }
                        initDataTable();
                    },
                    error: function (data) {
                        window.parent.error("删除菜单出现问题");
                        initDataTable();
                    }
                });
            }
        });
    });


    $('#menu-table-list').on('click', 'tr', function () {
        $(this).toggleClass('selected');
    });

    $('#menu-table-list').on('click', '.editButton', function () {
        $(this).parent().parent().toggleClass('edit');
        //$(this).toggleClass('edit');
    });

    /**
     * 初始化表格数据
     */
    function initDataTable() {
        $.getJSON(config.baseurl + "/api/menus/list?size="+config.searchResultSize, function (data) {
            var dataSet = new Array();
            $(data).each(function (index) {
                dataSet.push([data[index]['id'], data[index]['name'], data[index]['description'], data[index]['type'], data[index]['url'],data[index]['icon'],data[index]['parentName'],data[index]['parentId'],data[index].outsideUrl,data[index].orderNo])
            });
            $('#menu-table-list').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="menu-table"></table>');

            $('#menu-table').dataTable({
                "data": dataSet,
                "columns": [
                    {"title": "ID"},
                    {"title": "名称"},
                    {"title": "说明"},
                    {"title": "类型"},
                    {"title": "链接"},
                    {"title": "图标"},
                    {"title": "所属菜单"},
                    {"title": "所属菜单ID"},
                    {"title": "是否外部链接"},
                    {"title": "排序号"},
                    {"title": "操作"}
                ],
                "columnDefs": [
                    {
                        "targets": 0,
                        "visible": false
                    },
                    {
                        "targets": 2,
                        "render": function (data, type, full, meta) {
                            return '<a target="_blank" href="/pages/management/viewMenu.html?id=' + full[0] + '">' + data + '</a>';
                        }
                    },
                    {
                        "targets": 4,
                        "render": function (data, type, full, meta) {
                            return '<a target="_blank" href="' + data + '">' + data + '</a>';
                        }
                    },
                    {
                        "targets": 5,
                        "render": function (data, type, full, meta) {
                            return '<span class="fa '+data+' fa-fw" style="width: 100%">' + data + '</a>';
                        }
                    },
                    //{
                    //    "targets": 6,
                    //    "render": function (data, type, full, meta) {
                    //        return '<button type="button" class="btn btn-default btn-xs editButton"  data-toggle="modal" data-target="#addMenu" id="m_'+full[0]+'"> <i class="fa fa-edit"></i>编辑</button>';
                    //    }
                    //},
                    {
                        "targets": 7,
                        "visible": false
                    },{
                        "targets": 8,
                        "visible": false
                    },{
                        "targets": 9,
                        "visible": false
                    },{
                        "targets": 10,
                        "render": function (data, type, full, meta) {
                            return '<button type="button" class="btn btn-default btn-xs editButton"  data-toggle="modal" data-target="#addMenu" id="m_'+full[0]+'"> <i class="fa fa-edit"></i>编辑</button>';
                        }
                    }]
            });
        });
    };

    /**
     * 初始化父菜单下拉列表
     */
    function initParentMenu(parentId){
        $.get(config.baseurl+'/api/menus/list',function (data) {
            $("#selectParentMenu").html("");
            $(data).each(function(index){
                $("#selectParentMenu").append($('<option value="'+data[index].id+'">'+data[index].name+'</option>'));
                $(".selectpicker").selectpicker('refresh');
                if(""!=parentId){
                    $(".selectpicker").selectpicker('val',parentId);
                }

            });
        });
    }
});

