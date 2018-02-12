package cn.edu.scau.cmi.hombio.partialJson;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.sf.json.JSONObject;

/**
 *基于Gson的半json化
 *
 */

public class PSon 
{
    public enum Flag{
    	FIRST_TIME,SECOND_TIME,BASIC_OBJECT_1,BASIC_OBJECT_2;
    }

//    将Pson对象封装为普通对象，可以用来在界面中使用。
    public <T extends Object> T fromPson(String partialJson, Class<T> clazz) {
    	return new Gson().fromJson(partialJson, clazz);
    }
//    将对象转换为Json对象
    public String toPson(Object object,Flag flag) throws IllegalArgumentException, IllegalAccessException {
    	//判断是否为集合类
//    	***集合类Pson化***
    	if(object instanceof Collection) {
			@SuppressWarnings("unchecked")
			Collection<Object> collection = (Collection<Object>)object;
			StringBuilder psonStringBuilder = new StringBuilder();
			psonStringBuilder.append("[");
			collection.forEach(new Consumer<Object>() {
				public void accept(Object object) {
					String objectJson;
					try {
						objectJson = toPson(object, flag);
						psonStringBuilder.append(objectJson).append(",");
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				};
			});
			if(psonStringBuilder.length()>1) {
				psonStringBuilder.deleteCharAt(psonStringBuilder.length()-1);
			}
			psonStringBuilder.append("]");
			return psonStringBuilder.toString();
		}
//    	***Map对象***
    	
    	else if(object instanceof Map) {
			StringBuilder partialJsonStringBuilder = new StringBuilder();
			partialJsonStringBuilder.append("{");
			@SuppressWarnings("unchecked")
			Map<Object,Object> map = (Map<Object,Object>)object;
			for(Object key : map.keySet()) {
				Object value = map.get(key);
				partialJsonStringBuilder.append(toPson(key, flag)).append(":").append(toPson(value, flag)).append(",");
			}
			if(partialJsonStringBuilder.length()>1) {
				partialJsonStringBuilder.deleteCharAt(partialJsonStringBuilder.length()-1);
			}
			partialJsonStringBuilder.append("}");
			return partialJsonStringBuilder.toString();
		}
//    	***Array类型***
    	else if(object!=null&&object.getClass().isArray()) {
			StringBuilder psonStringBuilder = new StringBuilder();
			psonStringBuilder.append("[");
			for(int i=0; i<Array.getLength(object);i++) {
				Object arrayi = Array.get(object, i);
				String objectJson;
				try {
					objectJson = toPson(arrayi, flag);
//					if(!objectJson.equals("{}")&&!objectJson.equals("null")) {
//						partialJsonStringBuilder.append(objectJson).append(",");
//					}
					psonStringBuilder.append(objectJson).append(",");
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			if(psonStringBuilder.length()>1) {
				psonStringBuilder.deleteCharAt(psonStringBuilder.length()-1);
			}
			psonStringBuilder.append("]");
			return psonStringBuilder.toString();
		}
    	
//    	***基本类型对象***
    	
    	else {
			//这里开始处理基本对象（即为基本数据类型、包装类、String和只含这些类型属性的对象）
			try {
				// 先判断是否存在循环链
				if (object instanceof Calendar) {
					if (flag.equals(Flag.BASIC_OBJECT_2)) {
						return "null";
					} else {
						return new Gson().toJson(object);
					}
				}
				try {
					JSONObject.fromObject(object);
				} catch (Exception e) {
					if (e.getMessage().contains("cycle")) {
						// 如果在json基本对象时遇到含有循环链的组合对象，直接返回{{}}特殊标识
						if (flag.equals(Flag.BASIC_OBJECT_1) || flag.equals(Flag.BASIC_OBJECT_2)) {
							return "{{}}";
						} else {
							throw new Exception();
						}
					}
				}
				// 不存在循环链
				String preJson = new Gson().toJson(object);
				if (!preJson.contains("{")) {
					return preJson;
				} else if (preJson.lastIndexOf("{") == 0) {
					if (flag.equals(Flag.BASIC_OBJECT_2)) {
						return "null";
					} else {
						return preJson;
					}
				} else {
					if (flag.equals(Flag.BASIC_OBJECT_1) || flag.equals(Flag.BASIC_OBJECT_2)) {
						return "{{}}";
					} else {
						throw new Exception();
					}
				}
			} catch (Exception exception) {
				// 这里开始Json化组合对象
				// 1. 先json化基本类型对象
				StringBuilder psonStringBuilder = new StringBuilder(getBasicTypeJson(object));
				// 2. 再json化基本对象
				String basicObjectJson = "";
				Map<String, Object> map = getNotBasicTypeMap(object);
				// json化最外层组合对象中的基本对象
				if (flag.equals(Flag.FIRST_TIME)) {
					basicObjectJson = getBasicObjectJson(map, Flag.BASIC_OBJECT_1);
				}
				// json化第二层组合对象中的基本对象
				if (flag.equals(Flag.SECOND_TIME)) {
					basicObjectJson = getBasicObjectJson(map, Flag.BASIC_OBJECT_2);
				}
				if (basicObjectJson.length() != 0) {
					psonStringBuilder.deleteCharAt(psonStringBuilder.length() - 1);
					if (psonStringBuilder.length() > 1) {
						psonStringBuilder.append(",").append(basicObjectJson).append("}");
					} else {
						psonStringBuilder.append(basicObjectJson).append("}");
					}
				}
				// 3. 最后json组合对象：第一次时要json化组合对象里的组合对象，第二次不用
				if (flag.equals(Flag.FIRST_TIME)) {
					String secondJson = getSecondJson(map);
					if (secondJson.length() != 0) {
						psonStringBuilder.deleteCharAt(psonStringBuilder.length() - 1);
						if (psonStringBuilder.length() > 1) {
							psonStringBuilder.append(",").append(secondJson).append("}");
						} else {
							psonStringBuilder.append(secondJson).append("}");
						}
					}
				}
				return psonStringBuilder.toString();
			}
		}
    }
    
    //获取基本对象的json字符串同时将其从map移除
    private String getBasicObjectJson(Map<String,Object> map ,Flag flag) throws IllegalArgumentException, IllegalAccessException {
    	StringBuilder stringBuilder = new StringBuilder();
    	List<String> keyList = new ArrayList<>();
    	for(String key : map.keySet()) {
    		Object object = map.get(key);
    		String basicObjectJson = toPson(object, flag);
    		if(basicObjectJson.lastIndexOf("{")<=0) {
    			keyList.add(key);
    		}
    		//如果基本对象的Json化字符串不为null并且有字符或者为{......}形的字符串
    		if(!basicObjectJson.equals("null")&&basicObjectJson.lastIndexOf("{")<=0) {
    			stringBuilder.append("\"").append(key).append("\"").append(":");
    			stringBuilder.append(basicObjectJson).append(",");
    		}
//    		if(basicObjectJson.lastIndexOf("{")<=0) {
//    			stringBuilder.append("\"").append(key).append("\"").append(":");
//    			stringBuilder.append(basicObjectJson).append(",");
//    		}
    	}
    	if(stringBuilder.length()!=0) {
    		stringBuilder.deleteCharAt(stringBuilder.length()-1);
    	}
    	for(String key : keyList) {
    		map.remove(key);
    	}
    	return stringBuilder.toString();
    }
    
    //获取组合对象的json字符串
    private String getSecondJson(Map<String,Object> map ) throws IllegalArgumentException, IllegalAccessException {
    	StringBuilder stringBuilder = new StringBuilder();
    	for(String key : map.keySet()) {
    		Object object = map.get(key);
    		String secondJson = toPson(object, Flag.SECOND_TIME);
    		if(!secondJson.equals("null")) {
    			stringBuilder.append("\"").append(key).append("\"").append(":");
    			stringBuilder.append(secondJson).append(",");
    		}
//    		stringBuilder.append("\"").append(key).append("\"").append(":");
//			stringBuilder.append(secondJson).append(",");
    	}
    	if(stringBuilder.length()!=0) {
    		stringBuilder.deleteCharAt(stringBuilder.length()-1);
    	}
    	return stringBuilder.toString();
    }
    
    //获取不是基本类型对象的对象
    private Map<String,Object> getNotBasicTypeMap(Object object) throws IllegalArgumentException, IllegalAccessException{
    	Map<String,Object> map = new HashMap<>();
    	Class<? extends Object> classType = object.getClass();
		Field[] fields = classType.getDeclaredFields();
		Field.setAccessible(fields, true);
		for (Field field : fields) {
			String fieldTypeName = field.getType().getSimpleName();
			if(!isBasicObject(fieldTypeName)) {
				map.put(field.getName(), field.get(object));
			}
		}
		return map;
    }
  
//    获取基本类型对象的json字符串，例如int, float，等等，获取这些基本类型对象的json字符串
    private String getBasicTypeJson(Object object) throws IllegalArgumentException, IllegalAccessException{
    	Class<? extends Object> classType = object.getClass();
		Field[] fields = classType.getDeclaredFields();
		Field.setAccessible(fields, true);
		JsonObject basicObject =  new JsonObject();
		for (Field field : fields) {
			String fieldTypeName = field.getType().getSimpleName();
			String fieldName = field.getName();
			Object fieldObject = field.get(object);
			switch(fieldTypeName) {
			case "int":
			{
				basicObject.addProperty(fieldName, new Integer((int)fieldObject));
				break;
			}
			case "float":
			{
				basicObject.addProperty(fieldName, new Float((float)fieldObject));
				break;
			}
			case "double":
			{
				basicObject.addProperty(fieldName, new Double((double)fieldObject));
				break;
			}
			case "char":	
			{
				basicObject.addProperty(fieldName, new Character((char)fieldObject));
				break;
			}
			case "boolean":
			{
				basicObject.addProperty(fieldName, new Boolean((boolean)fieldObject));
				break;
			}
			case "byte":
			{
				basicObject.addProperty(fieldName, new Byte((byte)fieldObject));
				break;
			}
			case "long":
			{
				basicObject.addProperty(fieldName, new Long((long)fieldObject));
				break;
			}
			case "short":
			{
				basicObject.addProperty(fieldName, new Short((short)fieldObject));
				break;
			}
		}
	}
	 return basicObject.toString();
  }
    
	/**
	 * 判断是否为基本类型
	 * @param fieldTypeName
	 * @return
	 */
	private  boolean isBasicObject(String typeName){
		switch(typeName){
		case "int":
		case "char":
		case "boolean":
		case "byte":
		case "float":
		case "double":
		case "short":
		case "long":return true; 
		default:return false;
		}
	}
}