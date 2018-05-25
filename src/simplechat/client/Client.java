package simplechat.client;

import simplechat.GlobalConstants;
import simplechat.server.ServerAPI;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.UUID;

/*
 * Date : 04/2018
 * Description : 
 * 1. client console allows users to call different functionalities of server 
 * 		=> ex: connect/disconnect, display the member list...
 * 2. provides 2 functionalities for server
 * 		=> receiveMessage : deliver the message from server
 * 		=> chatSessionRequest : request from server to client for starting a chat session
 */
public class Client implements ClientAPI {

    public static void main(String[] args)
    {
        // Get hostname from program arguments
        String host = (args.length < 1) ? null : args[0];

        try
        {
            // Set SSL settings
            String SSLPass = "password";
            System.setProperty("javax.net.ssl.debug", "all");
            System.setProperty("javax.net.ssl.keyStore", GlobalConstants.SSL_PATH + "keystore-client.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", SSLPass);
            System.setProperty("javax.net.ssl.trustStore", GlobalConstants.SSL_PATH + "truststore-server.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", SSLPass);

            // Bind the client object to the RMI registry
            UUID uuid_client = UUID.randomUUID();
            UUID uuid = null;
            Client obj = new Client();
            SslRMIClientSocketFactory csf = new SslRMIClientSocketFactory();
            SslRMIServerSocketFactory ssf = new SslRMIServerSocketFactory();
            ClientAPI stub = (ClientAPI) UnicastRemoteObject.exportObject(obj, 0, csf, ssf);
            Registry registry = LocateRegistry.getRegistry( 0);


            // Get ServerAPI object from registry
            ServerAPI serverApi = (ServerAPI) registry.lookup("ServerAPI");

            // Ready message
            System.out.println();
            System.out.println("Ready to chat! Type `help` to display the list of commands.");
            System.out.println();

            // Start the user input infinite loop
            Scanner inputScan = new Scanner(System.in);
            while(true)
            {
                // Ask for input
                System.out.print("> ");
                String in = inputScan.nextLine();

                System.out.println();

                if (in.startsWith("signup"))
                {
                    String[] split = in.split(" ");

                    if (split.length <3)
                    {
                        System.out.println("Syntax error : please try `signup <username> <password>`");
                    }
                    else
                    {
                        String username = split[1];
                        String password = split[2];

                        // Sign up to the server
                        if (serverApi.singUp(username,password)){
                            System.out.println("Successfully signed up with username \"" + username + "\".");
                        } else {
                            System.out.println("Failed signing up with username \"" + username + "\".");
                        }
                    }
                }
                else if (in.startsWith("login"))
                {
                    String[] split = in.split(" ");

                    if (split.length < 3)
                    {
                        System.out.println("Syntax error : please try `login <username> <password>`");
                    }
                    else
                    {
                        String username = split[1];
                        String password = split[2];
                        // login to the server
                        uuid=serverApi.login(username,password);
                        if (uuid!=null){
                            if (uuid.equals(GlobalConstants.UIDLOCKED)){
                                System.out.println("User \"" + username + "\" was locked after " + (GlobalConstants.MAX_NBFAILEDCONNEXION + 1) + " unsuccessful connexions.");
                            } else {
                                System.out.println("Successfully logged in with username `" + username + "`.");
                                registry.bind(uuid.toString(), stub);
                                System.out.println(serverApi.memberListJoin(username, uuid));
                            }

                        } else {
                            System.out.println("Failed to login with username \"" + username + "\".");
                        }
                    }
                }

                // To disconnect from the chat
                else if (in.equals("logout"))
                {
                    System.out.println(serverApi.memberListLeave(uuid));
                }

                // To show the member list
                else if (in.equals("showmembers"))
                {
                    System.out.println(serverApi.memberListRequest(uuid));
                }

                // To request a chat session
                else if (in.startsWith("requestchat"))
                {
                    // Get username, error if not specified
                    String[] split = in.split(" ");
                    if (split.length == 1)
                    {
                        System.out.println("Please specify a fellow user's name. Use `showmembers`to show the other members names.");
                    }
                    else
                    {
                        String username = split[1];

                        // Connect to the server
                        System.out.println(serverApi.chatSessionRequest(username, uuid));
                    }
                }

                // To quit current chat session
                else if (in.equals("quitchat"))
                {
                    System.out.println(serverApi.chatSessionQuit(uuid));
                }

                // To send a message
                else if (in.startsWith("message"))
                {
                    // Get message, error if not specified
                    String[] split = in.split(" ");
                    if (split.length == 1)
                    {
                        System.out.println("Please specify a message.");
                    }
                    else
                    {
                        String message = split[1];

                        // Connect to the server
                        System.out.println(serverApi.chatSessionSendMessage(message, uuid));
                    }
                }

                // To exit the app
                else if(in.equals("exit"))
                {
                    System.exit(0);
                }

                // To test the ServerAPI
                else if (in.equals("hello"))
                {
                    String response = serverApi.sayHello();
                    System.out.println("response: " + response);
                }

                // List of commands
                else if (in.equals("help"))
                {
                    System.out.println("Here are the commands you can use:");
                    System.out.println();
                    System.out.println(" - signup <username> <password>");
                    System.out.println(" - login <username> <password>");
                    System.out.println(" - logout");
                    System.out.println(" - showmembers");
                    System.out.println(" - requestchat <username>");
                    System.out.println(" - message <message>");
                    System.out.println(" - quitchat");
                    System.out.println(" - exit");
                }

                // Error message if command is invalid
                else
                {
                    System.out.println("Invalid command. Type `help` for the list of commands.");
                }

                System.out.println();
            }
        }
        catch (Exception e)
        {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    // print the message sent by server
    public void receiveMessage(String message)
    {
        System.out.println();
        System.out.println(message);
        System.out.println();
    }
    
    // print the message sent by server
    public void receiveMessage(String message, String username)
    {
        System.out.println();
        System.out.println(username + " said: " + message);
        System.out.println();
    }

    // request from server to ask user for stating a chat session
    public boolean chatSessionRequest(String username)
    {
        System.out.println("Would you like to start a chat with " + username + "? (y/N)");
        Scanner inputScan = new Scanner(System.in);
        System.out.print("> ");
        String in = inputScan.nextLine();
        boolean yes = in.equals("y");

        if (yes)
        {
            System.out.println("Now chatting with " + username);
        }

        return yes;
    }
}