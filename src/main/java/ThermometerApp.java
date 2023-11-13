import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

public class ThermometerApp {
	private UserInput userInput;
	private ReadingAssessmentLogic assessmentLogic;
	private MongoConnector mongoConnector;
	
	public ThermometerApp(String user_input_path) throws FileNotFoundException {
		System.out.println("### " + Utils.getCurrentDateTime() + "Getting user input");
		Map<String, String> userInputMap = IOOperation.userInput(user_input_path);
		this.userInput = new UserInput(userInputMap);
		System.out.println("### " + Utils.getCurrentDateTime() + "Getting mongo connector");
		this.mongoConnector = new MongoConnector(userInputMap.get("MONGO_URI"), userInputMap.get("MONGO_DATABASE"), userInputMap.get("MONGO_COLLECTION"));
		System.out.println("### " + Utils.getCurrentDateTime() + "Creating ReadingAssessmentLogic");
		this.assessmentLogic = new ReadingAssessmentLogic(mongoConnector);
	}
	
	private void readingLoop() {
		System.out.println("### " + Utils.getCurrentDateTime() + "Starting reading loop");
		String scriptPath = userInput.getUserInputMap().get("SCRIPT_PATH");
		String restartPath = userInput.getUserInputMap().get("RESTART_BT_PATH");
		String thermometerAddress = userInput.getUserInputMap().get("THERMOMETER_ADDR");
		String extraHour = userInput.getUserInputMap().get("EXTRA_HOUR");
		while(true) {
			try {
				System.out.println("### " + Utils.getCurrentDateTime() + "getting BT reading");
				BLEReading reading = IOOperation.getReading(scriptPath, thermometerAddress);
				assessmentLogic.readingLogic(reading, Boolean.parseBoolean(extraHour), restartPath);
			} catch (IOException e) {
				System.err.println("### " + Utils.getCurrentDateTime() + "IOException in readingLoop");
				e.printStackTrace();
			}
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				System.err.println("### " + Utils.getCurrentDateTime() + "InterruptedException in readingLoop");
				e.printStackTrace();
			}
		}
	
	}
	
	
	public static void main(String[] args) {
		System.out.println("### " + Utils.getCurrentDateTime() + "Starting ThermometerApp");

		String user_input_path = args[0];
//		String user_input_path = "C:\\Users\\AntónioRodrigo\\IdeaProjects\\ThermometerApp\\src\\main\\resources\\user_input.config";

		ThermometerApp m = null;
		try {
			m = new ThermometerApp(user_input_path);
		} catch (FileNotFoundException e) {
			System.err.println("### " + Utils.getCurrentDateTime() + "Error during ThermometerApp creation");
			e.printStackTrace();
		}
		m.readingLoop();
	}
	
}
