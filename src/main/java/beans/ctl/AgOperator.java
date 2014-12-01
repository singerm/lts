package beans.ctl;

import java.util.Set;

import beans.Lts;
import beans.State;

public class AgOperator implements CtlFormula {
	private CtlFormula phi = null;

	public AgOperator(CtlFormula phi) {
		// AG === !EF (!phi)
		this.phi = new NegationOperator(new EfOperator(
				new NegationOperator(phi)));
	}

	public void reduce(Lts automaton) {
		phi.reduce(automaton);

		Set<State> states = automaton.getCachedStates();

		for (State state : states) {
			// The equivalence of this operator was stored in phi, so a state
			// satisfies the AG-operator if it satisfies phi
			if (state.formulas.contains(phi)) {
				state.formulas.add(this);
			}
		}
	}

}
