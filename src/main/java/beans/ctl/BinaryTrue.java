package beans.ctl;

import java.util.Set;

import beans.Lts;
import beans.State;


public class BinaryTrue
  implements CtlFormula
{

  @Override
  public void reduce(Lts automaton)
  {
    Set<State> states = automaton.getCachedStates();

    for (State state : states)
    {
      state.formulas.add(this);
    }
  }
}
