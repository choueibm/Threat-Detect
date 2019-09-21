import java.util.ArrayList;
import java.util.Random;

/**
 * This is a quicksort class that is not generic, it has been implemented only to be used with incidents.
 * 
 * @author Michael Barreiros
 * @version 2.0
 *
 * Citation: ArrayList implementation of QuickSort is referencing
 * https://www.withexample.com/quicksort-implementation-example-using-arraylist-java/
 */
public class QuickSort {
	private static ArrayList<Incidents> inputArray = new ArrayList<Incidents>();
	
	// following two lines are variables needed to create a random object to use for shuffling our array
	private static Random random; 
	private static long seed;

	/**
	 * This is the constructor for the quicksort class, it takes an ArrayList of incidents to be sorted
	 * 
	 * @param inputArray the array to be sorted
	 */
	public QuickSort(ArrayList<Incidents> inputArray){
        QuickSort.inputArray = inputArray;
    }
	
	/**
	 * This method randomly shuffles the incoming array
	 * 
	 */
	public static void shuffle() {
		seed = System.currentTimeMillis();
		random = new Random(seed);

		//If the inputArray is not null then shuffle it, otherwise do nothing
		if (inputArray != null) {
			int n = inputArray.size();
			for (int i = 0; i < n; i++) {
				//get a random position and swap i with that random position
				int r = i + random.nextInt(n - i);
				Incidents temp = inputArray.get(i);
				inputArray.set(i, inputArray.get(r));
				inputArray.set(r, temp);
			}
		}
	}
	
	/**
	 * This method does the partitioning that is necessary for quicksort to function
	 * 
	 * @param lo the lower bound of the array to be partitioned
	 * @param hi the upper bound of the array to be partitioned
	 * @return returns the final location of the incident that was partitioned correctly
	 */
	public static int partition(int lo, int hi) {
		int i = lo;
		int j = hi + 1;
		Incidents v = inputArray.get(lo); // Partitioning element

		while (true) {
			while (inputArray.get(++i).compareTo(v) < 0)
				if (i == hi)
					break;
			while (v.compareTo(inputArray.get(--j)) < 0)
				if (j == lo)
					break;
			if (i >= j)
				break;
			//after comparing pointers i and j to v, we have i and j pointing to the incidents that need to be swapped, swap i and j
			Incidents temp = inputArray.get(i);
			inputArray.set(i, inputArray.get(j));
			inputArray.set(j, temp);
		}
		//swap the partitioning element to its correct position
		Incidents temp = inputArray.get(lo);
		inputArray.set(lo, inputArray.get(j));
		inputArray.set(j,temp);
		
		//return the new element to be partitioned
		return j;
	}

	/**
	 * basic quick sort shuffles the array first then calls the other sortBasicQuick function that uses 0 as its lower bound and the size of the 
	 * array - 1 as its upper bound
	 * 
	 */
	public static void sortBasicQuick() {
		shuffle();
		sortBasicQuick(0, inputArray.size() - 1);
	}
	
	/**
	 * A recursive basic quicksort that partitions an element then performs quicksort on sub-array to the left of the partitioned element and then to the left
	 * 
	 * @param lo the lower bound of the array to be sorted
	 * @param hi the upper bound of the array to be sorted
	 */
	public static void sortBasicQuick(int lo, int hi) {
		if (hi <= lo)
			return;
		int j = partition(lo, hi); // Partition array
		sortBasicQuick(lo, j - 1); // Sort the left sub-array
		sortBasicQuick(j + 1, hi); // Sort the right sub-array
	}

}
