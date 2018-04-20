package physics;

import java.util.ArrayList;

public class Physics {
	public static void physicsIteration(ArrayList<Body> bodies, double timeDeltaSeconds) {
		
		Body b1, b2;
		for(int bI = 0; bI < bodies.size(); bI++) {
			for(int b2I = bI + 1; b2I < bodies.size(); b2I++) {
				b1 = bodies.get(bI);
				b2 = bodies.get(b2I);
				
				
				//calc distance and directional force
				double distance = Functions.calcDistance(b1, b2);				
				double force = Functions.calcGravityForce(b1.mass, b2.mass, distance);
				
				//atan2 Ursprung = oben, Richtung: uhrzeigersinn
				//ABER: das koordi system ist ja hier nach unten gerichtet 
				// => Ursprung unten, rechts = +90, links = -90
				double degrB1 = Functions.getDegree(b1, b2);
				
				//sin(degrB1) = forceX / force
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
				
				b1.addForce(forceB1X, forceB1Y);
				b2.addForce(forceB2X, forceB2Y);
				
			}
		}
		
		for(Body b : bodies)
			b.applyForces(timeDeltaSeconds, true);
	}
	
	public static AnalazysResult runAnalizis(ArrayList<Body> bodies, Body centerBody, Body target, double timeMdklSDeltaSeconds) {
		
		//variables for storing max and min values
		double minDistance = Double.MAX_VALUE, minDistanceDegree = 0;
		double maxDistance = Double.MIN_VALUE, maxDistanceDegree = 0;

		boolean rotationDirection = getRotationDirection(centerBody, target);
//		System.out.println(rotationDirection);
		//System.out.println("Rotation: " + (rotationDirection ? "left" : "right"));
		
		double degreeStart = Functions.getDegree(centerBody, target);
		
		boolean hasReachedAngleOverflow = false;
		
		boolean hasCompletedHalfRotation = false;
		//winkel, bei dem eine halbe Umdrehung vollzogen ist
		double halfRotationDegree = (degreeStart + (Math.PI)) % (Math.PI*2);
		if(halfRotationDegree > (Math.PI))
			halfRotationDegree -= Math.PI*2;
		else if(halfRotationDegree < (-Math.PI))
			halfRotationDegree += Math.PI*2;
		
		//System.out.println("HRD: " + halfRotationDegree);
		
		while(true) {
			
			Body b1, b2;
			for(int bI = 0; bI < bodies.size(); bI++) {
				for(int b2I = bI + 1; b2I < bodies.size(); b2I++) {
					b1 = bodies.get(bI);
					b2 = bodies.get(b2I);
					
					
					//calc distance and directional force
					double distance = Functions.calcDistance(b1, b2);				
					double force = Functions.calcGravityForce(b1.mass, b2.mass, distance);
					
					//atan2 Ursprung = oben, Richtung: uhrzeigersinn
					//ABER: das koordi system ist ja hier nach unten gerichtet 
					// => Ursprung unten, rechts = +90, links = -90
					double degrB1 = Functions.getDegree(b1, b2);
					
					//sin(degrB1) = forceX / force
					//cos(degrB1) = forceY / force
					
					// für b1:
					// => sin(degrB1) * force = forceX
					double forceB1X = Math.sin(degrB1) * force;
					// => cos(degrB1) * force = forceY
					double forceB1Y = Math.cos(degrB1) * force;
					
					
					// für b2: (entgegengesetzte Richtung)
					double forceB2X = -forceB1X;
					double forceB2Y = -forceB1Y;
					
					b1.addForce(forceB1X, forceB1Y);
					b2.addForce(forceB2X, forceB2Y);
					
				}
			}
			
			for(Body b : bodies)
				b.applyForces(timeMdklSDeltaSeconds, true);
			
			
			////// ANALYZIS
			// analyze current stats
			
			double dist = Functions.calcDistance(target, centerBody);
			double degree = Functions.getDegree(centerBody, target);
			
			if(dist > maxDistance) {
				maxDistance = dist;
				maxDistanceDegree = degree;
			}
			if(dist < minDistance) {
				minDistance = dist;
				minDistanceDegree = degree;
//				System.out.println("minDist
			}
			
			
			//check if overflow has been reached
			if(hasReachedAngleOverflow == false) {
				//noch keinen Overflow erreicht
				
				//if rotating leftwards and angle is smaller
				if(rotationDirection == true && degree < degreeStart) {
					//Overflow has been reached!
					hasReachedAngleOverflow = true;
					
					//System.out.println("overflow reached!");
				}
				
				//if rotating rightwards and angle is bigger
				if(rotationDirection == false && degree > degreeStart) {
					//Overflow has been reached!
					hasReachedAngleOverflow = true;
					
					//System.out.println("overflow reached!");
				}			
				
			}
			//check if degree is now bigger / smaller than degreeStart
			if(hasReachedAngleOverflow){
				
				//if rotating leftwards and angle is bigger
				if(rotationDirection == true && degree > degreeStart) {
					//one full rotation completed!
					
					System.out.println("trigger: Overflow system");
					break;					
				}
				
				//if rotating rightwards and angle is smaller
				if(rotationDirection == false && degree < degreeStart) {
					//one full rotation completed!
					
					System.out.println("trigger: Overflow system");
					break;					
				}
				
			}
			
			
			if(hasReachedAngleOverflow == false) {
				
				
				//check if half a rotation was completed
				if(hasCompletedHalfRotation == false) {
					
					//if rotating leftwards and degree is bigger
					if(rotationDirection == true && degree > halfRotationDegree) {
						//completed half rotation!
						
						hasCompletedHalfRotation = true;
					}
					
					//if rotating rightwards and degree is smaller
					if(rotationDirection == false && degree < halfRotationDegree) {
						//completed half rotation!
						
						hasCompletedHalfRotation = true;
					}
				}
				//check if degree is smaller / bigger than halfRotation Degree
				//dann is das nämlich der Zeitpunkt, bei dem normalerweise der overflow eingetreten wäre
				if(hasCompletedHalfRotation == true) {
					
					//if rotating leftwards and degree is smaller
					if(rotationDirection == true && degree < halfRotationDegree) {
						//completed full rotation!
						
						System.out.println("trigger: Half rotation system");
						break;			
					}
					
					//if rotating rightwards and degree is bigger
					if(rotationDirection == false && degree > halfRotationDegree) {
						//completed full rotation!
						
						System.out.println("trigger: Half rotation system");
						break;			
					}
					
				}
			}
		}
		
		//System.out.println("analazys finished!");
		return new AnalazysResult(minDistance, minDistanceDegree, maxDistance, maxDistanceDegree, centerBody);
//		return null;
		
//		Main.getController().getWindow().getDrawComp().setShouldRepaint(false);		
	}

	
	/**
	 * @returns true for left, false for rightwards
	 */	
	private static boolean getRotationDirection(Body bodyCenter, Body target) {
		//TODO die auch für Ellipsen zum funktionieren bringen
		
		if(target.vx > bodyCenter.vx) {
			//target bewegt sich nach rechts
			
			if(target.y < bodyCenter.y) {
				
				//target ist über center
				return false;
				
			}else {
				
				//target ist unter center
				return true;
				
			}
			
		}else {
			//target bewegt sich nach links
			
			if(target.y < bodyCenter.y) {
				
				//target ist über center
				return true;
				
			}else {
				
				//target ist unter center
				return false;
				
			}
		}		
		
	}
	
	public static class AnalazysResult {
		public double minDistance;
		public double minDistanceDegree;
		
		public double maxDistance;
		public double maxDistanceDegree;
		
		public double a;
		public double b;
		
		public double eLin;
		public double eNum;
		
		public Body bodyCenter;		
		
		public AnalazysResult(double minDistance, double minDistanceDegree, double maxDistance, double maxDistanceDegree, Body bodyCenter) {
			this.minDistance = minDistance;
			this.minDistanceDegree = minDistanceDegree;
			
			this.maxDistance = maxDistance;
			this.maxDistanceDegree = maxDistanceDegree;
			
			this.bodyCenter = bodyCenter;
			
			a = (maxDistance + minDistance) / 2;
			eLin = a - minDistance;
			eNum = eLin / a;
			// a² = b² + eLin²
			b = Math.sqrt((a*a) - (eLin*eLin));
		}
		
		public String toString() {
			return "Analizys-Result: minimum distance: " + minDistance + ", at: " + Math.toDegrees(minDistanceDegree) + "°, maximum distance : " + maxDistance + ", at: " + Math.toDegrees(maxDistanceDegree) + "°";
		}
	}
}
