import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class ClientTest {

    Client client;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() {
        if (testName.getMethodName().equals("captchaTimeIsOver")) {
            System.setProperty("ttl", "0");
        }
        client = new Client();
    }

    @Test
    public void newCaptchaNotNull() {
        client.newCaptcha();
        Captcha captcha = client.getCaptcha();

        assertNotNull(captcha);
    }

    @Test
    public void captchaTimeIsOver() throws InterruptedException {
        client.newCaptcha();
//        for (int i = 0; i < 1000000; i++) {
//            new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
//        }
        Captcha captcha = client.getCaptcha();

        assertNull(captcha);
    }

    @Test
    public void hasNullAnswer() {
        boolean clientHasAnswer = client.captchaHasAnswer("");

        assertTrue(!clientHasAnswer);
    }

    @Test
    public void deleteCaptcha() {
        client.newCaptcha();
        client.deleteCaptcha();
        Captcha captcha = client.getCaptcha();

        assertNull(captcha);
    }

    @Test
    public void generateToken() {
        client.generateToken();
        String token = client.getToken();

        assertNotNull(token);
        assertTrue(token.length() == Client.NUM_CHARS_TOKEN);
    }

    @Test
    public void hasNullToken() {
        boolean clientHasToken = client.hasToken("");

        assertTrue(!clientHasToken);
    }

    @Test
    public void deleteToken() {
        client.generateToken();
        client.deleteToken();
        String token = client.getToken();

        assertNull(token);
    }
}