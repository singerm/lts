package beans;

import java.util.HashSet;
import java.util.Set;

/**
 * Zustand eines LTS. Von einem Zustand aus können mehrere Transitionen
 * (Zustandsübergänge) zu anderen Zuständen ausgehen. Da es sich hierbei das
 * Äquivalent zu einem DEA handelt, kann eine Transition mit dem gleichen Namen
 * nur einmal von einem Zustand ausgehen.
 * 
 * @author Dagon
 * 
 */
public class State {

	public String name = "";

	// In diesem Fall wird ein Set genutzt, da dies sicherstellt, da jeder
	// Transition-Name nur einmal verwendet wird.
	public Set<Transition> transitions = new HashSet<Transition>();

	public State() {
		super();
	}

	public State(String name) {
		this.name = name;
	}

	/**
	 * Ein State ist gleich einem anderen State, wenn der Name gleich ist!
	 */
	@Override
	public boolean equals(Object name) {

		return this.name.equals(name);
	}

	/**
	 * Ein State ist gleich einem anderen State, wenn der Name gleich ist
	 */
	public int hashCode() {
		return this.name.hashCode();
	}

}
