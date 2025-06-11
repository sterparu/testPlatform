package artepsy.testplatform.controller;

import artepsy.testplatform.bo.TestCRS;
import artepsy.testplatform.bo.TestCRSForm;
import artepsy.testplatform.datamodels.request.TestCRSRequest;
import artepsy.testplatform.datamodels.response.TestCRSResponse;
import artepsy.testplatform.service.TestCRSService;
import artepsy.testplatform.service.PDFService;
import artepsy.testplatform.service.EmailService;
import artepsy.testplatform.utils.QuestionAnswerInt;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final PDFService pdfService;
    private final EmailService emailService;

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

        Map<Integer, Integer> answerMap = answers.stream()
                .filter(qa -> qa.getAnswer() != null)
                .collect(Collectors.toMap(
                        QuestionAnswerInt::getQuestionNumber,
                        QuestionAnswerInt::getAnswer
                ));
        TestCRSRequest request = new TestCRSRequest(formCRS.getNumeClient(), formCRS.getMailClient(), answerMap);
        TestCRSResponse response = testCRSService.scoreTestCRS(request);
        response.setScores(answerMap);
        redirectAttributes.addFlashAttribute("answers", response);

        return "redirect:/resultTestCRS";
    }

    @PostMapping("/sendEmail")
    public String sendEmail(@ModelAttribute("answers") TestCRSResponse answers, Model model) {
        try {
            TestCRSRequest request = new TestCRSRequest(answers.getNumeClient(), answers.getMailClient(), answers.getScores());
            pdfService.generateAndSendPDF(request);
            model.addAttribute("answers", answers);
            List<QuestionAnswerInt> scoresList  = new ArrayList<>();

            for( int i = 1; i <= 28; i++){
                scoresList.add(new QuestionAnswerInt(i, null));
            }
            model.addAttribute("formCRS", new TestCRSForm(scoresList, 0, 0, 0, 0, 0, 0));
            return "formCRS";// Ensure data is still in the model
        } catch (Exception e) {
            log.error("Error sending email", e);
            model.addAttribute("error", "Error sending email: " + e.getMessage());
            model.addAttribute("answers", answers); // Ensure data is still in the model
            return "formCRS";
        }
    }
}
