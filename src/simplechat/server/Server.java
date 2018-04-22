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

    private void printClientError(UUID uuid, String error)
    {
        System.err.println(uuid.toString() + " client error: " + error);
    }

    private void printClientInfo(UUID uuid, String info)
    {
        System.out.println(uuid.toString() + " client info: " + info);
    }

    @Override
    public String sayHello() {
        return "Hello, world!";
    }

    // TODO
    @Override
    public String chatSessionRequest(String username, UUID uuid) throws RemoteException {
        // Request the chat session
        if (!this.chatSessionManager.handleRequest(username, uuid))
        {
            this.printClientError(uuid, "could not start a chat session.");
            return "User not found or connexion refused.";
        }

        this.printClientInfo(uuid, "now chatting with \"" + username + "\".");
        return "You are now chatting with \"" + username + "\".";
    }

    // TODO
    @Override
    public String chatSessionSendMessage(String message, UUID uuid) throws RemoteException {
        // Send a message to the current chat session
        if (!this.chatSessionManager.sendMessage(message, uuid))
        {
            this.printClientError(uuid, "could not send a message.");
            return "Could not send the message.";
        }

        this.printClientInfo(uuid, "sent a message.");
        return "";
    }

    // TODO
    @Override
    public String chatSessionQuit(UUID uuid) throws RemoteException {
        // Quit a chat session
        if (!this.chatSessionManager.chatSessionQuit(uuid))
        {
            this.printClientError(uuid, "could not quit chat session");
            return "Could not quit chat session.";
        }

        this.printClientInfo(uuid, "quit the current chat session.");
        return "You successfully quit the current chat session.";
    }

    @Override
    public String memberListJoin(String username, UUID uuid) throws RemoteException, NotBoundException {
        // Add user to member list
        if (!this.memberListManager.add(username, uuid))
        {
            this.printClientError(uuid, "tried to connect but already connected or username already exists.");
            return "Already connected to the chat or username already exists.";
        }

        this.printClientInfo(uuid, "added to the member list with username \"" + username + "\".");
        return "Connected to the chat with username \"" + username + "\".";
    }

    @Override
    public String memberListLeave(UUID uuid) throws ServerNotActiveException
    {
        // Remove user from member list
        if (!this.memberListManager.remove(uuid))
        {
            this.printClientError(uuid, "tried to disconnect but already disconnected.");
            return "Already disconnected from chat.";
        }

        this.printClientInfo(uuid, "disconnected from chat.");
        return "Successfully disconnected from chat.";
    }

    @Override
    public String memberListRequest(UUID uuid) throws ServerNotActiveException {
        return this.memberListManager.toString(uuid);
    }
}