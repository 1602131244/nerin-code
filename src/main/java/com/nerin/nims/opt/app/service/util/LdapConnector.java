package com.nerin.nims.opt.app.service.util;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

public class LdapConnector {
	private final static String CTX_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
	private final static String CONT_FACTORY = "com.sun.jndi.ldap.ControlFactory";
	private final static String HOST_URL = "ldap://192.168.15.110:389/";
	private static String username = "administrator@nims";
	private static String password = "password_ad12!@#";
	private static String root = "dc=nims,dc=com"; // 所操作的WLS域。也就是LDAP的根节点的DC
	private LdapConnector() {}

	public static LdapContext ldapContext() throws NamingException {
		LdapContext context = null;
		Hashtable<String, String> env=null;
			env = new Hashtable<String, String>();
			env.put(LdapContext.CONTROL_FACTORIES, CONT_FACTORY);
			env.put(Context.INITIAL_CONTEXT_FACTORY, CTX_FACTORY);
			env.put(Context.PROVIDER_URL, HOST_URL + root);
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, username);
			env.put(Context.SECURITY_CREDENTIALS, password);
			context = new InitialLdapContext(env, null);
		return context;
	}
	
	public static LdapContext ldapContext(String un,String pwd) throws Exception {
		LdapContext context = null;
		Hashtable<String, String> env=null;
			env = new Hashtable<String, String>();
			env.put(LdapContext.CONTROL_FACTORIES, CONT_FACTORY);
			env.put(Context.INITIAL_CONTEXT_FACTORY, CTX_FACTORY);
			env.put(Context.PROVIDER_URL, HOST_URL + root);
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, un);
			env.put(Context.SECURITY_CREDENTIALS, pwd);
			context = new InitialLdapContext(env, null);
		return context;
	}

	public static LdapContext getLdapCtx() throws NamingException {
		return ldapContext();
	}
	
//	public static void main(String[] args) throws Exception{
//		LdapConnector.ldapContext(username, password);
//    }
}
