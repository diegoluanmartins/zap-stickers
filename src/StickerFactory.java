import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
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
            Font font = new Font(Font.SERIF, Font.BOLD, Math.round(Math.min(orgHeight * 1.2f, (orgWidth*1.4f)/(text.length()-1))));
            graphics.setFont(font);
            graphics.setColor(Color.RED);

        /*
        * Write text
        */
            graphics.drawString(text, 0, rsdHeigth - (rsdHeigth-orgHeight)/2f + font.getSize()/3);

        }
        /*
         * Save image
         */
        ImageIO.write(resized, "png", new File("src/resources/output/" + System.currentTimeMillis() + ".png"));
    }

    public static void main(String[] args) throws IOException {
        StickerFactory stickerFactory = new StickerFactory();
        InputStream sourceImage = new FileInputStream("src/resources/shawshank.jpg");
        stickerFactory.create(sourceImage, "8888888888888888888888889");
    }
}
