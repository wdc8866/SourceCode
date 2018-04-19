/**
 * 
 */
package framework.boot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ClassName: BootTest <br>
 * Description: 框架启动测试 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月3日 上午10:59:43 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class BootTest {

	@Test
	public void test() {
		System.out.println("service startup complete");

		try {
			Thread.sleep(600000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
