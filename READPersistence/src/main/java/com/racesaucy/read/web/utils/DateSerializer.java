package com.racesaucy.read.web.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * The Class DateSerializer.
 */
public class DateSerializer extends JsonSerializer<Date> {

    /* (non-Javadoc)
     * @see org.codehaus.jackson.map.JsonSerializer#serialize(java.lang.Object, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
     */
    @Override
    public void serialize(Date value_p, JsonGenerator gen, SerializerProvider prov_p)
        throws IOException, JsonProcessingException
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formattedDate = formatter.format(value_p);
        gen.writeString(formattedDate);
    }
    
    // copy  C:\Users\edmund\.m2\com\racesaucy\read\read-persist\1.0\read-persist-1.0.jar  C:\Java\apache-tomcat-8.0.18\webapps\read-webservice\WEB-INF\lib
}