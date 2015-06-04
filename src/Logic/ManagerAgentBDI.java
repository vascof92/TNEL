package Logic;

import jadex.bdiv3.BDIAgent;
import jadex.bridge.service.annotation.Service;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.*;

import java.util.ArrayList;

/**
 * Created by jorgelima on 04-06-2015.
 */

@Agent
@Service
@Description("This agents manages the auctions")
@ProvidedServices(@ProvidedService(type=IManagerService.class))
public class ManagerAgentBDI implements IManagerService{

    @Agent
    protected BDIAgent agent;

    protected ArrayList<Integer> prices;

    @AgentBody
    public void init(){
        prices = new ArrayList<>();
    }


    @Override
    public IFuture<Boolean> submitFinalPrice(String ba, int price) {
        //System.out.println("Preço submetido para análise..." + price + " por " + ba);
        prices.add(price);
        System.out.println("prices: " + prices);
        return new Future<>(true);
    }

    @Override
    public Future<ArrayList<Integer>> requestPriceList() {

        return new Future<>((ArrayList<Integer>) prices.clone());
    }

}
