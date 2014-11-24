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
	public Set<Proposition> props = new HashSet<Proposition>();

	// In diesem Fall wird ein Set genutzt, da dies sicherstellt, da jeder
	// Transition-Name nur einmal verwendet wird.
	public Set<Transition> transitions = new HashSet<Transition>();

	public State() {
		super();
	}

	public State(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

}
