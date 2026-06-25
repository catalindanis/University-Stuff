package sm.boundary.boundary;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.ResponseStatus;
import sm.boundary.entity.dto.SubscriptionsCostResponse;
import sm.control.control.ReportsPdfService;
import sm.control.control.ReportsService;

import java.time.LocalDate;

@Authenticated
@RequiredArgsConstructor
@Path("/api/reports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReportsController {

    private final ReportsService reportsService;
    private final ReportsPdfService reportsPdfService;

    @GET
    @Path("/costs")
    @ResponseStatus(200)
    public SubscriptionsCostResponse getSubscriptionsCost() {
        return reportsService.calculateSubscriptionsCost();
    }

    @GET
    @Path("/pdf")
    @Produces("application/pdf")
    public Response exportSubscriptionsPdfReport() {
        byte[] report = reportsPdfService.generateSubscriptionsReportPdf();
        String filename = "subscriptions-report-" + LocalDate.now() + ".pdf";

        return Response.ok(report)
                .type("application/pdf")
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .build();
    }
}
