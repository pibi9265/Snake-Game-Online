import java.io.Serializable;
import java.util.ArrayList;

public class InputInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	ArrayList<Snake> inputSnake = new ArrayList<Snake>();
	Apple inputApple = null;
	
	InputInfo()
	{
		inputSnake = new ArrayList<Snake>();
		inputApple = null;
	}
	
	InputInfo(InputInfo input) throws CloneNotSupportedException
	{
		inputSnake = new ArrayList<Snake>();
		inputSnake.addAll(input.inputSnake);
		inputApple = input.inputApple.clone();
	}
}
