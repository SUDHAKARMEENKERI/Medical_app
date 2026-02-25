package modal;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalStoreLoginRequest {

    @NotBlank
    private String storeMobileOrStoreId;

    @NotBlank
    private String password;

    @NotBlank
    private String loginAs;

    // Lombok @Getter and @Setter used
}