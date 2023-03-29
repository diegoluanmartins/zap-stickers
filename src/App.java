import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class App {
    public static void main(String[] args) throws Exception {
        /*
         * Load IMDB data with HTTP connection
        */
        String apiKey = getImdbApiKey();
        System.out.println(apiKey);
        // String rawUrl = "https://imdb-api.com/en/API/Top250Movies/" + apiKey;
        String rawUrl = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/TopMovies.json";
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(rawUrl);
        HttpRequest request = HttpRequest.newBuilder(url).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String body = response.body();

        /*
         * Extract json data: title, poster and classification
        */
        JsonParser jsonParser = new JsonParser();
        List<Map<String, String>> moviesList = jsonParser.parse(body);

        /*
         * Manipulate and show data
         *  
        */
        int maxTitleLength = Collections.max(moviesList, Comparator.comparing(obj -> obj.get("title").length())).get("title").length();
        int maxUrlLength = Collections.max(moviesList, Comparator.comparing(obj -> obj.get("image").length())).get("image").length();
        int maxRatingLength = Collections.max(moviesList, Comparator.comparing(obj -> obj.get("imDbRating").length())).get("imDbRating").length();
        StickerFactory sf = new StickerFactory();
        for (Map<String,String> movie : moviesList) {
            String title = movie.get("title") + String.join("", Collections.nCopies(maxTitleLength - movie.get("title").length(), " "));
            String image = movie.get("image") + String.join("", Collections.nCopies(maxUrlLength - movie.get("image").length(), " "));
            String rating = movie.get("imDbRating") + String.join("", Collections.nCopies(maxRatingLength - movie.get("imDbRating").length(), " "));
            System.out.println(title + "\t|\t" + rating +  "\t|\t" + image);
            InputStream watermark;
            if(Double.parseDouble(rating) >= 9d){
                watermark = new FileInputStream(new File("resources/watermark-devil.png"));
            } else{
                watermark = new FileInputStream(new File("resources/watermark-angel.png"));
            }
            sf.create(new URL(image).openStream(), title, watermark); 
        }
    }

    private static String getImdbApiKey(){
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("resources/app.properties"));
        } catch (IOException e) {
            // Handle exception
        }

        // Get a property value
        return props.getProperty("imdb-key");
    }

}
