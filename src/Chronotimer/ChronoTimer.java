package Chronotimer;

import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.ArrayList;

import chronoSimulator.ChronoTimerSimulator;
//other possible imports


public class ChronoTimer {

	//fields
	private boolean powerState; //CT on or off
	private Run	currentRun; //run in progress
	private ArrayList<Run> pastRuns; //run history
	//private Sensor[] channels; //8 sensor inputs
	private String log; //text of previous commands
	//private ArrayList<String> log; //
	private String currentCommand; //current task to be carried out by execute()
	//private Event currentEvent;
	private ChronoTimerSimulator sim; // chrono timer simulator

	//field initialization (default constructor);
	public ChronoTimer(){ 
		powerState = false;
		currentRun = null;
		pastRuns = new ArrayList<Run>();
		//channels = new Sensor[8];
		log = null;
		currentCommand = null;
		//currentEvent = null;
	}

	//toggles CT power on/off and returns the new power state
	private boolean power(){ 
		powerState = !powerState;
		return powerState;
	}

	//CT set to initial state
	private void reset(){
		powerState = false;
		currentRun = null;
		pastRuns = new ArrayList<Run>();
		//channels = new Sensor[8];
		log = null;
		currentCommand = null;
		//currentEvent = null;
	}

	//sets a new sensor to specified channel and returns the previous
	//sensor at that channel
	/*
	public Sensor conn(Sensor newInput, int newSensorChannel){
		Sensor oldInput = channels[newSensorChannel];
		channels[newSensorChannel] = newInput;
		return oldInput;
	}*/

	//disconnects a channel and returns the sensor previously
	//connected to that channel
	/*
	public Sensor disc(int disabledChannel){
		Sensor oldInput = channels[disabledChannel];
		channels[disabledChannel] = null;
		return oldInput;
	}*/


	//	private event(String){
	//		
	//	}

	//changes to specified event and returns previous event
	//private Event setEvent(Event newEvent){
	//	oldEvent = currentEvent;
	//	currentEvent = newEvent;
	//	return oldEvent;
	//}

	//Event getter
	//private Event getEvent(){
	//	return currentEvent;
	//}

	//prints a given run
	private String print(LocalTime time, Run run){
		System.out.print(run.standings(time));
	}

	//prints out given run to a new file
	private export(Run run){
		PrintWriter out;
		try{
			out= new PrintWriter(new FileWriter("C:\\location\\outputfile.txt"))); 
			out.print(run.toString())); 
			out.println("world");
		}
		catch (IOException e){
			//do something?
		}finally{
			try{
				out.close();
			}catch (Exception e){
				//do nothing?
			}
		}
	}

	//still not fully understood
//	public void setCommand(String command){
//		execute(command)
//		switch
//	}
//
//	//incomplete, needs implementations in several classes
//	//return type?
	private void execute(String c){
		if(powerState == true){
			String[] commands = c.split(" ");
			LocalTime time = LocalTime.parse(commands[0]);
			switch(commands[1].toUpperCase()){
				case("EXIT"): // Exit simulator
					sim.exit(); //*Still need to be implemented in ChronoTimerSimulator class*//
					break;
					
				case("CANCEL"): // Discard current run for racer and place racer back to queue
					currentRun.cancel(); //*Still need to be implemented in IND class*//
					break;
				
				case("RESET"): // Reset system to initial state
					reset();
					break;
					
				case("TIME"): // Set the current time. Default time is host system time.
					//Time object.setTime(hr,min,sec); or Time object.defaultTime(); //*Still need to be implemented in Time class*//
					break;
				
				case("TOG"): // Toggle the state of the channel TOG<CHANNEL>
					try{
						int channel = Integer.parseInt(commands[1]);
						if(channel < 0 || channel >= 9){
							System.out.println("Invalid channel to toggle.");
						}
						//channels[channel].setState(Something?); //*Still need to be implemented in Sensor class*//
						
					}catch(NumberFormatException e){
						System.out.println("Error on parsing Channel to a number.");
						//e.getStackTrace();
					}
					break;
					
				case("DNF"): // DNF says the run for the bib number is over, and their end time is DNF
					currentRun.dnf(); //*Still need to be implemented in IND class*//
					break;
					
				case("TRIG"): // Trigger channel Trig<NUM>
					try{
						int channel = Integer.parseInt(commands[1]);
						if(channel < 0 || channel >= 9){
							System.out.println("Invalid channel to toggle.");
						}
						currentRun.trig(time, channel); //*Still need to be implemented in Sensor class*//
						
					}catch(NumberFormatException e){
						System.out.println("Error on parsing Channel to a number.");
						//e.getStackTrace();
					}
					break;
					
				case("START"): // Start trigger channel 1 (shorthand for TRIG 1)
					execute(time.toString()+" TRIG 1");
					break;
					
				case("FINISH"): // Finish trigger channel 2 (shorthand for TRIG 2)
					execute(time.toString()+" TRIG 2");
					break;
					
				default:
					
			}
		}
		else{ // only listen for POWER since the power to the system is off
			if(c.toUpperCase().equals("POWER")){
				power(); // power on!!! LETS DO THIS!
			}
		}
	}

	//creates a new run
	private void newRun(){
		if (currentRun == null){
			throw new IllegalStateException("run in progress");
		}
		currentRun = new IND();
	}

	//adds the current run to past run history and resets
	//the current run to null
	//returns false if there is no current run
	private boolean endRun(){
		if (currentRun == null){
			return false;
		}
		pastRuns.add(currentRun);
		currentRun = null;
	}

}
