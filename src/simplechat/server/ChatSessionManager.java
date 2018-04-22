package simplechat.server;

import simplechat.client.ClientAPI;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ChatSessionManager {
	private ArrayList<Session> sessionList = new ArrayList<>();
	private MemberListManager memberListManager;

	ChatSessionManager(MemberListManager memberListManager) {
		this.memberListManager = memberListManager;
	}

	public String handleRequest(Member requester, String destName, MemberListManager memberManger) throws RemoteException {
    	Member dest = memberListManager.getByName(destName);
    	if (dest != null) {
    	    ClientAPI clientAPI = dest.getClientAPI();
    		if (clientAPI.chatSessionRequest()) {
        		sessionList.add(new Session(requester,dest));
        		// accepter
        		return "OK";
        	} else {
        		//refuse chat
        		return "KO";
        	}
    	} else {
    		// dest n'est pas dans la liste des members
    		System.out.println(destName + " n'est pas présent");
    		return "KO";
    	}
	}
	
    public void sendMessage(String message, String destName, MemberListManager memberManger) throws RemoteException {
    	Member dest = memberListManager.getByName(destName);
    	if (dest != null) {
            ClientAPI clientAPI = dest.getClientAPI();
            clientAPI.receiveMessage(message);
    	}
    }
    
    public void chatSessionQuit(String memberName){
    	int i = 0;
    	while(i < sessionList.size()) {
    		if (sessionList.get(i).getMember1().getUsername() == memberName) {
    			sessionList.remove(i);
    			i++;
    		}
    	}
    }
}
