package mywebserver.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HttpHeadersTest {

    @DisplayName("헤더 라인들을 파싱하여 key-value 맵으로 저장한다")
    @Test
    void HttpHeaders_parseHeaders_SaveAsMap() {
        String headersString = "Host: localhost:8080\r\n" +
                "User-Agent: my-test-client\r\n" +
                "Connection: keep-alive\r\n" +
                "\r\n";
        BufferedReader reader = createReader(headersString);

        HttpHeaders headers = new HttpHeaders(reader);

        assertThat(headers.get("Host")).isEqualTo("localhost:8080");
        assertThat(headers.get("User-Agent")).isEqualTo("my-test-client");
        assertThat(headers.get("Connection")).isEqualTo("keep-alive");
    }

    @DisplayName("빈 헤더에도 오류가 발생하지 않는다")
    @Test
    void HttpHeaders_EmptyHeaders_NotError() {
        String headersString = "\r\n";
        BufferedReader reader = createReader(headersString);

        HttpHeaders headers = new HttpHeaders(reader);

        assertThat(headers.get("Host")).isNull();
    }

    private BufferedReader createReader(String headersString) {
        InputStream in = new ByteArrayInputStream(headersString.getBytes((StandardCharsets.UTF_8)));
        return new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
    }
}
