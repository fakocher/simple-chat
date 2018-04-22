package simplechat.server;

import simplechat.client.ClientAPI;

import java.util.UUID;

class Member {

    private String username;
    private String clientHost;
    private UUID uuid;
    private ClientAPI clientAPI;

    Member(String username, String clientHost, UUID uuid, ClientAPI clientAPI)
    {
        this.username = username;
        this.clientHost = clientHost;
        this.uuid = uuid;
        this.clientAPI = clientAPI;
    }

    String getClientHost() {
        return clientHost;
    }

    UUID getClientUUID() {
        return uuid;
    }

    String getUsername() {
        return username;
    }

    ClientAPI getClientAPI() {
        return clientAPI;
    }
}
