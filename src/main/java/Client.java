import com.alibaba.fastjson.annotation.JSONField;
import java.util.Timer;
import java.util.TimerTask;

public class Client {

    private static long TTL;
    static {
        try {
            TTL = Integer.parseInt(System.getProperty("ttl")) * 1000; // в миллисекундах
            System.out.println("TTL set in " + TTL/1000 + " seconds");
        } catch (NumberFormatException e) {
            TTL = 15 * 1000;
            System.out.println("TTL set in 15 seconds");
        }
    }

    @JSONField(name = "response")
    private String token;

    private Captcha captcha;

    public void newCaptcha() {
        captcha = new Captcha();
        captcha.initialize();
        Timer timer = new Timer();
        timer.schedule(new CaptchaKill(), TTL);
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
        token = new RandomStringGenerator().generate(4);
    }

    public String getToken() { // для перевода в JSON
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
            deleteCaptcha();
            System.out.println("Captcha killed");
        }
    }
}
