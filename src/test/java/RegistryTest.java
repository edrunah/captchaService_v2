import com.alibaba.fastjson.JSON;
import fi.iki.elonen.NanoHTTPD.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.Test;

public class RegistryTest {

    @Test
    public void registry() {
        Responser responser = new Registry();
        Map<String, List<String>> parameters = new HashMap<>();

        Response response = responser.generateResponse(parameters);
        String body = BodyMaker.getBody(response);
        Map<String, Object> responseBody = JSON.parseObject(body);

        String publicKeyString = (String) responseBody.get("public");
        String secretKeyString = (String) responseBody.get("secret");
        UUID.fromString(publicKeyString);
        UUID.fromString(secretKeyString);
    }
}