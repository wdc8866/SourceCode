/**
 * 
 */
package com.yqjr.framework.component.sequence;

import com.yqjr.framework.datatype.Date;

/**
 * ClassName: ISequenceManager <br>
 * Description: 序列生成接口 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月19日 下午3:28:28 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public interface ISequenceManager {

	/**
	 * Description: 生成long型序列 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月19日 下午3:34:50
	 *
	 * @param seqName
	 *            序列名称
	 * @return
	 */
	public long generateLongSeq(String seqName);

	/**
	 * Description: 生成int型序列 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月12日 下午3:40:39
	 *
	 * @param seqName
	 *            序列名称
	 * @return
	 */
	public int generateIntSeq(String seqName);

	/**
	 * Description: 生成string型序列 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月19日 下午3:36:03
	 *
	 * @param seqName
	 *            序列名称
	 * @param len
	 *            生成长度
	 * @return
	 */
	public String generateStringSeq(String seqName, int len);

	/**
	 * Description: 生成交易流水,流水号格式为 yyyyMMdd+长度为len的序号 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月19日 下午3:36:36
	 *
	 * @param seqName
	 *            序列名称
	 * @param len
	 *            生成长度
	 * @return
	 */
	public String generateSerialNo(String seqName, int len);

	/**
	 * Description: 根据指定日期生成流水号 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月19日 下午3:39:25
	 *
	 * @param seqName
	 * @param date
	 * @param len
	 * @return
	 */
	public String generateSerialNo(String seqName, Date date, int len);

	/**
	 * Description: 生成UUID <br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月19日 下午3:38:08
	 *
	 * @return
	 */
	public String generateUUID();

}
