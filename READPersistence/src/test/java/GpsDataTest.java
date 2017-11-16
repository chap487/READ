import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.racesaucy.read.domain.GpsData;
import com.racesaucy.read.domain.SessionPersist;
import com.racesaucy.read.service.GpsDataService;
import com.racesaucy.read.web.utils.HibernateAwareObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")

public class GpsDataTest {
	

	@Autowired 
	GpsData gpsData;
	@Autowired 
	GpsData gpsData2;
	
	@Autowired 
	SessionPersist sessionPersist;
	
	@Autowired
	GpsDataService emService;

	public static Integer persistSessionId = -1;
	
@Ignore	
@Test
public void testCreateData() {
		
//		try {
//			HibernateUtil.init();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	
    System.out.println("load context testCreateData");
    //ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    //GpsDataService emService = (GpsDataService) context.getBean("gpsDataService"); //new GpsDataServiceImpl(); //

//	SessionPersist sessionPersist = (SessionPersist) context.getBean("sessionPersist"); //new GpsData();
    sessionPersist.setDateTime(new Date()) ;
    sessionPersist.setStartDateTime(new Date());
    sessionPersist.setEndDateTime(new Date());
    sessionPersist.setSessionName("test 1");
    sessionPersist.setSessionDescription("test Session Description");

//    GpsData gpsData = (GpsData) context.getBean("gpsData"); //new GpsData();
    gpsData.setAltitude((float) 10.0);
	gpsData.setLegHeading((float) 350.0);
	gpsData.setLatitude((float) 41.100);
	gpsData.setLatitudeDirection("N");
	gpsData.setLongitude((float) 87.100);
	gpsData.setLongitudeDirection("W");
	gpsData.setDateTime(new Date());
	
//	GpsData gpsData2 = (GpsData) context.getBean("gpsData"); //new GpsData();
    gpsData2.setAltitude((float) 11.0);
	gpsData2.setLegHeading((float) 355.0);
	gpsData2.setLatitude((float) 41.000);
	gpsData2.setLatitudeDirection("N");
	gpsData2.setLongitude((float) 87.000);
	gpsData2.setLongitudeDirection("W");
	gpsData2.setDateTime(new Date());

	List<GpsData> gpsDataList = new ArrayList<GpsData>();
	gpsDataList.add(gpsData);
	gpsDataList.add(gpsData2);
	
    emService.persistSessionPersist(sessionPersist,gpsDataList);

	if (sessionPersist.getSessionPersistId() > 0) {
		persistSessionId = sessionPersist.getSessionPersistId();
	}
    
	System.out.println("\n\nAdded:\n" + sessionPersist.toString() + "\n\n");
	
//	got a class cast exception below
//	HashSet<GpsData> HashgpsDataList =   (HashSet<GpsData>) sessionPersist.getGpsDataList();
//	if (HashgpsDataList == null) {
//		System.out.println("\n\nJust added and gpsDataList still null \n\n");
//	}

  //  context.close();
    
	//Assert.assertNotEquals(gpsData.getGpsDataId(),0);
    Assert.assertTrue(sessionPersist.getSessionPersistId() > 0 && gpsData.getGpsDataId() > 0);
}
	
@Ignore
@Test
public void testDeleteData() {
	
    System.out.println("load context testDeleteData");
    ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    GpsDataService emService = (GpsDataService) context.getBean("gpsDataService");
    
	SessionPersist sessionPersist = emService.findSessionPersistById(persistSessionId);
	
    System.out.println("\n\nDeleting: \n" + emService.toStringSessionPersistById(persistSessionId) + "\n\n");

//	if (sessionPersist.getGpsDataList() == null) {
//	    System.out.println("\n\nGpsDataList is null \n\n");
//	} else {
//		System.out.println("\n\n GpsDataList() size is: " + sessionPersist.getGpsDataList().size() + "\n\n");
//	}
//    
	
	ObjectMapper mapper = new ObjectMapper();
	mapper.registerModule(new Hibernate4Module());
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


	int result = 0;
	try {
		emService.deleteSessionPersist(sessionPersist);	
	} catch (Exception e) {
		result = -1;
		System.out.println("Exception caught in testDeleteData: " + e.getMessage());
	}
	
    context.close();
    
	//Assert.assertNotEquals(gpsData.getGpsDataId(),0);
    Assert.assertTrue(result == 0);
}


@Test
public void testAllSessionPersist() {
	
    System.out.println("load context testAllSessionPersist");
    ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    GpsDataService emService = (GpsDataService) context.getBean("gpsDataService");
    
    System.out.println("\n\nGetting all sessionPersist\n\n");

	List<SessionPersist> sessionPersistIds = emService.findAllSessionPersist();
	List<SessionPersist> sessionPersist = emService.findAllSessionPersistWithGpsData();
	
//	if (sessionPersist.getGpsDataList() == null) {
//	    System.out.println("\n\nGpsDataList is null \n\n");
//	} else {
//		System.out.println("\n\n GpsDataList() size is: " + sessionPersist.getGpsDataList().size() + "\n\n");
//	}
    
	HibernateAwareObjectMapper mapper = new HibernateAwareObjectMapper();
	
	//ObjectMapper mapper = new ObjectMapper();
	//mapper.registerModule(new Hibernate4Module());
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


	int result = 0;
	
    context.close();
    
	//Assert.assertNotEquals(gpsData.getGpsDataId(),0);
    Assert.assertTrue(result == 0);
}


@Test
public void testGetGpsData() {
	
    System.out.println("load context testGetGpsData");
    ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    GpsDataService emService = (GpsDataService) context.getBean("gpsDataService");
    
    List<SessionPersist> sessionPersist = emService.findAllSessionPersistWithGpsData();
    int sessionPersistId = sessionPersist.get(0).getSessionPersistId();
    
    //Set<GpsData> gpsDataSet = sessionPersist.get(0).getGpsDataList();

	int result = 0;
	try {
		List<GpsData> gpsDataList = emService.findGpsDataBySessionPersistId(sessionPersistId);
		result = gpsDataList.size();
	    System.out.println("\n\n\tTotal records returned: " + gpsDataList.size() + "\n\n");
	} catch (Exception e) {
		result = -1;
		System.out.println("Exception caught in testDeleteData: " + e.getMessage());
	}
	
    context.close();
    
	//Assert.assertNotEquals(gpsData.getGpsDataId(),0);
    Assert.assertTrue(result > 0);
}

	@Test
	public void testGetNewSessionPersist() {
	    System.out.println("load context testGetGpsData");
	    ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	    GpsDataService emService = (GpsDataService) context.getBean("gpsDataService");
	
	    SessionPersist sessionPersist = emService.getNewSessionPersist(new Date(), "New Session Test");
	    
	    context.close();
	    
		//Assert.assertNotEquals(gpsData.getGpsDataId(),0);
	    Assert.assertTrue(sessionPersist.getSessionPersistId() > 0);

	}


//	@Test
//	public void testGetNewSessionPersist() {
//	
//		sessionPersist.getNewSessionPersist(new Date(), "New Session Test");
//	}


}
