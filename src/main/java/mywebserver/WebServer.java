package mywebserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import mywebserver.controller.RequestHandler;
import mywebserver.util.ErrorMessage;
import mywebserver.view.OutputView;

public class WebServer {
    private final int port;

    public WebServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            OutputView.printServerStart(port);

            runServerLoop(serverSocket);

        } catch (IOException e) {
            OutputView.printException(ErrorMessage.SERVER_START_FAILED.getMessage(), e);
        }
    }

    private void runServerLoop(ServerSocket serverSocket) throws IOException {
        while (!Thread.currentThread().isInterrupted()) {
            Socket connection = serverSocket.accept();
            handleConnection(connection);
        }
    }

    private void handleConnection(Socket connection) {
        try {
            OutputView.printClientConnected(connection.getInetAddress());

            RequestHandler requestHandler = new RequestHandler(
                    connection.getInputStream(),
                    connection.getOutputStream()
            );
        } catch (IOException e) {
            OutputView.printException(ErrorMessage.CLIENT_CONNECTION_FAILED.getMessage(), e);
        }
    }
}
