/**
 * 
 */
package com.birth.test.databse;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.birth.back.impl.BackendDatabase;

/**
 * @author Bee
 *
 */
class DataBaseTest {

	/**
	 * Test method for {@link com.birth.back.impl.BackendDatabase#getConnection()}.
	 */
	@Test
	void testGetConnection() {
		Connection con = BackendDatabase.getConnection();
		assertNotNull(con);
	}
	
	@Test
	void testfetchConnectionInfos() {
		Properties prop = BackendDatabase.fetchConnectionProperties();
		assertNotNull(prop);
		assertNotNull(prop.get("DATABASE_URL"));
		assertNotNull(prop.get("DATABASE_NAME"));
		assertNotNull(prop.get("DATABASE_USER"));
		assertNotNull(prop.get("DATABASE_PASSWORD"));
	}
}
