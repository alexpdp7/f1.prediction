package net.pdp7.f1.prediction.predictors.alex;

import junit.framework.TestCase;
import net.pdp7.f1.prediction.LoggingUtils;
import net.pdp7.f1.prediction.model.ModelUtils;
import net.pdp7.f1.prediction.predictors.Predictor.Entrant;
import net.pdp7.f1.prediction.predictors.PredictorPastEvaluatorTest;
import net.pdp7.f1.prediction.predictors.alex.AlexPredictor.AlexPredictorParams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class AlexPredictorTest extends TestCase {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
//	public void test() throws Exception {
//		LoggingUtils.setupLogging();
//		SimpleJdbcTemplate jdbcTemplate = new SimpleJdbcTemplate(ModelUtils.get20052011DataSource());
//		
//		int predictorResult = PredictorPastEvaluatorTest.testPredictor(
//				new AlexPredictor(
//						new SimpleRatingCalculator(jdbcTemplate), 
//						jdbcTemplate, 
//						new AlexPredictorParams(0.7655972053017919f, 0.6198067666337721f, .99803f, 0)), 2011, 2011);
//		
//		logger.info("predictor result {}" , predictorResult);
//	}
//	
//	public void test2() throws Exception {
//		SimpleJdbcTemplate jdbcTemplate = new SimpleJdbcTemplate(ModelUtils.get20052011DataSource());
//		
//		Entrant[] entrants = new Entrant[] {
//				new Entrant("Red Bull-Renault","Sebastian Vettel"),
//				new Entrant("McLaren-Mercedes","Lewis Hamilton"),
//				new Entrant("McLaren-Mercedes","Jenson Button"),
//				new Entrant("Red Bull-Renault","Mark Webber"),
//				new Entrant("Ferrari","Fernando Alonso"),
//				new Entrant("Ferrari","Felipe Massa"),
//				new Entrant("Mercedes","Nico Rosberg"),
//				new Entrant("Mercedes","Michael Schumacher"),
//				new Entrant("Force India-Mercedes","Adrian Sutil"),
//				new Entrant("Force India-Mercedes","Paul di Resta"),
//				new Entrant("Sauber-Ferrari","Sergio Pérez"),
//				new Entrant("Renault","Vitaly Petrov"),
//				new Entrant("Toro Rosso-Ferrari","Sébastien Buemi"),
//				new Entrant("Renault","Bruno Senna"),
//				new Entrant("Toro Rosso-Ferrari","Jaime Alguersuari"),
//				new Entrant("Sauber-Ferrari","Kamui Kobayashi"),
//				new Entrant("Williams-Cosworth","Pastor Maldonado"),
//				new Entrant("Lotus-Renault","Heikki Kovalainen"),
//				new Entrant("Lotus-Renault","Jarno Trulli"),
//				new Entrant("Virgin-Cosworth","Timo Glock"),
//				new Entrant("HRT-Cosworth","Daniel Ricciardo"),
//				new Entrant("Virgin-Cosworth","Jérôme d'Ambrosio"),
//				new Entrant("HRT-Cosworth","Vitantonio Liuzzi"),
//				new Entrant("Williams-Cosworth","Rubens Barrichello"),
//		};
//		
//		System.out.println(new AlexPredictor(new SimpleRatingCalculator(jdbcTemplate), jdbcTemplate, new AlexPredictorParams(
//				0.7655972053017919f, 
//				0.6198067666337721f, 
//				.99803f, 
//				0)).predict(2011, 19, "Autódromo José Carlos Pace, São Paulo", entrants));
//	}
	
}
