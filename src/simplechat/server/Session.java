package simplechat.server;
/*
 * Date : 04/2018
 * Description : allows to create a chat session with 2 members 
 */
public class Session {
	private Member member1 ;
	private Member member2 ;
	
	// create a session with 2 members
	public Session(Member member1, Member member2) {
		this.member1 = member1;
		this.member2 = member2;
	}
	
	// return member1 of chat session
	public Member getMember1() {
		return member1;
	}
	
	// return member2 of chat session
	public Member getMember2() {
		return member2;
	}
	
	
}
