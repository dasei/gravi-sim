package window;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JComponent;

import main.Controller;
import main.Main;
import physics.Body;
import physics.Physics.AnalazysResult;


public class DrawComp extends JComponent{
	private static final long serialVersionUID = 41048338189849420L;
	
//	private double pxInMeters = 100000;
	private double pxInMeters = 1E9;
	
	private boolean shouldRepaint = true;
	
	private int cameraOffsetXPix;
	private double cameraOffsetXMeters;
	private int cameraOffsetYPix;
	private double cameraOffsetYMeters;
	
	private int lastWidth;
	private int lastHeight;
	
//	private long lastEllipseRepaintID = -1;
	
	private Controller controller;
	
	public DrawComp() {
//		this.addComponentListener(new ComponentListener() {
//			public void componentHidden(ComponentEvent e) {}
//			public void componentMoved(ComponentEvent e) {}
//			public void componentResized(ComponentEvent e) {
//				
//			}
//			public void componentShown(ComponentEvent e) {}
//		});
	}
	
	public void paintComponent(Graphics g) {
		if(!shouldRepaint)
			return;
		Graphics2D g2 = (Graphics2D) g;
		
		if(controller == null) {
			controller = Main.getController();
			if(controller == null)
				return;
		}
		ArrayList<Body> bodies = controller.getBodies();
		
		
		//modify offset if drawcomp was resized
		if(this.getWidth() != this.lastWidth || this.getHeight() != this.lastHeight) {
			this.centerCamera((this.lastWidth/2 - this.cameraOffsetXPix) * this.pxInMeters, (this.lastHeight/2 - this.cameraOffsetYPix) * this.pxInMeters);
		}
		
		int radiusPix;
		if(bodies != null)
			for(Body b : bodies) {
				radiusPix = (int) (b.radiusMeters / this.pxInMeters);
				g2.drawOval(cameraOffsetXPix + (int)(b.x/pxInMeters) - radiusPix, cameraOffsetYPix + (int)(b.y/pxInMeters) - radiusPix, radiusPix*2, radiusPix*2);
				drawInfoTab(g2, b);
			}
		
		drawEllipseWithFocusPoints(g2);
		
		lastWidth = this.getWidth();
		lastHeight = this.getHeight();
	}
	
	private void drawEllipseWithFocusPoints(Graphics2D g2) {
//		System.out.println("repainting ellipse");		
		
		AnalazysResult analazysResult = Main.getController().getAnalazysResult();
		
		if(analazysResult == null)
			return;
		
		double degreeRadians = -analazysResult.maxDistanceDegree + (Math.PI/2);
		double eLin = analazysResult.eLin / pxInMeters;
		
		//zeichnen der Brennpunkte
		double yB1 = Math.sin(-degreeRadians) * eLin;
		double xB1 = Math.cos(-degreeRadians) * eLin;
		double yB2 = Math.sin(-degreeRadians) * -eLin;
		double xB2 = Math.cos(-degreeRadians) * -eLin;
		g2.fillRect((int)(cameraOffsetXPix+xB1     -xB2)-1,(int)(cameraOffsetYPix-yB1     +yB2)-1,3,3);
		g2.fillRect((int)(cameraOffsetXPix+xB2     -xB2)-1,(int)(cameraOffsetYPix-yB2     +yB2)-1,3,3);
		

		drawEllipse(g2, analazysResult.a / pxInMeters, analazysResult.b / pxInMeters, degreeRadians, analazysResult.bodyCenter, -xB2, yB2);
	}
	
	private void drawEllipse(Graphics2D g2, double aPix, double bPix, double degreeRadians, Body bodyCenter, double offsetX, double offsetY) {
		
		double schrittweite = 2;
		int wdh = (int)(4*aPix);
		
	    double ellipseCenterX = cameraOffsetXPix + bodyCenter.x + offsetX;
	    double ellipseCenterY = cameraOffsetYPix + bodyCenter.y + offsetY;

	    int pointStartX = 0, pointStartY = 0;
	    int pointCacheX = 0, pointCacheY = 0;
	    
	    
	    double x = -aPix;
	    boolean rechts = true;
	    for (int i = 0; i < wdh; i++){
	    	
	    	//rechts
	    	if(rechts){
	    		
	    		double xNeu = Math.cos(degreeRadians) * x;
    			double yn1 = (bPix / aPix) * Math.sqrt((aPix * aPix) - (x * x));
    			double ynNeu = Math.sin(degreeRadians) * x;
				double z = Math.cos(degreeRadians) * yn1;
				double u = Math.sin(degreeRadians) * yn1;

				if(i == 0){
					pointCacheX = (int)(xNeu + u + ellipseCenterX);
					pointCacheY = (int)(ynNeu - z + ellipseCenterY);
					
					pointStartX = pointCacheX;
    				pointStartY = pointCacheY;
    				
				}else{
					
					g2.drawLine(pointCacheX, pointCacheY, (int)(xNeu + u + ellipseCenterX), (int)(ynNeu - z + ellipseCenterY));        	  
					pointCacheX = (int)(xNeu + u + ellipseCenterX);
					pointCacheY = (int)(ynNeu - z + ellipseCenterY);
				}
				
				x += schrittweite;
    		
				if(x > aPix) {
					rechts = false;
					x = aPix;
					continue;
				}
	    		
	    	//links
	    	}else{
	    		
	    		
	    		double xNeu = Math.cos(degreeRadians) * x;
    			double yn1 = (bPix / aPix) * Math.sqrt(aPix * aPix - x * x);		         		          
    			double ynNeu = Math.sin(degreeRadians) * x;		          
    			double z = Math.cos(degreeRadians) * yn1;
    			double u = Math.sin(degreeRadians) * yn1;
    			
    			//draw line
				g2.drawLine(pointCacheX, pointCacheY, (int)(xNeu - u + ellipseCenterX), (int)(ynNeu + z + ellipseCenterY));
				pointCacheX = (int)(xNeu - u + ellipseCenterX);
				pointCacheY = (int)(ynNeu + z + ellipseCenterY);	

    			x -= schrittweite;
    			
    			if(x < -aPix)
    				break;
	    	}
	    }
	    g2.drawLine(pointCacheX, pointCacheY, pointStartX, pointStartY);
	}

	private void drawInfoTab(Graphics2D g2, Body body) {
		
		
		
	}
	
//	private final int skalaOffsetX = 
	private void drawSkala(Graphics2D g2) {
		
		double[] zoomLevels = new double[] 
				{1E8, 1E9, 1E10};
		
	}
	
//--------------------------------------------------------------------------------------------------
	//Logic
	private double zoomStrength = 0.8;
	public void zoom(boolean in, int xPix, int yPix) {
		
		
		//1: modify this.pxInMeters
		//2: center camera on stored position
				
		Point2D.Double point = getPositionOnCoordinateSystem(xPix, yPix);
		if(in) {
			this.pxInMeters *= zoomStrength;
		}else {
			this.pxInMeters /= zoomStrength;
//			point = getPositionOnCoordinateSystem(this.getWidth() - xPix, this.getHeight() - yPix);
		}
		this.positionPointAt(point.getX(), point.getY(), xPix, yPix);
//		
//		//this.centerCamera(point.getX(), point.getY());
//		
//		this.positionPointAt(-100E9, -100E9, 0, 0);
	}
	
	public void centerCamera() {
		cameraOffsetXPix = this.getWidth()/2;
		cameraOffsetYPix = this.getHeight()/2;
		this.recalcOffsetMeters();
//		cameraOffsetXMeters = cameraOffsetXPix * pxInMeters;
//		cameraOffsetYMeters = cameraOffsetYPix * pxInMeters;
	}
	
	public void centerCamera(double xMeters, double yMeters) {
		this.cameraOffsetXPix = (int) ((this.getWidth() / 2) - (xMeters / this.pxInMeters));
		this.cameraOffsetYPix = (int) ((this.getHeight() / 2) - (yMeters / this.pxInMeters));
		
		this.recalcOffsetMeters();
//		this.cameraOffsetXMeters = this.cameraOffsetXPix * this.pxInMeters;
//		this.cameraOffsetYMeters = this.cameraOffsetYPix * this.pxInMeters;
	}
	
	public void positionPointAt(double xMeters, double yMeters, int xPixDestination, int yPixDestination) {
		this.cameraOffsetXMeters = (xPixDestination * this.pxInMeters) - xMeters;
		this.cameraOffsetYMeters = (yPixDestination * this.pxInMeters) - yMeters;
		
//		this.cameraOffsetXPix = (int) (this.cameraOffsetXMeters / this.pxInMeters);
//		this.cameraOffsetYPix = (int) (this.cameraOffsetYMeters / this.pxInMeters);
		this.recalcOffsetPix();
	}
	
	public Point2D.Double getPositionOnCoordinateSystem(int xPix, int yPix) {
		return new Point2D.Double(
				(xPix-this.cameraOffsetXPix)*this.pxInMeters,
				(yPix-this.cameraOffsetYPix)*this.pxInMeters
			);
	}
	
	private void recalcOffsetMeters() {
		this.cameraOffsetXMeters = this.cameraOffsetXPix * this.pxInMeters;
		this.cameraOffsetYMeters = this.cameraOffsetYPix * this.pxInMeters;
	}
	
	private void recalcOffsetPix() {
		this.cameraOffsetXPix = (int) (this.cameraOffsetXMeters / this.pxInMeters);
		this.cameraOffsetYPix = (int) (this.cameraOffsetYMeters / this.pxInMeters);
	}
//--------------------------------------------------------------------------------------------------
	//SETTERS
	
	public void setShouldRepaint(boolean shouldRepaint) {
		this.shouldRepaint = shouldRepaint;
	}	
}
