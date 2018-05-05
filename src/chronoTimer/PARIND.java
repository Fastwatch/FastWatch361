package chronoTimer;

import java.time.LocalTime;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class PARIND extends Run{

	IND Lane1;
	IND Lane2;
	Boolean addToOne;
	
	protected PARIND(){
		this.type = "PARIND";
		Lane1 = new IND();
		Lane2 = new IND();
		addToOne = true;
	}
	
	
	@Override
	protected String trig(LocalTime time, int channelNum) {
		String status;
		if(channelNum == 1||channelNum==2){
			status = Lane1.trig(time, channelNum);
		}else if(channelNum == 3||channelNum == 4){
			status = Lane2.trig(time, channelNum-2);
		}else{
			throw new IllegalStateException("Channel Not Supported");
		}
		return status;
	}

	@Override
	protected void swap() {
		throw new IllegalArgumentException("This Method not supported with this type of event");		
	}

	@Override
	protected String dnf() {
		throw new IllegalArgumentException("This Method not supported with this type of event");
	}

	@Override
	protected void cancel() {
		Racer r1 = Lane1.lastStart();
		Racer r2 = Lane2.lastStart();
		if(r1==null&&r2==null) throw new IllegalStateException("No racers have started yet");		
		else if (r1 == null) Lane2.cancel();
		else if (r2 == null) Lane1.cancel();
		else{
			if(r1.getStartTime().compareTo(r2.getStartTime())<= 0){
				Lane2.cancel();
			}else Lane1.cancel();
		}
	}

	@Override
	protected void num(int bibNum) {
		if(Lane1.contains(bibNum)||Lane2.contains(bibNum)) throw new IllegalArgumentException("Attempting to create duplicate racer");
		if (addToOne){
			Lane1.num(bibNum);
		}else{
			Lane2.num(bibNum);
		}
		addToOne = !addToOne;
	}

	@Override
	protected void clr(int bibNum) {
		if(Lane1.contains(bibNum)){
			Lane1.clr(bibNum);
		}else if(Lane2.contains(bibNum)){
			Lane2.clr(bibNum);
		}else throw new IllegalArgumentException("Runner Not Found");
	}

	@Override
	protected String standings(LocalTime time) {
		return "Lane 1:\n" + Lane1.standings(time) + "\nLane 2:\n" + Lane2.standings(time);
	}

	@Override
	protected void end() {
		active = false;
		Lane1.end();
		Lane2.end();
	}

	@Override
	protected void swap(int laneNum) {
		if (laneNum == 1){
			Lane1.swap();
		}else if (laneNum == 2){
			Lane2.swap();
		}else throw new IllegalArgumentException("Lane " + laneNum + " Not Supported");
	}

	@Override
	protected String dnf(int laneNum) {
		String out = "";
		if (laneNum == 1){
			out = Lane1.dnf();
		}else if (laneNum == 2){
			out = Lane2.dnf();
		}else throw new IllegalArgumentException("Lane " + laneNum + " Not Supported");
		return out;
	}

	@Override
	protected String export() {
		JsonObject obj = new JsonObject();
		JsonArray lane1 = new JsonArray();
		JsonArray lane2 = new JsonArray();
		JsonObject element;
	
		obj.addProperty("type", this.type.toString());
		// iterate through lane 1 and add it to array called queuedLane1
		for(Node n = Lane1.queued.head;n!=null;n=n.next){
			element = new JsonObject();
			element.addProperty("bibNum", n.Data.getBibNum());
			element.addProperty("startTime", "");
			element.addProperty("endTime", "");
			element.addProperty("dnf", n.Data.getDNF());
			lane1.add(element);
		}
		obj.add("queuedLane1", lane1);
		lane1 = new JsonArray();
		
		// iterate through lane 2 and add it to array called queuedLane2
		for(Node n = Lane2.queued.head;n!=null;n=n.next){
			element = new JsonObject();
			element.addProperty("bibNum", n.Data.getBibNum());
			element.addProperty("startTime", "");
			element.addProperty("endTime", "");
			element.addProperty("dnf", n.Data.getDNF());
			lane2.add(element);
		} 
		obj.add("queuedLane2", lane2);
		lane2 = new JsonArray();
		
		for(Node n = Lane1.running.head; n != null; n=n.next){
			element = new JsonObject();
			element.addProperty("bibNum", n.Data.getBibNum());
			element.addProperty("startTime", n.Data.getStartTime().toString());
			element.addProperty("endTime", "");
			element.addProperty("dnf", n.Data.getDNF());
			lane1.add(element);
		} 
		obj.add("runningLane1", lane1);
		lane1 = new JsonArray();
		
		for(Node n = Lane2.running.head; n != null; n=n.next){
			element = new JsonObject();
			element.addProperty("bibNum", n.Data.getBibNum());
			element.addProperty("startTime", n.Data.getStartTime().toString());
			element.addProperty("endTime", "");
			element.addProperty("dnf", n.Data.getDNF());
			lane2.add(element);
		} 
		obj.add("runningLane2", lane2);
		lane2 = new JsonArray();
		
		for(Node n = Lane1.complete.head;n!=null;n=n.next){
			element = new JsonObject();
			element.addProperty("bibNum", n.Data.getBibNum());
			element.addProperty("startTime",n.Data.getStartTime().toString());
			if(n.Data.getDNF() == false){
				element.addProperty("endTime", n.Data.getEndTime().toString());
			}else{
				element.addProperty("endTime", "");
			}
			element.addProperty("dnf", n.Data.getDNF());
			lane1.add(element);
		} 
		obj.add("finishedLane1", lane1);
		lane1 = new JsonArray();
		
		
		for(Node n = Lane2.complete.head;n!=null;n=n.next){
			element = new JsonObject();
			element.addProperty("bibNum", n.Data.getBibNum());
			element.addProperty("startTime",n.Data.getStartTime().toString());
			if(n.Data.getDNF() == false){
				element.addProperty("endTime", n.Data.getEndTime().toString());
			}else{
				element.addProperty("endTime", "");
			}
			element.addProperty("dnf", n.Data.getDNF());
			lane2.add(element);
		} 
		obj.add("finishedLane2", lane2);
		lane2 = new JsonArray();
		//System.out.println(obj.toString()); // debug
		return obj.toString();
	}


	
	protected boolean raceInProgress() {
		if(Lane1.raceInProgress() == true || Lane2.raceInProgress() == true)
			return true;
		return false;
	}


	
	protected ArrayList<Racer> getQueue() {
		boolean addedLane1 = false;
		ArrayList<Racer> queue = new ArrayList<>();
		Node lane1 = Lane1.queued.head;
		Node lane2 = Lane2.queued.head;
		
		while(lane1 != null && lane2 != null) {
			if (addedLane1 == false) {
				if (lane1 != null)
					queue.add(lane1.Data);

				lane1 = lane1.next;
				addedLane1 = true;
			} else {
				if (lane2 != null)
					queue.add(lane2.Data);

				lane2 = lane2.next;
				addedLane1 = false;
			}
		}
		
		if(lane1 != null)
			queue.add(lane1.Data);
		else if(lane2 != null)
			queue.add(lane2.Data);
		
		return queue;
	}
	
	protected String update(LocalTime time) {
		LinkedList queued1 = Lane1.queued;
		LinkedList running1 = Lane1.running;
		LinkedList complete1 = Lane1.complete;
		LinkedList queued2 = Lane2.queued;
		LinkedList running2 = Lane2.running;
		LinkedList complete2 = Lane2.complete;
		String timeStr = time.toString();
		String stand = "";
		int counter = 0;
		if(queued1.getLength()+running1.getLength()+complete1.getLength() == 0){
			return "No Racers Currently In Run\n";
		}
		stand += "Lane 1:		Lane 2:\n";	
		if(queued1.getLength()>0||queued2.getLength()>0){
			stand+= "In Queue to Start:\n";
			for(Node n = queued1.head, o = queued2.head;(n!=null||o!=null)&&counter<3;){
				if(n!=null&&o!=null)
					stand+= Integer.toString(n.Data.getBibNum()) +" "+ timeStr +"	"+ Integer.toString(o.Data.getBibNum()) +" "+ timeStr+"\n";
				else if(o==null){
					stand+= Integer.toString(n.Data.getBibNum()) +" "+ timeStr+"	----"+"\n";
				}else{
					stand+= "----		"+ Integer.toString(o.Data.getBibNum()) +" "+ timeStr+"\n";
				}
				if (n!=null) n=n.next;
				if (o!=null) o=o.next;
				++counter;
			}
		}
		counter = 0;
		if(running1.getLength()>0||running2.getLength()>0){
			stand+= "\nCurrently Racing:\n";
			for(Node n = running1.tail, o = running2.tail;(n!=null||o!=null)&&counter<3;){
				if(n!=null&&o!=null){
					n.Data.setFinish(time);
					o.Data.setFinish(time);
					stand+= Integer.toString(n.Data.getBibNum()) +" "+ n.Data.getTime().toString()+"	"+ Integer.toString(o.Data.getBibNum()) +" "+ o.Data.getTime().toString()+"\n";
					n.Data.setFinish(null);
					o.Data.setFinish(null);
				}else if(o==null){
					n.Data.setFinish(time);
					stand+= Integer.toString(n.Data.getBibNum()) +" "+ n.Data.getTime().toString()+"	----"+"\n";
					n.Data.setFinish(null);
				}else{
					o.Data.setFinish(time);
					stand+= "----		"+ Integer.toString(o.Data.getBibNum()) +" "+ o.Data.getTime().toString()+"\n";
					o.Data.setFinish(null);
				}
				if (n!=null) n=n.prev;
				if (o!=null) o=o.prev;
				++counter;
			}
		}
		counter = 0;
		if(complete1.getLength()>0||complete2.getLength()>0){
			stand+= "\nCompleted Race:\n";
			for(Node n = complete1.tail, o = complete2.tail;(n!=null||o!=null)&&counter<3;){
				if(n!=null&&o!=null){
					if(n.Data.getDNF()){
						stand+= Integer.toString(n.Data.getBibNum()) +" DNF	";
					}else{
						stand+= Integer.toString(n.Data.getBibNum()) +" "+ n.Data.getTime().toString();
					}
					stand +="	";
					if(o.Data.getDNF()){
						stand+= Integer.toString(o.Data.getBibNum()) +" DNF\n";
					}else{
						stand+= Integer.toString(o.Data.getBibNum()) +" "+ o.Data.getTime().toString() +"\n";
					}
				}else if(o==null){
					if(n.Data.getDNF()){
						stand+= Integer.toString(n.Data.getBibNum()) +" DNF		----\n";
					}else{
						stand+= Integer.toString(n.Data.getBibNum()) +" "+ n.Data.getTime().toString() +"	----\n";
					}
				}else{
					if(o.Data.getDNF()){
						stand+= "----		"+Integer.toString(o.Data.getBibNum()) +" DNF\n";
					}else{
						stand+= "----		"+ Integer.toString(o.Data.getBibNum()) +" "+ o.Data.getTime().toString() +"\n";
					}
				}
				if (n!=null) n=n.prev;
				if (o!=null) o=o.prev;
				++counter;
			}
		
		}	
		return stand;
	}

}
