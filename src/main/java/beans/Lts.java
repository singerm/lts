package beans;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Klasse, die einen LTS-Automaten repräsentiert. Über den Startzustand sind
 * alle Folgezustände und Transitions definiert. Ein Alphabet muss nicht
 * gespeichert werdn, da kein Modelchecking durchgeführt wird.
 * 
 * @author Dagon
 * 
 */
public class Lts {

	public State startState = null;
	private Set<State> allStates = new HashSet<State>();
	private List<Transition> allTransitions = new ArrayList<Transition>();

	public Set<State> getStates() {
		addFollowStates(startState);
		return allStates;

	}

	private void addFollowStates(State s) {
		allStates.add(s);

		for (Transition trans : s.transitions) {
			if (!allStates.contains(trans.followState)) {
				addFollowStates(trans.followState);
			}
		}
	}

	public List<Transition> getAllTransitions() {

		addTransitionsOfFollowState(startState);

		return allTransitions;

	}

	private void addTransitionsOfFollowState(State s) {

		for (Transition trans : s.transitions) {

			if (!allTransitions.contains(trans)) {
				allTransitions.add(trans);
				addTransitionsOfFollowState(trans.followState);
			}

		}

	}

}
