package sm.control.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import sm.boundary.entity.dto.SubscriptionReportEntryResponse;
import sm.boundary.entity.dto.SubscriptionsReportResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@ApplicationScoped
@RequiredArgsConstructor
public class ReportsPdfService {

    private static final float PAGE_MARGIN = 50f;
    private static final float LINE_HEIGHT = 15f;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ReportsService reportsService;

    public byte[] generateSubscriptionsReportPdf() {
        SubscriptionsReportResponse report = reportsService.buildSubscriptionsReport();

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                content.setLeading(LINE_HEIGHT);
                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
                content.newLineAtOffset(PAGE_MARGIN, page.getMediaBox().getHeight() - PAGE_MARGIN);

                content.showText("Subscriptions Report");
                content.newLine();
                content.newLine();

                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
                content.showText("Generated at: " + DATETIME_FORMATTER.format(report.generatedAt()));
                content.newLine();
                content.showText("Total subscriptions: " + report.subscriptionsCount());
                content.newLine();
                content.showText(String.format(Locale.US, "Total cost: $%.2f", report.totalCost()));
                content.newLine();
                content.newLine();
                content.showText("Details:");
                content.newLine();

                for (SubscriptionReportEntryResponse entry : report.subscriptions()) {
                    String line = String.format(
                            Locale.US,
                            "%s | %s | start: %s | unit: $%.2f | periods: %d | total: $%.2f",
                            entry.companyName(),
                            entry.billingType(),
                            DATE_FORMATTER.format(entry.startDate()),
                            entry.unitPrice(),
                            entry.billingPeriods(),
                            entry.totalCost()
                    );

                    if (line.length() > 115) {
                        line = line.substring(0, 112) + "...";
                    }

                    content.showText(line);
                    content.newLine();
                }

                content.endText();
            }

            document.save(output);
            return output.toByteArray();
        } catch (IOException e) {
            throw new InternalServerErrorException("Could not generate subscriptions PDF report", e);
        }
    }
}

