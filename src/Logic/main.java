package Logic;

import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.search.SServiceProvider;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.IFuture;
import jadex.commons.future.ThreadSuspendable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jorgelima on 20-11-2014.
 */
public class main {

    public static void main(String[] args) throws IOException {

        String[] defargs = new String[]
                {
                        "-gui", "false",
                        "-welcome", "false",
                        "-cli", "false",
                        "-printpass", "false"
                };
        String[] newargs = new String[defargs.length+args.length];
        System.arraycopy(defargs, 0, newargs, 0, defargs.length);
        System.arraycopy(args, 0, newargs, defargs.length, args.length);

        IComponentManagementService cms;

        IFuture<IExternalAccess> platfut = Starter.createPlatform(defargs);
        final ThreadSuspendable sus = new ThreadSuspendable();
        final IExternalAccess platform = platfut.get(sus);
        System.out.println("Started platform: "+platform.getComponentIdentifier());


        cms = SServiceProvider.getService(platform.getServiceProvider(),
                IComponentManagementService.class, RequiredServiceInfo.SCOPE_PLATFORM).get(sus);



        cms.createComponent("Agent Manager", "Logic.ManagerAgentBDI.class", null).getFirstResult(sus);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Map<String, Object> agentArgs4 = new HashMap<String, Object>();
        agentArgs4.put("strategy", 3);
        CreationInfo BuyerInfo4 = new CreationInfo(agentArgs4);

        Map<String, Object> agentArgs = new HashMap<String, Object>();
        agentArgs.put("strategy", 4);
        CreationInfo BuyerInfo = new CreationInfo(agentArgs);

        Map<String, Object> agentArgs2 = new HashMap<String, Object>();
        agentArgs2.put("strategy", 5);
        CreationInfo BuyerInfo2 = new CreationInfo(agentArgs2);


        Map<String, Object> agentArgs3 = new HashMap<String, Object>();
        agentArgs3.put("strategy", 6);
        CreationInfo BuyerInfo3 = new CreationInfo(agentArgs3);

        Map<String, Object> agentArgs7 = new HashMap<String, Object>();
        agentArgs7.put("strategy", 7);
        CreationInfo BuyerInfo7 = new CreationInfo(agentArgs7);

        Map<String, Object> agentArgs8 = new HashMap<String, Object>();
        agentArgs8.put("strategy", 8);
        CreationInfo BuyerInfo8 = new CreationInfo(agentArgs8);

        Map<String, Object> agentArgs9 = new HashMap<String, Object>();
        agentArgs9.put("strategy", 9);
        CreationInfo BuyerInfo9 = new CreationInfo(agentArgs9);


        Map<String, Object> agentArgs10 = new HashMap<String, Object>();
        agentArgs10.put("strategy", 1);
        CreationInfo BuyerInfo10 = new CreationInfo(agentArgs10);

        Map<String, Object> agentArgs11 = new HashMap<String, Object>();
        agentArgs11.put("strategy", 2);
        CreationInfo BuyerInfo11 = new CreationInfo(agentArgs10);



        for(int i=0; i<3;i++) {
            String name ="AllIn bidder " + i;
            cms.createComponent(name, "Logic.AuctionAgentBDI.class", BuyerInfo10).getFirstResult(sus);
        }

        for(int i=0; i<3;i++) {
            String name ="halfIn bidder " + i;
            cms.createComponent(name, "Logic.AuctionAgentBDI.class", BuyerInfo11).getFirstResult(sus);
        }


        for(int i=0; i<3;i++) {
            String name ="Average Bidder" + i;
            cms.createComponent(name, "Logic.AuctionAgentBDI.class", BuyerInfo).getFirstResult(sus);
        }
        for(int i=0; i<3;i++) {
            String name ="Over Bidder" + i;
            cms.createComponent(name, "Logic.AuctionAgentBDI.class", BuyerInfo2).getFirstResult(sus);
        }
        for(int i=0; i<3;i++) {
            String name ="Under Bidder" + i;
            cms.createComponent(name, "Logic.AuctionAgentBDI.class", BuyerInfo3).getFirstResult(sus);
        }

        for(int i=0; i<3;i++) {
            String name ="Average Bidder(L3)" + i;
            cms.createComponent(name, "Logic.AuctionAgentBDI.class", BuyerInfo7).getFirstResult(sus);
        }

        for(int i=0; i<3;i++) {
            String name ="Over Bidder(L3)" + i;
            cms.createComponent(name, "Logic.AuctionAgentBDI.class", BuyerInfo8).getFirstResult(sus);
        }

        for(int i=0; i<3;i++) {
            String name ="Under Bidder(L3)" + i;
            cms.createComponent(name, "Logic.AuctionAgentBDI.class", BuyerInfo9).getFirstResult(sus);
        }

        for(int i=0; i<1;i++) {
            String name = "Noise" + i;
            cms.createComponent(name, "Logic.AuctionAgentBDI.class", BuyerInfo4).getFirstResult(sus);

        }




    }


}
