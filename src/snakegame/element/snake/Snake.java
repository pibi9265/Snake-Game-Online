package snakegame.element.snake;

import java.io.Serializable;
import java.util.ArrayList;

import snakegame.element.snake.Part;

public class Snake implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public ArrayList<Part> body = new ArrayList<Part>();
	public int maxLength = 4;
	public boolean keysPressed = false;
	public Snake(){
		for(int i = 0;i < maxLength;i++) {
			body.add(new Part());
			body.get(i).x = maxLength - i -1;
			body.get(i).y = 0;
		}
	}
	public Snake(int x, int y){
		for(int i = 0;i < maxLength;i++) {
			body.add(new Part());
			body.get(i).x = maxLength - i - 1 + x;
			body.get(i).y = y;
		}
	}
}
