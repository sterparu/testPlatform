package artepsy.testplatform.service;

import artepsy.testplatform.bo.TestCRS;
import artepsy.testplatform.datamodels.request.TestCRSRequest;
import artepsy.testplatform.datamodels.response.TestCRSResponse;
import artepsy.testplatform.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TestCRSService {
    private final TestCRS testCRS;

    public TestCRSResponse scoreTestCRS(TestCRSRequest request) {
        TestCRSResponse response = new TestCRSResponse(request.getNumeClient(), request.getMailClient());
        int acordParental = 0;
        int apropiereParentala = 0;
        int suportParental = 0;
        int aprobareaPartenerului = 0;
        int subminareParentala = 0;
        int expunereLaConflict = 0;
        final List<Integer> acordParentalList = new ArrayList<>(List.of(5, 7, 11));
        List<Integer> apropiereParentalaList = new ArrayList<>(List.of(2, 13, 19, 23));
        List<Integer> suportParentalList = new ArrayList<>(List.of(3, 6, 15, 20, 21, 22));
        List<Integer> aprobareaParteneruluiList = new ArrayList<>(List.of(1, 4, 10, 14, 18));
        List<Integer> subminareParentalaList = new ArrayList<>(List.of(8, 9, 12, 16, 17));
        List<Integer> expunereLaConflictList = new ArrayList<>(List.of(24, 25, 26, 27, 28));

        for (Map.Entry<Integer, Integer> entry : request.getScores().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .toList()) {
            if (acordParentalList.contains(entry.getKey())) {
                acordParental = acordParental + Utils.reverseValue(entry.getValue(), 0, 6);
            }
            if (apropiereParentalaList.contains(entry.getKey())) {
                apropiereParentala = apropiereParentala + entry.getValue();
            }
            if (suportParentalList.contains(entry.getKey())) {
                suportParental = suportParental + entry.getValue();
            }
            if (aprobareaParteneruluiList.contains(entry.getKey())) {
                aprobareaPartenerului = aprobareaPartenerului + entry.getValue();
            }
            if (subminareParentalaList.contains(entry.getKey())) {
                subminareParentala = subminareParentala + entry.getValue();
            }
            if (expunereLaConflictList.contains(entry.getKey())) {
                expunereLaConflict = expunereLaConflict + entry.getValue();
            }
        }
        response.setAcordParental(acordParental);
        response.setApropiereParentala(apropiereParentala);
        response.setAprobareaPartenerului(aprobareaPartenerului);
        response.setSuportParental(suportParental);
        response.setSubminareParentala(subminareParentala);
        response.setExpunereLaConflict(expunereLaConflict);

        return response;
    }
}
