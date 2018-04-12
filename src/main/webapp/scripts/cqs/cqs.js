/**
 * Created by user on 16/6/28.
 */
/* 处理模态窗口的内容初始化*/
var config = new nerinJsConfig();
var indexData = [];
function loadPipingMatlClassHdr(pipingMatlClass) {
    indexData = [];
    $.getJSON(config.baseurl + "/api/indexer/pipingMatlClassHdr?pipingMatlClass=" + pipingMatlClass, function (data) {
        indexData = data[0];
        exsitsFavorite(data[0].pipingMatlClass);
        $("#PipingMatlClassM").val(data[0].pipingMatlClass);
        $("#FlangeFacingM").val(data[0].flangeFacing);
        $("#BasicMaterialM").val(data[0].basicMaterial);
        var servicesList = "";
        $(data).each(function (index) {
            if (servicesList == "")
                servicesList = data[index].service;
            else
                servicesList = servicesList + "、" + data[index].service;
        });
        indexData.servicesList = servicesList;
        $("#ServicesM").val(servicesList);
        $("#CaM").val(data[0].ca);
        $("#DesignTempSourceM").val(data[0].designTempSource);
        $("#DesignPresSourceM").val(data[0].designPresSource);

        $('#TabM2').find('div[id="div_jb"]').each(function () {
            $(this).remove();
        });

        var newDiv = $('#div_jb').clone(true);
        newDiv.insertBefore($('#div_wj'));
        $('textarea[name="ServicesM"]').val($('#ServicesM').val());
    });
}

// 查询是否已收藏
function exsitsFavorite(pmc) {
    $.getJSON(config.baseurl + "/api/indexer/exsitsFavorite?p_piping_matl_class=" + pmc, function (data) {
        if (0 == data) {
            $('#myFavoriteSign').removeClass("fa-star");
            $('#myFavoriteSign').addClass("fa-star-o");
            $('#myFavoriteSign').css("color", "black");
        } else {
            $('#myFavoriteSign').removeClass("fa-star-o");
            $('#myFavoriteSign').addClass("fa-star");
            $('#myFavoriteSign').css("color", "#FCEA7B;");
        }
    });
}


var rtRatings = [];
function loadPtRatingTbl(pipingMatlClass) {
    rtRatings = [];
    $.getJSON(config.baseurl + "/api/indexer/ptRatingTbl?pipingMatlClass=" + pipingMatlClass, function (data) {
        var line1 = [];
        var line2 = [];
        line1.push("温度T(℃)");
        line2.push("压力P(MPa)");
        $(data).each(function (index) {
            line1.push(data[index].temperature);
            line2.push(data[index].pressure);
        });
        count = 20 - line1.length;
        for (i = 0; i < count; i = i + 1) {
            line1.push("");
            line2.push("");
        }
        rtRatings.push(line1);
        rtRatings.push(line2);
        queryNo++;
        queryCompleted();
    });
}

function createPtRatingTbl() {
    $('#PtRating_container').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="PtRating-table"></table>');
    $('#PtRating-table').DataTable({
        //定义列名
        "columns": [
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""}
        ],
        //设置数据源
        "data": rtRatings,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "paging": false,
        "ordering": false,
        "searching": false,
        "autoWidth": true,
        "info": false,
        "initComplete": function () {
            $('#PtRating-table tr td:eq(0)').css("width", "80px");
            for (var i = 1; i <= 19; i++) {
                $('#PtRating-table tr td:eq(' + i + ')').css("width", "60px");
            }
            $('#PtRating-table').find("thead").remove();
            $('#PtRating-table').css("cssText", "margin-top: 0px !important; margin-bottom: 0px !important;");
        }
    })
}

var itemsTbl_data = [];
// 标准号表数据
var standardNumberData = [];
function loadItemsTbl(pipingMatlClass) {
    $.getJSON(config.baseurl + "/api/indexer/getStandardNumberSource", function (data) {
        standardNumberData = data;
        $.getJSON(config.baseurl + "/api/indexer/itemsTbl?pipingMatlClass=" + pipingMatlClass, function (data) {
            itemsTbl_data = data;
            queryNo++;
            queryCompleted();
        });
    });

}

function createItemsTbl() {
    $('#item_container').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="item-table">'
        + '<thead>'
            + '<tr>'
            + '<th>itemOrderNumber</th><th>itemId</th><th>piping_matl_class</th><th>itemCategory</th><th rowspan="2">元件名称<br>Item Name</th><th colspan="2" style="text-align: center; border-bottom: 1px solid #ddd;">公称直径 Size(DN)</th>'
            + '<th rowspan="2" style="width: 80px;">端面/密封面<br>End/Facing</th><th rowspan="2" style="width: 90px;">壁厚/压力等级<br>THK./Rating</th><th rowspan="2">材料<br>Material</th><th rowspan="2">标准 / 型号<br>Standard/Model</th><th rowspan="2">备注<br>Note</th>'
            + '</tr>'
            + '<tr>'
            + '<th>itemOrderNumber</th><th>itemId</th><th>piping_matl_class</th><th>itemCategory</th><th style="width: 60px;">最小MIN</th>'
            + '<th style="border-right: 1px solid #ddd; width: 60px;">最大MAX</th>'
            + '</tr>'
        + '</thead>'
        + '</table>');
    $('#item-table').DataTable({
        //设置数据源
        "data": itemsTbl_data,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        //定义列名
        "columns": [
            {"data": "itemOrderNumber"},
            {"data": "itemId"},
            {"data": "piping_matl_class"},
            {"data": "itemCategory"},
            {"data": "itemName"},
            {"data": "minDn"},
            {"data": "maxDn"},
            {"data": "endFacing"},
            {"data": "thkRating"},
            {"data": "material"},
            {"data": "standardModel"},
            {"data": "note"}
            // {"title": "itemOrderNumber", "data": "itemOrderNumber"},
            // {"title": "itemId", "data": "itemId"},
            // {"title": "piping_matl_class", "data": "piping_matl_class"},
            // {"title": "itemCategory", "data": "itemCategory"},
            // {"title": "元件名称<br>Item Name", "data": "itemName"},
            // {"title": "最小<br>MIN", "data": "minDn"},
            // {"title": "最大<br>MAX", "data": "maxDn"},
            // {"title": "端面/密封面<br>End/Facing", "data": "endFacing"},
            // {"title": "壁厚/压力等级<br>THK./Rating", "data": "thkRating"},
            // {"title": "材料<br>Material", "data": "material"},
            // {"title": "标准 / 型号<br>Standard/Model", "data": "standardModel"},
            // {"title": "备注<br>Note", "data": "note"}
        ],
        //控制隐藏列
        "columnDefs": [
            {
                "targets": 0,
                "visible": false
            },
            {
                "targets": 1,
                "visible": false
            },
            {
                "targets": 2,
                "visible": false
            },
            {
                "targets": 3,
                "visible": false
            },{
                "targets": 9,
                "render": function (data, type, full, meta) {
                    var aString = "";
                    if (data) {
                        for (var i = 0; i < standardNumberData.length; i++) {
                            var ind = data.indexOf(standardNumberData[i].standard_number);
                            if (0 <= ind) {
                                var tmp = "<a name='a_standardNumber' href='#1' data-value='" + standardNumberData[i].search_key + "'>" + standardNumberData[i].standard_number + "</a>";
                                var tmpS = data.split(standardNumberData[i].standard_number)[0];
                                if (0 < tmpS.length && ">" == tmpS.charAt(tmpS.length - 1)) {

                                } else {
                                    data = data.replace(standardNumberData[i].standard_number, tmp);
                                }
                            }
                        }
                        for (var i = 0; i < standardNumberData.length; i++) {
                            var ind2 = data.indexOf(standardNumberData[i].number_inexistent_year);
                            if (0 <= ind2) {
                                var tmp = "<a name='a_standardNumber' href='#1' data-value='" + standardNumberData[i].search_key + "'>" + standardNumberData[i].number_inexistent_year + "</a>";
                                var tmpS = data.split(standardNumberData[i].number_inexistent_year)[0];
                                if (0 < tmpS.length && ">" == tmpS.charAt(tmpS.length - 1)) {

                                } else {
                                    data = data.replace(standardNumberData[i].number_inexistent_year, tmp);
                                }
                            }
                        }
                        aString = data;
                    }
                    return aString;
                }
            },{
                "targets": 10,
                "render": function (data, type, full, meta) {
                    var aString = "";
                    if (data) {
                        for (var i = 0; i < standardNumberData.length; i++) {
                            var ind = data.indexOf(standardNumberData[i].standard_number);
                            if (0 <= ind) {
                                var tmp = "<a name='a_standardNumber' href='#1' data-value='" + standardNumberData[i].search_key + "'>" + standardNumberData[i].standard_number + "</a>";
                                var tmpS = data.split(standardNumberData[i].standard_number)[0];
                                if (0 < tmpS.length && ">" == tmpS.charAt(tmpS.length - 1)) {

                                } else {
                                    data = data.replace(standardNumberData[i].standard_number, tmp);
                                }
                            }
                        }
                        for (var i = 0; i < standardNumberData.length; i++) {
                            var ind2 = data.indexOf(standardNumberData[i].number_inexistent_year);
                            if (0 <= ind2) {
                                var tmp = "<a name='a_standardNumber' href='#1' data-value='" + standardNumberData[i].search_key + "'>" + standardNumberData[i].number_inexistent_year + "</a>";
                                var tmpS = data.split(standardNumberData[i].number_inexistent_year)[0];
                                if (0 < tmpS.length && ">" == tmpS.charAt(tmpS.length - 1)) {

                                } else {
                                    data = data.replace(standardNumberData[i].number_inexistent_year, tmp);
                                }
                            }
                        }
                        aString = data;
                    }
                    return aString;
                }
            }
        ],
        //控制分组，按照类别

        "drawCallback": function (settings) {
            var api = this.api();
            var rows = api.rows({page: 'current'}).nodes();
            var last = null;

            api.column(3, {page: 'current'}).data().each(function (group, i) {
                if (last !== group) {
                    $(rows).eq(i).before(
                        '<tr class="group"><td colspan="8">' + group + '</td></tr>'
                    );

                    last = group;
                }
            });
        },
        //不分页，加入垂直导航条
        "scrollY": "300px",
        "scrollCollapse": true,
        "ordering": false,
        "paging": false,
        "searching": false,
        "info": false,
        "autoWidth": true,
        "initComplete": function () {
            $('#item-table_wrapper').css("cssText", "margin-top: -5px;");
            $('[data-toggle="popover"]').popover();
            $('[data-toggle="popover"]').on("mouseover", function () {
                $(this).popover('show');
            });
            $('[data-toggle="popover"]').on("mouseout", function () {
                $(this).popover('hide');
            });
        }
    });

    $('#item-table').on('click', 'a[name=a_standardNumber]', function () {
        var code = $(this).attr("data-value");
        window.open(config.contextConfig.cqsStandardNumber + "key=" + code + "&idx=1&pcnt=10");
    });
}

var pipeThicknesses = [];
function loadPipeThicknessTbl(pipingMatlClass) {
    pipeThicknesses = [];
    $.getJSON(config.baseurl + "/api/indexer/pipeThicknessTbl?pipingMatlClass=" + pipingMatlClass, function (data) {
        var line1 = [];
        var line2 = [];
        var line3 = [];
        line1.push("DN");
        line2.push("外径(mm)");
        line3.push("壁厚(mm)");
        $(data).each(function (index) {
            if (index != 0 && index % 17 == 0) {
                pipeThicknesses.push(line1);
                pipeThicknesses.push(line2);
                pipeThicknesses.push(line3);
                line1 = [];
                line2 = [];
                line3 = [];
                line1.push("DN");
                line2.push("外径(mm)");
                line3.push("壁厚(mm)");
                line1.push(data[index].pipeDn);
                line2.push(data[index].pipeOuter);
                line3.push(data[index].pipeThickness);
            } else {
                line1.push(data[index].pipeDn);
                line2.push(data[index].pipeOuter);
                line3.push(data[index].pipeThickness);
            }

        });
        count = 17 - line1.length;
        for (i = 0; i < count; i = i + 1) {
            line1.push("");
            line2.push("");
            line3.push("");

        }
        pipeThicknesses.push(line1);
        pipeThicknesses.push(line2);
        pipeThicknesses.push(line3);
        queryNo++;
        queryCompleted();
    });
}

function createPipeThicknessTbl() {
    $('#pipe_thickness_container').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="pipeThickness-table"></table>');
    $('#pipeThickness-table').DataTable({
        //定义列名
        "columns": [
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""}
        ],
        //设置数据源
        "data": pipeThicknesses,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "createdRow": function (row, data, dataIndex) {
            for (i = 0; i <= 17; i++) {
                $(row).children('td').eq(i).attr('style', 'text-align: center;');
            }
        },
        "paging": false,
        "ordering": false,
        "searching": false,
        "info": false,
        "autoWidth": true,
        "initComplete": function () {
            $('#pipeThickness-table tr td:eq(0)').css("width", "80px");
            for (var i = 1; i <= 16; i++) {
                $('#pipeThickness-table tr td:eq(' + i + ')').css("width", "60px");
            }
            $('#pipeThickness-table').find("thead").remove();
            $('#pipeThickness-table').css("cssText", "margin-top: 0px !important; margin-bottom: 0px !important;");
        }

    });
}

var branchConnects = [];
function loadBranchConnectTbl(pipingMatlClass) {
    branchConnects = [];
    $.getJSON(config.baseurl + "/api/indexer/branchConnectTbl?pipingMatlClass=" + pipingMatlClass, function (data) {
        var line1 = [];
        var lineLast = [];
        var tblColNum = 41;
        lineLast.push(""); //占据左下角单元格

        $(data).each(function (index) {
            if (line1.length == 0) {
                line1.push(data[index].headerDn);
                line1.push(data[index].connectionType);
            } else if (line1[0] != data[index].headerDn) {
                var len = tblColNum - line1.length;
                for (i = 0; i < len; i++) {
                    line1.push("");
                }
                branchConnects.push(line1);
                line1 = [];
                line1.push(data[index].headerDn);
                line1.push(data[index].connectionType);
            }
            else {
                line1.push(data[index].connectionType);
            }
            if (lineLast.indexOf(data[index].branchDn) < 0) {
                lineLast.push(data[index].branchDn);
            }

        });
        count = tblColNum - line1.length;
        for (i = 0; i < count; i = i + 1) {
            line1.push("");
        }
        branchConnects.push(line1);

        for (i = 0; i < lineLast.length; i++) {
            if (lineLast[i] != "") {
                lineLast[i] = '<div class="polaroid rotate_right" style="margin-top: 5px; margin-left: 6px;"><small>' + lineLast[i] + '</small></div>'
            }
        }

        count = tblColNum - lineLast.length;
        for (i = 0; i < count; i = i + 1) {
            lineLast.push("");
        }

        branchConnects.push(lineLast);

        for (i = 0; i < branchConnects.length; i++) {
            if (branchConnects[i][0] != "") {
                branchConnects[i][0] = '<small>' + branchConnects[i][0] + '</small>';
            }
        }
        queryNo++;
        queryCompleted();
    });
}

function createBranchConnectTbl() {
    var rowSpanNo = 0;
    $('#branch_connection_container').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover2" id="branch_connection-table"></table>');
    var t = $('#branch_connection-table').DataTable({
        //定义列名
        "columns": [
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""},
            {"title": ""}, //29
            {"title": ""}  //30
        ],
        //设置数据源
        "data": branchConnects,
        "language": {
            "url": "/scripts/common/Chinese.json"
        },
        "columnDefs": [{
            "searchable": false,
            "orderable": false,
            "targets": 0,
            "width": 10
        }],
        "createdRow": function (row, data, dataIndex) {
             $(row).children('td').attr('style', 'text-align: center;padding: 1px; vertical-align: middle;');
            rowSpanNo++;
        },
        "paging": false,
        "ordering": false,
        "searching": false,
        "info": false,
        "autoWidth": false,
        "initComplete": function () {
            $('#branch_connection-table tr td:eq(0)').css("width", "30px");
            $('#branch_connection-table tr td:eq(0)').css("height", "30px");
            for (var i = 1; i <= 41; i++) {
                $('#branch_connection-table tr td:eq(' + i + ')').css("width", "30px");
                $('#branch_connection-table tr:eq(' + i + ')').css("height", "30px");
            }
            $('#branch_connection-table tr:last').find('td').css("height", "30px");
            $('#branch_connection-table').find("thead").remove();
            $('#branch_connection-table').css("cssText", "margin-top: 0px !important; margin-bottom: 0px !important;");
            var $col = $("<td style='width: 30px; height: 20px; vertical-align: middle;' rowspan='" + rowSpanNo + "'>&nbsp;&nbsp;支管<br>Branch<br>&nbsp;&nbsp;(DN)</td>");
            $("#branch_connection-table>tbody>tr:eq(0)").find("td:eq(0)").before($col);

            // 隐藏后面为空的列
            console.log($('#branch_connection-table tr:eq(0)'));
            console.log($('#branch_connection-table tr:not(:eq(0))'));
            for (var i = branchConnects.length + 1; i <= 41; i++) {
                $('#branch_connection-table tr:eq(0)').find('td:eq(' + i +')').hide();
            }
            for (var i = branchConnects.length; i <= 40; i++) {
                $('#branch_connection-table tr:not(:eq(0))').find('td:eq(' + i +')').hide();
            }
            $("#branch_connection-table").append($('<tr><td style=""></td><td colspan="' + (branchConnects.length) + '" style="text-align: center;"><p style="margin: 5px 0px 5px 0px;">主管 Header (DN)</p></td></tr>'))
            $('#branch_connection-table').css("width", (branchConnects.length * 30 + 130) + "px");
        }
    });
    $('#branch_connection-table tbody').on('mouseenter', 'td', function () {
        var colIdx = t.cell(this).index().column;
        $(t.cells().nodes()).removeClass('highlight');
        $(t.cells().nodes()).removeClass('highlightCheck');
        $(t.column(colIdx).nodes()).addClass('highlight');
        $(this).addClass("highlightCheck");
    });
}

var queryNo = 0;
function openModal(pipingMatlClass) {
    $('#nav_detail li:eq(0) a').tab('show');
    queryNo = 0;
    window.parent.showLoading();
    loadPipingMatlClassHdr(pipingMatlClass);
    loadPtRatingTbl(pipingMatlClass);
    loadItemsTbl(pipingMatlClass);
    loadPipeThicknessTbl(pipingMatlClass);
    loadBranchConnectTbl(pipingMatlClass);
}

function queryCompleted() {
    if (4 == queryNo) {
        $('#pipingMatlClassTbl').modal('show');
        window.parent.closeLoading();
    }
}

$(document).keyup(function(event){
    switch(event.keyCode) {
        case 13:
            $('#bu_queryData').trigger("click");
    }
});

$(function () {
    $('#bu_queryData').focus();
    $("#faq").load("faq/faq.html", function() {

    });

    $('#nav_index').find('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        if ("使用指南" == e.target.innerHTML) {
            $('body:eq(0)').removeAttr("oncontextmenu");
            $('body:eq(0)').removeAttr("onselectstart");
        } else if ("索引查询" == e.target.innerHTML) {
            $('body:eq(0)').attr("oncontextmenu", "return false");
            $('body:eq(0)').attr("onselectstart", "return false");
            loadDataTable();
        } else if ("等级收藏列表" == e.target.innerHTML) {
            queryMyFavorite();
        }
    });

    function delFavorite(pmc, flag) {
        $.ajax({
            url : config.baseurl +"/api/indexer/delFavorite",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                p_piping_matl_class: pmc
            },
            type:"POST",
            beforeSend:function(){window.parent.showLoading();},
            success:function(data){
                window.parent.success("取消收藏成功");
                if (1 == flag) {
                    $('#myFavoriteSign').removeClass("fa-star");
                    $('#myFavoriteSign').addClass("fa-star-o");
                    $('#myFavoriteSign').css("color", "black");
                    queryMyFavorite();
                } else if (0 == flag) {
                    queryMyFavorite();
                }
            },
            complete:function(){  window.parent.closeLoading();},
            error:function(){
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    $('#myFavoriteSign').on('click', function () {
        if ($('#myFavoriteSign').hasClass("fa-star")) {
            delFavorite(indexData.pipingMatlClass, 1);
        } else {
            $.ajax({
                url : config.baseurl +"/api/indexer/addFavorite",
                contentType : 'application/x-www-form-urlencoded',
                dataType:"json",
                data: {
                    p_piping_matl_class: indexData.pipingMatlClass,
                    p_flange_facing: indexData.flangeFacing,
                    p_basic_material: indexData.basicMaterial,
                    p_services: indexData.servicesList,
                    p_ca: indexData.ca,
                    p_design_temp_source: indexData.designTempSource,
                    p_design_pres_source: indexData.designPresSource
                },
                type:"POST",
                beforeSend:function(){window.parent.showLoading();},
                success:function(data){
                    window.parent.success("收藏成功");
                    $('#myFavoriteSign').removeClass("fa-star-o");
                    $('#myFavoriteSign').addClass("fa-star");
                    $('#myFavoriteSign').css("color", "#FCEA7B;");
                },
                complete:function(){  window.parent.closeLoading();},
                error:function(){
                    window.parent.closeLoading();
                    window.parent.error(ajaxError_loadData);
                }
            });
        }
    });

    var myFavoriteDate = [];
    function queryMyFavorite() {
        myFavoriteDate = [];
        $.ajax({
            url : config.baseurl +"/api/indexer/getFavorite",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            type:"GET",
            beforeSend:function(){window.parent.showLoading();},
            success:function(data){
                myFavoriteDate = data;
                console.log(myFavoriteDate);
                createMyFavorite();
            },
            complete:function(){  window.parent.closeLoading();},
            error:function(){
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    }

    function createMyFavorite() {
        $('#dataTables-MyFavoriteAll').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="dataTables-MyFavorite"></table>');
        var t = $('#dataTables-MyFavorite').DataTable({
            "scrollY": "500px",
            "scrollCollapse": true,
            "data": myFavoriteDate,
            "paging": false,
            "searching": false,
            "language": {
                "url": "/scripts/common/Chinese.json"
            },
            "columns": [
                {"title": "序号","data": null},
                {"title": "管道材料等级<br>Piping Mat'l Class", "data": "pipingMatlClass"},
                {"title": "适用介质<br>Services","data": "services"},
                {"title": "设计温度范围<br>Design Temp. Limit(℃)","data": "designTempSource"},
                {"title": "设计压力范围<br>Design P. Limit(MPa)","data": "designPresSource"},
                {"title": "基本材料<br>Baisc Material","data": "basicMaterial"},
                {"title": "压力等级及密封面<br>Rating & Facing","data": "flangeFacing"},
                {"title": "腐蚀裕量<br>C. A. (mm)","data": "attribute15"},
                {"title": "操作","data": "attribute15"}
            ],
            "columnDefs": [{
                "searchable": false,
                "orderable": false,
                "targets": 0,
                "width": 32
            },{
                "targets": 1,
                "width": 125,
                "render": function (data, type, full, meta) {
                    return "<a name='a_myFavorite' href='#1'>" + data + "</a>";
                }
            },{
                "targets": 2
                // "width": 300,
            },{
                "targets": 3,
                "width": 150
            },{
                "targets": 4,
                "width": 145
            },{
                "targets": 5,
                "width": 100
            },{
                "targets": 6,
                "width": 115
            },{
                "targets": 7,
                "width": 80
            },{
                "targets": 8,
                "width": 30,
                "render": function (data, type, full, meta) {
                    return "<a name='a_delFavorite' href='#1'>删除</a>";
                }
            }
            ],
            "order": [],
            "createdRow": function( row, data, dataIndex ) {
                $(row).children('td').eq(0).attr('style', 'text-align: center;');
            },
            "initComplete": function () {
                // $('#dataTables-MyFavorite' + '_length').insertBefore( $('#t_zykgbg_' + activeTabId + '_info'));
                // $('#t_zykgbg_' + activeTabId + '_length').addClass("col-sm-4");
                // $('#t_zykgbg_' + activeTabId + '_length').css("paddingLeft", "0px");
                // $('#t_zykgbg_' + activeTabId + '_length').css("paddingTop", "5px");
                // $('#t_zykgbg_' + activeTabId + '_length').css("maxWidth", "130px");
                // $('#t_zykgbg_' + activeTabId + '_wrapper').css("cssText", "margin-top: -5px;");
            }
        });

        $('#dataTables-MyFavorite').on('click', 'a[name=a_myFavorite]', function () {
            var userTable = $('#dataTables-MyFavorite').DataTable();
            var data = userTable.rows( $(this).parents('tr') ).data();
            var $tr = $(this).parent().parent();
            openModal(data[0].pipingMatlClass, 0);
            $.getJSON(config.baseurl + "/api/indexer/pipingMatlClassHdr?pipingMatlClass=" + data[0].pipingMatlClass, function (d) {
                var tmpData = d[0];
                // 刷新表格行
                var servicesList = "";
                $(d).each(function (index) {
                    if (servicesList == "")
                        servicesList = d[index].service;
                    else
                        servicesList = servicesList + "、" + d[index].service;
                });
                $($tr.find("td")[2]).html(servicesList);
                $($tr.find("td")[3]).html(tmpData.designTempSource);
                $($tr.find("td")[4]).html(tmpData.designPresSource);
                $($tr.find("td")[5]).html(tmpData.basicMaterial);
                $($tr.find("td")[6]).html(tmpData.flangeFacing);
                $($tr.find("td")[7]).html(tmpData.ca);
                $.ajax({
                    url : config.baseurl +"/api/indexer/addFavorite",
                    contentType : 'application/x-www-form-urlencoded',
                    dataType:"json",
                    data: {
                        p_piping_matl_class: tmpData.pipingMatlClass,
                        p_flange_facing: tmpData.flangeFacing,
                        p_basic_material: tmpData.basicMaterial,
                        p_services: servicesList,
                        p_ca: tmpData.ca,
                        p_design_temp_source: tmpData.designTempSource,
                        p_design_pres_source: tmpData.designPresSource
                    },
                    type:"POST",
                    beforeSend:function(){},
                    success:function(data){},
                    complete:function(){},
                    error:function(){}
                });
            });
        });

        $('#dataTables-MyFavorite').on('click', 'a[name=a_delFavorite]', function () {
            var userTable = $('#dataTables-MyFavorite').DataTable();
            var data = userTable.rows( $(this).parents('tr') ).data();
            delFavorite(data[0].pipingMatlClass, 0);
        });

        t.on( 'order.dt search.dt', function () {
            t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } );
        } ).draw();
    }

    $('#pipingMatlClassTbl').on('shown.bs.modal', function (e) {
        createPtRatingTbl();
        createItemsTbl();
        createPipeThicknessTbl();
        createBranchConnectTbl();
    });

    $('#nav_detail').find('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        if ("管道材料等级表" == e.target.innerHTML) {
            createPtRatingTbl();
            createItemsTbl();
        } else {
            createPipeThicknessTbl();
            createBranchConnectTbl();
        }
    });

    //定义索引结果集返回
    var IndexerDTO = [];
    //
    initServiceMenu();



    initDataTable();
    
    $('#bu_queryData').click(function () {
        $.ajax({
            url: config.baseurl + "/api/indexer/indexer",
            contentType: 'application/x-www-form-urlencoded',
            dataType: "json",   //返回格式为json
            async: true,//请求是否异步，默认为异步，这也是ajax重要特性
            data: $('#selectFrom').serialize(),
            type: "GET",   //请求方式
            beforeSend: function () {
                window.parent.showLoading();
            },
            success: function (data) {
                IndexerDTO = data;
                loadDataTable();
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

    //reset select
    $('#bu_resetQuery').click(function () {
        $('.selectpicker').selectpicker('val', '');
    });

    /**
     * 初始适用介质下拉列表
     */
    function initServiceMenu() {
        $.get(config.baseurl + '/api/indexer/service', function (data) {

            $.widget( "custom.catcomplete", $.ui.autocomplete, {
                _create: function() {
                    this._super();
                    this.widget().menu( "option", "items", "> :not(.ui-autocomplete-category)" );
                },
                _renderMenu: function( ul, items ) {
                    var that = this,
                        currentCategory = "";
                    $.each( items, function( index, item ) {
                        var li;
                        if ( item.category != currentCategory ) {
                            ul.append( "<li class='ui-autocomplete-category'>" + item.category + "</li>" );
                            currentCategory = item.category;
                        }
                        li = that._renderItemData( ul, item );
                        if ( item.category ) {
                            li.css("cssText", "padding-left: 20px !important;");
                            li.attr( "aria-label", item.category + " : " + item.label );
                        }
                    });
                }
            });
            var data1 = [];
            var o = new Object();
            $(data).each(function(index) {
                o = new Object();
                o.label = data[index].service + "<span style='display: none;'>" + data[index].pyszm + "</span>";
                o.category = data[index].category;
                data1.push(o);
            });

            $('#service').catcomplete({
                delay: 0,
                minLength: 0,
                source: data1,
                open:function () {
                    $('#ui-id-1 li').each(function (i) {
                        var text =  $(this).html().split("&lt;span style='display: none;'&gt;")[0];
                        var n = text.replace(
                            new RegExp(
                                "(?![^&;]+;)(?!<[^<>]*)(" +
                                $('#service').val() +
                                ")(?![^<>]*>)(?![^&;]+;)", "gi"
                            ), "<font style='color: red'>$1</font>" );
                        $(this).html(n);
                    });
                },
                close: function () {
                    var s = $('#service').val();
                    if ("" != s){
                        var i = s.indexOf("<span style='display: none;'>");
                        if (0 <= i) {
                            s = s.substring(0, i);
                            $('#service').val(s);
                        }
                    }
                }
            });

            $('#bu_service').on('click',function (){
                if ( $("#service").catcomplete("widget").is(":visible")) {
                    $("#service").catcomplete("close");
                    return;
                }
                $(this).blur();
                $("#service").catcomplete("search", "");
                $("#service").focus();
            });
            initRatingMenu();
            //
            // $("#service").html("");
            // $(data).each(function (index) {
            //     $("#service").append($('<option value="' + data[index].service + '">' + data[index].service + '</option>'));
            // });
            // $("#service").selectpicker('refresh');

        });
    }

    /**
     * 初始化压力等级
     * @param rating
     * 修改成AutoComplete的形式 by dl 20161018
     */

    function initRatingMenu(rating) {
        $.get(config.baseurl + '/api/indexer/rating', function (data) {
        /*
            $("#rating").html("");
            $(data).each(function (index) {
                $("#rating").append($('<option value="' + data[index].rating + '">' + data[index].rating + '</option>'));
            })
            $("#rating").selectpicker('refresh');
         */
            var ratingArray = [];
            $(data).each(function(index) {
                ratingArray.push(data[index].rating);
            });
            $("#rating").autocomplete({
                source: ratingArray,
                minLength: 0,
                open:function () {
                    $('#ui-id-2 li').each(function (i) {
                        var text =  $(this).text();
                        var n = text.replace(
                            new RegExp(
                                "(?![^&;]+;)(?!<[^<>]*)(" +
                                $('#rating').val() +
                                ")(?![^<>]*>)(?![^&;]+;)", "gi"
                            ), "<font style='color: red'>$1</font>" );
                        $(this).html(n);
                    });
                }
            });

            $('#bu_rating').on('click',function (){
                if ( $("#rating").autocomplete("widget").is(":visible")) {
                    $("#rating").autocomplete("close");
                    return;
                }
                $(this).blur();
                $("#rating").autocomplete("search", "");
                $("#rating").focus();
            });
            initPipingMatlClassMenu();
        });

    }

    /**
     * 初始化管道材料等级
     * @param pipingMatlClass
     * 修改成AutoComplete的形式 by dl 20161018
     */
     function initPipingMatlClassMenu(pipingMatlClass) {
        $.get(config.baseurl + '/api/indexer/pipingMatlClass', function (data) {
            console.log(data);
            /*
            $("#pipingMatlClass").html("");
            $(data).each(function (index) {
                console.log(data[index].rating);
                $("#pipingMatlClass").append($('<option value="' + data[index].pipingMatlClass + '">' + data[index].pipingMatlClass + '</option>'));
            })
            $("#pipingMatlClass").selectpicker('refresh');*/
            var pmcArray = [];
            $(data).each(function(index) {
                pmcArray.push(data[index].pipingMatlClass);
            });
            $("#pipingMatlClass").autocomplete({
                source: pmcArray,
                minLength: 0,
                open:function () {
                    $('#ui-id-3 li').each(function (i) {
                        var text =  $(this).text();
                        var n = text.replace(
                            new RegExp(
                                "(?![^&;]+;)(?!<[^<>]*)(" +
                                $('#pipingMatlClass').val() +
                                ")(?![^<>]*>)(?![^&;]+;)", "gi"
                            ), "<font style='color: red'>$1</font>" );
                        $(this).html(n);
                    });
                }
            });

            $('#bu_pipingMatlClass').on('click',function (){
                if ( $("#pipingMatlClass").autocomplete("widget").is(":visible")) {
                    $("#pipingMatlClass").autocomplete("close");
                    return;
                }
                $(this).blur();
                $("#pipingMatlClass").autocomplete("search", "");
                $("#pipingMatlClass").focus();
            });
            initbasicMaterialMenu();
        });
    }

    /**
     * 初始化基础材料
     * @param basicMaterial
     * 修改成AutoComplete的形式 by dl 20161018
     */
    function initbasicMaterialMenu(basicMaterial) {
        $.get(config.baseurl + '/api/indexer/basicMaterial', function (data) {
            /*$("#basicMaterial").html("");
            $(data).each(function (index) {
                $("#basicMaterial").append($('<option value="' + data[index].basicMaterial + '">' + data[index].basicMaterial + '</option>'));
            });
            $("#basicMaterial").selectpicker('refresh');*/
            var bmArray = [];
            $(data).each(function(index) {
                bmArray.push(data[index].basicMaterial);
            });
            $("#basicMaterial").autocomplete({
                source: bmArray,
                minLength: 0,
                    open:function () {
                        $('#ui-id-4 li').each(function (i) {
                            var text =  $(this).text();
                            var n = text.replace(
                                new RegExp(
                                    "(?![^&;]+;)(?!<[^<>]*)(" +
                                    $('#basicMaterial').val() +
                                    ")(?![^<>]*>)(?![^&;]+;)", "gi"
                                ), "<font style='color: red'>$1</font>" );
                            $(this).html(n);
                        });
                    }
            });

            $('#bu_basicMaterial').on('click',function (){
                if ( $("#basicMaterial").autocomplete("widget").is(":visible")) {
                    $("#basicMaterial").autocomplete("close");
                    return;
                }
                $(this).blur();
                $("#basicMaterial").autocomplete("search", "");
                $("#basicMaterial").focus();
            });
        });
    }



    /**
     * 渲染管道压力等级索引表
     *
     */
    function loadDataTable() {
        var ds = [];
        $(IndexerDTO).each(function (index) {
            ds.push([IndexerDTO[index]['indexId'], IndexerDTO[index]['indexOrder'], IndexerDTO[index]['catagory'], IndexerDTO[index]['services'], IndexerDTO[index]['designTempSource'], IndexerDTO[index]['designPresSource'],
                IndexerDTO[index]['pipingMatlClass'], IndexerDTO[index]['basicMaterial'], IndexerDTO[index]['rating'], IndexerDTO[index]['flangeFacing'], IndexerDTO[index]['ca'], IndexerDTO[index]['note']]);
        });
        $('#indexer-table-list').html('<table cellpadding="0" cellspacing="0" border="0" class="table  table-bordered table-hover" id="indexer-table"></table>');

        $('#indexer-table').DataTable({
            "autoWidth": true,
            //设置数据源
            "data": ds,
            "language": {
                "sProcessing":   "处理中...",
                "sLengthMenu":   "显示 _MENU_ 项",
                "sZeroRecords":  "没有匹配结果",
                "sInfo":         "第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                "sInfoEmpty":    "第 0 至 0 项结果，共 0 项",
                "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                "sInfoPostFix":  "",
                "sSearch":       "在结果中动态搜索:",
                "sUrl":          "",
                "sEmptyTable":     "表中数据为空",
                "sLoadingRecords": "载入中...",
                "sInfoThousands":  ",",
                "oPaginate": {
                    "sFirst":    "首页",
                    "sPrevious": "上页",
                    "sNext":     "下页",
                    "sLast":     "末页"
                },
                "oAria": {
                    "sSortAscending":  ": 以升序排列此列",
                    "sSortDescending": ": 以降序排列此列"
                }
            },
            //定义列名
            "columns": [
                {"title": "ID"},
                {"title": "序号<br>No."},
                {"title": "等级类别 "},
                {"title": "适用介质<br>Services"},
                {"title": "设计温度（℃）<br>Design Temp.(℃)"},
                {"title": "设计压力（MPa）<br>Design Pres.(MPa)"},
                {"title": "管道材料等级<br>Piping Mat'l Class"},
                {"title": "基本材料<br>Basic Material"},
                {"title": "压力等级<br>Rating"},
                {"title": "法兰密封面<br>Flange Facing"},
                {"title": "腐蚀裕量(mm)<br>C. A.(mm)"},
                {"title": "备注<br>Note"}
            ],
            //控制隐藏列
            "columnDefs": [
                {
                    "targets": 0,
                    "visible": false
                },
                {
                    "targets": 1,
                    "visible": true,
                    "searchable": false,
                    "orderable": false
                },
                {
                    "targets": 2,
                    "visible": false
                },
                {
                    "targets": 6,
                    "render": function (data, type, row) {
                        return "<a href=javascript:openModal('" + data + "') >" + data + "</a>"; //data-toggle='modal' data-target='#pipingMatlClassTbl'
                    },
                }
            ],
            //控制排序:按照排序号
            "order": [],
            //控制分组，按照类别
            "drawCallback": function (settings) {
                var api = this.api();
                var rows = api.rows({page: 'current'}).nodes();
                var last = null;

                api.column(2, {page: 'current'}).data().each(function (group, i) {
                    if (last !== group) {
                        $(rows).eq(i).before(
                            '<tr class="group"><td colspan="10">' + group + '</td></tr>'
                        );

                        last = group;
                    }
                });
            },
            //不分页，加入垂直导航条
            "scrollY": "400px",
            "scrollCollapse": true,
            "paging": false,
            "initComplete": function () {
                var sy = $('#service').val().trim();
                var sj = $('#designTempValue').val().trim();
                var jb = $('#basicMaterial').val().trim();
                var gd = $('#pipingMatlClass').val().trim().toUpperCase();
                var sjy = $('#designTempValue').val().trim();
                var yl = $('#rating').val().trim();
                var tds = $('#indexer-table tbody>tr>td');
                tds.each(function () {
                    var s, $e;
                    if (0 < $(this).find("a").length) {
                        s = $($(this).find("a")[0]).text();
                        $e = $($(this).find("a")[0]);
                    } else {
                        s = $(this).text();
                        $e = $(this);
                    }
                    if (("" != sy && 0 <= s.indexOf(sy)) || ("" != sj && 0 <= s.indexOf(sj)) || ("" != jb && 0 <= s.indexOf(jb))
                        || ("" != gd && 0 <= s.indexOf(gd)) || ("" != sjy && 0 <= s.indexOf(sjy)) || ("" != yl && 0 <= s.indexOf(yl))) {
                        $e.css("color", "red");
                    }
                });
                $('[data-toggle="popover"]').popover();
                $('[data-toggle="popover"]').on("mouseover", function () {
                    $(this).popover('show');
                });
                $('[data-toggle="popover"]').on("mouseout", function () {
                    $(this).popover('hide');
                });
            }
        });
    }

    /**
     * 初始化索引表
     */
    function initDataTable() {
        window.parent.showLoading();
        $.getJSON(config.baseurl + "/api/indexer/indexer?pageSize=" + config.searchResultSize, function (data) {
            IndexerDTO = data;
            window.parent.closeLoading();
            loadDataTable();
        });
    }


});
