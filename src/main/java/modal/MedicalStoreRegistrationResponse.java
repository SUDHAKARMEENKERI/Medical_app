package modal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalStoreRegistrationResponse {

    private Long id;
    private String ownerName;
    private String storeName;
    private String storeMobile;
    private String email;
    private String licenseNo;
    private String role;
    private String gstinNumber;
    private String pharmacyCode;
    private String address;

    // Lombok @Getter and @Setter used
}