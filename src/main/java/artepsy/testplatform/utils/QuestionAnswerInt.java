package artepsy.testplatform.utils;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnswerInt {
    private Integer questionNumber;
    @NotNull(message = "Trebuie să selectați un răspuns pentru fiecare întrebare.")
    private Integer answer;
}
