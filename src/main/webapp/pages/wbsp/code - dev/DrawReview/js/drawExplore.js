var OAurl = "192.168.15.198";
var ecmurl = "http://192.168.15.252:16200";
var prjbasicdataurl = "http://"+window.location.host+"/api/lev2/enterProject";//"prjdataurl.json";//oracle包-当前项目阶段信息（面板标题、总部/分公司类型项目、是否启用双语、WBS发布版数、是否分子项管理、阶段计划起始日期、阶段计划完成日期）
var prjNumber = "";
var prjid = "111";
var prjName = "测试项目名称";
var prjPhaseID = "12";
var prjPhase = "DDA施工图设计";
var prjManagerName = "章颂泰";
var prjStartDate = "2015-09-10";
var prjEndDate = "2016-10-10";
var workcode = "100232";
var userID = null;
var userPar = "";
var userName = "章颂泰";
var isPM = false;
var draftwbsID = "123"; //草稿版id
var pubwbsID = "123"; //最新发布版id
var draftwbsCheckout = "";//草稿版WBS签出员工号，若未签出则为空值
var division = false;//是否分子项管理
var headOrbranch = "head";//总部或分公司类型项目
var dual = "";//双语
var oldPhase = false;//老阶段
//var prj_major_dataurl = "http://"+window.location.host+"/api/lev2/querySpecList";//"datagrid_Prjmajor.json";//引用专业数据源 oracle包-加载当前项目参与专业列表
var speData = '';
var workId = null;
var enablePlt = ["未校审","校审中"];
$(function () {
	$.ajax({
		url: "http://" + window.location.host +"/api/logonUser",//获取session身份
		contentType : 'application/x-www-form-urlencoded',
		dataType: "json",
		type: "GET",
		beforeSend: function() {},
		success: function(logondata) {
			if(!logondata.userId || logondata.userId == 0){
				alert("请登录 NIMS 2.0。");
				window.open("http://"+window.location.host,"_self");
			}else{
				workId = getUrlParam("workId");//url必须载入的参数
				var requestid = getUrlParam("requestid");
				if(!requestid){
					$('#root').layout('remove', 'south');
				}else $('#root').layout('expand', 'south');
				userID = ""+logondata.userId;
				workcode = logondata.employeeNo;
				$('#drawgrid').datagrid({ 
					url:"http://"+window.location.host+"/api/draw/DrawList?isPM=1",
					queryParams:{DlvrIds:workId},
					pageSize: 20,pagination: true, pageList:[10,20,50,100],
					rownumbers: true,
					method:'POST',
					//fitColumns:true,
					singleSelect:true,
					fit:true,striped:true,
					lines: true,
					idField: 'id',
					onLoadSuccess: function(){
						$('#drawgrid').datagrid('clearSelections');
						if($('#confirmCatbtn')){
							$('#confirmCatbtn').linkbutton({
								onClick: function () {
									var dgrows = $('#drawgrid').datagrid('getRows');
									if(dgrows.length==0){
										$.messager.alert("操作提示","无图纸可发布，请先使用CAD插件将图纸上传至NIMS系统。","error", function(){_backOA(requestid);});
									}else{
										//ajax改写OA表单字段，成功后跳转OA表单
										alert("更新OA表单字段值");
										_backOA(requestid);
									}
								}
							});
						}
					},
					frozenColumns: [[
						{ field: 'drawName', title: '图名'}]],
					columns: [[
						{ field: 'drawNum', title: '图号',formatter: function(value,row,index){
							return value+" <a href='javascript:_linkCat("+row.id+");'>查看</a>";
						}},
						{ field: 'xPltStatus', title: '状态',formatter:function(value,row,index){
							return _getdrawStat(row);
						},styler: function(value,row,index){
							var drawStat = _getdrawStat(row);
							if($.inArray(drawStat,enablePlt)==-1)
								return 'background-color:lightgray;color:white';
						} },
						{ field: 'reviseNum', title: '版本' },
						{ field: 'xSize', title: '规格' },
						{ field: 'xDivision', title: '子项'},
						{ field: 'specialityName', title: '专业'},
						{ field: 'xDlvrName', title: '工作包'},
						{ field: 'xdesigned_NAME', title: '设计人'},
						{ field: 'xchecked_NAME', title: '校核人'},
						{ field: 'xreviewed_NAME', title: '审核人'},
						{ field: 'xDlvrId', title: '工作包号'},
						{ field: 'xapproved_NAME', title: '审定人'},
						{ field: 'xregistered_Engineer_Name', title: '注册工程师'},
						{ field: 'xfangan_name', title: '方案设计人'},
						{ field: 'xEquipmentNum', title: '设备编码'},
						{ field: 'xEquipment', title: '设备名称'}]]
				});
			}
		},
		error: function(data){$.messager.alert("身份验证失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");}
	});
});

function _getdrawStat(row){
	if(!row.xWpApprStatus){
		var result = "未校审";
	}else if(row.xWpApprStatus!="校审通过"){
		var result = row.xWpApprStatus;
	}else{
		if(!row.xCountersignStatus){
			if(!row.xPltStatus){
				var result = row.xWpApprStatus;
			}else var result = row.xPltStatus;
		}else if(row.xCountersignStatus!="对图通过"){
			var result = row.xCountersignStatus;
		}else{
			if(!row.xPltStatus){
				var result = row.xCountersignStatus;
			}else var result = row.xPltStatus;
		}
	}
	return result;
}
function _linkCat(did){
	window.open(ecmurl+"/cs/idcplg?IdcService=DOC_INFO&dID="+did,'_blank');
}
function _backOA(requestid){
	window.open("http://"+OAurl+"/workflow/request/ViewRequest.jsp?requestid="+requestid,'_self');
}
//公共方法,字符串转换日期
String.prototype.stringToDate = function () {
    return new Date(Date.parse(this.replace(/-/g, "/")));
}

function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}
