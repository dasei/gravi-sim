package physics;

import java.util.ArrayList;

public enum BodySystemTemplate {
	SOLAR_SYSTEM_COMPLETE,
	SOLAR_SYSTEM_LITE,
	SOLAR_SYSTEM_METEORITE
	
	;
	
	public ArrayList<Body> getBodies(){
		
		switch(this){
		case SOLAR_SYSTEM_COMPLETE:
			return Templates.loadSolarsystemComplete();			
		case SOLAR_SYSTEM_LITE:
			return Templates.loadSolarsystemLite();
		case SOLAR_SYSTEM_METEORITE:
			return Templates.loadSolarsystemMeteorites();
		default:
			return Templates.loadSolarsystemComplete();
		}	
	}
}
