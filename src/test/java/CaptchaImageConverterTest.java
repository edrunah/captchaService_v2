import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.junit.Test;

public class CaptchaImageConverterTest {

    @Test
    public void convertImageToStream() throws IOException {
        CaptchaImageConverter convert = new CaptchaImageConverter();
        CaptchaImageCreator creator = new CaptchaImageCreator();
        BufferedImage testImage = creator.create("abcd");

        InputStream is = convert.convertImageToStream(testImage);

        try {
            BufferedImage image = ImageIO.read(is);
        } catch (Exception e) {
            fail();
        }
    }
}
