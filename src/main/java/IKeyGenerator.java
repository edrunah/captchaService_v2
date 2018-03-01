import java.util.UUID;

public interface IKeyGenerator {

    public UUID getPublicKey(UUID anotherUUID);

    public UUID getSecretKey();

}
