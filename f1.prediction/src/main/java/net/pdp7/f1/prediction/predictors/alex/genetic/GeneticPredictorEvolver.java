package net.pdp7.f1.prediction.predictors.alex.genetic;

import java.util.Arrays;

import net.pdp7.commons.spring.context.annotation.AnnotationConfigApplicationContextUtils;
import net.pdp7.commons.util.MapUtils;
import net.pdp7.f1.prediction.model.ModelUtils;
import net.pdp7.f1.prediction.spring.DataSourceConfig;
import net.pdp7.f1.prediction.spring.F1PredictionConfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.termination.ElapsedTime;

public class GeneticPredictorEvolver {

	public static void main(String[] args) throws Exception {
		AnnotationConfigApplicationContext applicationContext = AnnotationConfigApplicationContextUtils.createConfiguredAnnotationConfigApplicationContext(
				MapUtils.createPropertiesFromMap(MapUtils.build("jdbc.url", ModelUtils.get20052011DatabaseUrl()).map), 
				F1PredictionConfig.class, 
				DataSourceConfig.JdbcUrlDataSourceConfig.class,
				GeneticPredictorEvolutionConfig.class);
		
		@SuppressWarnings("unchecked")
		EvolutionEngine<double[]> evolutionEngine = applicationContext.getBean("evolutionEngine", EvolutionEngine.class);
		
		double[] evolve = evolutionEngine.evolve(1000, 10, new ElapsedTime(60000));
		
		System.out.println(Arrays.toString(evolve));
		
	}
	
}
