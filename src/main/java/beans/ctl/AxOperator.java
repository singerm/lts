package beans.ctl;

import java.util.Set;

import beans.Lts;
import beans.State;


public class AxOperator
  implements CtlFormula
{
  private CtlFormula phi;


  public AxOperator(CtlFormula phi)
  {
    // AX phi === !EX (!phi)
    this.phi = new NegationOperator(new ExOperator(new NegationOperator(phi)));
  }


  @Override
  public void reduce(Lts automaton)
  {
    phi.reduce(automaton);

    Set<State> states = automaton.getCachedStates();

    for (State state : states)
    {
      // The equivalence of this operator was stored in phi, so a state satisfies the AX-operator if it satisfies phi
      if (state.formulas.contains(phi))
      {
        state.formulas.add(this);
      }
    }
  }

}
