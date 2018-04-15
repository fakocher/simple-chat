package simplechat.client;

import simplechat.API;

import java.rmi.RemoteException;

public class ChatSessionManager {

    private API api;

    public ChatSessionManager(API api) {
        this.api = api;
    }

    public String showRequests() throws RemoteException {
        return this.api.chatSessionShowRequests();
    }
}
