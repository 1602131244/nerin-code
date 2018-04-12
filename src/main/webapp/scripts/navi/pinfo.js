/**
 * Created by Administrator on 2016/8/7.
 */
var dataSet1=[];
var dataSet_m=[];
var   l_userId=1613;
$(function () {
    getprojectInfo();
    getprojectperson();
    getprojectmhours();
    getprojectshours();

    function getprojectInfo() {
        $.ajax({     //异步，调用后台get方法
            url: config.baseurl + "/api/Pinfo/info", //调用地址，不包含参数
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

                var datas = data.dataSource;
                console.log(datas);
                $('#pinfoProjectNumber').html(datas[0].projectNumber);
                $('#pinfoProjectName').html(datas[0].projectName);
                $('#pinfoProjectManager').html(datas[0].manager);
                $('#pinfoProjectLongName').html(datas[0].projectLongName);
                $('#pinfoProjectClass').html(datas[0].projectClass);
                $('#pinfoOrgNmae').html(datas[0].orgName);
                $('#pinfoExeName').html(datas[0].exeName);
                $('#pinfoProjStartDate').html(datas[0].projStartDate);
                $('#pinfoProjEndDate').html(datas[0].projEndDate);
                $('#pinfoStratDate').html(datas[0].startDate);
                $('#pinfoCompletionDate').html(datas[0].completionDate);
                $('#pinfoCustomer').html(datas[0].customer);
                $('#pinfoprojectStatus').html(datas[0].projectStatus);
                $('#pinfoOverdue').html(datas[0].overdue);
            },
            complete: function () {
            },
            error: function () {
                window.parent.error(ajaxError_sys);
            }
        });
    }
    var t3;

    function getprojectperson() {
        $.ajax({     //异步，调用后台get方法
            url: config.baseurl + "/api/Pinfo/personInfo", //调用地址，不包含参数
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
                dataSet1=data.dataSource;
                console.log(dataSet1);
                createpersonInfo();
            },
            complete: function () {
            },
            error: function () {
                window.parent.error(ajaxError_sys);
            }
        });
    }
    //创建人员列表
    function createpersonInfo() {
        $('#dataTables-projperson').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-proj-line"></table>');
        t3 = $('#dataTables-proj-line').DataTable({
            "processing": true,
            "data": dataSet1,
          //  "scrollX": true,
            "pagingType": "full_numbers", //简单模式，只有上下页
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "序号", "data": null},
                {"title": "人员", "data": "fullName"},
                {"title": "专业", "data": "assignment_name"},
                {"title": "角色", "data": "specaility_name"},
                {"title": "办公电话", "data": "work_phone"},
                {"title": "移动电话", "data": "m_phone"},
                {"title": "电子邮箱", "data": "email_address"}
            ],
            "columnDefs": [{
                "targets": 1, //人员
                "width": 50

            }, {
                "searchable": false,
                "width": 15,
                "targets": 0 //排序

            }, {
                "width": 50,
                "targets": 2//专业
            }, {
                "width": 100,
                "targets": 3//角色
            }, {
                "width": 150,
                "targets": 4 //办公电话
            }, {
                "width": 150,
                "targets": 5 //移动电话
            }, {
                "width": 70,
                "targets": 6//电子邮箱
            }
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
            },
            "order": [[2, 'asc']],
        });
        t3.on('order.dt search.dt', function () {
                t3.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                    cell.innerHTML = i + 1;
                });
            }).draw();
    }
    var dataSet_m=[];
    var dataSet_s=[];
    function getprojectmhours() {
        $.ajax({     //异步，调用后台get方法
            url: config.baseurl + "/api/Pinfo/Mhours", //调用地址，不包含参数
            contentType: "application/json",
            dataType: "json",
            async: true,
            data: {
                projectId: l_projectId,  //传入参数，依次填写
                userId:l_userId,
                ismanager:1
            },
            type: "GET",  //调用方法类型
            beforeSend: function () {

            },
            success: function (data) {
                dataSet_m=data.dataSource;
                $('#pinfototal').html(data.total);

                createmhours();
            },
            complete: function () {
            },
            error: function () {
                window.parent.error(ajaxError_sys);
            }
        });
    }
    var t4;
    //创建工时列表
    function createmhours() {
        $('#dataTables-projmhours').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-mhours-line"></table>');
        t4 = $('#dataTables-mhours-line').DataTable({
            "processing": true,
            "data": dataSet_m,
            "pagingType": "simple", //简单模式，只有上下页
            "searching": false,
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "序号", "data": null},
                {"title": "人员", "data": "fullName"},
                {"title": "工时数", "data": "measure"}

            ],
            "columnDefs": [{
                "targets": 1, //人员
                "width": 50

            }, {
                "searchable": false,
                "width": 15,
                "targets": 0 //排序

            }, {
                "width": 50,
                "targets": 2//工时
            }
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
            },
            "order": [[1, 'asc']],
            // "initComplete": function () { //去掉标准表格多余部分
            //     $('#dataTables-mhours-line_length').remove();
            //     $('#dataTables-mhours-line_info').remove();
            //     $('#dataTables-mhours-line_previous').remove();
            //     $('#dataTables-mhours-line_next').remove();
            //
            // }
        });

        t4.on('order.dt search.dt', function () {
            t4.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                cell.innerHTML = i + 1;
            });
        }).draw();
    }
  //项目施工服务工时
    function getprojectshours() {
        $.ajax({     //异步，调用后台get方法
            url: config.baseurl + "/api/Pinfo/Shours", //调用地址，不包含参数
            contentType: "application/json",
            dataType: "json",
            async: true,
            data: {
                projectId: l_projectId,  //传入参数，依次填写
                userId:l_userId,
                ismanager:1
            },
            type: "GET",  //调用方法类型
            beforeSend: function () {

            },
            success: function (data) {
                dataSet_s=data.dataSource;

                createshours();
            },
            complete: function () {
            },
            error: function () {
                window.parent.error(ajaxError_sys);
            }
        });
    }
    var t_s;
    //创建工时列表
    function createshours() {
        $('#dataTables-projshours').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-shours-line"></table>');
        t_s = $('#dataTables-shours-line').DataTable({
            "processing": true,
            "data": dataSet_s,
            "pagingType": "simple", //简单模式，只有上下页
            "searching": false,
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "序号", "data": null},
                {"title": "人员", "data": "fullName"},
                {"title": "工时数", "data": "measure"}

            ],
            "columnDefs": [{
                "targets": 1, //人员
                "width": 50

            }, {
                "searchable": false,
                "width": 15,
                "targets": 0 //排序

            }, {
                "width": 50,
                "targets": 2//工时
            }
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
            },
            "order": [[1, 'asc']],
            // "initComplete": function () { //去掉标准表格多余部分
            //     $('#dataTables-mhours-line_length').remove();
            //     $('#dataTables-mhours-line_info').remove();
            //     $('#dataTables-mhours-line_previous').remove();
            //     $('#dataTables-mhours-line_next').remove();
            //
            // }
        });

        t_s.on('order.dt search.dt', function () {
            t_s.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
                cell.innerHTML = i + 1;
            });
        }).draw();
    }
})