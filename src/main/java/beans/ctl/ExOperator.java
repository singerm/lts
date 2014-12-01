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

    for (State s : states)
    {
      if (s.formulas.contains(phi))
      {
        // TODO Hole die rückwärtstransitionen und markiere ihre eingänge
      }
    }
  }

}
