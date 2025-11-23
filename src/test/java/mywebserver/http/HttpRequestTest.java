package mywebserver.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import mywebserver.util.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class HttpRequestTest {

    @DisplayName("요청이 비어있거나 null이면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", "  \r\n"})
    void HttpRequest_NullOrEmptyStartLine_ThrowException(String startLine) {
        InputStream in = createRequest(startLine);

        assertThatThrownBy(() -> new HttpRequest(in))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.HTTP_START_LINE_BLANK.getMessage());
    }

    @DisplayName("메소드와 경로를 올바르게 파싱한다")
    @ParameterizedTest
    @CsvSource({
            "GET /index.html HTTP/1.1, GET, /index.html",
            "POST / HTTP/1.1, POST, /"
    })
    void HttpRequest_ParseStartLine_ReturnsMethodAndPath(String startLine, HttpMethod httpMethod, String path) {
        String requestString = startLine + "\r\n" + "Host: localhost:8080\r\n" + "\r\n";
        InputStream in = createRequest(requestString);

        HttpRequest request = new HttpRequest(in);

        assertThat(request.getMethod()).isEqualTo(httpMethod);
        assertThat(request.getPath()).isEqualTo(path);
    }

    @DisplayName("유효하지 않은 HTTP Method인 경우 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(strings = {"get / HTTP/1.1", "ABC /index.html HTTP/1.1", "POST. / HTTP/1.1"})
    void HttpRequest_InvalidMethod_ThrowException(String startLine) {
        String requestString = startLine + "\r\n\r\n";
        InputStream in = createRequest(requestString);

        assertThatThrownBy(() -> new HttpRequest(in))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.HTTP_METHOD_INVALID.getMessage());
    }

    @DisplayName("유효하지 않은 Path인 경우 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(strings = {"GET a/ HTTP/1.1", "DELETE index.html HTTP/1.1", "POST ./index.html HTTP/1.1"})
    void HttpRequest_InvalidPath_ThrowException(String startLine) {
        String requestString = startLine + "\r\n\r\n";
        InputStream in = createRequest(requestString);

        assertThatThrownBy(() -> new HttpRequest(in))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.HTTP_PATH_INVALID.getMessage());
    }

    @DisplayName("HTTP 헤더를 올바르게 파싱하여 key:value 형태로 저장한다. 이후 key를 통해 value를 받아온다.")
    @Test
    void HttpRequest_ParseHeaders_ReturnsHeaderValues() {
        String requestString = "GET /index.html HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "User-Agent: my-test-client\r\n" +
                "\r\n";
        InputStream in = createRequest(requestString);

        HttpRequest request = new HttpRequest(in);

        assertThat(request.getHeader("Host")).isEqualTo("localhost:8080");
        assertThat(request.getHeader("User-Agent")).isEqualTo("my-test-client");
    }

    private InputStream createRequest(String requestString) {
        return new ByteArrayInputStream(requestString.getBytes(StandardCharsets.UTF_8));
    }
}
