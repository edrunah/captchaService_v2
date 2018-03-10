import static org.junit.Assert.fail;

import java.lang.reflect.Field;

public class ServerParameters {

    public static void setObjectField(Object object, String field, String value) {
        try {
            Field f = object.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(object, value);
        } catch (Exception e) {
            fail("Не удалось задать параметр " + field + " сервера");
        }
    }

}
