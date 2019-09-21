/**
 * Storing Gun Violence Incidents in appropriate States Location
 * 
 * @author Harsh Patel
 * @version 2.0
 */

import java.util.ArrayList;

public class States implements Comparable<States> {
	private String StateName;
	private ArrayList<Incidents> IncidentsList = new ArrayList<>();
	
	/**
	 * Constructor of State ADT initialized by Name of State
	 * 
	 * @param StateName Name of US State
	 */
	public States(String StateName) {
		this.StateName = StateName;
	}
	
	/**
	 * Add Incident located in this State to IncidentList
	 * 
	 * @param incident Incident that took place in this State
	 */
	public void addIncidents(Incidents incident) {
		IncidentsList.add(incident);
	}
	
	/**
	 * State name of this US State
	 * 
	 * @return State name of this US State as string
	 */
	public String getStateName() {
		return StateName;
	}
	
	/**
	 * List of incidents that happened in this State location
	 * 
	 * @return ArrayList of incidents in this US State location
	 */
	public ArrayList<Incidents> getIncidentsList(){
		return IncidentsList;
	}
	
	/**
	 * String format to present relevant info in this ADT to user
	 * 
	 */
	public String toString() {
		return String.format("State Name : %s", StateName);
	}
	
	/**
	 * Compare strings of US State names
	 * 
	 * @param that Another US State to compare with this US State
	 * @return 1 if this State name string is greater then the State name
	 * being compared to, -1 if other State's name is greater, and 0 if equal
	 */
	@Override
	public int compareTo(States that) {
		if ((this.StateName).compareTo(that.StateName) > 0) {
			return 1;
		}
		else if ((this.StateName).compareTo(that.StateName) < 0) {
			return -1;
		}
		else {
			return 0;
		}
	}
}
