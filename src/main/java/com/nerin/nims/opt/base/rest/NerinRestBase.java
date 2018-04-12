package com.nerin.nims.opt.base.rest;

import com.nerin.nims.opt.base.NerinBase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zach on 16/5/22.
 */
public class NerinRestBase extends NerinBase{
    public Map<String,String> responseMessage(int code, String message){
        Map messagmage = new HashMap<>();
        messagmage.put("code",code);
        messagmage.put("message",message);
        return messagmage;
    }

    public Map<String,String> responseMessageSucess(){
       return responseMessage(0,"操作成功!");
    }

    public Map<String,String> responseMessageError(){
        return responseMessage(-1,"操作失败!");
    }

    public Map<String,String> responseMessageByOpration(boolean flag){
        if(flag){
            return responseMessageSucess();
        }else {
            return responseMessageError();
        }
    }

}
