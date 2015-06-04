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

        Map<String, Object> agentArgs = new HashMap<String, Object>();
        agentArgs.put("strategy", 4);
        CreationInfo BuyerInfo = new CreationInfo(agentArgs);

        Map<String, Object> agentArgs2 = new HashMap<String, Object>();
        agentArgs2.put("strategy", 5);
        CreationInfo BuyerInfo2 = new CreationInfo(agentArgs2);

        cms.createComponent("Agent Manager", "Logic.ManagerAgentBDI.class", null).getFirstResult(sus);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




        Map<String, Object> agentArgs3 = new HashMap<String, Object>();
        agentArgs3.put("strategy", 6);
        CreationInfo BuyerInfo3 = new CreationInfo(agentArgs3);






        Map<String, Object> agentArgs4 = new HashMap<String, Object>();
        agentArgs4.put("strategy", 3);
        CreationInfo BuyerInfo4 = new CreationInfo(agentArgs4);



        for(int i=0; i<3;i++) {
            String name ="Average Bidder" + i;
            cms.createComponent(name, "Logic.AuctionAgentBDI.class", BuyerInfo).getFirstResult(sus);
        }
        for(int i=0; i<1;i++) {
            String name ="Over Bidder" + i;
            cms.createComponent(name, "Logic.AuctionAgentBDI.class", BuyerInfo2).getFirstResult(sus);
        }
        for(int i=0; i<3;i++) {
            String name ="Under Bidder" + i;
            cms.createComponent(name, "Logic.AuctionAgentBDI.class", BuyerInfo3).getFirstResult(sus);
        }

        for(int i=0; i<20;i++) {
            String name = "Noise" + i;
            cms.createComponent(name, "Logic.AuctionAgentBDI.class", BuyerInfo4).getFirstResult(sus);

        }




    }


}
