package simplechat.client;

import simplechat.API;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public class MemberListManager {

    private API api;

    public MemberListManager(API api) {
        this.api = api;
    }

    public String get() throws RemoteException, ServerNotActiveException {
        return api.memberListRequest();
    }
}
