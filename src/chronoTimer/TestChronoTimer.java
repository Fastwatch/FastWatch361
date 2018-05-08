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
	public void testPOWER(){
	assertFalse("Must turn on power.", ct.execute("12:01:20.0 EXIT"));
	assertFalse("Must turn on power.", ct.execute("12:01:20.0 RESET"));
	assertFalse("Must turn on power.", ct.execute("12:01:20.0 TOG 2"));
	assertFalse("Must turn on power.", ct.execute("12:01:20.0 CONN 2"));
	assertFalse("Must turn on power.", ct.execute("12:01:20.0 DISC 2"));
	assertFalse("Must turn on power.", ct.execute("12:01:20.0 EVENT GRP"));
	assertFalse("Must turn on power.", ct.execute("12:01:20.0 NEWRUN"));
	assertFalse("Must turn on power.", ct.execute("12:01:20.0 ENDRUN"));
	assertFalse("Must turn on power.", ct.execute("12:01:20.0 PRINT 1"));
	assertFalse("Must turn on power.", ct.execute("12:01:20.0 EXPORT 1"));
	assertFalse("Must turn on power.", ct.execute("12:01:20.0 NUM 3"));
	assertFalse("Must turn on power.", ct.execute("12:01:20.0 CLR 2"));
	assertFalse("Must turn on power.", ct.execute("12:01:20.0 SWAP"));
	assertFalse("Must turn on power.", ct.execute("12:01:20.0 DNF"));
	assertFalse("Must turn on power.", ct.execute("12:01:20.0 TRIG 1"));
	assertFalse("Must turn on power.", ct.execute("12:01:20.0 FINISH"));
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
		assertFalse("Power is off", ct.execute("12:01:10.0 NUM 10"));
		ct.execute("12:01:02.0 POWER");
		assertTrue("Should created a new run.",ct.execute("12:01:10.0 NEWRUN"));
		assertFalse("Should error as number is too large", ct.execute("12:01:10.0 NUM 1000"));
		for(int i = 0; i < 1000; i++) {
			System.out.println(i);
			assertTrue("Accepts number in 0 - 999 range", ct.execute("12:01:10.0 NUM " + i));
		}
		assertFalse("cannot add duplicate racer",ct.execute("12:01:10.0 NUM 5"));
	}
	
	@Test
	public void testExit() {
		ct.execute("12:01:02.0 POWER");
		ct.execute("12:01:02.0 NEWRUN");
		assertFalse("exit handled by simulator",ct.execute("12:01:02.0 EXIT"));
	}
	
	@Test
	public void testReset() {
		assertFalse("Power is off",ct.execute("12:01:10.0 RESET"));
		ct.execute("12:01:02.0 POWER");
		assertTrue("Reset System",ct.execute("12:01:10.0 RESET"));
		assertTrue("Reset System",ct.execute("12:01:10.0 RESET"));
		ct.execute("12:01:10.0 NEWRUN");
		ct.execute("12:01:10.0 ENDRUN");
		assertFalse("ct has log", (ct.getLog().equals("")));
		assertTrue("Reset System",ct.execute("12:01:10.0 RESET"));
	//	assertTrue("ct has no log after reset", (ct.getLog().equals("")));
		
	}
	
	@Test
	public void testEndrun() {
		assertFalse("Power is off",ct.execute("12:01:10.0 ENDRUN"));
		ct.execute("12:01:02.0 POWER");
		assertFalse("Need to have a run to end",ct.execute("12:01:10.0 ENDRUN"));
		assertTrue("Should created a new run.",ct.execute("12:01:10.0 NEWRUN"));
		assertTrue("Should end run",ct.execute("12:01:10.0 ENDRUN"));
		assertFalse("Need to have a run to end",ct.execute("12:01:10.0 ENDRUN"));	
	}
	
	@Test
	public void testClr() {
		ct.execute("12:01:02.0 POWER");
		ct.execute("12:01:02.0 NEWRUN");
		ct.execute("12:01:02.0 EVENT PARGRP");
		ct.execute("12:01:02.0 NUM 1");
		ct.execute("12:01:02.0 NUM 2");
		ct.execute("12:01:02.0 NUM 3");
		assertTrue("valid clear", ct.execute("12:01:02.0 CLR 1"));
		assertTrue("valid clear", ct.execute("12:01:02.0 CLR 2"));
		assertFalse("racer not in queue", ct.execute("12:01:02.0 CLR 2"));
		assertTrue("valid clear", ct.execute("12:01:02.0 CLR 3"));

	}
	
	@Test
	public void testSwap() {
		ct.execute("12:01:02.0 POWER");
		ct.execute("12:01:02.0 NEWRUN");
		ct.execute("12:01:02.0 EVENT PARIND");	
		ct.execute("12:01:02.0 NUM 1");
		ct.execute("12:01:02.0 NUM 2");
		ct.execute("12:01:02.0 NUM 3");
		ct.execute("12:01:02.0 CONN PAD 1");
		ct.execute("12:01:02.0 CONN PAD 2");
		ct.execute("12:01:02.0 CONN PAD 3");
		ct.execute("12:01:02.0 CONN PAD 4");
		ct.execute("12:01:02.0 TOG 1");
		ct.execute("12:01:02.0 TOG 2");
		ct.execute("12:01:02.0 TOG 3");
		ct.execute("12:01:02.0 TOG 4");
		ct.execute("12:01:02.0 TRIG 1");
		assertFalse("only one racer", ct.execute("12:01:03.0 SWAP 1"));
		ct.execute("12:01:04.0 TRIG 1");
		ct.execute("12:01:05.0 TRIG 3");
		assertTrue("valid swap", ct.execute("12:01:06.0 SWAP 1"));
		ct.execute("12:01:07.0 FINISH");
		assertFalse("only one racer", ct.execute("12:01:08.0 SWAP 1"));
		assertFalse("only one racer", ct.execute("12:01:08.0 SWAP 2"));
		assertFalse("invalid lane number", ct.execute("12:01:08.0 SWAP 3"));
	}
	
	@Test
	public void testDNF() {
		ct.execute("12:01:02.0 POWER");
		ct.execute("12:01:02.0 NEWRUN");
		ct.execute("12:01:02.0 EVENT PARIND");
		ct.execute("12:01:02.0 NUM 1");
		ct.execute("12:01:02.0 NUM 2");
		ct.execute("12:01:02.0 NUM 3");
		ct.execute("12:01:02.0 NUM 4");
		ct.execute("12:01:02.0 CONN PAD 1");
		ct.execute("12:01:02.0 CONN PAD 2");
		ct.execute("12:01:02.0 CONN PAD 3");
		ct.execute("12:01:02.0 CONN PAD 4");
		ct.execute("12:01:02.0 TOG 1");
		ct.execute("12:01:02.0 TOG 2");
		ct.execute("12:01:02.0 TOG 3");
		ct.execute("12:01:02.0 TOG 4");
		assertFalse("no racer", ct.execute("12:01:02.0 DNF 1"));
		ct.execute("12:01:02.0 TRIG 1");
		assertFalse("no racer in lane 2", ct.execute("12:01:02.0 DNF 2"));
		assertFalse("lane not specified", ct.execute("12:01:02.0 DNF"));
		assertTrue("racer 1 dnf'ed", ct.execute("12:01:02.0 DNF 1"));
		ct.execute("12:01:02.0 trig 1");
		ct.execute("12:01:02.0 trig 2");
		assertFalse("no racer in lane 1", ct.execute("12:01:02.0 DNF 1"));
		ct.execute("12:01:02.0 trig 3");
		ct.execute("12:01:02.0 trig 3");
		assertTrue("racer 2 dnf'ed", ct.execute("12:01:02.0 DNF 2"));
		assertTrue("racer 4 dnf'ed", ct.execute("12:01:02.0 DNF 2"));
		}
	
	@Test
	public void testFinish1() {
		ct.execute("12:01:02.0 POWER");
		ct.execute("12:01:02.0 NEWRUN");
		ct.execute("12:01:02.0 EVENT GRP");
		assertFalse("no racer", ct.execute("12:01:02.0 FINISH"));
		ct.execute("12:01:02.0 NUM 1");
		ct.execute("12:01:02.0 NUM 2");
		ct.execute("12:01:02.0 CONN PAD 1");
		ct.execute("12:01:02.0 CONN PAD 2");
		ct.execute("12:01:02.0 TOG 1");
		ct.execute("12:01:02.0 TOG 2");
		ct.execute("12:01:02.0 START");
		assertTrue("racer 1 finished", ct.execute("12:01:02.0 FINISH"));
		assertTrue("racer 2 finished", ct.execute("12:01:02.0 FINISH"));
		assertTrue("temp racer", ct.execute("12:01:02.0 FINISH"));	
		}	
	
	@Test
	public void testFinish2() {
		ct.execute("12:01:02.0 POWER");
		ct.execute("12:01:02.0 NEWRUN");
		ct.execute("12:01:02.0 EVENT IND");
		assertFalse("no racer", ct.execute("12:01:02.0 FINISH"));
		ct.execute("12:01:02.0 NUM 1");
		ct.execute("12:01:02.0 NUM 2");
		ct.execute("12:01:02.0 CONN PAD 1");
		ct.execute("12:01:02.0 CONN PAD 2");
		ct.execute("12:01:02.0 TOG 1");
		ct.execute("12:01:02.0 TOG 2");
		ct.execute("12:01:02.0 START");
		assertTrue("racer 1 finished", ct.execute("12:01:02.0 FINISH"));
		assertFalse("no racer", ct.execute("12:01:02.0 FINISH"));	
		}	
	
	public void testConn(){
		ct.execute("12:01:02.0 POWER");
		ct.execute("12:01:02.0 NEWRUN");
		ct.execute("12:01:02.0 EVENT PARGRP");
		ct.execute("12:01:02.0 NUM 1");
		ct.execute("12:01:02.0 NUM 2");
		ct.execute("12:01:02.0 NUM 3");
		assertFalse("sensors only from 1 - 8", ct.execute("12:01:02.0 CONN pad 0"));
		assertFalse("sensors only from 1 - 8", ct.execute("12:01:02.0 CONN pad 9"));
		assertFalse("sensors only from 1 - 8", ct.execute("12:01:02.0 TOG 0"));
		assertFalse("sensors only from 1 - 8", ct.execute("12:01:02.0 TOG 9"));
		assertTrue("pad 1 connected", ct.execute("12:01:02.0 CONN pad 1"));
		assertTrue("pad 1 disconnected", ct.execute("12:01:02.0 DISC 1"));
		assertTrue("no sensor connected", ct.execute("12:01:02.0 DISC 1"));
		assertFalse("sensors only from 1 - 8", ct.execute("12:01:02.0 DISC 0"));
		assertFalse("sensors only from 1 - 8", ct.execute("12:01:02.0 DISC 9"));
	}
}
