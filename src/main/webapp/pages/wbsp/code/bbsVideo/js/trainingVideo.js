$(function () {
	var jsonfile = getUrlParam("clip");
	var listnum = getUrlParam("listnum");
	$('#macros').datalist({
		url: jsonfile+".json",
		method:'get',
		rownumbers:listnum=="Y",
		singleSelect: true,nowrap:false,
		fit: false,striped: true,
		onSelect:function(index, row){
			startplayer(row,index);
		},
		onLoadSuccess:function(data){
			startplayer(data.rows[0],0);
		}
	}).datagrid('clearSelections');	
	function startplayer(row,index){
		if(listnum == "Y"){
			var centertitle = (index+1)+"、"+row.text;
		}else var centertitle = row.text;
		$('#center').panel({ title:centertitle});
		var flashvars={
			f:"http://192.168.27.55"+encodeURI(encodeURI(row.file)),//中文文件名会导致firefox浏览器加载文件失败。
			c:0,
			b:1,
			p:1
		};
		var params={bgcolor:'#FFF',allowFullScreen:true,allowScriptAccess:'always'};
		CKobject.embedSWF('ckplayer/ckplayer.swf','a1','ckplayer_a1',768,432,flashvars,params);
	}
});
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}