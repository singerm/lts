package operations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import beans.Lts;
import beans.State;
import beans.Transition;


/**
 * Realization of the Parallel-Execution-Operator aka ||.
 * 
 * @author Sebastian
 *
 */
public class ParallelComposition
{
  private Queue<Tuple<State, State>> undevelopedStates = new LinkedBlockingQueue<Tuple<State, State>>();
  private Set<State> targetStates = new HashSet<State>();

  private static final String STATE_CONCAT = "#";


  /**
   * Creates a merged automaton from two given automata using the parallel-operator (||) on the intersection of actions.
   * 
   * @param sourceAutomaton1
   *          First automaton to use
   * @param sourceAutomaton2
   *          Second automaton to use
   * @return The merged automaton
   */
  public Lts compute(Lts sourceAutomaton1, Lts sourceAutomaton2)
  {
    Lts targetAutomaton = new Lts();

    // Build entry-point (a new state that represents the two source-automata in their initial state)
    undevelopedStates.add(new Tuple<State, State>(sourceAutomaton1.startState, sourceAutomaton2.startState));

    while (!undevelopedStates.isEmpty())
    {
      Tuple<State, State> root = undevelopedStates.remove();
      State rootState = composedState(root.getLeft(), root.getRight());

      // Set first state as initial state
      if (targetAutomaton.startState == null)
        targetAutomaton.startState = rootState;

      rootState.transitions.addAll(tryTransitions(root.getLeft(), root.getRight()));
    }

    return targetAutomaton;
  }


  /**
   * Develop all possible transitions for a combination of states.
   * 
   * @param state1
   *          The first state
   * @param state2
   *          The second state
   * @return A set of valid transitions for the resulting merged LTS
   */
  private Set<Transition> tryTransitions(State state1, State state2)
  {
    HashSet<Transition> targetTransitions = new HashSet<Transition>();
    HashMap<String, Transition> transitions1 = new HashMap<String, Transition>();
    HashMap<String, Transition> transitions2 = new HashMap<String, Transition>();

    // Transitions of state 1
    for (Transition transition : state1.transitions)
    {
      transitions1.put(transition.name, transition);
    }

    // Transitions of state 2
    for (Transition transition : state2.transitions)
    {
      transitions2.put(transition.name, transition);
    }

    // The union of possible actions
    Set<String> allActions = new HashSet<String>(transitions1.keySet());
    allActions.addAll(transitions2.keySet());

    for (String action : allActions)
    {
      boolean inOne = transitions1.containsKey(action);
      boolean inTwo = transitions2.containsKey(action);

      // Create and add a new Transition for the current state of the target automaton
      Transition newTransition = new Transition();
      newTransition.name = action;
      targetTransitions.add(newTransition);

      // Only LTS 1 develops
      if (inOne && !inTwo)
      {
        newTransition.followState = composedState(transitions1.get(action).followState, state2);
      }
      // Only LTS 2 develops
      else if (!inOne && inTwo)
      {
        newTransition.followState = composedState(state1, transitions2.get(action).followState);
      }
      // Both LTS's develop
      else
      {
        newTransition.followState =
          composedState(transitions1.get(action).followState, transitions2.get(action).followState);
      }
    }

    return targetTransitions;
  }


  /**
   * Creates a new state for the target automaton using the information of two single source automata. If the corresponding state has
   * already been created, the previously created state is returned instead.
   * 
   * @param state1
   *          First state to merge
   * @param state2
   *          Second state to merge
   * @return The corresponding state in the target automaton
   */
  private State composedState(State state1, State state2)
  {
    // What the new state *would* be
    State composedState = new State(state1.name + STATE_CONCAT + state2.name);

    // Search for an existing state and return it, if one
    for (State targetState : targetStates)
    {
      if (targetState.equals(composedState))
      {
        return targetState;
      }
    }

    // Add new state to keep track of existing states
    targetStates.add(composedState);

    // Also add the newly created state to the queue of undeveloped states
    undevelopedStates.add(new Tuple<State, State>(state1, state2));

    return composedState;
  }


  public Lts computeByActions(Lts sourceAutomaton1, Lts sourceAutomaton2, Set<String> allowedActions)
  {
    // The set of actions to parallelize is limited to a given set of actions.
    // Transitions cannot be distinguished just by their name anymore.

    // TODO implementation, no priority (not part of the task?)

    return null;
  }

}
