package edu.uwm.cs351;

public class Student implements Comparable<Student> {

	private String ePanther;
	public Student(String ePanther) {
		this.ePanther = ePanther;
	}

	public String getName() {
		return ePanther;
	}
	public void setName(String name) {
		this.ePanther = name;
	}
	@Override
	public int compareTo(Student o) {
		return ePanther.compareTo(o.getName());
	}
	@Override
	public String toString() {
		return ePanther;
	}




}
