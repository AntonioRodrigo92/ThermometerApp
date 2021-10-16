import java.util.Map;

public class UserInput {
	private Map<String, String> userInputMap;
	
	public UserInput(Map<String, String> userInputMap) {
		this.userInputMap = userInputMap;
	}
	
	public void setUserInputMap(Map<String, String> userInputMap) {
		this.userInputMap = userInputMap;
	}
	
	public Map<String, String> getUserInputMap() {
		return userInputMap;
	}

}
