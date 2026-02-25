package modal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "billing_line_item")
public class BillingLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String medicineName;
    private String brand;
    private String composition;
    private String batch;
    private int qty;
    private double unitPrice;
    private double total;
    // Lombok @Getter and @Setter used

}
