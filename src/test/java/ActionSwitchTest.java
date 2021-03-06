import static org.junit.Assert.assertTrue;

import fi.iki.elonen.NanoHTTPD.Method;
import org.junit.Test;

public class ActionSwitchTest {

    @Test
    public void registry() {
        ActionSwitch actionSwitch = new ActionSwitch(Method.POST, "/client/register");

        Responser responser = actionSwitch.selectResponseAction();
        boolean isRegistry = responser instanceof Registry;

        assertTrue(isRegistry);
    }

    @Test
    public void newCaptcha() {
        ActionSwitch actionSwitch = new ActionSwitch(Method.GET, "/captcha/new");

        Responser responser = actionSwitch.selectResponseAction();
        boolean isNewCaptcha = responser instanceof NewCaptcha;

        assertTrue(isNewCaptcha);
    }

    @Test
    public void imageResponse() {
        ActionSwitch actionSwitch = new ActionSwitch(Method.GET, "/captcha/image");

        Responser responser = actionSwitch.selectResponseAction();
        boolean isImageResponse = responser instanceof ImageResponse;

        assertTrue(isImageResponse);
    }

    @Test
    public void solve() {
        ActionSwitch actionSwitch = new ActionSwitch(Method.POST, "/captcha/solve");

        Responser responser = actionSwitch.selectResponseAction();
        boolean isSolve = responser instanceof Solve;

        assertTrue(isSolve);
    }

    @Test
    public void verify() {
        ActionSwitch actionSwitch = new ActionSwitch(Method.GET, "/captcha/verify");

        Responser responser = actionSwitch.selectResponseAction();
        boolean isVerify = responser instanceof Verify;

        assertTrue(isVerify);
    }

    @Test
    public void defaultResponse() {
        ActionSwitch actionSwitch = new ActionSwitch(Method.GET, "/smth");

        Responser responser = actionSwitch.selectResponseAction();
        boolean isDefault = responser instanceof DefaultResponse;

        assertTrue(isDefault);
    }
}