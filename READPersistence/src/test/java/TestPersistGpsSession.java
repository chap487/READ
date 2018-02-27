import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.racesaucy.read.adapter.ReadGarminGPXFile;
import com.racesaucy.read.domain.GpsData;
import com.racesaucy.read.domain.SessionPersist;

public class TestPersistGpsSession {

	public static void main(String[] args) {
			
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
	    }
	    
	    ReadGarminGPXFile readGarminGPXFile = new ReadGarminGPXFile();

	    readGarminGPXFile.saveGPSSessionObservations("New Test Session", new ArrayList<GpsData>(sessionPersistFromFile.getGpsDataList()));

	}

}
