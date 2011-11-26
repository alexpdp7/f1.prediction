package net.pdp7.f1.prediction.predictors.alex;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachingRatingCalculator implements RatingCalculator {

	protected final RatingCalculator ratingCalculator;

	public CachingRatingCalculator(RatingCalculator ratingCalculator) {
		this.ratingCalculator = ratingCalculator;
	}
	
	protected Map<List<Object>, Float> driverRating = new HashMap<List<Object>, Float>();
	
	public float calculateDriverRating(int season, int round, String driverName) {
		List<Object> key = Arrays.<Object>asList(season, round, driverName);
		if(!driverRating.containsKey(key)) {
			driverRating.put(key, ratingCalculator.calculateDriverRating(season, round, driverName));
		}
		return driverRating.get(key);
	}

	protected Map<List<Object>, Float> teamRating = new HashMap<List<Object>, Float>();
	
	public float calculateTeamRating(int season, int round, String teamName) {
		List<Object> key = Arrays.<Object>asList(season, round, teamName);
		if(!teamRating.containsKey(key)) {
			teamRating.put(key, ratingCalculator.calculateTeamRating(season, round, teamName));
		}
		return teamRating.get(key);
	}

}
