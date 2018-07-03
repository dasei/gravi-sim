package physics;

import java.util.ArrayList;

public enum BodySystemTemplate {
	SOLAR_SYSTEM_COMPLETE,
	SOLAR_SYSTEM_LIGHT
	
	;
	
	public ArrayList<Body> getBodies(){
		
		switch(this){
		case SOLAR_SYSTEM_COMPLETE:
			return Templates.loadSolarsystemComplete();			
		case SOLAR_SYSTEM_LIGHT:
			return Templates.loadSolarsystemLight();
		default:
			return Templates.loadSolarsystemComplete();
		}	
	}
}
