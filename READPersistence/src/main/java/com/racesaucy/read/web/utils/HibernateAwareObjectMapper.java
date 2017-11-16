package com.racesaucy.read.web.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

public class HibernateAwareObjectMapper extends ObjectMapper {

    /**
	 * 
	 */
	private static final long serialVersionUID = 661770901846162000L;

	public HibernateAwareObjectMapper() {
        setSerializationInclusion(JsonInclude.Include.NON_NULL); 
        registerModule(new Hibernate4Module());
    }
}