var westexpanded = false;
var prjhisdataurl = "http://"+window.location.host+"/api/lev3/queryHistoryProjectList";//"datagrid_prjhis.json";//历史项目数据源 oracle包-模糊查找我参与过的历史项目列表
var industrydataurl = "http://"+window.location.host+"/api/lev2/queryIndustryList";//"data_industry.json"; // oracle包-我参与过的历史项目所属行业列表
var sysmajordataurl = "http://"+window.location.host+"/api/lev3/getDlvrClass";//"data_majors.json"; // oracle包-工作包类型列表
var prj_sys_dataurl = "http://"+window.location.host+"/api/lev3/getDeliverableTypeList";//"datagrid_PrjSys.json";//引用系统数据源 oracle包-模糊查找工作包列表
var prjhistreemaxid = 0;
LoadPrjIndustrysys();
$(function () {
    //第一次展开时的异步加载
    $(document.body).layout('panel', 'west').panel({
        onExpand: function () {
			//if($('#wbs_layout_west').is(":visible")) $('#wbs_layout_center').layout('collapse','west');
            if (!westexpanded) {
                westexpanded = true;
                //第一次展开,加载非条件工作包
                LoadSys();
                //LoadMajor();
            }
        },
		onCollapse: function(){
			//if(!$('#wbs_layout_west').is(":visible")) $('#wbs_layout_center').layout('expand','west');
		}
    });

    $('#reftabs').tabs({
        onSelect: function (title, index) {
            switch (title) {
                case "历史项目":
                    LoadPrjHisTree();
                    break;
                case "条件工作包":
                    LoadMajor();
                    break;
            }
        }
    });
});



/*****************历史项目tab相关******************/
var hisprjloaded = false;
function LoadPrjHisTree() {
    if (!hisprjloaded) {
        hisprjloaded = true;
        $('#prjhisgrid').treegrid({
            url: '',
			method: 'get',
            idField: 'id',
            treeField: 'name',
			fitColumns:true,
            pagination: true, pageList: [5, 10, 15, 20], pageSize: 5,
            columns: [[
                { field: 'name', title: '名称', width: 400 },
                { field: 'time', title: '发布/保存日期', width: 126 ,fixed:true},
                { field: 'Operator', title: '操作人', width: 50 , fixed:true}]],
            onDblClickRow: function (row) {
                if (!(row.structureId && row.structureId != 0 && row.operator.substr(0,1)=="2")){
					$("#prjhisgrid").treegrid('toggle', row.id);
				}else{
					var wbsID=row.structureId;
					var type = row.strClass;
					if(wbsID!==null&&wbsID!=0){
						LoadWbsHisTree(wbsID,type,row.operator);
						var hisTitle = $("#prjhisgrid").treegrid('find', row.parentId).name+" - "+row.name;
						$('#pub_layout1').layout('panel', 'north').panel("setTitle", hisTitle);
						$('#pub_layout1 div.panel-title').text(hisTitle);
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
					pClass: $("#btnprjindustry").text(),
					keywords: value
				});
			}
		});
		$('#histree').tree({//历史项目树
			formatter:function(node){
				var txt = node.dlvrName;
				if(node.level!="root") txt = node.code+" "+txt;
				if(dual) txt = txt+" | "+node.dualName;
				var childrenNum = 0;
				if(node.speChildren) childrenNum = node.speChildren.length;
				if(node.status == "失效"){
					var style = "background-color:lightgray;color:#fff";
				}else if(node.level=="spe" && childrenNum == 0){
					var style = "color:red";
				}else var style = "";
				if(node.level=="spe") txt += " - "+childrenNum;
				txt = "<span style='"+style+"'>"+txt+"</span>";
				if(node.level=="spe") txt += "&nbsp;<a id='gohis"+node.id+"'></a>";
				return txt;
			},
			onSelect:function(node){
				//_uncheckGrid('#hisgrid');
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
				if(node.id!=hisId && node.level=="spe"){
					_enterNode(node.id,'#histree','#hisgrid');
					$('#histree').tree('select', node.target);
				}
			},
			onLoadSuccess:function(node,data){
				$('#pub_layout1').layout('collapse', 'north');
				$('#his_layout_center').layout('expand', 'west');
				/* if (data[0].pbsId){
					$('#hisgrid').datagrid('hideColumn','startDate');
					$('#hisgrid').datagrid('hideColumn','endDate');
					$('#hisgrid').datagrid('hideColumn','workCoef');
					$('#hisgrid').datagrid('hideColumn','status');
				}else if(data[0].wbsId){
					$('#hisgrid').datagrid('showColumn','status');
					$('#hisgrid').datagrid('showColumn','startDate');
					$('#hisgrid').datagrid('showColumn','endDate');
				} */
			}
		});
		$('#hisgrid').datagrid({//历史项目表格
			rownumbers: true,
			border: false,
			singleSelect: false,
			fit: true,
			idField: 'id',
			//treeField: 'code',
			frozenColumns: [[
					{ field: 'code', title: '节点代号', align: 'left', halign: 'center', formatter:function(value,row,index){
						return '<img src="../../themes/icons/tree_file.png" /> '+value;
						},width:80 },
					{ field: 'dlvrName', title: '节点名称', align: 'left', halign: 'center' },
					{ field: 'description', title: '说明', align: 'left', halign: 'center' },
			]],
			columns: [[
					{ field: 'workHour', title: '计划工时',  },
					{
						field: 'status', title: '状态',
						styler: function (value, row, index) {
							if (row.status == "失效") {
								return 'background-color:lightgray;';
							}
						}
					},
					{ field: 'grandNum', title: '专业孙项号' },
					{ field: 'grandName', title: '专业孙项名称' },
					{ field: 'matCode', title: '关联物料编码' },
					{ field: 'matName', title: '关联物料名称' },
			]]
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
            if (typeof (data) == "string")//字符转对象
                data = JSON.parse(data);//eval("(" + data + ")");
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
function LoadWbsHisTree(wbsid, type, spe) {
	if(type=="WBS"){
		var url = "http://"+window.location.host+"/api/lev3/getHisWbsDlvrList?&wbsID="+wbsid+"&spec="+spe;
	}else var url = "http://"+window.location.host+"/api/lev3/getHisPbsDlvrList?wbsID="+wbsid+"&spec="+spe;
	$.ajax({
		url:url,
		type:"GET",
		success:function(data){
			$('#histree').tree('loadData',data);
			var Ids = [];
			var getSpe = false;
			_getObjId(data, Ids);
			for(var i=0;i<Ids.length;i++){
				if($('#histree').tree('find',Ids[i]).level=="spe"){
					_enterNode(Ids[i],"#histree","#hisgrid");
					$('#histree').tree('select',$('#histree').tree('find',Ids[i]).target);
					getSpe = true;
					break;
				}
			}
			if(!getSpe) $('#spepage').panel('setTitle',"无可用专业节点");
		}
	});
}

/*****************历史项目tab相关end******************/


/*****************系统工作包tab相关******************/
var sysloaded = false;
function LoadSys() {
    if (sysloaded) return false;
    else sysloaded = true;
    $('#gridPrjSys').datagrid({
        url: '',
        idField: 'dlvrID',
        rownumbers: true, singleSelect: false, pagination: true, method: 'get', pageSize: 20,
        fit: true, fitColumns:true,
        height: 162,
        columns: [[
            { field: 'dlvrCode', title: '编码', width: 80 },
            { field: 'dlvrName', title: '名称', width: 190 },
            { field: 'className', title: '类型', width: 70 }]],
        onLoadSuccess: function (data) {
            $('#gridPrjSys').datagrid("clearSelections");
            $('#gridPrjSys').datagrid("unselectAll");
        }
    });
	$('#gridPrjSys').datagrid('options').url = prj_sys_dataurl;
    $('#prjsyssearch').searchbox({
        searcher: function (value, name) {
			$('#gridPrjSys').datagrid('load', {
				phaseID: prjPhaseID,
				spec:tmpmajor.substring(5),
				"class": $("#btnprjindustrysys").attr("name"),
				keywords: value
			});
        }
    });
}
//载入类型选择
function LoadPrjIndustrysys() {
    $.ajax({
        url: sysmajordataurl,
        async: false,
        type: "GET",
        success: function (data) {
            if (typeof (data) == "string")//字符转对象
                data = JSON.parse(data);
			$('#btnprjindustrysys').menubutton({ menu: '#menuindustrysys' });
			var industryArr = [{"id":null, "type":"全部类型"}].concat(data);
			workTypeList = {};
            for (var j = 0; j < industryArr.length; j++) {
				var i = industryArr.length - j - 1;
				if(industryArr[i].id){
					workTypeList[industryArr[i].id] = industryArr[i].type;
				}else{
					workTypeList["05"] = "接口条件";
				}
				//if(industryArr[i].id != null) $('#form2div1').prepend("<input id='ckbworktype"+industryArr[i].id+"' type='checkbox'>"+industryArr[i].type+"&nbsp;&nbsp;");
                $('#menuindustrysys').menu('appendItem', {
                    text: industryArr[j].type,
                    id: industryArr[j].id,
                    onclick: function () {
                        $('#btnprjindustrysys').menubutton({ menu: '#menuindustrysys', text: this.innerText });
						$('#btnprjindustrysys').attr("name",this.id);
					}
                });
            }
        }
    });
}
/*****************系统工作包tab相关end******************/


/*****************接收条件专业tab相关******************/

var majorloaded = false;
function LoadMajor() {
    if (majorloaded) return;
    else{
		$('#gridPrjmajors').datagrid({
			idField: 'code',
			rownumbers: true, singleSelect: false, pagination: false,
			fit: true, fitColumns:true,
			columns: [[
				{ field: 'ck', checkbox: true },
				{ field: 'code', title: '接收专业代字', width: 160 },
				{ field: 'name', title: '接收专业名称', width: 180 }]]
		});
		$.ajax({
			url: prj_major_dataurl+"?projID="+prjid+"&phaseID="+prjPhaseID+"&"+userPar,
			type: "GET",
			beforeSend: function () {
				$('#gridPrjmajors').datagrid('loading');
			},
			success: function (data) {
				var infoData = [];
				for(var i=0;i<data.length;i++){
					if(data[i].name!="项目经理") infoData[infoData.length]={"code":data[i].code,"name":data[i].name};
				}
				$('#gridPrjmajors').datagrid('loadData', infoData);
				$('#prjmajorsearch').searchbox({
					searcher: function (value, name) {
						SearchPrjMajor(value);
					}
				});
				$('#gridPrjmajors').datagrid('loaded');
				majorloaded = true;
			}
		});
	}
}

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

/*****************接收条件专业tab相关end******************/