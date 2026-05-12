using System.Net;
using System.Text;

namespace FTS.RestClient
{
    // ─── INTERCEPTOR ─────────────────────────────────────────────────────────
    public class LoggingHandler : DelegatingHandler
    {
        public LoggingHandler() : base(new HttpClientHandler()) { }

        protected override async Task<HttpResponseMessage> SendAsync(
            HttpRequestMessage request,
            CancellationToken cancellationToken)
        {
            Console.WriteLine("REQUEST:");
            Console.WriteLine($"    Method : {request.Method}");
            Console.WriteLine($"    URI    : {request.RequestUri}");
            if (request.Content != null)
            {
                string body = await request.Content.ReadAsStringAsync(cancellationToken);
                if (!string.IsNullOrEmpty(body))
                    Console.WriteLine($"    Body   : {body}");
            }

            HttpResponseMessage response = await base.SendAsync(request, cancellationToken);

            Console.WriteLine("RESPONSE:");
            Console.WriteLine($"    Status : {response.StatusCode}");
            string responseBody = await response.Content.ReadAsStringAsync(cancellationToken);
            if (!string.IsNullOrEmpty(responseBody))
                Console.WriteLine($"    Body   : {responseBody}");
            Console.WriteLine();

            return response;
        }
    }

    // ─── REST CLIENT ─────────────────────────────────────────────────────────
    public class FestivalRestClient
    {
        private readonly HttpClient _client;
        private int _createdShowId;

        public FestivalRestClient(string baseUrl)
        {
            _client = new HttpClient(new LoggingHandler())
            {
                BaseAddress = new Uri(baseUrl)
            };
        }

        public async Task RunAll()
        {
            await CreateShow();
            await GetById();
            await UpdateShow();
            await GetAll();
            await GetByDate();
            await SellTicket();
            await ModifyTicket();
            await DeleteShow();
        }

        private async Task CreateShow()
        {
            Console.WriteLine("=== CREATE SHOW ===");
            string json = """
                {
                    "date": "2025-06-15T20:00:00",
                    "title": "Test Show",
                    "soldSeats": 0,
                    "venue": { "id": 6 }
                }
                """;

            var response = await _client.PostAsync(
                "/fts/show",
                new StringContent(json, Encoding.UTF8, "application/json")
            );

            string body = await response.Content.ReadAsStringAsync();
            var parsed = System.Text.Json.JsonDocument.Parse(body);
            _createdShowId = parsed.RootElement.GetProperty("id").GetInt32();
            Console.WriteLine($"Created show with id: {_createdShowId}");
        }

        private async Task GetById()
        {
            Console.WriteLine("=== GET BY ID ===");
            await _client.GetAsync($"/fts/show/{_createdShowId}");
        }

        private async Task UpdateShow()
        {
            Console.WriteLine("=== UPDATE SHOW ===");
            string json = """
                {
                    "date": "2025-06-15T21:00:00",
                    "title": "Updated Show",
                    "soldSeats": 0,
                    "venue": { "id": 6 }
                }
                """;

            await _client.PutAsync(
                $"/fts/show/{_createdShowId}",
                new StringContent(json, Encoding.UTF8, "application/json")
            );
        }

        private async Task GetAll()
        {
            Console.WriteLine("=== GET ALL ===");
            await _client.GetAsync("/fts/show");
        }

        private async Task GetByDate()
        {
            Console.WriteLine("=== GET BY DATE ===");
            await _client.GetAsync("/fts/show/date/2025-04-28");
        }

        private async Task SellTicket()
        {
            Console.WriteLine("=== SELL TICKET ===");
            await _client.PostAsync(
                $"/fts/show/{_createdShowId}/ticket?buyerName=John&seats=2",
                null
            );
        }

        private async Task ModifyTicket()
        {
            Console.WriteLine("=== MODIFY TICKET ===");
            var request = new HttpRequestMessage(
                HttpMethod.Patch,
                $"/fts/show/ticket/{_createdShowId}?seats=0"
            );
            await _client.SendAsync(request);
        }

        private async Task DeleteShow()
        {
            Console.WriteLine("=== DELETE SHOW ===");
            await _client.DeleteAsync($"/fts/show/{_createdShowId}");
        }
    }

    // ─── ENTRY POINT ─────────────────────────────────────────────────────────
    class Program
    {
        static async Task Main(string[] args)
        {
            var client = new FestivalRestClient("http://localhost:8080");
            await client.RunAll();
        }
    }
}