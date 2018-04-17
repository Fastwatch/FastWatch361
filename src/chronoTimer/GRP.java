package chronoTimer;

import java.time.LocalTime;
import java.util.ArrayList;

public class GRP extends Run{
	
	LocalTime start;
	LinkedList finished;
	LinkedList queued;
	int tempNum;
	Node pointer;
	
	protected GRP(){
		this.type="GRP";
		start=null;
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
			if(start!=null) throw new IllegalStateException("Group race already started.");
			start = time;
			status = "Group Race has now started";
		}else if(channelNum == 2){
			if (start==null) throw new IllegalStateException("Race has not started.");
			if (queued.getLength() == 0){		
				Node n = new Node(new Racer(tempNum,start,time));
				status = "Temp Racer " + tempNum +" completed their run in: " +n.Data.getTime();
				if(pointer==null){
					pointer = n;
				}
				finished.addEnd(n);
				tempNum++;
			}else{
				Node n = queued.removeStart();
				n.Data.setStart(start);
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
		if(start==null) throw new IllegalStateException("Race hasn't started yet");
		if(queued.getLength() < 2) throw new IllegalArgumentException("Not Enough Racers queued to Swap.");
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
		if(start==null) throw new IllegalStateException("Race hasn't started yet");
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
		if(start==null) throw new IllegalStateException("Race hasn't started yet");
		if(finished.getLength()!=0) throw new IllegalStateException("Cannot cancel start after a racer has finished.");
		start = null;
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
	}

	@Override
	protected void clr(int bibNum) {
		if(!queued.contains(bibNum))throw new IllegalArgumentException("Runner Not Found");
		queued.remove(bibNum);
	}

	@Override
	protected String standings(LocalTime time) {
		if (start==null)return "Race has not started\n";
		String stand = "Group Start Time: " + start.toString()+"\n";
		
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
		return queued.contains(bibNum)||finished.contains(bibNum);
	}
}
