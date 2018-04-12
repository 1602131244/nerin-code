/**
 * Created by Zach on 16/5/23.
 */
var config = new nerinJsConfig();
$(function () {
    queryEbsUserList();
    initMenuTree();
    $('#userNo').focus();
});

$(document).keyup(function(event){
    switch(event.keyCode) {
        case 13:
            $('#bu_queryEbsUserData').trigger("click");
    }
});

$('#bu_queryEbsUserData').click(function(){
    $('#tree').fancytree("getTree").visit(function(node){
        node.setSelected(false);
    });
    $.ajax({
        url : config.baseurl +"/api/menus/ebsUser",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: $('#userFrom').serialize(),
        type:"GET",   //请求方式
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            dataSet_ebsUser = data;
            queryEbsUserList();
        },
        complete:function(){
            window.parent.closeLoading();
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
});

var t_ebsUser;
var dataSet_ebsUser = [];
function queryEbsUserList() {
    $('#dataTables-userAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-users"></table>');

    t_ebsUser = $('#dataTables-users').DataTable({
        "searching": false,
        "processing": true,
        "data": dataSet_ebsUser,
        "scrollX": true,
        // "scrollY": "500px",
        // "scrollCollapse": true,
        "pagingType": "simple",
        "autoWidth": false,
        "pageLength": 20,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "用户编号", "data": "sysAccount"},
            {"title": "用户名称", "data": "lastName"}
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "targets": 1,
            "width": 100
        },{
            "searchable": false,
            "orderable": false,
            "targets": 0,
            "width": 100
        }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            // $(row).children('td').eq(0).attr('style', 'text-align: center;');
            // $(row).children('td').eq(1).attr('style', 'text-align: center;');
        },
        "initComplete": function () {
            $('#dataTables-users_length').remove();
            $('#dataTables-users_wrapper').css("cssText", "margin-top: -5px;");
            // $('#dataTables-users_length').insertBefore($('#dataTables-users_info'));
            // $('#dataTables-users_length').addClass("col-sm-4");
            // $('#dataTables-users_length').css("paddingLeft", "0px");
            // $('#dataTables-users_length').css("paddingTop", "5px");
            // $('#dataTables-users_length').css("maxWidth", "130px");
        }
    });

    $('#dataTables-users tbody').on('click', 'tr', function () {
        if ( $(this).hasClass('user-selected') ) {
            $(this).removeClass('user-selected');
            $('#tree').fancytree("getTree").visit(function(node){
                node.setSelected(false);
            });
        } else {
            $('#dataTables-users').DataTable().$('tr.user-selected').removeClass('user-selected');
            $(this).addClass('user-selected');
            var userTable = $('#dataTables-users').DataTable();
            var data = userTable.rows($(this)).data();
            queryUserMenu(data[0].sysAccount);
        }
    });

}

function queryUserMenu(sysAccount) {
    $.ajax({
        url : config.baseurl +"/api/menus/queryBackMenuId",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            userNo: sysAccount
        },
        type:"GET",   //请求方式
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            var arr = data;
            $('#tree').fancytree("getTree").visit(function(node){
                if (0 <= arr.indexOf(node.data.id))
                    node.setSelected(true);
                else
                    node.setSelected(false);
            });

        },
        complete:function(){
            window.parent.closeLoading();
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });

}

glyph_opts = {
    map: {
        doc: "glyphicon glyphicon-file",
        docOpen: "glyphicon glyphicon-file",
        checkbox: "glyphicon glyphicon-unchecked",
        checkboxSelected: "glyphicon glyphicon-check",
        checkboxUnknown: "glyphicon glyphicon-share",
        dragHelper: "glyphicon glyphicon-play",
        dropMarker: "glyphicon glyphicon-arrow-right",
        error: "glyphicon glyphicon-warning-sign",
        expanderClosed: "glyphicon glyphicon-menu-right",
        expanderLazy: "glyphicon glyphicon-menu-right",  // glyphicon-plus-sign
        expanderOpen: "glyphicon glyphicon-menu-down",  // glyphicon-collapse-down
        folder: "glyphicon glyphicon-folder-close",
        folderOpen: "glyphicon glyphicon-folder-open",
        loading: "glyphicon glyphicon-refresh glyphicon-spin"
    }
};

var menuData = [];
$("#tree").fancytree({
    extensions: ["dnd", "edit", "glyph", "wide"],
    checkbox: true,
    keyboard: false,
    dnd: {
        focusOnClick: false,
        dragStart: function(node, data) { return false; },
        dragEnter: function(node, data) { return false; },
        dragDrop: function(node, data) { data.otherNode.copyTo(node, data.hitMode); }
    },
    glyph: glyph_opts,
    selectMode: 3,
    source: menuData,
    wide: {
        iconWidth: "1em",     // Adjust this if @fancy-icon-width != "16px"
        iconSpacing: "0.5em", // Adjust this if @fancy-icon-spacing != "3px"
        levelOfs: "1.5em"     // Adjust this if ul padding != "16px"
    },
    icon: function(event, data){
        // if( data.node.isFolder() ) {
        //   return "glyphicon glyphicon-book";
        // }
    },
    lazyLoad: function(event, data) {
        data.result = {url: "ajax-sub2.json", debugDelay: 1000};
    }
});

function initMenuTree() {
    $.ajax({
        url : config.baseurl +"/api/menus/allBackMenuForTree",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        type:"GET",   //请求方式
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            menuData = data;
            var tree = $("#tree").fancytree("getTree");
            tree.reload(menuData).done(function(){
                $("#tree").fancytree("getRootNode").visit(function(node){
                    node.setExpanded(true);
                });
            });
        },
        complete:function(){
            window.parent.closeLoading();
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });

}

$('#bu_saveEbsUserData').on('click', function () {
    var userTable = $('#dataTables-users').DataTable();
    var selectData = userTable.rows('.user-selected').data();
    if (0 == selectData.length) {
        window.parent.warring("请选择一个人员！");
        return;
    }
    var arrId = new Array();
    var arr = new Array();
    var o;
    var tree = $("#tree").fancytree("getTree"),
        selNodes = tree.getSelectedNodes();
    selNodes.forEach(function(node) {
        o = new Object();
        o.userNo = selectData[0].sysAccount;
        o.menuId = node.data.id;
        arr.push(o);
        arrId.push(o.menuId);
        var parentNode = node.parent;
        test1 : for (var j = 0; j < 3; j++) {
            if (null != parentNode && undefined != parentNode.data.id) {
                var parentNodeId = parentNode.data.id;
                if (-1 == arrId.indexOf(parentNodeId)) {
                    o = new Object();
                    o.userNo = selectData[0].sysAccount;
                    o.menuId = parentNodeId;
                    arr.push(o);
                    arrId.push(o.menuId);
                }
                parentNode = parentNode.parent;
                continue test1;
            }
        }
    });

    if (0 == selNodes.length) {
        o = new Object();
        o.userNo = selectData[0].sysAccount;
        arr.push(o);
    }

    console.log(arr);

    $.ajax({
        url: config.baseurl +"/api/menus/saveUserMenuId",
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: JSON.stringify(arr),
        type: "POST",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            window.parent.success("保存成功");
        },
        complete: function() {
            window.parent.closeLoading();
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_sys);
        }
    });
});

