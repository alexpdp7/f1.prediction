package net.pdp7.commons.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MapUtils {
	
	protected MapUtils() {}
	
	public static <K,V> MapBuilder<K,V> build(K k, V v) {
		return new MapBuilder<K,V>(k,v);
	}
	
	public static Properties createPropertiesFromMap(Map<String, String> map) {
		Properties properties = new Properties();
		
		for(Map.Entry<String, String> entry : map.entrySet()) {
			properties.put(entry.getKey(), entry.getValue());
		}
		return properties;
	}
	
	public static class MapBuilder<K,V> {
		public final Map<K, V> map = new HashMap<K, V>();
		
		protected MapBuilder(K k, V v) {
			map.put(k, v);
		}
		
		public MapBuilder<K,V> put(K k, V v) {
			map.put(k, v);
			return this;
		}

	}

}
