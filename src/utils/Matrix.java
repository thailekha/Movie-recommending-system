package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import models.Movie;
import models.Rating;

public class Matrix {
	
	/**
	 * calculate similarity between 2 users
	 * @param movieIds
	 * @param ratings that user A made
	 * @param ratings that user B made
	 * @return similarity value
	 */
	public static double similarityInRadian(ArrayList<Long> movieIds, HashMap<Long, Rating> ratingsA,
			HashMap<Long, Rating> ratingsB) {
		//First: make them aligned
		int[] u = new int[movieIds.size()];
		int[] v = new int[movieIds.size()];
//		System.out.println("U vector: " + u.length);
//		System.out.println("V vector: " + v.length);
		
		int countU = 0;
		int countV = 0;
		for(long id: movieIds) {
			if(ratingsA.containsKey(id)) {
				u[countU++] = ratingsA.get(id).getRating();
			}
			else {
				//automatically 0
				countU++;
			}
			if(ratingsB.containsKey(id)) {
				v[countV++] = ratingsB.get(id).getRating();
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
		double angleInRadian = Math.acos(cos);
		return Math.abs(angleInRadian);
		//return cos;
	}
}
