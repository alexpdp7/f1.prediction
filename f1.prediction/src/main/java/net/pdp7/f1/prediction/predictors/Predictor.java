package net.pdp7.f1.prediction.predictors;

import java.util.Arrays;

public interface Predictor {

	public Prediction predict(int season, int round, String circuitName, Entrant[] entrants);
	
	public class Prediction {
		public final String pole;
		public final String fastestLap;
		public final String[] topTen;

		public Prediction(String pole, String fastestLap, String[] topTen) {
			this.pole = pole;
			this.fastestLap = fastestLap;
			this.topTen = topTen;
		}
		
		@Override
		public String toString() {
			return super.toString() + "pole:" + pole + ",fastestLap:" + fastestLap + ",topTen:" + Arrays.toString(topTen);
		}
	}
	
	public class Entrant {
		public final String teamName;
		public final String driverName;

		public Entrant(String teamName, String driverName) {
			this.teamName = teamName;
			this.driverName = driverName;
		}
		
		@Override
		public String toString() {
			return super.toString() + "teamName:" + teamName + ",driverName:" + driverName;
		}
	}
}
