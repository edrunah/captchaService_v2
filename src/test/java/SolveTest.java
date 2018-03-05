import static org.junit.Assert.*;

import com.alibaba.fastjson.JSON;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.IStatus;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import java.lang.reflect.Field;
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

public class SolveTest {

    private IResponser responser;

    private String PUBLIC_UUID_STRING = "c88c4314-0e11-3a44-9a4b-dd2eba64c868";

    private String CAPTCHAID = "12345678";

    private String ANSWER = "1234";

    private Client client;

    private Map<String, List<String>> parameters;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() {
        responser = new Solve();
        UUID publicKey = UUID.fromString(PUBLIC_UUID_STRING);
        if (!testName.getMethodName().equals("noSuchClient")) {
            ClientStorage storage = ClientStorage.getInstance();
            client = new Client();
            storage.addNewClient(publicKey, client);
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
        initRequestParameters();

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
        List<String> parameterRequest = new LinkedList<>();
        parameterRequest.add(CAPTCHAID);
        parameters.put("request", parameterRequest);

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
        setServerCaptchaIdAndAnswer();
        initRequestParameters();

        Response response = responser.generateResponse(parameters);
        IStatus status = response.getStatus();
        String mimeType = response.getMimeType();
        String body = BodyMaker.getBody(response);
        assertNotEquals("Правильный ответ не принят", "Wrong pair request-answer\n", body);
        try {
            Map<String, Object> responseBody = JSON.parseObject(body);
            String responseToken = (String) responseBody.get("response");

            assertEquals("Неверный статус отклика", Status.OK, status);
            assertEquals("Неверный Mime-type", "application/json", mimeType);
            assertEquals("Длина response неверна", Client.NUM_CHARS_TOKEN, responseToken.length());
        } catch (ClassCastException e) {
            fail("Неверный тип объекта response");
        } catch (NullPointerException e) {
            fail("В параметрах JSON отсутствует response");
        }
    }

    @Test
    public void twoTimesRightPairRequestAnswer() {
        setServerCaptchaIdAndAnswer();
        initRequestParameters();

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
        initRequestParameters();

        Response response = responser.generateResponse(parameters);
        IStatus status = response.getStatus();
        String mimeType = response.getMimeType();
        String body = BodyMaker.getBody(response);

        assertEquals("Неверный статус отклика", Status.BAD_REQUEST, status);
        assertEquals("Неверный Mime-type", "text/plain", mimeType);
        assertEquals("Wrong pair request-answer\n", body);
    }

    public void setServerCaptchaIdAndAnswer() {
        try {
            client.newCaptcha();
            Captcha captcha = client.getCaptcha();
            Field f = captcha.getClass().getDeclaredField("captchaId");
            f.setAccessible(true);
            f.set(captcha, CAPTCHAID);
            f = captcha.getClass().getDeclaredField("answer");
            f.setAccessible(true);
            f.set(captcha, ANSWER);
        } catch (Exception e) {
            fail();
        }
    }

    private void initRequestParameters() {
        List<String> parameterPublic = new LinkedList<>();
        parameterPublic.add(PUBLIC_UUID_STRING);
        parameters.put("public", parameterPublic);
        List<String> parameterRequest = new LinkedList<>();
        parameterRequest.add(CAPTCHAID);
        parameters.put("request", parameterRequest);
        List<String> parameterAnswer = new LinkedList<>();
        parameterAnswer.add(ANSWER);
        parameters.put("answer", parameterAnswer);
    }
}