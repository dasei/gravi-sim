package physics;
import java.util.ArrayList;

import main.Main;

public class Scenarios {
	public static void loadSolarsystem() {
		Main.bodies = new ArrayList<Body>();
		Main.bodies.add(new Body(5.9E24, 0, -150E9, 30000, 0, false));
		Main.bodies.add(new Body(1.9E30, 0, 0, 0, 0, true));
	}
}
