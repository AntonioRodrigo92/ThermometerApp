import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ReadingAssessmentLogic {
	private final int THRESHOLD_TIME = 300000;
	private Timestamp threshold;		//tempo para fazer a media
	private int counter;
	private float totalTemp;
	private float totalHum;
	private MongoConnector mongoConnector;


	public ReadingAssessmentLogic(MongoConnector mongoConnector) {
		setTimeThreshold(Timestamp.from(Instant.now()));
		this.counter = 0;
		this.mongoConnector = mongoConnector;
	}


	public void readingLogic(BLEReading reading, boolean extraHour) {

		try {
			if (reading.getTimestamp().before(threshold)) {
				counter++;
				totalTemp += reading.getTemp();
				totalHum += reading.getHum();
			}

			else {
				float meanTemp = totalTemp / counter;
				int meanHum = (int) (totalHum / counter);
				BLEReading meanReading = new BLEReading(true, meanTemp, meanHum, threshold);
				mongoConnector.insertReading(meanReading, extraHour);

				setTimeThreshold(threshold);
				counter = 0;
				totalTemp = 0;
				totalHum = 0;
			}
		}
		catch (Exception e) {
			System.out.println("### " + Utils.getCurrentDateTime() + "Generic exception in reading logic");
			e.printStackTrace();
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
