import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import org.junit.Before;
import org.junit.Test;

public class CaptchaTest {

    Captcha captcha;

    @Before
    public void setUp() {
        captcha = new Captcha();
    }

    @Test
    public void getCaptchaId() {
        try {
            String captchaIdNull = captcha.getCaptchaId();
            Field f = captcha.getClass().getDeclaredField("captchaId");
            f.setAccessible(true);
            f.set(captcha, "aaaaId");
            String captchaIdNotNull = captcha.getCaptchaId();

            assertNull(captchaIdNull);
            assertEquals("aaaaId", captchaIdNotNull);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void getAnswer() {
        try {
            String answerNull = captcha.getAnswer();
            Field f = captcha.getClass().getDeclaredField("answer");
            f.setAccessible(true);
            f.set(captcha, "aaaa");
            String answerNotNull = captcha.getAnswer();

            assertNull(answerNull);
            assertEquals("aaaa", answerNotNull);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void hasReceivedNullAnswer() {
        String checkingAnswer = "aaaa";

        boolean has = captcha.hasAnswer(checkingAnswer);

        assertTrue(!has);
    }

    @Test
    public void hasRightAnswer() {
        try {
            Field f = captcha.getClass().getDeclaredField("answer");
            f.setAccessible(true);
            f.set(captcha, "aaaa");

            boolean has = captcha.hasAnswer("aaaa");

            assertTrue(has);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void hasWrongAnswer() {
        try {
            Field f = captcha.getClass().getDeclaredField("answer");
            f.setAccessible(true);
            f.set(captcha, "bbbb");

            boolean has = captcha.hasAnswer("aaaa");

            assertTrue(!has);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void initialize() {
        captcha.initialize();
        try {
            Field f = captcha.getClass().getDeclaredField("captchaId");
            f.setAccessible(true);
            String captchaId = (String) f.get(captcha);
            f = captcha.getClass().getDeclaredField("answer");
            f.setAccessible(true);
            String answer = (String) f.get(captcha);

            assertNotNull(captchaId);
            assertNotNull(answer);
            assertEquals(Captcha.NUM_CHARS_CAPTCHAID, captchaId.length());
            assertEquals(Captcha.NUM_CHARS_ANSWER, answer.length());
        } catch (Exception e) {
            fail();
        }
    }
}