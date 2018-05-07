package chronoTimer;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import chronoTimer.Run.Node;

@SuppressWarnings("unused")
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
			if(racers[channelNum - 1] == null || racers[channelNum - 1].getEndTime() != null || racers[channelNum - 1].getDNF()) throw new IllegalStateException("No racers in lane " + channelNum + "\n");	
 
			racers[channelNum - 1].setFinish(time);
			status += "Racer " + racers[channelNum - 1].getBibNum() + " has finished with a time of " + racers[channelNum - 1].getTime() + "\n";

		}
		return status;
	}


	@Override
	protected void swap() {
		throw new IllegalStateException("Race Type does not support that");
	}

	@Override
	protected void swap(int laneNum) {
		throw new IllegalStateException("Race Type does not support that");
	}

	@Override
	protected String dnf() {
		throw new IllegalStateException("Race Type does not support that (Must have lane number)");
	}

	@Override
	protected String dnf(int laneNum) {
		if(racers[laneNum - 1] != null){
			racers[laneNum - 1].setDNF(true);
		}else{
			throw new IllegalStateException("No racer in lane " + laneNum + " to DNF");	
		}
		return "Racer " + racers[laneNum - 1].getBibNum() + " did not finish."; 
	}

	@Override
	protected void cancel() {
		if(startTime==null) throw new IllegalStateException("Race hasn't startTimeed yet");
		
		//Can't cancel a race startTime if a racer has finished
		for(int i = 0; i < numOfRacers; i++) {
			if(racers[i].getEndTime() != null|| racers[i].getDNF()) throw new IllegalStateException("Cannot cancel start after a racer has finished.");
		}
		startTime = null;
	}

	@Override
	protected void num(int bibNum) {
		if(contains(bibNum))throw new IllegalArgumentException("Attempting to create duplicate racer");
		if (bibNum<0 ||bibNum>=1000)throw new IllegalArgumentException("Bib Number must be between 000 and 999.");
		if(startTime != null) throw new IllegalArgumentException("Cannot add racers while a race is in progress.");
		if(numOfRacers >= 8) throw new IllegalArgumentException("Can only have up to 8 racers per race."); //can't have too many racers
		racers[numOfRacers] = new Racer(bibNum);
		numOfRacers++;
		
	}

	@Override
	protected void clr(int bibNum) {
		if(!contains(bibNum))throw new IllegalArgumentException("Runner Not Found");
		
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

	@Override
	protected String standings(LocalTime time) {
		if(numOfRacers <= 0) return "No Racers Currently In Run\n";
		String timeStamp = "";
		String stand = "";
		LocalTime duration = time;
		if (startTime != null) {
			stand += "Parallel Group startTime Time: " + startTime.toString()+"\n";
			stand+= "Currently Racing:\n";
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

	@Override
	protected void end() {
		active = false;
		for(int i = 0; i< 8; i++){
			if(racers[i] != null && racers[i].getEndTime() == null){
				racers[i].setDNF(true);
				numOfRacers = 0;
			}
		}
	}

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
		//System.out.println(obj.toString()); // debug
		return obj.toString();
	}

	@Override
	protected boolean raceInProgress() {
		return startTime!=null;
	}

	protected boolean contains(int bibNum){
		boolean contains = false;
		for(int i = 0; i < 8; i++) {
			if(racers[i] != null && bibNum == racers[i].getBibNum()) return true;
		}		
		return false;
	}
	
	protected String update(LocalTime time) {
		if(numOfRacers <= 0) return "No Racers Currently In Run\n";
		String timeStamp = "";
		String stand = "";
		LocalTime duration = time;
		if (startTime != null) {
			stand += "Parallel Group startTime Time: " + startTime.toString()+"\n";
			stand+= "Currently Racing:\n";
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