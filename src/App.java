import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class App {
    public static void main(String[] args) throws Exception {
        /*
         * Load IMDB data with HTTP connection
        */
        String rawUrl = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/TopMovies.json";
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(rawUrl);
        HttpRequest request = HttpRequest.newBuilder(url).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String body = response.body();
        System.out.println(body.length());


        /*
         * Extract json data: title, poster and classification
        */


        /*
         * Manipulate and show data
         *  
        */
        System.out.println("It's raining man");
    }

}
