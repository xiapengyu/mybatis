/**
 * Copyright 2017-2025 Evergrande Group.
 */
package com.spring.mybatis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Json工具类
 */
public class JsonUtil {

	private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

	private static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
	}
	
	private JsonUtil(){
		throw new IllegalStateException("Utility class");
	}

	public static ObjectMapper getObjectMapper() {
		return mapper;
	}

	/**
	 * parse and convert a json string to an object
	 * 
	 * <pre>
	 * MyType a = fromJson(&quot;{\&quot;a\&quot;: 1, \&quot;b\&quot;: 2}&quot;, MyType.class);
	 * int[] arr = fromJson(&quot;[1, 2]&quot;, int[].class);
	 * List list = fromJson(&quot;[1, 2]&quot;, List.class);
	 * List list = fromJson(&quot;[1, 2]&quot;, Object.class);
	 * Map map = fromJson(&quot;{\&quot;a\&quot;: 1, \&quot;b\&quot;: 2}&quot;, Map.class); // same as toMap(json);
	 * Map map = fromJson(&quot;{\&quot;a\&quot;: 1, \&quot;b\&quot;: 2}&quot;, Object.class); // same as
	 * 															// toMap(json);
	 * </pre>
	 * 
	 * @param json
	 * @param t
	 * @return null if there is any exception parsing json string
	 */
	public static <T> T fromJson(String json, Class<T> t) {

		if (json == null) {
			return null;
		}
		try {
			return mapper.readValue(json, t);
		} catch (Exception e) {
			logger.info("Cannot parse json string to Object. Json: <" + json
					+ ">, Object class: <" + t.getName() + ">.", e);
		}
		return null;
	}

	/**
	 * Convert a JsonArray string to a list of Class<T>
	 * 
	 * <pre>
	 *   List<Integer> l = toList("[1, 2]", Integer.class);
	 *   List<MyType> l = toList("[{"a": 2}, {"a": 3}]", MyType.class);
	 *   List<Map> l = toList("[{"a": 2}, {"a": 3}]", Map.class);
	 *   List<Map> l = toList("[{"a": 2}, {"a": 3}]", Object.class);
	 * </pre>
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 * @exception throw
	 *                IOException if there is invalid json string
	 */
	public static <T> List<T> toList(String json, Class<T> clazz)
			throws IOException {
		// .constructParametrizedType is introduced from jackson 2.5
		JavaType javaType = mapper.getTypeFactory().constructParametricType(
				List.class, clazz);
		return mapper.readValue(json, javaType);
	}

	/**
	 * convert a map to object
	 * 
	 * @param map
	 * @param t
	 * @return
	 */
	public static <T> T fromMap(Map<?, ?> map, Class<T> t) {

		if (map == null) {
			return null;
		}
		try {
			return mapper.readValue(toJsonString(map), t);
		} catch (Exception e) {
			logger.info("Cannot parse map to Object. Map: <" + map
					+ ">, Object class: <" + t.getName() + ">.", e);
		}
		return null;
	}

	/**
	 * convert json object to a Map, with designated element type
	 * 
	 * @param jsonText
	 * @return
	 * @exception throw
	 *                IOException if there is invalid json string
	 */
	public static <T> Map<String, T> toMap(String jsonText, Class<T> clazz)
			throws IOException {
		JavaType javaType = mapper.getTypeFactory().constructParametricType(
				Map.class, String.class, clazz);
		return mapper.readValue(jsonText, javaType);
	}

	@SuppressWarnings("rawtypes")
	public static Map toMap(String jsonText) {
		return fromJson(jsonText, Map.class);
	}

	/**
	 * convert any object to json string
	 * 
	 * <pre>
	 * toJsonString(map) returns{ "b" : "B", "a" : "A" }
	 * toJsonString(list) returns ["b", "a"]
	 * toJsonString(array) returns ["b", "a"]
	 * toJsonString(obj) returns { "fieldA" : "a", "fieldB" : "b" }
	 * </pre>
	 * 
	 * @param obj
	 * @return json string
	 */
	public static String toJsonString(Object obj) {
		try {
			if (obj != null) {
				return mapper.writeValueAsString(obj);
			}
		} catch (Exception e) {
			logger.warn("Cannot convert to json " + obj);
		}
		return "{}";
	}

	public static String toJsonStr(Object obj, boolean ignoreError) {
		try {
			if (obj != null) {
				return mapper.writeValueAsString(obj);
			}
		} catch (Exception e) {
			logger.debug("convert to json error for object: {}", obj, e);
			if (!ignoreError) {
				throw new IllegalArgumentException(
						"convert to json error for object", e);
			}
		}
		return null;
	}
	
	
	public static String toJson(Object o) {
		try {
			return mapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			logger.warn("toJson exception", e);
			return null;
		}
	}

	public static <T> T load(String filePath, Class<T> clazz) {
		InputStream is = null;
		try {
			is = new FileInputStream(new File(filePath));
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		}
		return load(is, clazz);
	}

	public static <T> T load(InputStream is, Class<T> clazz) {

		try {
			return mapper.readValue(is, clazz);
		} catch (JsonParseException e) {
			logger.warn("JsonParseException", e);
		} catch (JsonMappingException e) {
			logger.warn("JsonMappingException", e);
		} catch (IOException e) {
			logger.warn("IOException", e);
		}

		return null;
	}

	public static <T> T parser(String json, Class<T> clzz) {
		try {
			return mapper.readValue(json, clzz);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public final static boolean isJSONValid(String test) {
		try {
			JSONObject.parseObject(test);
		} catch (JSONException ex) {
			try {
				JSONObject.parseArray(test);
			} catch (JSONException ex1) {
				return false;
			}
		}
		return true;
	}
}
