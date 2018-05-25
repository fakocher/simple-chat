package simplechat.server;

import simplechat.client.ClientAPI;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;
/*
 * Date : 04/2018
 * Description : manage chat session (open/close chat, send message, etc.)
 */
class ChatSessionManager {
	private ArrayList<Session> sessionList = new ArrayList<>();
	private MemberListManager memberListManager;

	// get the memberListManager from server. this manager allows to get members info
	ChatSessionManager(MemberListManager memberListManager) {
		this.memberListManager = memberListManager;
	}

	/* session open manager 
	* 1. get receiver and requester information
	* 2. send a request to receiver to start a chat session
	* 3. according to receiver's answer, create or not a chat session 
	*/ 
	boolean handleRequest(String username, UUID uuid) throws RemoteException
	{
		// Check if session already exists
		if (this.getSessionByUUID(uuid) != null)
		{
			return false;
		}
		
		// get receiver members object from username
    	Member dest = memberListManager.getByName(username);
    	// get requester member object from uuid
		Member requester = memberListManager.getByUUID(uuid);

    	if (dest != null) {
    		// get clientAPI to ask for starting a chat session
    	    ClientAPI clientAPI = dest.getClientAPI();
    		if (clientAPI.chatSessionRequest(requester.getUsername())) {
    			// chat session request accepted
        		sessionList.add(new Session(requester, dest));
        		return true;
        	} else {
    			// chat session request refused
        		return false;
        	}
    	} else {
    		// dest member is not found in members list
    		return false;
    	}
	}
	
	// send message to receiver
    boolean sendMessage(String message, UUID uuid) throws RemoteException
	{
		// Get session
		Session session = this.getSessionByUUID(uuid);
		if (session == null) {
			return false;
		}

		// Send message to receiver (dest)
    	Member dest = this.getDest(session, uuid);
		Member sender = this.getSender(session, uuid);
		dest.getClientAPI().receiveMessage(message, sender.getUsername());
		return true;
    }
    
    // quit a chat session
    boolean chatSessionQuit(UUID uuid) throws RemoteException {
		// Get session
		Session session = this.getSessionByUUID(uuid);
		if (session == null) {
			return false;
		}

		// Close session
		this.sessionList.remove(session);

		// Send message to receiver (dest)
		Member dest = this.getDest(session, uuid);
		dest.getClientAPI().receiveMessage("Your chat session was stopped.");
		return true;
	}


    // get session object by client uuid
	private Session getSessionByUUID(UUID uuid)
	{
		// check all running chat session (sessionList)
		for(Session session: this.sessionList)
		{
			// if one of session members has the uuid searched
			if(session.getMember1().getClientUUID().equals(uuid) || session.getMember2().getClientUUID().equals(uuid))
				// return session
				return session;
		}
		return null;
	}

	// get sender
	private Member getSender(Session session, UUID uuid)
	{
		// if one of session members has the uuid searched
		if (session.getMember1().getClientUUID().equals(uuid))
		{
			// return his partner member
			return session.getMember1();
		}

		return session.getMember2();
	}

	// get member partner 
	private Member getDest(Session session, UUID uuid)
	{
		// if one of session members has the uuid searched
		if (session.getMember1().getClientUUID().equals(uuid))
		{
			// return his partner member
			return session.getMember2();
		}
		
		return session.getMember1();
	}
}
