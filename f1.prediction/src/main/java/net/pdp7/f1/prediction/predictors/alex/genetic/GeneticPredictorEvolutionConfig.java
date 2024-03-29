package net.pdp7.f1.prediction.predictors.alex.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.pdp7.f1.prediction.predictors.alex.CachingRatingCalculator;
import net.pdp7.f1.prediction.predictors.alex.RatingCalculator;
import net.pdp7.f1.prediction.predictors.alex.SimpleRatingCalculator;
import net.pdp7.f1.prediction.spring.DataSourceConfig;
import net.pdp7.f1.prediction.spring.F1PredictionConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.random.BinomialGenerator;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.CachingFitnessEvaluator;
import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.operators.DoubleArrayCrossover;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;

@Configuration
public class GeneticPredictorEvolutionConfig {

	@Autowired protected F1PredictionConfig f1PredictionConfig;
	@Autowired protected DataSourceConfig dataSourceConfig;
	
	public @Bean EvolutionEngine<double[]> evolutionEngine() {
		return new GenerationalEvolutionEngine<double[]>(candidateFactory(), evolutionaryOperator(), fitnessEvaluator(), selectionStrategy(), rng());
	}

	public @Bean Random rng() {
		return new MersenneTwisterRNG();
	}

	public @Bean RouletteWheelSelection selectionStrategy() {
		return new RouletteWheelSelection();
	}

	public @Bean FitnessEvaluator<double[]> fitnessEvaluator() {
		return new CachingFitnessEvaluator<double[]>(new AlexPredictorParamsFitnessEvaluator(f1PredictionConfig.predictorPastEvaluator(), ratingCalculator(), f1PredictionConfig.jdbcTemplate(), 2011, 2011));
	}

	public @Bean RatingCalculator ratingCalculator() {
		return new CachingRatingCalculator(simpleRatingCalculator());
	}

	private SimpleRatingCalculator simpleRatingCalculator() {
		return new SimpleRatingCalculator(f1PredictionConfig.jdbcTemplate());
	}

	public @Bean EvolutionaryOperator<double[]> evolutionaryOperator() {
		return new EvolutionPipeline<double[]>(pipeline());
	}

	public @Bean List<EvolutionaryOperator<double[]>> pipeline() {
		List<EvolutionaryOperator<double[]>> pipeline = new ArrayList<EvolutionaryOperator<double[]>>();
		pipeline.add(alexPredictorParamsCrossover());
		pipeline.add(alexPredictorParamsMutation());
		return pipeline;
	}

	public @Bean EvolutionaryOperator<double[]> alexPredictorParamsMutation() {
		return new AlexPredictorParamsMutation(0.03f, 0.4f);
	}

	public @Bean EvolutionaryOperator<double[]> alexPredictorParamsCrossover() {
		return new DoubleArrayCrossover(new BinomialGenerator(8, 0.03, rng()), new ConstantGenerator<Probability>(new Probability(0.03)));
	}

	public @Bean CandidateFactory<double[]> candidateFactory() {
		return new AlexPredictorParamsCandidateFactory();
	}
	
}
