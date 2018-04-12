package main;
import java.util.ArrayList;

import physics.Body;
import window.Window;

public class Main {
	
	public static ArrayList<Body> bodies;
	
	private static Window window;
	
	public static void main(String[] args){
//		bodies = new ArrayList<Body>();
//		
//		window = new Window();		
//		
//		Scenarios.loadSolarsystem();
////		bodies.add(new Body(5.9E24, 0, 150E9, -30000, 0, false));
//
//		try{
//			Thread.sleep(2000);
//		}catch(Exception e){}
//
//		
//		while(true){
//			calcIteration(1);
//			
////			try{
////				Thread.sleep(1);
////			}catch(Exception e){}
//		}
	}
	
	public static void calcIteration(double timeDeltaSeconds){
		
double pi = Math.PI;
		
		Body b1, b2;
		for(int bI = 0; bI < bodies.size(); bI++) {
			for(int b2I = bI + 1; b2I < bodies.size(); b2I++) {
				b1 = bodies.get(bI);
				b2 = bodies.get(b2I);
				
				
				
				//calc distance and directional force
				double distance = calcDistance(b1, b2);				
				double force = calcGravityForce(b1.mass, b2.mass, distance);
				
				//atan2 Ursprung = oben, Richtung: uhrzeigersinn
				//ABER: das koordi system ist ja hier nach unten gerichtet 
				// => Ursprung unten, rechts = +90, links = -90
				double degrB1 = getDegree(b1, b2);
				
				
//				degrB1 %= Math.PI*2;
//				degrB2 %= Math.PI*2;
//				
//				
//				//sin(degrB1) = forceX / force
//				//cos(degrB1) = forceY / force
//				
//				// für b1:
//				// => sin(degrB1) * force = forceX
				double forceB1X = Math.sin(degrB1) * force;
				// => cos(degrB1) * force = forceY
				double forceB1Y = Math.cos(degrB1) * force;
				
				
				// für b2: (entgegengesetzte Richtung)
				double forceB2X = -forceB1X;
				double forceB2Y = -forceB1Y;
				
//				System.out.println(Math.toDegrees(degrB1) + "°");
//				System.out.println(forceB1X + ", y: " + forceB1Y);
//				//System.out.println(Math.toDegrees(degrB2));
//				System.out.println();
				
				b1.addForce(forceB1X, forceB1Y);
				b2.addForce(forceB2X, forceB2Y);
				
				
				// F / m = a
				// v = a * t
//				b1.vx += (forceB1X / b1.mass) * timeDeltaSeconds;
//				b1.vy += (forceB1Y / b1.mass) * timeDeltaSeconds;
				
				
				
//				b1.addSpeed(
//						(forceB1X / b1.mass) * timeDeltaSeconds,
//						(forceB1Y / b1.mass) * timeDeltaSeconds
//					);
////				b2.vx += (forceB2X / b2.mass) * timeDeltaSeconds;
////				b2.vy += (forceB2Y / b2.mass) * timeDeltaSeconds;
//				b2.addSpeed(
//						(forceB2X / b2.mass) * timeDeltaSeconds,
//						(forceB2Y / b2.mass) * timeDeltaSeconds
//					);
//				
//				// s = v * t
////				b1.x += b1.vx * timeDeltaSeconds;
////				b1.y += b1.vy * timeDeltaSeconds;
//				b1.addPos(
//						b1.vx * timeDeltaSeconds,
//						b1.vy * timeDeltaSeconds
//					);
//				
//				
////				b2.x += b2.vx * timeDeltaSeconds;
////				b2.y += b2.vy * timeDeltaSeconds;
//				b2.addPos(
//						b2.vx * timeDeltaSeconds,
//						b2.vy * timeDeltaSeconds
//					);
//				iterations++;
//				if(iterations%1000 == 0)
//					System.out.println(distance);
				//System.out.println(bI + ", " + b2I);
			}
		}
		
		for(Body b : bodies)
			b.applyForces(timeDeltaSeconds, true);
		
		//bodies.get(0).y += 0.1E9;
		
//		System.out.println(bodies.get(0));
//		System.out.println(bodies.get(1));
//		System.out.println();
		
//		window.repaint();
		
	}
	
	private static void startAnalasis(Body centerBody, Body object) {
		
		double distanceLow = Double.MAX_VALUE;
		double distanceHigh = Double.MIN_VALUE;
		double degreeB1toB2;
		
		double degreeOnStart = getDegree(centerBody, object);
		double umrundungen = 0;
		
		while(umrundungen < 1.1) {
			calcIteration(1);
			
			distanceLow = Math.min(distanceLow, calcDistance(centerBody, object));
			distanceHigh = Math.max(distanceHigh, calcDistance(centerBody, object));
			
			
		}
		
	}
	
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
