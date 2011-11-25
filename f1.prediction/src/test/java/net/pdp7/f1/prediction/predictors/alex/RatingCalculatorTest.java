package net.pdp7.f1.prediction.predictors.alex;

import net.pdp7.f1.prediction.model.ModelTestUtils;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import junit.framework.TestCase;

public class RatingCalculatorTest extends TestCase {

	public void test() throws Exception {
		RatingCalculator calculator = new RatingCalculator(new SimpleJdbcTemplate(ModelTestUtils.get20052011DataSource()));
		
		assertEquals(1.0, calculator.calculateDriverRating(2011, 5, "Sebastian Vettel"), 0.001);   // 1 of 21
		assertEquals(0.0, calculator.calculateDriverRating(2011, 5, "Narain Karthikeyan"), 0.001); // 21 of 21
		assertEquals(0.0, calculator.calculateDriverRating(2011, 5, "Felipe Massa"), 0.001);       // retired
		assertEquals(12.0/20.0, calculator.calculateDriverRating(2011, 5, "Sergio Pérez"), 0.001); // 9 of 21
		
	}
	
}
