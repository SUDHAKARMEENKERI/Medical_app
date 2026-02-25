package modal;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerPatchRequest {

    private BigDecimal spent;
    private LocalDate visited;

    // Lombok @Getter and @Setter used
}
