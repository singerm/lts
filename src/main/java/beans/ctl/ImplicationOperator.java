package beans.ctl;

import java.util.Set;

import beans.Lts;
import beans.State;

public class ImplicationOperator implements CtlFormula {
	private CtlFormula nested;

	public ImplicationOperator(CtlFormula phi, CtlFormula psi) {
		// phi -> psi === (!phi or psi)
		nested = new DisjunctionOperator(new NegationOperator(phi), psi);
	}

	public void reduce(Lts automaton) {
		nested.reduce(automaton);

		Set<State> states = automaton.getCachedStates();

		for (State state : states) {
			// The equivalence of this operator was stored in `nested`, so a
			// state satisfies the AG-operator if it satisfies `nested`
			if (state.formulas.contains(nested)) {
				state.formulas.add(this);
			}
		}
	}

}
