package simplechat.server;

import simplechat.client.ClientAPI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.UUID;

class MemberListManager {

    private ArrayList<Member> memberList = new ArrayList<>();

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

    private boolean memberListContainsHost(UUID uuid)
    {
        for(Member member: this.memberList)
        {
            if(member.getClientUUID().equals(uuid))
                return true;
        }

        return false;
    }

    private boolean usernameExists(String username)
    {
        return this.getByName(username) != null;
    }

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

    Member getByUUID(UUID uuid)
    {
        for(Member member: this.memberList)
        {
            if(member.getClientUUID().equals(uuid))
                return member;
        }

        return null;
    }

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

            sb.append("\n");
        }

        return sb.toString();
    }
}
