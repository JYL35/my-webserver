package mywebserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpRequest {

    public HttpRequest(InputStream inputStream) {
        try (BufferedReader br = createBufferedReader(inputStream)) {
            String startLine = br.readLine();
            validateStartLine(startLine);
        } catch (IOException e) {
            throw new RuntimeException("HTTP 요청 실패", e);
        }
    }

    private BufferedReader createBufferedReader(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    private void validateStartLine(String startLine) {
        if (startLine == null || startLine.isBlank()) {
            throw new IllegalArgumentException("유효하지 않은 시작 라인입니다");
        }
    }
}
