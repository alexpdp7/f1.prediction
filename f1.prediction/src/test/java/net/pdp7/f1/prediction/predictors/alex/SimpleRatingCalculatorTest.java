package net.pdp7.f1.prediction.predictors.alex;

import net.pdp7.f1.prediction.model.ModelUtils;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import junit.framework.TestCase;

public class SimpleRatingCalculatorTest extends TestCase {

	public void test() throws Exception {
		RatingCalculator calculator = new SimpleRatingCalculator(new SimpleJdbcTemplate(ModelUtils.get20052011DataSource()));
		
		assertEquals(1.0, calculator.calculateDriverRating(2011, 5, "Sebastian Vettel"), 0.001);   // 1 of 21
		assertEquals(0.0, calculator.calculateDriverRating(2011, 5, "Narain Karthikeyan"), 0.001); // 21 of 21
		assertEquals(0.0, calculator.calculateDriverRating(2011, 5, "Felipe Massa"), 0.001);       // retired
		assertEquals(12.0/20.0, calculator.calculateDriverRating(2011, 5, "Sergio Pérez"), 0.001); // 9 of 21
		
		assertEquals(0.0, calculator.calculateTeamRating(2011, 12, "Toro Rosso"), 0.001);              // both Toro Rosso did not finish
		assertEquals((1+17.0/18)/2, calculator.calculateTeamRating(2011, 12, "Red Bull"), 0.001);      // 1 and 2 of 19
		assertEquals((15.0/18+11.0/18)/2, calculator.calculateTeamRating(2011, 12, "Ferrari"), 0.001); // 4 and 8 of 19
	}
	
}
