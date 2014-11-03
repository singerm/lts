package ctrl;

import beans.Lts;
import beans.State;
import beans.Transition;

public class Visualizer {

	public static void visualize(Lts lts) {

		System.out.println("digraph G {");

		for (State state : lts.getStates()) {
			String graphName = state.name.replace("#", "");
			if (lts.startState.equals(state)) {
				System.out.println(graphName + " [label=\"" + state.name
						+ "\" style=\"filled\" fillcolor=\"red\"]");
			}

			else {
				System.out.println(graphName + " [label=\"" + state.name
						+ "\"]");
			}

			for (Transition transition : state.transitions) {
				String followName = transition.followState.name
						.replace("#", "");
				System.out.println(graphName + " ->" + followName + "[label=\""
						+ transition.name + "\"]");
			}
		}
		System.out.println("}");

	}
}
