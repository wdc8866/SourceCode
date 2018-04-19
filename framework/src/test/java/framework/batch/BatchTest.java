/**
 * 
 */
package framework.batch;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yqjr.modules.user.entity.User;
import com.yqjr.modules.user.service.UserService;

/**
 * ClassName: BatchTest <br>
 * Description: 批量处理测试 <br>
 * Create By: admin <br>
 * Create Date: 2017年7月6日 上午9:04:20 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class BatchTest {

	@Autowired
	private UserService userService;

	@Test
	public void test() {

		List<User> users = new ArrayList<User>();
		for (int i = 0; i < 100; i++) {
			User user = new User();
			user.setId(new Long(1000 + i));
			if (i == 20) {
				user.setUserName(
						"---------------------------------------------------------------------------------------------------------");
			}
			users.add(user);
		}

		userService.batchSave(users);

	}

}
