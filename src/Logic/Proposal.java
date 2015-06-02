package Logic;

/**
 * Created by jorgelima on 10-12-2014.
 */
public class Proposal extends CloneableObject {



    private double price;
    private Request r;


    private AuctionAgentBDI sa;

    public Proposal( Request r, double price, AuctionAgentBDI seller)
    {

        this.price = price;
        this.r=r;
        this.sa = seller;
    }

    public double getPrice() {
        return price;
    }

    public Request getR() {
        return r;
    }

    public AuctionAgentBDI getSa() {
        return sa;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Logic.Proposal clone() {
        return (Logic.Proposal)super.clone();
    }
}