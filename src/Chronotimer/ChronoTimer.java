package Chronotimer;

import java.io.FileWriter;
import java.io.IOException;
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
		//System.out.print(run.standings(time));
		return run.standings(time);
	}

	//prints out given run to a new file
	private void export(Run run){
		PrintWriter out;
		try{
			out= new PrintWriter(new FileWriter("C:\\location\\outputfile.txt")); 
			out.print(run.toString()); 
			out.println("world");
		}
		catch (IOException e){
			//do something?
			System.out.println("Cannot open the file.");
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
	protected void execute(String c){
		String[] commands = c.split(" ");
		if(commands.length < 2){
			System.out.println("Not enough arguments. Need atleast 2.");
			return;
		}
		LocalTime time = null;
		
		if(powerState == true){
			try{
				time = LocalTime.parse(commands[0]);
			}catch(Exception e){
				System.out.println("Something went wrong when parsing time.\nNote: Time should be the first arg in format of 00:00:00");
				return;
			}
			switch(commands[1].toUpperCase()){
				case("POWER"):
					power();
				if(powerState == false)
					System.out.println("ChronotTimer is now off.");
					break;
				
				case("EXIT"): // Exit simulator
					//sim.exit(); //*Still need to be implemented in ChronoTimerSimulator class*//
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
							System.out.println("Invalid channel to set state.");
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
						if(commands.length <= 2){
							System.out.println("Need a channel number to trigger. Should be the 3rd arg.");
							return;
						}
						int channel = Integer.parseInt(commands[2]);
						if(channel < 0 || channel >= 9){
							System.out.println("Invalid channel to toggle.");
							return;
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
					System.out.println("Invalid command.");
			}
		}
		else{ // only listen for POWER since the power to the system is off
			try{
				time = LocalTime.parse(commands[0]);
			}catch(Exception e){
				System.out.println("Something went wrong when parsing time.\nNote: Time should be the first arg in format of 00:00:00");
				return;
			}
			if(commands[1].equalsIgnoreCase("POWER")){
				power();
				System.out.println("ChronoTimer is now on.");
				return;
			}
			System.out.println("ChronoTimer is off. Please turn on the power before providing commands. <TIME> <POWER>");

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
		return true;
	}

}
