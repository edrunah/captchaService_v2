import com.alibaba.fastjson.annotation.JSONField;
import java.util.Timer;
import java.util.TimerTask;

public class Client {

    public static int NUM_CHARS_TOKEN = 8;

    public static long TIME_TO_LIVE;

    static {
        try {
            TIME_TO_LIVE = Integer.parseInt(System.getProperty("ttl")) * 1000; // в миллисекундах
        } catch (NumberFormatException e) {
            TIME_TO_LIVE = 15 * 1000;
        }
    }

    @JSONField(name = "response")
    private String token;

    private Captcha captcha;

    public void newCaptcha() {
        captcha = new Captcha();
        captcha.initialize();
        Timer timer = new Timer();
        timer.schedule(new CaptchaKill(), TIME_TO_LIVE);
    }

    public Captcha getCaptcha() {
        return captcha;
    }

    public boolean hasCaptchaId(String receivedCaptchaId) {
        if (captcha == null) {
            return false;
        }
        String captchaId = captcha.getCaptchaId();
        return captchaId.equals(receivedCaptchaId);
    }

    public void deleteCaptcha() {
        captcha = null;
    }

    public void generateToken() {
        token = new RandomStringGenerator().generate(NUM_CHARS_TOKEN);
    }

    public String getToken() { // для перевода в JSON
        return token;
    } // для fastJson

    public boolean hasToken(String receivedToken) {
        if (token == null) {
            return false;
        }
        return token.equals(receivedToken);
    }

    public void deleteToken() {
        token = null;
    }

    private class CaptchaKill extends TimerTask {

        @Override
        public void run() {
            deleteCaptcha();
            System.out.println("Captcha killed");
        }
    }
}
