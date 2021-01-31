package com.birth.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.birth.back.impl.BackendDatabase;
import com.birth.constants.StringConstants;
import com.birth.helper.ParentHelper;

/**
 * All beans extend this method
 * 
 * @author Bee
 *
 */
public class ParentBean {
	
	protected String type;
	protected Connection con;
	protected Class clazz;
	
	private static final String SELECT_QUERY = "select * from";
	
	public ParentBean(String type, Class clazz) {
		this.type = type;
		this.clazz = clazz;
	}
	
	public void create() {
		
	}
	
	public List read(String conditions) {
		
		List records = new ArrayList();
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT_QUERY + StringConstants.SPACE + type );
		
		try {
			con = BackendDatabase.getConnection();
			Statement pStatement = con.createStatement();
			ResultSet rs = pStatement.executeQuery(sql.toString());
			if(rs != null) {
				records = ParentHelper.loadRecords(rs, clazz);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return records;
	}
	
	public int save() {
		return ParentHelper.saveObject(this, this.getClass());
		
	}
	
	public void update() {
		
	}
	
	public void delete() {
		
	}

}
