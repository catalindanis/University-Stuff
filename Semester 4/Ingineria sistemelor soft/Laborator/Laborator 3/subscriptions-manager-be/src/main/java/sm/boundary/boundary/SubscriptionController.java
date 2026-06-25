package sm.boundary.boundary;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import sm.boundary.entity.dto.*;
import sm.control.control.SubscriptionsService;

import java.time.LocalDate;
import java.util.List;

@Authenticated
@RequiredArgsConstructor
@Path("/api/subscriptions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SubscriptionController {

    private final SubscriptionsService subscriptionsService;

    @POST
    public Response saveSubscription(@Valid SubscriptionRequest request) {
        subscriptionsService.save(request);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    public Response updateSubscription(@Valid SubscriptionUpdateRequest request) {
        subscriptionsService.update(request);
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSubscription(@PathParam("id") long id) {
        subscriptionsService.deleteSubscription(id);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/{id}")
    public Response findSubscriptionById(@PathParam("id") long id) {
        return Response.ok(subscriptionsService.findById(id)).build();
    }

    @GET
    public Response findAllSubscriptions(@QueryParam("startDate") String startDate,
                                         @QueryParam("subscriptionCategoryId") Long subscriptionCategoryId) {
        return Response.ok(subscriptionsService.findAllSubscriptions(parseDate(startDate), subscriptionCategoryId)).build();
    }

    private LocalDate parseDate(String value) {
        return value == null || value.isBlank() ? null : LocalDate.parse(value);
    }

    @POST
    @RolesAllowed("ADMIN")
    @Path("/categories")
    public Response saveCategory(@Valid SubscriptionCategoryRequest categoryRequest) {
        subscriptionsService.save(categoryRequest);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @RolesAllowed("ADMIN")
    @Path("/categories")
    public Response updateCategory(@Valid SubscriptionCategoryUpdateRequest categoryUpdateRequest) {
        subscriptionsService.update(categoryUpdateRequest);
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @RolesAllowed("ADMIN")
    @Path("/categories/{id}")
    public Response deleteCategory(@PathParam("id") long id) {
        subscriptionsService.delete(id);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/categories/billingTypes")
    public Response findAllBillingTypes() {
        return Response.ok(subscriptionsService.findAllBillingTypes()).build();
    }

    @GET
    @Path("/categories")
    public Response findAllCategories() {
        return Response.ok(subscriptionsService.findAllCategories()).build();
    }

    @POST
    @Path("/alerts")
    public Response createPaymentAlert(@Valid PaymentAlertRequest paymentAlertRequest) {
        subscriptionsService.createPaymentAlert(paymentAlertRequest);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/alerts")
    public List<PaymentAlertResponse> findAllPaymentAlerts() {
        return subscriptionsService.findAllPaymentAlerts();
    }
}
