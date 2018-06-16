package physics;

import java.util.ArrayList;

public class Templates {
	
	public static final double MASS_SUN = 1.9E30; // Kg
	public static final double MASS_EARTH = 5.9E24;
	public static final double MASS_MOON = 7.349E22;
	
	public static final double DENSITY_SUN = 1408; // Kg / m³
	public static final double DENSITY_EARTH = 5515;
	public static final double DENSITY_MOON = 3341;
	
	public static ArrayList<Body> loadSolarsystem() {
		ArrayList<Body> bodies = new ArrayList<Body>();
		
		//bodies.add(new Body(1.9E30, 0, 0, 0, 0, true, "Sonne", DENSITY_SUN));
		//bodies.add(new Body(MASS_EARTH, 0, -150E9, -19000, 0, false, "Erde", DENSITY_EARTH));
		
		Body sun = new Body(MASS_SUN, 0, 0, 0, 0, true, "Sonne", DENSITY_SUN);
		Body earth = new Body(sun, true, true, MASS_EARTH, 0, -150E9, 29000, 0, false, "Erde", DENSITY_EARTH);
		Body moon = new Body(earth, true, true, MASS_MOON, 384.4E6, 0, 0, 1023, false, "Mond", DENSITY_MOON);
//		Body moon = new Body(earth, true, true, MASS_MOON, 384.4E6, 0, 0, 700, false, "Mond", DENSITY_MOON);
		bodies.add(sun);
		bodies.add(earth);
		bodies.add(moon);
		
		
		return bodies;
	}
	
	public static ArrayList<Body> loadMeteorite() {
		ArrayList<Body> bodies = new ArrayList<Body>();
		
		bodies.add(new Body(MASS_SUN, 0, 0, 0, 0, true, "Sonne", DENSITY_SUN));
		bodies.add(new Body(MASS_EARTH, 0, -150E9, 20000, 10000, false, "earth", DENSITY_EARTH));
		
		return bodies;
	}
	
	public static ArrayList<Body> loadSolarsystemDoubled() {
		ArrayList<Body> bodies = new ArrayList<Body>();
		
		bodies.add(new Body(MASS_SUN, 0, 0, 0, 0, true, "Sonne", DENSITY_SUN));
		
		bodies.add(new Body(MASS_EARTH, 0, -150E9, 30000, 0, false, "Erde 1", DENSITY_EARTH));
		bodies.add(new Body(MASS_EARTH, 0, 150E9, -30000, 0, false, "Erde 2", DENSITY_EARTH));
		
		return bodies;
	}
	
	public static ArrayList<Body> loadMeteoriteDoubled() {
		ArrayList<Body> bodies = new ArrayList<Body>();
		
		bodies.add(new Body(MASS_EARTH, 0, 0, 0, 0, true, "", DENSITY_SUN));
		
		bodies.add(new Body(MASS_EARTH, 0, -150E9, 20000, 10000, false, "", DENSITY_EARTH));
		bodies.add(new Body(MASS_EARTH, 0, 150E9, 20000, -9500, false, "", DENSITY_EARTH));
		
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
					MASS_EARTH,
					Math.random()*50000000*2 - 50000000 ,
					Math.random()*50000000*2 - 50000000 ,
					Math.random()*5000*2 - 2500,
					Math.random()*5000*2 - 2500,
					false,
					"",
					1
				)
			);
			
		}
		
		
		return bodies;
	}	
}
