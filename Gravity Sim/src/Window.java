import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends JFrame{
	
	private DrawComp dc;
	
	public Window() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		dc = new DrawComp();
		dc.setPreferredSize(new Dimension(500,500));
		this.add(dc);
		
		
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
//	public void repaint() {
//		dc.repaint();
//	}
	
	public void startRepaintThread() {
		(new Thread() {
			public void run() {
				while(true) {
					dc.repaint();
					try {
						Thread.sleep(10);
					}catch(Exception exc) {}
				}
			}
		}).start();
	}
	
}
