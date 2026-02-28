package modal;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "medical_store_list")
@Getter
@Setter
public class MedicalStoreRegistrationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String ownerName;

    @NotBlank
    private String storeName;

    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "storeMobile must be 10 digits")
    @JsonAlias("mobile")
    @Column(name = "store_mobile")
    private String storeMobile;

    @Column(name = "mobile")
    private String mobile;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String licenseNo;

    @NotBlank
    private String role;

    @NotBlank
    private String gstinNumber;

    @NotBlank
    private String pharmacyCode;

    @NotBlank
    private String address;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Transient
    private String confirmPassword;

    @Transient
    private Boolean agreeTerms;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreMobile() {
        return storeMobile;
    }

    public void setStoreMobile(String storeMobile) {
        this.storeMobile = storeMobile;
        this.mobile = storeMobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
        this.storeMobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGstinNumber() {
        return gstinNumber;
    }

    public void setGstinNumber(String gstinNumber) {
        this.gstinNumber = gstinNumber;
    }

    public String getPharmacyCode() {
        return pharmacyCode;
    }

    public void setPharmacyCode(String pharmacyCode) {
        this.pharmacyCode = pharmacyCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Boolean getAgreeTerms() {
        return agreeTerms;
    }

    public void setAgreeTerms(Boolean agreeTerms) {
        this.agreeTerms = agreeTerms;
    }

    @PrePersist
    @PreUpdate
    private void syncMobileColumns() {
        if (isBlank(storeMobile) && !isBlank(mobile)) {
            storeMobile = mobile;
        }
        if (isBlank(mobile) && !isBlank(storeMobile)) {
            mobile = storeMobile;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

}