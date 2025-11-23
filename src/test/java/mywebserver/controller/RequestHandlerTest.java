package mywebserver.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class RequestHandlerTest {

    @DisplayName("요청에 따라 라우팅하여 올바른 응답을 반환한다")
    @ParameterizedTest
    @CsvSource({
            "GET / HTTP/1.1\r\nHost: localhost\r\n\r\n, HTTP/1.1 200 OK, <h1>Hello World</h1>",
            "GET /abc HTTP/1.1\r\nHost: localhost\r\n\r\n, HTTP/1.1 404 Not Found, <h1>404 Not Found</h1>"
    })
    void RequestHandler_RoutingToRequest_ReturnsResponse(String requestString, String startLine, String expected) {
        InputStream in = new ByteArrayInputStream(requestString.getBytes(StandardCharsets.UTF_8));
        OutputStream out = new ByteArrayOutputStream();

        RequestHandler requestHandler = new RequestHandler(in, out);
        requestHandler.run();

        String response = out.toString();
        assertThat(response).contains(startLine);
        assertThat(response).contains(expected);
    }
}
