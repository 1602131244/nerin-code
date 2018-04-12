$(function () {
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
							$('#check').val(data.bgyyjsp);
							$('#prjName').val(data.xmmc);
							$('#drawsName').val(data.drawsName);
							$('#drawsNum').val(data.drawsNum);
							$('#subName').val(data.subName);
							$('#major').val(data.zy);
							$('#requestid').val(sn);
							$('#titleform').submit();
						}
					});
				}
			});
		}
	});	
	$('#edit').linkbutton({//编辑已有按钮
		iconCls: 'icon-edit',
		plain:true,
		onClick: function () {
			window.open(nopturl+"/DataRegionEdit.jsp?requestid="+sn,"_self");
		}
	});
});
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}