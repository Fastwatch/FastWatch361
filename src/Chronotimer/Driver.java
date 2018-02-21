package Chronotimer;

import java.util.Scanner;  
public class Driver {

	
	public static void main(String[] args){
		
		Racer r1 = new Racer(1234);
	
		//Simple test run on the Racer class, Racer class may subject to change when Time object is implemented.
		
		r1.startTime();
		System.out.println("Recorded in military time: HH:mm:ss.sss");
		System.out.print("Racer: '" + r1.getBibNum() + "' start time is ");
		r1.startTime();
		System.out.println(r1.getStartTime()); // starts the timer
		
		System.out.println("Press ENTER to stop time.");
		Scanner input = new Scanner(System.in);
		input.nextLine();
		r1.endTime();; // stops the timer
		
		System.out.print("Racer: '" + r1.getBibNum() + "' end time is ");
		r1.endTime();
		System.out.println(r1.getEndTime());
		
		System.out.print("\nRacer: '" + r1.getBibNum() + "' duration time is ");
		System.out.println(r1.getTime());
		
		input.close();
	}
	
	
}
