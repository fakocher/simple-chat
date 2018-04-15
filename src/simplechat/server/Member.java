package simplechat.server;

public class Member {

    private String username;
    private String clientHost;

    public Member(String username, String clientHost)
    {
        this.username = username;
        this.clientHost = clientHost;
    }

    public String getClientHost() {
        return clientHost;
    }

    public String getUsername() {
        return username;
    }
}
