package api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import services.dao.Fax2EMR.model.Fax2EMRPatientInformation;
import services.dao.Fax2EMR.model.Fax2EMRTestForm;

public class DBModelRegester {
	static final String projectPrefix = "Fax2EMR";
	static List<Class<?>> configuredClass;
	static Map<String, Class<?>> shortNameMap;
	public static void main(String[] args){
		init();
	}
	static  {
		init();
	}
	
	static void init() {
		configuredClass = new ArrayList<Class<?>>();
		shortNameMap = new HashMap<String, Class<?>>();
		regester(Fax2EMRPatientInformation.class);
		regester(Fax2EMRTestForm.class);
	}
	
	static void regester(Class<?> dbClass) {
//		String canonicalName = dbClass.getCanonicalName();
		String simpleName = dbClass.getSimpleName();
		if(!simpleName.startsWith(projectPrefix)) {
			System.err.println("Unable to regester class:" + dbClass.getName());
			System.err.println("Not inner model.");
			return;
		}
		String shortName = simpleName.substring(projectPrefix.length());
		configuredClass.add(dbClass);
		shortNameMap.put(shortName, dbClass);
	}
	
	public static List<Class<?>> getClasses() {
		return configuredClass;
	}
	
	public static Class<?> getClassByShortName(String shortName) {
		return shortNameMap.get(shortName);
	}
	
	public static List<String> getShortNameList() {
		List<String> result = new ArrayList<String>();
		result.addAll(shortNameMap.keySet());
		return result;
	}
	
	public static Set<Entry<String, Class<?>>> getEntries() {
		return shortNameMap.entrySet();
	}
}	
