package net.pdp7.f1.prediction.predictors;

import java.io.IOException;

import net.pdp7.commons.spring.context.annotation.AnnotationConfigApplicationContextUtils;
import net.pdp7.commons.util.MapUtils;
import net.pdp7.f1.prediction.model.ModelTestUtils;
import net.pdp7.f1.prediction.spring.DataSourceConfig;
import net.pdp7.f1.prediction.spring.F1PredictionConfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import junit.framework.TestCase;

public class PredictorPastEvaluatorTest extends TestCase {

	public void test() throws Exception {
		testPredictor(new RandomPredictor());
	}

	public static int testPredictor(Predictor predictor) throws IOException {
		PredictorPastEvaluator predictorPastEvaluator = createPredictorPastEvaluator();
		return predictorPastEvaluator.evaluate(predictor);
	}

	public static PredictorPastEvaluator predictorPastEvaluator = null;
	
	public static PredictorPastEvaluator createPredictorPastEvaluator() throws IOException {
		if(predictorPastEvaluator != null) {
			return predictorPastEvaluator;
		}
		
		AnnotationConfigApplicationContext applicationContext = AnnotationConfigApplicationContextUtils.createConfiguredAnnotationConfigApplicationContext(
				MapUtils.createPropertiesFromMap(MapUtils.build("jdbc.url", ModelTestUtils.get20052011DatabaseUrl()).map), 
				F1PredictionConfig.class, DataSourceConfig.JdbcUrlDataSourceConfig.class);
		
		predictorPastEvaluator = applicationContext.getBean("predictorPastEvaluator", PredictorPastEvaluator.class);
		return predictorPastEvaluator;
	}
	
}
