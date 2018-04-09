import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class DrawComp extends JComponent{
	
	private double pxInMeters = 1E9;
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		int cameraOffsetX = this.getWidth()/2;
		int cameraOffsetY = this.getHeight()/2;
		
		Body[] bodies = Main.bodies.toArray(new Body[0]);
		
		for(Body b : bodies) {			
			g2.drawOval(cameraOffsetX + (int)(b.x/pxInMeters) - 5, cameraOffsetY + (int)(b.y/pxInMeters) - 5, 10, 10);			
		}
		
	}
}
