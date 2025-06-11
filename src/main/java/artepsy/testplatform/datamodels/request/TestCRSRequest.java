package artepsy.testplatform.datamodels.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestCRSRequest {
    private String numeClient;
    private String mailClient;
    private Map<Integer, Integer> scores;
}
