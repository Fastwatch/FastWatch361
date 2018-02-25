package chronoTimer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
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
	private boolean gotEvent; // their test text file has a EVENT command so I added the flag since we are only using Run
	
	// used for testing
	private boolean showMessage = true; //  TRUE - print invalid sysout msg, FALSE - print no invalid sysout msg 
	/**
	 * Helper method for printing invalid input messages and always return false. 
	 * Used in methods that requires a boolean return value of false.
	 * @param message message to print what was invalid/incorrect
	 * @return false always
	 */
	private boolean printMessage(String message){
		if(showMessage == true){
			System.out.println(message); 
		}
		return false;
	}

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
		gotEvent = false;
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
	public boolean execute(String c){
		String[] commands = c.split("\\s+");
		LocalTime time = null;
		
		if(commands.length < 2) return printMessage("Not enough arguments. Need atleast 2.");
		
		if(powerState == true){
			time = parseTime(commands[0]);
			if(time == null) {
				return false;
			}
			switch(commands[1].toUpperCase()){
				case("POWER"):
					power();
					if(powerState == false) {
						System.out.println("ChronotTimer is now off.");
						return false;
					}
					break; 
				
				case("EXIT"): // Exit simulator
					//sim.exit(); //*Still need to be implemented in ChronoTimerSimulator class*//
					break;
					
				case("CANCEL"): // Discard current run for racer and place racer back to queue
					if(currentRun == null) return printMessage("Cannot cancel run, there is currently no run.");
					try {
						currentRun.cancel();
					}catch(IllegalStateException e) {
						return printMessage(e.getMessage());
					}
					break;
					
				case("EVENT"): // Set Run to this particular event <TIME> <EVENT> <TYPE>
					if(commands.length <= 2) return printMessage("Need a event type. Should be the 3rd arg.");
					
					else if(commands[2].equalsIgnoreCase("IND")) {
						gotEvent = true;
						printMessage("Event set to IND");
					}	
					else
						return printMessage("Unsupported event type.");
					break;
					
				case("CONN"): // CONN <sensor> <NUM>
					// Connect a type of sensor to channel <NUM>
					//			<sensor> = {EYE,GATE,PAD}
					break;
				
				case("NEWRUN"): // Create a new Run (must end a Run first)
					try {
						newRun();
					}catch(IllegalStateException e) {
						return printMessage("Cannot create new run. Must end a run first.");
					}
					break;
				
				case("ENDRUN"): // Done with a Run
					if(endRun() == false) return printMessage("There is no current run.");
					break;
				
				case("RESET"): // Reset system to initial state
					reset();
					break;
					
				case("TIME"): // Set the current time. Default time is host system time.
					if(commands.length <= 2) return printMessage("Need a time to set. Should be the 3rd arg.");
					time = parseTime(commands[2]);
					if(time == null) return printMessage("Time is is null");
					else
						System.out.println("Time has been set to " + time);
					break;
				
				case("NUM"): // Set	NUM<NUMBER> as the next	competitor to start.
					if(commands.length <= 2) return printMessage("Need a bib number to set racer. Should be the 3rd arg.");
					if(currentRun == null) return printMessage("Cannot add racer, create a Run first.");
					try {
						int bibNum = Integer.parseInt(commands[2]);
						currentRun.num(bibNum);
						System.out.println("Racer with bib number " + bibNum + " has been added successfully.") ;
					}catch(NumberFormatException e) {
						return printMessage("Error on parsing bib number to a number.");
					}catch(IllegalStateException e) {
						return printMessage(e.getMessage());
					}
					break;
				
				case("TOG"): // Toggle the state of the channel TOG<CHANNEL>
					try{
						int channel = Integer.parseInt(commands[2]);
						if(channel < 0 || channel >= 9){
							return printMessage("Channel Not Supported.");
						}
						// do something here, unsure at this point what to toggle channel for
						
					}catch(NumberFormatException e){
						return printMessage("Error on parsing Channel to a number.");
					}
					break;
					
				case("DNF"): // DNF says the run for the bib number is over, and their end time is DNF
					if(currentRun == null) return printMessage("Cannot set run to dnf. There is no run.");
					try {
						currentRun.dnf(); 
					}catch(IllegalStateException e) {
						return printMessage(e.getMessage());
					}
					break;
					
				case("TRIG"): // Trigger channel Trig<NUM>
					try{
						if(commands.length <= 2) return printMessage("Need a channel number to trigger. Should be the 3rd arg.");
						int channel = Integer.parseInt(commands[2]);
						if(currentRun == null) return printMessage("Cannot start/end run. Please create a run first.");
						try {
							currentRun.trig(time, channel);
						}catch(IllegalStateException e) {
							return printMessage(e.getMessage());
						}
					}catch(NumberFormatException e){
						return printMessage("Error on parsing Channel to a number.");
					}
					break;
					
				case("START"): // Start trigger channel 1 (shorthand for TRIG 1)
					if(gotEvent == false) return printMessage("Cannot start Run. Please choose an event first.");
					if(execute(time.toString()+" TRIG 1") == false) // recursion, that's why it needs a second return false check
						return false;
					break;
					
				case("FINISH"): // Finish trigger channel 2 (shorthand for TRIG 2)
					if(execute(time.toString()+" TRIG 2") == false) // recursion, that's why it needs a second return false check
						return false;
					break;
					
				case("PRINT"): // print <RUN>
					if(currentRun == null) return printMessage("Cannot print Run, there is no Run.");
					else
						print(time, currentRun);
					break;
					
				default:
					System.out.println("Invalid command.");
					return false;
			}
		}
		else{ // only listen for POWER since the power to the system is off
			time = parseTime(commands[0]);
			if(time == null)
				return false;
			if(commands[1].equalsIgnoreCase("POWER")){
				power();
				System.out.println("ChronoTimer is now on.");
				return true;
			}
			System.out.println("ChronoTimer is off. Please turn on the power before providing commands. <TIME> <POWER>");
			return false;
		}
		
		return true;
	}

	// I was duplicating this block of code in execute(), so I decided to make a method for it
	private LocalTime parseTime(String commands) {
		LocalTime time = null;
		try{
			time = LocalTime.parse(commands);
		}catch(DateTimeParseException e){
			printMessage("Something went wrong when parsing time.\nNote: Time should be the first arg in format of 00:00:00");
			return null;
		}
		return time;
	}

	//creates a new run
	private void newRun(){
		if (currentRun != null){
			throw new IllegalStateException("run in progress");
		}
		currentRun = new IND();
		System.out.println("Run created.");
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
