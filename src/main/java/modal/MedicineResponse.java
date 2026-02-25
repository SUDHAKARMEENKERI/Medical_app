package modal;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicineResponse {

    private Long id;
    private String name;
    private String brand;
    private String composition;
    private String category;
    private String batch;
    private LocalDate expiry;
    private Integer quantity;
    private BigDecimal price;
    private String storeMobile;
    private Long storeId;
    private String email;

    // Lombok @Getter and @Setter used
}
