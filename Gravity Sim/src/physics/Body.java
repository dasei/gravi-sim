package physics;

import main.Main;

public class Body {
	public double mass;
	
	public double x;
	public double y;
	
	public double vx;
	public double vy;
	
	public double volume;
	public double radiusMeters;
	
	public final boolean posFixed;

	// stats of info tag
	// used to display data like name
	public int infoTagXPix;
	public int infoTagYPix;
	public int infoTagWidthPix;
	public int infoTagHeightPix;
	public int infoTagFontSize;
	
	private String name;
	
	private long id = Main.getNewID();
	
//	public Body(double mass, double x, double y, double vx, double vy, boolean posFixed){
//		
//		this.mass = mass;
//		this.x = x;
//		this.y = y;
//		this.vx = vx;
//		this.vy = vy;
//		
//		this.posFixed = posFixed;
//	}
	
	public Body(double mass, double x, double y, double vx, double vy, boolean posFixed, String name, double density){
		
		this.mass = mass;
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		
		this.posFixed = posFixed;
		
		this.setName(name);
		
		// p = m / V
		this.volume = this.mass / density;
		// V = 4/3 * r³ * pi
		this.radiusMeters = Math.cbrt(this.volume/((4D/3D) * Math.PI));
	}
	
	/**
	 * creates a Body Object. if usePosition/useSpeed is true, position/speed of reference object
	 * is added to  x & y / xx & vy  
	 */
	public Body(Body reference, boolean usePosition, boolean useSpeed, double mass, double x, double y, double vx, double vy, boolean posFixed, String name, double density){
		this.mass = mass;
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		
		this.posFixed = posFixed;
		
		this.setName(name);
		
		// p = m / V
		this.volume = this.mass / density;
		// V = 4/3 * r³ * pi
		this.radiusMeters = Math.cbrt(this.volume/((4D/3D) * Math.PI));
		
		if(reference == null){
			System.out.println("given Body equals null, using given params");
		}else{
			
			if(usePosition){
				this.x += reference.x;
				this.y += reference.y;
			}
			
			if(useSpeed){
				this.vx += reference.vx;
				this.vy += reference.vy;
			}
			
		}
			
		//if(usePosition)
		
	}
	
	private double forceCacheX = 0;
	private double forceCacheY = 0;
	public void addForce(double x, double y){
		if(this.posFixed)
			return;
		forceCacheX += x;
		forceCacheY += y;
	}
	
	public void applyForces(double timeInterval, boolean clearForcesOnEnd){
		if(this.posFixed)
			return;
		
		// F = m * a
		// F / m = a
		
		// v = a * t
		
		// => v = (F / m) * t  
		
		this.vx += (this.forceCacheX / this.mass) * timeInterval;
		this.vy += (this.forceCacheY / this.mass) * timeInterval;
		
		this.x += this.vx * timeInterval;
		this.y += this.vy * timeInterval;
		
		if(clearForcesOnEnd){
			forceCacheX = 0;
			forceCacheY = 0;
		}
			
	}
	
	public void resetForceCache(){
		this.forceCacheX = 0;
		this.forceCacheY = 0;
	}
	
	public String toString() {
		return
			"Body: " + (this.name == null ? "<null>" : this.name)
			+ ", " + this.id;
	}
	
	public String toString2() {
		return
			"Body: " + (this.name == null ? "<null>" : this.name)
			+ ", mass: " + this.mass
			+ ", x: " + this.x 
			+ ", y: " + this.y 
			+ ", speedX: " + this.vx
			+ ", speedY: " + this.vy;
	}
	
	public void setName(String name) {
		this.name = name.replace(":", "_");
	}
	
	public String getName(){
		return this.name;
	}
}
