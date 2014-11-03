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
  private Set<String> parallelActions = new HashSet<String>();
  private Set<String> allActions = new HashSet<String>();

  private static final String STATE_CONCAT = "#";


  /**
   * Creates a merged automaton from two given automata using the parallel-operator (||) based on the intersection of common actions.
   * 
   * @param sourceAutomaton1
   *          First automaton to use
   * @param sourceAutomaton2
   *          Second automaton to use
   * @return The merged automaton
   * @see #compute(Lts, Lts, Set)
   */
  public Lts compute(Lts sourceAutomaton1, Lts sourceAutomaton2)
  {
    return compute(sourceAutomaton1, sourceAutomaton2, buildActionIntersection(sourceAutomaton1, sourceAutomaton2));
  }


  /**
   * Creates a merged automaton from two given automata using the parallel-operator (||) based on a set of common actions.
   * 
   * @param sourceAutomaton1
   *          First automaton to use
   * @param sourceAutomaton2
   *          Second automaton to use
   * @param parallelActions
   *          A set of action names that have to be executed in parallel
   * @return The merged automaton
   */
  public Lts compute(Lts sourceAutomaton1, Lts sourceAutomaton2, Set<String> parallelActions)
  {
    Lts targetAutomaton = new Lts();

    // For security reasons, only allow common actions as parallels
    this.parallelActions = parallelActions;
    this.parallelActions.retainAll(buildActionIntersection(sourceAutomaton1, sourceAutomaton2));

    // Cache of actions
    allActions = buildActionUnion(sourceAutomaton1, sourceAutomaton2);

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

    for (String action : allActions)
    {
      boolean inOne = transitions1.containsKey(action);
      boolean inTwo = transitions2.containsKey(action);

      // Is a parallel action and both LTS's have it
      if (parallelActions.contains(action))
      {
        if (inOne && inTwo)
        {
          targetTransitions.add(new Transition(action, composedState(transitions1.get(action).followState,
              transitions2.get(action).followState)));
        }
      }

      // Is not a parallel action
      else
      {
        // LTS 1 has it
        if (inOne)
        {
          targetTransitions.add(new Transition(action, composedState(transitions1.get(action).followState, state2)));
        }

        // LTS 2 has it
        if (inTwo)
        {
          targetTransitions.add(new Transition(action, composedState(state1, transitions2.get(action).followState)));
        }
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
    if (allowedActions == null)
    {
      allowedActions = buildActionIntersection(sourceAutomaton1, sourceAutomaton2);
    }

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
   * Builds the intersection of all actions of two automata.
   * 
   * @param sourceAutomaton1
   * @param sourceAutomaton2
   * @return A set with the common actions of both automata
   */
  private Set<String> buildActionIntersection(Lts sourceAutomaton1, Lts sourceAutomaton2)
  {
    HashSet<String> transitions1 = new HashSet<String>();
    HashSet<String> transitions2 = new HashSet<String>();

    // Transitions of lts 1
    for (Transition transition : sourceAutomaton1.getAllTransitions())
    {
      transitions1.add(transition.name);
    }

    // Transitions of lts 2
    for (Transition transition : sourceAutomaton2.getAllTransitions())
    {
      transitions2.add(transition.name);
    }

    // Intersection
    transitions1.retainAll(transitions2);

    return transitions1;
  }


  /**
   * Builds the union of all actions of two automata.
   * 
   * @param sourceAutomaton1
   * @param sourceAutomaton2
   * @return A Set with all actions of both automata
   */
  private Set<String> buildActionUnion(Lts sourceAutomaton1, Lts sourceAutomaton2)
  {
    HashSet<String> transitions = new HashSet<String>();

    // Transitions of lts 1
    for (Transition transition : sourceAutomaton1.getAllTransitions())
    {
      transitions.add(transition.name);
    }

    // Transitions of lts 2
    for (Transition transition : sourceAutomaton2.getAllTransitions())
    {
      transitions.add(transition.name);
    }

    return transitions;
  }
}
