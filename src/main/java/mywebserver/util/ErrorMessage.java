package mywebserver.util;

public enum ErrorMessage {
    HTTP_START_LINE_BLANK("HTTP 시작 라인은 비어있을 수 없습니다."),
    HTTP_METHOD_INVALID("유효하지 않은 HTTP 메소드입니다."),
    HTTP_PATH_INVALID("유효하지 않은 경로입니다."),
    HTTP_START_LINE_INCORRECT("유효하지 않은 HTTP 시작 라인입니다."),
    HTTP_REQUEST_FAILED("HTTP 요청 실패");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
