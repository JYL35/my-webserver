package mywebserver.http;

import mywebserver.util.ErrorMessage;

public class HttpStartLine {
    private final HttpMethod httpMethod;
    private final String path;

    public HttpStartLine(String startLine) {
        validateStartLine(startLine);
        String[] tokens = parseStartLine(startLine);

        this.httpMethod = HttpMethod.from(tokens[0]);

        validatePath(tokens[1]);
        this.path = tokens[1];
    }

    private void validateStartLine(String startLine) {
        if (startLine == null || startLine.isBlank()) {
            throw new IllegalArgumentException(ErrorMessage.HTTP_START_LINE_BLANK.getMessage());
        }
    }

    private void validatePath(String path) {
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException(ErrorMessage.HTTP_PATH_INVALID.getMessage());
        }
    }

    private String[] parseStartLine(String startLine) {
        String[] tokens = startLine.split(" ");
        if (tokens.length < 2) {
            throw new IllegalArgumentException(ErrorMessage.HTTP_START_LINE_INCORRECT.getMessage());
        }
        return tokens;
    }

    public String getMethod() {
        return httpMethod.name();
    }

    public String getPath() {
        return path;
    }
}
