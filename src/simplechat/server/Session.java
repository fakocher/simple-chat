package simplechat.server;

public class Session {
	private Member member1 ;
	private Member member2 ;
	public Session(Member member1, Member member2) {
		this.member1 = member1;
		this.member2 = member2;
	}
	public Member getMember1() {
		return member1;
	}
	public Member getMember2() {
		return member2;
	}
	
	
}
