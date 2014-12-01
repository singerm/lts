package beans.ctl;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import util.LtsHelper;
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

    Set<State> phiStates = findPhiStates(automaton);
    Queue<State> candidates = new LinkedBlockingQueue<State>();

    // SCCs have to be calculated _only_ over states that satisfy phi
    Set<Set<State>> sccs = LtsHelper.getSCC(phiStates);

    for (Set<State> scc : sccs)
    {
      // All states in such SCC directly satisfy the EG-operator and are therefore candidates
      candidates.addAll(scc);
    }

    while (!candidates.isEmpty())
    {
      State candidate = candidates.remove();

      // Only mark a state if it directly satisfies phi
      // Avoid marking the same state twice (Side-effect: avoids running into an endless loop)
      if (candidate.formulas.contains(phi) && !candidate.formulas.contains(this))
      {
        candidate.formulas.add(this);

        // Predecessors of a candidate are also candidates
        candidates.addAll(candidate.prevStates);
      }
    }
  }


  private Set<State> findPhiStates(Lts automaton)
  {
    Set<State> states = automaton.getCachedStates();
    HashSet<State> phiStates = new HashSet<State>();

    for (State s : states)
    {
      if (s.formulas.contains(phi))
      {
        phiStates.add(s);
      }
    }

    return phiStates;
  }

}
