/**
 * 
 */
package framework.sequence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yqjr.framework.component.sequence.AbstractSequenceManager;
import com.yqjr.framework.component.sequence.ISequenceManager;

/**
 * ClassName: CustomSequenceTest <br>
 * Description: TODO <br>
 * Create By: admin <br>
 * Create Date: 2017年5月31日 上午8:26:31 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class CustomSequenceTest {

	@Test
	public void test() {
		for (int i = 0; i < 30; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					ISequenceManager sequenceManager = AbstractSequenceManager
							.getSequenceManager(AbstractSequenceManager.CUSTOM_SEQUENCE);
					for (int i = 0; i < 100; i++) {
						System.out.println(
								Thread.currentThread().getId() + "===" + sequenceManager.generateLongSeq("user"));
					}
				}
			}).start();
		}
		try {
			Thread.sleep(10000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
