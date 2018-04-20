package chronoTimer;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;

import chronoSimulator.ChronoTimerSimulator;

/**
 * 
 * @author Andrew Krill ATKrill@uwm.edu
 * 
 * 
 * 
 */


public class DispUpdate implements Runnable {

	
	public ChronoTimer ct;
	Run curRun;
	
	boolean kill = false;

    public void run() {
        while (kill == false) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            	curRun = ct.getCurrentRun();
            	if ((curRun)!=null) {
                	ct.DispUpdate(curRun.update(ct.parseTime(getTime())));            		
            	}
            };
        }
        return;
    }

    //Sets kill to true to exit run loop
    public void kill(){
        kill = true;
      }
    
    public String DispUpdate(String time){
    	if (curRun==null) return "No Active Run";
    	return curRun.update(ct.parseTime(time));
    }
    
    
   	private String getTime() {
		//Time Formated as HH:hh:ss
		//That is, Hour:Min:sec 
		String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
		return timeStamp;
	}
}

