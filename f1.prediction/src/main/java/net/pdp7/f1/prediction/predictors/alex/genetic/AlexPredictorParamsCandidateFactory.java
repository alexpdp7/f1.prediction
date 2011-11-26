package net.pdp7.f1.prediction.predictors.alex.genetic;

import java.util.Random;

import net.pdp7.f1.prediction.predictors.alex.AlexPredictor.AlexPredictorParams;

import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

public class AlexPredictorParamsCandidateFactory extends AbstractCandidateFactory<double[]>  implements CandidateFactory<double[]> {

	public double[] generateRandomCandidate(Random arg0) {
		return AlexPredictorParamsUtils.toArray(AlexPredictorParams.randomParams());
	}

}
