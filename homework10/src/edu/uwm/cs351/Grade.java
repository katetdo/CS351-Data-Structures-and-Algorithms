package edu.uwm.cs351;

public class Grade {
	private double grade;

	public Grade(double g) {
		this.grade = g;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Grade) {
			Grade g = (Grade)obj;
			return g.grade == grade;
		}
		return false;
	}
	@Override
	public String toString() {
		return Double.toString(grade);
		
	}
	

}
