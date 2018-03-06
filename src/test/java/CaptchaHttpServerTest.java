import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.alibaba.fastjson.JSON;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Method;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.IStatus;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CaptchaHttpServerTest {

    private CaptchaHttpServer server;

    private String PUBLIC_UUID_STRING = "c88c4314-0e11-3a44-9a4b-dd2eba64c868";

    private String CAPTCHAID = "12345678";

    private String ANSWER = "1234";

    private Map<String, List<String>> parameters;

    private Captcha captcha;

    @Before
    public void setUp() {
        server = new CaptchaHttpServer();
        UUID publicKey = UUID.fromString(PUBLIC_UUID_STRING);
        ClientStorage storage = ClientStorage.getInstance();
        Client client = new Client();
        storage.addNewClient(publicKey, client);
        client.newCaptcha();
        captcha = client.getCaptcha();
        parameters = new HashMap<>();
    }

    @After
    public void tearDown() {
        ClientStorage storage = ClientStorage.getInstance();
        UUID publicKey = UUID.fromString(PUBLIC_UUID_STRING);
        storage.removeClient(publicKey);
    }

    @Test
    public void serveGet() {
        IHTTPSession session = mock(IHTTPSession.class);
        when(session.getMethod()).thenReturn(Method.GET);
        when(session.getUri()).thenReturn("/captcha/image");
        when(session.getParameters()).thenReturn(parameters);
        RequestParameters.initParameter(parameters, "public", PUBLIC_UUID_STRING);
        RequestParameters.initParameter(parameters, "request", CAPTCHAID);
        ServerParameters.setObjectField(captcha, "captchaId", CAPTCHAID);

        Response response = server.serve(session);
        IStatus status = response.getStatus();
        String mimeType = response.getMimeType();
        String body = BodyMaker.getBody(response);

        assertEquals("Неверный статус отклика", Status.CREATED, status);
        assertEquals("Неверный Mime-type", "image/png", mimeType);
    }

    @Test
    public void servePost() {
        IHTTPSession session = mock(IHTTPSession.class);
        when(session.getMethod()).thenReturn(Method.POST);
        when(session.getUri()).thenReturn("/captcha/solve");
        when(session.getParameters()).thenReturn(parameters);
        RequestParameters.initParameter(parameters, "public", PUBLIC_UUID_STRING);
        RequestParameters.initParameter(parameters, "request", CAPTCHAID);
        RequestParameters.initParameter(parameters, "answer", ANSWER);
        ServerParameters.setObjectField(captcha, "captchaId", CAPTCHAID);
        ServerParameters.setObjectField(captcha, "answer", ANSWER);

        Response response = server.serve(session);
        IStatus status = response.getStatus();
        String mimeType = response.getMimeType();
        String body = BodyMaker.getBody(response);
        Map<String, Object> responseBody = JSON.parseObject(body);
        String responseToken = (String) responseBody.get("response");

        assertEquals("Неверный статус отклика", Status.OK, status);
        assertEquals("Неверный Mime-type", "application/json", mimeType);
        assertEquals("Длина response неверна", Client.NUM_CHARS_TOKEN, responseToken.length());
    }

}