import static org.junit.Assert.assertTrue;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

public class KeyGeneratorTest {

    KeyGenerator generator;

    @Before
    public void setUp() {
        generator = new KeyGenerator();
    }

    @Test
    public void getPublicKey() {
        UUID secretKey = UUID.fromString("c88c4314-0e11-3a44-9a4b-dd2eba64c868");

        boolean isUuidType = generator.getPublicKey(secretKey) instanceof UUID;

        assertTrue(isUuidType);
    }

    @Test
    public void getSecretKey() {
        UUID secretKey = generator.getSecretKey();

        boolean isUuidType = secretKey instanceof UUID;

        assertTrue(isUuidType);
    }
}
