package main;

import java.util.ArrayList;

import physics.Body;
import physics.Physics;
import physics.Physics.AnalazysResult;
import physics.Templates;
import window.Window;

public class Controller {
	private Window window;
	
	private ArrayList<Body> bodies;
	
	private double timeMdklSIterationSeconds = 0.1;
	private long timeLoopSleepMS = 0;
	
	private SimulationState state = SimulationState.SIMULATING;
	
	//Ellipse zeichnen
	private AnalazysResult analazysResult;
	
	public Controller() {
		
		
		window = new Window();
	
		bodies = Templates.loadSolarsystem();
		window.getDrawComp().setDrawWithDensity(true);
		
//		for(int i = 0; i < 2000000; i++) {
//			Physics.physicsIteration(bodies, timeMdklSIterationSeconds);
//		}
//		System.out.println("woop");
		
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
	//SETTERS
	
	private void setSimulationState(SimulationState state) {
		this.state = state;		
	}
	
//--------------------------------------------------------------------------------------------------
	//GETTERS
	
	public Window getWindow() {
		return this.window;
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
}
