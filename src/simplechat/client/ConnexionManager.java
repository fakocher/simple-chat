package simplechat.client;

import simplechat.serverAPI;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public class ConnexionManager {

    private serverAPI serverApi;
    private String username = null;
    private boolean connected = false;

    public ConnexionManager(serverAPI serverApi) {
        this.serverApi = serverApi;
    }

    public String start(String username) throws RemoteException, ServerNotActiveException {
        this.username = username;
        this.connected = true;
        return serverApi.connexionStart(username);
    }

    public String stop() throws RemoteException, ServerNotActiveException {
        this.username = null;
        this.connected = false;
        return serverApi.connexionStop();
    }
}
