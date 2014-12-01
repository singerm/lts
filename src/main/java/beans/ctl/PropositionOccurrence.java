package beans.ctl;

import java.util.Set;

import beans.Lts;
import beans.Proposition;
import beans.State;


public class PropositionOccurrence
  implements CtlFormula
{
  private Proposition proposition = null;


  public PropositionOccurrence(Proposition proposition)
  {
    this.proposition = proposition;
  }


  @Override
  public void reduce(Lts automaton)
  {
    Set<State> states = automaton.getCachedStates();

    for (State s : states)
    {
      if (s.props.contains(proposition))
      {
        s.formulas.add(this);
      }
    }
  }
}
