package artepsy.testplatform.datamodels.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TestCRSResponse {

    private String numeClient;
    private String mailClient;
    public Integer acordParental;
    public Integer apropiereParentala;
    public Integer suportParental;
    public Integer aprobareaPartenerului;
    public Integer subminareParentala;
    public Integer expunereLaConflict;

    public TestCRSResponse(String numeClient, String mailClient) {
        this.numeClient = numeClient;
        this.mailClient = mailClient;
    }
}
