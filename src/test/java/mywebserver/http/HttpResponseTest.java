package mywebserver.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HttpResponseTest {

    @DisplayName("200 OK 응답과 바디를 작성한다")
    @Test
    void HttpResponse_Send200_WritesOkResponse() {
        OutputStream out = new ByteArrayOutputStream();
        HttpResponse response = new HttpResponse(out);
        String body = "<h1>Hello World</h1>";

        response.send200(body);

        String expected = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html; charset=utf-8\r\n" +
                "\r\n" +
                body + "\r\n";
        assertThat(out.toString()).isEqualTo(expected);
    }

    @DisplayName("404 Not Found 응답과 바디를 작성한다")
    @Test
    void HttpResponse_Send404_WritesNotFoundResponse() {
        OutputStream out = new ByteArrayOutputStream();
        HttpResponse response = new HttpResponse(out);

        response.send404();

        String expected = "HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/html; charset=utf-8\r\n" +
                "\r\n" +
                "<html><body><h1>404 Not Found</h1></body></html>\r\n";
        assertThat(out.toString()).isEqualTo(expected);
    }
}
