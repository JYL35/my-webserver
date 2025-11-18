package mywebserver.http;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class HttpResponse {
    private final PrintWriter writer;

    public HttpResponse(OutputStream out) {
        this.writer = new PrintWriter(out, true, StandardCharsets.UTF_8);
    }

    public void send200(String body) {
        sendResponse("200 OK", body);
    }

    public void send404() {
        sendResponse("404 Not Found", "<html><body><h1>404 Not Found</h1></body></html>");
    }

    private void sendResponse(String startLine, String body) {
        writer.println("HTTP/1.1 " + startLine);
        writer.println("Content-Type: text/html; charset=utf-8\r\n");
        writer.println(body);
    }
}
