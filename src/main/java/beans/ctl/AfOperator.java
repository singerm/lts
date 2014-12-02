package beans.ctl;

import java.util.Set;

import beans.Lts;
import beans.State;

public class AfOperator implements CtlFormula {
	private CtlFormula phi = null;

	public AfOperator(CtlFormula phi) {
		// AF === !EG (!phi)
		this.phi = new NegationOperator(new EgOperator(
				new NegationOperator(phi)));
	}

	public void reduce(Lts automaton) {
		phi.reduce(automaton);

		Set<State> states = automaton.getCachedStates();

		for (State state : states) {
			// The equivalence of this operator was stored in phi, so a state
			// satisfies the AF-operator if it satisfies phi
			if (state.formulas.contains(phi)) {
				state.formulas.add(this);
			}
		}
	}

}
