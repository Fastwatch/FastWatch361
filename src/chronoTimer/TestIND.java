package chronoTimer;

import static org.junit.Assert.*;

import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;


/**
 *  JUnit test on the IND class. It will mainly attempt to perform certain operation in the run and compare it with the 
 *  standings() string output and not the data structure in the IND because the run.standings() already goes through the 
 *  data structure of the run being tested on (assuming the data structure is working correctly).
 *  The data structure is being tested on a separate JUnit test called TestLinkedList. 
 * @author Fue Her
 *
 */
public class TestIND {

	Run run; // the run to be tested 
	Run emptyRun; // always empty, used to check for illegal state exceptions
	LocalTime time = java.time.LocalTime.of(10, 30, 0); // time to use to test on run
	String queueStanding = "In Queue to Start:\n";
	String runningStanding = "\nCurrently Racing:\n";
	String completedStanding = "\nCompleted Race:\n";
	
	@Before
	public void setUp() throws Exception {
		run = new IND();
		run.num(1);
		run.num(2);
		run.num(3);
		emptyRun = new IND();
	}

	@Test
	public void testSwap(){
		try{
			run.swap();
			assertFalse("Cannot swap, there are no racer in the running list",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalStateException);
		}
		run.trig(time, 1);
		try{
			run.swap();
			assertFalse("Cannot swap, there is 1 racer in the running list",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalStateException);
		}
		
		run.trig(time, 1); //time start is 10:30:00
		run.trig(time, 1); // time start is 10:30:00
		run.swap();
		run.trig(time.plusSeconds(5), 2); // finish at 10:30:05
		run.trig(time.plusSeconds(10), 2); // finish at 10:30:10
		assertEquals("String is displayed incorrectly after swapping runners.",queueStanding + "3 " + time + 
					  "\n" + completedStanding + "2 00:00:05" + "\n"+ "1 00:00:10\n" , run.standings(time));
		
	}
	
	@Test
	public void testEndRun(){
		run.trig(time, 1);
		run.trig(time, 1);
		assertEquals("Run should be active.", true, run.active);
		run.end();
		assertEquals("Run shouldn't be active.", false, run.active);
		assertEquals("String is displayed incorrectly after ending a run.","Racers who did not start:\n" + "3\n"
			         + completedStanding + "1 DNF\n" + "2 DNF\n" , run.standings(time));
		// checking emptyRun
		assertEquals("Run should be active, even with no racers", true, emptyRun.active);
		emptyRun.end();
		assertEquals("Run shouldn't be active.", false, emptyRun.active);
		
	}
	
	@Test
	public void testCLR(){
		try{
			emptyRun.clr(1);
			assertFalse("Cannot clear the run, there are no racer in the empty run",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalArgumentException);
		}
		// all racers in queue
		assertEquals("String is displayed incorrectly in queue standing.",queueStanding + "1 " + time + 
				 "\n2 " + time + "\n3 " + time + "\n", run.standings(time));
		
		// racer 1 shouldn't be in the queue
		run.clr(1);
		assertEquals("String is displayed incorrectly in queue standing.",queueStanding +"2 " + time 
				     + "\n3 " + time + "\n", run.standings(time));
		// no racers
		run.clr(3);
		run.clr(2);
		assertEquals("String is diplayed incorrectly.","No Racers Currently In Run", run.standings(time));
		
		try{
			run.clr(1);
			assertFalse("Cannot clear the run, there are no racer in the run",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalArgumentException);
		}
	}
	
	@Test
	public void testCancel(){
		try{
			emptyRun.cancel();
			assertFalse("Cannot cancel racer, there are no racer in the running list",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalStateException);
		}
		// all racers in queue 
				assertEquals("String is displayed incorrectly in queue standing.",queueStanding + "1 " + time + 
					     		"\n2 " + time + "\n3 " + time + "\n", run.standings(time));
				
		// racer 1 racing
		run.trig(time, 1);
		assertEquals("String is displayed incorrectly in queue standing.",queueStanding + "2 " + time + "\n3 " 
		             + time + "\n" + runningStanding + "1 00:00\n", run.standings(time));
		
		// racer 1 back to the queue, should display standing like normal
		run.cancel();
		assertEquals("String is displayed incorrectly in queue standing.",queueStanding + "1 " + time + 
	     		"\n2 " + time + "\n3 " + time + "\n", run.standings(time));
		
	}
	
	@Test
	public void testDNF(){
		try{
			run.dnf();
			assertFalse("Cannot dnf racers, there are no racer in the running list",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalStateException);
		}
		// all racers in queue
		assertEquals("String is displayed incorrectly in queue standing.",queueStanding + "1 " + time + 
			     		"\n2 " + time + "\n3 " + time + "\n", run.standings(time));
		// racer 1 is going to be dnf
		run.trig(time, 1);
		run.dnf();
		assertEquals("String is displayed incorrectly after DNF on racer 1.",queueStanding + "2 " + time + 
				     "\n3 " + time +"\n" + completedStanding + "1 DNF\n" , run.standings(time));
		run.trig(time, 1);
		run.dnf();
		assertEquals("String is displayed incorrectly after DNF on racer 2.",queueStanding + "3 " + time +
				     "\n" + completedStanding + "1 DNF\n" + "2 DNF\n" , run.standings(time));
	}
	
	@Test
	public void testExport(){
		
	}

}
