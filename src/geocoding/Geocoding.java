package geocoding;
//https://www.programcreek.com/java-api-examples/?code=SofiaRosetti/S3-16-d-rescue/S3-16-d-rescue-master/utils/src/main/java/it/unibo/drescue/geocoding/GeocodingImpl.java#
import com.google.gson.JsonObject;

/**
 * An interface modelling geocoding operations.
 */
public interface Geocoding {

    /**
     * Gets the short name of a district from its latitude and longitude.
     *
     * @param latitude  the district latitude
     * @param longitude the district longitude
     * @return the district short name
     * @throws GeocodingException if an error occur while executing geocoding operations
     */
    String getDistrict(double latitude, double longitude) throws GeocodingException;

    /**
     * Gets latitude and longitude of a specified address.
     *
     * @param address the address
     * @return a JsonObject containing the address latitude and longitude
     * @throws GeocodingException if an error occur while executing geocoding operations
     */
    JsonObject getLatLng(String address) throws GeocodingException;
}