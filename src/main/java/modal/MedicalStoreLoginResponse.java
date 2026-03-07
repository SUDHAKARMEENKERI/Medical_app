package modal;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MedicalStoreLoginResponse {

    private String token;
    private String tokenType;

    // Lombok @Getter and @Setter used
}