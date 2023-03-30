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
        // String rawUrl = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY";
        String rawUrl = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/TopMovies.json";
        HttpClientController httpClient = new HttpClientController();
        String bodyString = httpClient.executeGET(rawUrl);
        System.out.println(bodyString);

        /*
         * Extract json data
        */
        ImageExtractor imageExtractor = new ImageExtractorImdb();
        List<Image> imagesList = imageExtractor.extractImages(bodyString);

        /*
         * Manipulate and show data
         *  
        */
        int maxTitleLength = Collections.max(imagesList, Comparator.comparing(obj -> obj.getTitle().length())).getTitle().length();
        int maxUrlLength = Collections.max(imagesList, Comparator.comparing(obj -> obj.getUrl().length())).getUrl().length();
        StickerFactory sf = new StickerFactory();
        for (Image image : imagesList) {
            String imageTitle = image.getTitle() + String.join("", Collections.nCopies(maxTitleLength - image.getTitle().length(), " "));
            String imageUrl = image.getUrl() + String.join("", Collections.nCopies(maxUrlLength - image.getUrl().length(), " "));
            System.out.println(imageTitle + "\t|\t" + imageUrl);
            InputStream watermark = new FileInputStream(new File("resources/watermark-devil.png"));
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
