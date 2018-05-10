package chronoTimer;

import static org.junit.Assert.*;


import java.time.LocalTime;
import org.junit.Before;
import org.junit.Test;


/**
 *  JUnit test on the PARIND class. It will mainly attempt to perform invalid and valid operation in the run and compare it with the 
 *  PARIND's fields (assuming the data structure is working correctly).
 *  The data structure is being tested on a separate JUnit test called TestLinkedList. 
 * @author Fue Her
 *
 */
public class TestPARIND {

	PARIND parind = new PARIND();
	Run emptyRun; // always empty, used to check for illegal state exceptions
	LocalTime time = java.time.LocalTime.of(10, 30, 0); // time to use to test on run
	
	//used for standing testing
	String queueStanding = "In Queue to Start:\n";
	String runningStanding = "\nCurrently Racing:\n";
	String completedStanding = "\nCompleted Race:\n";
	
	@Before
	public void setUp() throws Exception {
		// minor test for num(). The other tests will fail if num() doesn't work
		parind.num(1); // Lane 1
		parind.num(2); // Lane 2
		parind.num(3); // Lane 1
		emptyRun = new PARIND();
	}
	
	@Test
	public void testNum(){
		try{
			emptyRun.num(-1);
			emptyRun.num(1000);
			// already have these racers from setUp()
			parind.num(1);
			parind.num(2);
			parind.num(3);
			assertFalse("Should not be able to add any of the racers above", true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalArgumentException);
		}
		emptyRun.num(5);
		emptyRun.clr(5);
		
	}

	
	@Test
	public void testTrig(){
		// attempt to trigger each channel in a empty run
		for(int i = 0; i < 9; i++){
			try{
				emptyRun.trig(time,i);
				assertFalse("Cannot trigger, there are no racer in the run",true);
			}catch(RuntimeException e){
				assertTrue("wrong exception thrown: " + e, e instanceof IllegalStateException);
			}
		}
		
		// trigger finish channel, should not work
		try{
			parind.trig(time,2);
			parind.trig(time, 4);
			assertFalse("shouldn't been able to trigger finish channels, there are no racer in the running",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalStateException);
		}
		// should work correctly
		parind.trig(time, 1);
		parind.trig(time, 3);
		parind.trig(time.plusSeconds(5), 2);
		parind.trig(time.plusSeconds(10), 4);
		assertEquals(1,parind.Lane1.complete.head.Data.getBibNum());
		assertEquals(2,parind.Lane2.complete.head.Data.getBibNum());
		assertEquals(3,parind.Lane1.queued.head.Data.getBibNum());
	}

	@Test
	public void testSwap(){
		try{
			parind.swap();
			assertFalse("Cannot swap, there are no racer in the running list",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalArgumentException);
		}
		parind.trig(time, 1); //(racer 1) Lane 1
		try{
			parind.swap(1);
			assertFalse("Cannot swap, there is 1 racer in the running list of Lane 1",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalArgumentException);
		}
		
		parind.trig(time, 3); //time start is 10:30:00 (racer 2) Lane 2 
		try{
			parind.swap(1);
			assertFalse("Cannot swap, there is 1 racer in the running list of Lane 1",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalArgumentException);
		}
		parind.trig(time, 1); // time start is 10:30:00 (racer 3) Lane 1
		
		try{
			parind.swap(2);
			assertFalse("Cannot swap, there is 1 racer in the running list of Lane 2",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalArgumentException);
		}
		
		parind.swap(1); // swap racer 1 and racer 3 in Lane 1
		parind.trig(time.plusSeconds(5), 2); // finish at 10:30:05
		parind.trig(time.plusSeconds(10), 2); // finish at 10:30:10
		
		
	}
	
	@Test
	public void testEndRun(){
		parind.trig(time, 1); // racer 1 running
		parind.trig(time, 1); // racer 3 running
		parind.trig(time, 3); // racer 2 running
		assertEquals("Run should be active.", true, parind.active);
		parind.end();
		assertEquals("Run shouldn't be active.", false, parind.active);
		assertEquals("Racer 1 should be dnf", true, parind.Lane1.complete.head.Data.getDNF());
		assertEquals("Racer 3 has completed but is dnf",true,parind.Lane1.complete.head.next.Data.getDNF());
		assertEquals("Racer 2 should be dnf", true, parind.Lane2.complete.head.Data.getDNF());
		// checking emptyRun
		assertEquals("Run should be active, even with no racers", true, emptyRun.active);
		emptyRun.end();
		assertEquals("Run shouldn't be active.", false, emptyRun.active);
		
	}
	
	@Test
	public void testCLR(){
		try{
			emptyRun.clr(1);
			emptyRun.clr(-1);
			assertFalse("Cannot clear the run, there are no racer in the empty run",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalArgumentException);
		}
		
		// racer 1 shouldn't be in the queue
		parind.clr(1);
		assertEquals("Racers has been cleared already.",false,parind.Lane1.contains(1));
		
		// no racers
		parind.clr(2);
		parind.clr(3);
		assertEquals("Racers has been cleared already.",false,parind.Lane1.contains(2));
		assertEquals("Racers has been cleared already.",false,parind.Lane1.contains(3));
		
		try{
			parind.clr(1);
			parind.clr(2);
			parind.clr(3);
			assertFalse("Cannot clear the run, there are no racer in the run",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalArgumentException);
		}
	}
	
	@Test
	public void testStandings(){
		// all racers in queue
		assertEquals("String is displayed incorrectly in queue standing.","Lane 1:\n" +queueStanding + "1 " + time + 
				 "\n3 " + time + "\n\nLane 2:\n" + queueStanding + "2 " + time + "\n", parind.standings(time));
		
		parind.clr(1);
		assertEquals("String is displayed incorrectly in queue standing.","Lane 1:\n" + queueStanding +"3 " + time 
			     + "\n\nLane 2:\n" + queueStanding + "2 " + time + "\n", parind.standings(time));
		
		parind.clr(3);
		parind.clr(2);
		assertEquals("String is diplayed incorrectly.","Lane 1:\nNo Racers Currently In Run\n\nLane 2:\nNo Racers Currently In Run\n", parind.standings(time));
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
		assertEquals("String is displayed incorrectly in queue standing.","Lane 1:\n" +queueStanding + "1 " + time + 
				 "\n3 " + time + "\n\nLane 2:\n" + queueStanding + "2 " + time + "\n", parind.standings(time));
				
		// racer 1 racing
		parind.trig(time, 1);
		assertEquals("String is displayed incorrectly in queue standing.","Lane 1:\n" + queueStanding + "3 " + time + 
					"\n" + runningStanding + "1 00:00\n\nLane 2:\n" + queueStanding + "2 " + time + "\n", parind.standings(time));
		
		// racer 1 back to the queue, should display standing like normal
		parind.cancel();
		assertEquals("String is displayed incorrectly in queue standing.","Lane 1:\n" +queueStanding + "1 " + time + 
				 "\n3 " + time + "\n\nLane 2:\n" + queueStanding + "2 " + time + "\n", parind.standings(time));
		
		// racer 2 racing
		parind.trig(time, 3);
		assertEquals("String is displayed incorrectly in queue standing.","Lane 1:\n" +queueStanding + "1 " + time + 
				 "\n3 " + time + "\n\nLane 2:\n" + runningStanding + "2 00:00" + "\n", parind.standings(time));
		
		//cancel racer 2
		parind.cancel();
		assertEquals("String is displayed incorrectly in queue standing.","Lane 1:\n" +queueStanding + "1 " + time + 
				 "\n3 " + time + "\n\nLane 2:\n" + queueStanding + "2 " + time + "\n", parind.standings(time));
	}
	
	@Test
	public void testDNF(){
		try{
			//unsupported for this run
			parind.dnf();
			assertFalse("Cannot dnf racers, there are no racer in the running list",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalArgumentException);
		}
		
		try{
			parind.dnf(1); // dnf lane 1
			parind.dnf(2); // dnf lane 2
			assertFalse("Cannot dnf racers, there are no racer in the running list",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalStateException);
		}
		
		// racer 1 is going to be dnf
		parind.trig(time, 1); // racer 1 started running
		assertEquals("Racer 1 is running", 1, parind.Lane1.running.head.Data.getBibNum());
		parind.dnf(1); // dnf lane 1
		assertEquals("Racer 1 has completed but is dnf",1,parind.Lane1.complete.head.Data.getBibNum());
		assertEquals("Racer 1 should be dnf", true, parind.Lane1.complete.head.Data.getDNF());
		
		
		
	}
	
	@Test
	public void testExport(){
		parind = new PARIND();
		parind.num(1);
		//exporting JSON format of a racer in queue
		String expectedOutput = "{\"type\":\"PARIND\",\"queuedLane1\":[{\"bibNum\":1,\"startTime\":\"\",\"endTime\":\"\",\"dnf\":false}],\"queuedLane2\":[],"
								+"\"runningLane1\":[],\"runningLane2\":[],\"finishedLane1\":[],\"finishedLane2\":[]}";
		assertEquals("Converted to JSON incorrectly", expectedOutput, parind.export());
		parind.trig(time, 1);
		
		// racer 1 finish the run
		parind.trig(time.plusSeconds(10), 2);// racer ran for 10 seconds
		expectedOutput = "{\"type\":\"PARIND\",\"queuedLane1\":[],\"queuedLane2\":[],"
				+"\"runningLane1\":[],\"runningLane2\":[],\"finishedLane1\":[{\"bibNum\":1,\"startTime\":\"10:30\",\"endTime\":\"10:30:10\",\"dnf\":false}],\"finishedLane2\":[]}";
		assertEquals("Converted to JSON incorrectly", expectedOutput, parind.export());
	}

}
