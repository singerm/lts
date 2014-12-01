package beans.ctl;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import beans.Lts;
import beans.State;


public class EuOperator
  implements CtlFormula
{
  public CtlFormula phi = null;
  public CtlFormula psi = null;


  public EuOperator(CtlFormula phi, CtlFormula psi)
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
    HashSet<State> psiStates = new HashSet<State>();

    // Find states that satisfy psi
    for (State s : states)
    {
      if (s.formulas.contains(psi))
      {
        psiStates.add(s);
      }
    }

    // Mark predecessors
    Queue<State> toCheck = new LinkedBlockingQueue<State>(psiStates);
    while (!toCheck.isEmpty())
    {
      // TODO add (unmarked) predecessors to queue and mark them
    }

  }

}
