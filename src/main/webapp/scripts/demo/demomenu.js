/**
 * Created by Zach on 16/5/26.
 */
//表示页面加载完成后再执行
$(function () {
    console.log("page loaded");

    //初始化table数据
    initTables();


    //创建保存按钮click监听
    $("#saveDemoMenu").on("click",function () {

        // console.log("点击保存按钮!");
        // console.log($("#demoMenuForm").serializeJSON());

        //将form表单的数据收集并转换成json字符串
        var demomenujson= JSON.stringify($("#demoMenuForm").serializeJSON());

        //新增菜单rest api url
        var addurl = "http://localhost:8080/api/demo/menu/add";

        //将数据POST到addurl,并提供响应完成后所需要执行的方法
        $.post(addurl,demomenujson,function (data,textStatus,jqXHR) {
            console.log(data)
            console.log(textStatus)
            console.log(jqXHR)
            if("success" == textStatus){
                window.parent.success("保存成功!");
                //刷新table
                initTables();
            }else {
                window.parent.error("保存失败!");
            }
        });
    });

    //监控表格行选择中事件,并改变选择中行的css
    $("#dataTablesArea").on("click",'tr',function () {
        // console.log($(this))
        $(this).toggleClass("selected");
    });

    //删除按钮监听事件,弹出删除确认框
    $("#deleteMenu").on("click",function () {
        window.parent.showConfirm(function () {

            var datatableRows = $('#menutable').DataTable();
            console.log(datatableRows.rows('.selected').data());
            //获取选择中的行
            var selectedRows = datatableRows.rows('.selected').data();
            //如果选择中的行数大于0,则要执行删除方法
            if(selectedRows.length>0){
                var menuIds = new Array();
                //循环选择中的行
                $(selectedRows).each(function (index) {
                    console.log(selectedRows[index][0]);
                    menuIds.push(selectedRows[index][0]);
                })
                
                console.log(menuIds);
                //删除操作
                var deleteUrl = "http://localhost:8080/api/demo/menu/delete/"+menuIds.toString();
                $.ajax({
                    url: deleteUrl,
                    type: 'DELETE',
                    success: function (data) {
                        console.log(data);
                        //刷新table
                        initTables();
                    }
                });

            }

        });
    });

    function initTables() {
        //向服务端获取菜单数据
        var getDemoMenuUrl="http://localhost:8080/api/demo/menu/lsit";
        $.get(getDemoMenuUrl,function (data) {

            var tablesdata = new Array();
            //循环data对象
            $(data).each(function (index) {
                console.log(data[index]['name']);

                tablesdata.push(
                    [
                        data[index]['id'],
                        data[index]['name'],
                        data[index]['description'],
                        data[index]['url']
                    ]
                );
            });

            console.log(tablesdata);

            //创建html table元素
            $('#dataTablesArea')
                .html( '<table cellpadding="0" cellspacing="0" border="0" class="table table-bordered" id="menutable"></table>' );

            //给table赋数据
            $('#menutable').dataTable( {
                "data": tablesdata,
                "columns": [
                    { "title": "ID" },
                    { "title": "名称" },
                    { "title": "说明" },
                    { "title": "URL" }
                ]
            } );

        });

    }

});