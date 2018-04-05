package simplechat;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements API {

    public Server() {}

    public String sayHello() {
        return "API, world!";
    }

    public static void main(String args[]) {

        try {
            Server obj = new Server();
            API stub = (API) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("API", stub);

            System.out.println("Server ready");

            // Wait for input
            System.in.read();
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}