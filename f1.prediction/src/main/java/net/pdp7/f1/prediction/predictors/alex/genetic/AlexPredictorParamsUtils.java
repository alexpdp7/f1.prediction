package net.pdp7.f1.prediction.predictors.alex.genetic;

import net.pdp7.f1.prediction.predictors.alex.AlexPredictor.AlexPredictorParams;

public class AlexPredictorParamsUtils {

	protected AlexPredictorParamsUtils() {}
	
	public static double[] toArray(AlexPredictorParams alexPredictorParams) {
		return new double[] {
				alexPredictorParams.driverPowerRatingDecayRate, 
				alexPredictorParams.driverCircuitPowerRatingDecayRate, 
				alexPredictorParams.teamPowerRatingDecayRate, 
				alexPredictorParams.teamCircuitPowerRatingDecayRate, 
				alexPredictorParams.driverPowerWeight, 
				alexPredictorParams.driverCircuitPowerWeight,
				alexPredictorParams.teamPowerWeight, 
				alexPredictorParams.teamCircuitPowerWeight
		};
	}
	
	public static AlexPredictorParams fromArray(double[] params) {
		return new AlexPredictorParams(
				(float) params[0],
				(float) params[1],
				(float) params[2],
				(float) params[3],
				(float) params[4],
				(float) params[5],
				(float) params[6],
				(float) params[7]);
	}
	
}
