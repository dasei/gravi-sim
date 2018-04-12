package physics;

public class Body {
	public double mass;
	
	public double x;
	public double y;
	
	public double vx;
	public double vy;
	
	public final boolean posFixed;
	
	public Body(double mass, double x, double y, double vx, double vy, boolean posFixed){
		
		this.mass = mass;
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		
		this.posFixed = posFixed;
	}
	
	public void addPos(double dx, double dy) {
		if(this.posFixed)
			return;			
		this.x += dx;
		this.y += dy;		
	}
	
	public void addSpeed(double dvx, double dvy) {
		if(this.posFixed)
			return;			
		this.vx += dvx;
		this.vy += dvy;
	}
	
	public String toString() {
		return
				"Body: mass: " + this.mass 
				+ ", x: " + this.x 
				+ ", y: " + this.y 
				+ ", speedX: " + this.vx
				+ ", speedY: " + this.vy;
	}
}
