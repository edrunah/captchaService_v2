import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public interface ImageConverter {

    InputStream convertImageToStream(BufferedImage image) throws IOException;
}
