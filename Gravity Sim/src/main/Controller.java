package main;

import java.util.ArrayList;

import events.EventManager;
import physics.Body;
import physics.BodySystemTemplate;
import physics.Physics;
import physics.Physics.AnalazysResult;
import physics.Templates;
import window.Window;
import window.WindowOptions;

public class Controller {
	private Window window;
	private WindowOptions windowOptions;
	
	private ArrayList<Body> bodies;	
	
	public static final double defaultTimeMdklSDelta = 0.1;
	private double timeMdklSIterationSeconds = defaultTimeMdklSDelta;
	private long timeLoopSleepMS = 0;
	
	private SimulationState state = SimulationState.SIMULATING;
	
	//Ellipse zeichnen
	//private AnalazysResult analazysResult; => Bodies
	private Body[] bodiesToAnalyze;
	
	
	//Follow mode
	private Body bodyToFollow;
	
	public Controller() {
		
		
		window = new Window();
		
		windowOptions = new WindowOptions();
	
		System.out.print("loading template... ");
		bodies = Templates.loadSolarsystemComplete();
		System.out.println("finished");
		
		window.getDrawComp().positionPointAt(0, 0, 0, 0);
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
							EventManager.onAnalyzationStart();
							for(Body b : bodiesToAnalyze){
								if(b.getCenterBody() == null)
									continue;
								
								b.setAnalysisResult(Physics.runAnalizis(bodies, b.getCenterBody(), b, timeMdklSIterationSeconds*1000));
							}
							
							state = SimulationState.SIMULATING;
							EventManager.onAnalyzationFinish();
						}else if(state == SimulationState.PAUSED)
							try {
								Thread.sleep(10);
							}catch(Exception e) {}
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
		
		new Thread(loop, "Physics Thread").start();
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
	
//	public boolean startAnalyzation() {
//		
//		if(!pause())
//			return false;
//		
//		this.setSimulationState(SimulationState.ANALIZING);
//		
//		return true;
//	}
	
	public void startAnalyzation(Body[] bodies) {
		
		this.bodiesToAnalyze = bodies;
		
		this.setSimulationState(SimulationState.ANALIZING);
	}
	
	
//--------------------------------------------------------------------------------------------------
	//FOLLOW MODE
	public void follow(Body body) {
		this.bodyToFollow = body;
		System.out.println("set body to follow: " + body);
	}
	
//--------------------------------------------------------------------------------------------------
	//SETTERS
	
	public void loadBodySystemTemplates(BodySystemTemplate bodySystemTemplate){
		this.bodyToFollow = null;
		this.bodiesToAnalyze = null;
		this.bodies = bodySystemTemplate.getBodies();
		windowOptions.updateBodyList();
	}
	
	private void setSimulationState(SimulationState state) {
		this.state = state;		
	}
	
	public void setMdklSDelta(double delta) {
		this.timeMdklSIterationSeconds = delta;
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
	
//	public AnalazysResult getAnalazysResult() {
//		return this.analazysResult;
//	}
	
	public Body getBodyToFollow() {
		return this.bodyToFollow;
	}
}
