package modal;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponse {

    private Long id;
    private String name;
    private Long storeId;
    private String phone;
    private String storeMobile;
    private String email;
    private String gender;
    private String age;
    private String doctorName;
    private String referredBy;
    private BigDecimal spent;
    private LocalDate visited;

    // Lombok @Getter and @Setter used
}