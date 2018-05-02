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
	
	int tempNum;
	Node pointer;
	
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
		if(channelNum == 1 && start==null){
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
		
		if(active){
			if(queued.getLength()>0){
				stand+= "In Queue to finish:\n";
				for(Node n = queued.head;n!=null;n=n.next){
					stand+= Integer.toString(n.Data.getBibNum()) +" "+ time.toString()+"\n";
				}
			}
			if(finished.getLength() > 0){
				stand+="\nCompleted Race:\n";
				for(Node n = finished.head;n!=null;n=n.next){
					if(n.Data.getDNF()){
						stand+= Integer.toString(n.Data.getBibNum()) +" DNF\n";
					}else{
						stand+= Integer.toString(n.Data.getBibNum()) +" "+ n.Data.getTime().toString() +"\n";
					}
				}
			}
		}else{
			if(finished.getLength() > 0){
				stand+="\nCompleted Race:\n";
				for(Node n = finished.head;n!=null;n=n.next){
					if(n.Data.getDNF()){
						stand+= Integer.toString(n.Data.getBibNum()) +" DNF\n";
					}else{
						stand+= Integer.toString(n.Data.getBibNum()) +" "+ n.Data.getTime().toString() +"\n";
					}
					
				}
			}
		}
		return stand;
	}

	@Override
	protected void end() {
		active = false;
		while(queued.getLength()>0){
			dnf();
		}
		
	}

	@Override
	protected String export() {
		String output = "{\"type\":\"GRP\",";
		output+= "\"startTime\":\""+start.toString()+"\",";
		output+= "\"queued\":[";
		for(Node n = queued.head;n!=null;n=n.next){
			output+= "{\"bibNum\":"+Integer.toString(n.Data.getBibNum())+",\"startTime\":\"\",\"endTime\":\"\",\"dnf\":"+n.Data.getDNF()+"}";
			if (n.next!=null) output+=",";
		}
		output += "],";
		output+= "\"finished\":[";
		for(Node n = finished.head;n!=null;n=n.next){
			if (n.Data.getDNF()){
				output+= "{\"bibNum\":"+Integer.toString(n.Data.getBibNum())+",\"startTime\":\""+n.Data.getStartTime()+"\",\"endTime\":\"\",\"dnf\":"+n.Data.getDNF()+"}";
			}else{
				output+= "{\"bibNum\":"+Integer.toString(n.Data.getBibNum())+",\"startTime\":\""+n.Data.getStartTime()+"\",\"endTime\":\""+n.Data.getEndTime()+"\",\"dnf\":"+n.Data.getDNF()+"}";
			}
			if (n.next!=null) output+=",";
		}
		output += "]}";
		return output;
	}

	@Override
	protected boolean raceInProgress() {
		return start!=null;
	}

	@Override
	protected ArrayList<Racer> getQueue() {
		ArrayList<Racer> queue = new ArrayList<>();
		for(Node n = queued.head; n != null; n = n.next) {
			queue.add(n.Data);
		}
		return queue;
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
		if(queued.getLength()+finished.getLength() == 0){
			return stand + "No Racers queued or finished\n";
		}
		if(queued.getLength()>0){
			stand+= "In Queue to finish:\n";
			for(Node n = queued.head;n!=null&&counter<3;n=n.next){
				stand+= Integer.toString(n.Data.getBibNum()) +" "+ time.toString()+"\n";
				++counter;
			}
		}
		counter = 0;
		if(finished.getLength() > 0){
			stand+="\nCompleted Race:\n";
			for(Node n = finished.tail;n!=null&&counter<3;n=n.prev){
				if(n.Data.getDNF()){
					stand+= Integer.toString(n.Data.getBibNum()) +" DNF\n";
				}else{
					stand+= Integer.toString(n.Data.getBibNum()) +" "+ n.Data.getTime().toString() +"\n";
				}
				++counter;
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