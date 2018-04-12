/**
 * Created by Administrator on 2017/12/26.
 */

var config = new nerinJsConfig();
var counterRecordDataSet=[];
var counterRecordTable;

var ledgerCompanyData;
var reportItemData;
var referenceTypeData;



$(function () {
    //初始化模态框日期格式
    $('#datetimepicker1').datetimepicker({
        format: 'YYYY-MM-DD'
    });
    
});


/****************************************************************************表格DATATABLE**************************************************************/

// 初始化dataTables程序
var counterRecordTable;
function initRangeTables() {
    $('#dataTables-counterRecordList').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-counterRecordData"></table>');

    counterRecordTable = $('#dataTables-counterRecordData').DataTable({
        "processing": true,
        "data": counterRecordDataSet,
        "scrollX": true,
        "scrollY": "500px",
        "scrollCollapse": true,
        "pagingType": "full_numbers",
        "language": {
            "url": "/scripts/common/Chinese.json"
        },

        // searching : false, //去掉搜索框方法一：百度上的方法，但是我用这没管用
        // sDom : '"top"i',   //去掉搜索框方法二：这种方法可以
        // bLengthChange: false,   //去掉每页显示多少条数据方法

        "searching": false,
        "bLengthChange": false,
        "ordering ": false,
        // "paging": false,  //禁止表格分页

        // {"title": "<input type='checkbox' name='ch_listAll' id='ch_listAll' onclick='javascript:setCheckAll();' />", "data": null},
        "columns": [
            // {"title": "", "data": null},           //0
            {"title": "序号", "data": null ,"className": "dt_center"},      //0
            {"title": "摘要", "data": "description" , "className": "dt_center"},  //1
            {"title": "公司", "data": "rangeName" , "className": "dt_center"}, //2
            {"title": "项目代码", "data": "verson" , "className": "dt_center"}, //3
            {"title": "取数类型", "data": "startDate" , "className": "dt_center"},//4
            {"title": "借方", "data": "endDate" , "className": "dt_center"},  //5
            {"title": "贷方", "data": "lastByName" , "className": "dt_center"}, //6
            {"title": "参考借方", "data": "lastUpdateDate" , "className": "dt_center"},  //7
            {"title": "参考贷方", "data": "lastUpdateDate" , "className": "dt_center"},  //8

            {"title": "HEADER_ID", "data": "HEADER_ID"}, //9
            {"title": "LINE_ID", "data": "LINE_ID"} //10
        ],
        "columnDefs": [
           // {"searchable": false,
        //     "orderable": false,
        //     "targets": 0,
        //     "width": 5
        // },
           {
            "searchable": false,
            "orderable": false,
            "targets": 1,
            "width": 32
        },{
            "orderable": false,
            "width": 200,
            "targets": 2
        },{
            "orderable": false,
            "width": 150,
            "targets": 3
        },{
            "orderable": false,
            "width": 120,
            "targets": 4
        },{
            "orderable": false,
            "width": 95,
            "targets": 5
        },{
            "orderable": false,
            "width": 95,
            "targets": 6
        },{
            "orderable": false,
            "width": 95,
            "targets": 7
        },{
            "orderable": false,
            "width": 95,
            "targets": 8
        },{
            "width": 0,
            "targets": 10,
            "visible": false
        },{
            "width": 0,
            "targets": 10,
            "visible": false
        }],
        "createdRow": function( row, data, dataIndex ) {
            // var disabledFlag = ('Y' == data.attribute2 ? true : false);
            var disabledFlag = false;

            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            $(row).children('td').eq(1).attr('style', 'text-align: left;');
            // $(row).children('td').eq(2).attr('style', 'text-align: left;');
            $(row).children('td').eq(3).attr('style', 'text-align: left;');
            $(row).children('td').eq(4).attr('style', 'text-align: left;');
            $(row).children('td').eq(5).attr('style', 'text-align: right;');
            $(row).children('td').eq(6).attr('style', 'text-align: right;');
            $(row).children('td').eq(7).attr('style', 'text-align: right;');
            $(row).children('td').eq(8).attr('style', 'text-align: right;');


            $(row).children('td').eq(2).html(ledgerCompanyFun(data.deptid,disabledFlag));

        },
        "initComplete": function () {
        }
    });

    $('#dataTables-counterRecordData tbody').on( 'click', 'tr', function () {
        var table = $('#dataTables-counterRecordData').DataTable();
        if ( $(this).hasClass('dt-select') ) {
            $(this).removeClass('dt-select');
            // var check = $(this).find("input[name='ch_list']");
            // check.prop("checked", false);

            // $('#bu_edit').attr('disabled',"disabled");
            // $('#bu_del').attr('disabled',"disabled");
            // $('#bu_adu').attr('disabled',"disabled");
            // $('#bu_rej').attr('disabled',"disabled");
            // $('#bu_copy').attr('disabled',"disabled");
            // $('#bu_unit').attr('disabled',"disabled");
        }
        else {
            // var check = $('#dataTables-counterRecordData').DataTable().$('tr.dt-select').find("input[name='ch_list']");
            // check.prop("checked", false);
            $('#dataTables-counterRecordData').DataTable().$('tr.dt-select').removeClass('dt-select');
            $(this).addClass('dt-select');
            // var check = $(this).find("input[name='ch_list']");
            // check.prop("checked", true);

            // if (table.row($(this)).data().endDate ==null||''){
            //     if (table.row($(this)).data().startDate ==null||''){
            //         $('#bu_edit').removeAttr('disabled');
            //         $('#bu_del').removeAttr('disabled');
            //         $('#bu_copy').attr('disabled',"disabled");
            //         $('#bu_adu').removeAttr('disabled');
            //         $('#bu_rej').attr('disabled',"disabled");
            //         $('#bu_unit').removeAttr('disabled');
            //     }else{
            //         $('#bu_edit').removeAttr('disabled');
            //         $('#bu_del').attr('disabled',"disabled");
            //         $('#bu_copy').removeAttr('disabled');
            //         $('#bu_adu').attr('disabled',"disabled");
            //         $('#bu_rej').removeAttr('disabled');
            //         $('#bu_unit').removeAttr('disabled');
            //     }
            //     document.getElementById("bu_edit").innerHTML=('<i class="fa fa-edit" style="color: #FF4500;font-size: 13px;"></i> 编辑');
            // }else{
            //     $('#bu_edit').removeAttr('disabled');
            //     $('#bu_del').attr('disabled',"disabled");
            //     $('#bu_copy').attr('disabled',"disabled");
            //     $('#bu_adu').attr('disabled',"disabled");
            //     $('#bu_rej').attr('disabled',"disabled");
            //     $('#bu_unit').removeAttr('disabled');
            //     document.getElementById("bu_edit").innerHTML=('<i class="fa fa-edit" style="color: #FF4500;font-size: 13px;"></i> 查看');
            // }
        }
    } );

    counterRecordTable.on( 'order.dt search.dt', function () {
        // counterRecordTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
        //     cell.innerHTML = "<input type='checkbox' name='ch_list' />";
        // } ),
            counterRecordTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } );
    } ).draw();
}


/****************************************************************************程序**************************************************************/

/**
 * 当抵消日期变动后执行代码
 */
function searchDateChange(){
    initRange($("#searchRangeId").val());
};

/**
 * 查询合并范围记录
 * @param val  合并范围
 */
function initRange(val) {
    $.ajax({
        url : config.baseurl +"/api/rangeRest/rangeList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            rangeDate:        $('#rangeDate').val(),
        },
        type:"GET",
        beforeSend:function(){
        },
        success:function(data){
            // console.log("rangeId:" + val);
            // rangeDataSet = data.dataSource;
            $("#searchRangeId").empty();

            var initValue = "";
            var initText = "";
            // $('#rangeId').append("<option value='" + initValue + "'>" + initText + "," + initText + "," + initText +  "</option>");
            $('#searchRangeId').append("<option value='" + initValue + "'>" + initText + "," + initText +  "</option>");

            $(data.dataSource).each(function(index){
                // $("#rangeId").append($('<option value="'+data.dataSource[index].rangeId + '">'+data.dataSource[index].rangeNumber + "," +data.dataSource[index].rangeName + "," + data.dataSource[index].verson+'</option>'));
                $("#searchRangeId").append($('<option value="'+data.dataSource[index].rangeId +'_'+data.dataSource[index].verson+'">' +data.dataSource[index].rangeName + "," + data.dataSource[index].verson + "," + data.dataSource[index].startDate + "," + data.dataSource[index].endDate +  '</option>'));
            });

            $('#searchRangeId').removeAttr('disabled');

            if (val == '' || val ==  '_' || val == null){
                $("#searchRangeId").val('');
                $('#searchUnitId').attr('disabled',"disabled");
                $("#searchUnitId").empty();
                $("#searchUnitId").val("");
            }
            else{
                $("#searchRangeId").val(val);
                // initUnitSel(userAssignmentData ? userAssignmentData.unitId : "");
                //失效新增、打开按钮
                $('#searchUnitId').removeAttr('disabled');
                initUnitSel($('#searchUnitId').val());
            }

        },
        complete:function(){
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

/**
 * 合并范围调整
 */
function searchRangeIdChange(val){
    if (val !=""||null){
        $('#searchUnitId').removeAttr('disabled');
        initUnitSel($('#searchUnitId').val());
    }else{
        $('#searchUnitId').attr('disabled',"disabled");
        $("#searchUnitId").empty();
        $("#searchUnitId").val("");

        $('#bu_query').attr('disabled',"disabled");
        $('#bu_add').attr('disabled',"disabled");
        $('#bu_scdx').attr('disabled',"disabled");
    }
};

/**
 * 查询合并单元
 * @param val
 */
function initUnitSel(val) {
    $.ajax({
        url : config.baseurl +"/api/rangeRest/unitList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            rangeId:        $("#searchRangeId").val().split("_")[0],
            rangeVerson:   $("#searchRangeId").find("option:selected").text().split(",")[1]
        },
        type:"GET",   //请求方式
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            // console.log("unitId:" + val);
            $("#searchUnitId").empty();

            var initValue = "";
            var initText = "";
            $('#searchUnitId').append("<option value='" + initValue + "'>" + initText + "," + initText+ "</option>");

            $(data.dataSource).each(function(index){
                $("#searchUnitId").append($('<option value="'+data.dataSource[index].unitId + '">'+data.dataSource[index].unitNumber + "," +data.dataSource[index].unitName + '</option>'));
            });

            $("#searchUnitId").val(val);
        },
        complete:function(){
            window.parent.closeLoading();
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

/**
 * 合并范围调整
 */
function searchUnitIdChange(val){
    if (val !="" && val !=null){
        $('#bu_query').removeAttr('disabled');
        $('#bu_add').removeAttr('disabled');
        $('#bu_scdx').removeAttr('disabled');
    }else{
        $('#bu_query').attr('disabled',"disabled");
        $('#bu_add').attr('disabled',"disabled");
        $('#bu_scdx').attr('disabled',"disabled");
    }
};

/**
 * 点击新增按钮
 */
$('#bu_add').on('click', function (e) {
    // userAssignmentData = null;
    initEnteyModal();
});

function initEnteyModal() {
    // 显示新增界面
    $('#entry_modal').modal('show');
}


/**
 * 查询公司
 * @param rangeId
 * @param rangeVerson
 * @param unitId
 */
function queryCompanyTable(rangeId,rangeVerson,unitId) {
    $.ajax({
        url : config.baseurl +"/api/rangeRest/companyList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            rangeId:        rangeId,
            rangeVerson:   rangeVerson,
            unitId:         unitId,
            unitFlag:       1
        },
        type:"GET",
        beforeSend:function(){
            // window.parent.showLoading();
        },
        success:function(data){
            ledgerCompanyData = data.dataSource;
            console.log(ledgerCompanyData);
        },
        complete:function(){
            // window.parent.closeLoading();
        },
        error:function(){
            window.parent.error(ajaxError_loadData);
        }
    });
}

/**
 * 生成公司下拉菜单
 * @param val
 * @param disabledFlag
 * @returns {*|jQuery|HTMLElement}
 */
function ledgerCompanyFun (val,disabledFlag) {
    var select = $("<select name='ledgerCompanySel' id='ledgerCompanySel' class='selectpicker form-control input-sm' style='width: 100%;' placeholder='请选择'></select>");
    select.attr('disabled', disabledFlag);

    console.log(orgData1);

    select.append("<option value='-1'>请选择</option>");
    for (var i = 0; i < orgData1.length; i++) {
        select.append("<option value='" + ledgerCompanyData[i].code + "'>" + ledgerCompanyData[i].name + "</option>")
    }
    select.val(val);
    return select;
}