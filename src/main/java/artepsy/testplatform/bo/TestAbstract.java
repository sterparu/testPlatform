package artepsy.testplatform.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class TestAbstract {
    @NotBlank(message = "Numele este obligatoriu")
    private String numeClient;

    @NotBlank(message = "Emailul este obligatoriu")
    @Email(message = "Emailul nu este valid")
    private String mailClient;

}
