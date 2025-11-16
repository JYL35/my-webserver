package mywebserver.http;

import java.util.Arrays;
import mywebserver.util.ErrorMessage;

public enum HttpMethod {
    GET, POST, PUT, PATCH, DELETE, HEAD, OPTIONS, CONNECT, TRACE;

    public static HttpMethod from(String method) {
        return Arrays.stream(values())
                .filter(httpMethod -> httpMethod.name().equals(method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        ErrorMessage.HTTP_METHOD_INVALID.getMessage()
                ));
    }
}
