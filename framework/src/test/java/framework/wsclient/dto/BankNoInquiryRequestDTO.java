
package framework.wsclient.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>BankNoInquiryRequestDTO complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="BankNoInquiryRequestDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="payeeAcctNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="payeeName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="payeeBankName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BankNoInquiryRequestDTO", propOrder = {
    "payeeAcctNo",
    "payeeName",
    "payeeBankName"
})
public class BankNoInquiryRequestDTO {

    @XmlElement(required = true)
    protected String payeeAcctNo;
    @XmlElement(required = true)
    protected String payeeName;
    @XmlElement(required = true)
    protected String payeeBankName;

    /**
     * 获取payeeAcctNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayeeAcctNo() {
        return payeeAcctNo;
    }

    /**
     * 设置payeeAcctNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayeeAcctNo(String value) {
        this.payeeAcctNo = value;
    }

    /**
     * 获取payeeName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayeeName() {
        return payeeName;
    }

    /**
     * 设置payeeName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayeeName(String value) {
        this.payeeName = value;
    }

    /**
     * 获取payeeBankName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayeeBankName() {
        return payeeBankName;
    }

    /**
     * 设置payeeBankName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayeeBankName(String value) {
        this.payeeBankName = value;
    }

}
