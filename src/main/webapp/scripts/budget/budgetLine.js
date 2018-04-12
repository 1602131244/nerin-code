/**
 * Created by Administrator on 2016/9/19.
 */

var lineDataSet = [];
var subTypeData;
var costTypeData;
var lineStatusData;
var signData;
var orgData1;
var bugdetLineIds;

// 选中行程序
function setCheckLLineAll() {
    var isChecked = $('#ch_lineListAll').prop("checked");
    $("input[name='ch_lineList']").prop("checked", isChecked);
}

var createInput = function (name, type, val) {
    var input = $("<input type='" + type + "' style='width: 100%; display: inline-block;' name='" + name + "' id='" + name + "' class='form-control input-sm' value='" + val + "' />");
    return input;
}

// 初始化dataTables程序
var tableLine;
function initTablesLine() {
    $('#dataTables-listLineAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-lineData"></table>');

    tableLine = $('#dataTables-lineData').DataTable({
        "processing": true,
        "data": lineDataSet,
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

        // "searching": false,
        "bLengthChange": false,
        "ordering ": false,
         "paging": false,  //禁止表格分页

        // "lengthMenu": [ [10, 25, 50, -1], [10, 25, 50, "All"] ],
        // "lengthMenu": [ [-1], ["All"] ],

        "columns": [
            {"title": "<input type='checkbox' name='ch_lineListAll' id='ch_lineListAll' onclick='javascript:setCheckLLineAll();' />", "data": null},
            {"title": "序号", "data": null},
            {"title": "部门名称", "data": "deptid"},
            {"title": "上年项目编号", "data": "oldProjectNumber"},
            {"title": "上年项目名称", "data": "oldProjectName"},
            {"title": "项目编号", "data": "projectNumber"},
            {"title": "项目名称", "data": "projectName"},
            {"title": "项目类型", "data": "type"},
            {"title": "支出类型", "data": "expenditureType"},
            {"title": "标识", "data": "sign"},
            {"title": "预算说明", "data": "description"},
            {"title": "上年预算金额", "data": "oldAmount"},
            {"title": "上年成本金额", "data": "oldCost"},
            {"title": "上年预算分析对比", "data": "attribute15"},
            {"title": "参考人数", "data": "attribute14"},
            {"title": "填报人数", "data": "totalPersons"},
            {"title": "人均金额", "data": "perCapitaAmount"},
            {"title": "部门预算填报金额", "data": "auditAmount1"},
            {"title": "部门主管审批金额", "data": "auditAmount2"},
            {"title": "分管副总审批金额", "data": "auditAmount3"},
            {"title": "财务主管审批金额", "data": "auditAmount4"},
            {"title": "财务分管副总审批金额", "data": "auditAmount5"},
            {"title": "总经理审批金额", "data": "auditAmount6"},
            {"title": "预算金额", "data": "amount"},
            {"title": "状态", "data": "status"},

            {"title": "lineId", "data": "lineId"},
            {"title": "headerId", "data": "headerId"},
            {"title": "oldProjectId", "data": "oldProjectId"},
            {"title": "outlineNumber", "data": "outlineNumber"},
            {"title": "rbsVersionId", "data": "rbsVersionId"},
            {"title": "personIdAudit1", "data": "personIdAudit1"},
            {"title": "personIdAudit2", "data": "personIdAudit2"},
            {"title": "personIdAudit3", "data": "personIdAudit3"},
            {"title": "personIdAudit4", "data": "personIdAudit4"},
            {"title": "personIdAudit5", "data": "personIdAudit5"},
            {"title": "personIdAudit6", "data": "personIdAudit6"},
            {"title": "attribute1", "data": "attribute1"},
            {"title": "attribute2", "data": "attribute2"},
            {"title": "attribute3", "data": "attribute3"},
            {"title": "attribute4", "data": "attribute4"}           

            // {"title": "attributeCategory", "data": "attributeCategory"},


            // {"title": "attribute5", "data": "attribute5"},
            // {"title": "attribute6", "data": "attribute6"},
            // {"title": "attribute7", "data": "attribute7"},
            // {"title": "attribute8", "data": "attribute8"},
            // {"title": "attribute9", "data": "attribute9"},
            // {"title": "attribute10", "data": "attribute10"},
            // {"title": "attribute11", "data": "attribute11"},
            // {"title": "attribute12", "data": "attribute12"},
            // {"title": "attribute13", "data": "attribute13"},
            // {"title": "attribute14", "data": "attribute14"},
            // {"title": "attribute15", "data": "attribute15"}

        ],
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "targets": 0,
            "width": 5
        },{
            "searchable": false,
            "orderable": false,
            "targets": 1,
            "width": 32
        },{
            "width": 220,
            "targets": 2
        },{
            "width": 90,
            "targets": 3
        },{
            "width": 150,
            "targets": 4,
        },{
            "width": 90,
            "targets": 5
        },{
            "width": 150,
            "targets": 6
        },{
            "width": 120,
            "targets": 7
        },{
            "width": 95,
            "targets": 8
        },{
            "width": 65,
            "targets": 9
            // "visible": false
        },{
            "width": 120,
            "targets": 10
        },{
            "width": 95,
            "targets": 11
        },{
            "width": 95,
            "targets": 12
        },{
            "width": 120,
            "targets": 13
        },{
            "width": 65,
            "targets": 14
        },{
            "width": 65,
            "targets": 15
        },{
            "width": 95,
            "targets": 16
        },{
            "width": 95,
            "targets": 17
        },{
            "width": 95,
            "targets": 18
        },{
            "width": 95,
            "targets": 19
        },{
            "width": 95,
            "targets": 20
        },{
            "width": 95,
            "targets": 21
        },{
            "width": 95,
            "targets": 22
        },{
            "width": 95,
            "targets": 23
        },{
            "width": 65,
            "targets": 24
        },{
            "width": 32,
            "targets": 25,
            "visible": false
        },{
            "width": 32,
            "targets": 26,
            "visible": false
        },{
            "width": 32,
            "targets": 27,
            "visible": false
        },{
            "width": 32,
            "targets": 28,
            "visible": false
        },{
            "width": 32,
            "targets": 29,
            "visible": false
        },{
            "width": 32,
            "targets": 30,
            "visible": false
        },{
            "width": 32,
            "targets": 31,
            "visible": false
        },{
            "width": 32,
            "targets": 32,
            "visible": false
        },{
            "width": 32,
            "targets": 33,
            "visible": false
        },{
            "width": 32,
            "targets": 34,
            "visible": false
        },{
            "width": 32,
            "targets": 35,
            "visible": false
        },{
            "width": 32,
            "targets": 36,
            "visible": false
        },{
            "width": 32,
            "targets": 37,
            "visible": false
        },{
            "width": 32,
            "targets": 38,
            "visible": false
        },{
            "width": 90,
            "targets": 39,
            "visible": false
        }
        ],
        "createdRow": function( row, data, dataIndex ) {
            var disabledFlag = ('Y' == data.attribute2 ? true : false);
            var ldisable = (true == disabledFlag ? "disabled = 'true'" : "")

            $(row).children('td').eq(0).attr('style', 'text-align: center;');
            $(row).children('td').eq(1).attr('style', 'text-align: center;');
            // $(row).children('td').eq(10).attr('style', 'mso-number-format:'#,##0.00';');
            // $(row).children('td').eq(11).attr('style', 'mso-number-format:'#,##0.00';');

            // $(row).children('td').eq(2).attr('onclick', 'tdclick($(this))'); 测试

            $(row).children('td').eq(5).html("<input type='text' id='projectNumber' name='projectNumber' class='form-control input-sm' style='width: 100%;' value='" + (null != data.projectNumber ? data.projectNumber : "") + "'/>");
            $(row).children('td').eq(6).html("<input type='text' id='projectName' name='projectName' class='form-control input-sm' style='width: 100%;' value='" + (null != data.projectName ? data.projectName : "") + "'/>");
            $(row).children('td').eq(10).html("<input type='text' id='description' name='description' class='form-control input-sm' style='width: 100%;' value='" + (null != data.description ? data.description : "") + "'/>");
            $(row).children('td').eq(15).html("<input type='number' id='totalPersons' name='totalPersons' " + ldisable + " class='form-control input-sm' style='width: 100%;' value='" + data.totalPersons + "'/>");
            $(row).children('td').eq(17).html("<input type='number' id='auditAmount1' name='auditAmount1' " + ldisable + " class='form-control input-sm' style='width: 100%;' value='" + data.auditAmount1 + "'/>");
            $(row).children('td').eq(18).html("<input type='number' id='auditAmount2' name='auditAmount2' " + ldisable + " class='form-control input-sm' style='width: 100%;' value='" + data.auditAmount2 + "'/>");
            $(row).children('td').eq(19).html("<input type='number' id='auditAmount3' name='auditAmount3' " + ldisable + " class='form-control input-sm' style='width: 100%;' value='" + data.auditAmount3 + "'/>");
            $(row).children('td').eq(20).html("<input type='number' id='auditAmount4' name='auditAmount4' " + ldisable + " class='form-control input-sm' style='width: 100%;' value='" + data.auditAmount4 + "'/>");
            $(row).children('td').eq(21).html("<input type='number' id='auditAmount5' name='auditAmount5' " + ldisable + " class='form-control input-sm' style='width: 100%;' value='" + data.auditAmount5 + "'/>");
            $(row).children('td').eq(22).html("<input type='number' id='auditAmount6' name='auditAmount6' " + ldisable + " class='form-control input-sm' style='width: 100%;' value='" + data.auditAmount6 + "'/>");

            // .attr('disabled', disabledFlag)

            // $(row).children('td').eq(2).html(orgFun(data.deptid,disabledFlag));
            // $(row).children('td').eq(7).html(subTypefun(data.type,disabledFlag));
            // $(row).children('td').eq(8).html(costTypefun(data.type,data.expenditureType,disabledFlag));
            // $(row).children('td').eq(9).html(signFun(data.sign,disabledFlag));
            // $(row).children('td').eq(24).html(lineStatusFun(data.status));

            if ('Y' == data.attribute2)
                $(row).children('td').eq(2).html(createInput("deptid", "hidden", data.deptid)).append(data.deptName);
            else
                $(row).children('td').eq(2).html(orgFun(data.deptid,disabledFlag));

            if ('Y' == data.attribute2)
                $(row).children('td').eq(7).html(createInput("budget_subtype", "hidden", data.type)).append(data.type);
            else
                $(row).children('td').eq(7).html(subTypefun(data.type,disabledFlag));

            if ('Y' == data.attribute2)
                $(row).children('td').eq(8).html(createInput("budget_costtype", "hidden", data.expenditureType)).append(data.expenditureType);
            else
                $(row).children('td').eq(8).html(costTypefun(data.type,data.expenditureType,disabledFlag));

            if ('Y' == data.attribute2)
                $(row).children('td').eq(9).html(createInput("sign", "hidden", data.sign)).append(data.sign+'类');
            else
                $(row).children('td').eq(9).html(signFun(data.sign,disabledFlag));

            $(row).children('td').eq(24).html(createInput("status", "hidden", data.status)).append(data.statusName);
        },
        // fixedColumns: {
        //     leftColumns: 4
        // },
        "initComplete": function () {
            $('#dataTables-data_length').insertBefore($('#dataTables-data_info'));
            $('#dataTables-data_length').addClass("col-sm-4");
            $('#dataTables-data_length').css("paddingLeft", "0px");
            $('#dataTables-data_length').css("paddingTop", "5px");
            $('#dataTables-data_length').css("maxWidth", "130px");
        }
    });

    //显示所有行
    // tableLine.page.len( -1 ).draw();

    tableLine.on( 'order.dt search.dt', function () {
        tableLine.column(1, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );
        tableLine.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = "<input type='checkbox' name='ch_lineList' />";
        } );
    } ).draw();

//     //找到所有名字的单元格
//     var name = $("tbody td:even");
//     //给这些单元格注册鼠标点击事件
//     name.click(function () {
//         //找到当前鼠标单击的td
//         var tdObj = $(this);
//         //保存原来的文本
//         var oldText = $(this).text();
//         //创建一个文本框
//         var inputObj = $("<input type='text' value='" + oldText + "'/>");
//         //去掉文本框的边框
//         inputObj.css("border-width", 0);
//         inputObj.click(function () {
//             return false;
//         });
//         //使文本框的宽度和td的宽度相同
//         inputObj.width(tdObj.width());
//         inputObj.height(tdObj.height());
//         //去掉文本框的外边距
//         inputObj.css("margin", 0);
//         inputObj.css("padding", 0);
//         inputObj.css("text-align", "center");
//         inputObj.css("font-size", "12px");
//         inputObj.css("background-color", tdObj.css("background-color"));
//         //把文本框放到td中
//         tdObj.html(inputObj);
//         //文本框失去焦点的时候变为文本
//         inputObj.blur(function () {
//             var newText = $(this).val();
//             tdObj.html(newText);
//         });
//         //全选
//         inputObj.trigger("focus").trigger("select");
//     });

}

//单击单元格则变成输入框
// function tdclick(v) {
//         //找到当前鼠标单击的td
//         var tdObj = $(v);
//
//         //保存原来的文本
//         var oldText = $(v).text();
//         //创建一个文本框
//         var inputObj = $("<input type='text' value='" + oldText + "'/>");
//         //去掉文本框的边框
//         inputObj.css("border-width", 0);
//         inputObj.click(function () {
//             return false;
//         });
//         //使文本框的宽度和td的宽度相同
//         inputObj.width(tdObj.width());
//         inputObj.height(tdObj.height());
//         //去掉文本框的外边距
//         inputObj.css("margin", 0);
//         inputObj.css("padding", 0);
//         inputObj.css("text-align", "center");
//         inputObj.css("font-size", "12px");
//         inputObj.css("background-color", tdObj.css("background-color"));
//         //把文本框放到td中
//         tdObj.html(inputObj);
//         //文本框失去焦点的时候变为文本
//         inputObj.blur(function () {
//             var newText = $(this).val();
//             tdObj.html(newText);
//         });
//         //全选
//         inputObj.trigger("focus").trigger("select");
// }

//重新查询编辑数据
function loadBudgetLine(val) {
    $.ajax({
        url : config.baseurl +"/api/budgetRest/budgetLineList",
        contentType : 'application/x-www-form-urlencoded',
        dataType:"json",
        data: {
            headerId : val
        },
        type:"POST",   //请求方式
        beforeSend:function(){
            window.parent.showLoading();
        },
        success:function(data){
            lineDataSet = data.dataSource;
            console.log(lineDataSet);
            initTablesLine();
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

//生成组织部门数据源
function getOrgList(val) {
    $.getJSON(config.baseurl + "/api/budgetRest/getOrgList", function (data) {
        orgData1 = data.dataSource;

        console.log(orgData1);
    });
}
// 生成组织部门下拉选择菜单
function orgFun (val,disabledFlag) {
    var select = $("<select name='orgSel' class='form-control input-sm' style='width: 100%;'  placeholder='请选择'></select>");
    select.attr('disabled', disabledFlag);

    console.log(orgData1);

    select.append("<option value='-1'>请选择</option>");
    for (var i = 0; i < orgData1.length; i++) {
        select.append("<option value='" + orgData1[i].orgId + "'>" + orgData1[i].orgName + "</option>")
    }
    select.val(val);
    return select;
}

//生成预算项目类型数据源
function getSubTypeList(val) {
    $.getJSON(config.baseurl + "/api/budgetRest/getSubTypeList", function (data) {
        subTypeData = data.dataSource;
    });
}
// 生成预算项目类型下拉选择菜单
function subTypefun (val,disabledFlag) {
    var select = $("<select name='budget_subtype' onChange='loadcostType(this)' class='form-control input-sm'  style='width: 100%;'  placeholder='请选择'></select>");
    select.attr('disabled', disabledFlag);

    select.append("<option value='-1'>请选择</option>");
    for (var i = 0; i < subTypeData.length; i++) {
        select.append("<option value='" + subTypeData[i].subType + "'>" + subTypeData[i].subType + "</option>")
    }
    select.val(val);
    return select;
}

function loadcostType (val) {
    // 获取到对象
    var select = $('select', $(val).parent().parent().children('td').eq(8));
    //清空已有数据
    select.empty();

    for (var i = 0; i < costTypeData.length; i++) {
        if (val.value == costTypeData[i].subType)
            select.append('<option value="' + costTypeData[i].costType + '">' + costTypeData[i].costType + '</option>')
    }
}

//生成预算项支出类型数据源
function getCostTypeList(val) {
    $.getJSON(config.baseurl + "/api/budgetRest/getCostTypeList", function (data) {
        costTypeData = data.dataSource;
    });
}
// 生成预算支出类型下拉菜单
function costTypefun (subType,val,disabledFlag) {
    var select = $("<select name='budget_costtype' class='form-control input-sm' style='width: 100%;'  placeholder='请选择'></select>");
    select.attr('disabled', disabledFlag);

    // select.append("<option value='-1'>请选择</option>");
    for (var i = 0; i < costTypeData.length; i++) {
        if (subType == costTypeData[i].subType)
            select.append('<option value="' + costTypeData[i].costType + '">' + costTypeData[i].costType + '</option>')
    }
    select.val(val);
    return select;
}

//生成行状态数据源
function getLineStatusList(val) {
    $.getJSON(config.baseurl + "/api/budgetRest/getLineStatusList", function (data) {
        lineStatusData = data.dataSource;
    });
}
// 生成行状态下拉菜单
function lineStatusFun (val) {
    var select = $("<select name='lineStatusSel' disabled ='true' class='form-control input-sm' style='width: 100%;'  placeholder='请选择'></select>");

    // select.append("<option value='-1'>请选择</option>");
    for (var i = 0; i < lineStatusData.length; i++) {
        select.append('<option value="' + lineStatusData[i].code + '">' + lineStatusData[i].name + '</option>')
    }
    select.val(val);
    return select;
}

//生成标识数据源
function getSignList(val) {
    $.getJSON(config.baseurl + "/api/budgetRest/getSignList", function (data) {
        signData = data.dataSource;
    });
}
// 生成标识下拉菜单
function signFun (val,disabledFlag) {
    var select = $("<select name='signSel' class='form-control input-sm' style='width: 100%;'  placeholder='请选择'></select>");
    select.attr('disabled', disabledFlag);

    select.append("<option value='-1'>请选择</option>");
    for (var i = 0; i < signData.length; i++) {
        select.append('<option value="' + signData[i].code + '">' + signData[i].name + '</option>')
    }
    select.val(val);
    return select;
}

//格式化数字
function toThousands(num) {
    var num = (num || 0).toString(), result = '';
    while (num.length > 3) {
        result = ',' + num.slice(-3) + result;
        num = num.slice(0, num.length - 3);
    }
    if (num) { result = num + result; }
    return result;
}

// 保存预算任务明细数据
function saveLines(v) {
    var budgetSubType = $("select[name='budget_subtype']");
    var budgetCostType = $("select[name='budget_costtype']");
    var sign = $("select[name='signSel']");
    var deptId = $("select[name='orgSel']");
    // var lineStatus = $("select[name='lineStatusSel']");

    var lineStatus = $("input[name='status']");
    var projectNumber = $("input[name='projectNumber']");
    var projectName = $("input[name='projectName']");
    var description = $("input[name='description']");
    var totalPersons = $("input[name='totalPersons']");
    var auditAmount1 = $("input[name='auditAmount1']");
    var auditAmount2 = $("input[name='auditAmount2']");
    var auditAmount3 = $("input[name='auditAmount3']");
    var auditAmount4 = $("input[name='auditAmount4']");
    var auditAmount5 = $("input[name='auditAmount5']");
    var auditAmount6 = $("input[name='auditAmount6']");


    var deptIds = $("input[name='deptid']");
    var signs = $("input[name='sign']");
    var budgetSubTypes = $("input[name='budget_subtype']");
    var budgetCostTypes = $("input[name='budget_costtype']");

    var o;
    var arr = new Array();
    var lno = -1 ;

    var table = $('#dataTables-lineData').DataTable();
    // table.data().each(function (i) {
    for( i=0;i<table.data().length;i++)  {
        o = new Object();

        if ('Y' != table.cell(i,37).data()){
            lno = lno + 1;

            if(deptId[lno].value=="-1"){
                window.parent.error("第" + (i+1) + "行部门未输！");
                return false;
            }

            if(budgetSubType[lno].value=="-1"){
                window.parent.error("第" + (i+1) + "行项目项目类型未输！");
                return false;
            }

            if(budgetCostType[lno].value=="-1"){
                window.parent.error("第" + (i+1) + "行项目支出类型未输！");
                return false;
            }
        }

        // if(deptId[i].value=="-1"){
        //     window.parent.error("第" + (i+1) + "行部门未输！");
        //     return false;
        // }
        if(projectName[i].value==null){
            window.parent.error("第" + (i+1) + "行项目名称未输！");
            return false;
        }
        // if(budgetSubType[i].value=="-1"){
        //     window.parent.error("第" + (i+1) + "行项目项目类型未输！");
        //     return false;
        // }
        // if(budgetCostType[i].value=="-1"){
        //     window.parent.error("第" + (i+1) + "行项目支出类型未输！");
        //     return false;
        // }

        //第二种：用获取Table（通过其id），指定获取的行、列
        // var valueTd=document .getElementById ("tbl").rows [1].cells[2];
        // txt.value=valueTd.innerHTML ;

        // document.getElementById ("dataTables-lineData").rows [i].cells[23].innerHTML;
        o.lineId =table.cell(i,25).data();
        o.headerId = table.cell(i,26).data();

        if ('Y' == table.cell(i,37).data())
                {
                    o.deptid = deptIds[i - lno - 1 ].value;
                    o.sign = signs[i - lno - 1 ].value;
                    o.type = budgetSubTypes[i - lno - 1 ].value;
                    o.expenditureType = budgetCostTypes[i - lno - 1 ].value;
                }
            // o.deptid = $("input", $(table.row(i)).children('td').eq(2)).value;
            // o.deptid = $(table.row(i)).children('td').eq(2).children('input').value;
            // {
            // var deptIds = $('input', $(table.row(i)).children('td').eq(2));
            // o.deptid = $(deptIds).val();
            // }
        else
                {
                    o.deptid = deptId[lno].value;
                    o.sign = sign[lno].value;
                    o.type = budgetSubType[lno].value;
                    o.expenditureType = budgetCostType[lno].value;
                }

        // o.deptid = deptId[i].value;

        // if ('Y' == table.cell(i,37).data())
        //     o.sign = signs[i - lno - 1 ].value;
        //     // o.sign = $("input", $(table.row(i)).children('td').eq(9)).value;
        // else
        //     o.sign = sign[lno].value;

        o.status = lineStatus[i].value;
        o.oldProjectId = table.cell(i,27).data();
        o.projectNumber = projectNumber[i].value;
        o.projectName = projectName[i].value;
        o.outlineNumber = table.cell(i,28).data();
        o.rbsVersionId = table.cell(i,29).data();
        // o.alias = document.getElementById ("dataTables-lineData").rows [i].cells[24].innerHTML;
        o.oldAmount = table.cell(i,11).data();
        o.oldCost = table.cell(i,12).data();
        o.totalPersons = totalPersons[i].value;
        o.perCapitaAmount = table.cell(i,16).data();
        o.amount = table.cell(i,23).data();
        o.auditAmount1 = auditAmount1[i].value;
        o.personIdAudit1 = table.cell(i,17).data();
        o.auditAmount2 = auditAmount2[i].value;
        o.personIdAudit2 = table.cell(i,18).data();
        o.auditAmount3 = auditAmount3[i].value;
        o.personIdAudit3 = table.cell(i,19).data();
        o.auditAmount4 = auditAmount4[i].value;
        o.personIdAudit4 = table.cell(i,20).data();
        o.auditAmount5 = auditAmount5[i].value;
        o.personIdAudit5 = table.cell(i,21).data();
        o.auditAmount6 = auditAmount6[i].value;
        o.personIdAudit6 = table.cell(i,22).data();
        o.description = description[i].value;

        o.personIdAudit1 = table.cell(i,30).data();
        o.personIdAudit2 = table.cell(i,31).data();
        o.personIdAudit3 = table.cell(i,32).data();
        o.personIdAudit4 = table.cell(i,33).data();
        o.personIdAudit5 = table.cell(i,34).data();
        o.personIdAudit6 = table.cell(i,35).data();
        o.attribute1 = table.cell(i,36).data();
        o.attribute2 = table.cell(i,37).data();
        o.attribute3 = table.cell(i,38).data();
        o.attribute4 = table.cell(i,39).data();
        o.attribute14 = table.cell(i,14).data();
        o.attribute15 = table.cell(i,13).data();

        arr.push(o);
    };

    console.log(JSON.stringify(arr));

    $.ajax({
        url: config.baseurl + "/api/budgetRest/saveBudgetLine",
        contentType: "application/json",
        dataType: "json",
        async: true,
        data: JSON.stringify(arr),
        type: "POST",
        beforeSend: function () {
            window.parent.showLoading();
        },
        success: function (data) {
            if ("0000" == data.returnCode) {
                    // window.parent.error(data.db_msg);
                    window.parent.success(data.db_msg);
                    loadBudgetLine(v);
            } else {
                window.parent.error(data.returnMsg);
            }
        },
        complete: function () {
            window.parent.closeLoading();
        },
        error: function () {
            window.parent.closeLoading();
            window.parent.error(ajaxError_sys);
        }
    });
}

// 删除头表程序
function doDelBudgetLines(v) {
        $.ajax({
            url: config.baseurl +"/api/budgetRest/delBudgetLines",
            data: {
                lineIds: v.toString()
            },
            contentType : 'application/x-www-form-urlencoded',
            dataType: "json",
            type: "POST",
            beforeSend: function() {
                window.parent.showLoading();
            },
            success: function(data) {
                if ("0000" == data.returnCode) {
                    // window.parent.success(tips_delSuccess);
                    // $('#bu_queryData').trigger("click");
                    lineIds =[];
                } else {
                    window.parent.error(data.returnMsg);
                }
            },
            complete: function() {
                window.parent.closeLoading();
            },
            error: function() {
                window.parent.closeLoading();
                window.parent.error(ajaxError_sys);
            }
        });
}

//执行发起填报流程，打开OA审批流程页面
function doFillProcess(v) {
    $.ajax({
        url: config.baseurl +"/api/budgetRest/getFillProcessList",
        data: {
            headerId: v.toString()
        },
        contentType : 'application/x-www-form-urlencoded',
        dataType: "json",
        type: "GET",
        beforeSend: function() {
            window.parent.showLoading();
        },
        success: function(data) {
            if (0 != data.dataTotal) {
                for (var i = 0; i < data.dataSource.length; i++) {
                    window.open(data.dataSource[i].url);
                }
                // loadBudgetLine(v);
            } else {
                window.parent.error(data.returnMsg);
            }
        },
        complete: function() {
            window.parent.closeLoading();
        },
        error: function() {
            window.parent.closeLoading();
            window.parent.error(ajaxError_sys);
        }
    });
    
}    