import com.alibaba.fastjson.annotation.JSONField;
import java.util.Timer;
import java.util.TimerTask;

public class Client {

    public static int NUM_CHARS_TOKEN = 8;

    public static long TIME_TO_LIVE;

    private Timer timer;

    static {
        try {
            TIME_TO_LIVE = Integer.parseInt(System.getProperty("ttl")) * 1000; // в миллисекундах
            if (TIME_TO_LIVE < 0) {
                TIME_TO_LIVE *= (-1);
            }
        } catch (NumberFormatException e) {
            TIME_TO_LIVE = 60 * 1000;
        }
    }

    @JSONField(name = "response")
    private String token;

    private Captcha captcha;

    public void newCaptcha() {
        captcha = new Captcha();
        captcha.initialize();
        timer = new Timer();
        timer.schedule(new CaptchaKill(), TIME_TO_LIVE);
    }

    public Captcha getCaptcha() {
        return captcha;
    }

    public boolean hasCaptchaId(String receivedCaptchaId) {
        try {
            String captchaId = captcha.getCaptchaId();
            return captchaId.equals(receivedCaptchaId);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean captchaHasAnswer(String receivedAnswer) {
        try {
            String answer = captcha.getAnswer();
            return answer.equals(receivedAnswer);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public void deleteCaptcha() {
        captcha = null;
        timer.cancel();
    }

    public void generateToken() {
        token = new RandomStringGenerator().generate(NUM_CHARS_TOKEN);
    }

    public String getToken() { // для fastJson
        return token;
    }

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
            captcha = null;
        }
    }
}
