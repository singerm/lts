package util;

import java.util.HashSet;
import java.util.Set;

import beans.Lts;
import beans.State;
import beans.Transition;

public class LtsHelper {

	public static Set<State> getPreviousStates(Lts lts, State state) {
		Set<State> prevs = new HashSet<State>();

		for (State possiblePrev : lts.getStates()) {

			for (Transition trans : possiblePrev.transitions) {
				if (trans.followState.equals(state)) {
					prevs.add(possiblePrev);
				}
			}

		}
		return prevs;

	}
}
