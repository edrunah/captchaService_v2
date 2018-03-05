import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class CaptchaImageCreator implements ImageCreator {

    public static void loadAWTLibraries() {
        new CaptchaImageCreator().create("aaaa");
    }

    public BufferedImage create(String text) {
        BufferedImage img = new BufferedImage(180, 56, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
            RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
            RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
            RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setColor(Color.BLACK);
        g2d.fill(new Rectangle(0, 0, img.getWidth(), img.getHeight()));
        Font font = new Font("Arial", Font.PLAIN, 48);
        g2d.setFont(font);
        g2d.setColor(Color.YELLOW);
        FontMetrics fm = g2d.getFontMetrics();
        int stringWidth = fm.stringWidth(text);
        g2d.drawString(text, (img.getWidth() - stringWidth) / 2, fm.getAscent());
        g2d.dispose();
        return img;
    }
}
