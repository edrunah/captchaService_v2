import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import java.util.List;
import java.util.Map;

public class DefaultResponse implements IResponser {

    public Response generateResponse(Map<String, List<String>> parameters) {
        return NanoHTTPD.newFixedLengthResponse(Status.NOT_FOUND, "text/plain", "Not found\n");
    }
}
