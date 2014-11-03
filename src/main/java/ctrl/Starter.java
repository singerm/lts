package ctrl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import beans.Lts;

public class Starter {

	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			System.out
					.print("Bite Verzeichnis angeben, aus dem gelesen werden soll!");
			String folder = br.readLine().trim();

			br.close();

			List<Lts> allLts = Reader.read(folder, true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
