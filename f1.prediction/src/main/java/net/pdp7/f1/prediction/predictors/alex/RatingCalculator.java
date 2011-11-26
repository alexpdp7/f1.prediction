package net.pdp7.f1.prediction.predictors.alex;

public interface RatingCalculator {

	public float calculateDriverRating(int season, int round, String driverName);
	public float calculateTeamRating(int season, int round, String teamName);

}