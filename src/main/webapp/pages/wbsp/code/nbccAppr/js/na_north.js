var northexpanded = false;
$(function () {
    //设置标题
	if(taskHeaderId)
		$('#north').panel({ title: taskName + " - " +prjName + " ("+prjNumber+") " + "<a name='titlephase'>项目阶段:" + prjPhase + "</a> 项目经理:" + prjManagerName});
	$(document.body).layout('collapse', 'north');
    //第一次展开时的异步加载项目
    $(document.body).layout('panel', 'north').panel({
        onExpand: function () {
            if (!northexpanded) {
                northexpanded = true;
				$('#taskdg').datagrid({//协作任务列表
					data:[],
					method: 'get',
					idField: 'taskHeaderId',
					rownumbers: true, singleSelect: true, 
					fit: true,toolbar:'#tasktb',border:false,striped:true,
					onLoadSuccess: function(){
						$('#taskdg').datagrid('clearSelections');
					},
					frozenColumns: [[
						{ field: 'taskName', title: '任务名称',formatter: function(value,row,index){
							if(row.taskHeaderId!=taskHeaderId){
								return value+" <a href='javascript:switchTask("+index+");'>进入</a>";
							}else return value;
						}}]],
					columns: [[
						{ field: 'designPhaseName', title: '阶段'},
						{ field: 'taskTypeName', title: '文本类型'},
						{ field: 'projectManagerName', title: '项目经理'},
						{ field: 'projectRoleName', title: '我的角色'},
						{ field: 'taskProgress', title: '章节进度'},
						{ field: 'taskProgress2', title: '专业校审进度'},
						{ field: 'taskStatusName', title: '任务状态'},
						{ field: 'createByName', title: '创建人'},
						{ field: 'creationDate', title: '创建日期'},
						{ field: 'projectName', title: '项目名称'},
						{ field: 'projectNumber', title: '项目编号'}]]
				});
				$('#prjtree').tree({
					border:false,
					onLoadSuccess:function(node, data){$('#loaddiv').window('close');},
					checkbox:true,
					cascadeCheck:true,
					onCheck:function(node){
						var checkednodes = $('#prjtree').tree('getChecked');
						for(var i=0;i<checkednodes.length;i++){
							$(checkednodes[i].target).find("span.tree-title").css({"background-color":"#0e2d5f","color":"#fff"});
						}
						var uncheckednodes = $('#prjtree').tree('getChecked', 'unchecked');
						for(var i=0;i<uncheckednodes.length;i++){
							$(uncheckednodes[i].target).find("span.tree-title").removeAttr("style");
						}
						var incheckednodes = $('#prjtree').tree('getChecked', 'indeterminate');
						for(var i=0;i<incheckednodes.length;i++){
							$(incheckednodes[i].target).find("span.tree-title").removeAttr("style");
						}
					},
					data:[],
					formatter:function(node){
						if(node.tasklist){
							return node.text + " - " + node.tasklist.length;
						}else return node.text;
					},
					onBeforeSelect:function(){return false;},
					onDblClick:function(node){
						if(node.tasklist){
							var oldcknodes = $('#prjtree').tree('getChecked');
							for(var i=0;i<oldcknodes.length;i++){
								$('#prjtree').tree('uncheck',oldcknodes[i].target);
							}
							$('#prjtree').tree('check',node.target);
							$('#taskdg').datagrid('loadData', node.tasklist).datagrid('autoSizeColumn', 'taskName');
						}
					}
				});
                $('#prjsearch').searchbox({
					prompt:"项目名称、编号模糊查找协作文本任务",
					searcher: function (value, name) {
						$.ajax({
							url: taskHeaderListUrl,
							data: {"projSearch":value},
							contentType : 'application/x-www-form-urlencoded',
							dataType: "json",
							type: "POST",
							beforeSend: function (){$('#loaddiv').window('open');},
							success: function (data) {
								var treeData = [];
								for(var i=0;i<data.dataSource.length;i++){
									var matchPrj = false;
									for(var j=0;j<treeData.length;j++){
										if(data.dataSource[i].projectName==treeData[j].projectName && data.dataSource[i].projectNumber==treeData[j].projectNumber){
											matchPrj = true;//匹配项目
											var matchPhase = false;
											for(var k=0;k<treeData[j].children.length;k++){
												if(treeData[j].children[k].designPhaseName==data.dataSource[i].designPhaseName){
													matchPhase = true;//匹配阶段向treeData项目阶段下追加任务
													treeData[j].children[k].tasklist[treeData[j].children[k].tasklist.length] = data.dataSource[i];
													break;
												}
											}
											if(!matchPhase){//无匹配阶段向treeData项目下追加阶段、任务
												treeData[j].children[treeData[j].children.length] = {
													"text":data.dataSource[i].designPhaseName?data.dataSource[i].designPhaseName:"其他",
													"designPhaseName":data.dataSource[i].designPhaseName,
													"tasklist":[data.dataSource[i]]
												};
											}
											break;
										}
									}
									if(!matchPrj){//无匹配项目向treeData追加项目、阶段、任务
										treeData[treeData.length] = {
											"text":data.dataSource[i].projectName+"("+data.dataSource[i].projectNumber+")",
											"projectName":data.dataSource[i].projectName,
											"projectNumber":data.dataSource[i].projectNumber,
											"children":[
												{
													"text":data.dataSource[i].designPhaseName?data.dataSource[i].designPhaseName:"其他",
													"designPhaseName":data.dataSource[i].designPhaseName,
													"tasklist":[data.dataSource[i]]
												}
											]
										};
									}
								}
								$('#prjtree').tree('loadData',treeData);
							}
						});
					}
				});
				$("#listtask").linkbutton({
					iconCls: 'icon-ahead',
					plain:true,
					onClick: function () {
						var listtree = $('#prjtree').tree('getChecked');
						var listdg = [];
						for(var i=0;i<listtree.length;i++){
							if(listtree[i].tasklist){
								for(var j=0;j<listtree[i].tasklist.length;j++){
									listdg[listdg.length] = listtree[i].tasklist[j];
								}
							}
						}
						$('#taskdg').datagrid('loadData', listdg).datagrid('autoSizeColumn', 'taskName');
					}
				});
            }
        }
    });
	if(!taskHeaderId) $(document.body).layout('expand', 'north');
});

//切换协作任务
function switchTask(tkIndex) {
	var tkrow = $('#taskdg').datagrid('getRows')[tkIndex];
	$.messager.confirm('操作提示', '进入任务：<br>'+tkrow.taskName+'<br>将放弃所有未保存的编辑，请确认是否继续？', function (r) {
		if (r) window.location.href = "http://"+window.location.host+window.location.pathname+"?taskHeaderId="+tkrow.taskHeaderId;
	});
}