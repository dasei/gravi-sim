package window;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
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
	
	public static enum ColorPreset{
		LIGHT(new Color(255, 255, 255), new Color(96, 101, 109), new Color(0, 0, 0)), 
		DARK(new Color(0, 0, 0), new Color(96, 101, 109), new Color(255, 255, 255)),
		RED(new Color(0, 0, 0), new Color(96, 101, 109), new Color(209, 0, 0));
		
		public final Color background;
		public final Color midground;
		public final Color foreground;
		private ColorPreset(Color background, Color midground, Color foreground) {
			this.background = background;
			this.midground = midground;
			this.foreground = foreground;
		}
	}
	
//	private double pxInMeters = 100000;
	private double pxInMeters = 1E9;
	
	private boolean shouldRepaint = true;
	
	private int cameraOffsetXPix;
	private double cameraOffsetXMeters;
	private int cameraOffsetYPix;
	private double cameraOffsetYMeters;
	
	private int lastWidth;
	private int lastHeight;
	
	//draw options
	public static final boolean drawBodyInfoTagsOnDefault = true;
	private boolean drawBodyInfoTags = drawBodyInfoTagsOnDefault;
	public static final boolean drawFocusPointsOnDefault = true;
	private boolean drawFocusPoints = drawFocusPointsOnDefault;
	public static final boolean drawWithDensityOnDefault = true;
	private boolean drawWithDensity = drawWithDensityOnDefault;
	public static final boolean drawGIFsOnDefault = true;
	private boolean drawGIFs = drawGIFsOnDefault;
	public static final boolean drawBodyOutlineOnDefault = true;
	private boolean drawBodyOutline = drawBodyOutlineOnDefault;
	public static final boolean drawEllipseOnDefault = true;
	private boolean drawEllipse = drawEllipseOnDefault;
	
	public static final float defaultDrawObjectScaleFactor = 1;
	private float drawObjectScaleFactor = defaultDrawObjectScaleFactor;
	public static final float defaultDrawInfoTagScaleFactor = 1;
	private float drawInfoTagScaleFactor = defaultDrawInfoTagScaleFactor;
	public static final int defaultDrawEllipseThickness = 1;
	private BasicStroke drawEllipseStroke = new BasicStroke(defaultDrawEllipseThickness);
	
	public static final BasicStroke defaultStroke = new BasicStroke(1);
	
	private Controller controller;
	
	private final ColorPreset defaultColorPreset = ColorPreset.DARK;
	private Color colorBackground;
	private Color colorMidground;
	private Color colorForeground;
	
	public static final int FPSMAX = 60;
	private long tmpTimeStart;
	
	public static final Font fontScaleDefault = new Font("Dialog", Font.PLAIN, 12);
	public static final Font fontInfoTagsDefault = new Font("Dialog", Font.PLAIN, 12);
	private Font fontInfoTags = fontInfoTagsDefault;
	
	public DrawComp() {
		this.setOpaque(true);
		loadColorPreset(defaultColorPreset);		
	}
	
	public void paintComponent(Graphics g) {
		tmpTimeStart = System.currentTimeMillis();
		
		if(!shouldRepaint)
			return;
		Graphics2D g2 = (Graphics2D) g;
		
		if(controller == null) {
			controller = Main.getController();
			if(controller == null)
				return;
		}
		ArrayList<Body> bodies = controller.getBodies();
		
		////// all offset-modifying related things
			
		//modify offset if drawcomp was resized
		if(this.getWidth() != this.lastWidth || this.getHeight() != this.lastHeight) {
			this.centerCamera((this.lastWidth/2 - this.cameraOffsetXPix) * this.pxInMeters, (this.lastHeight/2 - this.cameraOffsetYPix) * this.pxInMeters);
		}
		
		this.applyMouseDrag();
		
		
		//draw background
		g2.setColor(colorBackground);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		drawEllipseWithFocusPoints(g2);
		
		//draw bodies /w infotags
		int radiusPix;
		if(bodies != null){
			g2.setColor(colorForeground);
			
			if(drawWithDensity) {
				float radiusWithPaddingPix;
				for(Body b : bodies) {
					radiusPix = (int) ((b.radiusMeters / this.pxInMeters) * this.drawObjectScaleFactor);
					if(!isBodyVisibleOnScreen(b, radiusPix))
						continue;
					
					if(this.drawGIFs && b.getGIF() != null) {
						radiusWithPaddingPix = (1 - b.getGIFPadding()) * radiusPix;
						if(radiusWithPaddingPix > 2) {					
							g2.drawImage(
									b.getGIF(), 
									(int) (cameraOffsetXPix + (b.x/pxInMeters) - (radiusWithPaddingPix)),
									(int) (cameraOffsetYPix + (b.y/pxInMeters) - (radiusWithPaddingPix)), 
									(int) (radiusWithPaddingPix * 2) + 1,
									(int) (radiusWithPaddingPix * 2) + 1,
									null);
						}
					}
					
					if(this.drawBodyOutline)
						g2.drawOval(cameraOffsetXPix + (int)(b.x/pxInMeters) - radiusPix, cameraOffsetYPix + (int)(b.y/pxInMeters) - radiusPix, radiusPix*2, radiusPix*2);
				}
			}else {
				radiusPix = (int)(5 * this.drawObjectScaleFactor);
				float radiusWithPaddingPix;
				for(Body b : bodies) {
					if(!isBodyVisibleOnScreen(b, radiusPix))
						continue;
					
					if(this.drawGIFs && b.getGIF() != null) {
						radiusWithPaddingPix = (1 - b.getGIFPadding()) * radiusPix;
											
						g2.drawImage(
								b.getGIF(), 
								(int) (cameraOffsetXPix + (b.x/pxInMeters) - (radiusWithPaddingPix)),
								(int) (cameraOffsetYPix + (b.y/pxInMeters) - (radiusWithPaddingPix)), 
								(int) (radiusWithPaddingPix * 2) + 1,
								(int) (radiusWithPaddingPix * 2) + 1,
								null);
					}
					
					if(this.drawBodyOutline)
						g2.drawOval(cameraOffsetXPix + (int)(b.x/pxInMeters) - radiusPix, cameraOffsetYPix + (int)(b.y/pxInMeters) - radiusPix, radiusPix*2, radiusPix*2);
				}
			}
			
			//recalc body info tag positions
			if(drawBodyInfoTags) {
				//TODO only re instantiate font if this.drawInfoTagScaleFactor has changed since last draw				
				fontInfoTags = new Font( fontInfoTagsDefault.getName(), fontInfoTagsDefault.getStyle(), (int) (fontInfoTagsDefault.getSize() * this.drawInfoTagScaleFactor));
				
				recalcBodyInfoTags(g, bodies);
				drawBodyInfoTags(g, bodies);
			}
		}
		

		this.drawScale(g2);
		
		
		//save last width & height, to notice when component was resized (also maximized or minimized)
		// => max-/ minimizing the window will not be noticed by a ComponentListener 
		lastWidth = this.getWidth();
		lastHeight = this.getHeight();
		
		
		//wait, so that MAXFPS wont be overstepped
		long tmpTimeTaken = System.currentTimeMillis() - tmpTimeStart;
		if(tmpTimeTaken < 1000/FPSMAX)
			try {
				Thread.sleep(1000/FPSMAX - tmpTimeTaken);
			}catch(Exception e) {
			}
	}
	
	private void drawEllipseWithFocusPoints(Graphics2D g2) {
		if(!drawEllipse && !drawFocusPoints)
			return;
		
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
		
		
		if(drawFocusPoints) {
			g2.setColor(colorMidground);
			
			g2.fillRect((int) (centerX + xB1) - 1, (int) (centerY - yB1) - 1, 3, 3);
			g2.fillRect((int) (centerX + xB2) - 1, (int) (centerY - yB2) - 1, 3, 3);
		}

		if(drawEllipse) {
			drawEllipse(g2, analazysResult.a / pxInMeters, analazysResult.b / pxInMeters, degreeRadians, analazysResult.bodyCenter, centerX, centerY);
		}
	}
	
	private void drawEllipse(Graphics2D g2, double aPix, double bPix, double degreeRadians, Body bodyCenter, double centerX, double centerY) {
		
		//width of ellipse
		g2.setStroke(drawEllipseStroke);
		
		g2.setColor(colorMidground);
		
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
	    
	    //reset stroke
	    g2.setStroke(defaultStroke);
	}

	/**
	 * recalculates the position of all info tags to be drawn
	 */
	private void recalcBodyInfoTags(Graphics g, ArrayList<Body> bodies){
		
		g.setFont(fontInfoTags);
		
		Point bodyPos;
		for(Body body : bodies){
			
			//get Position of body
			bodyPos = getPositionOnCoordinateSystemInPixels(body.x, body.y);
			
			body.infoTagXPix = bodyPos.x;
			body.infoTagYPix = bodyPos.y;
			body.infoTagWidthPix = SwingUtilities.computeStringWidth(g.getFontMetrics(), body.getName());
			body.infoTagFontSize = g.getFont().getSize();
			body.infoTagHeightPix = body.infoTagFontSize + 4;
		}
	}
	
	private void drawBodyInfoTags(Graphics g, ArrayList<Body> bodies){
		
		g.setFont(fontInfoTags);
		
		for(Body body : bodies){
			//Background (background rectangle)
			g.setColor(colorBackground);
			g.fillRect(body.infoTagXPix + 10, body.infoTagYPix + 10, body.infoTagWidthPix + 4, body.infoTagHeightPix);
			
			//Midground (text, line and border)
			g.setColor(colorMidground);
			g.drawString(body.getName(), body.infoTagXPix + 10 + 2, body.infoTagYPix + 10 + body.infoTagFontSize);
			g.drawLine(body.infoTagXPix, body.infoTagYPix, body.infoTagXPix + 10, body.infoTagYPix + 10);
			g.drawRect(body.infoTagXPix + 10, body.infoTagYPix + 10, body.infoTagWidthPix + 4, body.infoTagHeightPix);
		}
	}
	
	private final int scaleOffsetX = 50;
	private final int scaleOffsetY = 50;
	private final int scaleBarThickness = 5;
	private volatile String scaleText;
	private volatile int scaleLengthPix;
	/**
	 * refresht sozusagen die Größenskala
	 */
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
		
		g2.setColor(Color.DARK_GRAY);		
		g2.drawRect(this.getWidth() - scaleOffsetX - (int) this.scaleLengthPix, this.getHeight() - scaleOffsetY - this.scaleBarThickness, (int) this.scaleLengthPix, this.scaleBarThickness);
		
		
		g2.setFont(fontScaleDefault);		
		g2.drawString(
				this.scaleText, 
				this.getWidth() - this.scaleOffsetX - SwingUtilities.computeStringWidth(g2.getFontMetrics(), this.scaleText),
				this.getHeight() - this.scaleOffsetY - this.scaleBarThickness - g2.getFont().getSize()
			);
		
	}
	
//--------------------------------------------------------------------------------------------------
	//Logic
	public void centerCamera() {
		this.centerCamera(0, 0);		
	}

	
	public void centerCamera(double xMeters, double yMeters) {
		this.positionPointAt(xMeters, yMeters, (this.getWidth() / 2), (this.getHeight() / 2));
	}
	
	public void positionPointAt(double xMeters, double yMeters, int xPixDestination, int yPixDestination) {
		this.cameraOffsetXMeters = (xPixDestination * this.pxInMeters) - xMeters;
		this.cameraOffsetYMeters = (yPixDestination * this.pxInMeters) - yMeters;

		this.recalcOffsetPix();
		this.onCameraPosChanged();
	}
	
	public void resetPosition() {
		this.cameraOffsetXPix = 0;
		this.cameraOffsetYPix = 0;
		this.recalcOffsetMeters();
	}
	
	public Point2D.Double getPositionOnCoordinateSystemInMeters(int xPix, int yPix) {
		return new Point2D.Double(
				(double) (xPix - this.cameraOffsetXPix) * this.pxInMeters,
				(double) (yPix - this.cameraOffsetYPix) * this.pxInMeters
			);
	}
	
	public Point getPositionOnCoordinateSystemInPixels(double xMeters, double yMeters) {
		return new Point(
				(int) (xMeters/this.pxInMeters) + this.cameraOffsetXPix,
				(int) (yMeters/this.pxInMeters) + this.cameraOffsetYPix
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
	
	private boolean isBodyVisibleOnScreen(Body body, int radiusPix) {
		Point position = getPositionOnCoordinateSystemInPixels(body.x, body.y);
		
		return	position.x + radiusPix > 0 
				&& position.x - radiusPix < this.getWidth() 
				&& position.y + radiusPix > 0 
				&& position.y - radiusPix < this.getHeight();   
		}
	
//--------------------------------------------------------------------------------------------------
	//MOVEMENT
	private double zoomStrength = 0.9D;
	public void zoom(boolean in, int xPix, int yPix) {
		
		Point2D.Double point = getPositionOnCoordinateSystemInMeters(xPix, yPix);
		if(in) {
			this.pxInMeters *= zoomStrength;
		}else {
			this.pxInMeters /= zoomStrength;
		}
		
		if(this.pxInMeters < 1)
			this.pxInMeters = 1;
		
		this.positionPointAt(point.getX(), point.getY(), xPix, yPix);
		
		onZoomChanged();
	}
	
	
	//--------------------------------------------------------------------------------------------------
		//State-Changed methods
	public void onZoomChanged(){
		this.recalcScale((Graphics2D) this.getGraphics());		
	}
	
	public void onCameraPosChanged(){
		
	}	
	
	private volatile boolean currentlyMouseDragging = false;
	private double mouseDragStartMetersX;
	private double mouseDragStartMetersY;
	public void startMouseDrag(MouseEvent e) {
		Point2D.Double point = this.getPositionOnCoordinateSystemInMeters(e.getX(), e.getY());
		this.mouseDragStartMetersX = point.getX();
		this.mouseDragStartMetersY = point.getY();
		this.currentlyMouseDragging = true;
	}
	
	private void applyMouseDrag() {
		if(!this.currentlyMouseDragging)
			return;
		
		Point mousePos = this.getMousePosition();
		if(mousePos != null)
			this.positionPointAt(this.mouseDragStartMetersX, this.mouseDragStartMetersY, mousePos.x, mousePos.y);
	}
	
	public void stopMouseDrag() {
		this.currentlyMouseDragging = false;
	}
	
	public String toString() {
		return "dc: camOffXPx:" + cameraOffsetXPix + ", camOffYPx:" + cameraOffsetYPix;
	}
	
	
//--------------------------------------------------------------------------------------------------
	//COLORS
	public void loadColorPreset(ColorPreset colorPreset) {
		this.colorBackground = colorPreset.background;
		this.colorMidground = colorPreset.midground;
		this.colorForeground = colorPreset.foreground;
	}
	
	public void loadDefaultColorPreset() {
		loadColorPreset(this.defaultColorPreset);
	}
	
//--------------------------------------------------------------------------------------------------
	//GETTERS
	public Color getColorBackground() {
		return colorBackground;
	}

	public Color getColorMidground() {
		return colorMidground;
	}

	public Color getColorForeground() {
		return colorForeground;
	}
	
	
//--------------------------------------------------------------------------------------------------
	//SETTERS
	
	public void setShouldRepaint(boolean shouldRepaint) {
		this.shouldRepaint = shouldRepaint;
	}
	
	public void setDrawWithDensity(boolean drawWithDensity){
		this.drawWithDensity = drawWithDensity;
	}
	
	public void setColorForeground(Color c) {
		this.colorForeground = c;
	}
	
	public void setColorMidground(Color c) {
		this.colorMidground = c;
	}
	
	public void setColorBackground(Color c) {
		this.colorBackground = c;
	}
	
	public void setDrawBodyInfoTags(boolean drawBodyInfoTags) {
		this.drawBodyInfoTags = drawBodyInfoTags;
	}
	
	public void setDrawFocusPoints(boolean drawFocusPoints) {
		this.drawFocusPoints = drawFocusPoints;
	}
	
	public void setDrawObjectScaleFactor(float factor) {
		this.drawObjectScaleFactor = factor;
	}
	
	public void setDrawGIFs(boolean drawGIFs) {
		this.drawGIFs = drawGIFs;
	}
	
	public void setDrawBodyOutline(boolean drawBodyOutline) {
		this.drawBodyOutline = drawBodyOutline;
	}
	
	public void setDrawInfoTagScaleFactor(float drawInfoTagScaleFactor) {
		this.drawInfoTagScaleFactor = drawInfoTagScaleFactor;
	}
	
	public void setDrawEllipseThickness(int drawEllipseThickness) {
		this.drawEllipseStroke = new BasicStroke(drawEllipseThickness);
	}
	
	public void setDrawEllipse(boolean drawEllipse) {
		this.drawEllipse = drawEllipse;
	}
}
