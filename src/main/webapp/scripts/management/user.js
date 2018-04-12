/**
 * Created by Zach on 16/5/23.
 */
$(function () {

    $('#addUserForm').bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            login: {
                validators: {
                    notEmpty: {
                        message: '登录名不能为空'
                    }
                }
            }
        }
    });

    var addOrUpdateTips = "";

    function updateUserDialog(title, data) {
        addOrUpdateTips = "更新用户成功";
        $('#addUserLabel').text(title);
        $('#id').val(data.id);
        $('#login').val(data.login);
        $('#firstName').val(data.firstName);
        $('#lastName').val(data.lastName);
        $('#email').val(data.email);
    }

    function clearAddUserDialog() {
        addOrUpdateTips = "创建用户成功";
        $('#addUserLabel').text("新增用户");
        $('#id').val("");
        $('#login').val("");
        $('#firstName').val("");
        $('#lastName').val("");
        $('#email').val("");
    }

    var config = new nerinJsConfig();

    initDataTable();


    function initDataTable() {
        // if ($.fn.dataTable.isDataTable('#dataTables-userList'))
        //     $('#dataTables-userList').DataTable().destroy();

        $.getJSON(config.baseurl + "/api/management/queryUserList", function (data) {
            console.log(data);
            var dataSet = new Array();
            var dataList = data.dataSource;
            $(dataList).each(function (index) {
                dataSet.push([dataList[index]['id'], dataList[index]['login'], dataList[index]['firstName'], dataList[index]['lastName'], dataList[index]['email'], dataList[index]['createdDate'],])
            });

            $('#dataTables-userListAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-userList"></table>');

            $('#dataTables-userList').dataTable({
                "processing": true,
                "data": dataSet,
                // "draw": data.draw,
                // "recordsTotal": data.recordsTotal,
                // "recordsFiltered": data.recordsFiltered,
                "language": {
                    "url": "/scripts/common/Chinese.json"
                },
                "columns": [
                    {"title": "ID"},
                    {"title": "登录名"},
                    {"title": "First Name"},
                    {"title": "Last Name"},
                    {"title": "邮箱"},
                    {"title": "创建日期"},
                    {"title": "操作"}
                ],
                "columnDefs": [
                    {
                        "targets": 0,
                        "visible": false
                    },
                    {
                        "targets": 6,
                        "render": function (dataList, type, full, meta) {
                            return '<button type="button" name="edit" class="btn btn-default btn-xs"><i class="fa fa-edit"></i>编辑</button>&nbsp;'
                                + '<button type="button" name="role" class="btn btn-default btn-xs"><i class="fa fa-edit"></i>角色</button>';
                        }
                    }]
            });

            $('#dataTables-userList tbody').on('click', 'button[name=edit]', function () {
                var userTable = $('#dataTables-userList').DataTable();
                var data = userTable.rows( $(this).parents('tr') ).data();
                queryUser(data[0][0]);
            });

            $('#dataTables-userList tbody').on('click', 'button[name=role]', function () {
                var userTable = $('#dataTables-userList').DataTable();
                var data = userTable.rows( $(this).parents('tr') ).data();
                queryUserRole(data[0][0]);
            });

            $('#dataTables-userList tbody').on('click', 'tr', function () {
                if ( $(this).hasClass('selected') ) {
                    $(this).removeClass('selected');
                }
                else {
                    $('#dataTables-userList').DataTable().$('tr.selected').removeClass('selected');
                    $(this).addClass('selected');
                }
            });

        });
    }

    function queryUserRole(id) {
        $.get(config.baseurl + "/api/management/queryAuthorityAll", function(datas) {
            $('#roleList').html("");
            $(datas).each(function (index) {
                $('#roleList').append(datas[index].name + '：<input type="checkbox" name="role" value="' + datas[index].name + '"/>&nbsp;');
            });
            $('#userRole').on('shown.bs.modal', function (e) {}).modal('toggle');

            $.get(config.baseurl + "/api/management/queryUserInfo/" + id, function(data) {
                $('#rid').val(id);
                var userRoles = data.authorities;
                $(userRoles).each(function (index) {
                    $("input[name='role']").each(function(){
                        if ($(this).val() == userRoles[index].name) {
                            $(this).attr("checked","checked");
                        }
                    });
                });
            });
            
        });
    }

    $('#saveUserRole').click(function(){
        $.ajax({
            url : config.baseurl +"/api/management/editUserRoles",
            type : 'POST',
            data : $('#userRoleForm').serialize(),
            contentType : 'application/x-www-form-urlencoded ',
            success: function (data) {
                window.parent.success("更新角色成功");
                $('#userRole').modal('toggle');
            },
            error: function (data) {
                console.log(data);
                window.parent.error("更新角色失败!");
            }
        });
    });

    function queryUser(id) {
        $.get(config.baseurl + "/api/management/queryUserInfo/" + id, function(data) {
            updateUserDialog("修改用户", data);
            $('#addUser').on('shown.bs.modal', function (e) {}).modal('toggle');
        });
    }

    $('#addUserButton').click(function() {
        clearAddUserDialog();
        $('#addUser').on('shown.bs.modal', function (e) {}).modal('toggle');
    });

    $('#saveUser').click(function(){
        $('#addUserForm').data('bootstrapValidator').validate();
        if (!$('#addUserForm').data('bootstrapValidator').isValid())
            return false;

        var jsonString = JSON.stringify($('#addUserForm').serializeJSON());
        var formData = new FormData($("#addUserForm")[0]);
        $.ajax({
            url : config.baseurl +"/api/management/createOrUpdateUser",
            type : 'POST',
            data : jsonString,
            success: function (data) {
                window.parent.success(addOrUpdateTips);
                clearAddUserDialog();
                $('#addUser').modal('toggle');
                initDataTable();
            },
            error: function (data) {
                console.log(data);
                window.parent.error("操作用户失败!");
            }
        });
    });

    // $('#saveUser').click(function(){
    //     var jsonString = JSON.stringify($('#addUserForm').serializeJSON());
    //     var formData = new FormData($("#addUserForm")[0]);
    //     $.ajax({
    //         url : config.baseurl +"/api/management/createOrUpdateUser",
    //         type : 'POST',
    //         data : formData,
    //         processData : false,
    //         contentType : false,
    //         success: function (data) {
    //             window.parent.success("创建用户成功!");
    //             clearAddUserDialog();
    //             $('#addUser').modal('toggle');
    //             initDataTable();
    //         },
    //         error: function (data) {
    //             console.log(data);
    //             window.parent.error("创建用户失败!");
    //         }
    //     });
    // });

    var deleteListData = function () {
        var userTable = $('#dataTables-userList').DataTable();
        var selectData = userTable.rows('.selected').data();
        var dataSet = new Array();
        $(selectData).each(function (index) {
            dataSet.push([selectData[index][0],]);
        });
        $.ajax({
            url : config.baseurl +"/api/management/deleteUserInfo/" + dataSet,
            type : 'POST',
            success: function (data) {
                window.parent.success("删除成功!");
                initDataTable();
            },
            error: function (data) {
                console.log(data);
                window.parent.error("删除失败!");
            }
        });
    }

    $('#deleteSelection').click(function () {
        var userTable = $('#dataTables-userList').DataTable();
        var selectData = userTable.rows('.selected').data();
        if (0 == selectData.length) {
            window.parent.warring("请选择一条数据");
            return;
        }
        window.parent.showConfirm(deleteListData);
    });

});

