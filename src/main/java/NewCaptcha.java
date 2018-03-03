import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NewCaptcha implements IResponser {

    public Response generateResponse(Map<String, List<String>> parameters) {
        try {
            UUID receivedPublicUUID = UUID.fromString(parameters.get("public").get(0));
            ClientStorage storage = ClientStorage.getInstance();
            Client client = storage.getClient(receivedPublicUUID);
            client.newCaptcha();
            Captcha captcha = client.getCaptcha();
            SimplePropertyPreFilter filter;
            String production = System.getProperty("production");
            if (production != null && production.equals("true")) {
                filter = new SimplePropertyPreFilter(Captcha.class, "request");
            } else {
                filter = new SimplePropertyPreFilter(Captcha.class, "request", "answer");
            }
            String body = JSON.toJSONString(captcha, filter);
            return NanoHTTPD.newFixedLengthResponse(Status.OK, "application/json", body);
        } catch (NullPointerException e) {
            return NanoHTTPD
                .newFixedLengthResponse(Status.BAD_REQUEST, "text/plain", "Not enough data\n");
        } catch (IllegalArgumentException e) {
            return NanoHTTPD
                .newFixedLengthResponse(Status.BAD_REQUEST, "text/plain", "No such client\n");
        }
    }
}
