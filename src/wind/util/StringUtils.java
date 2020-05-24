package wind.util;

/**
*
* @author Fuzen Seth
*/
public class StringUtils {

	public static String toProperCase(String s) {
		String temp = s.trim();
		String spaces = "";
		if (temp.length() != s.length()) {
			int startCharIndex = s.charAt(temp.indexOf(0));
			spaces = s.substring(0, startCharIndex);
		}
		temp = temp.substring(0, 1).toUpperCase() + spaces
				+ temp.substring(1).toLowerCase() + " ";
		return temp;
	}
}
