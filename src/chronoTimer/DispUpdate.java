package chronoTimer;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import chronoSimulator.ChronoTimerSimulator;

/**
 * 
 * @author Andrew Krill ATKrill@uwm.edu
 * 
 * 
 * 
 */

public class DispUpdate implements Runnable {
	boolean kill = false;

    public void run() {
        while (kill == false) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            	System.out.println("update");
                    kill();
            };
        }
        return;
    }

    //Sets kill to true to exit run loop
    public void kill(){
        kill = true;
      }
}