package simplechat.server;

import simplechat.GlobalConstants;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
/*
 * Date : 04/2018
 * Description : 
 * server console provide different functionalities :
 * 		1. memberListJoin : add a new member to memberList when connection
 * 		2. memberListLeave : remove member from memberList when disconnection
 * 		3. memberListRequest : show memberList (all members connected)
 * 
 * 		4. chatSessionRequest : start a chat session
 * 		5. chatSessionSendMessage : send a message 
 * 		6. chatSessionQuit : stop a chat session
 */
public class Server implements ServerAPI {
    private ChatSessionManager chatSessionManager;
    private MemberListManager memberListManager;
    private Authentication authentication;

    public Server() {
        authentication= new Authentication();
        memberListManager = new MemberListManager(); // members who connect to server
        chatSessionManager = new ChatSessionManager(memberListManager); // chat sessions
    }

    public static void main(String args[])
    {
        try
        {
            // Set SSL settings
            String SSLPass = "password";
            System.setProperty("javax.net.ssl.debug", "all");
            System.setProperty("javax.net.ssl.keyStore", "C:\\ssl\\keystore-server.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", SSLPass);
            System.setProperty("javax.net.ssl.trustStore", "C:\\ssl\\truststore-server.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", SSLPass);
            
            // Bind the server object to the RMI registry
            Server obj = new Server();
            SslRMIClientSocketFactory csf = new SslRMIClientSocketFactory();
            SslRMIServerSocketFactory ssf = new SslRMIServerSocketFactory();
            ServerAPI stub = (ServerAPI) UnicastRemoteObject.exportObject(obj,0, csf, ssf);
            Registry registry = LocateRegistry.getRegistry(0);
            registry.rebind("ServerAPI", stub);

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

    // print info when error
    private void printClientError(UUID uuid, String error)
    {
        System.err.println(uuid.toString() + " client error: " + error);
    }

    // print client info
    private void printClientInfo(UUID uuid, String info)
    {
        System.out.println(uuid.toString() + " client info: " + info);
    }

    @Override
    public String sayHello() {
        return "Hello, world!";
    }

    @Override
    public UUID login(String userName, String password) throws RemoteException {
        UUID loginOK;
        loginOK=authentication.login(userName,password);

        if (loginOK!=null){
            if (loginOK.equals(GlobalConstants.UIDLOCKED)){
                System.out.println("login of " + userName + " is locked, nb of connexion failed >  " + GlobalConstants.MAX_NBFAILEDCONNEXION);
            } else {
                System.out.println("client " + userName + " has successfully connected to server ");
            }
        }else{
            System.out.println("client "+userName + " failed to connect to server ");
        }
        return loginOK;
    }

    @Override
    public boolean singUp(String userName, String password) throws RemoteException {
        Boolean signUpOK;
        signUpOK=authentication.singUp(userName,password);
        if (signUpOK){
            System.out.println("client "+userName + " has successfully sign up.");
        }else{
            System.out.println("client "+userName + " failed to sign up.");
        }
        return signUpOK;
    }

    /* open chat session manager
     * call chatSessionManager.handleRequest with receiver's username and requester's uuid
     * the receiver's username allows to identify receiver's ip for sending the request
     * the requester's uuid and reciver's username allow to create a chat session
    */
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

    /* Send message to receiver
     * call chatSessionManager.sendMessage by passing message and requester's uuid
     * this uuid allows to identify the requester's session and his receiver
    */
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

    // quit chat session manager
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
    // add a new member to members list when connection
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
    // remove member from members List when disconnection
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
    // show all members connected to server
    public String memberListRequest(UUID uuid) throws ServerNotActiveException {
        return this.memberListManager.toString(uuid);
    }
}