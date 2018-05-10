package chronoTimer;

import static org.junit.Assert.*;


import java.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

/**
 *  JUnit test on the GRP class.
 * @author Riley Mahr
 *
 */

public class TestGRP {
	Run run; // the run to be tested 
	Run emptyRun; // always empty, used to check for illegal state exceptions
	LocalTime time = java.time.LocalTime.of(10, 30, 0); // time to use to test on run
	String queueStanding = "In Queue to Start:\n";
	String runningStanding = "\nCurrently Racing:\n";
	String completedStanding = "\nCompleted Race:\n";
	
	@Before
	public void setUp() throws Exception {
		run = new GRP();
		emptyRun = new GRP();
	}

	
	@Test
	public void testTrig(){
		for(int i = 3; i < 9; i++){
			try{
				emptyRun.trig(time,i);
				assertFalse("Trigger not supported",true);
			}catch(RuntimeException e){
				assertTrue("wrong exception thrown: " + e, e instanceof IllegalStateException);
			}
		}
		
		assertTrue("Group race start at Trig 1", "Group Race has now started".equals(emptyRun.trig(time, 1)));
		assertTrue("Temp racer 0 completes empty run", "Temp Racer 0 completed their run in: 00:00".equals(emptyRun.trig(time, 2)));
	}

	@Test
	public void testSwap(){
		run.num(1);
		run.num(2);
		run.num(3);
		run.num(4);
		assertTrue("Group race start at Trig 1", "Group Race has now started".equals(run.trig(time, 1)));
		assertTrue("racer 1 finishes", "Racer 1 completed their run in: 00:00".equals(run.trig(time, 2)));
		run.swap();
		assertTrue("racer 3 finishes", "Racer 3 completed their run in: 00:00".equals(run.trig(time, 2)));
	}
	
	@Test
	public void testCLR(){
		try{
			emptyRun.clr(1);
			assertFalse("Cannot clear the run, there are no racer in the empty run",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalArgumentException);
		}
		
		run.num(1);
		run.num(2);
		run.num(3);
		run.num(4);
		
		assertTrue("Group race start at Trig 1", "Group Race has now started".equals(run.trig(time, 1)));
		assertTrue("racer 1 finishes", "Racer 1 completed their run in: 00:00".equals(run.trig(time, 2)));
	
		run.clr(2);
		
		assertTrue("racer 3 finishes", "Racer 3 completed their run in: 00:00".equals(run.trig(time, 2)));
		
		run.clr(4);
		
		assertTrue("racer 0 finishes as no racers left", "Temp Racer 0 completed their run in: 00:00".equals(run.trig(time, 2)));
		
		
		
		
		
	}
	
	@Test
	public void testCancel(){
		try{
			emptyRun.cancel();
			assertFalse("Cannot cancel as run is not going",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalStateException);
		}
		
		run.trig(time, 1);
		
		try{
			run.cancel();
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalStateException);
		}
		run.num(1);
		run.num(2);
		run.num(3);
		run.trig(time, 1);
		run.trig(time, 2);
		try{
			run.cancel();
			assertFalse("Cannot cancel as competitor has finished",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalStateException);
		}
		
	}
	
	@Test
	public void testExport(){
		Run run = new IND();
		run.num(1);
		//exporting JSON format of a racer in queue
		String expectedOutput = "{\"type\":\"IND\",\"queued\":[{\"bibNum\":1,\"startTime\":\"\",\"endTime\":\"\",\"dnf\":false}],"
								+"\"running\":[],\"finished\":[]}";
		assertEquals("Converted to JSON incorrectly", expectedOutput, run.export());
		run.trig(time, 1);
		
		// racer 1 finish the run
		run.trig(time.plusSeconds(10), 2);// racer ran for 10 seconds
		expectedOutput = "{\"type\":\"IND\",\"queued\":[]," + "\"running\":[],\"finished\":[{\"bibNum\":1,\"startTime\":\"10:30\",\"endTime\":"
							+ "\"10:30:10\",\"dnf\":false}]}";
		assertEquals("Converted to JSON incorrectly", expectedOutput, run.export());
	}

}
