package events;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.Main;

public class EventManager implements KeyListener{
	public static void onBodyListChange() {
		if(Main.getController() != null)
			Main.getController().getWindowOptions().updateBodyList();
	}
	
	public static void onAnalyzationFinish() {
		Main.getController().getWindowOptions().onAnalyzationFinish();
	}
	
	public static void onOptionWindowMdklSChange() {
		Main.getController().setMdklSDelta(Main.getController().getWindowOptions().getSelectedMdklSDelta());
		Main.getController().getWindowOptions().updateLMdklSDelta();
	}
	
	
	//KeyListener
	
	public void keyPressed(KeyEvent e) {
		if(Main.getController() == null)
			return;
		
		switch(e.getKeyCode()) {
		case KeyEvent.VK_F1:
			Main.getController().getWindowOptions().setVisible(true);
			break;
		case KeyEvent.VK_F6:
			Main.getController().getWindowOptions().toggleBackgroundImage();
			break;
		case KeyEvent.VK_F11:
			Main.getController().getWindow().switchFullscreen();
			break;
//		case KeyEvent.VK_F10:			
//			Main.getController().getWindow().setUn();
//			break;
		}		
	}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
}
