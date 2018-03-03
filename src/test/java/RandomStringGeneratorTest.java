import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RandomStringGeneratorTest {

    @Test
    public void generateString() {
        RandomStringGenerator generator = new RandomStringGenerator();
        String result = generator.generate(4);
        assertEquals(4, result.length());
        assertTrue(result instanceof String);
    }
}