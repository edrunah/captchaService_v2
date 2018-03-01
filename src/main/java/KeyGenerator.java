import java.util.UUID;

public class KeyGenerator implements IKeyGenerator {

    public UUID getPublicKey(UUID anotherUUID) {
        return UUID.nameUUIDFromBytes(anotherUUID.toString().getBytes());
    }

    public UUID getSecretKey() {
        return UUID.randomUUID();
    }
}
