package client;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ro.mpp2026.festivalmuzicajavafx.domain.Show;
import ro.mpp2026.festivalmuzicajavafx.domain.ShowFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class ShowsClient {
    public static final String BASE_URL = "http://localhost:8080/shows";

    private final RestClient restClient = RestClient.builder()
            .baseUrl(BASE_URL)
            .requestInterceptor(new ClientLoggingInterceptor())
            .build();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) {
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Long save(Show show) {
        return execute(() -> restClient.post()
                .uri("")
                .contentType(APPLICATION_JSON)
                .body(show)
                .retrieve()
                .body(Long.class));
    }

    public void delete(long id) {
        execute(() -> restClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .toBodilessEntity());
    }

    public void update(long id, Show show) {
        execute(() -> restClient.put()
                .uri("/{id}", id)
                .contentType(APPLICATION_JSON)
                .body(show)
                .retrieve()
                .toBodilessEntity());
    }

    public Show findById(long id) {
        return execute(() -> restClient.get()
                .uri("/{id}", id)
                .retrieve()
                .body(Show.class));
    }

    public Show[] findAll() {
        return execute(() -> restClient.get()
                .uri("")
                .retrieve()
                .body(Show[].class));
    }

    public Show[] findAllFiltered(ShowFilter showFilter) {
        return execute(() -> restClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder.path("");
                    if (showFilter != null) {
                        if (showFilter.artistName() != null) {
                            builder.queryParam("artistName", showFilter.artistName());
                        }
                        if (showFilter.date() != null) {
                            builder.queryParam("date", showFilter.date());
                        }
                        if (showFilter.location() != null) {
                            builder.queryParam("location", showFilter.location());
                        }
                        if (showFilter.remainingSeats() != null) {
                            builder.queryParam("remainingSeats", showFilter.remainingSeats());
                        }
                    }
                    return builder.build();
                })
                .retrieve()
                .body(Show[].class));
    }

    private static class ClientLoggingInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            String bodyText = body == null || body.length == 0
                    ? ""
                    : new String(body, StandardCharsets.UTF_8);
            System.out.println("Sending " + request.getMethod() + " to " + request.getURI() + " body=[" + bodyText + "]");
            ClientHttpResponse response;
            try {
                response = execution.execute(request, body);
                System.out.println("Received status " + response.getStatusCode());
            } catch (IOException ex) {
                System.err.println("Request failed: " + ex.getMessage());
                throw ex;
            }
            return response;
        }
    }
}
