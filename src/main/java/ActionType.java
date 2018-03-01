import fi.iki.elonen.NanoHTTPD.Method;

public enum ActionType {

    CLIENT_REGISTER(Method.POST, "/client/register"),
    CAPTCHA_NEW(Method.GET, "/captcha/new"),
    CAPTCHA_IMAGE(Method.GET, "/captcha/image"),
    CAPTCHA_SOLVE(Method.POST, "/captcha/solve"),
    CAPTCHA_VERIFY(Method.GET, "/captcha/verify"),
    OTHER(Method.GET, "");

    private final Method method;

    private final String uri;

    private ActionType(Method method, String uri) {
        this.method = method;
        this.uri = uri;
    }

    static public ActionType getType(Method method, String uri) {
        for (ActionType type: ActionType.values()) {
            if (type.uri.equals(uri) && type.method.equals(method)) {
                return type;
            }
        }
        return OTHER;
    }

}
