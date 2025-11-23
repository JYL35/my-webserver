package mywebserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

// 반드시 Application.java를 실행 후 테스트 해주세요.
public class ConcurrentLoadTest {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;
    private static final int TOTAL_REQUESTS = 100;
    private static final int CONCURRENT_USERS = 20;

    private static final AtomicInteger successCount = new AtomicInteger(0);
    private static final AtomicInteger failCount = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("부하 테스트 시작 (Total: " + TOTAL_REQUESTS + ")");
        ExecutorService clientPool = Executors.newFixedThreadPool(CONCURRENT_USERS);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(TOTAL_REQUESTS);

        long startTime = System.currentTimeMillis();

        submitTasks(clientPool, startLatch, doneLatch);
        waitForCompletion(startLatch, doneLatch);

        printReport(System.currentTimeMillis() - startTime);
        clientPool.shutdown();
    }

    private static void submitTasks(ExecutorService pool, CountDownLatch start, CountDownLatch done) {
        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            pool.execute(() -> executeTask(start, done));
        }
    }

    private static void executeTask(CountDownLatch start, CountDownLatch done) {
        try {
            start.await();
            sendRequestAndVerify();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            done.countDown();
        }
    }

    private static void waitForCompletion(CountDownLatch start, CountDownLatch done) throws InterruptedException {
        start.countDown();
        done.await();
    }

    private static void sendRequestAndVerify() {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             OutputStream out = socket.getOutputStream();
             InputStream in = socket.getInputStream()) {

            sendRequest(out);
            verifyResponse(in);

        } catch (IOException e) {
            failCount.incrementAndGet();
        }
    }

    private static void sendRequest(OutputStream out) throws IOException {
        String request = "GET / HTTP/1.1\r\n"
                + "Host: localhost\r\n"
                + "Connection: close\r\n"
                + "\r\n";
        out.write(request.getBytes(StandardCharsets.UTF_8));
        out.flush();
    }

    private static void verifyResponse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String statusLine = br.readLine();

        if (statusLine != null && statusLine.contains("200 OK")) {
            successCount.incrementAndGet();
            return;
        }
        failCount.incrementAndGet();
    }

    private static void printReport(long duration) {
        System.out.println("\n========================================");
        System.out.println("부하 테스트 결과 리포트");
        System.out.println("========================================");
        System.out.println("총 소요 시간    : " + duration + " ms");
        System.out.println("성공 요청       : " + successCount.get());
        System.out.println("실패 요청       : " + failCount.get());
        System.out.println("========================================");

        printResultVerdict();
    }

    private static void printResultVerdict() {
        if (successCount.get() == TOTAL_REQUESTS) {
            System.out.println("[PASS] 서버가 모든 요청을 안정적으로 처리했습니다!");
            return;
        }
        System.out.println("[FAIL] 일부 요청 처리에 실패했습니다.");
    }
}