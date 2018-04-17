package simplechat.client;

import simplechat.serverAPI;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public class MemberListManager {

    private serverAPI serverApi;

    public MemberListManager(serverAPI serverApi) {
        this.serverApi = serverApi;
    }

    public String get() throws RemoteException, ServerNotActiveException {
        return serverApi.memberListRequest();
    }
}
