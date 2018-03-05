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

public class VerifyTest {

    private IResponser responser;

    private String TOKEN = "12345678";

    private String SECRET_UUID_STRING = "c88c4314-0e11-3a44-9a4b-dd2eba64c868";

    private String PUBLIC_UUID_STRING;

    private Client client;

    private Map<String, List<String>> parameters;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() {
        responser = new Verify();
        UUID secretKey = UUID.fromString(SECRET_UUID_STRING);
        UUID publicKey = new KeyGenerator().getPublicKey(secretKey);
        PUBLIC_UUID_STRING = publicKey.toString();
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
        List<String> parameterResponse = new LinkedList<>();
        parameterResponse.add(TOKEN);
        parameters.put("response", parameterResponse);

        Response response = responser.generateResponse(parameters);
        IStatus status = response.getStatus();
        String mimeType = response.getMimeType();
        String body = BodyMaker.getBody(response);

        assertEquals(Status.BAD_REQUEST, status);
        assertEquals("text/plain", mimeType);
        assertEquals("Not enough data\n", body);
    }

    @Test
    public void successFalseVerify() {
        initRequestParameters();

        Response response = responser.generateResponse(parameters);
        IStatus status = response.getStatus();
        String mimeType = response.getMimeType();
        String body = BodyMaker.getBody(response);
        Map<String, Object> responseBody = JSON.parseObject(body);

        try {
            Boolean success = (Boolean) responseBody.get("success");
            Integer errorCode = (Integer) responseBody.get("errorCode");

            assertEquals("Неверный статус отклика", Status.NOT_ACCEPTABLE, status);
            assertEquals("Неверный Mime-type", "application/json", mimeType);
            assertEquals("success", false, success.booleanValue());
            assertEquals("errorCode", 406, errorCode.intValue());
        } catch (ClassCastException e) {
            fail("Неверный тип объекта success или errorCode");
        } catch (NullPointerException e) {
            fail("В параметрах JSON отсутствует success или errorCode");
        }

    }

    @Test
    public void successTrueVerify() {
        setTokenServerValue();
        initRequestParameters();

        Response response = responser.generateResponse(parameters);
        IStatus status = response.getStatus();
        String mimeType = response.getMimeType();
        String body = BodyMaker.getBody(response);
        Map<String, Object> responseBody = JSON.parseObject(body);

        try {
            Boolean success = (Boolean) responseBody.get("success");
            Integer errorCode = (Integer) responseBody.get("errorCode");

            assertEquals("Неверный статус отклика", Status.OK, status);
            assertEquals("Неверный Mime-type", "application/json", mimeType);
            assertEquals("success", true, success.booleanValue());
            assertNull("errorCode", errorCode);
        } catch (ClassCastException e) {
            fail("Неверный тип объекта success или errorCode");
        } catch (NullPointerException e) {
            fail("В параметрах JSON отсутствует success или errorCode");
        }

    }

    @Test
    public void twoTimesSameTokenVerify() {
        setTokenServerValue();
        initRequestParameters();

        Response responseOne = responser.generateResponse(parameters);
        Response responseTwo = responser.generateResponse(parameters);
        IStatus status = responseTwo.getStatus();
        String mimeType = responseTwo.getMimeType();
        String body = BodyMaker.getBody(responseTwo);
        Map<String, Object> responseBody = JSON.parseObject(body);

        try {
            Boolean success = (Boolean) responseBody.get("success");
            Integer errorCode = (Integer) responseBody.get("errorCode");

            assertEquals("Неверный статус отклика", Status.NOT_ACCEPTABLE, status);
            assertEquals("Неверный Mime-type", "application/json", mimeType);
            assertEquals("success", false, success.booleanValue());
            assertEquals("errorCode", 406, errorCode.intValue());
        } catch (ClassCastException e) {
            fail("Неверный тип объекта success или errorCode");
        } catch (NullPointerException e) {
            fail("В параметрах JSON отсутствует success или errorCode");
        }

    }

    private void setTokenServerValue() {
        try {
            Field f = client.getClass().getDeclaredField("token");
            f.setAccessible(true);
            f.set(client, TOKEN);
        } catch (Exception e) {
            fail();
        }
    }

    private void initRequestParameters() {
        List<String> parameterSecret = new LinkedList<>();
        parameterSecret.add(SECRET_UUID_STRING);
        parameters.put("secret", parameterSecret);
        List<String> parameterResponse = new LinkedList<>();
        parameterResponse.add(TOKEN);
        parameters.put("response", parameterResponse);
    }

}