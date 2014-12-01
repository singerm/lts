package util;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import beans.Lts;
import beans.State;
import beans.Transition;


public class LtsHelper
{

  private static Stack<State> stack = new Stack<State>();
  private static int depathIndex = 0;
  private static Set<Set<State>> allScc = new HashSet<Set<State>>();


  public static void setPreviouseStates(Lts lts)
  {
    for (State s : lts.getStates())
    {

      for (Transition trans : s.transitions)
      {

        trans.followState.prevStates.add(s);

      }

    }

  }


  public static Set<Set<State>> getSCC(Lts lts)
  {
    return getSCC(lts.getCachedStates());
  }


  public static Set<Set<State>> getSCC(Set<State> input)
  {
    depathIndex = 0;
    allScc.clear();
    stack.clear();
    for (State s : input)
    {
      if (s.deapthIndex == null)
      {
        strongConnected(s);
      }
    }

    return allScc;
  }


  private static void strongConnected(State s)
  {
    s.deapthIndex = depathIndex;
    s.lowlink = depathIndex;
    depathIndex++;
    stack.push(s);

    for (Transition trans : s.transitions)
    {

      if (trans.followState.deapthIndex == null)
      {
        strongConnected(trans.followState);
        s.lowlink = Math.min(s.lowlink, trans.followState.deapthIndex);

      }
    }

    if (s.lowlink == s.deapthIndex)
    {
      Set<State> scc = new HashSet<State>();
      State w = null;
      do
      {
        w = stack.pop();
        scc.add(w);
      }
      while (s.equals(w));
      allScc.add(scc);
    }

  }
}
