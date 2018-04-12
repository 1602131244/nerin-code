/**
 * Created by genmingli on 2016/9/1.
 */
var config = new nerinJsConfig();

// 任务头信息
var headerData;

//记录删除行ID
var lineIds = [];

// 选中行程序
function setCheckAll() {
    var isChecked = $('#ch_listAll').prop("checked");
    $("input[name='ch_list']").prop("checked", isChecked);
}

//表示页面加载完成后再执行
$(function () {
    console.log("page loaded");

    var dataSet = [];
    //初始化table数据
    initTables();
    
    // 初始化dataTables程序
    var t;
    function initTables() {
        $('#dataTables-listAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-data"></table>');

        t = $('#dataTables-data').DataTable({
            "processing": true,
            "data": dataSet,
            "scrollX": true,
            "scrollY": "500px",
            "scrollCollapse": true,
            "pagingType": "full_numbers",
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            
            "searching": false,
            "bLengthChange": false,
            "ordering ": false,
            
            "columns": [
                {"title": "<input type='checkbox' name='ch_listAll' id='ch_listAll' onclick='javascript:setCheckAll();' />", "data": null},
                {"title": "序号", "data": null},
                {"title": "公司名称", "data": "orgName"},
                {"title": "年份", "data": "year"},
                {"title": "预算任务名称", "data": "name"},
                {"title": "状态", "data": "statusName"},
                {"title": "创建人", "data": "createByName"},
                {"title": "创建日期", "data": "creationDate"},
            ],
            "columnDefs": [{
                "searchable": false,
                "orderable": false,
                "targets": 1,
                "width": 32
            },{
                "searchable": false,
                "orderable": false,
                "targets": 0,
                "width": 5
            },{
                "width": 200,
                "targets": 2
            },{
                "width": 65,
                "targets": 3
            },{
                "width": 200,
                "targets": 4
            },{
                "width": 95,
                "targets": 5
            },{
                "width": 65,
                "targets": 6
            },{
                "width": 65,
                "targets": 7
            }
            ],
            "createdRow": function( row, data, dataIndex ) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
                $(row).children('td').eq(1).attr('style', 'text-align: center;');
            },
            fixedColumns: {
                leftColumns: 4
            },
            "initComplete": function () {
                $('#dataTables-data_length').insertBefore($('#dataTables-data_info'));
                $('#dataTables-data_length').addClass("col-sm-4");
                $('#dataTables-data_length').css("paddingLeft", "0px");
                $('#dataTables-data_length').css("paddingTop", "5px");
                $('#dataTables-data_length').css("maxWidth", "130px");
            }
        });

        $('#dataTables-data tbody').on( 'click', 'tr', function () {
            var table = $('#dataTables-data').DataTable();
            if ( $(this).hasClass('dt-select') )  {  //当前记录为选中状态
                $(this).removeClass('dt-select');
                var check = $(this).find("input[name='ch_list']");
                check.prop("checked", false);
            }
            else {
                var check = $('#dataTables-data').DataTable().$('tr.dt-select').find("input[name='ch_list']");
                check.prop("checked", false);
                $('#dataTables-data').DataTable().$('tr.dt-select').removeClass('dt-select');
                $(this).addClass('dt-select');
                var check = $(this).find("input[name='ch_list']");
                check.prop("checked", true);
            }
        } );

        t.on( 'order.dt search.dt', function () {
            t.column(1, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } );
            t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = "<input type='checkbox' name='ch_list' />";
            } );
        } ).draw();
    }

    // 监听查询按钮
    $('#bu_queryData').click(function(){
        $.ajax({
            url : config.baseurl +"/api/budgetRest/budgetHeaderList",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            // 方法一：通过FORM自动转换，名称一致
            // data: $('#selectFrom').serialize(),
            // 方法二：指定对应的参数
            data: {
                orgName: $('#orgNameS').val(),
                year: $('#yearS').val(),
                budgetName: $('#nameS').val(),
                createName: $('#createrS').val()
            },            
            type:"POST",   //请求方式
            
            // 没有父窗口所以注释掉
            beforeSend:function(){
                window.parent.showLoading();
            },
            success:function(data){
                dataSet = data.dataSource;
                console.log(dataSet);
                initTables();
            },
            complete:function(){
                window.parent.closeLoading();
            },
            error:function(){
                window.parent.error(ajaxError_loadData);
            }
        });
    });

    // 监听新增按钮
    $('#bu_add').on('click', function () {
        headerData = null;
        initAddOrUpdateModal();
    });

    // 初始化新增界面
    function initAddOrUpdateModal() {
        // 隐藏组织下拉菜单
        $(".selectpicker").selectpicker('hide');


        $('#orgName').val(headerData ? headerData.orgName : "");
        $('#budgetId').val(headerData ? headerData.budgetId : "");
        $('#orgId').val(headerData ? headerData.orgId : "");
        $('#year').val(headerData ? headerData.year : "");
        $('#budgetName').val(headerData ? headerData.name : "");

        reload_sel_year(headerData ? headerData.year : "");

        document.getElementById("addHeaderLabel").innerHTML=(headerData ? "编辑部门年度预算任务" : "新增部门年度预算任务");

        // 显示新增界面
        $('#add_modal').modal('show');
    }

    // 初始化年份选择下拉菜单
    function reload_sel_year(v) {
        $.getJSON(config.baseurl + "/api/budgetRest/getYearList", function (data) {
            var list = data.dataSource;
            $('#year').empty();
            var initValue = "";
            var initText = "请选择";
            $('#year').append("<option value='" + initValue + "'>" + initText + "</option>");
            list.forEach(function (data) {
                $('#year').append("<option value='" + data.year + "'>" + data.year + "</option>");
            });
            $('#year').val(v);
        });
    }

    // 点击组织查询按钮
    $('#b_searchOrg').on('click', function (e) {
        $.ajax({
            url : config.baseurl +"/api/budgetRest/getOrgList",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                orgName: $('#orgName').val(),
                sign: 'Y'
            },
            type:"GET",
            beforeSend:function(){
            },
            success:function(data){
                initOrgMenu(data.dataSource);
            },
            complete:function(){
            },
            error:function(){
                window.parent.error(ajaxError_loadData);
            }
        });
    });

    // 初始化组织下拉选择
    function initOrgMenu(data){
        $("#sel_org").empty();
        $(data).each(function(index){
            $("#sel_org").append($('<option value="'+data[index].orgId + '">'+data[index].orgId + "," + data[index].orgName+'</option>'));
        });
        //$('.selectpicker').selectpicker('destroy');
        $(".selectpicker").selectpicker('refresh');
        $(".selectpicker").selectpicker('show');
        $('button[data-id="sel_org"]').trigger("click");
    }

    //当下拉选择隐藏时
    $('.selectpicker').on('hidden.bs.select', function (e) {
        $('#orgId').val($('#sel_org').val().split(",")[0]);
        var sTest = $('#sel_org option:selected').text();
        $('#orgName').val(sTest.split(",")[1]);

        // $('#proFullName').val($('#sel_org option:selected').text() + "," + $('#sel_pro').val().split(",")[1]);
        $(".selectpicker").selectpicker('hide');

    });

    // 监听保存按钮
    $('#saveHeader').on('click', function () {
        var valFlag = true;

        var orgId = $('#orgId').val();
        var year = $('#year').val();
        var budgetName = $('#budgetName').val();

        if ("" == orgId.trim()) {
            window.parent.error("请选择公司！");
            valFlag = false;
        }
        if ("" == year.trim()) {
            window.parent.error("请选择年份！");
            valFlag = false;
        }
        if ("" == budgetName.trim()) {
            window.parent.error("请填写任务名称！");
            valFlag = false;
        }

        if (!valFlag)
            return;

        saveHeader();
    });

    // 保存程序
    function saveHeader() {
        var o = new Object();
        o.headerId =  headerData ? headerData.headerId : $('#budgetId').val();
        o.orgId = $('#orgId').val();
        o.year = $('#year').val();
        o.name = $('#budgetName').val();

        if (headerData) {
            o.status = headerData.status;
        }

        console.log(JSON.stringify(o));
        // return;
        $.ajax({
            url : config.baseurl +"/api/budgetRest/saveBudgetHeader",
            contentType : 'application/json',
            dataType:"json",
            data: JSON.stringify(o),
            type:"POST",
            beforeSend:function(){
            },
            success:function(data){
                if ("0000" == data.returnCode) {
                    // tmp_initButton = 1;
                    // loadHeader(data.db_sid, 1);
                    window.parent.success("基本信息创建成功！")
                    $('#add_modal').on('shown.bs.modal', function (e) {}).modal('hide');
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

    // 监听删除按钮
    $('#bu_delHeader').on('click', function () {
        // if (!valWork())
        //     return;
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        }
        doNo = 4;
        // show_div_continueOrcancel();
        window.parent.showConfirm(doDelTask, "确认删除" + data[0].orgName + data[0].year  +"年预算任务吗？");
    });

    // 删除头表程序
    var doDelTask = function() {
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        }
        var to = [];
        $(data).each(function (index) {
            to.push(data[index].headerId);
        });
        $.ajax({
            url: config.baseurl +"/api/budgetRest/delBudget",
            data: {
                headerIds: to.toString()
            },
            contentType : 'application/x-www-form-urlencoded',
            dataType: "json",
            type: "POST",
            beforeSend: function() {
                window.parent.showLoading();
            },
            success: function(data) {
                if ("0000" == data.returnCode) {
                    window.parent.success(tips_delSuccess);
                    $('#bu_queryData').trigger("click");
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

    // 监听修改任务按钮
    $('#bu_editHeader').on('click', function () {
        // work = 1;
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        } else if (2 <= data.length) {
            window.parent.warring(valTips_workOne);
            return;
        }
        loadBudgetHeader(data[0].headerId);
    });

    //重新查询编辑数据
    function loadBudgetHeader(val) {
        $.ajax({
            url : config.baseurl +"/api/budgetRest/budgetHeaderList",
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
                headerData = data.dataSource[0];
                console.log(dataSet);
                initAddOrUpdateModal();
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

    // 监听导入按钮
    $('#bu_importLines').on('click', function () {
        // if (!valWork())
        //     return;
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        }
        doNo = 4;
        // show_div_continueOrcancel();
        window.parent.showConfirm(doImportLine, "确认导入预算往年数据吗？");
    });

    // 导入往年明细数据程序
    var doImportLine= function() {
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        }
        // var to = [];
        // $(data).each(function (index) {
        //     to.push(data[index].headerId);
        // });
        $.ajax({
            url: config.baseurl +"/api/budgetRest/importBudgetLines",
            data: {
                headerId: data[0].headerId
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
                    // window.parent.error(data.db_msg);
                    window.parent.success(data.db_msg);
                    $('#bu_queryData').trigger("click");
                } else {
                    window.parent.error(data.db_msg);
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

    //查询预算项目支出类型数据
    getSubTypeList();
    //查询预算支出类型数据
    getCostTypeList();
    //生成组织部门数据源
    getOrgList();
    // 生成行状态下拉菜单
    getLineStatusList ();
    //生成标识数据源
    getSignList();

    // 监听编辑章节明细按钮
    $('#bu_editLines').on('click', function () {
        // 判定选定数据行数，只能选择一行数据
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        } else if (2 <= data.length) {
            window.parent.warring(valTips_workOne);
            return;
        }

        lineDataSet = [];
        // 显示编辑任务明细界面
        $('#editLines_modal').modal('show');        
    });

    //监听编辑任务明细按钮
    $('#editLines_modal').on('shown.bs.modal', function (e) {
        // 查询明细表数据
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        loadBudgetLine(data[0].headerId);
    });

    // 监听保存任务明细按钮
    $('#bu_save_line').on('click', function () {
        // IF (0 < lineIds.length)
        if (lineIds==null||lineIds.length==0)
            null;
        else
            doDelBudgetLines(lineIds);

        
        // 查询明细表数据
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();        
        saveLines(data[0].headerId);
    });

    // 监听新增行按钮
    $('#bu_add_line').on('click', function () {
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();

        var table = $('#dataTables-lineData').DataTable();
        table.row.add({
            "deptid":                       -1,
            "oldProjectNumber":            "",
            "oldProjectName":               "",
            "projectNumber":                "",
            "projectName":                  "",
            "type":                         "部门专项",
            "expenditureType":            "项目全部",
            "sign":                         "D",
            "description":                  "",
            "oldAmount":                    0,
            "oldCost":                      0,
            "totalPersons":                "",
            "perCapitaAmount":                "",
            "auditAmount1":                "",
            "auditAmount2":                "",
            "auditAmount3":                "",
            "auditAmount4":                "",
            "auditAmount5":                "",
            "auditAmount6":                "",
            "amount":                       "",
            "status":                       "NEW",

            "lineId":                "",
            "headerId":                     data[0].headerId,
            "oldProjectId":                "",
            "outlineNumber":                "",
            "rbsVersionId":                "",
            "personIdAudit1":                "",
            "personIdAudit2":                "",
            "personIdAudit3":                "",
            "personIdAudit4":                "",
            "personIdAudit5":                "",
            "personIdAudit6":                "",
            "attribute1":                     "",
            "attribute2":                     "",
            "attribute3":                     "",
            "attribute4":                     "",
            "attribute14":                    "",
            "attribute15":                    "",
            "statusName":                       "新建"
        }).draw();
    });

    // 监听删除行按钮
    $('#bu_del_line').on('click', function () {
        // work = 1;
        var lineTable = $('#dataTables-lineData').DataTable();

        var lineData = lineTable.rows( $("input[name='ch_lineList']:checked").parents('tr') ).data();
        if (0 == lineData.length) {
            window.parent.warring(valTips_selOne);
            return;
        }

        // for( i=0;i<lineData.length;i++)  {
        //     if (lineData[i].attribute2 == 'Y'){
        //         window.parent.warring('勾选的行中存在导入数据，不允许删除！');
        //         return;
        //     }
        // }

        // 添加行ID
        for( i=0;i<lineData.length;i++)  {
            if (null != lineData[i].lineId)
                lineIds.push(lineData[i].lineId);
        }

        lineTable.rows( $("input[name='ch_lineList']:checked").parents('tr')).remove();
        lineTable.draw()

    });

    // 监听发起填报流程按钮
    $('#bu_fill_process').on('click', function (e) {
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        doFillProcess(data[0].headerId);
    });

    // 监听发起填报流程按钮
    $('#bu_fill_process_h').on('click', function (e) {
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        doFillProcess(data[0].headerId);
    });

    // 监听发起填报流程按钮
    $('#bu_excle').on('click', function (e) {
        method1('dataTables-lineData');
    });

    var idTmr;
    function  getExplorer() {
        var explorer = window.navigator.userAgent ;
        //ie
        if (explorer.indexOf("MSIE") >= 0) {
            return 'ie';
        }
        //firefox
        else if (explorer.indexOf("Firefox") >= 0) {
            return 'Firefox';
        }
        //Chrome
        else if(explorer.indexOf("Chrome") >= 0){
            return 'Chrome';
        }
        //Opera
        else if(explorer.indexOf("Opera") >= 0){
            return 'Opera';
        }
        //Safari
        else if(explorer.indexOf("Safari") >= 0){
            return 'Safari';
        }
    }

    function method1(tableid) {//整个表格拷贝到EXCEL中
        if(getExplorer()=='ie')
        {
            var curTbl = document.getElementById(tableid);
            var oXL = new ActiveXObject("Excel.Application");

            //创建AX对象excel
            var oWB = oXL.Workbooks.Add();
            //获取workbook对象
            var xlsheet = oWB.Worksheets(1);
            //激活当前sheet
            var sel = document.body.createTextRange();
            sel.moveToElementText(curTbl);
            //把表格中的内容移到TextRange中
            sel.select();
            //全选TextRange中内容
            sel.execCommand("Copy");
            //复制TextRange中内容
            xlsheet.Paste();
            //粘贴到活动的EXCEL中
            oXL.Visible = true;
            //设置excel可见属性

            try {
                var fname = oXL.Application.GetSaveAsFilename("Excel.xls", "Excel Spreadsheets (*.xls), *.xls");
            } catch (e) {
                print("Nested catch caught " + e);
            } finally {
                oWB.SaveAs(fname);

                oWB.Close(savechanges = false);
                //xls.visible = false;
                oXL.Quit();
                oXL = null;
                //结束excel进程，退出完成
                //window.setInterval("Cleanup();",1);
                idTmr = window.setInterval("Cleanup();", 1);

            }
        }
        else
        {
            tableToExcel(tableid)
        }
    }
    function Cleanup() {
        window.clearInterval(idTmr);
        CollectGarbage();
    }
    var tableToExcel = (function() {
        var uri = 'data:application/vnd.ms-excel;base64,',
            template = '<html><head><meta charset="UTF-8"></head><body><table>{table}</table></body></html>',
            base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) },
            format = function(s, c) {
                return s.replace(/{(\w+)}/g,
                    function(m, p) { return c[p]; }) }
        return function(table, name) {
            if (!table.nodeType) table = document.getElementById(table)
            var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML}
            window.location.href = uri + base64(format(template, ctx))
        }
    })()
    // var tableToExcel = (function() {
    //     var uri = 'data:application/vnd.ms-excel;base64,',
    //         template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><meta http-equiv="Content-Type" charset=utf-8"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>',
    //         base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) },
    //         format = function(s, c) {
    //             return s.replace(/{(\w+)}/g,
    //                 function(m, p) { return c[p]; }) }
    //     return function(table, name) {
    //         if (!table.nodeType) table = document.getElementById(table)
    //         var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML}
    //         window.location.href = uri + base64(format(template, ctx))
    //     }
    // })();
});
