package processor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import services.dao.Fax2EMR.dao.Fax2EMRPatientInformationDAO;
import services.dao.Fax2EMR.model.Fax2EMRPatientInformation;
import dataModel.Field;
import api.DBWriter;

public class DBWriterImpl implements DBWriter {
	@SuppressWarnings("rawtypes")
	static Map<String, Class[]> tableMapMapping = new HashMap<String, Class[]>();
	static {
		tableMapMapping.put("Patient_INFORMATION".toLowerCase(), new Class[] { Fax2EMRPatientInformation.class, Fax2EMRPatientInformationDAO.class });
		tableMapMapping.put("patientinformation".toLowerCase(), new Class[] { Fax2EMRPatientInformation.class, Fax2EMRPatientInformationDAO.class });
	}

	@SuppressWarnings("unchecked")
	@Override
	public void writeToDB(List<Field> fields) {
		Map<String, List<Field>> tableMap = new HashMap<String, List<Field>>();
		for (Field field : fields) {
			String table = field.getConfig().getTable().toLowerCase();
			if (!tableMap.containsKey(table)) {
				tableMap.put(table, new ArrayList<Field>());
			}
			tableMap.get(table).add(field);
		}

		for (String table : tableMap.keySet()) {
			@SuppressWarnings("rawtypes")
			Class[] classes = tableMapMapping.get(table);
			if (null == classes) {
				new Exception("unsupported table name: " + table).printStackTrace();
				continue;
			}
			List<Field> fieldList = tableMap.get(table);
			try {
				Object newInstance = classes[0].newInstance();
				for (Field f : fieldList) {
					java.lang.reflect.Field field = classes[0].getField(f.getField());
					field.setAccessible(true);
					field.set(newInstance, f.getContent());
				}
				java.lang.reflect.Field field = classes[0].getField("id");
				field.setAccessible(true);
				field.set(newInstance, 0);
				
				for(java.lang.reflect.Field f : classes[0].getDeclaredFields()) {
					f.setAccessible(true);
					Object object = f.get(newInstance);
					if(null == object && f.getType().equals(String.class)) {
						f.set(newInstance, "");
					}
				}
				Object daoInstance = classes[1].getMethod("getInstance").invoke(null);
				Method method = classes[1].getMethod("insert", classes[0]);
				
				method.invoke(daoInstance, newInstance);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

	}

}
