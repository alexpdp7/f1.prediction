package net.pdp7.f1.prediction.predictors.alex.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.EvolutionaryOperator;

public class AlexPredictorParamsMutation implements EvolutionaryOperator<double[]> {

	protected final float cromosomeMutationProbability;
	protected final float mutationWeight;

	public AlexPredictorParamsMutation(float cromosomeMutationProbability, float mutationWeight) {
		this.cromosomeMutationProbability = cromosomeMutationProbability;
		this.mutationWeight = mutationWeight;
	}
	
	public List<double[]> apply(List<double[]> selectedCandidates, Random rng) {
		List<double[]> result = new ArrayList<double[]>();
		
		for(double[] selectedCandidate : selectedCandidates) {
			result.add(mutate(selectedCandidate, rng));
		}
		
		return result;
	}

	protected double[] mutate(double[] selectedCandidate, Random rng) {
		double[] result = new double[selectedCandidate.length];
		
		for(int i=0; i<result.length; i++) {
			result[i] = rng.nextFloat() < cromosomeMutationProbability ? mutate(selectedCandidate[i], rng) : selectedCandidate[i]; 
		}
		return result;
	}

	protected double mutate(double d, Random rng) {
		return d * (1- mutationWeight) + rng.nextFloat() * mutationWeight;
	}

}
