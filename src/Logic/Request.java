package Logic;

public class Request extends CloneableObject {



	public AuctionAgent ba;

	
	public Request(AuctionAgent ba)
	{
		this.ba = ba;


	}
	


	public Logic.Request clone() {
		return (Logic.Request) super.clone();
	}
}
