package com.birth.helper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.birth.back.impl.BackendDatabase;

public class ParentHelper {
	
	public static List loadRecords(ResultSet rs, Class clazz) {
		
		Field[] fields = null;
		List setters = null;
		List output = new ArrayList();
		
		try {
			setters = Arrays.stream(clazz.getDeclaredMethods()).filter(m -> m.getName().contains("set")).collect(Collectors.toList());
		} catch(SecurityException e) {
		}
		
		try {
			fields = clazz.getDeclaredFields();
		}catch(SecurityException e) {
		}
		
		try {
			while(rs!=null && rs.next() && fields != null && fields.length >= 1) {
				Object obj = clazz.newInstance();
				
				for (Field field : fields) {
					if (!field.getName().equals("TABLE_NAME")) {
						try {
							Method method = getMethod(setters, field, clazz);
							try {
								if(method !=null) {
									if (field.getType().equals(String.class)) {
										method.invoke(obj, rs.getString(field.getName()));
									} else if (field.getType().equals(Integer.TYPE)) {
										method.invoke(obj, rs.getInt(field.getName()));
									}
								}
							} catch (IllegalArgumentException | InvocationTargetException e) { }

						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
				if(obj !=null) { output.add(obj); }
			}
		} catch (SQLException | InstantiationException | IllegalAccessException e1) {}
		
		return output;
	}
	
	public static Method getMethod(List<Method> setters, Field field, Class classToLoad) {
		try {
			return (Method) setters.stream().filter(k -> ("set"+ field.getName()).equalsIgnoreCase(k.getName())).findFirst().orElse(null);
		} catch(Exception e) {}
		return null;
	}

	/**
	 * Get an universally unique identifier for the object to be stored into the database
	 * 
	 * @return an id
	 */
	public static String generateIdForObject() {
		return (UUID.randomUUID().toString());
	}
	
	/**
	 * Save objects generically
	 * 
	 * @return
	 */
	public static int saveObject(Object object, Class clazz) {
		
		int executed = 0;
		String sqlString = generateSqlString(object, clazz);
		
		if(sqlString != null && !sqlString.isEmpty()) {
			//commit to database
			Connection con = BackendDatabase.getConnection();
			if(con != null) {
				try {
					Statement stmt = con.createStatement();
					executed = stmt.executeUpdate(sqlString);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return executed;
	}

	/**
	 * @param object
	 * @param clazz
	 */
	private static String generateSqlString(Object object, Class clazz) {
		StringBuilder sqlString = new StringBuilder();
		StringBuilder fieldString = new StringBuilder("( ");
		StringBuilder valueString = new StringBuilder(" VALUES( ");
		List<Method> getters = null;
		String tableName = null;
		
		try {
			getters = Arrays.stream(clazz.getDeclaredMethods()).filter(m -> m.getName().contains("get")).collect(Collectors.toList());
		} catch(SecurityException e) {
		}
		
		if(getters != null && !getters.isEmpty()) {
			Method getTableNameMethod = (Method) getters.stream().filter(m ->  ((Method) m).getName().equalsIgnoreCase("getTableName")).findFirst().orElse(null);
			
			if(getTableNameMethod != null) {
				try {
					tableName = (String) getTableNameMethod.invoke(object, null);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
				
				sqlString.append("INSERT INTO " + tableName);
				
				for(Method method : getters) {
					if(!method.getName().equalsIgnoreCase("getTableName")) {
						fieldString.append(method.getName().replace("get", "" )+ " , ");
						
						Object value = null;
						try {
							value = method.invoke(object, null);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
						}
						
						if(Integer.TYPE.equals(method.getReturnType())){
							valueString.append((int)value + " , ");
						}else if(method.getReturnType().equals(String.class)){
							valueString.append((null != (String) value)?"'"+value+"'":null);
							valueString.append(" , ");
						}
					}
				}
				
				int indexOfForFields = fieldString.toString().lastIndexOf(", ");
				fieldString.deleteCharAt(indexOfForFields);
				fieldString.append(")");
				
				
				int indexOfForValues = valueString.toString().lastIndexOf(", ");
				valueString.deleteCharAt(indexOfForValues);
				valueString.append(")");
				
				sqlString.append(fieldString.toString());
				sqlString.append(valueString.toString());
			}
			
		}
		
		return sqlString.toString();
	}
}
