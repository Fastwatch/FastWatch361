package chronoTimer;

import static org.junit.Assert.*;

import chronoSimulator.ChronoTimerSimulator;

import org.junit.Before;
import org.junit.Test;

/**
 * Testing class for chronotimer
 * @author Isaac Kadera, Riley Mahr
 *
 */

public class TestChronoTimer {

	ChronoTimer ct;
	ChronoTimerSimulator sim; // Needed for chronotimer print
	
	@Before
	public void setUp() throws Exception {
		ct = new ChronoTimer();
		sim = new ChronoTimerSimulator();
		ct.setSim(sim);
	}
	
	@Test
	public void testPower() {
		assertFalse("Must turn on the power first.",ct.execute("12:01:20.0 EVENT IND")); 
		ct.execute("12:01:02.0 POWER");
		assertTrue("Should of executed, and return true but did not.",ct.execute("12:01:20.0 EVENT IND"));
		ct.execute("12:01:20.0 POWER");
		assertFalse("Must turn on the power first.",ct.execute("12:01:20.0 NEWRUN")); 
		ct.execute("12:01:20.0 POWER");
		assertTrue("Should of executed, and return true but did not.",ct.execute("12:01:20.0 NEWRUN"));
	}
	
	@Test
	public void testEvent(){
		ct.execute("12:01:02.0 POWER");
		assertTrue("Valid event.", ct.execute("12:01:20.0 EVENT IND"));
		assertFalse("Invalid event.", ct.execute("12:01:20.0 EVENT Id"));
		assertFalse("There is no event to assign.", ct.execute("12:01:20.0 EVENT"));
		assertTrue("Valid event.", ct.execute("12:01:20.0 EVENT GRP"));
		assertTrue("Valid event.", ct.execute("12:01:20.0 EVENT PARIND"));
		assertTrue("Valid event.", ct.execute("12:01:20.0 EVENT PARGRP"));
	}
	
	@Test
	public void testNewRun(){
		ct.execute("12:01:02.0 POWER");
		assertTrue("Should created a new run.",ct.execute("12:01:10.0 NEWRUN"));
		assertFalse("Need to end a run first before creating new run.",ct.execute("12:01:10.0 NEWRUN"));
		assertTrue("Should of ended the current run.",ct.execute("12:01:10.0 ENDRUN"));
		assertTrue("Should of created a new run.",ct.execute("12:01:10.0 NEWRUN"));
		assertTrue("Should of ended the current run.",ct.execute("12:01:10.0 ENDRUN"));
		assertFalse("There is no run, it should not return true for ending the run.",ct.execute("12:01:10.0 ENDRUN"));
	}
	
	@Test
	public void testArguments(){
		ct.execute("12:01:02.0 POWER");
		assertFalse("Not enough arguments", ct.execute("12:01:20.0"));
		assertFalse("Not enough arguments", ct.execute("12:01:20.0 TIME"));
		assertFalse("Invalid command", ct.execute("12:01:20.0 Hello World"));
		assertTrue("There is enough arguments, should return true.", ct.execute("12:01:20.0 TIME 10:15:23.2"));
		assertFalse("Not enough arguments", ct.execute("12:01:20.0 EVENT"));
		assertFalse("Not enough arguments", ct.execute("12:01:20.0 TRIG"));
		assertTrue("There is enough arguments, should return true.", ct.execute("12:01:20.0 EVENT IND"));
	}
	
	@Test
	public void testStart(){
		
		assertFalse("Must turn on the power first.",ct.execute("12:01:02.0 START")); 
		ct.execute("12:01:02.0 POWER");
		ct.execute("12:01:02.0 CONN EYE 1");
		ct.execute("12:01:02.0 TOG 1");
		assertFalse("Should execute but return false because no Run created.",ct.execute("12:01:20.0 START"));
		assertTrue("Should of executed, and return true but did not.", ct.execute("12:01:20.0 NEWRUN"));
		assertFalse("Should execute but return false because no racer is in the Run.",ct.execute("12:01:20.0 START"));
		assertTrue("Should of executed, and return true but did not.",ct.execute("12:01:20.0 NUM 15"));
		assertTrue("Should of executed, and return true but did not.",ct.execute("12:01:20.0 START"));
	}
	
	@Test
	public void testTime(){
		assertFalse("Must turn on the power first.",ct.execute("12:01:20.0 TIME 12:01:13.2")); 
		ct.execute("12:01:02.0 POWER");
		assertTrue("Should of executed, and return true but did not.",ct.execute("12:01:20.0 TIME 12:01:13.2"));
		
		//invalid time format
		assertFalse("Should return false, error in parsing time",ct.execute("12:01:20.0 TIME 12:0s:13.2"));
		assertFalse("Should return false, error in parsing time",ct.execute("12:01.0 TIME 12:0s:13.2"));
		assertFalse("Should return false, error in parsing time",ct.execute("1:20.0 TIME 12s:0s:13.2"));
		assertFalse("Should return false, not enough arguments",ct.execute(""));
	}
	
	@Test
	public void testNum() {
		ct.execute("12:01:02.0 POWER");
		assertTrue("Should created a new run.",ct.execute("12:01:10.0 NEWRUN"));
		assertFalse("Should error as number is too large", ct.execute("12:01:10.0 NUM 1000"));
		for(int i = 0; i < 1000; i++) {
			System.out.println(i);
			assertTrue("Accepts number in 0 - 999 range", ct.execute("12:01:10.0 NUM " + i));
		}
		assertFalse("cannot add duplicate racer",ct.execute("12:01:10.0 NUM 5"));
	}

}
