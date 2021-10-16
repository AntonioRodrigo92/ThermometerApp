import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class IOOperation {
	
	public static BLEReading getReading(String scriptPath, String bleAddr) throws IOException {
		Runtime rt = Runtime.getRuntime();
		String[] commands = {scriptPath, bleAddr};
		Process proc = rt.exec(commands);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));	
		
		String valid = stdInput.readLine();
		
		if (valid.equals("Success")) {
			//sucesso
			try {
				float temp = Float.parseFloat(stdInput.readLine());
				int hum = Integer.parseInt(stdInput.readLine());
				Timestamp timestamp = Timestamp.from(Instant.now());
				if (temp > 100 || hum > 100 || hum < 0) {
					BLEReading reading = new BLEReading(false, 0.0f, 0, null);
					return reading;
				}
				BLEReading reading = new BLEReading(true, temp, hum, timestamp);
				return reading;
			}
			catch (NumberFormatException e) {
				BLEReading reading = new BLEReading(false, 0.0f, 0, null);
				return reading;
			}
		}
		else {
			//leitura errada
			BLEReading reading = new BLEReading(false, 0.0f, 0, null);
			return reading;
		}	
	}
	
	public static Map<String, String> userInput(String fileLocation) {
		Map<String, String> map = new HashMap<>();
		File f = new File(fileLocation);
	    
		try {
	    	Scanner sc = new Scanner(f);
	    	
	    	while (sc.hasNextLine()) {
	    		String line = sc.nextLine();
	    		if(! line.equals("")) {
						populateObject(line, map);
					}
	    	}
	    	sc.close();
	    } 
	    catch (FileNotFoundException e) {
	    	e.printStackTrace();
	    }
		
		return map;
	}
	
    private static void populateObject (String line, Map<String, String> map) {
        String[] part = line.split(": ");
        String key = part[0];
        String value = part[1];
        map.put(key, value);
    }
	
}
