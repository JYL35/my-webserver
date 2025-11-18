package mywebserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import mywebserver.util.ErrorMessage;

public class HttpRequest {
    private final HttpStartLine startLine;

    public HttpRequest(InputStream inputStream) {
        try (BufferedReader reader = createBufferedReader(inputStream)) {
            String line = reader.readLine();
            this.startLine = new HttpStartLine(line);
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.HTTP_REQUEST_FAILED.getMessage(), e);
        }
    }

    private BufferedReader createBufferedReader(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    public String getMethod() {
        return startLine.getMethod();
    }

    public String getPath() {
        return startLine.getPath();
    }
}
