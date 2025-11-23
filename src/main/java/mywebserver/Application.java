package mywebserver;

public class Application {
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        WebServer webServer = new WebServer(DEFAULT_PORT);
        webServer.start();
    }
}
