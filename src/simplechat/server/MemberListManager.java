package simplechat.server;

import java.util.ArrayList;

public class MemberListManager {

    private ArrayList<Member> memberList = new ArrayList<>();

    public boolean add(String username, String clientHost)
    {
        if (this.memberListContainsHost(clientHost))
        {
            return false;
        }

        Member newMember = new Member(username, clientHost);
        this.memberList.add(newMember);
        return true;
    }

    private boolean memberListContainsHost(String clientHost)
    {
        for(Member member: this.memberList)
        {
            if(member.getClientHost().equals(clientHost))
                return true;
        }

        return false;
    }

    public boolean remove(String clientHost)
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

    public String toString(String clientHost)
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
