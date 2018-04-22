package simplechat.server;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.UUID;

public interface ServerAPI extends Remote {
    String sayHello() throws RemoteException;

    String memberListJoin(String username, UUID uuid) throws RemoteException, ServerNotActiveException, NotBoundException;
    String memberListLeave() throws RemoteException, ServerNotActiveException;

    String chatSessionRequest() throws RemoteException;
    String chatSessionSendMessage() throws RemoteException;
    String chatSessionQuit() throws RemoteException;

    String memberListRequest() throws RemoteException, ServerNotActiveException;
}