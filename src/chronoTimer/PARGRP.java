package chronoTimer;

import java.time.LocalTime;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * The Parallel Group Race has a single start for all racers,
 * however each racer is in their own lane and has a dedicated sensor for their finish
 *
 * Any channel can be used to start the race,
 * Each channel number 1-8 is associated with the corresponding lane 1-8
 * 
 * @author Andrew Krill, Fue Her, Philip Sauvey
 *
 */

public class PARGRP extends Run{
	
	LocalTime startTime;
	Racer racers[];
	int numOfRacers;

	
	protected PARGRP(){
		this.type="PARGRP";
		startTime=null;
		active = true;
		racers = new Racer[8];
	}
	
	/**
	 * Trigger function for handling sensor input
	 * Channel Any: Start
	 * Channel 1-8: Finish for corresponding lane
	 *  
	 * @param time current system time trigger takes place
	 * @param channelNum Channel that is triggered
	 */
	@Override
	protected String trig(LocalTime time, int channelNum) {
		String status = "";
		//Can't have 0 racers in a race
		if(numOfRacers <= 0) {
			throw new IllegalStateException("No Racer in Queue");
		}
		//Channel 1 is both a startTime trigger and finish trigger 
		if(startTime == null){
			startTime = time;
			for(int i = 0; i < 8; i++) {
				if(racers[i] != null)
					racers[i].setStart(startTime);
			}
			status = "Parallel Group Race Now started";
			return status;
		} else {
			//Trying to trigger a finish for a channel with no racer
			if(racers[channelNum - 1] == null) throw new IllegalStateException("No racers in lane " + channelNum + "\n");	
			if(racers[channelNum - 1].getEndTime() != null || racers[channelNum - 1].getDNF()) throw new IllegalStateException("Racer in lane " + channelNum + "already finished\n");
			racers[channelNum - 1].setFinish(time);
			status += "Racer " + racers[channelNum - 1].getBibNum() + " has finished with a time of " + racers[channelNum - 1].getTime() + "\n";

		}
		return status;
	}

	/**
	 * Swapping is not supported for this race type
	 */
	@Override
	protected void swap() {
		throw new IllegalStateException("Race Type does not support that");
	}

	/**
	 * Swapping is not supported for this race type
	 */
	@Override
	protected void swap(int laneNum) {
		throw new IllegalStateException("Race Type does not support that");
	}

	/**
	 * This method not supported for this race type, a lane number must be provided to swap
	 */
	@Override
	protected String dnf() {
		throw new IllegalStateException("Race Type does not support that (Must have lane number)");
	}

	/**
	 * DNFs the racer in the lane specified
	 * 
	 * @param laneNum Lane number to DNF
	 */
	@Override
	protected String dnf(int laneNum) {
		if(startTime == null) throw new IllegalStateException("Cannot DNF, Race did not start yet.");
		if(laneNum >= 9 || laneNum <= 0) throw new IllegalStateException("No such lane exist.");
		if(racers[laneNum - 1] != null && racers[laneNum - 1].getEndTime() == null && racers[laneNum - 1].getDNF() == false){
			racers[laneNum - 1].setDNF(true);
		}else{
			throw new IllegalStateException("No racer in lane " + laneNum + " to DNF");	
		}
		return "Racer " + racers[laneNum - 1].getBibNum() + " did not finish."; 
	}

	/**
	 * Function to cancel the group start time.
	 * Cannot cancel start if a racer has already completed the course.
	 */
	@Override
	protected void cancel() {
		if(startTime==null) throw new IllegalStateException("Race hasn't startTimeed yet");
		
		//Can't cancel a race startTime if a racer has finished
		for(int i = 0; i < numOfRacers; i++) {
			if(racers[i].getEndTime() != null|| racers[i].getDNF()) throw new IllegalStateException("Cannot cancel start after a racer has finished.");
		}
		startTime = null;
		for(int i = 0; i < numOfRacers; i++){
			racers[i].setStart(null);
		}
	}

	/**
	 * Num function adds a racer to the next empty lane in the race
	 * cannot add more than 8 racers to this type of race
	 * 
	 * @param bibNum Racer Number to be added to race
	 */
	@Override
	protected void num(int bibNum) {
		if(contains(bibNum))throw new IllegalArgumentException("Attempting to create duplicate racer");
		if (bibNum<0 ||bibNum>=1000)throw new IllegalArgumentException("Bib Number must be between 000 and 999.");
		if(startTime != null) throw new IllegalArgumentException("Cannot add racers while a race is in progress.");
		if(numOfRacers >= 8) throw new IllegalArgumentException("Can only have up to 8 racers per race."); //can't have too many racers
		racers[numOfRacers] = new Racer(bibNum);
		numOfRacers++;
		
	}

	/**
	 * Clr function removes a racer from his lane and shifts all other racers down so there are
	 * no empty lanes between racers.
	 * 
	 * @param bibNum Racer to delete from race
	 */
	@Override
	protected void clr(int bibNum) {
		if(!contains(bibNum))throw new IllegalArgumentException("Runner Not Found");
		if(startTime != null) throw new IllegalArgumentException("Cannot remove racers while a race is in progress.");
		//Remove bib then fill missing spot in array with the last racer in array
		for(int i = 0; i < 8; i++) {
			if(racers[i].getBibNum() == bibNum) {
				for(int j = i;j<7;j++){
					racers[j] = racers[j+1];
				}
				racers[7] = null;
				numOfRacers--;
				break;
			}
		}		
	}

	/**
	 * Returns current status of race listing each lane their racer and their status
	 * 
	 * @param time current system time used to calculate current time of racers
	 * @return formatted string with all racers' bib numbers, times, and status
	 */
	@Override
	protected String standings(LocalTime time) {
		if(numOfRacers <= 0) return "No Racers Currently In Run\n";
		String stand = "Parallel Group\n";
		LocalTime duration = time;
		if (startTime != null) {
			stand += "Start Time: " + startTime.toString()+"\n";
			duration = time.minusHours(startTime.getHour());
			duration = duration.minusMinutes(startTime.getMinute());
			duration = duration.minusSeconds(startTime.getSecond());
			duration = duration.minusNanos(startTime.getNano());
		}
		int count = 1;
		for(Racer r:racers){
			if (r!=null){
				stand += "Lane " + count++ + ": " + r.getBibNum() + " ";
				if (r.getEndTime() == null && r.getDNF() == false)stand += duration.toString() + "\n";
				else if (r.getDNF()) stand += "DNF\n";
				else stand +=  r.getTime() +"\n";
			}else{
				stand += "Lane " + count++ + ":\n";
			}
		}
		return stand;
	}

	/**
	 * Function to handle graceful ending of race.
	 * DNFs all racers still running.
	 */
	@Override
	protected void end() {
		active = false;
		for(int i = 0; i< 8; i++){
			if(racers[i] != null && racers[i].getEndTime() == null){
				racers[i].setDNF(true);
			}
		}
	}

	/**
	 * This function exports a valid json string representation of the status of the race.
	 * 
	 * @return JSON formatted string of current race status.
	 */
	@Override
	protected String export() {
		JsonObject obj = new JsonObject();
		JsonArray array = new JsonArray();
		JsonObject element;
	
		obj.addProperty("type", "PARGRP");
		ArrayList<Racer> queue = getQueue();
		for(Racer r: queue){
				element = new JsonObject();
				element.addProperty("bibNum", r.getBibNum());
				element.addProperty("startTime", "");
				element.addProperty("endTime", "");
				element.addProperty("dnf", r.getDNF());
				array.add(element);
		} 
		obj.add("queued", array);
		array = new JsonArray();
		for(int i = 0; i<8; i++){
			if(racers[i] != null){
				if(racers[i].getEndTime() == null && racers[i].getStartTime() != null){
					element = new JsonObject();
					element.addProperty("bibNum", racers[i].getBibNum());
					element.addProperty("startTime", startTime.toString());
					element.addProperty("endTime", "");
					element.addProperty("dnf", racers[i].getDNF());
					array.add(element);
				}
			}
		}
		obj.add("running", array);
		array = new JsonArray();
		
		for(int i = 0; i<8; i++){
			if(racers[i] != null){
				if(racers[i].getEndTime() != null || racers[i].getDNF() == true) {
					element = new JsonObject();
					element.addProperty("bibNum", racers[i].getBibNum());
					element.addProperty("startTime", startTime.toString());
					if(racers[i].getEndTime() != null){
						element.addProperty("endTime", racers[i].getEndTime().toString());
					}else {
						element.addProperty("endTime", "");
					}
					element.addProperty("dnf", racers[i].getDNF());
					array.add(element);
				}
			}
		}
		obj.add("finished", array);
		//System.out.println(obj.toString()); // debug
		return obj.toString();
	}

	/**
	 * Tests to see if a race is already running by checking if there is a start time
	 * 
	 * @return True if race has started, false otherwise
	 */
	@Override
	protected boolean raceInProgress() {
		return startTime!=null;
	}

	/**
	 * Checks if a bibNum is already used in the current race.
	 * 
	 * @param bibNum Racer number to check
	 * @return True if bibNum already used, false otherwise
	 */
	protected boolean contains(int bibNum){
		for(int i = 0; i < 8; i++) {
			if(racers[i] != null && bibNum == racers[i].getBibNum()) return true;
		}		
		return false;
	}
	
	/**
	 * This function generates a formatted string for use of the display of the GUI/Hardware
	 * It provides a live look at the status of a race.
	 * 
	 * @param time LocalTime object representing current time of the race.
	 */
	protected String update(LocalTime time) {
		if(numOfRacers <= 0) return "No Racers Currently In Run\n";
		String stand = "Parallel Group\n";
		LocalTime duration = time;
		if (startTime != null) {
			stand += "Start Time: " + startTime.toString()+"\n";
			duration = time.minusHours(startTime.getHour());
			duration = duration.minusMinutes(startTime.getMinute());
			duration = duration.minusSeconds(startTime.getSecond());
			duration = duration.minusNanos(startTime.getNano());
		}
		stand+="Lane	Bib Num	Time\n=============================\n";
		int count = 1;
		for(Racer r:racers){
			if (r!=null){
				stand += "Lane " + count++ + ":	" + r.getBibNum() + "	";
				if (r.getEndTime() == null && r.getDNF() == false)stand += duration.toString() + "\n";
				else if (r.getDNF()) stand += "DNF\n";
				else stand +=  r.getTime() +"\n";
			}else{
				stand += "Lane " + count++ + ":\n";
			}
		}
		return stand;
	}

	/**
	 * This function generates an array list of all racers in queue to start
	 * It is generally used to transition between race types before a race has started
	 * but after racers have been added.
	 * 
	 * @return ArrayList of all racers in queue to start race
	 */
	@Override
	protected ArrayList<Racer> getQueue() {
		ArrayList<Racer> queue = new ArrayList<>();
		for(int i = 0; i < 8; i++){
			if(racers[i] != null && racers[i].getStartTime() == null)
				queue.add(racers[i]);
			}
		
		return queue;
	}
}