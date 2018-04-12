var tab_dataSetPtext=[];
var tab_dataSetPtext=[];
var tab_dataSetPtext=[];
var tab_ptext_text;
var tab_ptext_yb;
var tab_dataSetPyb=[];
var chackValue;
function tab_initPtext() {
    getptext();
    getYb();
    saveLine();
    chackValue=1;
    $('#bu_add_yb_' + activeTabId).on('click', function () {addYb();})
}

function getptext() {
    $.ajax({     //异步，调用后台get方法
        url: config.baseurl + "/api/Ptext/text", //调用地址，不包含参数
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: {
            projectId: tab_l_projectId  //传入参数，依次填写
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {

        },
        success: function (data) {
            tab_dataSetPtext=data.dataSource;
            allTabData[activeTabId].tab_dataSetPtext = tab_dataSetPtext;
            console.log(tab_dataSetPtext);
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
    $('#dataTables-projtext_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-projtext-line_' + activeTabId + '"></table>');
    tab_ptext_text = $('#dataTables-projtext-line_' + activeTabId).DataTable({
        "processing": true,
        "data": tab_dataSetPtext,
        "paging": false,
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
            "width": 42,
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
        "order": [],
        "initComplete": function () { //去掉标准表格多余部分
            $('#dataTables-projtext-line_' + activeTabId).css("cssText", "margin-top: 0px !important; margin-bottom: 0px !important;");
        }
    });
    $('#dataTables-projtext-line_' + activeTabId).on('click', 'a[name=ptext_oa]', function () {
        var userTable = $('#dataTables-projtext-line_' + activeTabId).DataTable();
        var data = userTable.rows( $(this).parents('tr') ).data();
        // query_text_oa(data[0].taskHeaderId,data[0].taskType);
        var id = "";
        var formId = "";
        if ("XMZJ" == data[0].taskType) {
            id = "4229";
            formId = "321";
        } else if ("SSJH" == data[0].taskType) {
            id = "422";
            formId = "114";
        } else if ("GLJH" == data[0].taskType) {
            id = "421";
            formId = "114";
        } else if ("ZCBYDBG" == data[0].taskType) {
            id = "3269";
            formId = "120";
        }
        queryOAInfor(data[0].taskheaderId, "", id, formId, "0");
    });
    tab_ptext_text.on('order.dt search.dt', function () {
        tab_ptext_text.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    }).draw();

    allTabData[activeTabId].tab_ptext_text = tab_ptext_text;
}

function getYb() {
    $.ajax({
        url: config.baseurl + "/api/Ptext/ybhead",
        contentType: "application/json",
        dataType: "json",
        data: {
            projectId: tab_l_projectId  //传入参数，依次填写
        },
        type: "GET",  //调用方法类型
        beforeSend: function () {

        },
        success: function (data) {
            tab_dataSetPyb=data.dataSource;
            allTabData[activeTabId].tab_dataSetPyb = tab_dataSetPyb;
            console.log(tab_dataSetPtext);
            createYb();
        },
        complete: function () {
        },
        error: function () {
            window.parent.error(ajaxError_sys);
        }
    });
}

function createYb() {
    $('#dataTables-ybAll_' + activeTabId).html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-yb_' + activeTabId + '"></table>');
    tab_ptext_yb = $('#dataTables-yb_' + activeTabId).DataTable({
        "processing": true,
        "data": tab_dataSetPyb,
        "paging": false,
        "searching": false,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "序号", "data": null},
            {"title": "标题", "data": "segment1"},
            {"title": "责任人", "data": "personName"},
            {"title": "创建日期", "data": "creationDate"},
            {"title": "查看/更新", "data": "creationDate"},
            {"title": "删除", "data": "creationDate"}
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "width": 42,
            "targets": 0 //排序
        }, {
            "targets": 5,
            "width": 40,
            "render": function (data, type, full, meta) {
                if ("EDIT" == full.status)
                    return '<button name="yb_del" type="button" class="btn btn-default btn-sm" style="padding: 2px 10px;"><i class="fa fa-trash-o"></i></button>';
                else
                    return "";
            }
        }, {
            "targets": 4,
            "width": 70,
            "render": function (data, type, full, meta) {
                if ("EDIT" == full.status)
                    return '<button name="yb_update" type="button" class="btn btn-default btn-sm" style="padding: 2px 10px;"><i class="fa fa-edit" style="color: green;"></i></button>';
                else
                    return '<button name="yb_view" type="button" class="btn btn-default btn-sm" style="padding: 2px 10px;"><i class="fa fa-file-o" style="color: blue;"></i></button>';
            }
        }, {
            "targets": 3,
            "width": 80
        }, {
            "targets": 2,
            "width": 60
        }
        ],
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            
        },
        "order": [],
        "initComplete": function () { //去掉标准表格多余部分
            $('#dataTables-yb_' + activeTabId).css("cssText", "margin-top: 0px !important; margin-bottom: 0px !important;");
        }
    });
    $('#dataTables-yb_' + activeTabId).on('click', 'button[name=yb_update]', function () {
        var userTable = $('#dataTables-yb_' + activeTabId).DataTable();
        var data = userTable.rows( $(this).parents('tr') ).data();
        editYbFlag = false;
        queryYb(data[0].headId);
    });
    $('#dataTables-yb_' + activeTabId).on('click', 'button[name=yb_view]', function () {
        var userTable = $('#dataTables-yb_' + activeTabId).DataTable();
        var data = userTable.rows( $(this).parents('tr') ).data();
        editYbFlag = true;
        queryYb(data[0].headId);
    });
    $('#dataTables-yb_' + activeTabId).on('click', 'button[name=yb_del]', function () {
        var userTable = $('#dataTables-yb_' + activeTabId).DataTable();
        var data = userTable.rows( $(this).parents('tr') ).data();
        window.parent.showConfirm(function () {
            $.ajax({
                url : config.baseurl +"/api/Ptext/delYb",
                contentType : 'application/x-www-form-urlencoded',
                data: {
                    pHeadId: data[0].headId
                },
                type:"POST",
                beforeSend:function(){window.parent.showLoading();},
                success:function(data){
                    window.parent.success("删除成功！");
                    getYb();
                },
                complete:function(){window.parent.closeLoading();},
                error:function(){
                    window.parent.closeLoading();
                    window.parent.error(ajaxError_loadData);
                }
            });
        }, "确认删除吗？");
    });
    tab_ptext_yb.on('order.dt search.dt', function () {
        tab_ptext_yb.column(0, {search: 'applied', order: 'applied'}).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    }).draw();

    allTabData[activeTabId].tab_ptext_yb = tab_ptext_yb;
}

//弹出oa查询
function query_text_oa(taskHeaderId,taskType) {
    $.ajax({
        url : config.baseurl +"/api/Ptext/textoa",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            taskHeaderId: taskHeaderId,
            taskType: taskType
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

function queryYb(headId) {
    $('#yb_doc').empty();
    ybHeaderData=[];
    $.ajax({
        url : config.baseurl +"/api/Ptext/ybline",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            pHeadId: headId,
            projectId: tab_l_projectId
        },
        type:"GET",
        beforeSend:function(){window.parent.showLoading();},
        success:function(data){
            ybHeaderData = data;
            if(ybHeaderData.isrequired=="Y"){
                $('#yb_doc').html("<font color='red'> 由于开票金额或收入确认金额＞收款金额，本项必填！</font>");
            }
            initYbHeader(ybHeaderData);
            $('#ybModal').modal("show");
        },
        complete:function(){window.parent.closeLoading();},
        error:function(){
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

var ybHeaderData;
function addYb() {
    $('#yb_doc').empty();
    ybHeaderData=[];
    editYbFlag = false;
    $.ajax({
        url : config.baseurl +"/api/Ptext/ybphase",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            projectId: tab_l_projectId
        },
        type:"GET",
        beforeSend:function(){window.parent.showLoading();},
        success:function(data){
            ybHeaderData = data;
            ybHeaderData.headId = -1;
           console.log(ybHeaderData);
            if(ybHeaderData.isrequired=="Y"){
                $('#yb_doc').html("<font color='red'> 由于开票金额或收入确认金额＞收款金额，本项必填！</font>");
            }
            $('#pSfqk').html(ybHeaderData.sfqk);
            initYbHeader(ybHeaderData);
            $('#ybModal').modal("show");
        },
        complete:function(){window.parent.closeLoading();},
        error:function(){
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

var editYbFlag = false;
function initYbHeader(data) {
    $('#pBt').val(data.bt).attr("disabled", editYbFlag);
    $('#pHeadId').val(data.headId);
    if (!editYbFlag) {
        $('#save_yb').show();

    } else {
        $('#save_yb').hide();
    }
    $('#pYxqk').val(data.yxqk).attr("disabled", editYbFlag);
    $('#pSfqk').val(data.sfqk).attr("disabled", editYbFlag);
    yeLineData = data.dataSource;
    console.log(yeLineData[0].phaseProgress);
    ljprocess=yeLineData[0].phaseProgress


}
var ljprocess;
$('#ybModal').on('shown.bs.modal', function (e) {
    $('#projectId').val(tab_l_projectId);
    createYbLine();

});

var yeLineData = [];
function createYbLine() {
    $('#dataTables-yb_lineAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-yb_line"></table>');
    $('#dataTables-yb_line').DataTable({
        "processing": true,
        "data": yeLineData,
        "paging": false,
        "searching": false,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columns": [
            {"title": "阶段", "data": "phase"},
            {"title": "进度权重（%）", "data": "phaseWeight"},
            {"title": "上月累计完成进度（%）", "data": "lastProgress"},
            {"title": "累计完成进度（%）", "data": "phaseProgress"},
            {"title": "备注（最多80汉字）", "data": "comments"},
        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "width": 160,
            "targets": 0 //排序
        }, {
            "targets": 1,
            "width": 65
        },{
            "targets": 2,
            "width": 65
        }, {
            "targets": 3,
            "width": 90,
            "render": function (data, type, full,meta) {
                var v = full.phaseProgress ? full.phaseProgress : "0";
                if(full.seq==1)
                    return v;
                else
                  return '<input  onchange="checkvalue(this);" onkeyup="setNumberOnly(this);" value="' + v + '" type="text" class="form-control input-sm" style="width: 100%" name="ybLineWcjd" '+ (!editYbFlag ? "" : "disabled=disabled") + '" />';
            }
        }, {
            "targets": 4,
            "render": function (data, type, full, meta) {
                var v = full.comments ? full.comments : "";
                if (full.seq == 1)
                    return v;
                else
                    return '<input value="' + v + '" type="text" class="form-control input-sm" style="width: 100%"  name="ybLineBz"' + (!editYbFlag ? "" : "disabled=disabled") + '" />';
            }
        }],
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            $(row).children('td').eq(1).attr('style', 'text-align: right;');
            $(row).children('td').eq(1).attr('style', 'background-color:#D3D3D3;');  //bgcolor="#FF0000"
            $(row).children('td').eq(2).attr('style', 'text-align: right;');
            $(row).children('td').eq(2).attr('style', 'background-color:#D3D3D3;');  //bgcolor="#FF0000"
                if(data.seq==1){
                    $(row).children('td').eq(3).attr('style', 'disabled:disabled;');
                    $(row).children('td').eq(3).attr('style', 'background-color:#D3D3D3;');
                }
           // $(row).children('td').eq(4).attr('style', 'display:none;');
        },
        "order": [],
        "initComplete": function () { //去掉标准表格多余部分
            $('#dataTables-yb_line').css("cssText", "margin-top: 0px !important; margin-bottom: 0px !important;");
            $('#dataTables-yb_line_info').remove();
            var input = $('input[name="ybLineWcjd"]');
            var input2 = $('input[name="ybLineBz"]');
            //$(input[0]).hide();
           // $(input2[0]).hide();
        }
    });
    $('#dataTables-yb_line').on('click', 'tr', function () {
        if ($(this).hasClass('dt-select')) {
          //  $(this).removeClass('dt-select');
        }
        else {
            $('#dataTables-yb' +
                '_line').DataTable().$('tr.dt-select').removeClass('dt-select');
            $(this).addClass('dt-select');
        }
    })
}
var aa=0;
function checkvalue(input) {
    var $td = $(input).parent();
    var $td2 = $td.prev();
    var $td3=$td2.prev().prev();
    var tmpInput = $td2.html();
    if(tmpInput > input.value ){
        window.parent.warring($td3.html()+"：所填累计完成进度需大于等于上月累计完成进度");
        return;
    }
}
function setNumberOnly(input) {
    input.value=input.value.replace(/[^\-?\d.]/g,'');
    setSum();
}
function setSum() {
    var progress = $('input[name="ybLineWcjd"]');
    var sum=0;
    progress.each(function (i) {
        var a = progress[i].value;
        var b=yeLineData[i+1].phaseWeight;
      sum=a*b+sum;
    })
    //console.log(sum/100);
    $("table[id=dataTables-yb_line] tr").eq(1).find("td").eq(3).html(sum/100);
    ljprocess=sum/100;
}

function saveLine() {
    $('#save_yb').on('click', function () {
        if(chackValue==1)
        window.parent.showConfirm(confirmsave, "确认【保存并提交】?");
        else
            chackValue=1 ;
    });
}
var confirmsave=function () {
    if ("" == $('#pBt').val().trim()) {
        window.parent.warring("请填写标题");
        return;
    }
    if ("" == $('#pYxqk').val().trim()) {
        window.parent.warring("请填写项目运行情况及存在的问题");
        return;
    }
    if (600 < getTextLength($('#pYxqk').val())) {
        window.parent.warring("项目运行情况及存在的问题最多300汉字，已经超过限制");
        return;
    }
    if (600 < getTextLength($('#pSfqk').val())) {
        window.parent.warring("应收账款情况说明最多300汉字，已经超过限制");
        return;
    }
    console.log(ybHeaderData.isrequired+"--"+getTextLength($('#pSfqk').val()))
    if (ybHeaderData.isrequired=="Y"&&getTextLength($('#pSfqk').val())<1) {
        window.parent.warring("应收账款情况说明必填");
        return;
    }
    var input = $('input[name="ybLineWcjd"]');
    var inputMsg = "";
    input.each(function (i) {
        if (0 != i && "" == input[i].value.trim()) {
            inputMsg += "请填写累计到本月实际完成进度[第" + (i + 1) + "行，累计完成进度]<br>";
        }
    });
    if ("" != inputMsg) {
        window.parent.warring(inputMsg);
        return;
    }
//验证
    input.each(function (i) {
        //inputMsg += yeLineData[i+1].lastProgress+"--"+input[i].value.trim() +"<br>";
        if (yeLineData[i+1].lastProgress > input[i].value) {
            inputMsg += yeLineData[i+1].phase+"：所填累计完成进度需大于等于上月累计完成进度<br>";
        }
    });
    if ("" != inputMsg) {
        window.parent.warring(inputMsg);
        return;
    }

    var input2 = $('input[name="ybLineBz"]');
    var inputMsg2 = "";
    input2.each(function (i) {
        if (0 != i && "" != input2[i].value.trim()) {
            if (160 < getTextLength(input2[i].value)) {
                inputMsg2 += "累计到本月实际完成进度[第" + (i + 1) + "行，备注最多80汉字，已经超过限制]<br>";
            }
        }
    });
    if ("" != inputMsg2) {
        window.parent.warring(inputMsg2);
        return;
    }
    $.ajax({
        url : config.baseurl +"/api/Ptext/savehead",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: $('#ybForm').serialize(),
        type:"POST",
        beforeSend:function(){window.parent.showLoading();},
        success:function(data){
            saveYbLine(data);
            $('#pHeadId').val(data);
        },
        complete:function(){window.parent.closeLoading();},
        error:function(){
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

var getTextLength = function(str) {
    var realLength = 0;
    for (var i = 0; i < str.length; i++)
    {
        charCode = str.charCodeAt(i);
        if (charCode >= 0 && charCode <= 128)
            realLength += 1;
        else
            realLength += 2;
    }
    return realLength;
}

function saveYbLine(headID) {
    var userTable = $('#dataTables-yb_line').DataTable();
    var data = userTable.rows().data();
    var o = null;
    var arr = [];
    var input = $('input[name="ybLineWcjd"]');
    var input2 = $('input[name="ybLineBz"]');
    $(data).each(function (i) {
        o = new Object();
        o.headID = headID;
        o.lineId = data[i].lineId ? data[i].lineId : "-1";
        o.phase = data[i].phase ? data[i].phase : "";
        o.phaseWeight = data[i].phaseWeight ? data[i].phaseWeight : "0";
        o.seq = data[i].seq ? data[i].seq : "";
        if(o.seq==1){
            var $tds = $('#dataTables-yb_line tr:first').find('td');
            o.phaseProgress = ljprocess;
            o.comments = data[i].comments? data[i].comments : "";
        }else {
            o.phaseProgress = input[i-1].value;
            o.comments = input2[i-1].value;
        }
        arr.push(o);
    });
    console.log(arr);
    $.ajax({
        url : config.baseurl +"/api/Ptext/saveline",
        contentType : 'application/json',
        dataType:"json",
        data:JSON.stringify(arr),
        type:"POST",
        beforeSend:function(){window.parent.showLoading();},
        success:function(data){
            window.parent.success("保存成功！");
            getYb();
        },
        complete:function(){window.parent.closeLoading();},
        error:function(){
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}