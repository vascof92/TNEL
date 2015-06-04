package Logic;

import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.impl.PlanFailureException;

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
@Arguments({

        @Argument(name="strategy", clazz=Integer.class, defaultvalue="1")

})
@Service
@Description("This agent participates in auctions")
@ProvidedServices(@ProvidedService(type= IAuctionService.class))
public class AuctionAgentBDI implements IAuctionService {

    @Agent
    protected BDIAgent agent;

    private ArrayList<Proposal> allProposals;
    private Integer balance;
    private Request request;
    private Boolean isProcessing;
    private Integer stock;
    private Integer strategy;
    private long starttime;
    private boolean ended;
    private Request r;
    private int sells;
    private int buys;
    ArrayList<Integer> pricelist;

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


            long end = (time-starttime);

            if(end>60000 & !ended){
                int value = (int)(balance + stock*calculateAverage(pricelist));
                System.out.println(agent.getAgentName()+" acabou o dia com "+ balance+"€ e "+ stock+" produtos. Valorizaçao = "+ value+". Comprou "+buys+" e vendeu "+sells );
                ended = true;

            }


            return !ended;
        }
    }

    @Plan(trigger=@Trigger(goals=AuctionGoal.class))
    protected void launchRequestPlan() throws InterruptedException {

        if (request != null) {
            //System.out.println(agent.getAgentName()+" broke with "+ balance+"€ and "+stock+"price" );
            if (!isProcessing) {
                processProposals();
            }
        }
        else {
            if(stock>0) {
                //System.out.println(agent.getAgentName() + " Lançou leilão. "+"Balance: "+balance+". Stock: "+stock);
                request = new Request(this);
                long rand = (long)(Math.random()*1000);
                Thread.sleep(rand);

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
        this.balance =10000;
        isProcessing = false;
        allProposals = new ArrayList<Proposal>();
        starttime = System.currentTimeMillis();
        stock = 10;
        sells =0;
        buys=0;
        ended= false;
        strategy = (Integer) agent.getArgument("strategy");
       // System.out.println(strategy);
        this.r = new Request(this);
        this.agent.dispatchTopLevelGoal(new AuctionGoal());
        SServiceProvider.getServices(agent.getServiceProvider(), Logic.IManagerService.class, RequiredServiceInfo.SCOPE_PLATFORM).addResultListener(new IntermediateDefaultResultListener<Logic.IManagerService>() {
            public void intermediateResultAvailable(IManagerService is) {
                is.registerAgent(r);
            }
        });

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
        if(allProposals.size()==1){
            return allProposals.get(0);
        }
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

        }
        else{

            int price = calculatePrice(strategy);



            Request req = r.clone();
            Proposal p = new Proposal(req, price, this);
            r.ba.sendProposal(p.clone());

            return new Future<Boolean>(true);

        }
        return null;
    }

    @Override
    public IFuture<Boolean> acceptedProposal(Proposal p) {




        if(balance -p.getPrice()>=0) {

            stock++;
            buys ++;
            balance -= (int)p.getPrice();


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


        allProposals.add(p);

        return new Future<Boolean>(true);

    }

    @Override
    public IFuture<Boolean> retrieveBuyer() {
        return null;
    }


    public void processProposals()
    {

        isProcessing = true;
        int count = 1;

        final Proposal chosen = chooseProposal();
        final Proposal chosen2 = chooseProposal2();




        Proposal chosenClone2 = chosen2.clone();


        if(chosen.getSa().acceptedProposal(chosenClone2).get()) {

            final int finalPrice = (int)chosenClone2.getPrice();

            this.stock--;
            sells++;
            this.balance += finalPrice;

            SServiceProvider.getServices(agent.getServiceProvider(), Logic.IManagerService.class, RequiredServiceInfo.SCOPE_PLATFORM).addResultListener(new IntermediateDefaultResultListener<Logic.IManagerService>() {
                public void intermediateResultAvailable(IManagerService is) {

                    allProposals.clear();
                    is.submitFinalPrice(new Submission(chosen.getSa().agent.getAgentName(), finalPrice, r));
                }
            });



        }



        request = null;
        isProcessing = false;
    }


    public int calculatePrice(int strategy){


        SServiceProvider.getServices(agent.getServiceProvider(), Logic.IManagerService.class, RequiredServiceInfo.SCOPE_PLATFORM).addResultListener(new IntermediateDefaultResultListener<Logic.IManagerService>() {
            public void intermediateResultAvailable(IManagerService is) {

                allProposals.clear();
                //System.out.println(agent.getAgentName());
                Future<ArrayList<Integer>> prices = is.requestPriceList();
                pricelist = prices.get();

            }
        });

        switch(strategy){

            case 1://all-in
                return balance;

            case 2://balanced
                return balance/2;
            case 3://ruido
                int bid = (int)((Math.random()*(balance/2))+(balance/2));

                return bid;
            /*case 4://média das ultimas 3
                if(pricelist.size()>=3){
                    int bid1 = pricelist.get(0);
                    int bid2 = pricelist.get(1);
                    int bid3 = pricelist.get(2);
                    int average = (bid1+bid2+bid3)/3;
                    if(average<balance){
                        return average;
                    }else{
                        return balance;
                    }
                }else{
                    //return 1;
                    bid = (int)((Math.random()*(balance/2)));
                    System.out.println("balance: "+balance+"; random bid: "+bid);
                    return bid;
                }
            case 5://overbid
                if(pricelist.size()>=3){
                    int bid1 = pricelist.get(0);
                    int bid2 = pricelist.get(1);
                    int bid3 = pricelist.get(2);
                    int average = (bid1+bid2+bid3)/3;
                    if((int)(1.3*average)<balance){
                        return (int)(1.3*average);
                    }else{
                        return balance;
                    }
                }else{
                    //return 1;
                    bid = (int)((Math.random()*(balance/2)));
                    System.out.println("balance: "+balance+"; random bid: "+bid);
                    return bid;
                }
            case 6://underbid
                if(pricelist.size()>=3){
                    int bid1 = pricelist.get(0);
                    int bid2 = pricelist.get(1);
                    int bid3 = pricelist.get(2);
                    int average = (bid1+bid2+bid3)/3;
                    if((int)(0.7*average)<balance){
                        return (int)(0.7*average);
                    }else{
                        return balance;
                    }
                }else{
                    //return 1;
                    bid = (int)((Math.random()*(balance/2)));
                    System.out.println("balance: "+balance+"; random bid: "+bid);
                    return bid;
                }*/
            case 4://média das ultimas 3
                if(pricelist.isEmpty()){
                    bid = (int)((Math.random()*(balance)));

                    return bid;
                }
                int average = (int)(calculateAverage(pricelist)*deviation());
                if(average<balance){
                    return average;
                }else{

                    return balance;
                }

            case 5://overbid
                if(pricelist.isEmpty()){
                    bid = (int)((Math.random()*(balance)));

                    return bid;
                }
                average = (int)(calculateAverage(pricelist)*deviation());
                if((int)(1.3*average)<balance){
                    return (int)(1.3*average);
                }else{

                    return balance;
                }

            case 6://underbid
                if(pricelist.isEmpty()){
                    bid = (int)((Math.random()*(balance)));

                    return bid;
                }
                average = (int)(calculateAverage(pricelist)*deviation());
                if((int)(0.7*average)<balance){
                    return (int)(0.7*average);
                }else{

                    return balance;
                }


        }

        return 0;


    }


    private double calculateAverage(ArrayList <Integer> marks) {
        Integer sum = 0;
        if(!marks.isEmpty()) {
            for (Integer mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }


    private double deviation(){

        double deviation = 1+(((Math.random()*20)-10)/100);


        return deviation;

    }





}