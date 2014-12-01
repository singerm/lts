package beans.ctl;

import java.util.Set;

import beans.Lts;
import beans.State;

public class NegationOperator implements CtlFormula {
	public CtlFormula phi = null;

	public NegationOperator(CtlFormula phi) {
		this.phi = phi;
	}

	public void reduce(Lts automaton) {
		phi.reduce(automaton);

		Set<State> states = automaton.getCachedStates();

		for (State s : states) {
			if (!s.formulas.contains(phi)) {
				s.formulas.add(this);
			}
		}
	}
}
