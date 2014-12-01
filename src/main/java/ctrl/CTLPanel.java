package ctrl;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.InputParser;
import beans.Lts;
import beans.ctl.CtlFormula;

public class CTLPanel extends JPanel implements ActionListener {
	private JTextField textField = null;
	private Lts lts = null;

	public CTLPanel(Lts lts) {
		super(new GridBagLayout());
		this.lts = lts;
		textField = new JTextField(20);

		// Add Components to this panel.
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;

		c.fill = GridBagConstraints.HORIZONTAL;
		add(textField, c);
		JButton button = new JButton("Check CTL");
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		button.addActionListener(this);
		add(button);

	}

	public void actionPerformed(ActionEvent e) {

		CtlFormula root = InputParser.parseInput(textField.getText());

		root.reduce(lts);
		if (!lts.startState.formulas.isEmpty()) {
			JOptionPane.showMessageDialog(this, "CTL is valid");
		}

		else {
			JOptionPane.showMessageDialog(this, "CTL is invalid", "Invalid",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
