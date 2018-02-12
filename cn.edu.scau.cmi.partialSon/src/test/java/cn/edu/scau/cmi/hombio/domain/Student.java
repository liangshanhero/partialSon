package cn.edu.scau.cmi.hombio.domain;

import java.util.Calendar;
import java.util.Date;

public class Student {
	private String name;
	private int age;
	private Date birthday;
	private Float gpa;
	private Teacher teacher;
	
	private Course[] courseArray;
	
	public Student() {
		setName("Hombio Chen");
		setAge(18);
		setBirthday(new Date());
		setGpa(new Float(3.95));
		courseArray = new Course[3];
	}

	public Student(String name) {
		setName(name);
		setBirthday(new Date());
	}
	

	public Course[] getCourseArray() {
		return courseArray;
	}




	public void setCourseArray(Course[] courseArray) {
		this.courseArray = courseArray;
	}




	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public Date getBirthday() {
		return birthday;
	}


	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}


	public Float getGpa() {
		return gpa;
	}


	public void setGpa(Float gpa) {
		this.gpa = gpa;
	}


	public Teacher getTeacher() {
		return teacher;
	}


	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	
}
