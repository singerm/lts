package util;

public enum Operations {

	EG("EG"), EU("E"), OR("OR"), NOT("NOT"), AND("AND"), EX("EX");

	private String expression = "";

	private Operations(String expression) {
		this.expression = expression;
	}
}
