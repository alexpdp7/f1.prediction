package net.pdp7.f1.prediction.predictors.alex.genetic;

import net.pdp7.f1.prediction.predictors.alex.AlexPredictor.AlexPredictorParams;

public class AlexPredictorParamsUtils {

	protected AlexPredictorParamsUtils() {}
	
	public static double[] toArray(AlexPredictorParams alexPredictorParams) {
		return new double[] {
				alexPredictorParams.driverCircuitPowerRatingDecayRate,
				alexPredictorParams.driverCircuitPowerWeight,
				alexPredictorParams.driverPowerRatingDecayRate,
				alexPredictorParams.driverPowerWeight,
		};
	}
	
	public static AlexPredictorParams fromArray(double[] params) {
		return new AlexPredictorParams(
				(float) params[0],
				(float) params[1],
				(float) params[2],
				(float) params[3]);
	}
	
}
