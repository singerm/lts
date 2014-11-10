package ctrl;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import beans.Lts;
import beans.State;
import beans.Transition;

public class Visualizer {

	protected static String styleSheet = "node {"
			+ "  size:20px;     fill-color: black;" + "}" + "node.marked {"
			+ "       fill-color: red;" + "}";

	public static void visualize(Lts lts) {

		Graph graph = new SingleGraph("Compose LTS");
		graph.setStrict(false);
		for (State state : lts.getStates()) {
			Node n = graph.addNode(state.name);
			n.addAttribute("ui.label", state.name);
			if (lts.startState.equals(state)) {
				n.setAttribute("ui.class", "marked");
			}

		}

		for (State state : lts.getStates()) {

			for (Transition trans : state.transitions) {

				Edge e = graph.addEdge(trans.name + state.name, state.name,
						trans.followState.name, true);
				e.addAttribute("ui.label", trans.name);

			}
		}
		graph.addAttribute("ui.stylesheet", styleSheet);
		graph.display();

	}
}
