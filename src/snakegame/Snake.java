package snakegame;

import java.io.Serializable;
import java.util.ArrayList;

import snakegame.Part;

public class Snake implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public ArrayList<Part> body = new ArrayList<Part>();
	public int maxLength = 4;
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
	
	public Snake(int player) {
		int x = 0, y = 0;
		if(player == 0)	{
			x = 10;
			y = 10;
		}
		else if(player == 1) {
			x = 18;
			y = 10;
		}
		
		for(int i = 0; i < maxLength;i++) {
			body.add(new Part());
			body.get(i).x = maxLength - i - 1 + x;
			body.get(i).y = y;
		}
	}
}
