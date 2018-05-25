package simplechat.server;

import simplechat.client.ClientAPI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.UUID;
/*
 * Date : 04/2018
 * Description : manage the members who connect to server. 
 * 				 for each connection, new member will be added in member list
 * 				 for each disconnection, member will be removed from member list
 */
class MemberListManager {
	// list of members who connect to server
    private ArrayList<Member> memberList = new ArrayList<>();

    // create member and add it to member list
    boolean add(String username, UUID uuid) throws RemoteException, NotBoundException {
        if (this.memberListContainsHost(uuid) || this.usernameExists(username))
        {
            return false;
        }
        // Retrieve client API from RMI registry
        Registry registry = LocateRegistry.getRegistry();
        ClientAPI clientApi = (ClientAPI) registry.lookup(uuid.toString());

        Member newMember = new Member(username, uuid, clientApi);
        this.memberList.add(newMember);
        return true;
    }

    // check if member exists already in members list by uuid
    private boolean memberListContainsHost(UUID uuid)
    {
        for(Member member: this.memberList)
        {
            if(member.getClientUUID().equals(uuid))
                return true;
        }
        return false;
    }

    // check if member exists already in members list by username
    private boolean usernameExists(String username)
    {
        return this.getByName(username) != null;
    }

    // remove member from member list by uuid
    boolean remove(UUID uuid)
    {
        Member member = this.getByUUID(uuid);

        if(member == null)
        {
            return false;
        }

        this.memberList.remove(member);
        return true;
    }

    // get member by uuid
    Member getByUUID(UUID uuid)
    {
        for(Member member: this.memberList)
        {
            if(member.getClientUUID().equals(uuid))
                return member;
        }

        return null;
    }

    // get member by username
    Member getByName(String username)
    {
        for(Member member: this.memberList)
        {
            if(member.getUsername().equals(username))
                return member;
        }

        return null;
    }

  
    String toString(UUID uuid)
    {
        if (memberList.size() == 0)
        {
            return "Nobody is connected :(";
        }

        StringBuilder sb = new StringBuilder();

        for(Member member: this.memberList)
        {
            sb.append(member.getUsername());

            if (member.getClientUUID().equals(uuid))
            {
                sb.append(" (you)");
            }
        }

        return sb.toString();
    }
}
