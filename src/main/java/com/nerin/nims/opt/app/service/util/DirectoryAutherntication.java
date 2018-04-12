package com.nerin.nims.opt.app.service.util;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import java.util.HashMap;
import java.util.Map;

public class DirectoryAutherntication {

	public static Map findLdapUserByUserName(String userName) throws NamingException {
		LdapContext ctx = LdapConnector.ldapContext();
		SearchControls ctls = new SearchControls();
		ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String searchBase = "OU=nerin";
		String FILTER = "CN=" + userName;
		@SuppressWarnings("rawtypes")
		NamingEnumeration list = null;
		list = ctx.search(searchBase, FILTER, ctls);
		Map data = null;
		if (list.hasMore()) {
			data = new HashMap();
			SearchResult sr = (SearchResult) list.next();
			Attributes atts = sr.getAttributes();
			data.put("name", atts.get("name").get().toString());
			Attribute atn = atts.get("name");
			Attribute atp = atts.get("pwdlastset");
			data.put("passWord", atp.get().toString());
		} else {

		}
		return data;
	}

	public static boolean verifyLdapUserOnly(String username, String password){
		boolean rs = false;
		try {
			LdapConnector.ldapContext(username+"@nims", password);
			rs = true;
		} catch (Exception e) {
			rs = false;
		}
		return rs;
	}

//	public static void main(String[] args) {
//		try {
//			//System.out.println("Result:" + verifyLdapUser("N100232", ""));
//			//System.out.println(verifyLdapUserOnly("administrator", ""));
//			//System.out.println(verifyLdapUserOnly("administrator", "password_ad12!@#"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
