package mywebserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import mywebserver.util.ErrorMessage;

public class HttpRequest {
    private final HttpMethod httpMethod;
    private final String path;

    public HttpRequest(InputStream inputStream) {
        try (BufferedReader br = createBufferedReader(inputStream)) {
            String startLine = br.readLine();

            validateStartLine(startLine);

            String[] tokens = parseStartLine(startLine);
            this.httpMethod = HttpMethod.from(tokens[0]);
            this.path = tokens[1];
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.HTTP_REQUEST_FAILED.getMessage(), e);
        }
    }

    private BufferedReader createBufferedReader(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    private void validateStartLine(String startLine) {
        if (startLine == null || startLine.isBlank()) {
            throw new IllegalArgumentException(ErrorMessage.HTTP_START_LINE_BLANK.getMessage());
        }
    }

    private String[] parseStartLine(String startLine) {
        String[] tokens = startLine.split(" ");
        if (tokens.length < 2) {
            throw new IllegalArgumentException(ErrorMessage.HTTP_START_LINE_INCORRECT.getMessage());
        }
        return tokens;
    }

    public HttpMethod getMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }
}
