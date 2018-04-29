package chronoTimer;

import java.time.LocalTime;
import java.util.ArrayList;

public class PARGRP extends Run{
	
	LocalTime start;
	LinkedList finished;
	LinkedList queued;
	int tempNum;
	Node pointer;
	
	protected PARGRP(){
		this.type="PARGRP";
		start=null;
		finished = new LinkedList();
		queued = new LinkedList();
		tempNum=0;
		pointer=null;
		active = true;
	}

	@Override
	protected String trig(LocalTime time, int channelNum) {

	}

	@Override
	protected void swap() {

	}

	@Override
	protected void swap(int laneNum) {
	
	}

	@Override
	protected String dnf() {

	}

	@Override
		return dnf();
	}

	@Override
	protected void cancel() {
	}

	@Override
	protected void num(int bibNum) {
	}

	@Override
	protected void clr(int bibNum) {
	}

	@Override
	protected String standings(LocalTime time) {
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

	}

	@Override
	protected boolean raceInProgress() {
		return start!=null;
	}

	@Override
	protected ArrayList<Racer> getQueue() {

	}
	
	protected boolean contains(int bibNum){
	}
	
	protected String update(LocalTime time) {

	}
}