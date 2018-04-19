/**
 * 
 */
package com.yqjr.framework.component.esb;

import java.net.URL;

import org.apache.cxf.tools.wsdlto.WSDLToJava;
import org.springframework.util.Assert;

/**
 * ClassName: DTOGenerator <br>
 * Description: WebService客户端DTO类生成 <br>
 * Create By: admin <br>
 * Create Date: 2017年6月2日 下午2:26:41 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class DTOGenerator {

	public static void generate(String packageName, String wsdl) {

		Assert.hasText(packageName);
		Assert.hasText(wsdl);

		String outputDir = System.getProperty("user.dir") + "\\src\\main\\java";
		URL url = Thread.currentThread().getContextClassLoader().getResource(wsdl);
		String resource = url == null ? wsdl : url.getPath();
		WSDLToJava.main(new String[] { "-client", "-d", outputDir, "-p", packageName, "-encoding", "UTF-8", "-frontend",
				"jaxws21", resource });
	}

}
