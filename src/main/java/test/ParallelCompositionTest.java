package test;

import java.util.Set;

import operations.ParallelComposition;
import beans.Lts;
import beans.State;
import beans.Transition;


public class ParallelCompositionTest
{

  public static void main(String[] args)
  {
    Lts one = createLtsOne();
    Lts two = createLtsTwo();

    showLts(one);
    System.out.println("-----");
    showLts(two);

    ParallelComposition composition = new ParallelComposition();

    Lts x = composition.compute(one, two);

    showLts(x);
  }


  public static Lts createLtsOne()
  {
    Lts lts = new Lts();

    State s1 = new State("1");
    State s2 = new State("2");

    Transition ta = new Transition();
    ta.name = "A";
    ta.followState = s2;
    Transition tb = new Transition();
    tb.name = "B";
    tb.followState = s1;

    s1.transitions.add(ta);
    s2.transitions.add(tb);

    lts.startState = s1;

    return lts;
  }


  public static Lts createLtsTwo()
  {
    Lts lts = new Lts();

    State s1 = new State("1");
    State s2 = new State("2");

    Transition ta = new Transition();
    ta.name = "A";
    ta.followState = s2;
    Transition tb = new Transition();
    tb.name = "C";
    tb.followState = s1;

    s1.transitions.add(ta);
    s2.transitions.add(tb);

    lts.startState = s1;

    return lts;
  }


  public static void showLts(Lts lts)
  {
    Set<State> states = lts.getStates();

    for (State state : states)
    {
      for (Transition transition : state.transitions)
      {
        System.out.println(state.name + " -" + transition.name + "-> " + transition.followState.name);
      }
    }
  }

}
