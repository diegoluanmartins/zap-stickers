import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class App {
    public static void main(String[] args) throws Exception {
        /*
         * Load API data with HTTP connection
        */
        API api = API.IMDB_TOP_MOVIES;
        ImageExtractor imageExtractor = api.getImageExtractor();
        HttpClientController httpClient = new HttpClientController();
        String bodyString = httpClient.executeGET(api.getUrl());

        /*
         * Extract json data
        */
        List<Image> imagesList = imageExtractor.extractImages(bodyString);

        /*
         * Manipulate and show data
         *  
        */
        int maxTitleLength = Collections.max(imagesList, Comparator.comparing(obj -> obj.title().length())).title().length();
        int maxUrlLength = Collections.max(imagesList, Comparator.comparing(obj -> obj.url().length())).url().length();
        StickerFactory sf = new StickerFactory();
        for (Image image : imagesList) {
            String imageTitle = image.title() + String.join("", Collections.nCopies(maxTitleLength - image.title().length(), " "));
            String imageUrl = image.url() + String.join("", Collections.nCopies(maxUrlLength - image.url().length(), " "));
            System.out.println(imageTitle + "\t|\t" + imageUrl);
            InputStream watermark = new FileInputStream(new File("resources/watermark-devil.png"));
            sf.create(new URL(imageUrl).openStream(), imageTitle, watermark); 
        }
    }
}
