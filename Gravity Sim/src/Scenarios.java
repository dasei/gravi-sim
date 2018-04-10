import java.util.ArrayList;

public class Scenarios {
	
	public static final double massSun = 1.9E30;
	public static final double massEarth = 5.9E24;
	
	public static ArrayList<Body> loadSolarsystem() {
		ArrayList<Body> bodies = new ArrayList<Body>();
		
		bodies.add(new Body(5.9E24, 0, -150E9, 30000, 0, false));
		bodies.add(new Body(1.9E30, 0, 0, 0, 0, true));
		
		return bodies;
	}
	
	public static ArrayList<Body> loadMeteorite() {
		ArrayList<Body> bodies = new ArrayList<Body>();
		
		bodies.add(new Body(5.9E24, 0, -150E9, 20000, 0, false));
		bodies.add(new Body(1.9E30, 0, 0, 0, 0, true));
		
		return bodies;
	}
	
	public static ArrayList<Body> loadSolarsystemDoubled() {
		ArrayList<Body> bodies = new ArrayList<Body>();
		
		bodies.add(new Body(massEarth, 0, -150E9, 30000, 0, false));
		bodies.add(new Body(massSun, 0, 0, 0, 0, true));
		bodies.add(new Body(massEarth, 0, 150E9, -30000, 0, false));
		
		return bodies;
	}
	
	public static ArrayList<Body> loadWTF() {
		ArrayList<Body> bodies = new ArrayList<Body>();
		
//		bodies.add(new Body(massSun, 0, 0, 0, 0, true));
		
		
		
		bodies.add(new Body(massEarth, 10000000, 10000000, 0, -5000, false));
		bodies.add(new Body(massEarth, 0, 0, 0, 1000, false));
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
