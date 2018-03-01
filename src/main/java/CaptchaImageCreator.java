import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.image.BufferedImage;

public class CaptchaImageCreator implements ImageCreator {

    Font font;

    public CaptchaImageCreator() {
        font = new Font("Arial", Font.PLAIN, 48);
    }

    public BufferedImage create(String text) {
        BufferedImage img = new BufferedImage(200, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);
        g2d.setBackground(Color.WHITE);
        g2d.drawString(text, 40, 65);
        g2d.dispose();
        return img;
    }
}
