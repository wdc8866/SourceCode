package framework.wsclient.dto;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.0.0
 * 2017-06-02T15:08:50.865+08:00
 * Generated source version: 3.0.0
 * 
 */
@WebServiceClient(name = "BankNoInquiryService", 
                  wsdlLocation = "/E:/03-source/yqjrframework_workspace/framework/target/test-classes/BankNoInquiry.wsdl",
                  targetNamespace = "http://service.pacp.faf.com") 
public class BankNoInquiryService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://service.pacp.faf.com", "BankNoInquiryService");
    public final static QName BankNoInquiry = new QName("http://service.pacp.faf.com", "BankNoInquiry");
    static {
        URL url = BankNoInquiryService.class.getResource("/E:/03-source/yqjrframework_workspace/framework/target/test-classes/BankNoInquiry.wsdl");
        if (url == null) {
            url = BankNoInquiryService.class.getClassLoader().getResource("/E:/03-source/yqjrframework_workspace/framework/target/test-classes/BankNoInquiry.wsdl");
        } 
        if (url == null) {
            java.util.logging.Logger.getLogger(BankNoInquiryService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "/E:/03-source/yqjrframework_workspace/framework/target/test-classes/BankNoInquiry.wsdl");
        }       
        WSDL_LOCATION = url;
    }

    public BankNoInquiryService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public BankNoInquiryService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BankNoInquiryService() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns BankNoInquiry
     */
    @WebEndpoint(name = "BankNoInquiry")
    public BankNoInquiry getBankNoInquiry() {
        return super.getPort(BankNoInquiry, BankNoInquiry.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns BankNoInquiry
     */
    @WebEndpoint(name = "BankNoInquiry")
    public BankNoInquiry getBankNoInquiry(WebServiceFeature... features) {
        return super.getPort(BankNoInquiry, BankNoInquiry.class, features);
    }

}
