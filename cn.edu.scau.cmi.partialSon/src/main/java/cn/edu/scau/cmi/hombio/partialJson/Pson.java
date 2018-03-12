package cn.edu.scau.cmi.hombio.partialJson;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.function.Consumer;
import com.google.gson.Gson;

public class Pson {
	private enum Flag{
		FIRST,SECOND,THIRD;
	}
	private Flag increFlag(Flag flag) {
		if(flag.equals(Flag.FIRST)) {
			return Flag.SECOND;
		}
		if(flag.equals(Flag.SECOND)) {
			return Flag.THIRD;
		}
		return Flag.THIRD;
	}
	public void fromJson(String json, Type tpeOff) {
		new Gson().fromJson(json,tpeOff);
	}
	public <T> T fromJson(String json, Class<T> classOfT) {
		return new Gson().fromJson(json, classOfT);
	}
	
	public String toJson(Object object) {
		return toJson(object, Flag.FIRST);
	}
	private String toJson(Object object, Flag flag) {
		StringBuilder sb = new StringBuilder();
		if(object == null) {
			return null;
		}
		else if(object instanceof Collection<?>) {
			sb.append("[");
			@SuppressWarnings("unchecked")
			Collection<Object> objCollection = (Collection<Object>) object;
			objCollection.forEach(new Consumer<Object>() {
				public void accept(Object subObject) {
					sb.append(toJson(subObject, flag)).append(",");
				};
			});
			//去掉最后一个 , 
			if(sb.length()>1) {
				sb.deleteCharAt(sb.length()-1);
			}
			sb.append("]");
			return sb.toString();
		}
		else if(object.getClass().isArray()) {
			sb.append("[");
			for(int i=0; i<Array.getLength(object); i++) {
				sb.append(toJson(Array.get(object, i), flag)).append(",");
			}
			if(sb.length()>1) {
				sb.deleteCharAt(sb.length()-1);
			}
			sb.append("]");
			return sb.toString();
		}
		//Java JDK 自带 非集合 类实例
		else if(object.getClass().getName().startsWith("java")) {
			return new Gson().toJson(object);
		}
		//用户自建类实例
		else {
			if(flag.equals(Flag.THIRD)) {
				return null;
			}
			sb.append("{");
			Field[] fields = object.getClass().getDeclaredFields();
			Field.setAccessible(fields, true);
			for(Field field : fields) {
				String fieldName = field.getName();
				Object fieldObject = null;
				try {
					fieldObject = field.get(object);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String fieldJsonString = toJson(fieldObject, increFlag(flag));
				if(fieldJsonString != null) {
					sb.append("\"").append(fieldName).append("\"").append(":").append(fieldJsonString).append(",");
				}
			}
			if(sb.length() > 1) {
				sb.deleteCharAt(sb.length()-1);
			}
			sb.append("}");
			return sb.toString();
		}
	}
}
