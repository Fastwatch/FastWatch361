package chronoTimer;

import java.time.LocalTime;
import java.util.ArrayList;

import chronoTimer.Run.Node;

public class PARGRP extends Run{
	
	LocalTime startTime;
	LocalTime[] finishTimes = new LocalTime[8];
	int bibNumbers[] = new bibNumbers[8];
	int dnf[] = new dnf[8];
	int numOfRacers = 0;
	
	protected PARGRP(){
		this.type="PARGRP";
		start=null;
		active = true;
	}
	
	@Override
	protected String trig(LocalTime time, int channelNum) {
		String status;
		
		//Can't have 0 racers in a race
		if(numOfRacers = 0) {
			throw new IllegalStateException("No Racers Entered");
		}
		//Channel 1 is both a start trigger and finish trigger
		if(start==null){
			for(int i = 0; i < numRacers; i++) {
				dnf[i] = false;
			}
			start = time;
			status = "Parallel Group Race Now Started"
		} else {
			//Trying to trigger a finish for a channel with no racer
			if(channelNum > numOfRacers) {
				throw new IllegalStateException("Channel Has No Racer");				
			} else {
				finishTimes[channelNum] = time;
			}

			//check finish times to see if all participants have a finish time
			bool raceFinished = true;
			for(int i = 0; i < numOfRacers; i++) {
				if(finishTimes[i]==null) {
					raceFinished = false;
				}
			}
			
			if(raceFinished) {
				status = "Race is Finished";
			} else {
				status = "Race is ongoing";
			}
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
		throw new IllegalStateException("Race Type does not support that (Must have bib number)");
	}

	@Override
	protected String dnf(int laneNum) {
		if(laneNumber <= numRacers) {
			dnf[laneNumber] = true;			
		} else {
			throw new IllegalStateException("Cannot DNF a lane without a racer");			
		}
		dnf[laneNum] = true;
		return "Racer " + n.Data.getBibNum() + " did not finish."; 
	}

	@Override
	protected void cancel() {
		if(start==null) throw new IllegalStateException("Race hasn't started yet");
		
		//Can't cancel a race start if a racer has finished
		bool hasRacerFinishedYet = false;
		for(int i = 0; i < numRacers; i++) {
			if(finishTimes[i] != null) {
				hasRacerFinishedYet = true;
			}
		}
		if(hasRacerFinishedYet) throw new IllegalStateException("Cannot cancel start after a racer has finished.");
		start = null;
	}

	@Override
	protected void num(int bibNum) {
		if(contains(bibNum))throw new IllegalArgumentException("Attempting to create duplicate racer");
		if (bibNum<0 ||bibNum>=1000)throw new IllegalArgumentException("Bib Number must be between 000 and 999.");

		//can't have too many racers
		if(numRacers < 9) {
			numRacers++;
			bibNums[numRacers] = bibNum;
		}
	}

	@Override
	protected void clr(int bibNum) {
		if(!contains(bibNum))throw new IllegalArgumentException("Runner Not Found");
		
		//Remove bib then fill missing spot in array with the last racer in array
		for(int i = 0; i < numRacers; i++) {
			if(bibNumbers[i] == bibNum) {
				bibNumbers[i] = bibNumbers[numRacers];
				numRacers--;
			}
		}
		
	}

	@Override
	protected String standings(LocalTime time) {
		if (start==null)return "Race has not started\n";
		String stand = "Group Start Time: " + start.toString()+"\n";
		stand+= "Currently In Race:\n";
		for(int i = 0; i < numRacers; i++) {
			if(hasFinishedYet[i] == false && dnf[i] == false) {
				stand+= bibNumbers[i].toString() +" "+ time.toString()+"\n";
		}
		
		}
		if(finished.getLength() > 0){
			stand+="\nCompleted Race:\n";
			
			for(int i = 0; i < numRacers; i++) {
				if(hasFinishedYet[i] == true && dnf[i] == false) {
					stand+= bibNumbers[i].toString() +" DNF\n";
				}
			}
			
			for(int i = 0; i < numRacers; i++){
				if(dnf[i] == true) {
					stand+= bibNumbers[i].toString() +" DNF\n";
				}
			}
		}
		return stand;
	}

	@Override
	protected void end() {
		active = false;

		//@TODO: 
		
	}

	@Override
	protected String export() {
		String output = "{\"type\":\"PARGRP\",";
		output+= "\"startTime\":\""+start.toString()+"\",";
		output+= "\"Racing\":[";
		for(int i = 0; i < numRacers; i++){
			if(hasFinishedYet[i] == false) {
				output+= "{\"bibNum\":"+bibNumbers[i].toString()+",\"startTime\":\"\",\"endTime\":\"\",\"dnf\":"+dnf[i].toString()+"}";				
			}
			if(i == numOfRacers){ output+=","};
		}
		output += "],";
		output+= "\"finished\":[";
		for(int i = 0; i < numRacers; i++){

			if(hasFinishedYet[i] == false && dnf[i] == true) {
				output+= "{\"bibNum\":"+bibNumbers[i].toString()+",\"startTime\":\"" + start +"\",\"endTime\":\"\",\"dnf\":"+dnf[i].toString()+"}";			
			}

			if(hasFinishedYet[i] == true) {
				output+= "{\"bibNum\":"+bibNumbers[i].toString()+",\"startTime\":\"" + start +"\",\"endTime\":\""+finishTimes[i].toString()+"\",\"dnf\":"+dnf[i].toString()+"}";		
			}
			
			if(i == numOfRacers){ output+=","};
		}
		output += "]}";
		return output;
	}

	@Override
	protected boolean raceInProgress() {
		return start!=null;
	}

	protected boolean contains(int bibNum){
		bool contains = false;
		for(int i = 0; i < numRacers; i++) {
			if(bibNum == bibNumbers[i]) contains = true;
		}
		return contains;
	}
	
	protected String update(LocalTime time) {
		if (start==null)return "Race has not started\n";
		String stand = "Group Start Time: " + start.toString()+"\n";
		int counter = 0;
		bool hasAnyoneFinished = false;
		for(int i = 0; i < numRacers; i++) {
			if(hasFinished[i]) {
				hasAnyoneFinished = true;
			}
		}
	
		if(!hasAnyoneFinished){
			return stand + "No Racers finished\n";
		} else {
			stand+="\nCompleted Race:\n";
			}
		}
		return stand;
	}
	@Override
	protected ArrayList<Racer> serverData() {
		ArrayList<Racer> finishedList = new ArrayList<>();
		for(Node n = finished.head; n != null; n = n.next) {
			finishedList.add(n.Data);
		}
		return finishedList;
	}
}