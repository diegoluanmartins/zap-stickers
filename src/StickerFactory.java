import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
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
        this.create(sourceImage, "", null);
    } 

    public void create(InputStream sourceImage, String text, InputStream watermark) throws IOException{
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
        * Configure water mark
        */
        if (watermark != null){
            BufferedImage waterMarkImage = ImageIO.read(watermark);
            float scale = Math.min((orgWidth/waterMarkImage.getWidth())*0.1f, (orgHeight/waterMarkImage.getHeight())*0.1f);
            waterMarkImage = resizeImage(waterMarkImage, Math.round(waterMarkImage.getWidth()*scale), Math.round(waterMarkImage.getHeight()*scale));

            /*
            * Write water mark
            */
            graphics.drawImage(waterMarkImage, null, Math.round(orgWidth - waterMarkImage.getWidth()*1.1f), Math.round(rsdHeigth - waterMarkImage.getHeight()*1.1f));
        }

        
        /*
         * Configure text font
         */
        if(text.length() > 0){
            Font font = new Font("Imperial", Font.BOLD, getFontSize(graphics, text, Font.SERIF, Font.BOLD, orgHeight, orgWidth));
            graphics.setFont(font);
            graphics.setColor(Color.RED); 
        
        /*
        * Write text
        */
            int textPosX = (orgWidth - graphics.getFontMetrics().stringWidth(text))/2;    
            int textPosY = Math.round(rsdHeigth - (rsdHeigth-orgHeight)/2f + font.getSize()/2.75f);
            graphics.drawString(text, textPosX, textPosY);
        
        /*
         * Outline
         */
            FontRenderContext fontRenderContext = graphics.getFontRenderContext();
            TextLayout textLayout = new TextLayout(text, font, fontRenderContext);

            Shape outline = textLayout.getOutline(null);
            AffineTransform transform = graphics.getTransform();
            transform.translate(textPosX, textPosY);
            graphics.setTransform(transform);
            
            BasicStroke stroke = new BasicStroke(font.getSize() * 0.025f);
            graphics.setStroke(stroke);
            graphics.setColor(Color.BLACK);
            graphics.draw(outline);
            graphics.setClip(outline);
        
        }

        /*
         * Save image
         */
        this.saveImage(resized, ImageExtension.PNG.description, OUTPUT, this.getFileName(text, ImageExtension.PNG.description));
    }

    public boolean saveImage(RenderedImage image, String extension, String path, String name) throws IOException{
        File folder = new File(path);
        if(!folder.exists()) folder.mkdir();
        try {
            ImageIO.write(image, ImageExtension.PNG.description, new File(name));
            return true;
        } catch (Exception e) {
            return false;
        }
        
    }

    /**
     * Create file name
     */
    private String getFileName(String text, String extension){
        return getFileName(OUTPUT, text, extension);
    }

    /**
     * Create file name
     */
    private String getFileName(String output, String text, String extension){
        return output + text.toLowerCase()
        .replaceAll(" ", "-")
        .replaceAll("[^A-Za-z0-9-]", "")
         + "." + extension;
    }


    private int getFontSize(Graphics2D graphics, String text, String fontName, int fontStyle, int height, int width){
        Font baseFont = new Font(fontName, fontStyle, Math.round((width*1.4f)/(text.length()-1)));
        graphics.setFont(baseFont);        
        int fontSize = (int) Math.floor(((Double.valueOf(width))/graphics.getFontMetrics().stringWidth(text))*baseFont.getSize());
        return Math.min(Math.round(height * 0.25f), fontSize);
    }

    /*
     * Resize image
     */
    BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        
        return resizedImage;
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
            stickerFactory.create(sourceImage, text, null);
        }
    }
}
