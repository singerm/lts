package operations;

import util.LtsHelper;
import beans.Lts;
import beans.ctl.CtlFormula;


public class CtlChecker
{
  public static boolean satisfies(Lts automaton, CtlFormula formula)
  {
    // Update the backlink-cache of the automaton
    LtsHelper.setPreviouseStates(automaton);

    // Evaluation of the automaton against the formula
    formula.reduce(automaton);

    // An automaton satisfies a formula if the root-formula is present within the initial state of the automaton after evaluating the
    // automaton
    return automaton.startState.formulas.contains(formula);
  }
}
