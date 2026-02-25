package modal;

import java.util.List;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "billing")
@Getter
@Setter
public class BillingResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;
    private String invoiceNumber;
    private String customerName;
    private String customerPhone;
    private String patientGender;
    private String patientAge;
    private String doctorName;
    private String referredBy;
    private double amount;
    private int itemCount;
    private String date;
    private String storeMobile;
    private Long storeId;
    private String email;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "bill_id")
    private List<BillingLineItem> lineItems;
    // Lombok @Getter and @Setter used
}