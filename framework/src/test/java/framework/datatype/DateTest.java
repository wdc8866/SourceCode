/**
 * 
 */
package framework.datatype;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yqjr.framework.datatype.Date;

/**
 * ClassName: DateTest <br>
 * Description: 日期类型单元测试 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月18日 下午4:40:29 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class DateTest {

	@Test
	public void test() {

		Date date = new Date();
		System.out.println(date);

		Date specialDate = new Date("pay_sysdate", "mdate", "id = 1");
		System.out.println(specialDate);

	}

}
