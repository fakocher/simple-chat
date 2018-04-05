package simplechat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface API extends Remote {
    String sayHello() throws RemoteException;

    String connexionStart() throws RemoteException;
    String connexionStop() throws RemoteException;

    String chatSessionRequest() throws RemoteException;
    String chatSessionAccept() throws RemoteException;
    String chatSessionDecline() throws RemoteException;
    String chatSessionSendMessage() throws RemoteException;
    String chatSessionQuit() throws RemoteException;

    String memberListRequest() throws RemoteException;
}