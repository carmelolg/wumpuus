package core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {

	public static void main(String[] args) {
		String regex = "[a-z]+[(][0-9]+[,][0-9]+[)]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher("carmelo(1,1)");

		while (matcher.find()) {
			System.out.println(matcher.group());
		}
	}

}
