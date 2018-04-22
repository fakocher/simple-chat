package simplechat.server;

import simplechat.client.ClientAPI;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;

class ChatSessionManager {
	private ArrayList<Session> sessionList = new ArrayList<>();
	private MemberListManager memberListManager;

	ChatSessionManager(MemberListManager memberListManager) {
		this.memberListManager = memberListManager;
	}

	boolean handleRequest(String username, UUID uuid) throws RemoteException
	{
		// Check if session already exists
		if (this.getSessionByUUID(uuid) != null)
		{
			return false;
		}

    	Member dest = memberListManager.getByName(username);
		Member requester = memberListManager.getByUUID(uuid);

    	if (dest != null) {
    	    ClientAPI clientAPI = dest.getClientAPI();
    		if (clientAPI.chatSessionRequest()) {
        		sessionList.add(new Session(requester, dest));
        		// Connection accepted
        		return true;
        	} else {
    			// Connection refused
        		return false;
        	}
    	} else {
    		// dest n'est pas dans la liste des members
    		return false;
    	}
	}
	
    boolean sendMessage(String message, UUID uuid) throws RemoteException
	{
		// Get session
		Session session = this.getSessionByUUID(uuid);
		if (session == null) {
			return false;
		}

		// Send message to dest
    	Member dest = this.getDest(session, uuid);
		dest.getClientAPI().receiveMessage(message);
		return true;
    }
    
    boolean chatSessionQuit(UUID uuid) throws RemoteException {
		// Get session
		Session session = this.getSessionByUUID(uuid);
		if (session == null) {
			return false;
		}

		// Close session
		this.sessionList.remove(session);

		// Send message to dest
		Member dest = this.getDest(session, uuid);
		dest.getClientAPI().receiveMessage("Your chat session was stopped.");
		return true;
	}


	private Session getSessionByUUID(UUID uuid)
	{
		for(Session session: this.sessionList)
		{
			if(session.getMember1().getClientUUID().equals(uuid) || session.getMember2().getClientUUID().equals(uuid))
				return session;
		}

		return null;
	}

	private Member getDest(Session session, UUID uuid)
	{
		if (session.getMember1().getClientUUID().equals(uuid))
		{
			return session.getMember2();
		}

		return session.getMember1();
	}
}
