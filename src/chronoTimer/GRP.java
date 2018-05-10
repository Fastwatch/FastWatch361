package chronoTimer;

import java.time.LocalTime;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * The GRP Race supports a single start with multiple distinct finish times from racers on the same channel.
 * 
 * Racers who finish are either assigned a temporary number, or a number from a queue of bib numbers added to the race.
 * If a racer is assigned a temporary bib number they later can be assigned a normal number using the num command
 * Since there can be overlap in the temporary bib numbers and the actual assigned bib numbers.
 * It is recommended that the Actual bib numbers used for a race start at n+1 where n is the number of racers competing
 * if possible, to minimize overlap and potential confusion.
 * 
 * Channel 1: Start Signal for Group
 * Channel 2: Finish Signal for racer
 * 
 * @author Fue Her, Philip Sauvey
 */
public class GRP extends Run{
	
	LocalTime startTime;
	LinkedList finished;
	LinkedList queued;
	int tempNum;
	ArrayList<Integer> usedBibNum = new ArrayList<>(); // collection of bib numbers user gave to us
	Node pointer;
	
	protected GRP(){
		this.type="GRP";
		startTime=null;
		finished = new LinkedList();
		queued = new LinkedList();
		tempNum=0;
		pointer=null;
		active = true;
	}

	/**
	 * Trigger function for handling sensor input
	 * 
	 *  Channel 1: Start Signal for Group
	 *  Channel 2: Finish Signal for racer
	 * 
	 * @param time current system time trigger takes place
	 * @param channelNum Channel that is triggered
	 */
	@Override
	protected String trig(LocalTime time, int channelNum) {
		String status;
		if(channelNum == 1){
			if(startTime!=null) throw new IllegalStateException("Group race already started.");
			startTime = time;
			status = "Group Race has now started";
		}else if(channelNum == 2){
			if (startTime==null) throw new IllegalStateException("Race has not started.");
			if (queued.getLength() == 0){
				while(usedBibNum.contains(tempNum)){
					tempNum++;
				}
				Node n = new Node(new Racer(tempNum,startTime,time));
				status = "Temp Racer " + tempNum +" completed their run in: " +n.Data.getTime();
				if(pointer==null){
					pointer = n;
				}
				finished.addEnd(n);
				tempNum++;
			}else{
				Node n = queued.removeStart();
				n.Data.setStart(startTime);
				n.Data.setFinish(time);
				status = "Racer " + n.Data.getBibNum() +" completed their run in: " +n.Data.getTime();
				finished.addEnd(n);
			}
		}else{
			throw new IllegalStateException("Channel Not Supported");
		}
		return status;
	}

	/**
	 * Swap Method for swapping the 2 racers at the front of the queued numbers
	 */
	@Override
	protected void swap() {
		if(startTime==null) throw new IllegalStateException("Race hasn't started yet");
		if(queued.getLength() < 2) throw new IllegalArgumentException("No Enough Racers queued to Swap.");
		Node n1 = queued.removeStart();
		Node n2 = queued.removeStart();
		queued.addStart(n1);
		queued.addStart(n2);
	}

	/**
	 * This function is present so that other race types may take advantage of swapping by lane
	 * This race type does not need a lane number to swap so if called it just calls the basic swap method without a lane
	 * 
	 * @param laneNum Lane number to swap (not used for this race type)
	 */
	@Override
	protected void swap(int laneNum) {
		swap();		
	}

	/**
	 * Used to trigger DNF for the next racer in the queue.
	 * Cannot DNF a racer who would finish with a temporary bib number.
	 */
	@Override
	protected String dnf() {
		if(startTime==null) throw new IllegalStateException("Race hasn't started yet");
		if(queued.getLength()==0) throw new IllegalArgumentException("Not Queued Racers queued to DNF.");
		Node n = queued.removeStart();
		n.Data.setDNF(true);
		finished.addEnd(n);
		return "Racer " + n.Data.getBibNum() + " did not finish."; 
	}

	/**
	 * This function is present in the interface so that other race types may take advantage of DNFing by lane
	 * This race type does not need a lane number to DNF so if called it just calls the basic DNF method without a lane
	 * 
	 * @param laneNum Lane number to DNF (not used for this race type)
	 */
	@Override
	protected String dnf(int laneNum) {
		return dnf();
	}

	/**
	 * Function to cancel the group start time.
	 * Cannot cancel start if a racer has already completed the course.
	 */
	@Override
	protected void cancel() {
		if(startTime==null) throw new IllegalStateException("Race hasn't started yet");
		if(finished.getLength()!=0) throw new IllegalStateException("Cannot cancel start after a racer has finished.");
		startTime = null;
	}

	/**
	 * num function to either assign a number to a previously finished racer who currently has a temporary num
	 * or if all racers have already been assigned a num
	 * Adds the number to a queue of numbers that will be assigned to the racers as they finish.
	 * 
	 * @param bibNum of racer to be added to end of queued racers
	 */
	@Override
	protected void num(int bibNum) {
		if(contains(bibNum))throw new IllegalArgumentException("Attempting to create duplicate racer");
		if (bibNum<0 ||bibNum>=1000)throw new IllegalArgumentException("Bib Number must be between 000 and 999.");
		if(pointer!=null){
			pointer.Data.setBib(bibNum);
			pointer = pointer.next;
		}else{
			Node n = new Node(new Racer(bibNum));
			queued.addEnd(n);
		}	
		usedBibNum.add(bibNum);
	}

	/**
	 * function to remove racer from the queue of bib numbers for future finishers
	 * 
	 * @param bibNum of racer to be removed from the queue
	 */
	@Override
	protected void clr(int bibNum) {
		if(!queued.contains(bibNum))throw new IllegalArgumentException("Runner Not Found");
		queued.remove(bibNum);
	}

	/**
	 * Returns current status of race including all racers who have finished,
	 * and a list of any potential bib numbers waiting to be assigned
	 * 
	 * @param time current system time used to calculate current time of racers
	 * @return formatted string with all racers' bib numbers, times, and status
	 */
	@Override
	protected String standings(LocalTime time) {
		if (startTime==null)return "Race has not started\n";
		String stand = "Group Start Time: " + startTime.toString()+"\n";
		
		if(queued.getLength()+finished.getLength() == 0){
			return stand + "No Racers queued or finished\n";
		}
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

	/**
	 * Function to handle graceful ending of race.
	 * DNFs all waiting bib numbers
	 */
	@Override
	protected void end() {
		active = false;
		while(queued.getLength()>0){
			dnf();
		}
		for(Node n = finished.head; n != null; n = n.next){
			
			n.Data.setStart(startTime);
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
		String time = "";
	
		obj.addProperty("type", "GRP");
		for(Node n = queued.head;n!=null;n=n.next){
			element = new JsonObject();
			element.addProperty("bibNum", n.Data.getBibNum());
			element.addProperty("startTime", time);
			element.addProperty("endTime", "");
			element.addProperty("dnf", n.Data.getDNF());
			array.add(element);
		} 
		
		obj.add("queued", array);
		array = new JsonArray();
		
		for(Node n = finished.head;n!=null;n=n.next){
			element = new JsonObject();
			element.addProperty("bibNum", n.Data.getBibNum());
			element.addProperty("startTime", n.Data.getStartTime().toString());
			if(n.Data.getDNF() == false){
				element.addProperty("endTime", n.Data.getEndTime().toString());
			}else{
				element.addProperty("endTime", "");
			}
			element.addProperty("dnf", n.Data.getDNF());
			array.add(element);
		} 
		obj.add("finished", array);
		//System.out.println(obj.toString()); // debug
		return obj.toString();
	}

	/**
	 * Tests to see if a race is already running
	 * 
	 * @return True if race has started, false otherwise
	 */
	@Override
	protected boolean raceInProgress() {
		return startTime!=null;
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
		for(Node n = queued.head; n != null; n = n.next) {
			queue.add(n.Data);
		}
		return queue;
	}
	
	/**
	 * Checks every leg of the race for a particular bibNum
	 * 
	 * @param bibNum The bibNumber of the racer to search for
	 * @return True if bibNumber is in race, false otherwise
	 */
	protected boolean contains(int bibNum){
		return queued.contains(bibNum)||finished.contains(bibNum);
	}
	
	/**
	 * This function generates a formatted string for use of the display of the GUI/Hardware
	 * It provides a live look at the status of a race.
	 * 
	 * @param time LocalTime object representing current time of the race.
	 */
	protected String update(LocalTime time) {
		if (startTime==null)return "Race has not started\n";
		String stand = "Group Start Time: " + startTime.toString()+"\n";
		LocalTime duration = time.minusHours(startTime.getHour());
		duration = duration.minusMinutes(startTime.getMinute());
		duration = duration.minusSeconds(startTime.getSecond());
		duration = duration.minusNanos(startTime.getNano());
		int counter = 0;
		if(queued.getLength()+finished.getLength() == 0){
			return stand + "No Racers queued or finished\n";
		}
		if(queued.getLength()>0){
			stand+= "In Queue to finish:\n";
			for(Node n = queued.head;n!=null&&counter<3;n=n.next){
				stand+= Integer.toString(n.Data.getBibNum()) +" "+ duration.toString()+"\n";
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

}
