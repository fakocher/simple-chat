package simplechat;

import simplechat.server.ConnexionManager;
import simplechat.server.ChatSessionManager;
import simplechat.server.MemberListManager;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.ServerNotActiveException;

public class Server implements serverAPI {

    ConnexionManager connexionManager = new ConnexionManager();
    ChatSessionManager chatSessionManager = new ChatSessionManager();
    MemberListManager memberListManager = new MemberListManager();

    public static void main(String args[])
    {
        try
        {
            Server obj = new Server();
            serverAPI stub = (serverAPI) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("serverAPI", stub);

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

    @Override
    public String connexionStart(String username) throws ServerNotActiveException
    {
        // Add user to member list
        String clientHost = RemoteServer.getClientHost();
        if (!this.memberListManager.add(username, clientHost))
        {
            this.printClientError(clientHost, "tried to connect but already connected.");
            return "Already connected to the chat.";
        }

        this.printClientInfo(clientHost, "added to the member list with username \"" + username + "\".");
        return "Connected to the chat with username \"" + username + "\".";
    }

    @Override
    public String connexionStop() throws ServerNotActiveException
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

    // TODO
    @Override
    public String chatSessionShowRequests() {
        return "chatSessionShowRequests to be implemented";
    }

    // TODO
    @Override
    public String chatSessionRequest() {
        return "chatSessionRequest to be implemented";
    }

    // TODO
    @Override
    public String chatSessionAccept() {
        return "chatSessionAccept to be implemented";
    }

    // TODO
    @Override
    public String chatSessionDecline() {
        return "chatSessionDecline to be implemented";
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

    // TODO
    @Override
    public String memberListRequest() throws ServerNotActiveException {
        String clientHost = RemoteServer.getClientHost();
        return this.memberListManager.toString(clientHost);
    }
}