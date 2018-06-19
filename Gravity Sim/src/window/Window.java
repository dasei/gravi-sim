package window;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import events.EventManager;
import main.Main;
import physics.Body;

public class Window extends JFrame{
	private static final long serialVersionUID = 335663715638775223L;
	
	//Components:
	private DrawComp dc;
	
	private JButton bPauseResume;
	private ImageIcon iconPause;
	private ImageIcon iconResume;
	
	private JButton bAnalyze;
	
	private boolean isInFullscreenMode = false;
//	private JComboBox<String> optTool;
//	private String[] optToolOptions = {"Place"};
	
	public Window() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		dc = new DrawComp();
		dc.setPreferredSize(new Dimension(500,500));
		dc.setFocusable(true);
		dc.addKeyListener(new EventManager());
		dc.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(e.getWheelRotation() > 0)
					dc.zoom(false, e.getX(), e.getY());
				else if(e.getWheelRotation() < 0)
					dc.zoom(true, e.getX(), e.getY());
			}
		});		
		dc.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isMiddleMouseButton(e))
					dc.startMouseDrag(e);
			}
			public void mouseReleased(MouseEvent e) {
				if(SwingUtilities.isMiddleMouseButton(e))
					dc.stopMouseDrag();
			}			
		});
		
		this.add(dc);
		buildJFrame(false);
		
		startRepaintThread();
	}
	
	private void buildJFrame(boolean fullscreen){
		
		this.setUndecorated(fullscreen);
		if(fullscreen){
			setExtendedState(JFrame.MAXIMIZED_BOTH);			
		}else{			
			setExtendedState(JFrame.NORMAL);
		}		
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public void switchFullscreen(){
		if(!isInFullscreenMode){
			this.isInFullscreenMode = true;
		}else{
			this.isInFullscreenMode = false;		
		}
		this.setVisible(false);
		this.dispose();
		buildJFrame(this.isInFullscreenMode);
	}
	
	private void startRepaintThread() {
		(new Thread() {
			public void run() {
				this.setName("repaint Thread");
				
				Body bodyToFollow;
				while(true) {
					
					if(Main.getController() != null) {
						bodyToFollow = Main.getController().getBodyToFollow();
						if(bodyToFollow != null) {
							dc.centerCamera(bodyToFollow.x, bodyToFollow.y);
						}
					}
					
					dc.repaint();
//					try {
//						Thread.sleep(0);
//					}catch(Exception exc) {}
				}
			}
		}).start();
	}
	
//--------------------------------------------------------------------------------------------------
	// update methods
	
	
	
//	private void setOptToolEnabled(boolean enabled) {
//		this.optTool.setEnabled(enabled);
//	}
	
//--------------------------------------------------------------------------------------------------
	// interaction calls
	
//	public enum InteractionState {VOID, WAITING_FOR_OK};
//	private InteractionState interactionState;
//
//	private void onMouseClick(MouseEvent e) {
//		if(!Main.getController().pause()) 
//			return;
//		
//		switch((String)( optTool.getSelectedItem() )) {
//		case "Place":
//			if(!Main.getController().pause())
//				return;
//			
//			this.interactionState = InteractionState.WAITING_FOR_OK;
//			this.setOptToolEnabled(false);
//			break;		
//		}
//	}
//	
//	private void onMouseRelease(MouseEvent e) {
//		if()
//	}
	
//--------------------------------------------------------------------------------------------------
	// Events
//	public void onAnalyzationFinish() {
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				updatePauseResumeButton();
//			}
//		});
//	}
//--------------------------------------------------------------------------------------------------
	// Getters
	
//	public InteractionState getInteractionState() {
//		return this.interactionState;
//	}
	
	public DrawComp getDrawComp() {
		return this.dc;
	}
}
