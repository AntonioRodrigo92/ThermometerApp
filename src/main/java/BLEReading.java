import java.sql.Timestamp;

public class BLEReading {
	private boolean isValid;
	private float temp;
	private int hum;
	private Timestamp timestamp;
	
	public BLEReading(boolean isValid, float temp, int hum, Timestamp timestamp) {
		this.isValid = isValid;
		this.temp = temp;
		this.hum = hum;
		this.timestamp = timestamp;
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public float getTemp() {
		return temp;
	}
	
	public int getHum() {
		return hum;		
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

}
