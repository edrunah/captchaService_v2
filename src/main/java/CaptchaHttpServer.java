import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;

public class CaptchaHttpServer extends NanoHTTPD {

    private static final Integer DEFAULT_PORT = 8080;

    private static final Integer DEFAULT_THREADS_NUMBER = 10;

    private static final Logger logger = Logger.getLogger(CaptchaHttpServer.class.getName());

    public CaptchaHttpServer(int port) {
        super(port);
    }

    public static void main(String[] args) {
        try {
            CaptchaImageCreator.loadAWTLibraries();

            Options options = new Options();
            options.addOption("p", "port", true, "port number to use");
            options.addOption("t", "threads", true, "maximum number of threads for the server");

            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            int port = Integer.parseInt(cmd.getOptionValue("p", DEFAULT_PORT.toString()));
            int webThreads = Integer
                .parseInt(cmd.getOptionValue("t", DEFAULT_THREADS_NUMBER.toString()));

            CaptchaHttpServer app = new CaptchaHttpServer(port);
            app.setAsyncRunner(new BoundRunner(Executors.newFixedThreadPool(webThreads)));
            app.start(50, false);
            logger.info("waiting for connections on port " + port + " using a maximum of " +
                webThreads + " web threads\n"
                + "Press Ctrl+C to quit");
        } catch (IllegalArgumentException | UnrecognizedOptionException e) {
            logger.log(Level.SEVERE, "Incorrect server options");
            System.exit(1);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Couldn't start server", e);
            System.exit(1);
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        String uri = session.getUri();
        CaptchaHttpServer.logger.log(Level.FINE, method + " '" + uri + "' ");

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
        Responser action = switcher.selectResponseAction();
        Response response = action.generateResponse(session.getParameters());
        return response;
    }
}
