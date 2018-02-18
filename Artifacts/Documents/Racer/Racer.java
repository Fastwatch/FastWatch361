import java.time.LocalTime;

public class Racer {

	private LocalTime startTime; // racer's start time
	private LocalTime endTime; // racer's end time
	private int bibNum; // racer's bib number
	private boolean dnf; // did not finish state
	
	//private LocalTime duration; //duration between endTime and start time
	
	//constructor//
	protected Racer(){}
	protected Racer(int bn){ this.bibNum = bn; }
	protected Racer(int bibNum, LocalTime sTime){
		this.bibNum = bn;
		this.startTime = sTime;
	}
	protected Racer(int bibNum, LocalTime sTime, LocalTime fTime){
		this.bibNum = bn;
		this.startTime = sTime;
		this.endTime = fTime;
	}
	
	//setters//
	protected void setStart(LocalTime sTime){ this.startTime = sTime; }
	
	protected void setFinish(LocalTime fTime) { this.endTime = fTime; }
	
	protected boolean setBib(int bibNum){ this.bibNum = bibNum; }
	
	protected void setDNF(boolean status) { this.dnf = status; }
	
	
	//getters//
	protected LocalTime getStartTime(){ return this.startTime; }
	
	protected LocalTime getEndTime(){ return this.endTime; }
	
	protected boolean getDNF(){ return this.dnf; }
	
	protected int getBibNum() { return this.bibNum; }
	
	protected LocalTime getTime(){
		
		if(startTime == null || endTime == null) throw new NullPointerException("Cannot get duration, there is either no start/endTime time for the racer: " + bibNum);
		
		LocalTime duration = endTime.minusHours(startTime.getHour());
		duration = duration.minusMinutes(startTime.getMinute());
		duration = duration.minusSeconds(startTime.getSecond());
		
		//System.out.println(duration.toString());
		//this.duration = duration;

		return duration;
	}
	
	// Test run to save system time to the racer's time //
	protected void startTime(){ startTime = java.time.LocalTime.now(); } 

	protected void endTime(){ endTime = java.time.LocalTime.now(); }
	
}
