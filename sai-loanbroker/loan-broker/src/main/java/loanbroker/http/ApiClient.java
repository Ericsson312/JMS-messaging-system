package loanbroker.http;

import com.google.gson.Gson;
import loanbroker.gateway.AgencyReplyListener;
import loanbroker.model.ClientCreditHistory;
import loanbroker.model.Archive;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ApiClient {
    private Gson gson = new Gson();
    private HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
    private HttpRequest request;
    private AgencyReplyListener agencyReplyListener;

    public ApiClient() {
    }

    public void sendToArchive(Archive archive) {
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/archive/rest/accepted"))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Accept", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(archive)))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {
            if (response.statusCode() / 100 == 2) {
                System.out.println("Loan data was successfully added to archive");
            } else {
                System.out.println("Response error " + response.statusCode());
                System.out.println(response);
            }
        });
    }

    public void sendToAgency(int ssn) {
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/credit/rest/history/" + ssn))
                .timeout(Duration.ofMinutes(1))
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {
            if (response.statusCode() / 100 == 2) {
                agencyReplyListener.onReplyReceived(gson.fromJson(response.body(), ClientCreditHistory.class));
            } else {
                System.out.println("Response error " + response.statusCode());
                System.out.println(response);
            }
        });
    }

    public void setAgencyReplyListener(AgencyReplyListener agencyReplyListener) {
        this.agencyReplyListener = agencyReplyListener;
    }
}
