//获取项目数据的url
var prjdataurl = "http://"+window.location.host+"/api/lev2/queryProjectList";//"datagrid_project.json";//oracle包-模糊查找项目列表及信息（包括各项目阶段列表）
var prjphasedataurl = "http://"+window.location.host+"/api/lev2/queryPhaseList";//"dataphase.json";//oracle包-当前项目阶段列表
var northexpanded = false;
$(function () {
    //设置标题
    $('#north').panel({ title: prjName + " ("+prjNumber+") " + "项目阶段:" + prjPhase + " 项目经理:" + prjManagerName});
    $(document.body).layout('collapse', 'north');
    //第一次展开时的异步加载项目
    $(document.body).layout('panel', 'north').panel({
        onExpand: function () {
            if (!northexpanded) {
                northexpanded = true;
                $('#prjgrid').datagrid({
                    url: '',
					method: 'get',
                    idField: 'projID',
                    rownumbers: true, singleSelect: true, pagination: true,pageList: [5, 10, 15, 20], pageSize: 5,
                    height: 162, fit: true,
                    sortName: 'startDate',
                    order: 'desc',
                    multiSort: true,
                    remoteSort: true,
                    columns: [[{ field: 'projNumber', title: '项目编号'},
                        { field: 'projName', title: '项目简称',width:190},
                        { field: 'projLongName', title: '项目全称',width:330},
                        { field: 'mgr', title: '项目经理'},
                        { field: 'startDate', title: '开始日期', align: 'right', sortable: true },
                        { field: 'endDate', title: '结束日期', align: 'right', sortable: true },
                        { field: 'status', title: '状态' },
                        { field: 'customer', title: '客户' },
                        { field: 'class', title: '行业'},
                        { field: 'orgName', title: '所属组织' },
                        { field: 'type', title: '项目类型' }]],
                    onSelect: function (index, row) {
                        $('a[id^="proj_a"]').hide();
                        var prjID = row.projID;
                        if (!$('#menu' + prjID)[0]) {
                            $('#proj_a' + prjID).append("<div id=menu" + prjID + "></div>");
                        }
                        $('#proj_a' + prjID).menubutton({
                            menu: '#menu' + prjID
                        }).html('&nbsp;<font color=black>进入</font>&nbsp;').css("background-color","GhostWhite").show();
                        for (var i = 0; i < row.phaseList.length; i++) {
                            var phase = row.phaseList[i];
                            if (!$('#menu' + prjID).menu('findItem', phase.phaseName))
                                $('#menu' + prjID).menu('appendItem', {
                                    text: phase.phaseName,
                                    disabled: prjID == prjid && phase.phaseName == prjPhase,
                                    id: phase.phaseID,
                                    onclick: function () {
                                        var item = $('#menu' + prjID).menu('findItem', this.innerText);
                                        SwitchProjectPhase(prjID, item.id);
                                    }
                                });
                        }
                    }
                });
				$('#prjgrid').datagrid('options').url = prjdataurl;
                //加载阶段
                $('#btnprjphase').menubutton({menu: '#menuphase' });
                LoadPrjPhase(prjid);
            }
        }
    });

    //查询项目
    $('#prjsearch').searchbox({
        searcher: function (value, name) {
            SearchProject(value);
        }
    });
});


function SearchProject(keyword) {
    $('#prjgrid').datagrid('load', {
        userID: userID,
        keywords: keyword
    });
}

//载入阶段选择
function LoadPrjPhase(prjid) {
    $.ajax({
        url: prjphasedataurl,
        data: { projID: prjid },
        async: true,
        type: "GET",
        success: function (data) {
            if (typeof (data) == "string")//字符转对象
                data = JSON.parse(data);
            for (var i = 0; i < data.length; i++) {
                var phase = data[i];
				if (!$('#menuphase').menu('findItem', phase.phaseName))
					$('#menuphase').menu('appendItem', {
						text: phase.phaseName,
						disabled: phase.phaseName == prjPhase,
						id: phase.phaseID,
						onclick: function () {
							var item = $('#menuphase').menu('findItem', this.innerText);
							SwitchProjectPhase(prjid, item.id);
						}
					});
            }
        }
    });
}

//加载所有专业
var prjmajors = [];

//切换项目阶段
//根据实际url地址,切换项目的url和参数
function SwitchProjectPhase(prjid, phaseID) {

		$.messager.confirm('操作提示', '将放弃所有未保存的编辑，请确认是否继续？', function (r) {
			if (r) window.location.href = "http://"+window.location.host+window.location.pathname+"?projID="+prjid+"&phaseID="+phaseID;
		});
	}