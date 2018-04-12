var strArr = ["招(投)标","总体规划","修建性规划","控制性规划","国外矿山项目详细设计","国外矿山项目国际银行融资","国外矿山项目中国标准","国外矿山项目预可研","国内矿山项目","国内矿山项目预可研","矿产资源开发利用","国内矿山矿床工业指标","详细规划","工业项目","市政工程","民用项目"];
var titleArr = ["建筑设计院民用类","环境影响评价报告书","安全与职业卫生专篇"];
var tabindex = 0;
var role;
var spec;
var checkRowsNum = [{'rownum': 0}, {'rownum': 0}, {'rownum': 0}];
var newConfig = new nerinJsConfig();
if (newConfig.evn == "dev") {
    //测试环境变量头
    var oaurl = [{'customid': 569, "formId": 385, "modeId": 361}, {'customid': 555, "formId": 374, "modeId": 353},
        {'customid': 572, "formId": 388, "modeId": 364}, {'customid': 571, "formId": 387, "modeId": 363}];
    var url_oa = "http://192.168.15.198/login/LoginSSO.jsp";
    var url_oa_server = "http://192.168.15.198";
    //测试环境变量尾
}
else{
    //正式环境
    var oaurl = [{'customid': 535, "formId": 378, "modeId": 332}, {'customid': 534, "formId": 377, "modeId": 331},
        {'customid': 536, "formId": 379, "modeId": 333}, {'customid': 537, "formId": 381, "modeId": 334}];
    var url_oa = "http://oa.nerin.com/login/LoginSSO.jsp";
    var url_oa_server = "http://oa.nerin.com";
}
//正式环境 */
var textArr = [];
var url_get_role = "http://" + window.location.host + "/api/dsin/diAuthority";
var url_get_spec_tree = "http://" + window.location.host + "/api/dsin/specTree";
var url_get_spec_grid = "http://" + window.location.host + "/api/dsin/specGrid";
var url_get_proj_grid = "http://" + window.location.host + "/api/dsin/projGrid";
var url_get_equi_data = "http://" + window.location.host + "/api/dsin/equiData";
var url_get_temp_grid = "http://" + window.location.host + "/api/dsin/tempGrid";
var url_get_erpid = "http://" + window.location.host + "/api/dsin/getErpId";
var url_get_spec = "http://" + window.location.host + "/api/dsin/getSpecList";
var url_change_stauts = "http://" + window.location.host + "/api/dsin/changeStauts";
var url_get_approve_erpid = "http://" + window.location.host + "/api/dsin/approve";
var url_delete = "http://" + window.location.host + "/api/dsin/deleteDoc";
var url_oa_edit = url_oa_server + "/formmode/view/AddFormMode.jsp";
var url_oa_view = url_oa_server + "/formmode/view/AddFormMode.jsp?type=0&opentype=0&viewfrom=fromsearchlist";
var tabLoadedArr = [0];

function get_role() {
    $.ajax({
        url: url_get_role + "?prjid=" + prjid + "&" + personPar,
        contentType: 'application/x-www-form-urlencoded',
        async: false,
        type: "GET",
        success: function (data) {
            role = data.role;
            spec = data.spec;
        }
    });
}
//是否是专业负责人所在专业
function isSpec(theSpec) {
    var array = new Array();
    array = spec.split(",");
    for (i = 0; i < array.length; i++)
        if (theSpec == array[i]) return true;
    return false;
}

function load_tab_grid_0() {
	$('input[id^="summit_0_"]').prop("checked",false);
    $('#tabgrid_0').datagrid({
        url: url_get_proj_grid + "?prjid=" + prjid + "&prjPhaseID=" + prjPhaseID,
        method: 'get',
        idField: 'id',
        rownumbers: true,
        //singleSelect: true,
        //pagination: true, pageList: [5, 10, 15, 20], pageSize: 5,
        height: 162, fit: true, striped:true,
        sortName: 'receiveDate',
        order: 'desc',
        multiSort: true,
        remoteSort: true,
        checkbox: true,
		frozenColumns: [[
			{field: 'ck', checkbox: true},
            {
                field: 'name', title: '名称', formatter: function (value, row, index) {
                return row.name + ' <a id ="href_' + tabindex + '_' + row.id + '" style="color:blue" target="_blank" href="' + url_oa_view + "&modeId=" + oaurl[tabindex].modeId + "&formId=-"
                    + oaurl[tabindex].formId + "&billid=" + row.id + "&customid=" + oaurl[tabindex].customid + '">查看</a>';
            }
            },
            {field: 'psyj', title: '评审结论'},
			{field: 'id', title: '评审状态',align:'center', formatter: function (value, row, index) {
				return row.approveStauts+' <a style="color:grey" href="javascript:_oarec(0,'+row.id+');">记录</a>';
			},styler: function(value,row,index){
				if($('#summit_0_'+row.id).is(":checked"))
					return 'background-color:#EEE8AA;color:red;';
			}
			},
			{field: 'approveStauts', title: '加入评审队列',align:'center', formatter: function (value, row, index) {
				if(role == "mgr" && (value=="待评审" || value=="再评审"))
					return '<input id="summit_0_'+row.id+'" type="checkbox" onclick="javascript:_checksummit(0,'+row.id+');">';
			},styler: function(value,row,index){
				if($('#summit_0_'+row.id).is(":checked"))
					return 'background-color:#EEE8AA;color:red;';
			}
			}]],
        columns: [[
            {field: 'describe', title: '说明'},
            {field: 'receiveDate', title: '接收日期', align: 'right', sortable: true},
            {field: 'createBy', title: '创建人'},
            {field: 'enabled', title: '有效性'}]],
        onLoadSuccess: function (data) {
            $('#tabgrid_0').datagrid('clearSelections');
        }
    });
}

function load_tab_grid_1(hid) {
	$('input[id^="summit_1_"]').prop("checked",false);
	var xrole = guest ? "mgr" : role;
	$.ajax({
        url: url_get_spec_grid + "?prjid=" + prjid + "&" + personPar + "&prjPhaseID=" + prjPhaseID + "&role=" + xrole,
        contentType: 'application/x-www-form-urlencoded',
        dataType: 'json',
        type: 'GET',
        success: function (data) {
			var dgdata = [];
			for(var i=0;i<data.length;i++){
				if(!hid || hid.length==0 || $.inArray(""+data[i].hid, hid)>-1){
					dgdata[dgdata.length] = data[i];
					if(role == "mgr" || data[i].personId==personID || (role == "spec" && isSpec(data[i].speciality))){
						dgdata[dgdata.length-1]["editable"] = true;
					}else dgdata[dgdata.length-1]["editable"] = false;
				}
			}
			$('#tabgrid_1').datagrid({
				data: dgdata,
				method: 'get',
				idField: 'id',
				rownumbers: true, //pagination: true, pageList: [5, 10, 15, 20], pageSize: 5,
				autoRowHeight: false, fit: true, striped:true,
				sortName: 'receiveDate',
				order: 'desc',
				multiSort: true,
				remoteSort: true,
				checkbox: true,
				/*checkOnSelect :false,
				 selectOnCheck :false,*/
				frozenColumns: [[
					{field: 'ck', checkbox: true},
					{
						field: 'name', title: '名称', formatter: function (value, row, index) {
						return row.name + ' <a id ="href_' + tabindex + '_' + row.id + '" style="color:blue" target="_blank" href="' + url_oa_view + "&modeId=" + oaurl[tabindex].modeId + "&formId=-"
							+ oaurl[tabindex].formId + "&billid=" + row.id + "&customid=" + oaurl[tabindex].customid + '">查看</a>';
					}, styler: function(value,row,index){
						if(!row.editable) return 'background-color:lightgray;color:white';
					}
					},
					{field: 'psyj', title: '评审结论'},
					{field: 'id', title: '评审状态',align:'center', formatter: function (value, row, index) {
						return row.approveStauts+' <a style="color:grey" href="javascript:_oarec(1,'+row.id+');">记录</a>';
					},styler: function(value,row,index){
						if($('#summit_1_'+row.id).is(":checked"))
							return 'background-color:#EEE8AA;color:red;';
					}
					},
					{field: 'approveStauts', title: '加入评审队列',align:'center', formatter: function (value, row, index) {
						if(row.editable && (value=="待评审" || value=="再评审"))
							return '<input id="summit_1_'+row.id+'" type="checkbox" onclick="javascript:_checksummit(1,'+row.id+');">';
					},styler: function(value,row,index){
						if($('#summit_1_'+row.id).is(":checked"))
							return 'background-color:#EEE8AA;color:red;';
					}
					}
					]],
				columns: [[
					{field: 'describe', title: '说明'},
					{field: 'speciality', title: '专业'},
					{field: 'receiveDate', title: '接收日期', align: 'right', sortable: true},
					{field: 'createBy', title: '创建人'},
					{field: 'required', title: '是否必选'},
					{field: 'enabled', title: '有效性'}]]
			}).datagrid('clearSelections');
		}
	});
}
function _checksummit(tabindex, rowid){//评审勾选
	var summitcheck = $('#summit_'+tabindex+'_'+rowid).is(":checked");
	var line = $('#tabgrid_'+tabindex).datagrid('getRowIndex', rowid);
	$('#tabgrid_'+tabindex).datagrid('refreshRow', line);
	if(summitcheck) $('#summit_'+tabindex+'_'+rowid).prop("checked",true);
}
function _oarec(tab_index, dgid){//评审OA流程列表
	$.ajax({
		url: "http://"+window.location.host+"/api/dsin/getApproveRequest?tabIndex="+tab_index+"&id="+dgid,
		type: "GET",
		success: function (data) {
			$('#oarecdg').datagrid({
				data: data,
				rownumbers: true,
				border: false,singleSelect:false,
				method: 'get',
				remoteSort:false,
				fitColumns:true,
				fit: true, striped:true,
				idField: 'requestId',
				columns:[[
					{ field: 'status', title: '当前状态',formatter: function(value,row,index){
						return value +" <a href='javascript:_linkOA("+row.requestId+");'>查看</a>";
					} },
					{ field: 'requestName', title: '流程标题', width: 240},
					{ field: 'createBy', title: '创建人', width: 40 },
					{ field: 'createDate', title: '创建日期', width: 80,sortable:true},
					{ field: 'currentPerson', title: '当前操作者', width: 45 }]]
			}).datagrid('clearSelections').datagrid('sort', {sortName: 'createDate',sortOrder: 'desc'});;
			var dgrows = $('#tabgrid_'+tab_index).datagrid('getRows');
			var dgindex = $('#tabgrid_'+tab_index).datagrid('getRowIndex', dgid);
			$('#oarecdiv').panel('setTitle',"评审记录："+(dgindex+1)+" "+dgrows[dgindex].name).window('open');
		}
	});
}
function _linkOA(requestId){//跳转OA表单
	window.open(url_oa_server+"/workflow/request/ViewRequest.jsp?requestid="+requestId);
}
function _download1(){//下载通信专业文件
	var wurl = window.location.href;
	var cut = wurl.lastIndexOf("/");
	window.open("http://oa.nerin.com/weaver/weaver.file.FileDownload?fileid=221440&download=1&requestid=0", "_blank");
	//window.open(wurl.substr(0,cut)+encodeURI("/通信专业各阶段设计输入文件中的设计规范20150420.doc"), "_blank");
}
function load_tab_tree_1(hid) {
	var xrole = guest ? "mgr" : role;
	$.ajax({
		url: url_get_spec_tree + "?prjid=" + prjid + "&" + personPar + "&prjPhaseID=" + prjPhaseID + "&role=" + xrole,
		type: "GET",
		success: function (data) {
			var redata = JSON.parse(data);
			if(!redata[0].children || redata[0].children.length==0 || (redata[0].children.length==1 && redata[0].children[0].id == "null"))
				return;
			textArr = [];
			_getObjId(redata, textArr);
			$('#checkbox').empty();
			if(textArr.length == 0){
				$('#spelayout').layout('collapse','west');
			}else{
				$('#spelayout').layout('expand','west');
				for(var i=0;i<textArr.length;i++){
					$('#checkbox').append("<input id='"+i+"_node' type='checkbox'>"+textArr[i]+"<br>");
					$('#'+i+'_node').prop("checked",false).change(_switchNode);
				}
			}
			for(var i=0;i<redata[0].children.length;i++){
				if(redata[0].children[i].text == "通信"){
					redata[0].children[i].children[redata[0].children[i].children.length] = {
						"id":"download1",
						"text":"<a href='javascript:_download1();'>通信专业各阶段设计输入文件中的设计规范20150420.doc</a>",
						"iconCls":"icon-input_cont",
						"color":"#000000"
					};
				}
			}
			$('#tabtree_1').tree({
				data: redata,
				method: 'get',
				checkbox: true,
				formatter: function (node) {
					txt = node.text;
					if(node.color != "#000000") txt = "<span style='color:" + node.color + "'>必选</span>_"+txt;
					//if(node.children) txt += " - " + node.children.length;
					return txt;
				},
				onCheck:function(node){
					var checkednodes = $('#tabtree_1').tree('getChecked');
					for(var i=0;i<checkednodes.length;i++){
						$(checkednodes[i].target).find("span.tree-title").css({"background-color":"#0e2d5f","color":"#fff"});
					}
					var uncheckednodes = $('#tabtree_1').tree('getChecked', 'unchecked');
					for(var i=0;i<uncheckednodes.length;i++){
						$(uncheckednodes[i].target).find("span.tree-title").removeAttr("style");
					}
					var incheckednodes = $('#tabtree_1').tree('getChecked', 'indeterminate');
					for(var i=0;i<incheckednodes.length;i++){
						$(incheckednodes[i].target).find("span.tree-title").removeAttr("style");
					}
				},
				onLoadSuccess:function(node, data){//遍历节点勾选与展开
					if(hid){//刷新后原先勾选的节点保留勾选
						var nodeArr = $('#tabtree_1').tree('getChildren', data[0].target);
						for(var i=0;i<nodeArr.length;i++){
							if($.inArray(nodeArr[i].id,hid)>-1){
								var tar = $('#tabtree_1').tree('find',nodeArr[i].id).target;
								$('#tabtree_1').tree('check',tar);
								$('#tabtree_1').tree('expandTo',tar);
							}
						}
					}
				}
/* 				filter:function(q,node){
					var pnode = $('#tabtree_1').tree('getParent',node.target);
					return node.text!="建筑设计院民用类";
				} */
			});
			//$('#tabtree_1').tree('doFilter','');
			_switchNode();
		}
	});
}
function _switchNode(){
    var i,j,k;
    var spanArr, liArr;
    var hideLiNode, siblings, isAllHide;
	for(i=0;i<textArr.length;i++){
		if($.inArray(textArr[i],strArr)>-1){//文件显示
			spanArr = $('ul#tabtree_1').find('span.tree-title:contains("'+textArr[i]+'-")');
		}else{
			liArr = $('ul#tabtree_1').find('span.tree-title:contains('+textArr[i]+')');
			spanArr = [];
			for(j=0;j<liArr.length;j++){
				if(liArr[j].innerHTML==textArr[i]) spanArr[spanArr.length] = liArr[j];
			}
		}
		for(j=0;j<spanArr.length;j++){
            hideLiNode = $(spanArr[j]).parent().parent();
			if($('#'+i+'_node').is(':checked')){
                hideLiNode.show();
                hideLiNode.parent().parent().show();
			} else {
                hideLiNode.hide();
                siblings = (hideLiNode.parent())[0].childNodes;
                isAllHide = true;
                for(k=0; k<siblings.length; k++){
                    if($(siblings[k]).css("display")!="none"){
                        isAllHide = false;
                        break;
                    }
                }
                if(isAllHide){
                    hideLiNode.parent().parent().hide();
                }
            }
		}
	}
}
function _getObjId(Obj, result) {//递归获取树形数据前缀数组
    for (var i = 0; i < Obj.length; i++) {
		var strcut = Obj[i].text.indexOf("-");
		if(strcut>0){
			var str = Obj[i].text.substr(0,strcut);
			if($.inArray(str,result)==-1 && $.inArray(str,strArr)>-1) result[result.length] = str;
		}
		if($.inArray(Obj[i].text,result)==-1 && $.inArray(Obj[i].text,titleArr)>-1) result[result.length] = Obj[i].text;
        if (Obj[i].children) {
            _getObjId(Obj[i].children, result);
        }
    }
}
function load_tab_tree_2(data, hid) {
	var newData = [];
	for(var i=0;i<data.length;i++){
		newData[i] = {};
		for(var key in data[i]){
			if(key!="children" && data[i][key]){
				newData[i][key] = data[i][key];
			}else if(key =="children"){
				newData[i]["children"] = [];
				for(var j=0;j<data[i].children.length;j++){
					newData[i]["children"][j] = {};
					for(var jkey in data[i].children[j]){
						if(jkey!="children" && data[i].children[j][jkey]){
							newData[i]["children"][j][jkey] = data[i].children[j][jkey];
						}else if(jkey =="children"){
							newData[i]["children"][j]["children"] = [];
							for(var k=0;k<data[i].children[j].children.length;k++){//排除重复、更正图标
								var unique = true;
								if(k<data[i].children[j].children.length-1)
									for(var m=k+1;m<data[i].children[j].children.length;m++)
										if(data[i].children[j].children[k].text == data[i].children[j].children[m].text){
											unique = false;
											break;
										}
								if(unique){
									newData[i]["children"][j]["children"][newData[i]["children"][j]["children"].length] = {};
									for(var kkey in data[i].children[j].children[k]){
										if(kkey!="iconCls" && data[i].children[j].children[k][kkey]){
											newData[i]["children"][j]["children"][newData[i]["children"][j]["children"].length-1][kkey] = data[i].children[j].children[k][kkey];
										}else if(kkey =="iconCls") newData[i]["children"][j]["children"][newData[i]["children"][j]["children"].length-1][kkey] = "icon-input_equp";
									}
								}
							}
						}
					}
				}
			}
		}
	}
	$('#tabtree_2').tree({
        data: newData,
        checkbox: true,
		formatter:function(node){
			var seq = node.text.indexOf("--");
			return seq>-1?node.text.substr(0,seq):node.text;
		},
		onCheck:function(node){
			var checkednodes = $('#tabtree_2').tree('getChecked');
			for(var i=0;i<checkednodes.length;i++){
				$(checkednodes[i].target).find("span.tree-title").css({"background-color":"#0e2d5f","color":"#fff"});
			}
			var uncheckednodes = $('#tabtree_2').tree('getChecked', 'unchecked');
			for(var i=0;i<uncheckednodes.length;i++){
				$(uncheckednodes[i].target).find("span.tree-title").removeAttr("style");
			}
			var incheckednodes = $('#tabtree_2').tree('getChecked', 'indeterminate');
			for(var i=0;i<incheckednodes.length;i++){
				$(incheckednodes[i].target).find("span.tree-title").removeAttr("style");
			}
		},
		onLoadSuccess:function(node, tdata){//遍历节点勾选与展开
			if(hid){
				var nodeArr = $('#tabtree_2').tree('getChildren', tdata[0].target);
				for(var i=0;i<nodeArr.length;i++){
					if(nodeArr[i].iconCls == "icon-input_equp"){
						var pnode = $('#tabtree_2').tree('getParent', nodeArr[i].target);
						var prof = pnode.text.split("--");
						equipinfo =  prof[0]+ " " + nodeArr[i].text;
						if($.inArray(equipinfo,hid)>-1){
							var tar = $('#tabtree_2').tree('find',nodeArr[i].id).target;
							$('#tabtree_2').tree('check',tar);
							$('#tabtree_2').tree('expandTo',tar);
						}
					}
				}
			}
		}
    });
}
function load_tab_grid_2(data, hid) {
	var dgdata = [];
	for(var i=0;i<data.length;i++){
		var equipinfo = data[i].speciality+" "+data[i].item+" "+data[i].equipName;
		if(!hid || hid.length==0 || $.inArray(equipinfo, hid)>-1){
			dgdata[dgdata.length] = data[i];
			if(role == "mgr" || data[i].personId==personID || (role == "spec" && isSpec(data[i].speciality))){
				dgdata[dgdata.length-1]["editable"] = true;
			}else dgdata[dgdata.length-1]["editable"] = false;
		}
	}
	$('input[id^="summit_2_"]').prop("checked",false);
    $('#tabgrid_2').datagrid({
        data: dgdata,
        idField: 'id',
        rownumbers: true, //pagination: true, pageList: [5, 10, 15, 20], pageSize: 5,
        checkbox: true, striped:true,
        autoRowHeight: false, fit: true,
        sortName: 'receiveDate',
        order: 'desc',
        multiSort: true,
        remoteSort: true,
        /*checkOnSelect :false,
         selectOnCheck :false,*/
        frozenColumns: [[
			{field: 'ck', checkbox: true},
            {
                field: 'name', title: '名称', formatter: function (value, row, index) {
                return row.name + ' <a id ="href_' + tabindex + '_' + row.id + '" style="color:blue" target="_blank" href="' + url_oa_view + "&modeId=" + oaurl[tabindex].modeId + "&formId=-"
                    + oaurl[tabindex].formId + "&billid=" + row.id + "&customid=" + oaurl[tabindex].customid + '">查看</a>';
            }, styler: function(value,row,index){
				if(!row.editable) return 'background-color:lightgray;color:white';
			}
            },
            {field: 'psyj', title: '评审结论'},
			{field: 'id', title: '评审状态',align:'center', formatter: function (value, row, index) {
				return row.approveStauts+' <a style="color:grey" href="javascript:_oarec(2,'+row.id+');">记录</a>';
			},styler: function(value,row,index){
				if($('#summit_2_'+row.id).is(":checked"))
					return 'background-color:#EEE8AA;color:red;';
			}
			},
			{field: 'approveStauts', title: '加入评审队列',align:'center', formatter: function (value, row, index) {
				if(row.editable && (value=="待评审" || value=="再评审"))
					return '<input id="summit_2_'+row.id+'" type="checkbox" onclick="javascript:_checksummit(2,'+row.id+');">';
			},styler: function(value,row,index){
				if($('#summit_2_'+row.id).is(":checked"))
					return 'background-color:#EEE8AA;color:red;';
			}
			}]],
		columns: [[
            {field: 'describe', title: '说明'},
            {field: 'speciality', title: '专业'},
            {field: 'item', title: '设备位号'},
            {field: 'equipName', title: '设备名称'},
            {field: 'orderNO', title: '订货单号'},
            {field: 'orderName', title: '订货单名称'},
            {field: 'requiredDate', title: '资料要求日期', align: 'right', sortable: true},
            {field: 'receiveDate', title: '接收日期', align: 'right', sortable: true},
            {field: 'createBy', title: '创建人'},
            {field: 'enabled', title: '有效性'}]]
    }).datagrid('clearSelections');
}
function load_tab_2(hid) {
	var xrole = guest ? "mgr" : role;
    $.getJSON(url_get_equi_data + "?prjid=" + prjid + "&" + personPar + "&prjPhaseID=" + prjPhaseID + "&role=" + xrole
        , function (data) {
            if(hid){
				load_tab_tree_2(data.tree,hid);
				load_tab_grid_2(data.grid,hid);
			}else{
				load_tab_tree_2(data.tree);
				load_tab_grid_2(data.grid);
			}
        })

}
function load_tab_grid_3() {
    $('#tabgrid_3').datagrid({
        url: url_get_temp_grid + "?prjid=" + prjid,
        method: 'get',
        idField: 'id',
        rownumbers: true,
        //singleSelect: true,
        //pagination: true, pageList: [5, 10, 15, 20], pageSize: 5,
        height: 162, fit: true, striped:true,
        sortName: 'receiveDate',
        order: 'desc',
        multiSort: true,
        remoteSort: true,
        checkbox: true,
        columns: [[{field: 'ck', checkbox: true},
            {
                field: 'name', title: '名称', formatter: function (value, row, index) {
                return row.name + ' <a id ="href_' + tabindex + '_' + row.id + '" style="color:blue" target="_blank" href="' + url_oa_view + "&modeId=" + oaurl[tabindex].modeId + "&formId=-"
                    + oaurl[tabindex].formId + "&billid=" + row.id + "&customid=" + oaurl[tabindex].customid + '">查看</a>';
            }
            },
            {field: 'describe', title: '说明'},
            {field: 'receiveDate', title: '接收日期', align: 'right', sortable: true},
            {field: 'createBy', title: '创建人'},
            {field: 'stauts', title: '状态'},
            {field: 'contact', title: '联系人'},
            {field: 'planBackDate', title: '拟归还日期', align: 'right', sortable: true},
            {field: 'backDate', title: '归还日期', align: 'right', sortable: true}
        ]]
    }).datagrid('clearSelections');
}
function _prof_1(){
	if($('#tabtree_1').find("li").length==0) return;
	$('#tabtree_1').tree('collapseAll');
	var troot = $('#tabtree_1').tree('getRoots');
	$('#tabtree_1').tree('expand',troot[0].target);
}
function _cont_1(){
	if($('#tabtree_1').find("li").length==0) return;
	$('#tabtree_1').tree('collapseAll');
	var troot = $('#tabtree_1').tree('getRoots');
	$('#tabtree_1').tree('expand',troot[0].target);
	for(var i=0;i<troot[0].children.length;i++){
		$('#tabtree_1').tree('expand',troot[0].children[i].target);
	}
}
function _docu_1(){
	if($('#tabtree_1').find("li").length==0) return;
	$('#tabtree_1').tree('expandAll');
}
function _prof_2(){
	if($('#tabtree_2').find("li").length==0) return;
	$('#tabtree_2').tree('collapseAll');
	var troot = $('#tabtree_2').tree('getRoots');
	$('#tabtree_2').tree('expand',troot[0].target);
}
function _equp_2(){
	if($('#tabtree_2').find("li").length==0) return;
	$('#tabtree_2').tree('expandAll');
}
function relaod_tabs(tabindex) {
    switch (tabindex) {
        case 0:
            load_tab_grid_0();
            break;
        case 1:
            load_tab_tree_1();
            load_tab_grid_1();
            break;
        case 2:
            load_tab_2();
            break;
        case 3:
			load_tab_grid_3();
            break;
    }
}

function relaod_toolbar(tabindex) {
	$('#subitem_toolbar_search').show();
	$('#subitem_toolbar_add,#subitem_toolbar_del,#subitem_toolbar_edit,#subitem_toolbar_summit').hide();
	if(!guest){
		switch (tabindex) {
			case 0:
				if(role == "mgr")
					$('#subitem_toolbar_add,#subitem_toolbar_del,#subitem_toolbar_edit,#subitem_toolbar_summit').show();
				break;
			case 1:
			case 2:
				$('#subitem_toolbar_add,#subitem_toolbar_del,#subitem_toolbar_edit,#subitem_toolbar_summit').show();
				break;
			case 3:
				if(role == "mgr")
					$('#subitem_toolbar_add,#subitem_toolbar_edit').show();
				break;
		}
	}
}

//当前tab的序号
function get_tab_index() {
    var tab = $('#tabs').tabs('getSelected');
    tabindex = $("#tabs").tabs("getTabIndex", tab);
    return tabindex;
}

//新增操作选定tabgrid文件类别的序号
function get_tab_grid_checked_node(tabindex) {
    var nodes = $('#tabgrid_' + tabindex).datagrid('getChecked');
    return nodes;
}

//加载按钮
$('#subitem_toolbar_search').linkbutton({
	//plain:true,
	iconCls: 'icon-reload',
	onClick: function () {
		_searchrefresh(tabindex);
	}
});
function _searchrefresh(tab_index){
	var xrole = guest ? "mgr" : role;
	switch (tab_index) {
		case  0:
		case  3:
			relaod_tabs(tab_index);
			break;
		case  1:
			var hidArr = [];
			var nodes = $('#tabtree_' + tab_index).tree('getChecked');
			for (var i = 0; i < nodes.length; i++)
				if($('#tabtree_' + tab_index).tree('isLeaf',nodes[i].target))
					hidArr[hidArr.length] = nodes[i].id;
			load_tab_tree_1(hidArr);
			load_tab_grid_1(hidArr);
			break;
		case  2:
			var equipArr = [];
			var nodes = $('#tabtree_' + tab_index).tree('getChecked');
			for (var i = 0; i < nodes.length; i++)
				if($('#tabtree_' + tab_index).tree('isLeaf',nodes[i].target) && nodes[i].iconCls=="icon-input_equp"){
					var pnode = $('#tabtree_' + tab_index).tree('getParent',nodes[i].target);
					var prof = pnode.text.split("--");
					equipArr[equipArr.length] =  prof[0]+ " " + nodes[i].text;
				}
			load_tab_2(equipArr);
			break;
	}
}

//新增按钮
$('#subitem_toolbar_add').linkbutton({
	//plain:true,
	iconCls: 'icon-add',
	onClick: function () {
		switch (tabindex) {
			case  0:
				$.getJSON(url_get_erpid + "?prjid=" + prjid + "&" + personPar + "&phaseName=" + encodeURI(prjPhase)
					, function (data) {
						erpId = data;
						window.open(url_oa + "?flowCode=MD02&erpid=" + erpId + "&workcode=" + userNum);
					})
				break;
			case  1:
				var node = $('#tabtree_1').tree('getSelected');
				if (node && node.iconCls == "icon-input_docu") {
					var cont = $('#tabtree_1').tree('getParent', node.target);
					var prof = $('#tabtree_1').tree('getParent', cont.target);
					$.messager.confirm('操作提示', '将新增一条' + prof.text + '专业的设计输入，<br>种类：'+cont.text+'，名称：'+node.text+'，是否继续？', function (r) {
						if (r) {
							docId = node.id;
							$.getJSON(url_get_erpid + "?prjid=" + prjid + "&" + personPar + "&docID=" + docId + "&phaseName=" + encodeURI(prjPhase)
								, function (data) {
									erpId = data;
									window.open(url_oa + "?flowCode=MD01&erpid=" + erpId + "&workcode=" + userNum);
								});
						}
					});
				}else $.messager.alert('操作提示', '请先在左侧树形选择文件类别。<br>展开专业>种类>文件后单击节点文字以选中。', 'info');
				break
			case  2:
				var xrole = role=="other" ? "spec" : role;
				$.getJSON(url_get_spec + "?prjid=" + prjid +"&" + personPar + "&prjPhaseID=" + prjPhaseID + "&role=" + xrole
					, function (data) {
						specialty = data;
						var specName;
						$("#majorsdiv_w").html("");
						for (var i = 0; i < specialty.length; i++) {
							var match = 0;
							for(var j=i;j<specialty.length; j++)
								if(specialty[j].spec==specialty[i].spec) match++;
							if(match==1){//去除重复专业
								specName = specialty[i].specName;
								$("#majorsdiv_w").append("<a id=major" + specialty[i].spec + ">" + specialty[i].specName + "</a>&nbsp;");
								$("#major" + specialty[i].spec).linkbutton({plain: true});
								$("#major" + specialty[i].spec).click(function () {
									specName = $(this).text()
									$.messager.confirm('操作提示', '将新增一条' + specName + '专业的设备反馈资料，是否继续？', function (r) {
										if (r) {
											$.getJSON(url_get_erpid + "?prjid=" + prjid + "&" + personPar + "&specName=" + encodeURI(specName) + "&phaseName=" + encodeURI(prjPhase)
												, function (data) {
													$('#speformdiv').window("close");
													erpId = data;
													window.open(url_oa + "?flowCode=MD03&erpid=" + erpId + "&workcode=" + userNum);
												})
										}
									});
								})
							}
						}
						$('#speformdiv').window("open");
					})
				break
			case  3:
				$.getJSON(url_get_erpid + "?prjid=" + prjid + "&" + personPar + "&phaseName=" + encodeURI(prjPhase)
					, function (data) {
						erpId = data;
						window.open(url_oa + "?flowCode=MD04&erpid=" + erpId + "&workcode=" + userNum);
					})
				break;
		}
	}
});

//删除按钮
$('#subitem_toolbar_del').linkbutton({
	//plain:true,
	iconCls: 'icon-remove',
	onClick: function () {
		var Rows = $('#tabgrid_' + tabindex).datagrid('getSelections');
		var delrows = [];
		var remrows = [];
		for(var i=0;i<Rows.length;i++){
			if(Rows[i].approveStauts == "待评审" && (Rows[i].editable == undefined || Rows[i].editable)){
				delrows[delrows.length] = {"tabId": tabindex, "id": Rows[i].id};
			}else remrows[remrows.length] = Rows[i].id;
		}
		if(delrows.length == 0){
			$.messager.alert('操作提示', '未选中可删除行，<br>只有“待评审”状态且非灰色的文件可被删除。', 'error');
		}else
			$.messager.confirm('操作提示', '共选中'+Rows.length+'行，其中'+delrows.length+'行可被删除，<br>只有“待评审”状态且非灰色的文件可被删除，是否确定？', function (r) {
				if(r){
					result = {"projectId": prjid, 'rows': delrows};
					$('#loaddiv').window('open');
					$.ajax({
						type: 'POST',
						url: url_delete,
						contentType: "application/json",
						data: JSON.stringify(result),
						success: function (data){
							$('#loaddiv').window('close');
							if(data.status == "S"){
								$('#tabgrid_' + tabindex).datagrid('clearSelections');
								relaod_tabs(tabindex);
								for(var i=0;i<remrows.length;i++)
									$('#tabgrid_' + tabindex).datagrid('selectRecord',remrows[i]);
							}else
								$.messager.alert("操作失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
						},
						error: function(data){
							$('#loaddiv').window('close');
							$.messager.alert("操作失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
						}
					});
				}
			});
	}
});

//编辑按钮
$('#subitem_toolbar_edit').linkbutton({
	//plain:true,
	iconCls: 'icon-edit',
	onClick: function () {
		var nodes = get_tab_grid_checked_node(tabindex);
		if (nodes.length == 1) {
			docId = nodes[0].id;
			if(nodes[0].approveStauts == "评审中"){
				$.messager.alert('操作提示', '文件当前正在评审中，不可编辑。', 'info');
			}else if(nodes[0].editable==undefined || nodes[0].editable){
				if(nodes[0].approveStauts == "已评审"){
					$.messager.confirm('操作提示', '已评审的文件编辑后需要进行再评审，请确认是否继续？', function (r) {
						if (r) window.open(url_oa_edit + "?isfromTab=0&modeId=" + oaurl[tabindex].modeId + "&formId=-" + oaurl[tabindex].formId + "&type=2&billid=" + docId +
							"&viewfrom=fromsearchlist&opentype=0&customid=" + oaurl[tabindex].customid);
					});
				}else window.open(url_oa_edit + "?isfromTab=0&modeId=" + oaurl[tabindex].modeId + "&formId=-" + oaurl[tabindex].formId + "&type=2&billid=" + docId +
					"&viewfrom=fromsearchlist&opentype=0&customid=" + oaurl[tabindex].customid);
			}else $.messager.alert('操作提示', '灰色文件无权限编辑。', 'info');
		}else $.messager.alert('操作提示', '请选择一个文件进行编辑，不可不选或多选。', 'info');
	}
});

//评审按钮
$('#subitem_toolbar_summit').linkbutton({
	//plain:true,
	iconCls: 'icon-ok',
	onClick: function () {
		var ids = [];
		var countArr = [0, 0, 0];
		var submitSpe = [];
		for(var i=0;i<3;i++){
			var summitcks = $(':checked[id^="summit_'+i+'_"]');
			for(var j=0;j<summitcks.length;j++){
				var dgid = summitcks[j].id.replace("summit_"+i+"_","");
				var url = $('#href_' + i + '_' + dgid).attr("href");
				ids[ids.length] = {"tabId": i, "id": dgid, "url": url};
				countArr[i] ++;
				if(i>0){
					var spei = $('#tabgrid_' + i).datagrid('getRowIndex',dgid);
					for(var key in majorData){
						if(majorData[key].full==$('#tabgrid_' + i).datagrid('getRows')[spei].speciality){
							submitSpe[submitSpe.length] = key;
							break;
						}
					}
				}
			}
		}
		if(ids.length ==0){
			$.messager.alert('操作提示', '请在评审状态列勾选提交评审的设计输入文件，支持跨类别和多选。', 'info');
		}else{
			$.ajax({
				url: "http://" + window.location.host +"/api/lev3/getProfessionListAll?projID="+prjid+"&specialty=ALL",
				type: "GET",
				success: function (chgrlist) {
					var resultinfo = "将提交";
					if(countArr[0]>0) resultinfo += countArr[0]+"条项目输入、";
					if(countArr[1]>0) resultinfo += countArr[1]+"条专业输入、";
					if(countArr[2]>0) resultinfo += countArr[2]+"条设备反馈资料、";
					resultinfo = resultinfo.substr(0,resultinfo.length-1) + "（共计" + ids.length + "条设计输入）进行评审，请确认是否继续？<br>建议以下专业负责人作为评审人员（可在发起流程后在OA表单中调整）：<br>";
					var chgrArr = [];
					if(chgrlist.length==0){
						resultinfo += "未查找到专业负责人，建议向项目管理部确认策划信息再发起评审。";
					}else{
						var chgrStr = "";
						for(var i=0;i<chgrlist.length;i++){
							if(countArr[0]>0 || $.inArray(chgrlist[i].paMemberSpecDTO.specialty,submitSpe)>-1){
								for(var j=0;j<chgrlist[i].member.length;j++){
									var chgrMatch = false;
									for(var k=0;k<chgrArr.length;k++){
										if(chgrArr[k]==chgrlist[i].member[j].perNum){
											chgrMatch = true;
											break;
										}
									}
									if(!chgrMatch){
										chgrArr[chgrArr.length] = chgrlist[i].member[j].perNum;
										chgrStr += ","+chgrlist[i].member[j].perName;
									}
								}
							}
						}
						resultinfo += chgrStr.substr(1);
					}
					$.messager.confirm('操作提示', resultinfo, function (r) {
						if (r){
							result = {"projectId": prjid, "phaseName": prjPhase, "rows": ids,"psry":chgrArr};
							$('#loaddiv').window('open');
							$.ajax({
								type: 'POST',
								url: url_get_approve_erpid,
								contentType: "application/json",
								data: JSON.stringify(result),
								success: function (data) {
									if (data.status) {
										window.open(url_oa + "?flowCode=MD05&erpid=" + data.erpId + "&workcode=" + userNum);
										//调接口改状态，返回成功后更新本地数据，关闭等待窗
										setTimeout(_refreshalltab, 10000);
									} else {
										$('#loaddiv').window('close');
										$.messager.alert('操作失败', '错误：' + data.errInfo + "ID:" + data.erpId, 'error');
									}
									function _refreshalltab(){
										for (var i = 0; i < tabLoadedArr.length; i++)
											_searchrefresh(tabLoadedArr[i]);
										$('#loaddiv').window('close');
									}
								}
							});
						}
					});
				},
				error: function(data){
					$('#loaddiv').window('close');
					$.messager.alert("操作提示","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
				}
			});
		}
	}
});

//取角色
get_role();

$("#tabs").tabs({
    onSelect: function (title, index) {
		if($.inArray(index, tabLoadedArr) == -1){
			tabLoadedArr[tabLoadedArr.length] = index;
			relaod_tabs(index);
		}
        relaod_toolbar(index);
		tabindex = index;
    }
});
relaod_tabs(0);
relaod_toolbar(0);