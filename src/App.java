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
         * Extract json data
        */
        JsonParser jsonParser = new JsonParser();
        List<Map<String, String>> imagesList = jsonParser.parse(body);

        /*
         * Manipulate and show data
         *  
        */
        int maxTitleLength = Collections.max(imagesList, Comparator.comparing(obj -> obj.get("title").length())).get("title").length();
        int maxUrlLength = Collections.max(imagesList, Comparator.comparing(obj -> obj.get("image").length())).get("image").length();
        int maxRatingLength = Collections.max(imagesList, Comparator.comparing(obj -> obj.get("imDbRating").length())).get("imDbRating").length();
        StickerFactory sf = new StickerFactory();
        for (Map<String,String> image : imagesList) {
            String imageTitle = image.get("title") + String.join("", Collections.nCopies(maxTitleLength - image.get("title").length(), " "));
            String imageUrl = image.get("image") + String.join("", Collections.nCopies(maxUrlLength - image.get("image").length(), " "));
            String imageRating = image.get("imDbRating") + String.join("", Collections.nCopies(maxRatingLength - image.get("imDbRating").length(), " "));
            System.out.println(imageTitle + "\t|\t" + imageRating +  "\t|\t" + imageUrl);
            InputStream watermark;
            if(Double.parseDouble(imageRating) >= 9d){
                watermark = new FileInputStream(new File("resources/watermark-devil.png"));
            } else{
                watermark = new FileInputStream(new File("resources/watermark-angel.png"));
            }
            sf.create(new URL(imageUrl).openStream(), imageTitle, watermark); 
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
