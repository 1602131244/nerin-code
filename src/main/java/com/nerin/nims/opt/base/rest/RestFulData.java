package com.nerin.nims.opt.base.rest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yinglgu on 6/17/2016.
 */
public class RestFulData {
    public static final String RETURN_CODE = "returnCode";
    public static final String RETURN_MSG = "returnMsg";
    public static final String RETURN_DATA = "data";

    public static final String RETURN_CODE_VALUE = "0000";
    public static final String RETURN_MSG_VALUE = "成功";

    public static Map getRestInitData() {
        Map map = new HashMap();
        map.put(RETURN_CODE, RETURN_CODE_VALUE);
        map.put(RETURN_MSG, RETURN_MSG_VALUE);
        return map;
    }
}
