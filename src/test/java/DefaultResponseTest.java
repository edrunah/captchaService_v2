import static org.junit.Assert.assertEquals;

import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.IStatus;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class DefaultResponseTest {

    @Test
    public void notFound() {
        Responser responser = new DefaultResponse();
        Map<String, List<String>> parameters = new HashMap<>();

        Response response = responser.generateResponse(parameters);
        IStatus status = response.getStatus();
        String mimeType = response.getMimeType();
        String body = BodyMaker.getBody(response);

        assertEquals(Status.NOT_FOUND, status);
        assertEquals("text/plain", mimeType);
        assertEquals("Not found\n", body);
    }
}