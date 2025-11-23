package mywebserver.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class RequestHandlerTest {

    @DisplayName("요청에 따라 라우팅하여 올바른 응답을 반환한다")
    @ParameterizedTest
    @CsvSource({
            "GET / HTTP/1.1, 200 OK, <h1>Hello World</h1>",
            "GET /abc HTTP/1.1, 404 Not Found, <h1>404 Not Found</h1>"
    })
    void RequestHandler_RoutingToRequest_ReturnsResponse(String startLine, String expectedStatus, String expectedBody) {
        String requestString = startLine + "\r\nHost: localhost\r\n\r\n";

        InputStream in = new ByteArrayInputStream(requestString.getBytes(StandardCharsets.UTF_8));
        OutputStream out = new ByteArrayOutputStream();

        RequestHandler requestHandler = new RequestHandler(in, out);
        requestHandler.run();

        String response = out.toString();
        assertThat(response).contains("HTTP/1.1 " + expectedStatus);
        assertThat(response).contains(expectedBody);
    }

    @DisplayName("index.html 요청 시 파일 내용을 포함하여 응답한다")
    @Test
    void RequestHandler_GetIndexHtml_ReturnsFileContent() {
        String requestString = "GET /index.html HTTP/1.1\r\nHost: localhost\r\n\r\n";
        InputStream in = new ByteArrayInputStream(requestString.getBytes(StandardCharsets.UTF_8));
        OutputStream out = new ByteArrayOutputStream();

        RequestHandler requestHandler = new RequestHandler(in, out);
        requestHandler.run();

        String response = out.toString();
        assertThat(response).contains("HTTP/1.1 200 OK");
        assertThat(response).contains("Content-Type: text/html");
        assertThat(response).contains("<h1>Hello File</h1>");
    }
}
