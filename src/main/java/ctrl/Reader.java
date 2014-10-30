package ctrl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import beans.Lts;
import beans.State;
import beans.Transition;

/**
 * Liest die XML-Dateien ein und generiert LTS daraus
 * 
 * @author Dagon
 * 
 */
public class Reader {

	private static List<Lts> ltsList = new ArrayList<Lts>();
	private static Map<String, Integer> statePosition = new HashMap<String, Integer>();
	private static NodeList nList = null;
	private static Set<State> openStates = new HashSet<State>();
	private static Set<State> printOpenStates = new HashSet<State>();
	private static Map<String, State> nameToState = new HashMap<String, State>();
	private static Lts newLts = null;

	/**
	 * 
	 * @param folder
	 *            - der Ordner, in dem die xml-Dateien liegen
	 * @param printAfterReading
	 *            - ob jeder LTS ausgegeben werden soll
	 * @return
	 * @throws Exception
	 */
	public static List<Lts> read(String folder, boolean printAfterReading)
			throws Exception {
		String directory = System.getProperty("user.dir");

		File dir = new File(directory + "\\" + folder);

		if (dir.isDirectory()) {

			for (File file : dir.listFiles()) {

				String fileName = file.getName();

				if (fileName.endsWith(".xml")) {
					createLtsFromFile(dir + "\\" + fileName);
					if (printAfterReading) {
						printLts();

					}
				}
			}

		} else {
			System.out
					.println("Dies ist kein Ordner, bitte gültigen Ordnernamen angeben");
			System.exit(1);
		}
		return ltsList;
	}

	private static void printLts() {
		if (newLts != null) {
			System.out.println("Startzustand von LTS ist: "
					+ newLts.startState.name);
			printOpenStates.add(newLts.startState);
			System.out.println("Er hat folgende Transitions: ");
			for (Transition trans : newLts.startState.transitions) {
				System.out.println(trans.name);
				System.out.println("Der Folgezustand von " + trans.name
						+ " ist " + trans.followState.name);
				printState(trans.followState);
			}
			printOpenStates.remove(newLts.startState);
			System.out.println("");
		}

		else {
			System.out.println("Es wurde kein LTS eingelesen!");
		}
	}

	private static void printState(State printState) {

		printOpenStates.add(printState);
		System.out.println("Er hat folgende Transitions: ");
		for (Transition trans : printState.transitions) {
			System.out.println(trans.name);
			System.out.println("Der Folgezustand von " + trans.name + " ist "
					+ trans.followState.name);
			if (!printOpenStates.contains(trans.followState)) {
				printState(trans.followState);
			}
		}

		printOpenStates.remove(printState);

	}

	private static void createLtsFromFile(String fileName) throws Exception {
		nameToState.clear();

		File fXmlFile = new File(fileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		nList = doc.getElementsByTagName("state");

		newLts = new Lts();
		State startState = findStartState();
		newLts.startState = startState;
		addFollower(nameToState.get(startState.name));
		newLts.allStates = new HashSet<State>(nameToState.values());
		ltsList.add(newLts);

	}

	private static void addFollower(State s) {
		Integer positionInXml = statePosition.get(s.name);
		Node node = nList.item(positionInXml);
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element currState = (Element) node;
			NodeList transitionList = currState
					.getElementsByTagName("transition");
			openStates.add(s);
			Set<Transition> transitions = new HashSet<Transition>();
			for (int temp = 0; temp < transitionList.getLength(); temp++) {
				Node transitionNode = transitionList.item(temp);

				if (transitionNode.getNodeType() == Node.ELEMENT_NODE) {
					Transition newTransition = new Transition();
					Element currTransition = (Element) transitionNode;
					newTransition.name = currTransition
							.getElementsByTagName("name").item(0)
							.getTextContent();
					transitions.add(newTransition);
					String targetStateName = currTransition
							.getElementsByTagName("targetState").item(0)
							.getTextContent();
					newTransition.followState = nameToState
							.get(targetStateName);
					if (!openStates.contains(nameToState.get(targetStateName))) {
						addFollower(nameToState.get(targetStateName));
					}
				}
			}
			s.transitions = transitions;
			openStates.remove(s);

		}

	}

	private static State findStartState() {
		State startState = null;
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node node = nList.item(temp);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element state = (Element) node;
				String name = state.getElementsByTagName("name").item(0)
						.getTextContent();
				State newState = new State();
				newState.name = name;
				statePosition.put(name, temp);
				nameToState.put(name, newState);

				if (!state.getAttribute("start").trim().isEmpty()
						&& startState == null) {
					startState = newState;

				}

				else if (!state.getAttribute("start").trim().isEmpty()
						&& startState != null) {
					System.out
							.println("Mehrere Startzustände sind nicht zugelassen!");
					System.exit(1);
				}

				else if (state.getAttribute("start").trim().isEmpty()
						&& startState == null && temp == nList.getLength() - 1) {
					System.out.println("Kein Startzustand gefunden!");
					System.exit(1);
				}
			}
		}
		return startState;
	}
}
