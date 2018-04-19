
package framework.wsclient.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>BankNoInquiryResponseReurn complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="BankNoInquiryResponseReurn">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://header.pacp.faf.com}PacpHeader"/>
 *         &lt;element name="body" type="{http://dto.pacp.faf.com}BankNoInquiryResponseDTO"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BankNoInquiryResponseReurn", propOrder = {
    "header",
    "body"
})
public class BankNoInquiryResponseReurn {

    @XmlElement(required = true)
    protected PacpHeader header;
    @XmlElement(required = true)
    protected BankNoInquiryResponseDTO body;

    /**
     * 获取header属性的值。
     * 
     * @return
     *     possible object is
     *     {@link PacpHeader }
     *     
     */
    public PacpHeader getHeader() {
        return header;
    }

    /**
     * 设置header属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link PacpHeader }
     *     
     */
    public void setHeader(PacpHeader value) {
        this.header = value;
    }

    /**
     * 获取body属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BankNoInquiryResponseDTO }
     *     
     */
    public BankNoInquiryResponseDTO getBody() {
        return body;
    }

    /**
     * 设置body属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BankNoInquiryResponseDTO }
     *     
     */
    public void setBody(BankNoInquiryResponseDTO value) {
        this.body = value;
    }

}
