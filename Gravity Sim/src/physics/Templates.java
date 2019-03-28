package physics;

import java.util.ArrayList;

public class Templates {
	
	public static final double MASS_SUN = 1.9E30; // Kg
	public static final double MASS_EARTH = 5.9E24;
	public static final double MASS_MOON = 7.349E22;
	public static final double MASS_MARS = 6.39E23;
	public static final double MASS_MERCURY = 3.301E23;
	public static final double MASS_VENUS = 4.867E24;
	public static final double MASS_JUPITER = 1.899E27;
	public static final double MASS_SATURN = 5.685E26;
	public static final double MASS_URANUS = 8.683E25;
	public static final double MASS_NEPTUNE = 1.0243E26;
	
	public static final double DENSITY_SUN = 1408; // Kg / m³
	public static final double DENSITY_EARTH = 5515;
	public static final double DENSITY_MOON = 3341;
	public static final double DENSITY_MARS = 3930;
	public static final double DENSITY_MERCURY = 5427;
	public static final double DENSITY_VENUS = 5243;
	public static final double DENSITY_JUPITER = 1326;
	public static final double DENSITY_SATURN = 687;
	public static final double DENSITY_URANUS = 1270;
	public static final double DENSITY_NEPTUNE = 1638;
	
	public static ArrayList<Body> loadSolarsystemComplete() {
		ArrayList<Body> bodies = new ArrayList<Body>();
		
		Body sun = new Body(MASS_SUN, 0, 0, 0, 0, true, "Sonne", DENSITY_SUN);
		sun.setGIF("res/gifsFinal/sun.gif", -0.5f);
		
		Body mercury = new Body(sun, true, true, MASS_MERCURY, 0, -57909000000d, 47360, 0, false, "Merkur", DENSITY_MERCURY);
//		Body mercury = new Body(sun, true, true, MASS_MERCURY, 0, -57909000000d, 4360, 0, false, "Merkur", DENSITY_MERCURY);
		
		Body venus = new Body(sun, true, true, MASS_VENUS, 0, -108160000000d, 35020, 0, false, "Venus", DENSITY_VENUS);
		
		Body earth = new Body(sun, true, true, MASS_EARTH, 0, -150E9, 29000, 0, false, "Erde", DENSITY_EARTH);
		earth.setGIF("res/gifsFinal/earth.png", -0.11f);		
		Body moon = new Body(earth, true, true, MASS_MOON, 384.4E6, 0, 0, 1023, false, "Mond", DENSITY_MOON);
//		Body moon = new Body(earth, true, true, MASS_MOON, 384.4E6, 0, 0, 700, false, "Mond", DENSITY_MOON);
		moon.setGIF("res/gifsFinal/moon.png", -0.12f);
		
		Body mars = new Body(sun, true, true, MASS_MARS, 0, -227900000000d, 24000, 0, false, "Mars", DENSITY_MARS);
		
		Body jupiter = new Body(sun, true, true, MASS_JUPITER, 0, -778360000000d, 13070, 0, false, "Jupiter", DENSITY_JUPITER);
//		Body jupiter = new Body(sun, true, true, MASS_JUPITER, 0, -778360000000d, 1370, 0, false, "Jupiter", DENSITY_JUPITER);
		
		Body saturn = new Body(sun, true, true, MASS_SATURN, 0, -1433500000000d, 9690, 0, false, "Saturn", DENSITY_SATURN);
		
		Body uranus = new Body(sun, true, true, MASS_URANUS, 0, -2872400000000d, 6810, 0, false, "Uranus", DENSITY_URANUS);
		
		Body neptune = new Body(sun, true, true, MASS_NEPTUNE, 0, -4498400000000d, 5430, 0, false, "Neptun", DENSITY_NEPTUNE);
		
		Body ceres = new Body(sun, true, true, 9394E17, 413940000000d, 0, 0, 10000, false, "Ceres <- Asteroid", 2160);
		
		Body halley = new Body(sun, true, true, 2E17, 0, 8.76644E11, -4570, 0, false, "Halley <- Komet", 550);
		
		Body pluto = new Body(sun, true, true, 1.303E22, 0, 4.436774E12, -4670, 0, false, "Pluto", 1860);
		
		bodies.add(mercury);
		
		bodies.add(venus);		
		
		bodies.add(moon);
		bodies.add(earth);
		
		bodies.add(mars);
		
		bodies.add(jupiter);
		
		bodies.add(saturn);
		
		bodies.add(uranus);
		
		bodies.add(neptune);
		
		bodies.add(ceres);
		
		bodies.add(halley);
		
		bodies.add(pluto);
		
		bodies.add(sun);
		
		return bodies;
	}
	
	public static ArrayList<Body> loadSolarsystemLite(){
		
		ArrayList<Body> bodies = new ArrayList<Body>();
		
		Body sun = new Body(MASS_SUN, 0, 0, 0, 0, true, "Sonne", DENSITY_SUN);
		sun.setGIF("res/gifsFinal/sun.gif", -0.5f);
		
		Body earth = new Body(sun, true, true, MASS_EARTH, 0, -150E9, 29000, 0, false, "Erde", DENSITY_EARTH);
		earth.setGIF("res/gifsFinal/earth.png", -0.11f);
		
		Body moon = new Body(earth, true, true, MASS_MOON, 384.4E6, 0, 0, 1023, false, "Mond", DENSITY_MOON);
//		Body moon = new Body(earth, true, true, MASS_MOON, 384.4E6, 0, 0, 700, false, "Mond", DENSITY_MOON);
		moon.setGIF("res/gifsFinal/moon.png", -0.12f);
		
		bodies.add(sun);
		bodies.add(moon);
		bodies.add(earth);
		
		return bodies;
	}
	
	public static ArrayList<Body> loadSolarsystemMeteorites(){
		ArrayList<Body> bodies = loadSolarsystemComplete();
		
		for(int i = 0; i < 1000; i++) {
			bodies.add(new Body(MASS_MOON, 600000000000d, (i*5000000000d) - 250000000000d, -1000, 0, false, "", DENSITY_MOON));
		}
		
		return bodies;
	}
	
//	public static ArrayList<Body> loadMeteorite() {
//		ArrayList<Body> bodies = new ArrayList<Body>();
//		
//		bodies.add(new Body(MASS_SUN, 0, 0, 0, 0, true, "Sonne", DENSITY_SUN));
//		bodies.add(new Body(MASS_EARTH, 0, -150E9, 20000, 10000, false, "earth", DENSITY_EARTH));
//		
//		return bodies;
//	}
//	
//	public static ArrayList<Body> loadSolarsystemDoubled() {
//		ArrayList<Body> bodies = new ArrayList<Body>();
//		
//		bodies.add(new Body(MASS_SUN, 0, 0, 0, 0, true, "Sonne", DENSITY_SUN));
//		
//		bodies.add(new Body(MASS_EARTH, 0, -150E9, 30000, 0, false, "Erde 1", DENSITY_EARTH));
//		bodies.add(new Body(MASS_EARTH, 0, 150E9, -30000, 0, false, "Erde 2", DENSITY_EARTH));
//		
//		return bodies;
//	}
//	
//	public static ArrayList<Body> loadMeteoriteDoubled() {
//		ArrayList<Body> bodies = new ArrayList<Body>();
//		
//		bodies.add(new Body(MASS_EARTH, 0, 0, 0, 0, true, "", DENSITY_SUN));
//		
//		bodies.add(new Body(MASS_EARTH, 0, -150E9, 20000, 10000, false, "", DENSITY_EARTH));
//		bodies.add(new Body(MASS_EARTH, 0, 150E9, 20000, -9500, false, "", DENSITY_EARTH));
//		
//		return bodies;
//	}
//	
//	/**
//	 * pxPerMeter scale about 100000
//	 */
//	public static ArrayList<Body> loadRandom(int amount){
//		ArrayList<Body> bodies = new ArrayList<Body>();
//		
//		
//		for(int i = 0; i < amount; i++){
//			
//			bodies.add(
//				new Body(
//					MASS_EARTH,
//					Math.random()*50000000*2 - 50000000 ,
//					Math.random()*50000000*2 - 50000000 ,
//					Math.random()*5000*2 - 2500,
//					Math.random()*5000*2 - 2500,
//					false,
//					"",
//					1
//				)
//			);
//			
//		}
//		
//		
//		return bodies;
//	}
	
}
