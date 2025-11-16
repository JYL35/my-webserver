package mywebserver.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
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
                .hasMessageContaining("유효하지 않은 시작 라인입니다");
    }

    @DisplayName("메소드와 경로를 올바르게 파싱한다")
    @ParameterizedTest
    @CsvSource({
            "GET /index.html HTTP/1.1, GET, /index.html",
            "POST / HTTP/1.1, POST, /"
    })
    void HttpRequest_ParseStartLine_ReturnsMethodAndPath(String startLine, String httpMethod, String path) {
        String requestString = startLine + "\r\n" + "Host: localhost:8080\r\n" + "\r\n";
        InputStream in = createRequest(requestString);

        HttpRequest request = new HttpRequest(in);

        assertThat(request.getMethod()).isEqualTo(httpMethod);
        assertThat(request.getPath()).isEqualTo(path);
    }

    private InputStream createRequest(String requestString) {
        return new ByteArrayInputStream(requestString.getBytes(StandardCharsets.UTF_8));
    }
}
