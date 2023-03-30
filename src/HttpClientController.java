import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class HttpClientController {
    
    public String executeGET(String url){
        String body;
        try {
            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create(url);
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            body = response.body();
        } catch (Exception e) {
            body = "";
        }
        return body;
    }

}
