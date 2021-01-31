package com.birth.rest;

import static org.junit.jupiter.api.Assertions.*;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

class PersonServiceTest {

	@Test
	void testGetPersons() {
		PersonService pService = new PersonService();
		Response response = pService.getPersons();
		assertNotNull(response);
		assertTrue(response.getStatus() == 200);
	}

}
