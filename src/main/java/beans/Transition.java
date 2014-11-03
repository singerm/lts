package beans;

/**
 * Diese Klasse stellt einen Zustandsübergang dar. Sie ist durch einen Namen und den Folgezustand gekennzeichnet. Jeder Zustand kann mehrere
 * dieser Transitions enthalten, jedoch immer nur eine pro Name.
 * 
 * @author Dagon
 * 
 */
public class Transition
{

  public String name = "";
  public State followState = null;


  public Transition()
  {
    super();
  }


  public Transition(String name, State followState)
  {
    this.name = name;
    this.followState = followState;
  }


  /**
   * Methode um Transitions in Sets richtig einzuordnen. Eine Transition ist gleich einer anderen Transition, wenn ihr Name gleich ist!
   */
  @Override
  public int hashCode()
  {
    return this.name.hashCode();
  }


  /**
   * Eine Transition ist equals einer Anderen, wenn Name und Folgezustand gleich sind!
   */
  @Override
  public boolean equals(Object o)
  {
    Transition t = (Transition) o;
    if (this.name.equals(t.name) && followState.name.equals(t.followState.name))
    {
      return true;
    }
    return false;
  }

}
