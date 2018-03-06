import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import org.junit.Test;

public class CaptchaImageCreatorTest {

    @Test
    public void create() {
        try {
            CaptchaImageCreator creator = new CaptchaImageCreator();

            BufferedImage image = creator.create("mwq6");
            OutputStream out = new FileOutputStream("image.png");
            ImageIO.write(image, "png", out);
            BufferedImage readImage = ImageIO.read(new File("image.png"));

            assertEquals(180, readImage.getWidth());
            assertEquals(56, readImage.getHeight());
        } catch (IOException e) {
            fail("Ошибка создания картинки");
        }
    }
}