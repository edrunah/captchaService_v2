import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ImageConverter {

    InputStream convertImageToStream(BufferedImage image) throws IOException;
}
