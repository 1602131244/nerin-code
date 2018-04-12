var westexpanded = false;
var pubgriddataurl = "http://"+window.location.host+"/api/lev2/queryPublishStructureList";//"datagrid_pubversion.json";//发布版数据源 oracle包-当前项目阶段发布版WBS版本列表
var pubwbsdataurl = "http://"+window.location.host+"/api/lev2/queryPublishStructureDetails";//"datagrid_pubwbs.json";//发布版详细数据源 oracle包-加载选中的发布版WBS详细数据

var prjhisdataurl = "http://"+window.location.host+"/api/lev2/queryHistoryProjectList";//"datagrid_prjhis.json";//历史项目数据源 oracle包-模糊查找我参与过的历史项目列表
var prjhis_phasedataurl = "http://"+window.location.host+"/api/lev2/queryHistoryStructureList";//"datagrid_prjhisphase.json";//历史项目阶段数据源 oracle包-加载历史项目阶段列表
var prjhis_phase_wbsvar_treedataurl = "http://"+window.location.host+"/api/lev2/queryHistoryWbsDetails";//"datagrid_prjhisphasewbsvartree.json";//历史项目发布版WBS树 oracle包-加载选中的历史项目WBS详细数据
var prjhis_phase_pbsvar_treedataurl = "http://"+window.location.host+"/api/lev2/queryHistoryPbsDetails";//"datagrid_prjhisphasepbsvartree.json";//历史项目发布版PBS详细树 oracle包-加载选中的历史项目PBS详细数据

var industrydataurl = "http://"+window.location.host+"/api/lev2/queryIndustryList";//"data_industry.json"; //行业数据源 oracle包-我参与过的历史项目所属行业列表
var industrysysdataurl = "http://"+window.location.host+"/api/lev2/queryAllIndustryList";//"data_industry.json"; //行业数据源 oracle包-系统所属行业列表

var prj_sys_dataurl = "http://"+window.location.host+"/api/lev2/queryHistorySystemList";//"datagrid_PrjSys.json";//引用系统数据源 oracle包-模糊查找系统列表

var industrysubdataurl = "http://"+window.location.host+"/api/lev2/queryAllIndustryList";//"data_industry.json"; //行业数据源 oracle包 - 子项所属行业列表
var prj_sub_dataurl = "http://"+window.location.host+"/api/lev2/queryHistorySubList";//"datagrid_Prjsub.json";//引用子项数据源 oracle包-模糊查找子项列表
var prj_major_saveurl = "http://"+window.location.host+"/api/lev2/creatIndustriesRate";//"datagrid_Prjmajor.json";//保存专业比例的url oracle包-保存专业总工作量比例

var prjhistreemaxid = 0;
var pubId = null;//当前进入的发布版wbs节点id
var hisId = null;//当前进入的历史项目结构节点id

//第一次展开时的异步加载
$(document.body).layout('panel', 'west').panel({
	onExpand: function () {
		if (!westexpanded) {
			westexpanded = true;
			//第一次展开,加载发布版
			LoadPublic(prjid);
			LoadMajor();
		}
	}
});

$('#reftabs').tabs({
	onSelect: function (title, index) {
		switch (title) {
			case "发布版":
				LoadPublic(prjid);
				break;
			case "历史项目":
				LoadPrjHisTree();
				break;
			case "系统":
				LoadSys();
				break;
			case "子项":
				LoadSub();
				break;
			case "专业":
				break;
		}
	}
});


/*****************本项目发布版tab相关处理******************/
var pubLoaded = false;
//加载项目的发布版信息
function LoadPublic(prjid) {
    if (!pubLoaded) {
        pubLoaded = true;
        $('#pub_grid').datagrid({
			method: 'get',
            url: pubgriddataurl,
            idField: 'pubwbsID',
            fitColumns: true,
            queryParams: {
                projID: prjid,
                phaseID: prjPhaseID
            },
            rownumbers: true, singleSelect: true, pagination: true, pageList: [5, 10, 15, 20], pageSize: 5,
            fit: true,
            columns: [[{ field: 'pubwbsName', title: '发布版WBS名称', width: 299 },
                { field: 'pubTime', title: '发布时间', width: 126,fixed:true },
                { field: 'pubOperator', title: '操作人', width: 55, fixed:true }]],
            onDblClickRow: function (index, row) {
				LoadWbsTree(row.structureId);
				$('#pub_layout').layout('panel', 'north').panel("setTitle", "当前已进入："+row.pubwbsName);
				$('#pub_layout div.panel-title').text("当前已进入："+row.pubwbsName);
				$('#pub_layout_center div.panel-title').text("");
				$('#pub_layout_center').layout('panel', 'west').panel("setTitle", "导航 - 系统子项树");
			}
        });
		$('#pubtree').tree({//发布版树
			formatter:function(node){
				var txt = node.name;
				if(node.level!="root") txt = node.code+" "+txt;
				if(dual && node.dualName && node.dualName!="") txt = txt+" | "+node.dualName;
				var childrenNum = 0;
				if(node.children) childrenNum = node.children.length;
				if(node.spechildren) childrenNum = node.spechildren.length;
				if(node.status == "失效"){
					var style = "background-color:lightgray;color:#fff";
				}else if(childrenNum == 0){
					var style = "color:red";
				}else var style = "";
				txt = "<span style='"+style+"'>"+txt+" - "+childrenNum+"</span>";
				txt += "&nbsp;<a id='gopub"+node.id+"'></a>";
				return txt;
			},
			onSelect:function(node){
				_uncheckGrid('#pubgrid');
				$('a[id^="gopub"]').hide();
				$('#gopub'+pubId).show();
				$('#gopub'+node.id).linkbutton({
					onClick:function(){
						_enterNode(node.id, '#pubtree', '#pubgrid');
					}
				}).show();
				if(node.id!=pubId)
					$('#gopub'+node.id).linkbutton('enable').html('&nbsp;进入&nbsp;');
				else $('#gopub'+node.id).linkbutton('disable').html('&nbsp;已进入&nbsp;');
			},
			onDblClick:function(node){
				if(node.id!=pubId){
					_enterNode(node.id,'#pubtree','#pubgrid');
					$('#pubtree').tree('select', node.target);
				}
			},
			onLoadSuccess:function(node,data){
				$('#pub_layout').layout('collapse', 'north');
				$('#pub_layout_center').layout('expand', 'west');
			}
		});
		$('#pubgrid').datagrid({//发布版详情表
			idField: 'id',
			rownumbers: true,
			fit: true,
			border:false,
			checkbox: true,
			columns: [[{field:'ck',checkbox:true },
				{ field: 'code', title: '节点代号', align: 'left', halign: 'center', formatter:function(value,row,index){
					var iconsrc = {"sys":"sysbr.png","div":"subbr.png","spe":"probr.png"};
					return '<img src="../../themes/icons/'+iconsrc[row.level]+'" /> '+value;
					} },
				{ field: 'name', title: '节点名称', align: 'left', halign: 'center' },
				{ field: 'startDate', title: '计划起始日期'},
				{ field: 'endDate', title: '计划完成日期'},
				{ field: 'workCoef', title: '工作量系数', hidden:true },
				{
					field: 'status', title: '状态',
					styler: function (value, row, index) {
						if (row.status == "失效") {
							return 'background-color:lightgray;';
						}
					}
				},
				{ field: 'dualName', title: '双语名称',hidden:true }
			]],
			onLoadSuccess:function(){
				if (dual)	$('#pubgrid').datagrid('showColumn','dualName');
				if (headOrbranch != "head")	$('#pubgrid').datagrid('showColumn','workCoef');
				var node = $('#pubtree').tree('find', pubId);
				var dualname = "";
				if(dual) dualname = " | "+node.dualName;
				_updateSpeTitle('#pubtree', '#pubpage');//$('#pubpage').panel('setTitle', node.code+" "+node.name+dualname+" - 状态："+node.status);
			},
			onSelect:function(){
				_uncheckTree('#pubtree');
			},
			onCheck:function(){
				_uncheckTree('#pubtree');
			}
		});
    }
}
//重载发布版
function reLoadPubGrid() {
    if (pubLoaded) {
        $('#pub_grid').datagrid("reload", {
            projID: prjid,
            phaseID: prjPhaseID
        });
    }
}

//加载单个发布版的wbs信息
function LoadWbsTree(pubwbsID) {
	$.ajax({
		url:pubwbsdataurl + "?pubwbsID=" + pubwbsID + "&phaseCode=" + prjPhaseID,
		type:"GET",
		success:function(data){
			var wbsDiv = true;
			if(data.divison!=undefined) wbsDiv = data.divison;
			var reData = [];
			_reObj(data.paDetailsDTO, wbsDiv, reData);
			$('#pubtree').tree('loadData',reData);
			_enterNode(reData[0].id, '#pubtree', '#pubgrid');
			$('#pubtree').tree('select',$('#pubtree').tree('find',reData[0].id).target);
		}
	});
    $('#pubgrid').datagrid("clearSelections");
}


/*****************本项目发布版tab相关end******************/



/*****************历史项目tab相关******************/
var hisprjloaded = false;
//var hisPhaseName = '';
function LoadPrjHisTree() {
    if (!hisprjloaded) { 
        hisprjloaded = true;
        $('#prjhisgrid').treegrid({
            url: '',
            method: 'GET',
            idField: 'id',
            treeField: 'name',
			fitColumns:true,
            pagination: true, pageList: [5, 10, 15, 20], pageSize: 5,
            columns: [[
                { field: 'name', title: '名称', width: 400 },
                { field: 'time', title: '发布/保存日期', width: 126 ,fixed:true},
                { field: 'Operator', title: '操作人', width: 50 , fixed:true}]],
            onDblClickRow: function (row) {
                if (!row.structureId || row.structureId == 0){
					$("#prjhisgrid").treegrid('toggle', row.id);
				}else{
					var wbsID=row.structureId;
					var type = row.strClass;
					if(wbsID!==null&&wbsID!=0){
						LoadWbsHisTree(wbsID,type);
						$('#his_layout').layout('panel', 'north').panel("setTitle", "当前已进入："+row.name);
						$('#his_layout div.panel-title').text("当前已进入："+row.name);
						$('#his_layout_center div.panel-title').text("");
						$('#his_layout_center').layout('panel', 'west').panel("setTitle", "导航 - 系统子项树");
					}
				}
            }
        });
		$('#prjhisgrid').treegrid('options').url = prjhisdataurl+"?"+userPar;
        //加载行业
        $('#btnprjindustry').menubutton({menu: '#menuindustry' });
        LoadPrjIndustry();
		$('#prjhissearch').searchbox({
			searcher: function (value, name) {
				$('#prjhisgrid').treegrid('load', {
					class: $("#btnprjindustry").text(),
					keywords: value
				});
			}
		});
		$('#histree').tree({//历史项目树
			formatter:function(node){
				var txt = node.name;
				if(node.level!="root") txt = node.code+" "+txt;
				if(dual && node.dualName && node.dualName!="") txt = txt+" | "+node.dualName;
				var childrenNum = 0;
				if(node.children) childrenNum = node.children.length;
				if(node.spechildren) childrenNum = node.spechildren.length;
				if(node.status == "失效"){
					var style = "background-color:lightgray;color:#fff";
				}else if(childrenNum == 0){
					var style = "color:red";
				}else var style = "";
				txt = "<span style='"+style+"'>"+txt+" - "+childrenNum+"</span>";
				txt += "&nbsp;<a id='gohis"+node.id+"'></a>";
				return txt;
			},
			onSelect:function(node){
				_uncheckGrid('#hisgrid');
				$('a[id^="gohis"]').hide();
				$('#gohis'+hisId).show();
				$('#gohis'+node.id).linkbutton({
					onClick:function(){
						_enterNode(node.id, '#histree', '#hisgrid');
					}
				}).show();
				if(node.id!=hisId)
					$('#gohis'+node.id).linkbutton('enable').html('&nbsp;进入&nbsp;');
				else $('#gohis'+node.id).linkbutton('disable').html('&nbsp;已进入&nbsp;');
			},
			onDblClick:function(node){
				if(node.id!=hisId){
					_enterNode(node.id,'#histree','#hisgrid');
					$('#histree').tree('select', node.target);
				}
			},
			onLoadSuccess:function(node,data){
				$('#his_layout').layout('collapse', 'north');
				$('#his_layout_center').layout('expand', 'west');
				if (data[0].pbsId){
					$('#hisgrid').datagrid('hideColumn','startDate');
					$('#hisgrid').datagrid('hideColumn','endDate');
					$('#hisgrid').datagrid('hideColumn','workCoef');
					$('#hisgrid').datagrid('hideColumn','status');
				}else if(data[0].wbsId){
					$('#hisgrid').datagrid('showColumn','status');
					$('#hisgrid').datagrid('showColumn','startDate');
					$('#hisgrid').datagrid('showColumn','endDate');
				}
			}
		});
		$('#hisgrid').datagrid({//历史项目详情表
			idField: 'id',
			rownumbers: true,
			fit: true,
			border:false,
			checkbox: true,
			columns: [[{field:'ck',checkbox:true },
				{ field: 'code', title: '节点代号', align: 'left', halign: 'center', formatter:function(value,row,index){
					var iconsrc = {"sys":"sysbr.png","div":"subbr.png","spe":"probr.png"};
					return '<img src="../../themes/icons/'+iconsrc[row.level]+'" /> '+value;
					} },
				{ field: 'name', title: '节点名称', align: 'left', halign: 'center' },
				{ field: 'startDate', title: '计划起始日期', hidden:true},
				{ field: 'endDate', title: '计划完成日期', hidden:true},
				{ field: 'workCoef', title: '工作量系数', hidden:true },
				{
					field: 'status', title: '状态', hidden:true,
					styler: function (value, row, index) {
						if (row.status == "失效") {
							return 'background-color:lightgray;';
						}
					}
				},
				{ field: 'dualName', title: '双语名称', hidden:true }
			]],
			onLoadSuccess:function(){
				if (dual)	$('#hisgrid').datagrid('showColumn','dualName');
				if (headOrbranch != "head")	$('#hisgrid').datagrid('showColumn','workCoef');
				var node = $('#histree').tree('find', hisId);
				var dualname = "";
				if(dual && node.dualName && node.dualName!="") dualname = " | "+node.dualName;
				_updateSpeTitle('#histree', '#hispage');
			},
			onSelect:function(){
				_uncheckTree('#histree');
			},
			onCheck:function(){
				_uncheckTree('#histree');
			}
		});
    }

}
var loadedIndustry = false;
//载入行业选择
function LoadPrjIndustry() {
    if (loadedIndustry) return;
    else loadedIndustry = true;
    $.ajax({
        url: industrydataurl+"?"+userPar,
        async: false,
        type: "GET",
        success: function (data) {
			if (typeof (data) == "string")
                data = eval(data);
			var industryArr = [{"industryName":"全部行业"}].concat(data);
            for (var i = 0; i < industryArr.length; i++) {
                $('#menuindustry').menu('appendItem', {
                    text: industryArr[i].industryName,
                    onclick: function () {
                        $('#btnprjindustry').menubutton({ menu: '#menuindustry', text: this.innerText });
                    }
                });
            }
        }
    });
}

//加载wbs详细树信息
function LoadWbsHisTree(wbsid, type) {
	if(type=="WBS"){
		var url = prjhis_phase_wbsvar_treedataurl + "?wbsID=" + wbsid;
	}else var url = prjhis_phase_pbsvar_treedataurl + "?pbsID=" + wbsid;
	$.ajax({
		url:url,
		type:"GET",
		success:function(data){
			var wbsDiv = true;
			if(data.divison!=undefined) wbsDiv = data.divison;
			var reData = [];
			_reObj(data.paDetailsDTO ? data.paDetailsDTO : data, wbsDiv, reData, "his");
			$('#histree').tree('loadData',reData);
			_enterNode(reData[0].id, '#histree', '#hisgrid');
			$('#histree').tree('select',$('#histree').tree('find',reData[0].id).target);
		}
	});
}
/*****************历史项目tab相关end******************/


/*****************系统tab相关******************/
var sysloaded = false;
function LoadSys() {
    if (sysloaded) return false;
    else sysloaded = true;
    $('#gridPrjSys').datagrid({
        url: '',
        idField: 'projID',
        rownumbers: true, singleSelect: false, pagination: true, method: 'get', pageSize: 20,
        fit: true,
		fitColumns:true,
        height: 162,
        columns: [[
            { field: 'name', title: '系统名称', width: 180 },
            { field: 'industry', title: '行业', width: 100 },
            { field: 'dual', title: '双语名称', width: 180 }]],
        onLoadSuccess: function (data) {
            $('#gridPrjSys').datagrid("clearSelections");
            $('#gridPrjSys').datagrid("unselectAll");
        }
    });
	$('#gridPrjSys').datagrid('options').url = prj_sys_dataurl;

    $('#prjsyssearch').searchbox({
        searcher: function (value, name) {
            SearchPrjSys(value);
        }
    });
    $('#btnprjindustrysys').menubutton({menu: '#menuindustrysys' });
    LoadPrjIndustrysys();
}
//载入行业选择
function LoadPrjIndustrysys() {
    $.ajax({
        url: industrysysdataurl,
        async: false,
        type: "GET",
        success: function (data) {
            if (typeof (data) == "string")//字符转对象
                data = eval(data);
			var industryArr = [{"industryName":"全部行业"}].concat(data);
            for (var i = 0; i < industryArr.length; i++) {
                $('#menuindustrysys').menu('appendItem', {
                    text: industryArr[i].industryName,
                    onclick: function () {
                        $('#btnprjindustrysys').menubutton({ menu: '#menuindustrysys', text: this.innerText });
                    }
                });
            }
        }
    });
}
function SearchPrjSys(value) {
    $('#gridPrjSys').datagrid('load', {
        class: $("#btnprjindustrysys").text(),
        keywords: value
    });

}
/*****************系统tab相关end******************/



/*****************子项tab相关******************/

var subloaded = false;
function LoadSub() {
    if (subloaded) return;
    else subloaded = true;
    $('#gridPrjsub').datagrid({
        url: '',
        idField: 'projID',
        rownumbers: true, singleSelect: false, pagination: true, method: 'get', pageSize: 20,
        fit: true,
		fitColumns: true,
        height: 162,
        columns: [[
            { field: 'name', title: '子项名称', width: 180 },
            { field: 'industry', title: '行业', width: 100 },
            { field: 'dual', title: '双语名称', width: 180 }]],
        onSelect: function (index, row) {
        },
        onLoadSuccess: function (data) {
            $('#gridPrjsub').datagrid("clearSelections");
            $('#gridPrjsub').treegrid("unselectAll");
        }
    });
	$('#gridPrjsub').datagrid('options').url = prj_sub_dataurl;

    $('#prjsubsearch').searchbox({
        searcher: function (value, name) {
            SearchPrjSub(value);
        }
    });
    $('#btnprjindustrysub').menubutton({menu: '#menuindustrysub' });
    LoadPrjIndustrysub();
}

//载入行业选择
function LoadPrjIndustrysub() {
    $.ajax({
        url: industrysubdataurl,
        async: false,
        type: "GET",
        success: function (data) {
            if (typeof (data) == "string")//字符转对象
                data = eval(data);
			var industryArr = [{"industryName":"全部行业"}].concat(data);
            for (var i = 0; i < industryArr.length; i++) {
                $('#menuindustrysub').menu('appendItem', {
                    text: industryArr[i].industryName,
                    onclick: function () {
                        $('#btnprjindustrysub').menubutton({ menu: '#menuindustrysub', text: this.innerText });
                    }
                });
            }
        }
    });
}
function SearchPrjSub(value) {
    $('#gridPrjsub').datagrid('load', {
        class: $("#btnprjindustrysub").text(),
        keywords: value
    });

}

/*****************子项tab相关end******************/



/*****************专业tab相关******************/

var majorloaded = false;
function LoadMajor() {
    if (majorloaded) return;
    else majorloaded = true;
    $('#gridPrjmajors').datagrid({
		data: JSON.parse(speData),
		method: 'get',
        idField: 'code',
        rownumbers: true, singleSelect: false, pagination: false,
        fit: true,
		fitColumns: true,
        columns: [[
            { field: 'ck', checkbox: true },
            { field: 'code', title: '专业代字', width: 70 },
            { field: 'name', title: '专业名称', width: 180 },
            { field: 'dual', title: '双语名称', width: 180 },
            { field: 'ratio', title: '总工作量比例%', width: 100 }]],
        onLoadSuccess: function (data) {
			prjmajors = [];
			for(var i =0;i<data.rows.length;i++){
				prjmajors[prjmajors.length] = data.rows[i];
			}
        }
    });

    $('#prjmajorsearch').searchbox({
        searcher: function (value, name) {
            SearchPrjMajor(value);
        }
    });

}
$('#refreshspelist').linkbutton({
	iconCls: 'icon-refresh',
	plain: true,
	onClick: function () {
		_refreshSpecList();
	}
});
function _refreshSpecList(){
	$('#gridPrjmajors').datagrid('loading');
	$('#gridPrjmajors').datagrid({
		url: prj_major_dataurl+"?projID="+prjid+"&phaseID="+prjPhaseID+"&"+userPar,
		onLoadSuccess:function(data){
			$('#gridPrjmajors').datagrid('loaded');
			speData = JSON.stringify(data.rows);
		}
	});
}
$('#prjmajorrates').linkbutton({
	iconCls: 'icon-edit',
	plain: true,
    onClick: function () {
		$('#grid_majorsets').datagrid({
			queryParams: {
				projID: prjid,
				phaseID: prjPhaseID
			},
			data: JSON.parse(speData),
			onClickRow: onClickMajorRow
		});
        $('#divmajorset').window('open');
    }
});

function onAfterMajorEdit() {
    var total = 0;
    var datarows = $('#grid_majorsets').datagrid("getRows")//"getChecked");
    for (var i = 0; i < datarows.length; i++) {
        if (datarows[i].ratio != "") {
			var rnd = Math.round(parseFloat(datarows[i].ratio)*100)/100;
			$('#grid_majorsets').datagrid('updateRow', {
                    index: $('#grid_majorsets').datagrid("getRowIndex", datarows[i]),
                    row: { ratio: rnd }
                });
            total += rnd;
        }
    }
	total = parseFloat(Math.round(total*100)/100);
    document.getElementById("majorsetmsgs").innerHTML = "提示:当前比例总和为" + total + "%。";
}

var editIndex = undefined;
function endEditing() {
    if (editIndex == undefined) { return true }
    if ($('#grid_majorsets').datagrid('validateRow', editIndex)) {
        var ed = $('#grid_majorsets').datagrid('getEditor', { index: editIndex, field: 'ratio' });
        if(ed != null){
			var radio = $(ed.target).numberbox('getValue');
			$('#grid_majorsets').datagrid('getRows')[editIndex]['ratio'] = radio;
			$('#grid_majorsets').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		}else return true;
    } else {
        return false;
    }
}

function onClickMajorRow(index) {
    if (editIndex != index) {
        if (endEditing()) {
            $('#grid_majorsets').datagrid('selectRow', index)
                    .datagrid('beginEdit', index);
            editIndex = index;
        } else {
            $('#grid_majorsets').datagrid('selectRow', editIndex);
        }
    }
}

$('#majorratesave').linkbutton({
    onClick: function () {
        $('#grid_majorsets').datagrid('acceptChanges');
        
		var total = 0;
		var datarows = $('#grid_majorsets').datagrid("getRows");
		for (var i = 0; i < datarows.length; i++) {
			if (datarows[i].ratio != "") {
				var rnd = Math.round(parseFloat(datarows[i].ratio)*100)/100;
				if (rnd < 0) {
                    document.getElementById("majorsetmsgs").innerHTML = "不可保存：比例存在负值,请修改！";
                    return false;
                }
				$('#grid_majorsets').datagrid('updateRow', {
						index: $('#grid_majorsets').datagrid("getRowIndex", datarows[i]),
						row: { ratio: rnd }
					});
				total += rnd;
			}
		}
		total = parseFloat(Math.round(total*100)/100);
        if (total > 100) {
            document.getElementById("majorsetmsgs").innerHTML = "提示:比例超过100,无法保存。";
            return false;
        }
        else if (total < 100) {
            document.getElementById("majorsetmsgs").innerHTML = "不可保存：总和不等于100%！当前比例总和为" + total + "%";
            return false;
        }
        var specialty = [];
        for (var i = 0; i < datarows.length; i++) {
            specialty[specialty.length] = { "id": datarows[i].id, "ratio": datarows[i].ratio, "industryCode": datarows[i].industryCode };
        }
        var subitem ={ projID: prjid,specialtyRatio:specialty };//【对接修正】161017
        $.ajax({
            url: prj_major_saveurl+"?"+userPar,
            data: JSON.stringify(subitem),
            contentType: "application/json",
            type: "POST",
            success: function (data) {
                if (data.speRatio == "done") {
                    document.getElementById("majorsetmsgs").innerHTML = "提示:保存成功。";
					_refreshSpecList();
                }
                else
                    document.getElementById("majorsetmsgs").innerHTML = "提示:不可保存！提交服务器失败，请检查后重试或联系系统管理员。";
            }
        });
        editIndex = undefined;
    }
});

//平分剩余比例
$('#majorratesplit').linkbutton({
    onClick: function () {
        $('#grid_majorsets').datagrid('acceptChanges');
        var total = 0;
        var datarowsall = $('#grid_majorsets').datagrid("getRows")//getChecked");
        var datarows = $('#grid_majorsets').datagrid("getChecked");
		var unchecktotal = 0;
        for (var k = 0; k < datarowsall.length; k++) {
            var unchecked = true;
            for (var i = 0; i < datarows.length; i++) {
                if (datarows[i].code == datarowsall[k].code) {
                    unchecked = false;
                    break;
                }
            }
            if (unchecked && datarowsall[k].ratio != "") {
                unchecktotal += parseFloat(datarowsall[k].ratio);
            }
        }
        if (unchecktotal < 100) {
            var left = 100 - unchecktotal;
            var avg = Math.round(left / datarows.length * 100) / 100;
            var totaladded = 0;
            for (var i = 0; i < datarows.length; i++) {
                totaladded += avg;
                if (i == datarows.length - 1 && totaladded != left)
                    avg = Math.round((avg + left - totaladded) * 100) / 100;
                $('#grid_majorsets').datagrid('updateRow', {
                    index: $('#grid_majorsets').datagrid("getRowIndex", datarows[i]),
                    row: { ratio: avg }
                });
            }
        }
        editIndex = undefined;
        onAfterMajorEdit();
    }
});

//求和计算
$('#majorratesum').linkbutton({
    onClick: function () {
        $('#grid_majorsets').datagrid('acceptChanges');
        onAfterMajorEdit();
    }
});

function SearchPrjMajor(value) {
	var key = value.split(" ");
    var datarows = $('#gridPrjmajors').datagrid("getData").rows;
    for (var i = 0; i < datarows.length; i++) {
        for(var j = 0; j < key.length; j++){
			if(key[j] != "" && (datarows[i].code.toString().toLowerCase().indexOf(key[j].toString().toLowerCase())==0||datarows[i].name.toString().toLowerCase().indexOf(key[j].toString().toLowerCase())!=-1))
			$('#gridPrjmajors').datagrid("selectRecord", datarows[i].code);
		}
    }

}

/*****************专业tab相关end******************/