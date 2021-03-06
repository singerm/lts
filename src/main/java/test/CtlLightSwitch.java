package test;

import operations.CtlChecker;
import beans.Lts;
import beans.Proposition;
import beans.State;
import beans.Transition;
import beans.ctl.AfOperator;
import beans.ctl.AgOperator;
import beans.ctl.CtlFormula;
import beans.ctl.EfOperator;
import beans.ctl.EgOperator;
import beans.ctl.ImplicationOperator;
import beans.ctl.NegationOperator;
import beans.ctl.PropositionOccurrence;


public class CtlLightSwitch
{
  private static Proposition lightOn = new Proposition("lightOn");
  private static Proposition highBattUse = new Proposition("highBattUse");


  public static void main(String[] args)
  {
    Lts automaton = createLightSwitch();

    // Lts automaton = createSimple();
    // CtlFormula ctl = createEgLightOn();

    // Working
    // CtlFormula ctl = createEfLightOn();
    // CtlFormula ctl = createAgEfNotLightOn();
    // CtlFormula ctl = createAgHighBattFollowsEfNotHighBatt();

    // Not working
    // CtlFormula ctl = createEgNotHighBattUse();
    CtlFormula ctl = createAgAfNotLightOn();

    boolean satisfaction = CtlChecker.satisfies(automaton, ctl);

    if (satisfaction)
      System.out.println("Yes");
    else
      System.out.println("No");
  }


  public static Lts createSimple()
  {
    Lts automaton = new Lts();

    State a = new State("a");
    State b = new State("b");
    a.props.add(lightOn);
    b.props.add(lightOn);

    Transition x = new Transition();
    a.transitions.add(x);
    x.followState = b;

    Transition y = new Transition();
    b.transitions.add(y);
    y.followState = a;

    automaton.startState = a;

    return automaton;
  }


  public static Lts createLightSwitch()
  {
    Lts automaton = new Lts();

    State reloff = new State("rel, off");
    State proff = new State("pr, off");
    State prlow = new State("pr, low");
    prlow.props.add(lightOn);
    State rellow = new State("rel, low");
    rellow.props.add(lightOn);
    State prhigh = new State("pr, high");
    prhigh.props.add(lightOn);
    prhigh.props.add(highBattUse);
    State relhigh = new State("rel, high");
    relhigh.props.add(lightOn);
    relhigh.props.add(highBattUse);

    Transition pressOffLow = new Transition();
    pressOffLow.followState = prlow;
    reloff.transitions.add(pressOffLow);

    Transition pressLowOff = new Transition();
    pressLowOff.followState = proff;
    rellow.transitions.add(pressLowOff);

    Transition pressHighOff = new Transition();
    pressHighOff.followState = proff;
    relhigh.transitions.add(pressHighOff);

    Transition releaseLowLow = new Transition();
    releaseLowLow.followState = rellow;
    prlow.transitions.add(releaseLowLow);

    Transition releaseOffOff = new Transition();
    releaseOffOff.followState = reloff;
    proff.transitions.add(releaseOffOff);

    Transition releaseHighHigh = new Transition();
    releaseHighHigh.followState = relhigh;
    prhigh.transitions.add(releaseHighHigh);

    Transition hold = new Transition();
    hold.followState = prhigh;
    prlow.transitions.add(hold);

    Transition hold2 = new Transition();
    hold2.followState = prlow;
    prhigh.transitions.add(hold2);

    automaton.startState = reloff;

    return automaton;
  }


  public static CtlFormula createEfLightOn()
  {
    PropositionOccurrence lightOnOcc = new PropositionOccurrence(lightOn);

    return new EfOperator(lightOnOcc);
  }


  public static CtlFormula createEgNotHighBattUse()
  {
    // FIXME EG-operator
    PropositionOccurrence battUseOcc = new PropositionOccurrence(highBattUse);

    return new EgOperator(new NegationOperator(battUseOcc));
  }


  public static CtlFormula createAgEfNotLightOn()
  {
    PropositionOccurrence lightOnOcc = new PropositionOccurrence(lightOn);

    CtlFormula efNotLightOn = new EfOperator(new NegationOperator(lightOnOcc));

    return new AgOperator(efNotLightOn);
  }


  public static CtlFormula createAgAfNotLightOn()
  {
    // FIXME EG-operator???
    PropositionOccurrence lightOnOcc = new PropositionOccurrence(lightOn);

    return new AgOperator(new AfOperator(new NegationOperator(lightOnOcc)));
  }


  public static CtlFormula createAgHighBattFollowsEfNotHighBatt()
  {
    PropositionOccurrence battUseOcc = new PropositionOccurrence(highBattUse);

    CtlFormula agBatt = new AgOperator(battUseOcc);
    CtlFormula efNotBatt = new EfOperator(new NegationOperator(battUseOcc));

    return new ImplicationOperator(agBatt, efNotBatt);
  }


  public static CtlFormula createEgLightOn()
  {
    PropositionOccurrence lightOnOcc = new PropositionOccurrence(lightOn);

    return new EgOperator(lightOnOcc);
  }
}
