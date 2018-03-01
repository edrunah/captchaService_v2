import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CaptchaImageConverter implements ImageConverter {

    public InputStream convertImageToStream(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;

    }
}

