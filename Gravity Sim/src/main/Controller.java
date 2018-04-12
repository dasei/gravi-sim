package main;

import java.util.ArrayList;

import physics.Body;
import physics.Functions;
import window.Window;

public class Controller {
	private Window window;
	
	private ArrayList<Body> bodies;
	
	private double timeMdklSIterationSeconds = 1;
	
	public Controller() {
		
		window = new Window();
	
		startLoop(1);
		
	}
	
	
	private void startLoop(long iterationDelay) {
		Runnable loop = new Runnable() {
			public void run() {
				
				// --
				
				while(true) {
					
					//do physics
					physicsIteration(timeMdklSIterationSeconds);					
					
					
					//sleep
					if(iterationDelay > 0)
						try {
							Thread.sleep(iterationDelay);
						}catch(Exception e) {							
						}
				}
					
				// --
			}
		};
		
		new Thread(loop).start();
	}
	
	private void physicsIteration(double timeDeltaSeconds) {
		
		double pi = Math.PI;
		
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
				
				
//				//sin(degrB1) = forceX / force
//				//cos(degrB1) = forceY / force

//				// für b1:
//				// => sin(degrB1) * force = forceX
				double forceB1X = Math.sin(degrB1) * force;
				// => cos(degrB1) * force = forceY
				double forceB1Y = Math.cos(degrB1) * force;
				
				
				// für b2: (entgegengesetzte Richtung)
				double forceB2X = -forceB1X;
				double forceB2Y = -forceB1Y;
				

				// F / m = a
				// v = a * t
				b1.addSpeed(
						(forceB1X / b1.mass) * timeDeltaSeconds,
						(forceB1Y / b1.mass) * timeDeltaSeconds
					);
				b2.addSpeed(
						(forceB2X / b2.mass) * timeDeltaSeconds,
						(forceB2Y / b2.mass) * timeDeltaSeconds
					);
				
				// s = v * t
				b1.addPos(
						b1.vx * timeDeltaSeconds,
						b1.vy * timeDeltaSeconds
					);
				
				
				b2.addPos(
						b2.vx * timeDeltaSeconds,
						b2.vy * timeDeltaSeconds
					);
			}
		}		
	}
	
//--------------------------------------------------------------------------------------------------
	//GETTERS
	
	public Window getWindow() {
		return this.window;
	}
}
