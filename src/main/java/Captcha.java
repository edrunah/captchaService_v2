import com.alibaba.fastjson.annotation.JSONField;

public class Captcha {

    @JSONField(name = "request")
    private String captchaId;

    private String answer;

    public Captcha() {
    }

    public String getCaptchaId() {
        return captchaId;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean hasAnswer(String receivedAnswer) {
        if (answer == null) {
            return false;
        }
        return answer.equals(receivedAnswer);
    }

    public void initialize() {
        StringGenerator g = new RandomStringGenerator();
        captchaId = g.generate(8);
        answer = g.generate(4);
    }

}
