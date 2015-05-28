package Logic;

import jadex.commons.future.IFuture;


public interface IAuctionService
{
  public IFuture<Boolean> requireProposal(Request r);
  public IFuture<Boolean> acceptedProposal(Logic.Proposal p);
  public IFuture<Double> negotiation(Logic.Proposal p, int count);
  public IFuture<Boolean> retrieveSeller();
  public IFuture<Boolean> sendProposal(Proposal p);    // the elements received by this function are used in the buyer agent.
  public IFuture<Boolean> retrieveBuyer();

}