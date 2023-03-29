import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;


public class StickerFactory {

    private static final String INPUT = "resources/";
    private static final String OUTPUT = "resources/output/";

    public enum ImageExtension  {
        PNG("png"),
        JPG("jpg"),
        BMP("bmp");

        private String description;
        ImageExtension(String description){
            this.description = description;
        }
        public String getDescription(){
            return description;
        }

    }
    
    public StickerFactory() {
    
    }
    public void create(InputStream sourceImage) throws IOException{
        this.create(sourceImage, "");
    } 

    public void create(InputStream sourceImage, String text) throws IOException{
        /*
         * Clean source text
         */
        text = text.trim();
        
        /*
         * Read image
         */
        BufferedImage original = ImageIO.read(sourceImage);

        /*
        * Create new resized image
        */
        int orgWidth = original.getWidth();
        int orgHeight = original.getHeight();
        int rsdHeigth = Math.round(orgHeight * 1.2f);
        BufferedImage resized = new BufferedImage(orgWidth, rsdHeigth, BufferedImage.TRANSLUCENT);

        /*
         * Copy source image to resized image
        */
        Graphics2D graphics = (Graphics2D) resized.getGraphics();
        graphics.drawImage(original, 0, 0, null);

        /*
         * Configure text font
         */
        if(text.length() > 0){
            Font font = new Font(Font.SERIF, Font.BOLD, getFontSize(graphics, text, Font.SERIF, Font.BOLD, orgHeight, orgWidth));
            graphics.setFont(font);
            graphics.setColor(Color.RED);           

        /*
        * Write text
        */
            graphics.drawString(text, (orgWidth - graphics.getFontMetrics().stringWidth(text))/2 , rsdHeigth - (rsdHeigth-orgHeight)/2f + font.getSize()/3);

        }

        /*
         * Save image
         */
        ImageIO.write(resized, ImageExtension.PNG.description, new File(this.getFileName(text, ImageExtension.PNG.description)));
    }

    /**
     * Create file name
     */
    private String getFileName(String text, String extension){
        return OUTPUT + text.toLowerCase()
        .replaceAll(" ", "-")
        .replaceAll("[^A-Za-z0-9-]", "")
         + "." + extension;
    }


    private int getFontSize(Graphics2D graphics, String text, String fontName, int fontStyle, int height, int width){
        Font baseFont = new Font(fontName, fontStyle, Math.round(Math.min(height * 1.2f, (width*1.4f)/(text.length()-1))));
        graphics.setFont(baseFont);        
        int fontSize = (int) Math.floor(((Double.valueOf(width))/graphics.getFontMetrics().stringWidth(text))*baseFont.getSize());
        return fontSize;
    }


    /**
     * Main used only to test the class functions and structure
     */
    public static void main(String[] args) throws IOException {
        StickerFactory stickerFactory = new StickerFactory();
        InputStream sourceImage;
        List<String> texts = new ArrayList<>(Arrays.asList(
            "888888",
             "888889",
             "889",
             "888888888888 8 8 88 9",
             "8.8.8.8.8.8....89"
             ));
        for (String text : texts) {
            sourceImage = new FileInputStream(INPUT + "shawshank.jpg");
            stickerFactory.create(sourceImage, text);
        }
    }
}
