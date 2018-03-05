import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Verify implements IResponser {

    public Response generateResponse(Map<String, List<String>> parameters) {
        try {
            UUID receivedSecretKey = UUID.fromString(parameters.get("secret").get(0));
            UUID calculatedPublicKey = new KeyGenerator().getPublicKey(receivedSecretKey);
            String receivedToken = parameters.get("response").get(0);
            ClientStorage storage = ClientStorage.getInstance();
            Client client = storage.getClient(calculatedPublicKey);
            Boolean success;
            Integer errorCode;
            Status status;
            if (client.hasToken(receivedToken)) {
                client.deleteToken();
                success = true;
                errorCode = null;
                status = Status.OK;
            } else {
                success = false;
                errorCode = 403;
                status = Status.FORBIDDEN;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success", success);
            jsonObject.put("errorCode", errorCode);

            String body = JSON.toJSONString(jsonObject, SerializerFeature.WriteMapNullValue);
            return NanoHTTPD.newFixedLengthResponse(status, "application/json", body);
        } catch (NullPointerException e) {
            return NanoHTTPD
                .newFixedLengthResponse(Status.BAD_REQUEST, "text/plain", "Not enough data\n");
        } catch (IllegalArgumentException e) {
            return NanoHTTPD
                .newFixedLengthResponse(Status.BAD_REQUEST, "text/plain", "No such client\n");
        }
    }
}
