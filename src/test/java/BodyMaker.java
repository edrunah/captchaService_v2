import static org.junit.Assert.fail;

import fi.iki.elonen.NanoHTTPD.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class BodyMaker {

    public static String getBody(Response response) {
        InputStream is = response.getData();
        Reader reader = new InputStreamReader(is);
        StringBuilder bodyBuilder = new StringBuilder();
        try {
            int c;
            while ((c = reader.read()) != -1) {
                bodyBuilder.append((char) c);
            }
        } catch (IOException e) {
            fail("Ошибка обработки тела ответа сервера");
        }
        return bodyBuilder.toString();
    }
}
