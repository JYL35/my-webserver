package mywebserver.view;

import java.net.InetAddress;

public class OutputView {

    public static void printServerStart(int port) {
        printMessage("웹서버가 " + port + " 포트에서 시작되었습니다.");
        printMessage("http://localhost:" + port + " 로 접속해 보세요.");
    }

    public static void printClientConnected(InetAddress address) {
        printMessage("클라이언트가 연결되었습니다. (IP: " + address + ")");
    }

    public static void printException(String message, Exception e) {
        System.err.println(message + ": " + e.getMessage());
    }

    private static void printMessage(String message) {
        System.out.println(message);
    }
}
