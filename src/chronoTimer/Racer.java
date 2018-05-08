package chronoTimer;

import java.time.LocalTime;


/**
 *  A racer with a unique bib number that has it's own start and end time (LocalTime class). 
 *  This class is used in racing events.
 * @author Fue Her
 *
 */
public class Racer {

	/**
	 * The racer's start time
	 */
	private LocalTime startTime; 
	/**
	 * The racer's end time
	 */
	private LocalTime endTime; // racer's end time
	/**
	 * The racer's bib number
	 */
	private int bibNum; // racer's bib number
	
	/**
	 * The racer's did not finish state (True - did not finish) (False - did finish)
	 */
	private boolean dnf; // did not finish state
	
	//private LocalTime duration; //duration between endTime and start time
	
	//constructor//
	protected Racer(){}
	protected Racer(int bn){ this.bibNum = bn; }
	protected Racer(int bibNum, LocalTime sTime){
		this.bibNum = bibNum;
		this.startTime = sTime;
	}
	protected Racer(int bibNum, LocalTime sTime, LocalTime fTime){
		this.bibNum = bibNum;
		this.startTime = sTime;
		this.endTime = fTime;
	}
	
	//setters//
	
	/**
	 * Set the start time to this racer.
	 * @param sTime start time
	 */
	protected void setStart(LocalTime sTime){ this.startTime = sTime; }
	
	/**
	 * Set the finish time to this racer.
	 * @param fTime finish time
	 */
	protected void setFinish(LocalTime fTime) { this.endTime = fTime; }
	
	/**
	 * Set a bib number to the Racer
	 * @param bibNum bib number to assign to this racer
	 * @param racers list of racers to check if this bib number is already assigned
	 * @return True if this racer has been assign to the bib number and false if
	 * the there is a racer with the bib number already. 
	 */
	protected void setBib(int bibNum){
		this.bibNum = bibNum;
	}
	
	/**
	 * Set the status of "Did Not Finish" state to this racer 
	 * @param status Status of what the racer state is. True if this racer did not finish and 
	 * false if this racer did finish.
	 */
	protected void setDNF(boolean status) { this.dnf = status; }
	
	
	//getters//
	
	/**
	 * Get this racer's start time
	 * @return Racer's start time
	 */
	protected LocalTime getStartTime(){ return this.startTime; }
	
	/**
	 * Get this racer's end time.
	 * @return Racer's end time.
	 */
	protected LocalTime getEndTime(){ return this.endTime; }
	
	/**
	 * Get racer's status if it is finished with the run
	 * @return True if racer did not finish and false if racer did finish.
	 */
	protected boolean getDNF(){ return this.dnf; }
	
	/**
	 * Get this racer's bib number
	 * @return Racer's bib number (int)
	 */
	protected int getBibNum() { return this.bibNum; }
	
	/**
	 * Get the difference between this racer's finish time and start time (Finish Time - Start Time)
	 * @return Duration of the racer's start and finish time
	 */
	protected LocalTime getTime(){
		
		if(startTime == null || endTime == null) throw new NullPointerException("Cannot get duration, there is either no start/endTime time for the racer: " + bibNum);
		
		LocalTime duration = endTime.minusHours(startTime.getHour());
		duration = duration.minusMinutes(startTime.getMinute());
		duration = duration.minusSeconds(startTime.getSecond());
		duration = duration.minusNanos(startTime.getNano());

		return duration;
	}
	
}
