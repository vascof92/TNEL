package Logic;

/**
 * Created by jorgelima on 10-12-2014.
 */
public class Proposal extends CloneableObject {



    private double price;
    private Request r;


    private AuctionAgent sa;

    public Proposal( Request r, double price, AuctionAgent seller)
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

    public AuctionAgent getSa() {
        return sa;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Logic.Proposal clone() {
        return (Logic.Proposal)super.clone();
    }
}