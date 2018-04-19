/**
 * 
 */
package framework.dict;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yqjr.framework.component.dict.DictLoader;
import com.yqjr.modules.dict.model.DictModel;

/**
 * ClassName: DictTest <br>
 * Description: TODO <br>
 * Create By: admin <br>
 * Create Date: 2017年5月31日 下午6:31:45 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class DictTest {

	@Test
	public void test() {
		DictLoader dictLoader = DictLoader.getInstance();
		// 根据指定value获取值
		System.out.println(dictLoader.getLabel("test_dict", "1"));
		System.out.println(dictLoader.getLabel("test_dict", "2"));
		System.out.println(dictLoader.getLabel("test_dict", "3"));
		System.out.println(dictLoader.getLabel("test_dict", "4"));
		// 根据指定类型获取数据字典
		for (DictModel dict : dictLoader.getDicts("test_dict")) {
			System.out.println(dict.getLabel());
		}
	}

}
