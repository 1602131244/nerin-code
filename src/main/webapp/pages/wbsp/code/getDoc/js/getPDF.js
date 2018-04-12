$(function () {
	var pngpatch = "[image]png/";
	//测试环境
	var nopturl = "http://192.168.15.91:8080/cux/DesignChange";
	//正式环境
	//var nopturl = "ebs.nerin.com:8008";
	var sn = getUrlParam("requestid");
	$('#create').linkbutton({//新建覆盖按钮
		iconCls: 'icon-add',
		plain:true,
		onClick: function () {
			$.messager.confirm('操作提示', '将新建设计变更通知书文件，现有文件将被覆盖，请确认是否继续？', function (r) {
				if (r){
					$.ajax({
						type:'get',
						url:"http://"+window.location.host+"/api/naviWbpsRest/sjbgdtOa?requestId="+sn,
						success:function(data){
							$.ajax({
								type:'get',
								url:"http://"+window.location.host+"/pages/wbsp/common/majorList.json",
								success:function(plist){
									if(data.sqr){
										$('#changer').val(pngpatch+data.sqr+".png[/image]");
									}else if(data.dqsjr) $('#changer').val(pngpatch+data.dqsjr+".png[/image]");
									if(data.sqr && data.dqsjr && data.sqr!=data.dqsjr) $('#changer02').val(pngpatch+data.dqsjr+".png[/image]");
									var checker = ["checker","checker02","checker03","checker04"];
									var checkerval = [data.dqjhr,data.dqshr,data.dqsdr,data.zrgcs];
									var ck = 0;
									for(var i=0;i<checkerval.length;i++){
										var match = false;
										if(i>0){
											for(var j=0;j<i;j++){
												if(checkerval[i]==checkerval[j]){
													match = true;
													break;
												}
											}
										}
										if(!match && checkerval[i]){
											$('#'+checker[ck]).val(pngpatch+checkerval[i]+".png[/image]");
											ck ++;
										}
									}
									var s_i = Math.min(4,data.detail.length);
									if(s_i>0){
										var sk = 1;
										for(var i=0;i<s_i;i++){
											var match = false;
											if(i>0){
												for(var j=0;j<i;j++){
													if(data.detail[i].zymc == data.detail[j].zymc && data.detail[i].zyfzrmx == data.detail[j].zyfzrmx){
														match = true;
														break;
													}
												}
											}
											if(!match){
												$('#s'+sk).val(plist["2"+data.detail[i].zymc].full);
												$('#i'+sk).val(pngpatch+data.detail[i].zyfzrmx+".png[/image]");
												sk ++;
											}
										}
									}
									if(data.dqzcgcs) $('#certier').val(pngpatch+data.dqzcgcs+".png[/image]");
									if(data.xmjl) $('#PM').val(pngpatch+data.xmjl+".png[/image]");
									if(data.sjrApproveDate) $('#changerDate').val(_getDate(data.sjrApproveDate));
									var dateArr = [data.jhrApproveDate,data.shrApproveDate,data.sdrApproveDate,data.zrgcsApproveDate];
									dateArr.sort();
									for(var i=0;i<dateArr.length;i++){
										if(dateArr[dateArr.length - 1 - i]){
											$('#checkerDate').val(_getDate(dateArr[dateArr.length - 1 - i]));
											break;
										}
									}
									if(data.zcgcsApproveDate) $('#certiDate').val(_getDate(data.zcgcsApproveDate));
									var now = new Date();
									var PMdate = now.getFullYear()+"年"+((now.getMonth()+1)<10?"0":"")+(now.getMonth()+1)+"月"+(now.getDate()<10?"0":"")+now.getDate()+"日";
									$('#PMdate').val(PMdate);
									if(data.bgdbh){
										$('#tabNum').val(data.bgdbh);
									}else $('#tabNum').val("(     )年设(  )字第   号");
									$('#requestid').val(sn);
									
									$('#titleform').submit();
								},
								error:function(data){
									$.messager.alert("加载失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
								}
							});
						},
						error:function(data){
							$.messager.alert("加载失败","请检查后重试或联系系统管理员。<br>错误信息:"+JSON.stringify(data),"error");
						}
					});
				}
			});
		}
	});	
	$('#view').linkbutton({//浏览现有按钮
		iconCls: 'icon-search',
		plain:true,
		onClick: function () {
			window.open(nopturl+"/OpenPDF.jsp?requestid="+sn,"_self");
		}
	});
});
function _getDate(OAtime){
	var yy = OAtime.substr(0,4);
	var mm = OAtime.substr(5,2);
	var dd = OAtime.substr(8,2);
	return yy+"年"+mm+"月"+dd+"日";
}
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}