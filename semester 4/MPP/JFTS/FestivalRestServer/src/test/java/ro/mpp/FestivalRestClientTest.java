package ro.mpp;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FestivalRestClientTest {
    private RestClient client;
    private static Integer createdShowId; // shared between tests

    // INTERCEPTOR — stays exactly the same
    static class LoggingInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            System.out.println("REQUEST:");
            System.out.println("    Method : " + request.getMethod());
            System.out.println("    URI    : " + request.getURI());
            if (body.length > 0)
                System.out.println("    Body   : " + new String(body, StandardCharsets.UTF_8));

            ClientHttpResponse response = execution.execute(request, body);
            byte[] responseBody = response.getBody().readAllBytes();

            System.out.println("RESPONSE:");
            System.out.println("    Status : " + response.getStatusCode());
            if (responseBody.length > 0)
                System.out.println("    Body   : " + new String(responseBody, StandardCharsets.UTF_8));
            System.out.println();

            return new BufferingClientHttpResponse(response, responseBody);
        }

        static class BufferingClientHttpResponse implements ClientHttpResponse {
            private final ClientHttpResponse original;
            private final byte[] body;

            BufferingClientHttpResponse(ClientHttpResponse original, byte[] body) {
                this.original = original;
                this.body = body;
            }

            @Override public InputStream getBody() { return new ByteArrayInputStream(body); }
            @Override public HttpStatusCode getStatusCode() throws IOException { return original.getStatusCode(); }
            @Override public String getStatusText() throws IOException { return original.getStatusText(); }
            @Override public HttpHeaders getHeaders() { return original.getHeaders(); }
            @Override public void close() { original.close(); }
        }
    }

    @BeforeEach
    void setup() {
        this.client = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .requestInterceptor(new LoggingInterceptor())
                .build();
    }

    @Test
    @Order(1)
    void testCreateShow() {
        String showJson = """
                {
                    "date": "2025-06-15T20:00:00",
                    "title": "Test Show",
                    "soldSeats": 0,
                    "venue": { "id": 6 }
                }
                """;

        String result = client.post()
                .uri("/fts/show")
                .contentType(MediaType.APPLICATION_JSON)
                .body(showJson)
                .retrieve()
                .body(String.class);

        System.out.println(result);
        assertNotNull(result);

        // extract the id from the response JSON so other tests can use it
        createdShowId = new org.json.JSONObject(result).getInt("id");
        System.out.println("Created show with id: " + createdShowId);
    }

    @Test
    @Order(2)
    void getById() {
        assertNotNull(createdShowId, "Create test must run first");
        String byId = client.get()
                .uri("/fts/show/" + createdShowId)
                .retrieve()
                .body(String.class);
        System.out.println(byId);
        assertNotNull(byId);
    }

    @Test
    @Order(3)
    void testUpdateShow() {
        assertNotNull(createdShowId, "Create test must run first");
        String showJson = """
                {
                    "date": "2025-06-15T21:00:00",
                    "title": "Updated Show",
                    "soldSeats": 0,
                    "venue": { "id": 6 }
                }
                """;

        String result = client.put()
                .uri("/fts/show/" + createdShowId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(showJson)
                .retrieve()
                .body(String.class);

        System.out.println(result);
        assertNotNull(result);
    }

    @Test
    @Order(4)
    void testGetAll() {
        String result = client.get()
                .uri("/fts/show")
                .retrieve()
                .body(String.class);
        System.out.println(result);
        assertNotNull(result);
    }

    @Test
    @Order(5)
    void getByDate() {
        String byDate = client.get()
                .uri("/fts/show/date/2025-04-28")
                .retrieve()
                .body(String.class);
        System.out.println(byDate);
        assertNotNull(byDate);
    }

    @Test
    @Order(6)
    void testSellTicket() {
        String url = "/fts/show/" + createdShowId + "/ticket?buyerName=John&seats=2";
        String result = client.post()
                .uri(url)
                .retrieve()
                .body(String.class);
        System.out.println(result);
        assertNotNull(result);
    }

    @Test
    @Order(7)
    void testModifyTicket() {
        String  url = "/fts/show/ticket/"+ createdShowId +"?seats=0";
        ResponseEntity<Void> modified = client.patch()
                .uri(url)
                .retrieve()
                .toBodilessEntity();

        System.out.println("Modify status: " + modified.getStatusCode());
        assertEquals(HttpStatus.OK, modified.getStatusCode());
    }

    @Test
    @Order(8)
    void testDeleteShow() {
        assertNotNull(createdShowId, "Create test must run first");
        ResponseEntity<Void> result = client.delete()
                .uri("/fts/show/" + createdShowId)
                .retrieve()
                .toBodilessEntity();

        System.out.println("Delete status: " + result.getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }
}