import com.alibaba.fastjson.JSON;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Solve implements Responser {

    public Response generateResponse(Map<String, List<String>> parameters) {
        try {
            UUID receivedPublicKey = UUID.fromString(parameters.get("public").get(0));
            String receivedCaptchaId = parameters.get("request").get(0);
            String receivedAnswer = parameters.get("answer").get(0);
            ClientStorage storage = ClientStorage.getInstance();
            Client client = storage.getClient(receivedPublicKey);
            if (client.hasCaptchaId(receivedCaptchaId) && client.captchaHasAnswer(receivedAnswer)) {
                client.generateToken();
                client.deleteCaptcha();
                String body = JSON.toJSONString(client);
                return NanoHTTPD
                    .newFixedLengthResponse(Status.OK, "application/json", body);
            } else {
                return NanoHTTPD
                    .newFixedLengthResponse(Status.BAD_REQUEST, "text/plain",
                        "Wrong pair request-answer\n");
            }
        } catch (NullPointerException e) {
            return NanoHTTPD
                .newFixedLengthResponse(Status.BAD_REQUEST, "text/plain", "Not enough data\n");
        } catch (IllegalArgumentException e) {
            return NanoHTTPD
                .newFixedLengthResponse(Status.BAD_REQUEST, "text/plain", "No such client\n");
        }
    }
}
