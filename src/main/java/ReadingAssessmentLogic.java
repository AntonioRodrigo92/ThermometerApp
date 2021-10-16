import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ReadingAssessmentLogic {
	private final int THRESHOLD_TIME = 300000;
	private UserInput userInput;
	private int invalidReadings;
	private int outOfBoundsReadingsTemp;
	private int outOfBoundsReadingsHum;
	private Timestamp threshold;		//tempo para fazer a media
	private int counter;
	private float totalTemp;
	private float totalHum;
	private MailSender mailSender;
	private MongoConnector mongoConnector;
	private MongoConnector mongoConnectorLocal;


	public ReadingAssessmentLogic(UserInput userInput, MailSender mailSender, MongoConnector mongoConnector, MongoConnector mongoConnectorLocal) {
		this.userInput = userInput;
		this.invalidReadings = 0;
		setTimeThreshold(Timestamp.from(Instant.now()));
		this.counter = 0;
		this.totalTemp = 0;
		this.totalHum = 0;
		this.outOfBoundsReadingsTemp = 0;
		this.outOfBoundsReadingsHum = 0;
		this.mailSender = mailSender;
		this.mongoConnector = mongoConnector;
		this.mongoConnectorLocal = mongoConnectorLocal;
	}


	public void assess(BLEReading reading) {
		if(! reading.isValid()) {
			invalidReadings++;
			if(invalidReadings == Integer.parseInt(userInput.getUserInputMap().get("MAX_INVALID_READINGS"))) {
				mailSender.sendMail("Warning: invalid readings", "The number of invalid readings has exceeded its limit. Please, check the thermometer!");
			}
		}
		else {
			invalidReadings = 0;
			readingLogic(reading);
		}
	}


	private void readingLogic (BLEReading reading) {

		if (reading.getTimestamp().before(threshold)) {
			counter++;
			totalTemp += reading.getTemp();
			totalHum += reading.getHum();
		}

		else {
			float meanTemp = totalTemp / counter;
			int meanHum = (int) (totalHum / counter);
			BLEReading meanReading = new BLEReading(true, meanTemp, meanHum, threshold);
			mongoConnector.insertReading(meanReading);
			if (mongoConnectorLocal != null) {
				mongoConnectorLocal.insertReading(meanReading);
			}
			humidityEval(meanHum, Integer.parseInt(userInput.getUserInputMap().get("HUMIDITY_MIN")), Integer.parseInt(userInput.getUserInputMap().get("HUMIDITY_MAX")));
			temperatureEval(meanTemp, Float.parseFloat(userInput.getUserInputMap().get("TEMPERATURE_MIN")), Float.parseFloat(userInput.getUserInputMap().get("TEMPERATURE_MAX")));
			setTimeThreshold(threshold);
			counter = 0;
			totalTemp = 0;
			totalHum = 0;
		}
	}


	private void humidityEval(int r, int min, int max) {
		if(r <= max && r >= min) {
			outOfBoundsReadingsHum = 0;
		}
		else {
			outOfBoundsReadingsHum++;
			if (outOfBoundsReadingsHum == Integer.parseInt(userInput.getUserInputMap().get("MAX_OUT_OF_BOUNDS_READINGS"))) {
				mailSender.sendMail("Warning: Humidity", "Warning, the humidity levels are out of bounds");
			}
		}
	}
	
	private void temperatureEval(float r, float min, float max) {
		if(r <= max && r >= min) {
			outOfBoundsReadingsTemp = 0;
		}
		else {
			outOfBoundsReadingsTemp++;
			if (outOfBoundsReadingsTemp == Integer.parseInt(userInput.getUserInputMap().get("MAX_OUT_OF_BOUNDS_READINGS"))) {
				mailSender.sendMail("Warning: Temperature", "Warning, the temperature levels are out of bounds");
			}
		}
	}

	private void setTimeThreshold (Timestamp current) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(current.getTime());
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		Timestamp ts = new Timestamp(cal.getTimeInMillis());
		ts.setTime(ts.getTime() + THRESHOLD_TIME);

		threshold = ts;
	}
	
}
