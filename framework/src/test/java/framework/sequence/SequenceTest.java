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
 * ClassName: SequenceTest <br>
 * Description: 序列测试 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月26日 下午7:42:40 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class SequenceTest {

	@Test
	public void test() {
		ISequenceManager seqManager = AbstractSequenceManager.getSequenceManager();
		System.out.println(seqManager.generateStringSeq("fmk_test_seq", 5));
		
		System.out.println(seqManager.generateSerialNo("fmk_test_seq", 5));
	}

}
