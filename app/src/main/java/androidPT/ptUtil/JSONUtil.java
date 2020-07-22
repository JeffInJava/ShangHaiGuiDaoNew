package androidPT.ptUtil;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class JSONUtil{
	public static <T> Object parseToObject(String parseStr,Class<T> mClass){
		Object obj = null;
		try {
			obj = JSON.parseObject(parseStr, mClass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	public static <T, E> List<E> parseToArray(String parseStr,Class<T> mClass){
		List<E> list = null;
		try {
			list = (List<E>) JSON.parseArray(parseStr, mClass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public static Object parseObj(String parseStr){
		Object obj = null;
		try {
			obj = JSON.parseObject(parseStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
}
