package chronoTimer;

import java.time.LocalTime;
import java.util.ArrayList;

class IND extends Run {
	
	LinkedList queued;
	LinkedList running;
	LinkedList complete;

	protected IND(){
		this.type = "IND";
		queued = new LinkedList();
		running = new LinkedList();
		complete = new LinkedList();
		active = true;
	}
	
	/**
	 * Trigger function for handling sensor input
	 * 
	 * @param time current system time trigger takes place
	 * @param channelNum Channel that is triggered
	 */
	@Override
	protected String trig(LocalTime time, int channelNum) {
		String status;
		if(channelNum == 1){
			if(queued.getLength() == 0) throw new IllegalStateException("No Racers waiting to start");
			Node n = queued.removeStart();
			n.Data.setStart(time);
			status = "Racer " + n.Data.getBibNum() +" is now racing.";
			running.addEnd(n);
		}else if(channelNum == 2){
			if(running.getLength() == 0) throw new IllegalStateException("No racers on course");
			Node n = running.removeStart();
			n.Data.setFinish(time);
			status = "Racer " + n.Data.getBibNum() +" completed their run in: " +n.Data.getTime();
			complete.addEnd(n);
		}else{
			throw new IllegalStateException("Channel Not Supported");
		}
		return status;
	}

	/**
	 * Swap Method for swapping the 2 racers at the front of the running queue
	 */
	@Override
	protected void swap() {
		if(running.getLength() < 2)throw new IllegalArgumentException("Not Enough Racers to Swap. Racers must be on course to swap");
		Node n1 = running.removeStart();
		Node n2 = running.removeStart();
		running.addStart(n1);
		running.addStart(n2);

	}

	/**
	 * Used to trigger DNF for the next racer to finish
	 */
	@Override
	protected String dnf() {
		if(running.getLength() == 0) throw new IllegalStateException("No racers on course");
		Node n = running.removeStart();
		n.Data.setDNF(true);
		complete.addEnd(n);
		return "Racer " + n.Data.getBibNum() + " did not finish."; 
	}
	
	/**
	 * Function to cancel last start signal and send the racer back to the beginning of the queued list
	 */
	@Override
	protected void cancel() {
		if(running.getLength() == 0) throw new IllegalStateException("No racers on course");
		Node n = running.removeEnd();
		n.Data.setStart(null);
		queued.addStart(n);
	}

	/**
	 * num function to generate new racers for this run
	 * 
	 * @param bibNum of racer to be added to end of queued racers
	 */
	@Override
	protected void num(int bibNum) {
		// TODO Auto-generated method stub
		if(contains(bibNum))throw new IllegalArgumentException("Attempting to create duplicate racer");
		if (bibNum<0 ||bibNum>=1000)throw new IllegalArgumentException("Bib Number must be between 000 and 999.");
		Node n = new Node(new Racer(bibNum));
		queued.addEnd(n);
	}

	/**
	 * num function to remove racer from this run
	 * 
	 * @param bibNum of racer to be removed from run
	 */
	protected void clr(int bibNum){
		if(queued.contains(bibNum)){
			queued.remove(bibNum);
		}else if(running.contains(bibNum)){
			running.remove(bibNum);
		}else if(complete.contains(bibNum)){
			complete.remove(bibNum);
		}else throw new IllegalArgumentException("Runner Not Found");
	}

	/**
	 * Returns current status of race including all racers in all 3 stages of the race, Finished, Running, and Waiting to Start
	 * 
	 * @param time current system time used to calculate current time of racers
	 * @return formatted string with all racers bib numbers times and status
	 */
	@Override
	protected String standings(LocalTime time){
		
		String stand = "";
		if(queued.getLength()+running.getLength()+complete.getLength() == 0){
			return "No Racers Currently In Run\n";
		}
		if(active){
			if(queued.getLength()>0){
				stand+= "In Queue to Start:\n";
				for(Node n = queued.head;n!=null;n=n.next){
					stand+= Integer.toString(n.Data.getBibNum()) +" "+ time.toString()+"\n";
				}
			}
			if(running.getLength() > 0){
				stand+="\nCurrently Racing:\n";
				for(Node n = running.head;n!=null;n=n.next){
					n.Data.setFinish(time);
					stand+= Integer.toString(n.Data.getBibNum()) +" "+ n.Data.getTime().toString() +"\n";
					n.Data.setFinish(null);
				}
			}
			if(complete.getLength() > 0){
				stand+="\nCompleted Race:\n";
				for(Node n = complete.head;n!=null;n=n.next){
					if(n.Data.getDNF()){
						stand+= Integer.toString(n.Data.getBibNum()) +" DNF\n";
					}else{
						stand+= Integer.toString(n.Data.getBibNum()) +" "+ n.Data.getTime().toString() +"\n";
					}
				}
			}
		}else{
			if(queued.getLength()>0){
				stand += "Racers who did not start:\n";
				for(Node n = queued.head;n!=null;n=n.next){
					stand+= Integer.toString(n.Data.getBibNum())+"\n";
				}
			}
			if(complete.getLength() > 0){
				stand+="\nCompleted Race:\n";
				for(Node n = complete.head;n!=null;n=n.next){
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
	
	/**
	 * Function to handle graceful ending of race.
	 */
	protected void end(){
		while(running.getLength()>0){
			dnf();
		}
		active = false;
	}



	@Override
	protected void swap(int laneNum) {
		swap();
	}

	@Override
	protected String dnf(int laneNum) {
		return dnf();
	}

	@Override
	protected String export() {
		String output = "{\"type\":\"IND\",";
		output+= "\"queued\":[";
		for(Node n = queued.head;n!=null;n=n.next){
			output+= "{\"bibNum\":"+Integer.toString(n.Data.getBibNum())+",\"startTime\":\"\",\"endTime\":\"\",\"dnf\":"+n.Data.getDNF()+"}";
			if (n.next!=null) output+=",";
		}
		output += "],";
		output+= "\"running\":[";
		for(Node n = running.head;n!=null;n=n.next){
			output+= "{\"bibNum\":"+Integer.toString(n.Data.getBibNum())+",\"startTime\":\""+n.Data.getStartTime()+"\",\"endTime\":\"\",\"dnf\":"+n.Data.getDNF()+"}";
			if (n.next!=null) output+=",";
		}
		output += "],";
		output+= "\"finished\":[";
		for(Node n = complete.head;n!=null;n=n.next){
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
	
	protected boolean contains(int bibNum){
		return (queued.contains(bibNum)||running.contains(bibNum)||complete.contains(bibNum));
	}
	
	protected Racer lastStart(){
		if (running.tail==null) return null;
		return running.tail.Data;
	}
	@Override
	protected boolean raceInProgress() {
		if(this.running.getLength() > 0 || this.complete.getLength() > 0)
			return true;
		return false;
	}
	@Override
	protected ArrayList<Racer> getQueue() {
		ArrayList<Racer> queue = new ArrayList<>();
		for(Node n = queued.head; n != null; n = n.next) {
			queue.add(n.Data);
		}
		return queue;
	}
	
	protected String update(LocalTime time){
		
		String stand = "";
		int counter = 0;
		if(queued.getLength()+running.getLength()+complete.getLength() == 0){
			return "No Racers Currently In Run\n";
		}
		if(queued.getLength()>0){
			stand+= "In Queue to Start:\n";
			for(Node n = queued.head;n!=null&&counter<3;n=n.next){
				stand+= Integer.toString(n.Data.getBibNum()) +" "+ time.toString()+"\n";
				++counter;
			}
		}
		counter =0;
		if(running.getLength() > 0){
			stand+="\nCurrently Racing:\n";
			for(Node n = running.tail;n!=null&&counter<3;n=n.prev){
				n.Data.setFinish(time);
				stand+= Integer.toString(n.Data.getBibNum()) +" "+ n.Data.getTime().toString() +"\n";
				n.Data.setFinish(null);
				++counter;
			}
		}
		counter =0;
		if(complete.getLength() > 0){
			stand+="\nCompleted Race:\n";
			for(Node n = complete.tail;n!=null&&counter<3;n=n.prev){
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
}
