package com.racesaucy.read.adapter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import com.racesaucy.read.domain.GpsData;
import com.racesaucy.read.domain.SessionPersist;

public class TcpAdapter extends ReadNME0183Data {

	public static void main(String[] args) {
		
		TcpAdapter tcpAdapter = new TcpAdapter();
		
		tcpAdapter.processTcpSocket();
		
		//tcpAdapter.saveGPSSessionObservations(tcpAdapter.gpsSessionName,tcpAdapter.gpsObservationList); 
	}
	
	private void processTcpSocket() {
		
		ReadGarminGPXFile readGarminGPXFile = new ReadGarminGPXFile();

		
		Socket socket = null;  
		DataOutputStream os = null;
		DataInputStream is = null;

		try {
            //socket = new Socket("192.168.1.105", 10110);
            //socket = new Socket("192.168.42.3", 10110);
            //socket = new Socket("67.175.139.230", 10110);
            socket = new Socket("127.0.0.1", 10110);
            os = new DataOutputStream(socket.getOutputStream());
            is = new DataInputStream(socket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: hostname");
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: hostname" + e);
        }
		SessionPersist sessionPersist = null; 
		if (socket != null && os != null && is != null) {
	        try {
	            String responseLine;
	            while ((responseLine = is.readLine()) != null) {
	                System.out.println("Server: " + responseLine);
	                
	    			String[] nme0183RecordValues = responseLine.trim().split(",");
	    			String recordType = nme0183RecordValues[0].substring(1);
	    			
	    			processNmea0183Record(recordType, nme0183RecordValues);

	    			if (gpsObservationList.size() >= 2) {
		                System.out.println("Client: gpsObservationList.size() >= 5");

		                sessionPersist = readGarminGPXFile.saveGPSSessionObservationsList(sessionPersist,gpsSessionName, gpsObservationList);
	    				gpsZonedDateTime = null;
	    				gpsObservationList = new ArrayList<GpsData>();
	    			}
	                
	                if (responseLine.indexOf("Ok") != -1) {
	                  break;
	                }
	            }

	            os.close();
	            is.close();
	            socket.close();   
            } catch (UnknownHostException e) {
                System.err.println("Trying to connect to unknown host: " + e);
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }           
}
