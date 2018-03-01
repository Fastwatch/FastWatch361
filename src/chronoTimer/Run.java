package chronoTimer;

import java.time.LocalTime;

public abstract class Run {
	protected String type;
	protected boolean active;
	
	protected abstract String trig(LocalTime time, int channelNum);
	
	protected abstract void swap();
	
	protected abstract String dnf();
	
	protected abstract void cancel();
	
	protected abstract void num(int bibNum);
	
	protected abstract void clr(int bibNum);
	
	protected abstract String standings(LocalTime time);
	
	protected abstract void end();
}
