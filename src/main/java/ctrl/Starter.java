package ctrl;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;

import operations.ParallelComposition;
import beans.Lts;
import beans.Transition;

public class Starter {

	public static void main(String[] args) {
		try {
			File folder = null;
			// JFileChooser-Objekt erstellen
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			// Dialog zum Oeffnen von Dateien anzeigen

			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				folder = chooser.getSelectedFile();
			}

			List<Lts> allLts = Reader.read(folder, false);

			ParallelComposition composition = new ParallelComposition();

			Lts x = composition.compute(allLts, getParallelActions(allLts));
			Visualizer.visualize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static Set<String> getParallelActions(List<Lts> allLts) {
		Set<String> parallelActions = new HashSet<String>();
		Set<String> allActions = new HashSet<String>();
		for (Lts lts : allLts) {
			Set<String> worked = new HashSet<String>();
			for (Transition trans : lts.getAllTransitions()) {

				if (allActions.contains(trans.name)
						&& !worked.contains(trans.name)) {
					parallelActions.add(trans.name);
				}

				allActions.add(trans.name);
				worked.add(trans.name);

			}

		}

		return parallelActions;

	}
}
