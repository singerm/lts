package beans;

/**
 * Diese Klasse stellt einen Zustandsübergang dar. Sie ist durch einen Namen und
 * den Folgezustand gekennzeichnet. Jeder Zustand kann mehrere dieser
 * Transitions enthalten, jedoch immer nur eine pro Name.
 * 
 * @author Dagon
 * 
 */
public class Transition {

	public String name = "";
	public State followState = null;

	public Transition() {
		super();
	}

	public Transition(String name, State followState) {
		this.name = name;
		this.followState = followState;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((followState == null) ? 0 : followState.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * Eine Transition ist equals einer Anderen, wenn Name und Folgezustand
	 * gleich sind!
	 */
	@Override
	public boolean equals(Object o) {
		Transition t = (Transition) o;
		if (this.name.equals(t.name)
				&& followState.name.equals(t.followState.name)) {
			return true;
		}
		return false;
	}

}
