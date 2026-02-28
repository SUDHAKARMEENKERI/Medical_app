package modal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalStorePatchRequest {
    private String ownerName;
    private String storeName;
    private String storeMobile;
    private String mobile;
    private String email;
    private String licenseNo;
    private String gstinNumber;
    private String pharmacyCode;
    private String address;
    private String password;
    // Add more fields as needed 
}