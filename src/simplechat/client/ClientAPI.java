package simplechat.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface ClientAPI extends Remote {
    void receiveMessage(String message) throws RemoteException;
    void receiveMessage(String message, String username) throws RemoteException;

    boolean chatSessionRequest(String username) throws RemoteException;
}