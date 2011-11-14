package net.pdp7.f1.prediction.predictors;

public class RandomPredictor implements Predictor {

	public Prediction predict(int season, int round, String circuitName, Entrant[] entrants) {
		return new Prediction(
				randomEntrantName(entrants),
				randomEntrantName(entrants),
				new String[] {
					randomEntrantName(entrants),
					randomEntrantName(entrants),
					randomEntrantName(entrants),
					randomEntrantName(entrants),
					randomEntrantName(entrants),
					randomEntrantName(entrants),
					randomEntrantName(entrants),
					randomEntrantName(entrants),
					randomEntrantName(entrants),
					randomEntrantName(entrants),
				});
	}
	
	protected String randomEntrantName(Entrant[] entrants) {
		return entrants[(int) (Math.random() * entrants.length)].driverName;
	}
}
