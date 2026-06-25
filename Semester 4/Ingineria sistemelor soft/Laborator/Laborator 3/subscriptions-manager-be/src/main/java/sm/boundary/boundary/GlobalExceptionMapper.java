package sm.boundary.boundary;

import io.quarkus.security.UnauthorizedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import sm.boundary.entity.exceptions.DuplicateEmailException;

@Provider
class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {

    @Override
    public Response toResponse(UnauthorizedException e) {
        return Response.
                status(Response.Status.UNAUTHORIZED)
                .entity(
                        new ErrorResponse(
                                Response.Status.UNAUTHORIZED.getStatusCode(),
                                Response.Status.UNAUTHORIZED.name(),
                                e.getMessage())
                )
                .build();
    }
}

@Provider
class DuplicateEmailExceptionMapper implements ExceptionMapper<DuplicateEmailException> {
    @Override
    public Response toResponse(DuplicateEmailException e) {
        return Response.
                status(Response.Status.CONFLICT)
                .entity(
                        new ErrorResponse(
                                Response.Status.CONFLICT.getStatusCode(),
                                Response.Status.CONFLICT.name(),
                                e.getMessage())
                )
                .build();
    }
}

@Provider
class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException e) {
        return Response.
                status(Response.Status.NOT_FOUND)
                .entity(
                        new ErrorResponse(
                                Response.Status.NOT_FOUND.getStatusCode(),
                                Response.Status.NOT_FOUND.name(),
                                e.getMessage())
                )
                .build();
    }
}

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(
                        new ErrorResponse(
                                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                Response.Status.INTERNAL_SERVER_ERROR.name(),
                                exception.getMessage())
                )
                .build();
    }
}

record ErrorResponse(int status, String error, String message) {}

