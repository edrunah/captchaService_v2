import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.util.ServerRunner;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class CaptchaHttpServer extends NanoHTTPD {

    private static final Logger LOG = Logger.getLogger(CaptchaHttpServer.class.getName());

    public CaptchaHttpServer() {
        super(8080);
    }

    public static void main(String[] args) {
        ServerRunner.run(CaptchaHttpServer.class);
    }

    @Override
    public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        String uri = session.getUri();
        CaptchaHttpServer.LOG.info(method + " '" + uri + "' ");

        Map<String, String> files = new HashMap<String, String>();
        if (Method.PUT.equals(method) || Method.POST.equals(method)) {
            try {
                session.parseBody(files);
            } catch (IOException ioe) {
                return NanoHTTPD
                    .newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain",
                        "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
            } catch (ResponseException re) {
                return NanoHTTPD
                    .newFixedLengthResponse(re.getStatus(), "text/plain", re.getMessage());
            }
        }
        ActionSwitch switcher = new ActionSwitch(method, uri);
        IResponser action = switcher.selectResponseAction();
        Response response = action.generateResponse(session.getParameters());
        return response;
    }
}
