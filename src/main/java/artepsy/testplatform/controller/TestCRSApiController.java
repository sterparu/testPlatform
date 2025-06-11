package artepsy.testplatform.controller;

import artepsy.testplatform.datamodels.request.TestCRSRequest;
import artepsy.testplatform.service.PDFService;
import artepsy.testplatform.service.TestCRSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api")
@Slf4j
public class TestCRSApiController {

    @Autowired
    private PDFService pdfService;

    @Autowired
    private TestCRSService testCRSService;

    @PostMapping("/sendResults")
    public String sendResults(@ModelAttribute TestCRSRequest request, RedirectAttributes redirectAttributes) {
        log.info("Received request to send results for email: {}", request.getMailClient());
        log.info("Request scores: {}", request.getScores());
        try {
            pdfService.generateAndSendPDF(request);
            redirectAttributes.addFlashAttribute("emailStatus", "Email trimis cu succes!");
        } catch (Exception e) {
            log.error("Error sending results", e);
            redirectAttributes.addFlashAttribute("emailStatus", "Eroare la trimiterea email-ului: " + e.getMessage());
        }
        return "redirect:/resultTestCRS";
    }
} 