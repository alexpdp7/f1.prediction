package net.pdp7.f1.prediction.predictors.alex.genetic;

import java.util.List;

import net.pdp7.f1.prediction.predictors.PredictorPastEvaluator;
import net.pdp7.f1.prediction.predictors.alex.AlexPredictor;
import net.pdp7.f1.prediction.predictors.alex.RatingCalculator;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

public class AlexPredictorParamsFitnessEvaluator implements FitnessEvaluator<double[]> {

	protected final PredictorPastEvaluator predictorPastEvaluator;
	protected final RatingCalculator ratingCalculator;
	protected final SimpleJdbcTemplate jdbcTemplate;
	protected final int fromSeason;
	protected final int toSeason;

	public AlexPredictorParamsFitnessEvaluator(PredictorPastEvaluator predictorPastEvaluator, RatingCalculator ratingCalculator, SimpleJdbcTemplate jdbcTemplate, int fromSeason, int toSeason) {
		this.predictorPastEvaluator = predictorPastEvaluator;
		this.ratingCalculator = ratingCalculator;
		this.jdbcTemplate = jdbcTemplate;
		this.fromSeason = fromSeason;
		this.toSeason = toSeason;
		
	}
	
	public double getFitness(double[] candidate, List<? extends double[]> population) {
		return predictorPastEvaluator.evaluate(new AlexPredictor(ratingCalculator, jdbcTemplate, AlexPredictorParamsUtils.fromArray(candidate)), fromSeason, toSeason);
	}

	public boolean isNatural() {
		return true;
	}

}
