package operations;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import beans.Lts;
import beans.State;


/**
 * Realization of the Parallel-Execution-Operator aka ||.
 * 
 * @author Sebastian
 *
 */
public class ParallelComposition
{
  /**
   * Creates a new composed LTS from several given LTS as defined by the ||-Operator.
   * 
   * @param singleAutomata
   *          Several automata to merge
   * @return A merged automaton
   */
  public static Lts intersect(Collection<Lts> singleAutomata)
  {
    Lts mergedAutomaton = new Lts();

    /*
     * The states
     */
    for (Lts singleAutomaton : singleAutomata)
    {

      // The first automaton is 'merged' via copy
      if (mergedAutomaton.getStates().size() == 0)
      {
        int numStates = singleAutomaton.getStates().size();
        HashSet<State> states = new HashSet<State>(numStates);

        for (State stateToAdd : singleAutomaton.getStates())
        {
          states.add(new State(stateToAdd.name));
        }

        mergedAutomaton.setStates(states);
      }

      // The other automata are added via 'cross-product' with existing states and new states
      else
      {
        int numStates = mergedAutomaton.getStates().size() * singleAutomaton.getStates().size();
        HashSet<State> mergedStates = new HashSet<State>(numStates);

        for (State existingState : mergedAutomaton.getStates())
        {
          for (State stateToMerge : singleAutomaton.getStates())
          {
            mergedStates.add(new State(mergeStateNames(existingState.name, stateToMerge.name)));
          }
        }

        mergedAutomaton.setStates(mergedStates);
      }
    }

    /*
     * The Transitions
     */
    // TODO

    return mergedAutomaton;
  }


  public static Lts intersectByActions(Collection<Lts> automata, Set<String> actions)
  {
    // TODO implementation, no priority (not part of the task?)

    return null;
  }


  private static String mergeStateNames(String firstStateName, String secondStateName)
  {
    return firstStateName + ", " + secondStateName;
  }

}
