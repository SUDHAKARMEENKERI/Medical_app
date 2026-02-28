package modal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "medicine_list")
@Getter
@Setter
public class MedicineRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    @NotBlank
    private String composition;

    @NotBlank
    private String category;

    @NotBlank
    private String batch;

    @NotNull
    private LocalDate expiry;

    @NotNull
    @Min(value = 0, message = "quantity must be 0 or greater")
    private Integer quantity;

    @NotNull
    @DecimalMin(value = "0.0", message = "price must be 0 or greater")
    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "storeMobile must be 10 digits")
    @Column(name = "store_mobile")
    private String storeMobile;

    @NotNull
    @Column(name = "store_id")
    private Long storeId;

    @NotBlank
    @Email
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public LocalDate getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDate expiry) {
        this.expiry = expiry;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStoreMobile() {
        return storeMobile;
    }

    public void setStoreMobile(String storeMobile) {
        this.storeMobile = storeMobile;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFormulation() { return formulation; }
    public void setFormulation(String formulation) { this.formulation = formulation; }
    public String getStrength() { return strength; }
    public void setStrength(String strength) { this.strength = strength; }
    public String getMfgDate() { return mfgDate; }
    public void setMfgDate(String mfgDate) { this.mfgDate = mfgDate; }
    public String getPackSize() { return packSize; }
    public void setPackSize(String packSize) { this.packSize = packSize; }
    public String getBoxQuantity() { return boxQuantity; }
    public void setBoxQuantity(String boxQuantity) { this.boxQuantity = boxQuantity; }
    public String getLowAlert() { return lowAlert; }
    public void setLowAlert(String lowAlert) { this.lowAlert = lowAlert; }
    public String getRackShelf() { return rackShelf; }
    public void setRackShelf(String rackShelf) { this.rackShelf = rackShelf; }
    public String getBuyPrice() { return buyPrice; }
    public void setBuyPrice(String buyPrice) { this.buyPrice = buyPrice; }
    public String getBoxBuyPrice() { return boxBuyPrice; }
    public void setBoxBuyPrice(String boxBuyPrice) { this.boxBuyPrice = boxBuyPrice; }
    public String getBoxSellPrice() { return boxSellPrice; }
    public void setBoxSellPrice(String boxSellPrice) { this.boxSellPrice = boxSellPrice; }
    public String getGst() { return gst; }
    public void setGst(String gst) { this.gst = gst; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }
    public String getBatchSize() { return batchSize; }
    public void setBatchSize(String batchSize) { this.batchSize = batchSize; }
}
