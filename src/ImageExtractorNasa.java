import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImageExtractorNasa implements ImageExtractor {
    
    @Override
    public List<Image> extractImages(String json){
        /*
         * Extract json data
        */
        List<Map<String, String>> attributesList = new JsonParser().parse(json);

        return attributesList.stream().map(attribute -> 
            new Image(attribute.get("title"),attribute.get("url"))
        ).toList();

    }
    
    public List<Image> extractImages(String json, boolean hdImage){
        /*
         * Extract json data
        */
        List<Map<String, String>> attributesList = new JsonParser().parse(json);
        return attributesList.stream().map(attribute -> 
            new Image(attribute.get("title"),attribute.get("urlhd"))
        ).toList();
    }
}
