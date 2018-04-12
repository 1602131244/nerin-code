/**
 * Created by Administrator on 2017/3/24.
 */

var config = new nerinJsConfig();

$(function () {
    console.log("page loaded");
    setFocus();

    //监听输入框
    $('#request_id').keydown(function (e) {
        if(!e){
            e = window.event;
        }
        if((e.keyCode || e.which) == 13){
            checkRequestId();
        }
    });
    
    // 监听发起填报流程按钮
    $('#BUT_OK').on('click', function () {
        //执行程序
        checkRequestId();
    });

    // 监听发起填报流程按钮
    $('#BUT_CLEAR').on('click', function () {
        clearrequest();
    });

    //检查域用户验证程序
    function checkRequestId() {
        var requestText = $('#request_id').val();
        if ("" == requestText.trim()) {
            window.parent.error("请输入流程ID！",1000);
            setTimeout(function(){
                    clearrequest();}
                ,1500 );
            return;
        }

        // var no = requestText.indexOf("-");
        // if  (no > 0){
        //     // var requestText = $('#request_id').val();
        //     var personNumber = requestText.substring(0,no);
        //     // var requestId = requestText.substring(no + 1,length);
        //     initCheckModal(personNumber);
        // }
        // else
        //     saverequest($('#request_id').val(),"","RECEIVE");

        var val = $('input:radio:checked').val();
        if  (val=="option2"){
            // var requestText = $('#request_id').val();
            // var personNumber = requestText.substring(0,no);
            // var requestId = requestText.substring(no + 1,length);
            initCheckModal("");
        }
        else
            saverequest($('#category').val(),$('#request_id').val(),"","RECEIVE");
    };

    //保存程序
    function saverequest(requestType,requestId,personNumber,type) {
        $.ajax({
            url: config.baseurl +"/api/oieRest/updateOaRequestOie",
            data: {
                requestType:requestType
                ,requestId: requestId
                ,personNumber: personNumber
                ,type:  type
            },
            contentType : 'application/x-www-form-urlencoded',
            dataType: "json",
            type: "POST",
            beforeSend: function() {
                window.parent.showLoading();
            },
            success: function(data) {
                if ("0000" == data.returnCode) {
                    // window.parent.info("已确认流程"+ requestId +"!",2000);
                    window.parent.info(data.db_msg,2000);
                    setTimeout(function(){
                        clearrequest();}
                    ,2500
                    );
                } else {
                    window.parent.error(data.returnMsg);
                    setTimeout(function(){
                            var test_val = $('#request_id').val();
                            $('#request_id').val(test_val);
                            setFocus();
                            }
                        ,11000
                    );
                }
            },
            complete: function() {
                window.parent.closeLoading();
            },
            error: function() {
                window.parent.closeLoading();
                window.parent.error(ajaxError_sys);
            }
        });

    };

    //清楚程序
    function clearrequest() {
        $('#request_id').val("");
        setFocus();
    };

    //设置鼠标焦点
    function setFocus()
    {
        document.getElementById('request_id').focus();
    };

    //显示验证域用户界面
    function initCheckModal(personNumber) {
        $('#person_number').val(personNumber);
        $('#password').val("");

        // 显示新增界面
        $('#check_modal').modal('show');
        // document.getElementById('person_number').focus();
    }


    // 监听验证域用户界面保存按钮
    $('#BUT_CHECK').on('click', function () {
        var valFlag = true;

        var personNumber = $('#person_number').val();
        var password = $('#password').val();

        if ("" == personNumber.trim()) {
            window.parent.error("请输入确认用户编码！");
            valFlag = false;
        }
        if ("" == password.trim()) {
            window.parent.error("请输入域用户密码！");
            valFlag = false;
        }

        if (!valFlag)
            return;

        //域用户验证
        $.ajax({
            url : config.baseurl +"/api/oieRest/checkAdUser",
            contentType : 'application/x-www-form-urlencoded',
            dataType:"json",
            data: {
                username: personNumber 
                ,password: password
            },
            type:"POST",
            beforeSend:function(){
            },
            success:function(data){
                if (1== data.returnCode) {
                    $('#check_modal').on('shown.bs.modal', function (e) {}).modal('hide');
                    // var requestText = $('#request_id').val();
                    // var no = requestText.indexOf("-");
                    // var requestId = requestText.substring(no + 1);

                    // var requestId = $('#request_id').val();

                    saverequest($('#category').val(),$('#request_id').val(),personNumber,'BACK');
                } else {
                    window.parent.error(data.returnMsg);
                }
            },
            complete:function(){
                window.parent.closeLoading();
            },
            error:function(){
                window.parent.closeLoading();
                window.parent.error(ajaxError_loadData);
            }
        });
    });


});