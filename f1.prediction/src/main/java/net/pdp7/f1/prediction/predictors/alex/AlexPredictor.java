package net.pdp7.f1.prediction.predictors.alex;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import net.pdp7.commons.util.MapUtils;
import net.pdp7.f1.prediction.predictors.Predictor;

public class AlexPredictor implements Predictor {

	protected final RatingCalculator ratingCalculator;
	protected final SimpleJdbcTemplate jdbcTemplate;
	protected final AlexPredictorParams alexPredictorParams;
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public AlexPredictor(RatingCalculator ratingCalculator, SimpleJdbcTemplate jdbcTemplate, AlexPredictorParams alexPredictorParams) {
		this.ratingCalculator = ratingCalculator;
		this.jdbcTemplate = jdbcTemplate;
		this.alexPredictorParams = alexPredictorParams;
	}
	
	public Prediction predict(int season, int round, String circuitName, Entrant[] entrants) {
		
		List<EntrantPower> entrantPowers = new ArrayList<EntrantPower>();
		
		for(Entrant entrant : entrants) {
			entrantPowers.add(new EntrantPower(entrant, calculatePowerRating(entrant, season, round, circuitName)));
		}
		
		Collections.sort(entrantPowers, EntrantPower.POWER_COMPARATOR);
		
		String[] topTen = new String[10];
		
		for(int i=0; i<10; i++) {
			topTen[i] = entrantPowers.get(i).entrant.driverName;
		}

		Prediction prediction = new Prediction(entrantPowers.get(0).entrant.driverName, entrantPowers.get(0).entrant.driverName, topTen);
		logger.debug("prediction for {}-{} {}: {}", new Object[] { season, round, circuitName, prediction });
		
		return prediction;
	}

	protected float calculatePowerRating(Entrant entrant, int season, int round, String circuitName) {
		return new PonderatingAccumulator()
			.accumulate(calculateDriverPowerRating(entrant, season, round, circuitName), alexPredictorParams.driverPowerWeight)
			.accumulate(calculateDriverCircuitPowerRating(entrant, season, round, circuitName), alexPredictorParams.driverCircuitPowerWeight)
			.accumulate(calculateTeamPowerRating(entrant, season, round, circuitName), alexPredictorParams.teamPowerWeight)
			.accumulate(calculateTeamCircuitPowerRating(entrant, season, round, circuitName), alexPredictorParams.teamCircuitPowerWeight)
			.value();
	}

	/** @return -1 if there's no prior data */
	protected float calculateDriverPowerRating(Entrant entrant, int season, int round, String circuitName) {
		List<Map<String, Object>> previousSeasonRounds = jdbcTemplate.queryForList(
				"select   season, round " +
				"from     grand_prix_driver_results " +
				"where    driver_name = :driverName " +
				"and      (season < :season " +
				"          or season = :season and round < :round) " +
				"and      finish_position is not null " +
				"order by season desc, round desc", 
				MapUtils
					.<String,Object>build("driverName", entrant.driverName)
					.put("season", season)
					.put("round", round)
					.map);
		
		return calculatePowerRatingOnRounds(entrant, previousSeasonRounds, alexPredictorParams.driverPowerRatingDecayRate, RatingType.DRIVER);
	}

	/** @return -1 if there's no prior data */
	protected float calculateDriverCircuitPowerRating(Entrant entrant, int season, int round, String circuitName) {
		List<Map<String, Object>> previousSeasonRounds = jdbcTemplate.queryForList(
				"select   grand_prix_driver_results.season, grand_prix_driver_results.round " +
				"from     grand_prix_driver_results " +
				"join     calendar on  grand_prix_driver_results.season = calendar.season " +
				"                  and grand_prix_driver_results.round = calendar.round " +
				"where    driver_name = :driverName " +
				"and      (grand_prix_driver_results.season < :season " +
				"          or grand_prix_driver_results.season = :season and grand_prix_driver_results.round < :round) " +
				"and      circuit_name = :circuitName " +
				"and      finish_position is not null " +
				"order by grand_prix_driver_results.season desc, grand_prix_driver_results.round desc", 
				MapUtils
					.<String,Object>build("driverName", entrant.driverName)
					.put("season", season)
					.put("round", round)
					.put("circuitName", circuitName)
					.map);
		
		return calculatePowerRatingOnRounds(entrant, previousSeasonRounds, alexPredictorParams.driverCircuitPowerRatingDecayRate, RatingType.DRIVER);
	}

	/** @return -1 if there's no prior data */
	protected float calculateTeamPowerRating(Entrant entrant, int season, int round, String circuitName) {
		List<Map<String, Object>> previousSeasonRounds = jdbcTemplate.queryForList(
				"select   grand_prix_driver_results.season, round " +
				"from     grand_prix_driver_results " +
				"join     season_team_drivers on grand_prix_driver_results.season = season_team_drivers.season " +
				"where    team_name = :teamName " +
				"and      (grand_prix_driver_results.season < :season " +
				"          or grand_prix_driver_results.season = :season and round < :round) " +
				"and      finish_position is not null " +
				"order by grand_prix_driver_results.season desc, round desc", 
				MapUtils
					.<String,Object>build("teamName", entrant.teamName)
					.put("season", season)
					.put("round", round)
					.map);
		
		return calculatePowerRatingOnRounds(entrant, previousSeasonRounds, alexPredictorParams.teamPowerRatingDecayRate, RatingType.TEAM);
	}

	/** @return -1 if there's no prior data */
	protected float calculateTeamCircuitPowerRating(Entrant entrant, int season, int round, String circuitName) {
		List<Map<String, Object>> previousSeasonRounds = jdbcTemplate.queryForList(
				"select   grand_prix_driver_results.season, grand_prix_driver_results.round " +
				"from     grand_prix_driver_results " +
				"join     calendar on  grand_prix_driver_results.season = calendar.season " +
				"                  and grand_prix_driver_results.round = calendar.round " +
				"join     season_team_drivers on grand_prix_driver_results.season = season_team_drivers.season " +
				"where    team_name = :teamName " +
				"and      (grand_prix_driver_results.season < :season " +
				"          or grand_prix_driver_results.season = :season and grand_prix_driver_results.round < :round) " +
				"and      circuit_name = :circuitName " +
				"and      finish_position is not null " +
				"order by grand_prix_driver_results.season desc, grand_prix_driver_results.round desc", 
				MapUtils
					.<String,Object>build("teamName", entrant.teamName)
					.put("season", season)
					.put("round", round)
					.put("circuitName", circuitName)
					.map);
		
		return calculatePowerRatingOnRounds(entrant, previousSeasonRounds, alexPredictorParams.teamCircuitPowerRatingDecayRate, RatingType.TEAM);
	}
	
	/** @return -1 if previousSeasonRounds is empty */
	protected float calculatePowerRatingOnRounds(Entrant entrant, List<Map<String, Object>> previousSeasonRounds, float decayRate, RatingType ratingType) {
		
		if(previousSeasonRounds.isEmpty()) {
			return -1;
		}
		
		float ratings = 0.0f;
		float upperRatings = 0.0f;
		int distance = 1;
		
		int priorSeason = -1, priorRound = -1;
		
		for(Map<String, Object> round : previousSeasonRounds) {
			int previousSeason = ((BigDecimal) round.get("SEASON")).intValue();
			int previousRound = (Integer) round.get("ROUND");
			
			float unadjustedPreviousRating = 
					ratingType.equals(RatingType.DRIVER) ? ratingCalculator.calculateDriverRating(previousSeason, previousRound, entrant.driverName) :
					ratingCalculator.calculateTeamRating(previousSeason, previousRound, entrant.driverName);
			
			float upperRating = decayFactor(distance, decayRate);
			
			ratings += unadjustedPreviousRating * upperRating;
			upperRatings += upperRating;
			if(previousRound != priorRound || previousSeason != priorSeason) {
				distance++;
			}
			priorRound = previousRound;
			priorSeason = previousSeason;
		}
		
		return ratings/upperRatings;
	}
	
	protected float decayFactor(int ago, float scale) {
		return (float) Math.exp(-ago*Math.pow(scale/1.3,1.8));
	}
	
	protected enum RatingType {
		DRIVER, TEAM;
	}
	
	protected static class PonderatingAccumulator {

		public static final float EPSILON = 0.00001f;
		
		float accumulatedValue = 0;
		float accumulatedWeight = 0;
		
		public PonderatingAccumulator accumulate(float value, float weight) {
			if(value>0) {
				accumulatedValue += value * weight;
				accumulatedWeight += weight;
			}
			return this;
		}

		public float value() {
			return accumulatedWeight > EPSILON ? accumulatedValue/accumulatedWeight : 0;
		}
	}
	
	public static class AlexPredictorParams {

		public final float driverPowerRatingDecayRate;
		public final float driverCircuitPowerRatingDecayRate;
		public final float driverPowerWeight;
		public final float driverCircuitPowerWeight;
		public final float teamPowerRatingDecayRate;
		public final float teamCircuitPowerRatingDecayRate;
		public final float teamPowerWeight;
		public final float teamCircuitPowerWeight;

		public AlexPredictorParams(
				float driverPowerRatingDecayRate, 
				float driverCircuitPowerRatingDecayRate, 
				float teamPowerRatingDecayRate, 
				float teamCircuitPowerRatingDecayRate, 
				float driverPowerWeight, 
				float driverCircuitPowerWeight,
				float teamPowerWeight, 
				float teamCircuitPowerWeight) {
			this.driverPowerRatingDecayRate = driverPowerRatingDecayRate;
			this.driverCircuitPowerRatingDecayRate = driverCircuitPowerRatingDecayRate;
			this.teamPowerRatingDecayRate = teamPowerRatingDecayRate;
			this.teamCircuitPowerRatingDecayRate = teamCircuitPowerRatingDecayRate;
			this.driverPowerWeight = driverPowerWeight;
			this.driverCircuitPowerWeight = driverCircuitPowerWeight;
			this.teamPowerWeight = teamPowerWeight;
			this.teamCircuitPowerWeight = teamCircuitPowerWeight;
		}
		
		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
		
		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}
		
		@Override
		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj);
		}
		
		public static AlexPredictorParams randomParams() {
			return new AlexPredictorParams(
					(float) Math.random(), 
					(float) Math.random(), 
					(float) Math.random(), 
					(float) Math.random(), 
					(float) Math.random(), 
					(float) Math.random(), 
					(float) Math.random(), 
					(float) Math.random());
		}
	}
}
