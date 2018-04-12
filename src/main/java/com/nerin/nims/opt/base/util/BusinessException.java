package com.nerin.nims.opt.base.util;

/**
 * Created by yinglgu on 6/21/2016.
 */
public class BusinessException extends Exception {

    private String retCd ;  //异常对应的返回码
    private String msgDes;  //异常对应的描述信息

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
        msgDes = message;
    }

    public BusinessException(String retCd, String msgDes) {
        super();
        this.retCd = retCd;
        this.msgDes = msgDes;
    }

    public String getRetCd() {
        return retCd;
    }

    public String getMsgDes() {
        return msgDes;
    }
}
