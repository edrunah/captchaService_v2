import fi.iki.elonen.NanoHTTPD.Method;

public class ActionSwitch {

    private ActionType uriType;

    public ActionSwitch(Method method, String uri) {
        uriType = ActionType.getType(method, uri);
    }

    public IResponser selectResponseAction() {
        switch (uriType) {
            case CLIENT_REGISTER:
                return new Registry();
            case CAPTCHA_NEW:
                return new NewCaptcha();
            case CAPTCHA_IMAGE:
                return new ImageResponse();
            case CAPTCHA_SOLVE:
                return new Solve();
            case CAPTCHA_VERIFY:
                return new Verify();
            default:
                return new DefaultResponse();
        }
    }
}
