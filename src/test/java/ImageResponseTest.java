import static org.junit.Assert.assertEquals;

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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class ImageResponseTest {

    private IResponser responser;

    private String PUBLIC_UUID_STRING = "c88c4314-0e11-3a44-9a4b-dd2eba64c868";

    private String CAPTCHAID = "12345678";

    private Captcha captcha;

    private Map<String, List<String>> parameters;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() {
        responser = new ImageResponse();
        UUID publicKey = UUID.fromString(PUBLIC_UUID_STRING);
        if (!testName.getMethodName().equals("noSuchClient")) {
            ClientStorage storage = ClientStorage.getInstance();
            Client client = new Client();
            storage.addNewClient(publicKey, client);
            client.newCaptcha();
            captcha = client.getCaptcha();
        }
        parameters = new HashMap<>();
    }

    @After
    public void tearDown() {
        ClientStorage storage = ClientStorage.getInstance();
        UUID publicKey = UUID.fromString(PUBLIC_UUID_STRING);
        storage.removeClient(publicKey);
    }

    @Test
    public void noSuchClient() {
        RequestParameters.initParameter(parameters, "public", PUBLIC_UUID_STRING);
        RequestParameters.initParameter(parameters, "request", CAPTCHAID);

        Response response = responser.generateResponse(parameters);
        IStatus status = response.getStatus();
        String mimeType = response.getMimeType();
        String body = BodyMaker.getBody(response);

        assertEquals(Status.BAD_REQUEST, status);
        assertEquals("text/plain", mimeType);
        assertEquals("No such client\n", body);
    }

    @Test
    public void notEnoughData() {
        List<String> parameterPublic = new LinkedList<>();
        parameterPublic.add(PUBLIC_UUID_STRING);
        parameters.put("public", parameterPublic);

        Response response = responser.generateResponse(parameters);
        IStatus status = response.getStatus();
        String mimeType = response.getMimeType();
        String body = BodyMaker.getBody(response);

        assertEquals(Status.BAD_REQUEST, status);
        assertEquals("text/plain", mimeType);
        assertEquals("Not enough data\n", body);
    }

    @Test
    public void noSuchRequest() {
        RequestParameters.initParameter(parameters, "public", PUBLIC_UUID_STRING);
        RequestParameters.initParameter(parameters, "request", CAPTCHAID);

        Response response = responser.generateResponse(parameters);
        IStatus status = response.getStatus();
        String mimeType = response.getMimeType();
        String body = BodyMaker.getBody(response);

        assertEquals("Неверный статус отклика", Status.BAD_REQUEST, status);
        assertEquals("Неверный Mime-type", "text/plain", mimeType);
        assertEquals("Неверное тело ответа", body, "No such request\n");
    }

    @Test
    public void successfulImageCreation() {
        ServerParameters.setObjectField(captcha, "captchaId", CAPTCHAID);
        RequestParameters.initParameter(parameters, "public", PUBLIC_UUID_STRING);
        RequestParameters.initParameter(parameters, "request", CAPTCHAID);

        Response response = responser.generateResponse(parameters);
        IStatus status = response.getStatus();
        String mimeType = response.getMimeType();
        String body = BodyMaker.getBody(response);

        assertEquals("Неверный статус отклика", Status.CREATED, status);
        assertEquals("Неверный Mime-type", "image/png", mimeType);
    }

}