package ctrl;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants.Units;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;

import beans.Lts;
import beans.Proposition;
import beans.State;
import beans.Transition;

public class Visualizer {

	protected static String styleSheet = "node {"
			+ "  shape: circle; text-size:30px; size:70px; fill-mode: plain; fill-color: white; stroke-mode: plain; stroke-color:black;"
			+ "}" + "node.marked {" + "       fill-color: red;"
			+ "} edge {text-size: 20px;} sprite {size: 0px; text-size: 20px;}";

	public static void visualize(Lts lts) {

		Graph graph = new SingleGraph("Compose LTS");
		SpriteManager sman = new SpriteManager(graph);
		graph.setStrict(false);
		graph.addAttribute("ui.antialias");
		graph.addAttribute("ui.quality");
		for (State state : lts.getStates()) {
			Node n = graph.addNode(state.name);
			n.addAttribute("ui.label", state.name);
			Sprite s = sman.addSprite("sprite" + state.name);
			s.attachToNode(state.name);
			s.setPosition(Units.PX, 50, 0, 0);
			String propositions = "";

			for (Proposition prop : state.props) {
				propositions += prop.name + ";";
			}

			propositions = propositions.substring(0, propositions.length() - 1);

			s.addAttribute("ui.label", "{" + propositions + "}");
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
