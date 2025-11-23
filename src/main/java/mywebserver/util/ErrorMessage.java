package mywebserver.util;

public enum ErrorMessage {
    HTTP_START_LINE_BLANK("HTTP 시작 라인은 비어있을 수 없습니다."),
    HTTP_METHOD_INVALID("유효하지 않은 HTTP 메소드입니다."),
    HTTP_PATH_INVALID("유효하지 않은 경로입니다."),
    HTTP_START_LINE_INCORRECT("유효하지 않은 HTTP 시작 라인입니다."),
    HTTP_REQUEST_FAILED("HTTP 요청 실패"),
    HTTP_HEADER_PARSING_ERROR("헤더 파싱 중 오류"),

    REQUEST_PROCESSING_FAILED("요청 처리 중 오류가 발생했습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
