import java.time.LocalTime;

public class Racer {

	private LocalTime start; // start time
	private LocalTime stop; // stop time
	private int iD; // racer's identity number
	
	//private LocalTime duration; //duration between stop and start time
	
	//constructor//
	public Racer(int id){ this.iD = id; }
	
	//setters//
	public void setStartTime(LocalTime t){ start = t; }
	
	public void setStopTime(LocalTime t) { stop = t; }
	
	//getters//
	public LocalTime getStartTime(){ return start; }
	
	public LocalTime getStopTime(){ return stop; }
	
	public int getID() { return this.iD; }
	
	public LocalTime getDuration(){
		
		if(start == null || stop == null) throw new NullPointerException("Cannot get duration, there is either no start/stop time for the racer: " + iD);
		
		LocalTime duration = stop.minusHours(start.getHour());
		duration = duration.minusMinutes(start.getMinute());
		duration = duration.minusSeconds(start.getSecond());
		
		//System.out.println(duration.toString());
		//this.duration = duration;

		return duration;
	}
	
	// Test run to save system time to the racer's time //
	public void startTime(){ start = java.time.LocalTime.now(); } 

	public void stoptime(){ stop = java.time.LocalTime.now(); }
	
}
