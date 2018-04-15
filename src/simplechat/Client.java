package simplechat;

import simplechat.client.ChatSessionManager;
import simplechat.client.ConnexionManager;
import simplechat.client.MemberListManager;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

    public static void main(String[] args)
    {
        // Get hostname from program arguments
        String host = (args.length < 1) ? null : args[0];

        try
        {
            // Get API object from server
            Registry registry = LocateRegistry.getRegistry(host);
            API api = (API) registry.lookup("API");

            // Init managers
            ConnexionManager connexionManager = new ConnexionManager(api);
            ChatSessionManager chatSessionManager = new ChatSessionManager(api);
            MemberListManager memberListManager = new MemberListManager(api);

            // Ready message
            System.out.println("Ready to chat! use `connect <username>` first.");

            // Start the user input infinite loop
            Scanner inputScan = new Scanner(System.in);
            while(true)
            {
                // Ask for input
                System.out.print("> ");
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
                        System.out.println(connexionManager.start(username));
                    }
                }

                // To disconnect from the chat
                else if (in.equals("disconnect"))
                {
                    System.out.println(connexionManager.stop());
                }

                // To show the member list
                else if (in.equals("showmembers"))
                {
                    System.out.println(memberListManager.get());
                }

                // To exit the app
                else if(in.equals("exit"))
                {
                    System.exit(0);
                }

                // To test the API
                else if (in.equals("hello"))
                {
                    String response = api.sayHello();
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
}