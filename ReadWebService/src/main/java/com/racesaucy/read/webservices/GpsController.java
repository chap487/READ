package com.racesaucy.read.webservices;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.racesaucy.read.domain.GpsData;
import com.racesaucy.read.domain.SessionPersist;
import com.racesaucy.read.service.GpsDataService;

@Controller
public class GpsController {

	@Autowired
	private GpsDataService gpsDataService;

	@Autowired
	private View jsonView_i;

	private static final String DATA_FIELD = "data";
	private static final String ERROR_FIELD = "error";

	private static final Logger logger_c = Logger.getLogger(GpsController.class);

	@RequestMapping(value = "/rest/GpsData/{sessionPersistId}", method = RequestMethod.GET)
	public ModelAndView getGpsData(@PathVariable("sessionPersistId") Integer sessionPersistId_p) {
		List<GpsData> gpsData = null;

		/* validate fund Id parameter */
		if (sessionPersistId_p == null) {
			String sMessage = "Error invoking getGpsData - Invalid fund Id parameter";
			return createErrorResponse(sMessage);
		}

		try {
			gpsData = gpsDataService.findGpsDataBySessionPersistId(sessionPersistId_p);
		} catch (Exception e) {
			String sMessage = "Error invoking getGpsData. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		logger_c.debug("Returing GpsData: " + gpsData.toString());
		ModelAndView mav = new ModelAndView(jsonView_i, DATA_FIELD, gpsData);
		return mav;
	}

	/**
	 * Gets all funds.
	 *
	 * @return the funds
	 */
	@RequestMapping(value = "/rest/SessionPersist/", method = RequestMethod.GET)
	public ModelAndView getSessionPersistList(
	        @RequestParam(value = "startDate", defaultValue = "-1", required=false) String startDate,
	        @RequestParam(value = "sessionName", defaultValue = "xxx", required=false) String sessionName
	 ) {
		List<SessionPersist> sessionPersistList = null;
		try {
			
			if ( !startDate.equals("-1") ) {
				System.out.println("Creating new SessionPersist for " + startDate + " and " + sessionName);
			    SimpleDateFormat dateformat3 = new SimpleDateFormat("ddMMyyyy");
			    Date date = dateformat3.parse(startDate);
			    SessionPersist newSessionPersist = gpsDataService.getNewSessionPersist(date, sessionName);
				sessionPersistList = new ArrayList<SessionPersist>();
				sessionPersistList.add(newSessionPersist);
			} else {
				sessionPersistList = gpsDataService.findAllSessionPersist();
			}
		} catch (Exception e) {
			String sMessage = "Error getting all SessionPersist. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}
		logger_c.debug("Returing sessionPersistList. Size = " + sessionPersistList.size());
		return new ModelAndView(jsonView_i, DATA_FIELD, sessionPersistList);
	}
	
	@RequestMapping(value = "/rest/SessionPersist/{sessionPersistId}", method = RequestMethod.GET)
	public ModelAndView getSessionPersist(@PathVariable("sessionPersistId") Integer sessionPersistId_p) {
		SessionPersist sessionPersist = null;

		/* validate fund Id parameter */
		if (sessionPersistId_p == null) {
			String sMessage = "Error invoking getGpsData - Invalid fund Id parameter";
			return createErrorResponse(sMessage);
		}

		try {
			sessionPersist = gpsDataService.findSessionPersistById(sessionPersistId_p);
		} catch (Exception e) {
			String sMessage = "Error invoking getGpsData. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		logger_c.debug("Returing sessionPersist");
		ModelAndView mav = new ModelAndView(jsonView_i, DATA_FIELD, sessionPersist);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File("c:/projects/user-modified.json"), sessionPersist);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mav;
	}

	
	private ModelAndView createErrorResponse(String sMessage) {
		return new ModelAndView(jsonView_i, ERROR_FIELD, sMessage);
	}


}

