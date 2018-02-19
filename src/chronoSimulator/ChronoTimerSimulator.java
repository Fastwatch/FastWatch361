package chronoSimulator;

import java.util.Scanner;

public class ChronoTimerSimulator {
	
	private static FileInput fn;
	
	public static void main(String[] args) {
		// Begin execution of simulator
		// use command prompt or input file??
		Scanner in = new Scanner(System.in);
		String input;
		boolean exit = false;
		
		do {
			System.out.print("Enter \"F\" for file input, \"C\" for console input or \"E\" for exit: ");
			input = in.nextLine();
			
			switch(input.toUpperCase()) {
				case "F":
					//do something
					fn = new FileInput();
					fn.readFile(in);
					break;
				case "C":
					//do something else
					System.out.println("C y'all");
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
