import java.io.PrintWriter;
//other possible imports

public class ChronoTimer {

	//fields
	private boolean powerState; //CT on or off
	private Run	currentRun; //run in progress
	private ArrayList<Run> pastRuns; //run history
	private Sensor[] channels; //8 sensor inputs
	private String log; //text of previous commands
	//private ArrayList<String> log; //
	private String currentCommand; //current task to be carried out by execute()
	private Event currentEvent;

	//field initialization (default constructor);
	public ChronoTimer(){ 
		powerState = false;
		currentRun = null;
		pastRuns = new ArrayList<Run>();
		channels = new Sensor[8];
		log = null;
		currentCommand = null;
		currentEvent = null;
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
		channels = new Sensor[8];
		log = null;
		currentCommand = null;
		currentEvent = null;
	}

	//sets a new sensor to specified channel and returns the previous
	//sensor at that channel
	public Sensor conn(Sensor newInput, int newSensorChannel){
		Sensor oldInput = channels[newSensorChannel];
		channels[newSensorChannel] = newInput;
		return oldInput;
	}

	//disconnects a channel and returns the sensor previously
	//connected to that channel
	public Sensor disc(int disabledChannel){
		Sensor oldInput = channels[disabledChannel];
		channels[disabledChannel] = null;
		return oldInput;
	}


	//	private event(String){
	//		
	//	}

	//changes to specified event and returns previous event
	private Event setEvent(Event newEvent){
		oldEvent = currentEvent;
		currentEvent = newEvent;
		return oldEvent;
	}

	//Event getter
	private Event getEvent(){
		return currentEvent;
	}

	//prints a given run
	private String print(Run run){
		SystemOut.print(run.toString());
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
//	//incomplete
//	//return type?
//	private void execute(String){
//
//	}

	//creates a new run
	private void newRun(){
		if (currentRun == null){
			throw new IllegalStateException("run in progress");
		}
		currentRun = new Run;
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
