package beans.ctl;

import java.util.Set;

import beans.Lts;
import beans.State;


public class ExOperator
  implements CtlFormula
{
  public CtlFormula phi = null;


  public ExOperator(CtlFormula phi)
  {
    this.phi = phi;
  }


  @Override
  public void reduce(Lts automaton)
  {
    phi.reduce(automaton);

    Set<State> states = automaton.getCachedStates();

    for (State phiCandidate : states)
    {
      if (phiCandidate.formulas.contains(phi))
      {
        // All direct predecessors of phi-satisfying states satisfy the EU-operator
        mark(phiCandidate.prevStates);
      }
    }
  }


  private void mark(Set<State> states)
  {
    for (State state : states)
    {
      state.formulas.add(this);
    }
  }

}
