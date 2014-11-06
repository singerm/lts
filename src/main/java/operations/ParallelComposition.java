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
 * Implementation of the Parallel-Execution-Operator aka ||.
 * 
 * @author Sebastian
 *
 */
public class ParallelComposition
{
  private Queue<Tuple<State, State>> unvisitedStates = new LinkedBlockingQueue<Tuple<State, State>>();
  private HashMap<String, State> existingStates = new HashMap<String, State>();
  private Set<String> parallelActions = new HashSet<String>();

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

    // Clear previous stuff
    existingStates.clear();
    unvisitedStates.clear();

    // For security reasons, only allow common actions as parallels
    this.parallelActions = parallelActions;
    this.parallelActions.retainAll(buildActionIntersection(sourceAutomaton1, sourceAutomaton2));

    // Build entry-point (a new state that represents the two source-automata in their initial state)
    unvisitedStates.add(new Tuple<State, State>(sourceAutomaton1.startState, sourceAutomaton2.startState));

    // Visit all target-states by consuming the queue
    while (!unvisitedStates.isEmpty())
    {
      Tuple<State, State> root = unvisitedStates.remove();
      State rootState = composedState(root.getLeft(), root.getRight());

      // Set first state as initial state
      if (targetAutomaton.startState == null)
        targetAutomaton.startState = rootState;

      rootState.transitions.addAll(discoverTransitions(root.getLeft(), root.getRight()));
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
   * @return A set of valid transitions for the given state-combination in the target LTS
   */
  private Set<Transition> discoverTransitions(State state1, State state2)
  {
    // Specifying the (maximum) sizes of the sets improves performance
    int numTransitions1 = state1.transitions.size();
    int numTransitions2 = state2.transitions.size();
    int maxTransitions = numTransitions1 + numTransitions2;

    HashSet<Transition> newTransitions = new HashSet<Transition>(maxTransitions);
    HashMap<String, Transition> transitions1 = new HashMap<String, Transition>(numTransitions1);
    HashMap<String, Transition> transitions2 = new HashMap<String, Transition>(numTransitions2);
    HashSet<String> possibleActions = new HashSet<String>(maxTransitions);

    // Cache transitions of state 1
    for (Transition transition : state1.transitions)
    {
      transitions1.put(transition.name, transition);
      possibleActions.add(transition.name);
    }

    // Cache transitions of state 2
    for (Transition transition : state2.transitions)
    {
      transitions2.put(transition.name, transition);
      possibleActions.add(transition.name);
    }

    // Check all the actions that could possibly be available from this state
    for (String action : possibleActions)
    {
      boolean inOne = transitions1.containsKey(action);
      boolean inTwo = transitions2.containsKey(action);

      // Is a parallel action
      if (parallelActions.contains(action))
      {
        // Both LTS's have the action
        if (inOne && inTwo)
        {
          newTransitions.add(new Transition(action, composedState(transitions1.get(action).followState,
              transitions2.get(action).followState)));
        }
      }

      // Is not a parallel action
      else
      {
        // LTS 1 has it
        if (inOne)
        {
          newTransitions.add(new Transition(action, composedState(transitions1.get(action).followState, state2)));
        }

        // LTS 2 has it
        if (inTwo)
        {
          newTransitions.add(new Transition(action, composedState(state1, transitions2.get(action).followState)));
        }
      }
    }

    return newTransitions;
  }


  /**
   * Creates a new state for the target automaton or returns an existing state that is associated with the given state-combination.
   * 
   * @param state1
   *          First state to merge
   * @param state2
   *          Second state to merge
   * @return The corresponding state in the target automaton
   */
  private State composedState(State state1, State state2)
  {
    // The name of the new state (a state is identified by its name)
    String stateName = state1.name + STATE_CONCAT + state2.name;

    // If we already have such state just return it
    if (existingStates.containsKey(stateName))
    {
      return existingStates.get(stateName);
    }

    // Otherwise add new state to keep track of existing states
    State state = new State(stateName);
    existingStates.put(stateName, state);

    // Also add the newly created state to the queue of undeveloped states
    unvisitedStates.add(new Tuple<State, State>(state1, state2));

    return state;
  }


  @Deprecated
  public Lts computeByActions(Lts sourceAutomaton1, Lts sourceAutomaton2, Set<String> allowedActions)
  {
    if (allowedActions == null)
    {
      allowedActions = buildActionIntersection(sourceAutomaton1, sourceAutomaton2);
    }

    Lts targetAutomaton = new Lts();

    // Build entry-point (a new state that represents the two source-automata in their initial state)
    unvisitedStates.add(new Tuple<State, State>(sourceAutomaton1.startState, sourceAutomaton2.startState));

    while (!unvisitedStates.isEmpty())
    {
      Tuple<State, State> root = unvisitedStates.remove();
      State rootState = composedState(root.getLeft(), root.getRight());

      // Set first state as initial state
      if (targetAutomaton.startState == null)
        targetAutomaton.startState = rootState;

      rootState.transitions.addAll(discoverTransitions(root.getLeft(), root.getRight()));
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
