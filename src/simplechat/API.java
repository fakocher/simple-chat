package simplechat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface API extends Remote {
    String sayHello() throws RemoteException;

    String connexionStart(String username) throws RemoteException, ServerNotActiveException;
    String connexionStop() throws RemoteException, ServerNotActiveException;

    String chatSessionShowRequests() throws RemoteException;
    String chatSessionRequest() throws RemoteException;
    String chatSessionAccept() throws RemoteException;
    String chatSessionDecline() throws RemoteException;
    String chatSessionSendMessage() throws RemoteException;
    String chatSessionQuit() throws RemoteException;

    String memberListRequest() throws RemoteException, ServerNotActiveException;
}