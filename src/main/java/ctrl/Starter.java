package ctrl;

import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;

import operations.ParallelComposition;
import beans.Lts;

public class Starter {

	public static void main(String[] args) {
		try {
			File folder = null;
			// JFileChooser-Objekt erstellen
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setDialogTitle("Bitte einen Ordner auswählen!");
			// Dialog zum Oeffnen von Dateien anzeigen

			if (chooser.showDialog(null, "Ordner wählen") == JFileChooser.APPROVE_OPTION) {
				folder = chooser.getSelectedFile();
			}

			if (folder != null) {
				List<Lts> allLts = Reader.read(folder, false);

				ParallelComposition composition = new ParallelComposition();

				Lts x = composition.compute(allLts);
				Visualizer.visualize(x);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
