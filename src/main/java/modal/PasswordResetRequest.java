package modal;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String storeMobile;
    @NotBlank
    private String password;
    @NotBlank
    private String confirmPassword;
    // Optionally, add a security question/answer or OTP for real-world scenarios
}
