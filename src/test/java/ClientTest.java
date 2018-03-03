import static org.junit.Assert.*;

import java.lang.reflect.Field;
import org.junit.Before;
import org.junit.Test;

public class ClientTest {

    Client client;

    @Before
    public void setUp() throws Exception {
        client = new Client();
    }

    @Test
    public void newCaptcha() {
        client.newCaptcha();

//        Field f = client.getClass().getDeclaredField("captcha");
    }

    @Test
    public void getCaptcha() {
        try {
            Field f = client.getClass().getDeclaredField("captcha");
            f.setAccessible(true);
            f.set(client, new Captcha());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void hasCaptchaId() {
    }

    @Test
    public void deleteCaptcha() {
    }

    @Test
    public void generateToken() {
    }

    @Test
    public void getToken() {
    }

    @Test
    public void hasToken() {
    }

    @Test
    public void deleteToken() {
    }
}