/**
 * 
 */
package framework.wsclient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yqjr.framework.component.esb.IReplyParser;
import com.yqjr.framework.component.esb.Reply;
import com.yqjr.framework.component.esb.WebServiceClient;

import framework.wsclient.dto.BankNoInquiry;
import framework.wsclient.dto.BankNoInquiryRequestDTO;
import framework.wsclient.dto.PacpHeader;

/**
 * ClassName: EsbClientTest <br>
 * Description: TODO <br>
 * Create By: admin <br>
 * Create Date: 2017年6月2日 下午3:16:03 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class EsbClientTest {

	@Test
	public void test() {
		PacpHeader header = new PacpHeader();
		header.setTxnRefNo("1231231");
		header.setTxnDate("txndate");
		header.setChannel("FMK");
		BankNoInquiryRequestDTO dto = new BankNoInquiryRequestDTO();
		dto.setPayeeAcctNo("F21123123");
		dto.setPayeeBankName("BANK");
		dto.setPayeeName("S212312312");
		BankNoInquiry bankNoInquiry = WebServiceClient.getInstance().getWsClient("pacp.005", BankNoInquiry.class,new IReplyParser() {
			
			@Override
			public Reply parseReply(Object response) {
				String replyCode = "0";
				String replyText= "交易成功";
				Reply reply = new Reply(replyCode,replyText,Reply.Status.OK);
				return reply;
			}
		});
		bankNoInquiry.bankNoInquiryRequest(header, dto);
		try {
			Thread.sleep(60 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
