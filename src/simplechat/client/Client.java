package simplechat.client;

import simplechat.GlobalConstants;
import simplechat.server.Server;
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
            System.setProperty("javax.net.ssl.keyStore", "C:\\ssl\\keystore-server.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", SSLPass);
            System.setProperty("javax.net.ssl.trustStore", "C:\\ssl\\truststore-server.jks");
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
            System.out.println("Ready to chat! use `connect <username>` first.");

            // Start the user input infinite loop
            Scanner inputScan = new Scanner(System.in);
            while(true)
            {
                // Ask for input
                String in = inputScan.nextLine();

                if (in.startsWith("signup"))
                {
                    String[] split = in.split(" ");

                    if (split.length <3)
                    {
                        System.out.println("Error syntaxe : please try signup <username> <password>");
                    }
                    else
                    {
                        String username = split[1];
                        String password = split[2];
                        // singup to the server
                        if (serverApi.singUp(username,password)){
                            System.out.println("signup of " + username + " is successed ");
                        } else {
                            System.out.println("signup of " + username + " is failed, please retry later ");
                        }
                    }
                }
                else if (in.startsWith("login"))
                {
                    String[] split = in.split(" ");

                    if (split.length < 3)
                    {
                        System.out.println("Error syntaxe : please try login <username> <password>");
                    }
                    else
                    {
                        String username = split[1];
                        String password = split[2];
                        // singup to the server
                        uuid=serverApi.login(username,password);
                        if (uuid!=null){
                            if (uuid.equals(GlobalConstants.UIDLOCKED)){
                                System.out.println("login of " + username + " is locked, nb of connexion failed > " + GlobalConstants.MAX_NBFAILEDCONNEXION);
                            } else {
                                System.out.println("login of " + username + " is successed ");
                                registry.bind(uuid.toString(), stub);
                            }

                        } else {
                            System.out.println("login of " + username + " is failed, please retry later ");
                        }
                    }
                }
                // To connect to the chat
                else if (in.startsWith("connect"))
                {
                    // Get username, error if not specified
                    String[] split = in.split(" ");
                    if (split.length == 1) {
                        System.out.println("Error syntaxe : please try : connect <username>");
                    }
                    else
                    {
                        String username = split[1];

                        // Connect to the server
                        if (uuid==null){
                            System.out.println("to connect to a chat, you need to login to chat server, please try : login <username> <password>");
                        } else {
                            System.out.println(serverApi.memberListJoin(username, uuid));
                        }
                    }
                }

                // To disconnect from the chat
                else if (in.equals("disconnect"))
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
                        System.out.println("Please specify a fellow username.");
                    }
                    else
                    {
                        String username = split[1];

                        // Connect to the server
                        System.out.println(serverApi.chatSessionRequest(username, uuid));
                    }
                }

                // To quit current chat session
                else if (in.equals("quitsession"))
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

                // Error message if command is invalid
                else
                {
                    System.out.println("Invalid command.");
                }
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
        System.out.println(message);
    }

    // request from server to ask user for stating a chat session
    public boolean chatSessionRequest()
    {
        System.out.println("Would you like to start a chat ? (y/N)");
        Scanner inputScan = new Scanner(System.in);
        String in = inputScan.nextLine();
        return in.equals("y");

    }
}