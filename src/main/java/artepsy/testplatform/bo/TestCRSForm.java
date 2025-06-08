package artepsy.testplatform.bo;

import artepsy.testplatform.utils.QuestionAnswerInt;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Component
@NoArgsConstructor
@AllArgsConstructor
public class TestCRSForm extends TestAbstract {

    @Valid
    public List<QuestionAnswerInt> scoresList = new ArrayList<>();
    public Integer acordParental;
    public Integer apropiereParentala;
    public Integer suportParental;
    public Integer aprobareaPartenerului;
    public Integer subminareParentala;
    public Integer expunereLaConflict;

}
