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

	/**
	 * Methode um Transitions in Sets richtig einzuordnen. Eine Transition ist
	 * gleich einer anderen Transition, wenn ihr Name gleich ist!
	 */
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

}
