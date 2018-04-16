package main;

public class Main {
	
	private static Controller controller;
	
	public static void main(String[] args){		
		controller = new Controller();
	}
	
	public static Controller getController() {
		return controller;
	}
	
}
