package mywebserver.controller;

import java.io.InputStream;
import java.io.OutputStream;
import mywebserver.http.HttpMethod;
import mywebserver.http.HttpRequest;
import mywebserver.http.HttpResponse;

public class RequestHandler implements Runnable {
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public RequestHandler(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try {
            HttpRequest request = new HttpRequest(inputStream);

            HttpResponse response = new HttpResponse(outputStream);

            requestRouting(request, response);
        } catch (Exception e) {
            System.out.println("요청 처리 중 오류 발생: " + e.getMessage());
        }
    }

    private void requestRouting(HttpRequest request, HttpResponse response) {
        if (request.getMethod() == HttpMethod.GET && request.getPath().equals("/")) {
            response.send200("<html><body><h1>Hello World</h1></body></html>");
            return;
        }
        response.send404();
    }
}
