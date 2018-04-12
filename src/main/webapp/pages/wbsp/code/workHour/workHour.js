/**
 * Created by N104528B on 2017/12/28.
 */

(function ($) {
    var api = {};
    // todo: 使用ajax代替静态数据
    api.getWorkHourByDateRange = function (start, length) {
        return [7, 7, 7, 7, 7, 0, 0];
    };
    api.getDetailByDate = function (date) {
        var res = {
            footer: [{code: 'total', hour: 7.5}],
            rows: [
                {code: 'asdf1', pName: 'foo', hour: 2},
                {code: 'asdf2', pName: 'bar', hour: 3},
                {code: 'asdf3', pName: 'exi', hour: 2.5}
            ]
        };
        res = [{
            "id":"date",
            code:date,
            "hour":7.5,
            state: "",
            "children":[
                {code: 'asdf1', pName: 'foo', hour: Math.random()},
                {code: 'asdf2', pName: 'bar', hour: 3},
                {code: 'asdf3', pName: 'exi', hour: 2.5}
            ]
        }];
        return res;
    };
    api.getDetailByDateRange = function(start, end, dates){
        var res = [];
        $.each(dates, function(k, v){
            res.push({
                "id":"date",
                code:v,
                "hour":7.5,
                state: "closed",
                chedk: true,
                "children":[
                    {code: 'asdf1', pName: 'foo', hour: Math.random()},
                    {code: 'asdf2', pName: 'bar', hour: 3},
                    {code: 'asdf3', pName: 'exi', hour: 2.5}
                ]
            })
        });
        return res;
    };
    // todo: 修订列子段
    $("#history-detail").treegrid({
        showHeader: true,
        showFooter: true,
        checkbox: function(row){return row.id=='date';},
        lines: true,
        rownumbers: true,
        idField: 'code',
        treeField: 'code',
        toolbar: '#history-table-footer',
        columns: [[
            {field: 'code', title: '编号', width: 200},
            {field: 'pName', title: '项目名称', width: 100},
            {field: 'hour', title: '工时', width: 100, align: 'right'}
        ]]
    });
    // todo: 编辑表格改为单日
    $("#edit-detail").datagrid({
        toolbar: $("#edit-table-toolbar"),
        showHeader: true,
        showFooter: true,
        checkbox: true,
        rownumbers: true,
        columns: [[
            {field: 'ck', checkbox: true},
            {field: 'code', title: '编号', width: 100},
            {field: 'pName', title: '项目名称', width: 100},
            {field: 'hour1', title: '星期一', align: 'right'},
            {field: 'hour2', title: '星期二', align: 'right'},
            {field: 'hour3', title: '星期三', align: 'right'},
            {field: 'hour4', title: '星期四', align: 'right'},
            {field: 'hour5', title: '星期五', align: 'right'},
            {field: 'hour6', title: '星期六', align: 'right'},
            {field: 'hour7', title: '星期日', align: 'right'}
        ]]
    });
    $("#history-calendar").fullCalendar({
        fit: true,
        history: true,
        multiSelect: true,
        onSelect: function (date, target) {
            $("#history-detail").treegrid("loadData",api.getDetailByDate($(target).attr('abbr')));
        },
        onMultiSelect: function(start, end, dates, target){
            console.log(start, end);
            $("#history-detail").treegrid("loadData",api.getDetailByDateRange(start, end, dates));
        },
        onCheckNode: function(node){
            console.log(node);
        }
    });
    $("#edit-calendar").fullCalendar({
        fit: true,
        history: false
    });
    $("#chedkAll").click(function(){
        $("#history-detail").parent().find("span.tree-checkbox").each(function(){
            $(this).removeClass("tree-checkbox0").addClass("tree-checkbox1");
        })
    });
    $("#reverseCheck").click(function(){
        $("#history-detail").parent().find("span.tree-checkbox").each(function(){
            $(this).toggleClass("tree-checkbox0").toggleClass("tree-checkbox1");
        })
    });
    $("#getChedked").click(function(){
        // todo: 获取已选日期，改为复制已选日期按钮。在多天对多天工时复制时使用
        var checked = [];
        $("#history-detail").parent().find("span.tree-checkbox1").each(function(){
            checked.push($(this).parent().parent().parent().attr("node-id"));
        });
        console.log(checked);
        alert(checked);
        return checked;
    })

})(jQuery);
