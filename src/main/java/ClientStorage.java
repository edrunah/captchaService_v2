import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientStorage {

    private static ClientStorage storage = new ClientStorage();

    private Map<UUID, Client> clientMap = new HashMap<UUID, Client>();

    private ClientStorage() {
    }

    public static ClientStorage getInstance() {
        return storage;
    }

    public void addNewClient(UUID publicUUID, Client client) {
        clientMap.put(publicUUID, client);

    }

    public Client getClient(UUID publicUUID) {
        if (clientMap.containsKey(publicUUID)) {
            return clientMap.get(publicUUID);
        } else {
            throw new IllegalArgumentException();
        }

    }
}
