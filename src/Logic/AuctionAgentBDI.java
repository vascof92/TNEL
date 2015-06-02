package Logic;

import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.search.SServiceProvider;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.commons.future.IntermediateDefaultResultListener;
import jadex.micro.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Agent
@Service
@Description("This agent participates in auctions")
@ProvidedServices(@ProvidedService(type= IAuctionService.class))
public class AuctionAgentBDI implements IAuctionService {

    @Agent
    protected BDIAgent agent;

    private ArrayList<Proposal> allProposals;
    private Double balance;
    private Request request;
    private Boolean isProcessing;
    private Integer stock;

    @Belief(updaterate=1000)
    protected long time = System.currentTimeMillis();


    @Plan(trigger=@Trigger(factchangeds = "time"))
    protected void printTime(){
    }

    @Goal(recur=true)
    public class AuctionGoal {
        public AuctionGoal() {

            this.units = 0;
        }

        public int units;
        @GoalRecurCondition(beliefs="time")
        public boolean checkRecur() {

            // The buyer's job is done when all required units have been purchased
            return true;
        }
    }

    @Plan(trigger=@Trigger(goals=AuctionGoal.class))
    protected void launchRequestPlan() throws InterruptedException {


        //System.out.println(isProcessing);



        if (request != null) {
            if (!isProcessing) {
                processProposals();
            }} else {
            if(stock>0) {
                System.out.println(agent.getAgentName() + " Lançou leilão. "+"Balance: "+balance+". Stock: "+stock);
                request = new Request(this);
                SServiceProvider.getServices(agent.getServiceProvider(), Logic.IAuctionService.class, RequiredServiceInfo.SCOPE_PLATFORM).addResultListener(new IntermediateDefaultResultListener<Logic.IAuctionService>() {
                    public void intermediateResultAvailable(IAuctionService is) {

                        allProposals.clear();
                        is.requireProposal(request.clone());
                    }
                });
            }
        }


        throw new PlanFailureException();
    }


    @AgentCreated
    public void init() {
        this.balance =(double)1000;
        isProcessing = false;
        allProposals = new ArrayList<Proposal>();
        stock = (Integer) 10;
        this.agent.dispatchTopLevelGoal(new AuctionGoal());
    }

    @AgentBody
    public void body(){
    }


    public Proposal chooseProposal()
    {
        proposalComparator comp = new proposalComparator();
        Collections.sort(allProposals, comp);
        return allProposals.get(0);
    }

    public Proposal chooseProposal2()
    {
        proposalComparator comp = new proposalComparator();
        Collections.sort(allProposals, comp);
        return allProposals.get(1);
    }

    public class proposalComparator implements Comparator<Proposal> {

        @Override
        public int compare(Proposal o1, Proposal o2) {
            // TODO Auto-generated method stub
            if(o1.getPrice() < o2.getPrice())
            {
                return 1;
            }
            else if(o1.getPrice() == o2.getPrice())
                return 0;
            else
                return -1;

        }
    }

    @Override
    public IFuture<Boolean> requireProposal(Request r) { //in seller type agent, meaning seller agent receives this request from buyer//TODO REMOVE BEFORE DEPLOYMENT

        //Verification if sender is receiver not to respond to own
        if (this.agent.getAgentName().equals(r.ba.agent.getAgentName())) {
            //System.out.println("Sou eu tá quieto");
        }
        else{
            //System.out.println(this.agent.getAgentName() + " sent request to " + r.ba.agent.getAgentName());
            int price = (int)(Math.random()*500)+1;
            while(price > balance){
                price = (int)(Math.random()*500)+1;
            }
            //System.out.println("price:  " + price);
            Request req = r.clone();
            Proposal p = new Proposal(req, price, this);
            r.ba.sendProposal(p.clone());

            return new Future<Boolean>(true);

        }
        return null;
    }

    @Override
    public IFuture<Boolean> acceptedProposal(Proposal p) {

        System.out.println(this.agent.getAgentName()+" ganhou leilao e pagou "+ p.getPrice() );


        if(balance -p.getPrice()>=0) {

            stock++;
            balance -= p.getPrice();
            System.out.println("Retirou "+p.getPrice() );
            //System.out.println("Comprador: "+ agent.getAgentName());
           // System.out.println("Balance"+ balance);
            //System.out.println("stock"+ stock);
            return new Future<Boolean>(true);
        }else{
            return new Future<Boolean>(false);
        }
    }

    @Override
    public IFuture<Double> negotiation(Proposal p, int count) {
        return null;
    }

    @Override
    public IFuture<Boolean> retrieveSeller() {
        return null;
    }

    @Override
    public IFuture<Boolean> sendProposal(Proposal p) {

        // System.out.println("Buyer received a valid proposal, analysing...");


        allProposals.add(p);

        return new Future<Boolean>(true);

    }

    @Override
    public IFuture<Boolean> retrieveBuyer() {
        return null;
    }


    public void processProposals()
    {

        //System.out.println("Processing Proposal");
        isProcessing = true;
        int count = 1;

        final Proposal chosen = chooseProposal();
        final Proposal chosen2 = chooseProposal2();


        //System.out.println("Leilão ganho por "+ chosen.getSa().agent.getAgentName()+ " com custo de "+ chosen.getPrice());
        //System.out.println("Custo: "+ chosen2.getPrice());


        Proposal chosenClone2 = chosen2.clone();

        System.out.println("seller: "+ agent.getAgentName()+ " | chosen: "+ chosen.getSa().agent.getAgentName()+"; chosen.price: "+chosen.getPrice()+" | chosen2: "+ chosen2.getSa().agent.getAgentName()+"; chosen2.price: "+chosen2.getPrice());
        //System.out.println("chosen2: "+ chosen2.getSa().agent.getAgentName()+"; chosen2.price: "+chosen2.getPrice());

        if(chosen.getSa().acceptedProposal(chosenClone2).get()) {

            //System.out.println("entrou");

            this.stock--;
            this.balance += chosenClone2.getPrice();
            System.out.println("Adicionou "+chosenClone2.getPrice() );
            //System.out.println(agent.getAgentName()+" recebeu "+ chosenClone2.getPrice()+"do "+ chosen.getSa().agent.getAgentName());
           // System.out.println("Vendedor: "+ agent.getAgentName());
          //  System.out.println("Balance"+ balance);
          //  System.out.println("stock"+ stock);

        }



        request = null;
        isProcessing = false;
    }





}