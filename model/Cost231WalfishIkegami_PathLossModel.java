package model;

public class Cost231WalfishIkegami_PathLossModel {

	public static double getPathLossInDecibel( double frequency, double distance_in_km ) {
		double pl = 42.6;
		pl += 26 * Math.log10(distance_in_km);
		pl += 20 * Math.log10(frequency);
		return pl;
	}

	public static double getPathLoss( double frequency, double distance_in_km ) {
		return Math.pow(10, (1/10.0)*getPathLossInDecibel(frequency, distance_in_km));
	}
	
	public static double getReceivedPower( double transmitPower,
			double frequency, double distance_in_km ) {
		double p_r = transmitPower;
		p_r = p_r / Math.pow(10, 4.26);
		p_r = p_r / Math.pow(distance_in_km, 2.6);
		p_r = p_r / Math.pow(frequency, 2);
		return p_r;
	}
}
