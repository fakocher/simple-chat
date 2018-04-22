package simplechat.server;

import simplechat.client.ClientAPI;

import java.util.UUID;

class Member {

    private String username;
    private UUID uuid;
    private ClientAPI clientAPI;

    Member(String username, UUID uuid, ClientAPI clientAPI)
    {
        this.username = username;
        this.uuid = uuid;
        this.clientAPI = clientAPI;
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
