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
        agentArgs.put("strategy",1 );
        CreationInfo BuyerInfo = new CreationInfo(agentArgs);

        Map<String, Object> agentArgs2 = new HashMap<String, Object>();
        agentArgs2.put("strategy",2 );
        CreationInfo BuyerInfo2 = new CreationInfo(agentArgs2);

        cms.createComponent("Agent 1", "Logic.AuctionAgentBDI.class", BuyerInfo).getFirstResult(sus);

        cms.createComponent("Agent 2", "Logic.AuctionAgentBDI.class", BuyerInfo2).getFirstResult(sus);

        cms.createComponent("Agent 3", "Logic.AuctionAgentBDI.class", BuyerInfo2).getFirstResult(sus);

        cms.createComponent("Agent 4", "Logic.AuctionAgentBDI.class", BuyerInfo2).getFirstResult(sus);





    }


}
