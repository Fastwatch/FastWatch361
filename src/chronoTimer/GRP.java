package chronoTimer;

import java.time.LocalTime;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

	@Override
	protected void swap() {
		if(startTime==null) throw new IllegalStateException("Race hasn't started yet");
		if(queued.getLength() < 2) throw new IllegalArgumentException("No Enough Racers queued to Swap.");
		Node n1 = queued.removeStart();
		Node n2 = queued.removeStart();
		queued.addStart(n1);
		queued.addStart(n2);
	}

	@Override
	protected void swap(int laneNum) {
		swap();		
	}

	@Override
	protected String dnf() {
		if(startTime==null) throw new IllegalStateException("Race hasn't started yet");
		if(queued.getLength()==0) throw new IllegalArgumentException("Not Queued Racers queued to DNF.");
		Node n = queued.removeStart();
		n.Data.setDNF(true);
		finished.addEnd(n);
		return "Racer " + n.Data.getBibNum() + " did not finish."; 
	}

	@Override
	protected String dnf(int laneNum) {
		return dnf();
	}

	@Override
	protected void cancel() {
		if(startTime==null) throw new IllegalStateException("Race hasn't started yet");
		if(finished.getLength()!=0) throw new IllegalStateException("Cannot cancel start after a racer has finished.");
		startTime = null;
	}

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

	@Override
	protected void clr(int bibNum) {
		if(!queued.contains(bibNum))throw new IllegalArgumentException("Runner Not Found");
		queued.remove(bibNum);
	}

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

	@Override
	protected boolean raceInProgress() {
		return startTime!=null;
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
		return queued.contains(bibNum)||finished.contains(bibNum);
	}
	
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
