import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.racesaucy.read.adapter.ReadGarminGPXFile;
import com.racesaucy.read.adapter.ReadNME0183Data;
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
	
	Date defaultStartDate;
	Date defaultEndDate;
	

@Before
public void setUp() throws Exception {
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.YEAR, 2090);
	cal.set(Calendar.MONTH, Calendar.JANUARY);
	cal.set(Calendar.DAY_OF_MONTH, 1);
	defaultStartDate = cal.getTime();

	cal.set(Calendar.YEAR, 1990);
	defaultEndDate = cal.getTime();
}

	
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
	SessionPersist sessionPersistEntry = emService.findSessionPersistById(sessionPersistIds.get(0).getSessionPersistId());
	
//	if (sessionPersist.getGpsDataList() == null) {
//	    System.out.println("\n\nGpsDataList is null \n\n");
//	} else {
//		System.out.println("\n\n GpsDataList() size is: " + sessionPersist.getGpsDataList().size() + "\n\n");
//	}
    
	HibernateAwareObjectMapper mapper = new HibernateAwareObjectMapper();
	
	//ObjectMapper mapper = new ObjectMapper();
	//mapper.registerModule(new Hibernate4Module());
	try {
		mapper.writeValue(new File("c:/projects/user-modified.json"), sessionPersistEntry);
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
	
	    SessionPersist sessionPersist = emService.getNewSessionPersist(new Date(), "testGetNewSessionPersist");
	    
	    context.close();
	    
	    Assert.assertTrue(sessionPersist.getSessionPersistId() > 0);
	}

	@Ignore
	@Test
	public void testLoadJsonFileAndCreateSessionPersistAndStore() {
		
		ObjectMapper mapper = new ObjectMapper();
		SessionPersist sessionPersistFromFile = null;;
		try {
			sessionPersistFromFile = mapper.readValue(new File("c:/projects/READ/READPersistence/src/test/resources/GpsTestData.json"), SessionPersist.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    if (sessionPersistFromFile == null) {
	    	System.out.println("sessionPersistFromFile is null afer read json file");
		    Assert.assertTrue(false);
	    }
	    ReadGarminGPXFile readGarminGPXFile = new ReadGarminGPXFile();
	    readGarminGPXFile.saveGPSSessionObservations("New Test Session", new ArrayList(sessionPersistFromFile.getGpsDataList()));
	    Assert.assertTrue(sessionPersist.getSessionPersistId() > 0);
	}

	@Ignore
	@Test
	public void createJsonGPSDataFileTest() {
	
		// read and convert raw nmea file to gpsDataList
	    ReadNME0183Data readNME0183Data = new ReadNME0183Data();
		String fileName = "C:\\projects\\READ\\READPersistence\\src\\test\\resources\\GpsTestData.dat"; 
		List<GpsData> gpsDataList = readNME0183Data.loadInputfile(fileName);

		// gps data id normally populated with db 
		int i = 0;
	    for (GpsData gpsData: gpsDataList) {
	    	gpsData.setGpsDataId(i++);
	    }

	    SessionPersist sessionPersistEntry = new SessionPersist(); 
	    sessionPersistEntry.setSessionPersistId(99);
	    sessionPersistEntry.setGpsDataList(new HashSet<>(gpsDataList));
	    sessionPersistEntry.setEndDateTime(gpsDataList.get(gpsDataList.size()-1).getDateTime());
	    sessionPersistEntry.setStartDateTime(gpsDataList.get(0).getDateTime());
	    
	    HibernateAwareObjectMapper mapper = new HibernateAwareObjectMapper();
		try {
			mapper.writeValue(new File("c:/projects/READ/READPersistence/src/test/resources/GpsTestData.json"), sessionPersistEntry);
			
	        String content = new String(Files.readAllBytes(Paths.get("c:/projects/READ/READPersistence/src/test/resources/GpsTestData.json")));
	        
	        System.out.println("Json String: " + content);

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
	    
	    Assert.assertTrue(true); //sessionPersistEntry.getSessionPersistId() > 0);
	}


	
	@Test
	public void readJsonGPSDataFileAndPersistTest() {
		SessionPersist sessionPersistFromFile = readJsonFileAndReturnSessionPersist();
		
	    ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	    GpsDataService emService = (GpsDataService) context.getBean("gpsDataService");
	    SessionPersist newSessionPersist = emService.getNewSessionPersist(new Date(), "readJsonGPSDataFileAndPersistTest");

	    // need to reset gpsDataId to store new records
	    for (GpsData gpsData: sessionPersistFromFile.getGpsDataList()) {
	    	gpsData.setGpsDataId(0);
	    	gpsData.setSessionPersist(newSessionPersist);
	    }
    	List<GpsData> list = new ArrayList<GpsData>(sessionPersistFromFile.getGpsDataList());

		ReadGarminGPXFile readGarminGPXFile = new ReadGarminGPXFile();
        readGarminGPXFile.saveGPSSessionObservationsList(newSessionPersist,
        		newSessionPersist.getSessionName(), list);

        context.close();
		
	    Assert.assertTrue(newSessionPersist != null && sessionPersistFromFile.getGpsDataList().size() > 0);
	}
	
	SessionPersist readJsonFileAndReturnSessionPersist() {
		SessionPersist sessionPersistEntry = null;
		try {
			
			ObjectMapper mapper = new ObjectMapper();

		    sessionPersistEntry = mapper.readValue(new File("c:/projects/READ/READPersistence/src/test/resources/GpsTestData.json"), SessionPersist.class);
	       
//		    String content = new String(Files.readAllBytes(Paths.get("c:/projects/READ/READPersistence/src/test/resources/GpsTestData.json")));
//	        System.out.println("Json String: " + content);

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
		
		return sessionPersistEntry;

	}

}
