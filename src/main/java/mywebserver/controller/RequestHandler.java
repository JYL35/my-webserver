package mywebserver.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import mywebserver.http.HttpMethod;
import mywebserver.http.HttpRequest;
import mywebserver.http.HttpResponse;
import mywebserver.util.ErrorMessage;
import mywebserver.view.OutputView;

public class RequestHandler implements Runnable {
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public RequestHandler(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try (InputStream in = inputStream; OutputStream out = outputStream){
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            requestRouting(request, response);
        } catch (Exception e) {
            OutputView.printException(ErrorMessage.REQUEST_PROCESSING_FAILED.getMessage(), e);
        }
    }

    private void requestRouting(HttpRequest request, HttpResponse response) {
        if (request.getMethod() == HttpMethod.GET && request.getPath().equals("/")) {
            response.send200("<html><body><h1>Hello World</h1></body></html>");
            return;
        }

        if (request.getMethod() == HttpMethod.GET && request.getPath().endsWith(".html")) {
            serveStaticFile(request.getPath(), response);
            return;
        }
        response.send404();
    }

    private void serveStaticFile(String path, HttpResponse response) {
        URL url = getClass()
                .getClassLoader()
                .getResource("static" + path);

        if (url == null) {
            response.send404();
            return;
        }

        try {
            String body = Files.readString(Path.of(url.toURI()));
            response.send200(body);
        } catch (Exception e) {
            response.send404();
        }
    }
}
