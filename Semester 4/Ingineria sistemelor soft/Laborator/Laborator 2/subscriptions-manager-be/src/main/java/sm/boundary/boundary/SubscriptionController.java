package sm.boundary.boundary;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import sm.boundary.entity.dto.SubscriptionCategoryRequest;
import sm.boundary.entity.dto.SubscriptionCategoryUpdateRequest;
import sm.control.control.SubscriptionsService;

@Authenticated
@RequiredArgsConstructor
@Path("/api/subscriptions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SubscriptionController {

    private final SubscriptionsService subscriptionsService;

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

    @GET
    @RolesAllowed("ADMIN")
    @Path("/categories/billingTypes")
    public Response findAllBillingTypes() {
        return Response.ok(subscriptionsService.findAllBillingTypes()).build();
    }

    @GET
    @Path("/categories")
    public Response findAllCategories() {
        return Response.ok(subscriptionsService.findAllCategories()).build();
    }
}
