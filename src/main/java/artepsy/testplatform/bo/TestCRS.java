package artepsy.testplatform.bo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
@NoArgsConstructor
@AllArgsConstructor
public class TestCRS extends TestAbstract {


    public Map<Integer,Integer> scores = new HashMap<>();
    public Integer acordParental;
    public Integer apropiereParentala;
    public Integer suportParental;
    public Integer aprobareaPartenerului;
    public Integer subminareParentala;
    public Integer expunereLaConflict;



}
