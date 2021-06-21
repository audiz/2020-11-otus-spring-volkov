import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpReq {
    volatile static AtomicInteger j = new AtomicInteger();

    public static void main(String[] args) throws Exception {

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for(int i = 0; i < 100 ; i++) {
            executorService.execute(() -> {
                try {
                    run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }



    public static void run() throws Exception {
        /*for(int i = 0; i < 30 ; i++) {

            new Thread(() -> {*/
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/api/convert"))
                        .build();

                HttpResponse<String> response = null;
                try {
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int jj = j.getAndIncrement();

                System.out.println(jj + " : " +response.body());
          /*  }).start();
        }*/
    }

}
