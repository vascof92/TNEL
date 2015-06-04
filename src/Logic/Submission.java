package Logic;

/**
 * Created by jorgelima on 04-06-2015.
 */
public class Submission extends CloneableObject {


    String winner;
    int price;
    public Request t;


    public Submission(String winner, int price, Request r)
    {
        this.t = r.clone();
        this.price = price;
        this.winner = winner;


    }



    public Submission clone() {
        return (Submission) super.clone();
    }
}
