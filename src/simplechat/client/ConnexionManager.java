package simplechat.client;

import simplechat.API;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public class ConnexionManager {

    private API api;
    private String username = null;
    private boolean connected = false;

    public ConnexionManager(API api) {
        this.api = api;
    }

    public String start(String username) throws RemoteException, ServerNotActiveException {
        this.username = username;
        this.connected = true;
        return api.connexionStart(username);
    }

    public String stop() throws RemoteException, ServerNotActiveException {
        this.username = null;
        this.connected = false;
        return api.connexionStop();
    }
}
