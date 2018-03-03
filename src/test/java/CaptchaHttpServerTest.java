import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.alibaba.fastjson.JSON;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Method;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.IStatus;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class CaptchaHttpServerTest {

    private String PUBLIC_UUID_STRING = "c88c4314-0e11-3a44-9a4b-dd2eba64c868";

    @Test
    public void serve() {
        CaptchaHttpServer server = new CaptchaHttpServer();
        IHTTPSession session = mock(IHTTPSession.class);
        when(session.getMethod()).thenReturn(Method.GET);
        when(session.getUri()).thenReturn("/captcha/new");
        Map<String, List<String>> parameters = new HashMap<>();
        List<String> parameter = new LinkedList<>();
        parameter.add(PUBLIC_UUID_STRING);
        parameters.put("public", parameter);
        when(session.getParameters()).thenReturn(parameters);

        Response response = server.serve(session);

    }
}