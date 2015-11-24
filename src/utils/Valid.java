package utils;

public class Valid {

	public static String str(String toCheck, String defaultStr) {
		if(toCheck == null)
			return defaultStr;
		String result = toCheck.trim();
		return (result.length() > 0 && result.length() <= 200) ? result : defaultStr;
	}

	public static int integer(int toCheck, int min, int max, int defaultInt) {
		return toCheck >= min && toCheck <= max ? toCheck : defaultInt;
	}
}
