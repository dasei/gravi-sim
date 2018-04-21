package window;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import main.Main;

public class Window extends JFrame{
	private static final long serialVersionUID = 335663715638775223L;
	
	//Components:
	private DrawComp dc;
	
	private JButton bPauseResume;
	private ImageIcon iconPause;
	private ImageIcon iconResume;
	
	private JButton bAnalyze;
	
//	private JComboBox<String> optTool;
//	private String[] optToolOptions = {"Place"};
	
	public Window() {
		loadIcons();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		dc = new DrawComp();
		dc.setPreferredSize(new Dimension(500,500));
		dc.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(e.getWheelRotation() > 0)
					dc.zoom(false, e.getX(), e.getY());
				else if(e.getWheelRotation() < 0)
					dc.zoom(true, e.getX(), e.getY());
			}
		});		
//		dc.addMouseListener(new MouseListener() {
//			public void mouseClicked(MouseEvent e) {}
//			public void mouseEntered(MouseEvent e) {}
//			public void mouseExited(MouseEvent e) {}
//			public void mousePressed(MouseEvent e) {
//				onMouseClick(e);				
//			}
//			public void mouseReleased(MouseEvent e) {
////				onMouseRelease(e);
//			}			
//		});
		this.add(dc);
		
		
		JPanel pBottomBar = new JPanel();
			bPauseResume = new JButton(iconPause);			
			bPauseResume.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Main.getController().pauseOrResume();
					updatePauseResumeButton();
				}
			});
			pBottomBar.add(bPauseResume);
			
			bAnalyze = new JButton("Analyze");
			bAnalyze.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Main.getController().startAnalyzation();
					updatePauseResumeButton();
				}
			});
			pBottomBar.add(bAnalyze);
			
//			optTool = new JComboBox(optToolOptions);
//			optTool.setSelectedItem(optToolOptions[0]);			
//			pBottomBar.add(optTool);
			
		this.add(pBottomBar, BorderLayout.SOUTH);
		
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);		
		
		startRepaintThread();
	}
	
	private void loadIcons() {
		iconPause = new ImageIcon("res/icons/icon_pause.png", "pause");
		iconResume = new ImageIcon("res/icons/icon_resume.png", "resume");		
	}
	
	private void startRepaintThread() {
		(new Thread() {
			public void run() {
				while(true) {
					dc.repaint();
					try {
						Thread.sleep(33);
					}catch(Exception exc) {}
				}
			}
		}).start();
	}
	
//--------------------------------------------------------------------------------------------------
	// update methods
	
	private void updatePauseResumeButton() {
		switch(Main.getController().getSimulationState()) {
		case PAUSED:
			//bPauseResume.setText("resume");
			bPauseResume.setIcon(iconResume);
			bPauseResume.setEnabled(true);
			break;
		case SIMULATING:
//			bPauseResume.setText("pause");
			bPauseResume.setIcon(iconPause);
			bPauseResume.setEnabled(true);
			break;		
		default:
			bPauseResume.setEnabled(false);
			break;
		}
	}
	
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
	public void onAnalyzationFinish() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				updatePauseResumeButton();
			}
		});
	}
//--------------------------------------------------------------------------------------------------
	// Getters
	
//	public InteractionState getInteractionState() {
//		return this.interactionState;
//	}
	
	public DrawComp getDrawComp() {
		return this.dc;
	}
}
