import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.util.ServerRunner;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CaptchaHttpServer extends NanoHTTPD {

    private static final Logger LOG = Logger.getLogger(CaptchaHttpServer.class.getName());

    public CaptchaHttpServer() {
        super(8080);
    }

    public static void main(String[] args) {
        CaptchaImageCreator.loadAWTLibraries();

        try {
            CaptchaImageCreator.loadAWTLibraries();
            Options options = new Options();
            options.addOption("port", true, "port number to use");
            options.addOption("web_threads", true, "maximum number of threads for the web server");

            int port = 8080;
            int webThreads = 10;

            App app = new App(port);
            app.setAsyncRunner(new BoundRunner(Executors.newFixedThreadPool(webThreads)));
            app.start(100, false);
            logger.info("waiting for connections on port " + port + " using a maximum of " +
                webThreads + " web threads");

        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Couldn't start server", t);
            System.exit(1);
        }

//        ServerRunner.run(CaptchaHttpServer.class);
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
