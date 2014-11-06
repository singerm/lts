package ctrl;

import java.io.PrintWriter;

import beans.Lts;
import beans.State;
import beans.Transition;

public class Visualizer {

	public static void visualize(Lts lts) {
		try {
			PrintWriter writer = new PrintWriter("out.gv", "UTF-8");
			writer.println("digraph G {");

			for (State state : lts.getStates()) {
				String graphName = state.name.replace("#", "");
				if (lts.startState.equals(state)) {
					writer.println(graphName + " [label=\"" + state.name
							+ "\" style=\"filled\" fillcolor=\"red\"]");
				}

				else {
					writer.println(graphName + " [label=\"" + state.name
							+ "\"]");
				}

				for (Transition transition : state.transitions) {
					String followName = transition.followState.name.replace(
							"#", "");
					writer.println(graphName + " ->" + followName + "[label=\""
							+ transition.name + "\"]");
				}
			}
			writer.println("}");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
