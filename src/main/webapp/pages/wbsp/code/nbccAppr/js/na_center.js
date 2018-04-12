$.getJSON("http://" + window.location.host +"/api/taskRest/getMyTaskChapterSpecialtysList?taskHeaderId="+taskHeaderId, function (spelist) {
	$.getJSON("http://" + window.location.host +"/api/taskRest/taskChaptersList?lineType=ALL&taskHeaderId="+taskHeaderId, function (chaplist) {
		
		$('#loaddiv').window('close');
	});
});



/* var listgridurl = 'headPrj.json';
var OAurl = "192.168.15.198";
$('#nophase').change(_nophase);
function _nophase(){
	if($('#nophase').is(":checked")){
		$('a[name="titlephase"]').hide();
	}else $('a[name="titlephase"]').show();
	_loaddg();
}
$('#oalistgrid').datalist({ 
    url:listgridurl,
	rownumbers: true,
	method:'get',
    fit:true,striped:true,
	lines: true,
	border: false,
	onLoadSuccess:function(data){$('#loaddiv').window('close');},
	textFormatter: function(value,row,index){
		var datatext = row.workflow[0].name;
		if(row.workflow.length>1)
			for(var i=1;i<row.workflow.length;i++)
				datatext += '/<br>'+row.workflow[i].name;
		return datatext;//'<img src="../../themes/icons/enter.png" /> ' + datatext;
	},
	onSelect:function(index, row){
		var dgtitle = '';
		for(var i=0;i<row.workflow.length;i++){
			if(row.workflow[i].isOA){
				dgtitle += '<a href="javascript:_addworkflow('+row.workflow[i].id+');">'+row.workflow[i].name+'（发起）</a> ';
			}else dgtitle += row.workflow[i].name+" ";
		}
		$("#dgLayout").panel('setTitle', dgtitle);
		if(!row.nophase){
			$('#nophase,#nophasetext').show();
			_nophase();
		}else{
			$('a[name="titlephase"],#nophase,#nophasetext').hide();
			_loaddg();
		}
	}
});
function _loaddg(){
	var listrows = $('#oalistgrid').datagrid('getSelections');
	if(listrows.length>0){
		var row = listrows[0];
		if($('#nophase').is(":checked")){
			var dgurl = "http://"+window.location.host + row.url+"?projectId="+prjid;
		}else var dgurl = "http://"+window.location.host + row.url+"?projectId="+prjid+"&phaseId="+prjPhaseID;
		$("#oadatagrid").datagrid({
			url:dgurl,
			//data:row.data,
			rownumbers: true,
			border: false,
			singleSelect:true,
			method: 'get',
			//fitColumns:true,
			fit: true,striped:true,
			idField: 'requestId',
			columns:[[
				{ field: 'requestName', title: '流程标题',formatter: function(value,row,index){
					return value+" <a href='javascript:_linkOA("+row.requestId+");'>查看</a>";
				} },
				{ field: 'phaseName', title: '阶段', hidden:row.nophase },
				{ field: 'major', title: '专业', hidden:row.noMajor },
				{ field: 'createBy', title: '创建人' },
				{ field: 'createDate', title: '创建日期' },
				{ field: 'status', title: '当前状态' },
				{ field: 'currentPerson', title: '当前操作者'}]]
		}).datagrid('clearSelections');
	}
} */
function _addworkflow(workflowid){
	window.open('http://'+OAurl+'/interface/addworkflow.jsp?workflowid='+workflowid+'&workcode='+workcode,"_blank","location=no");
}
function _linkOA(requestId){//跳转OA表单
	window.open("http://"+OAurl+"/workflow/request/ViewRequest.jsp?requestid="+requestId,'_blank','location=no');
}