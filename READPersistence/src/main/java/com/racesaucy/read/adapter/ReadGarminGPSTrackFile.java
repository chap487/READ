package com.racesaucy.read.adapter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.racesaucy.read.domain.GpsData;
import com.racesaucy.read.domain.SessionPersist;
import com.racesaucy.read.service.GpsDataService;

// This adapter program reads the Garmin saved gps track file, 
// and stores data in the GPS_DATA and SESSION_PERSIST tables. 
public class ReadGarminGPSTrackFile {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
        String fileName = "C:/projects/READPersistence/2004-nood.txt";
        Path path = Paths.get(fileName);
        Scanner scanner = null;
        
        //System.out.println("load context testCreateData");
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        GpsDataService emService = (GpsDataService) context.getBean("gpsDataService"); //new GpsDataServiceImpl(); //
        
        // TODO: fix deprecated issues below 
    	SessionPersist sessionPersist = (SessionPersist) context.getBean("sessionPersist"); //new GpsData();
        sessionPersist.setDateTime(new Date()) ;
        sessionPersist.setSessionName("Garmin Track Data - " + sessionPersist.getDateTime().toLocaleString());
        sessionPersist.setSessionDescription("test Session Description");
        sessionPersist.setStartDateTime(new Date(2200,1,1));
        sessionPersist.setEndDateTime(new Date(2100,1,1));

        
		Pattern p = Pattern.compile("(Trackpoint)\t((N|S)(\\d\\d) (\\d\\d\\.\\d\\d\\d)) ((W|E)(\\d\\d) (\\d\\d\\.\\d\\d\\d))"
				+ "\t(\\d?\\d/\\d\\d/\\d\\d\\d\\d \\d?\\d\\:\\d\\d:\\d\\d (AM|PM))\t(\\d?\\d?\\d\\d\\d) ft\t\t(\\d?\\d?\\d?\\d?\\d) ft"
				+ "\t(\\d\\d\\:\\d\\d:\\d\\d)\t(\\d?\\d?\\d\\.\\d\\d?) kt\t(\\d?\\d?\\d)");
		
	    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");

	    GpsData gpsData = null; 
	    List<GpsData> gpsDataList = new ArrayList<GpsData>();
	    
	    
		try {
			scanner = new Scanner(path);
	        while(scanner.hasNext()){
	            //parse line to get Emp Object
	    		Matcher m = p.matcher(scanner.nextLine());
	    		while (m.find()) {
	    			System.out.println("GPS Observation:" + 
    					"\n\tLat: " + m.group(2) + 
    					//" N/S " + m.group(3) + 
    					//" DEG " + m.group(4) + 
    					//" MIN " + m.group(5) + 
    					"\n\tLong: " + m.group(6) + 
    					//" E/W " + m.group(7) + 
    					//" DEG " + m.group(8) + 
    					//" MIN" + m.group(9) + 
    					"\n\tDate/Time: " + m.group(10) + 
    					//" " + m.group(11) + 
    					"\n\tAltitude: " + m.group(12) + 
    					"\n\tLeg Length: " + m.group(13) + 
    					"\n\tLeg Time: " + m.group(14) +
    					"\n\tLeg Speed: " + m.group(15) +
    					"\n\tLeg Course: " + m.group(16)
   	    			);
	    			
	    			Date t = df.parse(m.group(10));

	    			if (sessionPersist.getStartDateTime().before(t)) {
	    				sessionPersist.setStartDateTime(t);
	    			}
  			        
	    			if (sessionPersist.getEndDateTime().after(t)) {
	    				sessionPersist.setEndDateTime(t);
	    			}
	    			
	    		    gpsData = (GpsData) context.getBean("gpsData"); //new GpsData();
	    		    
	    		    gpsData.setDateTime(t);
	    		    gpsData.setLatitude(new Float(new Integer(m.group(4)) + new Float(m.group(5)) / 60.0));
	    		    gpsData.setLatitudeDirection(m.group(3));
	    		    gpsData.setLongitude(new Float(new Integer(m.group(8)) + new Float(m.group(9)) / 60.0));
	    		    gpsData.setLongitudeDirection(m.group(7));
	    		    
	    		    gpsData.setAltitude(new Float(m.group(12)));
	    		    gpsData.setLegHeading(new Float(m.group(16)));
	    		    gpsData.setLegSpeed(new Float(m.group(15)));
	    		    gpsData.setLegLength(new Float(m.group(13)));
	    		    String[] timeParts = m.group(14).split(":");
	    		    int time = new Integer(timeParts[0]) * 3600 + new Integer(timeParts[1]) * 60 + new Integer(timeParts[2]);
	    		    gpsData.setLegTimeSecs(new Float(time));
	    		    
	    			System.out.println("ReadGarminGPSTrackFile - gpsData:" + gpsData.toString());
	    			gpsDataList.add(gpsData);
	    		}    
	        }
	        
	        emService.persistSessionPersist(sessionPersist,gpsDataList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    context.close();
		}
         
        scanner.close();
 

	}

}
