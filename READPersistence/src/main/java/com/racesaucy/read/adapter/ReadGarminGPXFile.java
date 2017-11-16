package com.racesaucy.read.adapter;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.racesaucy.read.domain.GpsData;
import com.racesaucy.read.domain.SessionPersist;
import com.racesaucy.read.service.GpsDataService;

// This adapter program reads the Garmin saved GPX xml file, 
// and stores data in the GPS_DATA and SESSION_PERSIST tables. 
public class ReadGarminGPXFile {
	
	static ConfigurableApplicationContext context = null; 
	static GpsDataService emService = null; 

	//@SuppressWarnings("deprecation")
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		Date startTime = new Date(System.currentTimeMillis());
		System.out.println("Start time: " + startTime.toLocaleString());
		
		ReadGarminGPXFile readGarminGPXFile = new ReadGarminGPXFile();

		//String fileName = "/home/ec2-user/runReadPersistence/SaucyGpsTracks2.gpx";
		//String fileName = "/home/pi/temp/SaucyGpsTracks2.gpx";
		String fileName = "C:/projects/READPersistence/SaucyGpsTracks2.gpx";

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		DateFormat dateFormatMDY = new SimpleDateFormat("yyyy-MM-dd");

		try {

			File file = new File(fileName);
			JAXBContext jaxbContext = JAXBContext.newInstance(TrkptList.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			TrkptList trkptList = (TrkptList) jaxbUnmarshaller.unmarshal( file );

			Date currentDate = null;
		    List<GpsData> currentGpsPtList = null;

	        ZoneId defaultZoneId = ZoneId.systemDefault();
			Calendar cal = Calendar.getInstance();
			
			GpsData prevGpsDataObservation = null;
			for(Trkpt trkpt : trkptList.getTrkptList())
			{
				try {
					// Convert the date in UTC to local time zone. TODO: revisit and not just subtract 5 hours 
					LocalDateTime localDateTime = df.parse(trkpt.getTime()).toInstant().atZone(defaultZoneId).toLocalDateTime();
					//localDateTime = localDateTime.minusHours(5);
					cal.set(localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth(), 
						localDateTime.getHour(), localDateTime.getMinute(),localDateTime.getSecond());
					trkpt.setTrkPtDate(cal.getTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				GpsData currentGpsDataObservation = readGarminGPXFile.convertTrkPtToGPSData(trkpt);
				
				if (currentDate == null) { 
					currentDate = trkpt.getTrkPtDate();
					prevGpsDataObservation = new GpsData(currentGpsDataObservation); //readGarminGPXFile.convertTrkPtToGPSData(trkpt);
				    currentGpsPtList = new ArrayList<GpsData>();
					currentGpsPtList.add(currentGpsDataObservation);
				} else {
					// subtract 5 hours here because other time frame spans two days and I want to save session by date
					if (dateFormatMDY.format(subtract5Hours(currentDate)).equals(dateFormatMDY.format(subtract5Hours(trkpt.getTrkPtDate())))) {
						calcDistanceSpeedAndHeading(prevGpsDataObservation,currentGpsDataObservation);
						currentGpsPtList.add(currentGpsDataObservation);
						prevGpsDataObservation = new GpsData(currentGpsDataObservation);
					} else {
						// New Date, create a session and save track points
						DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						String formattedCurrentDate = df2.format(subtract5Hours(currentDate));
						readGarminGPXFile.saveGPSSessionObservations("Garmin Track for: " + 
								formattedCurrentDate, currentDate, prevGpsDataObservation.getDateTime(),currentGpsPtList);

						currentDate = null; trkpt.getTrkPtDate();
//						currentGpsPtList.add(currentGpsDataObservation);
//						prevGpsDataObservation = new GpsData(currentGpsDataObservation);
					}
				}
				//System.out.println("currentGpsDataObservation: " + currentGpsDataObservation.toString());
			}

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		Date endTime = new Date(System.currentTimeMillis());
		System.out.println("End time: " + endTime.toLocaleString());

		System.out.println("Run time: " + (endTime.getTime() - startTime.getTime())*1000.0);
	}
	
	static Date subtract5Hours(Date date) {
		Date newDate = new Date(date.getTime() - 5 * 60 * 60 * 1000);
		
		return newDate;
	}


	static void calcDistanceSpeedAndHeading(GpsData fromGpsPoint, GpsData toGpsPoint) {
		float distanceInMeteres = (float) LatLonUtilities.calcDistance(
				fromGpsPoint.getLatitude(),
				fromGpsPoint.getLongitude(),
				toGpsPoint.getLatitude(),
				toGpsPoint.getLongitude());
		
		
		float bearingInDegrees = (float) LatLonUtilities.calcBearing(
				fromGpsPoint.getLatitude(),
				fromGpsPoint.getLongitude(),
				toGpsPoint.getLatitude(),
				toGpsPoint.getLongitude());
		
		double metersPerNauticalMile = 1.000000364 / 0.000539957;
		
		double distanceInNauticalMile = distanceInMeteres / metersPerNauticalMile;

		double timeInHours = (float) ((toGpsPoint.getDateTime().getTime() - fromGpsPoint.getDateTime().getTime()) / 1000.0 / 60.0 / 60.0);
		
		// Note: 6 knots = 3.08667 meters per second
		double speed;
		if (timeInHours == 0.0) {
			speed = 0;
		} else {
			speed = distanceInNauticalMile / timeInHours;
		}
		
		speed = LatLonUtilities.round(speed,  10.0);
		
		toGpsPoint.setLegTimeSecs((float) timeInHours);
		toGpsPoint.setLegSpeed((float) speed);
		toGpsPoint.setLegLength(distanceInMeteres);
		toGpsPoint.setLegHeading(bearingInDegrees);
		
//		System.out.println("In calcDistanceSpeedAndHeading"
//				+ "\tFrom Point A: " + fromGpsPoint.getLatitude() + " " + fromGpsPoint.getLongitude()
//				+ "\tTo Point B: " + toGpsPoint.getLatitude() + " " + toGpsPoint.getLongitude()
//				+ "\tDistinance in nautical miles: " + distanceInNauticalMile + " meters"
//				+ "\tTime: " + timeInHours + " hours" 
//				+ "\tSpeed: " + speed + " knots"
//				+ "\tBearing: " + bearingInDegrees + " degress");
	}

	void saveGPSSessionObservations(String sessionName, Date currentDate,Date endDate, List<GpsData> currentGpsPtList) {

		if (context == null) {
			context = new ClassPathXmlApplicationContext("applicationContext.xml");
			emService = (GpsDataService) context.getBean("gpsDataService"); //new GpsDataServiceImpl(); //
		}

		SessionPersist sessionPersist = (SessionPersist) context.getBean("sessionPersist"); 
		sessionPersist.setDateTime(currentDate) ;
		sessionPersist.setEndDateTime(endDate);
		
//		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		Date currentDateAdjusted = subtract5Hours(currentDate); 
//		String formattedCurrentDate = df2.format(currentDateAdjusted);
		
		sessionPersist.setSessionName(sessionName);
		sessionPersist.setSessionDescription(sessionName);

	    GpsData gpsData = null; 
	    List<GpsData> gpsDataList = new ArrayList<GpsData>();
	    
	    for (int i=0; i < currentGpsPtList.size(); i++)
		{
	    	GpsData currentGpsObservation = currentGpsPtList.get(i);
	    	Date observationDateTime = currentGpsObservation.getDateTime();
	    	if (i == 0) {
	    		sessionPersist.setDateTime(observationDateTime); 
	    		sessionPersist.setStartDateTime(observationDateTime);
	    	}
	    	
	    	if (i == currentGpsPtList.size()-1) {
	    		sessionPersist.setEndDateTime(observationDateTime);
	    	}
	    	
			gpsData = (GpsData) context.getBean("gpsData"); //new GpsData();
			gpsData.copyValues(currentGpsObservation);
	
			//System.out.println("ReadGarminGPXFile gpsData:" + gpsData.toString());
			gpsDataList.add(gpsData);
		}
		
        emService.persistSessionPersist(sessionPersist,gpsDataList);
		
	};
	
	GpsData convertTrkPtToGPSData(Trkpt trkpt) {
	
		GpsData gpsData = new GpsData();
		gpsData.setDateTime(trkpt.getTrkPtDate());
		
		gpsData.setLatitude(new Float(trkpt.getLat()));
		gpsData.setLatitudeDirection("N");
		
		gpsData.setLongitude(new Float(trkpt.getLon()));
		gpsData.setLongitudeDirection("W");

		gpsData.setAltitude(new Float(trkpt.getEle()));
		gpsData.setLegHeading((float) 0.0);
		gpsData.setLegSpeed((float) 0.0);
		gpsData.setLegLength((float) 0);
		gpsData.setLegTimeSecs(new Float(0));
		
		return gpsData;
	}

}
