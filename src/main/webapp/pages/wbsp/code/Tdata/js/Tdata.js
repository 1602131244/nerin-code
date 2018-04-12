$(function () {
	var hosturl = "http://192.168.27.55:8080/pages/TdataFiles/files";
	var btns = '#selectbtn,#applybtn,#unselectbtn';
	var tree = '#pTree';
	$('#selectbtn').linkbutton({
		onClick:function(){
			if($('#Ttree').tree('getSelected') && $('#Ttree').tree('getSelected').isFile){
				_addGrid($('#Ttree').tree('getSelected').id);
			}else $.messager.alert('操作提示', '请先在左侧栏目中选择一个文件。', 'error');
		}
	});
	$('#expandbtn').linkbutton({
		onClick:function(){
			new ActiveXObject("Wscript.Shell").run("G:\\NIMSwork\\InfoCenter\\NIMS开发\\CADI\\设计文印集成流程数据接口.xlsx");
			//"G:\\NIMSwork\\InfoCenter\\NIMS开发\\测试培训\\2015培训\\1510公司培训\\NIMS培训助手v2.4.exe");
			//$(tree).tree('expandAll');
		}
	});
	$('#collapsebtn').linkbutton({
		onClick:function(){
			//$(tree).tree('collapseAll');
			
			var i=0;
			var int = window.setInterval(function(){
				if(i==0){
					$('#loaddiv').window('open');
					i++;
				}else if(i==1){
					i++;
					var fso = new ActiveXObject("Scripting.FileSystemObject");
					var s = "";
					var sArray = [];
					_getdir("G:\\Projects\\", s);
					var tempf = fso.GetSpecialFolder(2);
					var f1 = fso.createtextfile(tempf+"\\CollabWise.txt",true);
					f1.write(s);
					new ActiveXObject("Wscript.Shell").run(tempf+"\\CollabWise.txt");//"notepad.exe "+
					function _getdir(fpath){
						var folder = fso.GetFolder(fpath);
						var files = new Enumerator(folder.Files);
						for(;!files.atEnd();files.moveNext()){
							s += files.item();
							s += "\r\n";
						}
						var sub = new Enumerator(folder.SubFolders);
						for(;!sub.atEnd();sub.moveNext()){
							s += sub.item();
							s += "\r\n";
							_getdir(sub.item());
						}
					}
					$('#loaddiv').window('close');
				}else int=window.clearInterval(int);
			},100);
		}
	});
	function _readPath(){
		var fso = new ActiveXObject("Scripting.FileSystemObject");
		var s = "";
		var sArray = [];
		_getdir("G:\\Projects\\", s);
		var tempf = fso.GetSpecialFolder(2);
		var f1 = fso.createtextfile(tempf+"\\CollabWise.txt",true);
		f1.write(s);
		new ActiveXObject("Wscript.Shell").run("notepad.exe "+tempf+"\\CollabWise.txt");
		function _getdir(fpath){
			var folder = fso.GetFolder(fpath);
			var files = new Enumerator(folder.Files);
			for(;!files.atEnd();files.moveNext()){
				s += files.item();
				s += "\r\n";
			}
			var sub = new Enumerator(folder.SubFolders);
			for(;!sub.atEnd();sub.moveNext()){
				s += sub.item();
				s += "\r\n";
				_getdir(sub.item());
			}
		}
	}
	$('#applybtn').linkbutton({
		onClick:function(){
			var gridData = $('#Tgrid').datagrid('getData').rows;
			if(gridData.length>0){
				$.messager.confirm('操作提示', '将右侧表格中所有的文件（包括未勾选行）发起新的授权申请审批流程，是否继续？', function (r) {
					if (r) {
						var uploadData = [];
						for(var i=0;i<gridData.length;i++){
							uploadData[i]={};
							for(var key in gridData[i]){
								switch (key){
									case "text":
									uploadData[i].documentName=gridData[i][key];
									break;
									case "time":
									uploadData[i].documentDate=gridData[i][key];
									break;
									case "size":
									uploadData[i].documentSize=gridData[i][key];
									break;
									case "path":
									uploadData[i].documnetDirectory=gridData[i][key];
									break;
									case "linkurl":
									uploadData[i].documentLink=hosturl+"/controlled"+gridData[i][key]+"[code_"+gridData[i].code+"]"+gridData[i].text;
									break;
								}
							}
						}
						$.ajax({
							url: "http://192.168.15.211:8080/api/documentManage/saveDocumentManage",
							data: JSON.stringify(uploadData),
							contentType: "application/json",
							type: "POST",
							beforeSend: function () {
								$(btns).linkbutton('disable');
								$('#Tgrid').datagrid('loading');
							},
							success: function (data) {
								$(btns).linkbutton('enable');
								$('#Tgrid').datagrid('loaded');
								if(data.db_state==1){
									window.open(data.db_url, "_blank");
								}else $.messager.alert('操作提示', '提交失败：请检查后重试或联系系统管理员。', 'error');
							}
						});
					}
				});
			}else $.messager.alert('操作提示', '右侧表格中无文件。', 'error');
		}
	});
	$('#unselectbtn').linkbutton({
		onClick:function(){
			var rows = $('#Tgrid').datagrid('getSelections');
			var delId = [];
			var delIndex = [];
			for(var i=0;i<rows.length;i++){
				delId[delId.length] = {"id":rows[i].id,"text":rows[i].text,"time": rows[i].time,"size":rows[i].size,"isFile":rows[i].isFile,"parentId":rows[i].parentId,"link":rows[i].code};
				delIndex[delIndex.length] = $('#Tgrid').datagrid('getRowIndex', rows[i].id);
			}
			if(delId.length>0){
				for(var j=0;j<delId.length;j++){
					var i = delId.length - 1 - j;
					if(delId[i].parentId==0){
						var peers = $('#Ttree').tree('getRoots');
					}else{
						var pNode = $('#Ttree').tree('find', delId[i].parentId);
						var peers = $('#Ttree').tree('getChildren', pNode.target);
					}
					var getBigger = false;
					if(peers.length>0){
						for(var k=0;k<peers.length;k++){
							if(parseInt(peers[k].id)>parseInt(delId[i].id) && peers[k].parentId == delId[i].parentId){
								var peerNode = $('#Ttree').tree('find',peers[k].id);
								$('#Ttree').tree('insert', {before: peerNode.target, data: [delId[i]]});
								getBigger = true;
								break;
							}
						}
					}
					if(!getBigger){
						if(delId[i].parentId==0){
							$('#Ttree').tree('append', {data: [delId[i]]});
						}else $('#Ttree').tree('append', {parent: pNode.target, data: [delId[i]]});
					}
					$('#Tgrid').datagrid('deleteRow', delIndex[i]);
				}
				$('#Tgrid').datagrid('clearSelections');
			}else $.messager.alert('操作提示', '请先在右侧表格中选择文件。', 'error');
		}
	});
	$('#tabs').tabs({
		fit:true,
		//selected:1,
		onSelect:function(title,index){
			if(index==0){
				tree = '#pTree';
				$('#selectbtn,#unselectbtn').linkbutton('disable');
			}else{
				tree = '#Ttree';
				$('#selectbtn,#unselectbtn').linkbutton('enable');
			}
		}
	});
	$.ajax({
		url:hosturl+'/pTree.json',
		contentType: 'application/x-www-form-urlencoded',
		type: "GET",
		success: function (data) {
			var treedata = eval(data);
			var total = treedata.length;
			for(var j=0;j<total;j++){
				var i=total - 1- j;
				if(!treedata[i].isFile){
					treedata[i].iconCls = "icon-tree-folder";
					treedata[i].state = "closed";
				}else{
					treedata[i].time = treedata[i].time.substr(0,4)+"-"+treedata[i].time.substr(4,2)+"-"+treedata[i].time.substr(6,2)+" "+treedata[i].time.substr(8,2)+":"+treedata[i].time.substr(10,2);
					if(parseFloat(treedata[i].size)>1000){
						treedata[i].size = Math.round(100*parseFloat(treedata[i].size)/1000)/100 + "MB";
					}else treedata[i].size = Math.round(100*parseFloat(treedata[i].size))/100 + "KB";
				}
				if(treedata[i].parentId!=0){
					for(var k=0;k<treedata.length;k++){
						if(p_setChildren(treedata[i],treedata[k])){
							treedata.splice(i,1);
							break;
						}
					}
				}
			}
			$('#pTree').tree({
				data : treedata,
				onDblClick:function(node){
					if(node.isFile){
						var span = "<span style='background-color:lightgray;color:#fff'>";
						var linkurl = "/";
						p_getPath($('#pTree').tree('getParent', node.target));
						if(node.text.substr(-7,7)!="</span>"){
							var pFile = node.text;
							$('#pTree').tree('update', {target: node.target,text: span+node.text+"</span>"});
						}else var pFile = node.text.substr(span.length,node.text.length-span.length-7);
						window.open(hosturl+"/public"+linkurl+pFile, "_blank");
					}
					function p_getPath(treenode){
						if(!treenode) return;
						linkurl = "/"+treenode.text+linkurl;
						if(treenode.parentId){
							p_getPath($('#pTree').tree('getParent', treenode.target));
						}
					}
				}
			});
			function p_setChildren(node,obj){
				var result = false;
				if(obj.id==node.parentId){
					if(!obj.children) obj["children"] = [];
					obj.children.splice(0,0,node);
					result = true;
				}else if(obj.children){
					for(var i=0;i<obj.children.length;i++){
						p_setChildren(node,obj.children[i]);
					}
				}
				return result;
			}
		}
	});
	$.ajax({
		url:hosturl+'/Ttree.json',
		contentType: 'application/x-www-form-urlencoded',
		type: "GET",
		success: function (data) {
			var treedata = eval(data);
			var total = treedata.length;
			for(var j=0;j<total;j++){
				var i=total - 1- j;
				if(!treedata[i].isFile){
					treedata[i].iconCls = "icon-tree-folder";
					treedata[i].state = "closed";
				}else{
					treedata[i].time = treedata[i].time.substr(0,4)+"-"+treedata[i].time.substr(4,2)+"-"+treedata[i].time.substr(6,2)+" "+treedata[i].time.substr(8,2)+":"+treedata[i].time.substr(10,2);
					if(parseFloat(treedata[i].size)>1000){
						treedata[i].size = Math.round(100*parseFloat(treedata[i].size)/1000)/100 + "MB";
					}else treedata[i].size = Math.round(100*parseFloat(treedata[i].size))/100 + "KB";
				}
				if(treedata[i].parentId!=0){
					for(var k=0;k<treedata.length;k++){
						if(_setChildren(treedata[i],treedata[k])){
							treedata.splice(i,1);
							break;
						}
					}
				}
			}
			$('#Ttree').tree({
				data : treedata,
				onDblClick:function(node){
					if(!$(btns).linkbutton('options').disabled) _addGrid(node.id);
				}
			});
			function _setChildren(node,obj){
				var result = false;
				if(obj.id==node.parentId){
					if(!obj.children) obj["children"] = [];
					obj.children.splice(0,0,node);
					result = true;
				}else if(obj.children){
					for(var i=0;i<obj.children.length;i++){
						_setChildren(node,obj.children[i]);
					}
				}
				return result;
			}
		}
	});
	$('#Tgrid').datagrid({
		idField: 'id',
		rownumbers: true,
		fit: true,
		border:false,
		checkbox: true,
		toolbar:'#toolbar',
		columns: [[{field:'ck',checkbox:true },
			{ field: 'text', title: '文件名称'},
			{ field: 'time', title: '修改时间'},
			{ field: 'size', title: '大小'},
			{ field: 'path', title: '所在目录'}
		]],
		onDblClickRow:function(index,row){
			if(row.parentId!=0){
				var target = $('#Ttree').tree('find', row.parentId).target;
				$('#Ttree').tree('select', target).tree('scrollTo', target);;
				$('#Tgrid').datagrid('selectRow',index);
			}else $.messager.show({title:'提示', msg:'此文件位于根目录。'});
		}
	});
});
function _addGrid(id){
	var node = $('#Ttree').tree('find', id);
	if(node.isFile){
		var titleTxt = "\\";
		var linkurl = "/";
		_getPath($('#Ttree').tree('getParent', node.target));
		$('#Tgrid').datagrid('appendRow',{
			id: id,
			text: node.text,
			time: node.time,
			size:node.size,
			isFile:node.isFile,
			parentId:node.parentId,
			path:titleTxt,
			linkurl:linkurl,
			code:node.link
		}).datagrid('autoSizeColumn');
		$('#Ttree').tree('remove',node.target);
		var index = $('#Tgrid').datagrid('getRowIndex', id);
		$('#Tgrid').datagrid('scrollTo', index);
	}
	function _getPath(treenode){
		if(!treenode) return;
		titleTxt = "\\ "+treenode.text+" "+titleTxt;
		linkurl = "/"+treenode.text+linkurl;
		if(treenode.parentId){
			_getPath($('#Ttree').tree('getParent', treenode.target));
		}
	}
}