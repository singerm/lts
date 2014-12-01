package beans.ctl;

import java.util.Set;

import beans.Lts;
import beans.State;


public class DisjunctionOperator
  implements CtlFormula
{
  public CtlFormula phi = null;
  public CtlFormula psi = null;


  public DisjunctionOperator(CtlFormula phi, CtlFormula psi)
  {
    this.phi = phi;
    this.psi = psi;
  }


  @Override
  public void reduce(Lts automaton)
  {
    phi.reduce(automaton);
    psi.reduce(automaton);

    Set<State> states = automaton.getCachedStates();

    for (State s : states)
    {
      if (s.formulas.contains(phi) || s.formulas.contains(psi))
      {
        s.formulas.add(this);
      }
    }
  }
}
