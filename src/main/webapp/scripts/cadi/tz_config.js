// 文印途径
var tz_wytj;
// 项目负责人
var tz_xmfzr;
var CatConfigHeader;
var logonUser;
function initTz_config(id) {
    $.getJSON(config.baseurl + "/api/logonUser", function (data) {
        logonUser = data;
    });
    window.parent.showLoading();
    $.getJSON(config.baseurl + "/api/draw/searchDrawPrintInfo?id=" + id, function (data) {
        window.parent.closeLoading();
        CatConfigHeader = data;
        console.log(CatConfigHeader);
        $('#MyDrawsPO').modal('show');
    });
}

function initData() {
    $('#PltNum').val(CatConfigHeader.plt_Num);
    $('#PltContent').val(CatConfigHeader.plt_Content);
    $('#PltStatus').val(CatConfigHeader.plt_Status_Name);
    $('#PltCategory').val(CatConfigHeader.plt_Category_Name);
    $('#CreationDate').val(CatConfigHeader.creation_Date);
    $('#ProjectNum').val(CatConfigHeader.project_Num);
    $('#TaskName').val(CatConfigHeader.task_Name);
    $('#MainSpciality').val(CatConfigHeader.main_Speciality);
    $('#ProjectName').val(CatConfigHeader.project_Name);
    $('#SpecManager').val(CatConfigHeader.spec_Manager_Name);
    $('#ProjectSecretary').val(CatConfigHeader.project_Secretary);
    $('#Attribute7').val(CatConfigHeader.equipment_Name);
    $('#PltRequestor').val(CatConfigHeader.plt_Requestor_Name);
    $('#PltDepartment').val(CatConfigHeader.plt_Department);
    $('#SendOwner').val(CatConfigHeader.send_Owner);
    $('#SgService').val(CatConfigHeader.sg_Service);
    $('#ArchiveFlag').val(CatConfigHeader.archive_Flag);


    var type = CatConfigHeader.plt_Category.substr(0, 1);
    $('#PaperType').empty();
    if ("P" == type) {
        $('#PaperType').append('<option value="">请选择</option>');
        $('#PaperType').append('<option value="WHITE">白图</option>');
        $('#PaperType').append('<option value="BLUE_YD">蓝图(有底图)</option>');
        $('#PaperType').append('<option value="BLUE_WD">蓝图(无底图)</option>');
    } else {
        $("#OptionsCal_D .modal-body").load("drawsConfig_d_header.html", function() {
            $('#print_size').val(CatConfigHeader.print_size);
            $('#his_typeset').val(CatConfigHeader.his_typeset);
            $('#bindiing_type').val(CatConfigHeader.bindiing_type);
            $('#is_inparts').val(CatConfigHeader.is_inparts);
            $('#plt_comment').val(CatConfigHeader.plt_comment);
            $('#TypesetTotalPrice').val(CatConfigHeader.plt_Total_Price); // 2017-03-07两个字段反了
            $('#PltTotalPrice').val(CatConfigHeader.typeset_Total_Price);
        });
        $('#PaperType').append('<option value="TEXT">文本</option>');
    }
    $('#PaperType').val(CatConfigHeader.paper_Type);
    $('#ContractNum').val(CatConfigHeader.contract_Num);
    setPrintCount();
    $.getJSON(config.baseurl + "/api/draw/getPrintWayList", function (data) {
        tz_wytj = data;
        $('#Attribute5').empty();
        $('#Attribute5').append("<option value=''>请选择</option>");
        $(tz_wytj).each(function (i) {
            var s = tz_wytj[i];
            $('#Attribute5').append('<option value="' + s.split(",")[0] + '">' + s.split(",")[1] + '</option>');
        });
        $('#Attribute5').val(CatConfigHeader.print_Channel);
        $.getJSON(config.baseurl + "/api/draw/getProjectManagerList?proId=" + CatConfigHeader.project_Id, function (data) {
            tz_xmfzr = data;
            $('#ProjectManager').empty();
            $('#ProjectManager').append("<option value=''>请选择</option>");
            $(tz_xmfzr).each(function (i) {
                var s = tz_xmfzr[i];
                $('#ProjectManager').append('<option value="' + s.split(",")[0] + '">' + s.split(",")[1] + '</option>');
            });
            if (tz_xmfzr && 1 == tz_xmfzr.length) {
                $('#ProjectManager').find("option:eq(1)").attr("selected",true);
            } else {
                $('#ProjectManager').val(CatConfigHeader.project_Res_By);
            }
            setBu(CatConfigHeader.plt_Status);
        });
    });
}

// function createFileLine() {
//     $('#dataTables-fileAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-file"></table>');
//     $('#dataTables-file').DataTable({
//         "processing": true,
//         "data": [],
//         "paging": false,
//         "searching": false,
//         "language": {
//             "url": "/scripts/common/Chinese.json"
//         },
//         "columns": [
//             {"title": "标题", "data": "phase"},
//             {"title": "说明", "data": "phaseWeight"},
//             {"title": "文档类型", "data": "phaseProgress"},
//             {"title": "作者", "data": "comments"},
//             {"title": "日期", "data": "comments"},
//             {"title": "文件信息", "data": "comments"},
//             {"title": "操作", "data": "comments"}
//         ],
//         "columnDefs": [{
//             "searchable": false,
//             "orderable": false,
//             "width": 160,
//             "targets": 0 //排序
//         }, {
//             "targets": 1,
//             "width": 65
//         }, {
//             "targets": 2,
//             "width": 90
//         }, {
//             "targets": 3
//         }],
//         "createdRow": function (row, data, dataIndex) {
//             // $(row).children('td').eq(0).attr('style', 'text-align: center;');
//             // $(row).children('td').eq(1).attr('style', 'text-align: right;');
//         },
//         "order": [],
//         "initComplete": function () { //去掉标准表格多余部分
//             $('#dataTables-file').css("cssText", "margin-top: 0px !important; margin-bottom: 0px !important;");
//             $('#dataTables-file_info').remove();
//         }
//     });
//
// }

var editFlag = false;
function setBu(status) {
    if("APPROVED" == status || "APPROVING" == status || "SIGNET_APPROVED" == status || "SET_APPROVED" == status || "COMPLETED" == status) {
        setBu_1(true);
        editFlag = true;
    } else{
        setBu_1(false);
        editFlag = false;
    }
}

function setBu_1(v) {
    $('#PltContent').attr("disabled", v);
    $('#ProjectManager').attr("disabled", v);
    $('#Attribute5').attr("disabled", v);
    $('#PaperType').attr("disabled", v);
    $('#SendOwner').attr("disabled", v);
    $('#SgService').attr("disabled", v);
    $('#ArchiveFlag').attr("disabled", v);
    $('#SavePlt').attr("disabled", v);
    $('#SubmitPlt').attr("disabled", v);
    $('#bu_fileUpload').attr("disabled", v);
    $('#save_doc').attr("disabled", v);
    $('#bu_addSf').attr("disabled", v);
    $('#print_size').attr("disabled", v);
    $('#his_typeset').attr("disabled", v);
    $('#bindiing_type').attr("disabled", v);
    $('#is_inparts').attr("disabled", v);
    $('#plt_comment').attr("disabled", v);
    $('#bu_addSf').attr("disabled", v);
    $('input[name="amount"]').attr("disabled", v);
    $('select[name="charge_item"]').attr("disabled", v);
    $('select[name="print_set_size"]').attr("disabled", v);
}

$('#MyDrawsPO').on('shown.bs.modal', function (e) {
    $("#MyDrawsPO .modal-body").load("drawsConfig.html", function() {
        var type = CatConfigHeader.plt_Category.substr(0, 1);
        if ("P" == type) {
            $('#div_file').hide();
        } else {
            $('#div_file').show();
            queryFileList()
        }
        initData();
        // createFileLine();
        $('#SavePlt').off('click');
        $('#SavePlt').on('click', function () {
            savePlt(0);
        });
        $('#SubmitPlt').off('click');
        $('#SubmitPlt').on('click', function () {
            savePlt(1);
            // window.open(config.contextConfig.oaCatUrl + CatConfigHeader.plt_Order_Header_Id + "&workcode=" + logonUser.employeeNo);
        });
        $('#EditPltDetail').off('click');
        $('#EditPltDetail').on('click', function () {
            var type = CatConfigHeader.plt_Category.substr(0, 1);
            if ("P" == type) {
                savePlt(2);
                // $('#OptionsCal').modal('show');
            } else {
                $('#OptionsCal_D').modal('show');
            }
        });
        $('#ViewPltDetail').off('click');
        $('#ViewPltDetail').on('click', function () {
            loadConfigDetail();
        });
        $('#ViewOA').off('click');
        $('#ViewOA').on('click', function () {
            if (null != CatConfigHeader.viewoa_Link)
                window.open(CatConfigHeader.viewoa_Link);
            else
                window.parent.warring("OA流程url为空，请检查");
        });
        $('#bu_fileUpload').off('click');
        $('#bu_fileUpload').on('click', function () {
            $('#span_fileName').html("");
            $('.progress-bar').css("width", "0%");
            $('#upload_plt_order_header_id').val(CatConfigHeader.plt_Order_Header_Id);
            $('#uploadModal').modal("show");
        });
    });
});


$('#OptionsCal').on('shown.bs.modal', function (e) {
    $("#OptionsCal .modal-body").load("drawsConfig_header.html", function() {
        $('#NewNum').val(CatConfigHeader.new_Num);
        $('#NewStdNum').val(CatConfigHeader.new_Std_Num);
        $('#InkNum').val(CatConfigHeader.ink_Num);
        $('#InkStdNum').val(CatConfigHeader.ink_Std_Num);
        $('#TypesetTotalPrice').val(CatConfigHeader.plt_Total_Price); // 2017-03-07两个字段反了
        $('#PltTotalPrice').val(CatConfigHeader.typeset_Total_Price);
        $('#OptionsCalTitle').html("填写文印要求（出图文印单：" + CatConfigHeader.plt_Num + "）");
    });
});

$('#OptionsCal_D').on('shown.bs.modal', function (e) {
    $('#print_size').val(CatConfigHeader.print_size);
    $('#his_typeset').val(CatConfigHeader.his_typeset);
    $('#bindiing_type').val(CatConfigHeader.bindiing_type);
    $('#is_inparts').val(CatConfigHeader.is_inparts);
    $('#plt_comment').val(CatConfigHeader.plt_comment);
    $('#TypesetTotalPrice').val(CatConfigHeader.plt_Total_Price); // 2017-03-07两个字段反了
    $('#PltTotalPrice').val(CatConfigHeader.typeset_Total_Price);
    $('#OptionsCal_DTitle').html("填写文印要求（出图文印单：" + CatConfigHeader.plt_Num + "）");
    $('#save_doc').off('click');
    $('#save_doc').on('click', function () {
        CatConfigHeader.print_size = $('#print_size').val();
        CatConfigHeader.his_typeset = $('#his_typeset').val();
        CatConfigHeader.bindiing_type = $('#bindiing_type').val();
        CatConfigHeader.is_inparts = $('#is_inparts').val();
        CatConfigHeader.plt_comment = $('#plt_comment').val();
        // $('#OptionsCal_D').modal("hide");
        saveSf();
    });
    $('#bu_addSf').off('click');
    $('#bu_addSf').on('click', function () {
        var d = {plt_print_set_id: "", plt_order_header_id: "", charge_item: "", print_set_size: "", amount: "", unit: "", unit_price: "", total_price: ""};
        addTableRow_sf(d);
    });
    querySfList();
});

var sfNames = [];
var sfNames_size = [];
var sfNames_unit = [];
var sfNames_unit_price = [];

function querySfList() {
    $.getJSON(config.baseurl + "/api/draw/getSfList", function (data) {
        data.forEach(function (d) {
            sfNames.push(d.charge_item);
            sfNames_size.push(d.charge_item + "|#|" + d.print_set_size);
            sfNames_unit.push(d.charge_item + "|#|" + d.unit.split(",")[0]);
            sfNames_unit_price.push(d.charge_item + "|#|" + d.unitPrice);
        });
        // 查询收费项表格
        $.ajax({
            url: config.baseurl + "/api/draw/getPltRint",
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",   //返回格式为json
            data: {
                plt_order_header_id: CatConfigHeader.plt_Order_Header_Id
            },
            type: "GET",   //请求方式
            beforeSend: function () {
                window.parent.showLoading();
            },
            success: function (data) {
                $('#t_sf tbody').empty();
                data.forEach(function (d) {
                    addTableRow_sf(d);
                });
            },
            complete: function () {
                window.parent.closeLoading();
            },
            error: function () {
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    });
}

function saveSf() {
    var v = $('input[name="plt_print_set_id"]');
    var v2 = $('input[name="plt_order_header_id"]');
    var v3 = $('select[name="charge_item"]');
    var v4 = $('select[name="print_set_size"]');
    var v5 = $('input[name="amount"]');
    var v6 = $('input[name="unit"]');
    var v7 = $('input[name="unit_price"]');
    var v8 = [];
    $('#t_sf tbody tr').each(function () {
        v8.push($(this).find('td').eq(5));
    });

    for (var i = 0; i < v.length; i++) {
        $.ajax({
            url: config.baseurl + "/api/draw/addPltRint",
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",   //返回格式为json
            data: {
                plt_print_set_id: $.trim(v[i].value),
                plt_order_header_id: $.trim(v2[i].value),
                charge_item: $.trim(v3[i].value),
                print_set_size: $.trim(v4[i].value),
                amount: $.trim(v5[i].value),
                unit: $.trim(v6[i].value.trim()),
                unit_price: $.trim(v7[i].value),
                total_price: $.trim($(v8[i]).html())
            },
            async: false,
            type: "POST",   //请求方式
            beforeSend: function () {
                window.parent.showLoading();
            },
            success: function (data) {
                if ("0000" == data.returnCode) {
                    if (i == (v.length - 1)) {
                        window.parent.success("收费项保存成功！");
                        querySfList();
                    }
                } else {
                    window.parent.error(data.returnMsg);
                }
            },
            complete: function () {
                window.parent.closeLoading();
            },
            error: function () {
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }
}

function setPrintCount() {
    var sendOwner = $('#SendOwner').val();
    sendOwner = ("" == sendOwner ? 0 : sendOwner);
    var sgService = $('#SgService').val();
    sgService = ("" == sgService ? 0 : sgService);
    var archiveFlag = $('#ArchiveFlag').val();
    archiveFlag = ("1" == archiveFlag ? 1 : 0);
    // $('#ContractNum').val(parseInt(sendOwner) + parseInt(sgService));
    $('#TotalNum').val(parseInt(sendOwner) + parseInt(sgService) + archiveFlag);
}

function savePlt(v) {
    // 如果是文本，且附件表格有数据，则保存
    var files = $('#t_file tbody tr');
    if (0 < files.length) {
        saveFile();
    }

    var type = CatConfigHeader.plt_Category.substr(0, 1);

    var pltContent = $('#PltContent').val();
    var projectResBy = $('#ProjectManager').val();
    var printChannel = $('#Attribute5').val();
    if ("" == printChannel) {
        window.parent.warring("请选择文印途径");
        return;
    }
    var sendOwner = $('#SendOwner').val();
    if ("" == sendOwner) {
        window.parent.warring("文印选项，请填写发送业主");
        return;
    }
    var sgService = $('#SgService').val();
    if ("" == sgService) {
        window.parent.warring("文印选项，请填写施工服务");
        return;
    }
    var archiveFlag = $('#ArchiveFlag').val();
    if ("" == archiveFlag) {
        window.parent.warring("文印选项，请选择是否归档");
        return;
    }
    var paperType = $('#PaperType').val();
    if ("" == paperType) {
        window.parent.warring("文印选项，请选择类型");
        return;
    }
    var projectManager = $('#ProjectManager').val();
    if ("" == projectManager) {
        window.parent.warring("请选择项目负责人");
        return;
    }

    var print_size;
    var his_typeset;
    var bindiing_type;
    var is_inparts;
    var plt_comment;
    if ("P" != type) {
        print_size = $('#print_size').val();
        his_typeset = $('#his_typeset').val();
        bindiing_type = $('#bindiing_type').val();
        is_inparts = $('#is_inparts').val();
        plt_comment = $('#plt_comment').val();
        if ("" == print_size) {
            window.parent.warring("请选择文印幅面");
            return;
        }
        if ("" == his_typeset) {
            window.parent.warring("请选择需要排版");
            return;
        }
        if ("" == bindiing_type) {
            window.parent.warring("请选择装订要求");
            return;
        }
        if ("" == is_inparts) {
            window.parent.warring("请选择是否分册");
            return;
        }
    }

    // 提交操作，先检查
    if (1 == v && "P" == type) {
        if ("" == $('#SpecManager').val()) {
            window.parent.warring("专业负责人没有对应的人员，请通知相关人员维护对应的人员信息");
            return;
        }
        if ("" == $('#ProjectSecretary').val()) {
            window.parent.warring("项目秘书没有对应的人员，请通知相关人员维护对应的人员信息");
            return;
        }
        // window.open(config.contextConfig.oaCatUrl + CatConfigHeader.plt_Order_Header_Id + "&workcode=" + logonUser.employeeNo);
    }

    if ("P" == type) {
        $.ajax({
            url: config.baseurl + "/api/draw/submitDrawprints",
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",   //返回格式为json
            data: {
                id: CatConfigHeader.plt_Order_Header_Id,
                pltContent: pltContent,
                projectResBy: projectResBy,
                printChannel: printChannel,
                sendOwner: sendOwner,
                sgService: sgService,
                archiveFlag: archiveFlag,
                paperType: paperType,
                operationType: (0 == v || 2 == v) ? "SAVE" : "SUBMIT"
            },
            type: "POST",   //请求方式
            beforeSend: function () {
                window.parent.showLoading();
            },
            success: function (data) {
                if ("0000" == data.returnCode) {
                    if (0 == v) {
                        window.parent.success("保存成功！");
                    } else if (2 == v) {
                        window.parent.showLoading();
                        $.getJSON(config.baseurl + "/api/draw/searchDrawPrintInfo?id=" + CatConfigHeader.plt_Order_Header_Id, function (data) {
                            window.parent.closeLoading();
                            CatConfigHeader = data;
                            $('#OptionsCal').modal('show');
                        });
                    } else {
                        window.parent.success("提交成功！");
                        window.open(data.db_url);
                        $('#MyDrawsPO').modal("hide");
                        $('#bu_queryData').trigger('click');
                    }
                } else {
                    window.parent.error(data.returnMsg);
                }
            },
            complete: function () {
                window.parent.closeLoading();
            },
            error: function () {
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    } else {
        $.ajax({
            url: config.baseurl + "/api/draw/submitTextprints",
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",   //返回格式为json
            data: {
                id: CatConfigHeader.plt_Order_Header_Id,
                pltContent: pltContent,
                projectResBy: projectResBy,
                printChannel: printChannel,
                sendOwner: sendOwner,
                sgService: sgService,
                archiveFlag: archiveFlag,
                print_size: print_size,
                his_typeset: his_typeset,
                bindiing_type: bindiing_type,
                is_inparts: is_inparts,
                plt_comment: plt_comment,
                operationType: 0 == v ? "SAVE" : "SUBMIT"
            },
            type: "POST",   //请求方式
            beforeSend: function () {
                window.parent.showLoading();
            },
            success: function (data) {
                if ("0000" == data.returnCode) {
                    if (0 == v) {
                        window.parent.success("保存成功！");
                    } else {
                        window.parent.success("提交成功！");
                        window.open(data.db_url);
                        $('#MyDrawsPO').modal("hide");
                        $('#bu_queryData').trigger('click');
                    }
                } else {
                    window.parent.error(data.returnMsg);
                }
            },
            complete: function () {
                window.parent.closeLoading();
            },
            error: function () {
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }

}

function loadConfigDetail() {
    window.parent.showLoading();
    $.getJSON(config.baseurl + "/api/draw/searchDrawprintLines?id=" + CatConfigHeader.plt_Order_Header_Id, function (data) {
        window.parent.closeLoading();
        configDetail_set = data;
        $('#PoDetailTitle').html("查看文印明细（出图文印单：" + CatConfigHeader.plt_Num + "）");
        $('#PoDetail').modal('show');
    });
}

$('#PoDetail').on('shown.bs.modal', function (e) {
    $("#PoDetail .modal-body").load("drawsConfig_detail.html", function() {
        initPltLines();
        $('#PltDownload').off('click');
        $('#PltDownload').on('click', function () {
            // 下载打印包
            $.ajax({
                url: config.baseurl + "/api/draw/getZipFileByEcm",
                contentType: 'application/x-www-form-urlencoded',
                dataType: "json",   //返回格式为json
                data: {
                    plt_order_header_id: CatConfigHeader.plt_Order_Header_Id,
                    plt_num: CatConfigHeader.plt_Num,
                    plt_category: CatConfigHeader.plt_Category
                },
                type: "POST",   //请求方式
                beforeSend: function () {
                    window.parent.showLoading();
                },
                success: function (data) {
                    if ("0000" == data.returnCode) {
                        if ("dev" == config.evn) {
                            if (null != data.filePath) {
                                var tmp = data.filePath.split(".com");
                                window.open(config.contextConfig.catPdfUrl + tmp[1]);
                            }
                        } else {
                            window.open(data.filePath);
                        }
                    } else {
                        window.parent.error(data.returnMsg);
                    }
                },
                complete: function () {
                    window.parent.closeLoading();
                },
                error: function () {
                    window.parent.closeLoading();
                    window.parent.error(ajaxError_loadData);
                }
            });
        });
    });
});

var t_configDetail;
var configDetail_set = [];
function initPltLines() {
    $('#pltLines_container').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="pltLines_table"></table>');
    t_configDetail = $('#pltLines_table').DataTable({
        //设置数据源
        "data": configDetail_set,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        //定义列名
        "columns": [
            {"title": "序号", "data": null},
            {"title": "项目编号", "data": "projectNum"},
            {"title": "图纸名称", "data": "ddoctitle"},
            {"title": "图号编号", "data": "ddocname"},
            {"title": "版本号", "data": "pjtReviseNum"},
            {"title": "自然张数", "data": "commQty"},
            {"title": "折A1张数", "data": "stdQty"},
            {"title": "本批文印份数", "data": "totalNum"}
        ],
        //控制隐藏列
        "columnDefs": [
            {
                "orderable": false,
                "targets": 0,
                "width": 32
            },
            {
                "orderable": false,
                "targets": 1,
                "width": 100
            },
            {
                "orderable": false,
                "targets": 2,
                "width": 150
            },
            {
                "searchable": false,
                "orderable": false,
                "targets": 3
            },
            {
                "targets": 4
            }
        ],
        //不分页，加入垂直导航条
        "scrollY": "400",
        "scrollCollapse": true,
        "paging": false,
        "searching": false,
        "order": [],
        "createdRow": function (row, data, dataIndex) {
            $(row).children('td').eq(0).attr('style', 'text-align: center;');
        },
        "initComplete": function () {
            $('#pltLines_table_wrapper').css("cssText", "margin-top: -5px;");
        }
    });

    t_configDetail.on( 'order.dt search.dt', function () {
        t_configDetail.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );
    } ).draw();
}

var getSel = function (data, val) {
    var select = $("<select name='charge_item' class='form-control input-sm' placeholder='请选择' onchange='getSel2Begin(this);'" + (editFlag ? 'disabled' : '') + "></select>");
    select.append("<option value=''>请选择</option>");
    for (var i = 0; i < data.length; i++) {
        select.append("<option value='" + data[i] + "'>" + data[i] + "</option>");
    }
    select.val(val);
    if (data && 1 == data.length) {
        select.find("option:eq(1)").attr("selected",true);
    }
    return select;
}

function getSel2Begin(sel) {
    var sel2 = getSel2(sel);
    $(sel).parent().next().html(sel2);
    $(sel).parent().next().next().find('input')[0].value = "";
    $(sel).parent().next().next().next().find('input')[0].value = "";
    $(sel).parent().next().next().next().next().find('input')[0].value = "";
    $(sel).parent().next().next().next().next().next().text("");
    // getSel3(sel2.get(0));
}

function getSel2(sel, val) {
    var sel2Value = "";
    sfNames_size.forEach(function (d) {
        if (0 <= d.indexOf(sel.value))
            sel2Value = d.split("|#|")[1];
    });
   var data = sel2Value.split(",");
    var select = $("<select name='print_set_size' class='form-control input-sm' placeholder='请选择' onchange='getSel3(this, true);'" + (editFlag ? 'disabled' : '') + "></select>");
    select.append("<option value=''>请选择</option>");
    for (var i = 0; i < data.length; i++) {
        if ("" != data[i])
            select.append("<option value='" + data[i] + "'>" + data[i] + "</option>");
    }
    select.val(val);
    return select;
}

function getSel3(sel, flag) {
    var $td = $(sel).parent();
    var sel1 = $td.prev().find('select')[0];
    var sel3Unit = "";
    var sel3UnitPriceNo = $(sel).get(0).selectedIndex;
    var sel3UnitPriceValue = "";
    if (0 != sel3UnitPriceNo) {
        sel3UnitPriceNo--;
    }
    sfNames_unit.forEach(function (d) {
        if (0 <= d.indexOf(sel1.value)) {
            sel3Unit = d.split("|#|")[1];
        }
    });
    sfNames_unit_price.forEach(function (d) {
        if (0 <= d.indexOf(sel1.value)) {
            var t = d.split("|#|")[1];
            sel3UnitPriceValue = t.split(",")[sel3UnitPriceNo];
        }
    });
    if (0 != $(sel).get(0).selectedIndex) {
        var $td = $(sel).parent();
        $td.next().next().find('input')[0].value = sel3Unit;
        $td.next().next().next().find('input')[0].value = sel3UnitPriceValue;
        if (flag) {
            $td.next().next().next().next().text("");
            $td.next().find('input')[0].value = "";
        }
    } else {
        var $td = $(sel).parent();
        $td.next().next().find('input')[0].value = "";
        $td.next().next().next().find('input')[0].value = "";
        if (flag) {
            $td.next().next().next().next().text("");
            $td.next().find('input')[0].value = "";
        }
    }
}

function addTableRow_sf(d) {
    var sel1 = getSel(sfNames, d.charge_item);
    var table = $('#t_sf');
    var row = $('<tr></tr>');
    var td = $('<td>' + '<input type="hidden" class="form-control input-sm" name="plt_print_set_id" value="' + d.plt_print_set_id + '"/>'
        + '<input type="hidden" class="form-control input-sm" value="' + CatConfigHeader.plt_Order_Header_Id + '" name="plt_order_header_id" /></td>');
    var td2 = $('<td></td>');
    var td3 = $('<td><input type="text" class="form-control input-sm" name="amount" onkeyup="setCheck(this);" value="' + d.amount + '"'  + (editFlag ? "disabled" : "") + '/></td>');
    var td4 = $('<td><input readonly type="text" class="form-control input-sm" name="unit" value="' + d.unit + '"/></td>');
    var td5 = $('<td><input readonly type="text" class="form-control input-sm" name="unit_price" onkeyup="setCheck2(this);" value="' + d.unit_price + '"/></td>');
    var td6 = $('<td style="vertical-align: middle;text-align: right;">' + (d.total_price ? d.total_price : "") + '</td>');
    var td7 = $('<td style="vertical-align: middle; text-align: center;">' + (editFlag ? "" : '<a href="#1" onclick="delSf(this);">删除</a>') + '</td>');
    row.append(td).append(td2).append(td3).append(td4).append(td5).append(td6).append(td7);
    table.append(row);
    td.append(sel1);

    var sel2 = getSel2(sel1.get(0), d.print_set_size);
    if ("" != d.charge_item) {
        td2.append(sel2);
    }
    if ("" != d.print_set_size) {
        getSel3(sel2.get(0));
    }
    setDetailAll();
}

function delSf(a) {
    var $tr = $(a).parent().parent();
    var v = $tr.find('input')[0].value;
    if ("" != v) {
        $.ajax({
            url: config.baseurl + "/api/draw/delPltRint",
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",   //返回格式为json
            data: {
                plt_print_set_id: v
            },
            type: "POST",   //请求方式
            beforeSend: function () {},
            success: function (data) {
                if ("0" == data.db_state)
                    $tr.remove();
            },
            complete: function () {},
            error: function () {
                window.parent.error(ajaxError_loadData);
            }
        });
    } else {
        $tr.remove();
    }
}

function fmoney(s, n) //s:传入的float数字 ，n:希望返回小数点几位
{
    n = n > 0 && n <= 20 ? n : 2;
    s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
    var l = s.split(".")[0].split("").reverse(),
        r = s.split(".")[1];
    t = "";
    for(i = 0; i < l.length; i ++ )
    {
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
    }
    return t.split("").reverse().join("") + "." + r;
}

function rmoney(s)
{
    if ("" == s)
        return "";
    return parseFloat(s.replace(/[^\d\.-]/g, ""));
}

function setCheck(input) {
    input.value=input.value.replace(/[^\d]/g,'')
    var $td = $(input).parent();
    setDetailPrice($td, $td.next().next());
}

function setCheck2(input) {
    input.value=input.value.replace(/[^\-?\d.]/g,'');
    var $td = $(input).parent();
    setDetailPrice($td.prev().prev(), $td);
}

function setDetailPrice(td, td2) {
    var input = td.find('input');
    var input2 = td2.find('input');
    var v = input[0].value;
    var v2 = input2[0].value;
    if ("" == v.trim())
        v = 0;
    if ("" == v2.trim())
        v2 = 0;
    if ("" != v && "" != v2) {
        td2.next().html(fmoney((rmoney(v) * rmoney(v2)), 2));
    }
    setDetailAll();
}

function setDetailAll() {
    var sum = 0;
    $('#t_sf tbody tr').each(function () {
        var v = $(this).find('td').eq(5).text();
        sum += rmoney(v);
    });
    $('#TypesetTotalPrice').val(fmoney(sum, 2));
}

$('#do_uploadFile').click(function(){
    if ("" == $('#templateFile').val()) {
        window.parent.warring("请选择需要上传的文件！");
        return;
    }
    var formData = new FormData($('#templateUploadForm')[0]);
    $.ajax({
        url: config.baseurl +"/api/draw/uploadFile",  //server script to process data
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
            addTableRow_file(data, "save");
            $('#uploadModal').modal("hide");
        },
        error: function () {
            window.parent.error("上传失败，请稍后再试！")
        },
        // Form数据
        data: formData,
        //Options to tell JQuery not to process data or worry about content-type
        cache: false,
        contentType: false,
        processData: false
    });
});

function saveFile() {
    var v = $('input[name="file_p_plt_order_header_id"]');
    var v2 = $('input[name="file_id"]');
    var v3 = $('input[name="title"]');
    var v4 = $('input[name="comments"]');
    var v5 = $('input[name="doc_info"]');
    var v6 = [];
    $('#t_file tbody tr').each(function () {
        v6.push($(this).find('td').eq(4));
    });

    for (var i = 0; i < v.length; i++) {
        $.ajax({
            url: config.baseurl + "/api/draw/addPltAtt",
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",   //返回格式为json
            data: {
                plt_order_header_id: $.trim(v[i].value),
                p_file_id: $.trim(v2[i].value),
                p_title: $.trim(v3[i].value),
                p_comments: $.trim(v4[i].value),
                p_doc_info: $.trim(v5[i].value),
                creation_date: $.trim($(v6[i]).html())
            },
            async: false,
            type: "POST",   //请求方式
            beforeSend: function () {
                window.parent.showLoading();
            },
            success: function (data) {
                if ("0000" == data.returnCode) {
                    if (i == (v.length - 1)) {
                        window.parent.success("附件保存成功！");
                        queryFileList();
                    }
                } else {
                    window.parent.error(data.returnMsg);
                    if (i == (v.length - 1)) {
                        queryFileList();
                    }
                }

            },
            complete: function () {
                window.parent.closeLoading();
            },
            error: function () {
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }
}

function queryFileList() {
    $.ajax({
        url: config.baseurl + "/api/draw/getPltAtt",
        contentType: 'application/x-www-form-urlencoded',
        dataType: "json",   //返回格式为json
        data: {
            plt_order_header_id: CatConfigHeader.plt_Order_Header_Id
        },
        type: "GET",   //请求方式
        beforeSend: function () {
            window.parent.showLoading();
        },
        success: function (data) {
            $('#t_file tbody').empty();
            data.forEach(function (d) {
                addTableRow_file(d, "");
            });
        },
        complete: function () {
            window.parent.closeLoading();
        },
        error: function () {
            window.parent.closeLoading();
            window.parent.error(ajaxError_loadData);
        }
    });
}

function addTableRow_file(d, flag) {
    var status = CatConfigHeader.plt_Status;
    var v = true;
    if("APPROVED" == status || "APPROVING" == status || "SIGNET_APPROVED" == status || "SET_APPROVED" == status || "COMPLETED" == status) {
        v = true;
    } else{
        v = false;
    }

    var table = $('#t_file');
    var row = $('<tr></tr>');
    var td = $('<td>' + '<input type="hidden" class="form-control input-sm" name="file_p_plt_order_header_id" value="' + CatConfigHeader.plt_Order_Header_Id + '"/>'
        + '<input type="hidden" class="form-control input-sm" value="' + (d.file_id ? d.file_id : "") + '" name="file_id" />'
        + '<input type="text" class="form-control input-sm" name="title" value="' + d.title + '"/></td>');
    var td2 = $('<td><input type="text" class="form-control input-sm" name="comments" value="' + d.comments + '"/></td>');
    var td3 = $('<td>' + (d.attribute1 ? d.attribute1 : "") + '</td>');
    var td4 = $('<td>' + (d.author ? d.author : "") + '</td>');
    var td5 = $('<td>' + (d.creation_date ? d.creation_date : "") + '</td>');
    var td6 = $('<td style="vertical-align: middle; text-align: center;"><input type="hidden" class="form-control input-sm" name="doc_info" value="' + d.doc_info + '"/><a href="#1" onclick="downFile(this);">下载</a></td>');
    var td7 = $('<td style="vertical-align: middle; text-align: center;"><a name="a_link_fileDel" href="#1" onclick="delFile(this);">删除</a></td>');
    row.append(td).append(td2).append(td3).append(td4).append(td5).append(td6).append(td7);
    table.append(row);

    if (v)
        $('a[name="a_link_fileDel"]').remove();
    $('input[name="title"]').attr("disabled", v);
    $('input[name="comments"]').attr("disabled", v);
    if ("save" == flag) {
        saveFile();
    }
}

function delFile(a) {
    var $tr = $(a).parent().parent();
    var v = $tr.find('input')[0].value;
    var v2 = $tr.find('input')[1].value;
    if ("" != v && "" != v2) {
        $.ajax({
            url: config.baseurl + "/api/draw/delPltAtt",
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",   //返回格式为json
            data: {
                p_plt_order_header_id: v,
                p_file_id: v2
            },
            type: "POST",   //请求方式
            beforeSend: function () {},
            success: function (data) {
                if ("0" == data.db_state)
                    $tr.remove();
            },
            complete: function () {},
            error: function () {
                window.parent.error(ajaxError_loadData);
            }
        });
    } else {
        $tr.remove();
    }
}

function downFile(a) {
    var $td = $(a).parent();
    var v = $td.find('input')[0].value;
    var form = $("<form>");   //定义一个form表单
    form.attr('style', 'display:none');   //在form表单中添加查询参数
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action',  config.baseurl + "/api/draw/downloadFile");
    var input1 = $('<input>');
    input1.attr('type', 'hidden');
    input1.attr('name', 'path');
    input1.attr('value', v);
    $('body').append(form);  //将表单放置在web中
    form.append(input1);   //将查询参数控件提交到表单上
    form.submit();
}

function progressHandlingFunction(e){
    if(e.lengthComputable){
        $('.progress-bar').css("width", e.loaded + "%");
    }
}

$('#templateFile').change(function(){
    var file = this.files[0];
    var fileType = file.type;
    $('#do_uploadFile').attr("disabled", false);

    var file_path = getPath($(this).get(0));
    if (0 <= file_path.indexOf("fakepath")) {
        file_path = file_path.substr(file_path.lastIndexOf("\\") + 1, file_path.length);
    }
    $('#span_fileName').html(file_path);
    // if (0 <= fileType.indexOf("word") && 0 <= fileType.indexOf("template")) {
    //
    // } else {
    //     window.parent.warring("请上传word模板文件(.dotm或者.dotx)！");
    //     $('#do_uploadFile').attr("disabled", true);
    //     $('#span_fileName').html("");
    // }

});

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