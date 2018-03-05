import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RequestParameters {

    public static void initParameter(Map<String, List<String>> parameters, String name,
        String value) {
        List<String> parameter = new LinkedList<>();
        parameter.add(value);
        parameters.put(name, parameter);
    }


}
