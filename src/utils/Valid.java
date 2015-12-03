package utils;

import java.util.HashSet;

public class Valid {
	private Valid() {
		
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
