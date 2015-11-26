package utils;

import java.util.HashSet;

public class Valid {
	private Valid() {
		
	}
	
	public static String str(String toCheck,int length,String defaultStr) {
		if(toCheck == null)
			return defaultStr;
		String result = toCheck.trim();
		return (result.length() > 0 && result.length() <= length) ? result : defaultStr;
	}

	public static int integer(int toCheck, int min, int max, int defaultInt) {
		return toCheck >= min && toCheck <= max ? toCheck : defaultInt;
	}
	
	public static String autoStr(char agent, int length) {
		String result = "";
		for(int i  = 0; i < length; i ++) {
			result += agent;
		}
		return result;
	}
	
//	public static String genreEncode(String[] genre) {
//		HashSet<String> genres = new HashSet<>();
//		for(int i = 0;i < )
//	}
//	
//	public static String genreDecode(String genre) {
//		
//	}
}
