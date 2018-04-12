package physics;

public class Functions {
	
	public static double calcDistance(Body b0, Body b1){
		return Math.sqrt(Math.pow(b1.x - b0.x, 2) + Math.pow(b1.y - b0.y, 2));
	}
	
	public static double calcGravityForce(double m1, double m2, double distance) {
		return (m1*m2*6.674E-11) / (distance * distance);
	}
	
	/**
	 * @returns degree from base to target
	 * (=> base = center of polar coordinate space,
	 *     0° = positive y axis)
	 */
	public static double getDegree(Body base, Body target) {
		return Math.atan2(target.x - base.x, target.y - base.y);
	}
}
