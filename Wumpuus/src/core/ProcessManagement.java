package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class ProcessManagement {

	File file;
	public ProcessManagement(File file) {
		this.file = file;
	}
	private String answer_set() {
		String answer_set = "";

		try {
			Process p = Runtime.getRuntime().exec("dlv.mingw.exe " + file.getName() + " -filter=CellaDerivedSafe");
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String tmp = "";
			while ((tmp = input.readLine()) != null) {
				answer_set += tmp;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return answer_set;
	}

	public static void main(String[] args) {
		File file = new File("wumpus_v2.dlv");
		ProcessManagement pm = new ProcessManagement(file);
		System.out.println(pm.answer_set());
	}

}
