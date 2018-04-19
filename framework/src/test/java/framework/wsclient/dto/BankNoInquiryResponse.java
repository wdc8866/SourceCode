
package framework.wsclient.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bankNoInquiryReturn" type="{http://dto.pacp.faf.com}BankNoInquiryResponseReurn"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "bankNoInquiryReturn"
})
@XmlRootElement(name = "bankNoInquiryResponse", namespace = "http://service.pacp.faf.com")
public class BankNoInquiryResponse {

    @XmlElement(namespace = "http://service.pacp.faf.com", required = true)
    protected BankNoInquiryResponseReurn bankNoInquiryReturn;

    /**
     * 获取bankNoInquiryReturn属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BankNoInquiryResponseReurn }
     *     
     */
    public BankNoInquiryResponseReurn getBankNoInquiryReturn() {
        return bankNoInquiryReturn;
    }

    /**
     * 设置bankNoInquiryReturn属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BankNoInquiryResponseReurn }
     *     
     */
    public void setBankNoInquiryReturn(BankNoInquiryResponseReurn value) {
        this.bankNoInquiryReturn = value;
    }

}
