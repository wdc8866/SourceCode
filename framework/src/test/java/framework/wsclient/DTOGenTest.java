/**
 * 
 */
package framework.wsclient;

import org.junit.Test;

import com.yqjr.framework.component.esb.DTOGenerator;

/**
 * ClassName: DTOGenTest <br>
 * Description: TODO <br>
 * Create By: admin <br>
 * Create Date: 2017年6月2日 下午2:30:14 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class DTOGenTest {

	@Test
	public void test() {

		String packageName = "com.yqjr.modules.dto";
		String wsdl = "wsdl/wstest_1.wsdl";

		DTOGenerator.generate(packageName, wsdl);

	}

}
