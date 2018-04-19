/**
 * 
 */
package framework.webservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yqjr.framework.component.webservice.Publisher;

/**
 * ClassName: PublishTest <br>
 * Description: TODO <br>
 * Create By: admin <br>
 * Create Date: 2017年5月19日 下午6:16:49 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class PublishTest {

	@Test
	public void test() {
		Publisher.getInstance().publish();

		try {
			Thread.sleep(5 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
