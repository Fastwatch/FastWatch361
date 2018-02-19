package chronoTimer;

import java.util.Scanner;  
public class Driver {

	
	public static void main(String[] args){
		
		Racer r1 = new Racer(1234);
		
		
		r1.startTime();
		System.out.println("Recorded in military time: HH:mm:ss.sss");
		System.out.print("Racer: '" + r1.getID() + "' start time is ");
		System.out.println(r1.getStartTime()); // starts the timer
		
		System.out.println("Press ENTER to stop time.");
		Scanner input = new Scanner(System.in);
		input.nextLine();
		r1.stoptime(); // stops the timer
		
		System.out.print("Racer: '" + r1.getID() + "' end time is ");
		System.out.println(r1.getStopTime());
		
		System.out.print("\nRacer: '" + r1.getID() + "' duration time is ");
		System.out.println(r1.getDuration());
		
		input.close();
	}
	
	
}
