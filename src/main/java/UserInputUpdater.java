import java.util.Map;

public class UserInputUpdater extends Thread {
	private UserInput userInput;
	private String fileLocation;
	
	public UserInputUpdater(UserInput userInput, String fileLocation) {
		this.userInput = userInput;
		this.fileLocation = fileLocation;
	}
	
	public void run() {
		//LOGICA PARA UPDATE + SLEEP
		while(true) {
			try {
				Thread.sleep(300000);
//				Thread.sleep(10000);
				Map<String, String> userInputMap = IOOperation.userInput(fileLocation);
				userInput.setUserInputMap(userInputMap);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
