package main;

public class Main {
	
	private static Controller controller;
	
	public static void main(String[] args){		
		controller = new Controller();
		
		//initial update of body list in WindowOptions
		EventManager.onBodyListChange();
	}
	
	public static Controller getController() {
		return controller;
	}
	
	private static long lastGivenID = 0;
	public static long getNewID() {
		return lastGivenID++;
	}
}
