package physics;

import java.util.ArrayList;

public enum BodySystemTemplate {
	SOLAR_SYSTEM_COMPLETE,
	SOLAR_SYSTEM_LITE
	
	;
	
	public ArrayList<Body> getBodies(){
		
		switch(this){
		case SOLAR_SYSTEM_COMPLETE:
			return Templates.loadSolarsystemComplete();			
		case SOLAR_SYSTEM_LITE:
			return Templates.loadSolarsystemLite();
		default:
			return Templates.loadSolarsystemComplete();
		}	
	}
}
