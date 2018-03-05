import static org.junit.Assert.fail;

import java.lang.reflect.Field;



public class ServerParameters {

    public static void setCaptchaId(Client client, String captchaIdValue) {
        try {
            client.newCaptcha();
            Captcha captcha = client.getCaptcha();
            Field f = captcha.getClass().getDeclaredField("captchaId");
            f.setAccessible(true);
            f.set(captcha, captchaIdValue);
        } catch (Exception e) {
            fail();
        }
    }

    public static void setToken(Client client, String tokenValue) {
        try {
            Field f = client.getClass().getDeclaredField("token");
            f.setAccessible(true);
            f.set(client, tokenValue);
        } catch (Exception e) {
            fail();
        }
    }

    public static void setCaptchaIdAndAnswer(Client client, String captchaIdValue, String answerValue) {
        try {
            client.newCaptcha();
            Captcha captcha = client.getCaptcha();
            Field f = captcha.getClass().getDeclaredField("captchaId");
            f.setAccessible(true);
            f.set(captcha, captchaIdValue);
            f = captcha.getClass().getDeclaredField("answer");
            f.setAccessible(true);
            f.set(captcha, answerValue);
        } catch (Exception e) {
            fail();
        }
    }

}
