package main;

import java.util.ArrayList;

import physics.Body;
import physics.Physics;
import physics.Physics.AnalazysResult;
import physics.Templates;
import window.Window;
import window.WindowOptions;

public class Controller {
	private Window window;
	private WindowOptions windowOptions;
	
	private ArrayList<Body> bodies;
	
	private double timeMdklSIterationSeconds = 0.1;
	private long timeLoopSleepMS = 0;
	
	private SimulationState state = SimulationState.SIMULATING;
	
	//Ellipse zeichnen
	private AnalazysResult analazysResult;
	
	//Follow mode
	private Body bodyToFollow;
	
	public Controller() {
		
		
		window = new Window();
		window.getDrawComp().setDrawWithDensity(true);
		
		windowOptions = new WindowOptions();
	
		bodies = Templates.loadSolarsystem();
		
		
//		for(int i = 0; i < 2000000; i++) {
//			Physics.physicsIteration(bodies, timeMdklSIterationSeconds);
//		}
//		System.out.println("woop");
		
		window.getDrawComp().resetPosition();
		startLoop();
	}
	
	private void startLoop() {
		Runnable loop = new Runnable() {
			public void run() {
				
				// --
				
				while(true) {
					
					//do physics
					if(bodies != null) {
						if(state == SimulationState.SIMULATING)
							Physics.physicsIteration(bodies, timeMdklSIterationSeconds);
						else if(state == SimulationState.ANALIZING) {//							
							analazysResult = Physics.runAnalizis(bodies, bodies.get(1), bodies.get(2), timeMdklSIterationSeconds*10);
							state = SimulationState.SIMULATING;
							getWindow().onAnalyzationFinish();
						}
					}
					
					//sleep
					if(timeLoopSleepMS > 0)
						try {
							Thread.sleep(timeLoopSleepMS);
						}catch(Exception e) {							
						}
						
				}
					
				// --
			}
		};
		
		new Thread(loop).start();
	}

//--------------------------------------------------------------------------------------------------
	//Logic
	
	public boolean pauseOrResume() {
		if(this.getSimulationState() == SimulationState.PAUSED)
			this.setSimulationState(SimulationState.SIMULATING);
		else if(this.getSimulationState() == SimulationState.SIMULATING)
			this.setSimulationState(SimulationState.PAUSED);
		else
			return false;
		return true;
	}
	
	/**
	 * @returns if pause was successfull
	 */
	public boolean pause() {
		this.setSimulationState(SimulationState.PAUSED);
		return true;
	}
	
	public boolean startAnalyzation() {
		
		if(!pause())
			return false;
		
		this.setSimulationState(SimulationState.ANALIZING);
		
		return true;
	}
	
	
//--------------------------------------------------------------------------------------------------
	//FOLLOW MODE
	public void follow(Body body) {
		//TODO all
		this.bodyToFollow = body;
	}
	
//--------------------------------------------------------------------------------------------------
	//SETTERS
	
	private void setSimulationState(SimulationState state) {
		this.state = state;		
	}
	
//--------------------------------------------------------------------------------------------------
	//GETTERS
	
	public Window getWindow() {
		return this.window;
	}
	
	public WindowOptions getWindowOptions() {
		return this.windowOptions;
	}
	
	public ArrayList<Body> getBodies(){
		return this.bodies;
	}
	
	public SimulationState getSimulationState() {
		return this.state;
	}
	
	public AnalazysResult getAnalazysResult() {
		return this.analazysResult;
	}
	
	public Body getBodyToFollow() {
		return this.bodyToFollow;
	}
}
