import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

public class NewCaptchaTest {

    private IResponser responser;

    private String PUBLIC_UUID_STRING = "c88c4314-0e11-3a44-9a4b-dd2eba64c868";

    private Map<String, List<String>> parameters;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() {
        responser = new NewCaptcha();
        UUID publicKey = UUID.fromString(PUBLIC_UUID_STRING);
        if (!testName.getMethodName().equals("noSuchClient")) {
            ClientStorage storage = ClientStorage.getInstance();
            storage.addNewClient(publicKey, new Client());
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
        Response response = responser.generateResponse(parameters);
        IStatus status = response.getStatus();
        String mimeType = response.getMimeType();
        String body = BodyMaker.getBody(response);

        assertEquals(Status.BAD_REQUEST, status);
        assertEquals("text/plain", mimeType);
        assertEquals("Not enough data\n", body);
    }

    @Test
    public void newCaptchaProductionIsTrue() {
        System.setProperty("production", "true");
        RequestParameters.initParameter(parameters, "public", PUBLIC_UUID_STRING);

        Response response = responser.generateResponse(parameters);
        IStatus status = response.getStatus();
        String mimeType = response.getMimeType();
        String body = BodyMaker.getBody(response);
        Map<String, Object> responseBody = JSON.parseObject(body);
        try {
            String request = (String) responseBody.get("request");
            String answer = (String) responseBody.get("answer");

            assertNotNull("В отклике отсутствует request", request);
            assertNull("В отклике есть answer", answer);
            assertEquals("Неверный статус отклика", Status.OK, status);
            assertEquals("Неверный Mime-type", "application/json", mimeType);
            assertEquals("Количество букв request неверно", Captcha.NUM_CHARS_CAPTCHAID,
                request.length());
        } catch (ClassCastException e) {
            fail("Неверный тип объекта request");
        }
    }

    @Test
    public void newCaptchaProductionNotTrue() {
        RequestParameters.initParameter(parameters, "public", PUBLIC_UUID_STRING);

        Response response = responser.generateResponse(parameters);
        IStatus status = response.getStatus();
        String mimeType = response.getMimeType();
        String body = BodyMaker.getBody(response);
        Map<String, Object> responseBody = JSON.parseObject(body);
        try {
            String request = (String) responseBody.get("request");
            String answer = (String) responseBody.get("answer");

            assertNotNull("В отклике отсутствует request", request);
            assertNotNull("В отклике отсутствует answer", answer);
            assertEquals("Неверный статус отклика", Status.OK, status);
            assertEquals("Неверный Mime-type", "application/json", mimeType);
            assertEquals("Количество букв request неверно", Captcha.NUM_CHARS_CAPTCHAID,
                request.length());
            assertEquals("Количество букв answer неверно", Captcha.NUM_CHARS_ANSWER,
                answer.length());
        } catch (ClassCastException e) {
            fail("Неверный тип объекта request или answer");
        }
    }

}