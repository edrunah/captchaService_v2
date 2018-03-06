import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import com.alibaba.fastjson.JSON;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.IStatus;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class SolveTest {

    private IResponser responser;

    private String PUBLIC_UUID_STRING = "c88c4314-0e11-3a44-9a4b-dd2eba64c868";

    private String CAPTCHAID = "12345678";

    private String ANSWER = "1234";

    private Captcha captcha;

    private Map<String, List<String>> parameters;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() {
        responser = new Solve();
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
        RequestParameters.initParameter(parameters, "answer", ANSWER);

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
        RequestParameters.initParameter(parameters, "public", PUBLIC_UUID_STRING);
        RequestParameters.initParameter(parameters, "request", CAPTCHAID);

        Response response = responser.generateResponse(parameters);
        IStatus status = response.getStatus();
        String mimeType = response.getMimeType();
        String body = BodyMaker.getBody(response);

        assertEquals(Status.BAD_REQUEST, status);
        assertEquals("text/plain", mimeType);
        assertEquals("Not enough data\n", body);
    }

    @Test
    public void rightPairRequestAnswer() {
        ServerParameters.setObjectField(captcha, "captchaId", CAPTCHAID);
        ServerParameters.setObjectField(captcha, "answer", ANSWER);
        RequestParameters.initParameter(parameters, "public", PUBLIC_UUID_STRING);
        RequestParameters.initParameter(parameters, "request", CAPTCHAID);
        RequestParameters.initParameter(parameters, "answer", ANSWER);

        Response response = responser.generateResponse(parameters);
        IStatus status = response.getStatus();
        String mimeType = response.getMimeType();
        String body = BodyMaker.getBody(response);
        assertNotEquals("Правильный ответ не принят", "Wrong pair request-answer\n", body);
        Map<String, Object> responseBody = JSON.parseObject(body);
        String responseToken = (String) responseBody.get("response");

        assertEquals("Неверный статус отклика", Status.OK, status);
        assertEquals("Неверный Mime-type", "application/json", mimeType);
        assertEquals("Длина response неверна", Client.NUM_CHARS_TOKEN, responseToken.length());
    }

    @Test
    public void twoTimesRightPairRequestAnswer() {
        ServerParameters.setObjectField(captcha, "captchaId", CAPTCHAID);
        ServerParameters.setObjectField(captcha, "answer", ANSWER);
        RequestParameters.initParameter(parameters, "public", PUBLIC_UUID_STRING);
        RequestParameters.initParameter(parameters, "request", CAPTCHAID);
        RequestParameters.initParameter(parameters, "answer", ANSWER);

        Response responseOne = responser.generateResponse(parameters);
        Response responseTwo = responser.generateResponse(parameters);
        IStatus status = responseTwo.getStatus();
        String mimeType = responseTwo.getMimeType();
        String body = BodyMaker.getBody(responseTwo);

        assertEquals("Неверный статус отклика", Status.BAD_REQUEST, status);
        assertEquals("Неверный Mime-type", "text/plain", mimeType);
        assertEquals("Wrong pair request-answer\n", body);
    }

    @Test
    public void wrongPairRequestAnswer() {
        RequestParameters.initParameter(parameters, "public", PUBLIC_UUID_STRING);
        RequestParameters.initParameter(parameters, "request", CAPTCHAID);
        RequestParameters.initParameter(parameters, "answer", ANSWER);

        Response response = responser.generateResponse(parameters);
        IStatus status = response.getStatus();
        String mimeType = response.getMimeType();
        String body = BodyMaker.getBody(response);

        assertEquals("Неверный статус отклика", Status.BAD_REQUEST, status);
        assertEquals("Неверный Mime-type", "text/plain", mimeType);
        assertEquals("Wrong pair request-answer\n", body);
    }

}