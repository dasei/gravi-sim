package main;

public class EventManager {
	public static void onBodyListChange() {
		if(Main.getController() != null)
			Main.getController().getWindowOptions().updateBodyList();
	}
}
