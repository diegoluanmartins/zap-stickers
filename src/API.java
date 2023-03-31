import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public enum API {
    IMDB_TOP_MOVIES("https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/TopMovies.json","imdb-key-mock", new ImageExtractorImdb()),
    NASA_APOD("https://api.nasa.gov/planetary/apod?api_key=","nasa-key", new ImageExtractorNasa()),;

    private String url;
    private ImageExtractor imageExtractor;

    public ImageExtractor getImageExtractor() {
        return imageExtractor;
    }

    public String getUrl() {
        return url;
    }

    API(String url, String key, ImageExtractor imageExtractor){
        this.url = url  + getApiKey(key);
        this.imageExtractor = imageExtractor;
    }

    private static String getApiKey(String key){
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("resources/app.properties"));
        } catch (IOException e) {
            return "";
        }

        // Get a property value
        String property = props.getProperty(key);
        if(property == null) property = "";
        return property;
    }
}
