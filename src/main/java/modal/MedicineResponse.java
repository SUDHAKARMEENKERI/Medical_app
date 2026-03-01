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
    private String formulation;
    private String strength;
    private String mfgDate;
    private String packSize;
    private String boxQuantity;
    private String lowAlert;
    private String rackShelf;
    private String buyPrice;
    private String boxBuyPrice;
    private String boxSellPrice;
    private String gst;
    private String manufacturer;
    private String supplier;
    private String batchSize;

    // Lombok @Getter and @Setter used
}
