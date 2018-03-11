package chronoTimer;

import java.time.LocalTime;

public class PARIND extends Run{

	IND Lane1;
	IND Lane2;
	boolean add1Next;
	
	protected PARIND(){
		Lane1 = new IND();
		Lane2 = new IND();
		add1Next = true;
	}
	
	
	@Override
	protected String trig(LocalTime time, int channelNum) {
		String status;
		if(channelNum == 1||channelNum==2){
			status = Lane1.trig(time, channelNum);
		}else if(channelNum == 3||channelNum == 4){
			status = Lane1.trig(time, channelNum-2);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void num(int bibNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void clr(int bibNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String standings(LocalTime time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String swap(int laneNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String dnf(int laneNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String export() {
		// TODO Auto-generated method stub
		return null;
	}

}
