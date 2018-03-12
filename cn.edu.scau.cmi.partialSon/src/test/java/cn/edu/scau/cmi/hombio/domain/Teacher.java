package cn.edu.scau.cmi.hombio.domain;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class Teacher {
	private String name;
	private Double salary;
	private Student student;

	Set<Course> courseSet ;
	List<Student> studentList;
	
	
	
	
	public List<Student> getStudentList() {
		return studentList;
	}
	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}
	public Set<Course> getCourseSet() {
		return courseSet;
	}
	public void setCourseSet(Set<Course> courseSet) {
		this.courseSet = courseSet;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getSalary() {
		return salary;
	}
	public void setSalary(Double salary) {
		this.salary = salary;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	
	public Teacher() {
		setName("������");
	}
}
