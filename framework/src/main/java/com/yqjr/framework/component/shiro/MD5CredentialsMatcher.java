package com.yqjr.framework.component.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

import com.yqjr.framework.utils.MD5Utils;

/**
 * 
 * ClassName: MD5CredentialsMatcher <br>
 * Description: 自定义MD5校验规则 <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年5月5日 上午10:27:29 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
public class MD5CredentialsMatcher extends SimpleCredentialsMatcher {

	
	@Override  
    public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {  
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;  

        Object tokenCredentials = encrypt(String.valueOf(token.getPassword()));  
        Object accountCredentials = getCredentials(info);  
        //将密码加密与系统加密后的密码校验，内容一致就返回true,不一致就返回false  
        return equals(tokenCredentials, accountCredentials);  
    }  

    //将传进来密码加密方法  
    private String encrypt(String data) {  
        return MD5Utils.encode(data);  
    }  

}
