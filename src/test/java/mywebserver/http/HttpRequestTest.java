package mywebserver.http;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

public class HttpRequestTest {

    @DisplayName("요청이 비어있거나 null이면 예외가 발생한다")
    @ParameterizedTest
    @NullAndEmptySource
    void HttpRequest_NullOrEmptyStartLine_ThrowException(String startLine) {
        InputStream in = createRequest(startLine + "Host: localhost\r\n\r\n");

        assertThatThrownBy(() -> new HttpRequest(in))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유효하지 않은 시작 라인입니다");
    }

    private InputStream createRequest(String requestString) {
        return new ByteArrayInputStream(requestString.getBytes(StandardCharsets.UTF_8));
    }
}
