import java.util.List;
import java.util.Map;

public class ImageExtractorPLang implements ImageExtractor {

    @Override
    public List<Image> extractImages(String json) {
        /*
         * Extract json data
        */
        JsonParser jsonParser = new JsonParser();
        List<Map<String, String>> attributesList = jsonParser.parse(json);
        return attributesList.stream().map(attribute -> 
            new Image(attribute.get("title"),attribute.get("image"))
        ).toList();
    }

}
