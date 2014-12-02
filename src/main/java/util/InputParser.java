package util;

import beans.Proposition;
import beans.ctl.ConjunctionOperator;
import beans.ctl.CtlFormula;
import beans.ctl.DisjunctionOperator;
import beans.ctl.EgOperator;
import beans.ctl.EuOperator;
import beans.ctl.ExOperator;
import beans.ctl.NegationOperator;
import beans.ctl.PropositionOccurrence;

public class InputParser {

	public static CtlFormula parseInput(String input) {

		return appendFormula(input);

	}

	private static CtlFormula appendFormula(String input) {
		input = input.trim();

		if (input.indexOf("(") < 0 && input.indexOf(")") < 0
				&& !input.contains(Operations.AND.getExpression())
				&& !input.contains(Operations.OR.getExpression())
				&& !input.contains(Operations.EU.getExpression())) {
			return new PropositionOccurrence(new Proposition(input));
		}

		else {
			int start = input.indexOf("(") >= 0 ? input.indexOf("(") : 0;
			int end = input.lastIndexOf(")") >= 0 ? input.lastIndexOf(")") : 0;

			String operation = input.substring(0, start).trim().toUpperCase();

			// uniäre operatoren behandeln

			if (operation.equals(Operations.EX.getExpression())) {
				CtlFormula phi = appendFormula(input.substring(start + 1, end));
				return new ExOperator(phi);
			}

			else if (operation.equals(Operations.EG.getExpression())) {
				CtlFormula phi = appendFormula(input.substring(start + 1, end));
				return new EgOperator(phi);
			}

			else if (operation.equals(Operations.NOT.getExpression())) {
				CtlFormula phi = appendFormula(input.substring(start + 1, end));
				return new NegationOperator(phi);
			}

			else {
				if ((operation.length() == 0 && input.contains(Operations.AND
						.getExpression()))
						|| (operation.length() > 0 && operation
								.contains(Operations.AND.getExpression()))) {

					String op = (operation.length() == 0 ? input : operation)
							.trim();
					int delemiter = op.indexOf(Operations.AND.getExpression());
					CtlFormula phi = appendFormula(op.substring(start,
							delemiter));

					CtlFormula psi = appendFormula(op.substring(delemiter
							+ Operations.AND.getExpression().length(),
							op.length()));

					return new ConjunctionOperator(phi, psi);

				}

				else if ((operation.length() == 0 && input
						.contains(Operations.OR.getExpression()))
						|| (operation.length() > 0 && operation
								.contains(Operations.OR.getExpression()))) {
					String op = (operation.length() == 0 ? input : operation)
							.trim();
					int delemiter = op.indexOf(Operations.OR.getExpression());
					CtlFormula phi = appendFormula(op.substring(start,
							delemiter));
					CtlFormula psi = appendFormula(op.substring(delemiter
							+ Operations.OR.getExpression().length(),
							op.length()));

					return new DisjunctionOperator(phi, psi);

				}

				else if ((operation.length() == 0 && input
						.contains(Operations.EU.getExpression()))
						|| (operation.length() > 0 && operation
								.contains(Operations.EU.getExpression()))) {
					input = replaceLast(input.replaceFirst("\\[", ""), "]", "")
							.trim();
					// TODO find the right delemiter
					int delemiter = input.indexOf("U");
					CtlFormula phi = appendFormula(input.substring(start + 1,
							delemiter - 1));
					CtlFormula psi = appendFormula(input.substring(
							delemiter + 1, input.length()));

					return new EuOperator(phi, psi);
				}

				else {
					// something somewhere went horribly wrong
					System.out.println("Etwas lief falsch");
					return null;
				}
			}

		}

	}

	// TODO find the right delemiter
	// private static int findDelimiter(String input, String delemiter) {
	// int open = 0;
	// for (int i = 0; i < input.length(); i++) {
	// String test = input.substring(0, i);
	// String rest = input.substring(i + 1, input.length());
	// int del = test.indexOf(delemiter);
	// int next = rest.indexOf(delemiter);
	// char c = input.charAt(i);
	// if (c == '(') {
	// open++;
	// }
	//
	// else if (c == ')') {
	// open--;
	// }
	//
	// }
	//
	// }

	private static String replaceLast(String input, String needle,
			String replace) {
		StringBuilder b = new StringBuilder(input);
		b.replace(input.lastIndexOf(needle),
				input.lastIndexOf(needle) + needle.length(), replace);
		return b.toString();
	}
}
