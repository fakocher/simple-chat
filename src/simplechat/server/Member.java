package simplechat.server;

import simplechat.client.ClientAPI;

import java.util.UUID;
/*
 * Date : 04/2018
 * Description : 
 * Member class allows to create member object with properties like username, id, ap
 * different methods are provided to read properties of member object 
 */
class Member {

    private String username;
    private UUID uuid;
    private ClientAPI clientAPI;

    // initial the properties of Member object
    Member(String username, UUID uuid, ClientAPI clientAPI)
    {
        this.username = username;
        this.uuid = uuid;
        this.clientAPI = clientAPI;
    }

    // get ClientUUID (unique id of client)
    UUID getClientUUID() {
        return uuid;
    }

    // get username of member
    String getUsername() {
        return username;
    }

    // get ClientAPI
    ClientAPI getClientAPI() {
        return clientAPI;
    }
}
