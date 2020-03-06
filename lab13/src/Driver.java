import java.util.Arrays;

import edu.uwm.cs351.Person;
import edu.uwm.cs351.util.MergeSort;

public class Driver {

	public static void main(String[] args) {
		
		Person[] brewers = { 
				new Person("Christian", "Yelich"),
				new Person("Ryan", "Braun"),
				new Person("Khloe", "Kardashian"),
				new Person("Lorenzo", "Cain"),
				new Person("Mike", "Moustakas"),
				new Person("Jesus", "Aguilar"),
				new Person("Yasmani", "Grandal"),
				new Person("Josh", "Hader"),
				new Person("Travis", "Shaw"),
				new Person("Kim", "Kardashian")
		};
			
		System.out.println("original list: " + Arrays.toString(brewers));
		
		Person[] sortedBrewers = MergeSort.sort(brewers);
		
		System.out.println("sorted list:   " + Arrays.toString(sortedBrewers));
	}
}
