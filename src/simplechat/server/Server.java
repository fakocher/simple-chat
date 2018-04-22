package simplechat.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.ServerNotActiveException;
import java.util.UUID;

public class Server implements ServerAPI {

    private ChatSessionManager chatSessionManager;
    private MemberListManager memberListManager;

    public Server() {
        memberListManager = new MemberListManager();
        chatSessionManager = new ChatSessionManager(memberListManager);
    }

    public static void main(String args[])
    {
        try
        {
            // Bind the server object to the RMI registry
            Server obj = new Server();
            ServerAPI stub = (ServerAPI) UnicastRemoteObject.exportObject(obj, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("ServerAPI", stub);

            System.out.println("Server ready");

            // Wait for input
            System.in.read();
        }
        catch (Exception e)
        {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private void printClientError(String clientHost, String error)
    {
        System.err.println(clientHost + " client error: " + error);
    }

    private void printClientInfo(String clientHost, String info)
    {
        System.out.println(clientHost + " client info: " + info);
    }

    @Override
    public String sayHello() {
        return "Hello, world!";
    }

    // TODO
    @Override
    public String chatSessionRequest() {
        return "chatSessionRequest to be implemented";
    }

    // TODO
    @Override
    public String chatSessionSendMessage() {
        return "chatSessionSendMessage to be implemented";
    }

    // TODO
    @Override
    public String chatSessionQuit() {
        return "chatSessionQuit to be implemented";
    }

    @Override
    public String memberListJoin(String username, UUID uuid) throws ServerNotActiveException, RemoteException, NotBoundException {
        // Add user to member list
        String clientHost = RemoteServer.getClientHost();
        if (!this.memberListManager.add(username, clientHost, uuid))
        {
            this.printClientError(clientHost, "tried to connect but already connected.");
            return "Already connected to the chat.";
        }

        this.printClientInfo(clientHost, "added to the member list with username \"" + username + "\".");
        return "Connected to the chat with username \"" + username + "\".";
    }

    @Override
    public String memberListLeave() throws ServerNotActiveException
    {
        // Remove user from member list
        String clientHost = RemoteServer.getClientHost();
        if (!this.memberListManager.remove(clientHost))
        {
            this.printClientError(clientHost, "tried to disconnect but already disconnected.");
            return "Already disconnected from chat.";
        }

        this.printClientInfo(clientHost, "disconnected from chat.");
        return "Successfully disconnected from chat.";
    }

    @Override
    public String memberListRequest() throws ServerNotActiveException {
        String clientHost = RemoteServer.getClientHost();
        return this.memberListManager.toString(clientHost);
    }
}