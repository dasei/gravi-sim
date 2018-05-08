package window;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

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
	
	private boolean drawWithDensity = true;
	
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
		
		this.applyMouseDrag();
		
		drawEllipseWithFocusPoints(g2);
		
		int radiusPix;
		if(bodies != null){
			
			if(drawWithDensity)
				for(Body b : bodies) {
					radiusPix = (int) (b.radiusMeters / this.pxInMeters);
					g2.drawOval(cameraOffsetXPix + (int)(b.x/pxInMeters) - radiusPix, cameraOffsetYPix + (int)(b.y/pxInMeters) - radiusPix, radiusPix*2, radiusPix*2);
				}
			else
				for(Body b : bodies) {
					radiusPix = 5;
					g2.drawOval(cameraOffsetXPix + (int)(b.x/pxInMeters) - radiusPix, cameraOffsetYPix + (int)(b.y/pxInMeters) - radiusPix, radiusPix*2, radiusPix*2);
				}
			
		}
		
		this.drawScale(g2);
		
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
		
		double centerX = cameraOffsetXPix - xB2 + (analazysResult.bodyCenter.x / this.pxInMeters);		
		double centerY = cameraOffsetYPix + yB2 + (analazysResult.bodyCenter.y / this.pxInMeters);
		
//		g2.fillRect((int)(cameraOffsetXPix+xB1     -xB2)-1,(int)(cameraOffsetYPix-yB1     +yB2)-1,3,3);
//		g2.fillRect((int)(cameraOffsetXPix+xB2     -xB2)-1,(int)(cameraOffsetYPix-yB2     +yB2)-1,3,3);

		g2.fillRect((int) (centerX + xB1) - 1, (int) (centerY - yB1) - 1, 3, 3);
		g2.fillRect((int) (centerX + xB2) - 1, (int) (centerY - yB2) - 1, 3, 3);

		

		drawEllipse(g2, analazysResult.a / pxInMeters, analazysResult.b / pxInMeters, degreeRadians, analazysResult.bodyCenter, centerX, centerY);
	}
	
	private void drawEllipse(Graphics2D g2, double aPix, double bPix, double degreeRadians, Body bodyCenter, double centerX, double centerY) {
		
		g2.setColor(Color.GRAY);
		
		double schrittweite = 2;
		int wdh = (int)(4*aPix);
		
//		double ellipseCenterX = cameraOffsetXPix + offsetX;
//		double ellipseCenterY = cameraOffsetYPix + offsetY;

		double ellipseCenterX = centerX;
		double ellipseCenterY = centerY;

		
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

	
	private final int scaleOffsetX = 50;
	private final int scaleOffsetY = 50;
	private final int scaleBarThickness = 5;
	private volatile String scaleText;
	private volatile int scaleLengthPix;
	private void recalcScale(Graphics2D g2) {
		
		////calculate scale line length
		
		double scalePix = 1 / this.pxInMeters;
		int scalePixMin = 40;
		while(true) {
			
			//if scale is too small, increase it
			if(scalePix < scalePixMin)
				scalePix *= 5;
			else
				break;
			
			//if scale is too small, increase it
			if(scalePix < scalePixMin)
				scalePix *= 2;
			else
				break;			
		}
		this.scaleLengthPix = (int) scalePix;		
		
		
		////calculate scale text
		double scaleMeters = scalePix * this.pxInMeters;
		String scaleRaw = String.valueOf(scaleMeters);
		
		//first number:
		String firstNumber;
		int zeros;
		if(scaleRaw.startsWith("4") || scaleRaw.startsWith("5"))
			firstNumber = "5";			
		else 
			firstNumber = "1";
				
		
		//amount of zeros:
		zeros = scaleRaw.indexOf(".") - 1;
		
		if(scaleRaw.startsWith("9"))
			zeros++;
		
		//unit:
		int exp = zeros;
		String[] scaleRawSplit = scaleRaw.split("E");
		if(scaleRawSplit.length == 2)
			exp += Integer.valueOf(scaleRawSplit[1]);
		
		//System.out.println("exp only calc: " + (int) Math.log10(scaleMeters));
		
		
		String scaleUnit;		
		if(exp < 3) {
			scaleUnit = " m";
		}else if(exp < 6) {
			scaleUnit = " km";
		}else if(exp < 9) {
			scaleUnit = " 000 km";
		}else if(exp < 12) {
			scaleUnit = " Mio km";
		}else if(exp < 15) {
			scaleUnit = " Mrd km";
		}else if(exp < 18) {
			scaleUnit = " Billio km";
		}else if(exp < 21) {
			scaleUnit = " Billia km";
		}else {
			scaleUnit = " too big to display xD";
		}
		
		//append everything
		String scaleText = firstNumber;
		for(int i = 0; i < exp % 3; i++)
			scaleText += "0";
		scaleText += scaleUnit;
		
		this.scaleText = scaleText;	
	
	}
	
	private void drawScale(Graphics2D g2) {
		if(scaleText == null)
			recalcScale(g2);
		
		g2.drawRect(this.getWidth() - scaleOffsetX - (int) this.scaleLengthPix, this.getHeight() - scaleOffsetY - this.scaleBarThickness, (int) this.scaleLengthPix, this.scaleBarThickness);
		
		g2.drawString(
				this.scaleText, 
				this.getWidth() - this.scaleOffsetX - SwingUtilities.computeStringWidth(g2.getFontMetrics(), this.scaleText),
				this.getHeight() - this.scaleOffsetY - this.scaleBarThickness - g2.getFont().getSize()
			);
		
	}
	
//--------------------------------------------------------------------------------------------------
	//Logic
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
	//MOVEMENT
	private double zoomStrength = 0.9D;
	public void zoom(boolean in, int xPix, int yPix) {
		
		Point2D.Double point = getPositionOnCoordinateSystem(xPix, yPix);
		if(in) {
			this.pxInMeters *= zoomStrength;
		}else {
			this.pxInMeters /= zoomStrength;
		}
		
		if(this.pxInMeters < 1)
			this.pxInMeters = 1;
		
		this.positionPointAt(point.getX(), point.getY(), xPix, yPix);
		
		this.recalcScale((Graphics2D) this.getGraphics());
	}
	
	
	private volatile boolean currentlyMouseDragging = false;
	private double mouseDragStartMetersX;
	private double mouseDragStartMetersY;
	public void startMouseDrag(MouseEvent e) {
		Point2D.Double point = this.getPositionOnCoordinateSystem(e.getX(), e.getY());
		this.mouseDragStartMetersX = point.getX();
		this.mouseDragStartMetersY = point.getY();
		this.currentlyMouseDragging = true;
	}
	
	public void stopMouseDrag() {
		this.currentlyMouseDragging = false;
	}
	
	private void applyMouseDrag() {
		if(!this.currentlyMouseDragging)
			return;
		
		Point mousePos = this.getMousePosition();
		if(mousePos != null)
			this.positionPointAt(this.mouseDragStartMetersX, this.mouseDragStartMetersY, mousePos.x, mousePos.y);
	}
	
//--------------------------------------------------------------------------------------------------
	//SETTERS
	
	public void setShouldRepaint(boolean shouldRepaint) {
		this.shouldRepaint = shouldRepaint;
	}
	
	public void setDrawWithDensity(boolean drawWithDensity){
		this.drawWithDensity = drawWithDensity;
	}
}
