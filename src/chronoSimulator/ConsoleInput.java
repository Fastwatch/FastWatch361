package chronoSimulator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import chronoTimer.ChronoTimer;
/**
 * @author Andrew Krill atkrill@uwm.edu
 */ 
public class ConsoleInput {
	
	/**
	 * Base class to read in console commands
	 */
	
	public boolean readConsole(ChronoTimer ct) {
		boolean isRunning = true;
		Scanner scanner = new Scanner(System.in);
		String rawInput;
		String input;	
		
		System.out.println("Welcome to the Console Input! Enter your command, or type \"exit\" to exit the console mode!");
		
		while (isRunning) {
			rawInput = scanner.nextLine();
			if(rawInput.equals("exit")) {
				isRunning = false;
				break;
			}
			String timeStamp = getTime();

			input = timeStamp.concat(" " + rawInput);
			ct.execute(input);
		}
		return isRunning;
	}
	
	private static String getTime() {
		//Time Formated as HH:hh:ss
		//That is, Hour:Min:sec 
		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		return timeStamp;
	}
	
}
 