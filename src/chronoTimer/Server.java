package chronoTimer;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * 
 * @author Fue Her
 *
 */

public class Server {

	// a shared area where we get the POST data and then use it in the other handler
	static boolean gotMessageFlag = false;
	static ArrayList<Racer> results = new ArrayList<Racer>();
	static String runType = "IND";

	public static void main(String[] args) throws Exception {

		// set up a simple HTTP server on our local host
		HttpServer serverDisplay;
		HttpServer serverResults;
		try {
			// use different port for sending and displaying results
			serverResults = HttpServer.create(new InetSocketAddress(8100), 0); // chronotimer port
			serverDisplay =  HttpServer.create(new InetSocketAddress(8001), 0); // spectator's port

			// create a context to get the request for the POST
			serverResults.createContext("/sendresults", new PostHandler());

			// create a context to get the request to display the Formatted results
			serverDisplay.createContext("/displayresults/racerlist", new RacerListHandler());

			// create a context to get the request for the Style Sheet
			serverDisplay.createContext("/displayresults/style.css", new StyleHandler());

			serverDisplay.setExecutor(null); // creates a default executor

			// get it going
			System.out.println("Starting Server...");
			serverDisplay.start();
			serverResults.start();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	static class RacerListHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			// HTML file headers stuff
			String response = "<!DOCTYPE html>" + "\n";
			response += "<html lang=\"en-US\">" + "\n";
			response += "<head>" + "\n";
			response += "<title>Racers</title>" + "\n";
			response += "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />" + "\n";
			response += "<body>" + "\n";

			// Initialize HTML Table
			response += "<table style=\"width:100%\">" + "\n";
			response += "<tr>" + "\n";
			response += "<th>Place</th>" + "\n";
			response += "<th>Number</th>" + "\n";
			response += "<th>Name</th>" + "\n";
			response += "<th>Time</th>" + "\n";
			response += "</tr>" + "\n";

			if (results.isEmpty()) {
				response += "NOTE: NO RECENT COMPLETED RUN\n";
			} else {
				try {
					int place = 1;
					
					// read from file and check if there is any name matching with the bibNumber
					HashMap<Integer, String> names = parseFile(); 
					
					results.sort(new Comparator<Racer>(){

						public int compare(Racer o1, Racer o2) {
							if(o1.getDNF() == true && o2.getDNF() == false) {
								return 1;
							}else if(o1.getDNF() == false && o2.getDNF() == true) {
								return -1;
							}else if(o1.getDNF() == true && o1.getDNF() == true) {
								return 0;
							}
							else {
								if(o1.getTime().compareTo(o2.getTime()) > 0) return 1;
								else if(o1.getTime().compareTo(o2.getTime()) < 0) return -1;
								else return 0;
							}
						}
					});
					
					for (Racer r : results) {
						String racerName = "";
						response += "<tr>" + "\n";
						response += "<td>" + place++ + "</td>" + "\n";
						response += "<td>" + r.getBibNum() + "</td>" + "\n";
						
						for(Integer i : names.keySet()) {
							if(r.getBibNum() == i) {
								racerName = names.get(i);
							}
						}
						response += "<td>" + racerName + "</td>" + "\n";
						if (r.getDNF() == false) {
							response += "<td>" + r.getTime() + "</td>" + "\n";
						} else {
							response += "<td>" + "DNF" + "</td>" + "\n";
						}

						response += "</tr>" + "\n";
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// End the HTML doc and table
			response += "</table>" + "\n";
			response += "</body>" + "\n";
			response += "</html>" + "\n";

			// write out the response
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();

		}

		/**
		 * Read from the racers.txt file and parses by ":" expecting a format of bibNum:Name and returns
		 * a Hash Map of the racers bib number and name.
		 * @param fileName
		 * @return HashMap containing key values of an integer(bibNum) associated with a string value(Name).
		 */
		private HashMap<Integer,String> parseFile() {
			File file = null;
			file = new File("racers.txt");
			HashMap<Integer, String> names = new HashMap<Integer, String>();
			
			if(!file.exists() || !file.isFile()) {
				//System.out.println("No file called racers.txt found");
			}
			
			try (BufferedReader br = new BufferedReader(new FileReader("racers.txt"));){
				
				String line;
				
				while ((line = br.readLine()) != null) {
					String[] s = line.split(":");
					names.put(Integer.parseInt(s[0]), s[1]);
				}
			} catch (Exception e) {
				//System.out.println(e.getMessage());
			}
			
			return names;
		}
	}

	/* Commenting that phil wrote this code */
	static class StyleHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			File f = new File("style.css");
			Scanner sc = new Scanner(f);
			String response = "";
			while (sc.hasNextLine()) {
				response += sc.nextLine();
			}
			Headers h = t.getResponseHeaders();
			h.set("Content-Type", "text/css");
			t.sendResponseHeaders(200, 0);
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			sc.close();
		}
	}

	static class PostHandler implements HttpHandler {
		public void handle(HttpExchange transmission) throws IOException {

			// set up a stream to read the body of the request
			InputStream inputStr = transmission.getRequestBody();

			// set up a stream to write out the body of the response
			OutputStream outputStream = transmission.getResponseBody();

			// string to hold the result of reading in the request
			StringBuilder sb = new StringBuilder();

			// read the characters from the request byte by byte and build up the
			// sharedResponse
			int nextChar = inputStr.read();
			while (nextChar > -1) {
				sb = sb.append((char) nextChar);
				nextChar = inputStr.read();
				
			}
			// print what we received
			System.out.println(sb);
			
			//ChronoTimer data
			receiveData(sb.toString());
			
			// respond to the POST with ROGER
			String postResponse = "Run results received";

			// assume that stuff works all the time
			transmission.sendResponseHeaders(300, postResponse.length());

			// write it and return it
			outputStream.write(postResponse.getBytes());

			outputStream.close();
		}
	}

	/**
	 * Sends the most recent sorted RUN data to the server.
	 * @param data
	 */
	public static void receiveData(String JSONString) {
		Gson g = new Gson();
		results = new ArrayList<>(); // reset results every update
		
		// get the whole JSON Data converted into JsonObject
		JsonElement element = g.fromJson(JSONString, JsonElement.class); 
		JsonObject runData = element.getAsJsonObject(); 
		JsonArray finishedRacers = new JsonArray();
		
		// parse and initialize to create a Json Array containing finished racers
		runType = runData.get("type").toString();
		if(runType.equalsIgnoreCase("\"PARIND\"")){
			JsonArray lane1 = runData.getAsJsonArray("finishedLane1");
			JsonArray lane2 = runData.getAsJsonArray("finishedLane2");
			finishedRacers.addAll(lane1);
			finishedRacers.addAll(lane2);
		}else{
			finishedRacers = runData.getAsJsonArray("finished");
		}
		 

		// iterate through Json Array and add it to an ArrayList for sorting and fetching data from
		for(int i = 0; i < finishedRacers.size(); i++) {
			Racer r;
			LocalTime startTime;
			JsonObject object = finishedRacers.get(i).getAsJsonObject();
			int bibNum = Integer.parseInt(object.get("bibNum").toString());
			boolean dnf = object.get("dnf").getAsBoolean();
			String sTime = object.get("startTime").getAsString();
			
			if(dnf == false) {
				String eTime = object.get("endTime").getAsString();
				LocalTime endTime = LocalTime.parse(eTime);
				
				if(!sTime.equalsIgnoreCase("")){
					startTime = LocalTime.parse(sTime);
					r = new Racer(bibNum, startTime, endTime);
				}else{
					r = new Racer(bibNum);
					r.setFinish(endTime);
				}
				r.setDNF(false);
			}else {
				if(!sTime.equalsIgnoreCase("")){
					startTime = LocalTime.parse(sTime);
					r = new Racer(bibNum,startTime);
				}else{
					r = new Racer(bibNum);
				}
				r.setDNF(true); 
			}
			
			results.add(r);
		}
		
	}


}
