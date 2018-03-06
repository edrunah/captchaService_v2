import com.alibaba.fastjson.annotation.JSONField;

public class Captcha {

    public static final int NUM_CHARS_CAPTCHAID = 8;

    public static final int NUM_CHARS_ANSWER = 4;

    @JSONField(name = "request")
    private String captchaId;

    private String answer;

    public String getCaptchaId() {
        return captchaId;
    }

    public String getAnswer() {
        return answer;
    }

    public void initialize() {
        StringGenerator g = new RandomStringGenerator();
        captchaId = g.generate(NUM_CHARS_CAPTCHAID);
        answer = g.generate(NUM_CHARS_ANSWER);
    }

}
