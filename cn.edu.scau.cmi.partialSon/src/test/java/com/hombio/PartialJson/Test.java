package com.hombio.PartialJson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

import cn.edu.scau.cmi.hombio.partialJson.HombioJson;
import cn.edu.scau.cmi.hombio.partialJson.HombioJson.Flag;

public class Test {
	static HombioJson   hombioSon = new HombioJson();
	
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {

		testCompositeObjectSet();
	}
	
	private static void testNull() throws IllegalArgumentException, IllegalAccessException {
		System.out.println(hombioSon.toPartialJson(null, HombioJson.Flag.FIRST_TIME));
	}
	private static void testBasicType() throws IllegalArgumentException, IllegalAccessException {
		System.out.println("测试基本类型");
		int a = 1;
		System.out.println(hombioSon.toPartialJson(a, HombioJson.Flag.FIRST_TIME));
		float b = (float)3.14;
		System.out.println(hombioSon.toPartialJson(b, HombioJson.Flag.FIRST_TIME));
		double c = (double)3.14159;
		System.out.println(hombioSon.toPartialJson(c, HombioJson.Flag.FIRST_TIME));
		long d = new Long(88888888);
		System.out.println(hombioSon.toPartialJson(d, HombioJson.Flag.FIRST_TIME));
		byte e = 127;
		System.out.println(hombioSon.toPartialJson(e, HombioJson.Flag.FIRST_TIME));
		boolean f = false;
		System.out.println(hombioSon.toPartialJson(f, HombioJson.Flag.FIRST_TIME));
		char g = 0;
		System.out.println(hombioSon.toPartialJson(g, HombioJson.Flag.FIRST_TIME));
	}
	private static void testWrapperObject() throws IllegalArgumentException, IllegalAccessException {
		System.out.println("测试包装类型");
		Integer a = new Integer(111);
		System.out.println(hombioSon.toPartialJson(a, HombioJson.Flag.FIRST_TIME));
		Double b = new Double(3.14159);
		System.out.println(hombioSon.toPartialJson(b, HombioJson.Flag.FIRST_TIME));
		Character c = new Character('X');
		System.out.println(hombioSon.toPartialJson(c, HombioJson.Flag.FIRST_TIME));
		Boolean d = new Boolean(true);
		System.out.println(hombioSon.toPartialJson(d, HombioJson.Flag.FIRST_TIME));
		Float e = new Float(8.888);
		System.out.println(hombioSon.toPartialJson(e, HombioJson.Flag.FIRST_TIME));
		Long f = new Long(9999999);
		System.out.println(hombioSon.toPartialJson(f, HombioJson.Flag.FIRST_TIME));
		Byte g = new Byte((byte) 44);
		System.out.println(hombioSon.toPartialJson(g, HombioJson.Flag.FIRST_TIME));
	}
	private static void testListObject() throws IllegalArgumentException, IllegalAccessException {
		System.out.println("测试List");
		List<Integer> list =new ArrayList<>();
		for(int i=0; i<10;i++) {
			list.add(i);
		}
		System.out.println(hombioSon.toPartialJson(list, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(list));
	}
	private static void testBasicObject() throws IllegalArgumentException, IllegalAccessException {
		System.out.println("测试基本对象");
		String str = "This is a string";
		System.out.println(hombioSon.toPartialJson(str, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(str));
		Date date = new Date();
		System.out.println(hombioSon.toPartialJson(date, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(date));
		Calendar calendar = Calendar.getInstance();
		System.out.println(hombioSon.toPartialJson(calendar, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(calendar));
	}
	private static void testArray() throws IllegalArgumentException, IllegalAccessException{
		System.out.println("测试数组");
		String[] strArray = {"a","b","c","d","e","f"};
		System.out.println(hombioSon.toPartialJson(strArray, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(strArray));
		Number[] numberArray = {new Integer(100),new Double(3.14159),new Float(1.1314),new Byte((byte) 88),new Long(1314520)};
		System.out.println(hombioSon.toPartialJson(numberArray, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(numberArray));
		Course[] courseArray = {new Course("语文"),new Course("数学"),new Course("英语")};
		System.out.println(hombioSon.toPartialJson(courseArray, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(courseArray));
	}
	private static void testCompositeObject() throws IllegalArgumentException, IllegalAccessException {
		System.out.println("测试组合对象");
		Student stu = new Student();
		System.out.println(hombioSon.toPartialJson(stu, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(stu));
		stu.setTeacher(new Teacher());
		System.out.println(hombioSon.toPartialJson(stu, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(stu));
	}
	private static void testCycleCompositeObject() throws IllegalArgumentException, IllegalAccessException {
		System.out.println("测试含有循环链的组合对象");
		Student student = new Student();
		Teacher teacher = new Teacher();
		student.setTeacher(teacher);
		teacher.setStudent(student);
		System.out.println(hombioSon.toPartialJson(student, HombioJson.Flag.FIRST_TIME));
		System.out.println(hombioSon.toPartialJson(teacher, HombioJson.Flag.FIRST_TIME));
//		这里将报错，因为存在循环链
//		System.out.println(new Gson().toJson(student));
	}
	
	private static void testCompositeObjectSet() throws IllegalArgumentException, IllegalAccessException {
		System.out.println("测试Set和map");
		Set<Student> students = new HashSet<>();
		for(int i=0;i<10;i++) {
			students.add(null);
		}
		System.out.println(hombioSon.toPartialJson(students, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(students));
		Map<String,Student> map =new HashMap<>();
		map.put("1", new Student());
		map.put("2", new Student("陈宏彪"));
		System.out.println(hombioSon.toPartialJson(map, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(map));
		Map<Teacher,Student> map1 =new HashMap<>();
		map1.put(new Teacher(), new Student());
		System.out.println(hombioSon.toPartialJson(map1, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(map1));
	}
	private static void testCompositeObjectWithArray() throws IllegalArgumentException, IllegalAccessException {
		System.out.println("测试含有数组的组合对象");
		Student stu = new Student();
		System.out.println(hombioSon.toPartialJson(stu, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(stu));
		stu.setCourseArray(null);
		System.out.println(hombioSon.toPartialJson(stu, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(stu));
		Course[] courseArray = {new Course("语文"),new Course("数学"),new Course("英语")};
		stu.setCourseArray(courseArray);
		System.out.println(hombioSon.toPartialJson(stu, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(stu));
	}
	private static void testCompositeObjectWithCollection() throws IllegalArgumentException, IllegalAccessException {
		System.out.println("测试含有集合的组合对象");
		Teacher teacher = new Teacher();
		System.out.println(hombioSon.toPartialJson(teacher, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(teacher));
		Set<Course> courseSet = new HashSet<>();
		courseSet.add(new Course("语文"));
		courseSet.add(new Course("数学"));
		courseSet.add(new Course("英语"));
		teacher.setCourseSet(courseSet);
		System.out.println(hombioSon.toPartialJson(teacher, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(teacher));
		List<Student> studentList = new ArrayList<>();
		studentList.add(new Student("张三"));
		studentList.add(new Student("李四"));
		Student student = new Student("王五");
		student.setTeacher(new Teacher());
		studentList.add(student);
		teacher.setStudentList(studentList);		
		System.out.println(hombioSon.toPartialJson(teacher, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(teacher));
	}
	private static void testMutiDimensionArray() throws IllegalArgumentException, IllegalAccessException {
		System.out.println("测试多维数组");
		String[][] str = new String[2][3];
		str[0] = new String[3];
		str[1] = new String[3];
		str[0][1] = "asd";
		System.out.println(hombioSon.toPartialJson(str, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(str));
		Student[][] students = new Student[2][2];
		students[0][0] = new Student();
		students[0][0].setTeacher(new Teacher());
		students[1][1] = new Student("陈宏彪");
		System.out.println(hombioSon.toPartialJson(students, HombioJson.Flag.FIRST_TIME));
		System.out.println(new Gson().toJson(students));
	}
}