package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import models.Movie;

public class Matrix {
	
	public static HashSet<String> getAllContents(HashMap<Long,Movie> movies,HashSet<Long> ids) {
		HashSet<String> genres = new HashSet<>();
		Iterator<Long> ite = ids.iterator();
		while(ite.hasNext()) {
			Movie m = movies.get(ite.next());
			String genreCode = m.getGenreCode();
			for(int i = 0; i < genreCode.length(); i++) {
				int bit = (int) genreCode.charAt(i);
				if(bit == 1) {
					genres.add(Movie.getGenres().get(bit));
				}
			}
		}
		return genres;
	}
	
	public static double similarityInRadian(ArrayList<Long> movieIds, HashMap<Long, Integer> ratingsA,
			HashMap<Long, Integer> ratingsB) {
		//First: make them aligned
		int[] u = new int[movieIds.size()];
		int[] v = new int[movieIds.size()];
//		System.out.println("U vector: " + u.length);
//		System.out.println("V vector: " + v.length);
		
		int countU = 0;
		int countV = 0;
		for(long id: movieIds) {
			if(ratingsA.containsKey(id)) {
				u[countU++] = ratingsA.get(id);
			}
			else {
				countU++;
			}
			if(ratingsB.containsKey(id)) {
				v[countV++] = ratingsB.get(id);
			}
			else {
				countV++;
			}
		}
		
		double squaredNormU = 0;
		double squaredNormV = 0;
		int dotProduct = 0;
		int count = 0;
		while(count < u.length) {
			int uVal = u[count];
			int vVal = v[count];
			squaredNormU += (uVal * uVal);
			squaredNormV += (vVal * vVal);
			dotProduct += (uVal * vVal);
			count++;
		}
		
		double normU = Math.sqrt(squaredNormU);
		double normV = Math.sqrt(squaredNormV);
		
		double cos = dotProduct / (normU * normV);
//		double angleInRadian = Math.acos(cos);
//		return Math.abs(angleInRadian);
		return cos;
	}
}
