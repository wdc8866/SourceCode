/**
 * 
 */
package modules.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yqjr.modules.user.service.UserService;

/**
 * ClassName: UserTest <br>
 * Description: TODO <br>
 * Create By: admin <br>
 * Create Date: 2017年6月21日 上午9:33:36 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class UserTest {

	@Autowired
	private UserService userService;

	@Test
	public void test() {

		// spring 10->578   100->2476 1000->23471  10000->207609 209870
		// reflect 10->602   100->2660 1000->21840  10000->209404
		// 直接调用 10->568 100->2447 1000->20337 10000->218700
		
		
		long begin = System.currentTimeMillis();
		for (int k = 0; k < 10000; k++) {
			//userService.extMethod();
		}
		System.out.println(System.currentTimeMillis()-begin);

		try

		{
			Thread.sleep(5 * 60 * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
