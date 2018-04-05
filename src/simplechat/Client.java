package simplechat;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

    private Client() {}

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            API api = (API) registry.lookup("API");

            // Ready message
            System.out.println("Ready to chat !.");

            // Start the user input infinite loop
            Scanner inputScan = new Scanner(System.in);
            while(true)
            {
                // Handle input
                System.out.print("> ");
                switch(inputScan.nextLine())
                {
                    // To quit the app
                    case "exit":
                    case "quit":
                        System.exit(0);
                        break;

                    // To test the API
                    case "hello":
                        String response = api.sayHello();
                        System.out.println("response: " + response);
                        break;

                    default:
                        System.out.println("Invalid command.");
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}