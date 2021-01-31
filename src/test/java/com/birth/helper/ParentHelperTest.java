package com.birth.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.birth.back.impl.BackendDatabase;
import com.birth.bean.PersonBean;

class ParentHelperTest {

	@Test
	void testLoadRecords() {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select * from Person limit 1");
		try {
			Statement pStatement = BackendDatabase.getConnection().createStatement();
			ResultSet rs = pStatement.executeQuery(sql.toString());
			List records = ParentHelper.loadRecords(rs, PersonBean.class);
			assertNotNull(records);
			assertTrue(records.size() == 1);
		}catch(Exception e) {}
	}
	
	@Test
	void testUniveralId() {
		String universalId = ParentHelper.generateIdForObject();
		assertNotNull(universalId);
	}

}
