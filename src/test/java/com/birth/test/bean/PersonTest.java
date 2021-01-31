/**
 * 
 */
package com.birth.test.bean;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.birth.bean.PersonBean;
import com.birth.helper.ParentHelper;

/**
 * @author Bee
 *
 */
class PersonTest {

	/**
	 * Test method for {@link com.birth.bean.ParentBean#read(java.lang.String)}.
	 */
	@Test
	void testRead() {
		PersonBean person = new PersonBean();
		List persons = person.read(null);
		assertNotNull((persons));
		assertTrue(persons.size()>=1);
	}
	
	@Test
	void testSave() {
		PersonBean person = new PersonBean();
		person.setId(ParentHelper.generateIdForObject());
		person.setFirstName("Manu");
		person.setLastName("Cherian");
		person.setAge(30);
		person.setEmail("cherianbinoy@live.com");
		
		person.save();
	}

}
