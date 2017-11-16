package com.racesaucy.read.adapter;

import java.util.Date;

public class LatLonUtilities {
	
	public static void main(String[] args) {
		
		Date date = new Date(1471431516);
		System.out.println("The bearing is: " + date);

		
		
        // name: MCenter 
		double lat1 = 41.962333333;
		double lon1 = -87.606666666;
		
        // name: MEast 
//		double lat2 = 41.962333;
//		double lon2 = -87.589999; 

//        // name: MNorth 
//		double lat2 = 41.974833;
//		double lon2 = -87.606666; 
//
//        // name: MNorthEast 
//		double lat2 = 41.971166;
//		double lon2 = -87.594833; 
//
//        // name: MNorthWest 
		double lat2 = 41.971166;
		double lon2 = -87.618499; 
//
//        // name: MSouth 
//		double lat2 = 41.949833
//		double lon2 = -87.606666; 
//
//        // name: MSouthEast 
//		double lat2 = 41.953499
//		double lon2 = -87.594833; 
//
//        // name: MSouthWest 
//		double lat2 = 41.953499
//		double lon2 = -87.618499; 
//
//        // name: MWest 
//		double lat2 = 41.962333
//		double lon2 = -87.623333; 
		
		double distance = calcDistance( lat1,  lon1,  lat2,  lon2);
				
		System.out.println("The distance is: " + distance);
		
		double expectedDistance = .75 * 1852;
		System.out.println("The expected distance is: " + expectedDistance);
		
		System.out.println("Difference: " + (expectedDistance - distance) + 
				" Percent diff = " + (expectedDistance - distance) / expectedDistance * 100);
		
		double bearing = calcBearing( lat1,  lon1,  lat2,  lon2);
		
		System.out.println("The bearing is: " + bearing);

	}
	
	static double calcDistance(double lat1, double lon1, double lat2, double lon2) {
		
		double R = 6371e3; // meters around the earth ?
		
		// convert lat & lon to radians 
		double theta1 = lat1 / 180 * Math.PI; 
		double theta2 = lat2  / 180  * Math.PI;

		double deltaTheta = (lat2-lat1)  / 180  * Math.PI;
		double deltaLambda = (lon2-lon1)  / 180  * Math.PI;

		double a = Math.sin(deltaTheta/2) * Math.sin(deltaTheta/2) +
		        Math.cos(theta1) * Math.cos(theta2) *
		        Math.sin(deltaLambda/2) * Math.sin(deltaLambda/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

		double d = R * c;
		
		d = round(d, 10.0);

		return d;
	}
	
	static double calcBearing(double lat1, double lon1, double lat2, double lon2) {

		// convert lat & lon to radians 
		double lambda1 = lon1 / 180 * Math.PI; 
		double lambda2 = lon2  / 180  * Math.PI;

		
		double theta1 = lat1 / 180 * Math.PI; 
		double theta2 = lat2  / 180  * Math.PI;
		
		
		double y = Math.sin(lambda2 - lambda1 ) * Math.cos(theta2);
		double x = Math.cos(theta1)*Math.sin(theta2) -
		        Math.sin(theta1)*Math.cos(theta2)*Math.cos(lambda2-lambda1);
		double bearing = Math.atan2(y, x) / Math.PI * 180;  //.toDegrees();
		
		if (bearing < 0) {
			bearing += 360;
		}
		
		bearing = round(bearing, 10.0);
		
		return bearing;
	}

	static double round(double value, double decimalPlaces) {
		return Math.round(value * decimalPlaces) / decimalPlaces;

	}

}
