import fi.iki.elonen.NanoHTTPD.Response;
import java.util.List;
import java.util.Map;

public interface IResponser {

    public Response generateResponse(Map<String, List<String>> parameters);

}
