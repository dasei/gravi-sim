package physics;

public class Body {
	public double mass;
	
	public double x;
	public double y;
	
	public double vx;
	public double vy;
	
	public final boolean posFixed;
	
	private String name;
	
	public Body(double mass, double x, double y, double vx, double vy, boolean posFixed, String name){
		
		this.mass = mass;
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		
		this.posFixed = posFixed;
		
		this.name = name;
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
			"Body: mass: " + this.mass 
			+ ", x: " + this.x 
			+ ", y: " + this.y 
			+ ", speedX: " + this.vx
			+ ", speedY: " + this.vy;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
