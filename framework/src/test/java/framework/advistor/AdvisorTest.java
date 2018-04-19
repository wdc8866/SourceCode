/**
 * 
 */
package framework.advistor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yqjr.modules.user.model.TransModel;
import com.yqjr.modules.user.service.UserService;

/**
 * ClassName: AdvisorTest <br>
 * Description: TODO <br>
 * Create By: admin <br>
 * Create Date: 2017年7月10日 下午6:30:40 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class AdvisorTest {

	@Autowired
	private UserService userService;

	@Test
	public void test() {
		TransModel transModel = new TransModel();
		transModel.setTransKey("1112");
		userService.test(transModel);
	}

}
