package chronoTimer;

import java.time.LocalTime;
import java.util.ArrayList;


public abstract class Run {
	protected String type;
	protected boolean active;
	
	protected abstract String trig(LocalTime time, int channelNum);
	
	protected abstract void swap();
	
	protected abstract void swap(int laneNum);
	
	protected abstract String dnf();
	
	protected abstract String dnf(int laneNum);
	
	protected abstract void cancel();
	
	protected abstract void num(int bibNum);
	
	protected abstract void clr(int bibNum);
	
	protected abstract String standings(LocalTime time);
	
	protected abstract void end();
	
	protected abstract String export();
	
	protected abstract boolean raceInProgress();
	
	protected abstract ArrayList<Racer> getQueue();
	
	protected abstract String update(LocalTime time);
	
	/**
	 * Node Class for use in Linked lists for keeping track of racers
	 * @author Philip Sauvey
	 *
	 */
	protected class Node{
		Node next;
		Node prev;
		protected Racer Data;
		
		protected Node(Racer r){
			this.Data = r;
		}
	}
	
	/**
	 * Custom Internal Linked list class used for the queued, running, and completed
	 * sets in the different run types.
	 * 
	 * Supports constant time operations for adding nodes to the head and tail, 
	 * and constant time removal from head tail or O(n) time removal by specific racer number
	 * @author Philip Sauvey
	 *
	 */
	protected class LinkedList{
		
		Node head;
		Node tail;
		private int count;
		
		protected LinkedList(){
			head=null;
			tail = null;
			count = 0;
		}
		
		/**
		 * Add End function adds a node to the end of the linked list as a constant time operation
		 * 
		 * @param n Node to add to end of list.
		 */
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
		
		/**
		 * addStart function inserts a node at the start of the linked list as a constant time operation
		 * 
		 * @param n Node to add to start of list.
		 */
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
		
		/**
		 * This function removes the first node from the linked list whose racer matches the bib number supplied
		 * 
		 * @param bibNum: The bib number of the racer that is to be removed
		 * @return True if the bibNum is found and removed, false otherwise
		 */
		protected boolean remove(int bibNum){
			Node n = head;
			while (n!=null){
				if(n.Data.getBibNum()==bibNum){
					if(count == 1){
						head = null;
						tail = null;
					}else if(n.equals(tail)){
						n.prev.next = n.next;
						tail = n.prev;
					}else if(n.equals(head)){
						n.next.prev = n.prev;
						head = n.next;
					}else{
						n.prev.next = n.next;
						n.next.prev = n.prev;
					}
					n.next = null;
					n.prev = null;
					n.Data = null;
					--count;
					return true;
				}
				n=n.next;
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
				tail = null;
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
			if(count == 1){
				Node n = head;
				head = null;
				tail = null;
				--count;
				return n;
			}
			Node n = tail;
			tail = n.prev;
			tail.next = null;
			n.prev = null;
			--count;
			return n;
		}
	}
}
