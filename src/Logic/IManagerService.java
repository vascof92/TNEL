package Logic;

import jadex.bdiv3.BDIAgent;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;

import java.util.ArrayList;

/**
 * Created by jorgelima on 04-06-2015.
 */
public interface IManagerService {

    public IFuture<Boolean> registerAgent(Request r);

    public IFuture<Boolean> submitFinalPrice(Submission s);

    public Future<ArrayList<Integer>> requestPriceList();
}
