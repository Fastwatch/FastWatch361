package chronoSimulator;

import java.util.Scanner;

/**
 * 
 * @author Riley Mahr rwmahr@gmail.com
 * <p>
 * This class is used as a "spring board" for file and console simulation
 * It contains little logic, which should all be handles in other classes
 */

public class ChronoTimerSimulator {
	
	private static FileInput fn;
	
	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);
		String input;
		boolean exit = false;
		
		do {
			System.out.print("Enter \"F\" for file input, \"C\" for console input or \"E\" for exit: ");
			input = in.nextLine();
			
			switch(input.toUpperCase()) {
				case "F":
					fn = new FileInput();
					fn.readFile(in);
					break;
				case "C":
					// Console Input
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
}
