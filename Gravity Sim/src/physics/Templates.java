package physics;

import java.util.ArrayList;

public class Templates {
	
	public static final double massSun = 1.9E30;
	public static final double massEarth = 5.9E24;
	
	public static final double DENSITY_SUN = 1408;
	public static final double DENSITY_EARTH = 5515;
	
	public static ArrayList<Body> loadSolarsystem() {
		ArrayList<Body> bodies = new ArrayList<Body>();
		
		bodies.add(new Body(1.9E30, 0, 0, 0, 0, true, "Sonne", DENSITY_SUN));
		bodies.add(new Body(5.9E24, 0, -150E9, -19000, 0, false, "Erde", DENSITY_EARTH));
		
		return bodies;
	}
	
//	public static ArrayList<Body> loadMeteorite() {
//		ArrayList<Body> bodies = new ArrayList<Body>();
//		
//		bodies.add(new Body(1.9E30, 0, 0, 0, 0, true, "Sonne"));
//		bodies.add(new Body(5.9E24, 0, -150E9, 20000, 10000, false, ""));
//		
//		return bodies;
//	}
	
	public static ArrayList<Body> loadSolarsystemDoubled() {
		ArrayList<Body> bodies = new ArrayList<Body>();
		
		bodies.add(new Body(massSun, 0, 0, 0, 0, true));
		
		bodies.add(new Body(massEarth, 0, -150E9, 30000, 0, false));
		bodies.add(new Body(massEarth, 0, 150E9, -30000, 0, false));
		
		return bodies;
	}
	
	public static ArrayList<Body> loadMeteoriteDoubled() {
		ArrayList<Body> bodies = new ArrayList<Body>();
		
		bodies.add(new Body(1.9E30, 0, 0, 0, 0, true));
		
		bodies.add(new Body(5.9E24, 0, -150E9, 20000, 10000, false));
		bodies.add(new Body(5.9E24, 0, 150E9, 20000, -9500, false));
		
		return bodies;
	}
	
	public static ArrayList<Body> loadWTF() {
		ArrayList<Body> bodies = new ArrayList<Body>();
		
//		bodies.add(new Body(massSun, 0, 0, 0, 0, true));
		
		
		bodies.add(new Body(massEarth, 0, 0, 0, 1000, false));
		
		bodies.add(new Body(massEarth, 10000000, 10000000, 0, -5000, false));
		bodies.add(new Body(massEarth, -10000000, -10000000, 0, 5000, false));
		//bodies.add(new Body(massEarth, -10000000, -10000000, 0, 5000, false));
		
		return bodies;
	}
	
	/**
	 * pxPerMeter scale about 100000
	 */
	public static ArrayList<Body> loadRandom(int amount){
		ArrayList<Body> bodies = new ArrayList<Body>();
		
		
		for(int i = 0; i < amount; i++){
			
			bodies.add(
				new Body(
					massEarth,
					Math.random()*50000000*2 - 50000000 ,
					Math.random()*50000000*2 - 50000000 ,
					Math.random()*5000*2 - 2500,
					Math.random()*5000*2 - 2500,
					false
					)
				);
			
		}
		
		
		return bodies;
	}
	
	
}
