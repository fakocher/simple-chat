package simplechat.client;

import simplechat.serverAPI;

import java.rmi.RemoteException;

public class ChatSessionManager {

    private serverAPI serverApi;

    public ChatSessionManager(serverAPI serverApi) {
        this.serverApi = serverApi;
    }

    public String showRequests() throws RemoteException {
        return this.serverApi.chatSessionShowRequests();
    }
}
