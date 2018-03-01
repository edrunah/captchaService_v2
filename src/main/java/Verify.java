import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Verify implements IResponser {

    public Response generateResponse(Map<String, List<String>> parameters) {
        try {
            UUID receivedSecretUUID = UUID.fromString(parameters.get("secret").get(0));
            UUID calculatedPublicUUID = new KeyGenerator().getPublicKey(receivedSecretUUID);
            String receivedToken = parameters.get("response").get(0);
            ClientStorage storage = ClientStorage.getInstance();
            Client client = storage.getClient(calculatedPublicUUID);
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
                errorCode = 406;
                status = Status.NOT_ACCEPTABLE;
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
