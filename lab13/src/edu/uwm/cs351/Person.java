package edu.uwm.cs351;

public class Person implements Comparable<Person> {

	public String firstName;
	public String lastName;
	
	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "Person("+ lastName + ", " + firstName + ")";
	}

	public int compareTo(Person o) {
		
		int c = this.lastName.compareTo(o.lastName);
		
		if (c != 0)
			return c;
		
		c = this.firstName.compareTo(o.firstName);
		
		return c;
	}
}
