package beans;

import java.util.HashSet;
import java.util.Set;


/**
 * Klasse, die einen LTS-Automaten repräsentiert. Über den Startzustand sind alle Folgezustände und Transitions definiert. Ein Alphabet muss
 * nicht gespeichert werdn, da kein Modelchecking durchgeführt wird.
 * 
 * @author Dagon
 * 
 */
public class Lts
{

  public State startState = null;


  public Set<State> getStates()
  {
    // TODO implementation
    return new HashSet<State>();
  }


  public void setStates(Set<State> states)
  {
    // TODO implementation
  }
}
