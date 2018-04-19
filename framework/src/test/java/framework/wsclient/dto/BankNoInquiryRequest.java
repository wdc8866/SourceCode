
package framework.wsclient.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * anonymous complex type的 Java 类。
 * 
 * <p>
 * 以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://header.pacp.faf.com}PacpHeader"/>
 *         &lt;element name="body" type="{http://dto.pacp.faf.com}BankNoInquiryRequestDTO"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "header", "body" })
@XmlRootElement(name = "bankNoInquiryRequest", namespace = "http://service.pacp.faf.com")
public class BankNoInquiryRequest {

	@XmlElement(namespace = "http://service.pacp.faf.com", required = true)
	protected PacpHeader header;
	@XmlElement(namespace = "http://service.pacp.faf.com", required = true)
	protected BankNoInquiryRequestDTO body;

	/**
	 * 获取header属性的值。
	 * 
	 * @return possible object is {@link PacpHeader }
	 * 
	 */
	public PacpHeader getHeader() {
		return header;
	}

	/**
	 * 设置header属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link PacpHeader }
	 * 
	 */
	public void setHeader(PacpHeader value) {
		this.header = value;
	}

	/**
	 * 获取body属性的值。
	 * 
	 * @return possible object is {@link BankNoInquiryRequestDTO }
	 * 
	 */
	public BankNoInquiryRequestDTO getBody() {
		return body;
	}

	/**
	 * 设置body属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link BankNoInquiryRequestDTO }
	 * 
	 */
	public void setBody(BankNoInquiryRequestDTO value) {
		this.body = value;
	}

}
