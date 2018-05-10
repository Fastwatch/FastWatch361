package chronoTimer;

import static org.junit.Assert.*;

import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

/**JUnit test on the PARGRP class. It will mainly attempt to perform invalid and valid operation in the run and compare it with the 
 *  PARGRP's fields
 * 
 * @author Fue Her
 *
 */
public class TestPARGRP {

	PARGRP pargrp = new PARGRP();
	LocalTime time = java.time.LocalTime.of(10, 30, 0); // time to use to test on run
	Run emptyRun = new PARGRP();
	@Before
	public void setUp() throws Exception {
		pargrp.num(1);
		pargrp.num(2);
		pargrp.num(3);
	}

	@Test
	public void testNum() {
		try{
			pargrp.num(-1);
			pargrp.num(1000);
			// already have these racers from setUp()
			pargrp.num(1);
			pargrp.num(2);
			pargrp.num(3);
			assertFalse("Should not be able to add any of the racers above", true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalArgumentException);
		}
	}
	
	@Test
	public void testEndRun(){
		pargrp.trig(time, 1);
		pargrp.trig(time, 1);
		assertEquals("Run should be active.", true, pargrp.active);
		pargrp.end();
		assertEquals("Run shouldn't be active.", false, pargrp.active);
		assertEquals("Racer 2 should be dnf", true, pargrp.racers[1].getDNF());
		assertEquals("Racer 3 should be dnf", true, pargrp.racers[2].getDNF());
		assertEquals("Racer 1 shouldn't be dnf", false, pargrp.racers[0].getDNF());
		
		// checking emptyRun
		assertEquals("Run should be active, even with no racers", true, emptyRun.active);
		emptyRun.end();
		assertEquals("Run shouldn't be active.", false, emptyRun.active);
		
	}
	
	@Test
	public void testDNF(){
		for(int i = 1; i < 9; i++){
			try{
				pargrp.dnf(i);
				assertFalse("Cannot trigger, there are no racer in the run",true);
			}catch(RuntimeException e){
				assertTrue("wrong exception thrown: " + e, e instanceof IllegalStateException);
			}
		}
		pargrp.trig(time, 1);
		pargrp.trig(time, 1);
		try{
			pargrp.dnf(1);
			assertFalse("Cannot dnf, there are no racer in Lane 1",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalStateException);
		}
		pargrp.dnf(2);
		pargrp.dnf(3);
		
		assertEquals("Racer 2 should be dnf", true, pargrp.racers[1].getDNF());
		assertEquals("Racer 3 should be dnf", true, pargrp.racers[2].getDNF());
		assertEquals("Racer 1 shouldn't be dnf", false, pargrp.racers[0].getDNF());
	}
	
	@Test
	public void testTrig(){
		// attempt to trigger each channel in a empty run
		pargrp.clr(1);
		pargrp.clr(2);
		pargrp.clr(3);

		for(int i = 0; i < 9; i++){
			try{
				pargrp.trig(time,i);
				assertFalse("Cannot trigger, there are no racer in the run",true);
			}catch(RuntimeException e){
				assertTrue("wrong exception thrown: " + e, e instanceof IllegalStateException);
			}
		}
		
		// trigger finish channel, should not work
		try{
			pargrp.trig(time,2);
			pargrp.trig(time, 4);
			assertFalse("shouldn't been able to trigger finish channels, there are no racers",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalStateException);
		}
		
		pargrp.num(1);
		pargrp.num(2);
		pargrp.num(3);
		// should work correctly
		pargrp.trig(time, 1); // grp race start
		pargrp.trig(time.plusSeconds(10), 3); // lane 3 (racer 3)
		pargrp.trig(time.plusSeconds(5), 2);
		try{
			pargrp.trig(time.plusSeconds(10), 4);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalStateException);
		}
		assertEquals("Racer 3 completed the race", time.plusSeconds(10), pargrp.racers[2].getEndTime());
		assertEquals("Racer 1 has a startTime", time, pargrp.racers[0].getStartTime());
		assertEquals("Racer 1 has a no endTime", null, pargrp.racers[0].getEndTime());
		assertEquals("Racer 2 has a startTime", time, pargrp.racers[1].getStartTime());
		assertEquals("Racer 2 has a endTime", time.plusSeconds(5), pargrp.racers[1].getEndTime());
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
		pargrp.clr(1);
		assertEquals("Racers has been cleared already.",false,pargrp.contains(1));
		
		// no racers
		pargrp.clr(2);
		pargrp.clr(3);
		assertEquals("Racers has been cleared already.",false,pargrp.contains(2));
		assertEquals("Racers has been cleared already.",false,pargrp.contains(3));
		
		try{
			pargrp.clr(1);
			pargrp.clr(2);
			pargrp.clr(3);
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
		assertEquals("Racer 1 in queue", null, pargrp.racers[0].getStartTime());
		assertEquals("Racer 2 in queue", null, pargrp.racers[1].getStartTime());
		assertEquals("Racer 3 in queue", null, pargrp.racers[2].getStartTime());		
		
		// all racers racing
		pargrp.trig(time, 1);
		assertEquals("Racer 1 racing", time, pargrp.racers[0].getStartTime());
		assertEquals("Racer 2 racing", time, pargrp.racers[1].getStartTime());
		assertEquals("Racer 3 racing", time, pargrp.racers[2].getStartTime());
		
		// all racer back in queue
		pargrp.cancel();
		assertEquals("Racer 1 in queue", null, pargrp.racers[0].getStartTime());
		assertEquals("Racer 2 in queue", null, pargrp.racers[1].getStartTime());
		assertEquals("Racer 3 in queue", null, pargrp.racers[2].getStartTime());	

		try{
			pargrp.cancel();
			assertFalse("Cannot cancel racer, there are no racer in the running list",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalStateException);
		}
		
	}
	
	@Test
	public void testSwap(){
		// Unsupported feature to swap in this race type
		try{
			pargrp.swap();
			assertFalse("Unsupported feature in this race type",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalStateException);
		}
		pargrp.trig(time, 1);
		try{
			pargrp.swap();
			pargrp.swap(2);
			assertFalse("Unsupported feature in this race type",true);
		}catch(RuntimeException e){
			assertTrue("wrong exception thrown: " + e, e instanceof IllegalStateException);
		}
		
	}
	
	@Test
	public void testExport(){
		Run run = new PARGRP();
		run.num(1);
		//exporting JSON format of a racer in queue
		String expectedOutput = "{\"type\":\"PARGRP\",\"queued\":[{\"bibNum\":1,\"startTime\":\"\",\"endTime\":\"\",\"dnf\":false}],"
								+"\"running\":[],\"finished\":[]}";
		assertEquals("Converted to JSON incorrectly", expectedOutput, run.export());
		run.trig(time, 1);
		
		// racer 1 finish the run
		run.trig(time.plusSeconds(10), 1);// racer ran for 10 seconds
		expectedOutput = "{\"type\":\"PARGRP\",\"queued\":[]," + "\"running\":[],\"finished\":[{\"bibNum\":1,\"startTime\":\"10:30\",\"endTime\":"
							+ "\"10:30:10\",\"dnf\":false}]}";
		assertEquals("Converted to JSON incorrectly", expectedOutput, run.export());
	}

}
