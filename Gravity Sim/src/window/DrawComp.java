package window;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	
	private int cameraOffsetX;
	private int cameraOffsetY;
	
	public void paintComponent(Graphics g) {
		if(!shouldRepaint)
			return;
		Graphics2D g2 = (Graphics2D) g;
		
		cameraOffsetX = this.getWidth()/2;
		cameraOffsetY = this.getHeight()/2;
		
		Controller controller = Main.getController();
		if(controller == null)
			return;
		ArrayList<Body> bodies = controller.getBodies();
		
		if(bodies != null)
			for(Body b : bodies) 
				g2.drawOval(cameraOffsetX + (int)(b.x/pxInMeters) - 5, cameraOffsetY + (int)(b.y/pxInMeters) - 5, 10, 10);
		
		drawEllipseWithFocusPoints(g2);
	}
	
	private void drawEllipseWithFocusPoints(Graphics2D g2) {
		
		AnalazysResult analazysResult = Main.getController().getAnalazysResult();
		
		if(analazysResult == null)
			return;
		
		double aPix = analazysResult.a / pxInMeters;
		double bPix = analazysResult.b / pxInMeters;
		double degreeRadians = -analazysResult.maxDistanceDegree + (Math.PI/2);
		double eLin = analazysResult.eLin / pxInMeters;
		Body bodyCenter = analazysResult.bodyCenter;
		
//		drawEllipse(g2, aPix, bPix, degreeRadians);
		//drawFocusPoints(g2, aPix, bPix, degreeRadians);
//		testBrennpunkte(g2, aPix, bPix, degreeRadians, eLin);
		
//		System.out.println("eLin archived: " + eLin);
		
		
//		double e = Math.sqrt(1-(bPix*bPix)/(aPix*aPix)); 	//numerische Exzentrizität
//		eLin = aPix*e;							//lineare Exzentrizität
		
		//eLin = Math.sqrt((aPix*aPix) - (bPix*bPix));
		
		//zeichnen der Brennpunkte
		double yB1 = Math.sin(-degreeRadians) * eLin;
		double xB1 = Math.cos(-degreeRadians) * eLin;
		double yB2 = Math.sin(-degreeRadians) * -eLin;
		double xB2 = Math.cos(-degreeRadians) * -eLin;
		g2.fillRect((int)(cameraOffsetX+xB1     -xB2)-2,(int)(cameraOffsetY-yB1     +yB2)-2,5,5);
		g2.fillRect((int)(cameraOffsetX+xB2     -xB2)-2,(int)(cameraOffsetY-yB2     +yB2)-2,5,5);
		

		drawEllipse(g2, aPix, bPix, degreeRadians, bodyCenter, -xB2, yB2);
//		test(g2, aPix, bPix, degreeRadians, bodyCenter, -0,0);
	}
	
	private void drawEllipse(Graphics2D g2, double aPix, double bPix, double degreeRadians, Body bodyCenter, double offsetX, double offsetY) {
		
//		double degrees = Math.toDegrees(degreeRadians);
		
		
	 	double x = 0.0D;
	    double mitteFensterHorizontal = cameraOffsetX + bodyCenter.x;
	    double mitteFensterVertikal = cameraOffsetY + bodyCenter.y;

	    boolean rechts = true;
	    double schrittw1 = 1;
	    
	    for (int i = 0; i < 1000; i++){
		
	    	if ((x < aPix) && rechts){
	    		x += schrittw1;
	    		
	    		if (x > 0.0D){         
					double xeu = Math.cos(degreeRadians) * x;
					double yn1 = (bPix / aPix) * Math.sqrt(aPix * aPix - x * x);		         

					double ynNeu = Math.sin(degreeRadians) * x;		          
					double z = Math.cos(degreeRadians) * yn1;
					double u = Math.sin(degreeRadians) * yn1;		          		          
					g2.drawOval((int)(xeu - u + mitteFensterHorizontal + offsetX), (int)(ynNeu + z + mitteFensterVertikal + offsetY), 1, 1);
					g2.drawOval((int)(xeu + u + mitteFensterHorizontal + offsetX), (int)(ynNeu - z + mitteFensterVertikal + offsetY), 1, 1);
      
	    		}
	    		
	    		if(rechts != true)
	    			System.out.println("rechts = true");
//	    		rechts = true;
	    	}
	    	else
	    	{
	    		rechts = false;
	    	}
	    	if ((x > -aPix) && !rechts)
	    	{
	    		x -= schrittw1;
	    		if (x < 0.0D)
	    		{		          
	    			double xeu = Math.cos(degreeRadians) * x;		          
  //	System.out.println(""+(b / a * Math.sqrt(a * a - x * x)));
	    			double yn1 = (bPix / aPix) * Math.sqrt(aPix * aPix - x * x);		         		          
	    			double ynNeu = Math.sin(degreeRadians) * x;		          
	    			double z = Math.cos(degreeRadians) * yn1;
	    			double u = Math.sin(degreeRadians) * yn1;
	    			g2.drawOval((int)(xeu - u + mitteFensterHorizontal + offsetX), (int)(ynNeu + z + mitteFensterVertikal + offsetY), 1, 1);
	    			g2.drawOval((int)(xeu + u + mitteFensterHorizontal + offsetX), (int)(ynNeu - z + mitteFensterVertikal + offsetY), 1, 1);
	    		}
	    		if(rechts != false)
	    			System.out.println("rechts = false");
//	    		rechts = false;
	    	}	
	    	else
	    	{
	    		rechts = true;
	    	}
	    }
	}
	
	public void testBrennpunkte(Graphics2D g2, double aPix, double bPix, double degreeRadians, double eLinPix)
	{
//		double a = aPix, b = bPix;
		
//		degrees = Math.toDegrees(degrees);
		
//		double mitteFensterHorizontal = cameraOffsetX;
//		double mitteFensterVertikal = cameraOffsetY;
//		double e = Math.sqrt(1-(b*b)/(a*a)); 	//numerische Exzentrizität
//		double c = a*e;							//lineare Exzentrizität
//		//System.out.println("e: "+e+"c: "+c);
//		double yB1 = Math.sin(Math.toRadians(-degrees)) * c;
//		double xB1 = Math.cos(Math.toRadians(-degrees)) * c;
//		double yB2 = Math.sin(Math.toRadians(-degrees)) * -c;
//		double xB2 = Math.cos(Math.toRadians(-degrees)) * -c;
//		g2.fillOval((int)(mitteFensterHorizontal+xB1)-5,(int)(mitteFensterVertikal-yB1)-5,10,10);
//		g2.fillOval((int)(mitteFensterHorizontal+xB2)-5,(int)(mitteFensterVertikal-yB2)-5,10,10);
		
//		double mitteFensterHorizontal = cameraOffsetX;
//		double mitteFensterVertikal = cameraOffsetY;
//		double e = Math.sqrt(1-(bPix*bPix)/(aPix*aPix)); 	//numerische Exzentrizität
//		double c = aPix*e;							//lineare Exzentrizität
		
//		double eLin = Math.sqrt((aPix*aPix) - (bPix*bPix));
		
//		//System.out.println("e: "+e+"c: "+c);
//		double yB1 = Math.sin(-degreeRadians) * c;
//		double xB1 = Math.cos(-degreeRadians) * c;
//		double yB2 = Math.sin(-degreeRadians) * -c;
//		double xB2 = Math.cos(-degreeRadians) * -c;
//		g2.fillOval((int)(cameraOffsetX+xB1)-5,(int)(cameraOffsetY-yB1)-5,10,10);
//		g2.fillOval((int)(cameraOffsetX+xB2)-5,(int)(cameraOffsetY-yB2)-5,10,10);
		double yB1 = Math.sin(-degreeRadians) * eLinPix;
		double xB1 = Math.cos(-degreeRadians) * eLinPix;
		double yB2 = Math.sin(-degreeRadians) * -eLinPix;
		double xB2 = Math.cos(-degreeRadians) * -eLinPix;
		g2.fillOval((int)(cameraOffsetX+xB1)-5,(int)(cameraOffsetY-yB1)-5,10,10);
		g2.fillOval((int)(cameraOffsetX+xB2)-5,(int)(cameraOffsetY-yB2)-5,10,10);		
	}
	
//--------------------------------------------------------------------------------------------------
	//SETTERS
	
	public void setShouldRepaint(boolean shouldRepaint) {
		this.shouldRepaint = shouldRepaint;
	}
}
