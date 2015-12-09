package utils;

import java.util.HashMap;
import java.util.Iterator;
import models.Rating;

public class Matrix {

	/**
	 * calculate similarity between 2 users
	 * 
	 * @param ratings
	 *            that user A made
	 * @param ratings
	 *            that user B made
	 * @return similarity value
	 */
	public static double similarityInRadian(HashMap<Long, Rating> ratingsA, HashMap<Long, Rating> ratingsB) {

		int[] u = new int[ratingsA.size()];
		int[] v = new int[ratingsA.size()];

		// System.out.println("U vector: " + u.length);
		// System.out.println("V vector: " + v.length);

		boolean vIsAllNeutral = true; // to avoid divide by 0 later on, since
										// ratingsB is trimmed according to
										// ratings A, this has to be done
		int countU = 0;
		int countV = 0;
		// First: make them aligned using ids, then put rating points into the arrays
		Iterator<Long> iteA = ratingsA.keySet().iterator();
		while (iteA.hasNext()) {
			long id = iteA.next();
			int ratingPointA = ratingsA.get(id).getRating();
			u[countU++] = ratingPointA;

			if (ratingsB.containsKey(id)) {
				int ratingPointB = ratingsB.get(id).getRating();
				if (ratingPointB != 0) {
					vIsAllNeutral = false;
				}
				v[countV++] = ratingPointB;
			} else {
				countV++; //automatically 0
			}
		}

		if (vIsAllNeutral)
			return -999;

		double squaredNormU = 0;
		double squaredNormV = 0;
		int dotProduct = 0;
		int count = 0;
		while (count < u.length) {
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
		// return cos;
	}
}
