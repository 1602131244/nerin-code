package com.nerin.nims.opt.base.ecm.ridc;

import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.IdcClientManager;
import oracle.stellent.ridc.IdcContext;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.protocol.ServiceResponse;

/**
 * Created by Zak on 2017/1/17.
 */
public class RidcUtil {
    private static final String clientUrl = "http://192.168.15.252:16200/cs/idcplg";
    private static final String username = "weblogic";
    private static final String password = "weblogic123";
    static IdcClientManager manager = new IdcClientManager();
    static IdcContext userContext = new IdcContext(username, password);

    public static IdcClient getIdcClient() {
        IdcClient idcClient = null;
        try {
            idcClient = manager
                    .createClient(clientUrl);
        } catch (IdcClientException e) {
            e.printStackTrace();
        }
        return idcClient;
    }

    public static IdcContext getUserContext() {
        return userContext;
    }

    public static DataBinder getDataBinder() {
        return getIdcClient().createBinder();
    }

    public static DataBinder executeService(DataBinder dataBinder) throws IdcClientException {
        ServiceResponse response = null;
        try {
            response = RidcUtil.getIdcClient().sendRequest(getUserContext(), dataBinder);
            return response.getResponseAsBinder();
        } catch (IdcClientException e) {
//            e.printStackTrace();
            throw new IdcClientException(e);
        } finally {
            if (null != response)
                response.close();
        }
    }
}
