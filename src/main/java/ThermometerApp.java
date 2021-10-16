import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

public class ThermometerApp {
	private UserInput userInput;
	private ReadingAssessmentLogic assessmentLogic;
	private MongoConnector mongoConnector;
	private MongoConnector mongoConnectorLocal;
	private MailSender mailSender;
	
	public ThermometerApp(String user_input_path) {
		Map<String, String> userInputMap = IOOperation.userInput(user_input_path);
		this.userInput = new UserInput(userInputMap);
		this.mongoConnector = new MongoConnector(userInputMap.get("MONGO_URI"), userInputMap.get("MONGO_DATABASE"), userInputMap.get("MONGO_COLLECTION"));
		if(userInputMap.get("LOCAL_MONGO").equals("yes")) {
			this.mongoConnectorLocal = new MongoConnector(userInputMap.get("MONGO_LOCAL_URI"),	userInputMap.get("MONGO_DATABASE"), userInputMap.get("MONGO_COLLECTION"));
		}
		this.mailSender = new MailSender(userInputMap.get("EMAIL_SENDER"), userInputMap.get("EMAIL_SENDER_PASSWORD"), userInputMap.get("EMAIL_RECEIVER"), userInput);
		this.assessmentLogic = new ReadingAssessmentLogic(userInput, mailSender, mongoConnector, mongoConnectorLocal);
	}
	
	private void userInputUpdater(String fileLocation) {
		UserInputUpdater updater = new UserInputUpdater(userInput, fileLocation);
		updater.start();
	}
	
	private void readingLoop() {
		String scriptPath = userInput.getUserInputMap().get("SCRIPT_PATH");
		while(true) {
			try {
				BLEReading reading = IOOperation.getReading(scriptPath, userInput.getUserInputMap().get("THERMOMETER_ADDR"));


				assessmentLogic.assess(reading);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
	}
	
	
	public static void main(String[] args) {
		
		String user_input_path = args[0];
//		String user_input_path = "C:\\Users\\AntónioRodrigo\\IdeaProjects\\ThermometerApp\\src\\main\\resources\\user_input.config";

		ThermometerApp m = new ThermometerApp(user_input_path);
		m.userInputUpdater(user_input_path);
		m.readingLoop();
	}
	
}
