package mywebserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import mywebserver.controller.RequestHandler;
import mywebserver.util.ErrorMessage;
import mywebserver.view.OutputView;

public class WebServer {
    private static final int THREAD_POOL_SIZE = 50;
    private final int port;
    private final ExecutorService executorService;

    public WebServer(int port) {
        this.port = port;
        this.executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            OutputView.printServerStart(port);

            runServerLoop(serverSocket);
        } catch (IOException e) {
            OutputView.printException(ErrorMessage.SERVER_START_FAILED.getMessage(), e);
        } finally {
            executorService.shutdown();
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

            executorService.execute(requestHandler);
        } catch (IOException e) {
            OutputView.printException(ErrorMessage.CLIENT_CONNECTION_FAILED.getMessage(), e);
        }
    }
}
