package core;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum Status {
	BREEZE, STENCH
}

public class ProcessManagement {

	File file;
	Point nextPointOfHunter;
	char[][] map;

	public ProcessManagement(File file, char[][] map) {
		this.file = file;
		this.map = map;
	}

	/*
	 * FIXME private Map<Point, Status> readMapStatus() { Possibile estensione
	 * utilizzare map.txt per configurare la mappa temporaneamente uso una
	 * matrice passata da input
	 */

	/*
	 * private Map<Point, Status> readMapStatus() { Map<Point, Status> map = new
	 * HashMap<Point, Status>(); return map; }
	 */

	private String answer_set() {
		String answer_set = "";

		try {
			Process p = Runtime.getRuntime().exec(
					"dlv.mingw.exe " + file.getName()
							+ " -filter=CellaDerivedSafe");
			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String tmp = "";
			while ((tmp = input.readLine()) != null) {
				answer_set += tmp;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return answer_set;
	}

	private void step(String answer_set) {
		List<String> facts = readFact(answer_set);
		nextPointOfHunter = chooseNextTilesToGo(facts);
		FileOutputStream fos;
		PrintStream ps = null;
		try {
			fos = new FileOutputStream(file, true);
			ps = new PrintStream(fos);
			eraseLastLine(file);
			ps.append("\n");
			for (String string : facts) {
				ps.append(string + "\n");
			}
			ps.append("MapStatus(" + nextPointOfHunter.x + ","
					+ nextPointOfHunter.y + ",hunter).");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (ps != null)
				ps.close();
		}

	}

	private List<String> readFact(String answer_set) {
		List<String> facts = new ArrayList<>();
		String matcher_facts = "[a-zA-Z]+[(][0-9]+[,][0-9]+[)]";
		String matcher_points = "[0-9]+";
		Pattern pattern_facts = Pattern.compile(matcher_facts);
		Matcher matcher_1 = pattern_facts.matcher(answer_set);

		while (matcher_1.find()) {
			Pattern pattern_points = Pattern.compile(matcher_points);
			Matcher matcher_2 = pattern_points.matcher(matcher_1.group());
			int x = 0, y = 0;
			String xy_temp = "";
			while (matcher_2.find()) {
				xy_temp += matcher_2.group() + " ";
			}
			String[] xy = xy_temp.split(" ");
			x = Integer.parseInt(xy[0]);
			y = Integer.parseInt(xy[1]);

			String CellaSafe = "CellaSafe(" + x + "," + y + ").";
			String celleVisitateSafe = "celleVisitateSafe(" + x + "," + y
					+ ").";
			String MapStatus = "";
			if (map[x - 1][y - 1] == 'B') {
				MapStatus = "MapStatus(" + x + "," + y + ",breeze).";
			} else if (map[x - 1][y - 1] == 'S') {
				MapStatus = "MapStatus(" + x + "," + y + ",stench).";
			} else {
				MapStatus = "MapStatus(" + x + "," + y + ",safe).";
			}
			facts.add(CellaSafe);
			facts.add(celleVisitateSafe);
			facts.add(MapStatus);
		}
		return facts;
	}

	/*
	 * FIXME "Solo se veramente necessario" In realtà questa funziona sceglie
	 * random la prossima posizione dell'hunter. Ma se l'hunter torna indietro
	 * potrebbe riscegliere la stessa posizione Si potrebbe fixare in futuro in
	 * movimento più intelligente come ad esempio aggiungere una struttura dati
	 * sulle celle già visitate.
	 */
	private Point chooseNextTilesToGo(List<String> facts) {
		Point xy = new Point();
		Random rand = new Random();
		int rand_int = rand.nextInt(facts.size());
		String matcher_points = "[0-9]+";
		Pattern pattern_points = Pattern.compile(matcher_points);
		Matcher matcher = pattern_points.matcher(facts.get(rand_int));
		String xy_temp = "";
		while (matcher.find()) {
			xy_temp += matcher.group() + " ";
		}
		String[] stringToSplit = xy_temp.split(" ");
		xy.setLocation(Integer.parseInt(stringToSplit[0]),
				Integer.parseInt(stringToSplit[1]));
		return xy;
	}

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
		File file = new File("test.dlv");
		char[][] map = { { '0', 'B', '0', '0', '0', '0', '0', '0' },
				{ 'S', '0', '0', '0', '0', '0', '0', '0' },
				{ '0', '0', 'B', '0', '0', '0', '0', '0' },
				{ '0', '0', '0', '0', '0', '0', '0', '0' },
				{ '0', '0', '0', '0', '0', '0', '0', '0' },
				{ '0', '0', '0', '0', '0', '0', '0', '0' },
				{ '0', '0', '0', '0', '0', '0', '0', '0' },
				{ '0', '0', '0', '0', '0', '0', '0', '0' } };
		ProcessManagement pm = new ProcessManagement(file, map);
		System.out.println(pm.answer_set());
		pm.step(pm.answer_set());
	}

}
