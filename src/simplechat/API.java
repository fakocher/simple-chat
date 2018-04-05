package simplechat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface API extends Remote {
    String sayHello() throws RemoteException;
}