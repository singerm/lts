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
	public Set<State> allStates = new HashSet<State>();

}
