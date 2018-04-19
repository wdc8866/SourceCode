/**
 * 
 */
package framework.codegen;

import org.junit.Test;

import com.yqjr.framework.component.mybatis.Generator;

/**
 * ClassName: CodeGenTest <br>
 * Description: TODO <br>
 * Create By: admin <br>
 * Create Date: 2017年6月23日 下午4:06:45 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class CodeGenTest {

	@Test
	public void test() {
		String modelPackage = "com.yqjr.modules.test";
		String className = "Test";
		String configFileName = "xml/demo.xml";
		Generator.generate(modelPackage, className, configFileName);
	}

}
