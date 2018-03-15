package chronoTimer;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import chronoTimer.Run.LinkedList;
import chronoTimer.Run.Node;


/**
 * JUnit test on the LinkedList data structure of the abstract class Run.
 * @author Fue Her
 *
 */
public class TestLinkedList {

	Run run;
	LinkedList list; // list to be tested on
	ArrayList<Node> nodes = new ArrayList<>();
	
	@Before
	public void setUp() throws Exception {
		run = new IND();
		list = new IND().new LinkedList();
		// Create a collection of racer nodes to test against the LinkedList
		for(int i = 0; i <10; i++){
			nodes.add(new IND().new Node(new Racer(i))); 
		}
	}
	
	@Test
	public void testHead(){
		list.addStart(nodes.get(0)); 
		list.addStart(nodes.get(1));
		assertEquals("Incorrect Racer at the beginning of the list",nodes.get(1).Data.getBibNum(), list.head.Data.getBibNum());
		list.removeStart();
		assertEquals("getLength is not correct.", 1, list.getLength());
		assertEquals("Should only contain this racer",nodes.get(0).Data.getBibNum(), list.head.Data.getBibNum());
		list.head = nodes.get(2);
		assertFalse("Racer is no longer in the list",list.contains(0));
		assertEquals("list size is incorrect.", 1, list.getLength() );
	}
	
	@Test
	public void testTail(){
		list.addStart(nodes.get(0)); //head
		list.addEnd(nodes.get(1));
		list.addEnd(nodes.get(2)); //tail
		assertEquals("This racer should be at the end of the list.", nodes.get(2).Data.getBibNum(), list.tail.Data.getBibNum());
		list.remove(2);
		assertEquals("This racer should be at the end of the list.", nodes.get(1).Data.getBibNum(), list.tail.Data.getBibNum());
		nodes.get(2).Data = new Racer(2); // had to do this since remove() changed Racer's Data to null
		list.addEnd(nodes.get(2));
		assertEquals("Incorrect racer at the end of the list, should of been racer 2", nodes.get(2), list.tail);
		list.remove(1); 
		assertEquals("This racer should be at the end.", nodes.get(2).Data.getBibNum(), list.tail.Data.getBibNum());
		list.removeEnd();
		assertEquals("Head and Tail should be racer 0", nodes.get(0), list.tail);
		assertEquals("Incorrect size", 1, list.getLength());
		assertEquals("Both should be the same racer", list.head, list.tail);
	}
	
	@Test
	public void testRemove(){
		for(int i = 0; i < 10; i++){
			list.addEnd(nodes.get(i));
		}
		assertEquals("Should have same size", nodes.size(), list.getLength());
		assertTrue("Should of return true, racer is removed.", list.remove(2));
		assertEquals("Should have same size", 9, list.getLength());
		assertFalse("Should return false, racer 2 already removed", list.remove(2));
		assertTrue("Should of return true, racer is removed.", list.remove(3));
		list.removeStart();
		assertEquals("Racer head should be bib number 1", nodes.get(1).Data.getBibNum(), list.head.Data.getBibNum());
	}
	
	@Test
	public void testContains(){
		list.addEnd(nodes.get(0));
		assertTrue("List should have racer 0", list.contains(0));
		list.addStart(nodes.get(1));
		assertTrue("List should have racer 1", list.contains(1));
		list.remove(0);
		assertFalse("List should not have racer 0", list.contains(0));
		list.removeEnd();
		assertFalse("List should not have racer 1", list.contains(1));
		list.addEnd(nodes.get(3));
		list.addEnd(nodes.get(5));
		list.removeStart();
		assertFalse("List should not have racer 1", list.contains(3));
		assertTrue("List should have racer 5", list.contains(5));
		list.removeStart();
		assertFalse("List should not have racer 1", list.contains(5));
	}


}
