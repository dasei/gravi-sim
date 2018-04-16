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
		

		test(g2, aPix, bPix, degreeRadians, bodyCenter, -xB2, yB2);
//		test(g2, aPix, bPix, degreeRadians, bodyCenter, -0,0);
	}
	
	private void drawEllipse(Graphics2D g2, int aPix, int bPix, double degreeRadians) {
		
		System.out.println("DrawComp.drawEllipse() <-- called");	
		System.out.println("data: aPix:" + aPix + ", bPix:" + bPix + ", degree:" + degreeRadians);
		double x = 0.0D;

		double schrittweite = 1;
			
		boolean rechts = true;
		for (int i = 0; i < 500; i++){
			
			//System.out.println("5");
	 
			if ((x < aPix) && (rechts))
			{
				x += schrittweite;
				if (x > 0.0D)
				{
	     
					double xNeu = Math.cos(degreeRadians) * x;
					double yn1 = (bPix / aPix) * Math.sqrt(aPix * aPix - x * x);		         
					//System.out.println("yn1: "+(b / a * Math.sqrt(a * a - xn * xn)));
					double ynNeu = Math.sin(degreeRadians) * x;		          
					double z = Math.cos(degreeRadians) * yn1;
					double u = Math.sin(degreeRadians) * yn1;		          		          
					g2.drawRect((int)(xNeu - u + cameraOffsetX), (int)(ynNeu + z + cameraOffsetY), 1, 1);
					g2.drawRect((int)(xNeu + u + cameraOffsetX), (int)(ynNeu - z + cameraOffsetY), 1, 1);
	      
				}
				//rechts = true;
			}else{
				rechts = false;
			}
			if ((x > -aPix) && (!rechts)){
				x -= schrittweite;
				if (x < 0.0D){		          
					double xNeu = Math.cos(degreeRadians) * x;		          
					//System.out.println(""+(b / a * Math.sqrt(a * a - xn * xn)));
					double yn1 = (bPix / aPix) * Math.sqrt(aPix * aPix - x * x);		         		          
					double ynNeu = Math.sin(degreeRadians) * x;		          
					double z = Math.cos(degreeRadians) * yn1;
					double u = Math.sin(degreeRadians) * yn1;
					g2.drawRect((int)(xNeu - u + cameraOffsetX), (int)(ynNeu + z + cameraOffsetY), 1, 1);
					g2.drawRect((int)(xNeu + u + cameraOffsetX), (int)(ynNeu - z + cameraOffsetY), 1, 1);
				}
				rechts = false;
			}else{
				rechts = true;
			}
	  			      
		}
			
	}
	
	private void drawFocusPoints(Graphics2D g2, int aPix, int bPix, double degreeRadians) {
		
		double e = Math.sqrt(1-(bPix*bPix)/(aPix*aPix)); 	//numerische Exzentrizität
		double c = aPix*e;							//lineare Exzentrizität
		//System.out.println("e: "+e+"c: "+c);
		double yB1 = Math.sin(Math.toRadians(-degreeRadians)) * c;
		double xB1 = Math.cos(Math.toRadians(-degreeRadians)) * c;
		double yB2 = Math.sin(Math.toRadians(-degreeRadians)) * -c;
		double xB2 = Math.cos(Math.toRadians(-degreeRadians)) * -c;
		g2.fillOval((int)(cameraOffsetX+xB1)-5,(int)(cameraOffsetY-yB1)-5,10,10);
		g2.fillOval((int)(cameraOffsetX+xB2)-5,(int)(cameraOffsetY-yB2)-5,10,10);
		
	}
	
	private void test(Graphics2D g2, double aPix, double bPix, double degreeRadians, Body bodyCenter, double offsetX, double offsetY) {
		
		double degrees = Math.toDegrees(degreeRadians);
		double a = aPix, b = bPix;
		
		//System.out.println("DrawECalled");
	 	double xn = 0.0D;
	    double mitteFensterHorizontal = cameraOffsetX + bodyCenter.x;
	    double mitteFensterVertikal = cameraOffsetY + bodyCenter.y;
//	    g.drawLine(0, this.getHeight() / 2, Main.getWindow().getWidth(), Main.getWindow().getHeight() / 2);
//	    g.drawLine(Main.getWindow().getWidth() / 2-100, 0, Main.getWindow().getWidth() / 2-100, Main.getWindow().getHeight());
//	    System.out.println(", Width: "+Main.getWindow().getDraw().getWidth()+"Height: "+Main.getWindow().getDraw().getHeight());
	    
	    boolean rechts = true;
	    
	    
	    double schrittw1 = 1;
	    
//		if(shouldworkE&&!running)
//	    	{
//			MathemSammlung.shouldworkE = false;
//			running=true;
			for (int i = 0; i < 1000; i++)
			{
				
				//System.out.println("5");
     
		      if ((xn < a) && (rechts))
		      {
		        xn += schrittw1;
		        if (xn > 0.0D)
		        {
		         
		          double xNeu = Math.cos(Math.toRadians(degrees)) * xn;
		          double yn1 = (b / a) * Math.sqrt(a * a - xn * xn);		         
		          //System.out.println("yn1: "+(b / a * Math.sqrt(a * a - xn * xn)));
		          double ynNeu = Math.sin(Math.toRadians(degrees)) * xn;		          
		          double z = Math.cos(Math.toRadians(degrees)) * yn1;
		          double u = Math.sin(Math.toRadians(degrees)) * yn1;		          		          
		          g2.drawOval((int)(xNeu - u + mitteFensterHorizontal + offsetX), (int)(ynNeu + z + mitteFensterVertikal + offsetY), 1, 1);
		          g2.drawOval((int)(xNeu + u + mitteFensterHorizontal + offsetX), (int)(ynNeu - z + mitteFensterVertikal + offsetY), 1, 1);
		          
		        }
		        rechts = true;
		      }
		      else
		      {
		        rechts = false;
		      }
		      if ((xn > -a) && (!rechts))
		      {
		        xn -= schrittw1;
		        if (xn < 0.0D)
		        {		          
		          double xNeu = Math.cos(Math.toRadians(degrees)) * xn;		          
		          //System.out.println(""+(b / a * Math.sqrt(a * a - xn * xn)));
		          double yn1 = (b / a) * Math.sqrt(a * a - xn * xn);		         		          
		          double ynNeu = Math.sin(Math.toRadians(degrees)) * xn;		          
		          double z = Math.cos(Math.toRadians(degrees)) * yn1;
		          double u = Math.sin(Math.toRadians(degrees)) * yn1;
		          g2.drawOval((int)(xNeu - u + mitteFensterHorizontal + offsetX), (int)(ynNeu + z + mitteFensterVertikal + offsetY), 1, 1);
		          g2.drawOval((int)(xNeu + u + mitteFensterHorizontal + offsetX), (int)(ynNeu - z + mitteFensterVertikal + offsetY), 1, 1);
		        }
		        rechts = false;
		      }
		      else
		      {
		        rechts = true;
		      }
		      			      
		    }
//		    running=false;
//		    }
//			
//			System.out.println("FinishedE");
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
