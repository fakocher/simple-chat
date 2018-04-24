package simplechat.client;

import simplechat.server.Server;
import simplechat.server.ServerAPI;

import java.rmi.RemoteException;
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
            // Bind the client object to the RMI registry
            UUID uuid = UUID.randomUUID();
            Client obj = new Client();
            ClientAPI stub = (ClientAPI) UnicastRemoteObject.exportObject(obj, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind(uuid.toString(), stub);

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

                // To connect to the chat
                if (in.startsWith("connect"))
                {
                    // Get username, error if not specified
                    String[] split = in.split(" ");
                    if (split.length == 1)
                    {
                        System.out.println("Please specify a username.");
                    }
                    else
                    {
                        String username = split[1];

                        // Connect to the server
                        System.out.println(serverApi.memberListJoin(username, uuid));
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