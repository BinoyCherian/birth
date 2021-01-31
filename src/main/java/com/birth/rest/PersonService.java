package com.birth.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.birth.bean.PersonBean;
import com.birth.constants.ServiceMessages;
import com.birth.constants.StringConstants;
import com.birth.helper.ParentHelper;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Path("/hello")
public class PersonService {
	
    @GET
    @Path("/")
    public Response getMsg(@PathParam("param") String msg) {
        return Response.status(200).entity(ServiceMessages.SERVICE_UP).build();
    }
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/persons")
    public Response getPersons() {
    	ObjectMapper objectMapper = new ObjectMapper();
    	objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.PUBLIC_ONLY);
    	objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    	String output = StringConstants.EMPTY_STRING;
    	List persons = (new PersonBean()).read(null);
    	try {
			output = objectMapper.writeValueAsString(persons);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

    	return Response.status(200).entity(output).build();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPerson(PersonBean person) {
    	
    	int status = 409;
    	
    	if(person != null) {
    		person.setId(ParentHelper.generateIdForObject());
    		
    		//save to database 
    		if(person.save()>0) {
    			//return 200 if save was ok with some basic infos to be displayed on the page (name etc)
    			status = 200;
    		}
    	}
		return Response.status(status).build();
    	
    }
}
