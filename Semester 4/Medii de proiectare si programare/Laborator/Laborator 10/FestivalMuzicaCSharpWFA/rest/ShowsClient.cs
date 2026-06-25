using FestivalMuzicaCSharp.Domain;
using System.Globalization;
using System.Net;
using System.Text;
using System.Text.Json;

namespace Rest;

public sealed class ShowsClient : IDisposable
{
    public const string BaseUrl = "http://localhost:8080/shows";

    private readonly HttpClient _httpClient;
    private readonly JsonSerializerOptions _jsonOptions;

    public ShowsClient(HttpMessageHandler? handler = null)
    {
        _jsonOptions = new JsonSerializerOptions
        {
            PropertyNamingPolicy = JsonNamingPolicy.CamelCase,
            PropertyNameCaseInsensitive = true
        };
        _jsonOptions.Converters.Add(new DateOnlyJsonConverter());

        handler ??= new LoggingHandler(new HttpClientHandler());
        _httpClient = new HttpClient(handler, disposeHandler: true);
    }

    public void Dispose() => _httpClient.Dispose();

    public Task<long> SaveAsync(Show show)
    {
        return ExecuteAsync(async () =>
        {
            var content = CreateJsonContent(show);
            using var response = await _httpClient.PostAsync(BaseUrl, content);
            return await ReadAsAsync<long>(response);
        });
    }

    public Task DeleteAsync(long id)
    {
        return ExecuteAsync(async () =>
        {
            using var response = await _httpClient.DeleteAsync($"{BaseUrl}/{id}");
            await EnsureSuccess(response);
        });
    }

    public Task UpdateAsync(long id, Show show)
    {
        return ExecuteAsync(async () =>
        {
            var content = CreateJsonContent(show);
            using var response = await _httpClient.PutAsync($"{BaseUrl}/{id}", content);
            await EnsureSuccess(response);
        });
    }

    public Task<Show?> FindByIdAsync(long id)
    {
        return ExecuteAsync(async () =>
        {
            using var response = await _httpClient.GetAsync($"{BaseUrl}/{id}");
            if (response.StatusCode == HttpStatusCode.NotFound)
            {
                return null;
            }

            return await ReadAsAsync<Show>(response);
        });
    }

    public Task<Show[]> FindAllAsync()
    {
        return ExecuteAsync(async () =>
        {
            using var response = await _httpClient.GetAsync(BaseUrl);
            return await ReadAsAsync<Show[]>(response);
        });
    }

    public Task<Show[]> FindAllFilteredAsync(ShowFilter showFilter)
    {
        return ExecuteAsync(async () =>
        {
            var query = BuildQuery(showFilter);
            var requestUri = string.IsNullOrWhiteSpace(query)
                ? BaseUrl
                : $"{BaseUrl}?{query}";

            using var response = await _httpClient.GetAsync(requestUri);
            return await ReadAsAsync<Show[]>(response);
        });
    }

    private async Task ExecuteAsync(Func<Task> action)
    {
        try
        {
            await action();
        }
        catch (HttpRequestException ex)
        {
            throw new ServiceException("Request failed.", ex);
        }
        catch (TaskCanceledException ex)
        {
            throw new ServiceException("Request timed out.", ex);
        }
    }

    private async Task<T> ExecuteAsync<T>(Func<Task<T>> action)
    {
        try
        {
            return await action();
        }
        catch (HttpRequestException ex)
        {
            throw new ServiceException("Request failed.", ex);
        }
        catch (TaskCanceledException ex)
        {
            throw new ServiceException("Request timed out.", ex);
        }
    }

    private StringContent CreateJsonContent<T>(T value)
    {
        var json = JsonSerializer.Serialize(value, _jsonOptions);
        return new StringContent(json, Encoding.UTF8, "application/json");
    }

    private async Task EnsureSuccess(HttpResponseMessage response)
    {
        if (response.IsSuccessStatusCode)
        {
            return;
        }

        var body = response.Content is null
            ? string.Empty
            : await response.Content.ReadAsStringAsync();

        throw new ServiceException($"Request failed with status {(int)response.StatusCode} {response.ReasonPhrase}. Body: {body}");
    }

    private async Task<T> ReadAsAsync<T>(HttpResponseMessage response)
    {
        await EnsureSuccess(response);
        var content = response.Content is null
            ? string.Empty
            : await response.Content.ReadAsStringAsync();

        if (string.IsNullOrWhiteSpace(content))
        {
            return default!;
        }

        return JsonSerializer.Deserialize<T>(content, _jsonOptions)!;
    }

    private sealed class LoggingHandler : DelegatingHandler
    {
        public LoggingHandler(HttpMessageHandler innerHandler)
            : base(innerHandler)
        {
        }

        protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            var requestBody = request.Content is null
                ? string.Empty
                : await request.Content.ReadAsStringAsync(cancellationToken);

            Console.WriteLine($"Sending {request.Method} to {request.RequestUri} body=[{requestBody}]");

            if (request.Content is not null)
            {
                var mediaType = request.Content.Headers.ContentType?.MediaType ?? "application/json";
                request.Content = new StringContent(requestBody, Encoding.UTF8, mediaType);
            }

            HttpResponseMessage response;
            try
            {
                response = await base.SendAsync(request, cancellationToken);
            }
            catch (Exception ex) when (ex is HttpRequestException or TaskCanceledException)
            {
                Console.Error.WriteLine($"Request failed: {ex.Message}");
                throw;
            }

            var responseBody = response.Content is null
                ? string.Empty
                : await response.Content.ReadAsStringAsync(cancellationToken);

            Console.WriteLine($"Received status {(int)response.StatusCode} {response.StatusCode} body=[{responseBody}]");

            if (response.Content is not null)
            {
                var mediaType = response.Content.Headers.ContentType?.MediaType ?? "application/json";
                response.Content = new StringContent(responseBody, Encoding.UTF8, mediaType);
            }

            return response;
        }
    }

    private static string BuildQuery(ShowFilter? showFilter)
    {
        if (showFilter is null)
        {
            return string.Empty;
        }

        var parameters = new List<string>();

        if (!string.IsNullOrWhiteSpace(showFilter.ArtistName))
        {
            parameters.Add($"artistName={Uri.EscapeDataString(showFilter.ArtistName)}");
        }

        if (showFilter.Date is not null)
        {
            var dateValue = showFilter.Date.Value.ToString("yyyy-MM-dd", CultureInfo.InvariantCulture);
            parameters.Add($"date={Uri.EscapeDataString(dateValue)}");
        }

        if (!string.IsNullOrWhiteSpace(showFilter.Location))
        {
            parameters.Add($"location={Uri.EscapeDataString(showFilter.Location)}");
        }

        if (showFilter.RemainingSeats is not null)
        {
            parameters.Add($"remainingSeats={showFilter.RemainingSeats.Value}");
        }

        return string.Join("&", parameters);
    }
}
