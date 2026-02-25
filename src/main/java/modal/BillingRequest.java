package modal;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingRequest {
    private String customerName;
    private String customerPhone;
    private String patientGender;
    private String patientAge;
    private String doctorName;
    private String referredBy;
    private double amount;
    private int itemCount;
    private List<BillingLineItem> lineItems;
    private String date;
    private String invoiceNumber;

    private Long storeId;
    private String storeMobile;
    private String email;

    // Lombok @Getter and @Setter used
}
