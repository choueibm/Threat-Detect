/**
 * Storing Information for Gun Incident
 * 
 * @author Harsh Patel
 * @version 3.0
 */

public class Incidents implements Comparable<Incidents> {

	private String UId;
	private double longitude;
	private double latitude;
	private String address;
	private String State;
	private String City;
	private int numKilled;
	private int numInjured;
	private double userLongitude = 0;
	private double userLatitude = 0;
	private double disToIncident = 0;

	/**
	 * Constructor of an Incident ADT with required Information
	 * 
	 * @param UId Unique Id of the Incident
	 * @param address Address gun incident took place
	 * @param State US State location of incident
	 * @param City US City location of incident 
	 * @param longitude Precise longitude location of incident
	 * @param latitude Precise latitude location of incident
	 * @param numKilled Number of people killed in the incident 
	 * @param numInjured Number of people injured in the incident
	 */
	public Incidents(String UId, String address, String State, String City, double longitude, double latitude,
			int numKilled, int numInjured) {
		this.UId = UId;
		this.address = address;
		this.State = State;
		this.City = City;
		this.longitude = longitude;
		this.latitude = latitude;
		this.numKilled = numKilled;
		this.numInjured = numInjured;
	}

	/**
	 * Get Unique ID string of incident
	 * 
	 * @return Unique Id associated with incident
	 */
	public String getUId() {
		return UId;
	}

	/**
	 * Street location of incident
	 * 
	 * @return Address of which gun incident took place
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * US State of Incident
	 * 
	 * @return State location of incident
	 */
	public String getState() {
		return State;
	}

	/**
	 * City of incident
	 * 
	 * @return City gun incident took place
	 */
	public String getCity() {
		return City;
	}

	/**
	 * Longitude Coordinate of Incident
	 * 
	 * @return Longitude Coordinate as double of incident location 
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Latitude Coordinate of Incident
	 * 
	 * @return Latitude Coordinate as double of incident location 
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Change userLatitiude to location that the user enters
	 * 
	 * @param userLatitude Latitude coordinate of users desired location
	 */
	public void setUserLatitude(double userLatitude) {
		this.userLatitude = userLatitude;
	}

	/**
	 * Change userLongitude to location that the user enters
	 * 
	 * @param userLongitude Longitude coordinate of users desired location
	 */
	public void setUserLongitude(double userLongitude) {
		this.userLongitude = userLongitude;
	}

	/**
	 * Number of people killed in incident
	 * 
	 * @return Count of people killed in incident
	 */
	public int getnumKilled() {
		return numKilled;
	}

	/**
	 * Number of people injured in incident
	 * 
	 * @return Count of people injured in incident
	 */
	public int getnumInjured() {
		return numInjured;
	}

	/**
	 * Calculate distance from incident to users desired location
	 * 
	 * @param latitude Latitude coordinate of incident
	 * @param longitude Longitude coordinate of incident
	 * @param userLatitude Latitude coordinate of users desired location
	 * @param userLongitude Longitude coordinate of users desired location
	 * @return Distance in Kilometers from incident to users desired location
	 */
	// Finding distance between two sets of longitude latitude points from :
	// https://www.geodatasource.com/developers/java
	public double toDistance(double latitude, double longitude, double userLatitude, double userLongitude) {
		if ((latitude == userLatitude) && (longitude == userLongitude)) {
			return 0;
		} else {
			double theta = longitude - userLongitude;
			double dist = Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(userLatitude))
					+ Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(userLatitude)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515; //Calculates distance in miles
			dist = dist * 1.609344; //Converts from miles to km
			disToIncident = dist;
			return (dist);
		}
	}
	
	/**
	 * Distance from users desired location to incident
	 * 
	 * @return Distance in kilometers from users desired location to incident
	 */
	public double getDisToIncident() {
		return disToIncident;
	}

	/**
	 * String format of information about the incident to be read by user
	 * 
	 */
	public String toString() {
		return String.format("id : %s, city : %s, state : %s, address : %s, Killed : %d, Injured : %d, Distance to You: %.2f", UId, City, State, address, numKilled, numInjured, disToIncident);
	}
	
	/**
	 * Compares this incident with another in terms of distance from incident 
	 * to users desired location
	 * 
	 * @param that Another incident to compare to
	 * @return 1 If this incident is closer, -1 if this incident is further,
	 * 0 if they are the same distance from users desired location
	 */
	@Override
	public int compareTo(Incidents that) {
		if (this.toDistance(latitude, longitude, userLatitude, userLongitude) > that.toDistance(that.getLatitude(), that.getLongitude(), userLatitude, userLongitude)) {
			return 1;
		} 
		else if (this.toDistance(latitude, longitude, userLatitude, userLongitude) < that.toDistance(that.getLatitude(), that.getLongitude(), userLatitude, userLongitude)) {
			return -1;
		}
		else {
			return 0;
		}
	}
}
