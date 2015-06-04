package Logic;

import jadex.bdiv3.BDIAgent;
import jadex.bridge.service.annotation.Service;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

    String logFile = "AuctionLog.txt";

    protected ArrayList<Integer> prices;
    protected ArrayList<AuctionAgentBDI> registeredAgents;


    @AgentBody
    public void init(){
        prices = new ArrayList<>();
        registeredAgents = new ArrayList<>();


        writeToFile("Starting auctions!");
    }


    @Override
    public IFuture<Boolean> registerAgent(Request r) {
        String register = "Registered user: " + r.ba.agent.getAgentName();
        registeredAgents.add(r.ba);
        writeToFile(register);

        return new Future<>(true);
    }

    @Override
    public IFuture<Boolean> submitFinalPrice(Submission s) {
        String submission = s.t.ba.agent.getAgentName() + " auction ended: " + s.winner + " won and paid " + s.price;

        writeToFile(submission);
        prices.add(s.price);

        return new Future<>(true);
    }

    @Override
    public Future<ArrayList<Integer>> requestPriceList() {

        return new Future<>((ArrayList<Integer>) prices.clone());
    }


    public void writeToFile(String line){
        try{
            FileWriter fileWriter = new FileWriter(logFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(line);
            bufferedWriter.newLine();

            bufferedWriter.close();



        }
        catch(IOException ex){
            System.out.println("Error writing to file '" + logFile + "'");
            ex.printStackTrace();
        }
    }

}
