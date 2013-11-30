package core;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessManagement {

	File file;

	public ProcessManagement(File file) {
		this.file = file;
	}

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
		System.out.println("Entro. Facts.size() = "+facts.size());
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file, true);
			PrintStream ps = new PrintStream(fos);
			ps.append("\n");
			for (String string : facts) {
				ps.append(string + "\n");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private List<String> readFact(String answer_set) {
		List<String> facts = new ArrayList<>();
		String matcher_facts = "[a-zA-Z]+[(][0-9]+[,][0-9]+[)]";
		String matcher_points = "[0-9]+";
		Pattern pattern_facts = Pattern.compile(matcher_facts);
		Matcher matcher_1 = pattern_facts.matcher(answer_set);

		while (matcher_1.find()) {
			System.out.println(matcher_1.group());
			Pattern pattern_points = Pattern.compile(matcher_points);
			Matcher matcher_2 = pattern_points.matcher(matcher_1.group());
			int x = 0, y = 0;
			String xy_temp = "";
			while (matcher_2.find()) {
				xy_temp += matcher_2.group()+" ";
			}
			String[] xy = xy_temp.split(" ");
			x = Integer.parseInt(xy[1]);
			y = Integer.parseInt(xy[2]);
			
			//TODO creating a string of facts and adds to List
//			String CellaSafe =
//			String celleVisitateSafe =
//			String MapStatus
		}
		return facts;
	}

	public static void main(String[] args) {
		File file = new File("wumpus_test.dlv");
		ProcessManagement pm = new ProcessManagement(file);
		System.out.println(pm.answer_set());
		pm.step(pm.answer_set());
	}

}
