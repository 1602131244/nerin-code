package com.nerin.nims.opt.oie.module;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/**
 * Created by Administrator on 2017/3/28.
 */
public class CheckAdUser {
    private String returnCode;
    private String returnMsg;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public void connect(String username, String password) {
//        String userName = "username";//AD域认证，用户的登录UserName
//        String password = "";//AD域认证，用户的登录PassWord
        String host = "192.168.15.110";//AD域IP，必须填写正确
        String domain = "@nims.com";//域名后缀，例.@noker.cn.com
        String port = "389"; //端口，一般默认389
        String url = new String("ldap://" + host + ":" + port);//固定写法
        String user = username.indexOf(domain) > 0 ? username : username
                + domain;//网上有别的方法，但是在我这儿都不好使，建议这么使用
//        String user = domain + '\\' + username;
                //这里有两种格式，domain\User或邮箱的后缀名,建议用domain\User这种格式
        Hashtable env = new Hashtable();//实例化一个Env
        DirContext ctx = null;
        env.put(Context.SECURITY_AUTHENTICATION, "simple");//LDAP访问安全级别(none,simple,strong),一种模式，这么写就行
        env.put(Context.SECURITY_PRINCIPAL, user); //用户名
        env.put(Context.SECURITY_CREDENTIALS, password);//密码
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");// LDAP工厂类
        env.put(Context.PROVIDER_URL, url);//Url
        try {
            ctx = new InitialDirContext(env);// 初始化上下文
            this.setReturnCode("1");
            this.setReturnMsg("身份验证成功!");
//            System.out.println("身份验证成功!");
        } catch (AuthenticationException e) {
            this.setReturnCode("0");
            this.setReturnMsg("身份验证失败!");
//            System.out.println("身份验证失败!");
//            e.printStackTrace();
        } catch (javax.naming.CommunicationException e) {
            this.setReturnCode("0");
            this.setReturnMsg("AD域连接失败!");
//            System.out.println("AD域连接失败!");
//            e.printStackTrace();
        } catch (Exception e) {
            this.setReturnCode("0");
            this.setReturnMsg("身份验证未知异常!");
//            System.out.println("身份验证未知异常!");
//            e.printStackTrace();
        } finally{
            if(null!=ctx){
                try {
                    ctx.close();
                    ctx=null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
