/**
 * Created by Zach on 16/5/23.
 */
var queryBars;
var queryBars_2;
var projectRoles;
var specialtys;
var xmlTemplates;
var systemUis;
var nEditing = null;
var config = new nerinJsConfig();

$(document).keyup(function(event){
    if (null != nEditing)
        return;
    switch(event.keyCode) {
        case 13:
            $('#bu_queryData').trigger("click");
    }
});

$(function () {
    $('#bu_resetQuery').on('click', function () {
        if ($('#b_taskTypeAll').hasClass('active')) {
            $('#b_taskTypeAll').trigger("click");
        } else {
            $('#b_taskTypeAll').trigger("click");
            $('#b_taskTypeAll').trigger("click");
        }

        if ($('#b_templateStatusAll').hasClass('active')) {
            $('#b_templateStatusAll').trigger("click");
        } else {
            $('#b_templateStatusAll').trigger("click");
            $('#b_templateStatusAll').trigger("click");
        }
    });

    var dataSet = [];

    initQueryBars();
    queryList();

    // var createSpecialtySel = function (val) {
    //     var select = $("<select name='sel_specialty' class='form-control' placeholder='请选择'></select>");
    //     select.append("<option value=''>请选择</option>");
    //     for (var i = 0; i < specialtys.length; i++) {
    //         select.append("<option value='" + specialtys[i].specialty + "'>" + specialtys[i].specialtyName + "</option>")
    //     }
    //     select.val(val);
    //     return select;
    // }

   function initQueryBars(val) {
       window.parent.showLoading();
       projectRoles = [];
       specialtys = [];
       xmlTemplates = [];
       systemUis = [];

       //查询条件，文本类型
       queryBars = [{"taskType": "a", "taskTypeName": "开工报告"}, {"taskType": "KXXYJ", "taskTypeName": "可行性研究"}
           , {"taskType": "c", "taskTypeName": "初步设计"}, {"taskType": "d", "taskTypeName": "安全专篇"}
           , {"taskType": "e", "taskTypeName": "环保专篇"}, {"taskType": "f", "taskTypeName": "消防专篇"}
           , {"taskType": "g", "taskTypeName": "项目总结"}, {"taskType": "h", "taskTypeName": "规划"}
           , {"taskType": "i", "taskTypeName": "管理计划"}, {"taskType": "j", "taskTypeName": "实施计划"}
           , {"taskType": "k", "taskTypeName": "总承包月度报告"}];

       // 查询条件，状态
       queryBars_2 = [{"templateStatus": "NEW", "templateStatusName": "新建"}, {"templateStatus": "EFFECTIVE", "templateStatusName": "有效"}
           , {"templateStatus": "INVALID", "templateStatusName": "无效"}];

       $.getJSON(config.baseurl + "/api/templateRest/taskTypeList", function (data) {
           queryBars = data.dataSource;
           var queryBarsHtml = "<div class='btn-group' data-toggle='buttons'><label class='btn btn-primary2 btn-xs' style='margin-bottom: 2px;' id='b_taskTypeAll'><input type='checkbox' name='b_taskTypeAll' value='all'>全选</label>";
           queryBars.forEach(function (data) {
               queryBarsHtml += "<label name='l_taskType' class='btn btn-primary2 btn-xs' style='margin-bottom: 2px;'><input type='checkbox' name='taskTypes' value='" + data.taskType + "'>" + data.taskTypeName + "</label>";
           });
           queryBarsHtml += " </div>";
           $('#div_taskType').append(queryBarsHtml);

           $('#b_taskTypeAll').on('click', function () {
               if (0 == $("input[name='b_taskTypeAll']:checked").length) {
                   $("label[name='l_taskType']").each(function () {
                       if (!$(this).hasClass('active')) {
                           $(this).trigger("click");
                       } else {

                       }
                   });
               } else {
                   $("label[name='l_taskType']").each(function () {
                       if ($(this).hasClass('active')) {
                           $(this).trigger("click");
                       } else {

                       }
                   });
               }
               // var chk_value = [];
               // $("input[name='b_taskType']:checked").each(function(){
               //     chk_value.push($(this).val());
               // });
               // console.log(chk_value);
           });
       });

       $.getJSON(config.baseurl + "/api/templateRest/templateStatusList", function (data) {
           queryBars_2 = data.dataSource;
           var queryBarsHtml_2 = "<div class='btn-group' data-toggle='buttons'><label class='btn btn-primary2 btn-xs' style='margin-bottom: 2px;' id='b_templateStatusAll'><input type='checkbox' name='b_templateStatusAll' value='all'>全选</label>&nbsp;";
           queryBars_2.forEach(function (data) {
               queryBarsHtml_2 += "<label name='l_templateStatus' class='btn btn-primary2 btn-xs' style='margin-bottom: 2px;'><input type='checkbox' name='templateStatus' value='" + data.templateStatus + "'>" + data.templateStatusName + "</label>&nbsp;";
           });
           queryBarsHtml_2 += " </div>";
           $('#div_templateStatus').append(queryBarsHtml_2);

           $('#b_templateStatusAll').on('click', function () {
               if (0 == $("input[name='b_templateStatusAll']:checked").length) {
                   $("label[name='l_templateStatus']").each(function () {
                       if (!$(this).hasClass('active')) {
                           $(this).trigger("click");
                       } else {

                       }
                   });
               } else {
                   $("label[name='l_templateStatus']").each(function () {
                       if ($(this).hasClass('active')) {
                           $(this).trigger("click");
                       } else {

                       }
                   });
               }
           });
       });

       window.parent.closeLoading();

       $.getJSON(config.baseurl + "/api/templateRest/projectRoleList", function (data) {
           projectRoles = data.dataSource;
       });
       $.getJSON(config.baseurl + "/api/templateRest/specialtyList", function (data) {
           specialtys = data.dataSource;
           specialtys.forEach(function (data) {
               data.specialtyName = data.specialty.substring(1) + data.specialtyName;
           });
       });
       $.getJSON(config.baseurl + "/api/templateRest/xmlTemplateList", function (data) {
           xmlTemplates = data.dataSource;
       });
       $.getJSON(config.baseurl + "/api/templateRest/systemUiList", function (data) {
           systemUis = data.dataSource;
            // for (var i = 0; i < systemUis.length; i++) {
            //     if ("7" == systemUis[i].systemUiCode) {
            //         systemUis.splice(i);
            //     }
            // }
       });

   }

    var t;
    function queryList() {
        hide_div_continueOrcancel();
        $('#dataTables-listAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-data"></table>');

        t = $('#dataTables-data').DataTable({
            "searching": false,
            "processing": true,
            // "scrollY": "500px",
            // "scrollCollapse": true,
            "data": dataSet,
            "pagingType": "full_numbers",
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "<input type='checkbox' name='ch_listAll' id='ch_listAll' onclick='javascript:setCheckAll();' />", "data": null},
                {"title": "序号", "data": null},
                {"title": "模板名称", "data": "templateName"},
                {"title": "模板说明", "data": "templateDescription"},
                {"title": "文本类型", "data": "taskTypeName"},
                {"title": "状态", "data": "templateStatusName"},
                {"title": "附件", "data": "attribute5"}
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
                "width": 300,
                "targets": 2,
                "render": function (data, type, full, meta) {
                    return "<a name='a_linkTemplate' href='#1'>" + data + "</a>";
                }
            },{
                "targets": 5,
                "width": 70
            },{
                "targets": 4,
                "width": 150
            },{
                "targets": 3,
                "width": 350
            },{
                "targets": 6,
                "render": function (data, type, full, meta) {
                    if (null == data)
                        return "";
                    else
                        return "<a target='_blank' href='/api/templateRest/downloadTemplateFile?templateHeaderId=" + full.templateId + "'>" + data + "</a>";
                }
            }
            ],
            "order": [[2, 'asc']],
            "createdRow": function( row, data, dataIndex ) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
                $(row).children('td').eq(1).attr('style', 'text-align: center;');
            },
            "initComplete": function () {
                $('#dataTables-data_wrapper').css("cssText", "margin-top: -5px;");
                $('#dataTables-data_length').insertBefore($('#dataTables-data_info'));
                $('#dataTables-data_length').addClass("col-sm-4");
                $('#dataTables-data_length').css("paddingLeft", "0px");
                $('#dataTables-data_length').css("paddingTop", "5px");
                $('#dataTables-data_length').css("maxWidth", "130px");
                t.page(now_pageNo).draw('page');
                now_pageNo = 0;
            }
        });

        $('#dataTables-data tbody').on('click', 'a[name=a_linkTemplate]', function () {
            var userTable = $('#dataTables-data').DataTable();
            var data = userTable.rows( $(this).parents('tr') ).data();
            loadChapter(data[0].templateId);
            editAbled = false;
        });

        $('#dataTables-data tbody').on('dblclick', 'tr', function () {
            var userTable = $('#dataTables-data').DataTable();
            var data = userTable.rows( $(this)).data();
            if (undefined != data[0].templateId && "" != data[0].templateId) {
                loadChapter(data[0].templateId);
                editAbled = true;
            }
        });

        $('#dataTables-data tbody').on('click', 'td', function () {
            var $tr = $(this).parent("tr");
            if ( $tr.hasClass('dt-select') ) {
                $tr.removeClass('dt-select');
                var check = $tr.find("input[name='ch_list']");
                check.prop("checked", false);
            } else {
                $tr.addClass('dt-select');
                var check = $tr.find("input[name='ch_list']");
                check.prop("checked", true);
            }
            // else {
            //     $('#dataTables-data').DataTable().$('tr.dt-select').removeClass('dt-select');
            //     $tr.addClass('dt-select');
            // }
            // var tdNo = $(this).parents("tr").find("td").index($(this));
            // if (0 != tdNo) {
            //     var check = $tr.find("input[name='ch_list']");
            //     if (!check.prop("checked")) {
            //         $(this).parents("tr").css("backgroundColor", "#FFFF00");
            //         check.prop("checked", true);
            //     } else {
            //         check.prop("checked", false);
            //         $(this).parents("tr").css("backgroundColor", "");
            //     }
            // }
        });

        t.on( 'order.dt search.dt', function () {
            t.column(1, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } );
            t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = "<input type='checkbox' name='ch_list' />";
            } );
        } ).draw();
        
    }

    // dt操作
    var creater = {
        //生成一个行对象
        "createRowObj": function (json) {
            var templateId = json ? json.templateId : "";
            var fromTemplateId = json ? json.fromTemplateId : "";
            var templateName = json ? json.templateName : "";
            var templateDescription = json ? json.templateDescription : "";
            var taskType = json ? json.taskType : "";
            var taskTypeName = json ? json.taskTypeName : "";
            var templateStatus = json ? json.templateStatus : "NEW";
            var templateStatusName = json ? json.templateStatusName : "有效";
            var attribute5 = json ? json.attribute5 : "";
            return {
                "templateId": templateId,
                "fromTemplateId": fromTemplateId,
                "templateName": templateName,
                "templateDescription": templateDescription,
                "taskType": taskType,
                "taskTypeName": taskTypeName,
                "templateStatus": templateStatus,
                "templateStatusName": templateStatusName,
                "attribute5" : attribute5
            };
        },
        //input生成
        "createInputHidden": function (id, val) {
            return '<input id="' + id + '" type="hidden" value="' + val + '" />';
        },
        "createInput": function (val) {
            return '<input id="dt_templateName" type="text" value="' + val + '" class="form-control input-sm" style="width: 100%;" placeholder="请填写模板名称" />';
        },
        "createInput2": function (val) {
            return '<input id="dt_templateDescription" type="text" value="' + val + '" class="form-control input-sm" style="width: 100%;" placeholder="请填写模板说明" />';
        },
        "createSelect": function (arr, val) {
            var select = $("<select id='dt_taskType' class='form-control input-sm' placeholder='请选择'></select>");
            select.append("<option value=''>请选择</option>");
            for (var i = 0; i < arr.length; i++) {
                select.append("<option value='" + arr[i].taskType + "'>" + arr[i].taskTypeName + "</option>")
            }
            select.val(val);
            return select;
        },
        "createSelect2": function (arr, val) {
            var select = $("<select id='dt_templateStatus' class='form-control input-sm' placeholder='请选择' disabled></select>");
            select.append("<option value=''>请选择</option>");
            for (var i = 0; i < arr.length; i++) {
                select.append("<option value='" + arr[i].templateStatus + "'>" + arr[i].templateStatusName + "</option>")
            }
            select.val(val);
            return select;
        }
    };

    function valWork() {
        if (!$('#div_continueOrcancel').is(':hidden')) {
            window.parent.warring("请完成之前的操作后，再进行其他操作");
            return false;
        } else {
            return true;
        }
    }

    function show_div_continueOrcancel() {
        $('#div_continueOrcancel').show(200);
    }

    function hide_div_continueOrcancel() {
        $('#div_continueOrcancel').hide(200);
    }
    $('#div_continueOrcancel').hide();


    var doNo = 0;
    //添加一行
    $('#bu_add').click(function () {
        if (!valWork())
            return;
        if (nEditing !== null) {
            return;
        }

        var aiNew = t.row.add(creater.createRowObj(saveAsObject));
        var nRow = t.row(aiNew[0]).node();
        editRow(t, nRow);
        nEditing = nRow;
        doNo = 1;
        show_div_continueOrcancel();
    });

    //把当前行变为可编辑状态
    function editRow(oTable02, nRow) {
        var aData = oTable02.row(nRow).data();
        var jqTds = $('>td', nRow);
        $(jqTds[2]).html(creater.createInputHidden("dt_fromTemplateId", aData.fromTemplateId ? aData.fromTemplateId : "")
                + creater.createInputHidden("dt_templateId", aData.templateId)
            + creater.createInput(aData.templateName));
        $(jqTds[3]).html(creater.createInput2(aData.templateDescription ? aData.templateDescription : ""));
        $(jqTds[4]).html(creater.createSelect(queryBars, aData.taskType));
        $(jqTds[5]).html(creater.createSelect2(queryBars_2, aData.templateStatus));
        oTable02.draw(false);
    }

    // 保存行
    function saveRow(oTable02, nRow) {
        var jqInputs = $('input', nRow);
        var jqSelects = $('select', nRow);
        var rowObj = oTable02.row(nRow);
        var json = {
            "fromTemplateId": $(jqInputs[1]).val(),
            "templateId": $(jqInputs[2]).val(),
            "templateName": $(jqInputs[3]).val(),
            "templateDescription": $(jqInputs[4]).val(),
            "taskType": $(jqSelects[0]).val(),
            "taskTypeName": $(jqSelects[0]).find("option:selected").text(),
            "templateStatus": $(jqSelects[1]).val(),
            "templateStatusName": $(jqSelects[1]).find("option:selected").text(),
            "attribute5" : $(nRow).find("td:last-child").text()
        };
        var tmpobj = creater.createRowObj(json);
        rowObj.data(tmpobj).draw();
        nEditing = null;
        saveAsObject = null;
        doNo = 0;
        hide_div_continueOrcancel();
    }

    var backData = null;
    $('#bu_edit').on('click', function () {
        if (!valWork())
            return;
        if (nEditing !== null) {
            return;
        }
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();

        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        } else if (2 <= data.length) {
            window.parent.warring(valTips_workOne);
            return;
        }

        backData = data[0];
        var nRow = t.rows( $("input[name='ch_list']:checked").parents('tr') ).nodes()[0];
        editRow(t, nRow);
        nEditing = nRow;
        doNo = 2;
        show_div_continueOrcancel();
    });

    $('#bu_del').on('click', function () {
        if (!valWork())
            return;
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();

        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        }

        doNo = 3;
        show_div_continueOrcancel();
    });

    $('#bu_effective').on('click', function () {
        if (!valWork())
            return;
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        }

        doNo = 4;
        show_div_continueOrcancel();
        now_pageNo = t.page();
    });

    $('#bu_invalid').on('click', function () {
        if (!valWork())
            return;
        var data = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == data.length) {
            window.parent.warring(valTips_selOne);
            return;
        }

        doNo = 5;
        show_div_continueOrcancel();
        now_pageNo = t.page();
    });

    var saveAsObject;
    $('#bu_saveas').on('click', function () {
        var tmp = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == tmp.length) {
            window.parent.warring(valTips_selOne);
            return;
        } else if (2 <= tmp.length) {
            window.parent.warring(valTips_workOne);
            return;
        }

        saveAsObject = new Object();
        saveAsObject.fromTemplateId = tmp[0].templateId;
        saveAsObject.templateName = tmp[0].templateName;
        saveAsObject.templateDescription = tmp[0].templateDescription;
        saveAsObject.templateStatus = tmp[0].templateStatus;
        saveAsObject.templateStatusName = tmp[0].templateStatusName;
        saveAsObject.taskType = tmp[0].taskType;
        saveAsObject.taskTypeName = tmp[0].taskTypeName;
        saveAsObject.templateId = "";

        $('#bu_add').trigger("click");
    });

    $('#bu_continue_cancel').click(function () {
        if (1 == doNo) {
            t.row(nEditing).remove().draw(false);
            nEditing = null;
        } else if (2 == doNo) {
            backRow(t, nEditing);
        } else if (3 == doNo || 4 == doNo || 5 == doNo) {
            
        }
        hide_div_continueOrcancel();
        now_pageNo = 0;
    });

    var now_pageNo = 0;
    $('#bu_uploadFile').on('click', function () {
        var tmp = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
        if (0 == tmp.length) {
            window.parent.warring(valTips_selOne);
            return;
        } else if (2 <= tmp.length) {
            window.parent.warring(valTips_workOne);
            return;
        }
        $('#uploadModal').modal('show');
        $('#uploadFile_templateId').val(tmp[0].templateId);
        $('#templateFile').val("");
        $('.progress-bar').css("width", "0%");
        now_pageNo = t.page();
        $('#span_fileName').html("");
    });

    $('#do_uploadFile').click(function(){
        if ("" == $('#templateFile').val()) {
            window.parent.warring("请选择需要上传的word文件！");
            return;
        }
        var formData = new FormData($('#templateUploadForm')[0]);
        $.ajax({
            url: config.baseurl +"/api/templateRest/uploadTemplateFile",  //server script to process data
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
            success: function () {
                window.parent.success("上传成功！");
                $('#uploadModal').modal('hide');
                $('#bu_queryData').trigger("click");
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

    function progressHandlingFunction(e){
        if(e.lengthComputable){
            $('.progress-bar').css("width", e.loaded + "%");
        }
    }

    $('#templateFile').change(function(){
        var file = this.files[0];
        var fileType = file.type;
        if (0 <= fileType.indexOf("word") && 0 <= fileType.indexOf("template")) {
            $('#do_uploadFile').attr("disabled", false);

            var file_path = getPath($(this).get(0));
            if (0 <= file_path.indexOf("fakepath")) {
                file_path = file_path.substr(file_path.lastIndexOf("\\") + 1, file_path.length);
            }
            $('#span_fileName').html(file_path);
        } else {
            window.parent.warring("请上传word模板文件(.dotm或者.dotx)！");
            $('#do_uploadFile').attr("disabled", true);
            $('#span_fileName').html("");
        }

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

    function backRow(oTable02, nRow) {
        var jqInputs = $('input', nRow);
        var jqSelects = $('select', nRow);
        var rowObj = oTable02.row(nRow);
        var json = backData;
        var tmpobj = creater.createRowObj(json);
        rowObj.data(tmpobj).draw();
        nEditing = null;
        backData = null;
        hide_div_continueOrcancel();
    }

    $('#bu_continue').click(function () {
        if (1 == doNo || 2 == doNo) {
            //var arr = [];
            var o = new Object();
            o.templateId =  $('#dt_templateId').val();
            o.fromTemplateId =  $('#dt_fromTemplateId').val();
            o.templateName = $('#dt_templateName').val();
            o.templateDescription = $('#dt_templateDescription').val();
            o.taskType = $('#dt_taskType').val();
            o.taskTypeName = $('#dt_taskType').find('option:selected').text();
            o.templateStatus = $('#dt_templateStatus').val();
            o.templateStatusName = $('#dt_templateStatus').find('option:selected').text();
            o.attribute5 = $(nEditing).find("td:last-child").text();

            if ("" == o.templateName.trim()) {
                window.parent.warring("请填写模板名称！");
                return;
            }
            if ("" == o.taskType.trim()) {
                window.parent.warring("请选择文本类型！");
                return;
            }
            //arr.push(o);
            valTemplateHeaderName(o);
        } else if (3 == doNo) {
            var tmp = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
            if (0 == tmp.length) {
                window.parent.warring(valTips_selOne);
                return;
            }

            var to = [];
            $(tmp).each(function (index) {
                to.push(tmp[index].templateId);
            });

            $.ajax({
                url: config.baseurl +"/api/templateRest/delTemplate",
                data: {
                    templateIds: to.toString()
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
        } else if (4 == doNo || 5 == doNo) {
            var _templateStatus;
            var _msg;
            if (4 == doNo) {
                _templateStatus = "EFFECTIVE";
                _msg = "启用成功！";
            } else if ( 5 == doNo) {
                _templateStatus = "INVALID";
                _msg = "失效成功！";
            }
            var tmp = t.rows( $("input[name='ch_list']:checked").parents('tr') ).data();
            if (0 == tmp.length) {
                window.parent.warring(valTips_selOne);
                return;
            }
            $(tmp).each(function (index) {
                $.ajax({
                    url: config.baseurl +"/api/templateRest/upTemplateStatus",
                    data: {
                        templateId: tmp[index].templateId,
                        templateStatus: _templateStatus
                    },
                    contentType : 'application/x-www-form-urlencoded',
                    dataType: "json",
                    type: "POST",
                    beforeSend: function() {
                        window.parent.showLoading();
                    },
                    success: function(data) {
                        if ("0000" == data.returnCode) {
                            window.parent.success(_msg);
                            if (index == tmp.length -1)
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
            });

        }
    });

    function valTemplateHeaderName(data) {
        $.ajax({
            url: config.baseurl +"/api/templateRest/valTemplateName",
            contentType: "application/json",
            dataType: "json",
            async: true,
            data: {
                templateId: data.templateId,
                templateName: data.templateName
            },
            type: "GET",
            beforeSend: function() {
                window.parent.showLoading();
            },
            success: function(d) {
                if (d) {
                    saveHeader(data);
                } else {
                    window.parent.error("模板名称必须唯一，请修改模板名称！");
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

    function saveHeader(data) {
        $.ajax({
            url: config.baseurl +"/api/templateRest/saveTemplateHeader",
            contentType: "application/json",
            dataType: "json",
            async: true,
            data: JSON.stringify(data),
            type: "POST",
            beforeSend: function() {
            },
            success: function(data) {
                if ("0000" == data.returnCode) {
                    $('#dt_templateId').val(data.db_sid);
                    $('#dt_fromTemplateId').val("");
                    window.parent.success(tips_saveSuccess);
                    saveRow(t, nEditing);
                    loadChapter(data.db_sid);
                    editAbled = true;
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

    $('#bu_queryData').click(function(){
        nEditing = null;
        $.ajax({
            url : config.baseurl +"/api/templateRest/templateHeaderList",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",   //返回格式为json
            async: true,//请求是否异步，默认为异步，这也是ajax重要特性
            data: $('#selectFrom').serialize(),
            type:"POST",   //请求方式
            beforeSend:function(){
                window.parent.showLoading();
            },
            success:function(data){
                dataSet = data.dataSource;
                queryList();
            },
            complete:function(){
                window.parent.closeLoading();
            },
            error:function(){
                window.parent.showLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    });

    function loadChapter(templateId) {
        clearCheckAll();
        $.ajax({
            url: config.baseurl +"/api/templateRest/templateChapters",
            data: {
                templateId: templateId
            },
            contentType : 'application/x-www-form-urlencoded',
            dataType: "json",
            type: "GET",
            beforeSend: function() {
                window.parent.showLoading();
            },
            success: function(data) {
                chapter_modal_data = data;
                $('#treetable tbody').empty();
                $('#add_modal').modal('show');
            },
            complete: function() {
                //window.parent.closeLoading();
            },
            error: function() {
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    var chapter_modal_data = [];
    $('#add_modal').on('shown.bs.modal', function (e) {
        $('#add_modal').css("cssText", "display: block; padding-left: 0px; padding-right: 0px;");
        reloadTemplateChapter(chapter_modal_data);
    });

});

