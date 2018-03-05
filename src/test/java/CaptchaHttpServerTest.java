import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Method;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.IStatus;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CaptchaHttpServerTest {

    private String PUBLIC_UUID_STRING = "c88c4314-0e11-3a44-9a4b-dd2eba64c868";

    private String CAPTCHAID = "12345678";

    private Map<String, List<String>> parameters;

    private Client client;

    @Before
    public void setUp() {
        UUID publicKey = UUID.fromString(PUBLIC_UUID_STRING);
        ClientStorage storage = ClientStorage.getInstance();
        client = new Client();
        storage.addNewClient(publicKey, client);
        parameters = new HashMap<>();
    }

    @After
    public void tearDown() {
        ClientStorage storage = ClientStorage.getInstance();
        UUID publicKey = UUID.fromString(PUBLIC_UUID_STRING);
        storage.removeClient(publicKey);
    }

    @Test
    public void serve() {
        CaptchaHttpServer server = new CaptchaHttpServer();
        IHTTPSession session = mock(IHTTPSession.class);
        when(session.getMethod()).thenReturn(Method.GET);
        when(session.getUri()).thenReturn("/captcha/image");
        initRequestParameters();
        when(session.getParameters()).thenReturn(parameters);
        ServerParameters.setCaptchaId(client, CAPTCHAID);

        Response response = server.serve(session);
        IStatus status = response.getStatus();
        String mimeType = response.getMimeType();
        String body = BodyMaker.getBody(response);

        assertEquals("Неверный статус отклика", Status.CREATED, status);
        assertEquals("Неверный Mime-type", "image/png", mimeType);
    }



    private void initRequestParameters() {
        List<String> parameterPublic = new LinkedList<>();
        parameterPublic.add(PUBLIC_UUID_STRING);
        parameters.put("public", parameterPublic);
        List<String> parameterRequest = new LinkedList<>();
        parameterRequest.add(CAPTCHAID);
        parameters.put("request", parameterRequest);
    }
}