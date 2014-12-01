package beans.ctl;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import beans.Lts;
import beans.State;

public class EuOperator implements CtlFormula {
	public CtlFormula phi = null;
	public CtlFormula psi = null;

	public EuOperator(CtlFormula phi, CtlFormula psi) {
		this.phi = phi;
		this.psi = psi;
	}

	public void reduce(Lts automaton) {
		phi.reduce(automaton);
		psi.reduce(automaton);

		Set<State> psiStates = findPsiStates(automaton);
		Queue<State> candidates = new LinkedBlockingQueue<State>();

		for (State psiState : psiStates) {
			// All direct predecessors of psi-satisfying states are candidates
			// to match the EU-operator
			candidates.addAll(psiState.prevStates);
		}

		while (!candidates.isEmpty()) {
			State candidate = candidates.remove();

			// Candidates that have already been marked have to be skipped to
			// avoid endless loops
			if (candidate.formulas.contains(phi)
					&& !candidate.formulas.contains(this)) {
				// Candidate matches the criteria for EU-operator so mark it
				candidate.formulas.add(this);

				// Predecessors of a candidate are also candidates
				candidates.addAll(candidate.prevStates);
			}
		}
	}

	private Set<State> findPsiStates(Lts automaton) {
		Set<State> states = automaton.getCachedStates();
		HashSet<State> psiStates = new HashSet<State>();

		for (State s : states) {
			if (s.formulas.contains(psi)) {
				psiStates.add(s);
			}
		}

		return psiStates;
	}

}
