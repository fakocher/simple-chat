package simplechat.server;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.UUID;

public interface ServerAPI extends Remote {
    String sayHello() throws RemoteException;
    // add a new member into members list when connection
    String memberListJoin(String username, UUID uuid) throws RemoteException, ServerNotActiveException, NotBoundException;
    // remove member from members List when disconnection
    String memberListLeave(UUID uuid) throws RemoteException, ServerNotActiveException;

    // open chat session manager with receiver's username and requester's uuid
    String chatSessionRequest(String username, UUID uuid) throws RemoteException;
    // 	Send message to receiver by passing message and requester's uuid. this uuid allows to identify the requester's session and his receiver 
    String chatSessionSendMessage(String message, UUID uuid) throws RemoteException;
    // quit chat session manager
    String chatSessionQuit(UUID uuid) throws RemoteException;
    
    // show all members connected to server
    String memberListRequest(UUID uuid) throws RemoteException, ServerNotActiveException;
}