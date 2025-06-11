package artepsy.testplatform.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import artepsy.testplatform.datamodels.request.TestCRSRequest;
import artepsy.testplatform.datamodels.response.TestCRSResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.Rectangle;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PDFService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TestCRSCacheService cacheService;

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(74, 144, 226));
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, new BaseColor(74, 144, 226));
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(44, 62, 80));
    private static final Font BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(74, 144, 226));

    public void generateAndSendPDF(TestCRSRequest request) throws Exception {
        log.info("Trying to get response from cache for email: {}", request.getMailClient());
        TestCRSResponse response = cacheService.getResponse(request.getMailClient());
        if (response == null) {
            log.error("No response found in cache for email: {}", request.getMailClient());
            throw new Exception("Nu s-au găsit rezultatele în cache pentru email-ul: " + request.getMailClient());
        }
        log.info("Found response in cache: {}", response);
        byte[] pdfContent = generateTestResultsPDF(response);
        emailService.sendTestResultsEmail(request.getMailClient(), request.getNumeClient(), pdfContent);
        // După trimiterea email-ului, ștergem din cache
        cacheService.removeResponse(request.getMailClient());
        log.info("Removed response from cache for email: {}", request.getMailClient());
    }

    public byte[] generateTestResultsPDF(TestCRSResponse response) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add title
            Paragraph title = new Paragraph("Rezultate Chestionar CRS", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Add client info
            Paragraph clientInfo = new Paragraph();
            clientInfo.add(new Chunk("Nume: ", BOLD_FONT));
            clientInfo.add(new Chunk(response.getNumeClient(), NORMAL_FONT));
            clientInfo.add(new Chunk("\nEmail: ", BOLD_FONT));
            clientInfo.add(new Chunk(response.getMailClient(), NORMAL_FONT));
            clientInfo.setSpacingAfter(20);
            document.add(clientInfo);

            // Add scores and logo in a table layout
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{2, 2});

            // Left cell: scoruri
            PdfPCell leftCell = new PdfPCell();
            leftCell.setBorder(Rectangle.NO_BORDER);

            Paragraph scores = new Paragraph("Scoruri:", HEADER_FONT);
            scores.setSpacingAfter(10);
            leftCell.addElement(scores);
            addCategoryScoreToCell(leftCell, "Acord parental", response.getAcordParental());
            addCategoryScoreToCell(leftCell, "Apropiere parentală", response.getApropiereParentala());
            addCategoryScoreToCell(leftCell, "Suport parental", response.getSuportParental());
            addCategoryScoreToCell(leftCell, "Aprobarea partenerului", response.getAprobareaPartenerului());
            addCategoryScoreToCell(leftCell, "Subminare parentală", response.getSubminareParentala());
            addCategoryScoreToCell(leftCell, "Expunere la conflict", response.getExpunereLaConflict());
            table.addCell(leftCell);

            // Right cell: logo
            PdfPCell rightCell = new PdfPCell();
            rightCell.setBorder(Rectangle.NO_BORDER);
            try {
                Image logo = Image.getInstance(getClass().getClassLoader().getResource("static/logo_artepsy.png"));
                float width = 180;
                float height = 180;
                logo.scaleToFit(width, height);
                logo.setAlignment(Image.ALIGN_CENTER);
                rightCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                rightCell.addElement(logo);
            } catch (Exception e) {
                // Ignore image errors
            }
            table.addCell(rightCell);

            document.add(table);

            // Add score descriptions
            Paragraph descriptionsTitle = new Paragraph("\nSemnicatia scorurilor", HEADER_FONT);
            descriptionsTitle.setSpacingBefore(20);
            descriptionsTitle.setSpacingAfter(10);
            document.add(descriptionsTitle);

            // Add descriptions for each score
            addScoreDescription(document, "Acord parental", 
                "Dimensiunea \"Acord parental\" indica gradul in care ambii parinti impartasesc o viziune comuna si sunt in consens cu privire la aspectele esentiale ale cresterii si educatiei copilului. Este vorba despre un acord asupra regulilor, disciplinarii, valorilor si a altor decizii majore care privesc dezvoltarea copilului.");
            
            addScoreDescription(document, "Apropiere parentala",
                "Dimensiunea \"Apropiere parentala\" se refera la gradul de afectiune, respect si sustinere reciproca intre parinti in cadrul relatiei de coparentalitate. Ea reflecta cat de apropiati emotional se simt parintii unul de celalalt, in contextul responsabilitatilor legate de cresterea copilului.");
            
            addScoreDescription(document, "Suport parental",
                "Dimensiunea \"Suport parental\" evalueaza nivelul de sustinere pe care parintii si-l ofera reciproc. Aceasta include aspecte precum incurajarea, ajutorul practic, validarea deciziilor si respectul fata de competentele celuilalt parinte.");
            
            addScoreDescription(document, "Aprobarea partenerului",
                "Dimensiunea \"Aprobarea partenerului\" masoara gradul de respect, aprobare si sustinere pe care un parinte il simte din partea celuilalt parinte in ceea ce priveste abilitatile si deciziile legate de cresterea copilului.");
            
            addScoreDescription(document, "Subminare parentala",
                "Dimensiunea \"Subminare parentala\" indica tendinta unui parinte de a submina sau critica autoritatea, deciziile sau competentele celuilalt parinte in fata copilului sau in contextul parental.");
            
            addScoreDescription(document, "Expunere la conflict",
                "Dimensiunea \"Expunere la conflict\" masoara gradul in care copilul este expus la conflictele dintre parinti. Aceasta include observarea directa a certurilor, a tensiunilor sau a dezacordurilor deschise intre parinti.");

        } finally {
            document.close();
        }

        return out.toByteArray();
    }

    private void addCategoryScore(Document document, String category, int score) throws DocumentException {
        Paragraph p = new Paragraph();
        p.add(new Chunk(category + ": ", BOLD_FONT));
        Font scoreFont;
        if (score <= 2) {
            scoreFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(46, 204, 113)); // #2ECC71
        } else if (score == 3) {
            scoreFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(241, 196, 15)); // #F1C40F
        } else {
            scoreFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(52, 73, 94)); // Dark blue
        }
        p.add(new Chunk(String.valueOf(score), scoreFont));
        p.setSpacingAfter(10);
        document.add(p);
    }

    private void addScoreDescription(Document document, String title, String description) throws DocumentException {
        Paragraph p = new Paragraph();
        p.add(new Chunk(title + ": ", BOLD_FONT));
        p.add(new Chunk(description, NORMAL_FONT));
        p.setSpacingAfter(10);
        document.add(p);
    }

    private void addCategoryScoreToCell(PdfPCell cell, String category, int score) {
        Paragraph p = new Paragraph();
        p.add(new Chunk(category + ": ", BOLD_FONT));
        Font scoreFont;
        if (score <= 2) {
            scoreFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(46, 204, 113)); // #2ECC71
        } else if (score == 3) {
            scoreFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(241, 196, 15)); // #F1C40F
        } else {
            scoreFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(52, 73, 94)); // Dark blue
        }
        p.add(new Chunk(String.valueOf(score), scoreFont));
        p.setSpacingAfter(5);
        cell.addElement(p);
    }
} 