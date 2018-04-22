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

    boolean add(String username, String clientHost, UUID uuid) throws RemoteException, NotBoundException {
        if (this.memberListContainsHost(clientHost, uuid))
        {
            return false;
        }

        // Retrieve client API from RMI registry
        Registry registry = LocateRegistry.getRegistry();
        ClientAPI clientApi = (ClientAPI) registry.lookup(uuid.toString());

        Member newMember = new Member(username, clientHost, uuid, clientApi);
        this.memberList.add(newMember);
        return true;
    }

    private boolean memberListContainsHost(String clientHost, UUID uuid)
    {
        for(Member member: this.memberList)
        {
            if(member.getClientHost().equals(clientHost) && member.getClientUUID() == uuid)
                return true;
        }

        return false;
    }

    boolean remove(String clientHost)
    {
        Member member = this.get(clientHost);

        if(member == null)
        {
            return false;
        }

        this.memberList.remove(member);
        return true;
    }

    private Member get(String clientHost)
    {
        for(Member member: this.memberList)
        {
            if(member.getClientHost().equals(clientHost))
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

    String toString(String clientHost)
    {
        if (memberList.size() == 0)
        {
            return "Nobody is connected :(";
        }

        StringBuilder sb = new StringBuilder();

        for(Member member: this.memberList)
        {
            sb.append(member.getUsername());

            if (member.getClientHost().equals(clientHost))
            {
                sb.append(" (you)");
            }
        }

        return sb.toString();
    }
}
