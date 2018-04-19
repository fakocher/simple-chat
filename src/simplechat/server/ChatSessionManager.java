package simplechat.server;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ChatSessionManager {
	private ArrayList<Session> sessionList = new ArrayList<>();
	MemberListManager memberManager=new MemberListManager();
	
	public String chatSessionRequest(Member requester, String destName,MemberListManager memberManger) {
    	Member dest = memberManager.getByName(destName);
    	if (dest != null) {
    		if (dest.sendRequest()) {
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
	
    public void chatSessionSendMessage(String message, String destName,MemberListManager memberManger) {
    	Member dest = memberManager.getByName(destName);
    	if (dest != null) {
    		dest.retriveMessage(message);
    	}
    }
    
    public void chatSessionQuit(String memberName){
    	int i =0;
    	while(i < sessionList.size()) {
    		if (sessionList.get(i).getMember1().getUsername()==memberName) {
    			sessionList.remove(i);
    			i++;
    		}
    	}
    }
}
