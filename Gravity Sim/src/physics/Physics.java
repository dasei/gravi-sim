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

		boolean rotationDirection = getRotationDirection(bodies, centerBody, target, timeMdklSDeltaSeconds);
		
		double degreeStart = Functions.getDegree(centerBody, target);
		
		
		boolean hasReachedAngleOverflowTop = false;
		boolean hasReachedAngleOverflowBottom = false;
		boolean reachedAngleOverflowTopFirst;
		if(rotationDirection == true)
			if(degreeStart < 0)
				reachedAngleOverflowTopFirst = false;
			else
				reachedAngleOverflowTopFirst = true;
		else
			if(degreeStart < 0)
				reachedAngleOverflowTopFirst = true;
			else
				reachedAngleOverflowTopFirst = false;
		
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
			
			double degree = Functions.getDegree(centerBody, target);
			
			double dist = Functions.calcDistance(target, centerBody);
			
			if(dist > maxDistance) {
				maxDistance = dist;
				maxDistanceDegree = degree;
			}
			if(dist < minDistance) {
				minDistance = dist;
				minDistanceDegree = degree;
			}
			
			//if overflow top will be reached first
			if(reachedAngleOverflowTopFirst) {
				
				//check if it now has been completed
				if(hasReachedAngleOverflowTop == false) {
					
					hasReachedAngleOverflowTop =
							(rotationDirection == true && degree < 0)
						||	(rotationDirection == false && degree > 0);
					
				}else {
					
					if(hasReachedAngleOverflowBottom == false) {
						
						//check if overflowBottom has been completed
						hasReachedAngleOverflowBottom =
								(rotationDirection == true && degree > 0)
							||	(rotationDirection == false && degree < 0);
						
					}else {
						
						if(		rotationDirection == true && degree < 0
							||	rotationDirection == false && degree > 0)							
							break;
					}					
				}	
				
			//has reached overflow bottom first
			}else {
				
				//check if it now has been completed
				if(hasReachedAngleOverflowBottom == false) {
					
					hasReachedAngleOverflowBottom =
							(rotationDirection == true && degree > 0)
						||	(rotationDirection == false && degree < 0);
					
				}else {
					
					if(hasReachedAngleOverflowTop == false) {
						
						//check if overflowBottom has been completed
						hasReachedAngleOverflowTop =
								(rotationDirection == true && degree < 0)
							||	(rotationDirection == false && degree > 0);
						
					}else {
						
						if(		rotationDirection == true && degree > 0
							||	rotationDirection == false && degree < 0)							
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
	private static boolean getRotationDirection(ArrayList<Body> bodies, Body bodyCenter, Body target, double timeMdklSDelta) {
		
		double degree1 = Functions.getDegree(bodyCenter, target);
		
		double degree2 = degree1;
		//make sure, degree1 and degree2 arent the same (Rounding error)
		while(degree1 == degree2) {
			physicsIteration(bodies, timeMdklSDelta);		
			degree2 = Functions.getDegree(bodyCenter, target);
		}
		
		double degree3 = degree2;
		//make sure, degree2 and degree3 arent the same (Rounding error)
		while(degree2 == degree3) {
			physicsIteration(bodies, timeMdklSDelta);		
			degree3 = Functions.getDegree(bodyCenter, target);
		}
		
		//check if an overflow has (not) happened
		if(		   (degree1 < degree2 && degree2 < degree3)
				|| (degree1 > degree2 && degree2 > degree3) ) {
			
			//no overflow!
			return degree1 < degree2;
		}else {
			
			//Overflow!			
			return degree1 > 0;
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
