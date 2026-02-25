package modal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalStoreLoginResponse {

    private String message;
    private String loginAs;
    private Long storeId;
    private String storeName;
    private String ownerName;
    private String storeMobile;
    private String email;
    private String licenseNo;
    private String role;
    private String gstinNumber;
    private String pharmacyCode;
    private String address;

    // Lombok @Getter and @Setter used
}