package beans;

import java.util.HashSet;
import java.util.Set;

/**
 * Klasse, die einen LTS-Automaten repr�sentiert. �ber den Startzustand sind
 * alle Folgezust�nde und Transitions definiert. Ein Alphabet muss nicht
 * gespeichert werdn, da kein Modelchecking durchgef�hrt wird.
 * 
 * @author Dagon
 * 
 */
public class Lts {

	public State startState = null;
	private Set<State> allStates = new HashSet<State>();

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

}
