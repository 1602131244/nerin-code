

function initaddOrUpdateBtn() {
    $('#bu_save').off('click');
    $('#bu_save').on('click', function () {
        saveTs();
    });

    $('#bu_addLxr').off('click');
    $('#bu_addLxr').on('click', function () {
        // if (0 == ebsUser.length) {
        //     window.parent.warring("内部对接人数据还未加载完成，请稍后再试");
        //     return;
        // }
        addLxrLine();
    });
    $('#bu_delLxr').off('click');
    $('#bu_delLxr').on('click', function () {
        delLxrLine();
    });
    $('#bu_addZz').off('click');
    $('#bu_addZz').on('click', function () {
        if (null != headerData)
            $('#vendorId').val(headerData.vendorId);
        else
            $('#vendorId').val("");
        if ("" ==  $('#vendorId').val()){
            window.parent.warring("请先保存基本信息后，再上传资质文件！");
            return;
        }
        $('#zzFile').off("change");
        $('#zzFile').on("change", function(){
            var file = this.files[0];
            var fileType = file.type;
            var file_path = getPath($(this).get(0));
            if (0 <= file_path.indexOf("fakepath")) {
                file_path = file_path.substr(file_path.lastIndexOf("\\") + 1, file_path.length);
            }
            $('#span_fileName').html(file_path);
        });
        showUpFile();
    });
    $('#bu_delZz').off('click');
    $('#bu_delZz').on('click', function () {
        delZzLine();
    });

    $('#bu_submit').off("click");
    $('#bu_submit').on('click', function () {
        window.parent.showConfirm(submitHeader, "是否确认提交？");
    });
    $('#bu_approved').off("click");
    $('#bu_approved').on('click', function () {
        auditCode = "APPROVED";
        window.parent.showConfirm(doAudit, "确认[批准]？");
    });
    $('#bu_rejected').off("click");
    $('#bu_rejected').on('click', function () {
        auditCode = "REJECTED";
        window.parent.showConfirm(doAudit, "确认[拒绝]？");
    });
    $('#bu_cc').off("click");
    $('#bu_cc').on('click', function () {
        $('#gysModal').modal("show");
    });

}
function getlength(str) {
    var realLength = 0;
    var len = str.length;
    var charCode = -1;
    for (var i = 0; i < len; i++) {
        charCode = str.charCodeAt(i);
        if (charCode >= 0 && charCode <= 128)
            realLength += 1;
        else realLength += 3;
    }
    return realLength;

}
var auditCode;
var doAudit = function () {
    $.ajax({
        url : config.baseurl +"/api/tsc/ts/auditHeader",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            vendorId: $('#s_vendorId').val(),
            code: auditCode
        },
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                if ("APPROVED" == auditCode)
                    window.parent.success("批准成功！");
                else
                    window.parent.success("拒绝成功！");
                $('#addModal').modal("hide");
                $('#bu_queryData').trigger("click");
            } else {
                window.parent.error(data.returnMsg);
            }
        },
        complete:function(){
            window.parent.closeLoading();
        },
        error:function(){
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

var submitHeader = function() {
    saveTs(1);
}

function doSubmit() {
    if ("" == $('#s_vendorName').val().trim()) {
        window.parent.warring("请填写供应商名称");
        return ;
    }
    if ("" == $('#s_bankName').val().trim()) {
        window.parent.warring("请填写帐户名");
        return ;
    }
    if ("" == $('#s_bankAccountNum').val().trim()) {
        window.parent.warring("请填写银行帐号");
        return ;
    }
    if ("" == $('#s_address').val().trim()) {
        window.parent.warring("请填写地址");
        return ;
    }

    var userTable_lxr = $('#dataTables-lxr').DataTable();
    var data_lxr = userTable_lxr.rows().data();
    var userTable_zz = $('#dataTables-zz').DataTable();
    var data_zz = userTable_zz.rows().data();
    if (0 == data_lxr.length) {
        window.parent.warring("请添加一个联系人");
        return ;
    }
    if (0 == data_zz.length) {
        window.parent.warring("请添加一个资质附件");
        return ;
    }

    $.ajax({
        url : config.baseurl +"/api/tsc/ts/submitHeader",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            vendorId: $('#s_vendorId').val()
        },
        type:"POST",
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("提交成功！")
                $('#addModal').modal("hide");
                // $('#bu_queryData').trigger("click");
            } else {
                window.parent.error(data.returnMsg);
            }
        },
        complete:function(){
            window.parent.closeLoading();
        },
        error:function(){
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

function getPath(obj)
{
    if(obj)
    {
        if (window.navigator.userAgent.indexOf("MSIE")>=1)
        {
            obj.select();
            return document.selection.createRange().text;
        } else if(window.navigator.userAgent.indexOf("Firefox")>=1) {
            if(obj.files)
            {
                return obj.files.item(0).getAsDataURL();
            }
            return obj.value;
        }
        return obj.value;
    }
}

function showUpFile() {
    $('#uploadModal').modal('show');
    $('#zzFile').val("");
    $('.progress-bar').css("width", "0%");
    $('#span_fileName').html("");
    $('#do_uploadFile').off("click");
    $('#do_uploadFile').on('click', function () {
        upFile();
    });
}

function upFile(){
    if ("" == $('#zzFile').val()) {
        window.parent.warring("请选择需要上传的文件！");
        return;
    }
    var formData = new FormData($('#templateUploadForm')[0]);
    $.ajax({
        url: config.baseurl +"/api/tsc/ts/uploadZzFile",  //server script to process data
        type: 'POST',
        xhr: function() {  // custom xhr
            myXhr = $.ajaxSettings.xhr();
            if(myXhr.upload){ // check if upload property exists
                myXhr.upload.addEventListener('progress',progressHandlingFunction, false); // for handling the progress of the upload
            }
            return myXhr;
        },
        //Ajax事件
        beforeSend: function () {

        },
        success: function (data) {
            window.parent.success("上传成功！");
            $('#uploadModal').modal('hide');
            $(data).each(function (i) {
                dataSetzz.push(data[i]);
            });
            queryZzList(1);
        },
        error: function (t) {
            console.log(t);
            window.parent.error("上传失败，请稍后再试！")
        },
        // Form数据
        data: formData,
        //Options to tell JQuery not to process data or worry about content-type
        cache: false,
        contentType: false,
        processData: false
    });
}

function progressHandlingFunction(e){
    if(e.lengthComputable){
        $('.progress-bar').css("width", e.loaded + "%");
    }
}

function addLxrLine() {
    var table = $('#dataTables-lxr').DataTable();
    var o = new Object();
    o.contactId = "";
    o.personName = "";
    o.personTitle = "";
    o.department = "";
    o.mobile = "";
    o.phone = "";
    o.emailAddress = "";
    o.contactPersonName = "";
    o.comments = "";
    table.row.add(o).draw();
}

function delLxrLine() {
    var arr = [];
    var table = $('#dataTables-lxr').DataTable();
    var allCh = $("input[name='ch_lxr']");
    var allData = $("#dataTables-lxr  tr:not(:first)");
    $("input[name='ch_lxr']:checked").each(function (i) {
        var n = allCh.index($(this));
        var $data = $(allData[n]);
        var jqInputs = $('input', $data);
        arr.push( $(jqInputs[1]).val());
    });
    if (0 < arr.length) {
        $(arr).each(function (i) {
            if ("" != arr[i]) {
                $.ajax({
                    url : config.baseurl +"/api/tsc/ts/delContact",
                    contentType : 'application/x-www-form-urlencoded',
                    dataType:"json",
                    data: {
                        vendorId: $('#s_vendorId').val(),
                        contactId: arr[i]
                    },
                    type:"POST",
                    beforeSend:function(){

                    },
                    success:function(data){

                    },
                    complete:function(){

                    },
                    error:function(){
                        window.parent.error(ajaxError_loadData);
                    }
                });
            }
        });
    }
    table.rows( $("input[name='ch_lxr']:checked").parents('tr')).remove();
    table.draw();
}

function delZzLine() {
    var arr = [];
    var table = $('#dataTables-zz').DataTable();
    var allCh = $("input[name='ch_zz']");
    var allData = $("#dataTables-zz  tr:not(:first)");
    $("input[name='ch_zz']:checked").each(function (i) {
        var n = allCh.index($(this));
        var $data = $(allData[n]);
        var jqInputs = $('input', $data);
        arr.push( $(jqInputs[1]).val());
    });
    if (0 < arr.length) {
        $(arr).each(function (i) {
            if ("" != arr[i]) {
                $.ajax({
                    url: config.baseurl + "/api/tsc/ts/delAttachment",
                    contentType: 'application/x-www-form-urlencoded',
                    dataType: "json",
                    async: false,
                    data: {
                        vendorId: $('#s_vendorId').val(),
                        fileId: arr[i]
                    },
                    type: "POST",
                    beforeSend: function () {

                    },
                    success: function (data) {

                    },
                    complete: function () {

                    },
                    error: function () {

                    }
                });
            }
        });
    }
    // table.rows( $("input[name='ch_zz']:checked").parents('tr')).remove();
    // table.draw();
    $.ajax({
        url : config.baseurl +"/api/tsc/ts/attachmentContact",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            vendorId: $('#s_vendorId').val()
        },
        type:"GET",   //请求方式
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            dataSetzz = data;
            queryZzList();
        },
        complete:function(){
            window.parent.closeLoading();
        },
        error:function(){
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}


function saveTs(v) {
    if ("" == $('#s_vendorName').val().trim()) {
        window.parent.warring("请填写供应商名称");
        return ;
    }
    if (getlength($('#s_num_1099').val().trim())>30) {
        window.parent.warring("纳税人标识内容过长");
        return ;
    }
    window.parent.showLoading();
    var o = new Object();
    o.vendorId = $('#s_vendorId').val();
    o.vendorName = $('#s_vendorName').val().trim();
    o.bankName = $('#s_bankName').val();
    o.bankAccountNum = $('#s_bankAccountNum').val();
    o.address = $('#s_address').val();
    o.vendorTypeCode = $('#s_vendorTypeCode').val();
    o.vendorType = $('#s_vendorType').val();
    o.statusCode = $('#s_statusCode').val();
    o.status = $('#s_status').val();
    o.vendorLevel = $('#s_vendorLevel').val();
    o.supplierNature = $('#s_supplierNature').val();
    o.vatRegistrationNum = $('#s_vatRegistrationNum').val();
    o.num_1099 = $('#s_num_1099').val();

    var userTable_lxr = $('#dataTables-lxr').DataTable();
    var data_lxr = userTable_lxr.rows().data();
    var userTable_zz = $('#dataTables-zz').DataTable();
    var data_zz = userTable_zz.rows().data();

    var lxrArray = [];
    var o_lxr;
    if (0 < data_lxr.length) {
        $("#dataTables-lxr  tr:not(:first)").each(function () {
            o_lxr = new Object();
            var jqInputs = $('input', $(this));
            if (null != headerData)
                o_lxr.vendorId = headerData.vendorId;
            o_lxr.contactId = $(jqInputs[1]).val();
            o_lxr.personName = $(jqInputs[2]).val();
            o_lxr.personTitle = $(jqInputs[3]).val();
            o_lxr.department = $(jqInputs[4]).val();
            o_lxr.mobile = $(jqInputs[5]).val();
            o_lxr.phone = $(jqInputs[6]).val();
            o_lxr.emailAddress = $(jqInputs[7]).val();
            o_lxr.contactPersonName = $(jqInputs[8]).val();
            o_lxr.comments = $(jqInputs[9]).val();
            lxrArray.push(o_lxr);
        });
    }

    var valLxr = 0;
    $(lxrArray).each(function (i) {
        if ("" == lxrArray[i].personName) {
            window.parent.warring("联系人，第[" + (i + 1) + "]行姓名未填写");
            valLxr++;
        }
        // if ("" == lxrArray[i].personTitle) {
        //     window.parent.warring("联系人，第[" + (i + 1) + "]行职务未填写");
        //     valLxr++;
        // }
        if ("" == lxrArray[i].mobile) {
            window.parent.warring("联系人，第[" + (i + 1) + "]行手机未填写");
            valLxr++;
        }
        if ("" == lxrArray[i].phone) {
            window.parent.warring("联系人，第[" + (i + 1) + "]行电话未填写");
            valLxr++;
        }
        if ("" == lxrArray[i].contactPersonName) {
            window.parent.warring("联系人，第[" + (i + 1) + "]行瑞林内部对接人未填写");
            valLxr++;
        }
    });
    if (0 < valLxr) {
        window.parent.closeLoading();
        return;
    }


    var zzArray = [];
    var o_zz;
    if (0 < data_zz.length) {
        $("#dataTables-zz  tr:not(:first)").each(function () {
            var jqInputs = $('input', $(this));
            o_zz = new Object();
            if (null != headerData)
                o_zz.vendorId = headerData.vendorId;
            o_zz.fileId = $(jqInputs[1]).val();
            o_zz.fileName = $(jqInputs[2]).val();
            o_zz.fileUrl = $(jqInputs[3]).val();
            zzArray.push(o_zz);
        });
    }

    o.contacts = lxrArray;
    o.attachments = zzArray;
    console.log(JSON.stringify(o));
    // return ;
    $.ajax({
        url : config.baseurl +"/api/tsc/ts/saveTs",
        contentType : 'application/json',
        dataType:"json",
        data: JSON.stringify(o),
        type:"POST",
        beforeSend:function(){
        },
        success:function(data){
            if ("0000" == data.returnCode) {
                window.parent.success("保存成功！");
                $('#bu_queryData').trigger("click");
                if (null == headerData)
                    $('#s_vendorId').val(data.db_sid);
                if (1 == v) {
                    doSubmit();
                } else {
                    showNo = 2;
                    loadGys($('#s_vendorId').val());
                }
            } else {
                window.parent.error(data.returnMsg);
            }
        },
        complete:function(){
            window.parent.closeLoading();
        },
        error:function(){
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

function setCheckLxrAll() {
    var isChecked = $('#ch_lxrAll').prop("checked");
    $("input[name='ch_lxr']").prop("checked", isChecked);
}

var dataSetlxr = [];
var t_lxr;
function queryLxrList() {
    $('#dataTables-lxrAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-lxr"></table>');

    t_lxr = $('#dataTables-lxr').DataTable({
        "searching": false,
        "processing": true,
        "data": dataSetlxr,
        "scrollY": "300px",
        "scrollCollapse": true,
        "paging": false,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "<input type='checkbox' name='ch_lxrAll' id='ch_lxrAll' onclick='javascript:setCheckLxrAll();' />", "data": null, "className": "style_column"},
            {"title": "<font color='red'>*姓名</font>", "data": "personName"},
            {"title": "职务", "data": "personTitle"},
            {"title": "部门", "data": "department"},
            {"title": "<font color='red'>*手机</font>", "data": "mobile"},
            {"title": "<font color='red'>*电话</font>", "data": "phone"},
            {"title": "邮箱", "data": "emailAddress"},
            {"title": "<font color='red'>*瑞林内部对接人</font>", "data": "contactPersonName"},
            {"title": "备注", "data": "comments"}
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
            "width": 100,
            "targets": 2
        },{
            "width": 100,
            "targets": 3
        },{
            "width": 100,
            "targets": 4
        },{
            "width": 100,
            "targets": 5
        },{
            "width": 105,
            "targets": 7
        },{
            "width": 200,
            "targets": 8
        }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            $(row).children('td').eq(1).html(createInput("hidden", data.contactId)).append(createInput("text", data.personName));
            $(row).children('td').eq(2).html(createInput("text", data.personTitle));
            $(row).children('td').eq(3).html(createInput("text", data.department));
            $(row).children('td').eq(4).html(createInput("text", data.mobile));
            $(row).children('td').eq(5).html(createInput("text", data.phone));
            $(row).children('td').eq(6).html(createInput("text", data.emailAddress));
            $(row).children('td').eq(7).html(createInput("text", data.contactPersonName, true));
            $(row).children('td').eq(8).html(createInput("text", data.comments));
        },
        "initComplete": function () {
            $('#dataTables-lxr_wrapper').css("cssText", "margin-top: -5px;");
            var e;
            if ("NEW" == headerData.status || "REJECTED" == headerData.status) {
                e = false;
            } else{
                e = true;
            }
            $("#dataTables-lxr  tr:not(:first)").each(function () {
                var jqInputs = $('input', $(this));
                $(jqInputs[0]).attr("disabled", e);
                $(jqInputs[2]).attr("disabled", e);
                $(jqInputs[3]).attr("disabled", e);
                $(jqInputs[4]).attr("disabled", e);
                $(jqInputs[5]).attr("disabled", e);
                $(jqInputs[6]).attr("disabled", e);
                $(jqInputs[7]).attr("disabled", e);
                $(jqInputs[8]).attr("disabled", e);
                $(jqInputs[9]).attr("disabled", e);
            });
        }
    });

    t_lxr.on( 'order.dt search.dt', function () {
        t_lxr.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = "<input type='checkbox' name='ch_lxr' />";
        } );
    } ).draw();

}

var createInput = function (type, val, flag) {
    if (undefined == val)
        val = "";
    var input = $("<input type='" + type + "' style='width: 100%; display: inline-block;' class='form-control input-sm' value='" + val + "' />");
    // if (undefined != flag && flag) {
    //     input.autocomplete({
    //         source: ebsUser,
    //         minLength: 0
    //     });
    // }
    return input;
}

function setCheckZzAll() {
    var isChecked = $('#ch_zzAll').prop("checked");
    $("input[name='ch_zz']").prop("checked", isChecked);
}

var dataSetzz = [];
var t_zz;
function queryZzList(q) {
    $('#dataTables-zzAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-zz"></table>');

    t_zz = $('#dataTables-zz').DataTable({
        "searching": false,
        "processing": true,
        "data": dataSetzz,
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
                return "<a name='a_linkTemplate' target='_blank' href='/api/tsc/ts/downloadZzFile?vendorId=" + full.vendorId + "&fileName=" + encodeURIComponent(full.fileUrl) + "'>下载</a>";
            }
        }
        ],
        "order": [],
        "createdRow": function( row, data, dataIndex ) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            $(row).children('td').eq(1).html(createInput("hidden", data.fileId)).append(createInput("hidden", data.fileName)).append(createInput("hidden", data.fileUrl)).append(data.fileName);
        },
        "initComplete": function () {
            $('#dataTables-zz_wrapper').css("cssText", "margin-top: -5px;");
            if (undefined != q && 1 == q) {
                $('#bu_save').trigger('click');
            }
        }
    });

    t_zz.on( 'order.dt search.dt', function () {
        t_zz.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = "<input type='checkbox' name='ch_zz' />";
        } );
    } ).draw();

}


