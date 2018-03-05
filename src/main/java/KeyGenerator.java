import java.nio.ByteBuffer;
import java.util.UUID;

public class KeyGenerator implements IKeyGenerator {

    public UUID getPublicKey(UUID anotherUUID) {
        int hash = anotherUUID.hashCode();
        byte[] bytes = ByteBuffer.allocate(4).putInt(hash).array();
        return UUID.nameUUIDFromBytes(bytes);
    }

    public UUID getSecretKey() {
        return UUID.randomUUID();
    }
}
