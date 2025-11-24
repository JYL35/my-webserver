package mywebserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import mywebserver.util.ErrorMessage;

public class HttpHeaders {
    private final Map<String, String> headers;

    public HttpHeaders(BufferedReader reader) {
        try {
            headers = new HashMap<>();
            parseHeaders(reader);
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.HTTP_HEADER_PARSING_ERROR
                    .getMessage(), e);
        }
    }

    private void parseHeaders(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !line.isBlank()) {
            String[] tokens = line.split(": ", 2);
            headers.put(tokens[0], tokens[1]);
        }
    }

    public String get(String key) {
        return headers.get(key);
    }
}
