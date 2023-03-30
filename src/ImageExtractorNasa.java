import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImageExtractorNasa implements ImageExtractor {
    
    @Override
    public List<Image> extractImages(String json){
        /*
         * Extract json data
        */
        JsonParser jsonParser = new JsonParser();
        List<Map<String, String>> attributesList = jsonParser.parse(json);
        List<Image> imagesList = new ArrayList<>();
        
        for (Map<String, String> attributes : attributesList) {
            String title = attributes.get("title");
            String imgUrl = attributes.get("url");
            imagesList.add(new Image(title, imgUrl));
        }
        return imagesList;
    }
    
    public List<Image> extractImages(String json, boolean hdImage){
        /*
         * Extract json data
        */
        JsonParser jsonParser = new JsonParser();
        List<Map<String, String>> attributesList = jsonParser.parse(json);
        List<Image> imagesList = new ArrayList<>();
        
        for (Map<String, String> attributes : attributesList) {
            String title = attributes.get("title");
            String imgUrl = attributes.get("urlhd");
            imagesList.add(new Image(title, imgUrl));
        }
        return imagesList;
    }
}
