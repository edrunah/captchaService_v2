import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class CaptchaTest {

    Captcha captcha;

    @Before
    public void setUp() {
        captcha = new Captcha();
    }

    @Test
    public void initialize() {
        captcha.initialize();
        String captchaId = captcha.getCaptchaId();
        String answer = captcha.getAnswer();

        assertNotNull(captchaId);
        assertNotNull(answer);
        assertEquals(Captcha.NUM_CHARS_CAPTCHAID, captchaId.length());
        assertEquals(Captcha.NUM_CHARS_ANSWER, answer.length());
    }
}