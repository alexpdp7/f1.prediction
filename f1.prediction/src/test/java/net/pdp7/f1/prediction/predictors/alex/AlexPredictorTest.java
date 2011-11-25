package net.pdp7.f1.prediction.predictors.alex;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import net.pdp7.f1.prediction.model.ModelTestUtils;
import net.pdp7.f1.prediction.predictors.PredictorPastEvaluatorTest;
import junit.framework.TestCase;

public class AlexPredictorTest extends TestCase {

	public void test() throws Exception {
		SimpleJdbcTemplate jdbcTemplate = new SimpleJdbcTemplate(ModelTestUtils.get20052011DataSource());
		PredictorPastEvaluatorTest.testPredictor(new AlexPredictor(new RatingCalculator(jdbcTemplate), jdbcTemplate, 0.32f));
	}
	
}
