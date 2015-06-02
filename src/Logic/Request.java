package Logic;

public class Request extends CloneableObject {



	public AuctionAgentBDI ba;

	
	public Request(AuctionAgentBDI ba)
	{
		this.ba = ba;


	}
	


	public Logic.Request clone() {
		return (Logic.Request) super.clone();
	}
}
