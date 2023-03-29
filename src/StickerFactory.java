import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;


public class StickerFactory {

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
            // AffineTransform affinetransform = new AffineTransform();     
            // FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
            // Font font = new Font(Font.SERIF, Font.PLAIN, 12);
            // int textwidth = (int)(font.getStringBounds(text, frc).getWidth());
            // int textheight = (int)(font.getStringBounds(text, frc).getHeight());
            Font baseFont = new Font(Font.SERIF, Font.BOLD, Math.round(Math.min(orgHeight * 1.2f, (orgWidth*1.4f)/(text.length()-1))));
            graphics.setFont(baseFont);
            graphics.setColor(Color.RED);
            
            int fontSize = (int) Math.floor(((new Double(orgWidth))/graphics.getFontMetrics().stringWidth(text))*baseFont.getSize());
            Font rsdFont = new Font(Font.SERIF, Font.BOLD, fontSize);
            graphics.setFont(rsdFont);
            
            System.out.println("Base Font size: " + baseFont.getSize());
            System.out.println("Rsd Font size: " + rsdFont.getSize());
            System.out.println("Font size: " + fontSize);
            System.out.println("Text width:" + graphics.getFontMetrics().stringWidth(text));
            System.out.println("Image width:" + orgWidth);

        /*
        * Write text
        */
            graphics.drawString(text, (orgWidth - graphics.getFontMetrics().stringWidth(text))/2 , rsdHeigth - (rsdHeigth-orgHeight)/2f + rsdFont.getSize()/3);

        }

        /*
         * Save image
         */
        ImageIO.write(resized, "png", new File("resources/output/" + System.currentTimeMillis() + ".png"));
    }

    /**
     * Main used only to test the class functions and structure
     */
    public static void main(String[] args) throws IOException {
        StickerFactory stickerFactory = new StickerFactory();
        InputStream sourceImage = new FileInputStream("resources/shawshank.jpg");
        stickerFactory.create(sourceImage, "8888888888888888888888889");
        sourceImage = new FileInputStream("resources/shawshank.jpg");
        stickerFactory.create(sourceImage, "888888888888888889");
        sourceImage = new FileInputStream("resources/shawshank.jpg");
        stickerFactory.create(sourceImage, "8888888888888888888888888888889");
        sourceImage = new FileInputStream("resources/shawshank.jpg");
        stickerFactory.create(sourceImage, "88889");
        sourceImage = new FileInputStream("resources/shawshank.jpg");
        stickerFactory.create(sourceImage, "888888888889");
        sourceImage = new FileInputStream("resources/shawshank.jpg");
        stickerFactory.create(sourceImage, "8888888888888888888889");
    }
}
