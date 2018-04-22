package simplechat.server;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.UUID;

public interface ServerAPI extends Remote {
    String sayHello() throws RemoteException;

    String memberListJoin(String username, UUID uuid) throws RemoteException, ServerNotActiveException, NotBoundException;
    String memberListLeave(UUID uuid) throws RemoteException, ServerNotActiveException;

    String chatSessionRequest(String username, UUID uuid) throws RemoteException;
    String chatSessionSendMessage(String message, UUID uuid) throws RemoteException;
    String chatSessionQuit(UUID uuid) throws RemoteException;

    String memberListRequest(UUID uuid) throws RemoteException, ServerNotActiveException;
}