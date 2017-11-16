package com.racesaucy.read.adapter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.racesaucy.read.domain.GpsData;

public class ReadNME0183Data extends ReadGarminGPXFile{

	//String gpsLocalDateString = null; // Date from GPZDA record. Needed because GPS observations only have time of day
	ZonedDateTime gpsZonedDateTime = null;
	static int gpsCurrentHour = -99;
	String gpsSessionName = null;
	
    LocalDateTime gpsLocalDate = null;
    LocalDateTime kplexDateTime;
    
    int MinTimeBetweenObservations = 60*1000; 

	public static void main(String[] args) {
		ReadNME0183Data readNME0183Data = new ReadNME0183Data();

		String fileName = "C:/projects/READPersistence/src/test/resources/October122017.dat"; //June_07_2017.dat"; //GpsMay282017.txt";
		//String fileName = "C:/projects/READPersistence/nmea018Data.txt";

		readNME0183Data.loadInputfile(fileName);
	}

	public void loadInputfile(String fileName) {
		try {
	        Path path = Paths.get(fileName);
			Pattern p = Pattern.compile("(.c:)(\\d*)(\\*...\\$)(.*$)");
			Scanner scanner = new Scanner(path);

			//boolean foundDateRecord = false;
		    List<GpsData> currentGpsPtList = new ArrayList<GpsData>();
		    GpsData currentGpsDataObservation = null;
		    
			GpsData fromGpsDataObservation = null;
			while(scanner.hasNext()){
	    		Matcher m = p.matcher(scanner.nextLine());
	    		while (m.find()) {
	    			//System.out.println("GPS Observation:" + "\n\tTime: " + m.group(2) + "\tData: " + m.group(4));

	    			// TODO: check if record has timestamp from Kplex. If not consider using system date-time
	    			Long timestamp = Long.valueOf(m.group(2).trim());
	    			kplexDateTime = Instant.ofEpochMilli(timestamp).
	    					atZone(ZoneId.systemDefault()).toLocalDateTime();
	    			
	    			String[] nme183InputRecord = m.group(4).trim().split(",");
	    			String recordType = nme183InputRecord[0];

	    			if (recordType.equalsIgnoreCase("GPZDA") && gpsZonedDateTime == null) {
	    				// get the date from the first GPZDA record
	    				gpsZonedDateTime = processGZDARecord(nme183InputRecord);
	    				
    				}

    				if (recordType.equalsIgnoreCase("GPGSV")) {
    					// ignore this record type for now
    					//processGPGSVRecord(inputRecordValues);
    					continue;
    				}

    				//GpsData currentGpsDataObservation = null;
    				if (recordType.equalsIgnoreCase("GPGGA") && gpsZonedDateTime != null) {
    					currentGpsDataObservation = processGPGGARecord(nme183InputRecord, currentGpsPtList);
	    				//System.out.println("GPS DATA POINT: Lat: " + latitude + " Lon: " + longitude + " Alt:" + altitude + " Time: " + localDateTime);
	    				
	    				if (fromGpsDataObservation == null) {
	    					// first point do not calc speed and heading.
	    					fromGpsDataObservation = new GpsData(currentGpsDataObservation);
    		    			currentGpsPtList.add(currentGpsDataObservation);
	    				} else {
	    					if (currentGpsDataObservation.getDateTime().getTime() - fromGpsDataObservation.getDateTime().getTime() >= MinTimeBetweenObservations) {
	    						calcDistanceSpeedAndHeading(fromGpsDataObservation, currentGpsDataObservation);
	    						fromGpsDataObservation = new GpsData(currentGpsDataObservation);
	
	    						System.out.println(currentGpsDataObservation.toString());
	    		    			currentGpsPtList.add(currentGpsDataObservation);
	    					}
	    				}
    				}
	    		} // while m.find
	        } //while scanner.hasNext()
			scanner.close();
			//LocalDateTime here = there.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();

			//LocalDateTime here = there.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
			
			saveGPSSessionObservations("GPS Track Data for: " + 
					gpsSessionName, convertLocalDateTimeToDate(gpsZonedDateTime),currentGpsDataObservation.getDateTime(),currentGpsPtList);

			
			// save or print session persist for now
			System.out.println("GPS Track for: " + gpsSessionName);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}

//	if (gpsLocalDate != null) {
//	Calendar cal = Calendar.getInstance();
//	cal.set(gpsLocalDate.getYear(), gpsLocalDate.getMonthValue(), gpsLocalDate.getDayOfMonth(), 
//			gpsLocalDate.getHour(), gpsLocalDate.getMinute(),gpsLocalDate.getSecond());
//	Date currentDate = cal.getTime();
//	DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//	String formattedCurrentDate = df2.format(subtract5Hours(currentDate));
//	saveGPSSessionObservations("GPS Track Data for: " + 
//			formattedCurrentDate, subtract5Hours(currentDate), currentGpsPtList);
//}

	GpsData processGPGGARecord(String[] inputRecordValues, List<GpsData> currentGpsPtList) {
		//	     GGA          Global Positioning System Fix Data
		//	     123519       Fix taken at 12:35:19 UTC
		//	     4807.038,N   Latitude 48 deg 07.038' N
		//	     01131.000,E  Longitude 11 deg 31.000' E
		//	     1            Fix quality: 0 = invalid
		//	                               1 = GPS fix (SPS)
		//	                               2 = DGPS fix
		//	                               3 = PPS fix
		//				       4 = Real Time Kinematic
		//				       5 = Float RTK
		//	                               6 = estimated (dead reckoning) (2.3 feature)
		//				       7 = Manual input mode
		//				       8 = Simulation mode
		//	     08           Number of satellites being tracked
		//	     0.9          Horizontal dilution of position
		//	     545.4,M      Altitude, Meters, above mean sea level
		//	     46.9,M       Height of geoid (mean sea level) above WGS84
		//	                      ellipsoid
		//	     (empty field) time in seconds since last DGPS update
		//	     (empty field) DGPS station ID number
		//	     *47          the checksum data, always begins with *
		GpsData fromGpsDataObservation = null;

//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy HHmmss");
//		LocalDateTime localDateTime = LocalDateTime.parse( gpsDate.toString() + " " + gpsTime, formatter);
//		LocalDateTime.of(gpsDate.getYear(), gpsDate.getMonth(), gpsDate.getDayOfMonth(), Integer.parseInt(gpsTime.substring(0, 1)), Integer.parseInt(gpsTime.substring(2,3)), 
//				Integer.parseInt(gpsTime.substring(4,5)));
		
		String gpsTime = inputRecordValues[1];
		
		int hour = Integer.parseInt(inputRecordValues[1].substring(0, 2));
		int minute = Integer.parseInt(inputRecordValues[1].substring(2, 4));
		int second = Integer.parseInt(inputRecordValues[1].substring(4, 6));
	
		if (gpsCurrentHour != hour) {
			//if (gpsCurrentHour == 23) {
				gpsZonedDateTime = gpsZonedDateTime.plusHours(1);
				gpsCurrentHour = gpsZonedDateTime.getHour();
			//}
		}
		LocalDateTime gpsDateTime  = LocalDateTime.of(gpsZonedDateTime.toLocalDate(),LocalTime.of(hour, minute, second)); 
				//LocalTime.of(Integer.parseInt(gpsTime.substring(0, 2)), Integer.parseInt(gpsTime.substring(2,4)),Integer.parseInt(gpsTime.substring(4,6))));

		int latitudeDegrees = Integer.valueOf(inputRecordValues[2].trim().substring(0, inputRecordValues[2].trim().indexOf(".")))/ 100;
		float latitudeMinutes = Float.valueOf(inputRecordValues[2].trim())/ 100;
		float latitude  = (float) (latitudeDegrees + (latitudeMinutes - latitudeDegrees)*100.0/60.0);

		int longitudeDegrees = Integer.valueOf(inputRecordValues[4].trim().substring(0, inputRecordValues[4].trim().indexOf(".")))/ 100;
		float longitudeMinutes = Float.valueOf(inputRecordValues[4].trim())/ 100;
		float longitude  = -(float) (longitudeDegrees + (longitudeMinutes - longitudeDegrees)*100.0/60.0);

		float altitude  = Float.valueOf(inputRecordValues[7].trim());

		GpsData gpsDataObservation = createGPSData(gpsDateTime,latitude,longitude,altitude);
		
		return gpsDataObservation;
	}
	
	ZonedDateTime processGZDARecord(String[] inputRecordValues) {
		// Need to get the gps date from GPZDA record
		
		//	GPZDA 	Date & Time
		//	UTC, day, month, year, and local time zone.
		//	$--ZDA,hhmmss.ss,xx,xx,xxxx,xx,xx
		//	hhmmss.ss = UTC 
		//	xx = Day, 01 to 31 
		//	xx = Month, 01 to 12 
		//	xxxx = Year 
		//	xx = Local zone description, 00 to +/- 13 hours 
		//	xx = Local zone minutes description (same sign as hours)
		
		//String gpsDate = inputRecordValues[2] + "/" + inputRecordValues[3] + "/" + inputRecordValues[4];
		if (inputRecordValues[1].length() < 6) {
			return null;
		}
		int year = Integer.parseInt(inputRecordValues[4]);
		Month month = Month.of(Integer.parseInt(inputRecordValues[3]));
		int dayOfMonth = Integer.parseInt(inputRecordValues[2]);
		int hour = Integer.parseInt(inputRecordValues[1].substring(0, 2));
		int minute = Integer.parseInt(inputRecordValues[1].substring(2, 4));
		int second = Integer.parseInt(inputRecordValues[1].substring(4, 6));
		LocalDateTime gpsDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);

		
		ZonedDateTime	gpsZonedDateTime = ZonedDateTime.of(gpsDateTime, ZoneId.of("UTC"));
		gpsCurrentHour = gpsZonedDateTime.getHour();
		
		gpsSessionName = gpsZonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).
				format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

		return gpsZonedDateTime;
		
//		String gpsTimeString = inputRecordValues[1].trim();
//		String gpsTime = gpsTimeString.substring(0, gpsTimeString.indexOf("."));
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy HHmmss");
//		gpsLocalDate = LocalDateTime.parse( gpsDate + " " + gpsTime, formatter);
//		
//		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss ");
//		String gpsLocalDateTimeString = gpsLocalDate.format(formatter2);
//		String raspberryPiDateString = systemDateTime.format(formatter2);
//		System.out.println("GPS DATE: " + gpsLocalDateTimeString + " " + "PI DATE: " + raspberryPiDateString);   
//
//		formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//		String gpsLocalDateString = gpsLocalDate.format(formatter2);
//		return gpsLocalDateString;

	}
	
	double getMillisecondsFromLocalDate(LocalDateTime date) {
		Calendar cal = Calendar.getInstance();
		cal.set(date.getYear(), date.getMonthValue()-1, date.getDayOfMonth(), 
				date.getHour(), date.getMinute(),date.getSecond());
		return cal.getTimeInMillis();
	}

	GpsData createGPSData(LocalDateTime date, float lat, float lon, float altitude) {
		
		GpsData gpsData = new GpsData();
		
		Calendar cal = Calendar.getInstance();
		cal.set(gpsZonedDateTime.getYear(), gpsZonedDateTime.getMonthValue()-1, gpsZonedDateTime.getDayOfMonth(), 
				date.getHour(), date.getMinute(),date.getSecond());
		gpsData.setDateTime(cal.getTime());

		gpsData.setLatitude(lat);
		gpsData.setLatitudeDirection("N");
		
		gpsData.setLongitude(lon);
		gpsData.setLongitudeDirection("W");

		gpsData.setAltitude(altitude);
		gpsData.setLegHeading((float) 0.0);
		gpsData.setLegSpeed((float) 0.0);
		gpsData.setLegLength((float) 0);
		gpsData.setLegTimeSecs(new Float(0));
		
		return gpsData;
	}
	
	Date convertLocalDateTimeToDate(ZonedDateTime gpsZonedDateTime) {
		Calendar cal = Calendar.getInstance();
		cal.set(gpsZonedDateTime.getYear(), gpsZonedDateTime.getMonthValue()-1, gpsZonedDateTime.getDayOfMonth(), 
				gpsZonedDateTime.getHour(), gpsZonedDateTime.getMinute(),gpsZonedDateTime.getSecond());
		return cal.getTime();

	}
	void processGPGSVRecord(String[] inputRecordValues) {
		//		1    = Total number of messages of this type in this cycle
		//		2    = Message number
		//		3    = Total number of SVs in view
		//		4    = SV PRN number
		//		5    = Elevation in degrees, 90 maximum
		//		6    = Azimuth, degrees from true north, 000 to 359
		//		7    = SNR, 00-99 dB (null when not tracking)
		//		8-11 = Information about second SV, same as field 4-7
		//		12-15= Information about third SV, same as field 4-7
		//		16-19= Information about fourth SV, same as field 4-7
	}
	

}