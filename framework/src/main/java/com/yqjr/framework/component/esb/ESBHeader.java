/**
 * 
 */
package com.yqjr.framework.component.esb;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * ClassName: ESBHeader <br>
 * Description: ESB报文头 <br>
 * Create By: admin <br>
 * Create Date: 2017年6月2日 下午1:39:08 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class ESBHeader {

	private static final String NAMESPACE = "http://www.faf.com.cn/osb/common/header/in";

	private String msgRef;

	private String channel;

	private String serviceCode;

	private String externalReferenceNo;

	private String replyCode = "";

	private String replyText = "";

	/**
	 * @return the msgRef
	 */
	public String getMsgRef() {
		return msgRef;
	}

	/**
	 * @param msgRef
	 *            the msgRef to set
	 */
	public void setMsgRef(String msgRef) {
		this.msgRef = msgRef;
	}

	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * @param channel
	 *            the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	/**
	 * @return the serviceCode
	 */
	public String getServiceCode() {
		return serviceCode;
	}

	/**
	 * @param serviceCode
	 *            the serviceCode to set
	 */
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	/**
	 * @return the externalReferenceNo
	 */
	public String getExternalReferenceNo() {
		return externalReferenceNo;
	}

	/**
	 * @param externalReferenceNo
	 *            the externalReferenceNo to set
	 */
	public void setExternalReferenceNo(String externalReferenceNo) {
		this.externalReferenceNo = externalReferenceNo;
	}

	/**
	 * @return the replyCode
	 */
	public String getReplyCode() {
		return replyCode;
	}

	/**
	 * @param replyCode
	 *            the replyCode to set
	 */
	public void setReplyCode(String replyCode) {
		this.replyCode = replyCode;
	}

	/**
	 * @return the replyText
	 */
	public String getReplyText() {
		return replyText;
	}

	/**
	 * @param replyText
	 *            the replyText to set
	 */
	public void setReplyText(String replyText) {
		this.replyText = replyText;
	}

	/**
	 * Description: 生成ESBHeader <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月2日 下午2:09:54
	 *
	 * @return
	 */
	public SoapHeader getEsbHeader() {

		// 构建ESBHeader
		Document doc = DOMUtils.createDocument();
		Element root = doc.createElementNS(NAMESPACE, "esbHeader");

		// 交易参考号
		Element msgRef = doc.createElementNS(NAMESPACE, "msgRef");
		msgRef.setTextContent(this.msgRef);
		root.appendChild(msgRef);
		// 交易渠道
		Element channel = doc.createElementNS(NAMESPACE, "channel");
		channel.setTextContent(this.channel);
		root.appendChild(channel);
		// 交易码
		Element serviceCode = doc.createElementNS(NAMESPACE, "serviceCode");
		serviceCode.setTextContent(this.serviceCode);
		root.appendChild(serviceCode);
		// 交易流水号
		Element externalReferenceNo = doc.createElementNS(NAMESPACE, "externalReferenceNo");
		externalReferenceNo.setTextContent(this.externalReferenceNo);
		root.appendChild(externalReferenceNo);
		// 应答码
		Element replyCode = doc.createElementNS(NAMESPACE, "replyCode");
		replyCode.setTextContent(this.replyCode);
		root.appendChild(replyCode);
		// 应答信息
		Element replyText = doc.createElementNS(NAMESPACE, "replyText");
		replyText.setTextContent(this.replyText);
		root.appendChild(replyText);

		// 构建soapHeader对象
		QName qname = new QName("esb", NAMESPACE);
		SoapHeader header = new SoapHeader(qname, root);
		return header;
	}

	/**
	 * Description: 解析ESBHeader <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月5日 上午10:33:11
	 *
	 * @param message
	 */
	public void parseEsbHeader(SoapMessage message, ESBHeader esbHeader) {

		// 从应答报文中获取soapheader信息
		QName qname = new QName(NAMESPACE, "esbHeader");
		Header header = message.getHeader(qname);
		Assert.notNull(header);
		Assert.isInstanceOf(Element.class, header.getObject());

		// 遍历esbHeader进行解析
		Element element = (Element) header.getObject();
		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			Assert.hasText(node.getNodeName());
			// 解析replyCode
			if ("replyCode".equals(node.getNodeName())) {
				Assert.hasText(node.getTextContent());
				esbHeader.setReplyCode(node.getTextContent());
			}
			// 解析replyText
			else if ("replyText".equals(node.getNodeName())) {
				Assert.hasText(node.getTextContent());
				esbHeader.setReplyText(node.getTextContent());
			}
		}
	}

	/**
	 * Description: ESBHeader校验 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年7月6日 下午4:13:07
	 */
	public void verify() {
		Assert.hasText(msgRef, "msgRef can't be empty");
		Assert.hasText(channel, "channel can't be empty");
		Assert.hasText(serviceCode, "serviceCode can't be empty");
		Assert.hasText(externalReferenceNo, "externalReferenceNo can't be empty");
	}

}
