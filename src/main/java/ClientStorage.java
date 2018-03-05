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

    public void addNewClient(UUID publicKey, Client client) {
        clientMap.put(publicKey, client);

    }

    public Client getClient(UUID publicKey) {
        if (clientMap.containsKey(publicKey)) {
            return clientMap.get(publicKey);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void removeClient(UUID publicKey) {
        clientMap.remove(publicKey);
    }
}
