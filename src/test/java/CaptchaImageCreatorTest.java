import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import javax.imageio.ImageIO;
import org.junit.Test;

public class CaptchaImageCreatorTest {

    @Test
    public void create() throws IOException {
        CaptchaImageCreator creator = new CaptchaImageCreator();
        BufferedImage image = creator.create("m7A8");
        OutputStream out = new FileOutputStream("image.png");
        ImageIO.write(image, "png", out);
    }
}