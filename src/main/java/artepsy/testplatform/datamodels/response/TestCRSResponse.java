package artepsy.testplatform.datamodels.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestCRSResponse {
    private String numeClient;
    private String mailClient;
    private Map<Integer, Integer> scores;
    private int acordParental;
    private int apropiereParentala;
    private int suportParental;
    private int aprobareaPartenerului;
    private int subminareParentala;
    private int expunereLaConflict;

    public TestCRSResponse(String numeClient, String mailClient) {
        this.numeClient = numeClient;
        this.mailClient = mailClient;
    }
}
