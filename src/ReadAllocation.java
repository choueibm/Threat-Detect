import java.io.BufferedWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import javax.swing.*;
import com.google.gson.JsonElement;
import java.util.Arrays;
import com.google.gson.JsonObject;
import com.google.maps.model.LatLng;

import geocoding.GeocodingException;
import geocoding.GeocodingImpl;

/**
 * Reads from file and presents gun violence incidents to user based on filter conditions
 * 
 * @author Michael Barreiros, Harsh Patel
 * @version 6.0
 */
public class ReadAllocation {
	
	/**
	 * Reads from file and appends each line representing incident to an ArrayList
	 * 
	 * @param file File name to be read from
	 * @return ArrayList of strings representing incidents being read from the file
	 * @throws IOException If file is not found 
	 */
	public static ArrayList<String> read(String file) throws IOException {
		ArrayList<String> incidents = new ArrayList<>();
		try {
			Scanner input = new Scanner(new File(file), "UTF-8");
			String previousString = input.nextLine(); // This reads and skips the first line of the file which is just
														// naming for the data
			int counter = 0;
			while(input.hasNextLine()) {
				String line = input.nextLine();
				if(counter == 0) {
					previousString = line;
					counter++;
					continue;
				}
				
				if(line.equals("")) {
					String newLine = input.nextLine();
					while(newLine.equals("")) {
						newLine = input.nextLine();
					}
					previousString = previousString + newLine;
				}
				
				if(line.equals("")) continue;
				incidents.add(previousString);
				
				if(!input.hasNext()) incidents.add(line);
				previousString = line;
				counter++;
			}
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
		return incidents;
	}

	/**
	 * Builds an ArrayList of incidents ADT
	 * 
	 * @param data ArrayList of incidents as strings from file
	 * @return ArrayList of Incidents ADT
	 * @throws IOException If file not found
	 */
	public static ArrayList<Incidents> buildIncidents(ArrayList<String> data) throws IOException {
		ArrayList<Incidents> incidents = new ArrayList<>();
		for (int i = 0; i < data.size(); i++) {
			String incident = data.get(i);
			String[] columns = data.get(i).split(",(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))");
			
			if (columns[0].equals("") || columns[4].equals("") || columns[2].equals("") || columns[3].equals("")
					|| columns[8].equals("") || columns[7].equals("") || columns[5].equals("")
					|| columns[6].equals("")) {
				continue;
			}
	
			Incidents temp = new Incidents(columns[0], columns[4], columns[2], columns[3],
					Double.parseDouble(columns[8]), Double.parseDouble(columns[7]), Integer.parseInt(columns[5]),
					Integer.parseInt(columns[6]));
			incidents.add(temp);
		}
		return incidents;
	}

	/**
	 * Initializes the RedBLackTree with nodes as States and appropriate ArrayList of
	 * Incidents in each State ADT
	 * 
	 * @param incidents ArrayList of incidents ADT
	 * @return RedBlackTree with nodes as US States with ArrayList of Incidents appropriate
	 * to that State as value and State name as Key
	 */
	public static RedBlackTree<String, States> buildBST(ArrayList<Incidents> incidents) {
		RedBlackTree<String, States> StateTree = new RedBlackTree<String, States>();
		int i = 0;
		while (i != incidents.size()) {
			if (StateTree.contains(incidents.get(i).getState())) {
				States state = StateTree.get(incidents.get(i).getState());
				state.addIncidents(incidents.get(i));
				StateTree.put(incidents.get(i).getState(), state);
			} else {
				States state = new States(incidents.get(i).getState());
				state.addIncidents(incidents.get(i));
				StateTree.put(incidents.get(i).getState(), state);
			}
			i++;
		}
		return StateTree;
	}

	/**
	 * Sort ArrayList of incidents ADT
	 * 
	 * @param state US state location to get Incidents list from
	 * @param userLong Longitude coordinate of users desired location
	 * @param userLat Latitude coordinate of users desired location
	 * @return Sorted array list of Incidents based on distance from users 
	 * desired location
	 */
	public static ArrayList<Incidents> SortIncidents(States state, double userLong, double userLat) {
		ArrayList<Incidents> SortIncidents = state.getIncidentsList();
		for (int i = 0; i < SortIncidents.size(); i++) {
			Incidents incident = SortIncidents.get(i);
			incident.setUserLatitude(userLat);
			incident.setUserLongitude(userLong);
		}
		QuickSort sortRange = new QuickSort(SortIncidents);
		sortRange.sortBasicQuick();

		return SortIncidents;
	}
	
	/**
	 * Filters Incidents based on Range entered by user from users location
	 * 
	 * @param InRangeIncidents Array list of incidents in a certain US State
	 * @param Range Distance from users desired location in kilometers
	 * @return ArrayList of incidents that fall within the range set by user
	 */
	public static ArrayList<Incidents> filterIncidents(ArrayList<Incidents> InRangeIncidents, double Range) {
		int sizeofInci = InRangeIncidents.size();
		ArrayList<Incidents> InRangeIncidents2 = new ArrayList<Incidents>();
		for (int i = 0; i < sizeofInci; i++) {
			if (InRangeIncidents.get(i).getDisToIncident() <= Range) {
				InRangeIncidents2.add(InRangeIncidents.get(i));
			}
		}
		return InRangeIncidents2;
	}

	/**
	 * Prompts user to enter inputs in the form of address, US State, and range which
	 * initializes the RedBlackTree and gets the ArrayList of incidents from the entered
	 * US State, and filters the list based on the range. It prints to the console all the
	 * incidents within the US State that are within the Range entered. It also shows total
	 * number killed, injured, and number of incidents within the range. Once the program terminates
	 * the information on the console is written to a text file.
	 * 
	 * @param args String arguments from the user from the console
	 * @throws IOException If file being read is not found
	 * @throws GeocodingException Google Geocoding exception handling
	 */
	public static void main(String args[]) throws IOException, GeocodingException {
		ArrayList<String> strIncidents;
		String system;
		Scanner askUser = new Scanner(System.in);
		System.out.print("Enter OS to configure file path. Type Windows or Mac: ");
		system = askUser.nextLine();
		if (system.equals("Mac")) {
			strIncidents = read("Data/gun-violence-data_01-2013_03-2018.csv");
		} else {
			strIncidents = read("Data\\gun-violence-data_01-2013_03-2018.csv");
		}
		
		System.out.println("Building graph tree of States. Give 30 seconds... ");

		ArrayList<Incidents> Incidents = buildIncidents(strIncidents);
		RedBlackTree<String, States> StateTree = buildBST(Incidents);

		Scanner user_input = new Scanner(System.in);

        /*
         * The below code outputs and formats the answers to the questions to a text file as desired
         */
		File file = new File("src/incidents_out.txt");
		FileWriter out = new FileWriter(file,false);


		out.write("------------" + "\n");
		out.write("|DANGER LOG|");
		out.write("\n" + "------------" + "\n" + "\n");
	

		do {
			String address;
			System.out.print("Enter an address or enter 0 to end program: ");
			address = user_input.nextLine();
			
			if(address.equals("0")) {
				System.out.println("The results of this session have been sent to incidents_out.txt");
				break;
			}
			
			String UserState;
			System.out.print("Enter a State: ");
			UserState = user_input.nextLine();
			if(!(StateTree.contains(UserState))) {
				System.out.println("No Incidents in this area or Wrong Address");
				continue;
			}
	
			String Range;
			System.out.print("Enter a range to look for incidents (in km): ");
			Range = (user_input.next());
			Double range = Double.parseDouble(Range);
	
			States userState = StateTree.get(UserState);
			GeocodingImpl geocode = new GeocodingImpl();
			JsonObject Userlatlong = geocode.getLatLng(address);
			Double userLong = Double.parseDouble(Userlatlong.get("lng").toString());
			Double userLat = Double.parseDouble(Userlatlong.get("lat").toString());
	
			ArrayList<Incidents> stateIncidents = SortIncidents(userState, userLong, userLat);
			ArrayList<Incidents> InRangeIncidents = filterIncidents(stateIncidents, range);
			
			System.out.println("");
			if (InRangeIncidents.size() == 0) {
				System.out.println("No Incidents in this area or in range");
				//user_input.nextLine();
				continue;
			}
			int totalKilled = 0, totalInjured = 0;
			out.write("Gun violence incidents near " + address + " in range " + range + " km: " + "\n");
			System.out.println("Gun violence incidents near " + address + " in range " + range + " km: ");

			for (int i = 0; i < InRangeIncidents.size(); i++) {
				System.out.println(InRangeIncidents.get(i).toString());
				out.write("\n" + InRangeIncidents.get(i).toString());
				totalKilled = totalKilled + InRangeIncidents.get(i).getnumKilled();
				totalInjured = totalInjured + InRangeIncidents.get(i).getnumInjured();
			}
			out.write("\n" + "\n" + "Total Incidents: " + InRangeIncidents.size() + "\n" +
								"Total Killed: " + totalKilled + "\n" +
								"Total Injured: " + totalInjured);

			System.out.println("Total Incidents: " + InRangeIncidents.size() + "\n" +
								"Total Killed: " + totalKilled + "\n" +
								"Total Injured: " + totalInjured);
			
			String DangerLevel;
		
			if(InRangeIncidents.size() < 15) {
				DangerLevel = "LOW DANGER";
			}
			else if (15 <= InRangeIncidents.size() && InRangeIncidents.size() < 30 ) {
				DangerLevel = "MODERATE DANGER";
			}
			else {
				DangerLevel = "HIGH DANGER. Stay Cautious";
			}
			System.out.println("");
			out.write("\n"   + "THE DANGER LEVEL IN THIS AREA IS: " + DangerLevel);
			
			out.write("\n" + "----------------------------------------------------------------------------------------------------------------------------------------------------------" +  "\n" + "\n");
			System.out.println("THE DANGER LEVEL IN THIS AREA IS: " + DangerLevel);
			System.out.println("");
			user_input.nextLine();
		}while(true);
		user_input.close();
		askUser.close();
		out.close();
	}
}
