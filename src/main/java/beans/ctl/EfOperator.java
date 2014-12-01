package beans.ctl;

import java.util.Set;

import beans.Lts;
import beans.State;


public class EfOperator
  implements CtlFormula
{
  private CtlFormula phi = null;


  public EfOperator(CtlFormula phi)
  {
    // EF === E[true U phi]
    this.phi = new EuOperator(new BinaryTrue(), phi);
  }


  @Override
  public void reduce(Lts automaton)
  {
    phi.reduce(automaton);

    Set<State> states = automaton.getCachedStates();

    for (State state : states)
    {
      // The equivalence of this operator was stored in phi, so a state satisfies the EF-operator if it satisfies phi
      if (state.formulas.contains(phi))
      {
        state.formulas.add(this);
      }
    }
  }

}
