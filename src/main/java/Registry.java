import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Registry implements IResponser {

    public Response generateResponse(Map<String, List<String>> parameters) {
        IKeyGenerator keyGenerator = new KeyGenerator();
        UUID secretUUID = keyGenerator.getSecretKey();
        UUID publicUUID = keyGenerator.getPublicKey(secretUUID);
        Client client = new Client();
        ClientStorage storage = ClientStorage.getInstance();
        storage.addNewClient(publicUUID, client);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("public", publicUUID);
        jsonObject.put("secret", secretUUID);
        String body = JSON.toJSONString(jsonObject);
        Response response = NanoHTTPD.newFixedLengthResponse(Status.OK, "application/json", body);
        return response;
    }
}
