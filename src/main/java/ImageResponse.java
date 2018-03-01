import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ImageResponse implements IResponser {

    public Response generateResponse(Map<String, List<String>> parameters) {
        try {
            UUID receivedPublicUUID = UUID.fromString(parameters.get("public").get(0));
            String receivedCaptchaId = parameters.get("request").get(0);
            ClientStorage storage = ClientStorage.getInstance();
            Client client = storage.getClient(receivedPublicUUID);
            if (client.hasCaptchaId(receivedCaptchaId)) {
                Captcha captcha = client.getCaptcha();
                String answer = captcha.getAnswer();
                BufferedImage image = new CaptchaImageCreator().create(answer);
                try (InputStream is = new CaptchaImageConverter().convertImageToStream(image)) {
                    return NanoHTTPD
                        .newFixedLengthResponse(Status.CREATED, "image/png", is, -1);
                } catch (IOException e) {
                    return NanoHTTPD
                        .newFixedLengthResponse(Status.INTERNAL_ERROR, "text/plain",
                            "Image creation error\n");
                }
            } else {
                return NanoHTTPD
                    .newFixedLengthResponse(Status.BAD_REQUEST, "text/plain", "No such request\n");
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
