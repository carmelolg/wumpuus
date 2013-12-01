package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


/*
 * Classe utilizzata solo a fini di test
 * 
 * 
 */
public class TestRegex {

	public TestRegex() {
	}

	public File file = new File("test.dlv");

	private void eraseLastLine(File f) {
		FileInputStream fstream = null;
		DataInputStream in = null;
		BufferedWriter out = null;

		try {
			// apro il file
			fstream = new FileInputStream(file);

			// prendo l'inputStream
			in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			StringBuilder fileContent = new StringBuilder();

			// Leggo il file riga per riga
			while ((strLine = br.readLine()) != null) {
				System.out.println(strLine); // stampo sulla console la riga
												// corrispondente

				if (strLine.contains(",hunter).")) {
					// se la riga è uguale a quella ricercata
					fileContent.append(""
							+ System.getProperty("line.separator"));
				} else {
					// ... altrimenti la trascrivo così com'è
					fileContent.append(strLine);
					fileContent.append(System.getProperty("line.separator"));
				}
			}

			// Sovrascrivo il file con il nuovo contenuto (aggiornato)
			FileWriter fstreamWrite = new FileWriter(file);
			out = new BufferedWriter(fstreamWrite);
			out.write(fileContent.toString());

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// chiusura dell'output e dell'input
			try {
				fstream.close();
				out.flush();
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		TestRegex test = new TestRegex();
		test.eraseLastLine(test.file);
	}

}
