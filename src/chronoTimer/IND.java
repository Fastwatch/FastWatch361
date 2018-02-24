package chronoTimer;

import java.time.LocalTime;

class IND extends Run {
	
	LinkedList queued;
	LinkedList running;
	LinkedList complete;
	
	private class Node{
		Node next;
		Node prev;
		protected Racer Data;
		
		protected Node(Racer r){
			this.Data = r;
		}
	}
	
	protected IND(){
		this.type = "IND";
		queued = new LinkedList();
		running = new LinkedList();
		complete = new LinkedList();
	}
	@Override
	protected void trig(LocalTime time, int channelNum) {
		if(channelNum == 1){
			if(queued.getLength() == 0) throw new IllegalStateException("No Racers waiting to start");
			Node n = queued.removeStart();
			n.Data.setStart(time);
			running.addEnd(n);
		}else if(channelNum == 2){
			if(running.getLength() == 0) throw new IllegalStateException("No racers on course");
			Node n = running.removeStart();
			n.Data.setFinish(time);
			complete.addEnd(n);
		}else{
			throw new IllegalStateException("Channel Not Supported");
		}

	}

	@Override
	protected void swap() {
		if(running.getLength() < 2)throw new IllegalArgumentException("Not Enough Racers to Swap");
		Node n1 = running.removeStart();
		Node n2 = running.removeStart();
		running.addStart(n1);
		running.addStart(n2);

	}

	@Override
	protected void dnf() {
		if(running.getLength() == 0) throw new IllegalStateException("No racers on course");
		Node n = running.removeStart();
		n.Data.setDNF(true);
		complete.addEnd(n);
	}

	@Override
	protected void cancel() {
		if(running.getLength() == 0) throw new IllegalStateException("No racers on course");
		Node n = running.removeEnd();
		n.Data.setStart(null);
		queued.addStart(n);
	}

	@Override
	protected void num(int bibNum) {
		// TODO Auto-generated method stub
		if(queued.contains(bibNum)||running.contains(bibNum)||complete.contains(bibNum))throw new IllegalStateException("Attempting to create duplicate racer");
		Node n = new Node(new Racer(bibNum));
		queued.addEnd(n);
	}

	protected void clr(int bibNum){
		if(queued.contains(bibNum)){
			queued.remove(bibNum);
		}else if(running.contains(bibNum)){
			running.remove(bibNum);
		}else if(complete.contains(bibNum)){
			complete.remove(bibNum);
		}else throw new IllegalArgumentException("Runner Not Found");
	}

	@Override
	protected String standings(LocalTime time){
		String stand= "";
		for(Node n = queued.head;n!=null;n=n.next){
			stand+= Integer.toString(n.Data.getBibNum()) +" "+ time.toString()+"\n";
		}
		for(Node n = running.head;n!=null;n=n.next){
			n.Data.setFinish(time);
			stand+= Integer.toString(n.Data.getBibNum()) +" "+ n.Data.getTime().toString() +"\n";
			n.Data.setFinish(null);
		}
		for(Node n = complete.head;n!=null;n=n.next){
			if(n.Data.getDNF()){
				stand+= Integer.toString(n.Data.getBibNum()) +" DNF";
			}else{
				stand+= Integer.toString(n.Data.getBibNum()) +" "+ n.Data.getTime().toString() +"\n";
			}
			
		}
		return stand;
	}

	private class LinkedList{
		
		Node head;
		Node tail;
		private int count;
		
		protected LinkedList(){
			head=null;
			tail = null;
			count = 0;
		}
		
		protected void addEnd(Node n){
			if(tail==null){
				head = n;
				tail = head;
				++count;
			}else{
				n.prev = tail;
				tail.next = n;
				tail = n;
				++count;
			}
		}
		
		protected void addStart(Node n){
			if(tail==null){
				addEnd(n);
			}else{
				n.next = head;
				head.prev = n;
				head = n;
				++count;
			}
		}
		
		protected boolean remove(int bibNum){
			Node n = head;
			while (n!=null){
				if(n.Data.getBibNum()==bibNum){
					n.prev.next = n.next;
					n.next.prev = n.prev;
					n.next = null;
					n.prev = null;
					n.Data = null;
					--count;
					return true;
				}
			}
			return false;
		}
		
		protected boolean contains(int bibNum){
			for(Node n = head; n!=null; n=n.next){
				if(n.Data.getBibNum()==bibNum)
					return true;
			}
			return false;
		}
		
		protected int getLength(){
			return count;
		}
		
		protected Node removeStart(){
			if(count == 1){
				Node n = head;
				head = null;
				--count;
				return n;
			}
			Node n = head;
			head = n.next;
			head.prev = null;
			n.next = null;
			--count;
			return n;
		}
		
		protected Node removeEnd(){
			Node n = tail;
			tail = n.prev;
			tail.next = null;
			n.prev = null;
			--count;
			return n;
		}
	}
	
	
}
