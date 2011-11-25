package net.pdp7.f1.prediction.predictors.alex;

import java.util.Comparator;

import net.pdp7.f1.prediction.predictors.Predictor.Entrant;

public class EntrantPower {
	
	public final Entrant entrant;
	public final float power;

	public EntrantPower(Entrant entrant, float power) {
		this.entrant = entrant;
		this.power = power;
	}
	
	public static final Comparator<EntrantPower> POWER_COMPARATOR = new PowerComparator();
	
	protected static class PowerComparator implements Comparator<EntrantPower> {

		public int compare(EntrantPower o1, EntrantPower o2) {
			float powerDifference = o2.power - o1.power;
			return powerDifference == 0 ? o1.entrant.driverName.compareTo(o2.entrant.driverName) : (int) Math.signum(powerDifference);
		}
		
	}

}
