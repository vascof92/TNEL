package Logic;

import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.search.SServiceProvider;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.IFuture;
import jadex.commons.future.ThreadSuspendable;

import java.io.IOException;

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
        cms.createComponent("Agent 1", "Logic.AuctionAgentBDI.class", null).getFirstResult(sus);

        cms.createComponent("Agent 2", "Logic.AuctionAgentBDI.class", null).getFirstResult(sus);

        cms.createComponent("Agent 3", "Logic.AuctionAgentBDI.class", null).getFirstResult(sus);

        cms.createComponent("Agent 4", "Logic.AuctionAgentBDI.class", null).getFirstResult(sus);
        cms.createComponent("Agent 5", "Logic.AuctionAgentBDI.class", null).getFirstResult(sus);

        cms.createComponent("Agent 6", "Logic.AuctionAgentBDI.class", null).getFirstResult(sus);

        cms.createComponent("Agent 7", "Logic.AuctionAgentBDI.class", null).getFirstResult(sus);

        cms.createComponent("Agent 8", "Logic.AuctionAgentBDI.class", null).getFirstResult(sus);

        cms.createComponent("Agent 9", "Logic.AuctionAgentBDI.class", null).getFirstResult(sus);
        cms.createComponent("Agent 10", "Logic.AuctionAgentBDI.class", null).getFirstResult(sus);

        cms.createComponent("Agent 11", "Logic.AuctionAgentBDI.class", null).getFirstResult(sus);

        cms.createComponent("Agent 12", "Logic.AuctionAgentBDI.class", null).getFirstResult(sus);

        cms.createComponent("Agent 13", "Logic.AuctionAgentBDI.class", null).getFirstResult(sus);


    }


}
