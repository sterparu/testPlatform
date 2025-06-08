package artepsy.testplatform.controller;

import artepsy.testplatform.bo.TestCRS;
import artepsy.testplatform.bo.TestCRSForm;
import artepsy.testplatform.datamodels.request.TestCRSRequest;
import artepsy.testplatform.datamodels.response.TestCRSResponse;
import artepsy.testplatform.service.TestCRSService;
import artepsy.testplatform.utils.QuestionAnswerInt;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TestCRSController {

    private final TestCRSService testCRSService;


    @GetMapping("/formCRS")
    public String getForm(Model theModel){
        List<QuestionAnswerInt> scoresList  = new ArrayList<>();

        for( int i = 1; i <= 28; i++){
            scoresList.add(new QuestionAnswerInt(i, null));
        }

        theModel.addAttribute("formCRS", new TestCRSForm(scoresList, 0, 0, 0, 0, 0, 0));
        return "formCRS";
    }

    @GetMapping("/resultTestCRS")
    public String getResult(@ModelAttribute("answers") TestCRSResponse answers, Model model){

        if (answers == null) {
            // no answers in model - redirect to form or handle as needed
            return "redirect:/formCRS";
        }
        model.addAttribute("answers", answers);

        return "resultTestCRS";
    }

    @PostMapping("/scoreTestCRS")
    public String handleFormSubmit(@Valid @ModelAttribute("formCRS") TestCRSForm formCRS, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        List<QuestionAnswerInt> answers = formCRS.getScoresList();

        if (bindingResult.hasErrors()) {
            return "formCRS";
        }

        TestCRSResponse response = new TestCRSResponse();
        Map<Integer, Integer> answerMap = answers.stream()
                .filter(qa -> qa.getAnswer() != null) // optional: skip nulls
                .collect(Collectors.toMap(
                        QuestionAnswerInt::getQuestionNumber,
                        QuestionAnswerInt::getAnswer
                ));
        TestCRSRequest request = new TestCRSRequest(formCRS.getNumeClient(), formCRS.getMailClient(), answerMap);
        response = testCRSService.scoreTestCRS(request);

        redirectAttributes.addFlashAttribute("answers", response);

        return "redirect:/resultTestCRS";
    }

    @PostMapping("/api/scoreTestCRS")
    public TestCRSResponse scoreTestCRSApi(@RequestBody TestCRSRequest request) {
        log.info("Received request to score TestCRS: {}", request);
        return testCRSService.scoreTestCRS(request);
    }
}
