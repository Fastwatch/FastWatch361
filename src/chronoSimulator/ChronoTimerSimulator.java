package chronoSimulator;

import java.util.Scanner;

import chronoTimer.ChronoTimer;

/**
 * 
 * @author Riley Mahr rwmahr@gmail.com
 * 
 * This class is used as a "spring board" for file and console simulation
 * It contains little logic, which should all be handles in other classes
 */

public class ChronoTimerSimulator {
	
	private ChronoTimer ct;
	
	private static FileInput fn;
	
	public void start() {

		Scanner in = new Scanner(System.in);
		
		ct = new ChronoTimer();
		
		String input;
		boolean exit = false;
		
		do {
			System.out.print("Enter \"F\" for file input, \"C\" for console input or \"E\" for exit: ");
			input = in.nextLine();
			
			switch(input.toUpperCase()) {
				case "F":
					fn = new FileInput();
					fn.readFile(in, ct);
					break;
				case "C":
					ConsoleInput cn = new ConsoleInput();
					cn.readConsole(ct);
					break;
				case "E":
					exit = true;
					break;
				default:
					System.out.println("Invalid Input, please enter a valid character");
			}
		
		} while (!exit);
		
		System.out.println("\nGoodbye..");
	}
	
	public void execute (String s) {
		System.out.println(s);
	}
}
