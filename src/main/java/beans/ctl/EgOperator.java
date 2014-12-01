package beans.ctl;

import java.util.HashSet;
import java.util.Set;

import beans.Lts;
import beans.State;


public class EgOperator
  implements CtlFormula
{
  public CtlFormula phi = null;


  public EgOperator(CtlFormula phi)
  {
    this.phi = phi;
  }


  @Override
  public void reduce(Lts automaton)
  {
    phi.reduce(automaton);

    Set<State> states = automaton.getCachedStates();
    HashSet<State> phiStates = new HashSet<State>();

    // Find states that satisfy phi
    for (State s : states)
    {
      if (s.formulas.contains(phi))
      {
        phiStates.add(s);
      }
    }

    // TODO find SCC's in <phiStates>, then mark all states from where at least one SCC is reachable
  }

}
