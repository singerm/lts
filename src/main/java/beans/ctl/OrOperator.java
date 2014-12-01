package beans.ctl;

import beans.Lts;

public class OrOperator implements CtlFormula {
	public CtlFormula phi = null;
	public CtlFormula psi = null;

	public OrOperator(CtlFormula phi, CtlFormula psi) {
		this.phi = phi;
		this.psi = psi;
	}

	public void reduce(Lts automaton) {
		// TODO Auto-generated method stub

	}
}
