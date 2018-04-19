/**
 * 
 */
package framework.watcher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yqjr.framework.component.config.Configuration;
import com.yqjr.framework.component.log.Logger;

/**
 * ClassName: WatcherTest <br>
 * Description: watcher测试 <br>
 * Create By: admin <br>
 * Create Date: 2017年7月5日 上午11:48:12 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class WatcherTest {

	private Logger logger = Logger.getLogger();

	@Test
	public void test() {

		while (true) {
			logger.info(Configuration.getConfig().getStringValue("framework.timemode"));
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
